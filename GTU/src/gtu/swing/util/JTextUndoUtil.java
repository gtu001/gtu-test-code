package gtu.swing.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class JTextUndoUtil {

    private static final JTextUndoUtil _INST = new JTextUndoUtil();

    public static void applyUndoProcess1(JTextComponent text) {
        UndoManager um = new UndoManager();
        text.getDocument().addUndoableEditListener(um);
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == KeyEvent.VK_Z) {
                        um.undo();
                    } else if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == KeyEvent.VK_Y) {
                        um.redo();
                    }
                } catch (Exception ex) {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    public static void applyUndoProcess2(JTextComponent text) {
        CustomUndoHandler handler = _INST.new CustomUndoHandler();
        handler.apply(text);
    }

    private class CustomUndoHandler {
        UndoManager undoManager = new UndoManager();
        Document doc = new PlainDocument() {
            @Override
            public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                undoManager.undoableEditHappened(new UndoableEditEvent(this, new ReplaceUndoableEdit(offset, length, text)));
                replaceIgnoringUndo(offset, length, text, attrs);
            }

            private void replaceIgnoringUndo(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                for (UndoableEditListener uel : getUndoableEditListeners()) {
                    removeUndoableEditListener(uel);
                }
                super.replace(offset, length, text, attrs);
                for (UndoableEditListener uel : getUndoableEditListeners()) {
                    addUndoableEditListener(uel);
                }
            }

            class ReplaceUndoableEdit extends AbstractUndoableEdit {
                private final String oldValue;
                private final String newValue;
                private int offset;

                public ReplaceUndoableEdit(int offset, int length, String newValue) {
                    String txt;
                    try {
                        txt = getText(offset, length);
                    } catch (BadLocationException e) {
                        txt = null;
                    }
                    this.oldValue = txt;
                    this.newValue = newValue;
                    this.offset = offset;
                }

                @Override
                public void undo() throws CannotUndoException {
                    try {
                        replaceIgnoringUndo(offset, newValue.length(), oldValue, null);
                    } catch (BadLocationException ex) {
                        throw new CannotUndoException();
                    }
                }

                @Override
                public void redo() throws CannotRedoException {
                    try {
                        replaceIgnoringUndo(offset, oldValue.length(), newValue, null);
                    } catch (BadLocationException ex) {
                        throw new CannotUndoException();
                    }
                }

                @Override
                public boolean canUndo() {
                    return true;
                }

                @Override
                public boolean canRedo() {
                    return true;
                }
            }
        };

        public void apply(JTextComponent text) {
            doc.addUndoableEditListener(undoManager);
            text.setDocument(doc);
            text.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                        e.getKeyCode() == KeyEvent.VK_Z) {
                            undoManager.undo();
                        } else if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                        e.getKeyCode() == KeyEvent.VK_Y) {
                            undoManager.redo();
                        }
                    } catch (Exception ex) {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                }
            });
        }
    }
}