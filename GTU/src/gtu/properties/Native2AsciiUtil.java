package gtu.properties;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.Validate;

public class Native2AsciiUtil {

    public static void main(String[] args) throws IOException {
        // String srcDir = "C:\\RIS3";// 來源目錄
        // String destDir = FileUtil.DESKTOP_PATH + "RIS3";// 目的目錄
        // Native2AsciiUtil.newInstance().srcDir(new File(srcDir)).destDir(new
        // File(destDir)).reverse().execute();

        File srcFile = new File("I:\\workstuff\\workspace_scsb\\SCSB_CCBill_AP\\src\\main\\java\\com\\fuco\\mb\\bill\\ASConstants.java");
        File destFile = new File(FileUtil.DESKTOP_PATH, "test.java");
        // System.out.println(Mode.ENCODING.apply(srcFile, destFile));

        String val = FileUtil.loadFromFile(srcFile, "utf8");
        val = val.replace("\ufeff", "");
        FileUtil.saveToFile(destFile, val, "utf8");

        System.out.println("done...");
    }

    public static Native2AsciiUtil newInstance() {
        return new Native2AsciiUtil();
    }

    private Native2AsciiUtil() {
        init();
    }

    List<File> files;
    Set<File> mkdirs;
    private File srcDir;
    private File destDir;
    private Mode mode;
    private static final String JAVAHOME_BIN;

    static {
        String javaHomeBin = null;
        if (new File(System.getenv("JAVA_HOME")).exists()) {
            javaHomeBin = System.getenv("JAVA_HOME") + File.separator + "bin";
        }
        JAVAHOME_BIN = javaHomeBin;
    }

    public Native2AsciiUtil srcDir(File srcDir) {
        this.srcDir = srcDir;
        return this;
    }

    public Native2AsciiUtil destDir(File destDir) {
        this.destDir = destDir;
        return this;
    }

    public Native2AsciiUtil execute() throws IOException, TimeoutException {
        Validate.notNull(srcDir);
        Validate.notNull(destDir);
        Validate.isTrue(srcDir.exists());
        // Validate.isTrue(destDir.exists());
        Validate.notNull(mode);
        addFile(srcDir);
        StringBuilder sb = new StringBuilder();
        for (File f : mkdirs) {
            sb.append("mkdir " + getDestFilePath(srcDir, f, destDir).getAbsolutePath() + "\r\n");
        }
        sb.append("cd " + JAVAHOME_BIN + "\r\n");
        for (File f : files) {
            File dest = this.getDestFilePath(srcDir, f, destDir);
            sb.append(mode.apply(f, dest) + "\r\n");
        }
        File createBat = File.createTempFile("Native2Ascii", ".bat");
        System.out.println("bat : " + createBat);
        FileUtil.saveToFile(createBat, sb.toString().getBytes());
        ProcessWatcher.newInstance(Runtime.getRuntime().exec("cmd /c start " + createBat.getAbsolutePath())).getStream();
        createBat.deleteOnExit();
        return this;
    }

    private void init() {
        files = new ArrayList<File>();
        mkdirs = new HashSet<File>();
    }

    private void addFile(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                addFile(f);
            }
        } else {
            if (file.getName().toLowerCase().endsWith(".properties")) {
                files.add(file);
                mkdirs.add(file.getParentFile());
            }
        }
    }

    private File getDestFilePath(File srcBaseDir, File srcFile, File destBaseDir) {
        Validate.isTrue(srcFile.getAbsolutePath().startsWith(srcBaseDir.getAbsolutePath()), "srcFile not include srcBaseDir path!");
        return new File(destBaseDir.getAbsolutePath() + "\\" + srcFile.getAbsolutePath().substring(srcBaseDir.getAbsolutePath().length()));
    }

    public Native2AsciiUtil encoding() {
        mode = Mode.ENCODING;
        return this;
    }

    public Native2AsciiUtil reverse() {
        mode = Mode.REVERSE;
        return this;
    }

    enum Mode {
        // native2ascii [-reverse] [-encoding encoding] [inputfile [outputfile]]
        ENCODING("native2ascii -encoding %s %s %s") {
            String apply(File srcFile, File toFile) {
                return String.format(value, "UTF8", srcFile.getAbsolutePath(), toFile.getAbsolutePath());
            }
        },
        REVERSE("native2ascii -reverse %s %s") {
            String apply(File srcFile, File toFile) {
                return String.format(value, srcFile.getAbsolutePath(), toFile.getAbsolutePath());
            }
        };

        final String value;

        Mode(String value) {
            this.value = value;
        }

        abstract String apply(File srcFile, File toFile);
    }
}
