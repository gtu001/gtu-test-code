package gtu.string;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gtu.clipboard.ClipboardUtil;

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
public class DbJavaTransferUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea replaceArea;
    private JScrollPane jScrollPane1;
    private JButton javaToDbBtn;
    private JButton dbToJavaBtn;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DbJavaTransferUI inst = new DbJavaTransferUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public DbJavaTransferUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    jPanel1.setPreferredSize(new java.awt.Dimension(569, 288));
                    {
                        jPanel2 = new JPanel();
                        FlowLayout jPanel2Layout = new FlowLayout();
                        jPanel1.add(jPanel2, BorderLayout.NORTH);
                        jPanel2.setLayout(jPanel2Layout);
                        jPanel2.setPreferredSize(new java.awt.Dimension(569, 39));
                        {
                            dbToJavaBtn = new JButton();
                            jPanel2.add(dbToJavaBtn);
                            dbToJavaBtn.setText("db to java");
                            dbToJavaBtn.setPreferredSize(new java.awt.Dimension(114, 22));
                            dbToJavaBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    String content = ClipboardUtil.getInstance().getContents();
                                    Pattern pattern = Pattern.compile("[\\w-_.]+", Pattern.MULTILINE);
                                    Matcher matcher = pattern.matcher(content);
                                    StringBuffer sb = new StringBuffer();
                                    for (; matcher.find();) {
                                        String str = matcher.group();
                                        matcher.appendReplacement(sb, StringUtilForDb.dbFieldToJava(str));
                                    }
                                    matcher.appendTail(sb);
                                    replaceArea.setText(sb.toString());
                                }
                            });
                        }
                        {
                            javaToDbBtn = new JButton();
                            jPanel2.add(javaToDbBtn);
                            javaToDbBtn.setText("java to db");
                            javaToDbBtn.setPreferredSize(new java.awt.Dimension(114, 22));
                            javaToDbBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    String content = ClipboardUtil.getInstance().getContents();
                                    Pattern pattern = Pattern.compile("[\\w-_.]+", Pattern.MULTILINE);
                                    Matcher matcher = pattern.matcher(content);
                                    StringBuffer sb = new StringBuffer();
                                    for (; matcher.find();) {
                                        String str = matcher.group();
                                        matcher.appendReplacement(sb, StringUtilForDb.javaToDbField(str));
                                    }
                                    matcher.appendTail(sb);
                                    replaceArea.setText(sb.toString());
                                }
                            });
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(569, 288));
                        {
                            replaceArea = new JTextArea();
                            jScrollPane1.setViewportView(replaceArea);
                        }
                    }
                }
            }
            pack();
            this.setSize(582, 381);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

}
