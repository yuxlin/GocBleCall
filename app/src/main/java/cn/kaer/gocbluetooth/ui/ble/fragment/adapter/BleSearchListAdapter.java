package cn.kaer.gocbluetooth.ui.ble.fragment.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import cn.kaer.bluetooth.entity.BlueToothInfo;
import cn.kaer.gocbluetooth.R;

/**
 * User: yxl
 * Date: 2020/10/9
 */
public class BleSearchListAdapter extends BaseQuickAdapter<BlueToothInfo, BaseViewHolder> {
    private String TAG = getClass().getSimpleName();


    public BleSearchListAdapter() {
        super(R.layout.recycle_blelist);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BlueToothInfo blueToothInfo) {
        String status = "";
        if (blueToothInfo.status == 2) {
            status = "正在配对";
            baseViewHolder.itemView.setAlpha(0.5f);
        } else {
            baseViewHolder.itemView.setAlpha(1f);
        }
        baseViewHolder.setText(R.id.bleRecycle_tv_name, blueToothInfo.name)
                .setText(R.id.bleRecycle_tv_status, status)
                .setGone(R.id.bleRecycle_tv_status, TextUtils.isEmpty(status));


        if (getItemCount() > 1) {
            baseViewHolder.setVisible(R.id.recycleLine, baseViewHolder.getAdapterPosition() != getItemCount() - 1);
        }
    }

    public boolean isConnecting() {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).status == 2) {
                return true;
            }
        }
        return false;
    }
}
