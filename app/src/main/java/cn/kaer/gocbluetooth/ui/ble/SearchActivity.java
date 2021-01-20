package cn.kaer.gocbluetooth.ui.ble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.bluetooth.SharedKeys;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.entity.BlueToothInfo;
import cn.kaer.bluetooth.entity.BlueToothPairedInfo;
import cn.kaer.common.bases.BaseActivity;
import cn.kaer.common.utils.SharedPrefHelper;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.ui.ble.fragment.adapter.BleSearchListAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements OnGocsdkCallback.OnDeviceSearchCallback, OnGocsdkCallback.OnDeviceConnectCallback {
    private BleSearchListAdapter mBleSearchListAdapter;
    private RecyclerView mRecyclerViewSearch;
    private IGocSdkController mGocSdkController;
    private List<BlueToothInfo> mBlueToothInfoList = new ArrayList<>();
    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void init() {
        mGocSdkController = GocSdkController.get();

    }

    @Override
    public void initView() {
        mRecyclerViewSearch = findViewById(R.id.recycleView_search);
        mRecyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        mBleSearchListAdapter = new BleSearchListAdapter();
        mRecyclerViewSearch.setAdapter(mBleSearchListAdapter);

        mBleSearchListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mBleSearchListAdapter.isConnecting()) {
                    return;
                }

                if (mGocSdkController.getHfpStatus() == 2) {
                    return;
                }
                if (mGocSdkController.getHfpStatus() >= 3) {
                    Toast.makeText(SearchActivity.this, R.string.alreadyConnected, Toast.LENGTH_SHORT).show();
                    return;
                }

                BlueToothInfo item = (BlueToothInfo) adapter.getItem(position);
                mGocSdkController.connectDevice(item.address);
                item.status = 2;
                mBleSearchListAdapter.notifyDataSetChanged();
            }
        });
        initBleSearchListen();
    }

    @Override
    public void initData() {
        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            ((TextView) findViewById(R.id.gocble_tv_localName)).setText(name);
        }
        if (getIntent().hasExtra("address")) {
            String address = getIntent().getStringExtra("address");
        }
        mGocSdkController.startSearchDevice();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGocSdkController.stopSearchDevice();
        mGocSdkController.unRegisterDeviceSearchCallback(this);
        mGocSdkController.unRegisterDeviceConnCallback(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    public int getToolBarId() {
        return R.id.toolbar;
    }


    @Override
    public void onDiscovery(String type, String name, String addr) {
        Log.e(TAG, "onDiscovery:" + type + "--" + name + Thread.currentThread().getName());
        BlueToothInfo blueToothInfo = new BlueToothInfo();
        blueToothInfo.name = name;
        blueToothInfo.address = addr;
        for (int i = 0; i < mBlueToothInfoList.size(); i++) {
            BlueToothInfo tempBlueToothInfo = mBlueToothInfoList.get(i);
            if (tempBlueToothInfo.address.equals(blueToothInfo.address)) {
                return;
            }
        }

        mBlueToothInfoList.add(blueToothInfo);

        mBleSearchListAdapter.addData(blueToothInfo);

    }

    @Override
    public void onDiscoveryDone() {
        Log.e(TAG, "onDiscoveryDone :");
        //    refreshSearchList(mBlueToothInfoList);

        handlerSearchDone();
        if (mGocSdkController.getHfpStatus() != 2) {
            mGocSdkController.startSearchDevice();
        }

    }

    private void initBleSearchListen() {
        mGocSdkController.registerDeviceSearchCallback(this);
        mGocSdkController.registerDeviceConnCallback(this);

    }

    private void handlerSearchDone() {
        Log.e(TAG, "handlerSearchDone");
        isSearching = false;
        //  mIv_refresh.clearAnimation();
    }

    public static void startActivity(Context context, String name, String addr) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("address", addr);
        context.startActivity(intent);
    }

    @Override
    public void onLocalAddress(String addr) {

    }

    @Override
    public void onLocalName(String name) {

    }

    @Override
    public void onInit(int state) {

    }

    @Override
    public void onCurrentAndPairList(BlueToothPairedInfo blueToothPairedInfo) {

    }

    @Override
    public void onCurrentConnAddr(String addr) {

    }

    @Override
    public void onCurrentConnName(String name) {

    }

    @Override
    public void onDeviceConnected(String addr) {
        Log.e(TAG, "连接成功");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }

    @Override
    public void onDeviceDisconnected(String address) {

    }
}
