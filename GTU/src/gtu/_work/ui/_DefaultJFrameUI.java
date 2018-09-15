package gtu._work.ui;
import gtu._work.PersonIdFinder;
import gtu.log.Log;
import gtu.log.PrintStreamAdapter;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;


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
public class _DefaultJFrameUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField srcPathText;
    private JTextArea logArea;
    private JButton executeBtn;
    private JPanel jPanel2;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _DefaultJFrameUI inst = new _DefaultJFrameUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }
    
    public _DefaultJFrameUI() {
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
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        srcPathText = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcPathText, true);
                        jPanel1.add(srcPathText);
                        srcPathText.setPreferredSize(new java.awt.Dimension(291, 24));
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("\u57f7\u884c");
                        executeBtn.setPreferredSize(new java.awt.Dimension(66, 39));
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed(evt);
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(533, 334));
                        {
                            logArea = new JTextArea();
                            jScrollPane1.setViewportView(logArea);
                            logArea.setText("");
                        }
                    }
                }
            }
            pack();
            this.setSize(554, 401);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void executeBtnActionPerformed(ActionEvent evt) {
        try{
            new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                @Override
                public void run() {
                    PersonIdFinder test = new PersonIdFinder();
                    test.setOut(new PrintStream(new PrintStreamAdapter("big5") {
                        @Override
                        public void println(String message) {
                            Log.debug(message);
                            if(StringUtils.length(logArea.getText()) > 500){
                                logArea.setText("");
                            }
                            logArea.append(message+"\n");
                        }
                    }, true));
                    test.execute();
                }
            }, "xxxxxxxx").start();
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
