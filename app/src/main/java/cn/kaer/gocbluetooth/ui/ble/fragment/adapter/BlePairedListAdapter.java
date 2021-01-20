package cn.kaer.gocbluetooth.ui.ble.fragment.adapter;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.entity.BlueToothPairedInfo;
import cn.kaer.gocbluetooth.R;

/**
 * User: yxl
 * Date: 2020/10/9
 */
public class BlePairedListAdapter extends BaseQuickAdapter<BlueToothPairedInfo, BaseViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Handler mHandler = new Handler();
    public BlePairedListAdapter() {
        super(R.layout.recycle_blelist);
        addChildClickViewIds(R.id.ib_delete);

    }




    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BlueToothPairedInfo blueToothPairedInfo) {
        // baseViewHolder.setText(R.);
//        int hfpStatus = GocSdkController.get().getHfpStatus();
        Log.e(TAG,"convert:"+getItemPosition(blueToothPairedInfo));

        baseViewHolder.setText(R.id.bleRecycle_tv_name, blueToothPairedInfo.name);
        String status = "";

        if (blueToothPairedInfo.status >= 3) {
            status = "已连接 | 使用中";
            baseViewHolder.itemView.setAlpha(1);
        } else if (blueToothPairedInfo.status == 2) {
            status = "连接中";
            baseViewHolder.itemView.setAlpha(0.5f);
        } else {
            status = "";
            baseViewHolder.itemView.setAlpha(1);
        }
        baseViewHolder.setText(R.id.bleRecycle_tv_status, status)
                .setGone(R.id.bleRecycle_tv_status, TextUtils.isEmpty(status))
                .setVisible(R.id.ib_delete, true)
        ;



        if (getItemCount() > 1) {
            baseViewHolder.setVisible(R.id.recycleLine, baseViewHolder.getAdapterPosition() != getItemCount() - 1);
        }
    }

    public void refreshData(final List<BlueToothPairedInfo> list) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                BlueToothPairedInfo currentConnect = new BlueToothPairedInfo();

                currentConnect.status = GocSdkController.get().getHfpStatus();
                currentConnect.address = GocSdkController.get().getCurrentConnDevice().address;
                Log.e(TAG, "HFP STATUS:" + currentConnect.status);
                for (int i = 0; i < list.size(); i++) {
                    BlueToothPairedInfo tempInfo = list.get(i);
                    if (tempInfo.address.equals(currentConnect.address)) {
                        tempInfo.status = currentConnect.status;
                    } else {
                        tempInfo.status = 1;
                    }
                }
                setList(list);
            }
        });

    }

    public void refreshStatus() {

        refreshData(getData());
    }



    /*  public void updateItemState(BlueToothPairedInfo blueToothPairedInfo) {
        List<BlueToothPairedInfo> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            BlueToothPairedInfo temp = dataList.get(i);
            if (temp.address.equals(blueToothPairedInfo.address)) {
                temp.status = blueToothPairedInfo.status;
            } else {
                temp.status = 1;
            }

        }
        notifyDataSetChanged();
    }


    private BlueToothPairedInfo getItemDataByAddress(String address) {
        List<BlueToothPairedInfo> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            BlueToothPairedInfo temp = dataList.get(i);
            if (temp.address.equals(address)) {
                return temp;
            }
        }
        return null;
    }
*/

}
