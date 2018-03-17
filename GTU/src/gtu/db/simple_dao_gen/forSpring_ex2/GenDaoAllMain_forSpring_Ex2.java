package gtu.db.simple_dao_gen.forSpring_ex2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import gtu.db.simple_dao_gen.GenDaoBean;
import gtu.db.simple_dao_gen.GenDaoSchemaInterface;
import gtu.string.StringUtilForDb;

public class GenDaoAllMain_forSpring_Ex2 {

    public String execute(String tableName, List<String> colList, List<String> pkList, Connection conn) throws SQLException {
        colList = toUpperCase(colList);
        pkList = toUpperCase(pkList);
        String className = StringUtils.capitalize(StringUtilForDb.dbFieldToJava(tableName));
        
        StringBuilder sb = new StringBuilder();

        sb.append(new GenDaoBean().execute("select * from " + tableName + " where 1!=1", tableName, conn));
        
        sb.append("public class " + className + "Dao {\n");
        
        sb.append(new GenDaoSchemaInterface().execute(tableName, colList));
        
        //自訂內容
        sb.append("\n");
        sb.append("\t@Autowired\n");
        sb.append("\tprivate JdbcTemplate template;\n");
        sb.append("\n");
        
        sb.append(new GenDaoInsertMethod_forSpring().execute(tableName, colList));
        sb.append(new GenDaoDeleteMethod_forSpring().execute(tableName, pkList));
        sb.append(new GenDaoUpdateMethod_forSpring().execute(tableName, colList, pkList));
        sb.append(new GenDaoSelectMethod_forSpring().execute(tableName, pkList));
        sb.append("}\n");
        
        return sb.toString();
    }

    private List<String> toUpperCase(List<String> colList) {
        for (int ii = 0; ii < colList.size(); ii++) {
            String col = colList.get(ii).toUpperCase();
            colList.set(ii, col);
        }
        return colList;
    }

    public List<String> getColumnList(String tableName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from " + tableName + " where 1!=1 ");
        ResultSetMetaData metaData = stmt.getMetaData();
        List<String> colList = new ArrayList<String>();
        for (int ii = 1; ii <= metaData.getColumnCount(); ii++) {
            String columnLabel = metaData.getColumnLabel(ii);
            colList.add(columnLabel);
        }
        JdbcUtils.closeConnection(conn);
        return colList;
    }
}
