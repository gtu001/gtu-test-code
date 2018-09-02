package gtu.qrcode;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.file.FileUtil;
import gtu.image.ImageUtil;
import gtu.swing.util.JCommonUtil;

public class QRCodeMakerUI extends JFrame {

    private JPanel contentPane;
    private JTextField destionText;
    private JTextArea textArea;
    private JLabel qrCodePngLabel;
    private File picFile;
    private JPanel panel_1;
    private JLabel lblNewLabel;
    private JTextField scaledText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    QRCodeMakerUI frame = new QRCodeMakerUI();
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
    public QRCodeMakerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 658, 510);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("txt", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);

        JLabel lblpng = new JLabel("目的PNG:");
        panel_2.add(lblpng);

        destionText = new JTextField();
        panel_2.add(destionText);
        destionText.setColumns(25);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(destionText, false);

        JButton makeBtn1 = new JButton("產生");
        makeBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeBtn1Action();
            }
        });
        
        lblNewLabel = new JLabel("寬高:");
        panel_2.add(lblNewLabel);
        
        scaledText = new JTextField();
        scaledText.setText("1200");
        panel_2.add(scaledText);
        scaledText.setColumns(10);
        panel_2.add(makeBtn1);

        textArea = new JTextArea();
        panel.add(JCommonUtil.createScrollComponent(textArea), BorderLayout.CENTER);

        panel_1 = new JPanel();
        tabbedPane.addTab("QR", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        qrCodePngLabel = new JLabel("");
        panel_1.add(qrCodePngLabel, BorderLayout.CENTER);

        //form resize
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                setImageToLabel(picFile);
            }
        });
    }

    private void setImageToLabel(File picFile) {
        try {
            if(picFile == null) {
                return;
            }
            BufferedImage image = ImageIO.read(picFile);
            int resizeWidth = Math.min(panel_1.getWidth(), panel_1.getHeight());
            Image image2 = ImageUtil.getInstance().getScaledImage(image, resizeWidth, resizeWidth);
            ImageIcon icon = new ImageIcon(image2);
            qrCodePngLabel.setIcon(icon);
        } catch (IOException e) {
            JCommonUtil.handleException(e);
        }
    }

    private void makeBtn1Action() {
        try {
            picFile = new File(destionText.getText());
            if (picFile.exists()) {
                String content = QRCodeUtil.getInstance().readQRCode(picFile);
                textArea.setText(content);
                setImageToLabel(picFile);
                JCommonUtil._jOptionPane_showMessageDialog_info("QRCode轉成文字成功!");
            } else {
                if (StringUtils.isBlank(destionText.getText())) {
                    picFile = new File(FileUtil.DESKTOP_PATH, "QRCode_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".png");
                }
                String content = StringUtils.trimToEmpty(textArea.getText());
                if (StringUtils.isBlank(content)) {
                    Validate.isTrue(false, "請輸入文字!");
                }
                if(StringUtils.isBlank(scaledText.getText()) || !StringUtils.isNumeric(scaledText.getText())) {
                    Validate.isTrue(false, "寬高必須為數值");
                }
                int resizeWidth = Integer.parseInt(StringUtils.defaultIfEmpty(scaledText.getText(), "1200"));
                QRCodeUtil.getInstance().createQRCode(content, "png", picFile, resizeWidth);
                destionText.setText(picFile.getAbsolutePath());
                setImageToLabel(picFile);
                JCommonUtil._jOptionPane_showMessageDialog_info("QRCode圖片產生成功 : " + picFile);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
