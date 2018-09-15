package gtu.xml.xstream.iisi;
import gtu.string.StringCompressUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.Validate;

import tw.gov.moi.ae.jms.JmsMessageNew;


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
public class MQDecodeUI extends javax.swing.JFrame {
    private JTextArea beforeArea;
    private JButton cleanBtn;
    private JButton executeBtn;
    private JPanel jPanel1;
    private JTextArea afterArea;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MQDecodeUI inst = new MQDecodeUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }
    
    public MQDecodeUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            JCommonUtil.defaultLookAndFeel();
            GridLayout thisLayout = new GridLayout(3, 1);
            thisLayout.setColumns(1);
            thisLayout.setHgap(5);
            thisLayout.setVgap(5);
            thisLayout.setRows(3);
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                beforeArea = new JTextArea();
                getContentPane().add(beforeArea);
            }
            {
                afterArea = new JTextArea();
                getContentPane().add(afterArea);
            }
            {
                jPanel1 = new JPanel();
                FlowLayout jPanel1Layout = new FlowLayout();
                getContentPane().add(jPanel1);
                jPanel1.setPreferredSize(new java.awt.Dimension(546, 38));
                jPanel1.setLayout(jPanel1Layout);
                {
                    executeBtn = new JButton();
                    jPanel1.add(executeBtn);
                    executeBtn.setText("\u7522\u751f");
                    executeBtn.setPreferredSize(new java.awt.Dimension(78, 22));
                    executeBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            try{
                                Validate.notBlank(beforeArea.getText(), "請貼入messgaeContent");
                                String msg2 = beforeArea.getText();
                                
                                XmlParserImpl xmlParserImpl = new XmlParserImpl();
                                final String jmsMessageXML = StringCompressUtil.uncompress(msg2);
                                JmsMessageNew jmsMessageNew = (JmsMessageNew)xmlParserImpl.parseToObj(jmsMessageXML);
                                String returnMessage = jmsMessageNew.getMessageXML();
                                System.out.println(returnMessage);
                                afterArea.setText(returnMessage);
                                System.out.println("done...");
                            }catch(Exception ex){
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                }
                {
                    cleanBtn = new JButton();
                    jPanel1.add(cleanBtn);
                    cleanBtn.setText("\u6e05\u9664");
                    cleanBtn.setPreferredSize(new java.awt.Dimension(78,22));
                    cleanBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            try{
                                beforeArea.setText("");
                                afterArea.setText("");
                            }catch(Exception ex){
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                }
            }
            pack();
            this.setSize(554, 402);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }
}
