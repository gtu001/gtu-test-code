package com.example.epub.com.example.epub.base;

import android.util.Log;

/**
 * Created by wistronits on 2018/8/7.
 */

public class DamnCoreParserCallback extends ParserCallback {

    private static final String TAG = DamnCoreParserCallback.class.getSimpleName();

    public void flush() throws BadLocationException {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleText(char[] data, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleComment(char[] data, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleError(String errorMsg, int pos) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }

    public void handleEndOfLineString(String eol) {
        class X {
        }
        Class clz = new X().getClass();
        String clzName = clz.getEnclosingClass().getSimpleName();
        String methodName = clz.getEnclosingMethod().getName();
        Log.v(TAG, "[[" + clzName + "." + methodName + "]]");
    }
}
