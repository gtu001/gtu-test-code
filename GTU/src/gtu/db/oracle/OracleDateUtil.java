package gtu.db.oracle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OracleDateUtil {
    
    private static OracleDateUtil instance;
    
    public static OracleDateUtil getInstance() {
        if(instance == null) {
            instance = new OracleDateUtil();
        }
        return instance;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(OracleDateUtil.getInstance().getFullDateSql(new Date()));
        System.out.println(OracleDateUtil.getInstance().getDateDeff(-10));
        System.out.println(OracleDateUtil.getInstance().getDateDeff(-10,-10));
    }
    
    
    /**
     * 取得加減現在的日期
     * @param day
     * @return
     */
    private String getDateDeff(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, day);
        return String.format(" to_date('%04d-%02d-%02d', 'yyyy.mm.dd') "
                , cal.get(Calendar.YEAR)
                , cal.get(Calendar.MONTH) + 1
                , cal.get(Calendar.DAY_OF_MONTH));
    }
    
    private String getFullDateSql(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return String.format(" to_date('%s', 'yyyy.mm.dd.HH24.MI.SS') "
                ,sdf.format(date));
    }
    
    private String getFullDateSql_toMilliseconds(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
        return String.format(" to_timestamp('%s', 'yyyy.mm.dd.HH24.MI.SS.FF') "
                ,sdf.format(date));
    }
    
    private String getFullDateSql(boolean isEasy, Date date) {
        String dformat = "";
        String format = "";
        switch(isEasy ? 0 : 1) {
        case 0:
            dformat = "yyyy-MM-dd";
            format = "yyyy.mm.dd";
            break;
        default:
            dformat = "yyyy-MM-dd-HH-mm-ss";
            format = "yyyy.mm.dd.HH24.MI.SS";
            break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dformat);
        return String.format(" to_date('%s', '%s') " ,sdf.format(date), format);
    }
    
    private String getDateDeff(int day, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, day);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        return String.format(" to_date('%s', 'yyyy.mm.dd.HH24') "
                ,sdf.format(cal.getTime()));
    }
}
