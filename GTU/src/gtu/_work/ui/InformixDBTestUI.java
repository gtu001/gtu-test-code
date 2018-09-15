package gtu._work.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.sql.PooledConnection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.informix.jdbcx.IfxConnectionPoolDataSource;

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
public class InformixDBTestUI extends javax.swing.JFrame {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3934516700796954816L;

    private JTextArea tableName;
    private JButton query;
    private JList tableResult;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InformixDBTestUI inst = new InformixDBTestUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public InformixDBTestUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                ListModel tableResultModel = new DefaultComboBoxModel();
                tableResult = new JList();
                tableResult.setModel(tableResultModel);
            }
            {
                tableName = new JTextArea();
            }
            {
                query = new JButton();
                query.setText("query");
                query.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        List<String> tableNameList = new ArrayList<String>();
                        StringTokenizer token = new StringTokenizer(tableName.getText());
                        while (token.hasMoreElements()) {
                            String value = (String) token.nextElement();
                            tableNameList.add(value.trim().toLowerCase());
                        }

                        Connection conn = getConnection();

                        StringBuilder sb = new StringBuilder();
                        sb.append("   SELECT count(*)                               ");
                        sb.append("   FROM \"informix\".systables t where t.tabname  ");
                        sb.append(" like                                        ");

                        DefaultComboBoxModel model = new DefaultComboBoxModel();
                        ResultSet rs = null;
                        Statement pstmt = null;
                        for (String table : tableNameList) {
                            try {
                                pstmt = conn.createStatement();
                                rs = pstmt.executeQuery(sb + "'%" + table + "%'");
                                if (rs.next()) {
                                    model.addElement(table + "---->" + rs.getInt(1));
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            pstmt.close();
                            rs.close();
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            pstmt = null;
                            rs = null;
                            conn = null;
                        }
                        tableResult.setModel(model);
                    }
                });
            }
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addComponent(tableName, GroupLayout.Alignment.LEADING, 0, 79, Short.MAX_VALUE)
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGap(55)
                                                    .addComponent(query, GroupLayout.PREFERRED_SIZE,
                                                            GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tableResult, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap(18, 18)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout.createSequentialGroup()
                                                    .addComponent(tableName, 0, 342, Short.MAX_VALUE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(query, 0, 84, Short.MAX_VALUE))
                                    .addComponent(tableResult, GroupLayout.Alignment.LEADING, 0, 432, Short.MAX_VALUE))
                    .addContainerGap());
            this.setSize(478, 328);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ThreadLocal<PooledConnection> DBConPool = new ThreadLocal<PooledConnection>() {
        @Override
        protected PooledConnection initialValue() {
            IfxConnectionPoolDataSource cpds;
            try {
                cpds = new IfxConnectionPoolDataSource();
                cpds.setDescription("Pick-A-Seat Connection pool");
                cpds.setIfxCPMMaxConnections(-1);
                cpds.setIfxCPMMaxPoolSize(10);
                cpds.setIfxCPMMinPoolSize(5);
                // common
                cpds.setIfxIFXHOST("192.168.10.11");
                cpds.setPortNumber(4546);
                cpds.setUser("risappl");
                cpds.setPassword("risappl");
                cpds.setServerName("ibm_11");
                cpds.setDatabaseName("teun0020");
                cpds.setIfxCLIENT_LOCALE("zh_tw.utf8");
                cpds.setIfxDB_LOCALE("zh_tw.utf8");
                return cpds.getPooledConnection();
            } catch (Exception sqle) {
                System.out.println("Get ConnectionPool Error : " + sqle.getMessage());
                return null;
            }
        }
    };

    public static synchronized Connection getConnection() {
        try {
            return InformixDBTestUI.DBConPool.get().getConnection();
        } catch (SQLException ex) {
            System.out.println("Get DB Connection Error : " + ex.getMessage() + ex.getErrorCode());
            return null;
        }
    }
}
