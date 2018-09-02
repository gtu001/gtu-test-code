package com.example.englishtester.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.DropboxEnglishService;
import com.example.englishtester.R;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import gtu._work.etc.EnglishTester_Diectory2;

/**
 * Created by gtu001 on 2017/9/27.
 */

public class ExampleSentenceDialogHelper {

    private static final String TAG = ExampleSentenceDialogHelper.class.getSimpleName();

    final List<String> currentList = new ArrayList<String>();
    final List<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();

    Set<Integer> loadedPageList = new HashSet<Integer>();

    public void exeute(final String englishId, final Context context, WindowManager mWindowManager) {
        if(pairList.isEmpty()){
            //讀取第一頁
            loadPage(englishId, 1);

            if (pairList.isEmpty()) {
                Toast.makeText(context, "此單字無例句!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final WindowItemListDialog senDialog = new WindowItemListDialog(mWindowManager, context);

        //設定非自動關閉
        senDialog.setAutoClose(false);

        //設定lazyLoding
        senDialog.setItemListOnScrollListener(new EndlessScrollListener() {

            int newLoadPage;
            Handler mHandler = new Handler();

            Runnable loadMoreRunnable = new Runnable() {
                @Override
                public void run() {
                    //讀取第n頁
                    loadPage(englishId, newLoadPage);

                    senDialog.getMyAdapter().notifyDataSetChanged();
                    mHandler.removeCallbacks(loadMoreRunnable);
                }
            };

            @Override
            protected boolean hasMoreDataToLoad() {
                return true;
            }

            @Override
            protected void loadMoreData(final int page) {
                newLoadPage = page + 1;
                Log.v(TAG, "loading page --- " + newLoadPage);
                mHandler.postDelayed(loadMoreRunnable, 0);
            }
        });

        //設定為多行
        senDialog.setItemTextViewStyle(new WindowItemListDialog.WindowItemListDialog_SettingTextView() {
            public void apply(TextView text) {
                text.setSingleLine(false);
            }
        });

        //顯示例句中文
        senDialog.showItemListDialog("例句", currentList, new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair<String,String> p = pairList.get(position);
                TextView text = (TextView)view.findViewById(R.id.ItemTitle);
                text.setText(p.getKey() + "\n" + p.getValue());
            }
        });
    }

    /**
     * 讀取新page
     */
    private void loadPage(final String englishId, final int page) {
        if(loadedPageList.contains(page)){
            return;
        }

        List<Pair<String, String>> pairList_new = new ArrayList<Pair<String,String>>();
        try{
            pairList_new = DropboxEnglishService.getRunOnUiThread(new Callable<List<Pair<String, String>>>() {
                @Override
                public List<Pair<String, String>> call() throws Exception {
                    final EnglishTester_Diectory2 d = new EnglishTester_Diectory2();
                    EnglishTester_Diectory2.WordInfo2 word = d.parseToWordInfo(englishId, page);
                    return word.getExampleSentanceList();
                }
            }, 5000);
        }catch(Exception ex){
            Log.e(TAG, "ERROR OCCOR", ex);
        }

        pairList.addAll(pairList_new);
        for (Pair<String, String> p : pairList_new) {
            currentList.add(p.getKey());
        }

        loadedPageList.add(page);
    }
}
