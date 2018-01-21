package com.example.gtuandroid;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ToastActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);

        // 傳統
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                Toast.makeText(ToastActivity.this, "你的願望已送達耶誕老人信箱!", Toast.LENGTH_LONG).show();
            }
        });

        // 重力顯示
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                final List<String> gravityList = new ArrayList<String>();
                for (Field f : Gravity.class.getDeclaredFields()) {
                    if (Modifier.isStatic(f.getModifiers()) && //
                            Modifier.isPublic(f.getModifiers()) && //
                            Modifier.isFinal(f.getModifiers()) && //
                            f.getType() == int.class) {
                        try {
                            gravityList.add(f.getName() + "," + f.get(Gravity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ToastActivity.this, android.R.layout.simple_list_item_1, gravityList);

                new AlertDialog.Builder(ToastActivity.this)//
                        .setTitle("重力選項")//
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                try {
                                    String value = gravityList.get(paramInt);
                                    int gravity = Integer.parseInt(value.split(",")[1]);
                                    
                                    Toast toast = Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT);
                                    toast.setGravity(gravity, 0, 0);

                                    toast.show();
                                } catch (Exception ex) {
                                    Log.e("ERR", ex.getMessage());
                                    ex.printStackTrace();
                                    Toast.makeText(ToastActivity.this, "Error : 不支援!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })//
                        .show();
            }
        });

        // 自訂
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                LayoutInflater inflater = getLayoutInflater();
                ViewGroup viewGroup = (ViewGroup)findViewById(R.id.linearLayout01);
                View view = inflater.inflate(R.layout.activity_customtoast, viewGroup);//viewGroup可填null
                TextView textView = (TextView) view.findViewById(R.id.textView);
                textView.setText("呵呵呵!");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setView(view);
                // toast.setText("哈哈哈!");//會出錯
                toast.show();
            }
        });
        back();
    }

    void back() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ToastActivity.this.setResult(RESULT_CANCELED, ToastActivity.this.getIntent());
                ToastActivity.this.finish();
            }
        });
    }
}
