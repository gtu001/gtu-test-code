package com.example.gtuandroid.sub;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gtuandroid.R;
import com.example.gtuandroid.bean.MyEvent;

import de.greenrobot.event.EventBus;

public class EventBusDialog extends Dialog{

    private Context context;

    private Button sendBtn;

    private EditText editText;

    private EventBus mEventBus;

    public EventBusDialog(Context context) {
        super(context, android.R.style.Theme_Light);
        this.context = context;

        mEventBus = EventBus.getDefault();
        setContentView(R.layout.subview_eventbus_dialog);
        sendBtn = (Button) findViewById(R.id.send_button);
        editText = (EditText) findViewById(R.id.edit_text);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyEvent event = new MyEvent();
                event.setMyEventString(editText.getText().toString());
                mEventBus.post(event);
                dismiss();
            }
        });
    }
}