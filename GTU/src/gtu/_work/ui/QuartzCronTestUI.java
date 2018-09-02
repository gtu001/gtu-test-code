package gtu._work.ui;

import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.CronExpression;

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
public class QuartzCronTestUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField limitText;
    private JList cronList;
    private JButton executeBtn;
    private JTextField cronText;
    private JPanel jPanel2;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                QuartzCronTestUI inst = new QuartzCronTestUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public QuartzCronTestUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("CronExpression", null, jPanel1, null);
                    {
                        jPanel2 = new JPanel();
                        jPanel1.add(jPanel2, BorderLayout.NORTH);
                        jPanel2.setPreferredSize(new java.awt.Dimension(439, 34));
                        {
                            cronText = new JTextField();
                            jPanel2.add(cronText);
                            cronText.setText("");
                            cronText.setPreferredSize(new java.awt.Dimension(229, 27));
                        }
                        {
                            executeBtn = new JButton();
                            jPanel2.add(executeBtn);
                            executeBtn.setText("execute");
                            executeBtn.setPreferredSize(new java.awt.Dimension(85, 28));
                            executeBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    //XXX
                                    try {
                                        CronExpression cexp = new CronExpression(cronText.getText());
                                        Date current = new Date();
                                        setTitle(DateFormatUtils.format(current, "yyyy/MM/dd HH:mm:ss"));

                                        DefaultListModel cronListModel = new DefaultListModel();
                                        for (int ii = 0, total = Integer.parseInt(limitText.getText()); ii < total; ii++) {
                                            current = cexp.getNextValidTimeAfter(current);
                                            if (current == null) {
                                                break;
                                            }
                                            cronListModel.addElement(ii + " : " + DateFormatUtils.format(current, "yyyy/MM/dd HH:mm:ss"));
                                        }
                                        cronList.setModel(cronListModel);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        JCommonUtil.handleException(e);
                                        return;
                                    }
                                }
                            });
                        }
                        {
                            limitText = new JTextField();
                            jPanel2.add(limitText);
                            limitText.setText("2000");
                            limitText.setPreferredSize(new java.awt.Dimension(61, 24));
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(439, 253));
                        {
                            DefaultListModel cronListModel = new DefaultListModel();
                            cronList = new JList();
                            jScrollPane1.setViewportView(cronList);
                            cronList.setModel(cronListModel);
                        }
                    }
                }
            }
            pack();
            this.setSize(460, 354);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
}
