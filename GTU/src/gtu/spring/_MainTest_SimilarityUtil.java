package gtu.spring;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

public class _MainTest_SimilarityUtil {

    public static void main(String[] args) throws IOException {
        StringReader content = new StringReader(SystemInUtil.readContent());
        LineNumberReader reader = new LineNumberReader(content);

        List<String> fromLst = new ArrayList<String>();
        List<String> toLst = new ArrayList<String>();

        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trimToEmpty(line);
            if(line.startsWith("from.")) {
                fromLst.add(line);
            }
            if(line.startsWith("to.")) {
                toLst.add(line);
            }
        }

        for(String f : fromLst) {
            TreeMap<Double, String> map = new TreeMap<Double, String>();
            for(String t : toLst) {
                map.put(SimilarityUtil.sim(f, t), t);
            }
            List<Double> keylst = new ArrayList(map.keySet());
            Collections.reverse(keylst);
            String val = map.get(keylst.get(0));
            System.out.println(val + "\t" + f);
        }
        System.out.println("done...");
    }
}
