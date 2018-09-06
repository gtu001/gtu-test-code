package gtu.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.git.GitLogToWorksheet_GTU.GitLog;
import gtu.runtime.ProcessWatcher;

public class GitLogToWorksheet_Aelta {

    public static void main(String[] args) throws IOException, ParseException {
        GitLogToWorksheet_Aelta t = new GitLogToWorksheet_Aelta();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d1 = sdf.parse("20180801");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Date d2 = sdf.parse("20180831");
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        c2.set(Calendar.MILLISECOND, 999999);

        String logCommand = GitLog.newInstance().since(c1.getTime()).before(c2.getTime()).author("gtu001").nameType(999).build();

        File fileDirs = new File("D:/workstuff/workspace_taida");
        List<String> lst = new ArrayList<String>();
        lst.add(FileUtil.replaceSpecialChar("cd " + fileDirs));
        lst.add("d:");
        lst.add(logCommand);

        try {
            String commands = StringUtils.join(lst, " && ");
            System.out.println(commands);
            Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
            ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
            newInstance.getStream(30000);
            newInstance.encode("UTF8");

            String orignLog = newInstance.getInputStreamToString();

            Set<String> logSet = t.getUniqueLog(orignLog);

            for (String log : logSet) {
                System.out.println(log);
            }
        } catch (java.util.concurrent.TimeoutException ex) {
            System.err.println("Timeout !!");
        }
        System.out.println("done...");
    }

    private Set<String> getUniqueLog(String content) {
        try {
            Set<String> st = new LinkedHashSet<String>();
            BufferedReader reader = new BufferedReader(new StringReader(content));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.defaultString(line).replaceAll("\\QSigned-off-by: gtu001 <gtu001@gmail.com>\\E", "");
                st.add(line);
            }
            return st;
        } catch (Exception e) {
            throw new RuntimeException("getUniqueLog ERR : " + e.getMessage(), e);
        }
    }
}
