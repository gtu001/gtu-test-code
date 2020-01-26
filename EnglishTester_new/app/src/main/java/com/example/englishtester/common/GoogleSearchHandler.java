package com.example.englishtester.common;

import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.commons.lang3.StringUtils;

import android.app.SearchManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gtu001 on 2018/8/26.
 */

public class GoogleSearchHandler {

    private static final String TAG = GoogleSearchHandler.class.getSimpleName();

    public static void search(Context context, String text) {
        String inputText = StringUtils.trimToEmpty(text);
//            try {
//                inputText = URLEncoder.encode(inputText, "UTF8");
//            } catch (UnsupportedEncodingException e) {
//            }
        Intent browserIntent = new Intent(Intent.ACTION_WEB_SEARCH);
        browserIntent.putExtra(SearchManager.QUERY, inputText); // query contains search string
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }

    private static String presetUrl(String urlText) {
        String inputText = StringUtils.trimToEmpty(urlText);
        if (!inputText.matches("^https?\\:\\/.*")) {
            inputText = "http://" + inputText;
        }
        return inputText;
    }

    public static void openWithChrome1(String urlText, Context context) {
        try {
            String inputText = presetUrl(urlText);
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse(inputText));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "[openWithChrome1] ERR : " + ex.getMessage(), ex);
            Toast.makeText(context, "無法開啟網頁!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openWithBrowser(String urlText, Context context) {
        try {
            String inputText = presetUrl(urlText);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(inputText));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (Exception ex) {
            Log.e(TAG, "[openWithBrowser] ERR : " + ex.getMessage(), ex);
            Toast.makeText(context, "無法開啟網頁!", Toast.LENGTH_SHORT).show();
        } finally {
        }
    }

    public static void openWithChrome2(String urlText, Context context) {
        Uri uri = Uri.parse("googlechrome://navigate?url=" + urlText);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (i.resolveActivity(context.getPackageManager()) == null) {
            i.setData(Uri.parse(urlText));
        }
        context.startActivity(i);
    }

    public static AppWidgetProviderInfo getAppWidgetProviderInfo(Context context) {
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        ComponentName searchComponent = searchManager.getGlobalSearchActivity();
        if (searchComponent == null) {
            return null;
        }
        String providerPkg = searchComponent.getPackageName();
        AppWidgetProviderInfo defaultWidgetForSearchPackage = null;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (AppWidgetProviderInfo info : appWidgetManager.getInstalledProviders()) {
            if (info.provider.getPackageName().equals(providerPkg) && info.configure == null) {
                if ((info.widgetCategory & AppWidgetProviderInfo.WIDGET_CATEGORY_SEARCHBOX) != 0) {
                    return info;
                } else if (defaultWidgetForSearchPackage == null) {
                    defaultWidgetForSearchPackage = info;
                }
            }
        }
        return defaultWidgetForSearchPackage;
    }
}
