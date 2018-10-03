package gtu.db.jdbc.util;

public class DBDateUtil {

    public static void main(String[] args) {
    }

    public enum DBDateFormat {
        Oracle {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY/MM/DD') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY-MM-DD HH24:MI:SS.FF9') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" TO_TIMESTAMP(%s, 'YYYY-MM-DD HH24:MI:SS.FF9') ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" TO_DATE(%s,'YYYY/MM/DD') ", columnName);
            }
        }, //
        DB2 {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" VARCHAR_FORMAT(%s, 'YYYY/MM/DD') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" VARCHAR_FORMAT(%s, 'YYYY-MM-DD HH24:MI:SS.NNNNNN') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" TO_TIMESTAMP(%s,'YYYY-MM-DD HH24:MI:SS.NNNNNN') ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" TO_DATE(%s,'YYYY/MM/DD') ", columnName);
            }
        }, //
        Postgres {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY-MM-DD') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" TO_CHAR(%s, 'YYYY-MM-DD HH24:MI:SS.MS') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" TO_TIMESTAMP(%s,'YYYY-MM-DD HH24:MI:SS.MS') ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" to_date(%s, 'YYYY/MM/DD') ", columnName);
            }
        },//
        DERBY {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" DATE(%s) ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" TIMESTAMP(%s) ", columnName);
            }
            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" TIMESTAMP(%s) ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" DATE(%s) ", columnName);
            }
        },//
        ;

        public abstract String date2Varchar(String columnName);

        public abstract String timestamp2Varchar(String columnName);

        public abstract String varchar2Timestamp(String columnName);

        public abstract String varchar2Date(String columnName);
    }
}
