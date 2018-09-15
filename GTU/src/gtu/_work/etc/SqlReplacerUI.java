package gtu._work.etc;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SqlReplacerUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JButton execute;
    private JTextField replaceToText;
    private JTextField replaceFromText;
    private JTextArea jTextArea1;
    private JPanel jPanel2;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SqlReplacerUI inst = new SqlReplacerUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }
    
    public SqlReplacerUI() {
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
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(387, 246));
                        {
                            jTextArea1 = new JTextArea();
                            jScrollPane1.setViewportView(jTextArea1);
                            jTextArea1.setText("");
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    FlowLayout jPanel2Layout = new FlowLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                    {
                        replaceFromText = new JTextField();
                        jPanel2.add(replaceFromText);
                        replaceFromText.setPreferredSize(new java.awt.Dimension(266, 22));
                    }
                    {
                        replaceToText = new JTextField();
                        jPanel2.add(replaceToText);
                        replaceToText.setPreferredSize(new java.awt.Dimension(266, 22));
                    }
                    {
                        execute = new JButton();
                        jPanel2.add(execute);
                        execute.setText("execute");
                        execute.setPreferredSize(new java.awt.Dimension(149, 42));
                        execute.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    String text = jTextArea1.getText();
                                    if (StringUtils.isBlank(text)) {
                                        JCommonUtil._jOptionPane_showMessageDialog_error("area empty!");
                                        return;
                                    }

                                    String fromTxt = replaceFromText.getText();
                                    String toTxt = StringUtils.defaultString(replaceToText.getText());
                                    if (StringUtils.isBlank(fromTxt)) {
                                        JCommonUtil._jOptionPane_showMessageDialog_error("fromTxt empty!");
                                        return;
                                    }

                                    StringBuffer sb = new StringBuffer();
                                    Pattern ptn = Pattern.compile("(.){0,1}" + fromTxt + "(.){0,1}", Pattern.CASE_INSENSITIVE);
                                    Matcher mth = null;
                                    
                                    String[] scopeStrs = {","," ","(",")","[","]"};
                                    BufferedReader reader = new BufferedReader(new StringReader(text));

                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        mth = ptn.matcher(line);
                                        while (mth.find()) {
                                            String scope1 = mth.group(1);
                                            String scope2 = mth.group(2);
                                            boolean ok1 = scope1.length() == 0 || StringUtils.indexOfAny(scope1, scopeStrs) != -1;
                                            boolean ok2 = scope2.length() == 0 || StringUtils.indexOfAny(scope2, scopeStrs) != -1;
                                            if (ok1 && ok2) {
                                                mth.appendReplacement(sb, scope1 + toTxt + scope2);
                                            }
                                        }
                                        mth.appendTail(sb);
                                        sb.append("\n");
                                    }
                                    
                                    reader.close();
                                    jTextArea1.setText(sb.toString());
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                            
                        });
                    }
                }
            }
            pack();
            setSize(400, 300);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

}
