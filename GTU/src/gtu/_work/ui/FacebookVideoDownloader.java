package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.javafx.traynotification.NotificationType;
import gtu.javafx.traynotification.TrayNotificationHelper;
import gtu.javafx.traynotification.animations.AnimationType;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.youtube.DownloadProgressHandler;
import gtu.youtube.JavaYoutubeVideoUrlHandler;
import gtu.youtube.Porn91Downloader;
import gtu.youtube.Porn91Downloader.VideoUrlConfig;

public class FacebookVideoDownloader extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField urlText;
    private JTable videoTable;
    private JTextArea cookieArea;

    private Porn91Downloader downloader = new Porn91Downloader();
    private DownloadThreadPoolWatcher downloadPool = new DownloadThreadPoolWatcher();
    private JTable downloadListTable;
    private DefaultTableModel downloadListModel;
    private JTextField downloadThreadSizeText;

    private PropertiesUtilBean config = new PropertiesUtilBean(FacebookVideoDownloader.class);
    private JTextField targetDirText;
    private File targetDirectory = FileUtil.DESKTOP_DIR;
    private HideInSystemTrayHelper sysutil;
    private JTextArea headerTextArea;

    private DownloadLogKeeper downloadLog = new DownloadLogKeeper();
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private JPanel panel_23;

    private class DownloadThreadPoolWatcher extends Thread {
        LinkedList<VideoUrlConfigZ> downloadLst = new LinkedList<VideoUrlConfigZ>();
        List<Thread> pool = new ArrayList<Thread>();

        private int getMaxSize() {
            int maxSize = 5;
            try {
                maxSize = Integer.parseInt(downloadThreadSizeText.getText());
            } catch (Exception ex) {
            }
            // System.out.println("目前同時最大下載數 : " + maxSize);
            return maxSize;
        }

        @Override
        public void run() {
            while (true) {
                checkDownload();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        }

        private void checkDownload() {
            for (int ii = 0; ii < pool.size(); ii++) {
                if (pool.get(ii).getState() == Thread.State.TERMINATED) {
                    pool.remove(ii);
                    ii--;
                }
            }
            int addSize = getMaxSize() - pool.size();
            for (int ii = 0; ii < addSize; ii++) {
                if (!downloadLst.isEmpty()) {
                    VideoUrlConfigZ vo = downloadLst.pop();
                    DownloadGOGO thread = new DownloadGOGO(vo, FacebookVideoDownloader.this);
                    thread.start();
                    pool.add(thread);
                }
            }
        }

        private class DownloadGOGO extends Thread {
            FacebookVideoDownloader ui;
            VideoUrlConfigZ vo;

            private DownloadGOGO(VideoUrlConfigZ vo, FacebookVideoDownloader ui) {
                this.ui = ui;
                this.vo = vo;
            }

            @Override
            public void run() {
                try {
                    Porn91Downloader downloader = new Porn91Downloader();
                    downloader.setProgressPerformd(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DownloadProgressHandler.DownloadProgress proc = (DownloadProgressHandler.DownloadProgress) e.getSource();
                            String kbpers = proc.getKbpers() != 0 ? proc.getKbpers() + "KB/s " : "";
                            String description = proc.getPercent() + "% " + kbpers + proc.getRemainDescrpition() + DownloadProgressHandler.getSummaryAverageKbps(proc);

                            // 設定進度
                            // downloadListModel.addRow(new Object[] { serail,
                            // vo.getFileName(), vo.getUrl(), vo.getFizeSize(),
                            // "", vo });
                            Vector vec = downloadListModel.getDataVector();

                            // 更新下載狀態
                            for (int row = 0; row < downloadListModel.getRowCount(); row++) {
                                if (downloadListModel.getValueAt(row, 5) == DownloadGOGO.this.vo) {
                                    downloadListModel.setValueAt(description, row, 4);
                                }
                            }

                            if (proc.isComplete()) {
                                // 完成顯示訊息
                                showCompleteMessage();

                                // 完成下載要清除
                                downloadLog.processCompleteLog(DownloadGOGO.this.vo);
                            }
                        }

                        private void showCompleteMessage() {
                            try {
                                TrayNotificationHelper.newInstance()//
                                        .title("下載完成!!")//
                                        .message(DownloadGOGO.this.vo.getFileName())//
                                        .notificationType(NotificationType.INFORMATION)//
                                        .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())//
                                        .animationType(AnimationType.FADE)//
                                        .onPanelClickCallback(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                try {
                                                    Desktop.getDesktop().open(DownloadGOGO.this.vo.getDownloadToFile());
                                                } catch (Exception ex) {
                                                }
                                            }
                                        }).show(2000);
                            } catch (Exception ex) {
                                sysutil.displayMessage("下載完成!!", DownloadGOGO.this.vo.getFileName(), MessageType.INFO);
                            }
                        }
                    });
                    downloader.processDownload(this.vo.orign, this.vo.downloadToFile.getParentFile(), null);
                } catch (Throwable e) {
                    JCommonUtil.handleException(this.vo.getFileName() + "失敗!", e);
                }
            }
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(FacebookVideoDownloader.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FacebookVideoDownloader frame = new FacebookVideoDownloader();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FacebookVideoDownloader() {
        this.setAutoRequestFocus(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 680, 512);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("下載設定", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel_1.add(panel, BorderLayout.NORTH);

        JLabel label = new JLabel("網址");
        panel.add(label);

        urlText = new JTextField();
        urlText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                urlTextMouseAction(arg0);
            }
        });
        panel.add(urlText);
        urlText.setColumns(35);

        JButton clearUrlConfigBtn = new JButton("清除");
        clearUrlConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearUrlConfigBtnAction();
            }
        });

        JButton urlDetectBtn = new JButton("偵測");
        urlDetectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                urlTextOnBlur(false);
            }
        });
        panel.add(urlDetectBtn);
        panel.add(clearUrlConfigBtn);

        JButton autoDownloadBtn = new JButton("自動");
        autoDownloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autoDownload(false);
            }
        });
        panel.add(autoDownloadBtn);
        urlText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    urlTextOnBlur(false);
                }
            }
        });

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.WEST);

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.EAST);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.SOUTH);

        videoTable = new JTable();
        JTableUtil.defaultSetting_AutoResize(videoTable);
        panel_1.add(JCommonUtil.createScrollComponent(videoTable), BorderLayout.CENTER);

        JPanel panel_5 = new JPanel();
        tabbedPane.addTab("Cookie設定", null, panel_5, null);
        panel_5.setLayout(new BorderLayout(0, 0));

        JPanel panel_6 = new JPanel();
        panel_5.add(panel_6, BorderLayout.NORTH);

        JLabel lblcookie = new JLabel("貼上Cookie設定");
        panel_6.add(lblcookie);

        JLabel label_1 = new JLabel("同時下載數");
        panel_6.add(label_1);

        downloadThreadSizeText = new JTextField();
        downloadThreadSizeText.setText(String.valueOf(downloadPool.getMaxSize()));
        panel_6.add(downloadThreadSizeText);
        downloadThreadSizeText.setColumns(10);
        downloadThreadSizeText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                    } catch (Exception ex) {
                    }
                }
            }
        });

        JPanel panel_7 = new JPanel();
        panel_5.add(panel_7, BorderLayout.WEST);

        JPanel panel_8 = new JPanel();
        panel_5.add(panel_8, BorderLayout.EAST);

        JPanel panel_9 = new JPanel();
        panel_5.add(panel_9, BorderLayout.SOUTH);

        JButton clearCookieBtn = new JButton("清除");
        clearCookieBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cookieArea.setText("");
            }
        });
        panel_9.add(clearCookieBtn);

        cookieArea = new JTextArea();
        panel_5.add(JCommonUtil.createScrollComponent(cookieArea), BorderLayout.CENTER);

        JPanel panel_18 = new JPanel();
        tabbedPane.addTab("檔頭設定", null, panel_18, null);
        panel_18.setLayout(new BorderLayout(0, 0));

        JPanel panel_19 = new JPanel();
        panel_18.add(panel_19, BorderLayout.NORTH);

        JPanel panel_20 = new JPanel();
        panel_18.add(panel_20, BorderLayout.WEST);

        JPanel panel_21 = new JPanel();
        panel_18.add(panel_21, BorderLayout.EAST);

        JPanel panel_22 = new JPanel();
        panel_18.add(panel_22, BorderLayout.SOUTH);

        headerTextArea = new JTextArea();
        panel_18.add(JCommonUtil.createScrollComponent(headerTextArea), BorderLayout.CENTER);

        JPanel panel_15 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_15, null);
        panel_15.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel label_2 = new JLabel("下載位置");
        panel_15.add(label_2, "2, 2, right, default");

        targetDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(targetDirText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = (File) e.getSource();
                if (file.isDirectory()) {
                    targetDirectory = file;
                }
            }
        });
        panel_15.add(targetDirText, "4, 2, fill, default");
        targetDirText.setColumns(10);

        JPanel panel_17 = new JPanel();
        panel_15.add(panel_17, "4, 4, fill, fill");

        JButton batchDownloadBtn = new JButton("批量下載");
        batchDownloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                batchAutoDownloadBtnAction(null);
            }
        });
        panel_17.add(batchDownloadBtn);

        JButton continueLastestDownloadBtn = new JButton("繼續上次下載");
        continueLastestDownloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                continueLastestDownloadBtnAction();
            }
        });
        panel_17.add(continueLastestDownloadBtn);

        panel_23 = new JPanel();
        panel_15.add(panel_23, "4, 6, fill, fill");

        JPanel panel_16 = new JPanel();
        panel_15.add(panel_16, "4, 34, fill, fill");

        JButton button = new JButton("儲存設定");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfigBtnAction();
            }
        });
        panel_16.add(button);

        JPanel panel_10 = new JPanel();
        tabbedPane.addTab("下載清單", null, panel_10, null);
        panel_10.setLayout(new BorderLayout(0, 0));

        JPanel panel_11 = new JPanel();
        panel_10.add(panel_11, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("下載清單");
        panel_11.add(lblNewLabel);

        JButton saveDownloadListBtn = new JButton("儲存清單");
        saveDownloadListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveDownloadListBtnAction();
            }
        });
        panel_11.add(saveDownloadListBtn);

        JButton cleanDownloadListBtn = new JButton("清除已完成");
        cleanDownloadListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cleanDownloadListBtnAction();
            }
        });
        panel_11.add(cleanDownloadListBtn);

        JPanel panel_12 = new JPanel();
        panel_10.add(panel_12, BorderLayout.WEST);

        JPanel panel_13 = new JPanel();
        panel_10.add(panel_13, BorderLayout.EAST);

        JPanel panel_14 = new JPanel();
        panel_10.add(panel_14, BorderLayout.SOUTH);

        downloadListTable = new JTable();
        downloadListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JTableUtil jTab = JTableUtil.newInstance(downloadListTable);
                    if (JMouseEventUtil.buttonLeftClick(2, e)) {
                        int row = JTableUtil.getRealRowPos(jTab.getSelectedRow(), downloadListTable);
                        VideoUrlConfigZ vo = (VideoUrlConfigZ) jTab.getModel().getValueAt(row, DownloadTableConfig.VO.ordinal());
                        if (!vo.downloadToFile.exists()) {
                            JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在或被移除\n" + vo.downloadToFile);
                            return;
                        }
                        Desktop.getDesktop().open(vo.downloadToFile);
                    }
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        final int row = JTableUtil.getRealRowPos(jTab.getSelectedRow(), downloadListTable);
                        final VideoUrlConfigZ vo = (VideoUrlConfigZ) jTab.getModel().getValueAt(row, DownloadTableConfig.VO.ordinal());

                        JPopupMenuUtil.newInstance(downloadListTable)//
                                .addJMenuItem("重新下載", new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (downloadPool.getState() == Thread.State.TERMINATED) {
                                            downloadPool = new DownloadThreadPoolWatcher();
                                            downloadPool.start();
                                        }
                                        ((DefaultTableModel) downloadListTable.getModel()).removeRow(row);
                                        downloadPool.downloadLst.add(vo);
                                    }
                                })//
                                .applyEvent(e)//
                                .show();
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        JTableUtil.defaultSetting_AutoResize(downloadListTable);
        panel_10.add(JCommonUtil.createScrollComponent(downloadListTable), BorderLayout.CENTER);

        JCommonUtil.setJFrameDefaultSetting(this);
        JCommonUtil.setLocationToRightBottomCorner(this);

        initDownloadListTable();

        config.reflectInit(this);

        // 設定下載目錄
        File testTargetDir = new File(targetDirText.getText());
        if (testTargetDir.exists() && testTargetDir.isDirectory()) {
            targetDirectory = testTargetDir;
        }

        sysutil = HideInSystemTrayHelper.newInstance();
        sysutil.apply(this, "Facebook下載", "resource/images/ico/facebook.ico");
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/facebook.ico");

        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);

        panel_23.add(jFrameRGBColorPanel.getToggleButton(false));
    }

    private enum DownloadTableConfig {
        順序(5f), //
        檔名(60f), //
        URL(20f), //
        大小(8f), //
        進度(7f), //
        VO(0f),//
        ;

        final float width;

        DownloadTableConfig(float width) {
            this.width = width;
        }

        private static float[] getWidth() {
            float[] arry = new float[DownloadTableConfig.values().length];
            for (DownloadTableConfig e : DownloadTableConfig.values()) {
                arry[e.ordinal()] = e.width;
            }
            return arry;
        }

        private static String[] getTitle() {
            String[] arry = new String[DownloadTableConfig.values().length];
            for (DownloadTableConfig e : DownloadTableConfig.values()) {
                arry[e.ordinal()] = e.name();
            }
            return arry;
        }
    }

    private void initDownloadListTable() {
        downloadListModel = JTableUtil.createModel(true, DownloadTableConfig.getTitle());
        downloadListTable.setModel(downloadListModel);
        JTableUtil.setColumnWidths_Percent(downloadListTable, DownloadTableConfig.getWidth());
        downloadPool.start();
        JTableUtil.newInstance(downloadListTable).hiddenColumn(DownloadTableConfig.VO.name());
    }

    private void urlTextOnBlur(final boolean throwEx) {
        final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = StringUtils.trimToEmpty(urlText.getText()).replaceAll("\t", "");
                    String cookieContent = StringUtils.trimToEmpty(cookieArea.getText());
                    String headerContent = StringUtils.trimToEmpty(headerTextArea.getText());
                    if (StringUtils.isBlank(url)) {
                        return;
                    }

                    List<VideoUrlConfig> list = new ArrayList<VideoUrlConfig>();

                    String youtubeId = JavaYoutubeVideoUrlHandler.getYoutubeID(url);
                    System.out.println("youtubeId == " + youtubeId);
                    if (StringUtils.isNotBlank(youtubeId)) {
                        JavaYoutubeVideoUrlHandler youtube = new JavaYoutubeVideoUrlHandler(youtubeId, "", JavaYoutubeVideoUrlHandler.DEFAULT_USER_AGENT);
                        youtube.execute();
                        list = youtube.getVideoFor91Lst();
                    } else {
                        list = downloader.processVideoLst(url, cookieContent, headerContent);
                        list.addAll(downloader.processVideoLst("\"" + url + "\""));
                    }

                    if (list.isEmpty()) {
                        if (throwEx) {
                            throw new RuntimeException("找步道影片 : " + url);
                        } else {
                            JCommonUtil._jOptionPane_showMessageDialog_InvokeLater("找不到影片!", "", true);
                        }
                        return;
                    }

                    DefaultTableModel model = JTableUtil.createModel(true, new String[] { "檔名", "URL", "大小", "下載", "VO" });
                    videoTable.setModel(model);
                    JTableUtil.newInstance(videoTable).columnIsButton("下載");
                    JTableUtil.setColumnWidths_Percent(videoTable, new float[] { 60f, 20f, 10f, 10f, 0f });
                    JTableUtil.newInstance(videoTable).hiddenColumn("VO");

                    for (int ii = 0; ii < list.size(); ii++) {
                        final VideoUrlConfig vo = list.get(ii);

                        JButton b1 = new JButton("download");
                        b1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                appendToDownloadBar(vo, throwEx);
                            }
                        });

                        model.addRow(new Object[] { //
                                vo.getFileName(), //
                                vo.getUrl(), //
                                vo.getFizeSize(), ////
                                b1, //
                                vo,//
                        });////
                    }
                } catch (Exception ex) {
                    if (throwEx) {
                        throw new RuntimeException("urlTextOnBlur ERR : " + ex.getMessage(), ex);
                    } else {
                        JCommonUtil.handleException(ex);
                    }
                } finally {
                    queue.offer("done..");
                }
            }
        }).start();

        final int SEC = 60;
        try {
            queue.poll(SEC * 1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            String message = "時間超出範圍 " + SEC + "秒!!";
            if (throwEx) {
                throw new RuntimeException(message, ex);
            } else {
                JCommonUtil.handleException(message, ex);
            }
        }
    }

    private void appendToDownloadBar(VideoUrlConfig vo, boolean throwEx) {
        boolean findOk1 = false;
        for (int ii = 0; ii < downloadPool.downloadLst.size(); ii++) {
            VideoUrlConfigZ vo2 = downloadPool.downloadLst.get(ii);
            if (StringUtils.equals(vo2.getUrl(), vo.getUrl())) {
                findOk1 = true;
                break;
            }
        }

        boolean findOk2 = false;
        for (int ii = 0; ii < downloadListModel.getRowCount(); ii++) {
            VideoUrlConfigZ vo2 = (VideoUrlConfigZ) downloadListModel.getValueAt(ii, 5);
            if (StringUtils.equals(vo2.getUrl(), vo.getUrl())) {
                findOk2 = true;
                break;
            }
        }

        File willingDownloadFile = new File(targetDirectory, vo.getFileName());
        if (willingDownloadFile.exists()) {
            if (throwEx) {
                throw new RuntimeException("檔案已存在目的! : " + willingDownloadFile);
            } else {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在目的!");
            }
            return;
        }

        if (!findOk1 && !findOk2) {
            VideoUrlConfigZ vo2 = new VideoUrlConfigZ(vo);
            vo2.downloadToFile = willingDownloadFile;

            // add to pool
            downloadPool.downloadLst.add(vo2);

            // append jtable
            {
                int serail = downloadListModel.getRowCount() + 1;
                Vector vec = new Vector();
                vec.add(serail);
                vec.add(vo.getFileName());
                vec.add(vo.getUrl());
                vec.add(vo.getFizeSize());
                vec.add("");
                vec.add(vo2);
                downloadListModel.addRow(vec);
            }

            // 每次下載紀錄
            downloadLog.appendDownloadVO(vo2);

            sysutil.displayMessage("開始下載", vo2.getFileName(), MessageType.INFO);
        } else {
            if (throwEx) {
                // do nothing
            } else {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案已正在下載!");
            }
        }
    }

    private void clearUrlConfigBtnAction() {
        urlText.setText("");
        videoTable.setModel(JTableUtil.createModel(true, new String[0]));
    }

    private void saveConfigBtnAction() {
        try {
            config.reflectSetConfig(FacebookVideoDownloader.this);
            config.store();
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void autoDownload(boolean throwEx) {
        try {
            urlTextOnBlur(throwEx);

            JTableUtil jTab = JTableUtil.newInstance(videoTable);
            DefaultTableModel model = jTab.getModel();

            long maxSize = -1;
            VideoUrlConfig tempVo = null;
            JButton btn = new JButton();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                final VideoUrlConfig vo = (VideoUrlConfig) model.getValueAt(ii, 4);
                final JButton btn2 = (JButton) model.getValueAt(ii, 3);
                if (tempVo != null) {
                    maxSize = tempVo.getLength();
                }
                if (vo.getLength() > maxSize) {
                    tempVo = vo;
                    btn = btn2;
                }
            }

            btn.doClick();
        } catch (Exception ex) {
            crashProcess(null, false);
            if (throwEx) {
                throw new RuntimeException(ex);
            } else {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private void batchAutoDownloadBtnAction(List<String> configLst) {
        StringBuffer sb = new StringBuffer();
        try {
            List<String> urLst = null;
            if (configLst == null) {
                File saveFile = JCommonUtil._jFileChooser_selectFileOnly();
                if (!saveFile.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return;
                }
                urLst = FileUtil.loadFromFile_asList(saveFile, "UTF-8");
            } else {
                urLst = configLst;
            }

            for (int ii = 0; ii < urLst.size(); ii++) {
                String url = StringUtils.trimToEmpty(urLst.get(ii));
                sb.append(String.format("<<%d>> : %s", ii, url) + "\n");
                if (StringUtils.isNotBlank(url)) {
                    try {
                        urlText.setText(url);
                        autoDownload(true);
                    } catch (Exception ex) {
                        sb.append("Err : \n" + parseToString(ex));
                    }
                }
            }

            String logName = "facebook_log_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm") + ".txt";
            File logFile = new File(FileUtil.DESKTOP_PATH, logName);
            FileUtil.saveToFile(logFile, sb.toString(), "UTF-8");
        } catch (Exception ex) {
            crashProcess(null, false);
            JCommonUtil.handleException(ex);
        }
    }

    private void crashProcess(String filename, boolean throwEx) {
        try {
            // downloadListModel = JTableUtil.createModel(true, new String[] {
            // "順序", "檔名", "URL", "大小", "進度", "VO" });
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < downloadListModel.getRowCount(); ii++) {
                VideoUrlConfigZ vo2 = (VideoUrlConfigZ) downloadListModel.getValueAt(ii, 5);
                String percent = (String) downloadListModel.getValueAt(ii, 4);
                sb.append( //
                        vo2.getOrign().getOrignConfig().getOrignUrl() + "\t" + //
                                vo2.getFileName() + "\t" + //
                                vo2.getFileSize() + "\t" + //
                                percent + "\n"//
                );
            }
            String logFileName = this.getClass().getSimpleName() + "_crash_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".log";
            FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, logFileName), sb.toString(), "UTF-8");
        } catch (Exception ex) {
            if (!throwEx) {
                ex.printStackTrace();
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    private void saveDownloadListBtnAction() {
        try {
            String logFileName = this.getClass().getSimpleName() + "_crash_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".log";
            logFileName = JCommonUtil._jOptionPane_showInputDialog("儲存清單! 檔名:", logFileName);
            if (StringUtils.isBlank(logFileName)) {
                logFileName = null;
            }
            crashProcess(logFileName, true);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void cleanDownloadListBtnAction() {
        try {
            Pattern ptn = Pattern.compile("100\\%.*");
            for (int ii = 0; ii < downloadListModel.getRowCount(); ii++) {
                String percent = (String) downloadListModel.getValueAt(ii, 4);
                if (ptn.matcher(percent).find()) {
                    downloadListModel.removeRow(ii);
                    ii--;
                }
            }
            JCommonUtil._jOptionPane_showMessageDialog_info("完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private String parseToString(Throwable ge) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ge.printStackTrace(pw);
        Throwable parentThrowEx = ge;
        while ((parentThrowEx = parentThrowEx.getCause()) != null) {
            parentThrowEx.printStackTrace(pw);
        }
        return sw.toString();
    }

    private class DownloadLogKeeper {
        private File logFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), FacebookVideoDownloader.class.getSimpleName() + "_downloadLog.cfg");
        // private File logFile = new
        // File("E:/my_tool/FacebookVideoDownloader_downloadLog.cfg");
        private PropertiesUtilBean config = new PropertiesUtilBean(logFile);

        private void continueNotOk() {
            config = new PropertiesUtilBean(logFile);
            List<String> keyLst = new ArrayList<String>();
            for (Object k : config.getConfigProp().keySet()) {
                keyLst.add((String) k);
            }
            Collections.sort(keyLst);
            List<String> continueLst = new ArrayList<String>();
            for (String key : keyLst) {
                String url = config.getConfigProp().getProperty(key);
                continueLst.add(url);
                System.out.println("-> " + url);
            }
            batchAutoDownloadBtnAction(continueLst);
        }

        private void appendDownloadVO(VideoUrlConfigZ vo) {
            config = new PropertiesUtilBean(logFile);
            String key = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + "_" + vo.getFileName();
            String valueUrl = vo.getOrign().getOrignConfig().getOrignUrl();
            config.getConfigProp().setProperty(key, valueUrl);
            config.store();
        }

        private void processCompleteLog(VideoUrlConfigZ vo) {
            config = new PropertiesUtilBean(logFile);
            for (Enumeration enu = config.getConfigProp().keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                String url = config.getConfigProp().getProperty(key);
                if (StringUtils.equals(vo.getOrign().getOrignConfig().getOrignUrl(), url)) {
                    config.getConfigProp().remove(key);
                    config.store();
                }
            }
        }
    }

    private void continueLastestDownloadBtnAction() {
        try {
            downloadLog.continueNotOk();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void urlTextMouseAction(MouseEvent arg0) {
        try {
            if (JMouseEventUtil.buttonRightClick(1, arg0)) {
                JPopupMenuUtil.newInstance(urlText)//
                        .addJMenuItem("從記事本貼上", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String content = ClipboardUtil.getInstance().getContents();
                                if (StringUtils.isBlank(content)) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("記事本為空!!");
                                    return;
                                }
                                urlText.setText(content);
                            }
                        }).applyEvent(arg0)//
                        .show();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class VideoUrlConfigZ {

        VideoUrlConfig orign;
        File downloadToFile;
        String fileName;
        String fileSize;
        String title;
        String url;
        long length = 0;

        VideoUrlConfigZ(VideoUrlConfig orign) {
            this.orign = orign;
            this.fileName = orign.getFileName();
            this.fileSize = orign.getFizeSize();
            this.title = orign.getTitle();
            this.url = orign.getUrl();
            this.length = orign.getLength();
        }

        public VideoUrlConfig getOrign() {
            return orign;
        }

        public File getDownloadToFile() {
            return downloadToFile;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileSize() {
            return fileSize;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public long getLength() {
            return length;
        }
    }
}
