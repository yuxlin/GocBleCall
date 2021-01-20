package cn.kaer.bluetooth.callback;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.goodocom.gocsdk.IGocsdkCallback;

import java.util.ArrayList;
import java.util.List;

import cn.kaer.bluetooth.BleConfig;
import cn.kaer.bluetooth.entity.BlueToothPairedInfo;

public class GocsdkCallbackImp extends IGocsdkCallback.Stub {
    public static String number = "";
    public static int hfpStatus = 1;
    public static int a2dpStatus = 1;
    private String TAG = getClass().getSimpleName();

    private List<OnGocsdkCallback.OnDeviceSearchCallback> mOnDeviceSearchCallbacks = new ArrayList<>();
    private List<OnGocsdkCallback.OnDeviceConnectCallback> mOnDeviceConnectCallbacks = new ArrayList<>();
    private List<OnGocsdkCallback.OnBleCallStateListen> mOnBleCallStateListens = new ArrayList<>();
    private List<OnGocsdkCallback.OnContactSyncListen> mOnContactSyncListens = new ArrayList<>();


    private Context mContext;

    public GocsdkCallbackImp(Context context) {
        mContext = context;
    }

    public void setOnDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceSearchCallback, boolean isRegister) {
        Log.e(TAG, "setOnDeviceSearchCallback:" + isRegister);
        if (isRegister) {
            mOnDeviceSearchCallbacks.add(onDeviceSearchCallback);
        } else {
            mOnDeviceSearchCallbacks.remove(onDeviceSearchCallback);
        }


    }

    public void setonDeviceConnectCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback, boolean isRegister) {
        Log.e(TAG, "setonDeviceConnectCallback:" + isRegister);

        if (isRegister) {
            mOnDeviceConnectCallbacks.add(onDeviceConnCallback);
        } else {
            mOnDeviceConnectCallbacks.remove(onDeviceConnCallback);
        }
    }

    public void setOnCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onBleCallStateListen, boolean isRegister) {
        Log.e(TAG, "setOnCallStateCallback:" + isRegister);
        if (isRegister) {
            mOnBleCallStateListens.add(onBleCallStateListen);
        } else {
            mOnBleCallStateListens.remove(onBleCallStateListen);
        }
    }

    public void setOnContactSyncListen(OnGocsdkCallback.OnContactSyncListen onContactSyncListen, boolean isRegister) {
        if (isRegister) {
            mOnContactSyncListens.add(onContactSyncListen);
        } else {
            mOnContactSyncListens.remove(onContactSyncListen);
        }
    }

    @Override
    public void onHfpDisconnected(String address) throws RemoteException {
        Log.e(TAG, "onHfpDisconnected:" + address);
/*        if (TextUtils.isEmpty(address))
            return;*/

        BleConfig.hfpStatus = 1;

        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onDeviceDisconnected(address);
        }
    }

    @Override
    public void onHfpConnected(String address) throws RemoteException {

     /*   if (TextUtils.isEmpty(address)){
            address = BleConfig.linkDevice.address;
        }
        Log.e(TAG, "onHfpConnected:" + address+"--"+BleConfig.linkDevice.name);*/
        BleConfig.hfpStatus = 3;

        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onDeviceConnected(address);
        }
    }

    @Override
    public void onCallSucceed(String number) throws RemoteException {
        Log.e(TAG, "onCallSucceed:" + number);

    }

    @Override
    public void onIncoming(String number) throws RemoteException {
        Log.e(TAG, "onIncoming:" + number);
        BleConfig.hfpStatus = 5;
        for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
            mOnBleCallStateListens.get(i).onInComingCall(number);
        }
        mContext.sendBroadcast(new Intent("cn.kaer.blecall2.incoming")
                .putExtra("phoneNum", number));

    }

    @Override
    public void onHangUp() throws RemoteException {
        Log.e(TAG, "onHangUp");
        BleConfig.hfpStatus = 3;
        for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
            mOnBleCallStateListens.get(i).onHookUp();
        }

    }

    @Override
    public void onTalking(String number) throws RemoteException {
        Log.e(TAG, "onTalking");

        BleConfig.hfpStatus = 6;


        for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
            mOnBleCallStateListens.get(i).onCallConnectSuccess(number);
        }


    }

    @Override
    public void onRingStart() throws RemoteException {
        Log.e(TAG, "onRingStart");
    }

    @Override
    public void onRingStop() throws RemoteException {
        Log.e(TAG, "onRingStop");
    }

    @Override
    public void onHfpLocal() throws RemoteException {
        Log.e(TAG, "onHfpLocal");
        Log.e(TAG, "onInPairMode");
        if (mOnBleCallStateListens != null) {
            for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
                mOnBleCallStateListens.get(i).onChannelTransfer(false);
            }
        }
    }

    @Override
    public void onHfpRemote() throws RemoteException {
        Log.e(TAG, "onHfpRemote");
        if (mOnBleCallStateListens != null) {
            for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
                mOnBleCallStateListens.get(i).onChannelTransfer(true);
            }
        }
    }

    @Override
    public void onInPairMode() throws RemoteException {

    }

    @Override
    public void onExitPairMode() throws RemoteException {
        Log.e(TAG, "onExitPairMode");
    }

    @Override
    public void onInitSucceed(String init) throws RemoteException {
        Log.e(TAG, "onInitSucceed:" + init);
        if (TextUtils.isEmpty(init)) return;


        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {

            if (init.equals("0")) {
                BleConfig.BLE_ON_OFF = 0;
                mOnDeviceConnectCallbacks.get(i).onInit(0);
            } else if (init.equals("1")) {
                BleConfig.BLE_ON_OFF = 1;
                mOnDeviceConnectCallbacks.get(i).onInit(1);
            } else if (init.equals("2")) {

            } else if (init.equals("3")) {

            } else if (init.equals("4")) {

            } else if (init.equals("5")) {

            }
        }


    }

    @Override
    public void onMusicPlaying() throws RemoteException {

    }

    @Override
    public void onMusicStopped() throws RemoteException {

    }

    @Override
    public void onVoiceConnected() throws RemoteException {

    }

    @Override
    public void onVoiceDisconnected() throws RemoteException {

    }

    @Override
    public void onAutoConnectAccept(String autoStatus) throws RemoteException {

    }

    @Override   //獲取已連接的藍牙設備地址
    public void onCurrentAddr(String addr) throws RemoteException {
        Log.e(TAG, "onCurrentAddr:" + addr);
        BleConfig.linkDevice.address = addr;


        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onCurrentConnAddr(addr);
        }

    }

    //获取当前连接的设备名
    @Override
    public void onCurrentName(String name) throws RemoteException {
        Log.e(TAG, "onCurrentName:" + name);
        BleConfig.linkDevice.name = name;
        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onCurrentConnName(name);
        }


    }

    /*
     * 0~初始化 1~待机状态 2~连接中 3~连接成功 4~电话拨出 5~电话打入 6~通话中
     */
    @Override
    public void onHfpStatus(int status) throws RemoteException {
        Log.e(TAG, "onHfpStatus:" + status);
        BleConfig.hfpStatus = status;
    }

    @Override
    public void onAvStatus(int status) throws RemoteException {
        Log.e(TAG, "onAvStatus:" + status);
    }

    @Override
    public void onVersionDate(String version) throws RemoteException {
        Log.e(TAG, "onVersionDate:" + version);
    }

    /* 获取当前设备名 */
    @Override
    public void onCurrentDeviceName(String name) throws RemoteException {
        Log.e(TAG, "onCurrentDeviceName:" + name);

        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onLocalName(name);
        }

    }

    @Override
    public void onCurrentPinCode(String code) throws RemoteException {
        Log.e(TAG, "onCurrentPinCode:" + code);
    }

    @Override
    public void onA2dpConnected() throws RemoteException {
        Log.e(TAG, "onA2dpConnected:");
    }

    @Override
    public void onCurrentAndPairList(int index, String name, String addr) throws RemoteException {
        // Log.e(TAG, "onCurrentAndPairList:" + index + "--" + name);

        BlueToothPairedInfo info = new BlueToothPairedInfo();
        info.index = index;
        info.name = name;
        info.address = addr;


        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onCurrentAndPairList(info);
        }

    }

    @Override
    public void onA2dpDisconnected() throws RemoteException {
        Log.e(TAG, "onA2dpDisconnected");
    }

    @Override
    public void onPhoneBook(String name, String number) throws RemoteException {
        Log.e(TAG, "onPhoneBook:" + name + "--" + number);
        for (int i = 0; i < mOnContactSyncListens.size(); i++) {
            mOnContactSyncListens.get(i).onReceiveItem(name, number);
        }
    }

    @Override
    public void onSimBook(String name, String number) throws RemoteException {
        Log.e(TAG, "onSimBook:" + name + "--" + number);
    }

    @Override
    public void onPhoneBookDone() throws RemoteException {
        Log.e(TAG, "onPhoneBookDone");
        for (int i = 0; i < mOnContactSyncListens.size(); i++) {
            mOnContactSyncListens.get(i).onDone();
        }
    }

    @Override
    public void onSimDone() throws RemoteException {

    }

    @Override
    public void onCalllogDone() throws RemoteException {

    }

    @Override
    public void onCalllog(int type, String name, String number) throws RemoteException {

    }

    @Override
    public void onDiscovery(String type, String name, String addr) throws RemoteException {
        // Log.e(TAG, "onDiscovery:" + type + "--" + name + "--" + addr);

        for (int i = 0; i < mOnDeviceSearchCallbacks.size(); i++) {
            mOnDeviceSearchCallbacks.get(i).onDiscovery(type, name, addr);
        }
    }

    @Override
    public void onDiscoveryDone() throws RemoteException {
        Log.e(TAG, "onDiscoveryDone");
        for (int i = 0; i < mOnDeviceSearchCallbacks.size(); i++) {
            mOnDeviceSearchCallbacks.get(i).onDiscoveryDone();
        }
    }

    @Override
    public void onLocalAddress(String addr) throws RemoteException {
        Log.e(TAG, "onLocalAddress:" + addr);

        for (int i = 0; i < mOnDeviceConnectCallbacks.size(); i++) {
            mOnDeviceConnectCallbacks.get(i).onLocalAddress(addr);
        }

    }

    @Override
    public void onHidConnected() throws RemoteException {
        Log.e(TAG, "onHidConnected");
    }

    @Override
    public void onHidDisconnected() throws RemoteException {
        Log.e(TAG, "onHidDisconnected");
    }

    @Override
    public void onMusicInfo(String music, String artist, String album, int duration, int pos, int total) throws RemoteException {

    }

    @Override
    public void onMusicPos(int current, int total) throws RemoteException {

    }

    @Override
    public void onOutGoingOrTalkingNumber(String number) throws RemoteException {
        Log.e(TAG, "onOutGoingOrTalkingNumber:" + number);
        BleConfig.hfpStatus = 4;
        for (int i = 0; i < mOnBleCallStateListens.size(); i++) {
            mOnBleCallStateListens.get(i).onOutgoingCall(number);
        }
    }

    @Override
    public void onConnecting() throws RemoteException {

    }

    @Override
    public void onSppData(String index, String data) throws RemoteException {
        Log.e(TAG, "onSppData");
    }

    @Override
    public void onSppConnect(String index, String address) throws RemoteException {

    }

    @Override
    public void onSppDisconnect(String index, String address) throws RemoteException {

    }

    @Override
    public void onSppStatus(int status) throws RemoteException {

    }

    @Override
    public void onOppReceivedFile(String path) throws RemoteException {

    }

    @Override
    public void onOppPushSuccess() throws RemoteException {

    }

    @Override
    public void onOppPushFailed() throws RemoteException {

    }

    @Override
    public void onHidStatus(int status) throws RemoteException {

    }

    @Override
    public void onPanConnect() throws RemoteException {

    }

    @Override
    public void onPanDisconnect() throws RemoteException {

    }

    @Override
    public void onPanStatus(int status) throws RemoteException {

    }

    @Override
    public void onProfileEnbled(boolean[] enabled) throws RemoteException {

    }

    @Override
    public void onMessageInfo(String content_order, String read_status, String time, String name, String num, String title) throws RemoteException {

    }

    @Override
    public void onMessageContent(String content) throws RemoteException {

    }

    @Override
    public void onPairedState(int state) throws RemoteException {

    }

    @Override
    public void onMusicListState(String type, String index, String name) throws RemoteException {

    }

    @Override
    public void onMusicIntoList(String index) throws RemoteException {

    }

    @Override
    public void onMusicListSucess() throws RemoteException {

    }

    @Override
    public void onMusicListFail() throws RemoteException {

    }

    @Override
    public void onMusicListSettingSuccess() throws RemoteException {

    }

    @Override
    public void onMusicListSettingFail() throws RemoteException {

    }

    @Override
    public void onMusicPlaySuccess() throws RemoteException {

    }

    @Override
    public void onMusicPlayFail() throws RemoteException {

    }

    @Override
    public void onMusicCoverSuccess(String index) throws RemoteException {

    }

    @Override
    public void onMusicCoverFail() throws RemoteException {

    }

    @Override
    public void onContactIcon(String icon_path) throws RemoteException {

    }

    @Override
    public void onContactId(String icon_id) throws RemoteException {

    }

    @Override
    public void onChangeBt(String address) throws RemoteException {

    }

    @Override
    public void onIMEIInfo(String imei) throws RemoteException {
        Log.e(TAG, "onIMEIInfo:" + imei);
    }

    @Override
    public void onPlayerSoft(String name) throws RemoteException {

    }


}
