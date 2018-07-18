package com.example.englishtester.common;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wistronits on 2018/7/18.
 */

public class WebViewHtmlFetcher {

    private static final String TAG = WebViewHtmlFetcher.class.getSimpleName();

    Context context;
    WebView webView;
    boolean configDone = false;

    public WebViewHtmlFetcher(final Context context) {
        this.context = context;
        this.webView = new WebView(context);
    }

    public void applyConfig(final boolean isHtml, final HtmlGet htmlGet) {
        if (configDone == true) {
            Log.v(TAG, "已設定過event!");
            return;
        }else{
            configDone = true;
        }

        final Handler handler = new Handler();
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processContent(String aContent) {
                final String content = aContent;
                Log.v(TAG, "content[1] " + content);

                if ("<html><head></head><body></body></html>".equals(content)) {
                    return;
                }

                if (htmlGet != null) {
                    htmlGet.action(content, handler);
                }
            }
        }

        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        Log.v(TAG, "default Agent : " + System.getProperty("http.agent"));
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 8_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12D508");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                for (int ii = 0; ii < 10; ii++)
                    Log.v(TAG, "onPageFinished !!!");
                if (isHtml) {
                    view.loadUrl("javascript:window.INTERFACE.processContent('<html>' + document.getElementsByTagName('html')[0].innerHTML +'</html>');");
                } else {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('html')[0].innerText);");
                }
            }
        });

        if (false)
            if (Build.VERSION.SDK_INT >= 19) {
                webView.evaluateJavascript(
                        isHtml ? //
                                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();" : //
                                "(function() { return (document.getElementsByTagName('html')[0].innerText); })();"//
                        ,
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String content) {
                                Log.v(TAG, "content[2] " + content);

                                if ("<html><head></head><body></body></html>".equals(content)) {
                                    return;
                                }

                                if (htmlGet != null) {
                                    htmlGet.action(content, handler);
                                }
                            }
                        });
            }
    }

    public void gotoUrl(String url) {
        webView.loadUrl(url);
    }

    public interface HtmlGet {
        public void action(String content, Handler handler);
    }
}
