package cn.kaer.multichat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import cn.kaer.multichat.adapter.CallOptAdapter;
import cn.kaer.multichat.base.BaseFragment;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallOptType;
import cn.kaer.multichat.enums.CallState;
import cn.kaer.multichat.mvp.CallContract;
import cn.kaer.multichat.mvp.CallModel;
import cn.kaer.multichat.mvp.CallPresenter;
import cn.kaer.multichat.utils.FragmentUtils;

/**
 * @author wanghx
 * @date 2020/8/13
 * @description
 */
public class ConferenceFragment extends BaseFragment<CallPresenter, CallModel>implements CallContract.View {
    private RecyclerView recyclerView;
    private List<CallOptBean> list = new ArrayList<>();
    private CallOptAdapter adapter;
    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new ConferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.call_conference;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(mContext,this,mModel);
    }

    @Override
    protected void initView(View paramView) {
        boolean isAddCall = getArguments().getBoolean("addCall",false);
        recyclerView = paramView.findViewById(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        adapter = new CallOptAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        mPresenter.getData(CallState.CONFERENCE,isAddCall);
        adapter.setOnItemClickListener(new CallOptAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(int position, CheckBox checkBox) {
                handleTag((Integer) checkBox.getTag(), checkBox.isChecked());
            }
        });
    }

    @Override
    public void transToCallFragment() {

    }

    @Override
    public void handleTag(int tag, boolean checked) {
        if (tag == CallOptType.ADD_CALL.toInt()){
            Log.e("whx","add call");
            showAddCallDialog();
        }
    }
    private void showAddCallDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_call,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("添加通话")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("addCall",true);
                        FragmentUtils.replace(ConferenceFragment.this,CallOutgoingFragment.newInstance(bundle));
                    }
                });
        builder.create().show();
    }
    @Override
    public void getDataSuc(List<CallOptBean> en) {
        if (en!=null&&en.size()>0){
            list.addAll(en);
            adapter.notifyDataSetChanged();
        }
    }
}
