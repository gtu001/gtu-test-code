package gtu._work.ui;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

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
public class EstoreCodeGenerateUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextField jspPathText;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JButton updateBtn;
    private JScrollPane jScrollPane1;
    private JTextArea xmlConfigArea;
    private JPanel jPanel2;
    private JTextField packageMiddleNameText;
    private JLabel jLabel6;
    private JTextField serviceImplText;
    private JTextField serviceInterfaceText;
    private JTextField actionPathText;
    private JLabel jLabel3;
    private JLabel jLabel2;
    private JTextField classNameText;
    private JLabel jLabel1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EstoreCodeGenerateUI inst = new EstoreCodeGenerateUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public EstoreCodeGenerateUI() {
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
                jTabbedPane1.setPreferredSize(new java.awt.Dimension(717, 582));
                {
                    jPanel1 = new JPanel();
                    GridLayout jPanel1Layout = new GridLayout(15, 1);
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("基本設定", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("\u985e\u5225\u540d\u7a31");
                    }
                    {
                        classNameText = new JTextField();
                        jPanel1.add(classNameText);
                    }
                    {
                        jLabel6 = new JLabel();
                        jPanel1.add(jLabel6);
                        jLabel6.setText("package\u4e2d\u9593\u540d");
                    }
                    {
                        packageMiddleNameText = new JTextField();
                        jPanel1.add(packageMiddleNameText);
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("jsp\u8def\u5f91");
                    }
                    {
                        jspPathText = new JTextField();
                        jPanel1.add(jspPathText);
                        jspPathText.addFocusListener(new FocusAdapter() {
                            public void focusLost(FocusEvent evt) {
                                try {
                                    String xmlConfigMessage = xmlConfigArea.getText();
                                    if (StringUtils.isBlank(xmlConfigMessage)) {
                                        return;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    String actionClassPath = jspPathText.getText();
                                    actionClassPath = actionClassPath.replaceFirst("/src/main/webapp", "");
                                    Pattern pattern = Pattern.compile("value=\"[\\w\\/]+\\.jsp\"");
                                    Matcher matcher = null;
                                    BufferedReader reader = new BufferedReader(new StringReader(xmlConfigMessage));
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        matcher = pattern.matcher(line);
                                        if (matcher.find()) {
                                            StringBuffer sb2 = new StringBuffer();
                                            matcher.appendReplacement(sb2, "value=\""+actionClassPath+"\"");
                                            matcher.appendTail(sb2);
                                            sb.append(sb2 + "\n");
                                        }else{
                                            sb.append(line + "\n");
                                        }
                                    }
                                    xmlConfigArea.setText(sb.toString());
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel1.add(jLabel3);
                        jLabel3.setText("action\u8def\u5f91");
                    }
                    {
                        actionPathText = new JTextField();
                        jPanel1.add(actionPathText);
                        actionPathText.addFocusListener(new FocusAdapter() {
                            public void focusLost(FocusEvent evt) {
                                try {
                                    String xmlConfigMessage = xmlConfigArea.getText();
                                    if (StringUtils.isBlank(xmlConfigMessage)) {
                                        return;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    String actionClassPath = actionPathText.getText();
                                    System.out.println(actionClassPath);
                                    actionClassPath = actionClassPath.replaceAll("/src/main/java/", "").replace('/', '.').replaceAll(".java", "");
                                    Pattern pattern = Pattern.compile("class=\"com\\.sti\\.[\\w\\.]+Action\"");
                                    Matcher matcher = null;
                                    BufferedReader reader = new BufferedReader(new StringReader(xmlConfigMessage));
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        matcher = pattern.matcher(line);
                                        if (matcher.find()) {
                                            StringBuffer sb2 = new StringBuffer();
                                            matcher.appendReplacement(sb2, "class=\""+actionClassPath+"\"");
                                            matcher.appendTail(sb2);
                                            sb.append(sb2 + "\n");
                                        }else{
                                            sb.append(line + "\n");
                                        }
                                    }
                                    xmlConfigArea.setText(sb.toString());
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                    {
                        jLabel4 = new JLabel();
                        jPanel1.add(jLabel4);
                        jLabel4.setText("service interface\u8def\u5f91");
                    }
                    {
                        serviceInterfaceText = new JTextField();
                        jPanel1.add(serviceInterfaceText);
                    }
                    {
                        jLabel5 = new JLabel();
                        jPanel1.add(jLabel5);
                        jLabel5.setText("service Impl\u8def\u5f91");
                    }
                    {
                        serviceImplText = new JTextField();
                        jPanel1.add(serviceImplText);
                        serviceImplText.addFocusListener(new FocusAdapter() {
                            public void focusLost(FocusEvent evt) {
                                try {
                                    String xmlConfigMessage = xmlConfigArea.getText();
                                    if (StringUtils.isBlank(xmlConfigMessage)) {
                                        return;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    String actionClassPath = serviceImplText.getText();
                                    actionClassPath = actionClassPath.replaceFirst("/src/main/java/", "").replaceAll(".java", "").replace('/', '.');
                                    Pattern pattern = Pattern.compile("class=\"com.sti[\\w\\.]+ServiceImpl\"");
                                    Matcher matcher = null;
                                    BufferedReader reader = new BufferedReader(new StringReader(xmlConfigMessage));
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        matcher = pattern.matcher(line);
                                        if (matcher.find()) {
                                            StringBuffer sb2 = new StringBuffer();
                                            matcher.appendReplacement(sb2, "class=\""+actionClassPath+"\"");
                                            matcher.appendTail(sb2);
                                            sb.append(sb2 + "\n");
                                        }else{
                                            sb.append(line + "\n");
                                        }
                                    }
                                    xmlConfigArea.setText(sb.toString());
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                    {
                        updateBtn = new JButton();
                        jPanel1.add(updateBtn);
                        updateBtn.setText("\u66f4\u65b0");
                        updateBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                updateBtnActionPerformed(evt);
                            }
                        });
                    }
                    {
                        makeFileBtn = new JButton();
                        jPanel1.add(makeFileBtn);
                        makeFileBtn.setText("\u7522\u751f\u6a94\u6848");
                        makeFileBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    makeFileBtnActionPerformed(evt);
                                } catch (IOException e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("xml相關", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            xmlConfigArea = new JTextArea();
                            jScrollPane1.setViewportView(xmlConfigArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(733, 525);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private final static String REPLACE_MESSAGE;
    private final static String ACTION_DATA;
    private JButton makeFileBtn;
    private final static String SERVICE_INTERFACE;
    private final static String SERVICE_IMPL;
    
    static {
        //%1$s - 小寫class
        //%2$s - package
        //%3$s - 大寫class
        //%4$s - jsp路徑
        StringBuilder sb = new StringBuilder();
        sb.append("  <!--tiles-defs.xml-->                                                                                                                     \n");
        sb.append("      <definition name=\".marketingAdmin.%2$s.%1$sInit\" extends=\".marketingAdmin\">                   \n");
        sb.append("          <put name=\"bodyTitle\" type=\"string\" value=\"marketing Admin\"/>                                                               \n");
        sb.append("          <put name=\"body\" type=\"page\" value=\"%4$s\" />        \n");
        sb.append("      </definition>                                                                                                                         \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("  <!--struts-backend-marketing.xml-->                                                                                                       \n");
        sb.append("             <form-bean name=\"%1$sForm\" type=\"org.apache.struts.action.DynaActionForm\">               \n");
        sb.append("             <form-property name=\"adTabId\" type=\"java.lang.String\" />                                                           \n");
        sb.append("             <form-property name=\"adCategoryName\" type=\"java.lang.String\" />                                                    \n");
        sb.append("             <form-property name=\"tabType\" type=\"java.lang.String\" />                                                           \n");
        sb.append("             <form-property name=\"displayName\" type=\"java.lang.String\" />                                                       \n");
        sb.append("             <form-property name=\"uploadFile\" type=\"org.apache.struts.upload.FormFile\" />                                       \n");
        sb.append("             <form-property name=\"uploadFileOff\" type=\"org.apache.struts.upload.FormFile\" />                                    \n");
        sb.append("             <form-property name=\"accessoryCategories\" type=\"java.lang.String[]\" />                                             \n");
        sb.append("             <form-property name=\"handsetCategories\" type=\"java.lang.String[]\" />                                               \n");
        sb.append("             <form-property name=\"tabletCategories\" type=\"java.lang.String[]\" />                                                \n");
        sb.append("         </form-bean>                                                                                                                   \n");
        sb.append("                                                                                                                                        \n");
        sb.append("     <action path=\"/admin/marketing/%1$s\" name=\"%1$sForm\" parameter=\"cmd\" scope=\"request\">      \n");
        sb.append("              <forward name=\"initPage\" path=\".marketingAdmin.%2$s.%1$sInit\" />                      \n");
        sb.append("             <forward name=\"%1$sEdit\" path=\".marketingAdmin.%2$s.%1$sEdit\" />              \n");
        sb.append("             <forward name=\"%1$sSort\" path=\"/jsp/backend/marketing/%2$s/%1$sSort.jsp\" />            \n");
        sb.append("             <forward name=\"%1$sList\" path=\".marketingAdmin.%2$s.%1$sList\" />                 \n");
        sb.append("             <forward name=\"%1$sEdit\" path=\".marketingAdmin.%2$s.%1$sEdit\" />                 \n");
        sb.append("          </action>                                                                                                                         \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("  <!--spring-backend-marketing.xml-->                                                                                                       \n");
        sb.append("      <bean name=\"/admin/marketing/%1$s\" scope=\"prototype\" parent=\"backendAction\"                                   \n");
        sb.append("            class=\"com.sti.estore.backend.web.action.marketing.%2$s.%3$sAction\">                      \n");
        sb.append("         <property name=\"%1$sService\" ref=\"%1$sService\" />                                      \n");
        sb.append("      </bean>                                                                                                                               \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("                                                                                                                                            \n");
        sb.append("  <!--applicationConfig-service.xml-->                                                                                                      \n");
        sb.append("         <bean id=\"%1$sService\" class=\"com.sti.estore.service.impl.%3$sServiceImpl\">            \n");
        sb.append("     </bean>                                                                                                                                \n");
        REPLACE_MESSAGE = sb.toString();
        
        //%1$s - package
        //%2$s - 大寫class
        //%3$s - 小寫class
        sb = new StringBuilder();
        sb.append("package %1$s;                                                                                                                                                     \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("import javax.servlet.http.HttpServletRequest;                                                                                                                     \n");
        sb.append("import javax.servlet.http.HttpServletResponse;                                                                                                                    \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("import org.apache.commons.logging.Log;                                                                                                                            \n");
        sb.append("import org.apache.commons.logging.LogFactory;                                                                                                                     \n");
        sb.append("import org.apache.struts.action.ActionForm;                                                                                                                       \n");
        sb.append("import org.apache.struts.action.ActionForward;                                                                                                                    \n");
        sb.append("import org.apache.struts.action.ActionMapping;                                                                                                                    \n");
        sb.append("import org.apache.struts.action.DynaActionForm;                                                                                                                   \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("import com.sti.estore.backend.web.action.BackendAction;                                                                                                           \n");
        sb.append("import com.sti.estore.util.StringUtil;                                                                                                                            \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("/**                                                                                                                                                               \n");
        sb.append(" *                                                                                                                                                                \n");
        sb.append(" */                                                                                                                                                               \n");
        sb.append("public class %2$sAction extends BackendAction {                                                                                                                   \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    private static final long serialVersionUID = 1L;                                                                                                              \n");
        sb.append("    private static final Log log = LogFactory.getLog(%2$sAction.class);                                                                                           \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    private %2$sService %3$sService;                                                                                                                              \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    @Override                                                                                                                                                     \n");
        sb.append("    public String getFunctionId() {                                                                                                                               \n");
        sb.append("        return this.getClass().getSimpleName();                                                                                                                   \n");
        sb.append("    }                                                                                                                                                             \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    /**                                                                                                                                                           \n");
        sb.append("     * 初始化.                                                                                                                                                    \n");
        sb.append("     */                                                                                                                                                           \n");
        sb.append("    public ActionForward initSearchForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {      \n");
        sb.append("        DynaActionForm dyForm = (DynaActionForm) form;                                                                                                            \n");
        sb.append("        String adTabId = StringUtil.validate((String) dyForm.get(\"adTabId\"));                                                                                     \n");
        sb.append("        request.setAttribute(\"adTabList\", \"xxxxxxxxxxxxxxxxxxx\");                                                                                                 \n");
        sb.append("        return mapping.findForward(\"initPage\");                                                                                                                   \n");
        sb.append("    }                                                                                                                                                             \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    // get set below .............                                                                                                                                \n");
        sb.append("    public %2$sService get%2$sService() {                                                                                                                         \n");
        sb.append("        return %3$sService;                                                                                                                                       \n");
        sb.append("    }                                                                                                                                                             \n");
        sb.append("                                                                                                                                                                  \n");
        sb.append("    public void set%2$sService(%2$sService %3$sService) {                                                                                                         \n");
        sb.append("        this.%3$sService = %3$sService;                                                                                                                           \n");
        sb.append("    }                                                                                                                                                             \n");
        sb.append("}                                                                                                                                                                 \n");
        ACTION_DATA = sb.toString();
        
        //%1$s - package
        //%2$s - 大寫class
        sb = new StringBuilder();
        sb.append("package %1$s;                    \n");
        sb.append("                                 \n");
        sb.append("public interface %2$sService {    \n");
        sb.append("}                                \n");
        SERVICE_INTERFACE = sb.toString();
        
        //%1$s - package
        //%2$s - 大寫class
        sb = new StringBuilder();
        sb.append("package %1$s;                                                                          \n");
        sb.append("                                                                                       \n");
        sb.append("import java.io.BufferedReader;                                                         \n");
        sb.append("import java.io.IOException;                                                            \n");
        sb.append("import java.io.InputStreamReader;                                                      \n");
        sb.append("import java.util.ArrayList;                                                            \n");
        sb.append("import java.util.HashMap;                                                              \n");
        sb.append("import java.util.List;                                                                 \n");
        sb.append("import java.util.Map;                                                                  \n");
        sb.append("                                                                                       \n");
        sb.append("import org.apache.commons.httpclient.HttpClient;                                       \n");
        sb.append("import org.apache.commons.httpclient.methods.PostMethod;                               \n");
        sb.append("import org.apache.commons.httpclient.params.HttpClientParams;                          \n");
        sb.append("import org.apache.commons.logging.Log;                                                 \n");
        sb.append("import org.apache.commons.logging.LogFactory;                                          \n");
        sb.append("                                                                                       \n");
        sb.append("public class %2$sServiceImpl implements %2$sService {                                  \n");
        sb.append("    protected static final Log log = LogFactory.getLog(%2$sServiceImpl.class);         \n");
        sb.append("}                                                                                      \n");
        SERVICE_IMPL = sb.toString();
    }
    
    private void updateBtnActionPerformed(ActionEvent evt) {
        //[jsp] /src/main/webapp/jsp/backend/marketing/littleWebHomepageApplyMaintain/littleWebHomepageApplyMaintainInit.jsp
        //[action] /src/main/java/com/sti/estore/backend/web/action/marketing/littleWebHomepageApplyMaintain/LittleWebHomepageApplyMaintainAction.java
        //[serviceImpl] /src/main/java/com/sti/estore/service/impl/LittleWebHomepageApplyMaintainServiceImpl.java
        //[serviceInterface] /src/main/java/com/sti/estore/service/LittleWebHomepageApplyMaintainService.java

        String className = StringUtils.defaultString(classNameText.getText());
        String packageMiddleName = StringUtils.defaultString(packageMiddleNameText.getText());
        String jspPath = StringUtils.defaultString(jspPathText.getText());
        String actionPath = StringUtils.defaultString(actionPathText.getText());
        String serviceInterface = StringUtils.defaultString(serviceInterfaceText.getText());
        String serviceImpl = StringUtils.defaultString(serviceImplText.getText());
        
        String lowerCaseClassName = className.substring(0, 1).toLowerCase() + className.substring(1);
        Validate.notBlank(className, "類別名稱必田");
        if(StringUtils.isBlank(packageMiddleName)){
            packageMiddleName = lowerCaseClassName;
            packageMiddleNameText.setText(packageMiddleName);
        }
        
        jspPath = String.format("/src/main/webapp/jsp/backend/marketing/%s/%s.jsp", packageMiddleName, lowerCaseClassName);
        actionPath = String.format("/src/main/java/com/sti/estore/backend/web/action/marketing/%s/%sAction.java", packageMiddleName, className);
        serviceInterface = String.format("/src/main/java/com/sti/estore/service/%sService.java", className);
        serviceImpl = String.format("/src/main/java/com/sti/estore/service/impl/%sServiceImpl.java", className);
        
        jspPathText.setText(jspPath);
        actionPathText.setText(actionPath);
        serviceInterfaceText.setText(serviceInterface);
        serviceImplText.setText(serviceImpl);
        
        jspPath = jspPath.replaceFirst("/src/main/webapp", "");
        
        //%1$s - 小寫class
        //%2$s - package
        //%3$s - 大寫class
        //%4$s - jsp路徑
        String configMessage = String.format(REPLACE_MESSAGE, lowerCaseClassName, packageMiddleName, className, jspPath);
        xmlConfigArea.setText(configMessage);
    }
    
    private void makeFileBtnActionPerformed(ActionEvent evt) throws IOException {
        String className = StringUtils.defaultString(classNameText.getText());
        String packageMiddleName = StringUtils.defaultString(packageMiddleNameText.getText());
        String jspPath = StringUtils.defaultString(jspPathText.getText());
        String actionPath = StringUtils.defaultString(actionPathText.getText());
        String serviceInterface = StringUtils.defaultString(serviceInterfaceText.getText());
        String serviceImpl = StringUtils.defaultString(serviceImplText.getText());
        
        String lowerCaseClassName = className.substring(0, 1).toLowerCase() + className.substring(1);
        Validate.notBlank(className, "類別名稱必田");
        if(StringUtils.isBlank(packageMiddleName)){
            packageMiddleName = lowerCaseClassName;
        }
        
        String actionPath2 = actionPath.replaceAll("/src/main/java/", "").replaceAll(className + "Action.java", "").replace('/', '.');
        String serviceInterfacePath2 = serviceInterface.replaceAll("/src/main/java/", "").replaceAll(className + "Service.java", "").replace('/', '.');
        String serviceImplPath2 = serviceImpl.replaceAll("/src/main/java/", "").replaceAll(className + "ServiceImpl.java", "").replace('/', '.');
        actionPath2 = actionPath2.substring(0, actionPath2.length() -1);
        serviceInterfacePath2 = serviceInterfacePath2.substring(0, serviceInterfacePath2.length() -1);
        serviceImplPath2 = serviceImplPath2.substring(0, serviceImplPath2.length() -1);
        
        System.out.println(actionPath2);
        System.out.println(serviceInterfacePath2);
        System.out.println(serviceImplPath2);
        
        // %1$s - package
        // %2$s - 大寫class
        // %3$s - 小寫class
        String actionMessage = String.format(ACTION_DATA, actionPath2, className, lowerCaseClassName);

        // %1$s - package
        // %2$s - 大寫class
        String serviceInterfaceMessage = String.format(SERVICE_INTERFACE, serviceInterfacePath2, className);

        // %1$s - package
        // %2$s - 大寫class
        String serviceImplMessage = String.format(SERVICE_IMPL, serviceImplPath2, className);
        
        File jspFile = new File(FileUtil.DESKTOP_DIR, jspPath);
        File actionFile = new File(FileUtil.DESKTOP_DIR, actionPath);
        File serviceInterfaceFile = new File(FileUtil.DESKTOP_DIR, serviceInterface);
        File serviceImplFile = new File(FileUtil.DESKTOP_DIR, serviceImpl);
        
        System.out.println(jspFile);
        System.out.println(actionFile);
        System.out.println(serviceInterfaceFile);
        System.out.println(serviceImplFile);
        
        jspFile.getParentFile().mkdirs();
        actionFile.getParentFile().mkdirs();
        serviceInterfaceFile.getParentFile().mkdirs();
        serviceImplFile.getParentFile().mkdirs();
        
        FileUtils.write(jspFile, "", "utf8");
        FileUtils.write(actionFile, actionMessage, "utf8");
        FileUtils.write(serviceInterfaceFile, serviceInterfaceMessage, "utf8");
        FileUtils.write(serviceImplFile, serviceImplMessage, "utf8");
        
        JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案於桌面!");
    }
}
