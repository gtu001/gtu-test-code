package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gtuandroid.bean.MyEvent;
import com.example.gtuandroid.sub.EventBusDialog;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class EventBusActivity extends Activity {

    private EventBus eventBus;

    private TextView textView;

    private Button mShowDialogBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);
        textView = (TextView) findViewById(R.id.text);
        mShowDialogBtn = (Button) findViewById(R.id.show_btn);
        mShowDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EventBusDialog(EventBusActivity.this).show();
            }
        });
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Subscribe
    public void onEventMainThread(MyEvent event){
        textView.setText(event.getMyEventString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
}