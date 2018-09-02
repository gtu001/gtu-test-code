package gtu._work.ui;

import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DebugGraphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

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
public class JSFMakerUI_attrDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = -4943104260161641351L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JButton confirmOk;
    private JTable attrTable;

    /**
     * Auto-generated main method to display this JDialog
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                JSFMakerUI_attrDialog inst = new JSFMakerUI_attrDialog(frame);
                inst.setVisible(true);
            }
        });
    }

    public JSFMakerUI_attrDialog(JFrame frame) {
        super(frame);
        initGUI();
        setLocationRelativeTo(frame);
    }

    private void initGUI() {
        try {
            final SwingActionUtil actionUtil = SwingActionUtil.newInstance(this);
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
                    jTabbedPane1.addTab("attribute", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(379, 233));
                        {
                            DefaultTableModel model = JTableUtil.createModel(true, "enable", "attribute", "value");
                            attrTable = new JTable();

                            //XXX defnine attribute
                            model.addRow(transfromAttribute(new Attribute("size", null, "", new String[] { "", "3", "5", "10", "15", "20" })));
                            model.addRow(transfromAttribute(new Attribute("readonly", null, "", new String[] { "", "false", "true" })));
                            model.addRow(transfromAttribute(new Attribute("styleClass", null, "", new String[] { "", "readonly-input" })));
                            //XXX defnine attribute

                            jScrollPane1.setViewportView(attrTable);
                            attrTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                            attrTable.setAutoscrolls(true);
                            attrTable.setAutoCreateRowSorter(false);
                            attrTable.setUpdateSelectionOnSort(false);
                            attrTable.setAutoCreateColumnsFromModel(true);
                            attrTable.setColumnSelectionAllowed(true);
                            attrTable.setModel(model);
                            attrTable.getTableHeader().setAutoscrolls(true);
                            attrTable.getTableHeader().setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
                            attrTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    actionUtil.invokeAction(evt);
                                }
                            });
                            attrTable.addKeyListener(JTableUtil.newInstance(attrTable).defaultKeyAdapter());
                        }
                    }
                    {
                        confirmOk = new JButton();
                        jPanel1.add(confirmOk, BorderLayout.SOUTH);
                        confirmOk.setText("OK");
                        confirmOk.setPreferredSize(new java.awt.Dimension(379, 32));
                        confirmOk.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                actionUtil.invokeAction(evt);
                            }
                        });
                    }
                }

                actionUtil.addAction(confirmOk, ActionEvent.class, new Action() {
                    public void action(EventObject evt) throws Exception {
                        dispose();
                        setVisible(false);
                    }
                });

                actionUtil.addAction(attrTable, MouseEvent.class, new Action() {
                    public void action(EventObject evt) throws Exception {
                        if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                            return;
                        }

                        int colPos = JTableUtil.newInstance(attrTable).getSelectedColumn();
                        int rowPos = JTableUtil.newInstance(attrTable).getSelectedRow();

                        Function func = Function.valueOf(colPos);
                        if (func == null) {
                            System.err.println("ERROR!!!!!");
                            return;
                        }

                        Attribute realAttr = (Attribute) JTableUtil.newInstance(attrTable).getModel().getValueAt(rowPos, Function.VALUE.col);

                        switch (func) {
                        case ENABLE:
                            boolean bool = (Boolean) JTableUtil.newInstance(attrTable).getModel().getValueAt(rowPos, Function.ENABLE.col);
                            JTableUtil.newInstance(attrTable).getModel().setValueAt(!bool, rowPos, Function.ENABLE.col);
                            break;
                        case ATTRIBUTE:
                            break;
                        case VALUE:
                            String value = (String) JOptionPaneUtil.newInstance().showInputDialog_drowdown("choice", "", realAttr.dropdown, realAttr.defaultVal);
                            realAttr.value = value;
                            JTableUtil.newInstance(attrTable).getModel().setValueAt(StringUtils.isNotEmpty(value), rowPos, Function.ENABLE.col);
                            break;
                        }
                    }
                });

            }
            setSize(400, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAttribute() {
        DefaultTableModel model = (DefaultTableModel) attrTable.getModel();
        Attribute attr = null;
        boolean enable = false;
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            attr = (Attribute) model.getValueAt(ii, Function.VALUE.col);
            enable = (Boolean) model.getValueAt(ii, Function.ENABLE.col);
            if (StringUtils.isNotEmpty(attr.value) && enable) {
                sb.append(String.format(" %s=\"%s\"", attr.name, attr.value));
            }
        }
        return sb.toString();
    }

    Object[] transfromAttribute(Attribute attribute) {
        return new Object[] { attribute.enable, attribute.name, attribute };
    }

    enum Function {
        ENABLE(0), //
        ATTRIBUTE(1), //
        VALUE(2), //
        ;//
        final int col;

        Function(int col) {
            this.col = col;
        }

        static Function valueOf(int col) {
            for (Function f : Function.values()) {
                if (f.col == col) {
                    return f;
                }
            }
            return null;
        }
    }

    static class Attribute {
        boolean enable;
        String name;
        String value;
        String defaultVal;
        String[] dropdown;

        public Attribute(String name, String value, String defaultVal, String[] dropdown) {
            super();
            this.name = name;
            this.value = value;
            this.defaultVal = defaultVal;
            this.dropdown = dropdown;
        }

        @Override
        public String toString() {
            return StringUtils.defaultString(value);
        }
    }
}
