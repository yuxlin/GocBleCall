package cn.kaer.multichat.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.kaer.multichat.utils.BarUtils;
import cn.kaer.multichat.utils.TUtil;


public abstract class BaseActivity<T extends BasePresenter, E extends BaseModelCreate>  extends AppCompatActivity {
    protected T mPresenter;
    protected E mModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = TUtil.getT(this, 0);
        this.mModel = TUtil.getT(this, 1);
//        BarUtils.transparentStatusBar(this);
        setContentView(getContantViewId());
        initPresenter();
        initView();
    }

    protected abstract void initPresenter();

    protected abstract void initView();

    protected abstract int getContantViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mPresenter != null) {
            this.mPresenter.onDestroy();
        }
    }
}
