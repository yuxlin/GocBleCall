package cn.kaer.common.bases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cn.kaer.common.rx.RxManage;

public abstract class BaseFragment extends Fragment {

    protected RxManage mRxManage;
    protected String TAG = getClass().getSimpleName();
    private View mInflate;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManage = new RxManage();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return init(inflater, container);
    }

    private View init(LayoutInflater inflater, ViewGroup container) {
        mInflate = inflater.inflate(initLayout(), container, false);


        initView(mInflate);
        initData();
        initEvent();
        return mInflate;
    }



    protected <T extends View> T findViewById(@IdRes int id) {
        return mInflate.findViewById(id);
    }

    protected abstract int initLayout();

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void initEvent();

    protected RxManage getRxManage() {
        return mRxManage;
    }

    public void startActivityC(Class cls) {
        super.startActivity(new Intent(getContext(), cls));
    }

    public void startActivityC(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
