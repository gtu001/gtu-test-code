package com.example.gtuandroid;

import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AsyncTaskTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back();

        new TestAsyncTask(this).execute();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                AsyncTaskTestActivity.this.setResult(RESULT_CANCELED, AsyncTaskTestActivity.this.getIntent());
                AsyncTaskTestActivity.this.finish();
            }
        });
    }

    public class TestAsyncTask extends AsyncTask<URL, Integer, String> {
        private Context mContext;
        private ProgressDialog mDialog;

        public TestAsyncTask(Context mContext) {
            this.mContext = mContext;
        }

        // 進行一些初始化的工作
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.show();
        }

        /*
         * 傳入一個參數, 這個參數的數量是不定的, 也就是說, 你可以傳入零到數個參數也不是問題, 從官網的例子來看, 當宣告這個類別的物件時候,
         * 只要呼叫execute這個方法, 傳入你想下載的網址, 就可以開始使用它了
         */
        protected String doInBackground(URL... urls) {
            int progress = 0;
            while (progress <= 100) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(Integer.valueOf(progress));
                progress++;
            }
            return "ok";
        }

        /*
         * onProgressUpdate(), 它一樣會傳入一些參數, 也是不定數量的,
         * 而這個方法通常是在執行onInBackground的時候, 才會去呼叫它, 只要執行publishProgress()這個方法,
         * 就會呼叫onProgressUpdate(), 利用這個方法, 就可以讓程式顯示目前進度為何。
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            mDialog.setProgress(progress[0]);
        }

        /*
         * 最後一個方法是onPostExecute(), 這個用來接收結果, 通常你在onProgressUpdate()回傳的結果,
         * 就會當參數傳到這個方法, 不過你要注意型態, 才不會沒有傳入。
         */
        protected void onPostExecute(String result) {
            if (result.equals("ok")) {
                mDialog.dismiss();
            }
        }
    }
}
