package gtu.db.simple_dao_gen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

public class GenCustomSelectMethodUI extends JFrame {

    private JPanel contentPane;
    private JTextArea sqlArea;
    private JTable paramTable;
    private JTextField methodNameText;
    private JTextArea resultStrArea;
    private ButtonGroup sqlTypeRadioGroup;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GenCustomSelectMethodUI frame = new GenCustomSelectMethodUI();
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
    public GenCustomSelectMethodUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 612, 471);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JButton btnNewButton = new JButton("Generate");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButtonAction();
            }
        });

        JPanel p33 = new JPanel();
        p33.add(btnNewButton);
        panel.add(p33, BorderLayout.SOUTH);

        JButton btnClear = new JButton("clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAction();
            }
        });
        p33.add(btnClear);

        paramTable = new JTable();
        paramTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                List<JMenuItem> menuList = JTableUtil.newInstance(paramTable).getDefaultJMenuItems_NoAddRemoveColumn();
                JPopupMenuUtil.newInstance(paramTable).addJMenuItem(menuList).applyEvent(e).show();
            }
        });
        initModel();
        JTableUtil.defaultSetting_AutoResize(paramTable);
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.setViewportView(paramTable);
        panel.add(jScrollPane2, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(130, 0));
        jScrollPane2.setRowHeaderView(panel_1);
        panel_1.setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("96px"), FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("51px"),
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("57px"), },
                new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("23px"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, }));

        methodNameText = new JTextField();
        panel_1.add(methodNameText, "2, 2, left, center");
        methodNameText.setColumns(10);

        JRadioButton selectRadio = new JRadioButton("select");
        panel_1.add(selectRadio, "2, 4, left, top");

        JRadioButton updateRadio = new JRadioButton("update");
        panel_1.add(updateRadio, "2, 6, left, top");
        sqlTypeRadioGroup = JButtonGroupUtil.createRadioButtonGroup(selectRadio, updateRadio);

        sqlArea = new JTextArea();
        sqlArea.setPreferredSize(new Dimension(0, 75));
        sqlArea.setBackground(Color.YELLOW);
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(sqlArea);
        panel.add(jScrollPane1, BorderLayout.NORTH);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_2, null);
        panel_2.setLayout(new BorderLayout(0, 0));

        resultStrArea = new JTextArea();
        JScrollPane jScrollPan3 = new JScrollPane();
        jScrollPan3.setViewportView(resultStrArea);
        panel_2.add(jScrollPan3, BorderLayout.CENTER);
        
        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/dao.ico");
    }

    private void initModel() {
        JComboBox box = new JComboBox();
        DefaultComboBoxModel bm = new DefaultComboBoxModel();
        for (DbType e : DbType.values()) {
            bm.addElement(e);
        }
        box.setModel(bm);
        DefaultTableModel model = JTableUtil.createModel(false, "名稱", "類別");
        model.addRow(new Object[] { "", "" });
        paramTable.setModel(model);
        JTableUtil.newInstance(paramTable).columnIsComponent(1, box);
    }

    private enum DbType {
        t1("string", String.class), //
        t2("int", Integer.class), //
        t3("date", Timestamp.class), //
        ;
        final String label;
        final Class<?> clz;

        DbType(String label, Class<?> clz) {
            this.label = label;
            this.clz = clz;
        }

        public String toString() {
            return label;
        }
    }

    private void clearAction() {
        methodNameText.setText("");
        sqlArea.setText("");
        resultStrArea.setText("");
        initModel();
    }

    private void btnNewButtonAction() {
        try {
            String methodName = methodNameText.getText();
            String sql = sqlArea.getText();
            Validate.notEmpty(methodName, "請輸入方法名稱");
            Validate.notEmpty(sql, "請輸入sql");

            DefaultTableModel model = JTableUtil.newInstance(paramTable).getModel();
            Map<String, Class<?>> paramMap = new LinkedHashMap<String, Class<?>>();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String key = (String) model.getValueAt(ii, 0);
                if (StringUtils.isBlank(key)) {
                    continue;
                }
                if(model.getValueAt(ii, 1) instanceof String) {
                    Validate.isTrue(false, "請選擇類別下拉");
                }
                DbType d = (DbType) model.getValueAt(ii, 1);
                paramMap.put(key, d.clz);
            }

            GenCustomSelectMethod t1 = new GenCustomSelectMethod();
            GenCustomUpdateMethod t2 = new GenCustomUpdateMethod();
            
            String returnStr = "";
            JRadioButton choiceRadio = ((JRadioButton)JButtonGroupUtil.getSelectedButton(sqlTypeRadioGroup));
            Validate.isTrue(choiceRadio != null, "請選擇方法類型radio");
            
            String sqlType= choiceRadio.getText();
            
            if("select".equals(sqlType)) {
                returnStr = t1.execute(methodName, sql, paramMap);
            }else if("update".equals(sqlType)) {
                returnStr = t2.execute(methodName, sql, paramMap);
            }
            
            resultStrArea.setText(returnStr);
            JCommonUtil._jOptionPane_showMessageDialog_error("完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
