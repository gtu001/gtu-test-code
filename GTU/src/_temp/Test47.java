package _temp;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.string.StringUtilForDb;

public class Test47 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test47 t = new Test47();

        String text = SystemInUtil.readContent();

        List<String> lst = new ArrayList<String>();

        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trim(line);

            if (StringUtils.isBlank(line)) {
                continue;
            }

            String name = StringUtilForDb.dbFieldToJava(line);

            if (lst.size() == 0) {
                System.out.println("@Id \n@GeneratedValue \n private Long " + name + ";");
                lst.add(name);
                continue;
            }

            lst.add(name);

            System.out.println("private String " + name + ";");

            // System.out.println(reader.getLineNumber() + "\t" + line);
        }

        reader.close();
        System.out.println("done...");
    }

}
