package com.example.englishtester.common;

import android.content.Context;
import android.os.PowerManager;
import android.widget.Toast;

public class LockScreenHelper {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private String tag;

    public LockScreenHelper(Context context, String tag) {
        this.context = context;
        this.tag = tag;
    }

    public void toggle() {
        if (mWakeLock == null) {
            lock();
            Toast.makeText(context, "螢幕鎖定(on)", Toast.LENGTH_SHORT).show();
        } else {
            unlock();
            Toast.makeText(context, "螢幕鎖定(off)", Toast.LENGTH_SHORT).show();
        }
    }

    public void lock() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, tag);
        mWakeLock.acquire();
    }

    public void unlock() {
        if (mWakeLock == null) {
            return;
        }
        mWakeLock.release();
        mWakeLock = null;
    }
}
