package cn.kaer.gocbluetooth.ui.tmkphone.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.utils.SystemUtils;
import cn.kaer.gocbluetooth.widget.FunctionButton;


public class OutgoingCallFragment extends BleCallFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PHONE_NUM = "phoneNum";
    private static final String ARG_NAME = "name";
    private static final String ARG_LOCATION = "location";
    private String mCallNum = "";
    private String mName = "";
    private TextView mTv_num;
    private IGocSdkController mIGocSdkController;
    private Chronometer mChronometer;
    private Handler mHandler = new Handler();


    private long startTime;
    private String mLocation;
    private TextView mTv_keyboard;

    // TODO: Rename and change types and number of parameters
    public static OutgoingCallFragment newInstance(String phoneNum, String name, String location) {
        OutgoingCallFragment fragment = new OutgoingCallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUM, phoneNum);
        args.putString(ARG_NAME, name);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_call_outgoing;
    }

    @Override
    protected void initView(View view) {
        mIGocSdkController = GocSdkController.get();
        mTv_num = findViewById(R.id.tv_num);
        findViewById(R.id.btn_end).setOnClickListener(this);
        mChronometer = findViewById(R.id.chronometer);
        mTv_keyboard = findViewById(R.id.tv_keyboard);
        mTv_keyboard.setOnClickListener(this);
        setViewEnable(mTv_keyboard,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        startTime = System.currentTimeMillis();
        mCallNum = getArguments().getString(ARG_PHONE_NUM);

        Log.e(TAG, "ble call out :" + mCallNum);
        mName = getArguments().getString(ARG_NAME);
        Log.e(TAG, "incoming num:" + mCallNum);
        mLocation = getArguments().getString(ARG_LOCATION);
        if (TextUtils.isEmpty(mName) && TextUtils.isEmpty(mLocation)) {//name location null
            mChronometer.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(mName)) { // only name null
            mChronometer.setText(mLocation);

        } else if (TextUtils.isEmpty(mLocation)) { // only location null
            mChronometer.setText(mCallNum);
        } else {  //all
            mChronometer.setText(mCallNum + "  |  " + mLocation);

        }

        mTv_num.setText(TextUtils.isEmpty(mName) ? mCallNum : mName);

    }


    @Override
    protected void initEvent() {
        GocSdkController.get().phoneDial(mCallNum);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                GocSdkController.get().switchToPhone(false);
                SystemClock.sleep(100);
                GocSdkController.get().switchToPhone(false);

            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mChronometer.stop();
    }

    @Override
    protected void onPhoneConnectSuccess(String num) {
        Log.e(TAG, "onCallConnectSuccess:" + num);
        Toast.makeText(getContext(), "电话接通成功", Toast.LENGTH_SHORT).show();
        startTime = System.currentTimeMillis();
        findViewById(R.id.tv_tip).setVisibility(View.GONE);
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime() - 1000);
        mChronometer.start();
        setViewEnable(mTv_keyboard,true);

    }

    @Override
    protected void onPhoneHookUp() {
        Log.e(TAG, "onHookUp");
        if (getActivity() != null && !getActivity().isDestroyed()) {
            getActivity().finish();
        }

        long interval = System.currentTimeMillis() - startTime;
        Log.e(TAG, "hookUp insert callLog :" + interval);
        addCallLog(mCallNum, mName, CallLog.Calls.OUTGOING_TYPE, startTime, (int) interval / 1000);
    }

    @Override
    protected void onHookStatus(boolean isHookOff) {

    }

    @Override
    public void secondDial(String num) {
        mSecondDialNum += num;
        mTv_num.setText(mSecondDialNum);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_end:
                mIGocSdkController.hookUp();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null && !getActivity().isDestroyed()) {
                            getActivity().finish();
                        }
                    }
                }, 2000);
                break;

            case R.id.tv_keyboard:
                boolean isShow = isKeyboardShowing();
                switchKeyboardShow(!isShow);
                if (mOnBleCallFragmentCallBack != null) {
                    mOnBleCallFragmentCallBack.onKeyboardVisibility(!isShow);
                }
                break;
        }
    }


    @Override
    public void onChannelTransfer(boolean isRemote) {
        if (mOnBleCallFragmentCallBack != null) {
            mOnBleCallFragmentCallBack.onChannelTransfer(isRemote);
        }
    }

    private void switchKeyboardShow(boolean isShow) {
        mTv_keyboard.setTag(isShow);
        mTv_keyboard.setText(getString(isShow ? R.string.fun : R.string.keyboard));

        if (!TextUtils.isEmpty(mSecondDialNum) && isShow) {
            mTv_num.setText(mSecondDialNum);
        } else {
            mTv_num.setText(TextUtils.isEmpty(mName) ? mCallNum : mName);
        }

    }

    private boolean isKeyboardShowing() {
        return mTv_keyboard.getTag() != null && (boolean) mTv_keyboard.getTag();
    }




}
