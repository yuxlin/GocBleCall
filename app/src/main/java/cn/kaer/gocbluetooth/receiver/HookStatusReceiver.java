package cn.kaer.gocbluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HookStatusReceiver extends BroadcastReceiver {
    public interface HookStatusListen {
        void hook_off();

        void hook_on();
    }

    private HookStatusListen mHookStatusListen;
    public static final String ACTION_HOOK_OFF = "com.android.PhoneWinowManager.HOOK_OFF";
    public static final String ACTION_HOOK_ON = "com.android.PhoneWinowManager.HOOK_ON";
    public static final String ACTION_CLOSE_ACTIVITY = "com.android.PhoneWinowManager.CLOSE_DIALER_ACTIVITY";

    private HookStatusReceiver(HookStatusListen listen) {
        mHookStatusListen = listen;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_HOOK_OFF:

                mHookStatusListen.hook_off();
                break;
            case ACTION_HOOK_ON:
         //   case ACTION_CLOSE_ACTIVITY:
                mHookStatusListen.hook_on();
                break;
        }
    }

    public static HookStatusReceiver registerReceiver(Context context, HookStatusListen listen) {
        HookStatusReceiver hookStatusReceiver = new HookStatusReceiver(listen);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(ACTION_HOOK_OFF);
        intentFilter2.addAction(ACTION_HOOK_ON);
        intentFilter2.addAction(ACTION_CLOSE_ACTIVITY);
        context.registerReceiver(hookStatusReceiver, intentFilter2);
        return hookStatusReceiver;
    }

}
