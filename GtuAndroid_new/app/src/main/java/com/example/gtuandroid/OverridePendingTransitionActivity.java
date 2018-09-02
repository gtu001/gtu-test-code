package com.example.gtuandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class OverridePendingTransitionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contextView = createContentView();

        Button button1 = new Button(this);
        button1.setText("fade");
        contextView.addView(button1);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent phoneIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                final CharSequence[] animStrs = new String[] {//
                "fade",//
                        "slide_left",//
                        "slide_right",//
                        "translate_LR",//
                        "zoom",//
                };
                new AlertDialog.Builder(OverridePendingTransitionActivity.this)//
                        .setTitle("顯示切換動畫")//
                        .setItems(animStrs, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int index) {
                                CharSequence choice = animStrs[index];
                                if ("fade".equals(choice)) {
                                    startActivityForResult(phoneIntent, 1);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else if ("slide_left".equals(choice)) {
                                    startActivityForResult(phoneIntent, 1);
                                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                                } else if ("slide_right".equals(choice)) {
                                    startActivityForResult(phoneIntent, 1);
                                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                } else if ("translate_LR".equals(choice)) {
                                    startActivityForResult(phoneIntent, 1);
                                    overridePendingTransition(R.anim.translate_l, R.anim.translate_r);
                                } else if ("zoom".equals(choice)) {
                                    startActivityForResult(phoneIntent, 1);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                }
                            }
                        }).show();
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
