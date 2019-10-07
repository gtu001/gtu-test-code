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
import java.io.File;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

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
                        rect = textComp.getUI().modelToView(textComp, e.getDot());
                        System.out.println("caretUpdate = " + rect.toString());
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
}
