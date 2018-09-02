package gtu.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.runtime.ProcessRuntimeExec;

public class MavenInstallJarFile {

    public static void main(String[] args) throws IOException, InterruptedException {
        String command = MavenInstallJarFile.newInstance()//
                .mavenBinDir("E:/apps/apache-maven-3.3.9/bin")//
                .targetJarPath("E:/my_tool/simple_dao_gen/sqljdbc4-4.1.jar")//
                .groupId("com.microsoft.sqlserver")//
                .artifactId("sqljdbc4")//
                .version("4.1")//
                // .targetRespositoryDir("E:/workstuff/workstuff/workspace_scsb/SCSB_CCBill_DC/lib")//
                .buildCommand_singleLine();
        
        System.out.println(command);

        boolean result = ProcessRuntimeExec.runCommandForWin(command);
        System.out.println(result);
        System.out.println("done...v4");
    }

    private MavenInstallJarFile() {
    }

    public static MavenInstallJarFile newInstance() {
        return new MavenInstallJarFile();
    }

    private String mavenBinDir;
    private String targetJarPath;
    private String groupId;
    private String artifactId;
    private String version;
    private String targetRespositoryDir;

    public MavenInstallJarFile targetJarPath(String targetJarPath) {
        this.targetJarPath = targetJarPath;
        return this;
    }

    public MavenInstallJarFile mavenBinDir(String mavenBinDir) {
        this.mavenBinDir = mavenBinDir;
        return this;
    }

    public MavenInstallJarFile groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public MavenInstallJarFile artifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public MavenInstallJarFile version(String version) {
        this.version = version;
        return this;
    }

    public MavenInstallJarFile targetRespositoryDir(String targetRespositoryDir) {
        this.targetRespositoryDir = targetRespositoryDir;
        return this;
    }

    public String[] buildCommand() {
        Validate.notBlank(mavenBinDir, "maven 的 Bin 目錄 不可為空(mavenBinDir)");
        Validate.notBlank(targetJarPath, "目標 jar路徑  不可為空(targetJarPath)");
        Validate.notBlank(groupId, "groupId  不可為空(groupId)");
        Validate.notBlank(artifactId, "artifactId  不可為空(artifactId)");
        Validate.notBlank(version, "version  不可為空(version)");

        List<String> lst = new ArrayList<String>();

        lst.add(String.format(" %smvn install:install-file ", mavenBinDir + File.separator));
        lst.add(" -Dpackaging=jar ");
        lst.add(String.format(" -Dfile=\"%s\" ", targetJarPath));
        lst.add(String.format(" -DgroupId=%s ", groupId));
        lst.add(String.format(" -DartifactId=%s ", artifactId));
        lst.add(String.format(" -Dversion=%s ", version));

        if (StringUtils.isNotBlank(targetRespositoryDir)) {
            lst.add(String.format(" -DlocalRepositoryPath=\"%s\" ", targetRespositoryDir));
        }

        return lst.toArray(new String[0]);
    }

    public String buildCommand_singleLine() {
        return StringUtils.join(buildCommand(), " ");
    }
}