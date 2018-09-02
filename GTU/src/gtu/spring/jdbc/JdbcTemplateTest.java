package gtu.spring.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateTest {

    public static void main(String[] args) {
        JdbcTemplate template = new JdbcTemplate();
        template.getQueryTimeout(); //設置其所創建Statement查詢數據所需最大超時時間，默認為0,表示使用底成驅動程式默認設置
        template.getFetchSize();//取得底層ResultSet每次從數據庫反回行數,莫認為0...同上
        template.getMaxRows();//取得底層ResultSet數據庫最大返回行數,默認為零...同上
        template.isIgnoreWarnings();//是否忽略Sql警告訊息,莫認為true,所有的警告被log起來,若為false則拋出SQLWarningException
    }

}
