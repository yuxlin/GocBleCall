package cn.kaer.common.bases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import cn.kaer.common.R;
import cn.kaer.common.rx.RxManage;
import cn.kaer.common.utils.SimpleUtil;

@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();
    private Toolbar mToolbar;
    protected RxManage mRxManage;
    protected Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SimpleUtil.setTranslucentStatus(this, isDeepStatusBar(), isHideNavigation());
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        initBase();

        init();

        initView();

        initData();


    }

    protected abstract void init();

    public interface OnTabActivityResultListener {
        public void onTabActivityResult(int requestCode, int resultCode, Intent data);
    }


    protected void initBase() {
        mRxManage = new RxManage();
        mToolbar = findViewById(getToolBarId());
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isHideNavigation()) { //切换到别的有导航按钮的页面再切换来会显示导航按钮所以在Onresume中再切换一遍
            SimpleUtil.setTranslucentStatus(this, isDeepStatusBar(), isHideNavigation());
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public abstract void initView();

    public abstract void initData();

    public abstract int getContentView();

    public boolean isDeepStatusBar() { //是否是深色状态栏
        return true;
    }

    public boolean isHideNavigation() {
        return false;
    }

    public abstract int getToolBarId();


    protected RxManage getRxManage() {
        return mRxManage;
    }

    protected BaseActivity getParentActivity() {
        return this;
    }


    public void replaceFragment(int container, Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
          /*      .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)*/
                .replace(container, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();


    }

    public void addFragment(int container, Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .add(container, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();

    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();

    }

    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        currentFragment = fragment;
    }

    public void startActivityC(Class cls) {
        super.startActivity(new Intent(this, cls));
    }


    public void startActivityC(Intent intent) {
        super.startActivity(intent);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    *    Observable.create((ObservableOnSubscribe<Boolean>) e -> {

                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(dataList -> {

                }, Throwable::printStackTrace);
    * */
}