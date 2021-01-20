package cn.kaer.gocbluetooth.ui.tmkphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.common.bases.BaseActivity;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.data.ContactDao;
import cn.kaer.gocbluetooth.data.PhoneAddrUtil;
import cn.kaer.gocbluetooth.entity.ContactInfo;
import cn.kaer.gocbluetooth.receiver.HookStatusReceiver;
import cn.kaer.gocbluetooth.service.GocBleService;
import cn.kaer.gocbluetooth.ui.tmkphone.fragment.BleCallFragment;
import cn.kaer.gocbluetooth.ui.tmkphone.fragment.InComingCallFragment;
import cn.kaer.gocbluetooth.ui.tmkphone.fragment.OutgoingCallFragment;
import cn.kaer.gocbluetooth.utils.CallstateUtil;
import cn.kaer.gocbluetooth.utils.PSTNConf;
import cn.kaer.gocbluetooth.utils.SystemUtils;
import cn.kaer.gocbluetooth.utils.WakeLockHelper;
import cn.kaer.gocbluetooth.widget.FunctionButton;

public class BleCallActivity extends BaseActivity implements FunctionButton.OnFunctionStateChange, HookStatusReceiver.HookStatusListen, View.OnClickListener, View.OnTouchListener {

    public static final int CALL_TYPE_INCOMING = 1;
    public static final int CALL_TYPE_OUTGOING = 2;

