package cn.kaer.multichat.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author wanghx
 * @date 2019/7/9
 * @description
 */
public class HookStateUtil {
    private static final String TAG = "HookStateUtil";
    private static final Uri mUri = Uri.parse("content://com.android.dialer.calllog.DialerStateProvider/");
    private static final String KEY_LOUDSPEAKER_STATE = "loudspeaker_state";
    public static String queryHookState(Context mContext){
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = resolver.query(mUri, null, null, null, null);
        String ret = "";

        if (c != null && c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex(KEY_LOUDSPEAKER_STATE));
            c.close();
        }

        return ret;
    }

    /**
     * 检查手柄拿起状态
     * @param mContext
     * @return true拿起 false免提
     */
    public static boolean isHookOff(Context mContext){
        String rst = queryHookState(mContext);
        if ("inner".equals(rst)) {
            Log.d(TAG,"HOOK_OFF");
            return true;
        } else if ("outside".equals(rst)) {
            Log.d(TAG,"HOOK_ON");
            return false;
        }
        return false;
    }
}
