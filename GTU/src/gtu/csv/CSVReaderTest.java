package gtu.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CSVReaderTest {

    public static void main(String[] args) throws IOException {
        File file = new File("d:/test.csv");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file)));
        List<String[]> ls = reader.readAll();
        reader.close();
    }
}
