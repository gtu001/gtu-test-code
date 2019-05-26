package com.example.englishtester.common;

import android.text.Spannable;
import android.text.SpannableStringBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.Pack200;

/**
 * Created by gtu001 on 2017/10/31.
 */

public class SpannableStringBuilderHelper {

    public SpannableStringBuilderHelper() {
    }

    Map<String, String> strMap = new LinkedHashMap<String, String>();
    Set<String> keys;
    SpannableStringBuilder span;

    public void append(String key, String strValue) {
        if (strMap.containsKey(key)) {
            throw new RuntimeException(String.format("已放過 key : %s, value : %s", key, strMap.get(key)));
        }
        strMap.put(key, strValue);
    }

    public void done(boolean sortedKey) {
        keys = strMap.keySet();
        if (sortedKey) {
            keys = new TreeSet<String>(strMap.keySet());
        }
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(strMap.get(key));
        }
        SpannableStringBuilder span = new SpannableStringBuilder();
        span.append(sb.toString());
        this.span = span;
    }

    private void validate() {
        if (keys == null || span == null) {
            throw new RuntimeException("請先執行done()");
        }
    }

    private int[] countStartEnd(String key) {
        StringBuffer sb = new StringBuffer();
        for (String k : keys) {
            if (!StringUtils.equals(k, key)) {
                sb.append(strMap.get(k));
            } else {
                return new int[]{sb.length(), sb.length() + strMap.get(key).length()};
            }
        }
        throw new RuntimeException("找不到key : " + key);
    }

    public void setSpan(String key, Object what) {
        validate();
        int[] startEnd = countStartEnd(key);
        if (startEnd[0] != startEnd[1]) {
            span.setSpan(what, startEnd[0], startEnd[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public SpannableStringBuilder getResult() {
        validate();
        strMap = new LinkedHashMap<String, String>();
        keys = null;
        return span;
    }
}
