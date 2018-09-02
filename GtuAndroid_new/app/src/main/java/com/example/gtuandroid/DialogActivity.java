package com.example.gtuandroid;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.gtuandroid.sub.NoticeDialogFragment;
import com.example.gtuandroid.sub.NoticeDialogFragment2;
import com.example.gtuandroid.sub.SimpleDialog;

import java.util.Arrays;

public class DialogActivity extends FragmentActivity implements NoticeDialogFragment.NoticeDialogListener {

    private static final String TAG = DialogActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("# onCreate");
        super.onCreate(savedInstanceState);

        LinearLayout contentView = createContentView();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        Button button1 = createButton("dialog", contentView);
        Button button2 = createButton("process", contentView);
        Button button3 = createButton("dialog choice", contentView);
        Button button4 = createButton("process bar", contentView);
        Button button5 = createButton("popupWindow", contentView);
        Button button6 = createButton("dialog custom", contentView);
        Button button7 = createButton("DialogFragment", contentView);
        Button button8 = createButton("DialogFragment2", contentView);
        Button button9 = createButton("simple dialog", contentView);
        Button button10 = createButton("warn dialog", contentView);
        Button button11 = createButton("check dialog", contentView);
        Button button12 = createButton("no title dialog", contentView);

        // dialog
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)//
                        .setTitle("Title")//
                        .setMessage("Message")//
                        .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        })//
                        .setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        }).setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        }).create();
                applyAnimation(dialog);
                applyDialogBackground(dialog);
                dialog.show();
            }
        });

        // warn dialog
        button10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                final Builder builder = new Builder(DialogActivity.this);
                builder.setTitle("title");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("warning");
                builder.setNeutralButton("ok", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);// 無法直接取消
                alertDialog.show();
            }
        });

        // process
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog myDialog = ProgressDialog.show(DialogActivity.this, "標題", "Body", true);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        myDialog.cancel();// 兩個好像都可以取消 cancel , dismiss
                        // myDialog.dismiss();
                    }
                }.start();
            }
        });

        // process bar
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog02 = new ProgressDialog(DialogActivity.this);
                dialog02.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog02.setMessage("讀取中...");
                dialog02.show();
                final Handler myHandler = new Handler();
                new Thread() {
                    public void run() {
                        int newProgressStatus = 0;
                        while (newProgressStatus < 100) {
                            try {
                                Thread.sleep(500);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            newProgressStatus += 10;
                            dialog02.setProgress(newProgressStatus);
                        }
                        // cancel 不寫在handler 也可以取消, 至於為何要寫在handler目前原因不明
                        myHandler.post(new Runnable() {
                            public void run() {
                                dialog02.cancel();
                            }
                        });

                    }
                }.start();
            }
        });

        // dialog choice
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] aryShop = DialogActivity.this.getResources().getStringArray(R.array.items_irdc_dialog);
                new AlertDialog.Builder(DialogActivity.this)//
                        .setTitle("選餐廳")//
                        .setItems(aryShop, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                Toast.makeText(DialogActivity.this, "選了" + aryShop[paramInt], Toast.LENGTH_SHORT).show();
                            }
                        })//
                        // .setSingleChoiceItems(aryShop, 0, new
                        // DialogInterface.OnClickListener() {
                        // @Override
                        // public void onClick(DialogInterface
                        // paramDialogInterface, int paramInt) {
                        // Toast.makeText(DialogActivity.this, "選了" +
                        // aryShop[paramInt], Toast.LENGTH_SHORT).show();
                        // }
                        // })
                        // .setMultiChoiceItems(aryShop, null, new
                        // DialogInterface.OnMultiChoiceClickListener() {
                        // @Override
                        // public void onClick(DialogInterface interFace, int
                        // paramInt, boolean checked) {
                        // if(checked){
                        // Toast.makeText(DialogActivity.this, "選了" +
                        // aryShop[paramInt], Toast.LENGTH_SHORT).show();
                        // }else{
                        // Toast.makeText(DialogActivity.this, "取消了" +
                        // aryShop[paramInt], Toast.LENGTH_SHORT).show();
                        // }
                        // }
                        // })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        })//
                        .setNeutralButton("wait", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                })//
                        .show();
            }
        });

        // popupWindow
        button5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = new Button(DialogActivity.this);
                button.setText("按下就可關閉");
                final PopupWindow popupWindow = new PopupWindow(DialogActivity.this);
                popupWindow.setContentView(button);
                popupWindow.setFocusable(true);
                popupWindow.setWidth(300);
                popupWindow.setHeight(200);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        // dialog custom
        button6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the layout inflater
                LayoutInflater inflater = DialogActivity.this.getLayoutInflater();
                View subView = inflater.inflate(R.layout.subview_dialog_singin, null);
                final EditText username = (EditText) subView.findViewById(R.id.username);
                final EditText password = (EditText) subView.findViewById(R.id.password);
                AlertDialog.Builder builder = new AlertDialog.Builder(DialogActivity.this);
                builder.setView(subView);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Toast.makeText(DialogActivity.this, "帳號:" + username.getText() + "/密碼:" + password.getText(), Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        // DialogFragment
        button7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the dialog fragment and show it
                DialogFragment dialog = new NoticeDialogFragment();
                // 主activity必須繼承FragmentActivity,且必須實作來源NoticeDialogFragment的介面
                // FIXME
                dialog.show(DialogActivity.this.getSupportFragmentManager(), "NoticeDialogFragment");
                // The second argument, "NoticeDialogFragment", is a unique tag
                // name that the system uses to
                // save and restore the fragment state when necessary. The tag
                // also allows you to get
                // a handle to the fragment by calling findFragmentByTag().

                android.support.v4.app.Fragment frag = DialogActivity.this.getSupportFragmentManager().findFragmentByTag("NoticeDialogFragment");
                Log.v(TAG, "dialog1 = " + dialog);
                Log.v(TAG, "dialog2 = " + frag);
            }
        });

        // DialogFragment2
        button8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the dialog fragment and show it
                NoticeDialogFragment2 dialog = new NoticeDialogFragment2();
                // 主activity必須繼承FragmentActivity,且必須實作來源NoticeDialogFragment的介面
                // FIXME
                dialog.show(DialogActivity.this.getSupportFragmentManager(), "NoticeDialogFragment2");
                // The second argument, "NoticeDialogFragment", is a unique tag
                // name that the system uses to
                // save and restore the fragment state when necessary. The tag
                // also allows you to get
                // a handle to the fragment by calling findFragmentByTag().

                android.support.v4.app.Fragment frag = DialogActivity.this.getSupportFragmentManager().findFragmentByTag("NoticeDialogFragment2");
                Log.v(TAG, "dialog1 = " + dialog);
                Log.v(TAG, "dialog2 = " + frag);
                Log.v(TAG, "text = " + dialog.getEditText());
            }
        });

        // simple dialog
        button9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog dialog = new SimpleDialog(DialogActivity.this);
                dialog.show();
                Log.v(TAG, "username = " + dialog.getUsername());
                Log.v(TAG, "password = " + dialog.getPassword());
            }
        });

        //check dialog
        button11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] chkItems = {"item1", "item2", "item3"};
                final boolean[] chkSts = {true, false, false};
                AlertDialog.Builder chkDlg = new AlertDialog.Builder(DialogActivity.this);
                chkDlg.setTitle("標題");
                chkDlg.setMultiChoiceItems(chkItems, chkSts, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Toast.makeText(DialogActivity.this, "click - " + which + " - " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });
                chkDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DialogActivity.this, "arry - " + Arrays.toString(chkSts), Toast.LENGTH_SHORT).show();
                    }
                });
                chkDlg.create().show();
            }
        });

        //no title dialog
        button12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(DialogActivity.this);
                imageView.setImageResource(R.drawable.ic_launcher);
                Dialog dialog = new Dialog(DialogActivity.this, R.style.ch0215_dialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(imageView);
                dialog.show();
            }
        });
    }

    /**
     * 設定dialog動畫效果
     */
    private void applyAnimation(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.anim.fade); // 添加动画
    }

    /**
     * 创建一个背景模糊的Window，且将对话窗口放在前景
     */
    private void applyDialogBackground(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    // ↓↓↓↓↓↓ NoticeDialogFragment.NoticeDialogListener
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Dialog dialog_ = dialog.getDialog();
        final EditText username = (EditText) dialog_.findViewById(R.id.username);
        final EditText password = (EditText) dialog_.findViewById(R.id.password);
        Toast.makeText(DialogActivity.this, "取得自NoticeDialogFragment的資料\n" + //
                "帳號:" + username.getText() + "/密碼:" + password.getText(), Toast.LENGTH_SHORT).show();

        android.support.v4.app.Fragment frag = DialogActivity.this.getSupportFragmentManager().findFragmentByTag("NoticeDialogFragment");
        Log.v("tag", "dialog3 = " + frag);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }
    // ↑↑↑↑↑↑ NoticeDialogFragment.NoticeDialogListener

    private Button createButton(String text, LinearLayout contentView) {
        Button btn = new Button(this);
        btn.setText(text);
        contentView.addView(btn);
        return btn;
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
