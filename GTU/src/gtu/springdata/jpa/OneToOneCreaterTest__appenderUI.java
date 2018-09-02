package gtu.springdata.jpa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.springdata.jpa.OneToManyCreaterTest__appenderUI.__JpaRelation_PerformExecute;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JLabelUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

public class OneToOneCreaterTest__appenderUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField refToTypeText;
    private JLabel lblRefFromType;
    private JTextField refFromTypeText;
    private JTextField projectFolderText;
    private JLabel lblNewLabel_1;
    private JPanel panel;
    private JButton resetBtn;
    private JButton btnNewButton;
    private JButton testBtn;
    private JLabel lblNewLabel;
    private JTextField dbColumnNameText;
    private JPanel panel_1;
    private JCheckBox cleanerChk;
    private JRadioButton unidirectionalRadio;
    private JRadioButton bidirectionalRadio;
    private ButtonGroup buttonGroup;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OneToOneCreaterTest__appenderUI frame = new OneToOneCreaterTest__appenderUI();
                    frame.setVisible(true);
                    frame.projectFolderText.setText("D:/workstuff/workspace_taida/isa95-model");// TODO
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public OneToOneCreaterTest__appenderUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        lblRefFromType = new JLabel(JLabelUtil.getText("ref from type"));
        contentPane.add(lblRefFromType, "2, 2, right, default");

        refFromTypeText = new JTextField();
        refFromTypeText.setColumns(10);
        contentPane.add(refFromTypeText, "4, 2, fill, default");
        refFromTypeText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String $refFromTypeText = StringUtils.trimToEmpty(refFromTypeText.getText());
                    if (StringUtils.isNotBlank($refFromTypeText)) {
                        String resultStr = StringUtilForDb.javaToDbField($refFromTypeText) + "_id";
                        dbColumnNameText.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        JLabel lblRefType = new JLabel(JLabelUtil.getText("ref to type"));
        contentPane.add(lblRefType, "2, 4, right, default");

        refToTypeText = new JTextField();
        refToTypeText.setColumns(10);
        refToTypeText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String $refToTypeText = StringUtils.trimToEmpty(refToTypeText.getText());
                    if (StringUtils.isNotBlank($refToTypeText)) {
                        String ljavaListNameTextistName = __JpaRelation_PerformExecute.toJavaListName($refToTypeText);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        contentPane.add(refToTypeText, "4, 4, fill, default");

        lblNewLabel = new JLabel("db column name");
        contentPane.add(lblNewLabel, "2, 6, right, default");

        dbColumnNameText = new JTextField();
        contentPane.add(dbColumnNameText, "4, 6, fill, default");
        dbColumnNameText.setColumns(10);

        panel_1 = new JPanel();
        contentPane.add(panel_1, "4, 14, fill, fill");

        cleanerChk = new JCheckBox("cleaner");
        panel_1.add(cleanerChk);

        unidirectionalRadio = new JRadioButton("unidirectional");
        panel_1.add(unidirectionalRadio);

        bidirectionalRadio = new JRadioButton("bidirectional");
        panel_1.add(bidirectionalRadio);

        lblNewLabel_1 = new JLabel("baseDir");
        contentPane.add(lblNewLabel_1, "2, 16, right, default");

        projectFolderText = new JTextField();
        contentPane.add(projectFolderText, "4, 16, fill, default");
        projectFolderText.setColumns(10);

        btnNewButton = new JButton("執行");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PerformExecuteClz tt = null;
                try {

                    String refFromType1 = refFromTypeText.getText(); //
                    String refToType1 = refToTypeText.getText(); //
                    String dbColumnName = dbColumnNameText.getText(); //
                    boolean doCleanser = cleanerChk.isSelected(); //
                    String projectDir = projectFolderText.getText(); //

                    if (unidirectionalRadio == JButtonGroupUtil.getSelectedButton(buttonGroup)) {
                        tt = new PerformExecuteClz_Unidirectional();
                    } else if (bidirectionalRadio == JButtonGroupUtil.getSelectedButton(buttonGroup)) {
                        tt = new PerformExecuteClz_Bidirectional();
                    } else {
                        throw new Exception("請選擇radio");
                    }

                    tt.actionPerformed(refFromType1, refToType1, dbColumnName, doCleanser, projectDir);

                    JCommonUtil._jOptionPane_showMessageDialog_info("ok!!");
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        });
        contentPane.add(btnNewButton, "2, 18");

        panel = new JPanel();
        contentPane.add(panel, "4, 18, fill, fill");

        resetBtn = new JButton("reset");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refFromTypeText.setText("");
                refToTypeText.setText("");
                dbColumnNameText.setText("");
            }
        });
        panel.add(resetBtn);

        testBtn = new JButton("test");
        testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCommonUtil._jOptionPane_showMessageDialog_info("TODO");
            }
        });
        panel.add(testBtn);

        buttonGroup = JButtonGroupUtil.createRadioButtonGroup(unidirectionalRadio, bidirectionalRadio);

        this.setTitle("OneToOne");
        JCommonUtil.setJFrameCenter(this);
    }

    private abstract class PerformExecuteClz extends __JpaRelation_PerformExecute {
        public abstract void actionPerformed(//
                String refFromType1, //
                String refToType1, //
                String dbColumnName, //
                boolean doCleanser, //
                String projectDir) throws IOException;
    }

    private class PerformExecuteClz_Unidirectional extends PerformExecuteClz {
        public void actionPerformed(//
                String refFromType1, //
                String refToType1, //
                String dbColumnName, //
                boolean doCleanser, //
                String projectDir) throws IOException {//
            OneToOneCreaterTest t = new OneToOneCreaterTest();

            File dir = JCommonUtil.filePathCheck(projectDir, "專案目錄", true);

            List<File> fileLst = new ArrayList<File>();
            FileUtil.searchFilefind(dir, ".*\\.java", fileLst);

            AtomicReference<String> refFromType = new AtomicReference<>();
            AtomicReference<String> refToType = new AtomicReference<>();

            refFromType.set(refFromType1);
            refToType.set(refToType1);

            System.out.println(refFromType.get());
            System.out.println(refToType.get());
            System.out.println(fileLst.size());

            File refFromTypeFile = this.getOptionsFile(fileLst, refFromType.get());
            File refToTypeFile = this.getOptionsFile(fileLst, refToType.get());

            if (doCleanser) {
                doCleanserFile(refFromTypeFile);
            }

            refFromType.set(fixName(refFromTypeFile.getName()));
            refToType.set(fixName(refToTypeFile.getName()));

            // 請勿砍掉 ↓↓↓↓↓↓↓
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("master_db_id_fk", dbColumnName); //
            root.put("detail_class", refToType.get()); //
            root.put("detail_name", StringUtils.uncapitalize(refToType.get())); //
            // 請勿砍掉 ↑↑↑↑↑↑↑

            t.execute(root, 1);

            String content2 = appendBlock(refFromTypeFile, t.getMasterBlock());

            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content2);
            System.out.println("-----------------------------------------------------------------------");

            FileUtil.saveToFile(refFromTypeFile, content2, "UTF8");
        }
    }

    private class PerformExecuteClz_Bidirectional extends PerformExecuteClz {
        public void actionPerformed(//
                String refFromType1, //
                String refToType1, //
                String dbColumnName, //
                boolean doCleanser, //
                String projectDir) throws IOException {//
            OneToOneCreaterTest t = new OneToOneCreaterTest();

            File dir = JCommonUtil.filePathCheck(projectDir, "專案目錄", true);

            List<File> fileLst = new ArrayList<File>();
            FileUtil.searchFilefind(dir, ".*\\.java", fileLst);

            AtomicReference<String> refFromType = new AtomicReference<>();
            AtomicReference<String> refToType = new AtomicReference<>();

            refFromType.set(refFromType1);
            refToType.set(refToType1);

            System.out.println(refFromType.get());
            System.out.println(refToType.get());
            System.out.println(fileLst.size());

            File refFromTypeFile = this.getOptionsFile(fileLst, refFromType.get());
            File refToTypeFile = this.getOptionsFile(fileLst, refToType.get());

            if (doCleanser) {
                doCleanserFile(refFromTypeFile);
                doCleanserFile(refToTypeFile);
            }

            refFromType.set(fixName(refFromTypeFile.getName()));
            refToType.set(fixName(refToTypeFile.getName()));

            // 請勿砍掉 ↓↓↓↓↓↓↓
            Map<String, Object> root = new HashMap<String, Object>();

            root.put("master_db_id_fk", dbColumnName); //
            root.put("detail_class", refToType.get()); //
            root.put("detail_name", StringUtils.uncapitalize(refToType.get())); //
            root.put("master_class", refFromType.get()); //
            root.put("master_name", StringUtils.uncapitalize(refFromType.get())); //

            // 請勿砍掉 ↑↑↑↑↑↑↑

            t.execute(root, 2);

            String content1 = appendBlock(refFromTypeFile, t.getMasterBlock());
            String content2 = appendBlock(refToTypeFile, t.getDetailBlock());

            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content1);
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content2);
            System.out.println("-----------------------------------------------------------------------");

            FileUtil.saveToFile(refFromTypeFile, content1, "UTF8");
            FileUtil.saveToFile(refToTypeFile, content2, "UTF8");
        }
    }
}
