package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;

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

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.clipboard.ClipboardUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StringSubstringCheckerUI extends JFrame {

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
    private JTextArea textArea;
    private JLabel lblStartpos;
    private JTextField startPosText;
    private JLabel lblEndpos;
    private JTextField endPosText;
    private JButton executeBtn;
    private JButton clearBtn;
    private JButton copySelectionBtn;
    private JLabel positionLbl;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(StringSubstringCheckerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StringSubstringCheckerUI frame = new StringSubstringCheckerUI();
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
    public StringSubstringCheckerUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 677, 532);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        positionLbl = new JLabel("        ");
        panel_3.add(positionLbl);

        lblStartpos = new JLabel("startPos");
        panel_3.add(lblStartpos);

        startPosText = new JTextField();
        panel_3.add(startPosText);
        startPosText.setColumns(10);

        lblEndpos = new JLabel("endPos");
        panel_3.add(lblEndpos);

        endPosText = new JTextField();
        endPosText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                swingUtil.invokeAction("text.onblur", arg0);
            }
        });

        panel_3.add(endPosText);
        endPosText.setColumns(10);

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("text.onblur", e);
            }
        });
        panel_3.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                swingUtil.invokeAction("btn.clear", arg0);
            }
        });

        copySelectionBtn = new JButton("複製選擇");
        copySelectionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("copySelectionBtn", e);
            }
        });
        panel_3.add(copySelectionBtn);
        panel_3.add(clearBtn);

        startPosText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                swingUtil.invokeAction("text.onblur", arg0);
            }
        });

        // startPosText.getDocument().addDocumentListener(ActionAdapter.DocumentListener.create("text.onblur",
        // swingUtil));
        // endPosText.getDocument().addDocumentListener(ActionAdapter.DocumentListener.create("text.onblur",
        // swingUtil));

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        textArea = new JTextArea();
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("textArea.click", e);
            }
        });
        JTextAreaUtil.applyCommonSetting(textArea);
        panel.add(JCommonUtil.createScrollComponent(textArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

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
        swingUtil.addActionHex("text.onblur", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                try {
                    int pos = Integer.parseInt(StringUtils.trimToEmpty(((JTextField) evt.getSource()).getText()));
                    textArea.setCaretPosition(pos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    int startPos = Integer.parseInt(StringUtils.trimToEmpty(startPosText.getText()));
                    int endPos = Integer.parseInt(StringUtils.trimToEmpty(endPosText.getText()));

                    if (endPos < startPos) {
                        int tmpPos = endPos;
                        endPos = startPos;
                        startPos = tmpPos;
                    }

                    textArea.setSelectionStart(startPos);
                    textArea.setSelectionEnd(endPos);
                    textArea.setSelectionColor(Color.yellow);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                textArea.setFocusable(true);
                textArea.requestFocus();
            }
        });
        swingUtil.addActionHex("btn.clear", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                startPosText.setText("");
                endPosText.setText("");
                textArea.setText("");
            }
        });
        swingUtil.addActionHex("copySelectionBtn", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                try {
                    int startPos = Integer.parseInt(StringUtils.trimToEmpty(startPosText.getText()));
                    int endPos = Integer.parseInt(StringUtils.trimToEmpty(endPosText.getText()));
                    String text = StringUtils.substring(textArea.getText(), startPos, endPos);
                    ClipboardUtil.getInstance().setContents(text);
                    JCommonUtil._jOptionPane_showMessageDialog_info("以複製!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        swingUtil.addActionHex("textArea.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                positionLbl.setText("" + textArea.getCaretPosition());
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
