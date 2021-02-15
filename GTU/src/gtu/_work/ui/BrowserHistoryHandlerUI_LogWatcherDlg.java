package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MouseInputAdapter;

import org.apache.commons.lang.StringUtils;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextPaneTextStyle;
import gtu.swing.util.JTextPaneUtil;
import gtu.swing.util.KeyEventUtil;

public class BrowserHistoryHandlerUI_LogWatcherDlg extends JDialog {

    private static final long serialVersionUID = 3618609891018335257L;
    private final JPanel contentPanel = new JPanel();
    private JTextPane logWatcherTextArea;
    private YellowMarkJTextPaneHandler mYellowMarkJTextPaneHandler;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            BrowserHistoryHandlerUI_LogWatcherDlg dialog = new BrowserHistoryHandlerUI_LogWatcherDlg();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BrowserHistoryHandlerUI_LogWatcherDlg newInstance() {
        BrowserHistoryHandlerUI_LogWatcherDlg dialog = new BrowserHistoryHandlerUI_LogWatcherDlg();
        try {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    private JPanel createJDialogResize(final int width, final int height, final char nswe) {
        JPanel resizePanel = new JPanel();
        resizePanel.setPreferredSize(new Dimension(width, height));
        DragJDialogResizeListener mDragJDialogResizeListener = new DragJDialogResizeListener(this, nswe);
        resizePanel.addMouseMotionListener(mDragJDialogResizeListener);
        resizePanel.addMouseListener(mDragJDialogResizeListener);
        return resizePanel;
    }

    /**
     * Create the dialog.
     */
    public BrowserHistoryHandlerUI_LogWatcherDlg() {
        this.applyOnTopUndecorated(this);

        setBounds(100, 100, 800, 350);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(new AlphaContainer(createJDialogResize(0, 5, 'n'), null), BorderLayout.NORTH);
        contentPanel.add(new AlphaContainer(createJDialogResize(0, 5, 's'), null), BorderLayout.SOUTH);
        contentPanel.add(new AlphaContainer(createJDialogResize(5, 0, 'w'), null), BorderLayout.WEST);
        contentPanel.add(new AlphaContainer(createJDialogResize(5, 0, 'e'), null), BorderLayout.EAST);

        // contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        {
            logWatcherTextArea = new JTextPane();
            JTextAreaUtil.applyCommonSetting(logWatcherTextArea);
            JTextAreaUtil.setScrollToBottomPloicy(logWatcherTextArea);
            logWatcherTextArea.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                AtomicBoolean doClearCacheSuccess = null;

                private int getBufferSize() {
                    int rtnSize = 100000;
                    // try {
                    // rtnSize =
                    // Integer.parseInt(logWatcherBufferSizeText.getText());
                    // } catch (Exception e) {
                    // logWatcherBufferSizeText.setText(String.valueOf(rtnSize));
                    // }
                    return rtnSize;
                }

                @Override
                public void process(DocumentEvent event) {
                    int bufferSize = getBufferSize();
                    if (logWatcherTextArea.getText().length() > bufferSize) {
                        if (doClearCacheSuccess == null || (doClearCacheSuccess != null && doClearCacheSuccess.get())) {
                            System.out.println("######################################################");
                            System.out.println("###  CLEAR BUFFER                       GO        ####");
                            System.out.println("######################################################");
                            doClearCacheSuccess = JTextPaneUtil.newInstance(logWatcherTextArea).remove(0, bufferSize / 2);
                        }
                    }
                }
            }));
            logWatcherTextArea.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    logWatcherTextAreaKeyEventAction(e);
                }
            });
            contentPanel.add(JCommonUtil.createScrollComponent(logWatcherTextArea), BorderLayout.CENTER);
        }

        getContentPane().add(new AlphaContainer(contentPanel, this), BorderLayout.CENTER);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(new AlphaContainer(buttonPane, this), BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);

                okButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        dispose();
                    }

                });
            }
            {
                // JButton cancelButton = new JButton("Cancel");
                // cancelButton.setActionCommand("Cancel");
                // buttonPane.add(cancelButton);
            }
        }

        JCommonUtil.setLocationToRightBottomCorner(this);
    }

    private void logWatcherTextAreaKeyEventAction(KeyEvent e) {
        if (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_F) {
            String findText = JCommonUtil._jOptionPane_showInputDialog("搜尋text!");
            if (StringUtils.isNotEmpty(findText)) {
                mYellowMarkJTextPaneHandler = new YellowMarkJTextPaneHandler(logWatcherTextArea, findText);
                mYellowMarkJTextPaneHandler.process();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F3 || (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_K)) {
            logWatcherTextArea.setCaretPosition(Integer.valueOf(String.valueOf(mYellowMarkJTextPaneHandler.findNext(logWatcherTextArea.getCaretPosition()))));
        }
        if ((KeyEventUtil.isMaskKeyPress(e, "s") && e.getKeyCode() == KeyEvent.VK_F3) || (KeyEventUtil.isMaskKeyPress(e, "cs") && e.getKeyCode() == KeyEvent.VK_K)) {
            logWatcherTextArea.setCaretPosition(Integer.valueOf(String.valueOf(mYellowMarkJTextPaneHandler.findPrevious(logWatcherTextArea.getCaretPosition()))));
        }
    }

    private class YellowMarkJTextPaneHandler {
        JTextPane logWatcherTextArea;
        String findText;
        List<Long> findPosLst;

        private int __findCheck(int textareaPos, boolean bigger) {
            if (findPosLst == null || findPosLst.isEmpty()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("沒有找到匹配");
                return -1;
            }
            if (bigger) {
                for (int ii = 0; ii < findPosLst.size(); ii++) {
                    if (textareaPos < findPosLst.get(ii)) {
                        return ii;
                    }
                }
            } else {
                for (int ii = findPosLst.size() - 1; ii >= 0; ii--) {
                    if (textareaPos > findPosLst.get(ii)) {
                        return ii;
                    }
                }
            }
            return 0;
        }

        private Long findPrevious(int textareaPos) {
            int findPosIndex = __findCheck(textareaPos, false);
            if (findPosIndex != -1) {
                Long pos = findPosLst.get(findPosIndex);
                return pos;
            }
            return 0L;
        }

        private Long findNext(int textareaPos) {
            int findPosIndex = __findCheck(textareaPos, true);
            if (findPosIndex != -1) {
                Long pos = findPosLst.get(findPosIndex);
                return pos;
            }
            return 0L;
        }

        private YellowMarkJTextPaneHandler(JTextPane logWatcherTextArea, String findText) {
            this.logWatcherTextArea = logWatcherTextArea;
            this.findText = findText;
        }

        private void process() {
            String remark = logWatcherTextArea.getText();
            JTextPaneUtil.newInstance(logWatcherTextArea).removeStyles();
            if (StringUtils.isNotBlank(remark)) {
                findPosLst = new ArrayList<Long>();
                Pattern ptn = Pattern.compile(Pattern.quote(findText), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
                Matcher mth = ptn.matcher(remark);
                while (mth.find()) {
                    int start = mth.start(0);
                    int end = mth.end(0);
                    JTextPaneTextStyle.of(logWatcherTextArea).startEnd(start, end).backgroundColor(Color.YELLOW).italic(true).apply();
                    findPosLst.add(Long.valueOf(String.valueOf(start)));
                }
                // logWatcherFindSizeLbl.setText("找到:" + findPosLst.size());

                // int tempPos = 0;
                // while (true) {
                // int startPos = StringUtils.indexOf(remark, findText,
                // tempPos);
                // if (startPos == -1) {
                // break;
                // }
                // JTextPaneTextStyle.of(logWatcherTextArea).startEnd(startPos,
                // startPos +
                // findText.length()).backgroundColor(Color.YELLOW).italic(true).apply();
                // tempPos = startPos + findText.length();
                // }
            }
        }
    }

    private void applyOnTopUndecorated(JDialog dialog) {
        dialog.setUndecorated(true);
        dialog.getRootPane().setOpaque(false);
        dialog.getContentPane().setBackground(new Color(0, 0, 0, 64));
        dialog.setBackground(new Color(0, 0, 0, 0));
        // dialog.setModal(true);
        dialog.setAlwaysOnTop(true);
        // dialog.pack();
    }

    private class AlphaContainer extends JComponent {
        private JComponent component;

        public AlphaContainer(JComponent component) {
            this(component, null);
        }

        public AlphaContainer(JComponent component, JDialog dialog) {
            this.component = component;
            this.component.setBackground(new Color(0, 0, 0, 64));// 0,0,0,0 全透明

            if (dialog != null) {
                DragJDialogListener drag = new DragJDialogListener(dialog);
                this.component.addMouseListener(drag);
                this.component.addMouseMotionListener(drag);
            }

            setLayout(new BorderLayout());
            setOpaque(false);
            component.setOpaque(false);
            add(component);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(component.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class DragJDialogListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;
        JDialog dialog;

        DragJDialogListener(JDialog dialog) {
            this.dialog = dialog;
        }

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent me) {
            location = dialog.getLocation();
            int x = location.x - pressed.getX() + me.getX();
            int y = location.y - pressed.getY() + me.getY();
            dialog.setLocation(x, y);
        }
    }

    private class DragListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent me) {
            Component component = me.getComponent();
            location = component.getLocation(location);
            int x = location.x - pressed.getX() + me.getX();
            int y = location.y - pressed.getY() + me.getY();
            component.setLocation(x, y);
        }
    }

    private class DragJDialogResizeListener extends MouseInputAdapter {
        Point location;
        MouseEvent pressed;
        JDialog dialog;
        char nswe;

        DragJDialogResizeListener(JDialog dialog, char nswe) {
            this.dialog = dialog;
            this.nswe = nswe;
        }

        public void mousePressed(MouseEvent me) {
            pressed = me;
        }

        public void mouseDragged(MouseEvent e) {
            Component component = e.getComponent();
            location = dialog.getLocation();
            int x1 = location.x - pressed.getX() + e.getX();
            int y1 = location.y - pressed.getY() + e.getY();
            Dimension orign = dialog.getSize();

            int x2 = (pressed.getX() - e.getX());
            int y2 = (pressed.getY() - e.getY());

            switch (nswe) {
            case 'n':
                dialog.setLocation(location.x, y1);
                dialog.setSize(new Dimension(orign.width, orign.height + (y2)));
                break;
            case 's':
                dialog.setSize(new Dimension(orign.width, orign.height + (y2 * -1)));
                break;
            case 'w':
                dialog.setLocation(x1, location.y);
                dialog.setSize(new Dimension(orign.width + (x2), orign.height));
                break;
            case 'e':
                dialog.setSize(new Dimension(orign.width + (x2 * -1), orign.height));
                break;
            }
        }
    }

    public JTextPane getLogWatcherTextArea() {
        return logWatcherTextArea;
    }
}
