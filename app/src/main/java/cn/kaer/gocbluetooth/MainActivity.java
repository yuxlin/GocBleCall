package cn.kaer.gocbluetooth;

import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.common.bases.BaseActivity;
import cn.kaer.gocbluetooth.utils.AuthNvRam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

import com.pgyersdk.update.PgyUpdateManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private IGocSdkController mGoSdkController;
    private String TAG = getClass().getSimpleName();
    private List<String> mList = new ArrayList<>();
    private Handler mHandler = new Handler();
    private ArrayAdapter<? extends String> mArrayAdapter;
    private PgyUpdateManager mPgyUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPgyUpdateManager = new PgyUpdateManager.Builder()
                .setForced(false)
                .setUserCanRetry(true)
                .register();


    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int getToolBarId() {
        return R.id.toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unRegister();

    }

    @Override
    public void onClick(View view) {

    }


}
