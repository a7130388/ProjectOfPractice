WITH T1
     AS (SELECT D030.PG_NM, D030.PG_ID
           FROM    DBQP.DTQPD030 D030
                LEFT JOIN
                   DBQP.DTQPF010 F010
                ON D030.PROJ_ID = F010.PROJ_ID
                   AND D030.FUNC_ID = F010.FUNC_ID
          WHERE D030.PGM_DT BETWEEN ':START_DATE' AND ':END_DATE'
                AND D030.SD_DVNO = ':DIV_NO'),
     T2
     AS (SELECT D030.PG_NM, D030.PG_ID
           FROM    DBQP.DTQPD030 D030
                LEFT JOIN
                   DBQP.DTQPF010 F010
                ON D030.PROJ_ID = F010.PROJ_ID
                   AND D030.FUNC_ID = F010.FUNC_ID
          WHERE     D030.PGM_DT BETWEEN ':START_DATE' AND ':END_DATE'
                AND D030.PGM_DT > D030.PGM_IF_DT
                AND D030.SD_DVNO = ':DIV_NO'
                AND D030.PROC_CD >= '080'
                AND D030.PROC_CD <> '900'),
     DUE
     AS (SELECT T2.PG_ID, COUNT (T2.PG_ID) AS DUE_PROC
           FROM T2
         GROUP BY T2.PG_ID),
     PROCNUM
     AS (SELECT T1.PG_ID, COUNT (T1.PG_ID) AS PROC_NUM
           FROM T1
         GROUP BY T1.PG_ID),
     EMP
     AS (SELECT EMPLOYEE_ID AS EMP_ID, NAME AS EMP_NM
           FROM CXLHR.DTA0_EMPLOYEE_WORK
          WHERE DIV_NO = ':DIV_NO' [AND EMPLOYEE_ID = ':EMP_ID']), 
     GRP
     AS (SELECT EMP_ID
           FROM DBXX.DTXXQK04
          WHERE 1 = 1  [AND GRP_ID = ':GRP_ID'] [AND EMP_ID = ':EMP_ID']),
     FINAL
     AS (SELECT COALESCE (EMP.EMP_ID, GRP.EMP_ID) AS EMP_ID,
                EMP_NM,
                PROCNUM.PROC_NUM AS PROC_NUM,
                CASE
                   WHEN DUE.DUE_PROC IS NULL
                   THEN
                      CASE WHEN PROCNUM.PROC_NUM IS NULL THEN NULL ELSE 0 END
                   ELSE
                      DUE.DUE_PROC
                END
                   AS DUE_PROC,
                cast (
                   DEC (DUE.DUE_PROC / PROCNUM.PROC_NUM) AS DECIMAL (3, 2))
                   AS DUE_NUM
           FROM PROCNUM
                FULL OUTER JOIN DUE
                   ON PROCNUM.PG_ID = DUE.PG_ID
                RIGHT JOIN EMP
                   ON EMP.EMP_ID = PROCNUM.PG_ID
                FULL OUTER JOIN GRP
                   ON PROCNUM.PG_ID = GRP.EMP_ID
          WHERE CASE WHEN 1 = ':KIND' THEN EMP.EMP_ID ELSE GRP.EMP_ID END
                   IS NOT NULL)
SELECT FINAL.EMP_ID,
       FINAL.EMP_NM,
       FINAL.PROC_NUM,
       FINAL.DUE_PROC,
       cast (DEC (FINAL.DUE_PROC / FINAL.PROC_NUM) AS DECIMAL (3, 2))
          AS DUE_NUM
  FROM FINAL
  WITH UR
