package cn.kaer.gocbluetooth.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;

/**
 * User: yxl
 * Date: 2020/10/30
 */
public class SystemUtils {
    private static final String TAG = "SystemUtils";
    private static final Uri mUri = Uri.parse("content://com.android.dialer.calllog.DialerStateProvider/");
    private static final String KEY_LOUDSPEAKER_STATE = "loudspeaker_state";


    //设置外放还是听筒
    public static void setSoundChannel(Context context, boolean isHookOff) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (isHookOff) { //听筒
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(false);
            Log.d(TAG, "切换为听筒");
        } else {//外放
            Log.d(TAG, "切换为外放");
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
        }
    }

    public static boolean isOuterChannel(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        return audioManager.isSpeakerphoneOn();

    }


    public static String queryHookState(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = resolver.query(mUri, (String[]) null, (String) null, (String[]) null, (String) null);
        String ret = "";
        if (c != null && c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex("loudspeaker_state"));
            c.close();
        }

        return ret;
    }

    public static boolean isHookOff(Context mContext) {
        String rst = queryHookState(mContext);
        if ("inner".equals(rst)) {
            return true;
        } else {
            return "outside".equals(rst) ? false : false;
        }
    }
}
