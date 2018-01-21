package gtu.swing.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang.StringUtils;

public class JTreeUtil {

    JTree tree;

    private JTreeUtil(JTree tree) {
        this.tree = tree;
    }

    public static JTreeUtil newInstance(JTree tree) {
        return new JTreeUtil(tree);
    }

    public void fileSystem(File dir) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new JFile(dir));
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        addFileSystemTreeNode(dir, null, rootNode);
    }

    public void fileSystem(File dir, FilenameFilter filenameFilter) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new JFile(dir));
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        addFileSystemTreeNode(dir, filenameFilter, rootNode);
    }

    public boolean removeNode(DefaultMutableTreeNode removeNode) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        Set<Boolean> result = new HashSet<Boolean>();
        removeNode(removeNode, rootNode, result);
        return result.contains(true);
    }

    void removeNode(DefaultMutableTreeNode removeNode, DefaultMutableTreeNode parent, Set<Boolean> result) {
        System.out.println(result.hashCode());
        if (parent.getIndex(removeNode) != -1) {
            parent.remove(removeNode);
            tree.updateUI();
            result.add(true);
        } else {
            for (Enumeration<?> enu = parent.children(); enu.hasMoreElements();) {
                DefaultMutableTreeNode nodes = (DefaultMutableTreeNode) enu.nextElement();
                removeNode(removeNode, nodes, result);
            }
        }
    }

    public DefaultMutableTreeNode getSelectItem() {
        List<DefaultMutableTreeNode> list = getSelectItems();
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() != 1) {
            throw new RuntimeException("getSelectItem(), select more than one!!");
        }
        return list.get(0);
    }

    public List<DefaultMutableTreeNode> getSelectItems() {
        TreeSelectionModel selectModel = tree.getSelectionModel();
        TreePath[] treePath = selectModel.getSelectionPaths();
        TreePath tmpTreePath = null;
        Object[] treeObject = null;
        Object currentObject = null;
        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
        if (treePath == null) {
            return list;
        }
        for (int ii = 0; ii < treePath.length; ii++) {
            tmpTreePath = treePath[ii];
            treeObject = tmpTreePath.getPath();
            currentObject = treeObject[treeObject.length - 1];
            list.add((DefaultMutableTreeNode) currentObject);
        }
        return list;
    }

    void addFileSystemTreeNode(File file, FilenameFilter filenameFilter, DefaultMutableTreeNode loot) {
        DefaultMutableTreeNode node = null;
        if (file.isDirectory()) {
            List<JFile> list = new ArrayList<JFile>();
            File[] listFile = null;
            if (filenameFilter != null) {
                listFile = file.listFiles(filenameFilter);
            } else {
                listFile = file.listFiles();
            }
            if (listFile != null) {
                for (File f : listFile) {
                    list.add(new JFile(f));
                }
            }
            Collections.sort(list);
            for (JFile f : list) {
                node = new DefaultMutableTreeNode(f);
                addFileSystemTreeNode(f.file, filenameFilter, node);
                loot.add(node);
            }
        }
    }

    public static class JFile implements Comparable<JFile> {
        File file;
        String name;

        public JFile(File file) {
            super();
            this.file = file;
            this.name = file.getName();
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return StringUtils.defaultString(name, file.getName());
        }

        @Override
        public int compareTo(JFile paramT) {
            if (file.isDirectory() && paramT.file.isDirectory()) {
                return name.compareTo(paramT.name);
            }
            if (file.isFile() && paramT.file.isFile()) {
                return file.length() > paramT.file.length() ? -1 : (file.length() < paramT.file.length() ? 1 : 0);
            }
            if (file.isDirectory()) {
                return -1;
            }
            if (paramT.file.isDirectory()) {
                return 1;
            }
            return 0;
        }
    }
}
