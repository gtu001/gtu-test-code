package gtu.maven;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class MavenInstallJarFile {
    
    public static void main(String[] args) {
//        MavenInstallJarFile.newInstance()//
//        .artifactId("CP937-FUCO").groupId("font").version("1.0")//
//        .targetJarPath("C:/Program Files/Java/jdk1.8.0_73/jre/lib/ext/CP937_FUCO.jar")//
//        .targetRespositoryDir("E:/workstuff/workstuff/workspace_scsb/SCSB_CCBill_DC/lib")//
//        .build();
        MavenInstallJarFile.newInstance()//
        .artifactId("sqljdbc4").groupId("com.microsoft.sqlserver").version("4.1")//
        .targetJarPath("E:/my_tool/simple_dao_gen/sqljdbc4-4.1.jar")//
//        .targetRespositoryDir("E:/workstuff/workstuff/workspace_scsb/SCSB_CCBill_DC/lib")//
        .build();
    }

    private MavenInstallJarFile() {
    }

    public static MavenInstallJarFile newInstance() {
        return new MavenInstallJarFile();
    }

    private String targetJarPath;
    private String groupId;
    private String artifactId;
    private String version;
    private String targetRespositoryDir;

    public MavenInstallJarFile targetJarPath(String targetJarPath) {
        this.targetJarPath = targetJarPath;
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

    public void build() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(" %smvn install:install-file ", "E:/apps/apache-maven-3.3.9/bin/"));
            sb.append(" -Dpackaging=jar ");

            if (StringUtils.isNotBlank(targetJarPath)) {
                sb.append(String.format(" -Dfile=\"%s\" ", targetJarPath));
            }

            if (StringUtils.isNotBlank(groupId)) {
                sb.append(String.format(" -DgroupId=%s ", groupId));
            }

            if (StringUtils.isNotBlank(artifactId)) {
                sb.append(String.format(" -DartifactId=%s ", artifactId));
            }

            if (StringUtils.isNotBlank(version)) {
                sb.append(String.format(" -Dversion=%s ", version));
            }

            if (StringUtils.isNotBlank(targetRespositoryDir)) {
                sb.append(String.format(" -DlocalRepositoryPath=\"%s\" ", targetRespositoryDir));
            }
            
            System.out.println(sb.toString());

//            Process exec = Runtime.getRuntime().exec(sb.toString());
//            List<String> inLst = IOUtils.readLines(exec.getInputStream());
//            List<String> exLst = IOUtils.readLines(exec.getErrorStream());
//            
//            for(String line : inLst) {
//                System.out.println(">>" + line);
//            }
//            for(String line : exLst) {
//                System.out.println("Err>>" + line);
//            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}