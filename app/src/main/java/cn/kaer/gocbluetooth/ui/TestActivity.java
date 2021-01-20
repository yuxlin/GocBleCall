package cn.kaer.gocbluetooth.ui;

import androidx.appcompat.app.AppCompatActivity;
import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.ui.tmkphone.BleCallActivity;
import cn.kaer.gocbluetooth.utils.SoundHelper;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;

public class TestActivity extends AppCompatActivity {

    private EditText mEt_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SoundHelper.getInstance().init(this);
        initView();
    }

    private void initView() {

        mEt_num = findViewById(R.id.et_num);

    }

    public void click_call(View view) {
        BleCallActivity.startBleCallActivity(this, mEt_num.getText().toString(), BleCallActivity.CALL_TYPE_OUTGOING);
    }

    public void click_outgoing(View view) {
        BleCallActivity.startBleCallActivity(this, mEt_num.getText().toString(), BleCallActivity.CALL_TYPE_INCOMING);
    }
    public void click_endCall(View view) {
        try {
            GocSdkController.get().getGocSdkService().phoneHangUp();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void click_outter(View view) {
        SoundHelper.getInstance().setSoundChannel(false);
    }

    public void click_innerl(View view) {
        SoundHelper.getInstance().setSoundChannel(true);
    }


    public void click_jump(View view) {
        startActivity(new Intent("cn.kaer.btphone.homepage"));
    }


}
