package _temp.worksheet;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;

public class Worksheet4Fuco {

    public static void main(String[] args) throws Exception {
        Worksheet4Fuco t = new Worksheet4Fuco();

        File file = new File(FileUtil.DESKTOP_DIR, "活頁簿1.xlsx");
        System.out.println(file.exists());
        Workbook wb = ExcelUtil.getInstance().readExcel_xlsx(file);
        Sheet sheet = wb.getSheetAt(0);

        List<WorkInfo> lst = new ArrayList<WorkInfo>();
        Map<Integer, List<WorkInfo>> valMap = new LinkedHashMap<Integer, List<WorkInfo>>();

        Map<Integer, AtomicReference<String>> refMap = new TreeMap<Integer, AtomicReference<String>>();
        for (int ii = 0; ii < sheet.getMergedRegions().size(); ii++) {
            CellRangeAddress range = sheet.getMergedRegions().get(ii);
            AtomicReference<String> ref = new AtomicReference<String>();
            for (int jj = range.getFirstRow(); jj <= range.getLastRow(); jj++) {
                refMap.put(jj, ref);
            }

            for (int jj = range.getFirstRow(); jj <= range.getLastRow(); jj++) {
                Row row = sheet.getRow(jj);
                String cellVal0 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 0));
                if (StringUtils.isNotBlank(cellVal0)) {
                    refMap.get(jj).set(cellVal0);
                }
            }
        }

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                System.out.println("row idx : " + ii + " is null");
                continue;
            }
            String cellVal0 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 0));
            if (!refMap.containsKey(ii)) {
                AtomicReference<String> ref = new AtomicReference<String>();
                ref.set(cellVal0);
                refMap.put(ii, ref);
            }
        }

        for (int ii = 0; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                System.out.println("row idx : " + ii + " is null");
                continue;
            }

            String cellVal0 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 0));
            String cellVal1 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 1));
            String cellVal2 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 2));
            String cellVal3 = ExcelUtil.getInstance().readCell(ExcelUtil.getInstance().getCellChk(row, 3));

            try {
                WorkInfo info = new WorkInfo();
                info.cal = (t.getDate(refMap.get(ii).get()));
                info.project = cellVal1;
                info.item = cellVal2;
                info.hours = cellVal3;
                lst.add(info);
            } catch (Exception ex) {
                System.out.println("err index : " + ii);
                ex.printStackTrace();
                continue;
            }
        }

        Collections.reverse(lst);

        for (int ii = 0; ii < lst.size(); ii++) {
            WorkInfo vo = lst.get(ii);
            int weekOfYear = vo.cal.get(Calendar.WEEK_OF_YEAR);
            List<WorkInfo> valLst = new ArrayList<WorkInfo>();
            if (valMap.containsKey(weekOfYear)) {
                valLst = valMap.get(weekOfYear);
            }
            valLst.add(vo);
            valMap.put(weekOfYear, valLst);
        }

        for (Integer weekOfYear : valMap.keySet()) {
            List<WorkInfo> vlst = valMap.get(weekOfYear);
            List<String>[] vsbArry = new List[7];
            for (WorkInfo v : vlst) {
                int pos = v.cal.get(Calendar.DAY_OF_WEEK) - 1;
                if (vsbArry[pos] == null) {
                    vsbArry[pos] = new ArrayList<String>();
                }
                vsbArry[pos].add(v.item);
            }

            System.out.println("週數 : " + weekOfYear + "-" + t.getWeekRange(vlst));
            System.out.print("上下班時間:09:30~18:30 ");
            System.out.println(StringUtils.join(t.markNumLst(vsbArry[1]), ","));
            System.out.print("上下班時間:09:30~18:30 ");
            System.out.println(StringUtils.join(t.markNumLst(vsbArry[2]), ","));
            System.out.print("上下班時間:09:30~18:30 ");
            System.out.println(StringUtils.join(t.markNumLst(vsbArry[3]), ","));
            System.out.print("上下班時間:09:30~18:30 ");
            System.out.println(StringUtils.join(t.markNumLst(vsbArry[4]), ","));
            System.out.print("上下班時間:09:30~18:30 ");
            System.out.println(StringUtils.join(t.markNumLst(vsbArry[5]), ","));
        }

    }

    private String getWeekRange(List<WorkInfo> lst) {
        long maxVal = Long.MIN_VALUE;
        long minVal = Long.MAX_VALUE;
        for (WorkInfo vo : lst) {
            maxVal = Math.max(vo.cal.getTimeInMillis(), maxVal);
            minVal = Math.min(vo.cal.getTimeInMillis(), minVal);
        }
        return DateFormatUtils.format(minVal, "yyyyMMdd") + "~" + //
                DateFormatUtils.format(maxVal, "yyyyMMdd");
    }

    private List<String> markNumLst(List<String> lst) {
        if (lst == null) {
            lst = new ArrayList<String>();
        }
        List<String> rtnLst = new ArrayList<String>(lst);
        for (int ii = 0; ii < rtnLst.size(); ii++) {
            rtnLst.set(ii, (ii + 1) + "." + rtnLst.get(ii));
        }
        return rtnLst;
    }

    private static class WorkInfo {
        String project;
        Calendar cal;
        String item;
        String hours;
    }

    // 2018/02/02 00:00:00
    private Calendar getDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d1 = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
