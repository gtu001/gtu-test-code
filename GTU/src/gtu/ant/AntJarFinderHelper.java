package gtu.ant;

import gtu.console.SystemInUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntJarFinderHelper {

    public static void main(String[] args) throws IOException {
        String inputStreamStr = SystemInUtil.readContent();

        //scan for...
        //        [javac] import org.springframework.jdbc.datasource.DriverManagerDataSource;

        Pattern pattern = Pattern.compile("(\\s*\\[javac\\]\\simport\\s)(.*)(;)");
        Matcher matcher = null;
        BufferedReader reader = new BufferedReader(new StringReader(inputStreamStr));
        String line = null;

        Set<String> classPath = new HashSet<String>();
        while ((line = reader.readLine()) != null) {
            matcher = pattern.matcher(line);
            if (matcher.matches()) {
                classPath.add(matcher.group(2));
            }
        }

        for (String classp : classPath) {
            System.out.println("<packagename mode=\"small\">" + classp + "</packagename>");
        }
        System.out.println("done...");
    }
}
