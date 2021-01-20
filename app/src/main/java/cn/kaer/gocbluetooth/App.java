package cn.kaer.gocbluetooth;

import android.app.Application;
import android.content.Intent;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.gocbluetooth.service.GocBleService;

/**
 * User: yxl
 * Date: 2020/10/22
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        GocSdkController.get().init(this);

        startService(new Intent(this, GocBleService.class));

    }


}