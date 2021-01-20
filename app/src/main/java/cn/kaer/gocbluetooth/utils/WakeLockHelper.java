package cn.kaer.gocbluetooth.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

import java.util.WeakHashMap;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.os.PowerManager.FULL_WAKE_LOCK;

public class WakeLockHelper {

    private KeyguardManager.KeyguardLock keyguardLock;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPm;

    private WakeLockHelper() {
    }

    private static WakeLockHelper instance;

    public static WakeLockHelper getInstance() {
        if (instance == null) {
            synchronized (WeakHashMap.class) {
                instance = new WakeLockHelper();
            }
        }
        return instance;
    }

    public void init(Context context) {
        initScreenWeaklock(context);
    }

    private void initScreenWeaklock(Context context) {
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (keyguardManager.isKeyguardLocked()) {
                keyguardLock = keyguardManager.newKeyguardLock("dialviewUnlockTag");
                keyguardLock.disableKeyguard();
            }
        }
        mPm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    //保持屏幕常亮
    public void keepScreenOn(long timeout) {
        mWakeLock = mPm.newWakeLock(FULL_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire(timeout);

    }

    //关闭屏幕常亮
    public void  releaseLock() {
        if (mWakeLock!=null && mWakeLock.isHeld())
            mWakeLock.release();
    }

    //点亮屏幕一次
    @SuppressLint("WakelockTimeout")
    public void wakeupScreen() {
        try {
            mWakeLock = mPm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            mWakeLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
