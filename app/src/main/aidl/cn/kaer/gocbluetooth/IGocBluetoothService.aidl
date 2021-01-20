// IGocBluetoothService.aidl
package cn.kaer.gocbluetooth;
import cn.kaer.gocbluetooth.IGoBluetoothCallback;
// Declare any non-default types here with import statements

interface IGocBluetoothService {

    boolean isConnectedDevice();

    void phoneDial(String num);

    void setOnBleServiceCallback(IGoBluetoothCallback callback);


}
