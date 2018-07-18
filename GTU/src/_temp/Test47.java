package _temp;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

public class Test47 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test47 t = new Test47();

        String text = SystemInUtil.readContent();

        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trim(line);

            System.out.println(reader.getLineNumber() + "\t" + line);
        }

        reader.close();
        System.out.println("done...");
    }
}
