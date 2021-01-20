package cn.kaer.bluetooth;

import android.content.Context;

import com.goodocom.gocsdk.IGocsdkService;

import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.entity.BlueToothInfo;

/**
 * User: yxl
 * Date: 2020/10/7
 */
public interface IGocSdkController {

    IGocSdkController init(Context context);

    void unInit();

    IGocsdkService getGocSdkService();

    /*设备搜索监听*/

    void registerDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceCallback);

    void unRegisterDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceCallback);

    /*设备连接监听*/
    void registerDeviceConnCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback);

    void unRegisterDeviceConnCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback);

    /*蓝牙通话状态监听*/
    void registerCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onCallStateCallback);

    void unRegisterCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onCallStateCallback);

    /*联系人同步状态监听*/
    void registerContactSyncCallback(OnGocsdkCallback.OnContactSyncListen onContactSyncListen);

    void unRegisterContactSyncCallback(OnGocsdkCallback.OnContactSyncListen onContactSyncListen);

    void startSearchDevice();

    void stopSearchDevice();

    /*获取连接状态   0~初始化 1~待机状态 2~连接中 3~连接成功 4~电话拨出 5~电话打入 6~通话中*/



    int getHfpStatus();

    BlueToothInfo getCurrentConnDevice();

    void connectDevice(String addr);

    void disConnectDevice();

    void deletePaired(String addr);

    void loadAllStatus();

    void openBlueTooth();

    void closeBlueTooth();

    boolean isBleOpen(); //是否开启状态

    /*通话相关*/
    void phoneDial(String callNum);

    void phoneAnswer();

    void hookUp();

    void setMuteState(boolean isCheckedMic);

    void secondDial(String keyCodeString);

    boolean isCalling();

    void switchToPhone(boolean isPhone);

    void rename(String name); //修改本地蓝牙名

    void syncContacts();


}
