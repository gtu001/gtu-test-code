package com.example.gtuandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoveToSDActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_sd);

        //AndroidManifest.xml 要設定 android:installLocation="preferExternal"

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                Intent intent = new Intent("android.intent.action.MANAGE_PACKAGE_STORAGE");
                startActivity(intent);
            }
        });
        back();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                MoveToSDActivity.this.setResult(RESULT_CANCELED, MoveToSDActivity.this.getIntent());
                MoveToSDActivity.this.finish();
            }
        });
    }
}
