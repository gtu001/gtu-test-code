package gtu._work.eclipse.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 將PlugIn目錄裡所有目錄產生*.link文件於eclipse\dropins\*.link
 * 
 * @author Troy
 * 
 *         2012/1/6
 */
public class EclipsePlugInLinkMaker {

    private File eclipseDir;

    private File plugInDir;

    private static final String DROP_IN_PATH = "\\dropins\\";

    // 匯出前先清除dropins裡的link檔
    private static final boolean BEFORE_COPY_DELETE = true;

    private StringBuffer linkLog = new StringBuffer();

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        EclipsePlugInLinkMaker test = new EclipsePlugInLinkMaker();
        test.setEclipseDir(new File("C:\\資拓宏宇相關檔案\\iisi_eclipse"));
        // test.setEclipseDir(new File("C:\\資拓宏宇相關檔案\\eclipse"));
        test.setPlugInDir(new File("C:\\MyPlugIn"));
        test.execute();
        System.out.println("done...");
    }

    public void execute() throws IOException {
        this.validateFile(eclipseDir);
        this.validateFile(plugInDir);

        beforeCopyDeleteLinks();

        String destPath = eclipseDir.getAbsolutePath() + DROP_IN_PATH;
        for (File f : plugInDir.listFiles()) {
            if (f.isDirectory()) {
                String path = f.getAbsolutePath();
                File writeTo = new File(destPath + f.getName() + ".link");
                path = "path=" + path.replaceAll("\\\\", "/");
                // System.out.println(path);
                System.out.println("write to = " + writeTo.getAbsolutePath());
                linkLog.append(writeTo.getName() + "\r\n");
                this.writeFile(path, writeTo);
            }
        }
    }

    private void beforeCopyDeleteLinks() {
        if (BEFORE_COPY_DELETE) {
            File file = new File(eclipseDir.getAbsolutePath() + DROP_IN_PATH);
            File[] links = file.listFiles(new FileFilter() {
                public boolean accept(File arg0) {
                    return arg0.getName().endsWith(".link");
                }
            });
            for (File del : links) {
                boolean result = del.delete();
                System.out.println(del.getAbsolutePath() + "\t 複製前先清除舊link ==> 刪除 : " + (result ? "成功" : "失敗"));

            }
        }
    }

    private void writeFile(String writeLine, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        writer.write(writeLine);
        writer.close();
    }

    private void validateFile(File file) {
        if (plugInDir == null) {
            throw new RuntimeException("plugInDir 不可為null");
        }
        if (!plugInDir.exists()) {
            throw new RuntimeException("plugInDir 檔案不存在");
        }
        if (!plugInDir.isDirectory()) {
            throw new RuntimeException("plugInDir 所選擇非目錄");
        }
    }

    public File getEclipseDir() {
        return eclipseDir;
    }

    public void setEclipseDir(File eclipseDir) {
        this.eclipseDir = eclipseDir;
    }

    public File getPlugInDir() {
        return plugInDir;
    }

    public void setPlugInDir(File plugInDir) {
        this.plugInDir = plugInDir;
    }

    public StringBuffer getLinkLog() {
        return linkLog;
    }

    public void setLinkLog(StringBuffer linkLog) {
        this.linkLog = linkLog;
    }
}
