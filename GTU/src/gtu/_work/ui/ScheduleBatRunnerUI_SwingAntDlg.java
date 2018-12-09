package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

public class ScheduleBatRunnerUI_SwingAntDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private SwingActionUtil swingUtil;
    private JTextField swingAntDirText;
    private JList swingAntList;
    private PropertiesUtilBean config = new PropertiesUtilBean(ScheduleBatRunnerUI_SwingAntDlg.class);
    private ActionListener actionEvent;
    private JTextField filterText;
    private DefaultListModel allModel;
    private JTextField formatText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        ScheduleBatRunnerUI_SwingAntDlg.newInstance(null);
    }

    public static ScheduleBatRunnerUI_SwingAntDlg newInstance(ActionListener actionEvent) {
        ScheduleBatRunnerUI_SwingAntDlg dialog = new ScheduleBatRunnerUI_SwingAntDlg();
        dialog.actionEvent = actionEvent;
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Create the dialog.
     */
    public ScheduleBatRunnerUI_SwingAntDlg() {
        swingUtil = SwingActionUtil.newInstance(this);

        setBounds(100, 100, 532, 438);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel label = new JLabel("輸入目錄");
                panel.add(label);
            }
            {
                swingAntDirText = new JTextField();
                JCommonUtil.jTextFieldSetFilePathMouseEvent(swingAntDirText, true);
                panel.add(swingAntDirText);
                swingAntDirText.setColumns(15);
                swingAntDirText.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        swingUtil.invokeAction("swingAntDirText.focusLost", e);
                    }
                });
            }
            {
                JButton saveConfigBtn = new JButton("儲存設定");
                saveConfigBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        swingUtil.invokeAction("saveConfigBtn.click", e);
                    }
                });
                {
                    JLabel lblNewLabel_1 = new JLabel("format");
                    panel.add(lblNewLabel_1);
                }
                {
                    formatText = new JTextField();
                    formatText.setText("ant -f \"%s\" ");
                    formatText.setColumns(15);
                    panel.add(formatText);
                }
                panel.add(saveConfigBtn);
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.WEST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.EAST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
            {
                JLabel lblNewLabel = new JLabel("過濾");
                panel.add(lblNewLabel);
            }
            {
                filterText = new JTextField();
                panel.add(filterText);
                filterText.setColumns(30);
                filterText.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        swingUtil.invokeAction("filterText.focusLost", e);
                    }
                });
            }
        }
        {
            swingAntList = new JList();
            swingAntList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    swingUtil.invokeAction("swingAntList.mouseClicked", e);
                }
            });
            contentPanel.add(JCommonUtil.createScrollComponent(swingAntList), BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        swingUtil.invokeAction("btn.Close", e);
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        swingUtil.invokeAction("btn.Close", e);
                    }
                });
            }
        }

        // event
        {
            swingUtil.addAction("swingAntDirText.focusLost", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {
                    if (StringUtils.isBlank(swingAntDirText.getText())) {
                        return;
                    }
                    File dir = new File(swingAntDirText.getText());
                    if (!dir.exists() || !dir.isDirectory()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("目錄有誤 : " + dir);
                        return;
                    }
                    allModel = JListUtil.createModel();
                    for (File f : dir.listFiles()) {
                        if (f.isFile() && f.getName().toLowerCase().endsWith(".xml")) {
                            FileZ f2 = new FileZ(f.getParentFile(), f.getName());
                            allModel.addElement(f2);
                        }
                    }
                    swingAntList.setModel(allModel);
                }
            });
            swingUtil.addAction("saveConfigBtn.click", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {
                    config.reflectSetConfig(ScheduleBatRunnerUI_SwingAntDlg.this);
                    config.store();
                    JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
                }
            });
            swingUtil.addAction("swingAntList.mouseClicked", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {
                    final FileZ file = (FileZ) JListUtil.getLeadSelectionObject(swingAntList);
                    if (file == null) {
                        return;
                    }

                    if (JMouseEventUtil.buttonRightClick(1, evt)) {
                        JPopupMenuUtil.newInstance(swingAntList)//
                                .addJMenuItem("開啟目錄", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            DesktopUtil.openDir(file);
                                        } catch (Exception e1) {
                                            JCommonUtil.handleException(e1);
                                        }
                                    }
                                })//
                                .addJMenuItem("開啟文件", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            DesktopUtil.browse(file.toURL().toString());
                                        } catch (Exception e1) {
                                            JCommonUtil.handleException(e1);
                                        }
                                    }
                                })//
                                .applyEvent(evt)//
                                .show();//
                    } else if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                        if (actionEvent == null) {
                            throw new Exception("EVENT未設定");
                        }

                        String command = file.getAbsolutePath();
                        if (StringUtils.isNotBlank(formatText.getText())) {
                            command = String.format(formatText.getText(), file.getAbsolutePath());
                        }

                        ActionEvent evt2 = new ActionEvent(command, -1, file.getName());
                        actionEvent.actionPerformed(evt2);
                        JCommonUtil._jOptionPane_showMessageDialog_info("加入成功！");
                    }
                }
            });
            swingUtil.addAction("btn.Close", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {
                    dispose();
                }
            });
            swingUtil.addAction("filterText.focusLost", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {
                    String text = StringUtils.trimToEmpty(filterText.getText()).toLowerCase();
                    DefaultListModel model = JListUtil.createModel();
                    for (int ii = 0; ii < allModel.getSize(); ii++) {
                        FileZ f = (FileZ) allModel.getElementAt(ii);
                        System.out.println("111 === " + f.getName());
                        System.out.println("222 === " + f.jarName);
                        if (StringUtils.isBlank(text)) {
                            model.addElement(f);
                        } else if (f.getName().toLowerCase().contains(text) || //
                        (StringUtils.isNotBlank(f.jarName) && f.jarName.toLowerCase().contains(text))) {
                            model.addElement(f);
                        }
                    }
                    swingAntList.setModel(model);
                }
            });
            swingUtil.addAction("XXXXXXXXXXXXXX", new Action() {
                @Override
                public void action(EventObject evt) throws Exception {

                }
            });
        }

        {
            setTitle("Ant Batch");
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setFoucsToChildren(this, this);
            this.setModal(false);
            config.reflectInit(this);

            swingUtil.invokeActionByComponent("swingAntDirText.focusLost", swingAntDirText);
        }
    }

    private class FileZ extends File {
        String jarName;

        FileZ(File dir, String name) {
            super(dir, name);

            Pattern ptn = Pattern.compile("\\<property\\s+name\\=\"main_class\"\\s+value\\=\"(\\w+?)\"");
            String content = FileUtil.loadFromFile(this, "UTF8");
            Matcher mth = ptn.matcher(content);
            if (mth.find()) {
                jarName = mth.group(1);
            }
        }

        public String toString() {
            return "<html>" + this.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;    <font color='blue'>" + StringUtils.trimToEmpty(jarName) + "</font></html>";
        }
    }
}
