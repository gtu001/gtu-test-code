package com.example.gtu001.qrcodemaker.custom_dialog;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gtu001.qrcodemaker.IUrlPlayerService;
import com.example.gtu001.qrcodemaker.Mp3Bean;
import com.example.gtu001.qrcodemaker.R;
import com.example.gtu001.qrcodemaker.common.ImageButtonImageHelper;
import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.ServiceUtil;
import com.example.gtu001.qrcodemaker.services.UrlPlayerService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private int replayMode = -1;
    private static AtomicReference<UrlPlayerServiceHander> urlPlayerServiceHander = new AtomicReference<UrlPlayerServiceHander>();
    private PercentProgressBarTimer mPercentProgressBarTimer;

    public UrlPlayerDialog_bg(Context context) {
        this.context = context;
    }

    public UrlPlayerDialog_bg setUrl(String message, Mp3Bean bean, List<Mp3Bean> totalUrlList) {
        this.bean = bean;

        this.message = message;
        this.totalUrlList = totalUrlList;
        Log.v(TAG, "<<<<<<<< totalUrlList size : " + totalUrlList.size());

        if (StringUtils.isBlank(this.message) && this.bean != null) {
            this.message = this.bean.getName();
        }

        if (totalUrlList != null && this.bean != null) {
            currentIndex = totalUrlList.indexOf(this.bean);
        }

        //只做一次
        if (this.urlPlayerServiceHander.get() == null || this.urlPlayerServiceHander.get().initNotDone(context)) {
            this.urlPlayerServiceHander.set(new UrlPlayerServiceHander());
            this.urlPlayerServiceHander.get().init(context);
        }
        return this;
    }

    public Dialog build() {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.subview_dialog_audioplayer);

        final TextView text_title = (TextView) dialog.findViewById(R.id.text_title);
        final TextView text_close = (TextView) dialog.findViewById(R.id.text_close);
        final TextView text_content = (TextView) dialog.findViewById(R.id.text_content);
        final TextView text_timer = (TextView) dialog.findViewById(R.id.text_timer);
        final ImageView btn_img_play = (ImageView) dialog.findViewById(R.id.btn_img_play);
        final ImageView btn_img_cancel = (ImageView) dialog.findViewById(R.id.btn_img_cancel);
        final ImageView btn_img_backward = (ImageView) dialog.findViewById(R.id.btn_img_backward);
        final ImageView btn_img_forward = (ImageView) dialog.findViewById(R.id.btn_img_forward);
        final ImageView btn_img_previous_song = (ImageView) dialog.findViewById(R.id.btn_img_previous_song);
        final ImageView btn_img_next_song = (ImageView) dialog.findViewById(R.id.btn_img_next_song);
        final ImageView btn_img_replay = (ImageView) dialog.findViewById(R.id.btn_img_replay);
        final SeekBar progressBar = (SeekBar) dialog.findViewById(R.id.progressBar);

        text_title.setText("播放");
        text_content.setText(UrlPlayerDialog_bg.this.message);

        new ImageButtonImageHelper(R.drawable.mp3_play_unpressed, R.drawable.mp3_pause_unpressed, btn_img_play);
        new ImageButtonImageHelper(R.drawable.mp3_stop_unpressed, R.drawable.going_icon, btn_img_cancel);
        new ImageButtonImageHelper(R.drawable.mp3_backward_unpressed, R.drawable.going_icon, btn_img_backward);
        new ImageButtonImageHelper(R.drawable.mp3_forward_unpressed, R.drawable.going_icon, btn_img_forward);
        new ImageButtonImageHelper(R.drawable.mp3_previous_song_unpressed, R.drawable.going_icon, btn_img_previous_song);
        new ImageButtonImageHelper(R.drawable.mp3_next_song_unpressed, R.drawable.going_icon, btn_img_next_song);
        new ImageButtonImageHelper(R.drawable.mp3_replay_unpressed, R.drawable.going_icon, btn_img_replay);

        btn_img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    if (urlPlayerServiceHander.get().getMService().isInitDone()) {
//                        String name = UrlPlayerDialog_bg.this.bean.getName();
//                        String url = UrlPlayerDialog_bg.this.bean.getUrl();
//                        Log.v(TAG, "clickStartPlay : " + name + " , " + url);
//                        String result = urlPlayerServiceHander.get().getMService().startPlay(name, url);
//                        if (StringUtils.isNotBlank(result)) {
//                            Validate.isTrue(false, result);
//                        }
//                        Toast.makeText(context, "開始撥放", Toast.LENGTH_SHORT).show();
//                    } else {
                    urlPlayerServiceHander.get().getMService().pauseAndResume();
                    String currentStatusMsg = urlPlayerServiceHander.get().getMService().isPlaying() ? "播放中" : "暫停";
                    Toast.makeText(context, currentStatusMsg, Toast.LENGTH_SHORT).show();
//                    }
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
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.getUrl(), -1);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                    } else {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.getUrl(), -1);
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
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.getUrl(), -1);
                        if (StringUtils.isNotBlank(result)) {
                            Validate.isTrue(false, result);
                        }
                    } else {
                        String result = urlPlayerServiceHander.get().getMService().startPlay(UrlPlayerDialog_bg.this.bean.getName(), UrlPlayerDialog_bg.this.bean.getUrl(), -1);
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

        btn_img_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReplayModeType rMode = ReplayModeType.nextVal(replayMode);
                    replayMode = rMode.mode;
                    rMode.apply(UrlPlayerDialog_bg.this);
                } catch (RemoteException e) {
                    Log.e(TAG, "btn_img_replay ERR : " + e.getMessage(), e);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    try {
                        if (mPercentProgressBarTimer.isPercentProgressTrigger.get()) {
                            mPercentProgressBarTimer.isPercentProgressTrigger.set(false);
                            return;
                        }
                    } catch (Exception ex) {
                    }
                    urlPlayerServiceHander.get().getMService().onProgressChange(progress);
                } catch (RemoteException e) {
                    Log.e(TAG, "progressBar ERR : " + e.getMessage(), e);
                    Toast.makeText(context, "mp3讀取錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //初始化服務
        this.initService(progressBar, text_timer, text_title, text_content);

        return dialog;
    }

    private void initService(SeekBar progressBar, TextView text_timer, TextView text_title, TextView text_content) {
        if (mPercentProgressBarTimer != null) {
            try {
                mPercentProgressBarTimer.timer.cancel();
            } catch (Exception ex) {
            }
        }
        mPercentProgressBarTimer = new PercentProgressBarTimer(this.context, progressBar, text_timer, text_title, text_content);
    }

    private static class PercentProgressBarTimer {
        private AtomicBoolean isPercentProgressTrigger = new AtomicBoolean(false);
        private Context context;
        private Timer timer;
        private SeekBar progressBar;
        private TextView textTimer;
        private TextView textContent;
        private TextView textTitle;
        private final Handler handler = new Handler();

        public SeekBar getProgressBar() {
            return progressBar;
        }

        public TextView getTextTimer() {
            return textTimer;
        }

        public TextView getTextContent() {
            return textContent;
        }

        public TextView getTextTitle() {
            return textTitle;
        }

        private PercentProgressBarTimer(final Context context, SeekBar progressBar, TextView textTimer, TextView textTitle, TextView textContent) {
            this.progressBar = progressBar;
            this.textTimer = textTimer;
            this.textContent = textContent;
            this.textTitle = textTitle;
            this.context = context;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (urlPlayerServiceHander.get() == null || !urlPlayerServiceHander.get().getMService().isInitDone()) {
                            return;
                        }

                        final int percent = urlPlayerServiceHander.get().getMService().getProgressPercent();
                        final String timeTxt = urlPlayerServiceHander.get().getMService().getProgressTime();
                        final Map<String, String> map = urlPlayerServiceHander.get().getMService().getCurrentBean();
                        Log.v(TAG, "[PercentProgressBarTimer] == " + timeTxt + "\t" + percent);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                isPercentProgressTrigger.set(true);
                                getProgressBar().setProgress(percent);
                                getTextTimer().setText(timeTxt);
                                getTextTitle().setText(map.get("name"));
                                getTextContent().setText(map.get("path"));
                            }
                        });
                    } catch (final Exception e) {
                        Log.e(TAG, "[PercentProgressBarTimer] ERR : " + e.getMessage(), e);
                    }
                }
            }, 0, 1000L);
        }
    }

    public static void stopService(Context context) {
        try {
            urlPlayerServiceHander.get().getMService().stopSelf();
            urlPlayerServiceHander.get().startStopService(false, context);
            Toast.makeText(context, "停止服務!!", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
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

    private class UrlPlayerServiceHander {
        private boolean isClose = false;
        private IUrlPlayerService mService;
        private ServiceConnection mConnection;

        private IUrlPlayerService getMService() {
            Log.v(TAG, "[getMService]" + mService);
            return mService;
        }

        private void init(Context context) {
            mConnection = getMConnection();
            startStopService(true, context);
            this.bindServiceMethod(true, context);
        }

        private void bindServiceMethod(boolean isOn, Context context) {
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
                    isClose = false;
                    Log.v(TAG, "[mService] init " + mService);
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    Log.v(TAG, "[onServiceDisconnected] called");
                    mService = null;
                    isClose = true;
                    Log.v(TAG, "[mService] setNull ");
                }
            };
        }

        /**
         * 開啟/停止 服務
         */
        private void startStopService(boolean isStart, Context context) {
            ServiceUtil.startStopService(isStart, context, UrlPlayerService.class);
        }

        private boolean initNotDone(Context context) {
            Log.e(TAG, "#...initNotDone...start");
            if (!ServiceUtil.isServiceRunning(context, UrlPlayerService.class)) {
                Log.e(TAG, "\tServiceUtil.isServiceRunning : off");
                return true;
            }
            if (mService == null || mConnection == null) {
                Log.e(TAG, "\tmService : " + mService);
                Log.e(TAG, "\tmConnection : " + mConnection);
                return true;
            }
            return false;
        }
    }

    private enum ReplayModeType {
        None(-1) {
            @Override
            void apply(UrlPlayerDialog_bg self) {
                Mp3Bean bean = self.bean;
                List<Mp3Bean> totalUrlList = self.totalUrlList;

                Toast.makeText(self.context, "無重複撥放", Toast.LENGTH_SHORT).show();
            }
        },//
        ReplayOne(1) {
            @Override
            void apply(UrlPlayerDialog_bg self) throws RemoteException {
                Mp3Bean bean = self.bean;
                List<Mp3Bean> totalUrlList = self.totalUrlList;

                Map<String, String> toMap = new HashMap<String, String>();
                toMap.put(bean.getName(), bean.getUrl());
                self.urlPlayerServiceHander.get().getMService().setReplayMode(bean.getName(), bean.getLastPositionInt(), Arrays.asList(bean.getName()), Arrays.asList(bean.getUrl()), false);
                Toast.makeText(self.context, "重複播放一首", Toast.LENGTH_SHORT).show();
            }
        },//
        ReplayAll(2) {
            @Override
            void apply(UrlPlayerDialog_bg self) throws RemoteException {
                Mp3Bean bean = self.bean;
                List<Mp3Bean> totalUrlList = self.totalUrlList;

                List<String> nameLst = new ArrayList<>();
                List<String> pathLst = new ArrayList<>();
                for (Mp3Bean b : totalUrlList) {
                    nameLst.add(b.getName());
                    pathLst.add(b.getUrl());
                }

                self.urlPlayerServiceHander.get().getMService().setReplayMode(bean.getName(), bean.getLastPositionInt(), nameLst, pathLst, false);
                Toast.makeText(self.context, "重複播放全部", Toast.LENGTH_SHORT).show();
            }
        },//
        ReplayAll_Random(3) {
            @Override
            void apply(UrlPlayerDialog_bg self) throws RemoteException {
                Mp3Bean bean = self.bean;
                List<Mp3Bean> totalUrlList = self.totalUrlList;

                List<String> nameLst = new ArrayList<>();
                List<String> pathLst = new ArrayList<>();
                for (Mp3Bean b : totalUrlList) {
                    nameLst.add(b.getName());
                    pathLst.add(b.getUrl());
                }

                self.urlPlayerServiceHander.get().getMService().setReplayMode(bean.getName(), bean.getLastPositionInt(), nameLst, pathLst, true);
                Toast.makeText(self.context, "重複播放全部(隨機)", Toast.LENGTH_SHORT).show();
            }
        },//
        ;

        int mode;

        ReplayModeType(int mode) {
            this.mode = mode;
        }

        private static ReplayModeType nextVal(int mode) {
            ReplayModeType e = valueOfByVal(mode);
            ReplayModeType[] es = values();
            for (int ii = 0; ii < es.length; ii++) {
                ReplayModeType e1 = es[ii];
                if (e1.mode == mode) {
                    if (es.length > e1.ordinal() + 1) {
                        return es[e1.ordinal() + 1];
                    } else {
                        return None;
                    }
                }
            }
            return None;
        }

        private static ReplayModeType valueOfByVal(int mode) {
            for (ReplayModeType e : ReplayModeType.values()) {
                if (e.mode == mode) {
                    return e;
                }
            }
            return None;
        }

        abstract void apply(UrlPlayerDialog_bg self) throws RemoteException;
    }
}
