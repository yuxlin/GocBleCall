// IGocBluetoothService.aidl
package cn.kaer.gocbluetooth;

// Declare any non-default types here with import statements

interface IGoBluetoothCallback {

    void onBleStateChange(boolean isOpen);

    void onDeviceConnected(String addr);

    void onDeviceDisconnect();

    void onContactSync(String data);
}
