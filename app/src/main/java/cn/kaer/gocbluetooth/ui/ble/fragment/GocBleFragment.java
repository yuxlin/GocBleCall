package cn.kaer.gocbluetooth.ui.ble.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.IGocSdkController;
import cn.kaer.bluetooth.SharedKeys;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.entity.BlueToothPairedInfo;
import cn.kaer.common.bases.BaseFragment;
import cn.kaer.common.utils.SharedPrefHelper;
import cn.kaer.common.utils.ToastFactory;
import cn.kaer.gocbluetooth.R;
import cn.kaer.gocbluetooth.receiver.BlueToothHandler;
import cn.kaer.gocbluetooth.ui.TestActivity;
import cn.kaer.gocbluetooth.ui.ble.SearchActivity;
import cn.kaer.gocbluetooth.ui.ble.fragment.adapter.BlePairedListAdapter;
import cn.kaer.gocbluetooth.utils.AuthNvRam;
import cn.kaer.gocbluetooth.utils.NetWorkUtil;

/**
 * User: yxl
 * Date: 2020/10/9
 */
public class GocBleFragment extends BaseFragment implements View.OnClickListener, OnGocsdkCallback.OnDeviceConnectCallback {

    private RecyclerView mRecyclerViewPaired;

    private BlePairedListAdapter mBlePairedListAdapter;
    private List<BlueToothPairedInfo> mPairedInfoList = new ArrayList<>();
    private TextView mTv_localName;
    private boolean hasKaerAuth = false;

    private IGocSdkController mGocSdkController;

