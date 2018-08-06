package gtu.springdata.jpa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.springdata.jpa.OneToManyCreaterTest__appenderUI.__JpaRelation_PerformExecute;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

public class ManyToManyCreaterTest_appenderUI extends JFrame {

    private JPanel contentPane;
    private JTextField middleTableText;
    private JTextField fromEntityText;
    private JTextField targetEntityText;
    private JTextField middleToFromColumnText;
    private JTextField middleToTargetColumnText;
    private JTextField baseDirText;
    private JCheckBox cleanerChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManyToManyCreaterTest_appenderUI frame = new ManyToManyCreaterTest_appenderUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManyToManyCreaterTest_appenderUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel lblMiddleTable = new JLabel("middle table");
        contentPane.add(lblMiddleTable, "2, 2, right, default");

        middleTableText = new JTextField();
        contentPane.add(middleTableText, "4, 2, fill, default");
        middleTableText.setColumns(10);

        JLabel lblFromEntity = new JLabel("from entity");
        contentPane.add(lblFromEntity, "2, 4, right, default");

        FocusAdapter fromEntityAndTargetEntityFocusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    String text1 = fromEntityText.getText();
                    String text2 = targetEntityText.getText();
                    if (StringUtils.isNotBlank(text1) && StringUtils.isNotBlank(text2)) {
                        String resultStr = StringUtilForDb.javaToDbField(text1) + "_" + StringUtilForDb.javaToDbField(text2);
                        middleTableText.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        };

