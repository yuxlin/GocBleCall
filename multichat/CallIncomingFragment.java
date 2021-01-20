package cn.kaer.multichat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.util.List;

import cn.kaer.multichat.base.BaseFragment;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.helper.CallHelper;
import cn.kaer.multichat.mvp.CallContract;
import cn.kaer.multichat.mvp.CallModel;
import cn.kaer.multichat.mvp.CallPresenter;
import cn.kaer.multichat.utils.FragmentUtils;

/**
 * @author wanghx
 * @date 2020/8/11
 * @description
 */
public class CallIncomingFragment extends BaseFragment<CallPresenter, CallModel>implements CallContract.View,View.OnClickListener {
    public static Fragment newInstance() {
        Fragment fragment = new CallIncomingFragment();

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.call_incoming;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(mContext,this,mModel);
    }

    @Override
    protected void initView(View paramView) {
        paramView.findViewById(R.id.btn_reject).setOnClickListener(this);
        paramView.findViewById(R.id.btn_accept).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reject:
                Log.e("whx","111111");
                CallHelper.rejectCall(mContext);
                getActivity().finish();
                break;
            case R.id.btn_accept:
                CallHelper.acceptComingCall(mContext);
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuc(List<CallOptBean> en) {

    }

    @Override
    public void transToCallFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("addCall",false);
        FragmentUtils.replace(this,CallFragment.newInstance(bundle));
    }
}
