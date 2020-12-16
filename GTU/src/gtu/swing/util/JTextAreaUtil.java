package gtu.swing.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

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

    public static void triggerTabKey(final JTextComponent jTextComponent, final KeyEvent e) {
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

    public static void applyTabKey(final JTextComponent jTextComponent) {
        jTextComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                triggerTabKey(jTextComponent, e);
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
        JTextAreaUtil.applyTabKey(jTextComponent);
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

    public static void setScrollToPosition(JTextComponent textArea, Integer toPosition) {
        if (toPosition == null) {
            toPosition = textArea.getDocument().getLength();
        }
        textArea.setCaretPosition(toPosition);
    }

    public static void setScrollToBottomPloicy(JTextComponent textArea) {
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public static String getSpaceOfCaretPositionLine(final JTextComponent textArea) {
        String prefixLine = "";
        LineNumberReader reader = null;
        try {
            String text = StringUtils.substring(StringUtils.defaultString(textArea.getText()), 0, textArea.getCaretPosition());
            reader = new LineNumberReader(new StringReader(text));
            String lastLine = "";
            int lastLineNumber = 0;
            for (String line = null; (line = reader.readLine()) != null;) {
                lastLine = line;
                lastLineNumber = reader.getLineNumber();
            }
            System.out.println("換行 ： " + lastLineNumber);
            Pattern ptn = Pattern.compile("^[\\s\t]+");
            Matcher mth = ptn.matcher(lastLine);
            if (mth.find()) {
                prefixLine = mth.group();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return prefixLine;
    }

    public static void applyTextAreaPosition(final JTextComponent textArea, final JLabel lbl4Position) {
        final Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String text = StringUtils.substring(StringUtils.defaultString(textArea.getText()), 0, textArea.getCaretPosition());
                LineNumberReader reader = null;
                int lastLineNumber = 0;
                int startPos = 0;
                int selectLen = 0;
                try {
                    reader = new LineNumberReader(new StringReader(text));
                    for (String line = null; (line = reader.readLine()) != null;) {
                        startPos = StringUtils.defaultString(line).length();
                        lastLineNumber = reader.getLineNumber();
                    }
                    selectLen = StringUtils.length(textArea.getSelectedText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                return new StringBuilder().append("(行：").append(lastLineNumber)//
                        .append(", 位：").append(startPos).append(", 長：")//
                        .append(selectLen).append(")").toString();//
            }
        };

        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    lbl4Position.setText((String) call.call());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                try {
                    lbl4Position.setText((String) call.call());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }
        });
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                try {
                    lbl4Position.setText((String) call.call());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            public void removeUpdate(DocumentEvent e) {
                try {
                    lbl4Position.setText((String) call.call());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            public void changedUpdate(DocumentEvent e) {
                try {
                    lbl4Position.setText((String) call.call());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void applyEnterKeyFixPosition(final JTextComponent textArea) {
        final AtomicReference<String> text = new AtomicReference<String>();
        final AtomicReference<Integer> caretPosition = new AtomicReference<Integer>();
        final AtomicBoolean ignoreInput = new AtomicBoolean(false);
        final AtomicBoolean isEnter = new AtomicBoolean(false);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isEnter.get()) {
                    return;
                }
                if (ignoreInput.get()) {
                    ignoreInput.set(false);
                    return;
                }

                String addChar = StringUtils.substring(textArea.getText(), e.getOffset(), e.getOffset() + e.getLength());
                System.out.println("insertUpdate - [" + addChar + "]");
                if (addChar.toCharArray()[0] != '\n') {
                    return;
                }

                String prefixLine = "";
                LineNumberReader reader = null;
                try {
                    reader = new LineNumberReader(new StringReader(text.get()));
                    String lastLine = "";
                    int lastLineNumber = 0;
                    for (String line = null; (line = reader.readLine()) != null;) {
                        lastLine = line;
                        lastLineNumber = reader.getLineNumber();
                    }
                    System.out.println("換行 ： " + lastLineNumber);
                    Pattern ptn = Pattern.compile("^[\\s\t]+");
                    Matcher mth = ptn.matcher(lastLine);
                    if (mth.find()) {
                        prefixLine = mth.group();
                    }

                    String beforeText = StringUtils.substring(textArea.getText(), 0, caretPosition.get());
                    String afterText = StringUtils.substring(textArea.getText(), caretPosition.get());
                    if (!beforeText.endsWith("\n") || StringUtils.length(prefixLine) == 0) {
                        beforeText += "\n";
                    }
                    if (afterText.startsWith("\n")) {
                        afterText = afterText.replaceFirst("\n", "");
                    }

                    final String inserText = beforeText + prefixLine + afterText;
                    final int caretPos = beforeText.length() + prefixLine.length();

                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                ignoreInput.set(true);
                                Document doc = textArea.getDocument();
                                doc.remove(0, StringUtils.length(textArea.getText()));
                                doc.insertString(0, inserText, null);
                                textArea.setCaretPosition(caretPos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    SwingUtilities.invokeLater(runnable);
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

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    isEnter.set(true);
                } else {
                    isEnter.set(false);
                }
                caretPosition.set(textArea.getCaretPosition());
                text.set(StringUtils.substring(StringUtils.defaultString(textArea.getText()), 0, caretPosition.get()));
            }
        });
    }

    public static void applyCloneLine(final JTextComponent textArea) {
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!(KeyEventUtil.isMaskKeyPress(e, "ca") && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN))) {
                    return;
                }

                LineNumberReader reader = null;
                try {
                    String text = StringUtils.defaultString(textArea.getText());
                    reader = new LineNumberReader(new StringReader(text));

                    int caretPosition = textArea.getCaretPosition();
                    int afterCaretPosition = 0;

                    boolean isCopyOk = false;
                    StringBuffer sb = new StringBuffer();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        sb.append(line + "\n");
                        if (!isCopyOk && sb.length() > caretPosition) {
                            if (e.getKeyCode() == KeyEvent.VK_UP) {
                                afterCaretPosition = sb.length() - 1;
                            }
                            isCopyOk = true;
                            sb.append(line + "\n");
                            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                afterCaretPosition = sb.length() - 1;
                            }
                        }
                    }

                    // 空白行判斷
                    if (StringUtils.length(text) == caretPosition) {
                        sb.append("" + "\n");
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            afterCaretPosition = sb.length() - 1;
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            afterCaretPosition = sb.length();
                        }
                    }

                    textArea.setText(sb.toString());
                    textArea.setCaretPosition(afterCaretPosition);
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
        });
    }

    public static void applyCaretPositionStatus(final JTextArea editor, final ActionListener mListener) {
        editor.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                JTextArea editArea = (JTextArea) e.getSource();
                int linenum = 1;
                int columnnum = 1;
                Rectangle rect = null;
                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - editArea.getLineStartOffset(linenum);
                    linenum += 1;
                } catch (Exception ex) {
                }
                try {
                    rect = editArea.getUI().modelToView(editArea, e.getDot());
                } catch (BadLocationException e1) {
                }
                if (mListener != null) {
                    mListener.actionPerformed(new ActionEvent(Triple.of(linenum, columnnum, rect), -1, "CretPosition"));
                }
            }
        });
    }
    
    public static void main(String[] args) {
    }
}
