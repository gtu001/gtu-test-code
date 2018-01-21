package gtu.db.oracle;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


/**
 * 檔案由 A copy 到B table 
 * 檔案對應與轉換的處理
 * @author Troy
 */
public class CopyDbRecord {

    private static final long serialVersionUID = 1L;
    
    
    private static List<CopyRecord> directBtsSiteImportList;
    private static List<CopyRecord> directValueList;
    private static List<CopyRecord> totalList;
    
    private void initFields() {
        if(directBtsSiteImportList != null && directValueList != null && totalList!=null) {
            return;
        }
        
        ProcessValue noProcess = new ProcessValue(this);
        
        //為數值格式
        ProcessValue numberProcess = new ProcessValue(this) {
            @Override
            public Object process(Object fromValue) throws Exception {
                if(fromValue == null) {
                    return null;
                }
                if(fromValue instanceof BigDecimal) {
                    return (BigDecimal)fromValue;
                }
                if(fromValue instanceof String) {
                    Long val = Long.parseLong((String)fromValue);
                    return BigDecimal.valueOf(val);
                }
                return fromValue;
            }
        };
        
        //系統日期
        ProcessValue sysdateProcess = new ProcessValue(this) {
            @Override
            public Object process(Object fromValue) throws Exception {
                return new Date();
            }
        };
        
        List<CopyRecord> list1 = new ArrayList<CopyRecord>();
        List<CopyRecord> list3 = new ArrayList<CopyRecord>();
        
        //AI_CO_SITE 對應欄位
        list1.add(new CopyRecord("SITE_ID           ", "C_SITE_ID            ", noProcess)); //10512
        list1.add(new CopyRecord("C_COUNTY          ", "C_COUNTY             ", noProcess));//City/County     ViewTable : AMS2.V_COUNTY  -全使用國字(台)  (需提供該資料表)
        list1.add(new CopyRecord("C_TOWN            ", "C_TOWN               ", noProcess));//市/鄉/區/鎮 ViewTable : AMS2.V_TOWN (需提供該資料表)
        
        directBtsSiteImportList = list1;
        directValueList = list3;
        
        List<CopyRecord> total = new ArrayList<CopyRecord>();
        total.addAll(list1);
        total.addAll(list3);
        totalList = total;
    }
    
    private static class Copy {
        public void copy(CopyRecord copyRecord, Map fromRecord, Map<String,Object> toRecord) throws Exception {
            try {
                Object val = fromRecord.get(copyRecord.getFromField());
                Object vzl = copyRecord.getProcessValue().process(val);
//                LogH.debug(this, "[copy] " + copyRecord.getFromField() + "->" + copyRecord.getToField() + "\t [trans]" + val + " , " + vzl);
                this.checkLen(copyRecord, vzl);
                toRecord.put(copyRecord.getToField(), vzl);
            }catch(Exception ex) {
//                LogH.debug(this, ex.getMessage());
                throw ex;
            }
        }
        public void setValue(CopyRecord copyRecord, Map<String,Object> toRecord) throws Exception {
            try {
                Object value = copyRecord.getValue();
                Object vzl = copyRecord.getProcessValue().process(value);
//                LogH.debug(this, "[setValue] " + copyRecord.getFromField() + "->" + copyRecord.getToField() + "\t" + vzl);
                this.checkLen(copyRecord, vzl);
                toRecord.put(copyRecord.getToField(), vzl);
            }catch(Exception ex) {
//                LogH.debug(this, ex.getMessage());
                throw ex;
            }
        }
        private void checkLen(CopyRecord copyRecord, Object value) throws Exception {
            if(copyRecord.getLength()!=null && value!=null) {
                int len = copyRecord.getLength();
                if(value instanceof BigDecimal) {
                    BigDecimal v = (BigDecimal)value;
                    int chkLen = v.toString().getBytes().length;
                    if(chkLen>len) {
                        throw new Exception(copyRecord.getToField()+" 長度超過限制["+len+"]:"+chkLen);
                    }
                    return;
                }
                if(value instanceof String) {
                    int chkLen = ((String)value).getBytes().length;
                    if(chkLen>len) {
                        throw new Exception(copyRecord.getToField()+" 長度超過限制["+len+"]:"+chkLen);
                    }
                    return;
                }
            }
        }
    }
    
    private static class CopyRecord {
        private final String fromField;
        private final String toField;
        private ProcessValue processValue;
        private Object value;
        private Integer length;
        
        public CopyRecord setValue(Object value) {
            this.value = value;
            return this;
        }
        
        public CopyRecord(String fromField, String toField, ProcessValue processValue) {
            super();
            if(fromField != null) {
                fromField = fromField.trim();
            }
            if(toField != null) {
                toField = toField.trim();
            }
            this.fromField = fromField;
            this.toField = toField;
            this.processValue = processValue;
        }
        
        public ProcessValue getProcessValue() {
            return processValue;
        }
        public void setProcessValue(ProcessValue processValue) {
            this.processValue = processValue;
        }
        public String getFromField() {
            return fromField;
        }
        public String getToField() {
            return toField;
        }
        public Object getValue() {
            return value;
        }
        public Integer getLength() {
            return length;
        }
        public void setLength(Integer length) {
            this.length = length;
        }
    }
    
    private static class ProcessValue {
        protected CopyDbRecord this1;
        public ProcessValue(CopyDbRecord this_){
            this.this1 = this_;
        }
        public Object process(Object fromValue) throws Exception {
            return fromValue;
        }
        public CopyDbRecord getThis1() {
            return this1;
        }
        public void setThis1(CopyDbRecord this1) {
            this.this1 = this1;
        }
    }
    
    private void initAddLengthProperty(DataSource dataSource) throws SQLException {
        boolean isNeedToInit = false;
        for(CopyRecord rec : totalList) {
            if(rec.getLength() == null) {
                isNeedToInit = true;
                break;
            }
        }
        if(!isNeedToInit) {
            return;
        }
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from AMS2.BTS_SITE_IMPORT@AMSII where 1!=1");
        ResultSetMetaData rsmd = rs.getMetaData();
        for(int column = 1 ; column<=rsmd.getColumnCount() ; column ++) {
            String col = rsmd.getColumnName(column);
            int len = rsmd.getColumnDisplaySize(column);
            for(CopyRecord rec : totalList) {
                if(rec.getToField().equals(col)) {
                    rec.setLength(len);
                }
            }
        }
        rs.close();
    }
    
    private void insertToDb(List<Map<String,Object>> btsSiteImportList, DataSource dataSource) {
        Set<String> data = btsSiteImportList.get(0).keySet();
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO AMS2.BTS_SITE_IMPORT@AMSII (   ");
        sql.append("   BTS_SITE_IMPORT_ID ,                     ");
        for(String d : data) {
            sql.append(d+",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" ) VALUES (                                 ");
        sql.append("   AMS2.BTS_SITE_IMPORT_SEQ.NEXTVAL@AMSII , ");
        for(String d : data) {
            sql.append(":"+d+",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" )                                          ");
        
        NamedParameterJdbcDaoSupport namedSupport = new NamedParameterJdbcDaoSupport();
        namedSupport.setDataSource(dataSource);
        for(Map<String,Object> map : btsSiteImportList) {
            namedSupport.getNamedParameterJdbcTemplate().update(sql.toString(), map);
        }
    }
}
