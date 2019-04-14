package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class HtmlFormLoadFromXlsUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JLabel lblNewLabel;
    private JTextField xlsFileText;
    private JButton readXlsBtn;
    private JButton clearBtn;
    private JTextArea xlsResultArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(HtmlFormLoadFromXlsUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HtmlFormLoadFromXlsUI frame = new HtmlFormLoadFromXlsUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public HtmlFormLoadFromXlsUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 523, 387);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("功能頁簽", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("讀取檔案");
        panel_3.add(lblNewLabel);

        xlsFileText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(xlsFileText, false);
        panel_3.add(xlsFileText);
        xlsFileText.setColumns(30);

        readXlsBtn = new JButton("執行");
        readXlsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("readXlsBtn.click", e);
            }
        });
        panel_3.add(readXlsBtn);

        clearBtn = new JButton("清除");
        panel_3.add(clearBtn);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.SOUTH);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.EAST);

        xlsResultArea = new JTextArea();
        panel.add(JCommonUtil.createScrollComponent(xlsResultArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("空", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addActionHex(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addActionHex("readXlsBtn.click", new Action() {
            class RowObj {
                String title;
                List<String> inputs = new ArrayList<String>();

                public String toString() {
                    return title + "  " + StringUtils.join(inputs, " ");
                }
            }

            final String InputPtn = "^.*\\[\\w+\\]$";

            @Override
            public void action(EventObject evt) throws Exception {
                File xlsFile = JCommonUtil.filePathCheck(xlsFileText.getText(), "請輸入xls", "xls");
                ExcelUtil_Xls97 eutil = ExcelUtil_Xls97.getInstance();
                HSSFWorkbook wb = eutil.readExcel(xlsFile);
                HSSFSheet sheet = wb.getSheetAt(0);

                StringBuilder sb = new StringBuilder();
                for (int rowPos = 0; rowPos <= sheet.getLastRowNum(); rowPos++) {
                    Row row = eutil.getRowChk(sheet, rowPos);

                    List<RowObj> rowLst = new ArrayList<RowObj>();
                    RowObj tmpRow = null;
                    for (int colPos = 0; colPos <= row.getLastCellNum(); colPos++) {
                        if (colPos == 0) {
                            tmpRow = new RowObj();
                        }

                        String text = eutil.readCell(row, colPos);
                        text = StringUtils.trimToEmpty(text);
                        if (StringUtils.isNotBlank(text)) {
                            System.out.println(text);

                            if (!text.matches(InputPtn)) {
                                if (tmpRow != null && colPos != 0) {
                                    rowLst.add(tmpRow);
                                    tmpRow = new RowObj();
                                }
                                tmpRow.title = text;
                            } else {
                                if (tmpRow == null) {
                                    throw new Exception("[第一欄必須是title] ERR text : " + text);
                                }
                                tmpRow.inputs.add(text);
                            }
                        }

                        if (colPos == row.getLastCellNum()) {
                            rowLst.add(tmpRow);
                        }
                    }

                    for (RowObj r : rowLst) {
                        sb.append(r + "\r\n");
                    }
                    sb.append("\r\n\r\n");
                }

                xlsResultArea.setText(sb.toString());
            }
        });
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("item2", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
