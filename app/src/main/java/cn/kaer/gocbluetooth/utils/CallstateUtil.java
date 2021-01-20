package cn.kaer.gocbluetooth.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import cn.kaer.common.utils.LogUtils;


public class CallstateUtil {
    private static final String BASE_CALLSTATE_URI_PATH = "content://com.kaer.callstate/";
    private static final String BASE_CALLVIEW_URI_PATH = "content://com.kaer.callview";

    /**
     * 查询所有模式的通话状态
     *
     * @param context
     * @return
     */
    public static Cursor queryAllState(Context context) {
        return context.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH), null, null, null, null);
    }

    /**
     * 重制所有通化模式的状态
     */
    public static void resetAllCallstate(Context context) {
        Cursor cursor = queryAllState(context);
        if (null == cursor || cursor.getCount() == 0) {
            return;
        }
        ContentValues cval = new ContentValues();
        cval.put("calltype", "wireless");
        cval.put("state", PSTNConf.CALLSTATE_IDLE);
        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), cval, null, null);

        ContentValues cval2 = new ContentValues();
        cval.put("calltype", "pstn");
        cval.put("state", PSTNConf.CALLSTATE_IDLE);
        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), cval2, null, null);

        ContentValues cval3 = new ContentValues();
        cval.put("calltype", "bluetooth");
        cval.put("state", PSTNConf.CALLSTATE_IDLE);
        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), cval3, null, null);


    }

    /**
     * 查询正在通话状态的通话模式
     *
     * @param mContext
     * @return -1 所有模式都没有通话
     */
    public static int queryIncallMode(Context mContext) {
        Cursor rCursor = mContext.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH), null, null, null, null);
        if (rCursor != null && rCursor.getCount() > 0) {
            while (rCursor.moveToNext()) {
                int calltype = rCursor.getInt(rCursor.getColumnIndex("calltype"));
                int state = rCursor.getInt(rCursor.getColumnIndex("state"));
                if (state == PSTNConf.CALLSTATE_INCALL) {
                    //正在通话中的calltype返回
                    rCursor.close();
                    return calltype;
                }
            }
        }
        rCursor.close();
        return -1;
    }

    /**
     * 写蓝牙通话状态
     *
     * @param mcontext
     * @param callstate
     */
    public static void writeBluetoothCallstate(Context mcontext, int callstate) {
        ContentValues cv = new ContentValues();
        cv.put("calltype", "bluetooth");
        cv.put("state", callstate);
        mcontext.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), cv, null, null);
    }

    public static boolean isInPSTNCall(Context mcontext) {
        Cursor resultCorsor = mcontext.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=pstn"), null, null, null, null);
        if (resultCorsor != null && resultCorsor.getCount() > 0) {
            resultCorsor.moveToNext();
            int callstate = resultCorsor.getInt(resultCorsor.getColumnIndex("state"));
            if (callstate >= PSTNConf.CALLSTATE_RINGING) {
                resultCorsor.close();
                return true;
            }
        }
        resultCorsor.close();
        return false;
    }

    public static boolean isInWirelessCall(Context mcontext) {
        Cursor resultCorsor = mcontext.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=wireless"), null, null, null, null);
        if (resultCorsor != null && resultCorsor.getCount() > 0) {
            resultCorsor.moveToNext();
            if (resultCorsor.getInt(resultCorsor.getColumnIndex("state")) > PSTNConf.CALLSTATE_RINGING) {//此条件满足响铃和接通
                resultCorsor.close();
                return true;
            }
        }
        resultCorsor.close();
        return false;
    }

    public static boolean isInWirelessComingCall(Context mcontext) {
        Cursor resultCorsor = mcontext.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=wireless"), null, null, null, null);
        if (resultCorsor != null && resultCorsor.getCount() > 0) {
            resultCorsor.moveToNext();
            if (resultCorsor.getInt(resultCorsor.getColumnIndex("state")) > 0) {
                resultCorsor.close();
                return true;
            }
        }
        resultCorsor.close();
        return false;
    }

    public static boolean isInBluetoothCall(Context mcontext) {
        Cursor resultCorsor = mcontext.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=bluetooth"), null, null, null, null);
        if (resultCorsor != null && resultCorsor.getCount() > 0) {
            resultCorsor.moveToNext();
            int curstate = resultCorsor.getInt(resultCorsor.getColumnIndex("state"));
             LogUtils.d("starttag", "blue当前状态：" + curstate);
          
            if (curstate > PSTNConf.CALLSTATE_IDLE) {
                resultCorsor.close();
                return true;
            }
        }
        resultCorsor.close();
        return false;
    }


    public static int queryPSTNState(Context context) {
        Cursor queryc = context.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=pstn"), null, null, null, null);
        if (queryc != null && queryc.getCount() > 0) {
            queryc.moveToNext();
            int state = queryc.getInt(queryc.getColumnIndex("state"));
            queryc.close();
            return state;
        }
        queryc.close();
        return 0;
    }

    public static int queryWirelessState(Context context) {
        Log.e("queryWirelessState", BASE_CALLSTATE_URI_PATH);
        Cursor queryc = context.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=wireless"), null, null, null, null);
        if (queryc == null) {
            return 0;
        }
        if (queryc.getCount() > 0) {
            queryc.moveToNext();
            int state = queryc.getInt(queryc.getColumnIndex("state"));
            queryc.close();
            return state;
        }
        queryc.close();
        return 0;
    }

    public static int queryBluetoothState(Context context) {
        Cursor queryc = context.getContentResolver().query(Uri.parse(BASE_CALLSTATE_URI_PATH + "?calltype=bluetooth"), null, null, null, null);
        if (queryc == null) {
            return 0;
        }
        if (queryc != null && queryc.getCount() > 0) {
            queryc.moveToNext();
            int state = queryc.getInt(queryc.getColumnIndex("state"));
            queryc.close();
            return state;
        }
        queryc.close();
        return 0;
    }


    public static void updateWirelessState(Context context, int callstate) {
        ContentValues contentv = new ContentValues();
        contentv.put("calltype", "wireless");
        contentv.put("state", callstate);

        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), contentv, null, null);
        context.getContentResolver().notifyChange(Uri.parse(BASE_CALLSTATE_URI_PATH), null);
    }

    public static void updatePSTNCallState(Context context, int callstate) {
        ContentValues contentv = new ContentValues();
        contentv.put("calltype", "pstn");
        contentv.put("state", callstate);

        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), contentv, null, null);
        context.getContentResolver().notifyChange(Uri.parse(BASE_CALLSTATE_URI_PATH), null);
    }

    public static void updateBluetoothState(Context context, int callstate) {
        ContentValues contentv = new ContentValues();
        contentv.put("calltype", "bluetooth");
        contentv.put("state", callstate);

        context.getContentResolver().update(Uri.parse(BASE_CALLSTATE_URI_PATH), contentv, null, null);
        context.getContentResolver().notifyChange(Uri.parse(BASE_CALLSTATE_URI_PATH), null);
    }

    public static void addWirelessClz(Context context, String clzName) {
        try {
             LogUtils.d("addWirelessClz clzname = {}", clzName);
            Cursor cursor = context.getContentResolver().query(Uri.parse(BASE_CALLVIEW_URI_PATH), null, "name=" + clzName, null, null);
            if (cursor == null) {
                return;
            }
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String clz = cursor.getString(cursor.getColumnIndex("name"));
                 LogUtils.d("addWirelessClz exit clzname = {}", clz);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", clzName);
                context.getContentResolver().insert(Uri.parse(BASE_CALLVIEW_URI_PATH), contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
