package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.reflect.MethodUtils;
import org.codehaus.plexus.util.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.swing.util.JComboBoxUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextWidthFitter;

public class EnglishSearchUI_MemoryBank_DialogOtherUI extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel questionLabel;
    private ActionListener deleteConfigAction;
    private ActionListener choiceRadioAction;
    private ActionListener customDescAction;
    private ActionListener skipBtnAction;
    private ActionListener skipAllBtnAction;
    private ActionListener onDismissAction;
    private ActionListener onCreateAction;
    private ActionListener appendBtnAction;
    private ActionListener suspendTerminatedBtnAction;
    private JButton deleteFromMemoryBankBtn;
    private JButton customDescBtn;
    private JButton skipAllBtn;
    private JComboBox hotKeyComboBox;
    private JComboBox windowSizeComboBox;
    private static AtomicInteger KEY_MAPPING_INDEX = new AtomicInteger(-1);
    private static AtomicInteger WINDOW_SIZE_INDEX = new AtomicInteger(-1);
    private static AtomicInteger ALIGENT_TYPE_INDEX = new AtomicInteger(-1);
    private static final int MAX_LENGTH_DESC = 40;
    private JButton appendBtn;
    private JComboBox alignTypeComboBox;
    private JButton suspendTerminatedBtn;
    private JTextArea questionDescArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        String testStr = "讓各位久等 Vue 課程的重頭戲了！" + //
                "近三小時的前後端分離電商 API 教學已上架，" + //
                "請各位盡情享用~";//
        EnglishSearchUI_MemoryBank_DialogOtherUI dialog = new EnglishSearchUI_MemoryBank_DialogOtherUI();
        dialog.initial();
        dialog.createDialog("title", "abcdefg", "xxxxxxxxxxx", null, null, null, null, null, null, null, null, null);
        dialog.showDialog();
    }

    public void showDialog() {
        if (onCreateAction != null) {
            onCreateAction.actionPerformed(new ActionEvent(this, -1, "create"));
        }
        JCommonUtil.setFrameAtop(this, true);
    }

    public void closeDialog() {
        this.setVisible(false);
        this.dispose();
        if (onDismissAction != null) {
            onDismissAction.actionPerformed(new ActionEvent(this, -1, "onDismiss"));
        }
    }

    public void createDialog(String title, String questionText, String questionDesc, //
            ActionListener deleteConfigAction, ActionListener choiceRadioAction, //
            ActionListener customDescAction, ActionListener skipBtnAction, //
            ActionListener skipAllBtnAction, ActionListener onCreateAction, //
            ActionListener onDismissAction, ActionListener appendBtnAction, //
            ActionListener suspendTerminatedBtnAction) {
        try {
            setTitle(title);
            questionLabel.setText(questionText);
            questionDescArea.setText(questionDesc);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.deleteConfigAction = deleteConfigAction;
            this.choiceRadioAction = choiceRadioAction;
            this.customDescAction = customDescAction;
            this.skipBtnAction = skipBtnAction;
            this.skipAllBtnAction = skipAllBtnAction;
            this.onCreateAction = onCreateAction;
            this.onDismissAction = onDismissAction;
            this.appendBtnAction = appendBtnAction;
            this.suspendTerminatedBtnAction = suspendTerminatedBtnAction;
            this.initialAfter();
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private void initialAfter() {
        if (KEY_MAPPING_INDEX.get() != -1) {
            hotKeyComboBox.setSelectedIndex(KEY_MAPPING_INDEX.get());
        }
        if (WINDOW_SIZE_INDEX.get() != -1) {
            windowSizeComboBox.setSelectedIndex(WINDOW_SIZE_INDEX.get());
        }
        if (ALIGENT_TYPE_INDEX.get() != -1) {
            alignTypeComboBox.setSelectedIndex(ALIGENT_TYPE_INDEX.get());
        }
    }

    /**
     * Create the dialog.
     */
    public EnglishSearchUI_MemoryBank_DialogOtherUI() {
        this.initial();
    }

    public void initial() {
        setBounds(100, 100, 650, 285);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow(5)"), FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), }));
        {
            JLabel lblNewLabel = new JLabel("");
            contentPanel.add(lblNewLabel, "2, 2");
        }
        {
            questionLabel = new JLabel("");
            questionLabel.setFont(new Font("Consolas", Font.PLAIN, 19));
            contentPanel.add(questionLabel, "4, 2");
        }
        {
            questionDescArea = new JTextArea();
            JTextAreaUtil.applyCommonSetting(questionDescArea);
            contentPanel.add(JCommonUtil.createScrollComponent(questionDescArea), "4, 4, fill, fill");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                deleteFromMemoryBankBtn = new JButton("刪除");
                deleteFromMemoryBankBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (deleteConfigAction != null) {
                            deleteConfigAction.actionPerformed(e);
                        }
                    }
                });
                {
                    customDescBtn = new JButton("修正");
                    customDescBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (customDescAction != null) {
                                customDescAction.actionPerformed(e);
                            }
                        }
                    });
                    // -------------------------------------------
                    if (true) {
                        hotKeyComboBox = new JComboBox();
                        hotKeyComboBox.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {
                            }
                        });
                        hotKeyComboBox.setModel(JComboBoxUtil.createModel(KeyMapping.values()));
                        buttonPane.add(hotKeyComboBox);
                    }
                    // -------------------------------------------
                    if (true) {
                        windowSizeComboBox = new JComboBox();
                        windowSizeComboBox.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {
                                ((WindowSizeHandler) windowSizeComboBox.getSelectedItem()).apply(EnglishSearchUI_MemoryBank_DialogOtherUI.this);
                                JCommonUtil.setFoucsToChildren(EnglishSearchUI_MemoryBank_DialogOtherUI.this, EnglishSearchUI_MemoryBank_DialogOtherUI.this);
                                EnglishSearchUI_MemoryBank_DialogOtherUI.this.requestFocusInWindow();
                                WINDOW_SIZE_INDEX.set(windowSizeComboBox.getSelectedIndex());
                            }
                        });
                        windowSizeComboBox.setModel(JComboBoxUtil.createModel(WindowSizeHandler.values()));
                        buttonPane.add(windowSizeComboBox);
                    }
                    // -------------------------------------------
                    if (true) {
                        alignTypeComboBox = new JComboBox();
                        alignTypeComboBox.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {
                                ((AlignType) alignTypeComboBox.getSelectedItem()).apply(EnglishSearchUI_MemoryBank_DialogOtherUI.this);
                                JCommonUtil.setFoucsToChildren(EnglishSearchUI_MemoryBank_DialogOtherUI.this, EnglishSearchUI_MemoryBank_DialogOtherUI.this);
                                EnglishSearchUI_MemoryBank_DialogOtherUI.this.requestFocusInWindow();
                                ALIGENT_TYPE_INDEX.set(alignTypeComboBox.getSelectedIndex());
                            }
                        });
                        alignTypeComboBox.setModel(JComboBoxUtil.createModel(AlignType.values()));
                        buttonPane.add(alignTypeComboBox);
                    }
                    // -------------------------------------------
                    {
                        appendBtn = new JButton("加字");
                        appendBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent paramActionEvent) {
                                if (appendBtnAction != null) {
                                    appendBtnAction.actionPerformed(paramActionEvent);
                                }
                            }
                        });
                        buttonPane.add(appendBtn);
                    }
                    buttonPane.add(customDescBtn);
                }
                buttonPane.add(deleteFromMemoryBankBtn);
            }
            {
                JButton skipBtn = new JButton("跳過");
                skipBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (skipBtnAction != null) {
                            skipBtnAction.actionPerformed(e);
                        }
                    }
                });
                {
                    suspendTerminatedBtn = new JButton("停止");
                    suspendTerminatedBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (suspendTerminatedBtnAction != null) {
                                suspendTerminatedBtnAction.actionPerformed(e);
                            }
                        }
                    });
                    buttonPane.add(suspendTerminatedBtn);
                }
                skipBtn.setActionCommand("OK");
                buttonPane.add(skipBtn);
                getRootPane().setDefaultButton(skipBtn);
            }
            {
                skipAllBtn = new JButton("暫停");
                skipAllBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        if (skipAllBtnAction != null) {
                            skipAllBtnAction.actionPerformed(arg0);
                        }
                    }
                });
                buttonPane.add(skipAllBtn);
            }
        }

        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (skipBtnAction != null) {
                    ActionEvent e2 = new ActionEvent(EnglishSearchUI_MemoryBank_DialogOtherUI.this, -1, "winClose");
                    skipBtnAction.actionPerformed(e2);
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Dialog Key Event - " + e.getKeyChar() + ", " + e.getKeyCode());

                // Key ESC
                if (e.getKeyCode() == 27 && skipBtnAction != null) {
                    skipBtnAction.actionPerformed(null);
                    return;
                }

                KeyMapping emun = (KeyMapping) hotKeyComboBox.getSelectedItem();
                emun.applyEvent(e, EnglishSearchUI_MemoryBank_DialogOtherUI.this);
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent arg0) {
                JCommonUtil.setFoucsToChildren(EnglishSearchUI_MemoryBank_DialogOtherUI.this, EnglishSearchUI_MemoryBank_DialogOtherUI.this);
            }
        });

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setFoucsToChildren(this, this);
        this.setModal(false);
    }

    public void setDeleteConfigAction(ActionListener deleteConfigAction) {
        this.deleteConfigAction = deleteConfigAction;
    }

    public void setChoiceRadioAction(ActionListener choiceRadioAction) {
        this.choiceRadioAction = choiceRadioAction;
    }

    private enum WindowSizeHandler {
        NORMAL("一般", 19, 12, 12), //
        FULLSCREEN("放大", 52, 42, 24),//
        ;

        final String chsName;
        final int questionLabelSize;
        final int answerLabelSize;
        final int kotKeyLabelSize;

        WindowSizeHandler(String chsName, int questionLabelSize, int answerLabelSize, int kotKeyLabelSize) {
            this.chsName = chsName;
            this.questionLabelSize = questionLabelSize;
            this.answerLabelSize = answerLabelSize;
            this.kotKeyLabelSize = kotKeyLabelSize;
        }

        public String toString() {
            return chsName;
        }

        private void apply(EnglishSearchUI_MemoryBank_DialogOtherUI _this) {
            if (this == NORMAL) {
                _this.setBounds(100, 100, 650, 285);
            } else {
                _this.setBounds(100, 100, 1280, 520);
            }

            _this.questionLabel.setFont(new Font("Consolas", Font.PLAIN, questionLabelSize));
            _this.questionDescArea.setFont(new Font("Consolas", Font.PLAIN, questionLabelSize));

            JCommonUtil.setJFrameCenter(_this);
        }
    }

    private enum KeyMapping {
        abcde(new char[] { 'a', 'b', 'c', 'd', 'e' }), //
        ABCDE(new char[] { 'A', 'B', 'C', 'D', 'E' }), //
        wsadz(new char[] { 'w', 's', 'a', 'd', 'z' }), //
        WSADZ(new char[] { 'W', 'S', 'A', 'D', 'Z' }), //
        _12345(new char[] { '1', '2', '3', '4', '5' }), //
        _上下左右空白(new int[] { 38, 40, 37, 39, 32 }),//
        ;

        Object arry;

        KeyMapping(Object arry) {
            this.arry = arry;
        }

        public String toString() {
            return this.name().replaceFirst("_", "");
        }

        private void applyEvent(KeyEvent event, EnglishSearchUI_MemoryBank_DialogOtherUI _this) {
            int triggerIndex = getMatchIndex(this, event);
            triggerRadio(triggerIndex, _this);
        }

        private void triggerRadio(int index, EnglishSearchUI_MemoryBank_DialogOtherUI _this) {
            switch (index) {
            case 0:
                // JCommonUtil.triggerButtonActionPerformed(_this.q1Radio);
                break;
            }
        }

        private int getMatchIndex(KeyMapping e, KeyEvent event) {
            Object val = null;
            if (e.arry.getClass() == char[].class) {
                val = event.getKeyChar();
                System.out.println("use keyChar : " + val);
            } else {
                val = event.getKeyCode();
                System.out.println("use keyCode : " + val);
            }
            for (int ii = 0; ii < Array.getLength(arry); ii++) {
                System.out.println(Array.get(arry, ii) + " - " + val);
                if (Array.get(arry, ii).equals(val)) {
                    return ii;
                }
            }
            throw new RuntimeException("找步道對應 index : " + e);
        }
    }

    private enum AlignType {
        左(SwingConstants.LEADING, true), //
        右(SwingConstants.RIGHT, false),//
        ;

        final int type;
        final boolean visible;

        AlignType(int type, boolean visible) {
            this.type = type;
            this.visible = visible;
        }

        private void apply(EnglishSearchUI_MemoryBank_DialogOtherUI _this) {
            try {
                JComponent[] ary = new JComponent[] { _this.questionLabel };
                for (JComponent v : ary) {
                    MethodUtils.invokeMethod(v, "setHorizontalAlignment", type);
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }
    }

    public String getQuestionDescText() {
        return StringUtils.defaultString(questionDescArea.getText());
    }

    private void fixContentLength(JComponent component, String content, int fontSize, int boundryWidth) {
        JTextWidthFitter.getInstance().setFitTextWidth(component, content, fontSize, boundryWidth);
    }
}
