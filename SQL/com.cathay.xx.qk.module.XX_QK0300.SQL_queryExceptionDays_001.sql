SELECT EMP_ID, SUM (EXC_HOURS) / 8
  FROM DBXX.DTXXQK05
 WHERE     EXC_DATE BETWEEN ':START_DATE' AND ':END_DATE'
       AND DIV_NO = ':DIV_NO'
      [ AND EMP_ID = ':EMP_ID' ]
GROUP BY EMP_ID
  WITH UR
