package gtu._work.ui;

import gtu._work.category.LoadJavaCheckImport;
import gtu.file.FileUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JTreeUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
public class LoadJavaCheckImportUI extends javax.swing.JFrame {

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
                LoadJavaCheckImportUI inst = new LoadJavaCheckImportUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public LoadJavaCheckImportUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setTitle("\u8b80\u53d6Import\u8cc7\u8a0a");
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                jButton1 = new JButton();
                jButton1.setText("\u76ee\u9304");
                jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });
            }
            {
                ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "不可改", "可改" });
                jComboBox1 = new JComboBox();
                getContentPane().add(jComboBox1, BorderLayout.NORTH);
                getContentPane().add(jButton1, BorderLayout.SOUTH);
                {
                    jScrollPane1 = new JScrollPane();
                    getContentPane().add(jScrollPane1, BorderLayout.CENTER);
                    {
                        jTree1 = new JTree();
                        jScrollPane1.setViewportView(jTree1);
                        jTree1.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                jTree1KeyPressed(evt);
                            }
                        });
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
                jComboBox1.setModel(jComboBox1Model);
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
            simpleJavaCheckImport.setDirPath(file.getAbsolutePath());
            simpleJavaCheckImport.execute();
            Map<String, Set<File>> map = simpleJavaCheckImport.getImportMap();

            setTreeNode(map);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(e.getMessage(), "error");
        }
    }

    private void setTreeNode(Map<String, Set<File>> map) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode childchild = null;
        List<String> list = new ArrayList<String>(map.keySet());
        List<File> childList = null;
        Collections.sort(list);
        for (String packageName : list) {
            child = new DefaultMutableTreeNode();
            child.setUserObject(packageName);
            root.add(child);
            childList = new ArrayList<File>(map.get(packageName));
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
            JTree tree = ((JTree) (evt.getSource()));
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            this.openFile(node);
        }
    }

    void openFile(DefaultMutableTreeNode node) {
        try {
            Object userObj = node.getUserObject();
            if (userObj != null && userObj instanceof ChieldNode) {
                File file = ((ChieldNode) userObj).file;
                if (jComboBox1.getSelectedItem().equals("不可改")) {
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

    private void jTree1KeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
            return;
        }
        JTree tree = ((JTree) (evt.getSource()));
        List<DefaultMutableTreeNode> list = JTreeUtil.newInstance(tree).getSelectItems();
        for (DefaultMutableTreeNode node : list) {
            openFile(node);
        }
    }

    class SimpleJavaCheckImport extends LoadJavaCheckImport {
        @Override
        public void execute() throws IOException {
            this.setImportMap(new HashMap<String, Set<File>>());
            loadCheckDir(new File(this.getDirPath()));
        }
    }

    private SimpleJavaCheckImport simpleJavaCheckImport = new SimpleJavaCheckImport();
    private JButton jButton1;
    private JScrollPane jScrollPane1;
    private JComboBox jComboBox1;
    private JTree jTree1;

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
