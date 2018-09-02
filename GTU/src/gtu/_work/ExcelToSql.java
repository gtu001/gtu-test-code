package gtu._work;

import gtu.collection.MapUtil;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class ExcelToSql {
    
    public static void main(String[] args){
        try {
            File file = new File("G:/20140225敏雄/鶯歌_rlde406w.xls");
           
            ExcelToSql test = new ExcelToSql();
            test.execute(file);

            System.out.println("done...");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void execute(File file){
        try {
            HSSFWorkbook workbook = ExcelUtil.getInstance().readExcel(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            File outputDir = new File(FileUtil.DESKTOP_DIR, "SQL_406");
            if(!outputDir.exists()){
                outputDir.mkdirs();
            }
            fetchColumnMapping(sheet, ColumnDefineRCDF019M.values());
            fetchData("RCDF019M", sheet, ColumnDefineRCDF019M.values(), insert_RCDF019M, new File(outputDir, "insert_RCDF019M.sql"));
            ColumnDefineRCDF019M.Colum1.init();
            fetchColumnMapping(sheet, ColumnDefineRcdf020m_forDel.values());
            fetchData("Rcdf020m", sheet, ColumnDefineRcdf020m_forDel.values(), delete_Rcdf020m, new File(outputDir, "delete_Rcdf020m.sql"));
            ColumnDefineRcdf020m_forDel.Column1.init();
            fetchData("Rcdf020m", sheet, ColumnDefineRcdf020m.values(), insert_Rcdf020m, new File(outputDir, "insert_Rcdf020m.sql"));
            ColumnDefineRcdf020m.Column1.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    //取得對應excel pos
    private void fetchColumnMapping(HSSFSheet sheet, ColumnDefineInterface[] values){
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        HSSFRow row = sheet.getRow(0);
        for (int jj = 0; jj < row.getLastCellNum(); jj++) {
            String columnName = ExcelUtil.getInstance().readHSSFCell(row.getCell(jj));
            columnName = StringUtils.trim(columnName);
            map.put(columnName, jj);
        }
        System.out.println("excel對應欄位直 = " + map);
        for(ColumnDefineInterface c : values){
            if(c.getIndex() == -1 && c.getDefalutVal() == null){
                String realColumn = c.getColumn();
                if(c.getMappingColumn() != null){
                    realColumn = c.getMappingColumn();
                }
                for(String key : map.keySet()){
                    if(StringUtils.trim(key).equalsIgnoreCase(realColumn)){
                        c.setIndex(map.get(key));
                        break;
                    }
                }
                if(c.getIndex() == -1 && c.getDefalutVal() == null){
//                    throw new RuntimeException("找不到對應:" + c);//TODO
                    System.err.println("找不到對應:" + c);
                }
            }
        }
        
        //印出對應結果
        List<String> mappingColumnList = null;
        for(ColumnDefineInterface c : values){
            if(c.getDefalutVal() != null){
                System.out.println(c.getColumn() + "\t----> 常數[" + c.getDefalutVal() + "]");
            }else{
                mappingColumnList = MapUtil.getKeyByValue(c.getIndex(), map);
                String fromColumn = null;
                if(!mappingColumnList.isEmpty()){
                    fromColumn = mappingColumnList.get(0);
                }
                System.out.println(c.getColumn() + "\t---->" + fromColumn + ", pos:" + c.getIndex() + "");
            }
        }
    }
    
    private File fetchData(String tableName, HSSFSheet sheet, ColumnDefineInterface[] values, SqlCreaterInterface sqlCreaterInterface, File outputFile) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
        HSSFRow row = null;
        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            row = sheet.getRow(ii);
            Map<String, String> valuemap = new HashMap<String, String>();
            Map<String, String> keymap = new HashMap<String, String>();
            for(ColumnDefineInterface c : values){
                String key = c.getColumn();
                String value = null;
                if(c.getIndex() != -1){
                    value = ExcelUtil.getInstance().readHSSFCell(row.getCell(c.getIndex()));
                }else if(c.getDefalutVal() instanceof GetDefineData){
                    value = (String)((GetDefineData)c.getDefalutVal()).getValue();
                }else{
                    value = (String)c.getDefalutVal();
                }
                //設定資料 ↓↓↓↓↓
                valuemap.put(key, value);
                if(c.isPk()){
                    keymap.put(key, value);
                }
                //設定資料↑↑↑↑↑ 
            }
            
            StringBuffer sb = new StringBuffer();
            sqlCreaterInterface.processSQL(tableName, valuemap, keymap, sb);
            System.out.println(sb);
            writer.write(sb.toString());
        }
        writer.flush();
        writer.close();
        return outputFile;
    }
    
    SqlCreaterInterface insert_RCDF019M = new SqlCreaterInterface() {
        @Override
        public void processSQL(String tableName, Map<String, String> dataMap, Map<String, String> keyMap,
                StringBuffer sb) {
            ExcelToSql.this.processSQL(tableName, dataMap, keyMap, sb, SqlType.Insert);
        }
    };
    SqlCreaterInterface delete_Rcdf020m = new SqlCreaterInterface() {
        @Override
        public void processSQL(String tableName, Map<String, String> dataMap, Map<String, String> keyMap,
                StringBuffer sb) {
            ExcelToSql.this.processSQL(tableName, dataMap, keyMap, sb, SqlType.Delete);
        }
    };
    SqlCreaterInterface insert_Rcdf020m = new SqlCreaterInterface() {
        @Override
        public void processSQL(String tableName, Map<String, String> dataMap, Map<String, String> keyMap,
                StringBuffer sb) {
            ExcelToSql.this.processSQL(tableName, dataMap, keyMap, sb, SqlType.Insert);
        }
    };

    //產生sql
    private void processSQL(String tableName, Map<String, String> dataMap, Map<String,String> keyMap, StringBuffer sb, SqlType sqlType) {
        String whereCondition = "";
        if (!keyMap.isEmpty()) {
            whereCondition = keyMap.toString();
        } else {
            whereCondition = dataMap.toString();
        }

        whereCondition = whereCondition.replaceAll(",", "' and ");
        whereCondition = whereCondition.replaceAll("=", "='");
        whereCondition = whereCondition.substring(1);
        whereCondition = whereCondition.substring(0, whereCondition.length() - 1);
        whereCondition = whereCondition + "'";

        String selectSQL = "select * from " + tableName + " where " + whereCondition;
        if(sqlType == SqlType.Select){
            sb.append(selectSQL + ";\n");
        }
        String deleteSQL = "delete from " + tableName + " where " + whereCondition;
        if(sqlType == SqlType.Delete){
            sb.append(deleteSQL + ";\n");
        }

        String updateSetStr = dataMap.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length() - 1);
        updateSetStr = updateSetStr + "'";

        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition;
        if(sqlType == SqlType.Update){
            sb.append(updateSQL + ";\n");
        }

        List<String> insertFieldList = new ArrayList<String>();
        List<String> insertValueList = new ArrayList<String>();
        for (String key : dataMap.keySet()) {
            insertFieldList.add(key);
            insertValueList.add(dataMap.get(key));
        }
        String inf = insertFieldList.toString();
        inf = inf.replaceAll(" ", "");
        inf = inf.substring(1, inf.length() - 1);
        String inv = insertValueList.toString();
        inv = inv.replaceAll(" ", "");
        inv = inv.replaceAll(",", "','");
        inv = inv.substring(1, inv.length() - 1);
        inv = "'" + inv + "'";
        String insertSQL = "insert into " + tableName + " (" + inf + ") values (" + inv + ")";
        if(sqlType == SqlType.Insert){
            sb.append(insertSQL + ";\n");
        }
    }
    
    class SeqNoData22 implements GetDefineData {
        int seq = 0;
        @Override
        public Object getValue() {
            return StringUtils.leftPad(String.valueOf(seq++), 22, '0');
        }
        @Override
        public void init() {
            seq = 0;
        }
    }
    
    class SeqNoData7 implements GetDefineData {
        int seq = 0;
        @Override
        public Object getValue() {
            return StringUtils.leftPad(String.valueOf(seq++), 7, '0');
        }
        @Override
        public void init() {
            seq = 0;
        }
    }
    
    private static final String YYYMMDD;
    private static final String HHMMSS;
    static {
        Calendar cal = Calendar.getInstance();
        String yyy = StringUtils.leftPad("" + (cal.get(Calendar.YEAR) - 1911), 3, '0');
        String mm = StringUtils.leftPad("" + (cal.get(Calendar.MONTH) + 1), 2, '0');
        String dd = StringUtils.leftPad("" + (cal.get(Calendar.DATE)), 2, '0');
        String hhmmss = StringUtils.leftPad("" + (cal.get(Calendar.HOUR_OF_DAY) + 1), 2, '0')
                + StringUtils.leftPad("" + (cal.get(Calendar.MINUTE) + 1), 2, '0')
                + StringUtils.leftPad("" + (cal.get(Calendar.SECOND) + 1), 2, '0');
        YYYMMDD = yyy + mm + dd;
        HHMMSS = hhmmss;
    }
    
    static class SysDateData implements GetDefineData {
        @Override
        public Object getValue() {
            return String.valueOf(YYYMMDD);
        }
        @Override
        public void init() {
        }
    }
    
    static class SysTimeData implements GetDefineData {
        @Override
        public Object getValue() {
            return String.valueOf(HHMMSS);
        }
        @Override
        public void init() {
        }
    }
    
    interface SqlCreaterInterface {
        void processSQL(String tableName, Map<String, String> dataMap, Map<String,String> keyMap, StringBuffer sb);
    }
    
    interface GetDefineData{
        Object getValue();
        void init();
    }
    
    enum SqlType {
        Insert, Update, Delete, Select;
    }
    
    interface ColumnDefineInterface {
        public int getIndex();
        public void setIndex(int index);
        public String getColumn();
        public String getMappingColumn();
        public Object getDefalutVal();
        public boolean isPk();
        public void init();
    }
    
    enum ColumnDefineRCDF019M implements ColumnDefineInterface {
        Colum1("SITE_ID", null, null, true),//
        Colum2("AREA_CODE", null, null, false),//
        Colum3("ADMIN_OFFICE_CODE", null, null, false),//
        Colum4("MODIFY_DATE", null, null, true),//
        Colum5("SERIAL_NO", null, new ExcelToSql().new SeqNoData7(), true),//
        Colum6("SEQ_NO", null, new ExcelToSql().new SeqNoData22(), true),//
        Colum7("NEW_VILLAGE","VILLAGE", null, true),//
        Colum8("NEW_NEIGHBOR","NEIGHBOR", null, true),//
        Colum9("NEW_STREET_DOORPLATE", null, null, true),//
        Colum10("NEW_STAND_DOORPLATE", null, null, false),//
        Colum11("NEW_STREET", null, null, false),//
        Colum12("NEW_SECTION", null, null, false),//
        Colum13("NEW_AREA", null, null, false),//
        Colum14("NEW_LANE", null, null, false),//
        Colum15("NEW_ALLEY", null, null, false),//
        Colum16("NEW_DOOR_NO", null, null, false),//
        Colum17("NEW_FLOOR_NO", null, null, false),//
        Colum18("OLD_VILLAGE","VILLAGE", null, false),//
        Colum19("OLD_NEIGHBOR","NEIGHBOR", null, false),//
        Colum20("OLD_STREET_DOORPLATE", null, null, false),//
        Colum21("OLD_STAND_DOORPLATE", null, null, false),//
        Colum22("OLD_STREET", null, null, false),//
        Colum23("OLD_SECTION", null, null, false),//
        Colum24("OLD_AREA", null, null, false),//
        Colum25("OLD_LANE", null, null, false),//
        Colum26("OLD_ALLEY", null, null, false),//
        Colum27("OLD_DOOR_NO", null, null, false),//
        Colum28("OLD_FLOOR_NO", null, null, false),//
        Colum29("MODIFY_BATCH", null,"", false),//
        Colum30("MODIFY_KIND", null,"7", false),//
        Colum31("OLD_REGISTER_DATE", null,"", false),//
        Colum32("OLD_REGISTER_KIND", null,"", false),//
        Colum33("NEW_REGISTER_DATE","MODIFY_DATE", null, false),//
        Colum34("NEW_REGISTER_KIND", null,"7", false),//
        Colum35("OTHERS", null,"", false),//
        Colum36("OLD_AREA_CODE","AREA_CODE", null, false),//
        Colum37("OLD_ADDR_TX_CODE", null,"D", false),//
        Colum38("NEW_ADDR_TX_CODE", null,"A", false),//
        Colum39("NEW_AREA_CODE","AREA_CODE", null, false),//
        Colum40("FROM_SITE_ID","SITE_ID", null, false),//
        Colum41("NOTICE_DATE","MODIFY_DATE", null, false),//
        Colum42("NOTICE_TIME", null,"000000", false),//
        Colum43("OLD_APPLY_SEQ", null,"", false),//
        ;
        final String column;//目的欄位名
        final String mappingColumn;//要轉換的欄位名稱(null則用column)
        final Object defalutVal;//預設值,可使用GetDefineData做特殊預設值處理
        final boolean isPk;//是否PK
        int index = -1;//對應excel的pos,用程式抓Row取得對應index
        ColumnDefineRCDF019M(String column, String mappingColumn, Object defalutVal, boolean isPk) {
            this.column = StringUtils.trim(column);
            this.mappingColumn = StringUtils.trim(mappingColumn);
            this.defalutVal = defalutVal;
            this.isPk = isPk;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public String getColumn() {
            return column;
        }
        public String getMappingColumn() {
            return mappingColumn;
        }
        public Object getDefalutVal() {
            return defalutVal;
        }
        public boolean isPk() {
            return isPk;
        }
        @Override
        public void init() {
            for(ColumnDefineInterface c : values()){
                if(c.getDefalutVal() instanceof GetDefineData){
                    ((GetDefineData)c.getDefalutVal()).init();
                }
            }
        }
    }

    enum ColumnDefineRcdf020m implements ColumnDefineInterface {
        Column1("site_id", null, null, true), //
        Column2("serial_no", null, new ExcelToSql().new SeqNoData22(), false), //
        Column3("seq_no", null, new ExcelToSql().new SeqNoData7(), false), //
        Column4("area_code", null, null, false), //
        Column5("admin_office_code", null, null, false), //
        Column6("village", null, null, true), //
        Column7("neighbor", null, null, true), //
        Column8("street_doorplate", "NEW_STREET_DOORPLATE", null, true), //
        Column9("street", "NEW_STREET", null, false), //
        Column10("section", "NEW_section", null, false), //
        Column11("area", "NEW_area", null, false), //
        Column12("lane", "NEW_lane", null, false), //
        Column13("alley", "NEW_alley", null, false), //
        Column14("door_no", "NEW_door_no", null, false), //
        Column15("floor_no", "NEW_floor_no", null, false), //
        Column16("ori_cs", null, "", false), //
        Column17("ori_x", null, "", false), //
        Column18("ori_y", null, "", false), //
        Column19("x97", null, "", false), //
        Column20("y97", null, "", false), //
        Column21("register_date", "modify_date", null, false), //
        Column22("register_kind", null, "7", false), //
        Column23("reserved_field", null, "", false), //
        Column24("notice_date", null, new SysDateData(), false), //
        Column25("notice_time", null, new SysTimeData(), false), //
        Column26("original_code", "site_id", null, false), //
        Column27("notallowset", null, "", false), //
        Column28("mark", null, "", false), //
        ;
        final String column;//目的欄位名
        final String mappingColumn;//要轉換的欄位名稱(null則用column)
        final Object defalutVal;//預設值,可使用GetDefineData做特殊預設值處理
        final boolean isPk;//是否PK
        int index = -1;//對應excel的pos,用程式抓Row取得對應index
        ColumnDefineRcdf020m(String column, String mappingColumn, Object defalutVal, boolean isPk) {
            this.column = StringUtils.trim(column);
            this.mappingColumn = StringUtils.trim(mappingColumn);
            this.defalutVal = defalutVal;
            this.isPk = isPk;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public String getColumn() {
            return column;
        }
        public String getMappingColumn() {
            return mappingColumn;
        }
        public Object getDefalutVal() {
            return defalutVal;
        }
        public boolean isPk() {
            return isPk;
        }
        @Override
        public void init() {
            for(ColumnDefineInterface c : values()){
                if(c.getDefalutVal() instanceof GetDefineData){
                    ((GetDefineData)c.getDefalutVal()).init();
                }
            }
        }
    }
    
    enum ColumnDefineRcdf020m_forDel implements ColumnDefineInterface {
        Column1("site_id", null, null, true), //
        Column6("village", null, null, true), //
        Column7("neighbor", null, null, true), //
        Column8("street_doorplate", "OLD_STREET_DOORPLATE", null, true), //
        ;
        final String column;//目的欄位名
        final String mappingColumn;//要轉換的欄位名稱(null則用column)
        final Object defalutVal;//預設值,可使用GetDefineData做特殊預設值處理
        final boolean isPk;//是否PK
        int index = -1;//對應excel的pos,用程式抓Row取得對應index
        ColumnDefineRcdf020m_forDel(String column, String mappingColumn, Object defalutVal, boolean isPk) {
            this.column = StringUtils.trim(column);
            this.mappingColumn = StringUtils.trim(mappingColumn);
            this.defalutVal = defalutVal;
            this.isPk = isPk;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public String getColumn() {
            return column;
        }
        public String getMappingColumn() {
            return mappingColumn;
        }
        public Object getDefalutVal() {
            return defalutVal;
        }
        public boolean isPk() {
            return isPk;
        }
        @Override
        public void init() {
            for(ColumnDefineInterface c : values()){
                if(c.getDefalutVal() instanceof GetDefineData){
                    ((GetDefineData)c.getDefalutVal()).init();
                }
            }
        }
    }
}
