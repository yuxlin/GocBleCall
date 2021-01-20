package cn.kaer.bluetooth.callback;

import cn.kaer.bluetooth.entity.BlueToothPairedInfo;

/**
 * User: yxl
 * Date: 2020/8/19
 */
public interface OnGocsdkCallback {

    interface OnDeviceSearchCallback { // 设备搜索相关
        void onDiscovery(String type, String name, String addr); //搜索到新设备

        void onDiscoveryDone();  //搜索完成
    }

    interface OnDeviceConnectCallback {
        void onLocalAddress(String addr); //本地地址

        void onLocalName(String name); //本地name

        void onInit(int state); // 0关闭 1开启

        void onCurrentAndPairList(BlueToothPairedInfo blueToothPairedInfo);//当前已配对列表

        void onCurrentConnAddr(String addr); //当前连接地址

        void onCurrentConnName(String name);

        void onDeviceConnected(String addr); //设备连接成功

        void onDeviceDisconnected(String address); // 设备断开
    }


    interface OnBleCallStateListen {

        void onOutgoingCall(String num);

        void onInComingCall(String num);

        void onCallConnectSuccess(String num); //电话接通

        void onChannelTransfer(boolean isRemote);

        void onHookUp(); //挂断
    }

    interface OnContactSyncListen {

        void onSyncStart();

        void onReceiveItem(String name, String num);

        void onDone();
    }

}
