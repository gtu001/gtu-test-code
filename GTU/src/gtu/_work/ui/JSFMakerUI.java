package gtu._work.ui;

import gtu.clipboard.ClipboardUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.Transformer;
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
public class JSFMakerUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 8484072391089016824L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JButton process;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTable datatableTable;
    private JButton processDatatableBtn;
    private JTextArea datatableTemplateArea;
    private JPanel jPanel2;
    private JTable contentTable;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JSFMakerUI inst = new JSFMakerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public JSFMakerUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final JFrame thisFrame = this;
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
                    jTabbedPane1.addTab("query condition", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            DefaultTableModel contentTableModel = JTableUtil.createModel(true, "th1", "td1");
                            contentTableModel.addRow(new Object[] {});
                            contentTable = new JTable();
                            jScrollPane1.setViewportView(contentTable);
                            contentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                            contentTable.setAutoscrolls(true);
                            contentTable.setAutoCreateRowSorter(false);
                            contentTable.setUpdateSelectionOnSort(false);
                            contentTable.setAutoCreateColumnsFromModel(true);
                            contentTable.setColumnSelectionAllowed(true);
                            contentTable.setModel(contentTableModel);
                            contentTable.addKeyListener(new KeyAdapter() {
                                ColumnType temp = null;

                                public void keyPressed(KeyEvent evt) {
                                    JTableUtil uu = JTableUtil.newInstance(contentTable);
                                    if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                                        uu.pasteFromObject_singleValueToSelectedCell(null);
                                    }
                                    if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_C) {
                                        temp = (ColumnType) uu.getSelectedValue();
                                    }
                                    if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_V) {
                                        int rowPos = -1;
                                        int colPos = -1;
                                        DefaultTableModel model = (DefaultTableModel) uu.getModel();
                                        for (int row : contentTable.getSelectedRows()) {
                                            if (contentTable.getRowSorter() != null) {
                                                rowPos = contentTable.getRowSorter().convertRowIndexToModel(row);
                                            } else {
                                                rowPos = row;
                                            }
                                            ColumnType oldCol = null;
                                            for (int col : contentTable.getSelectedColumns()) {
                                                colPos = contentTable.convertColumnIndexToModel(col);
                                                oldCol = (ColumnType) model.getValueAt(rowPos, colPos);
                                                if (oldCol == null) {
                                                    model.setValueAt(temp, rowPos, colPos);
                                                } else {
                                                    oldCol.tag = temp.tag;
                                                    oldCol.otherAttrs = temp.otherAttrs;
                                                }
                                            }
                                        }
                                    }
                                    if (evt.getModifiers() == KeyEvent.CTRL_MASK && evt.getKeyCode() == KeyEvent.VK_X) {
                                        temp = (ColumnType) uu.getSelectedValue();
                                        uu.setValueAtSelectedCell(null);
                                    }
                                }
                            });

                            contentTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JPopupMenuUtil poputil = JPopupMenuUtil.newInstance(contentTable).applyEvent(evt);
                                    if (contentTable.getSelectedColumn() != -1 && contentTable.getSelectedRow() != -1) {
                                        JMenuItem item = null;
                                        for (final JsfTag jsf : JsfTag.values()) {
                                            item = new JMenuItem();
                                            item.setText("==>" + jsf.tag);
                                            item.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent arg0) {
                                                    ColumnType current = (ColumnType) JTableUtil.newInstance(contentTable).getSelectedValue();
                                                    String defaultValue = null;
                                                    if (current != null) {
                                                        try {
                                                            current = (ColumnType) current.clone();
                                                        } catch (CloneNotSupportedException e) {
                                                            JCommonUtil.handleException(e);
                                                        }
                                                        defaultValue = current.attrValue;
                                                    } else {
                                                        current = new ColumnType();
                                                    }
                                                    String value = StringUtils.defaultString(JCommonUtil._jOptionPane_showInputDialog("input attribute value", defaultValue));
                                                    current.attrValue = value;
                                                    current.tag = jsf;
                                                    JTableUtil.newInstance(contentTable).setValueAtSelectedCell(current);
                                                }
                                            });
                                            poputil.addJMenuItem(item);
                                        }
                                    }
                                    poputil.addJMenuItem("==>paste from clipboard", new ActionListener() {
                                        public void actionPerformed(ActionEvent arg0) {
                                            JTableUtil.newInstance(contentTable).pasteFromClipboard_multiRowData(true, new Transformer() {
                                                public Object transform(Object paramObject) {
                                                    String value = (String) paramObject;
                                                    ColumnType col = new ColumnType();
                                                    col.attrValue = value;
                                                    col.tag = JsfTag.OUTPUTTEXT;
                                                    return col;
                                                }
                                            });
                                        }
                                    });
                                    poputil.addJMenuItem("==>paste from clipboard (html)", new ActionListener() {
                                        public void actionPerformed(ActionEvent arg0) {
                                            resetPasteClipboardHtmlToJtable();
                                            JTableUtil.newInstance(contentTable).pasteFromClipboard_multiRowData(true, new Transformer() {
                                                public Object transform(Object paramObject) {
                                                    String value = (String) paramObject;
                                                    ColumnType col = new ColumnType();
                                                    col.attrValue = value;
                                                    col.tag = JsfTag.OUTPUTTEXT;
                                                    return col;
                                                }
                                            });
                                        }
                                    });

                                    poputil.addJMenuItem("==>set other attributes", new ActionListener() {
                                        public void actionPerformed(ActionEvent paramActionEvent) {
                                            final ColumnType current = (ColumnType) JTableUtil.newInstance(contentTable).getSelectedValue();
                                            if (current == null) {
                                                JCommonUtil._jOptionPane_showMessageDialog_error("must select columnType frist!");
                                                return;
                                            }
                                            final JSFMakerUI_attrDialog dialog = new JSFMakerUI_attrDialog(thisFrame);
                                            dialog.setVisible(true);
                                            new Thread(new Runnable() {
                                                public void run() {
                                                    for (; dialog.isVisible();) {
                                                        try {
                                                            Thread.sleep(300);
                                                        } catch (InterruptedException e) {
                                                            JCommonUtil.handleException(e);
                                                        }
                                                    }
                                                    current.otherAttrs = dialog.getAttribute();
                                                }
                                            }).start();
                                        }
                                    });

                                    final int currentSelectedRow = JTableUtil.newInstance(contentTable).getSelectedRow();
                                    final Boolean defaultVal = colspanMap.get(currentSelectedRow) == null ? false : colspanMap.get(currentSelectedRow);
                                    poputil.addJMenuItem("==>set colspan td : " + defaultVal, new ActionListener() {
                                        public void actionPerformed(ActionEvent arg0) {
                                            Boolean bool = (Boolean) JOptionPaneUtil.newInstance().showInputDialog_drowdown("set current row is colspan ?", "COLSPAN", new Object[] { true, false },
                                                    defaultVal);
                                            colspanMap.put(currentSelectedRow, bool);
                                            System.out.println(colspanMap);
                                        }
                                    });

                                    poputil.addJMenuItem("-------------------", false);
                                    poputil.addJMenuItem(JTableUtil.newInstance(contentTable).getDefaultJMenuItems()).show();

                                    if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                                        ColumnType current = (ColumnType) JTableUtil.newInstance(contentTable).getSelectedValue();
                                        JCommonUtil._jOptionPane_showMessageDialog_info(current.tag.toXhtml(current));
                                    }
                                }
                            });
                        }
                    }
                    {
                        process = new JButton();
                        jPanel1.add(process, BorderLayout.SOUTH);
                        process.setText("process");
                        process.setPreferredSize(new java.awt.Dimension(725, 30));
                        process.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                DefaultTableModel table = (DefaultTableModel) contentTable.getModel();
                                List<Integer> colList = JTableUtil.newInstance(contentTable).getTableColumnModelIndex();
                                StringBuilder sb = new StringBuilder();
                                StringBuilder sb_inner = null;
                                for (int ii = 0; ii < table.getDataVector().size(); ii++) {
                                    sb_inner = new StringBuilder();
                                    Vector<?> vec = (Vector<?>) table.getDataVector().get(ii);
                                    sb_inner.append("<tr>\n");
                                    boolean moreThenOneTd = false;

                                    if (!colspanMap.containsKey(ii) || !colspanMap.get(ii)) {
                                        for (int jj = 0; jj < colList.size(); jj++) {
                                            String ttt = (jj % 2 == 0) ? "th" : "td";
                                            ColumnType col = (ColumnType) vec.get(colList.get(jj));
                                            sb_inner.append(String.format("<%s>\n", ttt));
                                            if (col != null) {
                                                sb_inner.append(col.tag.toXhtml(col));
                                                moreThenOneTd = true;
                                            }
                                            sb_inner.append(String.format("</%s>\n", ttt));
                                        }
                                    } else {
                                        //合併 第二個以後的 td
                                        {//第一欄一定顯示
                                            sb_inner.append("<th>\n");
                                            ColumnType col = (ColumnType) vec.get(colList.get(0));
                                            if (col != null) {
                                                sb_inner.append(col.tag.toXhtml(col));
                                            }
                                            sb_inner.append("</th>\n");
                                        }
                                        sb_inner.append(String.format("<td colspan=\"%d\">\n", colList.size()));
                                        for (int jj = 1; jj < colList.size(); jj++) {
                                            ColumnType col = (ColumnType) vec.get(colList.get(jj));
                                            if (col != null) {
                                                sb_inner.append(col.tag.toXhtml(col));
                                                sb_inner.append("<h:outputText value=\"　\" />");
                                            }
                                        }
                                        sb_inner.append("</td>");
                                        moreThenOneTd = true;
                                    }

                                    sb_inner.append("</tr>\n");
                                    if (moreThenOneTd) {
                                        sb.append(sb_inner);
                                    }
                                }
                                ClipboardUtil.getInstance().setContents("<table>" + sb + "</table>");
                                JCommonUtil._jOptionPane_showMessageDialog_info("ok!!");
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("datatable", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.NORTH);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(725, 227));
                        {
                            datatableTemplateArea = new JTextArea();
                            datatableTemplateArea.setText(dataTableTemplate);
                            jScrollPane2.setViewportView(datatableTemplateArea);
                            datatableTemplateArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
                        }
                    }
                    {
                        processDatatableBtn = new JButton();
                        jPanel2.add(processDatatableBtn, BorderLayout.EAST);
                        processDatatableBtn.setText("process");
                        processDatatableBtn.setPreferredSize(new java.awt.Dimension(139, 296));
                        processDatatableBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (StringUtils.isEmpty(datatableTemplateArea.getText())) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("template can't empty!");
                                    return;
                                }
                                DefaultTableModel model = (DefaultTableModel) datatableTable.getModel();
                                ColumnType column = null;
                                List<List<String>> list = new ArrayList<List<String>>();
                                List<String> list_inner = new ArrayList<String>();
                                for (int row = 0; row < model.getRowCount(); row++) {
                                    list_inner = new ArrayList<String>();
                                    boolean anyColunNotNull = false;
                                    for (int col = 0; col < model.getColumnCount(); col++) {
                                        column = (ColumnType) model.getValueAt(row, col);
                                        if (column != null) {
                                            anyColunNotNull = true;
                                            list_inner.add(column.tag.toXhtml(column));
                                        } else {
                                            list_inner.add("");
                                        }
                                    }
                                    if (anyColunNotNull) {
                                        list.add(list_inner);
                                    }
                                }

                                Map<String, Object> root = new HashMap<String, Object>();
                                root.put("list", list);

                                try {
                                    String content = FreeMarkerSimpleUtil.replace(datatableTemplateArea.getText(), root);
                                    if (datatableTemplateArea.getText().equals(content)) {
                                        JCommonUtil._jOptionPane_showMessageDialog_error("template error!");
                                        return;
                                    }
                                    ClipboardUtil.getInstance().setContents(content);
                                    JCommonUtil._jOptionPane_showMessageDialog_info("ok!!");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel2.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            DefaultTableModel datatableTableModel = JTableUtil.createModel(true, "title", "column");
                            datatableTableModel.addRow(new Object[] {});
                            datatableTable = new JTable();
                            jScrollPane3.setViewportView(datatableTable);
                            datatableTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                            datatableTable.setAutoscrolls(true);
                            datatableTable.setAutoCreateRowSorter(false);
                            datatableTable.setUpdateSelectionOnSort(false);
                            datatableTable.setAutoCreateColumnsFromModel(true);
                            datatableTable.setColumnSelectionAllowed(true);
                            datatableTable.setModel(datatableTableModel);
                            datatableTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    final Object selectValue = JTableUtil.newInstance(datatableTable).getSelectedValue();

                                    JPopupMenuUtil poputil = JPopupMenuUtil.newInstance(datatableTable).applyEvent(evt);//
                                    {
                                        JMenuItem item = null;
                                        for (final JsfTag jsf : JsfTag.values()) {
                                            item = new JMenuItem();
                                            item.setText("==>" + jsf.tag);
                                            item.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent arg0) {
                                                    ColumnType current = (ColumnType) selectValue;
                                                    String defaultValue = null;
                                                    if (current != null) {
                                                        try {
                                                            current = (ColumnType) current.clone();
                                                        } catch (CloneNotSupportedException e) {
                                                            JCommonUtil.handleException(e);
                                                        }
                                                        defaultValue = current.attrValue;
                                                    } else {
                                                        current = new ColumnType();
                                                    }
                                                    String value = StringUtils.defaultString(JCommonUtil._jOptionPane_showInputDialog("input attribute value", defaultValue));
                                                    current.attrValue = value;
                                                    current.tag = jsf;
                                                    JTableUtil.newInstance(datatableTable).setValueAtSelectedCell(current);
                                                }
                                            });
                                            poputil.addJMenuItem(item);
                                        }
                                    }

                                    poputil.addJMenuItem("==>paste from clipboard", new ActionListener() {
                                        public void actionPerformed(ActionEvent arg0) {
                                            JTableUtil.newInstance(datatableTable).pasteFromClipboard_multiRowData(true, new Transformer() {
                                                public Object transform(Object paramObject) {
                                                    String value = (String) paramObject;
                                                    ColumnType col = new ColumnType();
                                                    col.attrValue = value;
                                                    col.tag = JsfTag.OUTPUTTEXT;
                                                    return col;
                                                }
                                            });
                                        }
                                    });

                                    poputil.addJMenuItem("-------------------", false);

                                    JTableUtil tblUtil = JTableUtil.newInstance(datatableTable);
                                    JMenuItem a3 = tblUtil.jMenuItem_addRow(true, "");
                                    JMenuItem a3_1 = tblUtil.jMenuItem_addRow(true, "input row count!");
                                    JMenuItem a4 = tblUtil.jMenuItem_removeRow("");
                                    JMenuItem a5 = tblUtil.jMenuItem_removeAllRow("");
                                    a3_1.setText("add multi row");

                                    poputil.addJMenuItem(Arrays.asList(a3, a3_1, a4, a5)).show();
                                }
                            });
                            datatableTable.addKeyListener(JTableUtil.newInstance(datatableTable).defaultKeyAdapter());
                        }
                    }
                }
            }
            pack();
            this.setSize(746, 516);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

    void resetPasteClipboardHtmlToJtable() {
        String content = ClipboardUtil.getInstance().getContents();
        Pattern tdStartPattern = Pattern.compile("<[tT][dDhH][^>]*>");
        Pattern tdEndPattern = Pattern.compile("</[tT][dDhH]>");
        Pattern innerPattern_HasTag = Pattern.compile("<[\\w:]+\\s[^>]*value=\"([^\"]*)\"[^>]*>", Pattern.MULTILINE);
        Matcher innerMatcher = null;
        Scanner scan = new Scanner(content);
        Scanner tdScan = null;
        String currentContent = null;
        String tdContent = null;
        StringBuilder sb = new StringBuilder();
        scan.useDelimiter("<tr>");
        for (; scan.hasNext();) {
            boolean anyMatcher = false;

            tdScan = new Scanner(scan.next());
            tdScan.useDelimiter(tdStartPattern);
            while (tdScan.hasNext()) {
                tdScan.useDelimiter(tdEndPattern);
                if (tdScan.hasNext()) {
                    tdContent = tdScan.next().replaceAll(tdStartPattern.pattern(), "");
                    {
                        innerMatcher = innerPattern_HasTag.matcher(tdContent.toString());
                        if (innerMatcher.find()) {
                            currentContent = StringUtils.defaultIfEmpty(innerMatcher.group(1), "&nbsp;");
                            //                            System.out.format("1[%s]\n", currentContent);
                            sb.append(currentContent + "\t");
                            continue;
                        }
                        currentContent = tdContent.toString().replaceAll("<[\\w:=,.#;/'?\"\\s\\{\\}\\(\\)\\[\\]]+>", "");
                        currentContent = currentContent.replaceAll("[\\s\t\n]", "");
                        currentContent = StringUtils.defaultIfEmpty(currentContent, "&nbsp;");
                        //                        System.out.format("2[%s]\n", currentContent);
                        sb.append(currentContent + "\t");
                        anyMatcher = true;
                    }
                }
                tdScan.useDelimiter(tdStartPattern);
            }
            if (anyMatcher) {
                sb.append("\n");
            }
        }
        scan.close();
        ClipboardUtil.getInstance().setContents(sb);
        System.out.println("####################################");
        System.out.println(sb);
        System.out.println("####################################");
    }

    static Map<Integer, Boolean> colspanMap = new HashMap<Integer, Boolean>();

    static final String dataTableTemplate;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("        <p:dataTable value=\"\" var=\"item\" emptyMessage=\"\"                                                                            \n");
        sb.append("            rowIndexVar=\"rowIndex\" rowsPerPageTemplate=\"5,10,15\" paginator=\"true\"                                                   \n");
        sb.append("            selection=\"${r\"#{requestScope.tempItem}\"}\" selectionMode=\"single\" rowKey=\"${r\"#{item}\"}\"                                         \n");
        sb.append("            rows=\"10\" style=\"text-align:center;margin-top:20px;\"                                                                     \n");
        sb.append("            paginatorTemplate=\"{CurrentPageReport} {FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}\">   \n");
        sb.append("            <p:ajax event=\"rowSelect\" />                                                                                                  \n");
        sb.append("                     <p:column>                                                                           \n");
        sb.append("                         <f:facet name=\"header\">                                                      \n");
        sb.append("                             <h:outputText value=\"序號\" />                                                                 \n");
        sb.append("                         </f:facet>                                                                   \n");
        sb.append("                         <h:outputText value=\"${r\"#{index+1}\"}\" />                                                             \n");
        sb.append("                     </p:column>                                                                          \n");
        sb.append(" <#list list as row>                                                                                                      \n");
        sb.append("                     <p:column>                                                                           \n");
        sb.append("                         <f:facet name=\"header\">                                                      \n");
        sb.append("                             ${row[0]}                                                            \n");
        sb.append("                         </f:facet>                                                                   \n");
        sb.append("                         ${row[1]}                                                                    \n");
        sb.append("                     </p:column>                                                                          \n");
        sb.append(" </#list>                                                                                                                 \n");
        sb.append("                 </p:dataTable>                                                                               \n");
        dataTableTemplate = sb.toString();
    }

    static class ColumnType implements Cloneable {
        JsfTag tag;
        String attrValue;
        String otherAttrs;

        @Override
        public String toString() {
            return attrValue + "[" + tag.tag + "]" + (StringUtils.isNotEmpty(otherAttrs) ? "＊" : "");
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    enum JsfTag implements Cloneable {
        OUTPUTTEXT("h:outputText") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                return String.format("<h:outputText value=\"%1$s\" %2$s />", column.attrValue, column.otherAttrs);
            }
        }, //
        INPUTTEXT("p:inputText") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                return String.format("<p:inputText value=\"%1$s\" %2$s />", column.attrValue, column.otherAttrs);
            }
        }, //
        SELECTONEMENU("p:selectOneMenu") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                StringBuilder sb = new StringBuilder();
                sb.append(" <h:selectOneMenu value=\"%1$s\" %2$s>                               \n");
                sb.append("         <f:selectItem itemLabel=\"一\" itemValue=\"1\" />    \n");
                sb.append("         <f:selectItem itemLabel=\"二\" itemValue=\"2\" />    \n");
                sb.append("         <f:selectItem itemLabel=\"三\" itemValue=\"3\" />    \n");
                sb.append(" </h:selectOneMenu>                                       \n");
                return String.format(sb.toString(), column.attrValue, column.otherAttrs);
            }
        }, //
        SELECTONERADIO("p:selectOneRadio") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                StringBuilder sb = new StringBuilder();
                sb.append(" <p:selectOneRadio value=\"%1$s\" %2$s>                               \n");
                sb.append("         <f:selectItem itemLabel=\"一\" itemValue=\"1\" />    \n");
                sb.append("         <f:selectItem itemLabel=\"二\" itemValue=\"2\" />    \n");
                sb.append("         <f:selectItem itemLabel=\"三\" itemValue=\"3\" />    \n");
                sb.append(" </p:selectOneRadio>                                       \n");
                return String.format(sb.toString(), column.attrValue, column.otherAttrs);
            }
        }, //
        RIS_CALENDAR("ris:calendar") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                StringBuilder sb = new StringBuilder();
                System.out.println("column.attrValue == " + column.attrValue);
                sb.append("<ris:calendar styleClass=\"inputStyle\" showCalendar=\"true\" />\n");
                return String.format(sb.toString(), column.attrValue, column.otherAttrs);
            }
        }, //
        RIS_CALENDAR_DBL("ris:calendar(double)") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                StringBuilder sb = new StringBuilder();
                sb.append("<ris:calendar styleClass=\"inputStyle\" showCalendar=\"true\" />\n");
                sb.append("～<ris:calendar styleClass=\"inputStyle\" showCalendar=\"true\" />\n");
                return String.format(sb.toString(), column.attrValue, column.otherAttrs);
            }
        }, //
        YEAR_BETWEEN_INPUTTEXT("p:inputText(year between)") {
            String toXhtml(ColumnType column) {
                checkColumnType(column);
                StringBuilder sb = new StringBuilder();
                sb.append("<p:inputText value=\"%1$s\" maxlength=\"3\" size=\"3\" %2$s />\n");
                sb.append("～<p:inputText value=\"%1$s\" maxlength=\"3\" size=\"3\" %2$s />\n");
                return String.format(sb.toString(), column.attrValue, column.otherAttrs);
            }
        }, //
        ;//

        final String tag;

        JsfTag(String tag) {
            this.tag = tag;
        }

        void checkColumnType(ColumnType column) {
            column.attrValue = StringUtils.defaultString(column.attrValue);
            column.otherAttrs = StringUtils.defaultString(column.otherAttrs);
        }

        @Override
        public String toString() {
            return tag;
        }

        abstract String toXhtml(ColumnType column);
    }
}
