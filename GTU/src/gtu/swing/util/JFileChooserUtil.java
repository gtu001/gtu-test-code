package gtu.swing.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

/**
 * 檔案選擇工具, 簡化JFileChooser
 * 
 * 2012/1/6
 * 
 * @author Troy
 */
public class JFileChooserUtil {

    public static void main(String[] args) {
        System.out.println("done...");
    }

    private SelectOption selectOption;
    private FileSelectionMode fileSelectionMode;
    private JFileChooser fc;
    private boolean showDialog;
    
    private JFileChooserUtil() {
        this.fc = new JFileChooser();
//        fc.setCurrentDirectory(FileUtil.DESKTOP_DIR);//XXX
    }

    public static JFileChooserUtil newInstance() {
        return new JFileChooserUtil();
    }

    /**
     * 只能選擇檔案
     * 
     * @return
     */
    public JFileChooserUtil selectFileOnly() {
        this.fileSelectionMode = FileSelectionMode.FILES_ONLY;
        return this;
    }

    /**
     * 只能選擇目錄
     * 
     * @return
     */
    public JFileChooserUtil selectDirectoryOnly() {
        this.fileSelectionMode = FileSelectionMode.DIRECTORIES_ONLY;
        return this;
    }

    /**
     * 可選擇檔案與目錄
     * 
     * @return
     */
    public JFileChooserUtil selectFileAndDirectory() {
        this.fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
        return this;
    }

    /**
     * 設定可接受檔案類型
     * 
     * @param extension
     *            附檔名(Ex:jpg)
     * @param description
     *            附檔名的敘述(Ex:JPG圖檔)
     * @return
     */
    public JFileChooserUtil addAcceptFile(final String description, final String... extension) {
        for (int ii = 0; ii < extension.length; ii++) {
            extension[ii] = extension[ii].toLowerCase();
        }
        
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile() && containEndwiths(file, extension)) {
                    return true;
                }
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return description;
            }
            
            boolean containEndwiths(File file, String[] extension){
                String fileName = file.getName().toLowerCase();
                for(String ext : extension){
                    if(fileName.endsWith(ext)){
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 顯示儲存視窗
     * 
     * @return
     */
    public JFileChooserUtil showSaveDialog() {
        validateSelectModeBeforeShowDialog();
        int rtnVal = fc.showSaveDialog(null);
        this.selectOption = SelectOption.VALUE_TO_ENUM.get(rtnVal);
        return this;
    }

    /**
     * 顯示開啟視窗
     * 
     * @return
     */
    public JFileChooserUtil showOpenDialog() {
        validateSelectModeBeforeShowDialog();
        int rtnVal = fc.showOpenDialog(null);
        this.selectOption = SelectOption.VALUE_TO_ENUM.get(rtnVal);
        return this;
    }

    /**
     * 顯示自訂(標題/按鈕)視窗
     * 
     * @param titleAndBtn
     * @return
     */
    public JFileChooserUtil showDialog(String titleAndBtn) {
        validateSelectModeBeforeShowDialog();
        int rtnVal = fc.showDialog(null, titleAndBtn);
        this.selectOption = SelectOption.VALUE_TO_ENUM.get(rtnVal);
        return this;
    }
    
    private void validateIsBeforeShowDialog(String message){
        if(showDialog){
            throw new RuntimeException("設定參數["+message+"]必須在 showDialog/showOpenDialog/showSaveDialog 之前!");
        }
    }
    
    public JFileChooserUtil multiSelectionEnabled(){
        validateIsBeforeShowDialog("multiSelectionEnabled");
        fc.setMultiSelectionEnabled(true);
        return this;
    }
    
    public JFileChooserUtil multiSelectionDisabled(){
        validateIsBeforeShowDialog("multiSelectionDisabled");
        fc.setMultiSelectionEnabled(false);
        return this;
    }

    private void validateSelectModeBeforeShowDialog() {
        if (this.fileSelectionMode == null) {
            throw new RuntimeException("請執行 selectFileOnly/selectDirectoryOnly/selectFileAndDirectory其一!");
        }
        fc.setFileSelectionMode(fileSelectionMode.val);
        showDialog = true;
    }

    private void validateBeforeSelectedFile() {
        if (this.selectOption == null) {
            throw new RuntimeException("請執行 showDialog/showOpenDialog/showSaveDialog其一!");
        }
    }
    
    public JFileChooserUtil setCurrentDirectory(File file){
        validateIsBeforeShowDialog("setCurrentDirectory");
        fc.setCurrentDirectory(file);
        return this;
    }

    /**
     * 確認後取得所選的檔案
     * 
     * @return
     */
    public File getApproveSelectedFile() {
        validateBeforeSelectedFile();
        if (this.selectOption == JFileChooserUtil.SelectOption.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    /**
     * 確認後取得所選的檔案
     * 
     * @return
     */
    public File[] getApproveSelectedFiles() {
        validateBeforeSelectedFile();
        if (this.selectOption == JFileChooserUtil.SelectOption.APPROVE_OPTION) {
            return fc.getSelectedFiles();
        }
        return null;
    }

    private enum FileSelectionMode {
        FILES_ONLY(JFileChooser.FILES_ONLY), //
        DIRECTORIES_ONLY(JFileChooser.DIRECTORIES_ONLY), //
        FILES_AND_DIRECTORIES(JFileChooser.FILES_AND_DIRECTORIES), //
        ;
        private int val;

        FileSelectionMode(int val) {
            this.val = val;
        }
    }

    public static enum SelectOption {
        CANCEL_OPTION(JFileChooser.CANCEL_OPTION), //
        APPROVE_OPTION(JFileChooser.APPROVE_OPTION), //
        ERROR_OPTION(JFileChooser.ERROR_OPTION), //
        ;//

        private int value;

        private static Map<Integer, SelectOption> VALUE_TO_ENUM = new HashMap<Integer, SelectOption>();
        static {
            for (SelectOption s : SelectOption.values()) {
                VALUE_TO_ENUM.put(s.getValue(), s);
            }
        }

        SelectOption(int option) {
            this.value = option;
        }

        public int getValue() {
            return value;
        }
    }
}