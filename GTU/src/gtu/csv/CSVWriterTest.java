package gtu.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

public class CSVWriterTest {

    public static void main(String[] args) throws IOException {
        CSVPrinter printer = CSVFormat.DEFAULT.withNullString("")//
                .withQuoteMode(QuoteMode.ALL)//
                .print(new BufferedWriter(new FileWriter(new File("D:/gtu-test-code/GTU/src/gtu/csv/testFile.csv"))));
        
        List<List<String>> fileRecords = new ArrayList<List<String>>();
        
        fileRecords.add(Arrays.asList("aa","bb","cc"));
        fileRecords.add(Arrays.asList("11","22","33"));
        fileRecords.add(Arrays.asList("甲","乙","丙"));
        
        printer.printRecords(fileRecords); // 寫入傳 T24 檔案
        
        printer.flush();
        printer.close();

        System.out.println("done...");
    }
}
