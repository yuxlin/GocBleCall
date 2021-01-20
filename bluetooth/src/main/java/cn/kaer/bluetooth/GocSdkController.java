package cn.kaer.bluetooth;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.util.Log;

import com.goodocom.gocsdk.IGocsdkService;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.entity.BlueToothInfo;
import cn.kaer.bluetooth.service.GocsdkService;
import cn.kaer.common.utils.SharedPrefHelper;

/**
 * User: yxl
 * Date: 2020/8/19
 */
public class GocSdkController implements IGocSdkController {
    private String TAG = getClass().getSimpleName();
    private static IGocsdkService mIGocsdkService;
    private GocsdkService.GocsdkBinder mGocsdkBinder;
    private Context mContext;
    private GoSdkServiceConnection mGoSdkServiceConnection;
    private List<OnGocsdkCallback.OnDeviceSearchCallback> mOnDeviceSearchCallbacks = new ArrayList<>();
    private List<OnGocsdkCallback.OnDeviceConnectCallback> mOnDeviceConnectCallbacks = new ArrayList<>();
    private List<OnGocsdkCallback.OnBleCallStateListen> mOnBleCallStateListens = new ArrayList<>();
    private List<OnGocsdkCallback.OnContactSyncListen> mOnContactSyncListens = new ArrayList<>();

    private GocSdkController() {

    }

