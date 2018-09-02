package gtu._work.ui;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RenameUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JButton usePatternNewNameBtn;
    private JButton executeBtn;
    private JButton scanBtn;
    private JTextField renameRegexText;
    private JLabel jLabel2;
    private JTextField findFileRegexText;
    private JTable renameTable;
    private JPanel jPanel2;
    private JLabel jLabel1;
    private JTextField srcDirText;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RenameUI inst = new RenameUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public RenameUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u9078\u64c7\u76ee\u9304");
                    }
                    {
                        srcDirText = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcDirText, true);
                        jPanel1.add(srcDirText);
                        srcDirText.setPreferredSize(new java.awt.Dimension(470, 22));
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("\u6a94\u540dRegex");
                        jLabel2.setPreferredSize(new java.awt.Dimension(68, 15));
                    }
                    {
                        findFileRegexText = new JTextField();
                        jPanel1.add(findFileRegexText);
                        findFileRegexText.setPreferredSize(new java.awt.Dimension(446, 22));
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel1.add(jLabel3);
                        jLabel3.setText("\u4fee\u6539\u5f8c\u6a94\u540d");
                        jLabel3.setPreferredSize(new java.awt.Dimension(68, 15));
                    }
                    {
                        renameRegexText = new JTextField();
                        jPanel1.add(renameRegexText);
                        renameRegexText.setPreferredSize(new java.awt.Dimension(446,22));
                    }
                    {
                        scanBtn = new JButton();
                        jPanel1.add(scanBtn);
                        scanBtn.setText("\u6383\u63cf\u76ee\u9304");
                        scanBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                scanBtnActionPerformed();
                            }
                        });
                    }
                    {
                        usePatternNewNameBtn = new JButton();
                        jPanel1.add(usePatternNewNameBtn);
                        usePatternNewNameBtn.setText("\u5957\u7528\u6a94\u540d");
                        usePatternNewNameBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                usePatternNewNameBtnActionPerformed();
                            }
                        });
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u4ee5\u8868\u683c\u8a2d\u5b9a\u66f4\u6b63");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed();
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(552, 338));
                        {
                            renameTable = new JTable();
                            jScrollPane1.setViewportView(renameTable);
                            JTableUtil.defaultSetting(renameTable);
                            renameTable.setModel(createNewModel());
                        }
                    }
                }
            }
            pack();
            this.setSize(565, 392);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
//    private JButton executeBtn;
//    private JButton scanBtn;
//    private JTextField renameRegexText;
//    private JTextField findFileRegexText;
//    private JTable renameTable;
//    private JTextField srcDirText;
    private List<XFile> fileList = new ArrayList<XFile>();
    private void scanBtnActionPerformed(){
        try{
            File srcDir = JCommonUtil.filePathCheck(srcDirText.getText(), "改檔名目錄", true);
            if(srcDir.listFiles() == null || srcDir.listFiles().length == 0){
                JCommonUtil._jOptionPane_showMessageDialog_error("目錄下無任何檔案");
                return;
            }
            for(File f : srcDir.listFiles()){
                if(f.isFile()){
                    XFile file = new XFile();
                    file.file = f;
                    file.fileName = f.getName();
                    if(!fileList.contains(file)){
                        fileList.add(file);
                    }
                }
            }
            Collections.sort(fileList, new Comparator<XFile>() {
                @Override
                public int compare(XFile o1, XFile o2) {
                    return new Long(o1.file.lastModified()).compareTo(new Long(o2.file.lastModified()));
                }
            });
            createNewModel();
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    private DefaultTableModel createNewModel(){
        DefaultTableModel model = JTableUtil.createModel(false, "原檔名", "新檔名", "最後修改時間");
        for(XFile f : fileList){
            model.addRow(this.getXFile(f, null));
        }
        renameTable.setModel(model);
        return model;
    }
    private Object[] getXFile(XFile f, String newName){
        if(newName == null){
            newName = f.fileName;
        }
        return new Object[]{f, newName, DateFormatUtils.format(f.file.lastModified(), "yyyy/MM/dd HH:mm:ss")};
    }
    private void usePatternNewNameBtnActionPerformed() {
        try{
            String findFileRegex = Validate.notBlank(findFileRegexText.getText(), "沒填檔名Regex");
            String renameRegex = Validate.notBlank(renameRegexText.getText(), "沒填修改後檔名");
            Pattern renameRegexPattern = Pattern.compile("\\#(\\w+)\\#");
            Matcher matcher2 = null;
            
            Pattern findFileRegexPattern = Pattern.compile(findFileRegex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = null;
            DefaultTableModel model = JTableUtil.createModel(false, "原檔名", "新檔名", "最後修改時間");
            
            int ind = 1;
            for(XFile f : fileList){
                matcher = findFileRegexPattern.matcher(f.fileName);
                if(matcher.matches()){
                    StringBuffer sb = new StringBuffer();
                    matcher2 = renameRegexPattern.matcher(renameRegex);
                    while(matcher2.find()){
                        String val = matcher2.group(1);
                        if(val.matches("\\d+L")){
                            int index = Integer.parseInt(val.substring(0, val.length()-1));
                            matcher2.appendReplacement(sb, DateFormatUtils.format(Long.parseLong(matcher.group(index)), "yyyyMMdd_HHmmss"));
                        }else if(val.equalsIgnoreCase("date")){
                            matcher2.appendReplacement(sb, DateFormatUtils.format(f.file.lastModified(), "yyyyMMdd_HHmmss"));
                        }else if(val.equalsIgnoreCase("serial")){
                            matcher2.appendReplacement(sb, String.valueOf(ind++));
                        }else if(StringUtils.isNumeric(val)){
                            int index = Integer.parseInt(val);
                            matcher2.appendReplacement(sb, matcher.group(index));
                        }
                    }
                    matcher2.appendTail(sb);
                    model.addRow(this.getXFile(f, sb.toString()));
                }
            }
            renameTable.setModel(model);
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    private void executeBtnActionPerformed(){
        try{
            JTableUtil util = JTableUtil.newInstance(renameTable);
            List<Integer> ylist = util.getTableColumnModelIndex_realRowIndex();
            int xfileColIndex = -1;
            int renameColIndex = -1;
            Map<Object, TableColumn> columnModel = util.getTableColumnModel();
            for(Object obj : columnModel.keySet()){
                String key = (String)obj;
                System.out.println(key + " -> " + columnModel.get(key).getModelIndex());
                if(key.equals("原檔名")){
                    xfileColIndex = columnModel.get(key).getModelIndex();
                }else if(key.equals("新檔名")){
                    renameColIndex = columnModel.get(key).getModelIndex();
                }
            }
            for (int yy = 0; yy < ylist.size(); yy++) {
                int y = ylist.get(yy);
                XFile file = (XFile)renameTable.getValueAt(y, xfileColIndex);
                String renameFileName = (String)renameTable.getValueAt(y, renameColIndex);
                file.file.renameTo(new File(file.file.getParent(), renameFileName));
                System.out.println(file.fileName + " -> " + renameFileName);
            }
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    static class XFile {
        File file;
        String fileName;
        public String toString(){
            return fileName;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((file == null) ? 0 : file.hashCode());
            result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            XFile other = (XFile) obj;
            if (file == null) {
                if (other.file != null)
                    return false;
            } else if (!file.equals(other.file))
                return false;
            if (fileName == null) {
                if (other.fileName != null)
                    return false;
            } else if (!fileName.equals(other.fileName))
                return false;
            return true;
        }
    }
}
