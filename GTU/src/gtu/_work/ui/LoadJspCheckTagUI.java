package gtu._work.ui;

import gtu._work.category.LoadJspCheckTag;
import gtu._work.category.LoadJspCheckTag.Tag;
import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang3.Validate;

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
public class LoadJspCheckTagUI extends javax.swing.JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoadJspCheckTagUI inst = new LoadJspCheckTagUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public LoadJspCheckTagUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
        	BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setTitle("\u8b80\u53d6Jsp\u8cc7\u8a0a");
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
            	jTabbedPane1 = new JTabbedPane();
            	getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
            	{
            		jPanel1 = new JPanel();
            		BorderLayout jPanel1Layout = new BorderLayout();
            		jPanel1.setLayout(jPanel1Layout);
            		jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
            		{
            			jScrollPane1 = new JScrollPane();
            			jPanel1.add(jScrollPane1, BorderLayout.CENTER);
            			jScrollPane1.setPreferredSize(new java.awt.Dimension(475, 328));
            			{
            				jTree1 = new JTree();
            				jScrollPane1.setViewportView(jTree1);
            				jTree1.setModel(null);            			
            				jTree1.addMouseListener(new MouseAdapter() {
            					public void mouseClicked(MouseEvent evt) {
            						jTree1MouseClicked(evt);
            					}
            				});
            				jTree1.addTreeSelectionListener(new TreeSelectionListener() {
            					public void valueChanged(TreeSelectionEvent evt) {
            						jTree1ValueChanged(evt);
            					}
            				});
            			}
            		}
            	}
            	{
            		jPanel2 = new JPanel();
            		jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
            		{
            			subFileNameText = new JTextField();
            			jPanel2.add(subFileNameText);
            			subFileNameText.setPreferredSize(new java.awt.Dimension(95, 24));
            			subFileNameText.setText("xhtml");
            		}
            		{
            			ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "不可改", "可改" });
            			modifyFileBox = new JComboBox();
            			jPanel2.add(modifyFileBox);
            			modifyFileBox.setModel(jComboBox1Model);
            		}
            		{
            			exportReportToogleBtn = new JToggleButton();
            			jPanel2.add(exportReportToogleBtn);
            			exportReportToogleBtn.setText("\u662f\u5426\u532f\u51fa\u5831\u8868");
            			exportReportToogleBtn.setPreferredSize(new java.awt.Dimension(120, 24));
            			exportReportToogleBtn.addActionListener(new ActionListener() {
            				public void actionPerformed(ActionEvent evt) {
            					JCommonUtil.setJToggleButtonText(exportReportToogleBtn, new String[]{"匯出報表","不會出報表"});
            				}
            			});
            		}
            		{
            			projectSrcPathBtn = new JButton();
            			jPanel2.add(projectSrcPathBtn);
            			projectSrcPathBtn.setText("\u8a2d\u5b9a\u5c08\u6848\u76ee\u8def\u4e26\u6383\u63cf");
            			projectSrcPathBtn.setPreferredSize(new java.awt.Dimension(233, 95));
            			projectSrcPathBtn.addActionListener(new ActionListener() {
            				public void actionPerformed(ActionEvent evt) {
            					jButton1ActionPerformed(evt);
            				}
            			});
            		}
            	}
            }
            this.setSize(496, 395);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        try {
            File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
            if (file == null) {
                return;
            }

            loadJspCheckTag.dirPath(file.getAbsolutePath());
            loadJspCheckTag.writeLogFile(exportReportToogleBtn.isSelected());
            loadJspCheckTag.subFileName(Validate.notBlank(subFileNameText.getText()));
            loadJspCheckTag.execute();
            Map<Tag, Set<File>> map = loadJspCheckTag.getTagMap();

            setTreeNode(map);

        } catch (IOException e) {
        	JCommonUtil.handleException(e);
        }
    }

    private void setTreeNode(Map<Tag, Set<File>> map) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode childchild = null;
        List<Tag> list = new ArrayList<Tag>(map.keySet());
        List<File> childList = null;
        Collections.sort(list);
        for (Tag tagName : list) {
            child = new DefaultMutableTreeNode();
            child.setUserObject(tagName);
            root.add(child);
            childList = new ArrayList<File>(map.get(tagName));
            Collections.sort(childList);
            for (File chfile : childList) {
                childchild = new DefaultMutableTreeNode();
                childchild.setUserObject(new ChieldNode(chfile));
                child.add(childchild);
            }
        }
        jTree1.setModel(new DefaultTreeModel(root));
    }

    private void jTree1ValueChanged(TreeSelectionEvent evt) {
        // this.jTreeListener(evt);
    }

    private void jTree1MouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            this.jTreeListener(evt);
        }
    }

    private void jTreeListener(EventObject evt) {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((JTree) (evt.getSource())).getLastSelectedPathComponent();
            Object userObj = node.getUserObject();
            if (userObj != null && userObj instanceof ChieldNode) {
                File file = ((ChieldNode) userObj).file;
                if (modifyFileBox.getSelectedItem().equals("不可改")) {
                    File tempFile = File.createTempFile(file.getName().substring(0, file.getName().length() - 5), ".java");
                    byte[] content = FileUtil.loadFromFile(file);
                    FileUtil.saveToFile(tempFile, content);
                    Runtime.getRuntime().exec("cmd /c start " + tempFile.getAbsolutePath());
                    tempFile.deleteOnExit();
                } else {
                    Runtime.getRuntime().exec("cmd /c start " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(e.getMessage(), "error");
        }
    }

    private LoadJspCheckTag loadJspCheckTag = LoadJspCheckTag.newInstance();
    private JScrollPane jScrollPane1;
    private JButton projectSrcPathBtn;
    private JToggleButton exportReportToogleBtn;
    private JComboBox modifyFileBox;
    private JTextField subFileNameText;
    private JPanel jPanel2;
    private JTree jTree1;
    private JPanel jPanel1;
    private JTabbedPane jTabbedPane1;

    class ChieldNode {
        private String fileName;
        private File file;

        private ChieldNode(File file) {
            this.file = file;
            this.fileName = file.getName();
        }

        public String toString() {
            return this.fileName;
        }
    }
}
