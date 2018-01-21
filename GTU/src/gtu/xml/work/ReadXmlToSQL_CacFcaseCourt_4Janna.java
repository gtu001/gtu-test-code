package gtu.xml.work;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadXmlToSQL_CacFcaseCourt_4Janna extends BaseReadXmlToSQL implements DB4Janna {

    public static void main(String[] args) {
        new ReadXmlToSQL_CacFcaseCourt_4Janna().execute("XXXXXXX", new File("C:/Users/gtu001/Desktop/cac_fcase.xml"),
                "cac_fcase.sql");
        System.out.println("done...");
    }

    public void execute(String dmvValue, File srcFile, String fileName) {
        try {
            this.tableName = "cac_fcase_court";
            this.dmvValue = dmvValue;
            File outputFile = this.getDestFile(dmvValue, srcFile, fileName);
            this.processSql(srcFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected Map<String, String> afterHandleRow(Map<String, String> newRows) {
        Map<String, String> newRows2 = new LinkedHashMap<String, String>();
        newRows2.put("dmv", dmvValue);
        if(!newRows2.containsKey("court_sn")){
            newRows2.put("court_sn", "1");
        }
        newRows2.putAll(newRows);
        return newRows2;
    }

    static {
        destColumns = new String[] { "dmv", "fcase_no", "court_sn", "unit_court", "court_date", "court_class",
                "court_result", "court_accept_date", "create_uid", "create_dt", "update_uid", "update_dt" };
        
        intColumns = new String[] { "court_sn" };
        
        booleanColumns = new String[] {};

        map.put("CourtDate", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getDateValue(value);
            }
        });
    }
}