package gtu.db.jdbc;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.Struct;
import java.sql.Types;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ArrayDescriptor;
import oracle.sql.StructDescriptor;

public class CallableStatement_ArrayType_TableTypeWithObject {
    /*
    create or replace
    TYPE org_hook_object AS OBJECT (
      param_name  varchar2(1000),
      param_val   varchar2(1000)
    );
     
    create or replace
    TYPE org_hook_list IS TABLE OF org_hook_object;
    
    create or replace
    PROCEDURE test_table_type_with_object (inParam IN  org_hook_list,outParam OUT org_hook_list)
    IS
        i NUMBER;
        name1 VARCHAR2(1000);
        val1 VARCHAR2(10000);
    BEGIN
        outParam := org_hook_list();
        outParam.extend(2);
        FOR i IN inParam.FIRST .. inParam.LAST
        LOOP
        name1 := inParam(i).param_name || ' Out Param Name '|| i;
        val1 := inParam(i).param_val || ' Out Param Val '|| i;
        insert into test123 (name1,value1) values (name1,val1);
        outParam(i) := org_hook_object(name1,val1);
        END LOOP; 
    END test_table_type_with_object;
    */

    public static void main(String[] args) {
        new CallableStatement_ArrayType_TableTypeWithObject().execute();
    }

    public void execute() {

        Connection connection = null;
        oracle.sql.ARRAY array = null;
        ArrayDescriptor arrayDescriptor = null;
        OracleCallableStatement cStmt = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@msi-vmpl3db2:1521:pl11gdb", "pranaka", "pranaka");

            arrayDescriptor = ArrayDescriptor.createDescriptor("ORG_HOOK_LIST", connection);

            InputBean input[] = new InputBean[2];

            input[0] = new InputBean();
            input[0].setParamName("moving_node");
            input[0].setParamVal("100000");

            input[1] = new InputBean();
            input[1].setParamName("parent_node");
            input[1].setParamVal("200000");

            Object[] objDetailList = null;
            objDetailList = new Object[input.length];
            for (int i = 0; i < objDetailList.length; i++) {
                getInputArray(input[i]);
            }
            array = new oracle.sql.ARRAY(arrayDescriptor, connection, objDetailList);

            final String typeName = "ORG_HOOK_OBJECT";
            final String typeTableName = "ORG_HOOK_LIST";
            final StructDescriptor structDescriptor = StructDescriptor.createDescriptor(typeName.toUpperCase(), connection);
            final ResultSetMetaData metaData = structDescriptor.getMetaData();

            cStmt = (OracleCallableStatement) connection.prepareCall("{call test_table_type_with_object(?,?)}");
            cStmt.setArray(1, array);
            cStmt.registerOutParameter(2, Types.ARRAY, typeTableName);
            cStmt.execute();

            Object[] data = (Object[]) ((Array) cStmt.getObject(2)).getArray();
            for (Object tmp : data) {
                Struct row = (Struct) tmp;
                // Attributes are index 1 based...
                int idx = 1;
                for (Object attribute : row.getAttributes()) {
                    System.out.println(metaData.getColumnName(idx) + " = " + attribute);
                    ++idx;
                }
                System.out.println("---");
            }

            cStmt.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public class InputBean {
        private String param_name;
        private String param_val;

        public InputBean(String param_name, String param_val) {
            this.param_name = param_name;
            this.param_val = param_val;
        }

        public InputBean() {
        }

        /**
         * @return the canal
         */
        public String getParamVal() {
            return this.param_val;
        }

        /**
         * @param canal
         *            the canal to set
         */
        public void setParamVal(String paramVal) {
            this.param_val = paramVal;
        }

        /**
         * @return the mapKey
         */
        public String getParamName() {
            return this.param_name;
        }

        /**
         * @param mapKey
         *            the mapKey to set
         */
        public void setParamName(String paramName) {
            this.param_name = paramName;
        }

    }

    public static Object[] getInputArray(InputBean it) {
        Object[] objArr = new Object[2];
        objArr[0] = it.getParamName();
        objArr[1] = it.getParamVal();
        return objArr;
    }
}