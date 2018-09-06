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
import gtu.runtime.ProcessWatcher;

public class GitLogToWorksheet_GTU {

    static class GitLog {
        // git log --since="2018-01-22T00:00:00" --before="2018-01-23T00:00:00"
        // --author=gtu001 --no-merges --encoding=utf8 --all-match

        Date since;
        Date before;
        String author;
        String encode = "utf-8";
        String nameType;
        boolean hasDate;

        public static GitLog newInstance() {
            return new GitLog();
        }

        /**
         * 開始時間
         * 
         * @param since
         * @return
         */
        public GitLog since(Date since) {
            this.since = since;
            return this;
        }

        /**
         * 結束時間
         * 
         * @param before
         * @return
         */
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
            default:
                nameType = "";
                break;
            }
            return this;
        }

        public GitLog hasDate(boolean hasDate) {
            this.hasDate = hasDate;
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
            if (hasDate) {
                sb.append(" --pretty=format:\"%s%b  %ad\" ");// %n <-換行
            } else {
                sb.append(" --pretty=format:\"%s%b  \" ");// %n <-換行
            }
            sb.append(" --date=iso  ");
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d1 = sdf.parse("20180801");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(c1.getTime());
        c2.add(Calendar.DATE, 1);

        String logCommand = GitLog.newInstance().since(c1.getTime()).before(c2.getTime()).author("gtu001").nameType(999).build();

        File fileDirs = new File("D:/workstuff/workspace_taida");
        List<String> lst = new ArrayList<String>();
        lst.add(FileUtil.replaceSpecialChar("cd " + fileDirs));
        lst.add("d:");
        // lst.add(FileUtil.replaceSpecialChar("git remote set-url origin
        // https://github.com/gtu001/gtu-test-code.git "));
        lst.add(logCommand);

        try {
            String commands = StringUtils.join(lst, " && ");
            System.out.println(commands);
            Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
            ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
            newInstance.getStream(30000);
            newInstance.encode("UTF8");
            System.out.println(newInstance.getErrorStreamToString());
            System.out.println(newInstance.getInputStreamToString());
        } catch (java.util.concurrent.TimeoutException ex) {
            System.err.println("Timeout !!");
        }
        System.out.println("done...");
    }

}
