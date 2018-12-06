package gtu.log.finder;

import java.awt.Container;
import java.awt.MenuItem;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.table.JTableHeader;
import javax.swing.text.Document;

import org.mockito.Mockito;

import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTreeUtil;

public class DebugMointerUIUIContent {

    private boolean useMockUI = false;

    private JDialog _ui;

    //↓↓↓↓↓↓
    private JTextFieldKeeper classNameText;
    private JTextFieldKeeper classpathText;
    private JTextFieldKeeper executeMethodNameText;
    private JListKeeper executeList;
    //↑↑↑↑↑↑

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTree compositeTree;
    private JComboBox tempObjClassCombox;
    private JTextArea errorLogArea;
    private JTextArea callStackArea;
    private JButton indicateNodeClassBtn;
    private JComboBox nodeInfoComboBox;
    private JCheckBox tempModelCheckBox;
    private JScrollPane jScrollPane12;
    private JPanel jPanel16;
    private JLabel jLabel5;
    private JButton executeMethodBtn;
    private JScrollPane jScrollPane11;
    private JEditorPane showInfoArea2;
    private JScrollPane jScrollPane10;
    private JEditorPane showInfoArea;
    private JPanel jPanel15;
    private JPanel jPanel14;
    private JTabbedPane jTabbedPane2;
    private JScrollPane jScrollPane9;
    private JPanel jPanel13;
    private JButton importClassBtn;
    private JLabel jLabel4;
    private JLabel jLabel3;
    private JPanel jPanel12;
    private JPanel jPanel11;
    private JLabel jLabel2;
    private JButton tempValueSetBtn;
    private JTextField tempValueSetText;
    private JComboBox tempValueCombox;
    private JScrollPane jScrollPane8;
    private JList tempList;
    private JScrollPane jScrollPane7;
    private JTable tempValueTable;
    private JPanel jPanel10;
    private JPanel jPanel9;
    private JScrollPane jScrollPane5;
    private JTable parameterTable;
    private JPanel jPanel7;
    private JScrollPane jScrollPane4;
    private JTable breakpointTable;
    private JScrollPane jScrollPane3;
    private JTextArea conditionArea;
    private JToggleButton conditionActivationBtn;
    private JPanel jPanel6;
    private JPanel jPanel5;
    private JComboBox moniterComboBox;
    private JScrollPane jScrollPane2;
    private JLabel jLabel1;
    private JTable compositeTable;
    private JPanel jPanel4;
    private JScrollPane jScrollPane1;
    private JPanel jPanel3;
    private JPanel jPanel2;
    private JTreeUtil jtreeUtil;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextArea dynamicClassArea;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JTextArea runningClassArea;
    private JLabel lblClasses;
    private JTextField dynamic_classesDirText;
    private JLabel lblClass;
    private JTextField dynamic_classNameText;
    private JButton dynamic_executeBtn;
    private JTextField dynamic_classPathText;
    private JLabel lblJavahome;
    private JTextField dynamic_javaHomeText;
    private JLabel lblMvn;
    private JTextField dynamic_mvnProjectText;

    public DebugMointerUIUIContent() {
    }

    public void initUI() {
        try {
            initNormalUI();
            
            System.out.println("<<< use UI mode !!!!");
        } catch (java.awt.HeadlessException ex) {
            // 使用mock UI
            useMockUI = true;

            initMockUI();

            // 呼叫細節處理
            mockMethodInvoke();
            
            System.out.println("<<< use NonUI mode !!!!");
        }
    }

