package _temp.mulan.ex0;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;

public class MulanWorkTest001 {

    public static void main(String[] args) {
        ExcelUtil_Xls97 util = ExcelUtil_Xls97.getInstance();

        HSSFWorkbook wb = util.readExcel(new File("/home/gtu001/Desktop/mulan_work1.xls"));

        MulanWorkTest001 t = new MulanWorkTest001();

        List<PersonInfo> personLst = new ArrayList<PersonInfo>();
        List<WorkData> workLst = new ArrayList<WorkData>();
        {
            HSSFSheet sheet2 = wb.getSheetAt(1);
            for (int ii = 0; ii <= sheet2.getLastRowNum(); ii++) {
                Row row = sheet2.getRow(ii);
                if (row == null) {
                    continue;
                }

                PersonInfo p = t.new PersonInfo();
                int jj = 0;
                p.personId = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.workType = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.group = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.name = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.firstWork = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.secondWork = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.restDay1 = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.restDay2 = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.cardNum = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.workTime = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.exportDate = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                System.out.println(p);
                personLst.add(p);
            }
        }

        {
            HSSFSheet sheet2 = wb.getSheetAt(0);
            for (int ii = 0; ii <= sheet2.getLastRowNum(); ii++) {
                Row row = sheet2.getRow(ii);
                if (row == null) {
                    continue;
                }

                WorkData p = t.new WorkData();
                int jj = 0;
                p.personNum = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.name = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.workDate = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.regisTime = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.cardType = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.workType = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                p.groupType = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj++));
                System.out.println(p);
                workLst.add(p);
            }
        }

        // ========================================================================================
        // ========================================================================================
        Set<String> personNumLst = new LinkedHashSet<String>();
        Set<String> workDateLst = new TreeSet<String>();
        Map<String, String> personNumNameMap = new HashMap<String, String>();
        for (WorkData p : workLst) {
            personNumLst.add(p.personNum);
            workDateLst.add(p.workDate);
            personNumNameMap.put(p.personNum, p.name);
        }

        List<WorkDataHandler> disWorkLst = new ArrayList<WorkDataHandler>();
        for (WorkData p : workLst) {
            WorkDataHandler w1 = new WorkDataHandler(p);
            int pos = disWorkLst.indexOf(w1);
            if (pos != -1) {
                w1 = disWorkLst.get(pos);
            } else {
                disWorkLst.add(w1);
            }
            w1.dayLst.add(p);
        }

        Map<String, Map<String, List<WorkData>>> personNum_workDate_DayLst_MAP = new HashMap<String, Map<String, List<WorkData>>>();

        for (String personNum : personNumLst) {
            Map<String, List<WorkData>> innerMap = new HashMap<String, List<WorkData>>();
            personNum_workDate_DayLst_MAP.put(personNum, innerMap);
        }

        for (WorkDataHandler p : disWorkLst) {
            Map<String, List<WorkData>> innerMap = personNum_workDate_DayLst_MAP.get(p.personNum);
            innerMap.put(p.workDate, p.dayLst);
        }

        HSSFWorkbook wb2 = new HSSFWorkbook();
        HSSFSheet workSheet = wb2.createSheet();

        int rowPos = 0;
        HSSFRow titleRow = workSheet.createRow(rowPos);

        int titleRowPos = 0;
        titleRow.createCell(titleRowPos++).setCellValue("工號");

        Map<Integer, String> workDateMap = new HashMap<Integer, String>();
        for (String workDate : workDateLst) {
            titleRow.createCell(titleRowPos).setCellValue(workDate);
            workDateMap.put(titleRowPos, workDate);
            titleRowPos++;
        }

        int hasCount = 0;
        int noCount = 0;

        for (String personNum : personNumLst) {
            rowPos++;
            HSSFRow personRow = workSheet.createRow(rowPos);

            String personName = StringUtils.trimToEmpty(personNumNameMap.get(personNum));
            personRow.createCell(0).setCellValue(personNum + personName);

            Map<String, List<WorkData>> innerMap = personNum_workDate_DayLst_MAP.get(personNum);

            for (int cellPos : workDateMap.keySet()) {
                String workDate = workDateMap.get(cellPos);
                List<WorkData> wLst = innerMap.get(workDate);

                StringBuffer sb = new StringBuffer();
                if (wLst != null) {
                    hasCount++;
                    for (WorkData w : wLst) {
                        sb.append(w.cardType + w.regisTime + "\r\n");
                    }
                } else {
                    noCount++;
                }
                Cell cell = util.getCellChk(personRow, cellPos);
                cell.setCellValue(sb.toString());
            }
        }

        util.applyAutoHeight(workSheet, wb2);

        File excelFile = new File(FileUtil.DESKTOP_DIR, "xxxxxxxxx.xls");
        util.writeExcel(excelFile, wb2);
        System.out.println("File : " + excelFile);
        System.out.println("hasCount : " + hasCount);
        System.out.println("noCount : " + noCount);
    }

    private static class WorkDataHandler {
        String personNum;
        String workDate;
        List<WorkData> dayLst = new ArrayList<WorkData>();

        WorkDataHandler(WorkData w) {
            personNum = w.personNum;
            workDate = w.workDate;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((personNum == null) ? 0 : personNum.hashCode());
            result = prime * result + ((workDate == null) ? 0 : workDate.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            WorkDataHandler other = (WorkDataHandler) obj;
            if (personNum == null) {
                if (other.personNum != null)
                    return false;
            } else if (!personNum.equals(other.personNum))
                return false;
            if (workDate == null) {
                if (other.workDate != null)
                    return false;
            } else if (!workDate.equals(other.workDate))
                return false;
            return true;
        }
    }

    private class WorkData {
        String personNum;
        String name;
        String workDate;
        String regisTime;
        String cardType;
        String workType;
        String groupType;

        @Override
        public String toString() {
            return "WorkData [personNum=" + personNum + ", name=" + name + ", workDate=" + workDate + ", regisTime=" + regisTime + ", cardType=" + cardType + ", workType=" + workType + ", groupType="
                    + groupType + "]";
        }
    }

    private class PersonInfo {
        String personId;
        String workType;
        String group;
        String name;
        String firstWork;
        String secondWork;
        String restDay1;
        String restDay2;
        String cardNum;
        String workTime;
        String exportDate;

        @Override
        public String toString() {
            return "PersonInfo [personId=" + personId + ", workType=" + workType + ", group=" + group + ", name=" + name + ", firstWork=" + firstWork + ", secondWork=" + secondWork + ", restDay1="
                    + restDay1 + ", restDay2=" + restDay2 + ", cardNum=" + cardNum + ", workTime=" + workTime + ", exportDate=" + exportDate + "]";
        }
    }
}
