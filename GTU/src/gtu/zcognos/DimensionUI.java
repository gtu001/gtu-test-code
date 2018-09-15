package gtu.zcognos;

import gtu.file.FileUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
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
public class DimensionUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField projectId;
    private JButton create;
    private JLabel jLabel1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DimensionUI inst = new DimensionUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
        System.out.println("done...");
    }

    public DimensionUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {

            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil.newInstance(this);
            {
                GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
                getContentPane().setLayout(thisLayout);
                this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                {
                    projectId = new JTextField();
                }
                {
                    jLabel1 = new JLabel();
                    jLabel1.setText("PROJECT_ID");
                }
                {
                    create = new JButton();
                    create.setText("create");
                    create.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("create.actionPerformed", evt);
                        }
                    });
                }
                {
                    reportId = new JTextField();
                }
                {
                    jLabel8 = new JLabel();
                    jLabel8.setText("report id");
                }
                {
                    addDimensionFromDb = new JButton();
                    addDimensionFromDb.setText("add from db");
                    addDimensionFromDb.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("addFromDb.actionPerformed", evt);
                        }
                    });
                }
                {
                    dataSourceTable = new JTextField();
                    dataSourceTable.setText("rscdpg0901");
                }
                {
                    jLabel7 = new JLabel();
                    jLabel7.setText("data source table");
                }
                {
                    addDimension = new JButton();
                    addDimension.setText("add");
                    addDimension.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("add.actionPerformed", evt);
                        }
                    });
                }
                {
                    dimensionName = new JTextField();
                }
                {
                    jLabel6 = new JLabel();
                    jLabel6.setText("dimension chinese name");
                }
                {
                    jLabel5 = new JLabel();
                    jLabel5.setText("rscdzzzz id index");
                }
                {
                    String[] idx = new String[20];
                    for (int ii = 0; ii < idx.length; ii++) {
                        idx[ii] = "id" + (ii + 1);
                    }
                    ComboBoxModel rscdzzzzIdIndexModel = new DefaultComboBoxModel(idx);
                    rscdzzzzIdIndex = new JComboBox();
                    rscdzzzzIdIndex.setModel(rscdzzzzIdIndexModel);
                }
                {
                    jLabel3 = new JLabel();
                    jLabel3.setText("Dimension");
                }
                {
                    jScrollPane1 = new JScrollPane();
                    {
                        DefaultListModel dimensionListModel = new DefaultListModel();
                        dimensionList = new JList();
                        jScrollPane1.setViewportView(dimensionList);
                        dimensionList.setModel(dimensionListModel);
                        dimensionList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                JListUtil.newInstance(dimensionList).defaultJListKeyPressed(evt);
                            }
                        });
                    }
                }
                {
                    exportDir = new JButton();
                    exportDir.setText("export dir");
                    exportDir.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("exportDir.actionPerformed", evt);
                        }
                    });
                }
                {
                    category = new JTextField();
                }
                {
                    jLabel4 = new JLabel();
                    jLabel4.setText("category");
                }
                {
                    tableName = new JTextField();
                }
                {
                    jLabel2 = new JLabel();
                    jLabel2.setText("merge table name");
                }
                thisLayout
                        .setHorizontalGroup(thisLayout
                                .createSequentialGroup()
                                .addContainerGap(12, 12)
                                .addGroup(
                                        thisLayout
                                                .createParallelGroup()
                                                .addComponent(jLabel3, GroupLayout.Alignment.LEADING,
                                                        GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                                                .addGroup(
                                                        thisLayout
                                                                .createSequentialGroup()
                                                                .addGap(19)
                                                                .addGroup(
                                                                        thisLayout
                                                                                .createParallelGroup()
                                                                                .addGroup(
                                                                                        thisLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        exportDir,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        119,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                        LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(
                                                                                                        addDimension,
                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                        119,
                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                        LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(
                                                                                                        thisLayout
                                                                                                                .createParallelGroup()
                                                                                                                .addComponent(
                                                                                                                        addDimensionFromDb,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        191,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(
                                                                                                                        thisLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(88)
                                                                                                                                .addComponent(
                                                                                                                                        create,
                                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                                        119,
                                                                                                                                        GroupLayout.PREFERRED_SIZE))))
                                                                                .addGroup(
                                                                                        thisLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(
                                                                                                        thisLayout
                                                                                                                .createParallelGroup()
                                                                                                                .addComponent(
                                                                                                                        jLabel6,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel5,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel4,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel8,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel2,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel7,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        jLabel1,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        196,
                                                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                                                .addGap(39)
                                                                                                .addGroup(
                                                                                                        thisLayout
                                                                                                                .createParallelGroup()
                                                                                                                .addComponent(
                                                                                                                        dimensionName,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        rscdzzzzIdIndex,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        category,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        reportId,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        tableName,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        dataSourceTable,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(
                                                                                                                        projectId,
                                                                                                                        GroupLayout.Alignment.LEADING,
                                                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                                                        204,
                                                                                                                        GroupLayout.PREFERRED_SIZE)))
                                                                                .addComponent(jScrollPane1,
                                                                                        GroupLayout.Alignment.LEADING,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        436, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(11, 11));
                thisLayout.setVerticalGroup(thisLayout
                        .createSequentialGroup()
                        .addContainerGap(12, 12)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectId, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dataSourceTable, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(tableName, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(reportId, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(category, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup()
                                        .addComponent(jLabel5, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(rscdzzzzIdIndex, GroupLayout.Alignment.LEADING,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dimensionName, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addDimension, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exportDir, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addDimensionFromDb, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addGap(11)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
                        .addGap(12)
                        .addComponent(create, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE).addContainerGap(9, 9));
            }
            this.setSize(513, 632);

            swingUtil.addAction("exportDir.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog()
                            .getApproveSelectedFile();
                    if (file != null) {
                        baseDir = file;
                    }
                }
            });

            swingUtil.addAction("addFromDb.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    String project = projectId.getText();
                    Validate.notEmpty(project, "projectId is null");
                    Validate.notEmpty(dataSourceTable.getText(), "dataSourceTable is null");

                    DefaultListModel model = (DefaultListModel) dimensionList.getModel();
                    for (Dimension_ ddd : InformixDbConn.queryGetDaminsion(dataSourceTable.getText(), project)) {
                        model.addElement(ddd);
                    }
                }
            });

            swingUtil.addAction("add.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    // Validate.notEmpty(tableName.getText(),
                    // "tableName is null");
                    Validate.notEmpty(category.getText(), "category is null");
                    Validate.notEmpty(dimensionName.getText(), "dimensionName is null");
                    Validate.notEmpty(dataSourceTable.getText(), "dataSourceTable is null");
                    // Validate.notEmpty(reportId.getText(),
                    // "reportId is null");

                    String report_id = StringUtils.defaultString(reportId.getText(), projectId.getText());

                    String tName = tableName.getText();
                    if (StringUtils.isEmpty(tName)) {
                        tName = randomTableName();
                    }

                    DefaultListModel model = (DefaultListModel) dimensionList.getModel();
                    model.addElement(new Dimension_(dataSourceTable.getText(), tName, category.getText(),
                            (String) rscdzzzzIdIndex.getSelectedItem(), dimensionName.getText(), report_id));
                }
            });

            swingUtil.addAction("create.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {

                    String project = projectId.getText();
                    Validate.notEmpty(project, "projectId is null");
                    Validate.notNull(baseDir, "exportDir is null");

                    File destDir = new File(baseDir, project);

                    Map<String, Object> map = new HashMap<String, Object>();

                    List<Map<String, String>> llist = new ArrayList<Map<String, String>>();

                    int idx = 2;
                    int categoryId = 1;
                    DefaultListModel model = (DefaultListModel) dimensionList.getModel();
                    for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                        Dimension_ di = (Dimension_) enu.nextElement();

                        Map mmm = new HashMap();
                        mmm.put("rscdpg0901", di.dataSourceTable);//
                        mmm.put("rscdpg0901a", di.tableName);//
                        mmm.put("rscdpg0901a_category", di.category);//
                        mmm.put("rscdpg0901a_report_id", di.reportId);//
                        mmm.put("rscdzzzz_id", di.idIndex);//
                        mmm.put("rscdpg0901a_dname", di.dimensionName);//
                        llist.add(mmm);
                    }

                    map.put("PROJECT_ID", project);
                    map.put("RSCDPG0901", llist);
                    map.put("rscdpg0901", dataSourceTable.getText());

                    ConfigCopy.getInstance().applyBaseDir(baseDir).applyProjectId(project).execute();

                    Dimension.getInstance()//
                            .applyDestDir(destDir.getAbsolutePath())//
                            .applyParameter(map)//
                            .execute();

                    JOptionPaneUtil.newInstance().iconInformationMessage()
                            .showMessageDialog(project + " create completed!!\r\n dir : " //
                                    + destDir.getAbsolutePath(), project);

                    Desktop.getDesktop().open(destDir);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String randomTableName() {
        return "rscd" + StringUtils.leftPad(String.valueOf((int) (Math.random() * 10000)), 4, "0");
    }

    File baseDir = new File(FileUtil.DESKTOP_PATH);

    static class Dimension_ {
        String dataSourceTable;
        String tableName;
        String category;
        String idIndex;
        String dimensionName;
        String reportId;

        public Dimension_() {
            super();
        }

        public Dimension_(String dataSourceTable, String tableName, String category, String idIndex,
                String dimensionName, String reportId) {
            super();
            this.dataSourceTable = dataSourceTable;
            this.tableName = tableName;
            this.category = category;
            this.idIndex = idIndex;
            this.dimensionName = dimensionName;
            this.reportId = reportId;
        }

        @Override
        public String toString() {
            return "Dimension [tableName=" + tableName + ", category=" + category + ", idIndex=" + idIndex
                    + ", dimensionName=" + dimensionName + "]";
        }
    }

    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JScrollPane jScrollPane1;
    private JLabel jLabel8;
    private JTextField reportId;
    private JLabel jLabel7;
    private JTextField dataSourceTable;
    private JButton exportDir;
    private JLabel jLabel6;
    private JButton addDimension;
    private JList dimensionList;
    private JTextField dimensionName;
    private JComboBox rscdzzzzIdIndex;
    private JTextField category;
    private JTextField tableName;
    private JButton addDimensionFromDb;
}
