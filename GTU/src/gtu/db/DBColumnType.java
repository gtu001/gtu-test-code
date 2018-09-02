package gtu.db;

import java.sql.Types;

public enum DBColumnType {
    BIT(Types.BIT), //
    TINYINT(Types.TINYINT), //
    SMALLINT(Types.SMALLINT), //
    INTEGER(Types.INTEGER), //
    BIGINT(Types.BIGINT), //
    FLOAT(Types.FLOAT), //
    REAL(Types.REAL), //
    DOUBLE(Types.DOUBLE), //
    NUMERIC(Types.NUMERIC), //
    DECIMAL(Types.DECIMAL), //
    CHAR(Types.CHAR), //
    VARCHAR(Types.VARCHAR), //
    LONGVARCHAR(Types.LONGVARCHAR), //
    DATE(Types.DATE), //
    TIME(Types.TIME), //
    TIMESTAMP(Types.TIMESTAMP), //
    BINARY(Types.BINARY), //
    VARBINARY(Types.VARBINARY), //
    LONGVARBINARY(Types.LONGVARBINARY), //
    NULL(Types.NULL), //
    OTHER(Types.OTHER), //
    JAVA_OBJECT(Types.JAVA_OBJECT), //
    DISTINCT(Types.DISTINCT), //
    STRUCT(Types.STRUCT), //
    ARRAY(Types.ARRAY), //
    BLOB(Types.BLOB), //
    CLOB(Types.CLOB), //
    REF(Types.REF), //
    DATALINK(Types.DATALINK), //
    BOOLEAN(Types.BOOLEAN), //
    ROWID(Types.ROWID), //
    NCHAR(Types.NCHAR), //
    NVARCHAR(Types.NVARCHAR), //
    LONGNVARCHAR(Types.LONGNVARCHAR), //
    NCLOB(Types.NCLOB), //
    SQLXML(Types.SQLXML), //
    NA(-9999), //
    // Types.NUMERIC
    ;//
    final int val;

    DBColumnType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static boolean isNumber(int val) {
        for (DBColumnType c : new DBColumnType[] { TINYINT, SMALLINT, INTEGER, BIGINT, FLOAT, DOUBLE, NUMERIC, DECIMAL }) {
            if (c.getVal() == val) {
                return true;
            }
        }
        return false;
    }

    public static DBColumnType lookup(int val) {
        for (DBColumnType c : DBColumnType.values()) {
            if (c.getVal() == val) {
                return c;
            }
        }
        return NA;
    }
}