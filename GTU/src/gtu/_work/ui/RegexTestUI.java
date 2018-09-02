package gtu._work.ui;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

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
public class RegexTestUI extends javax.swing.JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanel1;
    private JTextField regexText;
    private JTextField regexText0;
    private JList scannerList;
    private JList groupList;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTextArea srcArea;
    private JPanel jPanel3;
    private JPanel jPanel2;
    private JTabbedPane jTabbedPane1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexTestUI inst = new RegexTestUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public RegexTestUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            DocumentListener docListener = JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                public void process(DocumentEvent event) {
                    jText1OrJArea1Change(event);
                }
            });

            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        jText1OrJArea1Change(null);
                    }
                });
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("src text", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            srcArea = new JTextArea();
                            jScrollPane1.setViewportView(srcArea);
                            srcArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                            JCommonUtil.setFont(srcArea);
                            srcArea.addKeyListener(new KeyAdapter() {
                                @Override
                                public void keyPressed(KeyEvent e) {
                                    jText1OrJArea1Change(null);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("group", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(398, 234));
                        {
                            ListModel groupListModel = new DefaultComboBoxModel();
                            groupList = new JList();
                            jScrollPane2.setViewportView(groupList);
                            groupList.setBorder(BorderFactory
                                    .createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                            groupList.setModel(groupListModel);
                            JCommonUtil.setFont(groupList);
                        }
                    }
                    {
                        regexText0 = new JTextField();
                        jPanel2.add(regexText0, BorderLayout.NORTH);
                        regexText0.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                        JCommonUtil.setFont(regexText0);
                        regexText0.getDocument().addDocumentListener(docListener);
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("scanner", null, jPanel3, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            ListModel scannerListModel = new DefaultComboBoxModel();
                            scannerList = new JList();
                            jScrollPane3.setViewportView(scannerList);
                            scannerList.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0,
                                    0)));
                            scannerList.setModel(scannerListModel);
                            JCommonUtil.setFont(scannerList);
                        }
                    }
                    {
                        regexText = new JTextField();
                        jPanel3.add(regexText, BorderLayout.NORTH);
                        regexText.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                        JCommonUtil.setFont(regexText);
                        regexText.getDocument().addDocumentListener(docListener);
                    }
                }
            }
            this.setTitle("\u6b63\u5247\u8868\u793a\u5f0f");
            this.setSize(419, 320);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jText1OrJArea1Change(DocumentEvent doc) {
        try {
            String complie1 = regexText.getText();
            String complie2 = regexText0.getText();
            String complie = complie1;
            if(StringUtils.isBlank(complie1)){
                complie = complie2;
            }

            String matcherStr = srcArea.getText();

            if (StringUtils.isBlank(complie)) {
                setTitle("請輸入Regex");
                return;
            }
            if (StringUtils.isBlank(matcherStr)) {
                setTitle("請輸入content");
                return;
            }

            Pattern pattern = Pattern.compile(complie);
            Matcher matcher = pattern.matcher(matcherStr);

            DefaultComboBoxModel model1 = new DefaultComboBoxModel();
            groupList.setModel(model1);
            while (matcher.find()) {
                model1.addElement("---------------------");
                for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                    model1.addElement(ii + " : [" + matcher.group(ii) + "]");
                }
            }

            DefaultComboBoxModel model2 = new DefaultComboBoxModel();
            scannerList.setModel(model2);
            Scanner scanner = new Scanner(matcherStr);
            scanner.useDelimiter(pattern);
            while (scanner.hasNext()) {
                model2.addElement("[" + scanner.next() + "]");
            }
            scanner.close();
            this.setTitle("正則表達示");
        } catch (Exception ex) {
            this.setTitle(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
