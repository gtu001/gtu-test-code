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

            @Override
            public String sysdate() {
                return " sysdate ";
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

            @Override
            public String sysdate() {
                return " (current date) "; // (current timestamp)
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

            @Override
            public String sysdate() {
                return " NOW() ";
            }
        }, //
        Derby {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" cast(date(%s) as varchar(10)) ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" cast(timestamp(%s) as varchar(23)) ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" TIMESTAMP(%s) ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" DATE(%s) ", columnName);
            }

            @Override
            public String sysdate() {
                return " current_date ";// current_timestamp
            }
        }, //
        SqlServer {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" FORMAT(%s, 'yyyy/MM/dd') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" FORMAT(%s, 'yyyy/MM/dd h:mm:ss.tt') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" convert(datetime2, %s, 25) ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" convert(datetime2, %s, 23) ", columnName);
            }

            @Override
            public String sysdate() {
                return " GETDATE() ";
            }
        },
        Sqlite {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" strftime('%%Y-%%m-%%d', %s / 1000, 'unixepoch') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" strftime('%%Y-%%m-%%d %%H:%%M:%%f', %s / 1000, 'unixepoch') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" datetime(%s) ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" date(%s) ", columnName);
            }

            @Override
            public String sysdate() {
                return " datetime('now', 'localtime') ";
            }
        },
        H2 {
            @Override
            public String date2Varchar(String columnName) {
                return String.format(" parsedatetime(%s, 'yyyy-MM-dd') ", columnName);
            }

            @Override
            public String timestamp2Varchar(String columnName) {
                return String.format(" parsedatetime(%s, 'yyyy-MM-dd hh:mm:ss.SS') ", columnName);
            }

            @Override
            public String varchar2Timestamp(String columnName) {
                return String.format(" parsedatetime(%s, 'yyyy-MM-dd hh:mm:ss.SS') ", columnName);
            }

            @Override
            public String varchar2Date(String columnName) {
                return String.format(" parsedatetime(%s, 'yyyy-MM-dd') ", columnName);
            }

            @Override
            public String sysdate() {
                return " CURRENT_TIMESTAMP() ";
            }
        };

        public abstract String date2Varchar(String columnName);

        public abstract String timestamp2Varchar(String columnName);

        public abstract String varchar2Timestamp(String columnName);

        public abstract String varchar2Date(String columnName);

        public abstract String sysdate();
    }
}
