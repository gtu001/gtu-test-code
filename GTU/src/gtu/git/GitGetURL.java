package gtu.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.runtime.ProcessRuntimeExec;
import gtu.runtime.ProcessRuntimeExec.ProcessRuntimeExec_Result;

public class GitGetURL {

    public static void main(String[] args) {
        File targetDir = new File("E:\\workstuff\\workspace\\gtu-test-code");
        String resultStr = GitGetURL.getInstance().getRemoteURL(targetDir, "BIG5");
        System.out.println("resultStr : " + resultStr);
        System.out.println("done...");
    }

    private static final GitGetURL _INST = new GitGetURL();

    private GitGetURL() {
    }

    public static GitGetURL getInstance() {
        return _INST;
    }

    private String getFileRoot(File file) {
        for (File f : File.listRoots()) {
            if (file.getAbsolutePath().startsWith(f.getAbsolutePath())) {
                String tmpStr = f.getAbsolutePath();
                tmpStr = tmpStr.replaceFirst("[\\/\\\\]$", "");
                return tmpStr;
            }
        }
        return "";
    }

    public String getRemoteURL(File targetDir, String consoleEncode) {
        List<String> cmdLst = new ArrayList<String>();
        cmdLst.add("cd " + targetDir + " ");

        String fileRoot = getFileRoot(targetDir);
        if (StringUtils.isNotBlank(fileRoot)) {
            cmdLst.add(fileRoot);
        }

        cmdLst.add(" git config --get remote.origin.url ");// local
        // cmdLst.add(" git remote show origin ");// remote fetch intact

        String command = StringUtils.join(cmdLst, " && ");

        ProcessRuntimeExec_Result vo = ProcessRuntimeExec.runCommandForWin_full(command, consoleEncode);
        return vo.getInputLst().get(0);
    }
}