    private void initNormalUI() {
        _ui = new JDialog();

        classNameText = new JTextFieldKeeper();
        classpathText = new JTextFieldKeeper();
        executeMethodNameText = new JTextFieldKeeper();
        executeList = new JListKeeper();

        breakpointTable = new JTable();
        callStackArea = new JTextArea();
        compositeTable = new JTable();
        compositeTree = new JTree();
        conditionActivationBtn = new JToggleButton();
        conditionArea = new JTextArea();
        errorLogArea = new JTextArea();
        executeMethodBtn = new JButton();
        importClassBtn = new JButton();
        indicateNodeClassBtn = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jPanel1 = new JPanel();
        jPanel10 = new JPanel();
        jPanel11 = new JPanel();
        jPanel12 = new JPanel();
        jPanel13 = new JPanel();
        jPanel14 = new JPanel();
        jPanel15 = new JPanel();
        jPanel16 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jPanel6 = new JPanel();
        jPanel7 = new JPanel();
        jPanel9 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jScrollPane10 = new JScrollPane();
        jScrollPane11 = new JScrollPane();
        jScrollPane12 = new JScrollPane();
        jScrollPane2 = new JScrollPane();
        jScrollPane3 = new JScrollPane();
        jScrollPane4 = new JScrollPane();
        jScrollPane5 = new JScrollPane();
        jScrollPane7 = new JScrollPane();
        jScrollPane8 = new JScrollPane();
        jScrollPane9 = new JScrollPane();
        jTabbedPane1 = new JTabbedPane();
        jTabbedPane2 = new JTabbedPane();
        moniterComboBox = new JComboBox();
        nodeInfoComboBox = new JComboBox();
        parameterTable = new JTable();
        showInfoArea = new JEditorPane();
        showInfoArea2 = new JEditorPane();
        tempList = new JList();
        tempModelCheckBox = new JCheckBox();
        tempObjClassCombox = new JComboBox();
        tempValueCombox = new JComboBox();
        tempValueSetBtn = new JButton();
        tempValueSetText = new JTextField();
        tempValueTable = new JTable();
        dynamic_classesDirText = new JTextField();
        dynamic_classNameText = new JTextField();
        dynamic_classPathText = new JTextField();
        dynamic_executeBtn = new JButton("執行");
        dynamic_javaHomeText = new JTextField();
        dynamic_mvnProjectText = new JTextField();
        dynamicClassArea = new JTextArea();
        lblClass = new JLabel("class名");
        lblMvn = new JLabel("mvn專案目錄");
        lblJavahome = new JLabel("JAVA_HOME");
        lblClasses = new JLabel("classes目錄");
        panel_1 = new JPanel();
        panel_2 = new JPanel();
        panel_3 = new JPanel();
        panel = new JPanel();
        runningClassArea = new JTextArea();
        scrollPane = new JScrollPane();
        
        JTextAreaUtil.applyCommonSetting(callStackArea);
        JTextAreaUtil.applyCommonSetting(conditionArea);
        JTextAreaUtil.applyCommonSetting(errorLogArea);
        JTextAreaUtil.applyCommonSetting(dynamicClassArea);
        JTextAreaUtil.applyCommonSetting(runningClassArea);
    }

