package cn.kaer.gocbluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * User: yxl
 * Date: 2020/12/18
 */
public class BlueToothHandler extends BroadcastReceiver {
    private OnBlueToothListen mOnBlueToothListen;
    private String TAG = getClass().getSimpleName();

    private BlueToothHandler(OnBlueToothListen onBlueToothListen) {
        mOnBlueToothListen = onBlueToothListen;
    }

    public interface OnBlueToothListen {

        void onTurnOn();

        void onBTOpen();

        void onBTClose();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
        switch (blueState) {
            case BluetoothAdapter.STATE_OFF:
                Log.i(TAG, "blueState: STATE_OFF");
                mOnBlueToothListen.onBTClose();
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                Log.i(TAG, "blueState: STATE_TURNING_ON");
                mOnBlueToothListen.onTurnOn();
                break;
            case BluetoothAdapter.STATE_ON:
                mOnBlueToothListen.onBTOpen();
                Log.i(TAG, "blueState: STATE_ON");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                Log.i(TAG, "blueState: STATE_TURNING_OFF");
                break;
            default:
                break;
        }
    }

    public static BlueToothHandler registerBroadcast(Context context, OnBlueToothListen onBlueToothListen) {
        BlueToothHandler blueToothReceiver = new BlueToothHandler(onBlueToothListen);
        context.registerReceiver(blueToothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return blueToothReceiver;
    }

    public static boolean isOpen() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public static void openBluetooth() {
        BluetoothAdapter.getDefaultAdapter().enable();
    }

    public static void closeBluetooth() {
        BluetoothAdapter.getDefaultAdapter().disable();
    }
}
