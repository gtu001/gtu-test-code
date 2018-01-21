package gtu._work.janna.xml;

import gtu._work.janna.xml.JavaBeanToXml.DsName;
import gtu._work.janna.xml.JavaBeanToXml.Field_;
import gtu._work.janna.xml.JavaBeanToXml.Operator;
import gtu._work.janna.xml.JavaBeanToXml.OrderBy;
import gtu._work.janna.xml.JavaBeanToXml.Orderfield;
import gtu._work.janna.xml.JavaBeanToXml.Table;
import gtu._work.janna.xml.JavaBeanToXml.Where;
import gtu._work.janna.xml.JavaBeanToXml.Xml;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
public class CreateHibernateXmlUI extends javax.swing.JFrame {

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
                CreateHibernateXmlUI inst = new CreateHibernateXmlUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public CreateHibernateXmlUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        fireChangeList = new ArrayList<FieldMatch<?>>();
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setTitle("hibernate xml creater");
            {
                jScrollPane2 = new JScrollPane();
                {
                    area = new JTextArea();
                    jScrollPane2.setViewportView(area);
                }
            }
            {
                ordertype = new JComboBox();
                FieldMatch<Orderfield_> fieldMatch = new FieldMatch<Orderfield_>(Orderfield_.class, "ordertype",
                        ordertype) {
                    @Override
                    Orderfield_ getVal() {
                        return getOrderfieldObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "asc", "desc" });
                ordertype.setModel(jComboBox1Model);
                ordertype.addActionListener(fieldMatch);
            }
            {
                jLabel1 = new JLabel();
                jLabel1.setText("table");
            }
            {
                tableName = new JTextField();

                FieldMatch<Table_> fieldMatch = new FieldMatch<Table_>(Table_.class, "name", tableName) {
                    @Override
                    Table_ getVal() {
                        return getTableObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                tableName.getDocument().addDocumentListener(fieldMatch);

            }
            {
                pretable = new JTextField();

                FieldMatch<Table_> fieldMatch = new FieldMatch<Table_>(Table_.class, "pretable", pretable) {
                    @Override
                    Table_ getVal() {
                        return getTableObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                pretable.getDocument().addDocumentListener(fieldMatch);
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setText("pretable");
            }
            {
                dsName = new JTextField();
                FieldMatch<DsName_> fieldMatch = new FieldMatch<DsName_>(DsName_.class, "name", dsName) {
                    @Override
                    DsName_ getVal() {
                        return getDsNameObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                dsName.getDocument().addDocumentListener(fieldMatch);
            }
            {
                prefield = new JTextField();
                FieldMatch<Field__> fieldMatch = new FieldMatch<Field__>(Field__.class, "prefield", prefield) {
                    @Override
                    Field__ getVal() {
                        return getFieldObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                prefield.getDocument().addDocumentListener(fieldMatch);
            }
            {
                jLabel9 = new JLabel();
                jLabel9.setText("ds name");
            }
            {
                jLabel10 = new JLabel();
                jLabel10.setText("type");
            }
            {
                type = new JComboBox();

                FieldMatch<DsName_> fieldMatch = new FieldMatch<DsName_>(DsName_.class, "type", type) {
                    @Override
                    DsName_ getVal() {
                        return getDsNameObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "select" });
                type.setModel(jComboBox1Model);
                type.addActionListener(fieldMatch);
            }
            {
                operation = new JComboBox();

                FieldMatch<Field__> fieldMatch = new FieldMatch<Field__>(Field__.class, "operation", operation) {
                    @Override
                    Field__ getVal() {
                        return getFieldObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(new String[] { "eq", "ge", "le" });
                operation.setModel(jComboBox2Model);
                operation.addActionListener(fieldMatch);
            }
            {
                operator = new JComboBox();

                FieldMatch<Operator_> fieldMatch = new FieldMatch<Operator_>(Operator_.class, "between", operator) {
                    @Override
                    Operator_ getVal() {
                        return getOperatorObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "and", "or" });
                operator.setModel(jComboBox1Model);
                operator.addActionListener(fieldMatch);
            }
            {
                prerelation = new JComboBox();
                FieldMatch<Operator_> fieldMatch = new FieldMatch<Operator_>(Operator_.class, "prerelation",
                        prerelation) {
                    @Override
                    Operator_ getVal() {
                        return getOperatorObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "and", "or" });
                prerelation.setModel(jComboBox1Model);
                prerelation.addActionListener(fieldMatch);
            }
            {
                jLabel3 = new JLabel();
                jLabel3.setText("operator");
            }
            {
                field = new JTextField();

                FieldMatch<Field__> fieldMatch = new FieldMatch<Field__>(Field__.class, "name", field) {
                    @Override
                    Field__ getVal() {
                        return getFieldObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                field.getDocument().addDocumentListener(fieldMatch);
            }
            {
                jLabel4 = new JLabel();
                jLabel4.setText("field ");
            }
            {
                jScrollPane1 = new JScrollPane();
                {
                    DefaultTreeModel defaultTreeModel = new DefaultTreeModel(null);
                    tree = new JTree();
                    jScrollPane1.setViewportView(tree);
                    tree.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent evt) {
                            treeMouseClicked(evt);
                        }
                    });
                    tree.setModel(defaultTreeModel);
                }
            }
            {
                jLabel5 = new JLabel();
                jLabel5.setText("operation");
            }
            {
                jLabel6 = new JLabel();
                jLabel6.setText("prerelation");
            }
            {
                jLabel7 = new JLabel();
                jLabel7.setText("ordertype");
            }
            {
                orderfield = new JTextField();

                FieldMatch<Orderfield_> fieldMatch = new FieldMatch<Orderfield_>(Orderfield_.class, "name", orderfield) {
                    @Override
                    Orderfield_ getVal() {
                        return getOrderfieldObject();
                    }
                };
                fireChangeList.add(fieldMatch);
                orderfield.getDocument().addDocumentListener(fieldMatch);
            }
            {
                jLabel8 = new JLabel();
                jLabel8.setText("orderfield");
            }
            {
                jLabel11 = new JLabel();
                jLabel11.setText("prefield");
            }
            thisLayout
                    .setVerticalGroup(thisLayout
                            .createSequentialGroup()
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(type, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dsName, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(pretable, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tableName, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(operator, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(prerelation, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(operation, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(field, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(prefield, GroupLayout.Alignment.BASELINE,
                                                                    GroupLayout.PREFERRED_SIZE, 24,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel11, GroupLayout.Alignment.BASELINE,
                                                                    GroupLayout.PREFERRED_SIZE, 17,
                                                                    GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(ordertype, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(orderfield, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, GroupLayout.Alignment.BASELINE,
                                                    GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup()
                                            .addGroup(
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE,
                                                                    200, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE,
                                                                    193, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 7, Short.MAX_VALUE))).addContainerGap());
            thisLayout
                    .setHorizontalGroup(thisLayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                    thisLayout
                                            .createParallelGroup()
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE,
                                                                    231, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE,
                                                                    429, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(
                                                    GroupLayout.Alignment.LEADING,
                                                    thisLayout
                                                            .createSequentialGroup()
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addGroup(
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addGroup(
                                                                                                    thisLayout
                                                                                                            .createParallelGroup()
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addPreferredGap(
                                                                                                                                    jScrollPane1,
                                                                                                                                    jLabel4,
                                                                                                                                    LayoutStyle.ComponentPlacement.INDENT)
                                                                                                                            .addComponent(
                                                                                                                                    jLabel4,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    69,
                                                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    jLabel3,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    54,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(27))
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    jLabel1,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    54,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(27))
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    jLabel9,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    65,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(16)))
                                                                                            .addGap(7)
                                                                                            .addGroup(
                                                                                                    thisLayout
                                                                                                            .createParallelGroup()
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    orderfield,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    100,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(11))
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    field,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    100,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(11))
                                                                                                            .addComponent(
                                                                                                                    operator,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                    111,
                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    tableName,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    100,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(11))
                                                                                                            .addGroup(
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    thisLayout
                                                                                                                            .createSequentialGroup()
                                                                                                                            .addComponent(
                                                                                                                                    dsName,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    100,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addGap(11))))
                                                                            .addGroup(
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    thisLayout
                                                                                            .createSequentialGroup()
                                                                                            .addComponent(
                                                                                                    jLabel8,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    93,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addGap(106)))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addComponent(jLabel7,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 69,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel5,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 69,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel6,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 69,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel2,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 69,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(jLabel10,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 69,
                                                                                    GroupLayout.PREFERRED_SIZE))
                                                            .addGap(8)
                                                            .addGroup(
                                                                    thisLayout
                                                                            .createParallelGroup()
                                                                            .addComponent(ordertype,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 100,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(operation,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 100,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(prerelation,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 100,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(pretable,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 100,
                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(type,
                                                                                    GroupLayout.Alignment.LEADING,
                                                                                    GroupLayout.PREFERRED_SIZE, 100,
                                                                                    GroupLayout.PREFERRED_SIZE))
                                                            .addGap(17)
                                                            .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 69,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(8)
                                                            .addComponent(prefield, GroupLayout.PREFERRED_SIZE, 100,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 84, Short.MAX_VALUE))).addContainerGap());
            this.setSize(712, 409);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init() {
        cdsName = new DsName_();
        ctable = new Table_();
        cfield = new Field__();
        cwhere = new Where_();
        // corderBy = new OrderBy_();
        ctable.where = cwhere;
        // ctable.orderBy = corderBy;
        coperator = new Operator_();
        // corderfield = new Orderfield_();
        cdsName.tables.add(ctable);
        cwhere.operators.add(coperator);
        // corderBy.orderfields.add(corderfield);
        coperator.fields.add(cfield);
        System.out.println("=======1");
        reloadTree();
        System.out.println("=======2");
        reloadXml();
        System.out.println("=======3");
        for (FieldMatch<?> match : fireChangeList) {
            match.setEnable(false);
        }
        System.out.println("=======4");
    }

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel10;
    private JLabel jLabel9;
    private JTree tree;
    private JLabel jLabel7;
    private JLabel jLabel6;
    private JPopupMenu jPopupMenu1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane1;
    private JLabel jLabel8;
    private JLabel jLabel11;

    private JComboBox type;
    private JComboBox operation;
    private JComboBox operator;
    private JComboBox ordertype;
    private JComboBox prerelation;

    private JTextField prefield;
    private JTextField dsName;
    private JTextField tableName;
    private JTextField pretable;
    private JTextField orderfield;
    private JTextField field;

    private JTextArea area;

    Table_ ctable;
    Field__ cfield;
    DsName_ cdsName;
    Where_ cwhere;
    Operator_ coperator;
    OrderBy_ corderBy;
    Orderfield_ corderfield;

    List<FieldMatch<?>> fireChangeList;

    Orderfield_ getOrderfieldObject() {
        return corderfield;
    }

    Field__ getFieldObject() {
        return cfield;
    }

    Table_ getTableObject() {
        return ctable;
    }

    DsName_ getDsNameObject() {
        return cdsName;
    }

    Operator_ getOperatorObject() {
        return coperator;
    }

    JavaBeanToXml xmlCreater = new JavaBeanToXml();

    void reloadXml() {
        Xml xml = new Xml();
        try {
            xmlCreater.createXml(cdsName, xml);
            area.setText(xml.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void reloadTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(cdsName);
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(root);
        DefaultMutableTreeNode t1 = null;
        DefaultMutableTreeNode w1 = null;
        DefaultMutableTreeNode ob1 = null;
        DefaultMutableTreeNode ot1 = null;
        DefaultMutableTreeNode of1 = null;
        DefaultMutableTreeNode f1 = null;
        for (Table t : cdsName.tables) {
            t1 = new DefaultMutableTreeNode(t);
            root.add(t1);
            if (t.where != null) {
                w1 = new DefaultMutableTreeNode(t.where);
                for (Operator ot : t.where.operators) {
                    ot1 = new DefaultMutableTreeNode(ot);
                    w1.add(ot1);
                    for (Field_ f : ot.fields) {
                        f1 = new DefaultMutableTreeNode(f);
                        ot1.add(f1);
                    }
                }
                t1.add(w1);
            }
            if (t.orderBy != null) {
                ob1 = new DefaultMutableTreeNode(t.orderBy);
                for (Orderfield of : t.orderBy.orderfields) {
                    of1 = new DefaultMutableTreeNode(of);
                    ob1.add(of1);
                }
                t1.add(ob1);
            }
        }
        tree.setModel(defaultTreeModel);
    }

    private void treeMouseClicked(MouseEvent evt) {
        if (evt.getButton() == 1) {
            currentPropSet();
        }
        if (evt.getButton() == 3) {
            treePopupSet();
            jPopupMenu1.show(tree, evt.getX(), evt.getY());
        }
    }

    void currentPropSet() {
        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (treeNode == null) {
            return;
        }
        final Object obj = treeNode.getUserObject();
        if (obj instanceof Table_) {
            ctable = (Table_) obj;
        }
        if (obj instanceof Field__) {
            cfield = (Field__) obj;
        }
        if (obj instanceof Operator_) {
            coperator = (Operator_) obj;
        }
        if (obj instanceof Where_) {
            cwhere = (Where_) obj;
        }
        if (obj instanceof DsName_) {
            cdsName = (DsName_) obj;
        }
        if (obj instanceof OrderBy_) {
            corderBy = (OrderBy_) obj;
        }
        if (obj instanceof Orderfield_) {
            corderfield = (Orderfield_) obj;
        }
        for (FieldMatch<?> match : fireChangeList) {
            match.setObjectToJComponent(obj.getClass());
        }
    }

    @SuppressWarnings("serial")
    private void treePopupSet() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        final Object treeObject = treeNode.getUserObject();
        DefaultMutableTreeNode parentNode = (!treeNode.isRoot() ? (DefaultMutableTreeNode) treeNode.getParent() : null);
        final Object parentObject = (!treeNode.isRoot() ? parentNode.getUserObject() : null);

        class Menu extends JMenuItem {
            private static final long serialVersionUID = 1L;

            Menu(final String name) {
                this(name, true);
            }

            Menu(final String name, final boolean enable) {
                this.setText(name);
                this.setEnabled(enable);
                this.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        execute(paramActionEvent);
                        reloadXml();
                        reloadTree();
                    }
                });
            }

            void execute(ActionEvent paramActionEvent) {
            }
        }
        List<JMenuItem> list = new ArrayList<JMenuItem>();

        if (treeObject instanceof Table_) {
            list.add(new Menu("Table", false));
            list.add(new Menu("new Where") {
                void execute(ActionEvent paramActionEvent) {
                    ((Table_) treeObject).where = new Where_();
                }
            });
            list.add(new Menu("new OrderBy") {
                void execute(ActionEvent paramActionEvent) {
                    ((Table_) treeObject).orderBy = new OrderBy_();
                }
            });
        }
        if (treeObject instanceof Field__) {
            list.add(new Menu("Field", false));
            list.add(new Menu("del Self") {
                void execute(ActionEvent paramActionEvent) {
                    ((Operator_) parentObject).fields.remove(treeObject);
                }
            });
        }
        if (treeObject instanceof Operator_) {
            list.add(new Menu("Operator", false));
            list.add(new Menu("add Field") {
                void execute(ActionEvent paramActionEvent) {
                    ((Operator_) treeObject).fields.add(new Field__());
                }
            });
            list.add(new Menu("del Self") {
                void execute(ActionEvent paramActionEvent) {
                    ((Where_) parentObject).operators.remove(treeObject);
                }
            });
        }
        if (treeObject instanceof Where_) {
            list.add(new Menu("Where", false));
            list.add(new Menu("add Operator") {
                void execute(ActionEvent paramActionEvent) {
                    ((Where_) treeObject).operators.add(new Operator_());
                }
            });
            list.add(new Menu("del self") {
                void execute(ActionEvent paramActionEvent) {
                    ((Table_) parentObject).where = null;
                }
            });
        }
        if (treeObject instanceof DsName_) {
            list.add(new Menu("DsName", false));
            list.add(new Menu("new Table") {
                void execute(ActionEvent paramActionEvent) {
                    ((DsName_) treeObject).tables.add(newTable());
                }
            });
        }

        if (treeObject instanceof OrderBy_) {
            list.add(new Menu("OrderBy", false));
            list.add(new Menu("add Orderfield") {
                void execute(ActionEvent paramActionEvent) {
                    ((OrderBy_) treeObject).orderfields.add(new Orderfield_());
                }
            });
            list.add(new Menu("del Self") {
                void execute(ActionEvent paramActionEvent) {
                    ((Table_) parentObject).orderBy = null;
                }
            });
        }
        if (treeObject instanceof Orderfield_) {
            list.add(new Menu("Orderfield", false));
            list.add(new Menu("del Self") {
                void execute(ActionEvent paramActionEvent) {
                    ((OrderBy_) parentObject).orderfields.remove(treeObject);
                }
            });
        }
        jPopupMenu1 = new JPopupMenu();
        for (JMenuItem menu : list) {
            jPopupMenu1.add(menu);
        }
    }

    abstract class FieldMatch<T> implements DocumentListener, ActionListener {
        Class<T> clz;
        String fieldname;
        String text;
        JComponent component;

        void setEnable(boolean enable) {
            component.setEnabled(enable);
        }

        FieldMatch(Class<T> clz, String fieldname, JComponent component) {
            this.clz = clz;
            this.fieldname = fieldname;
            this.component = component;
        }

        abstract T getVal();

        void setJComponentToObject() {
            try {
                Class<? super T> clzs = clz.getSuperclass();
                Field fid = clzs.getDeclaredField(fieldname);
                fid.set(getVal(), text);
                System.out.println(fid.getName() + " ... " + text);
                reloadXml();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void setObjectToJComponent(Class<?> clz) {
            try {
                if (this.clz != clz) {
                    this.component.setEnabled(false);
                } else {
                    this.component.setEnabled(true);
                    Class<?> clzs = clz.getSuperclass();
                    Field fid = clzs.getDeclaredField(fieldname);
                    Object value = fid.get(getVal());
                    if (component instanceof JComboBox) {
                        JComboBox combox = (JComboBox) component;
                        combox.setSelectedItem(value);
                        System.out.println("combox = " + fid.getName() + " = " + value);
                    }
                    if (component instanceof JTextField) {
                        JTextField textField = (JTextField) component;
                        textField.setText((String) value);
                        System.out.println("text = " + fid.getName() + " = " + value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void getDocText(DocumentEvent doc) {
            try {
                text = doc.getDocument().getText(0, doc.getDocument().getLength());
            } catch (BadLocationException e) {
            }
        }

        public void insertUpdate(DocumentEvent paramDocumentEvent) {
            getDocText(paramDocumentEvent);
            setJComponentToObject();
        }

        public void removeUpdate(DocumentEvent paramDocumentEvent) {
            getDocText(paramDocumentEvent);
            setJComponentToObject();
        }

        public void changedUpdate(DocumentEvent paramDocumentEvent) {
            getDocText(paramDocumentEvent);
            setJComponentToObject();
        }

        public void actionPerformed(ActionEvent paramActionEvent) {
            JComboBox combox = (JComboBox) paramActionEvent.getSource();
            text = (String) combox.getSelectedItem();
            setJComponentToObject();
        }
    }

    static class Table_ extends Table {
        public String toString() {
            return "Table : " + this.name;
        }
    }

    static class Field__ extends Field_ {
        public String toString() {
            return "Field : " + this.name;
        }
    }

    static class DsName_ extends DsName {
        public String toString() {
            return "DsName : " + this.name;
        }
    }

    static class Where_ extends Where {
        public String toString() {
            return "Where : " + this.operators.size();
        }
    }

    static class Operator_ extends Operator {
        public String toString() {
            return "Operator : " + this.fields.size();
        }
    }

    static class OrderBy_ extends OrderBy {
        public String toString() {
            return "OrderBy : " + this.orderfields.size();
        }
    }

    static class Orderfield_ extends Orderfield {
        public String toString() {
            return "Orderfield : " + this.name;
        }
    }

    Table_ newTable() {
        Table_ t = new Table_();
        Field__ f = new Field__();
        Where_ w = new Where_();
        // OrderBy_ ob = new OrderBy_();
        t.where = w;
        // t.orderBy = ob;
        Operator_ ct = new Operator_();
        Orderfield_ cf = new Orderfield_();
        w.operators.add(ct);
        // ob.orderfields.add(cf);
        ct.fields.add(f);
        return t;
    }
}
