package cn.kaer.multichat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cn.kaer.multichat.R;
import cn.kaer.multichat.bean.CallOptBean;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallOptAdapter extends RecyclerView.Adapter<CallOptAdapter.CallOptVH> {
    private List<CallOptBean>mList;
    private Context mContext;
    public CallOptAdapter(Context context, List<CallOptBean>list) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CallOptAdapter.CallOptVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_call_opt,null);
        CallOptVH vh = new CallOptVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CallOptAdapter.CallOptVH viewHolder, int position) {
        final int curPos = position;
        CallOptBean bean = mList.get(position);
        Drawable drawable = mContext.getResources().getDrawable(bean.getDrsInt());
        viewHolder.checkBox.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        viewHolder.textView.setText(bean.getTtlStr());
        viewHolder.checkBox.setTag(bean.getTag());
        viewHolder.checkBox.setEnabled(bean.isEnable());
        viewHolder.textView.setEnabled(bean.isEnable());
        viewHolder.checkBox.setChecked(bean.isChecked());
        if (bean.isNeedBg()){
            viewHolder.checkBox.setBackgroundResource(R.drawable.bg_circle);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(curPos,(CheckBox)view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    public class CallOptVH extends RecyclerView.ViewHolder{
        private final CheckBox checkBox;
        private final TextView textView;
        public CallOptVH(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbox);
            textView = itemView.findViewById(R.id.tv);
        }
    }
    private OnItemClickListen onItemClickListener;

    public void setOnItemClickListener(OnItemClickListen onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListen{
        void onItemClick(int position,CheckBox checkBox);
    }
}
