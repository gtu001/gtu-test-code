package com.example.englishtester;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.englishtester.common.HermannEbbinghaus_Memory_Service;
import com.example.englishtester.common.Log;
import com.example.englishtester.common.MainAdViewHelper;
import com.example.englishtester.common.PermissionUtil;
import com.example.englishtester.common.ServiceUtil;
import com.google.android.gms.ads.NativeExpressAdView;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.Map;

public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DumpDataService dumpDataService;
    private EnglishwordInfoService englishwordInfoService;

    private NativeExpressAdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate , PID : " + android.os.Process.myPid());

        LinearLayout contentView = (LinearLayout) findViewById(R.id.linearLayout2);
        mAdView = (NativeExpressAdView) findViewById(R.id.adView);

        //初始化服務
        initServices();

        //----------------------------------------------------------------
        contentView.addView(createLabel("開啟一個可任意移動放大鏡圖示於視窗上,可隨時查詢單字"));
        contentView.addView(createButton("懸浮字典",//
                createOnClickListener(FloatViewActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("純文字閱讀器,可開啟剪貼簿內容或Dropbox內容"));
        contentView.addView(createButton("文字閱讀器",//
                createOnClickListener(TxtReaderActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("Epub閱讀器(beta)"));
        contentView.addView(createButton("Epub閱讀器(beta)",//
                createOnClickListener(EpubReaderEpubActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("Mobi閱讀器(beta)"));
        contentView.addView(createButton("Mobi閱讀器(beta)",//
                createOnClickListener(MobiReaderMobiActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("Pdf閱讀器(beta)"));
        contentView.addView(createButton("Pdf閱讀器(beta)",//
                createOnClickListener(PdfReaderPdfActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("於系統字庫挑選單字做測驗, 記憶單字"));
        contentView.addView(createButton("背單字",//
                createOnClickListener(ReciteMainActivity.class, new Bundle(), null),
                R.drawable.answer_button_lightblue));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("使用懸浮字典查單字的歷史紀錄"));
        contentView.addView(createButton("查詢歷史紀錄",//
                createOnClickListener(ShowWordListActivity.class, getBundle(new Object[]{new Object[]{ShowWordListActivity.INIT_FLAG_KEY, "recent"}}), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("使用懸浮字典查單字的歷史紀錄"));
        contentView.addView(createButton("Dropbox查詢歷史紀錄",//
                createOnClickListener(ShowWordListActivity.class, getBundle(new Object[]{new Object[]{ShowWordListActivity.INIT_FLAG_KEY, "dropboxWord"}}), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("對字庫內的單字做查詢與編輯"));
        contentView.addView(createButton("字庫編輯",//
                createOnClickListener(EnglishwordInfoActivity.class, new Bundle(), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("將查詢過的紀錄產生成題庫檔(.properties)上傳到dropbox"));
        contentView.addView(createButton("將查詢歷史紀錄上傳到Dropbox", new OnClickListener() {
            @Override
            public void onClick(View v) {
                dropboxUploadSearchWordFile();
            }
        }));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("將題庫檔(.properties)匯入至手機"));
        contentView.addView(createButton("從題庫檔匯入至手機",//
                createOnClickListener(PropertiesFindActivity.class, new Bundle(), TaskInfo.IMPORT_WORD.requestCode)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("從新安裝可重複使用上一回備份的字庫"));
        contentView.addView(createButton("字庫備份", new OnClickListener() {
            @Override
            public void onClick(View v) {
                backupAllWordManage();
            }
        }));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("可從電腦透過Dropbox與app做檔案傳輸應用"));
        contentView.addView(createButton("Dropbox App申請",//
                createOnClickListener(DropboxApplicationActivity.class, new Bundle(), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("可於文字閱讀器做全文翻譯"));
        contentView.addView(createButton("百度Api申請",//
                createOnClickListener(BaiduApplicationActivity.class, new Bundle(), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("顯示系統狀態"));
        contentView.addView(createButton("系統狀態",//
                createOnClickListener(StatusInfoActivity.class, new Bundle(), null)));

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        contentView.addView(createLabel("將Log錯誤訊息反饋給我"));
        contentView.addView(createButton("與我聯繫",//
                createOnClickListener(DumpLogActivity.class, new Bundle(), null)));


        //----------------------------------------------------------------

        if (BuildConfig.DEBUG) {
            contentView.addView(createLabel(""));
            contentView.addView(createButton("Test",//
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }
            ));
        }

        //----------------------------------------------------------------
        //----------------------------------------------------------------
        //----------------------------------------------------------------
        //----------------------------------------------------------------
        //----------------------------------------------------------------

        //----------------------------------------------------------------
        contentView.addView(createLabel(""));
        //----------------------------------------------------------------

        //----------------------------------------------------------------
//        contentView.addView(createLabel(""));
//        contentView.addView(createButton("Test",//
//                new OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                    }
//                }
//        ));

        //初始化廣告框
        MainAdViewHelper.getInstance().initAdView(mAdView, this);

        //啟動記憶追朔服務
        ServiceUtil.startStopService(true, this, HermannEbbinghaus_Memory_Service.class);

        //確認讀取儲存空間權限
        boolean isFirstInstall = PermissionUtil.verifyStoragePermissions(this);

        /**
         * 判斷是否要從備份黨回復單字
         */
        if (!isFirstInstall) {
            dumpDataService.autoRestoreBackup(this);//取消每個月1號備份
        }
    }

    /**
     * 初始化服務
     */
    private void initServices() {
        dumpDataService = new DumpDataService(this);
        englishwordInfoService = new EnglishwordInfoService(this);
    }

    private Button createButton(String text, OnClickListener listener) {
        return createButton(text, listener, null);
    }

    private Button createButton(String text, OnClickListener listener, Integer backGroundId) {
        Button b = new Button(this);
        b.setText(text);
        b.setOnClickListener(listener);
        if (backGroundId == null) {
            backGroundId = R.drawable.answer_button_yellow;
        }
//        b.setBackground(getResources().getDrawable(backGroundId));
        b.setBackgroundResource(backGroundId);
        b.setPadding(5, 5, 5, 5);
        return b;
    }

    private TextView createLabel(String message) {
        TextView v = new TextView(this);
        v.setText(message);
        return v;
    }

    /**
     * dropbox上傳搜尋單字檔
     */
    private void dropboxUploadSearchWordFile() {
        String token = DropboxApplicationActivity.getDropboxAccessToken(MainActivity.this);
        final DropboxEnglishService t = new DropboxEnglishService(MainActivity.this, token);
        new AlertDialog.Builder(MainActivity.this)//
                .setTitle("確認").setMessage("成功後是否刪除字庫檔?")//
                .setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        t.executeAddDropboxWord(true);
                    }
                }).setNegativeButton("不用刪除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                t.executeAddDropboxWord(false);
            }
        }).show();
    }


    /**
     * 字庫管理
     */
    private void backupAllWordManage() {
        String[] argItems = new String[]{"還原備份", "匯出備份"};
        new AlertDialog.Builder(this)//
                .setTitle("字庫管理")//
                .setItems(argItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://還原
                                new AlertDialog.Builder(MainActivity.this)//
                                        .setTitle("還原備份")//
                                        .setMessage("是否要還原備份?")//
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dumpDataService.importResult();
                                            }
                                        }).show();
                                break;
                            case 1://匯出
                                new AlertDialog.Builder(MainActivity.this)//
                                        .setTitle("還原備份")//
                                        .setMessage("是否要匯出備份?")//
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dumpDataService.exportResult();
                                            }
                                        }).show();
                                break;
                        }
                    }
                })//
                .show();
    }

    static int REQUEST_CODE = 5566;
    static int MENU_FIRST = Menu.FIRST;

    private enum TaskInfo {
        IMPORT_WORD("從題庫檔匯入至手機", MENU_FIRST++, REQUEST_CODE++, null) {
            protected void onActivityResult(MainActivity _act, Intent data, Bundle bundle) {
                File file = (File) bundle.get(PropertiesFindActivity.BUNDLE_FILE);
                _act.englishwordInfoService.scanFileToEnglishword(_act, file);
            }
        }, //匯入單字
        ;
        final String title;
        final int option;
        final int requestCode;
        final Class<?> clz;

        TaskInfo(String title, int option, int requestCode, Class<?> clz) {
            this.title = title;
            this.option = option;
            this.requestCode = requestCode;
            this.clz = clz;
        }

        protected void onActivityResult(final MainActivity _act, Intent data, Bundle bundle) {
        }

        protected void onOptionsItemSelected(final MainActivity _act, Intent intent, Bundle bundle) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "# onActivityResult");
        Bundle bundle_ = new Bundle();
        if (data != null) {
            bundle_ = data.getExtras();
        }
        final Bundle bundle = bundle_;
        Log.v(TAG, "requestCode = " + requestCode);
        Log.v(TAG, "resultCode = " + resultCode);
        for (TaskInfo t : TaskInfo.values()) {
            if (requestCode == t.requestCode) {
                switch (resultCode) {
                    case RESULT_OK:
                        t.onActivityResult(this, data, bundle);
                        break;
                }
                break;
            }
        }
    }

    /**
     * 建立clickListener
     */
    public OnClickListener createOnClickListener(final Class<?> clz, final Bundle bundle, final Integer requestCode) {
        OnClickListener onClickListner = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int _requestCode = -9999;
                if (requestCode != null) {
                    _requestCode = requestCode;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, clz);
                intent.putExtras(bundle);
                MainActivity.this.startActivityForResult(intent, _requestCode);
            }
        };
        return onClickListner;
    }

    private Bundle getBundle(Object[] objs) {
        Map map = ArrayUtils.toMap(objs);
        Bundle b = new Bundle();
        for (Object key : map.keySet()) {
            String k = (String) key;
            String v = (String) map.get(key);
            b.putString(k, v);
        }
        return b;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume the AdView.
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //用在將此app設定為背景
        moveTaskToBack(true);
    }

    //設定權限callback行為
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        dumpDataService.autoRestoreBackup(this);
    }
}
