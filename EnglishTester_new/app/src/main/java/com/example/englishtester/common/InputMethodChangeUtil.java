package com.example.englishtester.common;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by gtu001 on 2018/5/14.
 * 改變輸入法無效
 */
@Deprecated
public class InputMethodChangeUtil {

    private enum InputMethodEnum {
        Zhuyin("com.google.android.apps.inputmethod.zhuyin/.ZhuyinInputMethodService"),//
        Pinyin("com.google.android.inputmethod.pinyin/.PinyinIME"),//
        Latin("com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME"),//
        Voice("com.google.android.googlequicksearchbox/com.google.android.voicesearch.ime.VoiceInputMethodService"),//
        ;
        final String id;

        InputMethodEnum(String id) {
            this.id = id;
        }
    }

    private static final String TAG = InputMethodChangeUtil.class.getSimpleName();

    //改變輸入法無效
    @Deprecated
    public static void changeInput(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.setInputMethod(view.getWindowToken(), InputMethodEnum.Zhuyin.id);
        im.setInputMethod(view.getApplicationWindowToken(), InputMethodEnum.Zhuyin.id);
    }

    public static void showID(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> inputMethodInfos = inputMethodManager.getInputMethodList();
        for (InputMethodInfo inputMethodInfo : inputMethodInfos) {
            Log.v(TAG, ">>>>>>>>> showID : " + inputMethodInfo.getId());
            List<InputMethodSubtype> lst = inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true);
            for (InputMethodSubtype sub : lst) {
                Log.v(TAG, "\t\t sub : " + ToStringBuilder.reflectionToString(sub));
            }
        }
    }
}
