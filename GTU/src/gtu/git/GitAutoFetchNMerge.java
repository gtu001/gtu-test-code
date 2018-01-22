package gtu.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;

public class GitAutoFetchNMerge {

    public static void main(String[] args) throws IOException {
        File fileDirs = new File("C:\\workstuff\\workspace_scsb");
        String[] prodArry = new String[] { "CMS", "DBResource", "UserPermission" };
        File[] files = fileDirs.listFiles();
        for (int ii = 0 ; ii < files.length ; ii ++) {
            File f = files[ii];
            List<String> lst = new ArrayList<String>();
            if (f.isDirectory() && !f.getName().startsWith(".") && !f.getName().startsWith("__")) {
                lst.add("cd " + f);
                lst.add("i:");
                if (ArrayUtils.contains(prodArry, f.getName())) {
                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://gtu001@192.168.93.205:8448/r/ProdModule/" + f.getName() + ".git"));
                } else {
                    lst.add(FileUtil.replaceSpecialChar("git remote set-url origin http://gtu001@192.168.93.205:8448/r/SCSB_CCBILL/" + f.getName() + ".git"));
                }
                lst.add("git fetch -v --progress \"origin\"");
                lst.add("git merge remotes/origin/master --edit --no-commit ");
            }

            try {
                System.out.println("index : " + ii + "# current : " + f.getName());
                String commands = StringUtils.join(lst, " && ");
                Process exec = Runtime.getRuntime().exec("cmd /c " + commands);
                ProcessWatcher newInstance = ProcessWatcher.newInstance(exec);
                newInstance.getStream(30000);
                System.out.println(newInstance.getErrorStreamToString());
                System.out.println(newInstance.getInputStreamToString());
                System.out.println("processed " + (ii+1) + " -> " + files.length);
            } catch (java.util.concurrent.TimeoutException ex) {
                System.err.println("Timeout !!");
            }
        }
        System.out.println("done...");
    }

}
