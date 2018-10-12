INSERT INTO DBXX.DTXXTP01 (EMP_ID,
                           DIV_NO,
                           EMP_NAME,
                           BIRTHDAY,
                           POSITION,
                           UPDT_ID,
                           UPDT_DATE,
                           OP_STATUS,
                           FLOW_NO)
VALUES (':EMP_ID',
        ':DIV_NO',
        ':EMP_NAME',
        ':BIRTHDAY',
        ':POSITION',
        ':UPDT_ID',
        ':UPDT_DATE',
        ':OP_STATUS',
        ':FLOW_NO')
  WITH UR