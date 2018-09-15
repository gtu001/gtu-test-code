package gtu._work.etc;
import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTreeUtil;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;


public class DirSearchUI extends JFrame { 

    private JPanel contentPane;
    private JTextField dirText;
    private JTextField searchText;
    JTreeUtil jtreeUtil;
    private JComboBox selectTypeComboBox;
    private JComboBox selectType2ComboBox;
    private JComboBox encodeComboBox;
    private JTree conditionTree;
    DefaultMutableTreeNode rootNode;
    private JTextField subFileNameText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DirSearchUI frame = new DirSearchUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DirSearchUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 759, 536);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        selectType2ComboBox = new JComboBox();
        DefaultComboBoxModel comboBoxModel2 = (DefaultComboBoxModel)selectType2ComboBox.getModel();
        selectTypeComboBox = new JComboBox();
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel)selectTypeComboBox.getModel();
        encodeComboBox = new JComboBox();
        DefaultComboBoxModel comboBoxModel3 = (DefaultComboBoxModel)encodeComboBox.getModel();
        
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        comboBoxModel.addElement("AND");
        comboBoxModel.addElement("OR");
        comboBoxModel2.addElement("包含");
        comboBoxModel2.addElement("不含");
        comboBoxModel3.addElement("UTF8");
        comboBoxModel3.addElement("BIG5");
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("輸入目錄名稱");
        lblNewLabel.setBounds(21, 10, 79, 24);
        panel.add(lblNewLabel);
        
        dirText = new JTextField();
        dirText.setBounds(108, 12, 294, 21);
        panel.add(dirText);
        dirText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(dirText, true);
        
        JLabel lblNewLabel_1 = new JLabel("搜尋關鍵字");
        lblNewLabel_1.setBounds(21, 80, 79, 24);
        panel.add(lblNewLabel_1);
        
        searchText = new JTextField();
        searchText.setColumns(10);
        searchText.setBounds(108, 82, 294, 21);
        panel.add(searchText);
        
        JLabel lblNewLabel_2 = new JLabel("類型");
        lblNewLabel_2.setBounds(21, 114, 79, 15);
        panel.add(lblNewLabel_2);
        
        selectTypeComboBox.setBounds(108, 110, 89, 21);
        panel.add(selectTypeComboBox);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 143, 381, 249);
        panel.add(scrollPane);
        
        //加入 ===================================================================================
        JButton addConditionBtn = new JButton("加入");
        addConditionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addConditionBtnClick();
            }
        });
        addConditionBtn.setBounds(315, 110, 87, 23);
        panel.add(addConditionBtn);
        
        //Tree ===================================================================================
        conditionTree = new JTree();
        resetBtnClick();
        jtreeUtil = JTreeUtil.newInstance(conditionTree);
        conditionTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                conditionTreeMouseClick(arg0);
            }
        });
        scrollPane.setViewportView(conditionTree);
        
        //開始查詢 ===================================================================================
        JButton startSearchBtn = new JButton("開始查詢");
        startSearchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                startSearchBtnClick();
            }
        });
        startSearchBtn.setBounds(271, 415, 131, 23);
        panel.add(startSearchBtn);
        
        //重設查詢 ===================================================================================
        JButton resetBtn = new JButton("重設查詢");
        resetBtn.setVerticalAlignment(SwingConstants.BOTTOM);
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBtnClick();
            }
        });
        resetBtn.setBounds(164, 415, 97, 23);
        panel.add(resetBtn);
        // ===================================================================================
        
        selectType2ComboBox.setBounds(207, 110, 89, 21);
        panel.add(selectType2ComboBox);
        
        encodeComboBox.setBounds(49, 416, 105, 21);
        panel.add(encodeComboBox);
        
        JLabel label = new JLabel("編碼");
        label.setBounds(21, 419, 29, 15);
        panel.add(label);
        
        JLabel label_1 = new JLabel("副檔名");
        label_1.setBounds(21, 45, 79, 15);
        panel.add(label_1);
        
        subFileNameText = new JTextField();
        subFileNameText.setColumns(10);
        subFileNameText.setBounds(108, 44, 142, 21);
        panel.add(subFileNameText);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(417, 14, 301, 421);
        panel.add(scrollPane_1);
        
        fileList = new JList();
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                fileListClick(arg0);
            }
        });
        scrollPane_1.setViewportView(fileList);
        
        caseChkCheckBox = new JCheckBox("忽略大小寫");
        caseChkCheckBox.setBounds(256, 39, 97, 23);
        panel.add(caseChkCheckBox);
        
        regexCheckBox = new JCheckBox("正則");
        regexCheckBox.setBounds(355, 39, 64, 23);
        panel.add(regexCheckBox);
    }
    
    public void fileListClick(MouseEvent arg0) {
        final File file = JListUtil.getLeadSelectionObject(fileList);
        JPopupMenuUtil.newInstance(fileList)//
                .addJMenuItem("開啟", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            Desktop.getDesktop().edit(file);
                        } catch (IOException e) {
                            JCommonUtil.handleException(e);
                        }
                    }
                }).addJMenuItem("目錄", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            Desktop.getDesktop().open(file.getParentFile());
                        } catch (IOException e) {
                            JCommonUtil.handleException(e);
                        }
                    }
                }).applyEvent(arg0).show();
    }
    public void resetBtnClick(){
        rootNode = new DefaultMutableTreeNode();
        rootNode.setUserObject("點此加入條件");
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        conditionTree.setModel(treeModel);
        searchText.setText("");
        defaultFileList = new ArrayList<File>();
    }
    public void addConditionBtnClick(){
        String selectType1 = (String)selectTypeComboBox.getSelectedItem();
        String selectType2 = (String)selectType2ComboBox.getSelectedItem();
        String searchTextStr = searchText.getText();
//        System.out.println(selectType1);
//        System.out.println(selectType2);
//        System.out.println(searchTextStr);
        
        DefaultMutableTreeNode parent = jtreeUtil.getSelectItem();
        if(parent == null){
            JCommonUtil._jOptionPane_showMessageDialog_error("請選擇節點!");
            return;
        }
        if(parent == rootNode){
            DefaultMutableTreeNode d = new DefaultMutableTreeNode();
            d.setUserObject(selectType1);
            parent.add(d);
        }else if(!(parent.getUserObject() instanceof ConditionNode)){
            ConditionNode nodeObj = new ConditionNode(selectType2, searchTextStr);
            DefaultMutableTreeNode d = new DefaultMutableTreeNode();
            d.setUserObject(nodeObj);
            parent.add(d);
        }
//        for(int ii = 0 ; ii < parent.getChildCount(); ii ++){
//            System.out.println(ii +"-----"+parent.getChildAt(ii));
//        }
        conditionTree.updateUI();
    }
    
    List<File> defaultFileList;
    private JList fileList;
    private JCheckBox caseChkCheckBox;
    private JCheckBox regexCheckBox;
    
    public void startSearchBtnClick(){
        try{
            if(StringUtils.isBlank(subFileNameText.getText())){
                JCommonUtil._jOptionPane_showMessageDialog_error("請輸入副檔名,以\",\"分隔!");
                return;
            }
            File dir = JCommonUtil.filePathCheck(dirText.getText(), "檔案目錄", true);
            String[] subFileNameArray = StringUtils.defaultString(subFileNameText.getText()).split(",", -1);
            defaultFileList = new ArrayList<File>();
            for(String sub : subFileNameArray){
                FileUtil.searchFilefind(dir, ".*\\.(" + sub.toLowerCase() + "|" + sub.toUpperCase() + ")$", defaultFileList);
            }
            if(defaultFileList.isEmpty()){
                JCommonUtil._jOptionPane_showMessageDialog_error("目錄下無此附檔名檔案存在:" + Arrays.toString(subFileNameArray));
                return;
            }
            
            for(int ii = 0 ; ii < rootNode.getChildCount(); ii ++){
                DefaultMutableTreeNode ch = (DefaultMutableTreeNode)rootNode.getChildAt(ii);
                String andOr = (String)ch.getUserObject();
                System.out.println(ii + "-----------" + andOr);
                
                List<File> tempList = new ArrayList<File>();
                for(int jj = 0 ; jj < ch.getChildCount(); jj ++){
                    DefaultMutableTreeNode ch2 = (DefaultMutableTreeNode)ch.getChildAt(jj);
                    ConditionNode nod = (ConditionNode)ch2.getUserObject();
                    findList(andOr, nod, tempList);
                }
                if("OR".equals(andOr)){
                    defaultFileList = tempList;
                }
            }
            
            DefaultListModel model = JListUtil.createModel();
            for(File f : defaultFileList){
                model.addElement(f);
            }
            fileList.setModel(model);
            
            JCommonUtil._jOptionPane_showMessageDialog_info("搜尋完成找到符合項目 :" + model.getSize());
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    private void findList(String andOr, ConditionNode nod, List<File> tempList){
        if(tempList == null){
            throw new RuntimeException("tempList不可為空");
        }
        for(int ii = 0 ; ii < defaultFileList.size(); ii ++){
            File file = defaultFileList.get(ii);
            boolean findChk = findOK(nod, file);
            System.out.println(findChk + "<<<<<" + nod + " --- " + file);
            if("AND".equals(andOr) && !findChk){
                defaultFileList.remove(ii);
                ii --;
            }else if("OR".equals(andOr) && findChk && !tempList.contains(file)){
                tempList.add(file);
            }
        }
    }
    
    private boolean findOK(ConditionNode nod, File file) {
        try {
            String content = FileUtils.readFileToString(file, (String) encodeComboBox.getSelectedItem());
            String searchTextStr = nod.searchTextStr;
            if(caseChkCheckBox.isSelected()){
                content = content.toLowerCase();
                searchTextStr = searchTextStr.toLowerCase();
            }
            boolean chk = false;
            if(regexCheckBox.isSelected()){
                chk = content.contains(searchTextStr);
            }else{
                Pattern ptn = Pattern.compile(searchTextStr);
                Matcher mth = ptn.matcher(content);
                chk = mth.find(); 
            }
            if("包含".equals(nod.selectType2) && chk){
                return true;
            }
            if("不含".equals(nod.selectType2) && !chk){
                return true;
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            throw new RuntimeException(ex);
        }
        return false;
    }
    
    public void conditionTreeMouseClick(MouseEvent arg0){
        final DefaultMutableTreeNode d = jtreeUtil.getSelectItem();
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            JPopupMenuUtil.newInstance(conditionTree).addJMenuItem("移除", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if(d.getParent()!=null){
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)d.getParent();
                        parent.remove(d);
                        conditionTree.updateUI();
                    }
                }
            }).applyEvent(arg0).show();
        }else{
            System.out.println(d);
        }
    }
    
    private static class ConditionNode {
        String selectType2;
        String searchTextStr;
        @Override
        public String toString() {
            return selectType2 + " " + searchTextStr;
        }
        public ConditionNode(String selectType2, String searchTextStr) {
            super();
            this.selectType2 = selectType2;
            this.searchTextStr = searchTextStr;
        }
    }
}
