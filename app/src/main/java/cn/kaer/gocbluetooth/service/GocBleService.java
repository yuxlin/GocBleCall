package cn.kaer.gocbluetooth.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.goodocom.gocsdk.IGocsdkService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.entity.BlueToothPairedInfo;
import cn.kaer.gocbluetooth.IGoBluetoothCallback;
import cn.kaer.gocbluetooth.IGocBluetoothService;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.entity.ContactInfo;
import cn.kaer.gocbluetooth.receiver.BlueToothHandler;
import cn.kaer.gocbluetooth.ui.tmkphone.BleCallActivity;
import cn.kaer.gocbluetooth.ui.tmkphone.HandlerCallsActivity;
import cn.kaer.gocbluetooth.utils.NotificationHelper;

public class GocBleService extends Service implements OnGocsdkCallback.OnDeviceConnectCallback, OnGocsdkCallback.OnBleCallStateListen, OnGocsdkCallback.OnContactSyncListen {
    private String TAG = getClass().getSimpleName();
    private IGocSdkController mGocSdkController;

    private IGoBluetoothCallback mIGoBleCallback;
    private Handler mHandler = new Handler();
    private static final int notificationId = 1337;
    private boolean isShowing = false;
    private BlueToothHandler mBlueToothReceiver;
    private volatile List<ContactInfo> mContactInfoList = new ArrayList<>();
    private static GocBleService instance;

    public static GocBleService getInstance() {
        return instance;
    }

    class GocBleBinder extends IGocBluetoothService.Stub {

        public GocBleBinder() {

        }

        @Override
        public boolean isConnectedDevice() throws RemoteException {
            return mGocSdkController.getHfpStatus() >= 3;
        }

        @Override
        public void phoneDial(String num) throws RemoteException {
            Log.e(TAG, "phone dial:" + num);
            BleCallActivity.startBleCallActivity(GocBleService.this, num, BleCallActivity.CALL_TYPE_OUTGOING);

        }

        @Override
        public void setOnBleServiceCallback(IGoBluetoothCallback callback) throws RemoteException {
            Log.e(TAG, "setOnBleServiceCallback");
            mIGoBleCallback = callback;
            mHandler.postDelayed(() -> {
                if (mIGoBleCallback != null) {

                    try {
                        if (mGocSdkController.getHfpStatus() >= 3) {
                            mIGoBleCallback.onDeviceConnected(mGocSdkController.getCurrentConnDevice().address);
                            mIGoBleCallback.onContactSync(contact2Json());
                        } else {
                            mIGoBleCallback.onDeviceDisconnect();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);
        }
    }

    public GocBleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        startService(new Intent(getApplicationContext(), GocBleService.class));
        return new GocBleBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        instance = this;

        // showFloatWindows();
        mGocSdkController = GocSdkController.get();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "HFP STATUS:" + mGocSdkController.getHfpStatus());
                setNotificationState(mGocSdkController.getHfpStatus() >= 3);
            }
        }, 1000);


        initBleConnectListen();

        initSystemBlueToothListen();

