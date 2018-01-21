package gtu._work.ui;
import gtu.hibernate.ReadDomainJarTableConfig;
import gtu.properties.PropertiesUtil;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
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

import org.apache.commons.lang.StringUtils;



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
public class ObnfInsertCreaterUI extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JLabel jLabel5;
    private JPanel jPanel4;
    private JButton deleteFieldBtn;
    private JButton processSqlBtn;
    private JList dbFieldList;
    private JLabel jLabel4;
    private JTextField dbFieldText;
    private JLabel jLabel3;
    private JTextField dbValue;
    private JButton addFieldBtn;
    private JPanel jPanel3;
    private JPanel jPanel2;
    private JLabel jLabel6;
    private JLabel jLabel1;
    private JButton executeBtn;
    private JTextArea insertSqlArea;
    private JTextArea manualDefineArea;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane2;
    private JTextArea manualDefinePkArea;
    private JCheckBox pkCheckBox;
    private JCheckBox useDomainJarDefineChkBox;
    private JButton manualDefineDbFieldBtn;
    private JTextArea obnfArea;
    private JTextField dominJarFileText;
    private JTextField tableNameText;
    private JScrollPane jScrollPane1;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ObnfInsertCreaterUI inst = new ObnfInsertCreaterUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public ObnfInsertCreaterUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            JCommonUtil.defaultToolTipDelay();
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    jTabbedPane1.addTab("取得schema", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("tableName");
                    }
                    {
                        tableNameText = new JTextField();
                        jPanel1.add(tableNameText);
                        tableNameText.setPreferredSize(new java.awt.Dimension(155, 22));
                    }
                    {
                        useDomainJarDefineChkBox = new JCheckBox();
                        jPanel1.add(useDomainJarDefineChkBox);
                        useDomainJarDefineChkBox.setText("\u4ee5dominJar\u70ba\u6e96");
                    }
                    {
                        dominJarFileText = new JTextField();
                        File currentFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "sris-db-domain-2.5.0-SRIS-SNAPSHOT.jar");
                        if(currentFile.exists()){
                            dominJarFileText.setText(currentFile.getAbsolutePath());
                        }else{
                            File cUserFile = new File("C:\\Users");
                            if(cUserFile.exists()){
                                String domainJarPath = "C:/Users/%s/.m2/repository/tw/gov/sris-db-domain/2.5.0-SRIS-SNAPSHOT/sris-db-domain-2.5.0-SRIS-SNAPSHOT.jar";
                                for(File f : cUserFile.listFiles()){
                                    File jf = new File(String.format(domainJarPath, f.getName()));
                                    if(jf.exists()){
                                        dominJarFileText.setText(jf.getAbsolutePath());
                                        break;
                                    }
                                }
                            }
                        }
                        jPanel1.add(dominJarFileText);
                        dominJarFileText.setPreferredSize(new java.awt.Dimension(219, 22));
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(dominJarFileText, false);
                    }
                    {
                        jLabel5 = new JLabel();
                        jPanel1.add(jLabel5);
                        jLabel5.setText("obnfString");
                        jLabel5.setPreferredSize(new java.awt.Dimension(83, 15));
                    }
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel1.add(jScrollPane2);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(544, 76));
                        {
                            obnfArea = new JTextArea();
                            jScrollPane2.setViewportView(obnfArea);
                        }
                    }
                    {
                        jLabel6 = new JLabel();
                        jPanel1.add(jLabel6);
                        jLabel6.setText("sql");
                        jLabel6.setPreferredSize(new java.awt.Dimension(58, 15));
                    }
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel1.add(jScrollPane3);
                        jScrollPane3.setPreferredSize(new java.awt.Dimension(543, 116));
                        {
                            insertSqlArea = new JTextArea();
                            jScrollPane3.setViewportView(insertSqlArea);
                        }
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u8b80\u53d6ObnfString");
                        executeBtn.setPreferredSize(new java.awt.Dimension(146, 29));
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnAction();
                            }
                        });
                    }
                    {
                        processSqlBtn = new JButton();
                        jPanel1.add(processSqlBtn);
                        processSqlBtn.setText("\u7522\u751fSQL");
                        processSqlBtn.setPreferredSize(new java.awt.Dimension(135, 29));
                        processSqlBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                processSqlBtnAction();
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("設定欄位直", null, jPanel2, null);
                    {
                        jPanel3 = new JPanel();
                        jPanel2.add(jPanel3, BorderLayout.NORTH);
                        jPanel3.setPreferredSize(new java.awt.Dimension(585, 61));
                        {
                            jLabel4 = new JLabel();
                            jPanel3.add(jLabel4);
                            jLabel4.setText("dbField");
                        }
                        {
                            dbFieldText = new JTextField();
                            jPanel3.add(dbFieldText);
                            dbFieldText.setPreferredSize(new java.awt.Dimension(216, 22));
                        }
                        {
                            jLabel3 = new JLabel();
                            jPanel3.add(jLabel3);
                            jLabel3.setText("value");
                        }
                        {
                            dbValue = new JTextField();
                            jPanel3.add(dbValue);
                            dbValue.setPreferredSize(new java.awt.Dimension(244, 22));
                        }
                        {
                            pkCheckBox = new JCheckBox();
                            jPanel3.add(pkCheckBox);
                            pkCheckBox.setText("\u662fPK");
                        }
                        {
                            addFieldBtn = new JButton();
                            jPanel3.add(addFieldBtn);
                            addFieldBtn.setText("saveOrUpdate");
                            addFieldBtn.setPreferredSize(new java.awt.Dimension(116, 22));
                            addFieldBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    addFieldBtnAction();
                                }
                            });
                        }
                        {
                            deleteFieldBtn = new JButton();
                            jPanel3.add(deleteFieldBtn);
                            deleteFieldBtn.setText("delete");
                            deleteFieldBtn.setPreferredSize(new java.awt.Dimension(88, 22));
                            deleteFieldBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    deleteFieldBtnAction();
                                }
                            });
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(585, 258));
                        {
                            DefaultListModel dbFieldListModel = new DefaultListModel();
                            dbFieldList = new JList();
                            jScrollPane1.setViewportView(dbFieldList);
                            dbFieldList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    dbFieldListMousePreformd(evt);
                                }
                            });
                            dbFieldList.setModel(dbFieldListModel);
                            dbFieldList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(dbFieldList).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel4 = new JPanel();
                    GridLayout jPanel4Layout = new GridLayout(1, 1);
                    jPanel4Layout.setColumns(1);
                    jPanel4Layout.setHgap(5);
                    jPanel4Layout.setVgap(5);
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("手動貼入欄位定義", null, jPanel4, null);
                    {
                        manualDefineArea = new JTextArea();
                        jPanel4.add(manualDefineArea);
                        manualDefineArea.setText("");
                        manualDefineArea.setToolTipText("設定欄位");
                    }
                    {
                        manualDefinePkArea = new JTextArea();
                        jPanel4.add(manualDefinePkArea);
                        manualDefinePkArea.setText("");
                        manualDefinePkArea.setToolTipText("設定PK欄位");
                    }
                    {
                        manualDefineDbFieldBtn = new JButton();
                        jPanel4.add(manualDefineDbFieldBtn);
                        manualDefineDbFieldBtn.setText("\u4ee5\u8cbc\u5165\u6b04\u4f4d\u5b9a\u7fa9\u6c7a\u5b9a\u6b04\u4f4d");
                        manualDefineDbFieldBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                manualDefineDbFieldBtnAction();
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(598, 366);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    ReadDomainJarTableConfig test;
    String tableName;

    private void executeBtnAction(){
        try{
            tableName = StringUtils.trimToEmpty(tableNameText.getText());
            String obnfStr = obnfArea.getText();
            if(StringUtils.isBlank(tableName)){
                JCommonUtil._jOptionPane_showMessageDialog_error("tableName為空");
                return;
            }
            if(StringUtils.isBlank(obnfStr)){
                JCommonUtil._jOptionPane_showMessageDialog_error("obnfStr為空");
                return;
            }
            
            Map<String, String> columnMap = new HashMap<String, String>();
            Map<String, String> pkMap = new HashMap<String, String>();
            
            Pattern objPattern2 = Pattern.compile("(\\w+)\\s*\\=\\s*'([^']*)'");
            Matcher matcher = objPattern2.matcher(obnfStr);
            while(matcher.find()){
                String dbField = StringUtils.trim(matcher.group(1)).toLowerCase();
                String value = StringUtils.defaultString(matcher.group(2));
                System.out.println("SQL:[" + dbField + "] = [" + value + "]");
                this.addToColumnMap(dbField, value, columnMap, pkMap);
            }
            
            Pattern objPattern = Pattern.compile("(\\w+)\\=([^\\,\\{\\}\\[\\]]*)");
            matcher = objPattern.matcher(obnfStr);
            while(matcher.find()){
                String g1 = StringUtils.trim(matcher.group(1));
                if(g1.indexOf("_") != -1){
                    continue;
                }
                String dbField = StringUtils.trim(StringUtilForDb.javaToDbField(g1)).toLowerCase();
                String value = StringUtils.defaultString(matcher.group(2));
                String tmpVal = StringUtils.trimToEmpty(value);
                if(tmpVal.startsWith("'") && tmpVal.endsWith("'")){
                    continue;
                }
                System.out.println("JAVA:[" + dbField + "] = [" + value + "]");
                this.addToColumnMap(dbField, value, columnMap, pkMap);
            }
            
            this.putToDbFieldList(pkMap, columnMap);
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    private void putToDbFieldList(Map<String, String> pkMap, Map<String, String> columnMap){
        DefaultListModel dbFieldListModel = new DefaultListModel();
        for(String key : columnMap.keySet()){
            KeyValue k = new KeyValue();
            k.key = key;
            k.value = columnMap.get(key);
            dbFieldListModel.addElement(k);
        }
        for(String key : pkMap.keySet()){
            KeyValue k = new KeyValue();
            k.key = key;
            k.value = pkMap.get(key);
            k.pk = true;
            dbFieldListModel.addElement(k);
        }
        dbFieldList.setModel(dbFieldListModel);
    }
    
    private void addToColumnMap(String key, String value, Map<String,String> columnMap, Map<String, String> pkMap){
        if(columnMap.containsKey(key)){
            String tmpVal = columnMap.get(key);
            if(!StringUtils.equals(StringUtils.trimToEmpty(value), StringUtils.trimToEmpty(tmpVal))){
                System.out.println(String.format("!!!值不同[%s][%s]", value, tmpVal));
                String val = (String)JOptionPaneUtil.newInstance().showInputDialog_drowdown("欄位:" + key + "有兩個不同的值,請決定哪個為pk", "決定PK", new Object[]{value, tmpVal}, value);
                columnMap.remove(key);
                if(StringUtils.equals(StringUtils.trimToEmpty(val), StringUtils.trimToEmpty(value))){
                    columnMap.put(key, tmpVal);
                }
                if(StringUtils.equals(StringUtils.trimToEmpty(val), StringUtils.trimToEmpty(tmpVal))){
                    columnMap.put(key, value);
                }
                pkMap.put(key, val);
            }
        }else{
            columnMap.put(key, value);
        }
    }
    
    private StringBuilder getMessage(List<String> notFoundList){
        StringBuilder sb = new StringBuilder();
        int ii = 0;
        for(String s : notFoundList){
            sb.append(s + ", ");
            ii ++;
            if(ii == 3){
                ii = 0;
                sb.append("\n");
            }
        }
        return sb;
    }
    
    private void loadJarConfig() {
        if(StringUtils.isBlank(dominJarFileText.getText())){
            JCommonUtil._jOptionPane_showMessageDialog_error("dominJar路徑為空");
            return;
        }
        File dominJarFile = new File(dominJarFileText.getText());
        if (dominJarFile == null || !dominJarFile.exists()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("dominJar不存在");
            return;
        }
        test = new ReadDomainJarTableConfig();
        test.execute(dominJarFile, tableName);
    }
    
    private void processSqlBtnAction(){
        Map<String,String> wkDataObjectMapCopy = new LinkedHashMap<String,String>();
        Map<String,String> wkKeyMapCopy = new LinkedHashMap<String,String>();
        
        DefaultListModel dbFieldListModel = (DefaultListModel)dbFieldList.getModel();
        for(Enumeration<?> enu = dbFieldListModel.elements(); enu.hasMoreElements();){
            KeyValue kv = (KeyValue)enu.nextElement();
            if(kv.pk == false){
                wkDataObjectMapCopy.put(kv.key, kv.value);
            }else{
                wkKeyMapCopy.put(kv.key, kv.value);
            }
        }
        
        if (useDomainJarDefineChkBox.isSelected()) {
            loadJarConfig();
            StringBuffer sb = new StringBuffer();
            sb.append("PK=>\n");
            this.keepKey(wkKeyMapCopy, test.getPkColumns(), sb);
            sb.append("COLUMN=>\n");
            this.keepKey(wkDataObjectMapCopy, test.getColumns(), sb);
            JCommonUtil._jOptionPane_showMessageDialog_info(sb);
            for(String key : wkKeyMapCopy.keySet()){
                if(StringUtils.isBlank(wkKeyMapCopy.get(key)) && //
                        wkDataObjectMapCopy.containsKey(key) && //
                        StringUtils.isNotBlank(wkDataObjectMapCopy.get(key))){
                    System.out.println("##補pk = " + key + " = " + wkDataObjectMapCopy.get(key));
                    wkKeyMapCopy.put(key, wkDataObjectMapCopy.get(key));
                }
            }
        }
        
        System.out.println("wkKeyMapCopy = " + wkKeyMapCopy);
        System.out.println("wkDataObjectMapCopy = " + wkDataObjectMapCopy);
        
        String whereCondition = "";
        if(!wkKeyMapCopy.isEmpty()){
            System.out.println("====> wkKeyMapCopy 不為空 !!!!!!");
            whereCondition = wkKeyMapCopy.toString();
        }else{
            whereCondition = wkDataObjectMapCopy.toString();
        }
        
        whereCondition = whereCondition.replaceAll(",", "' and ");
        whereCondition = whereCondition.replaceAll("=", "='");
        whereCondition = whereCondition.substring(1);
        whereCondition = whereCondition.substring(0, whereCondition.length()-1);
        whereCondition = whereCondition + "'"; 
        
        String selectSQL = "select * from " + tableName + " where " + whereCondition + ";\n\n";
        
        String updateSetStr = wkDataObjectMapCopy.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length()-1);
        updateSetStr = updateSetStr + "'"; 
        
        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition + ";\n\n";
        
        List<String> insertFieldList = new ArrayList<String>();
        List<String> insertValueList = new ArrayList<String>();
        for(String key : wkDataObjectMapCopy.keySet()){
            insertFieldList.add(key);
            insertValueList.add(wkDataObjectMapCopy.get(key));
        }
        String inf = insertFieldList.toString();
        inf = inf.replaceAll(" ", "");
        inf = inf.substring(1, inf.length()-1);
        String inv = insertValueList.toString();
        inv = inv.replaceAll(" ", "");
        inv = inv.replaceAll(",", "','");
        inv = inv.substring(1, inv.length()-1);
        inv = "'" + inv + "'";
        String insertSQL = "insert into " + tableName + " (" + inf + ") values (" + inv + ");\n\n";
        
        insertSqlArea.setText(selectSQL + updateSQL + insertSQL);
    }
    
    private void keepKey(Map<String,String> map, Set<String> keySet, StringBuffer sb){
        List<String> addCol = new ArrayList<String>();
        List<String> remCol = new ArrayList<String>();
        for(String k : keySet){
            if(!map.containsKey(k)){
                map.put(k, "");
                addCol.add(k);
            }
        }
        for(String k : map.keySet()){
            if(!keySet.contains(k)){
                remCol.add(k);
            }
        }
        for(String k : remCol){
            map.remove(k);
        }
        sb.append("新增=>" + getMessage(addCol) + "\n");
        sb.append("移除=>" + getMessage(remCol) + "\n");
    }
    
    private void addFieldBtnAction(){
        String dbField = dbFieldText.getText();
        String dbvalue = StringUtils.defaultString(dbValue.getText());
        if(StringUtils.isBlank(dbField)){
            JCommonUtil._jOptionPane_showMessageDialog_error("請輸入dbField");
            return;
        }
        DefaultListModel dbFieldListModel = (DefaultListModel)dbFieldList.getModel();
        boolean doUpdate = false;
        for(Enumeration<?> enu = dbFieldListModel.elements(); enu.hasMoreElements();){
            KeyValue kv = (KeyValue)enu.nextElement();
            if(kv.getKey().equalsIgnoreCase(dbField) && kv.pk == pkCheckBox.isSelected()){
                kv.value = dbvalue;
                doUpdate = true;
                JCommonUtil._jOptionPane_showMessageDialog_info("修改:" + kv);
                break;
            }
        }
        if(!doUpdate){
            KeyValue kv = new KeyValue();
            kv.key = dbField;
            kv.value = dbvalue;
            kv.pk = pkCheckBox.isSelected();
            dbFieldListModel.addElement(kv);
            JCommonUtil._jOptionPane_showMessageDialog_info("新增:" + kv);
        }
    }
    
    private void deleteFieldBtnAction() {
        String dbField = dbFieldText.getText();
        if(StringUtils.isBlank(dbField)){
            JCommonUtil._jOptionPane_showMessageDialog_error("請輸入dbField");
            return;
        }
        DefaultListModel dbFieldListModel = (DefaultListModel)dbFieldList.getModel();
        boolean deleteOk = false;
        for(Enumeration<?> enu = dbFieldListModel.elements(); enu.hasMoreElements();){
            KeyValue kv = (KeyValue)enu.nextElement();
            if(kv.getKey().equalsIgnoreCase(dbField)){
                dbFieldListModel.removeElement(kv);
                deleteOk = true;
                break;
            }
        }
        JCommonUtil._jOptionPane_showMessageDialog_info("刪除:" + dbField + "->" +(deleteOk?"成功":"找不到"));
    }
    
    private void manualDefineDbFieldBtnAction(){
        String manualDefineText = manualDefineArea.getText();
        String manualDefinePkText = StringUtils.defaultString(manualDefinePkArea.getText());
        if(StringUtils.isBlank(manualDefineText)){
            JCommonUtil._jOptionPane_showMessageDialog_error("沒填入任何欄位");
            return;
        }
        
        Set<String> useList = new HashSet<String>();
        StringTokenizer tok = new StringTokenizer(manualDefineText);
        while(tok.hasMoreElements()){
            String f = (String)tok.nextElement();
            useList.add(f);
        }
        
        Set<String> pkList = new HashSet<String>();
        StringTokenizer tok2 = new StringTokenizer(manualDefinePkText);
        while(tok2.hasMoreElements()){
            String f = (String)tok2.nextElement();
            pkList.add(f);
        }
        
        Map<String,String> pkMap = new HashMap<String,String>();
        Map<String,String> columnMap = new HashMap<String,String>();
        DefaultListModel dbFieldListModel = (DefaultListModel)dbFieldList.getModel();
        if(dbFieldListModel.isEmpty()){
            JCommonUtil._jOptionPane_showMessageDialog_error("請先執行\"讀取ObnfString\"");
            return;
        }
        for(Enumeration<?> enu = dbFieldListModel.elements(); enu.hasMoreElements();){
            KeyValue kv = (KeyValue)enu.nextElement();
            if(!pkList.isEmpty() && pkList.contains(kv.getKey())){
                pkMap.put(kv.getKey(), kv.value);
            }else if(pkList.isEmpty() && kv.pk){
                pkMap.put(kv.getKey(), kv.value);
            }else{
                columnMap.put(kv.getKey(), kv.value);
            }
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("COLUMN=>\n");
        this.keepKey(columnMap, useList, sb);
        JCommonUtil._jOptionPane_showMessageDialog_info(sb);
        
        this.putToDbFieldList(pkMap, columnMap);
    }
    
    private void dbFieldListMousePreformd(MouseEvent evt){
        if(JMouseEventUtil.buttonLeftClick(1, evt)){
            KeyValue kv = (KeyValue)dbFieldList.getSelectedValue();
            if(kv != null){
                dbFieldText.setText(kv.key);
                dbValue.setText(kv.value);
                pkCheckBox.setSelected(kv.pk);
            }
        }
        if(JMouseEventUtil.buttonRightClick(1, evt)){
            JPopupMenuUtil.newInstance(dbFieldList).addJMenuItem("刷新/排序", new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    List<KeyValue> list = new ArrayList<KeyValue>();
                    DefaultListModel model = (DefaultListModel)dbFieldList.getModel();
                    for(int ii = 0 ; ii < model.getSize() ; ii ++){
                        KeyValue kv = (KeyValue)model.get(ii);
                        list.add(kv);
                    }
                    Collections.sort(list, new Comparator<KeyValue>() {
                        @Override
                        public int compare(KeyValue o1, KeyValue o2) {
                            return o1.key.compareTo(o2.key);
                        }
                    });
                    DefaultListModel model1 = new DefaultListModel();
                    for(KeyValue kv : list){
                        model1.addElement(kv);
                    }
                    dbFieldList.setModel(model1);
                }
            }).addJMenuItem("設/取消PK", new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    Object[] values = dbFieldList.getSelectedValues();
                    if(values != null){
                        for (Object v : values) {
                            KeyValue kv = (KeyValue) v;
                            kv.pk = !kv.pk;
                        }
                    }
                }
            }).applyEvent(evt).show();
        }
    }
    
    static class KeyValue {
        String key;
        String value;
        boolean pk;
        @Override
        public String toString() {
            return (pk?"[PK]":"") + "\t" + key + " = '" + value + "'";
        }
        public String getKey(){
            return key;
        }
    }
}
