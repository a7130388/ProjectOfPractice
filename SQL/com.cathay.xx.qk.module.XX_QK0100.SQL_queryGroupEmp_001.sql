SELECT EMP_ID, EMP_NM
  FROM DBXX.DTXXQK04
 WHERE DIV_NO = ':DIV_NO' AND GRP_ID = ':GRP_ID' [ AND EMP_ID = ':EMP_ID' ]
  WITH UR