package com.example.englishtester.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.englishtester.BuildConfig;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import gtu.other.line.LineAppNotifiyHelper_Simple;

/**
 * Created by wistronits on 2018/8/10.
 */

public class Log {

    private static final String DEFAULT_TAG = "<Unknowed Class>";

    private static final Class<?>[] IGNORE_TOAST_CLZ = new Class[]{//
            ReaderCommonHelper.ScrollViewYHolder.class,//
    };//

    public static void i(String tag, String message) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.i(info.getTag(), message);
    }

    public static void w(String tag, String message) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.w(info.getTag(), message);
    }

    public static void e(String tag, String message) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.e(info.getTag(), message);
    }

    public static void d(String tag, String message) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.d(info.getTag(), message);
    }

    public static void d(String tag, String message, int times) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        for (int ii = 0; ii < times; ii++)
            android.util.Log.d(info.getTag(), message);
    }

    public static void v(String tag, String message, int times) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        for (int ii = 0; ii < times; ii++)
            android.util.Log.v(info.getTag(), message);
    }

    public static void v(String tag, String message) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.v(info.getTag(), message);
    }

    public static void i(String tag, String message, Throwable e) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.i(info.getTag(), message, e);
    }

    public static void w(String tag, String message, Throwable e) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.w(info.getTag(), message, e);
    }

    public static void e(String tag, String message, Throwable e) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.e(info.getTag(), message, e);
    }

    public static void e(String tag, String message, Throwable e, int times) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        for (int ii = 0; ii < times; ii++) {
            android.util.Log.e(info.getTag(), message);
        }
        android.util.Log.e(info.getTag(), message, e);
    }

    public static void d(String tag, String message, Throwable e) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.d(info.getTag(), message, e);
    }

    public static void v(String tag, String message, Throwable e) {
        ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.v(info.getTag(), message, e);
    }

    public static void line(String tag, final String message) {
        final ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.v(info.getTag(), message);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LineAppNotifiyHelper_Simple.getInstance().send(info.getTag() + " : " + message);
            }
        });
    }

    public static void line(String tag, final String message, int times) {
        line(tag, message);
    }

    public static void lineFix(String tag, String message) {
        final ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.v(info.getTag(), message);

        message = StringUtils.defaultString(message);
        List<String> lst = fixLength(message, 900);

        for (final String msg : lst) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LineAppNotifiyHelper_Simple.getInstance().send(info.getTag() + " : " + msg);
                }
            });
        }
    }

    public static void line(String tag, final String message, final Throwable e) {
        final ClassInfo info = new ClassInfo(Log.class, tag);
        android.util.Log.e(info.getTag(), message, e);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LineAppNotifiyHelper_Simple.getInstance().send(info.getTag() + message + "======" + ExceptionUtils.getMessage(e));
            }
        });
    }

    public static void toast(final Context context, final String message, final int duringType) {
        ClassInfo info = new ClassInfo(Log.class, DEFAULT_TAG);
        android.util.Log.v(info.getTag(), message);

        //過濾 toast clz
        if (info.clz != null && ArrayUtils.contains(IGNORE_TOAST_CLZ, info.clz)) {
            return;
        }

        if (BuildConfig.DEBUG) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, duringType).show();
                }
            });
        }
    }

    public static void toast(final Context context, final String message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }

    public static void toast_long(final Context context, final String message) {
        toast(context, message, Toast.LENGTH_LONG);
    }

    private static class ClassInfo {
        private String simpleName;
        private Class<?> clz;
        private int lineNumber = -1;
        private String methodName = "";
        private StackTraceElement stk;
        private Class ignoreClz;
        private String defaultTag;

        private ClassInfo(Class ignoreClz, String defaultTag) {
            this.defaultTag = defaultTag;
            if (!BuildConfig.DEBUG) {
                return;
            }

            this.ignoreClz = ignoreClz;
            stk = getCurrentStackTrace();
            if (stk != null) {
                try {
                    clz = Class.forName(stk.getClassName());
                    simpleName = clz.getSimpleName();
                } catch (Exception e) {
                }

                lineNumber = stk.getLineNumber();
                methodName = stk.getMethodName();
            }
        }

        private StackTraceElement getCurrentStackTrace() {
            StackTraceElement[] sks = Thread.currentThread().getStackTrace();
            StackTraceElement currentElement = null;
            boolean findThisOk = false;
            for (int ii = 0; ii < sks.length; ii++) {
                if (StringUtils.equals(sks[ii].getFileName(), ignoreClz.getSimpleName() + ".java")) {
                    findThisOk = true;
                }
                if (findThisOk && //
                        !StringUtils.equals(sks[ii].getFileName(), ignoreClz.getSimpleName() + ".java")) {
                    currentElement = sks[ii];
                    break;
                }
            }
            if (currentElement != null) {
                return currentElement;
            }
            return null;
        }

        private String getTag() {
            if (StringUtils.isEmpty(simpleName)) {
                return defaultTag;
            }
            methodName = StringUtils.isNotEmpty(methodName) ? methodName : "<na>";
            return simpleName + "." + methodName + ":" + lineNumber;
        }
    }

    private static final int ENCODE_LENGTH = 3;//UTF8=3,BIG5=2

    public static List<String> fixLength(String value, int length) {
        char[] array = value.toCharArray();
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, count = 0, lastTime = 0; i < array.length; i++) {
            if (String.valueOf(array[i]).matches("[^\\x00-\\xff]")) {
                count += ENCODE_LENGTH;
                lastTime = ENCODE_LENGTH;
            } else {
                count++;
                lastTime = 1;
            }
            if (count > length) {
                list.add(sb.toString());
                sb = new StringBuilder();
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
                count = lastTime;
            } else if (count == length) {
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
                list.add(sb.toString());
                sb = new StringBuilder();
                count = 0;
            } else {
                // sb.append(count + "" + array[i]);
                sb.append(array[i]);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list;
    }
}
