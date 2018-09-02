package gtu.xml.work;

import org.apache.commons.lang3.StringUtils;


class Util {
    static String getDateValue(String value) {
        if ("(null)".equals(value)) {
            return null;
        }
        try {
            String yyy = null;
            String mm = null;
            String dd = null;
            String date1 = value.replaceAll("[\"\\(\\)]", "");
            date1 = StringUtils.trim(date1);
            if(date1.indexOf("/")!=-1){
                String[] vals = date1.split("/", -1);
                yyy = StringUtils.trim(vals[0]);
                mm = StringUtils.trim(vals[1]);
                dd = StringUtils.trim(vals[2]);
            }else if(date1.indexOf(".")!=-1){
                String[] vals = date1.split(".", -1);
                yyy = StringUtils.trim(vals[0]);
                mm = StringUtils.trim(vals[1]);
                dd = StringUtils.trim(vals[2]);
            }else if(date1.length() == 7){
                yyy = StringUtils.trim(date1.substring(0, 3));
                mm = StringUtils.trim(date1.substring(3, 5));
                dd = StringUtils.trim(date1.substring(5, 7));
            }else if(date1.length() == 6){
                yyy = StringUtils.trim(date1.substring(0, 2));
                mm = StringUtils.trim(date1.substring(2, 4));
                dd = StringUtils.trim(date1.substring(4, 6));
            }
            if(StringUtils.isNotBlank(yyy) && StringUtils.isNotBlank(mm) && StringUtils.isNotBlank(dd)){
                yyy = String.valueOf(Integer.parseInt(yyy) + 1911);
                mm = StringUtils.leftPad(mm, 2, '0');
                dd = StringUtils.leftPad(dd, 2, '0');
                return String.format("%s-%s-%s", yyy, mm, dd);
            }
            return "";
//            throw new RuntimeException("沒有對應!");
        } catch (Exception ex) {
//            throw new RuntimeException("日期轉型錯誤 :" + value + ", " + ex.getMessage() + ", " + ex.getCause(), ex);
            System.out.println("日期轉型錯誤 :" + value + ", " + ex.getMessage() + ", " + ex.getCause());
            return "";
        }
    }

    static String getBooleanValue(String value) {
        if ("(null)".equals(value)) {
            return null;
        }
        return Boolean.toString("1".equals(value));
    }
}