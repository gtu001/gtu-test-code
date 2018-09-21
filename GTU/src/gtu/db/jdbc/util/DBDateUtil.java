package gtu.db.jdbc.util;

public class DBDateUtil {

    public static void main(String[] args) {
    }

    public enum DBDateDateFormat {
        ORACLE {
            @Override
            String date2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY/MM/DD') ", columnName);
            }

            @Override
            String timestamp2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY-MM-DD HH24:MI:SS.FF9') ", columnName);
            }

            @Override
            String varchar2Timestamp(String columnName) {
                return String.format(" TO_TIMESTAMP(%s, 'YYYY-MM-DD HH24:MI:SS.FF9') ", columnName);
            }

            @Override
            String varchar2Date(String columnName) {
                return String.format(" TO_DATE(%s,'YYYY/MM/DD') ", columnName);
            }
        }, //
        DB2 {
            @Override
            String date2Varchar(String columnName) {
                return String.format(" VARCHAR_FORMAT(%s, 'YYYY/MM/DD') ", columnName);
            }

            @Override
            String timestamp2Varchar(String columnName) {
                return String.format(" VARCHAR_FORMAT(%s, 'YYYY-MM-DD HH24:MI:SS.NNNNNN') ", columnName);
            }

            @Override
            String varchar2Timestamp(String columnName) {
                return String.format(" TO_TIMESTAMP(%s,'YYYY-MM-DD HH24:MI:SS.NNNNNN') ", columnName);
            }

            @Override
            String varchar2Date(String columnName) {
                return String.format(" TO_DATE(%s,'YYYY/MM/DD') ", columnName);
            }
        },//
        ;

        abstract String date2Varchar(String columnName);

        abstract String timestamp2Varchar(String columnName);

        abstract String varchar2Timestamp(String columnName);

        abstract String varchar2Date(String columnName);
    }
}
