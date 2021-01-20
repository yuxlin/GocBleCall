package cn.kaer.gocbluetooth.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * User: yxl
 * Date: 2021/1/8
 */
public class AuthNvRam {
    private String TAG = getClass().getSimpleName();
    private String mAuthData = "";
    private OnAuthCallback mAuthBlueListen;
    private OnAuthCallback mAuthAIListen;
    private OnAuthCallback mAuthFaceUnlockListen;
    private static final int MSG_WRITE_AUTH = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == MSG_WRITE_AUTH) {
                boolean auth = (boolean) msg.obj;
                Log.e(TAG, "write auth:" + auth);
            }
        }
    };

    public interface OnAuthCallback {
        void onResult(boolean authorized);
    }

    private Context mContext;

    private AuthNvRam() {
    }

    private volatile static AuthNvRam instance;

    public synchronized static AuthNvRam get() {
        if (instance == null) {
            instance = new AuthNvRam();
        }
        return instance;
    }

    public AuthNvRam init(Context context) {
        if (mContext != null) return instance;
        mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.kaer.action.nvdata.rlicense");
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);


        Intent intent = new Intent("cn.kaer.action.nvdata.rw");
        intent.putExtra("type", "read");
        mContext.sendBroadcast(intent); //初始化查询一次授权
        return instance;
    }

    public AuthNvRam unInit() {
        try {
            if (mContext != null) {
                mContext.unregisterReceiver(mBroadcastReceiver);
            }
        } catch (Exception ignore) {
        }
        mContext = null;
        return instance;
    }


    public AuthNvRam queryBluetoothAuth(OnAuthCallback onAuthCallback) {
        mAuthBlueListen = onAuthCallback;
        Intent intent = new Intent("cn.kaer.action.nvdata.rw");
        intent.putExtra("type", "read");
        mContext.sendBroadcast(intent);
        return this;
    }

    public AuthNvRam queryFaceUnlockAuth(OnAuthCallback onAuthCallback) {
        mAuthFaceUnlockListen = onAuthCallback;
        Intent intent = new Intent("cn.kaer.action.nvdata.rw");
        intent.putExtra("type", "read");
        mContext.sendBroadcast(intent);
        return this;
    }

    public AuthNvRam queryAISpeechRecognitionAuth(OnAuthCallback onAuthCallback) {
        mAuthAIListen = onAuthCallback;
        Intent intent = new Intent("cn.kaer.action.nvdata.rw");
        intent.putExtra("type", "read");
        mContext.sendBroadcast(intent);
        return this;
    }


    /********/
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                mAuthData = intent.getStringExtra("license");
                Log.e(TAG, "rec data:" + mAuthData);

                if (mAuthAIListen != null) {
                    mAuthAIListen.onResult(hasAuth(mAuthData, "AISpeech"));
                    mAuthAIListen = null;
                }

                if (mAuthBlueListen != null) {
                    mAuthBlueListen.onResult(hasAuth(mAuthData, "bluetooth"));
                    mAuthBlueListen = null;
                }


                if (mAuthFaceUnlockListen != null) {
                    mAuthFaceUnlockListen.onResult(hasAuth(mAuthData, "faceUnlock"));
                    mAuthFaceUnlockListen = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean hasAuth(String authData, String type) {

        try {
            JSONObject jsonObject = new JSONObject(authData);
            return jsonObject.getBoolean(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized void writeAuth(boolean bleCallAuth, boolean aiSpeechAuth, boolean faceUnlockAuth) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bluetooth", bleCallAuth);
            jsonObject.put("AISpeech", aiSpeechAuth);
            jsonObject.put("faceUnlock", faceUnlockAuth);
            mAuthData = jsonObject.toString();
            Intent intent = new Intent("cn.kaer.action.nvdata.rw");
            intent.putExtra("type", "write");
            intent.putExtra("license", mAuthData);

            Log.e(TAG, "write auth:" + mAuthData);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBluetoothAuth(final boolean authorize) {
      /*  mHandler.removeMessages(MSG_WRITE_AUTH);
        Message obtain = Message.obtain(mHandler, MSG_WRITE_AUTH, authorize);
        mHandler.sendMessageDelayed(obtain, 600);*/

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mAuthData)) {
                    Log.e(TAG, "write def false");
                    writeAuth(authorize, false, false);
                } else {
                    writeAuth(authorize, hasAuth(mAuthData, "AISpeech"), hasAuth(mAuthData, "faceUnlock"));
                }
            }
        }, 600);
    }


    public void writeAISpeechAuth(final boolean authorize) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mAuthData)) {
                    Log.e(TAG, "write def false");
                    writeAuth(false, authorize, false);
                } else {
                    writeAuth(hasAuth(mAuthData, "bluetooth"), authorize, hasAuth(mAuthData, "faceUnlock"));
                }
            }
        }, 600);
    }

    public void writeFaceUnlockAuth(final boolean authorize) {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mAuthData)) {
                    Log.e(TAG, "write def false");
                    writeAuth(false, false, authorize);
                } else {
                    writeAuth(hasAuth(mAuthData, "bluetooth"), hasAuth(mAuthData, "AISpeech"), authorize);
                }
            }
        }, 600);
    }

}
