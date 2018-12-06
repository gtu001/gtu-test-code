package gtu.swing.util;

import gtu.log.Log;
import gtu.log.PrintStreamAdapter;

import java.awt.Font;
import java.io.PrintStream;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

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

    public static void applyCommonSetting(JTextComponent sqlTextArea) {
        JTextAreaUtil.applyFont(sqlTextArea);
        JTextUndoUtil.applyUndoProcess1(sqlTextArea);
    }
}
