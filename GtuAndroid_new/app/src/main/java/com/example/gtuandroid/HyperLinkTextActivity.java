package com.example.gtuandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HyperLinkTextActivity extends Activity {
    private TextView textView;
    private EditText editText;
    private Button sendBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyperlink);
        
        sendBtn = (Button)findViewById(R.id.send_button);
        textView = (TextView)findViewById(R.id.text_view);
        editText = (EditText)findViewById(R.id.edit_text);
        
        //若輸入 http://www.google.com.tw 會自動變成超連結
        textView.setAutoLinkMask(Linkify.ALL);//超連結設定 XXX
        
        sendBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textView.setText(editText.getText().toString());
            }
            
        });
    }
}
