package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import gtu.spring.SimilarityUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTableUtil;
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
public class FuzzyCompareUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTable resultTable;
    private JPanel jPanel4;
    private JTextArea jTextArea2;
    private JTextArea jTextArea1;
    private JPanel jPanel3;
    private JPanel jPanel2;
    JSlider jslider;
    private JCheckBox isIgnoreCaseChk;
    private ButtonGroup buttonGroup;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FuzzyCompareUI inst = new FuzzyCompareUI();
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    public FuzzyCompareUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            this.setTitle("FuzzyCompareUI");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("compare1", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            jTextArea1 = new JTextArea();
                            jScrollPane1.setViewportView(jTextArea1);
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("compare2", null, jPanel2, null);
                    {
                        JPanel j2Panel = new JPanel();
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(j2Panel, BorderLayout.CENTER);
                        j2Panel.setLayout(new BorderLayout(0, 0));
                        j2Panel.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            panel_5 = new JPanel();
                            j2Panel.add(panel_5, BorderLayout.NORTH);
                        }
                        {
                            panel_6 = new JPanel();
                            j2Panel.add(panel_6, BorderLayout.WEST);
                        }
                        {
                            panel_7 = new JPanel();
                            j2Panel.add(panel_7, BorderLayout.SOUTH);
                            {
                                lblNewLabel = new JLabel("特殊format為=>  \\{c\\:(.*?),v\\:(.*?)\\}");
                                lblNewLabel.setFont(new Font("新細明體", Font.PLAIN, 14));
                                lblNewLabel.setForeground(Color.RED);
                                panel_7.add(lblNewLabel);
                            }
                        }
                        {
                            panel_8 = new JPanel();
                            j2Panel.add(panel_8, BorderLayout.EAST);
                        }
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(527, 309));
                        {
                            jTextArea2 = new JTextArea();
                            jScrollPane2.setViewportView(jTextArea2);
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("result", null, jPanel3, null);
                    {
                        jPanel4 = new JPanel();
                        jslider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);// 最小值
                                                                              // ,最大值,default值
                        jslider.setMajorTickSpacing(10);
                        jslider.setMinorTickSpacing(5);
                        jslider.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        jslider.setPaintTicks(true);
                        jslider.setPaintLabels(true);
                        jslider.addChangeListener(new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                compareBtnAction(e);
                            }
                        });
                        jPanel4.add(jslider);
                        jslider.setPreferredSize(new java.awt.Dimension(447, 44));
                        FlowLayout jPanel4Layout = new FlowLayout();
                        jPanel3.add(jPanel4, BorderLayout.NORTH);
                        jPanel4.setLayout(jPanel4Layout);
                        jPanel4.setPreferredSize(new java.awt.Dimension(527, 51));
                    }
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            DefaultTableModel model = JTableUtil.createModel(false, "compare1", "compare2");
                            resultTable = new JTable();
                            JTableUtil.defaultSetting_AutoResize(resultTable);
                            jScrollPane3.setViewportView(resultTable);
                            resultTable.setModel(model);
                        }
                        isIgnoreCaseChk = new JCheckBox("忽略大小寫");
                        isIgnoreCaseChk.setSelected(true);
                        jPanel3.add(isIgnoreCaseChk, BorderLayout.SOUTH);
                    }
                }
                {
                    panel = new JPanel();
                    jTabbedPane1.addTab("get set", null, panel, null);
                    panel.setLayout(new BorderLayout(0, 0));
                    {
                        panel_1 = new JPanel();
                        panel.add(panel_1, BorderLayout.NORTH);
                        {
                            compare1ToCompare2Radio = new JRadioButton("1->2");
                            compare1ToCompare2Radio.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    compare12Action();
                                }
                            });
                            panel_1.add(compare1ToCompare2Radio);
                        }
                        {
                            compare2ToCompare1Radio = new JRadioButton("2->1");
                            compare2ToCompare1Radio.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    compare12Action();
                                }
                            });
                            panel_1.add(compare2ToCompare1Radio);
                        }
                        {
                            compare1NameText = new JTextField();
                            compare1NameText.setToolTipText("compare1Name");
                            panel_1.add(compare1NameText);
                            compare1NameText.setColumns(10);
                            compare1NameText.setText("entity1");
                            compare1NameText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                    compare12Action();
                                }
                            }));
                        }
                        {
                            compare2NameText = new JTextField();
                            compare2NameText.setToolTipText("compare2Name");
                            compare2NameText.setColumns(10);
                            panel_1.add(compare2NameText);
                            compare2NameText.setText("entity2");
                            compare2NameText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                    compare12Action();
                                }
                            }));
                        }
                    }
                    {
                        panel_2 = new JPanel();
                        panel.add(panel_2, BorderLayout.WEST);
                    }
                    {
                        panel_3 = new JPanel();
                        panel.add(panel_3, BorderLayout.EAST);
                    }
                    {
                        panel_4 = new JPanel();
                        panel.add(panel_4, BorderLayout.SOUTH);
                    }
                    {
                        getterSetterTextarea = new JTextArea();
                        JTextAreaUtil.applyCommonSetting(getterSetterTextarea);
                        panel.add(JCommonUtil.createScrollComponent(getterSetterTextarea), BorderLayout.CENTER);
                        getterSetterTextarea.setColumns(10);
                    }
                    {
                        buttonGroup = JButtonGroupUtil.createRadioButtonGroup(compare1ToCompare2Radio, compare2ToCompare1Radio);
                    }
                }
                {
                    panel_9 = new JPanel();
                    jTabbedPane1.addTab("補正", null, panel_9, null);
                    panel_9.setLayout(new BorderLayout(0, 0));
                    {
                        panel_10 = new JPanel();
                        panel_9.add(panel_10, BorderLayout.NORTH);
                        {
                            lblNewLabel_1 = new JLabel("format");
                            panel_10.add(lblNewLabel_1);
                        }
                        {
                            fixFormatText = new JTextArea();
                            JTextAreaUtil.applyCommonSetting(fixFormatText);
                            fixFormatText.setText("#compare1# #compare2# #comment#");
                            panel_10.add(JCommonUtil.createScrollComponent(fixFormatText));
                            fixFormatText.setColumns(40);
                            fixFormatText.setRows(2);
                        }
                    }
                    {
                        panel_11 = new JPanel();
                        panel_9.add(panel_11, BorderLayout.WEST);
                    }
                    {
                        panel_12 = new JPanel();
                        panel_9.add(panel_12, BorderLayout.SOUTH);
                    }
                    {
                        panel_13 = new JPanel();
                        panel_9.add(panel_13, BorderLayout.EAST);
                    }
                    {
                        fixTextArea = new JTextArea();
                        panel_9.add(JCommonUtil.createScrollComponent(fixTextArea), BorderLayout.CENTER);
                    }
                }
            }

            {
                JCommonUtil.setJFrameCenter(this);
                JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
                JCommonUtil.defaultToolTipDelay();
            }
            pack();
            this.setSize(548, 376);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compareBtnAction(ChangeEvent evt) {
        String text1 = jTextArea1.getText();
        String text2 = jTextArea2.getText();
        int sliderValue = jslider.getValue();
        System.out.println("sliderValue = " + sliderValue);

        FixTextAreaHandler fix = new FixTextAreaHandler();

        DefaultTableModel model = JTableUtil.createModel(false, "compare1", "compare2", "相似度", "comment");
        resultTable.setModel(model);

        Pattern pattern = Pattern.compile("[^\\s\\t]+");
        Pattern pattern2 = Pattern.compile("\\{c\\:(.*?),v\\:(.*?)\\}");
        Pattern pattern21 = Pattern.compile("\\{v\\:(.*?),c\\:(.*?)\\}");
        Matcher matcher = pattern.matcher(text1);
        Matcher matcher2 = pattern.matcher(text2);
        Matcher matcher3 = pattern2.matcher(text2);
        Matcher matcher31 = pattern21.matcher(text2);
        List<String> text1List = new ArrayList<String>();
        List<String> text2List = new ArrayList<String>();
        Map<String, String> text2Map = new HashMap<String, String>();

        while (matcher.find()) {
            text1List.add(StringUtils.trimToEmpty(matcher.group()));
        }
        while (matcher3.find()) {
            text2Map.put(StringUtils.trimToEmpty(matcher3.group(1)), StringUtils.trimToEmpty(matcher3.group(2)));
        }
        while (matcher31.find()) {
            text2Map.put(StringUtils.trimToEmpty(matcher31.group(2)), StringUtils.trimToEmpty(matcher31.group(1)));
        }
        while (matcher2.find()) {
            text2List.add(StringUtils.trimToEmpty(matcher2.group()));
        }
        for (String text2Key : text2Map.keySet()) {
            if (!text2List.contains(text2Key)) {
                text2List.add(text2Key);
            }
        }

        for (String compare1 : text1List) {
            List<CompareResult> compareList = new ArrayList<CompareResult>();
            String comment = "";
            for (String compare2 : text2List) {
                CompareResult result = new CompareResult();
                result.result = fuzzyCompare(compare1, compare2, isIgnoreCaseChk.isSelected());
                result.compareStr = compare2;
                compareList.add(result);
            }
            Collections.sort(compareList, comparator);
            String compare2Str = "";
            BigDecimal percent = BigDecimal.ZERO;
            if (!compareList.isEmpty()) {
                compare2Str = compareList.get(0).compareStr;
                percent = compareList.get(0).getParcent();
                float fuzzyPersent = compareList.get(0).getParcent().floatValue();
                if (fuzzyPersent < sliderValue) {
                    compare2Str = "";
                }
                if (text2Map.containsKey(compare2Str)) {
                    comment = text2Map.get(compare2Str);
                }
            }
            model.addRow(new Object[] { compare1, compare2Str, percent, comment });

            // 設定補正資料
            fix.append(compare1, compare2Str, comment);
        }

        // 設定補正資料
        fix.setFixTextArea(fixFormatText.getText(), text1);
    }

    private class FixTextAreaHandler {
        Map<String, Data> fixMap = new HashMap<String, Data>();

        class Data {
            String compare1;
            String compare2;
            String comment;
        }

        void append(String compare1, String compare2Str, String comment) {
            Data d = new Data();
            d.compare1 = compare1;
            d.compare2 = compare2Str;
            d.comment = comment;
            fixMap.put(compare1, d);
        }

        void setFixTextArea(String format, String text1) {
            format = StringUtils.defaultString(format);
            Pattern pattern = Pattern.compile("[^\\s\\t]+");
            Matcher mth = pattern.matcher(StringUtils.defaultString(text1));
            StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                String key = mth.group();
                String tmpValue = format.toString();
                if (fixMap.containsKey(key)) {
                    Data d = fixMap.get(key);
                    tmpValue = tmpValue.replaceAll(Pattern.quote("#compare1#"), d.compare1);
                    tmpValue = tmpValue.replaceAll(Pattern.quote("#compare2#"), d.compare2);
                    tmpValue = tmpValue.replaceAll(Pattern.quote("#comment#"), d.comment);
                }
                mth.appendReplacement(sb, tmpValue);
            }
            mth.appendTail(sb);
            fixTextArea.setText(sb.toString());
        }
    }

    Comparator<CompareResult> comparator = new Comparator<CompareResult>() {
        @Override
        public int compare(CompareResult o1, CompareResult o2) {
            return Double.valueOf(o1.result).compareTo(o2.result) * -1;
        }
    };
    private JPanel panel;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JTextArea getterSetterTextarea;
    private JRadioButton compare1ToCompare2Radio;
    private JRadioButton compare2ToCompare1Radio;
    private JTextField compare1NameText;
    private JTextField compare2NameText;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JPanel panel_8;
    private JLabel lblNewLabel;
    private JPanel panel_9;
    private JPanel panel_10;
    private JPanel panel_11;
    private JPanel panel_12;
    private JPanel panel_13;
    private JTextArea fixTextArea;
    private JLabel lblNewLabel_1;
    private JTextArea fixFormatText;

    static class CompareResult {
        String compareStr;
        Double result;

        public BigDecimal getParcent() {
            BigDecimal b = new BigDecimal(result);
            b = b.multiply(new BigDecimal(100));
            b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
            return b;
        }
    }

    static double fuzzyCompare(String compare1, String compare2, boolean ignoreCase) {
        if (ignoreCase) {
            compare1 = compare1.toLowerCase();
            compare2 = compare2.toLowerCase();
        }
        return SimilarityUtil.sim(compare1, compare2);
    }

    private void compare12Action() {
        try {
            String compare1Name = compare1NameText.getText();
            String compare2Name = compare2NameText.getText();

            StringBuilder sb = new StringBuilder();
            final String pattern = "%s.set%s(%s.get%s());\n";

            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String compare1Str = (String) model.getValueAt(ii, JTableUtil.getRealColumnPos(0, resultTable));
                String compare2Str = (String) model.getValueAt(ii, JTableUtil.getRealColumnPos(1, resultTable));

                String tmpStr = "";
                if (JButtonGroupUtil.getSelectedButton(buttonGroup) == compare2ToCompare1Radio) {
                    tmpStr = String.format(pattern, compare1Name, StringUtils.capitalise(compare1Str), compare2Name, StringUtils.capitalise(compare2Str));
                } else if (JButtonGroupUtil.getSelectedButton(buttonGroup) == compare1ToCompare2Radio) {
                    tmpStr = String.format(pattern, compare2Name, StringUtils.capitalise(compare2Str), compare1Name, StringUtils.capitalise(compare1Str));
                }
                sb.append(tmpStr);
            }
            getterSetterTextarea.setText(sb.toString());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
