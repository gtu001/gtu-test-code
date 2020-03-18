package _temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;

public class Test56 {

    public static void main(String[] args) {
        List<String> allDataLst = new ArrayList<String>();

        File file = new File("D:\\work_tool\\");
        for (File f : file.listFiles()) {
            if (f.isDirectory() && (f.getName().matches("\\d+") || f.getName().matches("\\d+\\_\\w+"))) {
                ProcessWatcher watcher = //
                        ProcessWatcher.newInstance(RuntimeBatPromptModeUtil.newInstance()//
                                .command(" d: ")//
                                .command(" cd " + f)//
                                .command(" svn log -r {2011-02-02}:{2099-02-02} --verbose ")//
                                .runInBatFile(false)//
                                .apply("BIG5"));
                watcher.getStreamSync();
                String resultString1 = watcher.getInputStreamToString();
                String resultString2 = watcher.getErrorStreamToString();

                allDataLst.addAll(keepData(resultString1));
                allDataLst.addAll(keepData(resultString2));
                System.out.println("1<<<<<<<" + resultString1);
                // System.out.println("2<<<<<<<" + resultString2);
                // break;
            }
        }

        FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, "xxxxxxxxxx.txt"), StringUtils.join(allDataLst, "\r\n"), "UTF8");
        System.out.println("done..");
    }

    private static Pattern ptn = Pattern.compile("\\s+[A-Z]{1}\\s+(\\/.*+)");

    private static List<String> keepData(String resultString) {
        List<String> logLst = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(resultString));
            Matcher mth = null;
            for (String line = null; (line = reader.readLine()) != null;) {
                mth = ptn.matcher(line);
                if (mth.find()) {
                    logLst.add(StringUtils.trimToEmpty(mth.group(1)));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logLst;
    }
}
