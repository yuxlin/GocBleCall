package cn.kaer.gocbluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * User: yxl
 * Date: 2020/10/24
 */
public class InComingReceiver extends BroadcastReceiver {
    public interface OnInComingListen {
        void onInComingReceive(String num);
    }

    private OnInComingListen mOnInComingListen;

    public InComingReceiver(OnInComingListen listen) {
        mOnInComingListen = listen;
    }


    public static final String ACTION_INCOMING = "cn.kaer.blecall2.incoming";

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNum = intent.getStringExtra("phoneNum");
        mOnInComingListen.onInComingReceive(phoneNum);
    }

    public static InComingReceiver register(Context context, OnInComingListen listen) {
        InComingReceiver inComingReceiver = new InComingReceiver(listen);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INCOMING);
        context.registerReceiver(inComingReceiver, intentFilter);
        return inComingReceiver;
    }

}
