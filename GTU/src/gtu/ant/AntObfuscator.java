package gtu.ant;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;

/**
 * 做系統通知用
 */
public class AntObfuscator extends Task {

    private AntConfigHelper config;

    private String jarDir;
    private String inJar;

    private static boolean isWindows = false;
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    @Override
    public void execute() throws BuildException {
        try {
            config = AntConfigHelper.of(this.getProject());

            jarDir = StringUtils.trimToEmpty(jarDir);
            inJar = StringUtils.trimToEmpty(inJar);

            this.log("[jarDir]" + jarDir);
            this.log("[inJar]" + inJar);

            // jarDir = config.getParseAfterValue(jarDir);
            // inJar = config.getParseAfterValue(inJar);

            // <jar in="_#in#_" out="_#out#_"/>

            // [obfuscator]
            // [jarDir]/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/Allatori-6.9
            // [obfuscator] [inJar]/home/gtu001/桌面/FastDBQueryUI.jar

            String configData = FileUtil.loadFromFile(new File(jarDir, "config.xml"), "UTF8");
            File destNewFile = new File(inJar + ".bak");
            configData = StringUtils.replace(configData, "_#in#_", inJar);
            configData = StringUtils.replace(configData, "_#out#_", destNewFile.getAbsolutePath());
            File tmpFile = File.createTempFile("obfuscator_", "_" + System.currentTimeMillis() + ".xml");
            FileUtil.saveToFile(tmpFile, configData, "UTF8");

            RuntimeBatPromptModeUtil util = RuntimeBatPromptModeUtil.newInstance();
            util.command("cd " + jarDir);
            util.command("java  -cp  allatori.jar  com.allatori.Obfuscate  " + tmpFile);
            ProcessWatcher proc = ProcessWatcher.newInstance(util.apply());
            proc.getStreamSync();

            this.log("[InputStreamToString]" + proc.getInputStreamToString());
            this.log("[ErrorStreamToString]" + proc.getErrorStreamToString());

            File orignJar = new File(inJar);
            orignJar.delete();
            destNewFile.renameTo(orignJar);
            this.log("[orignJar]" + orignJar.exists());
            this.log("[destNewFile]" + destNewFile.exists());
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public String getJarDir() {
        return jarDir;
    }

    public void setJarDir(String jarDir) {
        this.jarDir = jarDir;
    }

    public String getInJar() {
        return inJar;
    }

    public void setInJar(String inJar) {
        this.inJar = inJar;
    }
}