    private boolean isSearching = false;
    private Switch mSw_ble;
    private View mBleSwitchParent;
    private View mPhoneNameParent;
    private static final int MSG_BLE_SWITCH_CHANGE = 0;
    private static final int MSG_REFRESH_STATUS = 1;
    private static final int MSG_JUMP_CONTACT = 2;
    private static String bleName = "";
    private static String bleAddress = "";

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BLE_SWITCH_CHANGE:
                    mPhoneNameParent.setAlpha(1f);
                    mBleSwitchParent.setAlpha(1f);
                    mPhoneNameParent.setEnabled(true);
                    mBleSwitchParent.setEnabled(true);
                    mGocSdkController.loadAllStatus();
                    break;
                case MSG_REFRESH_STATUS:
                    /*removeMessages(MSG_REFRESH_STATUS);
                    sendEmptyMessageDelayed(MSG_REFRESH_STATUS,5000);*/
                    mGocSdkController.loadAllStatus();
                    break;
                case MSG_JUMP_CONTACT:
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("cn.kaer.kecontacts", "cn.kaer.kecontacts.MainActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private TextView mTv_addr;

    @Override
    protected int initLayout() {
        return R.layout.fragment_gocble;
    }

    @Override
    protected void initView(View view) {
        Log.e(TAG, "initView");


        mGocSdkController = GocSdkController.get();

        mTv_localName = findViewById(R.id.gocble_tv_localName);

        mRecyclerViewPaired = findViewById(R.id.recycleView_paired);
        mBleSwitchParent = findViewById(R.id.bleSwitchParent);
        mSw_ble = findViewById(R.id.sw_ble);
        mPhoneNameParent = findViewById(R.id.phoneNameParent);
        mTv_addr = findViewById(R.id.tv_addr);
        mTv_addr.setText(getString(R.string.ble_addr, bleAddress));
        mRecyclerViewPaired.setLayoutManager(new LinearLayoutManager(getContext()));
        mBlePairedListAdapter = new BlePairedListAdapter();
        mRecyclerViewPaired.setAdapter(mBlePairedListAdapter);


        initViewListen();

        //mHandler.sendEmptyMessageDelayed(MSG_REFRESH_STATUS,3000);


        initSwitch();
    }


    private void initViewListen() {
        findViewById(R.id.searchDeviceParent).setOnClickListener(this);
        findViewById(R.id.deviceView).setOnClickListener(this);
        mBlePairedListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mGocSdkController.getHfpStatus() == 2) {
                    return;
                }


                mHandler.removeMessages(MSG_REFRESH_STATUS);
                mHandler.sendEmptyMessageDelayed(MSG_REFRESH_STATUS, 10000);
                BlueToothPairedInfo blueToothPairedInfo = (BlueToothPairedInfo) adapter.getItem(position);
                if (blueToothPairedInfo.status >= 3) {
                    createDisConnectDialog(blueToothPairedInfo);
                } else {

                    if (mGocSdkController.getHfpStatus() >= 3) {
                        Toast.makeText(requireContext(), R.string.alreadyConnected, Toast.LENGTH_SHORT).show();
                    } else {
                        mGocSdkController.connectDevice(blueToothPairedInfo.address);
                        Log.e(TAG, "连接设备");
                        mBlePairedListAdapter.refreshStatus();
                    }
                }
            }
        });

        mPhoneNameParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getContext(), TestActivity.class));
                createRenameDialog();
            }
        });

        mBlePairedListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                BlueToothPairedInfo item = mBlePairedListAdapter.getItem(position);
                createRemovePairedDialog(item);
            }
        });
    }

    private void createRenameDialog() {
        FrameLayout frameLayout = new FrameLayout(requireContext());
        frameLayout.setPadding(40, 0, 40, 0);
        EditText editText = new EditText(requireContext());
        editText.setText(bleName);
        frameLayout.addView(editText);
        new AlertDialog.Builder(requireContext())
                .setTitle("重命名此设备")
                .setView(frameLayout)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String s = editText.getText().toString();
                        mGocSdkController.rename(s);

                    }
                })
                .create()
                .show();
    }

    private void createRemovePairedDialog(final BlueToothPairedInfo item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("要与该设备取消取消配对吗？")
                .setMessage("您的话机将断开与" + item.name + "取消配对。")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        mBlePairedListAdapter.remove(item);
                        mPairedInfoList.remove(item);
                        if (item.address.equals(mGocSdkController.getCurrentConnDevice().address)) {
                            mGocSdkController.disConnectDevice();
                        }
                        mGocSdkController.deletePaired(item.address);
                    }
                })
                .create()
                .show();
    }

    private void createDisConnectDialog(BlueToothPairedInfo blueToothPairedInfo) {

        new AlertDialog.Builder(requireContext())
                .setTitle("要断开与该设备的连接吗？")
                .setMessage("您的话机将断开与" + blueToothPairedInfo.name + "的连接。")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mGocSdkController.disConnectDevice();
                    }
                })
                .create()
                .show();

        Log.e(TAG, "断开设备");
        //    mGocSdkController.disConnectDevice();
    }

    @Override
    protected void initData() {
        initBleListenConn();
        mGocSdkController.loadAllStatus();
        boolean isOpen = SharedPrefHelper.getBoolean(getContext(), SharedKeys.BLE_SWITCH, false);
        mSw_ble.setChecked(isOpen);
        setBleEnable(isOpen);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onLocalAddress(String addr) {
        Log.e(TAG, "本地地址：" + addr);
        bleAddress = addr;
        mTv_addr.setText(getString(R.string.ble_addr, bleAddress));

    }

    @Override
    public void onLocalName(String name) {
        Log.e(TAG, "本地名称" + name);
        mTv_localName.setText(name);
        bleName = name;
    }

    @Override
    public void onInit(int state) {
        if (-1 == state) {
            Log.e(TAG, "强制复位");
            mHandler.sendEmptyMessage(MSG_BLE_SWITCH_CHANGE);
        } else if (0 == state) {
            mHandler.sendEmptyMessageDelayed(MSG_BLE_SWITCH_CHANGE, 500);
        } else if (1 == state) {
            Log.e(TAG, "onInit：" + state);
            mHandler.sendEmptyMessageDelayed(MSG_BLE_SWITCH_CHANGE, 500);

        } else if (2 == state) {
            resetState();

        }
    }


    @Override
    public void onCurrentAndPairList(BlueToothPairedInfo blueToothPairedInfo) {
        Log.e(TAG, "fragment onCurrentAndPairList：" + blueToothPairedInfo.name);
        for (int i = 0; i < mPairedInfoList.size(); i++) {
            BlueToothPairedInfo tempBlueTooth = mPairedInfoList.get(i);
            if (tempBlueTooth.address.equals(blueToothPairedInfo.address)) {
                return;
            }
        }
        Log.e(TAG, "添加新设备：" + blueToothPairedInfo.name);
        mPairedInfoList.add(blueToothPairedInfo);
        refreshPairedList(mPairedInfoList);
    }

    @Override
    public void onCurrentConnAddr(String addr) {
        Log.e(TAG, "onCurrentConnAddr:" + addr);
        //   mBlePairedListAdapter.notifyDataSetChanged();
        // refreshPairedList(mPairedInfoList);

        mBlePairedListAdapter.refreshStatus();


    }

    @Override
    public void onCurrentConnName(String name) {
        Log.e(TAG, "onCurrentConnName:" + name);
    }

    @Override
    public void onDeviceConnected(String addr) {
        Log.e(TAG, "onDeviceConnected:" + addr);
        mHandler.removeMessages(MSG_JUMP_CONTACT);
        mHandler.sendEmptyMessageDelayed(MSG_JUMP_CONTACT, 1000);

    }

    @Override
    public void onDeviceDisconnected(String address) {
        Log.e(TAG, "onDeviceDisconnected:" + address);
        mBlePairedListAdapter.refreshStatus();
    }

    private void initBleListenConn() {

        mGocSdkController.registerDeviceConnCallback(this);
    }

    private void resetState() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!GocSdkController.get().isBleOpen()) {
            mSw_ble.setChecked(false);
            setBleEnable(false);
        } else {
            mGocSdkController.loadAllStatus();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void refreshPairedList(List<BlueToothPairedInfo> list) {
     /*   BlueToothPairedInfo current = new BlueToothPairedInfo();
        current.address = mGocSdkController.getCurrentConnDevice().getAddress();
        current.status = mGocSdkController.getHfpStatus();*/

        mBlePairedListAdapter.refreshData(list);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.searchDeviceParent:
                SearchActivity.startActivity(requireContext(), bleName, bleAddress);
                break;
            case R.id.deviceView:
                startActivityC(TestActivity.class);
                break;

            default:
                break;
        }
    }

    private void startSearchDevice() {
        if (!isSearching) {
            Log.e(TAG, "开始搜索设备");
            isSearching = true;
            mGocSdkController.startSearchDevice();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            animation.setInterpolator(new LinearInterpolator());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AuthNvRam.get().unInit();
        mGocSdkController.unRegisterDeviceConnCallback(this);

    }


    private void setBleEnable(boolean isOpen) {
        Log.e(TAG, "BLE ENABLE:" + isOpen);
        mPhoneNameParent.setAlpha(0.3f);
        mBleSwitchParent.setAlpha(0.3f);
        mPhoneNameParent.setEnabled(false);
        mBleSwitchParent.setEnabled(false);
        if (isOpen) {
            mGocSdkController.openBlueTooth();
            findViewById(R.id.gocble_paired_parent).setVisibility(View.VISIBLE);
            /*   findViewById(R.id.gocble_serarch_parent).setVisibility(View.VISIBLE);*/

        } else {
            mGocSdkController.closeBlueTooth();
            findViewById(R.id.gocble_paired_parent).setVisibility(View.GONE);
            //     findViewById(R.id.gocble_serarch_parent).setVisibility(View.GONE);


            mPairedInfoList.clear();
            mBlePairedListAdapter.refreshData(mPairedInfoList);
        }
    }

    private void initSwitch() {
        AuthNvRam.get().init(requireContext()).queryBluetoothAuth(new AuthNvRam.OnAuthCallback() {
            @Override
            public void onResult(boolean authorized) {
                hasKaerAuth = authorized;
                Log.e(TAG, "has kaer blue auth:" + hasKaerAuth);
            }
        });
        mBleSwitchParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !mSw_ble.isChecked();
                handlerAuth(isChecked);
            }

            private void handlerAuth(boolean isChecked) {

                Log.e(TAG, "switch:" + isChecked);
                if (!isChecked) {
                    mSw_ble.setChecked(false);
                    setBleEnable(false);
                    return;
                }

                boolean available = NetWorkUtil.isAvailable(requireContext());
                boolean hasGocDataBase = hasGocDataBase();//顾凯sdk是否授权成功

                Log.e(TAG, String.format("kaer auth:%s  gocsdk:%s  network:%s",hasKaerAuth,hasGocDataBase,available));
                if (!hasKaerAuth) {
                    ToastFactory.getToast(requireContext(), "暂无授权信息，请联系客服。").show();
                    return;
                }
                if (!hasGocDataBase && !available) {
                    createNetDialog();
                    return;
                }

                boolean isOpen = BlueToothHandler.isOpen();
                Log.e(TAG, "BLE STATE:" + isOpen);
                if (isOpen) {
                    handlerSystemBluetooth();//处理和系统蓝牙互斥，如果系统蓝牙开启则关闭
                    return;
                }
                mSw_ble.setChecked(true);
                setBleEnable(true);

            }
        });
    }

    private void handlerSystemBluetooth() {


        BlueToothHandler.closeBluetooth();
        mPhoneNameParent.setAlpha(0.3f);
        mBleSwitchParent.setAlpha(0.3f);
        mPhoneNameParent.setEnabled(false);
        mBleSwitchParent.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSw_ble.setChecked(true);
                setBleEnable(true);
            }
        }, 800);
    }

    private void createNetDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("联网授权")
                .setMessage("第一次使用蓝牙通话服务需网络授权，请确认网络连接正常。")
                /*.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })*/
                .setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private boolean hasGocDataBase() {
        String filePath = "data/goc/.goc_database";
        File file = new File(filePath);
        Log.e(TAG, "has goc database:" + file.exists());
        return file.exists();
    }

}
