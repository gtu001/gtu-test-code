package gtu.swing.util;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.TreeMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
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
        }
        if (font == null) {
            throw new RuntimeException("請測試jtext是否可設定font");
        }
        jtext.setFont(font);
    }

    public static void applyTabKey(final JTextComponent jTextComponent) {
        jTextComponent.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
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
        JTextAreaUtil.applyFont(jTextComponent);
        JTextUndoUtil.applyUndoProcess1(jTextComponent);
        JTextAreaUtil.applyTabKey(jTextComponent);
    }
}
