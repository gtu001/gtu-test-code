package com.example.englishtester.common;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
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

    public static WebViewHtmlFetcher newInstance(Context context) {
        return new WebViewHtmlFetcher(context);
    }

    public WebViewHtmlFetcher(Context context) {
        this.context = context;

        webView = new WebView(context);

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

        if (Build.VERSION.SDK_INT >= 19) {
            webView.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String content) {
                            for (int i = 0; i < 10; i++)
                                Log.v(TAG, "content " + content);
                            if (htmlGet != null) {
                                htmlGet.action(content, handler);
                            }
                        }
                    });
        }
    }

    public interface HtmlGet {
        public void action(String content, Handler handler);
    }

    private class MyJavaScriptInterface {
        public MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void processContent(String aContent) {
            final String content = aContent;
            for (int i = 0; i < 10; i++)
                Log.v(TAG, "content " + content);
            if (htmlGet != null) {
                htmlGet.action(content, handler);
            }
        }
    }
}
