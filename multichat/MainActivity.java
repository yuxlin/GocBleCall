package cn.kaer.multichat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import cn.kaer.multichat.constant.CallActConst;
import cn.kaer.multichat.enums.CallState;
import cn.kaer.multichat.helper.CallHelper;
import cn.kaer.multichat.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BroadcastReceiver receiver;
    private static String label = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerCallReciver();
    }

    private void registerCallReciver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actStr = intent.getAction();
                if (actStr.equals(CallActConst.ACTION_PHONE_STATE)){
                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    Log.e(TAG,"getExtras "+intent.getExtras().toString());
                    if (!TextUtils.isEmpty(state) &&
                            !label.equals(state)) {
                        label = state;
                        if (state.equals("RINGING")) {
                            Fragment fragment = FragmentUtils.findFragment(getSupportFragmentManager(),CallIncomingFragment.class);
                            if (fragment == null){
                                Intent startIntent = new Intent(MainActivity.this, CallingActivity.class);
                                startIntent.putExtra("callState",CallState.INCOMING.toInt());
                                startActivity(startIntent);
                                finish();
                            }
                        } else if (state.equals("OFFHOOK")) {
                                Intent startIntent = new Intent(MainActivity.this, CallingActivity.class);
                                startIntent.putExtra("callState",CallState.OUTGOING.toInt());
                                startActivity(startIntent);
                                finish();
                        } else if (state.equals("IDLE")) {
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(CallActConst.ACTION_PHONE_STATE);
        registerReceiver(receiver,filter);
    }

    public void transToCalling(View v){
        startActivity(new Intent(MainActivity.this, CallingActivity.class));
    }
    public void dial(View v){
        CallHelper.starCall(MainActivity.this,"18162018352",0);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
