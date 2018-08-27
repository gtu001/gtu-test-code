package gtu.db.jdbc.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.ebao.foundation.core.RTException;
import com.ebao.foundation.module.db.data.DBObjectOwner;
import com.ebao.foundation.module.db.dialect.DialectLocator;

public class DBUtils_eBaotech {
    private static Logger logger = Logger.getLogger(DBUtils_eBaotech.class);

    private static final int N_MAX_BUFFER_SIZE = 4 * 1024;

    private static final int MAX_BUFFER_SIZE = 4 * 1024;

    private static Hashtable<String, DBObjectOwner> dbObjectOwnerCache = new Hashtable<String, DBObjectOwner>();

    /**
     *
     * @param squence_name
     * @return
     */
    public static long getNextSequenceValue(String squence_name) {
        // return
        // OracleSequencePKGenerator.getInstance().getNextPKValue(squence_name);
        return DialectLocator.locateDialect().getNextValue(squence_name);
    }

    public static void clear(ResultSet rs, PreparedStatement pst, DBean db) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception e) {
        }
        try {
            if (pst != null)
                pst.close();
        } catch (Exception e) {
        }
        try {
            if (db != null)
                db.close();
        } catch (Exception e) {
        }
    }

    /*----------------------------------------------------------------------
     Function name:             readBlob()
     Description:
     Input:                 table_name,field_name(blob type),where 子句的描述部�????,不包含where
     Author:                             Robert Huang
     Date:                  July 4,2000
     -----------------------------------------------------------------------*/
    public static String readBlob(String sTableName, String stField_name, String stClause) throws Exception {
        byte[] buff = readBlob_byte(sTableName, stField_name, stClause);
        if (buff == null) {
            return null;
        } else {
            String s = null;
            try {
                s = new String(buff, "UTF-8");
            } catch (Exception e) {
                throw e;
            }
            return s;
        }
    } // end of readBlob();

    /*----------------------------------------------------------------------
     Function name:             readBlob_byte()
     Description:
     Input:                 table_name,field_name(blob type),where �Ӿ�������,����where
     Author:                             Robert Huang
     Date:                  July 4,2000
     Update:         Jason Shi,2000/10/30
     change use store procedure to use jdbc blob
     -----------------------------------------------------------------------*/
    public static byte[] readBlob_byte(String sTableName, String stField_name, String stClause) throws Exception {
        String sql = null;
        DBean db = new DBean(true, false);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        byte[] buff = null;
        try {
            db.connect();
            conn = db.getConnection();
            // get the blob length
            sql = "select " + stField_name + " from " + sTableName + "  where " + stClause;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                buff = blobToBytes(rs.getBlob(stField_name));
            }
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            clear(rs, null, db);
        }
        return buff;
    } // end of method readBlob_byte()

    public static char[] readClob(String sTableName, String stField_name, String stClause) throws Exception {
        String sql = "";
        Connection conn = null;
        DBean db = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        char[] buff;
        try {
            db = new DBean(true, false);
            db.connect();
            conn = db.getConnection();
            sql = "select " + stField_name + " from " + sTableName + " WHERE " + stClause;
            pst = conn.prepareStatement(sql);
            pst.execute(sql);
            rs = pst.getResultSet();
            java.sql.Clob clob;
            if (rs.next()) {
                clob = rs.getClob(1);
                if (clob == null)
                    return null;
                // Convent clob to char[]
                Reader ins = clob.getCharacterStream();
                StringBuffer buffer = new StringBuffer();
                int ch = 0;
                while ((ch = ins.read()) != -1) {
                    buffer.append((char) ch);
                }
                buff = buffer.toString().toCharArray();
                return buff;
            }
            return null;
        } catch (Exception ex) {
            logger.error("", ex);
            throw ex;
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception e) {
                    logger.warn(e);
                }
            }
        }
    }

    public static void readClob2File(String sTableName, String stField_name, String stClause, String file) throws Exception {
        ResultSet rs = null;
        String sql = "SELECT " + stField_name + "  FROM " + sTableName + " WHERE " + stClause;
        Connection con = null;
        Statement st = null;
        DBean db = null;
        FileWriter fwObj = null;
        try {
            db = new DBean(true, false);
            db.connect();
            con = db.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                Reader rdObj = rs.getClob(stField_name).getCharacterStream();
                char[] chr = new char[MAX_BUFFER_SIZE];
                int readed = 0;
                File files = new File(file);
                fwObj = new FileWriter(files);
                while ((readed = rdObj.read(chr)) != -1) {
                    fwObj.write(chr, 0, readed);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            try {

                if (fwObj != null)
                    fwObj.close();
                if (db != null)
                    DBean.closeAll(rs, st, db);
            } catch (Exception e) {
            }
        }
    }

    public static void readBlob2File(String sTableName, String stField_name, String stClause, String file) throws Exception {
        ResultSet rs = null;
        String sql = "SELECT " + stField_name + "  FROM " + sTableName + " WHERE " + stClause;
        if (logger.isDebugEnabled())
            logger.debug("sql=" + sql);
        Connection con = null;
        Statement st = null;
        DBean db = null;
        FileOutputStream fos = null;
        try {
            db = new DBean(true, false);
            db.connect();
            con = db.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                InputStream ins = rs.getBlob(stField_name).getBinaryStream();
                byte[] bt = new byte[MAX_BUFFER_SIZE];
                int readed = 0;
                File files = new File(file);
                fos = new FileOutputStream(files);
                while ((readed = ins.read(bt)) != -1) {
                    fos.write(bt, 0, readed);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            try {

                if (fos != null)
                    fos.close();
                if (db != null)
                    DBean.closeAll(rs, st, db);
            } catch (Exception e) {
            }
        }
    }

    public static void writeBlob(byte[] buf, int nDataLength, String sTableName, String stField_name, String stClause) throws Exception {
        // Tools.writeBlob(buf, nDataLength, sTableName, stField_name,
        // stClause);
        writeBlobUseProcedure(buf, nDataLength, sTableName, stField_name, stClause);
    }

    /**
     * ���ô洢���дCLOB���??????
     *
     * @param buf
     *            buffer
     * @param nDataLength
     *            buffer length
     * @param sTableName
     *            table name
     * @param stField_name
     *            field name
     * @param stClause
     *            clause
     * @throws Exception
     */
    public static void writeClobUseProcedure(char[] buf, int nDataLength, String sTableName, String stField_name, String stClause) throws Exception {
        writeClobUseProcedure(buf, nDataLength, sTableName, stField_name, stClause, false);
    } // end of method writeClob()

    public static void writeClobUseProcedure(char[] buf, int nDataLength, String sTableName, String stField_name, String stClause, boolean isAppend) {

        String sql = null;
        // String sql1="SELECT photo from t_agent where agent_id=261 for
        // update";
        String sql1 = "SELECT " + stField_name + "  FROM " + sTableName + " WHERE " + stClause + " FOR UPDATE";
        Connection conn = null;
        CallableStatement stmt = null;
        DBean db = null;
        try {
            db = new DBean(false, false);
            db.connect();
            conn = db.getConnection();
            if (!isAppend) {
                sql = "update " + sTableName + " set " + stField_name + "=EMPTY_CLOB() WHERE " + stClause;
                Statement stmt1 = conn.createStatement();
                stmt1.execute(sql);
                stmt1.close();
            }
            int iTimes = (int) (nDataLength / (N_MAX_BUFFER_SIZE));
            int iRead;

            char[] buf2 = new char[N_MAX_BUFFER_SIZE];
            // sql = "{call p_write_clob(?,?,?)}";
            sql = "{call PKG_PUB_DB_UTILS.P_WRITE_CLOB(?,?,?)}";
            stmt = conn.prepareCall(sql);
            stmt.setString(1, sql1);
            for (int i = 0; i < iTimes; i++) {
                // iRead=fin.read(buf2,i*31*1024,31*1024);
                // out.println(String.valueOf(iRead));
                /*
                 * for (int j = 0; j < N_MAX_BUFFER_SIZE; j++) { buf2[j] = buf[i
                 * * N_MAX_BUFFER_SIZE + j]; }
                 */
                System.arraycopy(buf, i * N_MAX_BUFFER_SIZE, buf2, 0, N_MAX_BUFFER_SIZE);

                stmt.setInt(2, N_MAX_BUFFER_SIZE);
                stmt.setString(3, new String(buf2));
                stmt.executeUpdate();
            }
            int iRemain = nDataLength - (int) iTimes * N_MAX_BUFFER_SIZE;
            if (iRemain > 0) {
                char[] buf3 = new char[iRemain];
                /*
                 * for (int j = 0; j < iRemain; j++) { buf3[j] = buf[iTimes *
                 * N_MAX_BUFFER_SIZE + j]; }
                 */
                System.arraycopy(buf, iTimes * N_MAX_BUFFER_SIZE, buf3, 0, iRemain);
                stmt.setInt(2, iRemain);
                stmt.setString(3, new String(buf3));
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (Exception e) {
            logger.error("", e);
            throw new RTException(e);
        } finally {
            db.close();
        }
    } // end of method writeClob()

    public static void writeClob(InputStream ins, String sTable, String sField, String sClause) {
        byte[] buf = new byte[MAX_BUFFER_SIZE];
        char[] c = null;
        int nCount = 0;
        int i = 0;
        String s = "";
        try {
            while ((nCount = ins.read(buf)) != -1) {
                i++;
                s = new String(buf, 0, nCount);
                c = s.toCharArray();
                if (c != null) {
                    if (i == 1) {
                        writeClobUseProcedure(c, c.length, sTable, sField, sClause, false);
                    } else {
                        writeClobUseProcedure(c, c.length, sTable, sField, sClause, true);
                    }
                }
            }

            ins.close();
        } catch (IOException e) {
            throw new RTException(e);
        }
    }

    /**
     * �����������д��ָ�����BLOB�ֶ�
     *
     * @param ins
     *            ���������??????
     * @param sTable
     *            ��ݱ�??????
     * @param sField
     *            Blob�ֶ�
     * @param sClause
     *            ��ѯ���??????
     */
    public static void writeBlob(InputStream ins, String sTable, String sField, String sClause) throws Exception {

        byte[] bt = new byte[MAX_BUFFER_SIZE];
        int readed = 0;
        File file = new File(String.valueOf(Math.random()) + ".data");
        FileOutputStream fos = new FileOutputStream(file);
        while ((readed = ins.read(bt)) != -1) {
            fos.write(bt, 0, readed);
        }
        fos.close();

        int length = (int) file.length();
        InputStream input = new FileInputStream(file);
        byte[] by = new byte[length];
        if (length > 0) {
            int i;
            while (-1 != (i = input.read(by, 0, by.length))) {
                input.read(by, 0, i);
            }
        }
        ins.close();
        input.close();
        file.delete();

        if (by != null) {
            writeBlob(by, by.length, sTable, sField, sClause);
        }

        /*
         * Connection conn = null; DBean db = null; PreparedStatement pst =
         * null; ResultSet rs = null;
         * 
         * try { db = new DBean(true, false); db.connect(); conn =
         * db.getConnection(); // ���ԭ�ȵ�BLOB�ֶ� String sql = " update " +
         * sTable + " set " + sField + " = EMPTY_BLOB() WHERE " + sClause; pst =
         * conn.prepareStatement(sql); pst.executeUpdate(); pst.close(); //
         * �s�BLOB�ֶΣ�׼��д�����?????? sql = "select " + sField + " from " +
         * sTable + " WHERE " + sClause + " FOR UPDATE"; pst =
         * conn.prepareStatement(sql); pst.execute(sql); rs =
         * pst.getResultSet();
         * 
         * if (rs.next()) { java.sql.Blob blob = (java.sql.Blob)rs.getBlob(1);
         * //oracle.sql.BLOB blob = (BLOB) rs.getBlob(1);//change blob to
         * oracle.sql.blob BufferedInputStream dataInput = new
         * BufferedInputStream(ins, MAX_BUFFER_SIZE); // ʹ����Weblogic��CLOBʵ��
         * //OutputStream outs = ((weblogic.jdbc.rmi.SerialOracleBlob)
         * blob).getBinaryOutputStream(); OutputStream outs = null;//LMF BLOB
         * //outs = blob.getBinaryOutputStream();//����oracle��BLOBʵ�� outs =
         * blob.setBinaryStream(1);
         * 
         * BufferedOutputStream dataOutput = new BufferedOutputStream(outs,
         * MAX_BUFFER_SIZE);
         * 
         * byte[] buf = new byte[MAX_BUFFER_SIZE]; int nCount = 0;
         * 
         * while ((nCount = dataInput.read(buf)) != -1) { dataOutput.write(buf,
         * 0, nCount); }
         * 
         * dataOutput.flush(); dataOutput.close(); outs.close();
         * 
         * dataInput.close(); ins.close(); }
         * 
         * rs.close(); pst.close(); } catch (Exception ex) { throw ex; } finally
         * { db.close(); }
         */
    }

    public static void writeClob(char[] buf, int nDataLength, String sTableName, String stField_name, String stClause) throws Exception {
        writeClobUseProcedure(buf, nDataLength, sTableName, stField_name, stClause);
    }

    /**
     * дT_BLOB��
     *
     * @param buf
     *            buffer
     * @param length
     *            buffer length
     * @return blob_id blob id
     * @throws Exception
     */
    public static long writeBlob(byte[] buf, int length) throws Exception {
        DBean db = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            db = new DBean(false, false);
            db.connect();
            Connection conn = db.getConnection();

            String sql = "select s_blob__blob_id.nextval from dual";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            long blobId = 0l;
            if (rs.next()) {
                blobId = rs.getLong(1);
            }
            pst.close();

            sql = "insert into t_BLOB (BLOB_ID) values (?) ";
            pst = conn.prepareStatement(sql);
            pst.setLong(1, blobId);
            pst.executeUpdate();
            pst.close();

            String sWhere = " BLOB_ID = " + blobId;
            writeBlob(buf, length, "t_BLOB", "content", sWhere);
            return blobId;
        } catch (Exception e) {
            throw e;
        } finally {
            clear(rs, pst, db);
        }
    }

    public static long writeClob(char[] buf, int length) throws Exception {
        DBean db = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            db = new DBean(false, false);
            db.connect();
            Connection conn = db.getConnection();

            String sql = "select s_clob__clob_id.nextval from dual";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            long clobId = 0l;
            if (rs.next()) {
                clobId = rs.getLong(1);
            }
            pst.close();

            sql = "insert into t_CLOB (CLOB_ID) values (?) ";
            pst = conn.prepareStatement(sql);
            pst.setLong(1, clobId);
            pst.executeUpdate();
            pst.close();

            String sWhere = " CLOB_ID = " + clobId;
            writeClob(buf, length, "t_CLOB", "content", sWhere);
            return clobId;
        } catch (Exception e) {
            throw e;
        } finally {
            clear(rs, pst, db);
        }
    }

    /**
     * ȡ��ݿ⵱ǰϵͳʱ��??????
     *
     * @return ��ݿ�ϵͳʱ��?????? ���ʧ�ܣ�����null
     * @throws Exception
     */
    public static Timestamp getDBTime() throws Exception {
        DBean db = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Timestamp ts = null;

        try {
            String sql = " select sysdate from dual ";
            db = new DBean(false, false);
            db.connect();
            conn = db.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                ts = rs.getTimestamp(1);
            }

            rs.close();
            pst.close();
        } catch (Exception ex) {
            logger.error("", ex);
            throw new RTException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (db != null) {
                    db.close();
                }
            } catch (SQLException sqlex) {
                throw new RTException(sqlex);
            }
        }
        return ts;
    }

    /**
     * �����ַ��е������ַ�for oracle
     *
     * @param pOrgString
     *            string
     * @return string after filter
     */
    public static String filterOracleSqlString(String pOrgString) {
        String sResult = pOrgString;
        if (sResult == null) {
            sResult = "";
        }
        sResult = replaceString("'", "''", sResult);
        return sResult;
    }

    private static byte[] blobToBytes(Blob pBlob) {
        byte[] buffer = null;
        try {
            if (pBlob != null) {
                buffer = pBlob.getBytes((long) 1, (int) pBlob.length());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return buffer;
    }

    /**
     * ���ô洢���дBLOB���??????
     *
     * @param buf
     * @param nDataLength
     * @param sTableName
     * @param stField_name
     * @param stClause
     */
    private static void writeBlobUseProcedure(byte[] buf, int nDataLength, String sTableName, String stField_name, String stClause) throws Exception {
        String sql = null;
        // String sql1="SELECT photo from t_agent where agent_id=261 for
        // update";
        String sql1 = "SELECT " + stField_name + "  FROM " + sTableName + " WHERE " + stClause + " FOR UPDATE";
        Connection conn = null;
        CallableStatement stmt = null;
        DBean db = null;
        try {
            db = new DBean(false, false);
            db.connect();
            conn = db.getConnection();

            sql = "update " + sTableName + " set " + stField_name + "=EMPTY_BLOB() WHERE " + stClause;
            Statement stmt1 = conn.createStatement();
            stmt1.execute(sql);
            stmt1.close();
            int iTimes = (int) (nDataLength / (N_MAX_BUFFER_SIZE));
            int iRead;

            byte[] buf2 = new byte[N_MAX_BUFFER_SIZE];
            // sql = "{call p_write_blob(?,?,?)}";
            sql = "{call PKG_PUB_DB_UTILS.P_WRITE_BLOB(?,?,?)}";
            stmt = conn.prepareCall(sql);
            stmt.setString(1, sql1);
            for (int i = 0; i < iTimes; i++) {
                // iRead=fin.read(buf2,i*31*1024,31*1024);
                // out.println(String.valueOf(iRead));
                /*
                 * for (int j = 0; j < N_MAX_BUFFER_SIZE; j++) { buf2[j] = buf[i
                 * * N_MAX_BUFFER_SIZE + j]; }
                 */
                System.arraycopy(buf, i * N_MAX_BUFFER_SIZE, buf2, 0, N_MAX_BUFFER_SIZE);
                stmt.setInt(2, N_MAX_BUFFER_SIZE);
                stmt.setBytes(3, buf2);
                stmt.executeUpdate();
            }
            int iRemain = nDataLength - (int) iTimes * N_MAX_BUFFER_SIZE;
            if (iRemain > 0) {
                byte[] buf3 = new byte[iRemain];
                /*
                 * for (int j = 0; j < iRemain; j++) { buf3[j] = buf[iTimes *
                 * N_MAX_BUFFER_SIZE + j]; }
                 */
                System.arraycopy(buf, iTimes * N_MAX_BUFFER_SIZE, buf3, 0, iRemain);
                stmt.setInt(2, iRemain);
                stmt.setBytes(3, buf3);
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            db.close();
        }
    } // end of method writeBlob()

    // this method to replace space to &nbsp and newline carriage return to <br>
    // ;

    public/***********************************************************************
           * Name:replaceString
           *
           * Function:replace the olsStr to newStr in a string wholeStr
           *
           * Author:Minghua Wang
           *
           * Create Date:July 5, 2000
           *
           * Parameter: String oldStr , String newStr, String wholeStr
           *
           * Return value: return the replaced string
           **********************************************************************/
    static String replaceString(String oldStr, String newStr, String wholeStr) {
        if (wholeStr == null)
            return "";

        if (oldStr == null)
            return wholeStr;
        if (newStr == null)
            return wholeStr;
        // canceled by rodolf
        /*
         * //change by jason shi for avoid dead loop
         * if(newStr.indexOf(oldStr)>=0){ return wholeStr; }
         */
        int start, end;
        StringBuffer result = new StringBuffer();
        result = result.append(wholeStr);
        // updated by rodolf
        start = 0;

        while (wholeStr.indexOf(oldStr, start) > -1) {
            start = wholeStr.indexOf(oldStr, start);
            end = start + oldStr.length();
            result.replace(start, end, newStr);
            wholeStr = result.toString();
            start += newStr.length();
        }
        return wholeStr;
    }

    /**
     * get Result List by sql String,the column name has converted into property
     * name eg:type_id-->typeId
     *
     * @param sqlResultList
     *            String
     * @throws Exception
     * @return Collection
     */
    public static Collection getResultList(String sqlResultList) throws Exception {
        return getResultList(sqlResultList, -1, -1);
    }

    /**
     * get Result List by sql String,the column name has converted into property
     * name eg:type_id-->typeId
     *
     * @param sqlResultList
     *            String
     * @param pageNo
     *            int
     * @param pageSize
     *            int
     * @throws Exception
     * @return Collection
     */
    public static Collection getResultList(String sqlResultList, int pageNo, int pageSize) throws Exception {
        PreparedStatement ps;
        ResultSet rs;
        DBean db;
        List resultList;
        Connection con = null;
        ps = null;
        rs = null;
        db = new DBean(true, false);
        resultList = new ArrayList();
        List list;
        try {
            db.connect();
            con = db.getConnection();
            if (pageNo < 0 || pageSize < 0)
                ps = con.prepareStatement(sqlResultList);
            else
                ps = con.prepareStatement(DBean.pageSQL(sqlResultList, pageNo, pageSize));
            rs = ps.executeQuery();
            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            HashMap resultCol;
            for (; rs.next(); resultList.add(resultCol)) {
                resultCol = new HashMap();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colName = metaData.getColumnName(i).toUpperCase();
                    Object value = rs.getObject(metaData.getColumnName(i));
                    if (value != null) {
                        if (value instanceof java.sql.Date)
                            value = new java.util.Date(((java.sql.Date) value).getTime());
                        resultCol.put(colName, value);
                    }
                }
            }
            list = resultList;
        } catch (Exception e) {
            throw e;
        } finally {
            DBean.closeAll(rs, ps, db);
        }
        return list;
    }

    /**
     * get property name
     *
     * @param colName
     *            String
     * @return String
     */
    public static String getPropertyName(String colName) {
        String propertyName = "";
        if (colName != null) {

            // commented by Martin Zhang for compatibility to JDK 1.3
            // String[] colNames = colName.toLowerCase().split("_");
            String[] colNames = org.apache.commons.lang.StringUtils.split(colName.toLowerCase(), "_");
            for (int i = 0; i < colNames.length; i++) {
                if (i == 0)
                    propertyName += colNames[i];
                else if (!colNames[i].equals(""))
                    propertyName += colNames[i].substring(0, 1).toUpperCase() + colNames[i].substring(1);
            }
        }
        return propertyName;
    }

    private static synchronized DBObjectOwner getDBObjectOwnerFromDB(String objectName) {
        DBean db = new DBean(false, false);
        DBObjectOwner dbObjectOwner = new DBObjectOwner();
        ;
        try {
            db.connect();
            Connection con = db.getConnection();
            // check object type
            String objectType;
            PreparedStatement pst = con.prepareStatement("select object_type from user_objects a where a.object_name=?");
            pst.setString(1, objectName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                objectType = rs.getString("object_type");
            } else {
                throw new RuntimeException("object not found in database:" + objectName);
            }
            rs.close();
            pst.close();

            if ("SYNONYM".equals(objectType)) {
                // search user_synonyms
                PreparedStatement pstSynonms = con.prepareStatement("select table_owner,table_name from user_synonyms a where a.synonym_name=?");
                pstSynonms.setString(1, objectName);
                rs = pstSynonms.executeQuery();
                if (rs.next()) {
                    dbObjectOwner.setObjectOwner(rs.getString("table_owner"));
                    dbObjectOwner.setObjectName(rs.getString("table_name"));
                } else {
                    throw new RuntimeException("SYNONYM not found:" + objectName);
                }
                rs.close();
                pstSynonms.close();
            } else {
                // return current user
                Statement st = con.createStatement();
                rs = st.executeQuery("select user from dual");
                rs.next();
                dbObjectOwner.setObjectOwner(rs.getString("user"));
                dbObjectOwner.setObjectName(objectName);
                rs.close();
                st.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            db.close();
        }
        return dbObjectOwner;

    }

    // get real owner of database object
    public static DBObjectOwner getDBObjectOwner(String objectName) {
        if (objectName == null) {
            throw new RuntimeException("objectName should not be null!");
        }
        String ucaseObjectName = objectName.toUpperCase();
        if (dbObjectOwnerCache.contains(ucaseObjectName)) {
            return dbObjectOwnerCache.get(ucaseObjectName);
        }
        DBObjectOwner owner = getDBObjectOwnerFromDB(ucaseObjectName);
        dbObjectOwnerCache.put(ucaseObjectName, owner);
        return owner;
    }
}