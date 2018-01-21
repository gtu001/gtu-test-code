package gtu._work.ui;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


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
public class CheckJavaClassPathUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextArea logArea;
    private JPanel jPanel2;
    private JTextField srcPathText;
    private JButton executeBtn;
    private JList classPathList;
    private JTextField classPathText;
    
    private static final String SRCPATHTEXT_KEY = "srcPathText";
    private static final String CLASSNAME_KEY = "className";
    private PropertiesUtilBean configBean = new PropertiesUtilBean(this.getClass());

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CheckJavaClassPathUI inst = new CheckJavaClassPathUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public CheckJavaClassPathUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            setTitle("搜尋含有特殊import的java黨");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("設定", null, jPanel1, null);
                    {
                        classPathText = new JTextField();
                        jPanel1.add(classPathText, BorderLayout.NORTH);
                        classPathText.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                if(StringUtils.isNotBlank(classPathText.getText())){
                                    String text = classPathText.getText();
                                    DefaultListModel model = (DefaultListModel)classPathList.getModel(); 
                                    boolean findOk = false;
                                    for(int ii = 0 ; ii < model.size(); ii ++){
                                        String val = (String)model.getElementAt(ii);
                                        if(StringUtils.equals(val, text)){
                                            findOk = true;
                                        }
                                    }
                                    if(!findOk){
                                        model.addElement(text);
                                    }
                                }
                            }
                        });
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(492, 302));
                        {
                            DefaultListModel classPathListModel = 
                                    new DefaultListModel();
                            classPathList = new JList();
                            
                            Set<String> clzSet = new HashSet<String>();
                            for(Enumeration<?> enu = configBean.getConfigProp().keys(); enu.hasMoreElements();){
                                String key = (String)enu.nextElement();
                                if(key.contains(CLASSNAME_KEY)){
                                    clzSet.add(configBean.getConfigProp().getProperty(key));
                                }
                            }
                            for(String clzName : clzSet){
                                classPathListModel.addElement(clzName);
                            }
                            
                            jScrollPane1.setViewportView(classPathList);
                            classPathList.setModel(classPathListModel);
                            classPathList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(classPathList).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn, BorderLayout.WEST);
                        executeBtn.setText("\u57f7\u884c");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed(evt);
                            }
                        });
                    }
                    {
                        srcPathText = new JTextField();
                        if(configBean.getConfigProp().containsKey(SRCPATHTEXT_KEY)){
                            srcPathText.setText(configBean.getConfigProp().getProperty(SRCPATHTEXT_KEY));
                        }
                        
                        jPanel1.add(srcPathText, BorderLayout.SOUTH);
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcPathText, true);
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("log", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(530, 350));
                        {
                            logArea = new JTextArea();
                            jScrollPane2.setViewportView(logArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(551, 417);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    
    
    private void executeBtnActionPerformed(ActionEvent evt) {
        try{
            Validate.notBlank(srcPathText.getText(), "java source資料夾不可為空");
            Validate.isTrue(classPathList.getModel().getSize()!=0, "定義classPath的List不可為空");
            
            logArea.setText("");
            PrintStream out = JCommonUtil.getNewPrintStream2JTextArea(logArea, 0, false);
            
            Pattern importPattern = Pattern.compile("import\\s+([\\w\\.]+)\\;");
            Pattern classNamePattern = Pattern.compile("(class|interface)\\s+\\w+[\\w\\s]*\\{");
            
            Set<String> classNameSet = new HashSet<String>();
            DefaultListModel model = (DefaultListModel)classPathList.getModel();
            for(int ii = 0 ; ii < model.getSize() ; ii ++){
                String className = (String)model.getElementAt(ii);
                classNameSet.add(className);
            }
            
            
            File srcFile = JCommonUtil.filePathCheck(srcPathText.getText(), "java source資料夾", true);
            
            List<File> fileList = new ArrayList<File>();
            FileUtil.searchFilefind(srcFile, ".*\\.[jJ][aA][vV][aA]", fileList);
            
            Multimap<File, String> javaContainMap = ArrayListMultimap.create();
            
            Matcher matcher = null;
            for(File file : fileList){
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
                for(String line = null; (line = reader.readLine())!=null;){
                    if(classNamePattern.matcher(line).find()){
                        break;
                    }
                    
                    matcher = importPattern.matcher(line);
                    if(matcher.find()){
                        String importClass = matcher.group(1);
                        for(String ptn : classNameSet){
                            Pattern pt2 = null;
                            try{
                                pt2 = Pattern.compile(ptn);
                            }catch(Exception ex){
                            }
                            if(StringUtils.equals(importClass, ptn)){
                                javaContainMap.put(file, importClass);
                            }else if(importClass.contains(ptn)){
                                javaContainMap.put(file, importClass);
                            }else if(pt2.matcher(importClass).find()){
                                javaContainMap.put(file, importClass);
                            }
                        }
                    }
                }
                reader.close();
            }
            
            if(javaContainMap.isEmpty()){
                out.println("沒有任何java含有清單裡的任何importClass");
            }else{
                out.println("以下含有定義的importClass");
                for(File errorFile : javaContainMap.keySet()){
                    out.println(errorFile);
                    for(String clzName : javaContainMap.get(errorFile)){
                        out.println("\t" + clzName);
                    }
                }
            }
            out.println("所有java數 : " + fileList.size());
            
            configBean.getConfigProp().put(SRCPATHTEXT_KEY, srcFile.getAbsolutePath());
            for(String clzName : classNameSet){
                if(configBean.getConfigProp().containsValue(clzName)){
                    continue;
                }
                int availableIndex = -1;
                for(int ii = 0 ;  ; ii ++){
                    if(!configBean.getConfigProp().containsKey(CLASSNAME_KEY + ii)){
                        availableIndex = ii;
                        break;
                    }
                }
                configBean.getConfigProp().put(CLASSNAME_KEY + availableIndex, clzName);
            }
            configBean.store();
            
            JCommonUtil._jOptionPane_showMessageDialog_info("掃描完成!");
            
//            C:/workspace/workspace_farEastStone/estore/fet_estore_search_engie_revamp/revamp_source_code
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
