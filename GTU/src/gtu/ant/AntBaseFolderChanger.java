package gtu.ant;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import gtu.number.RandomUtil;

/**
 * 切換目錄用
 */
public class AntBaseFolderChanger extends Task {

    private AntConfigHelper config;

    /** <FolderPlaceholder></FolderPlaceholder> */
    private Set<FolderPlaceholder> mFolderPlaceholders = new LinkedHashSet<FolderPlaceholder>();

    @Override
    public void execute() throws BuildException {
        try {
            config = AntConfigHelper.of(this.getProject());
            this.log("[AntBaseFolderChanger]" + "---- start");

            for (FolderPlaceholder folder : mFolderPlaceholders) {
                String folder1 = config.getParseAfterValue(folder.text);
                log("init 目錄 : " + folder.name + "\t" + config.getParseAfterValue(folder.text));
                File folder2 = new File(folder1);
                if (folder2.exists()) {
                    do {
                        String suffix = RandomUtil.randomStr(5);
                        folder2 = new File(folder2.getParent(), folder2.getName() + "_" + suffix);
                    } while (folder2.exists());
                    log("folder 目錄已存在建立新暫存目錄! : " + folder2);

                    this.getProject().setUserProperty(folder.name, folder2.getAbsolutePath());
                    this.getProject().setNewProperty(folder.name, folder2.getAbsolutePath());
                    this.getProject().setProperty(folder.name, folder2.getAbsolutePath());
                    System.setProperty(folder.name, folder2.getAbsolutePath());
                }

                // 建立目錄
                String mkdirPath = config.getParseAfterValue(folder.text);
                new File(mkdirPath).mkdirs();

                log("final目錄 : " + folder.name + "\t" + mkdirPath);
            }

            this.log("[AntBaseFolderChanger]" + "---- end");
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void showInfoMap(Hashtable tab) {
        log("#-----------------------------------------------------------Start");
        for (Enumeration enu = tab.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            Object val = tab.get(key);
            log("\t" + key + "\t" + val);
        }
        log("#-----------------------------------------------------------End");
    }

    public FolderPlaceholder createFolderPlaceholder() {
        FolderPlaceholder mFolderPlaceholder = new FolderPlaceholder();
        mFolderPlaceholders.add(mFolderPlaceholder);
        return mFolderPlaceholder;
    }

    public class FolderPlaceholder {
        private String text;
        private String name;

        public void addText(String text) {
            this.text = text;
        }

        public void setName(String name) {
            this.name = name;
        }

        private AntBaseFolderChanger getOuterType() {
            return AntBaseFolderChanger.this;
        }
    }
}