        initContactSyncListen();


    }


    private void initSystemBlueToothListen() {
        mBlueToothReceiver = BlueToothHandler.registerBroadcast(this, new BlueToothHandler.OnBlueToothListen() {
            @Override
            public void onTurnOn() {
                Log.e(TAG, "onBTOpen");
                if (mGocSdkController.isBleOpen()) {
                    Log.e(TAG, "gocble close");
                    mGocSdkController.closeBlueTooth();
                }
            }

            @Override
            public void onBTOpen() {


            }

            @Override
            public void onBTClose() {

            }
        });
    }

    private void initBleConnectListen() {
        mGocSdkController.registerDeviceConnCallback(this);
        mGocSdkController.registerCallStateCallback(this);
        mHandler.postDelayed(() -> {

            if (mGocSdkController.getHfpStatus() >= 3) {
                mGocSdkController.syncContacts();
            }

        }, 1000);

    }


    /*----连接状态监听*/
    @Override
    public void onLocalAddress(String addr) {

    }

    @Override
    public void onLocalName(String name) {

    }

    @Override
    public void onInit(int state) {
        Log.e(TAG, "onInit:" + state);

        try {
            if (mIGoBleCallback != null) {
                if (state == 0) {
                    mIGoBleCallback.onBleStateChange(true);
                } else if (state == 1) {
                    mIGoBleCallback.onBleStateChange(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCurrentAndPairList(BlueToothPairedInfo blueToothPairedInfo) {

    }

    @Override
    public void onCurrentConnAddr(String addr) {

    }

    @Override
    public void onCurrentConnName(String name) {

    }

    @Override
    public void onDeviceConnected(String addr) {
        Log.e(TAG, "onDeviceConnected:" + addr);
        setNotificationState(true);

        mHandler.postDelayed(() -> {
            mContactInfoList.clear();
            GocSdkController.get().syncContacts();
        }, 1000);
        if (mIGoBleCallback != null) {
            try {
                mIGoBleCallback.onDeviceConnected(addr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDeviceDisconnected(String address) {
        Log.e(TAG, "onDeviceDisconnected:" + address);
        setNotificationState(false);
        if (mIGoBleCallback != null) {
            try {


                mIGoBleCallback.onDeviceDisconnect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /*通话状态监听*/
    @Override
    public void onOutgoingCall(String num) {
        Log.e(TAG, "onOutgoingCall:" + num);
        if (TextUtils.isEmpty(num)) {
            num = "00000000000";
        }
        BleCallActivity.startBleCallActivity(this, num, BleCallActivity.CALL_TYPE_OUTGOING);
    }

    @Override
    public void onInComingCall(String num) {
        Log.e(TAG, "来电：" + num);
        BleCallActivity.startBleCallActivity(GocBleService.this, num, BleCallActivity.CALL_TYPE_INCOMING);
    }

    @Override
    public void onCallConnectSuccess(String num) {
        buildNotification(notificationId, getString(R.string.in_calling));
    }

    @Override
    public void onChannelTransfer(boolean isRemote) {

    }

    @Override
    public void onHookUp() {
        buildNotification(notificationId, getString(R.string.app_name));

    }


    private void initContactSyncListen() {
        mGocSdkController.registerContactSyncCallback(this);

    }

    /*联系人同步*/
    @Override
    public void onSyncStart() {
        Log.e(TAG, "onSyncStart");
    }

    @Override
    public void onReceiveItem(String name, String num) {
        Log.e(TAG, "onReceiveItem:" + name + "--" + num);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName(name);
        contactInfo.setNum(num);
        mContactInfoList.add(contactInfo);
    }

    @Override
    public void onDone() {
        Log.e(TAG, "onDone contact size:" + mContactInfoList.size());
        if (mContactInfoList.size() == 0) return;


        if (mIGoBleCallback != null) {
            try {
                mIGoBleCallback.onContactSync(contact2Json());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private String contact2Json() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < mContactInfoList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", mContactInfoList.get(i).getName());
                jsonObject.put("num", mContactInfoList.get(i).getNum());
                jsonArray.put(jsonObject);
            } catch (Exception ignore) {
            }
        }
        return jsonArray.toString();
    }

    public ContactInfo queryBluetoothContact(String num) {
        Log.e(TAG, "queryBluetoothContact:" + num);
        for (int i = 0; i < mContactInfoList.size(); i++) {
            try {
                ContactInfo contactInfo = mContactInfoList.get(i);
                if (num.length() > 10 && num.length() < 16) {
                    if (num.contains(contactInfo.getNum()) || contactInfo.getNum().contains(num)) {
                        return contactInfo;
                    }
                }
                if (contactInfo.getNum().equals(num)) {
                    return contactInfo;
                }
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        Log.e(TAG, "onDestroy");
        mGocSdkController.unRegisterCallStateCallback(this);
        mGocSdkController.unRegisterDeviceConnCallback(this);
        unregisterReceiver(mBlueToothReceiver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotificationState(boolean isShow) {
        Log.e(TAG, "setNotificationState:" + isShow);
        if (isShowing == isShow) {
            return;
        }
        isShowing = isShow;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    buildNotification(notificationId, getString(R.string.app_name));
                } else {
                    NotificationHelper.cancel(notificationId);
                }
            }
        });
    }


    private void buildNotification(int id, String title) {
        NotificationHelper.ChannelConfig defaultChannelConfig = NotificationHelper.ChannelConfig.DEFAULT_CHANNEL_CONFIG;
        defaultChannelConfig.setSound(null, null);
        NotificationHelper.notify(id, defaultChannelConfig, new Utils.Consumer<NotificationCompat.Builder>() {
            @Override
            public void accept(NotificationCompat.Builder builder) {
                builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
                builder.setSound(null);
                builder.setVibrate(new long[]{0});
                builder.setContentTitle(title);
                builder.setOngoing(true);
                builder.setSmallIcon(R.mipmap.blecall2);
                Intent intent = new Intent(GocBleService.this, HandlerCallsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(GocBleService.this, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setFullScreenIntent(pendingIntent, true);

            }
        });
    }

    private void showFloatWindows() {
        /*ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GocBleService.this, HandlerCallsActivity.class));
            }
        });
        FloatWindow
                .with(getApplicationContext())
                .setView(imageView)
                .setWidth(100)                               //设置控件宽高
                .setHeight(Screen.width, 0.2f)
                .setX(0)                                   //设置控件初始位置
                .setY(Screen.height, 0.3f)
                .setFilter(false, BleCallActivity.class)
                .setFilter(false, HandlerCallsActivity.class)
                .setDesktopShow(true)
                //桌面显示
                //  .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                // .setPermissionListener(mPermissionListener)  //监听权限申请结果
                .build();*/
    }
}
