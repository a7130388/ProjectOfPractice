WITH CHG_TB AS(
 SELECT * FROM (
  SELECT ROW_NUMBER ()
          OVER (PARTITION BY QPA011.PB_SER_NO ORDER BY QPA011.UPD_DT DESC)
          AS ROWNUM,
       QPA011.FUNC_ID,
       QPA011.PB_SER_NO
  FROM DBQP.DTQPA011 QPA011
 WHERE QPA011.PB_SER_NO IN (SELECT PB_SER_NO
                              FROM DBQP.DTQPA010 QPA010
                             WHERE QPA010.PB_STS_CD IN ('22', '31', '91') AND QPA010.PRC_DVNO = ':DIV_NO')
       AND QPA011.CHG_STS_CD IN ('21', '22')
       AND Date (QPA011.UPD_DT) BETWEEN ':START_DATE' AND ':END_DATE'
 ) WHERE ROWNUM >= 2
)
, EMP AS(
  SELECT EMPLOYEE_ID AS EMP_ID, NAME AS EMP_NM
  FROM CXLHR.DTA0_EMPLOYEE_WORK
  WHERE DIV_NO = ':DIV_NO'
    [AND EMPLOYEE_ID = ':EMP_ID']
),
 GRP  AS (SELECT EMP_ID, EMP_NM
   FROM DBXX.DTXXQK04
   WHERE DIV_NO = ':DIV_NO' [AND GRP_ID = ':GRP_ID'] [AND EMP_ID = ':EMP_ID']
)
SELECT  COALESCE (EMP.EMP_ID,GRP.EMP_ID) AS EMP_ID,COALESCE (EMP.EMP_NM,GRP.EMP_NM) AS EMP_NM, CAST (SUM (DIFF) / COUNT (DIFF) AS DECIMAL (5, 2)) AS PB_DAYS
  FROM (SELECT QPA011.PB_SER_NO,
               QPA011.UPD_ID,
               CAST (
                  DEC (
                     (days (MAX (QPA011.UPD_DT)) - days (MIN (QPA011.UPD_DT)))
                     * 1440
                     + (hour (MAX (QPA011.UPD_DT))
                        - hour (MIN (QPA011.UPD_DT)))
                       * 60
                     + (minute (MAX (QPA011.UPD_DT))
                        - minute (MIN (QPA011.UPD_DT))))
                  / 1440 AS DECIMAL (5, 2))
                  AS DIFF
          FROM DBQP.DTQPA011 QPA011
               LEFT JOIN CHG_TB
                  ON QPA011.PB_SER_NO = CHG_TB.PB_SER_NO
                     AND QPA011.FUNC_ID = CHG_TB.FUNC_ID
               LEFT JOIN DBQP.DTQPA010 QPA010
                  ON QPA010.PB_SER_NO = QPA011.PB_SER_NO
                     AND QPA010.FUNC_ID = QPA011.FUNC_ID
         WHERE     CHG_TB.PB_SER_NO IS NOT NULL
               AND QPA011.CHG_STS_CD IN ('21', '22')
            AND QPA011.UPD_DIV_NO LIKE ':DIV_NO'
           [AND QPA011.UPD_ID LIKE ':EMP_ID']
        GROUP BY QPA011.PB_SER_NO, QPA011.UPD_ID, QPA011.UPD_NAME
        HAVING CAST (
                  DEC (
                     (days (MAX (QPA011.UPD_DT)) - days (MIN (QPA011.UPD_DT)))
                     * 1440
                     + (hour (MAX (QPA011.UPD_DT))
                        - hour (MIN (QPA011.UPD_DT)))
                       * 60
                     + (minute (MAX (QPA011.UPD_DT))
                        - minute (MIN (QPA011.UPD_DT))))
                  / 1440 AS DECIMAL (5, 2)) > 0)
  RIGHT JOIN EMP
  ON EMP.EMP_ID = UPD_ID
  FULL OUTER JOIN GRP
  ON UPD_ID = GRP.EMP_ID
   WHERE CASE WHEN 1 = ':KIND' THEN EMP.EMP_ID ELSE GRP.EMP_ID END
                   IS NOT NULL
  GROUP BY GRP.EMP_ID,GRP.EMP_NM,EMP.EMP_ID,EMP.EMP_NM
WITH UR
