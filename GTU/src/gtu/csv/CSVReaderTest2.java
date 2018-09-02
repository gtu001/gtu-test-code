package gtu.csv;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReaderTest2 {
    
    public static void main(String[] args){
        List<String[]> list = commGetStr("C:/workspace/gtu-test-code/GTU/src/gtu/csv/test001.csv");
        for(String[] val : list){
            System.out.println(Arrays.toString(val));
        }
        System.out.println("done...");
    }
    
    public static List<String[]> commGetStr(String fileName) {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        CSVFormat csvFileFormat = CSVFormat.EXCEL.withDelimiter(',');
        List<String[]> rs = new ArrayList<String[]>();
        try {
            fileReader = new FileReader(fileName);
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            for (int i = 0; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                List<String> recList = new ArrayList<String>();
                for(int ii = 0 ; ii < record.size(); ii ++){
                    String val = record.get(ii);
                    recList.add(val);
                }
                rs.add(recList.toArray(new String[0]));
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (Exception e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return rs;
    }
    
    /**
     * 讀取 CSV 內容
     * @param fileName 絕對位置
     * @param colsName
     * @return
     */
    public static List<Map<String, String>> commGetStr(String fileName, String colsName[]) {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        // Create the CSVFormat object with the header mapping

        CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader(colsName);
        List rs = new ArrayList();
        try {
            // initialize FileReader object
            fileReader = new FileReader(fileName);
            // initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            // Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            // Read the CSV file records starting from the second record to skip
            // the header

            for (int i = 0; i < csvRecords.size(); i++) {

                CSVRecord record = csvRecords.get(i);

                Map<String, String> map = new HashMap<String, String>();
                for (String colName : colsName) {
                    map.put(colName, record.get(colName));
                }
                rs.add(map);
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (Exception e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return rs;
    }
}
