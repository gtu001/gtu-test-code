package gtu.db.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnConfig {

    /**
     * @param args
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Connection conn = null;
        
        //JDBC Transaction Isolation Levels ↓↓↓↓↓↓↓
        //Allows dirty reads, non-repeatable reads, and phantom reads to occur.
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        //Ensures only committed data can be read.
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //Is close to being "serializable," however, "phantom" reads are possible.
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        //Dirty reads, non-repeatable reads, and phantom reads are prevented. Serializable.
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        //判斷是否支援
        DatabaseMetaData dbMd = conn.getMetaData();
        if (dbMd.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)) {
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }
        //印出支援項目
        DatabaseMetaData dbMetaData = conn.getMetaData();
        System.out.println(dbMetaData.supportsTransactions());
        //JDBC Transaction Isolation Levels ↑↑↑↑↑↑↑
        
        //建立table ↓↓↓↓↓↓↓
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TYPE number_varray AS VARRAY(10) OF NUMBER(12, 2)");
        stmt.execute ("CREATE TYPE my_object AS OBJECT(col_string2 VARCHAR(30), col_int2 INTEGER)");
        String sql = "CREATE TABLE oracle_all_table("
            + "col_short           SMALLINT, "          // short
            + "col_int             INTEGER, "           // int
            + "col_float           REAL, "              // float; can also be NUMBER
            + "col_double          DOUBLE PRECISION, "  // double; can also be FLOAT or NUMBER
            + "col_bigdecimal      DECIMAL(13,0), "     // BigDecimal
            + "col_string          VARCHAR2(254), "     // String; can also be CHAR(n)
            + "col_characterstream LONG, "              // CharacterStream or AsciiStream
            + "col_bytes           RAW(2000), "         // byte[]; can also be LONG RAW(n)
            + "col_binarystream    RAW(2000), "         // BinaryStream; can also be LONG RAW(n)
            + "col_timestamp       DATE, "              // Timestamp
            + "col_clob            CLOB, "              // Clob
            + "col_blob            BLOB, "              // Blob; can also be BFILE
            + "col_array           number_varray, "     // oracle.sql.ARRAY
            + "col_object          my_object)";         // oracle.sql.OBJECT
        stmt.executeUpdate(sql);
        //建立table ↑↑↑↑↑↑↑
        
        //↓↓↓↓↓↓↓
        String procedure = "CREATE OR REPLACE PROCEDURE myproc IS "//
                + "BEGIN "//
                + "INSERT INTO oracle_table VALUES('string 1'); "//
                + "END;";//
        stmt.executeUpdate(procedure);
        //↑↑↑↑↑↑↑
        
        //Create procedure myprocin with an IN parameter named x. ↓↓↓↓↓↓↓
        // IN is the default mode for parameter, so both 'x VARCHAR' and 'x IN VARCHAR' are valid
        procedure = "CREATE OR REPLACE PROCEDURE myprocin(x VARCHAR) IS "//
                + "BEGIN "//
                + "INSERT INTO oracle_table VALUES(x); "//
                + "END;";
        stmt.executeUpdate(procedure);
        //↑↑↑↑↑↑↑
        
        //Create procedure myprocout with an OUT parameter named x ↓↓↓↓↓↓↓
        procedure = "CREATE OR REPLACE PROCEDURE myprocout(x OUT VARCHAR) IS BEGIN "
                + "INSERT INTO oracle_table VALUES('string 2'); x := 'outvalue'; END;";
        stmt.executeUpdate(procedure);
        //↑↑↑↑↑↑↑
        
        //Create procedure myprocinout with an IN/OUT parameter named x; x is an IN parameter and an OUT parameter ↓↓↓↓↓↓↓
        procedure = "CREATE OR REPLACE PROCEDURE myprocinout(x IN OUT VARCHAR) IS BEGIN "//
                + "INSERT INTO oracle_table VALUES(x); " // Use x as IN parameter
                + "x := 'outvalue'; " // Use x as OUT parameter
                + "END;";
        stmt.executeUpdate(procedure);
        //↑↑↑↑↑↑↑
        
        //Create a function named myfunc which returns a VARCHAR value; the function has no parameter ↓↓↓↓↓↓↓
        String function = "CREATE OR REPLACE FUNCTION myfunc RETURN VARCHAR IS BEGIN RETURN 'a returned string'; END;";
        stmt.executeUpdate(function);
        // ↑↑↑↑↑↑↑
        
        //Create a function named myfuncin which returns a VARCHAR value; the function has an IN parameter named x ↓↓↓↓↓↓↓
        function = "CREATE OR REPLACE FUNCTION myfuncin(x VARCHAR) RETURN VARCHAR IS "
                + "BEGIN RETURN 'a return string'||x; END;";
        stmt.executeUpdate(function);
        // ↑↑↑↑↑↑↑
        
        //Create a function named myfuncout which returns a VARCHAR value; ↓↓↓↓↓↓↓
        function = "CREATE OR REPLACE FUNCTION myfuncout(x OUT VARCHAR) RETURN VARCHAR IS " + "BEGIN "
                + "x:= 'outvalue'; " + "RETURN 'a returned string'; " + "END;";
        stmt.executeUpdate(function);
        // ↑↑↑↑↑↑↑
            
        //Create a function named myfuncinout that returns a VARCHAR value ↓↓↓↓↓↓↓
        function = "CREATE OR REPLACE FUNCTION myfuncinout(x IN OUT VARCHAR) RETURN VARCHAR IS "
                + "BEGIN x:= x||'outvalue'; RETURN 'a returned string'; END;";
        stmt.executeUpdate(function);
        // ↑↑↑↑↑↑↑
            
        //Creating an OBJECT Type in an Oracle Database ↓↓↓↓↓↓↓
        stmt.execute("CREATE TYPE object2 AS OBJECT(col_string2 VARCHAR(30), col_integer2 NUMBER)");
        stmt.execute("CREATE TYPE object1 AS OBJECT(col_string1 VARCHAR(30), col_integer2 object2)");
        stmt.execute("CREATE TABLE object1_table(col_integer NUMBER, col_object1 object1)");
        // ↑↑↑↑↑↑↑
        
        //Inserting an OBJECT Value into an Oracle Table ↓↓↓↓↓↓↓
        stmt.execute("CREATE TYPE object2 AS OBJECT(col_string2 VARCHAR(30), col_integer2 NUMBER)");
        stmt.execute("CREATE TYPE object1 AS OBJECT(col_string1 VARCHAR(30), col_integer2 object2)");
        stmt.execute("CREATE TABLE object1_table(col_integer NUMBER, col_object1 object1)");
        stmt.execute("INSERT INTO object1_table VALUES(1, object1('str1', object2('obj2str1', 123)))");
        // ↑↑↑↑↑↑↑
        
        //Get list of stored procedure names ↓↓↓↓↓↓↓
        DatabaseMetaData metadata = conn.getMetaData();
        ResultSet result = metadata.getProcedures(null, "JAVA", "%");
        while (result.next()) {
          System.out.println(result.getString("PROCEDURE_CAT") + " - "
              + result.getString("PROCEDURE_SCHEM") + " - " + result.getString("PROCEDURE_NAME"));
        }
        // ↑↑↑↑↑↑↑
        
        //Store and retrieve an object from a table ↓↓↓↓↓↓↓
        Employee employee = new Employee(42, "AA", 9);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(employee);
        byte[] employeeAsBytes = baos.toByteArray();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EMPLOYEE (emp) VALUES(?)");
        ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
        pstmt.setBinaryStream(1, bais, employeeAsBytes.length);
        pstmt.executeUpdate();
        pstmt.close();
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT emp FROM Employee");
        while (rs.next()) {
            byte[] st = (byte[]) rs.getObject(1);
            ByteArrayInputStream baip = new ByteArrayInputStream(st);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Employee emp = (Employee) ois.readObject();
        }
        stmt.close();
        rs.close();
        conn.close();
        // ↑↑↑↑↑↑↑
        
        //A JDBC Program to Access/Read Microsoft Excel ↓↓↓↓↓↓↓
        String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String url = "jdbc:odbc:excelDB";
        String username = "yourName";
        String password = "yourPass";
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        stmt = conn.createStatement();
        rs = stmt.executeQuery("select * from [Sheet1$]");
        while (rs.next()) {
            System.out.println(rs.getString("FirstName") + " " + rs.getString("LastName"));
        }
        //Read data from Excel worksheet ↓↓↓↓↓↓↓
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        stmt = DriverManager.getConnection("jdbc:odbc:employee").createStatement();
        rs = stmt.executeQuery("select lastname, firstname, id from [Sheet1$]");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2) + "  id : " + rs.getInt(3));
        }
        //Read data from Excel ↓↓↓↓↓↓↓
        rs = DriverManager.getConnection("jdbc:odbc:employee_xls")//
                .createStatement()//
                .executeQuery("Select * from [Sheet1$]");//
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        System.out.println("No of cols " + numberOfColumns);
        while (rs.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                String columnValue = rs.getString(i);
                System.out.println(columnValue);
            }
        }
        //Use JDBC ODBC bridge to read from Excel ↓↓↓↓↓↓↓
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String myDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=c:/data.xls;"
            + "DriverID=22;READONLY=false";
        conn = DriverManager.getConnection(myDB, "", "");
        //Write Data to a Microsoft Excel Spreadsheet File ↓↓↓↓↓↓↓
        stmt = conn.createStatement();
        String excelQuery = "insert into [Sheet1$](FirstName, LastName) values('A', 'K')";
        stmt.executeUpdate(excelQuery);
        // ↑↑↑↑↑↑↑
        
        
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
        // ↓↓↓↓↓↓↓
        // ↑↑↑↑↑↑↑
    }

    private static class Employee implements Serializable {
        int ID;
        String name;
        double salary;
        public Employee(int ID, String name, double salary) {
            this.ID = ID;
            this.name = name;
            this.salary = salary;
        }
    }
}
