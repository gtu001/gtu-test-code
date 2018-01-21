package gtu.xml.work;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadXmlToSQL_CacFcase_4Janna extends BaseReadXmlToSQL implements DB4Janna {

    public static void main(String[] args) {
        new ReadXmlToSQL_CacFcase_4Janna().execute("XXXXXXX", new File("C:/Users/gtu001/Desktop/cac_fcase.xml"),
                "cac_fcase.sql");
        System.out.println("done...");
    }

    public void execute(String dmvValue, File srcFile, String fileName) {
        try {
            this.tableName = "cac_fcase";
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
        newRows2.putAll(newRows);
        return newRows2;
    }

    static {
        destColumns = new String[] { "dmv", "fcase_no", "ecase_no", "applier", "applier_class", "rela_code",
                "doc_name", "doc_no", "appl_date", "appl_year", "appl_month", "accept_date", "accept_no",
                "fcase_analysis", "fcase_analysis1", "fcase_analysis2", "fcase_analysis3", "fcase_analysis4",
                "fcase_analysis5", "adj_what", "adj_result", "adj_result_y_n", "adj_extra", "status_code",
                "status_memo", "close_date", "adj_sn", "reex_date", "reex_class", "reex_result", "lock_user",
                "lock_time", "n_psn", "n_wit", "n_relate", "days_take", "fact_code", "type_code", "wpg_rec_time",
                "applier_addr", "applier_zip_code", "applier_tel", "applier_email", "under_court", "peaced", "purpose",
                "police_retrieval_date", "police_retrieval_doc_no", "mass_queryed_flag", "no_adj_result",
                "reex_search_date", "reex_search_return_date", "reex_accept_date", "reex_fcase_no", "new_case_flag",
                "exam_result", "country_loss", "road_loss", "police_supply_pic", "road_office_improve", "create_uid",
                "create_dt", "update_uid", "update_dt" };

        intColumns = new String[] { "ecase_no", "adj_sn", "n_psn", "n_wit", "n_relate", "days_take" };

        booleanColumns = new String[] { "adj_result_y_n", "under_court", "peaced", "country_loss", "road_loss",
                "police_supply_pic", "road_office_improve" };

        map.put("ApplDate", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getDateValue(value);
            }
        });
        map.put("AcceptDate", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getDateValue(value);
            }
        });
        map.put("CloseDate", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getDateValue(value);
            }
        });
        map.put("ReexDate", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getDateValue(value);
            }
        });
        map.put("AdjResultYN", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("UnderCourt", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("Peaced", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("StatusCode", new Logic() {
            @Override
            public String getValue(String value) {
                if ("(null)".equals(value)) {
                    return null;
                }
                if ("2".equals(value)) {
                    return "58";
                } else if ("10".equals(value) || "20".equals(value) || "30".equals(value)) {
                    return "10";
                }
                return "0";
            }
        });
    }
}