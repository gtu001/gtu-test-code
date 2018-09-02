package gtu.log.finder;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ExceptionLogFinderUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane2;
    private JLabel jLabel5;
    private JTextField startStrText;
    private JCheckBox isExceptionLogChkBox;
    private JTextField setParseFileText;
    private JLabel jLabel4;
    private JScrollPane jScrollPane1;
    private JButton searchTextBtn;
    private JTextField endStrText;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JTextField searchText;
    private JTextField srcFilePathText;
    private JLabel jLabel3;
    private JCheckBox ignoreCaseCheckBox;
    private JTextField preLineText;
    private JButton executeBtn;
    private JList searchMatchList;
    private JPanel jPanel4;
    private JTextArea logDetailTextArea;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExceptionLogFinderUI inst = new ExceptionLogFinderUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public ExceptionLogFinderUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            JCommonUtil.defaultLookAndFeel();
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("設定來源log檔案", null, jPanel1, null);
                    {
                        jPanel4 = new JPanel();
                        jPanel1.add(jPanel4, BorderLayout.NORTH);
                        jPanel4.setPreferredSize(new java.awt.Dimension(689, 115));
                        {
                            isExceptionLogChkBox = new JCheckBox();
                            jPanel4.add(isExceptionLogChkBox);
                            isExceptionLogChkBox.setText("\u53ea\u627e\u932f\u8aa4");
                        }
                        {
                            jLabel1 = new JLabel();
                            jPanel4.add(jLabel1);
                            jLabel1.setText("\u8cbc\u4e0alog\u6a94\u8def\u5f91");
                        }
                        {
                            srcFilePathText = new JTextField();
                            jPanel4.add(srcFilePathText);
                            srcFilePathText.setPreferredSize(new java.awt.Dimension(292, 22));
                            srcFilePathText.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    srcFilePathTextSetFileAction(evt);
                                }
                            });
                        }
                        {
                            jLabel3 = new JLabel();
                            jPanel4.add(jLabel3);
                            jLabel3.setText("\u524d\u7f6e\u884c\u6578");
                        }
                        {
                            preLineText = new JTextField();
                            jPanel4.add(preLineText);
                            preLineText.setText("5");
                            preLineText.setPreferredSize(new java.awt.Dimension(33,22));
                        }
                        {
                            executeBtn = new JButton();
                            jPanel4.add(executeBtn);
                            executeBtn.setText("\u57f7\u884c\u89e3\u6790");
                            executeBtn.setPreferredSize(new java.awt.Dimension(107, 22));
                            executeBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    executeBtnAction();
                                }
                            });
                        }
                        {
                            jLabel4 = new JLabel();
                            jPanel4.add(jLabel4);
                            jLabel4.setText("\u76f4\u63a5\u8a2d\u5b9a\u89e3\u6790\u6a94\u8def\u5f91");
                            jLabel4.setPreferredSize(new java.awt.Dimension(119, 15));
                        }
                        {
                            setParseFileText = new JTextField();
                            jPanel4.add(setParseFileText);
                            setParseFileText.setPreferredSize(new java.awt.Dimension(507, 22));
                            setParseFileText.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    setParseFileBtnAction(evt);
                                }
                            });
                        }
                        {
                            jLabel2 = new JLabel();
                            jPanel4.add(jLabel2);
                            jLabel2.setText("\u641c\u5c0b\u95dc\u9375\u5b57");
                        }
                        {
                            searchText = new JTextField();
                            jPanel4.add(searchText);
                            searchText.setPreferredSize(new java.awt.Dimension(355, 22));
                        }
                        {
                            ignoreCaseCheckBox = new JCheckBox();
                            jPanel4.add(ignoreCaseCheckBox);
                            ignoreCaseCheckBox.setText("\u7121\u8996\u5927\u5c0f\u5beb");
                            ignoreCaseCheckBox.setSelected(true);
                        }
                        {
                            searchTextBtn = new JButton();
                            jPanel4.add(searchTextBtn);
                            searchTextBtn.setText("搜尋");
                            searchTextBtn.setPreferredSize(new java.awt.Dimension(118,22));
                            searchTextBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    searchTextBtnAction();
                                }
                            });
                        }
                        {
                            jLabel5 = new JLabel();
                            jPanel4.add(jLabel5);
                            jLabel5.setText("\u4ee5\u958b\u59cb\u7d50\u675f\u95dc\u9375\u5b57\u89e3\u6790\u6587\u4ef6");
                            jLabel5.setPreferredSize(new java.awt.Dimension(155, 15));
                        }
                        {
                            startStrText = new JTextField();
                            jPanel4.add(startStrText);
                            startStrText.setPreferredSize(new java.awt.Dimension(229, 22));
                        }
                        {
                            endStrText = new JTextField();
                            jPanel4.add(endStrText);
                            endStrText.setPreferredSize(new java.awt.Dimension(244, 22));
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(689, 391));
                        {
                            DefaultListModel searchMatchListModel = new DefaultListModel();
                            searchMatchList = new JList();
                            jScrollPane1.setViewportView(searchMatchList);
                            searchMatchList.setModel(searchMatchListModel);
                            searchMatchList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    searchMatchListMouseAction(evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("詳細內容", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(689, 484));
                        {
                            logDetailTextArea = new JTextArea();
                            jScrollPane2.setViewportView(logDetailTextArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(702, 538);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }

    File outputLogFile;

    private void executeBtnAction() {
        try{
            String srcFilePath = srcFilePathText.getText();
            int preLine = 5;
            try {
                preLine = Integer.parseInt(preLineText.getText());
            } catch (Exception ex) {
                JCommonUtil._jOptionPane_showMessageDialog_error("前置行數必須為數值");
                return;
            }
            if (StringUtils.isBlank(srcFilePath)) {
                JCommonUtil._jOptionPane_showMessageDialog_error("未輸入log檔路徑");
                return;
            }
            File srcFile = new File(srcFilePath);
            String middleFileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
            
            String searchStr = StringUtils.defaultString(searchText.getText());
            String startStr = StringUtils.defaultString(startStrText.getText());
            String endStr = StringUtils.defaultString(endStrText.getText());
            
            Map<String,Object> valueMap = new HashMap<String,Object>();
            valueMap.put("preLine", preLine);
            valueMap.put("middleFileName", middleFileName);
            valueMap.put("srcFile", srcFile);
            valueMap.put("searchStr", searchStr);
            valueMap.put("startStr", startStr);
            valueMap.put("endStr", endStr);
            
            if(isExceptionLogChkBox.isSelected()){
                outputLogFile = ActionType.Exception.process(valueMap);
            }else if(StringUtils.isNotBlank(startStr) && StringUtils.isNotBlank(endStr)){
                outputLogFile = ActionType.StartEnd.process(valueMap);
            }else{
                outputLogFile = ActionType.KeyWord.process(valueMap);
            }
            
            setParseFileText.setText(outputLogFile.getAbsolutePath());
            JCommonUtil._jOptionPane_showMessageDialog_info("分析完成:"+outputLogFile.getName()+"\n路徑:"+outputLogFile.getAbsolutePath());            
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    enum ActionType {
        Exception {
            @Override
            File process(Map<String,Object> valueMap) {
                ExceptionLogFinder finder = new ExceptionLogFinder();
                finder.beforeLine = (Integer)valueMap.get("preLine");
                valueMap.get("preLine");
                finder.outputLogFile = new File(FileUtil.DESKTOP_DIR, "errorFinder_"+valueMap.get("middleFileName")+".log");
                finder.execute((File)valueMap.get("srcFile"));
                if(!finder.outputLogFile.exists()){
                    Validate.isTrue(false, "未成功產生分析檔");
                }
                return finder.outputLogFile;
            }
        },KeyWord {
            @Override
            File process(Map<String,Object> valueMap) {
                if(StringUtils.isBlank((String)valueMap.get("searchStr"))){
                    Validate.isTrue(false, "未輸入搜尋關鍵字");
                }
                LogFinder finder = new LogFinder();
                finder.patternStr = (String)valueMap.get("searchStr");
                finder.beforeLine = (Integer)valueMap.get("preLine");
                finder.afterLine = (Integer)valueMap.get("preLine");
                finder.outputLogFile = new File(FileUtil.DESKTOP_DIR, "logFinder_"+valueMap.get("middleFileName")+".log");
                finder.execute((File)valueMap.get("srcFile"));
                if(!finder.outputLogFile.exists()){
                    Validate.isTrue(false, "未成功產生分析檔");
                }
                return finder.outputLogFile;
            }
        },StartEnd {
            @Override
            File process(Map<String,Object> valueMap) {
                if(StringUtils.isBlank((String)valueMap.get("startStr"))){
                    Validate.isTrue(false, "未輸入開始關鍵字");
                }
                if(StringUtils.isBlank((String)valueMap.get("endStr"))){
                    Validate.isTrue(false, "未輸入結束關鍵字");
                }
                LogStartEndFinder finder = new LogStartEndFinder();
                finder.startPatternStr = (String)valueMap.get("startStr");
                finder.endPatternStr = (String)valueMap.get("endStr");
                finder.outputLogFile = new File(FileUtil.DESKTOP_DIR, "logStartEndFinder_"+valueMap.get("middleFileName")+".log");
                finder.execute((File)valueMap.get("srcFile"));
                return finder.outputLogFile;
            }
        };
        ActionType(){}
        abstract File process(Map<String,Object> valueMap);
    }
    
    private File getParseFile(){
        String parseFilePath = setParseFileText.getText();
        if(StringUtils.isNotBlank(parseFilePath)){
            File pareFile = new File(parseFilePath);
            if(pareFile.exists() && pareFile.isFile()){
                return pareFile;
            }
        }
        return null;
    }
    
    private void searchTextBtnAction() {
        File parseFile = getParseFile();
        if(outputLogFile == null || !outputLogFile.exists()){
            if(parseFile != null){
                outputLogFile = parseFile;
            }else{
                JCommonUtil._jOptionPane_showMessageDialog_error("分析檔不存在");
                return;
            }
        }
        String searchTxt = searchText.getText();
        System.out.println("findText ==> " + searchTxt);
        try{
            Pattern startPtn = Pattern.compile("區段(\\d+)[↓]+");
            Pattern searchTextPtn = Pattern.compile(searchTxt, Pattern.CASE_INSENSITIVE);
            if(!ignoreCaseCheckBox.isSelected()){
                searchTextPtn = Pattern.compile(searchTxt);
            }
            
            List<ExceptionSection> exceptionSecList = new ArrayList<ExceptionSection>();
            String tempSecStr = null;
            Matcher matcher = null;
            List<String> markLineSectionNumberList = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputLogFile), "utf8"));
            for(String line = null; (line = reader.readLine())!= null ; ){
                matcher = startPtn.matcher(line);
                if(matcher.find()){
                    tempSecStr = matcher.group(1);
                }
                if(tempSecStr == null){
                    continue;
                }
                String tmpLine = line.replaceFirst("[\\d]+\\:", "");
                tmpLine = tmpLine.replaceFirst("\\([\\d]+\\)\\:", "");
                if(tmpLine.contains(searchTxt) || searchTextPtn.matcher(tmpLine).find()){
                    System.out.println("\ttempSecStr => "+tempSecStr);
                    System.out.println("\ttmpLine => "+tmpLine);
                    if(!markLineSectionNumberList.contains(tempSecStr)){
                        ExceptionSection es = new ExceptionSection();
                        es.line = line;
                        es.sectionNumber = Integer.parseInt(tempSecStr);
                        exceptionSecList.add(es);
                        System.out.println(es);
                        markLineSectionNumberList.add(tempSecStr);
                        tempSecStr = null;
                    }
                }else{
                    System.out.println("找沒:"+tmpLine);
                }
            }
            reader.close();
            
            DefaultListModel searchMatchListModel = new DefaultListModel();
            for(int ii = 0; ii < exceptionSecList.size() ; ii++){
                ExceptionSection es = exceptionSecList.get(ii);
                searchMatchListModel.addElement(es);
            }
            searchMatchList.setModel(searchMatchListModel);
            if(exceptionSecList.isEmpty()){
                JCommonUtil._jOptionPane_showMessageDialog_info("找不到符合字串!");
            }
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    private void searchMatchListMouseAction(MouseEvent evt){
        try{
            if(JMouseEventUtil.buttonLeftClick(2, evt)){
                ExceptionSection es = (ExceptionSection)JListUtil.getLeadSelectionObject(searchMatchList);
                if(!outputLogFile.exists()){
                    JCommonUtil._jOptionPane_showMessageDialog_error("分析檔不存在:"+outputLogFile.getName());
                    return;
                }
                Pattern startPtn = Pattern.compile("區段"+es.sectionNumber+"[↓]+");
                Pattern endPtn = Pattern.compile("區段"+es.sectionNumber+"[↑]+");
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputLogFile), "utf8"));
                boolean startOk = false;
                for (String line = null; (line = reader.readLine()) != null;) {
                    if(startPtn.matcher(line).find()){
                        startOk = true;
                    }
                    if(startOk){
                        sb.append(line + "\n");
                    }
                    if(endPtn.matcher(line).find()){
                        break;
                    }
                }
                reader.close();
                logDetailTextArea.setText(sb.toString());
            }
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    private void setParseFileBtnAction(MouseEvent evt) {
        if(JMouseEventUtil.buttonLeftClick(2, evt)){
            outputLogFile = JCommonUtil._jFileChooser_selectFileOnly();
            if(outputLogFile == null){
                JCommonUtil._jOptionPane_showMessageDialog_error("選擇檔案不正確");
                return;
            }
            setParseFileText.setText(outputLogFile.getAbsolutePath());
        }
    }
    
    private void srcFilePathTextSetFileAction(MouseEvent evt){
        if(JMouseEventUtil.buttonLeftClick(2, evt)){
            File file = JCommonUtil._jFileChooser_selectFileOnly();
            if(file == null){
                JCommonUtil._jOptionPane_showMessageDialog_error("選擇檔案不正確");
                return;
            }
            srcFilePathText.setText(file.getAbsolutePath());
        }
    }

    static class ExceptionSection {
        int sectionNumber;
        String line;
        @Override
        public String toString() {
            return "[區段" + sectionNumber + "]:" + line;
        }
    }
}