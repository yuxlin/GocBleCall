package cn.kaer.gocbluetooth.ui.tmkphone.fragment;

import android.annotation.SuppressLint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.opengl.Visibility;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.provider.CallLog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.utils.SystemUtils;
import cn.kaer.gocbluetooth.widget.FunctionButton;


public class InComingCallFragment extends BleCallFragment implements View.OnClickListener {

    private static final String ARG_PHONE_NUM = "phoneNum";
    private static final String ARG_NAME = "name";
    private static final String ARG_LOCATION = "location";
    private String mCallNum = "";
    private String mName = "";
    private TextView mTv_num;
    private TextView mTv_position;
    private IGocSdkController mIGocSdkController;
    private Chronometer mChronometer;
    private Ringtone mRingtone;
    private TextView mTv_keyboard;
    private View mFunBtParent;
    private long startTime;
    private Handler mHandler = new Handler();
    private String mLocation;

    public static InComingCallFragment newInstance(String num, String name, String location) {
        InComingCallFragment fragment = new InComingCallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUM, num);
        args.putString(ARG_NAME, name);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_call_incoming;
    }

    @Override
    protected void initView(View view) {
        mIGocSdkController = GocSdkController.get();

        findViewById(R.id.btn_accept).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);

        mTv_position = findViewById(R.id.tv_position);
        mTv_num = findViewById(R.id.tv_num);
        mChronometer = findViewById(R.id.chronometer);
        mTv_position.setVisibility(View.GONE);
        mIGocSdkController.setMuteState(false);
        mTv_keyboard = findViewById(R.id.tv_keyboard);
        mTv_keyboard.setOnClickListener(this);

        setViewEnable(mTv_keyboard, false);

        mHandler.removeCallbacks(mRunnable_detection);
        mHandler.postDelayed(mRunnable_detection, 1000 * 10);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        startTime = 0;
        mCallNum = getArguments().getString(ARG_PHONE_NUM);
        mName = getArguments().getString(ARG_NAME);
        mLocation = getArguments().getString(ARG_LOCATION);
        Log.e(TAG, "incoming num:" + mCallNum);
        mTv_num.setText(TextUtils.isEmpty(mName) ? mCallNum : mName);

        if (TextUtils.isEmpty(mName) && TextUtils.isEmpty(mLocation)) {//name location null
            mChronometer.setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(mName)) { // only name null
            mChronometer.setText(mLocation);

        } else if (TextUtils.isEmpty(mLocation)) { // only location null
            mChronometer.setText(mCallNum);
        } else {  //all
            mChronometer.setText(mCallNum + "  |  " + mLocation);
        }

        if (mOnBleCallFragmentCallBack != null) {
            mOnBleCallFragmentCallBack.onFunButtonVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        mRingtone = RingtoneManager.getRingtone(requireContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        mRingtone.play();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                Log.e(TAG, "接听");
                mIGocSdkController.phoneAnswer();
                answerPhone();
                break;
            case R.id.btn_reject:
                Log.e(TAG, "拒接");
                mRingtone.stop();
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
    protected void onPhoneConnectSuccess(String num) {
        Log.e(TAG, "incoming answer success");
        startTime = System.currentTimeMillis();
        findViewById(R.id.tv_tip).setVisibility(View.INVISIBLE);
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        answerPhone();

        setViewEnable(mTv_keyboard, true);
    }

    @Override
    protected void onPhoneHookUp() {
        Log.e(TAG, "onHookUp");
        if (getActivity() != null && !getActivity().isDestroyed()) {
            getActivity().finish();
        }

        long interval = System.currentTimeMillis() - startTime;
        Log.e(TAG, "hookUp insert callLog :" + interval);

        if (startTime == 0) {
            addCallLog(mCallNum, mName, CallLog.Calls.MISSED_TYPE, System.currentTimeMillis(), 0);
        } else {
            addCallLog(mCallNum, mName, CallLog.Calls.INCOMING_TYPE, startTime, (int) interval / 1000);
        }

    }

    @Override
    protected void onHookStatus(boolean isHookOff) {
        if (isHookOff && mIGocSdkController.getHfpStatus() < 6) {
            Log.e(TAG, "摘机接听");
            mIGocSdkController.phoneAnswer();
        }


    }

    @Override
    public void secondDial(String num) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mRunnable_detection);
        mRingtone.stop();
    }

    private void answerPhone() {
        mRingtone.stop();
        if (mOnBleCallFragmentCallBack != null) {
            mOnBleCallFragmentCallBack.onFunButtonVisibility(View.VISIBLE);
        }
        findViewById(R.id.btn_accept).setVisibility(View.GONE);
        mTv_keyboard.setVisibility(View.VISIBLE);
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


    private Runnable mRunnable_detection = new Runnable() {
        @Override
        public void run() {
            try {

                if (mIGocSdkController.getHfpStatus() < 3 || !mIGocSdkController.isBleOpen()) {
                    requireActivity().finish();
                }
            } catch (Exception ignore) {
            }
        }
    };

}
