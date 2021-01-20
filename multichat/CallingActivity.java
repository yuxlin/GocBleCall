package cn.kaer.multichat;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import cn.kaer.multichat.base.BaseActivity;
import cn.kaer.multichat.base.BaseFragment;
import cn.kaer.multichat.constant.CallActConst;
import cn.kaer.multichat.enums.CallState;
import cn.kaer.multichat.helper.CallHelper;
import cn.kaer.multichat.utils.FragmentUtils;
import cn.kaer.multichat.utils.rx.RxManage;
import io.reactivex.functions.Consumer;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallingActivity extends BaseActivity {
    private static final String TAG = "CallingActivity";
    private RxManage rxManage = new RxManage();
    private LinearLayout holdFm;
    private BroadcastReceiver receiver;
    private static String label = "";
    private Handler handler = new Handler();
    @Override
    protected void initPresenter() {
    }
    @Override
    protected void initView() {
        holdFm = findViewById(R.id.fl_hold_on);
        int callState = getIntent().getIntExtra("callState", CallState.INCOMING.toInt());
        if (callState == CallState.INCOMING.toInt()){
            FragmentUtils.add(getSupportFragmentManager(),CallIncomingFragment.newInstance(),R.id.fl_container);
        }else if (callState == CallState.OUTGOING.toInt()){
            Bundle bundle = new Bundle();
            bundle.putBoolean("addCall",false);
            FragmentUtils.add(getSupportFragmentManager(),CallOutgoingFragment.newInstance(bundle),R.id.fl_container);
        }
        rxManage.on("showHideHoldFm", new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof Boolean){
                    boolean isShow = (boolean) o;
                    if (isShow){
                        holdFm.setVisibility(View.VISIBLE);
                    }else{
                        holdFm.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        registerCallReciver();
    }
    private void registerCallReciver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actStr = intent.getAction();
                Log.e(TAG,"action = "+actStr);
                if (actStr.equals(CallActConst.ACTION_PHONE_STATE)){
                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (!TextUtils.isEmpty(state) &&
                            !label.equals(state)) {
                        label = state;
                        Log.e(TAG,"state = "+state);
                        if (state.equals("RINGING")) {
                            String num = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            Fragment fragment = FragmentUtils.findFragment(getSupportFragmentManager(),CallFragment.class);
                            if (fragment!=null&&fragment instanceof CallFragment){
                                ((CallFragment)fragment).showIncomingDialog(num);
                            }

                        } else if (state.equals("OFFHOOK")) {
                        } else if (state.equals("IDLE")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },200);

                        }
                    }
                }else if (actStr.equals(CallActConst.ACTION_WIRELESS_ACTIVE)){
                    String state = "wireless";
                    if (!TextUtils.isEmpty(state) &&
                            !label.equals(state)) {
                        label = state;
                        BaseFragment fragment = (BaseFragment) FragmentUtils.findFragment(getSupportFragmentManager(),CallOutgoingFragment.class);
                        if (fragment!=null){
                            fragment.transToCallFragment();
                        }
                        BaseFragment fragment1 = (BaseFragment) FragmentUtils.findFragment(getSupportFragmentManager(),CallIncomingFragment.class);
                        if (fragment1!=null){
                            fragment1.transToCallFragment();
                        }
                        BaseFragment fragment2 = (BaseFragment) FragmentUtils.findFragment(getSupportFragmentManager(),CallFragment.class);
                        if (fragment2!=null){
                            holdFm.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(CallActConst.ACTION_PHONE_STATE);
        filter.addAction(CallActConst.ACTION_MEET_SUPPORT);
        filter.addAction(CallActConst.ACTION_MEET_SUPPORT_NOT);
        filter.addAction(CallActConst.ACTION_WIRELESS_ACTIVE);
        registerReceiver(receiver,filter);
    }

    @Override
    protected int getContantViewId() {
        return R.layout.activity_calling;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
