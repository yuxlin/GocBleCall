package cn.kaer.bluetooth.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import android.os.RemoteException;
import android.util.Log;

import com.goodocom.gocsdk.IGocsdkCallback;
import com.goodocom.gocsdk.Config;
import com.goodocom.gocsdk.IGocsdkService;
import com.goodocom.gocsdk.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;
import cn.kaer.bluetooth.R;
import cn.kaer.bluetooth.callback.GocsdkCallbackImp;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.command.CommandSend;
import cn.kaer.bluetooth.v6_0.CommandParser_6;
import cn.kaer.bluetooth.v6_0.GocsdkServiceImp_6;


public class GocsdkService extends Service {
    public static final String TAG = "GocsdkService";
    public static final int MSG_START_SERIAL = 1;//串口
    public static final int MSG_SERIAL_RECEIVED = 2; //接收到串口信息
    private static final int RESTART_DELAY = 2000; // ms
    private CommandParser_6 parser;
    private final boolean use_socket = false;
    private SerialThread serialThread = null;
    private volatile boolean running = true;

    private GocsdkServiceImp_6 mGocsdkServiceImp;

    public class GocsdkBinder extends Binder {

        public void setOnDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceCallback, boolean isRegister) {
            parser.setOnDeviceSearchCallback(onDeviceCallback, isRegister);
        }

        public IGocsdkService getIGocsdkService() {
            return mGocsdkServiceImp;
        }

        public void setonDeviceConnectCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback, boolean isRegister) {
            parser.setonDeviceConnectCallback(onDeviceConnCallback, isRegister);
        }

        public void setOnCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onBleCallStateListen, boolean isRegister) {
            parser.setOnCallStateCallback(onBleCallStateListen, isRegister);
        }

        public void setOnContactSyncListen(OnGocsdkCallback.OnContactSyncListen onContactSyncListen, boolean isRegister) {
            parser.setOnContactSyncListen(onContactSyncListen,isRegister);
        }
    }

    @Override
    public void onCreate() {
        Log.e("app_boot", "Service onCreate");
//		callbacks = new RemoteCallbackList<IGocsdkCallback>();
        parser = new CommandParser_6(/*callbacks,*/this);
        //CommandParser.getInstance(callbacks, this);
        mGocsdkServiceImp = new GocsdkServiceImp_6(this);
        handler.sendEmptyMessage(MSG_START_SERIAL);
        hand = handler;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("app_boot", "Service onStartCommand");
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence name = getString(R.string.app_name);
        String description = getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(description, name, importance);
        mNotificationManager.createNotificationChannel(mChannel);
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(R.drawable.ic_launcher)
                .setChannelId(description)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        running = false;
//		callbacks.kill();

        Log.d("app_boot", "Service onDestroy");
//		unregisterReceiver(receiver);

        super.onDestroy();
    }

    private static Handler hand = null;

    public static Handler getHandler() {
        return hand;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG_START_SERIAL) {
                Log.d("app", "serialThread start!");
                serialThread = new SerialThread();
                serialThread.start();
            } else if (msg.what == MSG_SERIAL_RECEIVED) {
                byte[] data = (byte[]) msg.obj;
                parser.onBytes(data);
            }
        }

        ;
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("app_boot", "onBind");
        write(CommandSend.INQUIRY_HFP_STATUS);
        return new GocsdkBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("app_boot", "onUnbind");
        return super.onUnbind(intent);
    }

    class SerialThread extends Thread {
        private InputStream inputStream;
        private OutputStream outputStream = null;
        private byte[] buffer = new byte[1024];

        public void write(byte[] buf) {
            if (outputStream != null) {
                try {

                    outputStream.write(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public SerialThread() {
        }

        @Override
        public void run() {
            LocalSocket client = null;
            SerialPort serial = null;

            int n;
            try {
                if (use_socket) {
                    Log.d("app", "use socket!");
                    client = new LocalSocket();
                    client.connect(new LocalSocketAddress(Config.SERIAL_SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED));
                    inputStream = client.getInputStream();
                    outputStream = client.getOutputStream();
                } else {
                    Log.d("app", "use serial!");
                    serial = new SerialPort(new File("/dev/goc_serial"), 115200, 0);
                    if (serial != null) {
                        Log.d("app", "serial not is null!");
                    } else {
                        Log.d("app", "serial is null!");
                    }
                    inputStream = serial.getInputStream();
                    outputStream = serial.getOutputStream();
                }
                while (running) {
                    n = inputStream.read(buffer);
                    if (n < 0) {
                        if (use_socket) {
                            if (client != null) client.close();
                        } else {
                            if (serial != null) serial.close();
                        }
                        throw new IOException("n==-1");
                    }

                    byte[] data = new byte[n];
                    System.arraycopy(buffer, 0, data, 0, n);
                    handler.sendMessage(handler.obtainMessage(
                            MSG_SERIAL_RECEIVED, data));
                }
            } catch (IOException e) {
                try {
                    if (use_socket) {
                        if (client != null) client.close();
                    } else {
                        if (serial != null) serial.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                handler.sendEmptyMessageDelayed(MSG_START_SERIAL, RESTART_DELAY);
                return;
            }

            try {
                if (use_socket) {
                    if (client != null) client.close();
                } else {
                    if (serial != null) serial.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String str) {
        Log.d("serial", "write:" + str + "    serialThread: " + serialThread);
        if (serialThread == null) return;

        serialThread.write((CommandSend.COMMAND_HEAD + str + "\r\n").getBytes());
    }

    public void registerCallback(IGocsdkCallback callback) {
        Log.d(TAG, "registerCallback");
 /*       try {
            mGocsdkServiceImp.registerCallback(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

    }

    public void unregisterCallback(IGocsdkCallback callback) {
/*
        try {
            mGocsdkServiceImp.unregisterCallback(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
*/

    }

}
