package gtu.ant;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
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

                    this.getProject().setProperty(folder.name, folder2.getAbsolutePath());
                }
                log("final目錄 : " + folder.name + "\t" + config.getParseAfterValue(folder.text));
            }
            this.log("[AntBaseFolderChanger]" + "---- end");
        } catch (Exception e) {
            throw new BuildException(e);
        }
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
