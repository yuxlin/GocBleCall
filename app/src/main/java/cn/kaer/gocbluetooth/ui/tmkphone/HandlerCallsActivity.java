package cn.kaer.gocbluetooth.ui.tmkphone;

import androidx.appcompat.app.AppCompatActivity;
import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.common.bases.BaseActivity;
import cn.kaer.gocbluetooth.MainActivity;
import cn.kaer.gocbluetooth.R;

import android.os.Bundle;

public class HandlerCallsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void init() {
        if (GocSdkController.get().isCalling()) {
            startActivityC(BleCallActivity.class);
        } else {
            startActivityC(MainActivity.class);
        }
        finish();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_handler_calls;
    }

    @Override
    public int getToolBarId() {
        return 0;
    }
}
