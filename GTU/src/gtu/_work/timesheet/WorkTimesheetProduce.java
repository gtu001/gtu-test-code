package gtu._work.timesheet;

import gtu.date.DateUtil;
import gtu.number.RandomUtil;
import gtu.number.RandomUtilByPercent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 工時表 timesheet 產生器
 * @author gtu001
 */
public class WorkTimesheetProduce {
    
    private static SimpleDateFormat BASE_SDF = new SimpleDateFormat("yyyyMMdd");
    private final static int BUFFER_DAY = 3;//超時緩衝日數
    private final static String END_TIME = "1800";//下班基準時間
    private RandomUtilByPercent<Float> percentUtil;
    private List<Float> overtimeList = new ArrayList<Float>();
    private Date startDate;
    private Date endDate;

    private Map<String,String> specialHoildayMap = new HashMap<String,String>();//特別假日
    private Map<String,String> specialWorkingDayMap = new HashMap<String,String>();//特別工作天
    private List<Date> workingDateList = new ArrayList<Date>();//工作天
    private List<Date> fullDateList = new ArrayList<Date>();//全部天
    private float overtimeNumber;//總加班時數
   
    public static void main(String[] args) throws ParseException {
        WorkTimesheetProduce test = new WorkTimesheetProduce();
        
        Map<String,String> specialHoildayMap = new HashMap<String,String>();
        specialHoildayMap.put("20150218", "春節");
        specialHoildayMap.put("20150219", "春節");
        specialHoildayMap.put("20150220", "春節");
        specialHoildayMap.put("20150221", "春節");
        specialHoildayMap.put("20150222", "春節");
        specialHoildayMap.put("20150223", "春節");
        specialHoildayMap.put("20150227", "228連假");
        Map<String,String> specialWorkingDayMap = new HashMap<String,String>();
        specialWorkingDayMap.put("20141227", "元旦補上班");
        
        Map<Float, Integer> overtimeRangeMap = new HashMap<Float, Integer>();
        overtimeRangeMap.put(0f, 40);
        overtimeRangeMap.put(1f, 40);
        overtimeRangeMap.put(2f, 20);
        
        test.beginEnd("20150301", null);
        test.specialHoliday(specialHoildayMap);
        test.specialWorkingDay(specialWorkingDayMap);
        test.setOvertimeHours(25.0f);
        test.overtimeRangeMap(overtimeRangeMap);
        
        test.caculateWorkrange();
        test.caculateWorkhours();
        test.finalExecute();
        
        System.out.println("done...");
    }
    
    
    /**
     * 計算工作天範圍
     */
    private void caculateWorkrange(){
        for(Date tempStartD = startDate; endDate.after(tempStartD);){
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(tempStartD);
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            if(!isHoliday(cal2)){
                workingDateList.add(cal2.getTime());
            }
            fullDateList.add(cal2.getTime());
            cal2.add(Calendar.DATE, 1);
            tempStartD = cal2.getTime();
        }
        System.out.println("上班天數 : " + workingDateList.size());
        for(Date d : workingDateList){
//            System.out.println("work -> " + DateFormatUtils.format(d, "yyyy/MM/dd"));
        }
    }
    
    /**
     * 案機率計算分配加班時間
     */
    private void caculateWorkhours(){
        float overtimeNumber_backup = overtimeNumber;
        for (; overtimeList.size() < workingDateList.size();) {
            //一般按百分比分配
            float overtime = percentUtil.nextRandomKey();
            if (overtimeNumber > overtime) {
                overtimeNumber -= overtime;
            } else {
                overtime = overtimeNumber;
                overtimeNumber = 0f;
            }
            //超量用buffer天數強制平均分配
            if(overtimeList.size() + BUFFER_DAY == workingDateList.size()){
                float bufferHours = overtimeNumber + overtime;
                float[] bufferHoursArray = new float[BUFFER_DAY];
                for(int ii = 0; bufferHours != 0f; ii++){
                    if(ii >= BUFFER_DAY){
                        ii = 0;
                    }
                    if(bufferHours > 1f){
                        bufferHoursArray[ii] += 1f;
                        bufferHours -= 1f;
                    }else{
                        bufferHoursArray[ii] += bufferHours;
                        bufferHours = 0f;
                    }
                }
                for(int ii = 0 ; ii < bufferHoursArray.length ; ii ++){
                    overtimeList.add(bufferHoursArray[ii]);
                    System.out.println("buffer分配時數-->" + bufferHoursArray[ii]);
                }
                break;
            }
            overtimeList.add(overtime);
        }
        overtimeList = RandomUtil.randomList(overtimeList);
        float validateHoursCount = 0f;
        for(int ii = 0 ; ii < overtimeList.size() ; ii ++){
            validateHoursCount += overtimeList.get(ii);
        }
        Validate.isTrue(validateHoursCount == overtimeNumber_backup, "時間分配總時數錯誤 : " + validateHoursCount);
    }
    