    private static GocSdkController instance;
    private static final int MSG_ONOFF_TIMEOUT = 0;
    private static final int MSG_CONNECT_DEVICE = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ONOFF_TIMEOUT:
                    Log.e(TAG, "MSG_ONOFF_TIMEOUT");
                    for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
                        mOnDeviceConnectCallbacks.get(i).onInit(-1);
                    }
                    break;
                case MSG_CONNECT_DEVICE:

                    try {

                        mIGocsdkService.connectDevice((String) msg.obj);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }
    };

    public static IGocSdkController get() {
        if (instance == null) {
            synchronized (GocSdkController.class) {
                instance = new GocSdkController();
            }
        }
        return instance;
    }


    @Override
    public IGocSdkController init(Context context) {
        mContext = context.getApplicationContext();

        initGocsdkService();
        return this;
    }

    @Override
    public void unInit() {
        if (mContext != null && mGoSdkServiceConnection != null)
            mContext.unbindService(mGoSdkServiceConnection);
    }

    private void initGocsdkService() {
        Intent intent = new Intent(mContext, GocsdkService.class);
        mGoSdkServiceConnection = new GoSdkServiceConnection();
        mContext.bindService(intent, mGoSdkServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public IGocsdkService getGocSdkService() {
        return mGocsdkBinder != null ? mGocsdkBinder.getIGocsdkService() : null;
    }

    @Override
    public void registerDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceCallback) {
        mOnDeviceSearchCallbacks.add(onDeviceCallback);
        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnDeviceSearchCallback(onDeviceCallback, true);
        }

    }

    @Override
    public void unRegisterDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceCallback) {
        mOnDeviceSearchCallbacks.remove(onDeviceCallback);
        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnDeviceSearchCallback(onDeviceCallback, false);
        }
    }

    @Override
    public void registerDeviceConnCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback) {
        mOnDeviceConnectCallbacks.add(onDeviceConnCallback);

        if (mGocsdkBinder != null) {
            mGocsdkBinder.setonDeviceConnectCallback(onDeviceConnCallback, true);
        }
    }

    @Override
    public void unRegisterDeviceConnCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback) {
        mOnDeviceConnectCallbacks.remove(onDeviceConnCallback);

        if (mGocsdkBinder != null) {
            mGocsdkBinder.setonDeviceConnectCallback(onDeviceConnCallback, false);
        }
    }

    @Override
    public void registerCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onCallStateCallback) {
        mOnBleCallStateListens.add(onCallStateCallback);

        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnCallStateCallback(onCallStateCallback, true);
        }
    }

    @Override
    public void unRegisterCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onCallStateCallback) {
        mOnBleCallStateListens.remove(onCallStateCallback);
        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnCallStateCallback(onCallStateCallback, false);
        }
    }

    @Override
    public void registerContactSyncCallback(OnGocsdkCallback.OnContactSyncListen onContactSyncListen) {
        mOnContactSyncListens.add(onContactSyncListen);
        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnContactSyncListen(onContactSyncListen, true);
        }

    }

    @Override
    public void unRegisterContactSyncCallback(OnGocsdkCallback.OnContactSyncListen onContactSyncListen) {
        mOnContactSyncListens.remove(onContactSyncListen);
        if (mGocsdkBinder != null) {
            mGocsdkBinder.setOnContactSyncListen(onContactSyncListen, false);
        }
    }

    @Override
    public void startSearchDevice() {
        try {
            if (mIGocsdkService != null)
                mIGocsdkService.startDiscovery();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopSearchDevice() {
        try {
            if (mIGocsdkService != null)
                mIGocsdkService.stopDiscovery();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getHfpStatus() {
        return BleConfig.hfpStatus;
    }

    @Override
    public BlueToothInfo getCurrentConnDevice() {
        return BleConfig.linkDevice;
    }

    @Override
    public void connectDevice(String addr) {

        if (mIGocsdkService != null) {
   /*         if (BleConfig.hfpStatus >= 3){
                disConnectDevice();
            }*/

            BleConfig.linkDevice.address = addr;
            BleConfig.hfpStatus = 2;

            Log.e(TAG, "connect address:" + addr);
            mHandler.removeMessages(MSG_CONNECT_DEVICE);
            mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_CONNECT_DEVICE, addr), 500);
        }

    }

    @Override
    public void loadAllStatus() {
        try {
            if (mIGocsdkService != null) {
                Log.e(TAG, "CallbackImp query all status");
                mIGocsdkService.inqueryHfpStatus();
                mIGocsdkService.inqueryA2dpStatus();
                mIGocsdkService.musicUnmute();
                mIGocsdkService.getLocalName();
                mIGocsdkService.getPinCode();
                mIGocsdkService.getCurrentDeviceAddr();
                mIGocsdkService.getCurrentDeviceName();
                mIGocsdkService.getImeiInfo();
                mIGocsdkService.getPairList();
                mIGocsdkService.getVersion();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openBlueTooth() {
        SharedPrefHelper.putBoolean(mContext, SharedKeys.BLE_SWITCH, true);
        mHandler.removeMessages(MSG_ONOFF_TIMEOUT);
        mHandler.sendEmptyMessageDelayed(MSG_ONOFF_TIMEOUT, 1000);

        if (mIGocsdkService != null) {
            try {
                mIGocsdkService.openBlueTooth();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeBlueTooth() {
        mHandler.removeMessages(MSG_ONOFF_TIMEOUT);
        mHandler.sendEmptyMessageDelayed(MSG_ONOFF_TIMEOUT, 1000);
        SharedPrefHelper.putBoolean(mContext, SharedKeys.BLE_SWITCH, false);

        if (mIGocsdkService != null) {
            try {
                mIGocsdkService.closeBlueTooth();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isBleOpen() {

        // return BleConfig.BLE_ON_OFF > 0;
        return SharedPrefHelper.getBoolean(mContext, SharedKeys.BLE_SWITCH, false);
    }

    @Override
    public void phoneDial(String callNum) {
        if (mIGocsdkService != null) {
            try {
                mIGocsdkService.phoneDail(callNum);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void phoneAnswer() {
        if (mIGocsdkService != null) {
            try {
                mIGocsdkService.phoneAnswer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hookUp() {
        if (mIGocsdkService != null) {
            try {
                mIGocsdkService.phoneHangUp();


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMuteState(boolean isOpen) {
        try {
            Log.e(TAG, "serMuteState:" + isOpen);
            if (mIGocsdkService != null)
                mIGocsdkService.muteOpenAndClose(isOpen ? 1 : 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCalling() {


        return BleConfig.hfpStatus > 3;
    }

    @Override
    public void switchToPhone(boolean isPhone) {

        try {
            if (mIGocsdkService != null) {
                if (isPhone) {
                    mIGocsdkService.phoneTransfer();
                } else {
                    mIGocsdkService.phoneTransferBack();
                    ;
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rename(String name) {
        try {
            if (mIGocsdkService != null) {
                mIGocsdkService.setLocalName(name);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syncContacts() {
        try {
            if (mIGocsdkService != null) {
                mIGocsdkService.phoneBookStartUpdate();

                for (int i = 0; i < mOnContactSyncListens.size(); i++) {
                    mOnContactSyncListens.get(i).onSyncStart();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void secondDial(String keyCodeString) {
        try {
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '#'};

            if (mIGocsdkService != null) {
                mIGocsdkService.phoneTransmitDTMFCode(chars[Integer.parseInt(keyCodeString)]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disConnectDevice() {

        try {
            if (mIGocsdkService != null) {
                mIGocsdkService.disconnect(BleConfig.linkDevice.address);
                mIGocsdkService.cancelAutoConnect();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deletePaired(String addr) {
        try {
            if (mIGocsdkService != null)
                mIGocsdkService.deletePair(addr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private class GoSdkServiceConnection implements ServiceConnection {
        // 绑定成功之后 会调用该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("app", "onServiceConnected");
            mGocsdkBinder = (GocsdkService.GocsdkBinder) service;
            mIGocsdkService = mGocsdkBinder.getIGocsdkService();
            try {
                mIGocsdkService.cancelAutoConnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {

                mGocsdkBinder.setonDeviceConnectCallback(mOnDeviceConnectCallbacks.get(i), true);
            }
            for (int i = 0; i < mOnDeviceSearchCallbacks.size(); i++) {
                mGocsdkBinder.setOnDeviceSearchCallback(mOnDeviceSearchCallbacks.get(i), true);
            }
            for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
                mGocsdkBinder.setOnCallStateCallback(mOnBleCallStateListens.get(i), true);
            }

            for (int i = 0; i < mOnContactSyncListens.size(); i++) {
                mGocsdkBinder.setOnContactSyncListen(mOnContactSyncListens.get(i), true);
            }
            connGocServiceSuccess();

        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIGocsdkService = null;

        }
    }

    private void connGocServiceSuccess() {
        try {

            boolean isOpen = SharedPrefHelper.getBoolean(mContext, SharedKeys.BLE_SWITCH, false);
            if (isOpen) {
                mIGocsdkService.openBlueTooth();
            } else {
                mIGocsdkService.closeBlueTooth();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAllStatus();
            }
        }, 100);


    }

}
