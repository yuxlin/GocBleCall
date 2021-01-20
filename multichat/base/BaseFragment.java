package cn.kaer.multichat.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.kaer.multichat.enums.CallOptType;
import cn.kaer.multichat.helper.CallHelper;
import cn.kaer.multichat.utils.TUtil;

/**
 * @describe：TODO</br> Created by wanghuixiang on 2018/12/27.
 */

public abstract class BaseFragment<T extends BasePresenter, E extends BaseModelCreate> extends Fragment
        implements BaseView {
    protected T mPresenter;
    protected E mModel;
    protected Context mContext;
    protected View fragmentRootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = TUtil.getT(this, 0);
        this.mModel = TUtil.getT(this, 1);
        initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == this.fragmentRootView) {
            this.fragmentRootView = inflater.inflate(getContentViewId(), container, false);
        }
        initView(this.fragmentRootView);
        return fragmentRootView;
    }

    protected abstract int getContentViewId();

    protected abstract void initPresenter();

    protected abstract void initView(View paramView);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mPresenter != null) {
            this.mPresenter.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
        }
        ViewGroup parent = (ViewGroup) this.fragmentRootView.getParent();
        if (null != parent) {
            parent.removeView(this.fragmentRootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            this.mPresenter.onResume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.mPresenter != null) {
            this.mPresenter.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.mPresenter != null) {
            this.mPresenter.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mPresenter != null) {
            this.mPresenter.onPause();
        }
    }
    protected void showToast(String msg){
//        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    public abstract void transToCallFragment();

    public void handleTag(int tag, boolean checked){
        if (tag == CallOptType.MUTE.toInt()){
            Log.e("whx","mute call");
            CallHelper.silentIncallVoice(mContext,checked);
        }else if (tag == CallOptType.SPEAKER_ON.toInt()){
            Log.e("whx","speaker call");
            CallHelper.audioModeSwitch(mContext,checked);
        }else if (tag == CallOptType.RECORD.toInt()){
            Log.e("whx","record call");
        }else if (tag == CallOptType.HOLD_ON.toInt()){
            Log.e("whx","hold on call");
        }
    }
}
