package cn.kaer.gocbluetooth.ui.tmkphone.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.utils.CalllogUtil;
import cn.kaer.common.bases.BaseFragment;
import cn.kaer.gocbluetooth.receiver.HookStatusReceiver;
import cn.kaer.gocbluetooth.utils.SystemUtils;

/**
 * User: yxl
 * Date: 2020/10/26
 */
public abstract class BleCallFragment extends BaseFragment implements OnGocsdkCallback.OnBleCallStateListen {
    private HookStatusReceiver mHookStatusReceiver;
    private HookStatusReceiver.HookStatusListen mHookStatusListen;
    protected OnBleCallFragmentCallBack mOnBleCallFragmentCallBack;
    public String mSecondDialNum = "";

    public interface OnBleCallFragmentCallBack {
        void onChannelTransfer(boolean isRemote);

        void onFunButtonVisibility(int visibility);

        void onKeyboardVisibility(boolean isShow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        initCallListen();
        initReceiver();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initCallListen() {
        GocSdkController.get().registerCallStateCallback(this);
    }

    @Override
    public void onOutgoingCall(String num) {

    }

    @Override
    public void onInComingCall(String num) {

    }

    @Override
    public void onCallConnectSuccess(String num) {

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                GocSdkController.get().switchToPhone(false);
                SystemClock.sleep(100);
                GocSdkController.get().switchToPhone(false);
                SystemClock.sleep(100);
                GocSdkController.get().switchToPhone(false);
            }
        }).start();*/
        onPhoneConnectSuccess(num);
    }

    @Override
    public void onHookUp() {
        Log.e("bleCallFragment", "onHookUp");
        onPhoneHookUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireContext().unregisterReceiver(mHookStatusReceiver);
        GocSdkController.get().unRegisterCallStateCallback(this);
    }

    private void initReceiver() {
        mHookStatusReceiver = HookStatusReceiver.registerReceiver(requireContext(), new HookStatusReceiver.HookStatusListen() {
            @Override
            public void hook_off() {
                Log.e(TAG, "rec hook off");
                SystemUtils.setSoundChannel(requireContext(), true);
                onHookStatus(true);

                if (mHookStatusListen != null)
                    mHookStatusListen.hook_off();
            }

            @Override
            public void hook_on() {

                Log.e(TAG, "rec hook on");
                if (!SystemUtils.isOuterChannel(requireContext())) {
                    GocSdkController.get().hookUp();
                }

                if (mHookStatusListen != null)
                    mHookStatusListen.hook_on();

                onHookStatus(false);
            }
        });
    }

    protected abstract void onPhoneConnectSuccess(String num);

    protected abstract void onPhoneHookUp();

    protected abstract void onHookStatus(boolean isHookOff); //手柄摘机

    public abstract void secondDial(String num);

    public void setOnCallFragmentCallback(OnBleCallFragmentCallBack onCallFragmentCallback) {
        mOnBleCallFragmentCallBack = onCallFragmentCallback;

    }

    public void setHookStatusListen(HookStatusReceiver.HookStatusListen listen) {
        mHookStatusListen = listen;
    }

    public Uri addCallLog(String num, String name, int type, long startTime, int callInterval) {
        Log.e(TAG, "addCallLog:" + num + "--" + name + "--" + type + "--" + callInterval);
        return CalllogUtil.addCallLog(requireContext(), num, name, type, startTime, callInterval);
    }
    protected void setViewEnable(View view, boolean isEnable) {
        if (view == null) {
            return;
        }
        view.setAlpha(isEnable ? 1f : 0.5f);
        view.setEnabled(isEnable);
    }
}
