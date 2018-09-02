package gtu.file;

import gtu.regex.PatternUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Troy 2009/05/04
 * 
 */
public class FileContentSearchUtil {
    public static void main(String args[]) {
        String folderPath = "E:\\j72\\grails-app\\views";
        String subName = ".gsp";
        String charset = "utf8";
        String content = ".\\D?Date.substring";
        List<String> list = FileContentSearchUtil.searhContent(content, folderPath, subName, charset);
        for (String str : list) {
            System.out.println(str);
        }
        System.out.println("done...");
    }

    private static List<String> searhContent(String content, String folderPath, String subName, String charset) {
        ArrayList<String> rtnlist = new ArrayList<String>();
        try {
            ArrayList<File> list = FileUtil.traceFileList(folderPath);
            for (int ii = 0; ii < list.size(); ii++) {
                File f = list.get(ii);
                if (f.getAbsolutePath().endsWith(subName)) {
                    byte[] b = PatternUtil.loadFromFile(f.getAbsolutePath());
                    if (b != null) {
                        String input = new String(b, charset);
                        if (input.indexOf(content) != -1) {
                            rtnlist.add(f.getAbsolutePath());
                        } else {
                            Pattern p = Pattern.compile(content);
                            Matcher m = p.matcher(input);
                            if (m.find() == true) {
                                rtnlist.add(f.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rtnlist;
    }
}
