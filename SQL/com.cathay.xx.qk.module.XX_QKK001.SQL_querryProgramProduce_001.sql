WITH T1
     AS (SELECT D030.PG_NM,
                D030.PG_ID,
                COALESCE (F010.DEV_STD_DAYS, 0)
                + COALESCE (F010.TST_STD_DAYS, 0)
                   AS ALL_DAYS
           FROM    DBQP.DTQPD030 D030
                LEFT JOIN
                   DBQP.DTQPF010 F010
                ON D030.PROJ_ID = F010.PROJ_ID
                   AND D030.FUNC_ID = F010.FUNC_ID
          WHERE D030.PGM_DT BETWEEN ':START_DATE' AND ':END_DATE'
                AND D030.SD_DVNO = ':DIV_NO'),
     TMP_A011
     AS (SELECT A011.UPD_NAME,
                A011.UPD_ID,
                A011.FUNC_ID,
                A011.PB_SER_NO,
                MIN (A011.UPD_DT) AS MIN_UPD_DT
           FROM DBQP.DTQPA011 A011
          WHERE     DATE (A011.UPD_DT) BETWEEN ':START_DATE' AND ':END_DATE'
                AND A011.PRC_DVNO = ':DIV_NO'
                AND A011.CHG_STS_CD = '22'
         GROUP BY A011.FUNC_ID,
                  A011.PB_SER_NO,
                  A011.UPD_ID,
                  A011.UPD_NAME),
     T2
     AS (SELECT TMP_A011.UPD_ID,
                TMP_A011.UPD_NAME,
                COALESCE (F010.DEV_STD_DAYS, 0)
                + COALESCE (F010.TST_STD_DAYS, 0)
                   AS ERROR_DAYS
           FROM TMP_A011
                JOIN DBQP.DTQPA011 A011
                   ON     A011.FUNC_ID = TMP_A011.FUNC_ID
                      AND A011.PB_SER_NO = TMP_A011.PB_SER_NO
                      AND A011.UPD_DT = TMP_A011.MIN_UPD_DT
                LEFT JOIN DBQP.DTQPF010 F010
                   ON A011.PROJ_ID = F010.PROJ_ID
                      AND A011.PB_SER_NO = F010.FUNC_ID
          WHERE     DATE (A011.UPD_DT) BETWEEN ':START_DATE' AND ':END_DATE'
                AND A011.PRC_DVNO = ':DIV_NO'
                AND A011.CHG_STS_CD = '22'),
     ALL_DAY
     AS (SELECT T1.PG_ID,
                T1.PG_NM,
                COUNT (T1.PG_ID) AS PROC_NUM,
                SUM (T1.ALL_DAYS) AS ALL_DAY
           FROM T1
         GROUP BY T1.PG_ID, T1.PG_NM),
     ERROR_DAY
     AS (SELECT T2.UPD_ID, T2.UPD_NAME, SUM (T2.ERROR_DAYS) AS ERROR_DAY
           FROM T2
         GROUP BY T2.UPD_ID, T2.UPD_NAME),
     EMP
     AS (SELECT EMPLOYEE_ID AS EMP_ID, NAME AS EMP_NM
           FROM CXLHR.DTA0_EMPLOYEE_WORK
          WHERE DIV_NO = ':DIV_NO' [ AND EMPLOYEE_ID = ':EMP_ID' ]
        ),
     GRP
     AS (SELECT EMP_ID, EMP_NM
           FROM DBXX.DTXXQK04
          WHERE DIV_NO = ':DIV_NO' [ AND GRP_ID = ':GRP_ID' ] [ AND EMP_ID = ':EMP_ID' ]
        ),
     FINAL
     AS (SELECT COALESCE (EMP.EMP_ID,
                          GRP.EMP_ID,
                          ALL_DAY.PG_ID,
                          ERROR_DAY.UPD_ID)
                   AS EMP_ID,
                COALESCE (EMP.EMP_NM, ALL_DAY.PG_NM, ERROR_DAY.UPD_NAME)
                   AS EMP_NM,
                CASE
                   WHEN ALL_DAY.ALL_DAY IS NULL
                   THEN
                      CASE
                         WHEN ERROR_DAY.ERROR_DAY IS NULL THEN NULL
                         ELSE 0
                      END
                   ELSE
                      ALL_DAY.ALL_DAY
                END
                   AS DEV_DAY,
                CASE
                   WHEN ERROR_DAY.ERROR_DAY IS NULL
                   THEN
                      CASE WHEN ALL_DAY.ALL_DAY IS NULL THEN NULL ELSE 0 END
                   ELSE
                      ERROR_DAY.ERROR_DAY
                END
                   AS FIX_DAY
           FROM ALL_DAY
                FULL OUTER JOIN ERROR_DAY
                   ON ALL_DAY.PG_ID = ERROR_DAY.UPD_ID                
                RIGHT JOIN EMP
                   ON EMP.EMP_ID = ALL_DAY.PG_ID
				FULL OUTER JOIN GRP
                   ON ALL_DAY.PG_ID = GRP.EMP_ID
          WHERE CASE WHEN 1 = ':KIND' THEN EMP.EMP_ID ELSE GRP.EMP_ID END
                   IS NOT NULL)
SELECT FINAL.EMP_ID,
       FINAL.DEV_DAY,
       FINAL.FIX_DAY,
       FINAL.DEV_DAY + FINAL.FIX_DAY AS TOTAL_DAY
  FROM FINAL
  WITH UR
