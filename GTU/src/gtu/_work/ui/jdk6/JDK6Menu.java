package gtu._work.ui.jdk6;

import java.io.IOException;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
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
public class JDK6Menu extends JFrame {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JEditorPane packageClass;
    private JEditorPane allPackage;

    public JDK6Menu() throws IOException {
        super("");

        URL url1 = this.getClass().getResource("/html_en/overview-frame.html");
        URL url2 = this.getClass().getResource("/html_en/allclasses-frame.html");

        this.setSize(234, 506);

        GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
        getContentPane().setLayout(thisLayout);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Menu");

        packageClass = new JEditorPane();
        allPackage = new JEditorPane();

        {
            jScrollPane1 = new JScrollPane();
            {
                jScrollPane1.setViewportView(allPackage);
                allPackage.setContentType("text/html");
                allPackage.setEditable(false);
                allPackage.setPage(url1);
                allPackage.addHyperlinkListener(new LinkChange(allPackage) {
                    void process(HyperlinkEvent event) throws IOException {
                        packageClass.setPage(this.currentPage);
                    }
                });
            }
        }
        {
            jScrollPane2 = new JScrollPane();
            {
                jScrollPane2.setViewportView(packageClass);
                packageClass.setContentType("text/html");
                packageClass.setPage(url2);
                packageClass.setEditable(false);
                packageClass.addHyperlinkListener(new LinkChange(packageClass) {
                    void process(HyperlinkEvent event) throws IOException {
                        jdk6Browser.setCurrentUrl(this.currentPage);
                        jdk6Browser.radioChange(null);
                        dispose();
                    }
                });
            }
        }

        thisLayout.setHorizontalGroup(thisLayout
                .createSequentialGroup()
                .addContainerGap(12, 12)
                .addGroup(
                        thisLayout
                                .createParallelGroup()
                                .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
                                        194, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
                                        194, GroupLayout.PREFERRED_SIZE)).addContainerGap(12, 12));
        thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap(12, 12)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, 12));

    }

    static abstract class LinkChange implements HyperlinkListener {

        protected URL currentPage;

        protected JEditorPane editor;

        LinkChange(JEditorPane editor) {
            this.editor = editor;
        }

        abstract void process(HyperlinkEvent event) throws IOException;

        public void hyperlinkUpdate(HyperlinkEvent event) {
            JEditorPane editor = (JEditorPane) event.getSource();
            HyperlinkEvent.EventType eventType = event.getEventType();
            if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
                if (event instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
                    HTMLDocument document = (HTMLDocument) editor.getDocument();
                    System.out.println("linkEvent = " + linkEvent.getURL());
                    document.processHTMLFrameHyperlinkEvent(linkEvent);
                    currentPage = linkEvent.getURL();
                } else {
                    System.out.println("hyperlinkUpdate = " + event.getURL());
                    currentPage = event.getURL();
                }
                try {
                    this.process(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private JDK6Browser jdk6Browser;

    public void setJdk6Browser(JDK6Browser jdk6Browser) {
        this.jdk6Browser = jdk6Browser;
    }

    public static void main(String[] args) throws IOException {
        JDK6Menu browser = new JDK6Menu();
        browser.setVisible(true);
    }
}