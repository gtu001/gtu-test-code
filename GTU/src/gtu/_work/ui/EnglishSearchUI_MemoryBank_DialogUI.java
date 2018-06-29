package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;

public class EnglishSearchUI_MemoryBank_DialogUI extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel englishWordLabel;
    private JRadioButton q1Radio;
    private JRadioButton q2Radio;
    private JRadioButton q3Radio;
    private JRadioButton q4Radio;
    private JRadioButton q5Radio;
    private ButtonGroup btnGroup;
    private int choiceIndex = -1;
    private String[] meaningLst;
    private String choiceAnswer;
    private ActionListener deleteConfigAction;
    private ActionListener choiceRadioAction;
    private ActionListener customDescAction;
    private ActionListener skipBtnAction;
    private ActionListener skipAllBtnAction;
    private ActionListener onDismissAction;
    private ActionListener onCreateAction;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        String[] arry = new String[] { "a1", "a2", "a3", "a4", "a5" };
        EnglishSearchUI_MemoryBank_DialogUI dialog = new EnglishSearchUI_MemoryBank_DialogUI();
        dialog.initial();
        dialog.createDialog("title", "AAAA", arry, null, null, null, null, null, null, null);
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

    public void createDialog(String title, String englishWord, String[] meaningLst, //
            ActionListener deleteConfigAction, ActionListener choiceRadioAction, //
            ActionListener customDescAction, ActionListener skipBtnAction, //
            ActionListener skipAllBtnAction, ActionListener onCreateAction, //
            ActionListener onDismissAction //
    ) {
        try {
            setTitle(title);
            this.meaningLst = meaningLst;
            englishWordLabel.setText(englishWord);
            q1Radio.setText(__getArryByIndex(meaningLst, 0));
            q2Radio.setText(__getArryByIndex(meaningLst, 1));
            q3Radio.setText(__getArryByIndex(meaningLst, 2));
            q4Radio.setText(__getArryByIndex(meaningLst, 3));
            q5Radio.setText(__getArryByIndex(meaningLst, 4));
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.deleteConfigAction = deleteConfigAction;
            this.choiceRadioAction = choiceRadioAction;
            this.customDescAction = customDescAction;
            this.skipBtnAction = skipBtnAction;
            this.skipAllBtnAction = skipAllBtnAction;
            this.onCreateAction = onCreateAction;
            this.onDismissAction = onDismissAction;
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private JButton deleteFromMemoryBankBtn;
    private JButton customDescBtn;
    private JButton skipAllBtn;
    private JLabel lblNewLabel_1;
    private JLabel lblb;
    private JLabel lblc;
    private JLabel lbld;
    private JLabel lblNewLabel_2;

    private void questionRadioChoiceAction(int index) {
        AbstractButton choiceBtn = JButtonGroupUtil.getSelectedButton(btnGroup);
        if (choiceBtn != null) {
            choiceIndex = Arrays.asList(q1Radio, q2Radio, q3Radio, q4Radio, q5Radio).indexOf(choiceBtn);
        } else {
            choiceIndex = index;
        }
        System.out.println("choiceIndex = " + choiceIndex);
        choiceAnswer = meaningLst[choiceIndex];
        System.out.println("choiceAnswer = " + choiceAnswer);
        if (choiceRadioAction != null) {
            choiceRadioAction.actionPerformed(new ActionEvent(this, choiceIndex, choiceAnswer));
        }
    }

    /**
     * Create the dialog.
     */
    public EnglishSearchUI_MemoryBank_DialogUI() {
    }

    public void initial() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));
        {
            JLabel lblNewLabel = new JLabel("");
            contentPanel.add(lblNewLabel, "2, 2");
        }
        {
            englishWordLabel = new JLabel("");
            englishWordLabel.setFont(new Font("Consolas", Font.PLAIN, 19));
            contentPanel.add(englishWordLabel, "4, 2");
        }
        {
            q1Radio = new JRadioButton("");
            q1Radio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    questionRadioChoiceAction(0);
                }
            });
            {
                lblNewLabel_1 = new JLabel("1A");
                contentPanel.add(lblNewLabel_1, "2, 4");
            }
            contentPanel.add(q1Radio, "4, 4");
        }
        {
            q2Radio = new JRadioButton("");
            q2Radio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    questionRadioChoiceAction(1);
                }
            });
            {
                lblb = new JLabel("2B");
                contentPanel.add(lblb, "2, 6");
            }
            contentPanel.add(q2Radio, "4, 6");
        }
        {
            q3Radio = new JRadioButton("");
            q3Radio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    questionRadioChoiceAction(2);
                }
            });
            {
                lblc = new JLabel("3C");
                contentPanel.add(lblc, "2, 8");
            }
            contentPanel.add(q3Radio, "4, 8");
        }
        {
            q4Radio = new JRadioButton("");
            q4Radio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    questionRadioChoiceAction(3);
                }
            });
            {
                lbld = new JLabel("4D");
                contentPanel.add(lbld, "2, 10");
            }
            contentPanel.add(q4Radio, "4, 10");
        }
        {
            q5Radio = new JRadioButton("");
            q5Radio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    questionRadioChoiceAction(4);
                }
            });
            {
                lblNewLabel_2 = new JLabel("5E");
                contentPanel.add(lblNewLabel_2, "2, 12");
            }
            contentPanel.add(q5Radio, "4, 12");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                deleteFromMemoryBankBtn = new JButton("從設定檔中刪除");
                deleteFromMemoryBankBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (deleteConfigAction != null) {
                            deleteConfigAction.actionPerformed(e);
                        }
                    }
                });
                {
                    customDescBtn = new JButton("修正解釋");
                    customDescBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (customDescAction != null) {
                                customDescAction.actionPerformed(e);
                            }
                        }
                    });
                    buttonPane.add(customDescBtn);
                }
                buttonPane.add(deleteFromMemoryBankBtn);
            }
            {
                JButton skipBtn = new JButton("跳過skip");
                skipBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (skipBtnAction != null) {
                            skipBtnAction.actionPerformed(e);
                        }
                    }
                });
                skipBtn.setActionCommand("OK");
                buttonPane.add(skipBtn);
                getRootPane().setDefaultButton(skipBtn);
            }
            {
                skipAllBtn = new JButton("全部掠過skip");
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
                    ActionEvent e2 = new ActionEvent(EnglishSearchUI_MemoryBank_DialogUI.this, -1, "winClose");
                    skipBtnAction.actionPerformed(e2);
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Dialog Key Event - " + e.getKeyChar());
                switch (e.getKeyChar()) {
                case '1':
                case 'a':
                case 'A':
                    JCommonUtil.triggerButtonActionPerformed(q1Radio);
                    break;
                case '2':
                case 'b':
                case 'B':
                    JCommonUtil.triggerButtonActionPerformed(q2Radio);
                    break;
                case '3':
                case 'c':
                case 'C':
                    JCommonUtil.triggerButtonActionPerformed(q3Radio);
                    break;
                case '4':
                case 'd':
                case 'D':
                    JCommonUtil.triggerButtonActionPerformed(q4Radio);
                    break;
                case '5':
                case 'e':
                case 'E':
                    JCommonUtil.triggerButtonActionPerformed(q5Radio);
                    break;
                }
            }
        });

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setFoucsToChildren(this, this);
        this.setModal(false);
        btnGroup = JButtonGroupUtil.createRadioButtonGroup(q1Radio, q2Radio, q3Radio, q4Radio, q5Radio);
    }

    public void setNewMeaning(String oldMeaning, String newMeaning) {
        JRadioButton[] radioArry = new JRadioButton[] { q1Radio, q2Radio, q3Radio, q4Radio, q5Radio };
        for (int ii = 0; ii < meaningLst.length; ii++) {
            if (StringUtils.equals(oldMeaning, meaningLst[ii])) {
                meaningLst[ii] = newMeaning;
                radioArry[ii].setText(newMeaning);
            }
        }
    }

    private String __getArryByIndex(String[] meaningLst, int index) {
        if (meaningLst != null && meaningLst.length > index) {
            return meaningLst[index];
        }
        return "_Oops!!_IndexOfOfBound__";
    }

    public int getChoiceIndex() {
        return choiceIndex;
    }

    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setDeleteConfigAction(ActionListener deleteConfigAction) {
        this.deleteConfigAction = deleteConfigAction;
    }

    public void setChoiceRadioAction(ActionListener choiceRadioAction) {
        this.choiceRadioAction = choiceRadioAction;
    }
}
