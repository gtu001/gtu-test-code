package com.example.englishtester;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishtester.common.SharedPreferencesUtil;

import org.apache.commons.lang3.StringUtils;

public class SearchDictionaryActivity extends Activity {

    private static final String TAG = SearchDictionaryActivity.class.getSimpleName();

    public static final int M_REQUEST_CODE = 99888;
    public static final String INTENT_KEY = "word";

    WebView webView;
    Spinner spinner;
    String currentWord;

    public static final String SEARCH_DICTIONARY_REF_KEY = SearchDictionaryActivity.class.getName() + "_REFKEY";
    private static final String KEY_LAST_BROWSE_LINK = SearchDictionaryActivity.class.getName() + "_KEY_LAST_BROWSE_LINK";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        currentWord = getIntent().getExtras().getString(INTENT_KEY);
        Log.v(TAG, "##查詢單字2:" + currentWord);

        this.initSpinner(contentView);

        this.initWebView(contentView);

        this.initBackButton(contentView);

        //設定預設瀏覽page
        String linkIndex = SearchDictionaryActivity.getLastBrowseLink(this);
        if(StringUtils.isNotBlank(linkIndex) && StringUtils.isNumeric(linkIndex)){
            spinner.setSelection(Integer.parseInt(linkIndex));
        }else{
            spinner.setSelection(0);
        }
    }

    /**
     * 網址選擇spinner
     */
    private void initSpinner(LinearLayout contentView) {
        spinner = new Spinner(this);
        final Map<String, String> webUrlMap = new LinkedHashMap<String, String>();
        webUrlMap.put("Yahoo字典", "https://tw.dictionary.yahoo.com/dictionary?p=%s");
        webUrlMap.put("VoiceTube", "https://tw.voicetube.com/definition/%s");
        webUrlMap.put("查查英語", "http://tw.ichacha.net/m/%s.html");
        webUrlMap.put("Free Dictionary", "http://www.thefreedictionary.com/%s");
        webUrlMap.put("Thesaurus", "https://thesaurus.plus/thesaurus/%s");
        webUrlMap.put("Flashcard Monkey", "http://flashcardmonkey.com/%s/");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, webUrlMap.keySet().toArray(new String[0]));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //設定最後一次瀏覽page
                SearchDictionaryActivity.setLastBrowseLink(SearchDictionaryActivity.this, String.valueOf(position));

                String key = new ArrayList<String>(webUrlMap.keySet()).get(position);
                String url = webUrlMap.get(key);
                String wordEncode = URLEncoder.encode(currentWord);
                webView.loadUrl(String.format(url, wordEncode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        contentView.addView(spinner);
    }

    /**
     * 回前頁按鈕
     */
    private void initBackButton(LinearLayout contentView) {
        Button button = new Button(this);
        button.setText("回上頁");
        contentView.addView(button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDictionaryActivity.this.setResult(RESULT_CANCELED, SearchDictionaryActivity.this.getIntent());
                SearchDictionaryActivity.this.finish();
            }
        });
    }

    /**
     * 取得最後瀏覽
     */
    public static String getLastBrowseLink(ContextWrapper context) {
        String lastLink = SharedPreferencesUtil.getData(context, //
                SEARCH_DICTIONARY_REF_KEY, KEY_LAST_BROWSE_LINK);
        return lastLink;
    }

    /**
     * 設定最後瀏覽
     */
    public static void setLastBrowseLink(ContextWrapper context, String lastLink) {
        SharedPreferencesUtil.putData(context, //
                SEARCH_DICTIONARY_REF_KEY, KEY_LAST_BROWSE_LINK, lastLink);
    }

    /**
     * 初始化WebView
     */
    private void initWebView(LinearLayout contentView) {
        webView = new WebView(this);
        contentView.addView(webView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        webView.getSettings().setJavaScriptEnabled(true);
        // webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(PluginState.ON_DEMAND);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Log.v(TAG, "default Agent : " + System.getProperty("http.agent"));
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 8_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12D508");
    }

    public static void searchWord(String word, Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SearchDictionaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY, word);
        intent.putExtras(bundle);
        Log.v(TAG, "##查詢單字1:" + word);
        activity.startActivityForResult(intent, M_REQUEST_CODE);
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
