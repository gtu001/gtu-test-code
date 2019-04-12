package gtu._work.ui;

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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.tuple.Triple;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.regex.tag.TagMatcher;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTextAreaUtil;

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
public class RegexDirReplacer extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexDirReplacer inst = new RegexDirReplacer();
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    public RegexDirReplacer() {
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
                            ListModel srcListModel = new DefaultListModel();
                            srcList = new JList();
                            jScrollPane1.setViewportView(srcList);
                            srcList.setModel(srcListModel);
                            {
                                panel = new JPanel();
                                jScrollPane1.setRowHeaderView(panel);
                                panel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
                                {
                                    childrenDirChkbox = new JCheckBox("含子目錄");
                                    childrenDirChkbox.setSelected(true);
                                    panel.add(childrenDirChkbox, "1, 1");
                                }
                                {
                                    subFileNameText = new JTextField();
                                    panel.add(subFileNameText, "1, 2, fill, default");
                                    subFileNameText.setColumns(10);
                                    subFileNameText.setText("(txt|java)");
                                }
                                {
                                    replaceOldFileChkbox = new JCheckBox("備份舊檔");
                                    replaceOldFileChkbox.setSelected(true);
                                    panel.add(replaceOldFileChkbox, "1, 3");
                                }
                                {
                                    charsetText = new JTextField();
                                    panel.add(charsetText, "1, 5, fill, default");
                                    charsetText.setColumns(10);
                                    charsetText.setText("UTF8");
                                }
                            }
                            srcList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JListUtil.newInstance(srcList).defaultMouseClickOpenFile(evt);

                                    JPopupMenuUtil.newInstance(srcList).applyEvent(evt)//
                                            .addJMenuItem("load files from clipboard", new ActionListener() {
                                                public void actionPerformed(ActionEvent arg0) {
                                                    String content = ClipboardUtil.getInstance().getContents();
                                                    DefaultListModel model = (DefaultListModel) srcList.getModel();
                                                    StringTokenizer tok = new StringTokenizer(content, "\t\n\r\f", false);
                                                    for (; tok.hasMoreElements();) {
                                                        String val = ((String) tok.nextElement()).trim();
                                                        model.addElement(new File(val));
                                                    }
                                                }
                                            }).show();
                                }
                            });
                            srcList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(srcList).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                    {
                        addDirFiles = new JButton();
                        jPanel1.add(addDirFiles, BorderLayout.NORTH);
                        addDirFiles.setText("add dir files");
                        addDirFiles.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectFileAndDirectory().showOpenDialog().getApproveSelectedFile();
                                if (file == null) {
                                    return;
                                }

                                List<File> fileLst = new ArrayList<File>();

                                String subName = StringUtils.trimToEmpty(subFileNameText.getText());
                                if (StringUtils.isBlank(subName)) {
                                    subName = ".*";
                                }
                                String patternStr = ".*\\." + subName;

                                if (childrenDirChkbox.isSelected()) {
                                    FileUtil.searchFileMatchs(file, patternStr, fileLst);
                                } else {
                                    for (File f : file.listFiles()) {
                                        if (f.isFile() && f.getName().matches(patternStr)) {
                                            fileLst.add(f);
                                        }
                                    }
                                }

                                DefaultListModel model = new DefaultListModel();
                                for (File f : fileLst) {
                                    model.addElement(f);
                                }
                                srcList.setModel(model);
                            }
                        });
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
                        jPanel2.add(jPanel3, BorderLayout.CENTER);
                        // jPanel3.add(repFromText, "2, 2, fill, top");
                        // jPanel3.add(repToText, "2, 4, fill, center");
                        jPanel3.setLayout(
                                new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                                                FormFactory.DEFAULT_ROWSPEC, }));
                        {
                            lblNewLabel = new JLabel("replace from");
                            jPanel3.add(lblNewLabel, "2, 2");
                        }
                        {
                            repFromText = new JTextArea();
                            repFromText.setRows(3);
                            JTextAreaUtil.applyCommonSetting(repFromText);
                            jPanel3.add(JCommonUtil.createScrollComponent(repFromText), "4, 2, fill, fill");
                        }
                        {
                            JPanel p1 = new JPanel();
                            startTagText = new JTextField();
                            startTagPtnText = new JTextField();
                            startTagText.setColumns(20);
                            startTagPtnText.setColumns(20);
                            {
                                lblStartTag = new JLabel("start tag");
                                p1.add(lblStartTag);
                            }
                            p1.add(startTagText);
                            {
                                lblPattern = new JLabel("pattern");
                                p1.add(lblPattern);
                            }
                            p1.add(startTagPtnText);
                            jPanel3.add(p1, "4, 4, fill, default");
                        }
                        {
                            JPanel p1 = new JPanel();
                            endTagText = new JTextField();
                            endTagPtnText = new JTextField();
                            endTagText.setColumns(20);
                            endTagPtnText.setColumns(20);
                            {
                                lblEndTag = new JLabel("end tag");
                                p1.add(lblEndTag);
                            }
                            p1.add(endTagText);
                            {
                                lblPattern_1 = new JLabel("pattern");
                                p1.add(lblPattern_1);
                            }
                            p1.add(endTagPtnText);
                            jPanel3.add(p1, "4, 6, fill, default");
                        }
                        {
                            lblNewLabel_3 = new JLabel("replace to");
                            jPanel3.add(lblNewLabel_3, "2, 8");
                        }
                        {
                            repToText = new JTextArea();
                            repToText.setRows(3);
                            JTextAreaUtil.applyCommonSetting(repToText);
                            jPanel3.add(JCommonUtil.createScrollComponent(repToText), "4, 8, fill, fill");
                        }
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
                        {

                            ListModel newRepListModel = new DefaultListModel();
                            newRepList = new JList();
                            jScrollPane2.setViewportView(newRepList);
                            newRepList.setModel(newRepListModel);
                            newRepList.addMouseListener(new MouseAdapter() {
                                static final String tortoiseMergeExe = "TortoiseMerge.exe";
                                static final String commandFormat = "cmd /c call \"%s\" /base:\"%s\" /theirs:\"%s\"";

                                public void mouseClicked(MouseEvent evt) {
                                    if (!JListUtil.newInstance(newRepList).isCorrectMouseClick(evt)) {
                                        return;
                                    }

                                    OldNewFile oldNewFile = (OldNewFile) JListUtil.getLeadSelectionObject(newRepList);

                                    String base = oldNewFile.newFile.getAbsolutePath();
                                    String theirs = oldNewFile.oldFile.getAbsolutePath();

                                    String command = String.format(commandFormat, tortoiseMergeExe, base, theirs);

                                    System.out.println(command);

                                    try {
                                        Runtime.getRuntime().exec(command);
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                            newRepList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    Object[] objects = (Object[]) newRepList.getSelectedValues();
                                    if (objects == null || objects.length == 0) {
                                        return;
                                    }
                                    DefaultListModel model = (DefaultListModel) newRepList.getModel();
                                    int lastIndex = model.getSize() - 1;
                                    Object swap = null;

                                    StringBuilder dsb = new StringBuilder();
                                    for (Object current : objects) {
                                        int index = model.indexOf(current);
                                        switch (evt.getKeyCode()) {
                                        case 38:// up
                                            if (index != 0) {
                                                swap = model.getElementAt(index - 1);
                                                model.setElementAt(swap, index);
                                                model.setElementAt(current, index - 1);
                                            }
                                            break;
                                        case 40:// down
                                            if (index != lastIndex) {
                                                swap = model.getElementAt(index + 1);
                                                model.setElementAt(swap, index);
                                                model.setElementAt(current, index + 1);
                                            }
                                            break;
                                        case 127:// del
                                            OldNewFile current_ = (OldNewFile) current;
                                            dsb.append(current_.newFile.getName() + "\t" + (current_.newFile.delete() ? "T" : "F") + "\n");
                                            current_.newFile.delete();
                                            model.removeElement(current);
                                        }
                                    }

                                    if (dsb.length() > 0) {
                                        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("del result!\n" + dsb, "DELETE");
                                    }
                                }
                            });

                        }
                    }
                    {
                        replaceOrignFile = new JButton();
                        jPanel4.add(replaceOrignFile, BorderLayout.SOUTH);
                        replaceOrignFile.setText("replace orign file");
                        replaceOrignFile.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                DefaultListModel model = (DefaultListModel) newRepList.getModel();
                                StringBuilder sb = new StringBuilder();
                                for (int ii = 0; ii < model.size(); ii++) {
                                    OldNewFile file = (OldNewFile) model.getElementAt(ii);
                                    boolean delSuccess = false;
                                    boolean renameSuccess = false;
                                    if (delSuccess = file.oldFile.delete()) {
                                        renameSuccess = file.newFile.renameTo(file.oldFile);
                                    }
                                    sb.append(file.oldFile.getName() + " del:" + (delSuccess ? "T" : "F") + " rename:" + (renameSuccess ? "T" : "F") + "\n");
                                }
                                JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(sb, getTitle());
                            }
                        });
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
                            reloadTemplateList();
                            jScrollPane3.setViewportView(templateList);
                            templateList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    if (templateList.getLeadSelectionIndex() == -1) {
                                        return;
                                    }
                                    Entry<Object, Object> entry = (Entry<Object, Object>) JListUtil.getLeadSelectionObject(templateList);
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
            this.setSize(653, 497);

            JCommonUtil.setFontAll(this.getRootPane());

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

    public String getCharset() {
        try {
            String tmpCharset = charsetText.getText();
            if (StringUtils.isBlank(tmpCharset)) {
                tmpCharset = "UTF8";
            }
            String charset = Charset.forName(tmpCharset).displayName();
            return charset;
        } catch (Exception ex) {
            throw new RuntimeException("getCharset ERR : " + ex.getMessage(), ex);
        }
    }

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea repFromText;
    private JButton addDirFiles;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton scheduleExecute;
    private JButton addToTemplate;
    private JScrollPane jScrollPane3;
    private JList templateList;
    private JPanel jPanel5;
    private JButton replaceOrignFile;
    private JList newRepList;
    private JList srcList;
    private JPanel jPanel4;
    private JTextArea repToText;
    private JPanel jPanel3;
    private JButton exeucte;
    private JPanel jPanel2;

    static final String MESSAGE = "SUCCESS\n%s\n\nERROR\n%s\n";

    static File propFile = new File(PropertiesUtil.getJarCurrentPath(RegexDirReplacer.class), "RegexReplacer.properties");
    static Properties prop = new Properties();
    private JPanel panel;
    private JComboBox comboBox;
    private JCheckBox childrenDirChkbox;
    private JTextField subFileNameText;
    private JCheckBox replaceOldFileChkbox;
    private JTextField charsetText;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_3;
    private JTextField startTagText;
    private JTextField endTagText;
    private JTextField startTagPtnText;
    private JTextField endTagPtnText;
    private JLabel lblStartTag;
    private JLabel lblEndTag;
    private JLabel lblPattern;
    private JLabel lblPattern_1;

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

    private void scheduleExecuteActionPerformed(ActionEvent evt) {
        try {
            DefaultListModel model = (DefaultListModel) srcList.getModel();
            DefaultListModel rmodel = new DefaultListModel();
            DefaultListModel pmodel = (DefaultListModel) templateList.getModel();

            StringBuilder errMsg = new StringBuilder();
            StringBuilder successMsg = new StringBuilder();

            String replaceText = null;
            String bakupReplaceText = null;
            int waitSecond = 3;

            File newFile = null;
            File oldFile = null;
            OldNewFile oldNewFile = null;
            for (int ii = 0; ii < model.getSize(); ii++) {
                oldFile = (File) model.getElementAt(ii);
                replaceText = FileUtil.loadFromFile(oldFile, getCharset());
                bakupReplaceText = replaceText.toString();

                for (int jj = 0; jj < pmodel.getSize(); jj++) {
                    Entry<Object, Object> entry = (Entry<Object, Object>) pmodel.getElementAt(jj);

                    System.out.println("start file : " + oldFile);
                    AtomicReference<Throwable> result = new AtomicReference<Throwable>();
                    replaceText = replacer((String) entry.getKey(), (String) entry.getValue(), replaceText, waitSecond, result);
                    if (result.get() != null) {
                        errMsg.append(oldFile.getName() + ":" + result.get() + "\n");
                    }
                    System.out.println("end   file : " + oldFile);
                }

                if (!bakupReplaceText.equals(replaceText)) {
                    newFile = new File(oldFile.getParent(), oldFile.getName() + ".rep_done");
                    FileUtil.saveToFile(newFile, replaceText, getCharset());
                    successMsg.append(newFile.getName() + "\n");
                    oldNewFile = new OldNewFile();
                    oldNewFile.oldFile = oldFile;
                    oldNewFile.newFile = newFile;
                    rmodel.addElement(oldNewFile);
                }
            }

            newRepList.setModel(rmodel);
            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(String.format(MESSAGE, successMsg, errMsg), getTitle());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void exeucteActionPerformed(ActionEvent evt) {
        try {
            String fromPattern = null;
            String toFormat = repToText.getText();
            Validate.notEmpty((fromPattern = repFromText.getText()), "replace regex can't empty");

            DefaultListModel model = (DefaultListModel) srcList.getModel();
            DefaultListModel rmodel = new DefaultListModel();

            StringBuilder errMsg = new StringBuilder();
            StringBuilder successMsg = new StringBuilder();

            String replaceText = null;
            String bakupReplaceText = null;
            int waitSecond = 3;

            File newFile = null;
            File oldFile = null;
            OldNewFile oldNewFile = null;

            for (int ii = 0; ii < model.getSize(); ii++) {
                oldFile = (File) model.getElementAt(ii);
                replaceText = FileUtil.loadFromFile(oldFile, getCharset());
                bakupReplaceText = replaceText.toString();

                System.out.println("start file : " + oldFile);
                AtomicReference<Throwable> result = new AtomicReference<Throwable>();
                replaceText = replacer(fromPattern, toFormat, replaceText, waitSecond, result);
                if (result.get() != null) {
                    errMsg.append(oldFile.getName() + ":" + result.get() + "\n");
                } 
                System.out.println("end   file : " + oldFile);

                if (!bakupReplaceText.equals(replaceText)) {

                    if (replaceOldFileChkbox.isSelected()) {
                        oldFile.renameTo(new File(oldFile.getParentFile(), oldFile.getName() + ".bak"));
                    }

                    newFile = oldFile;

                    FileUtil.saveToFile(newFile, replaceText, getCharset());
                    successMsg.append(newFile.getName() + "\n");
                    oldNewFile = new OldNewFile();
                    oldNewFile.oldFile = oldFile;
                    oldNewFile.newFile = newFile;
                    rmodel.addElement(oldNewFile);

                    successMsg.append(oldFile.getName() + "\n");
                }
            }

            newRepList.setModel(rmodel);
            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(String.format(MESSAGE, successMsg, errMsg), getTitle());
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * @param fromPattern
     *            要替換的來源pattern
     * @param toFormat
     *            要替換的目的pattern
     * @param replaceText
     *            要替換的本文
     */
    String replacer(String fromPattern, String toFormat, String replaceText, int waitSecond, AtomicReference<Throwable> result) {
        String errorRtn = replaceText.toString();
        final BlockingQueue<Triple<String, Throwable, String>> queue = new ArrayBlockingQueue<Triple<String, Throwable, String>>(1);
        new Thread(new Runnable() {

            private String orignProcess(String replaceText) {
                StringBuilder sb = new StringBuilder();
                Pattern pattern = Pattern.compile(fromPattern, Pattern.DOTALL | Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(replaceText);

                int startPos = 0;

                String tempStr = null;
                for (; matcher.find();) {
                    tempStr = toFormat.toString();
                    sb.append(replaceText.substring(startPos, matcher.start()));
                    for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                        tempStr = tempStr.replaceAll("#" + ii + "#", Matcher.quoteReplacement(matcher.group(ii)));
                        System.out.println("group[" + ii + "] -- " + matcher.group(ii) + "\t--> " + tempStr);
                    }
                    sb.append(tempStr);
                    startPos = matcher.end();
                }
                sb.append(replaceText.substring(startPos));
                return sb.toString();
            }

            private String tagProcess(String replaceText) {
                String startTag = StringUtils.trimToEmpty(startTagText.getText());
                String endTag = StringUtils.trimToEmpty(endTagText.getText());
                String startPtnStr = StringUtils.trimToEmpty(startTagPtnText.getText());
                String endPtnStr = StringUtils.trimToEmpty(endTagPtnText.getText());
                String content = replaceText.toString();

                TagMatcher tag = new TagMatcher(startTag, endTag, startPtnStr, endPtnStr, content);

                StringBuffer sb11 = new StringBuffer();
                while (tag.find()) {
                    String resultText = orignProcess(tag.getContent());
                    tag.appendReplacement(sb11, resultText);
                }
                tag.appendTail(sb11);
                return sb11.toString();
            }

            @Override
            public void run() {
                try {
                    // 特別處理
                    if (StringUtils.isNotBlank(startTagText.getText()) && StringUtils.isNotBlank(endTagText.getText())) {
                        String resultText = tagProcess(replaceText.toString());
                        queue.offer(Triple.of("ok", null, resultText));
                    } else {
                        // 一般處理
                        String resultText = orignProcess(replaceText.toString());
                        queue.offer(Triple.of("ok", null, resultText));
                    }
                } catch (Throwable ex) {
                    queue.offer(Triple.of("fail", ex, null));
                    ex.printStackTrace();
                } finally {
                }
            }
        }).start();
        try {
            Triple<String, Throwable, String> result2 = queue.poll(waitSecond * 1000L, TimeUnit.MILLISECONDS);
            if ("fail".equals(result2.getLeft())) {
                throw result2.getMiddle();
            }
            return result2.getRight();
        } catch (InterruptedException ex) {
            // String message = "時間超出範圍 " + waitSecond + "秒!!";
            result.set(ex);
            return errorRtn;
        } catch (Throwable ex) {
            result.set(ex);
            return errorRtn;
        }
    }

    static class OldNewFile {
        File oldFile;
        File newFile;

        @Override
        public String toString() {
            return newFile.getAbsolutePath();
        }
    }
}
