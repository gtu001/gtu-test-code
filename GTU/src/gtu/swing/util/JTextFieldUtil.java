package gtu.swing.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

import gtu.clipboard.ClipboardUtil;

public class JTextFieldUtil {

    public static String getText(final JTextComponent jText) {
        try {
            return jText.getDocument().getText(0, jText.getDocument().getLength());
        } catch (Exception e) {
            throw new RuntimeException("getText ERR : " + e.getMessage(), e);
        }
    }

    public static void setText(final JTextField textField, final String strValue) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textField.setText(strValue);
            }
        });
    }

    public static void setupDragDropFilePath(final JTextComponent textField, final ActionListener multiFileListener) {
        textField.setDragEnabled(true);
        textField.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        System.out.println(">> dropFile : " + file);
                    }
                    if (!droppedFiles.isEmpty()) {
                        textField.setText(droppedFiles.get(0).getAbsolutePath());
                    }
                    if (multiFileListener != null) {
                        if (!droppedFiles.isEmpty()) {
                            ActionEvent event = new ActionEvent(droppedFiles, -1, "go multi file");
                            multiFileListener.actionPerformed(event);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void setTextIgnoreDocumentListener(JTextComponent jtext, String text) {
        new JTextComponentSetContentProcessor(jtext).setText(text);
    }

    private static class JTextComponentSetContentProcessor {
        JTextComponent jtext;
        DocumentListener[] listeners;

        public JTextComponentSetContentProcessor(JTextComponent jtext) {
            this.jtext = jtext;
        }

        private void removeEvent() {
            Document doc = jtext.getDocument();
            if (doc instanceof AbstractDocument) {
                listeners = ((AbstractDocument) doc).getDocumentListeners();
                if (listeners != null) {
                    for (DocumentListener l : listeners) {
                        jtext.getDocument().removeDocumentListener(l);
                    }
                }
            }
        }

        private void addEvent() {
            if (listeners != null) {
                for (DocumentListener l : listeners) {
                    jtext.getDocument().addDocumentListener(l);
                }
            }
        }

        public void setText(String text) {
            removeEvent();
            jtext.setText(text);
            addEvent();
        }
    }

    public static class JTextComponentSelectPositionHandler {
        Rectangle rect;

        public static JTextComponentSelectPositionHandler newInst(JTextComponent textArea) {
            return new JTextComponentSelectPositionHandler(textArea);
        }

        private JTextComponentSelectPositionHandler(JTextComponent textArea) {
            textArea.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    JTextComponent textComp = (JTextComponent) e.getSource();
                    try {
                        Rectangle mRectangle = textComp.getUI().modelToView(textComp, e.getDot());
                        if (mRectangle != null) {
                            System.out.println("caretUpdate = " + mRectangle.toString());
                            rect = mRectangle;
                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException("Failed to get pixel position of caret", ex);
                    }
                }
            });
        }

        public Rectangle getRect() {
            return rect;
        }
    }

    /**
     * 取得文字邊界
     * 
     * @param text
     * @param font
     * @return
     */
    public static Dimension getTextBoundary(String text, Font font) {
        // get metrics from the graphics
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        // get the height of a line of text in this
        // font and render context
        int hgt = metrics.getHeight();
        // get the advance of my text in this font
        // and render context
        int adv = metrics.stringWidth(text);
        // calculate the size of a box to hold the
        // text with some padding.
        Dimension size = new Dimension(adv + 2, hgt + 2);
        return size;
    }

    public static void setText_withoutTriggerChange(JTextComponent textArea, String text) {
        JTextAreaUtil.setText_withoutTriggerChange(textArea, text);
    }

    public static int getValueFailSetDefault(int defaultVal, JTextComponent textComponent) {
        try {
            return Integer.parseInt(textComponent.getText());
        } catch (Exception ex) {
            textComponent.setText(String.valueOf(defaultVal));
            return defaultVal;
        }
    }

    public static String getValueFailSetDefault(String defaultVal, JTextComponent textComponent) {
        if (StringUtils.isBlank(textComponent.getText())) {
            textComponent.setText(defaultVal);
        }
        return textComponent.getText();
    }

    public static void applyDeaultSettings(JTextComponent textComponent) {
        textComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (JMouseEventUtil.buttonRightClick(1, e)) {
                    JPopupMenuUtil.newInstance(textComponent)//
                            .addJMenuItem(getJMenuItems_CopyPaste(textComponent))//
                            .applyEvent(e).show();
                }
            }
        });
    }

    public static List<JMenuItem> getJMenuItems_CopyPaste(JTextComponent textComponent) {
        List<JMenuItem> appendLst = new ArrayList<JMenuItem>();
        final AtomicReference<String> text = new AtomicReference<String>();
        text.set(textComponent.getSelectedText());
        if (StringUtils.isBlank(text.get())) {
            text.set(textComponent.getText());
        }
        if (StringUtils.isNotBlank(text.get())) {
            JMenuItem item1 = new JMenuItem();
            item1.setText("複製");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClipboardUtil.getInstance().setContents(text.get());
                }
            });
            appendLst.add(item1);
        }
        String text2 = ClipboardUtil.getInstance().getContents();
        if (StringUtils.isNotBlank(text2)) {
            JMenuItem item2 = new JMenuItem();
            item2.setText("貼上");
            item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        textComponent.getDocument().insertString(textComponent.getCaretPosition(), text2, null);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            appendLst.add(item2);
        }
        if (StringUtils.isNotBlank(text.get())) {
            JMenuItem item1 = new JMenuItem();
            item1.setText("清除");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textComponent.setText("");
                }
            });
            appendLst.add(item1);
        }
        return appendLst;
    }
}
