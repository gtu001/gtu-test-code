package com.example.gtuandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SendEmailIntentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();
        
        final EditText editText1 = new EditText(this);
        editText1.setHint("收件人");
        contentView.addView(editText1, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        final EditText editText2 = new EditText(this);
        editText2.setHint("主旨");
        contentView.addView(editText2, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        final EditText editText3 = new EditText(this);
        editText3.setHint("內容");
        contentView.addView(editText3, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        Button button = new Button(this);
        button.setText("寄出");
        contentView.addView(button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("plain/html");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{editText1.getText().toString()});
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, editText2.getText());
                intent.putExtra(android.content.Intent.EXTRA_TEXT, editText3.getText());
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }
        });
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
