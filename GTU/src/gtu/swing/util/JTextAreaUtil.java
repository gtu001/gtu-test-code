package gtu.swing.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.TreeMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.log.Log;
import gtu.log.PrintStreamAdapter;
import gtu.string.StringLineNumberHandler;

public class JTextAreaUtil {

    /**
     * 建立自動換行, 只有垂直卷軸, warp行的textarea
     */
    public static void setWrapTextArea(JTextArea textArea) {
        textArea.setLineWrap(true); // 激活自动换行功能
        textArea.setWrapStyleWord(true); // 激活断行不断字功能
    }

    /**
     * 將PrintStream物件寫入的值 直接寫到JTextArea
     * 
     * @param logArea
     *            JTextArea物件
     * @param limit
     *            長度大於多少要清空
     * @param logDebug
     *            是否顯示於console
     * @return
     */
    public static PrintStream getNewPrintStream2JTextArea(final JTextArea logArea, final int limit, final boolean logDebug) {
        return getNewPrintStream2JTextArea(logArea, "big5", limit, false);
    }

    /**
     * 將PrintStream物件寫入的值 直接寫到JTextArea
     * 
     * @param logArea
     *            JTextArea物件
     * @param limit
     *            長度大於多少要清空
     * @param logDebug
     *            是否顯示於console
     * @return
     */
    public static PrintStream getNewPrintStream2JTextArea(final JTextArea logArea, String encode, final int limit, final boolean logDebug) {
        return new PrintStream(new PrintStreamAdapter(encode) {
            @Override
            public void println(String message) {
                if (logDebug) {
                    Log.debug(message);
                }
                if (limit != -1 && StringUtils.length(logArea.getText()) > limit) {
                    logArea.setText("");
                }
                logArea.append(message + "\n");
            }
        });
    }

    /**
     * 將PrintStream物件寫入的值 直接寫到JTextArea
     * 
     * @param logArea
     *            JTextArea物件
     * @param limit
     *            長度大於多少要清空
     * @param logDebug
     *            是否顯示於console
     * @return
     */
    public static PrintStream getNewPrintStream2JTextArea(final JTextArea logArea) {
        return getNewPrintStream2JTextArea(logArea, -1, false);
    }

