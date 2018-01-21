package gtu.db.jdbc;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.Struct;
import java.sql.Types;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class CallableStatement_ArrayType {
    
    /*
    CREATE TYPE t_demo_object AS OBJECT (
            some_number  NUMBER,
            some_string  varchar2(32)
          )
    
    CREATE TYPE t_demo_objects IS TABLE OF t_demo_object
    
    CREATE OR REPLACE PROCEDURE p_generate_demo_objects(p_num IN NUMBER, p_data OUT t_demo_objects) AS 
    BEGIN
        p_data := t_demo_objects();
        p_data.extend(p_num);
        FOR i IN 1..p_num LOOP
            p_data(i) := t_demo_object(i, 'Index ' || i);
        END LOOP;
    END p_generate_demo_objects;
    
    CREATE TABLE demo (
            some_number  NUMBER,
            some_string  varchar2(32)
        );
         
    CREATE OR REPLACE PROCEDURE p_receive_demo_objects(p_data IN t_demo_objects) AS 
    BEGIN
        FOR idx IN p_data.FIRST .. p_data.LAST LOOP
            INSERT INTO demo(some_number, some_string) VALUES(p_data(idx).some_number, p_data(idx).some_string);
        END LOOP;
        COMMIT;
    END p_receive_demo_objects;
        
    SELECT t_demo_object(colum_a, column_b) bulk collect INTO p_data FROM some_table;
        */

    public static void main(String... a) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:<USER>/<PASS>@<DATABASEHOST>:1521:<SERVICE>");

        final String typeName = "T_DEMO_OBJECT";
        final String typeTableName = "T_DEMO_OBJECTS";

        // Get a description of your type (Oracle specific)
        final StructDescriptor structDescriptor = StructDescriptor.createDescriptor(typeName.toUpperCase(), connection);
        final ResultSetMetaData metaData = structDescriptor.getMetaData();

        // Call the procedure (or whatever else) that returns the table of a
        // custom type
        CallableStatement cs = connection.prepareCall("{call p_generate_demo_objects(?, ?)}");
        cs.setInt(1, 5);
        // Result is an java.sql.Array...
        cs.registerOutParameter(2, Types.ARRAY, typeTableName);
        cs.execute();

        // ...who's elements are java.sql.Structs
        Object[] data = (Object[]) ((Array) cs.getObject(2)).getArray();
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
        cs.close();

        // See sandes comment, passing arrays of Objects
        // We need the array descriptor as well
        final ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(typeTableName.toUpperCase(), connection);
        // Create generic object array

        // Create ARRAY from array of STRUCTS
        final ARRAY demoObjectsFromJava = new ARRAY(arrayDescriptor, connection, new STRUCT[] {
                // STRUCTS are created with the struct descriptor and a generic
                // object array containing the values of the
                // attributes of the T_DEMO_OBJECT
                new STRUCT(structDescriptor, connection, new Object[] { 23, "Testobject 1" }), new STRUCT(structDescriptor, connection, new Object[] { 42, "Testobject 2" }) });
        cs = connection.prepareCall("{call p_receive_demo_objects(?)}");
        // setObject with the designated sql type
        cs.setObject(1, demoObjectsFromJava, Types.ARRAY);
        cs.execute();
        // no output here, have a look in table demo
        cs.close();

        connection.close();
    }
}
