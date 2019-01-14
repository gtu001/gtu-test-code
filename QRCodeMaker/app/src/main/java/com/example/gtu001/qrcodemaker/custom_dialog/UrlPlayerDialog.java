package com.example.gtu001.qrcodemaker.custom_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.ImageButtonImageHelper;
import com.example.gtu001.qrcodemaker.common.Mp3PlayerHandler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class UrlPlayerDialog {

    private static final String TAG = UrlPlayerDialog.class.getSimpleName();

    private Context context;
    private Mp3PlayerHandler mp3Helper;
    private String message;
    private Mp3Bean bean;
    private List<Mp3Bean> totalUrlList;
    private int currentIndex = -1;

    public UrlPlayerDialog(Context context) {
        this.context = context;
    }

    public UrlPlayerDialog setUrl(String message, Mp3Bean bean, List<Mp3Bean> totalUrlList) {
        this.bean = bean;
        this.message = message;
        this.totalUrlList = totalUrlList;

        if (StringUtils.isBlank(this.message)) {
            this.message = bean.getName();
        }

        if (totalUrlList != null) {
            currentIndex = totalUrlList.indexOf(bean);
        }

        this.message = message;
        return this;
    }

    public Dialog build() {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.subview_dialog_audioplayer);

        final TextView text_title = (TextView) dialog.findViewById(R.id.text_title);
        final TextView text_close = (TextView) dialog.findViewById(R.id.text_close);
        final TextView text_content = (TextView) dialog.findViewById(R.id.text_content);
        final ImageView btn_img_play = (ImageView) dialog.findViewById(R.id.btn_img_play);
        final ImageView btn_img_cancel = (ImageView) dialog.findViewById(R.id.btn_img_cancel);
        final ImageView btn_img_forward = (ImageView) dialog.findViewById(R.id.btn_img_forward);
        final ImageView btn_img_backward = (ImageView) dialog.findViewById(R.id.btn_img_backward);
        final ImageView btn_img_previous_song = (ImageView) dialog.findViewById(R.id.btn_img_previous_song);
        final ImageView btn_img_next_song = (ImageView) dialog.findViewById(R.id.btn_img_next_song);

        text_title.setText("播放");
        text_content.setText(UrlPlayerDialog.this.message);

        new ImageButtonImageHelper(R.drawable.mp3_play_unpressed, R.drawable.mp3_pause_unpressed, btn_img_play);
        new ImageButtonImageHelper(R.drawable.mp3_stop_unpressed, R.drawable.going_icon, btn_img_cancel);
        new ImageButtonImageHelper(R.drawable.mp3_backward_unpressed, R.drawable.going_icon, btn_img_backward);
        new ImageButtonImageHelper(R.drawable.mp3_forward_unpressed, R.drawable.going_icon, btn_img_forward);
        new ImageButtonImageHelper(R.drawable.mp3_previous_song_unpressed, R.drawable.going_icon, btn_img_previous_song);
        new ImageButtonImageHelper(R.drawable.mp3_next_song_unpressed, R.drawable.going_icon, btn_img_next_song);

        btn_img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (UrlPlayerDialog.this.bean == null || StringUtils.isBlank(UrlPlayerDialog.this.bean.getUrl())) {
                        Toast.makeText(context, "檔案錯誤!", Toast.LENGTH_SHORT).show();
                    }
                    if (mp3Helper == null) {
                        mp3Helper = Mp3PlayerHandler.create(context);
                        mp3Helper.of(UrlPlayerDialog.this.bean.getUrl());
                        mp3Helper.play();
                    } else {
                        mp3Helper.pauseAndResume();
                    }
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

        btn_img_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp3Helper.backwardOrBackward(-20);
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp3Helper.backwardOrBackward(20);
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_img_previous_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    previousUrl();
                    text_title.setText(UrlPlayerDialog.this.bean.getName());
                    text_content.setText(UrlPlayerDialog.this.bean.getUrl());

                    if (mp3Helper.isPlaying()) {
                        mp3Helper = Mp3PlayerHandler.create(context);
                        mp3Helper.of(UrlPlayerDialog.this.bean.getUrl());
                        mp3Helper.play();
                    }
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_img_next_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextUrl();
                    text_title.setText(UrlPlayerDialog.this.bean.getName());
                    text_content.setText(UrlPlayerDialog.this.bean.getUrl());

                    if (mp3Helper.isPlaying()) {
                        mp3Helper = Mp3PlayerHandler.create(context);
                        mp3Helper.of(UrlPlayerDialog.this.bean.getUrl());
                        mp3Helper.play();
                    }
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
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

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
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

    private void nextUrl() {
        if (totalUrlList == null || totalUrlList.isEmpty()) {
            return;
        }
        currentIndex++;
        if (currentIndex >= totalUrlList.size()) {
            currentIndex = 0;
        }
        this.bean = totalUrlList.get(currentIndex);
    }

    private void previousUrl() {
        if (totalUrlList == null || totalUrlList.isEmpty()) {
            return;
        }
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = totalUrlList.size() - 1;
        }
        this.bean = totalUrlList.get(currentIndex);
    }
}
