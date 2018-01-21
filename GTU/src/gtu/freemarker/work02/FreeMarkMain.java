package gtu.freemarker.work02;

import gtu.db.DbConstant;
import gtu.db.JdbcDBUtil;
import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.string.StringUtilForDb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

public class FreeMarkMain {

    public static void main(String[] args) throws Exception {
        FreeMarkMain main = new FreeMarkMain();

        Map<String, Object> root = main.getRoot();

        String file_voName = root.get("voName") + ".java";
        String file_mapper_xml = root.get("mapperName") + ".xml";
        String file_jsp = root.get("actionName") + ".jsp";
        String file_action_java = root.get("actionName") + ".java";
        String file_dao_java = root.get("mapperName") + ".java";

        File processDir = new File(FileUtil.DESKTOP_DIR, (String) root.get("taskId"));
        processDir.mkdir();

        // 產vo
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("MyBatisVo.txt").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, file_voName)));
        }

        // 產mapper xml
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("MyBatisMapper.xml").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, file_mapper_xml)));
        }

        // 產jsp
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("HpMain.jsp").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, file_jsp)));
        }

        // 產action java
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("HpActionJava.txt").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, file_action_java)));
        }

        // 產mapper dao java
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("MyBatisMapperJava.txt").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, file_dao_java)));
        }

        // mybatis config
        {
            String voStr = FileUtil.loadFromStream(FreeMarkMain.class.getResource("MyBatisConfig.txt").openStream(), "utf8");
            FreeMarkerSimpleUtil.replace(voStr, root, new FileOutputStream(new File(processDir, "mybatis-config.txt")));
        }

        System.out.println("done.........");
    }

    /**
     * 設定都在這裡 TODO
     */
    private Map<String, Object> getRoot() throws Exception {
        
        // 要手動設定
        String tableName = "PTR_GROUP_CODE";// TODO 要設定
        String jspTitle = "PTRM0190團代參數維護";// TODO 要設定
        String taskGroup = "ptr"; // TODO 要設定
        String actionName = "PtrM0190";// TODO 要設定
        // 自動產生
        String baseName = StringUtilForDb.dbFieldToJava(tableName);
        String voName = StringUtils.capitalise(baseName) + "Vo";
        String mapperName = StringUtils.capitalise(baseName) + "Mapper";
        String voNameLower = baseName + "Vo";
        String mapperNameLower = baseName + "Mapper";
        String taskId = actionName.toLowerCase();

         List<Map<String, Object>> list = queryTableConfig(tableName);
//        List<Map<String, Object>> list = queryTableConfig_forTest();

        for (Map<String, Object> map : list) {
            String dataType = (String) map.get("DATA_TYPE");
            map.put("JAVA_TYPE", DataType.lookup(dataType));
            if(map.containsKey("COMMENTS") && map.get("COMMENTS")!=null){
                String columnChs = (String)map.get("COMMENTS");
                map.put("columnChs", columnChs);
            }
        }

        // 要手動設定 TODO
        setColumnChs("GROUP_CODE", "團體代號", list);// 設定欄位中文
        setColumnChs("SOURCE_ABBR_CODE", "來源代號前兩碼", list);// 設定欄位中文
        setColumnChs("GROUP_NAME", "中文名稱", list);// 設定欄位中文
        setColumnChs("MOD_USER", "異動使用者", list);// 設定欄位中文
        setColumnChs("MOD_TIME", "異動時間", list);// 設定欄位中文
        setColumnChs("MOD_PGM", "異動程式", list);// 設定欄位中文
        setColumnChs("MOD_SEQNO", "異動註記", list);// 設定欄位中文

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("list", list);// 欄位定義

        root.put("taskId", taskId);// 功能代碼
        root.put("taskGroup", taskGroup);// package群組
        root.put("actionName", actionName);// action class 名
        root.put("jspTitle", jspTitle);// jsp標題
        root.put("tableName", tableName);// db table名
        root.put("voName", voName);// VO名稱
        root.put("mapperName", mapperName);// Mapper黨名稱
        root.put("voNameLower", voNameLower);// VO名稱 (第一字小寫)
        root.put("mapperNameLower", mapperNameLower);// Mapper黨名稱 (第一字小寫)
        System.out.println("ROOT ==> " + root);
        return root;
    }

    private void setColumnChs(String columnName, String clumnChs, List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            if (((String) map.get("COLUMN_NAME")).equalsIgnoreCase(columnName)) {
                map.put("columnChs", clumnChs);
                break;
            }
        }
    }

    private enum DataType {
        CHAR(Character.class), //
        DATE(Date.class), //
        VARCHAR2(String.class), //
        NUMBER(Long.class), //
        ;
        final Class<?> clz;

        DataType(Class<?> clz) {
            this.clz = clz;
        }

        public static String lookup(String val) {
            for (DataType e : DataType.values()) {
                if (e.name().equalsIgnoreCase(val)) {
                    return e.clz.getSimpleName();
                }
            }
            throw new RuntimeException("找不到:" + val);
        }
    }

    private List<Map<String, Object>> queryTableConfig_forTest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(this.getClass().getResource("t_contract_master.txt").openStream());
            List<Map<String, Object>> list = (List<Map<String, Object>>) ois.readObject();
            ois.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException("file error!");
        }
    }

    private List<Map<String, Object>> queryTableConfig(String tableNmae) {
        DataSource ds = DbConstant.getTestDataSource_Oracle();
        StringBuilder sb = new StringBuilder();
        sb.append("  select t.COLUMN_NAME,                                ");
        sb.append("         t.DATA_TYPE,                                  ");
        sb.append("         t.DATA_LENGTH,                                ");
        sb.append("         t.DATA_PRECISION,                             ");
        sb.append("         t.DATA_SCALE,                                 ");
        sb.append("         t.NULLABLE,                                   ");
        sb.append("         d.COMMENTS                                    ");
        //sb.append("    from user_tab_columns t                            ");
        sb.append("    from user_tab_cols t                            ");
        sb.append("    left join user_col_comments d                      ");
        sb.append("      on t.TABLE_NAME = d.TABLE_NAME                   ");
        sb.append("     and t.COLUMN_NAME = d.COLUMN_NAME                 ");
        sb.append("  where t.TABLE_NAME = upper('"+tableNmae+"')          ");
        sb.append("    and t.hidden_column = 'NO'                         ");
        // data_precision = 22, data_scale = 6 --> NUMBER(22,6)
        // sb.append(" select t.TABLE_NAME from user_tables t                           ");

        try {
            return JdbcDBUtil.queryForList(sb.toString(), null, ds.getConnection(), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
