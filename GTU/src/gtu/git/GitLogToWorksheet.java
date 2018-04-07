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
import gtu.git.GitLogToWorksheet_GTU.GitLog;
import gtu.runtime.ProcessWatcher;

public class GitLogToWorksheet {

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String startDateStr = "20180402";
//        String startDateStr = baseStartDateStr + StringUtils.leftPad(String.valueOf(jj), 2, "0");

        System.out.println("## " + startDateStr);
        Date d1 = sdf.parse(startDateStr);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(c1.getTime());
        c2.add(Calendar.DATE, 1);// 1

        String logCommand = GitLog.newInstance()//
                .since(c1.getTime())//
                .before(c2.getTime())//
                .author("gtu001")//
                .encode("big5")//
                // .nameType(1)//
                .build();
        System.out.println(logCommand);

        StringBuilder sb = new StringBuilder();

        File fileDirs = new File("E:\\workstuff\\workstuff\\workspace_scsb");
        String[] prodArry = new String[] { "CMS", "DBResource", "UserPermission" };
        File[] files = fileDirs.listFiles();
        for (int ii = 0; ii < files.length; ii++) {
            File f = files[ii];
            List<String> lst = new ArrayList<String>();
            if (f.isDirectory() && !f.getName().startsWith(".") && !f.getName().startsWith("__")) {
                lst.add(FileUtil.replaceSpecialChar("cd " + f));
                lst.add("e:");
                // if (ArrayUtils.contains(prodArry, f.getName())) {
                // lst.add(FileUtil.replaceSpecialChar("git remote set-url
                // origin http://gtu001@192.168.93.205:8448/r/ProdModule/" +
                // f.getName() + ".git"));
                // } else {
                // lst.add(FileUtil.replaceSpecialChar("git remote set-url
                // origin http://gtu001@192.168.93.205:8448/r/SCSB_CCBILL/"
                // + f.getName() + ".git"));
                // }
                lst.add(logCommand);
            }

            try {
                System.out.println("index : " + ii + "# current : " + f.getName());
                String commands = StringUtils.join(lst, " && ");
                Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
                ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
                newInstance.getStream(30000);
                // System.out.println(newInstance.getErrorStreamToString());
                // System.out.println(newInstance.getInputStreamToString());
                String logContent = newInstance.getInputStreamToString();
                System.out.println("processed " + (ii + 1) + " -> " + files.length);

                if (StringUtils.isNotBlank(logContent)) {
                    sb.append("--->/" + f.getName() + "\r\n");
                    sb.append(logContent + "\r\n");
                }
            } catch (java.util.concurrent.TimeoutException ex) {
                System.err.println("Timeout !!");
            }
        }

        System.out.println("===============================================================");
        System.out.println(sb);

//        if (sb.length() > 0) {
//            FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH + "/xxxx/", startDateStr + ".txt"), sb.toString(), "utf8");
//        }
        System.out.println("done...");
    }

}