    private void initMockUI() {
        _ui = Mockito.mock(JDialog.class);
        breakpointTable = Mockito.mock(JTable.class);
        callStackArea = Mockito.mock(JTextArea.class);

        classNameText = new JTextFieldKeeper();
        classpathText = new JTextFieldKeeper();
        executeMethodNameText = new JTextFieldKeeper();
        executeList = new JListKeeper();

        compositeTable = Mockito.mock(JTable.class);
        compositeTree = Mockito.mock(JTree.class);
        conditionActivationBtn = Mockito.mock(JToggleButton.class);
        conditionArea = Mockito.mock(JTextArea.class);
        dynamic_classesDirText = Mockito.mock(JTextField.class);
        dynamic_classNameText = Mockito.mock(JTextField.class);
        dynamic_classPathText = Mockito.mock(JTextField.class);
        dynamic_executeBtn = Mockito.mock(JButton.class);
        dynamic_javaHomeText = Mockito.mock(JTextField.class);
        dynamic_mvnProjectText = Mockito.mock(JTextField.class);
        dynamicClassArea = Mockito.mock(JTextArea.class);
        errorLogArea = Mockito.mock(JTextArea.class);
        executeMethodBtn = Mockito.mock(JButton.class);
        importClassBtn = Mockito.mock(JButton.class);
        indicateNodeClassBtn = Mockito.mock(JButton.class);
        jLabel1 = Mockito.mock(JLabel.class);
        jLabel2 = Mockito.mock(JLabel.class);
        jLabel3 = Mockito.mock(JLabel.class);
        jLabel4 = Mockito.mock(JLabel.class);
        jLabel5 = Mockito.mock(JLabel.class);
        jPanel1 = Mockito.mock(JPanel.class);
        jPanel10 = Mockito.mock(JPanel.class);
        jPanel11 = Mockito.mock(JPanel.class);
        jPanel12 = Mockito.mock(JPanel.class);
        jPanel13 = Mockito.mock(JPanel.class);
        jPanel14 = Mockito.mock(JPanel.class);
        jPanel15 = Mockito.mock(JPanel.class);
        jPanel16 = Mockito.mock(JPanel.class);
        jPanel2 = Mockito.mock(JPanel.class);
        jPanel3 = Mockito.mock(JPanel.class);
        jPanel4 = Mockito.mock(JPanel.class);
        jPanel5 = Mockito.mock(JPanel.class);
        jPanel6 = Mockito.mock(JPanel.class);
        jPanel7 = Mockito.mock(JPanel.class);
        jPanel9 = Mockito.mock(JPanel.class);
        jScrollPane1 = Mockito.mock(JScrollPane.class);
        jScrollPane10 = Mockito.mock(JScrollPane.class);
        jScrollPane11 = Mockito.mock(JScrollPane.class);
        jScrollPane12 = Mockito.mock(JScrollPane.class);
        jScrollPane2 = Mockito.mock(JScrollPane.class);
        jScrollPane3 = Mockito.mock(JScrollPane.class);
        jScrollPane4 = Mockito.mock(JScrollPane.class);
        jScrollPane5 = Mockito.mock(JScrollPane.class);
        jScrollPane7 = Mockito.mock(JScrollPane.class);
        jScrollPane8 = Mockito.mock(JScrollPane.class);
        jScrollPane9 = Mockito.mock(JScrollPane.class);
        jTabbedPane1 = Mockito.mock(JTabbedPane.class);
        jTabbedPane2 = Mockito.mock(JTabbedPane.class);
        lblClass = Mockito.mock(JLabel.class);
        lblClasses = Mockito.mock(JLabel.class);
        lblJavahome = Mockito.mock(JLabel.class);
        lblMvn = Mockito.mock(JLabel.class);
        moniterComboBox = Mockito.mock(JComboBox.class);
        nodeInfoComboBox = Mockito.mock(JComboBox.class);
        panel_1 = Mockito.mock(JPanel.class);
        panel_2 = Mockito.mock(JPanel.class);
        panel_3 = Mockito.mock(JPanel.class);
        panel = Mockito.mock(JPanel.class);
        parameterTable = Mockito.mock(JTable.class);
        runningClassArea = Mockito.mock(JTextArea.class);
        scrollPane = Mockito.mock(JScrollPane.class);
        showInfoArea = Mockito.mock(JEditorPane.class);
        showInfoArea2 = Mockito.mock(JEditorPane.class);
        tempList = Mockito.mock(JList.class);
        tempModelCheckBox = Mockito.mock(JCheckBox.class);
        tempObjClassCombox = Mockito.mock(JComboBox.class);
        tempValueCombox = Mockito.mock(JComboBox.class);
        tempValueSetBtn = Mockito.mock(JButton.class);
        tempValueSetText = Mockito.mock(JTextField.class);
        tempValueTable = Mockito.mock(JTable.class);
    }

