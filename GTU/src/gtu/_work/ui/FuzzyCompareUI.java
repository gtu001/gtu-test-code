package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import gtu.spring.SimilarityUtil;
import gtu.swing.util.JTableUtil;

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

        Pattern pattern = Pattern.compile("[\\w_-]+");
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
                result.result = fuzzyCompare(compare1, compare2, false);
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
}
