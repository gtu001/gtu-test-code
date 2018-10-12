UPDATE DBXX.DTXXTP01
   SET (DIV_NO,
        EMP_NAME,
        POSITION,
        UPDT_ID,
        UPDT_DATE,
        BIRTHDAY) =
          (':DIV_NO',
           ':EMP_NAME',
           ':POSITION',
           ':UPDT_ID',
           ':UPDT_DATE',
           ':BIRTHDAY')
 WHERE EMP_ID = ':EMP_ID'
  WITH UR