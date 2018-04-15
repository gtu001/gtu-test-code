package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.AutoComboBox;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;

public class BrowserHistoryHandlerUI extends JFrame {
    private JTextField titleText;
    private JTextField urlText;
    private JLabel modifyTimeLabel;
    private PropertiesUtilBean config = new PropertiesUtilBean(BrowserHistoryHandlerUI.class);
    private JComboBox tagComboBox;
    private JTextArea remarkArea;
    private JTable urlTable;

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BrowserHistoryHandlerUI frame = new BrowserHistoryHandlerUI();
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
    public BrowserHistoryHandlerUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            getContentPane().add(tabbedPane, BorderLayout.CENTER);

            JPanel panel = new JPanel();
            tabbedPane.addTab("New tab", null, panel, null);
            panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

            JLabel lblTitle = new JLabel("title");
            panel.add(lblTitle, "2, 2, right, default");

            titleText = new JTextField();
            panel.add(titleText, "4, 2, fill, default");
            titleText.setColumns(10);

            JLabel lblUrl = new JLabel("url");
            panel.add(lblUrl, "2, 4, right, default");

            urlText = new JTextField();
            urlText.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    urlTextOnblur();
                }
            });
            panel.add(urlText, "4, 4, fill, default");
            urlText.setColumns(10);

            JLabel lblTag = new JLabel("tag");
            panel.add(lblTag, "2, 6, right, default");

            tagComboBox = new JComboBox();
            panel.add(tagComboBox, "4, 6, fill, default");

            JLabel lblRemark = new JLabel("remark");
            panel.add(lblRemark, "2, 8");

            JScrollPane scrollPane = new JScrollPane();
            panel.add(scrollPane, "4, 8, fill, fill");

            remarkArea = new JTextArea();
            scrollPane.setViewportView(remarkArea);

            JPanel panel_2 = new JPanel();
            panel.add(panel_2, "4, 10, fill, fill");

            modifyTimeLabel = new JLabel("修改時間");
            panel_2.add(modifyTimeLabel);

            JButton saveBtn = new JButton("儲存");
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveConfigBtnAction();
                }
            });
            panel_2.add(saveBtn);

            JButton deleteBtn = new JButton("刪除");
            deleteBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteBtnAction();
                }
            });
            panel_2.add(deleteBtn);

            JButton clearBtn = new JButton("清空");
            clearBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearBtnAction();
                }
            });
            panel_2.add(clearBtn);

            JPanel panel_1 = new JPanel();
            tabbedPane.addTab("New tab", null, panel_1, null);
            panel_1.setLayout(new BorderLayout(0, 0));

            JLabel lblNewLabel = new JLabel("           ");
            panel_1.add(lblNewLabel, BorderLayout.NORTH);

            JLabel lblNewLabel_1 = new JLabel("           ");
            panel_1.add(lblNewLabel_1, BorderLayout.SOUTH);

            JLabel lblNewLabel_2 = new JLabel("           ");
            panel_1.add(lblNewLabel_2, BorderLayout.WEST);

            JLabel lblNewLabel_3 = new JLabel("           ");
            panel_1.add(lblNewLabel_3, BorderLayout.EAST);

            urlTable = new JTable();
            JTableUtil.defaultSetting_AutoResize(urlTable);
            panel_1.add(JCommonUtil.createScrollComponent(urlTable), BorderLayout.CENTER);

            initLoading();

            pack();
            this.setSize(800, 450);

            JCommonUtil.setJFrameDefaultSetting(this);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void saveConfigBtnAction() {
        try {
            String url = StringUtils.trimToEmpty(urlText.getText());
            String title = StringUtils.trimToEmpty(titleText.getText());
            String tag = StringUtils.trimToEmpty(tagComboBox.getSelectedItem().toString());
            String remark = StringUtils.trimToEmpty(remarkArea.getText().toString());
            String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");

            Validate.notEmpty(url, "url 為空");
            Validate.notEmpty(title, "title 為空");
            Validate.notEmpty(tag, "tag 為空");

            UrlConfig d = new UrlConfig();
            d.url = url;
            d.title = title;
            d.tag = tag;
            d.remark = remark;
            d.timestamp = timestamp;

            config.getConfigProp().setProperty(url, UrlConfig.getConfigValue(d));
            config.store();

            this.initLoading();

            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void clearBtnAction() {
        urlText.setText("");
        titleText.setText("");
        AutoComboBox.setText(tagComboBox, "");
        remarkArea.setText("");
        modifyTimeLabel.setText("");
    }

    private void initLoading() {
        List<String> tagLst = new ArrayList<String>();
        List<UrlConfig> lst = new ArrayList<UrlConfig>();

        JTableUtil tableUtil = JTableUtil.newInstance(urlTable);

        DefaultTableModel model = JTableUtil.createModel(//
                true, new String[] { "title", "url", "tag", "timestamp", "remark", "選取", "刪除", "開啟" });
        urlTable.setModel(model);
        for (String v : new String[] { "選取", "刪除", "開啟" }) {
            tableUtil.columnIsButton(v);
        }

        for (Enumeration<?> enu = config.getConfigProp().keys(); enu.hasMoreElements();) {
            String url = (String) enu.nextElement();
            String title_tag_remark_time = config.getConfigProp().getProperty(url);

            UrlConfig d = UrlConfig.parseTo(url, title_tag_remark_time);
            lst.add(d);
            tagLst.add(d.tag);
        }

        // 設定tag 夏拉
        Collections.sort(tagLst);
        AutoComboBox.applyAutoComboBox(tagComboBox, tagLst);

        // 設定urlTable
        Collections.sort(lst, new Comparator<UrlConfig>() {
            @Override
            public int compare(UrlConfig o1, UrlConfig o2) {
                return o1.timestamp.compareTo(o2.timestamp);
            }
        });

        for (final UrlConfig d : lst) {
            JButton delBtn = new JButton("刪除");
            delBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("click 刪除");
                    String message = "title : " + d.title + "\n" + //
                    "url : " + d.url + "\n" + //
                    "tag : " + d.tag + "\n" + //
                    "remark : " + d.remark + "\n" + //
                    "timestamp : " + d.timestamp + "\n" + //
                    "";
                    boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(message, "確定刪除");
                    if (!result) {
                        return;
                    }
                    config.getConfigProp().remove(d.url);
                    initLoading();
                    JCommonUtil._jOptionPane_showMessageDialog_info("刪除成功!");
                }
            });
            JButton goBtn = new JButton("開啟");
            goBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("click 開啟");
                    try {
                        Desktop.getDesktop().browse(new URI(d.url));
                    } catch (Exception e1) {
                        JCommonUtil.handleException(e1);
                    }
                }
            });
            JButton choiceBtn = new JButton("選取");
            choiceBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("click 選取");
                    System.out.println(ReflectionToStringBuilder.toString(d));
                    urlText.setText(d.url);
                    titleText.setText(d.title);
                    remarkArea.setText(d.remark);
                    modifyTimeLabel.setText(d.timestamp);
                    AutoComboBox.setText(tagComboBox, d.tag);
                }
            });
            model.addRow(new Object[] { d.title, d.url, d.tag, d.timestamp, d.remark, choiceBtn, delBtn, goBtn });
        }
    }

    private void deleteBtnAction() {
        try {
            System.out.println("click 刪除");
            String message = "title : " + titleText.getText() + "\n" + //
                    "url : " + urlText.getText() + "\n" + //
                    "tag : " + tagComboBox.getSelectedItem() + "\n" + //
                    "remark : " + remarkArea.getText() + "\n" + //
                    "timestamp : " + modifyTimeLabel.getText() + "\n" + //
                    "";
            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(message, "確定刪除");
            if (!result) {
                return;
            }

            if (config.getConfigProp().containsKey(urlText.getText())) {
                config.getConfigProp().remove(urlText.getText());
                initLoading();
                JCommonUtil._jOptionPane_showMessageDialog_info("移除成功!");
                return;
            }
            JCommonUtil._jOptionPane_showMessageDialog_info("找不到此設定!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void urlTextOnblur() {
        try {
            if (StringUtils.isBlank(urlText.getText())) {
                return;
            }
            String title = getHtmlTitle(urlText.getText());
            titleText.setText(title);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private static class UrlConfig {
        String title;
        String url;
        String tag;
        String remark;
        String timestamp;

        private static String getConfigValue(UrlConfig d) {
            return d.title + "^" + d.tag + "^" + d.remark + "^" + d.timestamp;
        }

        private static UrlConfig parseTo(String key, String title_tag_remark_time) {
            System.out.println("title_tag_remark_time = " + title_tag_remark_time);
            String[] args = StringUtils.trimToEmpty(title_tag_remark_time).split("\\^", -1);
            System.out.println("length = " + args.length);
            if (args.length == 4) {
                String title = args[0];
                String tag = args[1];
                String remark = args[2];
                String timestamp = args[3];

                UrlConfig d = new UrlConfig();
                d.title = title;
                d.tag = tag;
                d.remark = remark;
                d.timestamp = timestamp;
                d.url = key;

                return d;
            }
            throw new RuntimeException("無法取得設定 : " + key + " -> " + title_tag_remark_time);
        }
    }

    private String getHtmlTitle(String url) throws IOException {
        String title = "";
        try {
            String content = __doGetRequest_UserAgent(url, "UTF-8", DEFAULT_USER_AGENT);
            System.out.println("-----------------------------------------------------------------------");
            // System.out.println(content);

            Pattern ptn = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(content);
            if (mth.find()) {
                title = mth.group(1);
                title = StringEscapeUtils.unescapeHtml(title);
                System.out.println(title);
            }
            System.out.println("-----------------------------------------------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return title;
    }

    public static String __doGetRequest_UserAgent(String url, String encoding, String userAgent) {
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            URI uri = new URI(url);

            CookieStore cookieStore = new BasicCookieStore();
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            if (userAgent != null && userAgent.length() > 0) {
                httpget.setHeader("User-Agent", userAgent);
            }

            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                InputStream instream = entity.getContent();
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(instream, encoding));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    instream.close();
                }
                String result = writer.toString();
                return result;
            } else {
                String errMsg = "StatusCode : " + response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase();
                throw new RuntimeException(errMsg);
            }
        } catch (Exception ex) {
            throw new RuntimeException("doGetRequest_UserAgent Err : " + ex.getMessage(), ex);
        }
    }

    private static String __doGetRequest(String urlStr, String encode) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader isr = null;
        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null)
                return "";

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= Integer.MAX_VALUE) {
                    break;
                }
            }

            return response.toString();

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
