package com.example.gtuandroid;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncTaskTest2Activity extends ListActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_builtin);

        data = new ArrayList<String>();

        // 显示ProgressDialog放到AsyncTask.onPreExecute()里
        // showDialog(PROGRESS_DIALOG);
        new ProgressTask().execute(data);
    }

    private class ProgressTask extends AsyncTask<ArrayList<String>, Void, Integer> {

        private ProgressDialog dialog;
        private static final int STATE_FINISH = 55;
        private static final int STATE_ERROR = 56;

        /* 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条。 */
        @Override
        protected void onPreExecute() {
            // 先显示ProgressDialog
            //設定字型
            Typeface font = Typeface.createFromAsset(AsyncTaskTest2Activity.this.getAssets(), "fonts/w3.ttc");
            dialog = ProgressDialog.show(AsyncTaskTest2Activity.this, "狀態", "更新中...");
            // ((Button)dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setTypeface(font);
            ((TextView) dialog.findViewById(getResources().getIdentifier("alertTitle", "id", "android"))).setTypeface(font);
            ((TextView) dialog.findViewById(getResources().getIdentifier("message", "id", "android"))).setTypeface(font);
        }

        /* 执行那些很耗时的后台计算工作。可以调用publishProgress方法来更新实时的任务进度。 */
        @Override
        protected Integer doInBackground(ArrayList<String>... datas) {
            ArrayList<String> data = datas[0];
            for (int i = 0; i < 8; i++) {
                data.add("ListItem");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return STATE_FINISH;
        }

        /*
         * 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，
         * 后台的计算结果将通过该方法传递到UI thread.
         */
        @Override
        protected void onPostExecute(Integer result) {
            int state = result.intValue();
            switch (state) {
            case STATE_FINISH:
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "加载完成!", Toast.LENGTH_LONG).show();
                adapter = new ArrayAdapter<String>(AsyncTaskTest2Activity.this, android.R.layout.simple_list_item_1, data);
                setListAdapter(adapter);
                break;
            case STATE_ERROR:
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "处理过程发生错误!", Toast.LENGTH_LONG).show();
                adapter = new ArrayAdapter<String>(AsyncTaskTest2Activity.this, android.R.layout.simple_list_item_1, data);
                setListAdapter(adapter);
                break;
            default:
            }
        }
    }
}
