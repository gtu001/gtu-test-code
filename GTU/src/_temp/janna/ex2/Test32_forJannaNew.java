package _temp.janna.ex2;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import gtu.file.FileUtil;
import gtu.number.RandomUtil;
import gtu.poi.hssf.ExcelPosToXY;
import gtu.poi.hssf.ExcelUtil;

public class Test32_forJannaNew {

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\my_tool\\IISI_Report_For_Janna\\年中人口調整附件.xls");
        HSSFWorkbook wbook = ExcelUtil.getInstance().readExcel(file);

        Test32_forJannaNew t = new Test32_forJannaNew();

        Test32Region region = new Test32Region("B17", "AA19");

        t.execute(wbook, region, 1);
    }

    public void execute(HSSFWorkbook wbook, Test32Region region, int sheetIndex) {
        HSSFSheet sheet = wbook.getSheetAt(sheetIndex);

        String sheetName = sheet.getSheetName();

        List<Data> list2 = readSheet(sheet, region);

        DataTotal rtnData = getSamePos(region.getStart().getY(), 'x', list2);// 1
        fix(rtnData);

        int maxCellIndex = sheet.getRow(1).getLastCellNum();
        for (int ii = region.getStart().getX() + 1; ii < maxCellIndex; ii++) {// 2
            DataTotal rtnData2 = getSamePos(ii, 'y', list2);
            fix(rtnData2);
        }

        summaryX1(list2, region);

        for (Data d : list2) {
            if (d.isLabel == false && isDataOdd(d)) {
                throw new RuntimeException("還有資料不正確 : " + d);
            }
        }

        processToExcel(list2, sheetName);

        System.out.println("done...");
    }

    public static class Test32Region {
        ExcelPosToXY start;
        ExcelPosToXY end;

        public Test32Region(String startLabel, String endLabel) {
            start = new ExcelPosToXY(startLabel);
            end = new ExcelPosToXY(endLabel);
            System.out.println("start -- " + start);
            System.out.println("end -- " + end);
        }

        public ExcelPosToXY getStart() {
            return start;
        }

        public ExcelPosToXY getEnd() {
            return end;
        }
    }

    private void summaryX1(List<Data> list2, Test32Region region) {
        int maxY = -1;
        for (Data d : list2) {
            maxY = Math.max(d.y, maxY);
        }
        DataTotal samePos = getSamePos(region.getStart().getX(), 'y', list2);
        List<Data> list = samePos.list;
        for (Data d : list) {
            if(d.isLabel) {
                continue;
            }
            DataTotal samePos1 = getSamePos(d.y, 'x', list2);
            BigDecimal samePos1Total = sumValue(samePos1.list);
            samePos1.total.value = samePos1Total;
        }
    }

    private BigDecimal sumValue(List<Data> list) {
        BigDecimal total = new BigDecimal(0);
        for (Data d : list) {
            total = total.add(d.value);
        }
        return total;
    }

    private void processToExcel(List<Data> orignList, String sheetName) {
        HSSFWorkbook wbook = new HSSFWorkbook();
        HSSFSheet sheet = wbook.createSheet();
        int maxY = -1;
        for (Data d : orignList) {
            maxY = Math.max(d.y, maxY);
        }
        for (int ii = 0; ii <= maxY; ii++) {
            HSSFRow row = sheet.createRow(ii);
            for (Data d : orignList) {
                if (d.y == ii) {
                    HSSFCell cell = ExcelUtil.getInstance().getCellChk(row, d.x);
                    String value = null;
                    if (d.value != null) {
                        value = String.valueOf(d.value);
                    } else {
                        value = d.orignValue;
                    }
                    ExcelUtil.getInstance().setCellValue(cell, value);
                }
            }
        }
        try {
            ExcelUtil.getInstance().writeExcel(new File(FileUtil.DESKTOP_DIR, sheetName + ".xls"), wbook);
        } catch (Exception ex) {
            try {
                ExcelUtil.getInstance().writeExcel(new File(FileUtil.DESKTOP_DIR, "new_sum_report.xls"), wbook);
            } catch (Exception ex2) {
                throw new RuntimeException(ex);
            }
        }
    }

    private List<Data> removeLabel(List<Data> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            if (list.get(ii).isLabel) {
                list.remove(ii);
                ii--;
            }
        }
        return list;
    }

    private void fix(DataTotal rtnData) {
        List<Data> list = removeLabel(rtnData.list);
        Data total = rtnData.total;
        while (!isAllDone(total, list)) {
            boolean execable = isStrategyExecuable(list);
            if (execable) {
                Strategy.ONE_UP_ONE_DOWN.apply(list, this);
            } else if (isOneOddAndTotalOdd(total, list)) {
                Data oddData = getOddData(list);
                if (oddData == null) {
                    throw new RuntimeException("找不到唯一odd");
                }
                char type = isUpOrDown();
                addValue(total, type, 0.5d);
                addValue(oddData, type, 0.5d);
                total.isFixed = true;
                oddData.isFixed = true;
            } else if (isDataOdd(total) == false && countOdd(list) == 1) {
                Data d = getOddData(list);
                addValue(d, 'd', 0.5d);
                d.isFixed = true;
            } else if (BigDecimal.ZERO.compareTo(getDiffValue(total, list)) != 0) {
                BigDecimal val = getDiffValue(total, list);
                System.out.println("val --- " + val);
                char type = val.doubleValue() > 0 ? 'u' : 'd';
                boolean findOk = false;
                for (Data d : list) {
                    if (d.isFixed == false) {
                        addValue(d, type, 1d);
                        findOk = true;
                        break;
                    }
                }
                if (!findOk) {
                    int pos = RandomUtil.rangeInteger(0, list.size() - 1);
                    Data d = list.get(pos);
                    addValue(d, type, 1d);
                    d.isFixed = true;
                }
            } else {
                System.out.println("countOdd - " + countOdd(list));
                System.out.println("diff - " + getDiffValue(total, list));
                throw new RuntimeException("無法進一步處理!");
            }
            showData(rtnData);
        }
    }

    private int countOdd(List<Data> list) {
        int count = 0;
        for (Data d : list) {
            if (isDataOdd(d)) {
                count++;
            }
        }
        return count;
    }

    private Data getOddData(List<Data> list) {
        for (Data d : list) {
            if (isDataOdd(d)) {
                return d;
            }
        }
        return null;
    }

    private boolean isOneOddAndTotalOdd(Data total, List<Data> list) {
        int count = 0;
        for (Data d : list) {
            if (d.isLabel == false && isDataOdd(d)) {
                count++;
            }
        }
        if (count == 1 && isDataOdd(total)) {
            return true;
        }
        return false;
    }

    private boolean isStrategyExecuable(List<Data> list) {
        try {
            Strategy.get(list, false, this);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private enum Strategy {
        ONE_UP_ONE_DOWN() {
            public void apply(List<Data> list, Test32_forJannaNew t) {
                Object[] chkVal = this.get(list, false, t);
                Pair<Data, Data> chk = (Pair<Data, Data>) chkVal[0];
                t.addValue(chk.getKey(), 'u', 0.5d);
                t.addValue(chk.getValue(), 'd', 0.5d);
                setToFixed(chk.getKey(), chk.getValue());
            }
        },
        TWO_UP_ONE_DOWN() {
            public void apply(List<Data> list, Test32_forJannaNew t) {
                Object[] chkVal = this.get(list, true, t);
                Pair<Data, Data> chk = (Pair<Data, Data>) chkVal[0];
                t.addValue(chk.getKey(), 'u', 0.5d);
                t.addValue(chk.getValue(), 'u', 0.5d);
                t.addValue(((Data) chkVal[1]), 'd', 1d);
                setToFixed(chk.getKey(), chk.getValue(), ((Data) chkVal[1]));
            }
        },
        TWO_DOWN_ONE_UP() {
            public void apply(List<Data> list, Test32_forJannaNew t) {
                Object[] chkVal = this.get(list, true, t);
                Pair<Data, Data> chk = (Pair<Data, Data>) chkVal[0];
                t.addValue(chk.getKey(), 'd', 0.5d);
                t.addValue(chk.getValue(), 'd', 0.5d);
                t.addValue(((Data) chkVal[1]), 'u', 1d);
                setToFixed(chk.getKey(), chk.getValue(), ((Data) chkVal[1]));
            }
        },;

        Strategy() {
        }

        abstract void apply(List<Data> list, Test32_forJannaNew t);

        protected void setToFixed(Data... datas) {
            for (Data d : datas) {
                d.isFixed = true;
            }
        }

        public static Object[] get(List<Data> list, boolean doNormal, Test32_forJannaNew t) {
            List<Data> twoList = new ArrayList<Data>();
            for (Data d : list) {
                if (d.isLabel == false && d.isFixed == false && t.isDataOdd(d)) {
                    twoList.add(d);
                }
                if (twoList.size() >= 2) {
                    break;
                }
            }
            if (twoList.size() < 2) {
                throw new RuntimeException("找不到兩個 odd值");
            }
            Data normal = null;
            if(doNormal) {
                for (Data d : list) {
                    if (d.isLabel == false && d.isFixed == false && t.isDataOdd(d) == false) {
                        normal = d;
                        break;
                    }
                }
                if (normal == null) {
                    List<Data> pickList = new ArrayList<Data>();
                    for (Data d : list) {
                        if (d.isLabel == false && t.isDataOdd(d) == false) {
                            pickList.add(d);
                        }
                    }
                    int pos = RandomUtil.rangeInteger(0, pickList.size() - 1);
                    normal = pickList.get(pos);
                }
            }
            return new Object[] { MutablePair.of(twoList.get(0), twoList.get(1)), normal };
        }
    }

    private BigDecimal getDiffValue(Data total, List<Data> list) {
        BigDecimal t2 = new BigDecimal(0);
        for (Data d : list) {
            t2 = t2.add(d.value);
        }
        if (!t2.equals(total.value)) {
            return total.value.subtract(t2);
        }
        return BigDecimal.ZERO;
    }

    private boolean isAllDone(Data total, List<Data> list) {
        if (isDataOdd(total)) {
            return false;
        }
        for (Data d : list) {
            if (isDataOdd(d)) {
                return false;
            }
        }
        BigDecimal t2 = new BigDecimal(0);
        for (Data d : list) {
            t2 = t2.add(d.value);
        }
        if (!new Double(t2.doubleValue()).equals(total.value.doubleValue())) {
            return false;
        }
        return true;
    }

    private void addValue(Data data, char type, double val) {
        if (data.isLabel) {
            throw new RuntimeException("此為標籤 : " + data);
        }
        switch (type) {
        case 'u':
            data.value = data.value.add(new BigDecimal(val));
            break;
        case 'd':
            data.value = data.value.subtract(new BigDecimal(val));
            break;
        }
    }

    private char isUpOrDown() {
        double random = Math.random();
        if (random >= 0.5d) {
            return 'u';
        } else {
            return 'd';
        }
    }

    private boolean isDataOdd(Data data) {
        if (data.value == null) {
            return false;
        }
        boolean isOdd = false;
        BigDecimal newVal = data.value.setScale(0, BigDecimal.ROUND_HALF_UP);
        if (new Double(newVal.doubleValue()).compareTo(data.value.doubleValue()) != 0) {
            isOdd = true;
        }
        return isOdd;
    }

    private void showData(DataTotal rtnData) {
        System.out.println("total - " + rtnData.total);
        for (Data d : rtnData.list) {
            System.out.println(d);
        }
    }

    /**
     * type -> x 抓衡的, y 抓值得
     */
    private DataTotal getSamePos(int pos, char type, List<Data> list) {
        List<Data> list2 = new ArrayList<Data>();
        for (Data dd : list) {
            switch (type) {
            case 'x':
                if (dd.y == pos) {
                    list2.add(dd);
                }
                break;
            case 'y':
                if (dd.x == pos) {
                    list2.add(dd);
                }
                break;
            }
        }
        switch (type) {
        case 'x':
            Collections.sort(list2, new Comparator<Data>() {
                @Override
                public int compare(Data o1, Data o2) {
                    return new Integer(o1.x).compareTo(o2.x);
                }
            });
            break;
        case 'y':
            Collections.sort(list2, new Comparator<Data>() {
                @Override
                public int compare(Data o1, Data o2) {
                    return new Integer(o1.y).compareTo(o2.y);
                }
            });
            break;
        }

        Data totalZ = null;
        Double total = -1d;
        for (int ii = 0; ii < list2.size(); ii++) {
            Data d = list2.get(ii);
            if (d.value == null || d.isLabel == true) {
                continue;
            }
            Double d2 = d.value.doubleValue();
            total = Math.max(d2, total);
            if (d2.equals(total)) {
                totalZ = d;
            }
        }

        DataTotal rtn = new DataTotal();
        rtn.list = list2;
        rtn.total = totalZ;
        rtn.list.remove(rtn.total);

        System.out.println("total -- " + rtn.total);

        for (int ii = 0; ii < rtn.list.size(); ii++) {
            Data d = rtn.list.get(ii);
            if (d.value == null || d.isLabel) {
                rtn.list.remove(d);
            }
        }
        return rtn;
    }

    private class DataTotal {
        List<Data> list;
        Data total;
    }

    private class Data {
        int x = -1;
        int y = -1;
        String orignValue;
        BigDecimal value;
        boolean isLabel;
        boolean isFixed;

        @Override
        public String toString() {
            return "Data [x=" + x + ", y=" + y + ", orignValue=" + orignValue + ", value=" + value + ", isLabel=" + isLabel + ", isFixed=" + isFixed + "]";
        }
    }

    private boolean isLabel(int x, int y, Test32Region region) {
        if ((x >= region.getStart().getX() && y >= region.getStart().getY()) && (x <= region.getEnd().getX() && y <= region.getEnd().getY())) {
            return false;
        }
        return true;
    }

    private List<Data> readSheet(HSSFSheet sheet, Test32Region region) {
        List<Data> list = new ArrayList<Data>();
        HSSFRow row = null;
        HSSFCell cell = null;
        String value = null;
        for (int y = 0; y <= sheet.getLastRowNum(); y++) {
            row = sheet.getRow(y);
            for (int x = 0; x < row.getLastCellNum(); x++) {
                cell = row.getCell(x);
                if (cell != null) {
                    value = ExcelUtil.getInstance().readHSSFCell(cell);
                } else {
                    value = null;
                }
                Data d = new Data();
                d.x = x;
                d.y = y;
                d.orignValue = value;
                d.isLabel = isLabel(x, y, region);
                try {
                    d.value = new BigDecimal(value);
                } catch (Exception ex) {
                }
                list.add(d);
            }
        }
        return list;
    }
}
