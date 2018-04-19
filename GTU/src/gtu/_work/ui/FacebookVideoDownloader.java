package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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

import org.apache.commons.lang3.StringUtils;

import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;
import gtu.youtube.DownloadProgressHandler;
import gtu.youtube.Porn91Downloader;
import gtu.youtube.Porn91Downloader.VideoUrlConfig;

public class FacebookVideoDownloader extends JFrame {

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

    private class DownloadThreadPoolWatcher extends Thread {
        LinkedList<VideoUrlConfig> downloadLst = new LinkedList<VideoUrlConfig>();
        List<Thread> pool = new ArrayList<Thread>();
        int maxSize = 5;

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
            int addSize = maxSize - pool.size();
            for (int ii = 0; ii < addSize; ii++) {
                if (!downloadLst.isEmpty()) {
                    VideoUrlConfig vo = downloadLst.pop();
                    DownloadGOGO thread = new DownloadGOGO(vo, FacebookVideoDownloader.this);
                    thread.start();
                    pool.add(thread);
                }
            }
        }

        private class DownloadGOGO extends Thread {
            FacebookVideoDownloader ui;
            VideoUrlConfig vo;

            private DownloadGOGO(VideoUrlConfig vo, FacebookVideoDownloader ui) {
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
                            String percent = proc.getPercent() + "%";

                            // 設定進度
                            // downloadListModel.addRow(new Object[] { serail,
                            // vo.getFileName(), vo.getUrl(), vo.getFizeSize(),
                            // "", vo });
                            Vector vec = downloadListModel.getDataVector();// .set(4,
                                                                           // percent);

                            for (int row = 0; row < downloadListModel.getRowCount(); row++) {
                                if (downloadListModel.getValueAt(row, 5) == DownloadGOGO.this.vo) {
                                    downloadListModel.setValueAt(percent, row, 4);
                                }
                            }
                        }
                    });
                    downloader.processDownload(this.vo, 0);
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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FacebookVideoDownloader frame = new FacebookVideoDownloader();
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
    public FacebookVideoDownloader() {
        this.setAutoRequestFocus(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 614, 512);
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
        panel.add(urlText);
        urlText.setColumns(40);

        JButton clearUrlConfigBetn = new JButton("清除");
        clearUrlConfigBetn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearUrlConfigBetnAction();
            }
        });
        panel.add(clearUrlConfigBetn);
        urlText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    urlTextOnBlur();
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
        downloadThreadSizeText.setText(String.valueOf(downloadPool.maxSize));
        panel_6.add(downloadThreadSizeText);
        downloadThreadSizeText.setColumns(10);
        downloadThreadSizeText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        Integer val = Integer.parseInt(downloadThreadSizeText.getText());
                        if (val <= 0) {
                            return;
                        }
                        downloadPool.maxSize = val;
                        // JCommonUtil._jOptionPane_showMessageDialog_info("同時下載數
                        // :"
                        // + val);
                    } catch (Exception ex) {
                        downloadThreadSizeText.setText(String.valueOf(downloadPool.maxSize));
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

        JButton saveConfigBtn = new JButton("儲存設定");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfigBtnAction();
            }
        });
        panel_9.add(saveConfigBtn);
        panel_9.add(clearCookieBtn);

        cookieArea = new JTextArea();
        panel_5.add(JCommonUtil.createScrollComponent(cookieArea), BorderLayout.CENTER);

        JPanel panel_10 = new JPanel();
        tabbedPane.addTab("下載清單", null, panel_10, null);
        panel_10.setLayout(new BorderLayout(0, 0));

        JPanel panel_11 = new JPanel();
        panel_10.add(panel_11, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("下載清單");
        panel_11.add(lblNewLabel);

        JPanel panel_12 = new JPanel();
        panel_10.add(panel_12, BorderLayout.WEST);

        JPanel panel_13 = new JPanel();
        panel_10.add(panel_13, BorderLayout.EAST);

        JPanel panel_14 = new JPanel();
        panel_10.add(panel_14, BorderLayout.SOUTH);

        downloadListTable = new JTable();
        JTableUtil.defaultSetting_AutoResize(downloadListTable);
        panel_10.add(downloadListTable, BorderLayout.CENTER);

        JCommonUtil.setJFrameDefaultSetting(this);

        initDownloadListTable();

        config.reflectInit(this);
    }

    private void initDownloadListTable() {
        downloadListModel = JTableUtil.createModel(true, new String[] { "順序", "檔名", "URL", "大小", "進度", "VO" });
        downloadListTable.setModel(downloadListModel);
        JTableUtil.setColumnWidths_Percent(downloadListTable, new float[] { 5f, 60f, 20f, 8f, 7f, 0f });
        downloadPool.start();
        JTableUtil.newInstance(downloadListTable).hiddenColumn("VO");
    }

    private void urlTextOnBlur() {
        try {
            String url = urlText.getText();
            String cookieContent = StringUtils.trimToEmpty(cookieArea.getText());
            if (StringUtils.isBlank(url)) {
                return;
            }

            List<VideoUrlConfig> list = downloader.processVideoLst(url, cookieContent);
            DefaultTableModel model = JTableUtil.createModel(true, new String[] { "檔名", "URL", "大小", "下載" });
            videoTable.setModel(model);
            JTableUtil.newInstance(videoTable).columnIsButton("下載");
            JTableUtil.setColumnWidths_Percent(videoTable, new float[] { 60f, 20f, 10f, 10f });

            for (int ii = 0; ii < list.size(); ii++) {
                final VideoUrlConfig vo = list.get(ii);

                JButton b1 = new JButton("download");
                b1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        appendToDownloadBar(vo);
                    }
                });

                model.addRow(new Object[] { //
                        vo.getFileName(), //
                        vo.getUrl(), //
                        vo.getFizeSize(), ////
                        b1,//
                });////
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void appendToDownloadBar(VideoUrlConfig vo) {
        boolean findOk1 = false;
        for (int ii = 0; ii < downloadPool.downloadLst.size(); ii++) {
            VideoUrlConfig vo2 = downloadPool.downloadLst.get(ii);
            if (StringUtils.equals(vo2.getUrl(), vo.getUrl())) {
                findOk1 = true;
                break;
            }
        }

        boolean findOk2 = false;
        for (int ii = 0; ii < downloadListModel.getRowCount(); ii++) {
            VideoUrlConfig vo2 = (VideoUrlConfig) downloadListModel.getValueAt(ii, 5);
            if (StringUtils.equals(vo2.getUrl(), vo.getUrl())) {
                findOk2 = true;
                break;
            }
        }

        if (!findOk1 && !findOk2) {
            downloadPool.downloadLst.add(vo);

            // append jtable
            {
                int serail = downloadListModel.getRowCount() + 1;
                Vector vec = new Vector();
                vec.add(serail);
                vec.add(vo.getFileName());
                vec.add(vo.getUrl());
                vec.add(vo.getFizeSize());
                vec.add("");
                vec.add(vo);
                downloadListModel.addRow(vec);
            }
        } else {
            JCommonUtil._jOptionPane_showMessageDialog_info("檔案已正在下載!");
        }
    }

    private void clearUrlConfigBetnAction() {
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
}
