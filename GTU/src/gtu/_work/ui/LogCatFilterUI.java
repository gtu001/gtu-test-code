package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;

public class LogCatFilterUI extends JFrame {

    private JPanel contentPane;
    private JTextArea logTextArea;
    private JTextField classPathText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogCatFilterUI frame = new LogCatFilterUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LogCatFilterUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 639, 460);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("原始log", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        logTextArea = new JTextArea();
        // panel.add(logTextArea, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel, logTextArea);

        JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(0, 75));
        panel.add(panel_2, BorderLayout.NORTH);

        JCommonUtil.defaultToolTipDelay();
        classPathText = new JTextField();
        panel_2.add(classPathText);
        classPathText.setColumns(20);
        classPathText.setToolTipText("classPath");

        tagText = new JTextField();
        tagText.setToolTipText("Tag");
        tagText.setColumns(20);
        panel_2.add(tagText);

        logText = new JTextField();
        logText.setToolTipText("log");
        logText.setColumns(20);
        panel_2.add(logText);

        final JCheckBox chckbxV = new JCheckBox("V");
        panel_2.add(chckbxV);

        final JCheckBox chckbxD = new JCheckBox("D");
        panel_2.add(chckbxD);

        final JCheckBox chckbxI = new JCheckBox("I");
        panel_2.add(chckbxI);

        final JCheckBox chckbxW = new JCheckBox("W");
        panel_2.add(chckbxW);

        final JCheckBox chckbxE = new JCheckBox("E");
        panel_2.add(chckbxE);

        chckbxV.setSelected(true);
        chckbxD.setSelected(true);
        chckbxI.setSelected(true);
        chckbxW.setSelected(true);
        chckbxE.setSelected(true);

        JButton button = new JButton("執行");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Config n = new Config();
                n.chkV = chckbxV.isSelected();
                n.chkD = chckbxD.isSelected();
                n.chkI = chckbxI.isSelected();
                n.chkW = chckbxW.isSelected();
                n.chkE = chckbxE.isSelected();
                n.classPath = StringUtils.defaultString(classPathText.getText());
                n.tag = StringUtils.defaultString(tagText.getText());
                n.log = StringUtils.defaultString(logText.getText());

                String log = StringUtils.defaultString(logTextArea.getText());
                List<LogInfo> list = processLog(log, n);
                StringBuffer sb = new StringBuffer();
                for (LogInfo info : list) {
                    sb.append(info + "\r\n");
                }
                resultTextArea.setText(sb.toString());
                loadTableData(list);
            }
        });
        panel_2.add(button);

        JButton button_1 = new JButton("清除條件");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                classPathText.setText("");
                tagText.setText("");
                logText.setText("");

                chckbxV.setSelected(false);
                chckbxD.setSelected(false);
                chckbxI.setSelected(false);
                chckbxW.setSelected(false);
                chckbxE.setSelected(false);

                resultTextArea.setText("");
                loadTableData(null);
            }
        });
        panel_2.add(button_1);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("過濾後", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.SOUTH);

        resultTextArea = new JTextArea();
        JCommonUtil.createScrollComponent(panel_1, resultTextArea);

        JPanel panel_4 = new JPanel();
        tabbedPane.addTab("過濾後Table", null, panel_4, null);
        panel_4.setLayout(new BorderLayout(0, 0));

        logTable = new JTable();
        JTableUtil.defaultSetting(logTable);
        JCommonUtil.createScrollComponent(panel_4, logTable);

        JCommonUtil.setFontAll(this.getRootPane());
    }

    private void loadTableData(List<LogInfo> list) {
        DefaultTableModel defModel = JTableUtil.createModel(true, new Object[] { "level", "class", "tag", "log" });
        if (list != null && !list.isEmpty()) {
            for (LogInfo info : list) {
                defModel.addRow(new Object[] { info.level, info.classPath, info.tag, info.line });
            }
        }
        logTable.setModel(defModel);
        JTableUtil.setColumnWidths_ByDataContent(logTable, null);

        JTableUtil.newInstance(logTable).setColumnColor_byCondition(0, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String valStr = String.valueOf(value);
                if (valStr.equals("W")) {
                    return Pair.of(Color.GREEN, null);
                } else if (valStr.equals("E")) {
                    return Pair.of(Color.pink, null);
                }
                return null;
            }
        });
    }

    private class Config {
        boolean chkV;
        boolean chkD;
        boolean chkI;
        boolean chkW;
        boolean chkE;
        String classPath;
        String tag;
        String log;
    }

    private List<LogInfo> processLog(String logText, Config config) {
        List<LogInfo> list = toList(logText);
        List<LogInfo> rtnList = new ArrayList<LogInfo>();
        for (LogInfo info : list) {
            boolean findOk = false;
            if (StringUtils.isNotBlank(config.classPath)) {
                if (info.classPath.toLowerCase().contains(config.classPath.toLowerCase())) {
                    findOk = true;
                }
            }
            if (StringUtils.isNotBlank(config.log)) {
                if (info.line.toLowerCase().contains(config.log.toLowerCase())) {
                    findOk = true;
                }
            }
            if (StringUtils.isNotBlank(config.tag)) {
                if (info.tag.toLowerCase().contains(config.tag.toLowerCase())) {
                    findOk = true;
                }
            }
            if (config.chkV && "V".equalsIgnoreCase(info.level)) {
                findOk = true;
            }
            if (config.chkD && "D".equalsIgnoreCase(info.level)) {
                findOk = true;
            }
            if (config.chkI && "I".equalsIgnoreCase(info.level)) {
                findOk = true;
            }
            if (config.chkW && "W".equalsIgnoreCase(info.level)) {
                findOk = true;
            }
            if (config.chkE && "E".equalsIgnoreCase(info.level)) {
                findOk = true;
            }
            if (findOk) {
                rtnList.add(info);
            }
        }
        return rtnList;
    }

    private class LogInfo {
        String line;
        String level;
        String tag;
        String classPath;

        @Override
        public String toString() {
            return "" + classPath + "|" + tag + "|" + level + " | " + line;
        }
    }

    /**
     * 05-30 02:42:48.582 17562-17562
     */
    private static final String DATE_TIME = "\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s\\d+\\-\\d+";
    Pattern dateTimePtn = Pattern.compile(DATE_TIME);

    private static final String NORMAL_PTN = DATE_TIME + "\\/(\\?|[^\\s]+)\\s([VIDWE])\\/(.*?)\\:(.*)";
    Pattern normalPtn = Pattern.compile(NORMAL_PTN);
    private JTextArea resultTextArea;
    private JTable logTable;
    private JTextField tagText;
    private JTextField logText;

    private boolean isPatternFind(Pattern ptn, String line) {
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return true;
        }
        return false;
    }

    private List<LogInfo> toList(String logText) {
        List<LogInfo> list = new ArrayList<LogInfo>();
        try {
            LogInfo log = new LogInfo();
            LineNumberReader reader = new LineNumberReader(new StringReader(logText));
            for (String line = null; (line = reader.readLine()) != null;) {

                if (isPatternFind(dateTimePtn, line)) {
                    if (log != null && StringUtils.isNotBlank(log.line)) {
                        list.add(log);
                        log = new LogInfo();
                    }
                } else {
                    if (log != null) {
                        log.line += "\n" + line;
                    }
                }

                Matcher mth = normalPtn.matcher(line);
                if (mth.find()) {
                    String clz = mth.group(1);
                    String level = mth.group(2);
                    String tag = mth.group(3);
                    String lineK = mth.group(4);

                    log.classPath = clz;
                    log.level = level;
                    log.tag = tag;
                    log.line = lineK;
                }
            }
            reader.close();

            if (log != null && StringUtils.isNotBlank(log.line)) {
                list.add(log);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
