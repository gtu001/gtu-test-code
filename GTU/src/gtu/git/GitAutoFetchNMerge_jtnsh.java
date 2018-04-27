package gtu.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;

public class GitAutoFetchNMerge_jtnsh {

    public static void main(String[] args) throws IOException {
        File fileDirs = new File("E:/workstuff/workstuff/workspace_esun_ccbill");

        String account = "gtu001";
        if (args != null && args.length == 2) {
            fileDirs = new File(args[0]);
            account = args[1];
        }

        String[] commonModules = new String[] { "CMS", "CMSAD", "Config", "DBResource", "Static", "UserPermission" };

        List<String> needMergetLst = new ArrayList<String>();
        File[] files = fileDirs.listFiles();
        for (int ii = 0; ii < files.length; ii++) {
            File f = files[ii];
            List<String> lst = new ArrayList<String>();
            if (f.isDirectory() && !f.getName().startsWith(".") && !f.getName().startsWith("__")) {
                lst.add("cd " + f);
                lst.add("e:");

                if (!ArrayUtils.contains(commonModules, f.getName())) {
                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://" + account + "@192.168.93.205:8448/r/ESUN_CCBill/" + f.getName() + ".git"));
                } else {
                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://" + account + "@192.168.93.205:8448/r/ProdModule2/" + f.getName() + ".git"));
                }

                // lst.add("git fetch -v --progress \"origin\"");
                lst.add("git fetch -v --all");
                lst.add("git merge remotes/origin/master --edit --no-commit ");
            }

            try {
                System.out.println("index : " + ii + "# current : " + f.getName());
                String commands = StringUtils.join(lst, " && ");
                Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
                ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
                newInstance.getStream(60000);

                String errorStr = newInstance.getErrorStreamToString();
                String normalStr = newInstance.getInputStreamToString();
                System.out.println(errorStr);
                System.out.println(normalStr);

                if (findIfMergeExists(errorStr) || findIfMergeExists(normalStr)) {
                    needMergetLst.add(f.getName());
                }

                System.out.println("processed " + (ii + 1) + " -> " + files.length);
            } catch (java.util.concurrent.TimeoutException ex) {
                System.err.println("Timeout !!");
            }
        }

        for (String merge : needMergetLst) {
            System.out.println("Do Merge -----> " + merge);
        }
        System.out.println("done...");
    }

    private static boolean findIfMergeExists(String content) {
        // Please commit your changes or stash them before you merge.
        // Aborting
        Pattern ptn = Pattern.compile("Please\\scommit\\syour\\schanges\\sor\\sstash\\sthem\\sbefore\\syou\\smerge\\.", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(content);
        return mth.find();
    }
}
