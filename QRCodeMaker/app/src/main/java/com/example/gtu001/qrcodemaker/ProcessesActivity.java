package com.example.gtu001.qrcodemaker;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gtu001 on 2017/12/19.
 */

public class ProcessesActivity extends Activity {

    private static final String TAG = ProcessesActivity.class.getSimpleName();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout layout = createContentView();

        listView = new ListView(this);
        layout.addView(listView, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定

        listView.setAdapter(createSimpleAdapter());

        //
        killprocess("cn.winvi360.vr3dcamera");
    }

    private void killprocess(String packageName) {
        try {
            ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> activityes = ((ActivityManager) manager).getRunningAppProcesses();
            for (int iCnt = 0; iCnt < activityes.size(); iCnt++) {
                if (activityes.get(iCnt).processName.contains(packageName)) {
                    android.os.Process.sendSignal(activityes.get(iCnt).pid, android.os.Process.SIGNAL_KILL);
                    android.os.Process.killProcess(activityes.get(iCnt).pid);
                    Toast.makeText(this, "Kill Success [1]!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        try {
            ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(packageName);
            Toast.makeText(this, "Kill Success [2]!!", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private SimpleAdapter createSimpleAdapter() {
        List<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> activityes = ((ActivityManager) manager).getRunningAppProcesses();
        for (int iCnt = 0; iCnt < activityes.size(); iCnt++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", null);// 圖像資源的ID
            map.put("item_title", activityes.get(iCnt).pid + " " + activityes.get(iCnt).processName);
            map.put("item_text", Arrays.toString(activityes.get(iCnt).pkgList));
            map.put("item_image_check", null);
            listItem.add(map);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }

    private SimpleAdapter createSimpleAdapter2() {
        List<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        try {
            // Get a list of running apps
            List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();

            for (AndroidAppProcess process : processes) {
                // Get some information about the process
                String processName = process.name;

                Stat stat = process.stat();
                int pid = stat.getPid();
                int parentProcessId = stat.ppid();
                long startTime = stat.stime();
                int policy = stat.policy();
                char state = stat.state();

                Statm statm = process.statm();
                long totalSizeOfProcess = statm.getSize();
                long residentSetSize = statm.getResidentSetSize();

                PackageInfo packageInfo = process.getPackageInfo(ProcessesActivity.this, 0);
                String appName = packageInfo.applicationInfo.loadLabel(this.getPackageManager()).toString();

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("item_image", null);// 圖像資源的ID
                map.put("item_title", appName);
                map.put("item_text", pid);
                map.put("item_image_check", null);
                listItem.add(map);
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }

        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }

    private SimpleAdapter createSimpleAdapter3() {
        List<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ps");

            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null; ) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("item_image", null);// 圖像資源的ID
                map.put("item_title", line);
                map.put("item_text", "");
                map.put("item_image_check", null);
                listItem.add(map);
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }

        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 資料來源
                R.layout.subview_listview, //
                new String[]{"item_title", "item_text", "item_image", "item_image_check"}, //
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemImage, R.id.ImageView01}//
        );
        return listItemAdapter;
    }

    private LinearLayout createContentView() {
        LinearLayout layout = new LinearLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        ViewGroup.LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }
}
