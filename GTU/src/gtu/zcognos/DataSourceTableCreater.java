package gtu.zcognos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceTableCreater {

    static String TABLE;
    static String COLUMN;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("         <querySubject status=\"valid\">                                                                           \n");
        sb.append("             <name locale=\"zh\">%1$s</name>                                                               \n");
        sb.append("             <lastChanged>2012-03-13T17:02:28</lastChanged>                                                  \n");
        sb.append("             <lastChangedBy>匿名</lastChangedBy>                                                             \n");
        sb.append("             <definition>                                                                                    \n");
        sb.append("                 <dbQuery>                                                                               \n");
        sb.append("                     <sources>                                                                       \n");
        sb.append("                         <dataSourceRef>[].[dataSources].[Informix_RIS_12]</dataSourceRef>       \n");
        sb.append("                     </sources>                                                                      \n");
        sb.append("                     <sql type=\"cognos\">                                                             \n");
        sb.append("                         Select                                                                  \n");
        sb.append("                         <column>*</column>from                                                  \n");
        sb.append("                         <table>[Informix_RIS_12].%1$s</table>                               \n");
        sb.append("                     </sql>                                                                          \n");
        sb.append("                     <tableType>table</tableType>                                                    \n");
        sb.append("                 </dbQuery>                                                                              \n");
        sb.append("             </definition>                                                                                   \n");
        sb.append("             <determinants/>                                                                                 \n");
        sb.append("                %2$s                                          ");//COLUMN here
        sb.append("         </querySubject>                                                                                         \n");
        TABLE = sb.toString();

        sb = new StringBuilder();
        sb.append("             <queryItem>                                                                                     \n");
        sb.append("                 <name locale=\"zh\">%1$s</name>                                                      \n");
        sb.append("                 <lastChanged>2012-03-13T17:02:28</lastChanged>                                          \n");
        sb.append("                 <externalName>%1$s</externalName>                                                  \n");
        sb.append("                 <usage>attribute</usage>                                                                \n");
        sb.append("                 <datatype>character</datatype>                                                          \n");
        sb.append("                 <precision>10</precision>                                                               \n");
        sb.append("                 <scale>0</scale>                                                                        \n");
        sb.append("                 <size>22</size>                                                                         \n");
        sb.append("                 <nullable>false</nullable>                                                              \n");
        sb.append("                 <regularAggregate>unsupported</regularAggregate>                                        \n");
        sb.append("                 <semiAggregate>unsupported</semiAggregate>                                              \n");
        sb.append("             </queryItem>                                                                                    \n");
        COLUMN = sb.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DataSourceTableCreater.queryForDb("select * from rscdz0101", "rscdz0101");
    }

    static void queryForDb(String sql, String tableName) {
        try {
            Connection conn = InformixDbConn.getConnection().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            StringBuilder sb = new StringBuilder();
            if (rs.next()) {
                for (int ii = 1; ii <= meta.getColumnCount(); ii++) {
                    sb.append(String.format(COLUMN, meta.getColumnName(ii)));
                }
            }
            System.out.println(String.format(TABLE, tableName, sb));
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
