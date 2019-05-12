package gtu.swing.util;

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
import javax.swing.text.BadLocationException;
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
}
