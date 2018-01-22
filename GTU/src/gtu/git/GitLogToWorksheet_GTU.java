package gtu.git;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.git.GitLogToWorksheet.GitLog;
import gtu.runtime.ProcessWatcher;

public class GitLogToWorksheet_GTU {

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d1 = sdf.parse("20180122");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(c1.getTime());
        c2.add(Calendar.DATE, 1);

        String logCommand = GitLog.newInstance().since(c1.getTime()).before(c2.getTime()).author("gtu001").nameType(1).build();

        File fileDirs = new File("E:\\workstuff\\workspace\\gtu-test-code");
        List<String> lst = new ArrayList<String>();
        lst.add(FileUtil.replaceSpecialChar("cd " + fileDirs));
        lst.add("e:");
//        lst.add(FileUtil.replaceSpecialChar("git remote set-url origin https://github.com/gtu001/gtu-test-code.git "));
        lst.add(logCommand);

        try {
            String commands = StringUtils.join(lst, " && ");
            System.out.println(commands);
            Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
            ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
            newInstance.getStream(30000);
            System.out.println(newInstance.getErrorStreamToString());
            System.out.println(newInstance.getInputStreamToString());
        } catch (java.util.concurrent.TimeoutException ex) {
            System.err.println("Timeout !!");
        }
        System.out.println("done...");
    }

}
