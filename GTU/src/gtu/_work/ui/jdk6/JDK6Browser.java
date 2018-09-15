package gtu._work.ui.jdk6;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class JDK6Browser extends JFrame implements HyperlinkListener {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private JEditorPane editorPane;
    private JRadioButton engRadio;
    private JButton menu;
    private JButton index;
    private ButtonGroup buttonGroup1;
    private JRadioButton chnRadio;

    public JDK6Browser() {
        super("JDK Browser");

        setSize(640, 480);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });

        JPanel buttonPanel = new JPanel();

        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        {
            engRadio = new JRadioButton();
            buttonPanel.add(engRadio);
            engRadio.setText("Eng");
            chnRadio = new JRadioButton();
            chnRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    radioChange(evt);
                }
            });
            buttonPanel.add(chnRadio);
            chnRadio.setText("Chn");
            buttonGroup1 = new ButtonGroup();
            buttonGroup1.add(chnRadio);
            buttonGroup1.add(engRadio);
            engRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    radioChange(evt);
                }
            });

        }
        {
            index = new JButton();
            buttonPanel.add(index);
            index.setText("return index");
            index.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    indexActionPerformed(evt);
                }
            });
        }
        {
            menu = new JButton();
            buttonPanel.add(menu);
            menu.setText("menu");
            menu.setPreferredSize(new java.awt.Dimension(85, 24));
            menu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    menuActionPerformed(evt);
                }
            });
        }
        getContentPane().add(new JScrollPane(editorPane), BorderLayout.CENTER);

        this.setLocationRelativeTo(null);

        init();
    }

    private void init() {
        this.showPage(this.getClass().getResource("/html_en/index.html"));
    }

    private void actionExit() {
        System.exit(0);
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showPage(URL pageUrl) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            editorPane.setPage(pageUrl);
        } catch (Exception e) {
            showError("Unable to load page : " + pageUrl);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent event) {
        HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document = (HTMLDocument) editorPane.getDocument();
                currentUrl = linkEvent.getURL();
                System.out.println("linkEvent = " + linkEvent.getURL());
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
                showPage(event.getURL());
                currentUrl = event.getURL();
                System.out.println("hyperlinkUpdate = " + event.getURL());
            }
        }
    }

    private URL currentUrl;

    public void setCurrentUrl(URL currentUrl) {
        this.currentUrl = currentUrl;
    }

    public static void main(String[] args) {
        JDK6Browser browser = new JDK6Browser();
         gtu.swing.util.JFrameUtil.setVisible(true,browser);
    }

    protected void radioChange(ActionEvent evt) {
        String chnBasePath = BasePath.CHN_BASE.getCurrentBasePath();
        String engBasePath = BasePath.ENG_BASE.getCurrentBasePath();

        String currentPage = currentUrl.toString();
        if (currentPage.startsWith(chnBasePath)) {
            currentPage = currentPage.replaceFirst(Pattern.quote(chnBasePath), "");
        }
        if (currentPage.startsWith(engBasePath)) {
            currentPage = currentPage.replaceFirst(Pattern.quote(engBasePath), "");
        }
        System.out.println("current page = " + currentPage);

        // JRadioButton radio = (JRadioButton) evt.getSource();
        JRadioButton radio = getSelectRadio();
        String urlString = null;
        if (radio == null) {
            urlString = engBasePath + currentPage;
        } else if (radio.getText().equals("Chn")) {
            urlString = chnBasePath + currentPage;
        } else if (radio.getText().equals("Eng")) {
            urlString = engBasePath + currentPage;
        }
        System.out.println("change to = " + urlString);
        try {
            this.showPage(new URL(urlString));
        } catch (MalformedURLException e) {
            showError("Unable to load page : " + e);
        }
    }

    private JRadioButton getSelectRadio() {
        for (Enumeration<AbstractButton> enu = buttonGroup1.getElements(); enu.hasMoreElements();) {
            AbstractButton abs = enu.nextElement();
            if (abs.isSelected()) {
                return (JRadioButton) abs;
            }
        }
        return null;
    }

    private void indexActionPerformed(ActionEvent evt) {
        init();
    }

    JDK6Menu jdk6menu;

    private void menuActionPerformed(ActionEvent evt) {
        if (jdk6menu == null) {
            try {
                jdk6menu = new JDK6Menu();
                jdk6menu.setJdk6Browser(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         gtu.swing.util.JFrameUtil.setVisible(true,jdk6menu);
    }

    private enum BasePath {
        CHN_BASE("/html_zh_CN/") {
        }, //
        ENG_BASE("/html_en/") {
        }, //
        ;//
        final String path;

        BasePath(String path) {
            this.path = path;
        }

        String getCurrentBasePath() {
            return JDK6Browser.class.getResource(path).toString();
        }
    }
}
