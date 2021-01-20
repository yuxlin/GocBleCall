package cn.kaer.multichat.base;

import android.content.Context;
import android.os.Bundle;

import cn.kaer.multichat.utils.rx.RxManage;

/**
 * @describe：TODO</br> Created by wanghuixiang on 2018/12/27.
 */

public abstract class BasePresenter<E, T> {
    public Context context;
    public E mModel;
    public T mView;
    public RxManage mRxManage = new RxManage();

    public void setVM(Context context, T v, E m)
    {
        this.context = context;
        this.mView = v;
        this.mModel = m;
    }

    public void onCreate(Bundle savedInstanceState) {}

    public void onResume() {}

    public void onStart() {}

    public void onStop() {}

    public void onPause() {}

    public void onRestart() {}

    public void onDestroy()
    {
        this.mRxManage.clear();
    }
}
