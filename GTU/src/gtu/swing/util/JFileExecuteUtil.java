package gtu.swing.util;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;

public class JFileExecuteUtil {

    public static void main(String[] args) {
        System.out.println(Extension.valueOf("JAR").getClass());
        System.out.println("done...");
    }

    final File file;

    private JFileExecuteUtil(File file) {
        this.file = file;
    }

    public static JFileExecuteUtil newInstance(File file) {
        return new JFileExecuteUtil(file);
    }

    public List<JMenuItem> createDefaultJMenuItems() {
        List<JMenuItem> list = new ArrayList<JMenuItem>();
        if (file == null) {
            return list;
        }
        if (!file.exists()) {
            File tmpFile = new File(file.getAbsolutePath());
            for (; !tmpFile.exists();) {
                tmpFile = tmpFile.getParentFile();
            }
            if (tmpFile.isFile()) {
                this.ifFileIsFile_addDefaultJMenuItem(tmpFile, list);
            }

            JMenuItem item = new JMenuItem();
            item.setText("create dir : " + file);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        file.mkdirs();
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            list.add(item);
            JMenuItem item1 = new JMenuItem();
            item1.setText("create file : " + file);
            item1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        File parent = file.getParentFile();
                        System.out.println("parent : " + parent);
                        if (!parent.exists()) {
                            System.out.println("mkdir : " + parent);
                            parent.mkdirs();
                        }
                        file.createNewFile();
                        Desktop.getDesktop().open(file.getParentFile());
                    } catch (IOException ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            list.add(item1);
            return list;
        }
        if (file.isDirectory()) {
            {
                JMenuItem item = new JMenuItem();
                item.setText("open dir");
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                list.add(item);
            }
            {
                list.add(createRenameMenuItem(file, true));
            }
            {
                JMenuItem item = new JMenuItem();
                item.setText("remove empty dir");
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        List<File> list = new ArrayList<File>();
                        FileUtil.deleteEmptyDir(file, list);
                        JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog("delete empty dir : \n" + list.toString().replace(',', '\n'), "DELETE");
                    }
                });
                list.add(item);
            }
        } else {
            this.ifFileIsFile_addDefaultJMenuItem(file, list);
        }
        return list;
    }

    void ifFileIsFile_addDefaultJMenuItem(final File file, List<JMenuItem> list) {
        final String fileName = file.getName().toLowerCase();
        Extension ext = Extension.valueOfFileExtension(fileName);
        System.out.println("1.Extension == " + ext);
        if (ext != null) {
            JMenuItem item = null;
            for (final Openner op : ext.type) {
                item = new JMenuItem();
                item.setText("file exec : " + op.desc);
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        try {
                            String command = String.format("cmd /c call \"%s\" \"%s\"", op.executer, file);
                            System.out.println(command);
                            Runtime.getRuntime().exec(command);
                        } catch (IOException ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                list.add(item);
            }
            {
                item = new JMenuItem();
                item.setText("file exec : " + " 預設開啟方式 ");
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        try {
                            String command = String.format("cmd /c call \"%s\"", file);
                            System.out.println(command);
                            Runtime.getRuntime().exec(command);
                        } catch (IOException ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                list.add(item);
            }
        }
        {
            JMenuItem item = new JMenuItem();
            item.setText("file open target dir");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        Desktop.getDesktop().open(file.getParentFile());
                    } catch (IOException ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            list.add(item);
        }
        {
            JMenuItem item = new JMenuItem();
            item.setText("file move to");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        File moveToDir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                        if (moveToDir == null) {
                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file not correct!", "ERROR");
                            return;
                        }
                        if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo()
                                .showConfirmDialog("are you sure move file to \n" + moveToDir, "MOVE")) {
                            return;
                        }
                        File moveToFile = null;
                        file.renameTo(moveToFile = new File(moveToDir, file.getName()));
                        JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog("move to\n" + moveToFile, "MOVE");
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            list.add(item);
        }
        {
            JMenuItem item = new JMenuItem();
            item.setText("file copy to");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    try {
                        File copyToDir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                        if (copyToDir == null) {
                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file not correct!", "ERROR");
                            return;
                        }
                        if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo()
                                .showConfirmDialog("are you sure copy file to \n" + copyToDir, "COPY")) {
                            return;
                        }
                        File copyToFile = new File(copyToDir, file.getName());
                        System.out.println("CopyFromTo : " + file + " -> " + copyToFile);
                        FileUtil.copyFile(file, copyToFile);
                        boolean copyOk = (copyToFile.exists() && copyToFile.length() == file.length());
                        JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog((copyOk ? "successd" : "failed") + " copy to\n" + copyToFile, "COPY");
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            list.add(item);
        }
        {
            list.add(createRenameMenuItem(file, false));
        }
        {
            JMenuItem item = new JMenuItem();
            item.setText("file delete");
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {
                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo()
                            .showConfirmDialog("are you sure delete file?\n" + file, "DELETE")) {
                        return;
                    }
                    boolean result = file.delete();
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("delete file " + (result ? "successd" : "failed"), "DELETE");
                }
            });
            list.add(item);
        }
    }

    JMenuItem createRenameMenuItem(final File file, final boolean isDir) {
        JMenuItem item = new JMenuItem();
        item.setText((file.isDirectory() ? "dir" : "file") + " rename : " + file.getName());
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                String newFileName = null;
                if (StringUtils.isBlank((newFileName = (String) JOptionPaneUtil.newInstance().showInputDialog("input new filename", "RENAME", file.getName())))) {
                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("can't rename empty!!", "ERROR");
                    return;
                }
                String subName = "";
                if (!isDir) {
                    subName = file.getName().toString();
                    if (subName.indexOf(".") == -1) {
                        subName.substring(subName.lastIndexOf("."));
                    } else {
                        subName = "";
                    }
                }
                File nfile = null;
                file.renameTo(nfile = new File(file.getParent(), newFileName + subName));
                JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog("rename to \n" + nfile, "RENAME");
            }
        });
        return item;
    }

    enum Extension {
        TXT("\\.txt$", Openner.MAD_EDIT), //
        JAVA("\\.java$", Openner.MAD_EDIT, Openner.ECLIPSE_HOME, Openner.ECLIPSE_COMPANY), //
        LOG("\\.log$", Openner.MAD_EDIT), //
        XHTML("\\.xhtml$", Openner.MAD_EDIT, Openner.ECLIPSE_HOME, Openner.ECLIPSE_COMPANY), //
        HTML("\\.html$", Openner.MAD_EDIT, Openner.FIREFOX), //
        JS("\\.js$", Openner.MAD_EDIT, Openner.ECLIPSE_HOME, Openner.ECLIPSE_COMPANY), //
        PROPERTIES("\\.properties$", Openner.MAD_EDIT, Openner.ECLIPSE_HOME, Openner.ECLIPSE_COMPANY), //
        XML("\\.xml$", Openner.MAD_EDIT, Openner.ECLIPSE_HOME, Openner.ECLIPSE_COMPANY), //
        ZIP("\\.zip$", Openner._7Z_ZIP), //
        _7Z("\\.7z$", Openner._7Z_ZIP), //
        RAR("\\.rar$", Openner._7Z_ZIP), //
        TAR("\\.tar$", Openner._7Z_ZIP), //
        JAR("\\.jar$", Openner.JD_JAR, Openner._7Z_ZIP), //
        DOC("\\.docx?$", Openner.OFFICE_WORD), //
        XLS("\\.xlsx?$", Openner.OFFICE_EXCEL), //
        BAT("\\.bat?$", Openner.MAD_EDIT), //
        UNKNOW("\\.XXX$", Openner.DEFAULT), //
        ;

        final Pattern extension;
        final Openner[] type;

        Extension(String extension, Openner... type) {
            this.extension = Pattern.compile(extension);
            this.type = type;
        }

        static Extension valueOfFileExtension(String fileName) {
            Matcher matcher = null;
            for (Extension ex : Extension.values()) {
                matcher = ex.extension.matcher(fileName);
                if (matcher.find()) {
                    return ex;
                }
            }
            return null;
        }
    }

    public static PropertiesUtilBean executeConfig = new PropertiesUtilBean(JFileExecuteUtil.class);
    private static final String Path_MAD_EDIT;
    private static final String Path_WINWORD;
    private static final String Path_EXCEL;
    private static final String Path_FIREFOX;
    private static final String Path_JD_GUI;
    private static final String Path_ECLIPSE;
    private static final String Path_ECLIPSE_COMPANY;
    private static final String Path_7Z;

    static {
        Properties prop = executeConfig.getConfigProp();
        Path_MAD_EDIT = prop.getProperty("MAD_EDIT");
        Path_WINWORD = prop.getProperty("WINWORD");
        Path_7Z = prop.getProperty("7Z");
        Path_EXCEL = prop.getProperty("EXCEL");
        Path_FIREFOX = prop.getProperty("FIREFOX");
        Path_JD_GUI = prop.getProperty("JD_GUI");
        Path_ECLIPSE = prop.getProperty("ECLIPSE");
        Path_ECLIPSE_COMPANY = prop.getProperty("ECLIPSE_COMPANY");
    }

    enum Openner {
        MAD_EDIT("madEdit", Path_MAD_EDIT), //
        _7Z_ZIP("7z", Path_7Z), //
        OFFICE_WORD("word", Path_WINWORD), //
        OFFICE_EXCEL("excel", Path_EXCEL), //
        FIREFOX("firefox", Path_FIREFOX), //
        JD_JAR("jdGui", Path_JD_GUI), //
        DEFAULT("default", " notepad "), //
        ECLIPSE_HOME("eclipse home", Path_ECLIPSE), //
        ECLIPSE_COMPANY("eclipse company", Path_ECLIPSE_COMPANY),//
        ;

        final String desc;
        final String executer;

        Openner(String desc, String executer) {
            this.desc = desc;
            this.executer = executer;
        }

        public String getDesc() {
            return desc;
        }

        public String getExecuter() {
            return executer;
        }
    }
}
