package com.example.gtu001.qrcodemaker.custom_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.ImageButtonImageHelper;
import com.example.gtu001.qrcodemaker.common.mp3.Mp3PlayerHandler;

import java.io.File;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class FilePlayerDialog {

    private static final String TAG = FilePlayerDialog.class.getSimpleName();

    private Context context;
    private Mp3PlayerHandler mp3Helper;
    private File file;

    public FilePlayerDialog(Context context) {
        this.context = context;
    }

    public FilePlayerDialog setFile(File file) {
        this.file = file;
        return this;
    }

    public Dialog build() {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.subview_dialog_audioplayer);

        final TextView text_title = (TextView)dialog.findViewById(R.id.text_title);
        final TextView text_close = (TextView)dialog.findViewById(R.id.text_close);
        final TextView text_content = (TextView)dialog.findViewById(R.id.text_content);
        final ImageView btn_img_play = (ImageView)dialog.findViewById(R.id.btn_img_play);
        final ImageView btn_img_cancel = (ImageView)dialog.findViewById(R.id.btn_img_cancel);

        text_title.setText("播放");
        text_content.setText(file.getName());

        new ImageButtonImageHelper(R.drawable.mp3_play_unpressed, R.drawable.going_icon, btn_img_play);
        new ImageButtonImageHelper(R.drawable.mp3_stop_unpressed, R.drawable.going_icon, btn_img_cancel);

        btn_img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (file == null) {
                        Toast.makeText(context, "檔案錯誤!", Toast.LENGTH_SHORT).show();
                    }
                    if (mp3Helper != null) {
                        mp3Helper.release();
                    }

                    Mp3PlayerHandler mp3Helper = new Mp3PlayerHandler();
                    mp3Helper.applyOf(file.getAbsolutePath(), context);
                    mp3Helper.play();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mp3Helper == null) {
                        Toast.makeText(context, "尚未撥放!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mp3Helper.release();
                    mp3Helper = null;
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });

        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.v(TAG, "# Listen onDismiss");
                btn_img_cancel.performClick();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(TAG, "# Listen onCancel");
                btn_img_cancel.performClick();
            }
        });

        return dialog;
    }
}
