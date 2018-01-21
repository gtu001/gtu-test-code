package com.example.gtuandroid;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ServiceTest_IntentService_Activity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        LinearLayout contentView = createContentView();

        Button button = new Button(this);
        contentView.addView(button);
        button.setText("start");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TestIntentService.class);
                
                // onHandleIntent() 執行結束後，接著呼叫 stopSelf(int)。由於 IntentService
                // 是設計用來可以在一個 Service 中服務多項工作，因此在這裡，不可以呼叫 stopSelf()。
                // 如果你呼叫 startService() 多次，每一次的呼叫都會被轉成一個 message，並放在
                // mServiceLooper 的 message queue 中，等待被服務。一個 message
                // 所對應的工作被完成後，才會繼續服務下一個工作。所以，這些等待被服務的工作，並不是一起並行 (Concurrent)
                // 的，而是循序執行。
                // 當所有的工作都處裡完時，也是 Service 該結束的時候。
                startService(intent);
            }
        });
    }

    // 關於官方描述:
    // 1,IntentService 會創建一個線程,來處理所有傳給onStartCommand()的Intent請求。
    // 2,對于startService()請求執行onHandleIntent()中的耗時任務,會生成一個隊列,
    // 每次只有一個Intent傳入onHandleIntent()方法並執行。
    // 也就是同一時間只會有一個耗時任務被執行,其他的請求還要在後面排隊,
    // onHandleIntent()方法不會多線程並發執行。
    // 3,當所有startService()請求被執行完成後,IntentService
    // 會自動銷毀,所以不需要自己寫stopSelf()或stopService()來銷毀服務。
    // 4,提供默認的onBind()實現 ,即返回null,不適合綁定的 Service。
    // 5,提供默認的 onStartCommand()
    // 實現,將intent傳入等待隊列中,然後到onHandleIntent()的實現。所以如果需要重寫onStartCommand()
    // 方法一定要調用父類的實現。
    public static class TestIntentService extends IntentService {
        public TestIntentService() {
            super("TestIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            DateFormat format = DateFormat.getTimeInstance();
            Log.v("test", "onHandleIntent开始:" + format.format(new Date()));
            // 这里Thread.sleep(5000)模拟执行一个耗时5秒的任务
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            Log.v("test", "onHandleIntent完成:" + format.format(new Date()));
        }

        // 这里还重写了onDestroy，记录日志用于观察Service何时销毁
        @Override
        public void onDestroy() {
            Log.v("test", "onDestroy");
            super.onDestroy();
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
