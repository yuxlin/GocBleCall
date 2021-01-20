package cn.kaer.bluetooth.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author wanghx
 * @date 2020/5/19
 * @description
 */
public class CalllogUtil {
    private static final String TAG = "JusCallListener";

    public static Uri addCallLog(Context context, String number, String name, int type, long currentTimemills, long callInterval) {
        Log.d(TAG, "计入数据库writeCallLog+++phoneNum:" + number + "\tname：" + name + "\ttype：" + type + "\tcounter:" + callInterval + "\tnew:" + 0);
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        if (!TextUtils.isEmpty(name)) {
            values.put(CallLog.Calls.CACHED_NAME, name);
        }
        values.put(CallLog.Calls.TYPE, type);// CallLog.Calls.OUTGOING_TYPE
        values.put(CallLog.Calls.DATE, currentTimemills);// System.currentTimeMillis()
        values.put(CallLog.Calls.DURATION, callInterval);
        values.put(CallLog.Calls.NEW, 0);
        // values.put(CallLog.Calls.DATA_USAGE, content);
        values.put(CallLog.Calls.FEATURES,"bluetooth");
        return context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

    @SuppressLint("MissingPermission")
    public static int updateCalllog(Context context,String id,String recordId){
        Log.e(TAG,"updateCalllog id = "+id+"\trecordId = "+recordId);
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.TRANSCRIPTION, recordId);
        int result = context.getContentResolver().update(CallLog.Calls.CONTENT_URI,values,"_id = ?",new String[]{id});
        Log.e(TAG, "update record="+result );
        return result;
    }

}
