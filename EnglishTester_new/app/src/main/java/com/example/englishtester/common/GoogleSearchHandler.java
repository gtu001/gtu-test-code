package com.example.englishtester.common;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gtu001 on 2018/8/26.
 */

public class GoogleSearchHandler {

    public static void search(Context context, String text) {
        String inputText = StringUtils.trimToEmpty(text);
        try {
            inputText = URLEncoder.encode(inputText, "UTF8");
        } catch (UnsupportedEncodingException e) {
        }
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, inputText); // query contains search string
        context.startActivity(intent);
    }
}
