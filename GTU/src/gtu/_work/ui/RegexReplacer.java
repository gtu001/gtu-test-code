package gtu._work.ui;

import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
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
public class RegexReplacer extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexReplacer inst = new RegexReplacer();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public RegexReplacer() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            {
            }
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("source", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            replaceArea = new JTextArea();
                            jScrollPane1.setViewportView(replaceArea);
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("param", null, jPanel2, null);
                    {
                        exeucte = new JButton();
                        jPanel2.add(exeucte, BorderLayout.SOUTH);
                        exeucte.setText("exeucte");
                        exeucte.setPreferredSize(new java.awt.Dimension(491, 125));
                        exeucte.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                exeucteActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jPanel3 = new JPanel();
                        GroupLayout jPanel3Layout = new GroupLayout((JComponent) jPanel3);
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel2.add(jPanel3, BorderLayout.CENTER);
                        {
                            repFromText = new JTextField();
                        }
                        {
                            repToText = new JTextField();
                        }
                        jPanel3Layout.setHorizontalGroup(jPanel3Layout
                                .createSequentialGroup()
                                .addContainerGap(25, 25)
                                .addGroup(
                                        jPanel3Layout
                                                .createParallelGroup()
                                                .addGroup(
                                                        jPanel3Layout.createSequentialGroup().addComponent(repFromText,
                                                                GroupLayout.PREFERRED_SIZE, 446,
                                                                GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                        jPanel3Layout.createSequentialGroup().addComponent(repToText,
                                                                GroupLayout.PREFERRED_SIZE, 446,
                                                                GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(20, Short.MAX_VALUE));
                        jPanel3Layout.setVerticalGroup(jPanel3Layout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addComponent(repFromText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repToText, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                    }
                    {
                        addToTemplate = new JButton();
                        jPanel2.add(addToTemplate, BorderLayout.NORTH);
                        addToTemplate.setText("add to template");
                        addToTemplate.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                prop.put(repFromText.getText(), repToText.getText());
                                reloadTemplateList();
                            }
                        });
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("result", null, jPanel4, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel4.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(491, 283));
                        {
                            resultArea = new JTextArea();
                            jScrollPane2.setViewportView(resultArea);
                        }
                    }
                }
                {
                    jPanel5 = new JPanel();
                    BorderLayout jPanel5Layout = new BorderLayout();
                    jPanel5.setLayout(jPanel5Layout);
                    jTabbedPane1.addTab("template", null, jPanel5, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel5.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            templateList = new JList();
                            jScrollPane3.setViewportView(templateList);
                            reloadTemplateList();
                        }
                        templateList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                if (templateList.getLeadSelectionIndex() == -1) {
                                    return;
                                }
                                Entry<Object, Object> entry = (Entry<Object, Object>) JListUtil
                                        .getLeadSelectionObject(templateList);
                                repFromText.setText((String) entry.getKey());
                                repToText.setText((String) entry.getValue());
                            }
                        });
                        templateList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                JListUtil.newInstance(templateList).defaultJListKeyPressed(evt);
                            }
                        });
                    }
                    {
                        scheduleExecute = new JButton();
                        jPanel5.add(scheduleExecute, BorderLayout.SOUTH);
                        scheduleExecute.setText("schedule execute");
                        scheduleExecute.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                scheduleExecuteActionPerformed(evt);
                            }
                        });
                    }
                }
            }
            this.setSize(512, 350);
            JCommonUtil.setFont(repToText, repFromText, replaceArea, templateList);

            JCommonUtil.frameCloseDo(this, new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    if (StringUtils.isNotBlank(repFromText.getText())) {
                        prop.put(repFromText.getText(), repToText.getText());
                    }
                    try {
                        prop.store(new FileOutputStream(propFile), "regexText");
                    } catch (Exception e) {
                        JCommonUtil.handleException("properties store error!", e);
                    }
                    setVisible(false);
                    dispose();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea replaceArea;
    private JScrollPane jScrollPane1;
    private JTextField repFromText;
    private JScrollPane jScrollPane2;
    private JButton scheduleExecute;
    private JButton addToTemplate;
    private JScrollPane jScrollPane3;
    private JList templateList;
    private JPanel jPanel5;
    private JTextArea resultArea;
    private JPanel jPanel4;
    private JTextField repToText;
    private JPanel jPanel3;
    private JButton exeucte;
    private JPanel jPanel2;

    static File propFile = new File(PropertiesUtil.getJarCurrentPath(RegexDirReplacer.class),
            "RegexReplacer.properties");
    static Properties prop = new Properties();
    static {
        try {
            if (!propFile.exists()) {
                propFile.createNewFile();
            }
            System.out.println(propFile + " == " + propFile.exists());
            prop.load(new FileInputStream(propFile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void reloadTemplateList() {
        DefaultListModel templateListModel = new DefaultListModel();
        for (Entry<Object, Object> entry : prop.entrySet()) {
            templateListModel.addElement(entry);
        }
        templateList.setModel(templateListModel);
    }

    private void exeucteActionPerformed(ActionEvent evt) {
        String replaceText = null;
        String fromPattern = null;
        String toFormat = repToText.getText();
        Validate.notEmpty((replaceText = replaceArea.getText()), "source can't empty");
        Validate.notEmpty((fromPattern = repFromText.getText()), "replace regex can't empty");
        resultArea.setText(replacer(fromPattern, toFormat, replaceText));
    }

    private void scheduleExecuteActionPerformed(ActionEvent evt) {
        String replaceText = null;
        Validate.notEmpty((replaceText = replaceArea.getText()), "source can't empty");
        DefaultListModel model = (DefaultListModel) templateList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            Entry<Object, Object> entry = (Entry<Object, Object>) model.getElementAt(ii);
            replaceText = replacer((String) entry.getKey(), (String) entry.getValue(), replaceText);
        }
        resultArea.setText(replaceText);
    }

    /**
     * @param fromPattern
     *            要替換的來源pattern
     * @param toFormat
     *            要替換的目的pattern
     * @param replaceText
     *            要替換的本文
     */
    String replacer(String fromPattern, String toFormat, String replaceText) {
        String errorRtn = replaceText.toString();
        try {
            Pattern pattern = Pattern.compile(fromPattern);
            Matcher matcher = pattern.matcher(replaceText);

            StringBuilder sb = new StringBuilder();
            int startPos = 0;

            String tempStr = null;
            for (; matcher.find();) {
                tempStr = toFormat.toString();
                sb.append(replaceText.substring(startPos, matcher.start()));
                for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                    System.out.println(ii + " -- " + matcher.group(ii));
                    tempStr = tempStr.replaceAll("#" + ii + "#", Matcher.quoteReplacement(matcher.group(ii)));
                }
                sb.append(tempStr);
                startPos = matcher.end();
            }

            sb.append(replaceText.substring(startPos));

            return sb.toString();
        } catch (Exception ex) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(), getTitle());
            return errorRtn;
        }
    }
}
