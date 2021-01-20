package cn.kaer.multichat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import cn.kaer.multichat.adapter.CallOptAdapter;
import cn.kaer.multichat.base.BaseFragment;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallState;
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
public class CallOutgoingFragment extends BaseFragment<CallPresenter, CallModel>
        implements CallContract.View, View.OnClickListener {
    private static final String TAG = CallOutgoingFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<CallOptBean>list = new ArrayList<>();
    private CallOptAdapter adapter;
    private boolean isAddCall;
    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new CallOutgoingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.call_outgoing;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(mContext,this,mModel);
    }

    @Override
    protected void initView(View paramView) {
        isAddCall = getArguments().getBoolean("addCall",false);
        if (isAddCall){
            mPresenter.showHideHoldFm(true);
        }else{

        }
        recyclerView = paramView.findViewById(R.id.recycler);
        paramView.findViewById(R.id.btn_end).setOnClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        adapter = new CallOptAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        mPresenter.getData(CallState.OUTGOING,isAddCall);
        adapter.setOnItemClickListener(new CallOptAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(int position, CheckBox checkBox) {
                handleTag((Integer) checkBox.getTag(), checkBox.isChecked());
            }
        });
    }


    @Override
    public void handleTag(int tag, boolean checked) {

    }

    @Override
    public void getDataSuc(List<CallOptBean> en) {
        if (en!=null&&en.size()>0){
            list.addAll(en);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void transToCallFragment() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("addCall",isAddCall);
        FragmentUtils.replace(CallOutgoingFragment.this,CallFragment.newInstance(bundle));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_end:
                CallHelper.endCall(mContext);
                break;
            default:
                break;
        }
    }
}