    private String mPhoneNum = "";
    private String mName = "";
    private int mCallType;
    private View mTv_mic;
    private View mTv_channel;
    private String mLocation;
    private FunctionButton mFb_channel, mFb_switch, mFb_mic;
    private BleCallFragment mBleCallFragment;
    private Handler mHandler = new Handler();
    private HookStatusReceiver mHookStatusReceiver;
    private TextView mTv_keyBoard;
    private View mKeyBoardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      Window window = getWindow();
        window.setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.e(TAG, "onCreate 123");*/
        WakeLockHelper.getInstance().init(this);
        WakeLockHelper.getInstance().wakeupScreen();

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
    }

    @Override
    protected void init() {
        if (CallstateUtil.isInPSTNCall(this) || CallstateUtil.isInWirelessCall(this) || CallstateUtil.isInBluetoothCall(this)) {
            finish();
            return;
        }

        if (getIntent().hasExtra("phoneNum")) {
            mPhoneNum = getIntent().getStringExtra("phoneNum");
            mName = ContactDao.getContactNameByNumb(this, mPhoneNum);
            if (TextUtils.isEmpty(mName)) {
                mName = ContactDao.getEnterpriseContactName(this, mPhoneNum);
                if (TextUtils.isEmpty(mName) && GocBleService.getInstance() != null) {
                    ContactInfo contactInfo = GocBleService.getInstance().queryBluetoothContact(mPhoneNum);
                    mName = (contactInfo == null ? "" : contactInfo.getName());
                }
            }

            mLocation = PhoneAddrUtil.getGeo(mPhoneNum, 86);
            if ("10000000".equals(mPhoneNum)) {
                mName = "媒体通话";
            }
            Log.e(TAG, "归属地--" + mLocation);
        }
        if (getIntent().hasExtra("callType")) {
            mCallType = getIntent().getIntExtra("callType", 0);
        }

        CallstateUtil.updatePSTNCallState(this, PSTNConf.CALLSTATE_INCALL);

        Log.e(TAG, "onCreate:" + mPhoneNum);
    }

    @Override
    public void initView() {
/*
        if (true){
            InComingCallFragment inComingCallFragment = InComingCallFragment.newInstance(mPhoneNum);

            replaceFragment(R.id.fl_container, inComingCallFragment);
            return;
        }*/


        if (CALL_TYPE_INCOMING == mCallType) {
            InComingCallFragment inComingCallFragment = InComingCallFragment.newInstance(mPhoneNum, mName, mLocation);
            mBleCallFragment = inComingCallFragment;
            replaceFragment(R.id.fl_container, inComingCallFragment);
        } else if (CALL_TYPE_OUTGOING == mCallType) {
            OutgoingCallFragment outgoingCallFragment = OutgoingCallFragment.newInstance(mPhoneNum, mName, mLocation);
            mBleCallFragment = outgoingCallFragment;
            replaceFragment(R.id.fl_container, outgoingCallFragment);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BleCallActivity.this, "呼叫类型参数异常！", Toast.LENGTH_SHORT).show();

                    finish();

                }
            }, 1000);
        }

        Log.e(TAG, "initView");
        mFb_mic = findViewById(R.id.fb_mic);
        mFb_channel = findViewById(R.id.fb_channel);
        mFb_mic.setOnCheckListen(this);
        mFb_switch = findViewById(R.id.fb_switch);
        mFb_channel.setOnCheckListen(this);
        mFb_switch.setOnCheckListen(this);

        initKeyboard();

  /*      mTv_keyBoard = findViewById(R.id.tv_keyboard);
        mTv_keyBoard.setOnClickListener(this);*/
    }

    private void initKeyboard() {
        mKeyBoardView = findViewById(R.id.keyboardView);
        for (int i = 0; i <= 11; i++) {
            View viewWithTag = mKeyBoardView.findViewWithTag("ib_" + i);
            viewWithTag.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Object tag = view.getTag();
            if (tag instanceof String) {
                try {
                    String keyCodeString = (String) tag;
                    int index = keyCodeString.indexOf("_");
                    keyCodeString = keyCodeString.substring(index + 1);
                    //   int keyCode = Integer.parseInt(keyCodeString);
                    Log.e(TAG, "input:" + keyCodeString); //10=*  11=#
                    mBleCallFragment.secondDial(keyCodeString);

                    GocSdkController.get().secondDial(keyCodeString);
                } catch (Exception ignored) {
                }

            }
        }
        return false;
    }

    @Override
    public void initData() {
        boolean hookOff = SystemUtils.isHookOff(this);
        mFb_channel.setCheck(!hookOff);

        GocSdkController.get().setMuteState(false);
        SystemUtils.setSoundChannel(this, hookOff);


        GocSdkController.get().loadAllStatus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GocSdkController.get().getHfpStatus() < 3) {
                    Toast.makeText(BleCallActivity.this, "蓝牙未连接设备", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 5000);

        mHookStatusReceiver = HookStatusReceiver.registerReceiver(this, this);

        if (mBleCallFragment != null) {
            mBleCallFragment.setOnCallFragmentCallback(new BleCallFragment.OnBleCallFragmentCallBack() {
                @Override
                public void onChannelTransfer(boolean isRemote) {
                    mFb_switch.setCheck(!isRemote);
                }

                @Override
                public void onFunButtonVisibility(int visibility) {
                    findViewById(R.id.functionButtonParent).setVisibility(visibility);
                }

                @Override
                public void onKeyboardVisibility(boolean isShow) {
                    mKeyBoardView.setVisibility(isShow ? View.VISIBLE : View.GONE);
                    mKeyBoardView.startAnimation(AnimationUtils.loadAnimation(BleCallActivity.this, isShow ? R.anim.left_in : R.anim.left_out));
                }


            });
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_calling;
    }

    @Override
    public int getToolBarId() {
        return 0;
    }

    public static void startBleCallActivity(Context context, String callNum, int callType) {
        Intent intent = new Intent(context, BleCallActivity.class)
                .putExtra("phoneNum", callNum)
                .putExtra("callType", callType);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause");
        mHandler.removeCallbacks(mRunnable_weakUp);
        WakeLockHelper.getInstance().releaseLock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "==onResume");
        mHandler.removeCallbacks(mRunnable_weakUp);
        mHandler.postDelayed(mRunnable_weakUp, 1000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallstateUtil.updatePSTNCallState(this, PSTNConf.CALLSTATE_IDLE);
        unregisterReceiver(mHookStatusReceiver);


    }


    @Override
    public void onChecked(int viewId, boolean isChecked) {
        switch (viewId) {
            case R.id.fb_mic:
                GocSdkController.get().setMuteState(!isChecked);
                break;
            case R.id.fb_channel:
                SystemUtils.setSoundChannel(this, !isChecked);
                break;
            case R.id.fb_switch:
                GocSdkController.get().switchToPhone(isChecked);
                break;
        }
    }

    @Override
    public void hook_off() {
        mFb_channel.setCheck(false);
    }

    @Override
    public void hook_on() {
        mFb_channel.setCheck(true);
    }


    private Runnable mRunnable_weakUp = () -> {
        //   WakeLockHelper.getInstance().wakeupScreen();
        WakeLockHelper.getInstance().keepScreenOn(1000 * 60 * 60 * 2);
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //     case R.id.tv_keyboard:
            // break;
        }
    }


}
