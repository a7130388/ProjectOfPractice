SELECT EMP_ID,
       EMP_NM as "title",
       EXC_DATE as "start",
       EXC_HOURS,
       MEMO
  FROM DBXX.DTXXQK05
 WHERE DIV_NO = ':DIV_NO'
       [ AND EMP_ID = ':EMP_ID' ]
  WITH UR
