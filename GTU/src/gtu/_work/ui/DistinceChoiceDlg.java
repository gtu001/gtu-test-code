package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.ArrayUtils;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;

public class DistinceChoiceDlg<T> extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JList leftMenuLst;
    private JList rightMenuLst;

    List<T> allLst;
    List<T> rightLst;
    ActionListener confirmListener;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            DistinceChoiceDlg dialog = new DistinceChoiceDlg();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apply(List<T> allLst, List<T> rightLst, ActionListener confirmListener) {
        initListModel(allLst, rightLst);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
        this.confirmListener = confirmListener;
    }

    public List<T> getChoiceLst() {
        List<Object> lst = new ArrayList<Object>();
        DefaultListModel model = (DefaultListModel) rightMenuLst.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            lst.add(model.getElementAt(ii));
        }
        return (List<T>) lst;
    }

    private void initListModel(List<T> allLst, List<T> rightLst) {
        this.allLst = allLst;
        this.rightLst = rightLst;
        List<Object> leftLst = new ArrayList<Object>();
        for (Object v : this.allLst) {
            if (!this.rightLst.contains(v)) {
                leftLst.add(v);
            }
        }
        DefaultListModel leftModel = new DefaultListModel();
        for (Object v : leftLst) {
            leftModel.addElement(v);
        }
        DefaultListModel rightModel = new DefaultListModel();
        for (Object v : this.rightLst) {
            rightModel.addElement(v);
        }
        this.leftMenuLst.setModel(leftModel);
        this.rightMenuLst.setModel(rightModel);
    }

    /**
     * Create the dialog.
     */
    public DistinceChoiceDlg() {
        setBounds(100, 100, 689, 462);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
        }
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
            contentPanel.add(panel, BorderLayout.WEST);
            {
                leftMenuLst = new JList();
                leftMenuLst.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent arg0) {
                        JListUtil.newInstance(leftMenuLst).defaultJListKeyPressed(arg0, false);
                    }
                });
                // leftMenuLst.setPreferredSize(new Dimension(0, 350));
                panel.add(JCommonUtil.createScrollComponent(leftMenuLst));
                panel.setPreferredSize(new Dimension(300, 550));
            }
        }
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
            contentPanel.add(panel, BorderLayout.EAST);
            {
                rightMenuLst = new JList();
                rightMenuLst.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent arg0) {
                        JListUtil.newInstance(rightMenuLst).defaultJListKeyPressed(arg0, false);
                    }
                });
                // rightMenuLst.setPreferredSize(new Dimension(0, 350));
                panel.add(JCommonUtil.createScrollComponent(rightMenuLst));
                panel.setPreferredSize(new Dimension(300, 550));
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.CENTER);
            {
                JButton addMenuBtn = new JButton(">");
                addMenuBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listObjectMoveTo(leftMenuLst, rightMenuLst);
                        JListUtil.setUnselected(leftMenuLst);
                        JListUtil.setUnselected(rightMenuLst);
                    }
                });
                {
                    JButton addAllBtn = new JButton(">>");
                    addAllBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JListUtil.newInstance(leftMenuLst).setSelectedAll();
                            listObjectMoveTo(leftMenuLst, rightMenuLst);
                            JListUtil.setUnselected(leftMenuLst);
                            JListUtil.setUnselected(rightMenuLst);
                        }
                    });
                    panel.add(addAllBtn);
                }
                panel.add(addMenuBtn);
            }
            {
                JButton removeMenuBtn = new JButton("<");
                removeMenuBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listObjectMoveTo(rightMenuLst, leftMenuLst);
                        JListUtil.setUnselected(leftMenuLst);
                        JListUtil.setUnselected(rightMenuLst);
                    }
                });
                panel.add(removeMenuBtn);
            }
            {
                JButton removeAllBtn = new JButton("<<");
                removeAllBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JListUtil.newInstance(rightMenuLst).setSelectedAll();
                        listObjectMoveTo(rightMenuLst, leftMenuLst);
                        JListUtil.setUnselected(leftMenuLst);
                        JListUtil.setUnselected(rightMenuLst);
                    }
                });
                panel.add(removeAllBtn);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        if (confirmListener != null) {
                            confirmListener.actionPerformed(arg0);
                        }
                    }
                });
                buttonPane.add(okButton);
                // getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        DistinceChoiceDlg.this.dispose();
                    }
                });
                buttonPane.add(cancelButton);
            }
        }

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
    }

    private void listObjectMoveTo(JList fromLst, JList toLst) {
        DefaultListModel rModel = (DefaultListModel) toLst.getModel();
        DefaultListModel lModel = (DefaultListModel) fromLst.getModel();
        List<T> lst = JListUtil.getLeadSelectionArry(fromLst);
        for (T obj : lst) {
            if (obj != null) {
                lModel.removeElement(obj);
                rModel.addElement(obj);
            }
        }
    }
}
