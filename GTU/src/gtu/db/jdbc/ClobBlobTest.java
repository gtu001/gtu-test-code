package gtu.db.jdbc;

import gtu.db.DbConstant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ClobBlobTest {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = DbConstant.getTestDataSource();
        Connection conn = dataSource.getConnection();
        ClobBlobTest test = new ClobBlobTest();
        test.addRowToCoffeeDescriptions(0, "C:/main.cpp", conn);
    }

    private static final String DBTYPE = "mysql";

    public void addRowToCoffeeDescriptions(long coffeeName, String fileName, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            Clob myClob = getWritedClob(fileName, con);
            Blob myBlob = getWritedBlob(fileName, con);

            String sql = "INSERT INTO test_clob_blob (id, col_clob, col_blob) VALUES(?,?,?)";

            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, coffeeName);
            pstmt.setClob(2, myClob);
            pstmt.setBlob(3, myBlob);
            
            pstmt.executeUpdate();
            
            myClob.free();
            myBlob.free();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Unexpected exception: " + ex.toString());
        } finally {
            if (pstmt != null){
                pstmt.close();
            }
        }
    }

    /**
     * blob
     */
    private Blob getWritedBlob(String fileName, Connection con) throws IOException, SQLException {
        //create blob
        Blob myBlob = con.createBlob();
        OutputStream outStream = myBlob.setBinaryStream(1);

        //write blob
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String nextLine = null;
        while ((nextLine = br.readLine()) != null) {
            System.out.println("Writing Blob: " + nextLine);
            writer.write(nextLine);
            writer.newLine();
        }
        br.close();
        writer.flush();
        writer.close();
        return myBlob;
    }

    /**
     * clob
     */
    private Clob getWritedClob(String fileName, Connection con) throws FileNotFoundException, IOException, SQLException {
        // create Clob
        Clob myClob = con.createClob();
        Writer clobWriter = myClob.setCharacterStream(1);

        // write content
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String nextLine = "";
        StringBuffer sb = new StringBuffer();
        while ((nextLine = br.readLine()) != null) {
            System.out.println("Writing Clob: " + nextLine);
            clobWriter.write(nextLine + "\n");
            sb.append(nextLine + "\n");
        }
        br.close();
        String clobData = sb.toString();

        if (DBTYPE.equals("mysql")) {
            System.out.println("MySQL, setting String in Clob " + "object with setString method");
            myClob.setString(1, clobData);
        }
        return myClob;
    }
}
