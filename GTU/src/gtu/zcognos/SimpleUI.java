package gtu.zcognos;

import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
public class SimpleUI extends javax.swing.JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField projectId;
    private JTextField packageId;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JButton create;
    private JTextArea dataTable;
    private JLabel jLabel1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleUI inst = new SimpleUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
        System.out.println("done...");
    }

    public SimpleUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {

            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil
                    .newInstance(this);
            {
                GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
                getContentPane().setLayout(thisLayout);
                this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                {
                    projectId = new JTextField();
                }
                {
                    packageId = new JTextField();
                }
                {
                    jLabel1 = new JLabel();
                    jLabel1.setText("PROJECT_ID");
                }
                {
                    jLabel2 = new JLabel();
                    jLabel2.setText("PACKAGE_ID");
                }
                {
                    jLabel3 = new JLabel();
                    jLabel3.setText("DATATABLE");
                }
                {
                    jScrollPane1 = new JScrollPane();
                    {
                        dataTable = new JTextArea();
                        jScrollPane1.setViewportView(dataTable);
                    }
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
                thisLayout.setVerticalGroup(thisLayout
                        .createSequentialGroup()
                        .addContainerGap(16, 16)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectId, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup()
                                        .addGroup(
                                                GroupLayout.Alignment.LEADING,
                                                thisLayout
                                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(packageId, GroupLayout.Alignment.BASELINE,
                                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2, GroupLayout.Alignment.BASELINE,
                                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addGroup(
                                                GroupLayout.Alignment.LEADING,
                                                thisLayout
                                                        .createSequentialGroup()
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0,
                                                                Short.MAX_VALUE)
                                                        .addComponent(create, GroupLayout.PREFERRED_SIZE, 22,
                                                                GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap());
                thisLayout.setHorizontalGroup(thisLayout
                        .createSequentialGroup()
                        .addContainerGap(31, 31)
                        .addGroup(
                                thisLayout
                                        .createParallelGroup()
                                        .addGroup(
                                                GroupLayout.Alignment.LEADING,
                                                thisLayout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                                thisLayout
                                                                        .createParallelGroup()
                                                                        .addComponent(jLabel2,
                                                                                GroupLayout.Alignment.LEADING,
                                                                                GroupLayout.PREFERRED_SIZE, 103,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel1,
                                                                                GroupLayout.Alignment.LEADING,
                                                                                GroupLayout.PREFERRED_SIZE, 103,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel3,
                                                                                GroupLayout.Alignment.LEADING,
                                                                                GroupLayout.PREFERRED_SIZE, 103,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                                thisLayout
                                                                        .createParallelGroup()
                                                                        .addComponent(packageId,
                                                                                GroupLayout.Alignment.LEADING,
                                                                                GroupLayout.PREFERRED_SIZE, 204,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(projectId,
                                                                                GroupLayout.Alignment.LEADING,
                                                                                GroupLayout.PREFERRED_SIZE, 204,
                                                                                GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(create, GroupLayout.PREFERRED_SIZE, 88,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0,
                                                                Short.MAX_VALUE))
                                        .addGroup(
                                                thisLayout
                                                        .createSequentialGroup()
                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 417,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))).addContainerGap(17, 17));
            }
            this.setSize(473, 356);

            swingUtil.addAction("create.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    String project = projectId.getText();
                    String dataTab = dataTable.getText();
                    Validate.notEmpty(project);
                    Validate.notEmpty(dataTab);
                    String pkgId = StringUtils.defaultIfEmpty(packageId.getText(), project);

                    List<String> dataTableList = new ArrayList<String>();
                    StringTokenizer tok = new StringTokenizer(dataTab);
                    for (int ii = 0; tok.hasMoreElements(); ii++) {
                        String column = (String) tok.nextElement();
                        System.out.format("%d -- %s\n", ii, column);
                        if (StringUtils.isBlank(column)) {
                            continue;
                        }
                        dataTableList.add(column.trim());
                    }

                    File destDir = new File(CREATE_DEST + "\\" + project);
                    destDir.mkdirs();

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("PROJECT_ID", project);
                    map.put("PACKAGE_ID", pkgId);
                    map.put("DATATABLE", dataTableList);

                    ConfigCopy.getInstance().applyBaseDir(new File(CREATE_DEST)).applyProjectId(project).execute();

                    Simple.getInstance()//
                            .applyDestDir(destDir.getAbsolutePath())//
                            .applyParameter(map)//
                            .execute();

                    JOptionPaneUtil.newInstance().iconInformationMessage()
                            .showMessageDialog(pkgId + " create completed!!\r\n dir : " //
                                    + destDir.getAbsolutePath(), pkgId);

                    Desktop.getDesktop().open(destDir);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String CREATE_DEST = "C:/Users/Troy/Desktop/";
}
