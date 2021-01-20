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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import cn.kaer.multichat.adapter.CallOptAdapter;
import cn.kaer.multichat.base.BaseFragment;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallOptType;
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
public class CallFragment extends BaseFragment<CallPresenter, CallModel>implements CallContract.View,
        View.OnClickListener {
    private RecyclerView recyclerView;
    private List<CallOptBean> list = new ArrayList<>();
    private CallOptAdapter adapter;
    private boolean isAddCall;
    public static Fragment newInstance(Bundle args) {
        Fragment fragment = new CallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.call;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(mContext,this,mModel);
    }

    @Override
    protected void initView(View paramView) {
        isAddCall = getArguments().getBoolean("addCall",false);
        paramView.findViewById(R.id.btn_end).setOnClickListener(this);
        recyclerView = paramView.findViewById(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        adapter = new CallOptAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        mPresenter.getData(CallState.INCALL,isAddCall);
        adapter.setOnItemClickListener(new CallOptAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(int position, CheckBox checkBox) {
                handleTag((Integer) checkBox.getTag(),checkBox.isChecked());
            }
        });
        updateSpeaker();
    }

    @Override
    public void transToCallFragment() {

    }

    private void updateSpeaker() {

    }

    @Override
    public void handleTag(int tag, boolean checked) {
        super.handleTag(tag, checked);
        if (tag == CallOptType.ADD_CALL.toInt()){
            Log.e("whx","add call");
            CallHelper.multimeeting(mContext);
            showAddCallDialog();
        }else if (tag == CallOptType.MERGE.toInt()){
            Log.e("whx","merge call");
            mPresenter.showHideHoldFm(false);
            Bundle bundle = new Bundle();
            bundle.putBoolean("addCall",false);
            FragmentUtils.replace(CallFragment.this,ConferenceFragment.newInstance(bundle));
        }else if (tag == CallOptType.SWAP.toInt()){
            Log.e("whx","swap call");
            CallHelper.swapCall(mContext);
        }else if (tag == CallOptType.HANGUP_ALL.toInt()){
            Log.e("whx","end all call");
            CallHelper.endAllCall(mContext);
        }else if (tag == CallOptType.HANGUP_HOLD.toInt()){
            Log.e("whx","end hold call");
            CallHelper.endHoldCall(mContext);
        }else if (tag == CallOptType.MERGE.toInt()){
            Log.e("whx","merge call");
            CallHelper.mergeCall(mContext);
        }
    }

    private void showAddCallDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_add_call,null);
        EditText et = view.findViewById(R.id.et_num);
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
                        String num = et.getText().toString().trim();
                        CallHelper.starCall(mContext,num,0);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("addCall",true);
                        FragmentUtils.replace(CallFragment.this,CallOutgoingFragment.newInstance(bundle));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_end:
                if (isAddCall){
                    mPresenter.showHideHoldFm(false);
                    isAddCall = false;
                    CallHelper.endCall(mContext);
                }else{
                    CallHelper.endAllCall(mContext);
                }
                break;
            default:
                break;
        }
    }

    public void showIncomingDialog(String num) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                .setTitle("新来电")
                .setMessage(num)
                .setNegativeButton("拒接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CallHelper.rejectCall(mContext);
                    }
                }).setPositiveButton("接听", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CallHelper.acceptComingCall(mContext);
                    }
                });
        builder.create().show();
    }
}
