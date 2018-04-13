package gtu._work.ui;

import gtu.clipboard.ClipboardUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.Validate;

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
public class StringArrayMakerUI extends javax.swing.JFrame {
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JList jList1;
    private JButton jButton1;
    private JScrollPane jScrollPane2;
    private JCheckBox jCheckBox2;
    private JCheckBox jCheckBox1;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JButton jButton2;
    private JTextArea jTextArea1;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StringArrayMakerUI inst = new StringArrayMakerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public StringArrayMakerUI() {
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
                        jScrollPane2 = new JScrollPane();
                        jPanel1.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(525, 267));
                        {
                            ListModel jList1Model = new DefaultListModel();
                            jList1 = new JList();
                            
                            jScrollPane2.setViewportView(jList1);
                            jList1.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(jList1).defaultJListKeyPressed(evt);
                                }
                            });
                            jList1.setModel(jList1Model);
                        }
                    }
                    {
                        jButton1 = new JButton();
                        jPanel1.add(jButton1, BorderLayout.NORTH);
                        jButton1.setText("\u8cbc\u4e0a");
                        jButton1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jPanel3 = new JPanel();
                        FlowLayout jPanel3Layout = new FlowLayout();
                        jPanel3Layout.setAlignment(FlowLayout.RIGHT);
                        jPanel1.add(jPanel3, BorderLayout.WEST);
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel3.setPreferredSize(new java.awt.Dimension(50, 291));
                        {
                            jCheckBox1 = new JCheckBox();
                            jPanel3.add(jCheckBox1);
                            jCheckBox1.setText("\\n");
                        }
                        {
                            jCheckBox2 = new JCheckBox();
                            jPanel3.add(jCheckBox2);
                            jCheckBox2.setText("\\t");
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                    {
                        jButton2 = new JButton();
                        jPanel2.add(jButton2, BorderLayout.NORTH);
                        jButton2.setText("\u7522\u751f");
                        jButton2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                jButton2ActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(525, 291));
                        {
                            jTextArea1 = new JTextArea();
                            jScrollPane1.setViewportView(jTextArea1);
                            jTextArea1.setText("");
                        }
                    }
                }
            }
            pack();
            this.setSize(546, 382);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }
    
    private void jButton1ActionPerformed(ActionEvent evt) {
        try{
            DefaultListModel model = JListUtil.createModel();
            String text = ClipboardUtil.getInstance().getContents();
            Validate.notEmpty(text, "剪貼簿沒有資料");
            StringBuilder pattern = new StringBuilder();
            if(jCheckBox1.isSelected()){
                pattern.append("\n");
            }
            if(jCheckBox2.isSelected()){
                pattern.append("\t");
            }
            StringTokenizer tok = new StringTokenizer(text, pattern.toString());
            while(tok.hasMoreElements()){
                String val = (String)tok.nextElement();
                model.addElement(val);
            }
            jList1.setModel(model);
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
    
    private void jButton2ActionPerformed(ActionEvent evt) {
        DefaultListModel model = (DefaultListModel)jList1.getModel();
        StringBuilder sb = new StringBuilder();
        for(int ii = 0 ; ii < model.getSize(); ii ++){
            String val = (String)model.getElementAt(ii);
            sb.append(String.format("\"%s\",", val));
        }
        if(sb.length() != 0){
            sb.deleteCharAt(sb.length() -1);
        }
        jTextArea1.setText(sb.toString());
    }
}
