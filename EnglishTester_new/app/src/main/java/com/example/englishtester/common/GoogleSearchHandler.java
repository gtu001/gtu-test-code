package com.example.englishtester.common;

import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.commons.lang3.StringUtils;

import android.app.SearchManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gtu001 on 2018/8/26.
 */

public class GoogleSearchHandler {

    public static void search(Context context, String text) {
        String inputText = StringUtils.trimToEmpty(text);
//            try {
//                inputText = URLEncoder.encode(inputText, "UTF8");
//            } catch (UnsupportedEncodingException e) {
//            }
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, inputText); // query contains search string
        context.startActivity(intent);
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
