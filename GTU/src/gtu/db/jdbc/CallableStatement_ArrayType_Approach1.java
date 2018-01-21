package gtu.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;

public class CallableStatement_ArrayType_Approach1 {
    /*
    create or replace
    type pre_hook_out_paramList is table of varchar2(1000);
    
    create or replace
    PROCEDURE test_table_type_only_out (
                    inParam IN VARCHAR2
                    ,outParam OUT pre_hook_out_paramList
                    )
    IS
        i NUMBER;
    BEGIN
        outParam := pre_hook_out_paramList();
        outParam.extend(2);
        outParam(1) := inParam;
        outParam(2) := inParam || ' Second Element';
    END test_table_type_only_out;
     */

    public static void main(String[] args) {

        Connection connection = null;
        String query = "call test_table_type_only_out(?,?)";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@msi-vmpl3db2:1521:pl11gdb", "pranaka", "pranaka");

            OracleCallableStatement statement = null;
            statement = (OracleCallableStatement) connection.prepareCall("{call test_table_type_only_out(?,?)}");
            statement.setString(1, "Manju");
            statement.registerOutParameter(2, OracleTypes.ARRAY, "pre_hook_out_paramList");
            statement.execute();

            ARRAY simpleArray = statement.getARRAY(1);

            System.out.println("Array is of type " + simpleArray.getSQLTypeName());

            System.out.println("Array element is of type code " + simpleArray.getBaseType());

            System.out.println("Array is of length " + simpleArray.length());

            String[] values = (String[]) simpleArray.getArray();

            for (int i = 0; i < values.length; i++)
                System.out.println("row " + i + " = '" + values[i] + "'");

            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}