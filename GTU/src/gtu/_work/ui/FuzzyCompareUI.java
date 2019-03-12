package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
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
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
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
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
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
                        jslider = new JSlider(JSlider.HORIZONTAL, 30, 100, 30);// 最小值
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
                            JTableUtil.defaultSetting(resultTable);
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

        DefaultTableModel model = JTableUtil.createModel(false, "compare1", "compare2", "same word");
        resultTable.setModel(model);

        Pattern pattern = Pattern.compile("[\\$\\w_-]+");
        Matcher matcher = pattern.matcher(text1);
        Matcher matcher2 = pattern.matcher(text2);
        List<String> text1List = new ArrayList<String>();
        List<String> text2List = new ArrayList<String>();
        while (matcher.find()) {
            text1List.add(matcher.group());
        }
        while (matcher2.find()) {
            text2List.add(matcher2.group());
        }
        for (String compare1 : text1List) {
            List<CompareResult> compareList = new ArrayList<CompareResult>();
            for (String compare2 : text2List) {
                CompareResult result = new CompareResult();
                result.result = fuzzyCompare(compare1, compare2, isIgnoreCaseChk.isSelected());
                result.compareStr = compare2;
                compareList.add(result);
            }
            Collections.sort(compareList, comparator);
            String compare2Str = "";
            BigDecimal sameWord = BigDecimal.ZERO;
            if (!compareList.isEmpty()) {
                compare2Str = compareList.get(0).compareStr;
                sameWord = compareList.get(0).getParcent();
                float fuzzyPersent = compareList.get(0).getParcent().floatValue();
                if (fuzzyPersent < sliderValue) {
                    compare2Str = "";
                }
            }
            model.addRow(new Object[] { compare1, compare2Str, sameWord });
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
