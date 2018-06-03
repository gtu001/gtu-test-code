package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;

public class AVChoicerUI extends JFrame {
    
    public static final String FILE_EXTENSTION_VIDEO_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3)";

    private JPanel contentPane;
    private JTextField avDirText;
    private JList avDirList;
    private JButton choiceAVBtn;
    private JTextField avExeText;

    private static final String AV_LIST_KEY = "avDirList";
    private static final String AV_EXE_KEY = "avExeText";

    private PropertiesUtilBean config = new PropertiesUtilBean(AVChoicerUI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AVChoicerUI frame = new AVChoicerUI();
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
    public AVChoicerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 563, 433);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("播放影片", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_7 = new JPanel();
        panel.add(panel_7, BorderLayout.NORTH);

        JPanel panel_8 = new JPanel();
        panel.add(panel_8, BorderLayout.WEST);

        JPanel panel_9 = new JPanel();
        panel.add(panel_9, BorderLayout.SOUTH);

        JPanel panel_10 = new JPanel();
        panel.add(panel_10, BorderLayout.EAST);

        choiceAVBtn = new JButton("隨機撥放");
        choiceAVBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choiceAVBtnAction();
            }
        });
        panel.add(choiceAVBtn, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("搜尋目錄", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.NORTH);

        avDirText = new JTextField();
        panel_3.add(avDirText);
        avDirText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(avDirText, true);

        JButton avDirAddBtn = new JButton("add");
        avDirAddBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                avDirAddBtnAction();
            }
        });
        panel_3.add(avDirAddBtn);

        JButton saveConfigBtn = new JButton("save");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfigBtnAction();
            }
        });
        panel_3.add(saveConfigBtn);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.WEST);

        JPanel panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.EAST);

        JPanel panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.SOUTH);

        avDirList = new JList();
        panel_1.add(avDirList, BorderLayout.CENTER);
        avDirList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent paramKeyEvent) {
                cacheFileList = null;
                JListUtil.newInstance(avDirList).defaultJListKeyPressed(paramKeyEvent);
            }
        });

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("設定", null, panel_2, null);
        panel_2.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel label = new JLabel("撥放器");
        panel_2.add(label, "2, 2, right, default");

        avExeText = new JTextField();
        panel_2.add(avExeText, "4, 2, fill, default");
        avExeText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(avExeText, true);

        JButton saveConfigBtn2 = new JButton("save");
        saveConfigBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveConfigBtnAction();
            }
        });
        panel_2.add(saveConfigBtn2, "2, 20");

        config.reflectInit(this);
        initAvDirList();

        JCommonUtil.setJFrameCenter(this);
    }

    private void initAvDirList() {
        try {
            JListUtil jUtil = JListUtil.newInstance(avDirList);
            DefaultListModel model = jUtil.createModel();
            config.getConfigProp().keySet().stream()//
                    .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                    .forEach(key -> {
                        File f = new File(config.getConfigProp().getProperty((String) key));
                        if (f.exists()) {
                            model.addElement(f);
                        }
                    });
            avDirList.setModel(model);
            System.out.println("INIT size = " + avDirList.getModel().getSize());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private int getMaxAvListId() {
        Pattern ptn = Pattern.compile(AV_LIST_KEY + "\\_(\\d+)");
        String reduce = config.getConfigProp().keySet().stream()//
                .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                .map(key -> {
                    Matcher mth = ptn.matcher((String) key);
                    mth.find();
                    return mth.group(1);
                }).reduce("-1", (a, b) -> {
                    if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                        return a;
                    } else {
                        return b;
                    }
                });
        return Integer.parseInt(reduce);
    }

    private int getMaxAvExeId() {
        Pattern ptn = Pattern.compile(AV_EXE_KEY + "\\_(\\d+)");
        String reduce = config.getConfigProp().keySet().stream()//
                .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                .map(key -> {
                    Matcher mth = ptn.matcher((String) key);
                    mth.find();
                    return mth.group(1);
                }).reduce("-1", (a, b) -> {
                    if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                        return a;
                    } else {
                        return b;
                    }
                });
        return Integer.parseInt(reduce);
    }

    private void saveConfigBtnAction() {
        try {
            StringBuffer sb = new StringBuffer();
            {
                File exeFile = new File(avExeText.getText());
                if (exeFile.exists()) {
                    List<File> fileLst = config.getConfigProp().keySet().stream()//
                            .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                            .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                            .collect(Collectors.toList());
                    if (!fileLst.contains(exeFile)) {
                        String key = AV_EXE_KEY + "_" + (getMaxAvExeId() + 1);
                        config.getConfigProp().setProperty(key, exeFile.getAbsolutePath());
                        sb.append("exe儲存成功!\n");
                    }
                }
            }

            {
                DefaultListModel model = (DefaultListModel) avDirList.getModel();
                List<File> fileLst = config.getConfigProp().keySet().stream()//
                        .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                        .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                        .collect(Collectors.toList());

                AtomicBoolean bool = new AtomicBoolean(false);
                AtomicInteger index = new AtomicInteger(getMaxAvListId() + 1);
                IntStream.range(0, model.size())//
                        .mapToObj(i -> (File) model.getElementAt(i))//
                        .forEach(file -> {
                            if (!fileLst.contains(file)) {
                                config.getConfigProp().setProperty(AV_LIST_KEY + "_" + index.get(), file.getAbsolutePath());
                                index.set(index.get() + 1);
                                bool.set(true);
                            }
                        });

                if (bool.get()) {
                    sb.append("list儲存成功!\n");
                }
            }

            if (sb.length() > 0) {
                config.store();
            }

            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成果~\n" + sb);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void avDirAddBtnAction() {
        try {
            Validate.notBlank(avDirText.getText(), "目錄或檔案為空!");

            File newFile = new File(avDirText.getText());

            List<File> fileLst = config.getConfigProp().keySet().stream()//
                    .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                    .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                    .collect(Collectors.toList());

            DefaultListModel model = (DefaultListModel) avDirList.getModel();
            List<File> fileLst2 = IntStream.range(0, model.size())//
                    .mapToObj(i -> (File) model.getElementAt(i))//
                    .collect(Collectors.toList());

            boolean anyMatch1 = fileLst.stream().anyMatch(f -> f.getAbsolutePath().equals(newFile.getAbsolutePath()));
            boolean anyMatch2 = fileLst2.stream().anyMatch(f -> f.getAbsolutePath().equals(newFile.getAbsolutePath()));

            if (anyMatch1 || anyMatch2) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在!");
            } else {
                model.addElement(newFile);
                cacheFileList = null;
                JCommonUtil._jOptionPane_showMessageDialog_error("新增成功!");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private List<File> cacheFileList = new ArrayList<File>();

    private File getRandomAvFile() {
        if (cacheFileList == null || cacheFileList.isEmpty()) {
            cacheFileList = new ArrayList<File>();

            DefaultListModel model = (DefaultListModel) avDirList.getModel();
            IntStream.range(0, model.size())//
                    .mapToObj(i -> (File) model.getElementAt(i))//
                    .forEach(file -> {
                        FileUtil.searchFileMatchs(file, ".*\\." + FILE_EXTENSTION_VIDEO_PATTERN, cacheFileList);
                    });
        }

        System.out.println("cacheFileList.size() = " + cacheFileList.size());
        return cacheFileList.get(new Random().nextInt(cacheFileList.size()));
    }

    private void choiceAVBtnAction() {
        try {
            File exe = config.getConfigProp().keySet().stream()//
                    .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                    .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                    .filter(File::exists)//
                    .findFirst().orElseThrow(() -> {
                        return new Exception("未設定Movie Exe!!");
                    });

            File avFile = getRandomAvFile();

            Runtime.getRuntime().exec(String.format("cmd /c call \"%s\" \"%s\" ", exe, avFile));
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
