package gtu._work.ui;

import gtu.runtime.ClipboardUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
public class TextScanUI extends javax.swing.JFrame {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TextScanUI inst = new TextScanUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public TextScanUI() {
        super();
        initGUI();
    }

    private static String TITLE = "\u6587\u4ef6\u700f\u89bd\u5668";

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    thisComponentResized(evt);
                }
            });
            this.addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent evt) {
                    System.out.println("this.windowStateChanged, event=" + evt);
                    // TODO add your code for this.windowStateChanged
                }
            });
            this.addWindowListener(new WindowAdapter() {
                public void windowIconified(WindowEvent evt) {
                    System.out.println("this.windowIconified, event=" + evt);
                    // TODO add your code for this.windowIconified
                }
            });
            this.setTitle(TITLE);
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1, BorderLayout.NORTH);
                GroupLayout jPanel1Layout = new GroupLayout((JComponent) jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1.setPreferredSize(new java.awt.Dimension(521, 89));
                jPanel1.setBounds(0, 0, 521, 89);
                {
                    jButton1 = new JButton();
                    jButton1.setText("File");
                    jButton1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            jButton1ActionPerformed(evt);
                        }
                    });
                }
                {
                    jButton2 = new JButton();
                    jButton2.setText("Clipboard");
                    jButton2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            jButton2ActionPerformed(evt);
                        }
                    });
                }
                {
                    contentFilter = new JTextField();
                    contentFilter.getDocument().addDocumentListener(new DocumentListener() {
                        public void insertUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField2filterAgain();
                        }

                        public void removeUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField2filterAgain();
                        }

                        public void changedUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField2filterAgain();
                        }
                    });
                }
                {
                    fileFilter = new JTextField();
                    fileFilter.getDocument().addDocumentListener(new DocumentListener() {
                        public void insertUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField1DocumentListener(paramDocumentEvent);
                        }

                        public void removeUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField1DocumentListener(paramDocumentEvent);
                        }

                        public void changedUpdate(DocumentEvent paramDocumentEvent) {
                            jTextField1DocumentListener(paramDocumentEvent);
                        }
                    });
                }
                {
                    totalLabel = new JLabel();
                }
                {
                    matchLabel = new JLabel();
                }
                {
                    jLabel4 = new JLabel();
                    jLabel4.setText("Match : ");
                }
                {
                    jLabel2 = new JLabel();
                    jLabel2.setText("Total : ");
                }
                {
                    ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "All", "Match" });
                    contextFilter = new JComboBox();
                    contextFilter.setModel(jComboBox1Model);
                    contextFilter.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            jTextField2filterAgain();
                        }
                    });
                }
                {
                    ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "Regex", "find" });
                    fileChk = new JComboBox();
                    fileChk.setModel(jComboBox1Model);
                    fileChk.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            fileChkActionPerformed(evt);
                        }
                    });
                }
                {
                    jScrollPane2 = new JScrollPane();
                    {
                        ListModel groupListModel = new DefaultComboBoxModel();
                        groupList = new JList();
                        jScrollPane2.setViewportView(groupList);
                        groupList.setModel(groupListModel);
                        groupList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                groupListKeyPressed(evt);
                            }
                        });
                    }
                }
                jPanel1Layout
                        .setVerticalGroup(jPanel1Layout
                                .createSequentialGroup()
                                .addContainerGap(16, 16)
                                .addGroup(
                                        jPanel1Layout
                                                .createParallelGroup()
                                                .addGroup(
                                                        jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addGroup(
                                                                        jPanel1Layout
                                                                                .createParallelGroup(
                                                                                        GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jButton1,
                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(fileFilter,
                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel2,
                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                        GroupLayout.PREFERRED_SIZE, 24,
                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(fileChk,
                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(
                                                                        jPanel1Layout
                                                                                .createParallelGroup()
                                                                                .addGroup(
                                                                                        jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                        GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(
                                                                                                        jButton2,
                                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(
                                                                                                        contentFilter,
                                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(
                                                                                                        jLabel4,
                                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        24,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(
                                                                                                        contextFilter,
                                                                                                        GroupLayout.Alignment.BASELINE,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(matchLabel,
                                                                                        GroupLayout.Alignment.LEADING,
                                                                                        GroupLayout.PREFERRED_SIZE, 24,
                                                                                        GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(
                                                        jPanel1Layout
                                                                .createParallelGroup()
                                                                .addComponent(totalLabel,
                                                                        GroupLayout.Alignment.LEADING,
                                                                        GroupLayout.PREFERRED_SIZE, 24,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jScrollPane2,
                                                                        GroupLayout.Alignment.LEADING,
                                                                        GroupLayout.PREFERRED_SIZE, 61,
                                                                        GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(12, 12));
                jPanel1Layout.setHorizontalGroup(jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap(12, 12)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addComponent(contextFilter, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fileChk, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addComponent(jLabel4, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addComponent(matchLabel, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totalLabel, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addComponent(contentFilter, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fileFilter, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addComponent(jButton1, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)).addGap(19)
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, 16));
            }
            {
                jScrollPane1 = new JScrollPane();
                getContentPane().add(jScrollPane1, BorderLayout.CENTER);
                jScrollPane1.setPreferredSize(new java.awt.Dimension(581, 235));
                {
                    ListModel jList1Model = new DefaultComboBoxModel();
                    matchList = new JList();
                    jScrollPane1.setViewportView(matchList);

                    // matchList.setPreferredSize(new java.awt.Dimension(575,
                    // 232));// 千萬不能加這行
                    // 否則捲軸無法拉
                    // XXX
                    // TODO
                    // IMPORT
                    // !!!!

                    matchList.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent evt) {
                            jList1MouseClicked(evt);
                        }
                    });
                    matchList.setModel(jList1Model);
                }
            }
            this.setSize(697, 440);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 以下是邏輯

    private JPanel jPanel1;
    private JLabel jLabel4;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane1;
    private JList groupList;
    private JComboBox fileChk;
    private JComboBox contextFilter;
    private JList matchList;
    private JButton jButton1;// open
    private JButton jButton2;// input dialog
    private JTextField fileFilter;// file regex

    private StringBuilder content;
    private Set<String> contentSet;
    private JLabel matchLabel;// match count
    private JLabel jLabel2;
    private JLabel totalLabel;// match total
    private JTextField contentFilter;// list regex

    private void readFile(File file) {
        try {
            content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line + "\r\n");
            }
            reader.close();
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
    }

    enum OpenMode {
        LINE_MATCH() {
            Set<String> openMode(String patternStr, StringBuilder content) {
                Set<String> contentSet = new HashSet<String>();
                try {
                    BufferedReader reader = new BufferedReader(new StringReader(content.toString()));
                    Pattern pattern = Pattern.compile(patternStr);
                    Matcher matcher = null;
                    contentSet = new TreeSet<String>();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        matcher = pattern.matcher(line);
                        if (matcher.find() == true) {
                            contentSet.add(line.substring(matcher.start(), matcher.end()).trim());
                        }
                    }
                    reader.close();
                } catch (Exception ex) {
                    // 忽略
                }
                return contentSet;
            }
        },
        SCANNER() {
            Set<String> openMode(String patternStr, StringBuilder content) {
                Set<String> contentSet = new HashSet<String>();
                try {
                    Scanner scan = new Scanner(content.toString());
                    scan.useDelimiter(patternStr);
                    while (scan.hasNext()) {
                        contentSet.add(scan.next());
                    }
                    scan.close();
                } catch (Exception ex) {
                    // 忽略
                }
                return contentSet;
            }
        },
        ; //

        public void apply(String patternStr, TextScanUI text) {
            Set<String> set = this.openMode(patternStr, text.content);
            text.contentSet = set;
            DefaultListModel listModel = new DefaultListModel();
            for (String str : set) {
                listModel.addElement(str);
            }
            text.matchList.setModel(listModel);
        }

        abstract Set<String> openMode(String patternStr, StringBuilder content);
    }

    private void jTextField1DocumentListener(DocumentEvent event) {
        contextReload();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
        if (file == null) {
            return;
        }
        this.readFile(file);
        this.setTitle(TITLE + "(" + file.getName() + ")");
        contextReload();
    }

    private void jList1MouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            String value = (String) matchList.getModel().getElementAt(matchList.getLeadSelectionIndex());
            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(appendLineSep(value), "詳細內容");
            ClipboardUtil.getInstance().setContents(value);
        }
    }

    private void resetGroupList(int groupCount) {
        DefaultListModel model = new DefaultListModel();
        for (int ii = 0; ii <= groupCount; ii++) {
            model.addElement(ii);
        }
        groupList.setModel(model);
    }

    private void jTextField2filterAgain() {
        jTextField2filterAgain(true);
    }

    private void jTextField2filterAgain(boolean resetGroupListModel) {
        try {
            Pattern pat = Pattern.compile(contentFilter.getText());
            Matcher matcher = null;
            DefaultListModel listModel = new DefaultListModel();
            Set<String> set = new HashSet<String>();
            boolean setGroupListModel = false;
            for (String str : contentSet) {
                matcher = pat.matcher(replaceNewLineChar(str));
                if (contextFilter.getSelectedItem().equals("All")) {
                    if (matcher.find()) {
                        listModel.addElement(str);
                    }
                } else if (contextFilter.getSelectedItem().equals("Match")) {
                    List<String> mlist = new ArrayList<String>();
                    while (matcher.find()) {
                        for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                            if (!resetGroupListModel) {
                                DefaultListModel model = (DefaultListModel) groupList.getModel();
                                for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                                    Integer gnum = (Integer) enu.nextElement();
                                    if (gnum == ii) {
                                        mlist.add(matcher.group(ii));
                                    }
                                }
                            } else {
                                mlist.add(matcher.group(ii));
                            }
                        }
                        if (resetGroupListModel && !setGroupListModel) {
                            resetGroupList(matcher.groupCount());
                        }
                    }
                    set.add(mlist.toString());
                }
            }
            if (contextFilter.getSelectedItem().equals("Match")) {
                List<String> ssset = new ArrayList<String>(set);
                Collections.sort(ssset);
                for (String str : ssset) {
                    listModel.addElement(str);
                }
            }
            matchList.setModel(listModel);
            matchLabel.setText(String.valueOf(matchList.getModel().getSize()));
        } catch (Exception ex) {
        }
        sysinfo();
    }

    private void contextReload() {
        try {
            validateValues();
            if (fileChk.getSelectedItem().equals("Regex")) {
                System.out.println("LINE_MATCH");
                OpenMode.LINE_MATCH.apply(fileFilter.getText(), this);
            }
            if (fileChk.getSelectedItem().equals("find")) {
                System.out.println("SCANNER");
                OpenMode.SCANNER.apply(fileFilter.getText(), this);
            }
            totalLabel.setText(String.valueOf(matchList.getModel().getSize()));
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
        sysinfo();
    }

    private void validateValues() {
        try {
            Validate.notNull(content, "請開啟檔案!");
            Validate.notEmpty(content.toString(), "檔案無資料!");
            Validate.notEmpty(fileFilter.getText(), "必須輸入Regex!");
        } catch (Exception ex) {
            matchList.setModel(new DefaultComboBoxModel());
        }
    }

    private void sysinfo() {
        try {
            if (content != null) {
                System.out.println("content = " + content.length());
            }
            if (contentSet != null) {
                System.out.println("contentSet = " + contentSet.size());
            }
            System.out.println("modelSize = " + matchList.getModel().getSize());
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
    }

    private static void exceptionHandler(Exception ex) {
        // System.out.println("# exceptionHandler..");
        // ExceptionUtil.handler(ex);
        // JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.toString(),
        // "error");
    }

    private static String replaceNewLineChar(String value) {
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (!(c == '\n' || c == '\r')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String appendLineSep(String value) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        final int lineLimit = 100;
        boolean hasLineSp = false;
        for (char c : value.toCharArray()) {
            if (index <= lineLimit && c == '\n') {
                hasLineSp = false;
            }
            if (index > lineLimit && hasLineSp == false) {
                sb.append("\r\n");
                index = 0;
            }
            sb.append(c);
            index++;
        }
        return sb.toString();
    }

    private void thisComponentResized(ComponentEvent evt) {
        // JFrame frame = (JFrame) evt.getSource();
        // // Dimension screenSize = getToolkit().getScreenSize(); //抓螢幕尺寸
        // Dimension resize = new Dimension();
        // resize.height = frame.getBounds().height - 140;
        // resize.width = frame.getBounds().width - 40;
        // System.out.println(resize);
    }

    private void fileChkActionPerformed(ActionEvent evt) {
        contextReload();
    }

    private void groupListKeyPressed(KeyEvent evt) {
        DefaultListModel model = (DefaultListModel) groupList.getModel();
        int index = groupList.getLeadSelectionIndex();
        switch (evt.getKeyCode()) {
        case 127:// del
            model.removeElementAt(index);
            jTextField2filterAgain(false);
            break;
        }
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        try {
            content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(ClipboardUtil.getInstance().getContents()));
            String line = null;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                content.append(line + "\r\n");
                System.out.println("===>" + index);
                index++;
            }
            reader.close();
        } catch (Exception ex) {
            exceptionHandler(ex);
        }
        this.setTitle(TITLE + "(自訂文件)");
        contextReload();
    }
}
