WITH tb1
     AS (SELECT WRK_HRS,
                MEMO,
                EMP_ID,
                EMP_DIV,
                BGN_DT,
                END_DT,
                TSK_STP
           FROM DBQB.DTQBA110
          WHERE     TSK_CODE = 'ZZ01'
                AND EMP_DIV LIKE '920%'
                AND UPD_ID = 'FMB0_B005'
                AND END_DT >= ':START_DATE'
                AND END_DT <= ':END_DATE'
                AND TSK_STP = '1'
                AND EMP_DIV = ':DIV_NO'),
     tb2
     AS (SELECT WRK_HRS,
                MEMO,
                EMP_ID,
                EMP_DIV,
                BGN_DT,
                END_DT,
                TSK_STP
           FROM DBQB.DTQBA110
          WHERE     TSK_CODE = 'ZZ01'
                AND (EMP_DIV LIKE '930%' OR EMP_DIV LIKE '940%')
                AND TSK_STS = 'C'
                AND CHK_STS = 'A'
                AND END_DT >= ':START_DATE'
                AND END_DT <= ':END_DATE'
                AND TSK_STP = '1'
                AND EMP_DIV = ':DIV_NO'),
     tb3
     AS (SELECT * FROM tb1
         UNION
         SELECT * FROM tb2)
SELECT EMP_ID, SUM (CASE WHEN WRK_HRS = 8 THEN 1 ELSE 0.5 END) AS Total_DAYS
  FROM tb3
GROUP BY EMP_ID
  WITH UR