    private void printResult(Date date, Calendar endTime, String dateType, float overtimeHours){
        String month = DateFormatUtils.format(date, "MM");//月份
        String day = DateFormatUtils.format(date, "yyyy/MM/dd");//日期
        String weekDay = DateUtil.getDayOfTheWeek(date);//星期幾
        String startTime = "08:50";//上班時間
        dateType = StringUtils.defaultString(dateType);
        float workingHours = overtimeHours + 9;//上班時數含午休
//        月份  日期  星期  上班時間    下班時間    上班日 上班時數含午休 備註  請假時數    加班時數
        if(endTime != null){
            String endTimeStr = DateFormatUtils.format(endTime, "HH:mm");//下班時間
            System.out.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", month, day, weekDay, startTime, endTimeStr, "V", workingHours, dateType, "", overtimeHours);
        }else{
            System.out.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n", month, day, weekDay, "", "", "", "", dateType, "", "");
        }
    }
    
    private void finalExecute(){
        System.out.println("1.工時表↓↓↓↓↓↓=======================================================");
        for(int ii = 0 ; ii < fullDateList.size() ; ii ++){
            Date allDate = fullDateList.get(ii);
            String dateKey = BASE_SDF.format(allDate.getTime());
            String dateType = "";
            if(workingDateList.indexOf(allDate) == -1){
                dateType = "周末";
                if(specialHoildayMap.containsKey(dateKey)){
                    dateType = specialHoildayMap.get(dateKey);
                }
                printResult(allDate, null, dateType, -1);//假日或國定假日
                continue;
            }
            if(specialWorkingDayMap.containsKey(dateKey)){
                dateType = specialWorkingDayMap.get(dateKey);
            }
            int pos = workingDateList.indexOf(allDate);
            allDate = workingDateList.get(pos);
            int overtime = (int)(overtimeList.get(pos) * 60);
            Calendar tempCal = Calendar.getInstance();
            tempCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(END_TIME.substring(0,2)));
            tempCal.set(Calendar.MINUTE, Integer.parseInt(END_TIME.substring(2,4)));
            tempCal.set(Calendar.SECOND, 0);
            tempCal.add(Calendar.MINUTE, overtime);
            float overtimeHours = overtimeList.get(pos);//加班時數
            printResult(allDate, tempCal, dateType, overtimeHours);//一般上班日
        }
    }
    
    private void setOvertimeHours(float overtimeNumber) {
        this.overtimeNumber = overtimeNumber;
    }

    private void specialWorkingDay(Map<String, String> specialWorkingDayMap) {
        this.specialWorkingDayMap = specialWorkingDayMap;
    }

    private void specialHoliday(Map<String, String> specialHoildayMap) {
        this.specialHoildayMap = specialHoildayMap;
    }
    
    private void overtimeRangeMap(Map<Float, Integer> overtimeRangeMap){
        percentUtil = new RandomUtilByPercent<Float>(overtimeRangeMap);
    }
    
    /**
     * 計算開始結束日
     */
    private void beginEnd(String begin, String end) throws ParseException{
        startDate = BASE_SDF.parse(begin);
        endDate = null;
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.YEAR, Integer.parseInt(begin.substring(0,4)));
        if(StringUtils.isBlank(end)){
            endCal.set(Calendar.MONTH, startDate.getMonth() + 1);
            endCal.set(Calendar.DATE, 1);
            endCal.add(Calendar.DATE, -1);
        }else{
            Date tempEndDate = BASE_SDF.parse(end);
            endCal.set(Calendar.MONTH, tempEndDate.getMonth());
            endCal.set(Calendar.DATE, tempEndDate.getDate());
        }
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 55);
        endDate = endCal.getTime();
        System.out.println("開始:" + BASE_SDF.format(startDate));
        System.out.println("結束:" + BASE_SDF.format(endDate));
    }

    /**
     * 六日 , 且非特別加班日
     */
    private boolean isHoliday(Calendar cal){
        if(specialWorkingDayMap.containsKey(BASE_SDF.format(cal.getTime()))){
            return false;
        }
        if(specialHoildayMap.containsKey(BASE_SDF.format(cal.getTime()))){
            return true;
        }
        if(cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7){
            return true;
        }
        return false;
    }
}