    private void mockMethodInvoke() {
        Container container = Mockito.mock(Container.class);
        Mockito.when(_ui.getContentPane()).thenReturn(container);
        JTableHeader jTableHeader = Mockito.mock(JTableHeader.class);
        Mockito.when(parameterTable.getTableHeader()).thenReturn(jTableHeader);
        Document document = Mockito.mock(Document.class);
        Mockito.when(getDynamic_javaHomeText().getDocument()).thenReturn(document);
        Mockito.when(getDynamic_mvnProjectText().getDocument()).thenReturn(document);
        Mockito.when(getDynamic_classesDirText().getDocument()).thenReturn(document);
    }

    public JDialog get_ui() {
        return _ui;
    }

    public void set_ui(JDialog _ui) {
        this._ui = _ui;
    }

    public JTextFieldKeeper getClassNameText() {
        return classNameText;
    }

    public JTextFieldKeeper getClasspathText() {
        return classpathText;
    }

    public JTextFieldKeeper getExecuteMethodNameText() {
        return executeMethodNameText;
    }

    public JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JTree getCompositeTree() {
        return compositeTree;
    }

    public JComboBox getTempObjClassCombox() {
        return tempObjClassCombox;
    }

    public JTextArea getErrorLogArea() {
        return errorLogArea;
    }

    public JTextArea getCallStackArea() {
        return callStackArea;
    }

    public JButton getIndicateNodeClassBtn() {
        return indicateNodeClassBtn;
    }

    public JComboBox getNodeInfoComboBox() {
        return nodeInfoComboBox;
    }

    public JCheckBox getTempModelCheckBox() {
        return tempModelCheckBox;
    }

    public JScrollPane getjScrollPane12() {
        return jScrollPane12;
    }

    public JListKeeper getExecuteList() {
        return executeList;
    }

    public JPanel getjPanel16() {
        return jPanel16;
    }

    public JLabel getjLabel5() {
        return jLabel5;
    }

    public JButton getExecuteMethodBtn() {
        return executeMethodBtn;
    }

    public JScrollPane getjScrollPane11() {
        return jScrollPane11;
    }

    public JEditorPane getShowInfoArea2() {
        return showInfoArea2;
    }

    public JScrollPane getjScrollPane10() {
        return jScrollPane10;
    }

    public JEditorPane getShowInfoArea() {
        return showInfoArea;
    }

    public JPanel getjPanel15() {
        return jPanel15;
    }

    public JPanel getjPanel14() {
        return jPanel14;
    }

    public JTabbedPane getjTabbedPane2() {
        return jTabbedPane2;
    }

    public JScrollPane getjScrollPane9() {
        return jScrollPane9;
    }

    public JPanel getjPanel13() {
        return jPanel13;
    }

    public JButton getImportClassBtn() {
        return importClassBtn;
    }

    public JLabel getjLabel4() {
        return jLabel4;
    }

    public JLabel getjLabel3() {
        return jLabel3;
    }

    public JPanel getjPanel12() {
        return jPanel12;
    }

    public JPanel getjPanel11() {
        return jPanel11;
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }

    public JButton getTempValueSetBtn() {
        return tempValueSetBtn;
    }

    public JTextField getTempValueSetText() {
        return tempValueSetText;
    }

    public JComboBox getTempValueCombox() {
        return tempValueCombox;
    }

    public JScrollPane getjScrollPane8() {
        return jScrollPane8;
    }

    public JList getTempList() {
        return tempList;
    }

    public JScrollPane getjScrollPane7() {
        return jScrollPane7;
    }

    public JTable getTempValueTable() {
        return tempValueTable;
    }

    public JPanel getjPanel10() {
        return jPanel10;
    }

    public JPanel getjPanel9() {
        return jPanel9;
    }

    public JScrollPane getjScrollPane5() {
        return jScrollPane5;
    }

    public JTable getParameterTable() {
        return parameterTable;
    }