        fromEntityText = new JTextField();
        contentPane.add(fromEntityText, "4, 4, fill, default");
        fromEntityText.setColumns(10);
        fromEntityText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            public void process(DocumentEvent event) {
                try {
                    String text = fromEntityText.getText();
                    if (StringUtils.isNotBlank(text)) {
                        String resultStr = StringUtilForDb.javaToDbField(text) + "_id";
                        middleToFromColumnText.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));
        fromEntityText.addFocusListener(fromEntityAndTargetEntityFocusAdapter);

        JLabel lblTargetEntity = new JLabel("target entity");
        contentPane.add(lblTargetEntity, "2, 6, right, default");

        targetEntityText = new JTextField();
        contentPane.add(targetEntityText, "4, 6, fill, default");
        targetEntityText.setColumns(10);
        targetEntityText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            public void process(DocumentEvent event) {
                try {
                    String text = targetEntityText.getText();
                    if (StringUtils.isNotBlank(text)) {
                        String resultStr = StringUtilForDb.javaToDbField(text) + "_id";
                        middleToTargetColumnText.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));
        targetEntityText.addFocusListener(fromEntityAndTargetEntityFocusAdapter);

        JLabel lblMiddleToFrom = new JLabel("middle to from column");
        contentPane.add(lblMiddleToFrom, "2, 8, right, default");

        middleToFromColumnText = new JTextField();
        contentPane.add(middleToFromColumnText, "4, 8, fill, default");
        middleToFromColumnText.setColumns(10);

        JLabel lblMiddleToTarget = new JLabel("middle to target column");
        contentPane.add(lblMiddleToTarget, "2, 10, right, default");

        middleToTargetColumnText = new JTextField();
        contentPane.add(middleToTargetColumnText, "4, 10, fill, default");
        middleToTargetColumnText.setColumns(10);

        JButton executeBtn = new JButton("execute");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String middleTable = middleTableText.getText();
                    String fromEntity = fromEntityText.getText();
                    String targetEntity = targetEntityText.getText();
                    String middleToFromColumn = middleToFromColumnText.getText();
                    String middleToTargetColumn = middleToTargetColumnText.getText();
                    String baseDir = baseDirText.getText();
                    boolean doCleanser = cleanerChk.isSelected();

                    PerformExecute t1 = new PerformExecute(//
                            middleTable, fromEntity, targetEntity, //
                            middleToFromColumn, middleToTargetColumn, //
                            baseDir, doCleanser);

                    t1.execute();

                    JCommonUtil._jOptionPane_showMessageDialog_info("執行完成!!");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        cleanerChk = new JCheckBox("do clean");
        contentPane.add(cleanerChk, "4, 14");

        JLabel lblBaseDir = new JLabel("base dir");
        contentPane.add(lblBaseDir, "2, 16, right, default");

        baseDirText = new JTextField();
        contentPane.add(baseDirText, "4, 16, fill, default");
        baseDirText.setColumns(10);
        contentPane.add(executeBtn, "2, 18");

        JPanel panel = new JPanel();
        contentPane.add(panel, "4, 18, fill, fill");

        JButton clearBtn = new JButton("clear");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    middleTableText.setText("");
                    fromEntityText.setText("");
                    targetEntityText.setText("");
                    middleToFromColumnText.setText("");
                    middleToTargetColumnText.setText("");
                    baseDirText.setText("");
                    cleanerChk.setSelected(false);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel.add(clearBtn);

        this.setTitle("ManyToMany");
    }

    private class PerformExecute extends __JpaRelation_PerformExecute {
        private String middleTable;
        private String fromEntity;
        private String targetEntity;
        private String middleToFromColumn;
        private String middleToTargetColumn;
        private String baseDir;
        private List<File> fileLst;
        private File fromEntityFile;
        private File targetEntityFile;
        private boolean doCleanser;
        private ManyToManyCreaterTest tt;

        private PerformExecute(String middleTable, String fromEntity, String targetEntity, String middleToFromColumn, String middleToTargetColumn, String baseDir, boolean doCleanser) {
            this.middleTable = StringUtils.trimToEmpty(middleTable);
            this.fromEntity = StringUtils.trimToEmpty(fromEntity);
            this.targetEntity = StringUtils.trimToEmpty(targetEntity);
            this.middleToFromColumn = StringUtils.trimToEmpty(middleToFromColumn);
            this.middleToTargetColumn = StringUtils.trimToEmpty(middleToTargetColumn);
            this.baseDir = StringUtils.trimToEmpty(baseDir);
            this.doCleanser = doCleanser;
        }

        private void initFile() {
            fromEntityFile = fileLst.stream().filter(f -> compare(f.getName(), this.fromEntity)).findAny()//
                    .orElseThrow(() -> {
                        throw new RuntimeException("找不到fromEntity");
                    });
            targetEntityFile = fileLst.stream().filter(f -> compare(f.getName(), this.targetEntity)).findAny()//
                    .orElseThrow(() -> {
                        throw new RuntimeException("找不到targetEntity");
                    });
            fromEntity = fixName(fromEntityFile.getName());
            targetEntity = fixName(targetEntityFile.getName());
        }

        private void init() {
            fileLst = new ArrayList<>();
            FileUtil.searchFilefind(new File(this.baseDir), ".*\\.java$", fileLst);
            this.initFile();

            if (doCleanser) {
                doCleanserFile(fromEntityFile);
                doCleanserFile(targetEntityFile);
            }

            String targetEntityLstName = toJavaListName(targetEntity);
            String fromEntityLstName = toJavaListName(fromEntity);

            // 請勿砍掉 ↓↓↓↓↓↓↓
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("middle_table", middleTable);
            root.put("middle_table_from_column", middleToFromColumn);
            root.put("middle_table_target_column", middleToTargetColumn);
            root.put("target_entity", targetEntity);
            root.put("target_entity_list", targetEntityLstName);
            root.put("from_entity", fromEntity);
            root.put("from_entity_list", fromEntityLstName);
            // 請勿砍掉 ↑↑↑↑↑↑↑

            tt = new ManyToManyCreaterTest();
            tt.execute(root);
        }

        public void execute() throws IOException {
            this.init();

            String content1 = appendBlock(fromEntityFile, tt.getFromEntityBlock());
            String content2 = appendBlock(targetEntityFile, tt.getTargetEntityBlock());

            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content1);
            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content2);
            System.out.println("-----------------------------------------------------------------------");

            FileUtil.saveToFile(fromEntityFile, content1, "UTF8");
            FileUtil.saveToFile(targetEntityFile, content2, "UTF8");
        }
    }
}
