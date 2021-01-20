package cn.kaer.bluetooth;

import com.goodocom.gocsdk.HfpStatus;

import cn.kaer.bluetooth.entity.BlueToothInfo;

/**
 * User: yxl
 * Date: 2020/10/13
 */
public class BleConfig {
    public static int BLE_ON_OFF = 0;
    public static int hfpStatus = HfpStatus.NONE;
    public static BlueToothInfo linkDevice = new BlueToothInfo(); //当前连接的设备



}
