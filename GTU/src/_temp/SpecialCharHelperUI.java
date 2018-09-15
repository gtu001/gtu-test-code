package _temp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

public class SpecialCharHelperUI extends JFrame {

    private JPanel contentPane;
    private JTextField charText;
    private JTextField intText;
    private JTextField hexText;
    private JTextField unicodeText;

    private ButtonGroup groupUtil;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SpecialCharHelperUI frame = new SpecialCharHelperUI();
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
    public SpecialCharHelperUI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 588, 574);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        final JRadioButton radio1 = new JRadioButton("");
        panel.add(radio1, "2, 2");

        JLabel lblChar = new JLabel("char");
        panel.add(lblChar, "4, 2, right, default");

        charText = new JTextField();
        panel.add(charText, "6, 2, fill, default");
        charText.setColumns(10);

        final JRadioButton radio2 = new JRadioButton("");
        panel.add(radio2, "2, 4");

        JLabel lblInt = new JLabel("int");
        panel.add(lblInt, "4, 4, right, default");

        intText = new JTextField();
        panel.add(intText, "6, 4, fill, default");
        intText.setColumns(10);

        final JRadioButton radio3 = new JRadioButton("");
        panel.add(radio3, "2, 6");

        JLabel label = new JLabel("16進位");
        panel.add(label, "4, 6, right, default");

        hexText = new JTextField();
        panel.add(hexText, "6, 6, fill, default");
        hexText.setColumns(10);

        final JRadioButton radio4 = new JRadioButton("");
        panel.add(radio4, "2, 8");

        JLabel lblUnicode = new JLabel("unicode");
        panel.add(lblUnicode, "4, 8, right, default");

        unicodeText = new JTextField();
        unicodeText.setColumns(10);
        panel.add(unicodeText, "6, 8, fill, default");


        groupUtil = JButtonGroupUtil.createRadioButtonGroup(radio1, radio2, radio3, radio4);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, "6, 38, fill, fill");

        JButton button = new JButton("執行");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AbstractButton btnChoice = JButtonGroupUtil.getSelectedButton(groupUtil);
                        if (radio1 == btnChoice) {
                            String text = StringUtils.trimToEmpty(charText.getText());
                            if (text.length() > 0) {
                                text = StringUtils.substring(text, 0, 1);
                                charText.setText(text);

                                // other process
                                int val = (int) text.charAt(0);
                                intText.setText(String.valueOf(val));
                                hexText.setText("0x" + Integer.toHexString(val));
                                unicodeText.setText("\\u" + Integer.toHexString(val));
                            }

                        } else if (radio2 == btnChoice) {
                            String text = StringUtils.trimToEmpty(intText.getText());
                            if (text.length() > 0) {
                                int val = Integer.parseInt(text);

                                // other process
                                charText.setText("" + (char) val);
                                hexText.setText("0x" + Integer.toHexString(val));
                                unicodeText.setText("\\u" + Integer.toHexString(val));
                            }
                        } else if (radio3 == btnChoice) {
                            String text = StringUtils.trimToEmpty(hexText.getText());
                            if (text.length() > 0) {
                                int val = Integer.parseInt(text, 16);

                                // other process
                                charText.setText("" + (char) val);
                                intText.setText(String.valueOf(val));
                                unicodeText.setText("\\u" + Integer.toHexString(val));
                            }
                        } else if (radio4 == btnChoice) {
                            String text = StringUtils.trimToEmpty(unicodeText.getText());
                            if (text.length() > 0) {
                                text = text.replaceFirst("\\u", text);
                                int val = Integer.parseInt(text, 16);

                                // other process
                                charText.setText("" + (char) val);
                                intText.setText(String.valueOf(val));
                                hexText.setText("0x" + Integer.toHexString(val));
                            }
                        }
                    }
                });
            }
        });
        panel_1.add(button);
    }
}
