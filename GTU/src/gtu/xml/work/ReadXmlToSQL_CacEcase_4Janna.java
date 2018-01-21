package gtu.xml.work;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ReadXmlToSQL_CacEcase_4Janna extends BaseReadXmlToSQL implements DB4Janna {

    public static void main(String[] args) {
        new ReadXmlToSQL_CacEcase_4Janna().execute("XXXXXXX", new File("C:/Users/gtu001/Desktop/cac_fcase.xml"),
                "cac_fcase.sql");
        System.out.println("done...");
    }

    public void execute(String dmvValue, File srcFile, String fileName) {
        try {
            this.tableName = "cac_ecase";
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
        this.handleEventDatetime(newRows2);
        return newRows2;
    }

    private Map<String, String> handleEventDatetime(Map<String, String> newRows) {
        String eventDate = null;
        String eventTime = null;
        if (newRows.containsKey("event_date")) {
            eventDate = newRows.get("event_date");
            newRows.remove("event_date");
        }
        if (newRows.containsKey("event_time")) {
            eventTime = newRows.get("event_time");
            newRows.remove("event_time");
        }
        if (StringUtils.isNotEmpty(eventDate) && StringUtils.isNotEmpty(eventTime)) {
            try {
                // 字串格式(880709+1130)轉日期時間格式2014-04-29 10:00:00
                String newDate = String.format("%s %s:%s:00", Util.getDateValue(eventDate), //
                        eventTime.substring(0, 2), //
                        eventTime.substring(2, 4) //
                        );
                newRows.put("event_date", newDate);
            } catch (Exception ex) {
                newRows.put("event_date", null);
            }
        } else {
            newRows.put("event_date", null);
        }
        return newRows;
    }

    static {
        destColumns = new String[] { "dmv", "ecase_no", "unit_process", "police_name", "event_date", "event_locate",
                "event_loc_id", "event_locate_x", "event_locate_y", "event_wound", "event_damage", "event_desc",
                "weath", "weat_code", "weat_police_flag", "weat_police", "light_code", "light_police_flag",
                "light_police", "road", "other_cond", "ex_cond", "exam_note", "acc_rep1", "acc_rep2", "acc_rep",
                "trial_doc", "invest_doc", "verify_doc", "police_doc", "civil_doc", "add_doc", "num_doc", "unit_prov",
                "other_doc", "location_type", "road_typ", "event_location_type", "event_locate_street_city",
                "event_locate_street_area", "event_locate_street_road", "event_locate_street_addr",
                "event_locate_route_highway", "event_locate_route_direction", "event_locate_route_km",
                "event_locate_route_m", "create_uid", "create_dt", "update_uid", "update_dt" };

        intColumns = new String[] { "ecase_no", "event_loc_id", "event_locate_x", "event_locate_y", "num_doc",
                "event_locate_route_km", "event_locate_route_m" };

        booleanColumns = new String[] { "acc_rep1", "acc_rep2", "acc_rep", "trial_doc", "invest_doc", "verify_doc",
                "police_doc", "civil_doc", "weat_police_flag", "light_police_flag" };

        map.put("AccRep1", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("AccRep2", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("AccRep", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("TrialDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("InvestDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("VerifyDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("PoliceDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("CivilDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
        map.put("AddDoc", new Logic() {
            @Override
            public String getValue(String value) {
                return Util.getBooleanValue(value);
            }
        });
    }
}