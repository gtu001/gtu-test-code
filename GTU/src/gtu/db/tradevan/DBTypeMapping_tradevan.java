package gtu.db.tradevan;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Timestamp;

public class DBTypeMapping_tradevan {
    
    public enum JdbcTypeMappingToJava {
        CHAR(java.sql.Types.CHAR, String.class), //
        NCHAR(java.sql.Types.NCHAR, String.class), //
        VARCHAR(java.sql.Types.VARCHAR, String.class), //
        NVARCHAR(java.sql.Types.NVARCHAR, String.class),//
        LONGVARCHAR(java.sql.Types.LONGVARCHAR, String.class), //
        NUMERIC(java.sql.Types.NUMERIC, BigDecimal.class), //
        DECIMAL(java.sql.Types.DECIMAL, BigDecimal.class), //
        BIT(java.sql.Types.BIT, Boolean.class), //
        BOOLEAN(java.sql.Types.BOOLEAN, Boolean.class), //
        TINYINT(java.sql.Types.TINYINT, Integer.class), //
        SMALLINT(java.sql.Types.SMALLINT, Short.class), //
        INTEGER(java.sql.Types.INTEGER, Integer.class), //
        BIGINT(java.sql.Types.BIGINT, Long.class), //
        REAL(java.sql.Types.REAL, Float.class), //
        FLOAT(java.sql.Types.FLOAT, Double.class), //
        DOUBLE(java.sql.Types.DOUBLE, Double.class), //
        BINARY(java.sql.Types.BINARY, byte[].class), //
        VARBINARY(java.sql.Types.VARBINARY, byte[].class), //
        LONGVARBINARY(java.sql.Types.LONGVARBINARY, byte[].class), //
        DATE(java.sql.Types.DATE, java.sql.Date.class), //
        TIME(java.sql.Types.TIME, java.sql.Time.class), //
        TIMESTAMP(java.sql.Types.TIMESTAMP, Timestamp.class), //
        CLOB(java.sql.Types.CLOB, Clob.class), //
        BLOB(java.sql.Types.BLOB, Blob.class), //
        ARRAY(java.sql.Types.ARRAY, Array.class), //
        STRUCT(java.sql.Types.STRUCT, Struct.class), //
        REF(java.sql.Types.REF, Ref.class), //
        DISTINCT(java.sql.Types.DISTINCT, void.class), //
        JAVA_OBJECT(java.sql.Types.JAVA_OBJECT, void.class), //
        ;

        final int typeInt;
        final Class<?> clz;

        JdbcTypeMappingToJava(int typeInt, Class<?> clz) {
            this.typeInt = typeInt;
            this.clz = clz;
        }

        public static Class<?> getMappingClass(int typeInt) {
            for (JdbcTypeMappingToJava e : JdbcTypeMappingToJava.values()) {
                if (e.typeInt == typeInt) {
                    return e.clz;
                }
            }
            throw new RuntimeException("找不到對應 : " + typeInt);
        }
    }
}
