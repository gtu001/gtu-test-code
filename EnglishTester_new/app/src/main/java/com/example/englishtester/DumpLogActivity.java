package com.example.englishtester;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import com.example.englishtester.common.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishtester.common.EmailUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DumpLogActivity extends Activity {

    private final static String TAG = DumpLogActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout contentView = createContentView();

        Button btn = new Button(this);
        contentView.addView(btn);
        btn.setText("發email通知我");
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        TextView tv = new TextView(this);
        contentView.addView(tv);
        tv.setText(getLogText());
    }

    /**
     * 發信給我
     */
    public void sendMail() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        File logFile = saveLogToFile(this, null);
        String emailTo = "gtu001.admob1@gmail.com";
        String emailCC = "gtu001.admob1@gmail.com";
        String subject = "懸浮字典發生問題";
        String emailText = "懸浮字典發生問題 " + sdf.format(new Date());
        List<String> filePaths = new ArrayList<String>();
        filePaths.add(logFile.getAbsolutePath());
        EmailUtil.sendEmail(this, emailTo, emailCC, subject, emailText, filePaths);
    }

    /**
     * 取得系統log
     */
    public static File saveLogToFile(Context context, String prefix) {
        if (StringUtils.isBlank(prefix)) {
            prefix = "logcat_";
        }
        String fileName = prefix + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + ".txt";
        final File envPath = new File(Environment.getExternalStorageDirectory(), "DBO_logs5");
        Log.v(TAG, "envPath - " + envPath);
        File outputFile = new File(context.getExternalCacheDir(), fileName);
        Log.v(TAG, "outputFile - " + outputFile);
        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -d -f " + outputFile.getAbsolutePath());
            Toast.makeText(context, "" + outputFile, Toast.LENGTH_LONG).show();
            return outputFile;
        } catch (IOException e) {
            Log.e(TAG, "saveLogToFile ERROR " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 取得系統log
     */
    private String getLogText() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            return log.toString();
        } catch (IOException e) {
            Log.e(TAG, "getLogInfo ERROR " + e.getMessage(), e);
            return "";
        }
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
