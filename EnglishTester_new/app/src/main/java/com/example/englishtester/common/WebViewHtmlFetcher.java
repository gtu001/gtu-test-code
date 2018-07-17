package com.example.englishtester.common;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wistronits on 2018/7/17.
 */

public class WebViewHtmlFetcher {

    private static final String TAG = WebViewHtmlFetcher.class.getSimpleName();

    private Context context;
    private WebView webView;
    private final Handler handler = new Handler();
    private boolean isHtml = true;
    private HtmlGet htmlGet;
    private Runnable htmlGetRunnable;

    public WebViewHtmlFetcher htmlGetRunnable(Runnable htmlGetRunnable) {
        this.htmlGetRunnable = htmlGetRunnable;
        return this;
    }

    public WebViewHtmlFetcher isHtml(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    public WebViewHtmlFetcher htmlGet(HtmlGet htmlGet) {
        this.htmlGet = htmlGet;
        return this;
    }

    public void apply(String url) {
        this.webView.loadUrl(url);
    }

    public WebViewHtmlFetcher(Context contex) {
        this.context = context;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                for (int ii = 0; ii < 10; ii++)
                    Log.v(TAG, "onPageFinished !!!");
                if (isHtml) {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('html')[0].innerHTML);");
                } else {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('html')[0].innerText);");
                }
            }
        });
    }

    public interface HtmlGet {
        public void action(String content);
    }

    private class MyJavaScriptInterface {
        public MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;
            Log.v(TAG, "content " + content);
            if (htmlGet != null) {
                htmlGet.action(content);
            }
            if (htmlGetRunnable != null) {
                handler.post(htmlGetRunnable);
            }
        }
    }
}
