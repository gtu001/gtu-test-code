package gtu._work;

import gtu.date.DateUtil;
import gtu.poi.hssf.BaseCommonReportPC;
import gtu.poi.hssf.BaseCommonReportPC.Cell;
import gtu.poi.hssf.ExcelUtil;
import gtu.poi.hssf.ExcelWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultiset;

public class Rcdf002eExcelMaker {

    private PrintStream out = System.out;
    public static void main(String[] args) throws IOException {
        Rcdf002eExcelMaker test = new Rcdf002eExcelMaker();
        test.execute();
        System.out.println("done...");
    }
    
    File baseDir = new File("g:/output_rcdf002e/");
    
    public void execute(){
        try {
            HashMultiset<String> wordBag = HashMultiset.create();
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(baseDir, "只1次請領.txt")), "utf8"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                String[] sp = line.split(",", -1);
                wordBag.add(StringUtils.substring(sp[0], 0, 5), 1);
            }
            reader.close();
            
            final HashBasedTable<String, Integer, Integer> hashTable = HashBasedTable.create();
            LineNumberReader reader1 = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(baseDir, "超過1次請領.txt")), "utf8"));
            for (String line = null; (line = reader1.readLine()) != null;) {
                String[] sp = line.split(",", -1);
                String siteId = sp[2];
                int count = Integer.parseInt(sp[3]);
                int realCount = 0;
                if(hashTable.contains(siteId, count)){
                    realCount = hashTable.get(siteId, count);
                }
                realCount ++;
                hashTable.put(siteId, count, realCount);
            }
            reader1.close();
            for(String siteId : wordBag.elementSet()){
                hashTable.put(siteId, 1, wordBag.count(siteId));
            }
            
            final List<List<String>> datalist = this.getPlusData(hashTable);
            
            //-------------------------------------------
            
            this.processExcel(new ProcessBody() {
                @Override
                public void action(HSSFSheet sheet) {
                    ExcelUtil util = ExcelUtil.getInstance();
                    for(List<String> data : datalist){
                        util.setRowByList(sheet.createRow(sheet.getLastRowNum() + 1), data);
                    }
                }
            });

            out.println("done...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            out.println("finally...");
        }
    }
    
    private List<List<String>> getPlusData(final HashBasedTable<String, Integer, Integer> hashTable){
        final Map<String,List<String>> siteMap = new LinkedHashMap<String,List<String>>();
        for(CityCode ccc : CityCode.values()){
            List<String> data = getRowData(ccc.siteId, ccc.cityName, hashTable);
            siteMap.put(data.get(0), data);
        }
        
        List<List<String>> dataList = new ArrayList<List<String>>();
        
        //總計
        List<String> totalList = this.getTotal("總計", null, siteMap);
        
        //台灣省
        List<String> taiwanList = this.getTotal("臺灣省", taiwanCity, siteMap);
        
        //福建省
        List<String> chinaList = this.getTotal("福建省", chinaCity, siteMap);
        
        dataList.add(totalList);
        for(String cityName : siteMap.keySet()){
            List<String> data = siteMap.get(cityName);
            if(StringUtils.equals(data.get(0), "臺灣省")){
                dataList.add(taiwanList);
            }else if(StringUtils.equals(data.get(0), "福建省")){
                dataList.add(chinaList);
            }else{
                dataList.add(data);
            }
        }
        
        return dataList;
    }
    
    private List<String> getTotal(String label, String[] cityContains, Map<String,List<String>> siteMap){
        int[] vals1 = new int[siteMap.values().iterator().next().size()];
        for(String cityName : siteMap.keySet()){
            List<String> data = siteMap.get(cityName);
            if(cityContains != null && StringUtils.indexOfAny(data.get(0), cityContains) == -1){
                continue;
            }
            for(int ii = 0 ; ii < data.size() ; ii ++){
                String valStr = data.get(ii);
                if(StringUtils.isNumeric(valStr)){
                    vals1[ii] += Integer.parseInt(valStr);
                }
            }
        }
        vals1 = ArrayUtils.remove(vals1, 0);
        List<String> totalList = new ArrayList<String>();
        totalList.add(label);
        for(int val : vals1){
            totalList.add(String.valueOf(val));
        }
        return totalList;
    }
    
    private List<String> getRowData(String rowKey, String rowTitle, HashBasedTable<String, Integer, Integer> hashTable){
//        out.println("========" + rowTitle + ":" + rowKey);
        List<String> rowData = new ArrayList<String>();
        rowData.add(rowTitle);
        for(int time = 1 ; time <= 10; time ++){
            String val = "0";
            if(hashTable.contains(rowKey, time)){
                val = String.valueOf(hashTable.get(rowKey, time));
            }
//            out.println("領" + time + " = 次" + val);
            rowData.add(val);
        }
        rowData.add(String.valueOf(this.getTotalCount(rowKey, 10, 20, hashTable)));
        rowData.add(String.valueOf(this.getTotalCount(rowKey, 20, 30, hashTable)));
        rowData.add(String.valueOf(this.getTotalCount(rowKey, 30, 40, hashTable)));
        rowData.add(String.valueOf(this.getTotalCount(rowKey, 40, 100, hashTable)));
        
        int littleTotal = 0;
        for(int time = 1 ; time <= 100; time ++){
            if(hashTable.contains(rowKey, time)){
                littleTotal += hashTable.get(rowKey, time);
            }
        }
        rowData.add(String.valueOf(littleTotal));
        out.println(rowData);
        return rowData;
    }
    
    private int getTotalCount(String rowKey , int start, int end, HashBasedTable<String, Integer, Integer> hashTable){
        int total = 0;
//        System.out.println("start end = " + start + " , " + end);
        for(int time = start + 1 ; time < end; time ++){
//            System.out.println("-->"+time);
            if(hashTable.contains(rowKey, time)){
                total += hashTable.get(rowKey, time);
            }
        }
        return total;
    }
    
    private void processExcel(ProcessBody body) throws FileNotFoundException, IOException{
        ExcelUtil util = ExcelUtil.getInstance();
        ExcelWriter writer = ExcelWriter.getInstance();
        
        String[] tableHeader = new String[]{"縣市名稱","1次","2次","3次","4次","5次","6次","7次","8次","9次","10次","10次以上","20次以上","30次以上","40次以上","小計"};
        int lastColNum = tableHeader.length - 1;
        
        HSSFWorkbook wk = new HSSFWorkbook();
        
        HSSFSheet sheet = wk.createSheet();
        
        BaseCommonReportPC pc = BaseCommonReportPC.getInstance();
        List<Cell> cellList = new ArrayList<Cell>();
        cellList.add(Cell.colspan(0, "新式國民身分證補證統計表", 0, lastColNum));
        cellList.add(Cell.colspan(1, "中華民國94年12月21日至XXX年XX月XX日止", 0, lastColNum));
        cellList.add(Cell.colspan(2, DateUtil.getCurrentDate(true)+"戶政司製", 0, lastColNum));
        pc.processExcelColumn(cellList, sheet);
        
        HSSFFont font = ExcelWriter.FontHandler.newInstance(wk).setFontName("標楷體").getFont();
        HSSFFont fontHead = ExcelWriter.FontHandler.newInstance(wk).setFontName("標楷體").setFontSize((short)16).getFont();
        
        util.setRowByList(sheet.createRow(sheet.getLastRowNum() + 1), Arrays.asList(tableHeader));
        
        body.action(sheet);
        
        //全設標楷且黑框
        ExcelWriter.CellStyleHandler.newInstance(wk)//
                .setSheet(sheet).setBorder(new HSSFColor.BLACK()).setFont(font)//
                .applyStyle(0, sheet.getLastRowNum(), 0, lastColNum);
        
        //表投置中
        ExcelWriter.CellStyleHandler.newInstance(wk)//
                .setAlignment(HSSFCellStyle.ALIGN_CENTER)//
                .setFont(font).setSheet(sheet).applyStyle(0, 2, 0, lastColNum);
        
        //第一列
        ExcelWriter.CellStyleHandler.newInstance(wk)//
                .setAlignment(HSSFCellStyle.ALIGN_CENTER)//
                .setFont(fontHead).setSheet(sheet).applyStyle(0, 0);
        
        //靠右
        ExcelWriter.CellStyleHandler.newInstance(wk)//
                .setAlignment(HSSFCellStyle.ALIGN_RIGHT)//
                .setFont(font).setSheet(sheet).setFont(font).applyStyle(2, 0);
        
        util.writeExcel(new File(baseDir, "統計excel結果.xls"), wk);
    }
    
    //台灣省
    String[] taiwanCity = {"宜蘭縣","桃園縣","新竹縣","苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣","屏東縣","臺東縣","花蓮縣","澎湖縣","基隆市","新竹市","嘉義市"};
    //福建省
    String[] chinaCity = {"連江縣", "金門縣"};
    
    enum CityCode {
        City1("65000", "新北市"),//
        City2("63000", "臺北市"),//
        City3("66000", "臺中市"),//
        City4("67000", "臺南市"),//
        City5("64000", "高雄市"),//
        City6("XXXXXX", "臺灣省"),//
        City7("10002", "宜蘭縣"),//
        City8("10003", "桃園縣"),//
        City9("10004", "新竹縣"),//
        City10("10005", "苗栗縣"),//
        City11("10007", "彰化縣"),//
        City12("10008", "南投縣"),//
        City13("10009", "雲林縣"),//
        City14("10010", "嘉義縣"),//
        City15("10013", "屏東縣"),//
        City16("10014", "臺東縣"),//
        City17("10015", "花蓮縣"),//
        City18("10016", "澎湖縣"),//
        City19("10017", "基隆市"),//
        City20("10018", "新竹市"),//
        City21("10020", "嘉義市"),//
        City22("XXXXXX", "福建省"),//
        City23("09007", "連江縣"),//
        City24("09020", "金門縣"),//
        ;
        final String siteId; final String cityName;
        CityCode(final String siteId, final String cityName){
            this.siteId = siteId;
            this.cityName = cityName;
        }
    }
    
    interface ProcessBody {
        void action(HSSFSheet sheet);
    }
    
    public void setOut(PrintStream out) {
        this.out = out;
    }

    public File getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    public PrintStream getOut() {
        return out;
    }
}
