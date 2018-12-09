package com.example.gtu001.qrcodemaker.custom_dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.ImageButtonImageHelper;
import com.example.gtu001.qrcodemaker.common.ServiceUtil;
import com.example.gtu001.qrcodemaker.services.UrlPlayerService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by gtu001 on 2017/12/12.
 */

public class UrlPlayerDialog_bg {

    private static final String TAG = UrlPlayerDialog_bg.class.getSimpleName();

    private Context context;
    private String message;
    private Mp3Bean bean;
    private List<Mp3Bean> totalUrlList;
    private int currentIndex = -1;
    private static AtomicReference<UrlPlayerServiceHander> urlPlayerServiceHander = new AtomicReference<UrlPlayerServiceHander>();

    public UrlPlayerDialog_bg(Context context) {
        this.context = context;
    }

    public UrlPlayerDialog_bg setUrl(String message, Mp3Bean bean, List<Mp3Bean> totalUrlList) {
        this.bean = bean;

        if (this.bean == null) {
            try {
                Map currentBeanMap = this.urlPlayerServiceHander.get().getMService().getCurrentBean();
                this.bean = Mp3Bean.valueOf(currentBeanMap);
            } catch (Exception e) {
                Log.e(TAG, "currentBeanMap ERR : " + e.getMessage(), e);
                Toast.makeText(context, "開啟失敗" + e.getMessage(), Toast.LENGTH_SHORT).show();
                this.bean = new Mp3Bean();
            }
        }

        this.message = message;
        this.totalUrlList = totalUrlList;

        if (StringUtils.isBlank(this.message)) {
            this.message = this.bean.name;
        }

        if (totalUrlList != null) {
            currentIndex = totalUrlList.indexOf(this.bean);
        }

        //只做一次
        if (this.urlPlayerServiceHander.get() == null || this.urlPlayerServiceHander.get().initNotDone()) {
            this.urlPlayerServiceHander.set(new UrlPlayerServiceHander());
            this.urlPlayerServiceHander.get().init();
        }
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
        final ImageView btn_img_backward = (ImageView) dialog.findViewById(R.id.btn_img_backward);
        final ImageView btn_img_forward = (ImageView) dialog.findViewById(R.id.btn_img_forward);
        final ImageView btn_img_previous_song = (ImageView) dialog.findViewById(R.id.btn_img_previous_song);
        final ImageView btn_img_next_song = (ImageView) dialog.findViewById(R.id.btn_img_next_song);

        text_title.setText("播放");
        text_content.setText(UrlPlayerDialog_bg.this.message);

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
                    if (!urlPlayerServiceHander.get().getMService().isInitDone()) {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.url);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                    } else {
                        urlPlayerServiceHander.get().getMService().pauseAndResume();
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

        btn_img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = urlPlayerServiceHander.get().getMService().stopPlay();
                    if (StringUtils.isNotBlank(result)) {
                        Validate.isTrue(false, result);
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

        btn_img_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    urlPlayerServiceHander.get().getMService().backwardOrBackward(-20);
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
                    urlPlayerServiceHander.get().getMService().backwardOrBackward(20);
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
                    text_title.setText(UrlPlayerDialog_bg.this.bean.getName());
                    text_content.setText(UrlPlayerDialog_bg.this.bean.getUrl());

                    if (urlPlayerServiceHander.get().getMService().isPlaying()) {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.url);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                    } else {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.url);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                        urlPlayerServiceHander.get().getMService().pauseAndResume();
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
                    text_title.setText(UrlPlayerDialog_bg.this.bean.getName());
                    text_content.setText(UrlPlayerDialog_bg.this.bean.getUrl());

                    if (urlPlayerServiceHander.get().getMService().isPlaying()) {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.url);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                    } else {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.url);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                        urlPlayerServiceHander.get().getMService().pauseAndResume();
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
                //btn_img_cancel.performClick();
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

    public static class Mp3Bean {
        String name;
        String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static Mp3Bean valueOf(Map map) {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Mp3Bean bean = new Mp3Bean();
            bean.setName((String) map.get("name"));
            bean.setUrl((String) map.get("url"));
            return bean;
        }
    }

    private class UrlPlayerServiceHander {
        private IUrlPlayerService mService;
        private ServiceConnection mConnection;

        private IUrlPlayerService getMService() {
            return mService;
        }

        private void init() {
            mConnection = getMConnection();
            startStopService(true);
            this.bindServiceMethod(true);
        }

        private void bindServiceMethod(boolean isOn) {
            if (ServiceUtil.isServiceRunning(context, UrlPlayerService.class)) {
                Intent intent = new Intent(context, UrlPlayerService.class);
                if (isOn) {
                    Log.v(TAG, "[bindServiceMethod] true");
                    context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    Log.v(TAG, "[bindServiceMethod] false");
                    context.unbindService(mConnection);
                }
            }
        }

        private ServiceConnection getMConnection() {
            return new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    Log.v(TAG, "[onServiceConnected] called");
                    mService = IUrlPlayerService.Stub.asInterface(service);
                    Log.v(TAG, "[mService] init " + mService);
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    Log.v(TAG, "[onServiceDisconnected] called");
                    mService = null;
                    Log.v(TAG, "[mService] setNull ");
                }
            };
        }

        /**
         * 開啟/停止 服務
         */
        private void startStopService(boolean isStart) {
            boolean isRunning = ServiceUtil.isServiceRunning(context, UrlPlayerService.class);
            if (!isRunning && isStart) {
                Intent intent = new Intent(context, UrlPlayerService.class);
                context.startService(intent);
                Log.v(TAG, "[startStopService] start");
            } else {
                Intent intent = new Intent(context, UrlPlayerService.class);
                context.stopService(intent);
                Log.v(TAG, "[startStopService] end");
            }
        }

        private boolean initNotDone() {
            if (!ServiceUtil.isServiceRunning(context, UrlPlayerService.class)) {
                return true;
            }
            if (mService == null || mConnection == null) {
                return true;
            }
            return false;
        }
    }
}
