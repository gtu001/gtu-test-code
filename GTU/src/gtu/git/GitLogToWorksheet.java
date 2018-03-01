package gtu.git;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;

public class GitLogToWorksheet {

    static class GitLog {
        // git log --since="2018-01-22T00:00:00" --before="2018-01-23T00:00:00"
        // --author=gtu001 --no-merges --encoding=utf8 --all-match

        Date since;
        Date before;
        String author;
        String encode = "utf-8";
        String nameType;

        public static GitLog newInstance() {
            return new GitLog();
        }

        public GitLog since(Date since) {
            this.since = since;
            return this;
        }

        public GitLog before(Date before) {
            this.before = before;
            return this;
        }

        public GitLog author(String author) {
            this.author = author;
            return this;
        }

        public GitLog encode(String encode) {
            this.encode = encode;
            return this;
        }
        
        public GitLog nameType(int type) {
            switch (type) {
            case 1:
                // For full path names of changed files
                nameType = " --name-only ";
                break;
            case 2:
                // For full path names and status of changed files
                nameType = " --name-status ";
                break;
            case 3:
                // For abbreviated pathnames and a diffstat of changed files
                nameType = " --stat ";
                break;
            }
            return this;
        }

        public String build() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            StringBuilder sb = new StringBuilder();
            sb.append("git log ");
            if (since != null) {
                String val = sdf.format(since);
                sb.append(String.format(" --since=\"%s\" ", val));
            }
            if (before != null) {
                String val = sdf.format(before);
                sb.append(String.format(" --before=\"%s\" ", val));
            }
            if (author != null) {
                sb.append(String.format(" --author=\"%s\" ", author));
            }
            if (encode != null) {
                sb.append(" --encoding=" + encode + " ");
            }
            if (nameType != null) {
                sb.append(" " + nameType + " ");
            }
            sb.append(" --no-merges ");
            sb.append(" --all-match ");
            sb.append(" --pretty=format:\"%s%b  \" ");//%n <-換行
//            sb.append(" --pretty=format:\"%s%b  %ad\" ");//%n <-換行
            sb.append(" --date=iso  ");
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d1 = sdf.parse("20180206");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(c1.getTime());
        c2.add(Calendar.DATE, 1);//1

        String logCommand = GitLog.newInstance()//
                .since(c1.getTime())//
                .before(c2.getTime())//
                .author("gtu001")//
                .encode("big5")//
//                .nameType(1)//
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
//                if (ArrayUtils.contains(prodArry, f.getName())) {
//                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://gtu001@192.168.93.205:8448/r/ProdModule/" + f.getName() + ".git"));
//                } else {
//                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://gtu001@192.168.93.205:8448/r/SCSB_CCBILL/" + f.getName() + ".git"));
//                }
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
                
                if(StringUtils.isNotBlank(logContent)) {
                    sb.append("--->/" + f.getName() + "\r\n");
                    sb.append(logContent + "\r\n");
                }
            } catch (java.util.concurrent.TimeoutException ex) {
                System.err.println("Timeout !!");
            }
        }
        
        System.out.println("===============================================================");
        System.out.println(sb);
        System.out.println("done...");
    }

}
