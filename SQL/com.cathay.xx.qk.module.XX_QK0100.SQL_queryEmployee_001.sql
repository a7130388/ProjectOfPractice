SELECT EMPLOYEE_ID AS EMP_ID, NAME AS EMP_NM
  FROM CXLHR.DTA0_EMPLOYEE_WORK
 WHERE DIV_NO = ':DIV_NO' [ AND EMPLOYEE_ID = ':EMP_ID' ]
  WITH UR