    public JPanel getjPanel7() {
        return jPanel7;
    }

    public JScrollPane getjScrollPane4() {
        return jScrollPane4;
    }

    public JTable getBreakpointTable() {
        return breakpointTable;
    }

    public JScrollPane getjScrollPane3() {
        return jScrollPane3;
    }

    public JTextArea getConditionArea() {
        return conditionArea;
    }

    public JToggleButton getConditionActivationBtn() {
        return conditionActivationBtn;
    }

    public JPanel getjPanel6() {
        return jPanel6;
    }

    public JPanel getjPanel5() {
        return jPanel5;
    }

    public JComboBox getMoniterComboBox() {
        return moniterComboBox;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public JTable getCompositeTable() {
        return compositeTable;
    }

    public JPanel getjPanel4() {
        return jPanel4;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JPanel getjPanel3() {
        return jPanel3;
    }

    public JPanel getjPanel2() {
        return jPanel2;
    }

    public JTreeUtil getJtreeUtil() {
        return jtreeUtil;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTextArea getDynamicClassArea() {
        return dynamicClassArea;
    }

    public JPanel getPanel_1() {
        return panel_1;
    }

    public JPanel getPanel_2() {
        return panel_2;
    }

    public JPanel getPanel_3() {
        return panel_3;
    }

    public JTextArea getRunningClassArea() {
        return runningClassArea;
    }

    public JLabel getLblClasses() {
        return lblClasses;
    }

    public JTextField getDynamic_classesDirText() {
        return dynamic_classesDirText;
    }

    public JLabel getLblClass() {
        return lblClass;
    }

    public JTextField getDynamic_classNameText() {
        return dynamic_classNameText;
    }

    public JButton getDynamic_executeBtn() {
        return dynamic_executeBtn;
    }

    public JTextField getDynamic_classPathText() {
        return dynamic_classPathText;
    }

    public JLabel getLblJavahome() {
        return lblJavahome;
    }

    public JTextField getDynamic_javaHomeText() {
        return dynamic_javaHomeText;
    }

    public JLabel getLblMvn() {
        return lblMvn;
    }

    public JTextField getDynamic_mvnProjectText() {
        return dynamic_mvnProjectText;
    }

    class JListKeeper {
        private JList list;
        private DefaultListModel model;

        private JListKeeper() {
            if (!useMockUI) {
                list = new JList();
            } else {
                list = Mockito.mock(JList.class);
            }
        }

        public JList get() {
            return list;
        }

        public DefaultListModel getModel() {
            if (!useMockUI) {
                return (DefaultListModel) list.getModel();
            } else {
                return this.model;
            }
        }

        public void setModel(DefaultListModel model) {
            if (!useMockUI) {
                list.setModel(model);
            } else {
                this.model = model;
            }
        }
    }

    class JTextFieldKeeper {
        private JTextField text;
        private String textKeeper;

        private JTextFieldKeeper() {
            if (!useMockUI) {
                text = new JTextField();
            } else {
                text = Mockito.mock(JTextField.class);
            }
        }

        public String getText() {
            if (!useMockUI) {
                return text.getText();
            } else {
                return textKeeper;
            }
        }

        public void setText(String textStr) {
            if (!useMockUI) {
                text.setText(textStr);
            } else {
                textKeeper = textStr;
            }
        }

        public JTextField get() {
            return text;
        }
    }

    public MenuItem createMenuItem() {
        if (!useMockUI) {
            return new MenuItem();
        } else {
            return Mockito.mock(MenuItem.class);
        }
    }

    public JScrollPane createJScrollPane() {
        if (!useMockUI) {
            return new JScrollPane();
        } else {
            return Mockito.mock(JScrollPane.class);
        }
    }

    public boolean isUseMockUI() {
        return useMockUI;
    }

    public void setJtreeUtil(JTreeUtil newInstance) {
        this.jtreeUtil = newInstance;
    }
}