    /**
     * 可把 TextArea當成console, 程式必須放在 Thread
     * 
     * @param consoleArea
     * @param encode
     * @return
     * @throws Exception
     */
    public static InputStream getConsoleInput_fromJTextArea(final JTextArea consoleArea, final String encode) throws Exception {
        try {
            consoleArea.setText("請輸入(exit,quit為結束):\r\n");
            final PipedOutputStream pop = new PipedOutputStream();
            PipedInputStream pin = new PipedInputStream();
            pin.connect(pop);
            consoleArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void removeUpdate(DocumentEvent e) {
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        String text = e.getDocument().getText(e.getOffset(), e.getLength());
                        System.out.print(text);
                        byte[] bs = text.getBytes(encode);
                        try {
                            pop.write(bs);
                        } catch (IOException ex) {
                            if (!ex.getMessage().contains("Pipe closed")) {
                                throw ex;
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
            return pin;
        } catch (Exception e2) {
            throw e2;
        }
    }

    /**
     * 可顯示中文的方式
     * 
     * @param jtext
     */
    public static void applyFont(JTextComponent jtext) {
        Font font = null;
        if (jtext instanceof JTextPane) {
            font = new Font("Courier New", Font.PLAIN, 14);
        } else if (jtext instanceof JTextArea) {
            font = new Font("宋体", Font.PLAIN, 14);
            // font = new Font("Serif", Font.PLAIN, 14);
        }
        if (font == null) {
            throw new RuntimeException("請測試jtext是否可設定font");
        }
        jtext.setFont(font);
    }

    public static void applyTabKey(final JTextComponent jTextComponent, ActionListener beforePerform) {
        jTextComponent.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (beforePerform == null) {
                    tabKeyProcess(e);
                } else {
                    ActionEvent event = new ActionEvent(e, -1, "applyTabKey");
                    beforePerform.actionPerformed(event);
                    if (event.getSource() instanceof Boolean) {
                        Boolean isConsume = (Boolean) event.getSource();
                        if (isConsume) {
                            e.consume();
                        } else {
                            tabKeyProcess(e);
                        }
                    }
                }
            }

            private void tabKeyProcess(KeyEvent e) {
                int startPos = jTextComponent.getSelectionStart();
                int endPos = jTextComponent.getSelectionEnd();

                boolean isShiftPress = (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0;

                if (e.getKeyCode() == KeyEvent.VK_TAB) {

                    if (!isShiftPress) {
                        if (startPos == endPos) {
                            try {
                                jTextComponent.getDocument().insertString(startPos, "    ", null);
                            } catch (BadLocationException e1) {
                                e1.printStackTrace();
                            }
                            e.consume();
                            return;
                        }
                    }

                    StringBuilder sb = new StringBuilder(jTextComponent.getText());

                    TreeMap<Integer, Pair<Integer, Integer>> linePosMap = StringLineNumberHandler.getLinePosMap(sb.toString());

                    int startLineNumber = StringLineNumberHandler.getLineNumber(startPos, linePosMap);
                    int endLineNumber = StringLineNumberHandler.getLineNumber(endPos, linePosMap);
                    Pair<Integer, Integer> selectionRange = Pair.of(-1, -1);

                    if ((startLineNumber != endLineNumber) || isShiftPress) {
                        LineNumberReader reader = null;
                        try {
                            reader = new LineNumberReader(new StringReader(sb.toString()));
                            sb.setLength(0);
                            for (String line = null; (line = reader.readLine()) != null;) {
                                String changeLine = reader.getLineNumber() == 1 ? "" : "\n";

                                if (reader.getLineNumber() >= startLineNumber && reader.getLineNumber() <= endLineNumber) {
                                    if (!isShiftPress) {
                                        // 單純按TAB
                                        sb.append(changeLine + "    ");
                                        if (selectionRange.getLeft() == -1) {
                                            selectionRange = Pair.of(sb.length() - 1, -1);
                                        }
                                        sb.append(line);
                                        if (selectionRange.getLeft() != -1) {
                                            selectionRange = Pair.of(selectionRange.getLeft(), sb.length());
                                        }
                                    } else {
                                        // 按Shift+TAB
                                        sb.append(changeLine);
                                        if (selectionRange.getLeft() == -1) {
                                            selectionRange = Pair.of(sb.length(), -1);
                                        }
                                        sb.append(line.replaceAll("^(\\s{0,4}|\t)", ""));
                                        if (selectionRange.getLeft() != -1) {
                                            selectionRange = Pair.of(selectionRange.getLeft(), sb.length());
                                        }
                                    }
                                } else {
                                    sb.append(changeLine + line);
                                }
                            }
                            jTextComponent.setText(sb.toString());
                            jTextComponent.setSelectionStart(selectionRange.getLeft());
                            jTextComponent.setSelectionEnd(selectionRange.getRight());
                            e.consume();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public static void applyCommonSetting(JTextComponent jTextComponent) {
        applyCommonSetting(jTextComponent, true);
    }

    public static void applyCommonSetting(JTextComponent jTextComponent, boolean isSetupFont) {
        if (isSetupFont) {
            JTextAreaUtil.applyFont(jTextComponent);
        }
        try {
            JTextUndoUtil.applyUndoProcess1(jTextComponent);
        } catch (Exception ex) {
            JTextUndoUtil.applyUndoProcess2(jTextComponent);
        }
        JTextAreaUtil.applyTabKey(jTextComponent, null);
    }

    /**
     * Utility method for setting the font and color of a JTextPane. The result
     * is roughly equivalent to calling setFont(...) and setForeground(...) on
     * an AWT TextArea.
     */
    public static void setJTextPaneFont(JTextPane jtp, Font font, Color c) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = jtp.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, c);

        // Retrieve the pane's document object
        StyledDocument doc = jtp.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }

    public static void setText_withoutTriggerChange(JTextComponent textArea, String text) {
        try {
            Field field = FieldUtils.getDeclaredField(AbstractDocument.class, "listenerList", true);
            EventListenerList listenerList = (EventListenerList) field.get(textArea.getDocument());
            Field field2 = FieldUtils.getDeclaredField(EventListenerList.class, "listenerList", true);
            Object[] arry = (Object[]) field2.get(listenerList);
            Object[] emptyArry = new Object[0];
            field2.set(listenerList, emptyArry);
            textArea.setText(text);
            field2.set(listenerList, arry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
