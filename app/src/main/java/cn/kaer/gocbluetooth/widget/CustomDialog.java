package cn.kaer.gocbluetooth.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;

import cn.kaer.common.utils.LogUtils;
import cn.kaer.gocbluetooth.R;

/**
 * User: yxl
 * Date: 2020/6/5
 */
public class CustomDialog extends BaseDialog<CustomDialog> {

    private TextView mTv_title;
    private View mLine;

    public interface OnBtnClickListen {
        void onClick(CustomDialog customDialog, View view);
    }

    private String TAG = getClass().getSimpleName();
    private OnBtnClickListen mOnBtnRightClickListener;
    private Button mBt_right;
    private TextView mTv_content;
    private Button mBt_cancel;
    private String content;
    private String title;
    private int leftBtnColor;
    private int rightBtnColor;
    private String leftBtnText;
    private String rightBtnText;
    private int contentColor;


    public CustomDialog(Context context) {
        super(context);
        /*default data*/
        leftBtnColor = Color.parseColor("#007dfe");
        rightBtnColor = Color.parseColor("#007dfe");
        contentColor = Color.BLACK;
        leftBtnText = context.getString(R.string.cancel);
        rightBtnText = context.getString(R.string.enter);
        content = "default text";
    }


    @Override
    public View onCreateView() {
        widthScale(0.95f);
        // showAnim(new ZoomInEnter());
        LogUtils.e(TAG, "onCreateView");
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(getContext(), R.layout.dialog_custom, null);
        mTv_title = inflate.findViewById(R.id.tv_title);

        mLine = inflate.findViewById(R.id.line);
        mTv_content = inflate.findViewById(R.id.dialog_system_tv_show);
        mBt_cancel = inflate.findViewById(R.id.dialog_delete_cancel);
        mBt_right = inflate.findViewById(R.id.dialog_delete_enter);
        mLine.setVisibility(View.GONE);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mTv_content.setText(content);
        mTv_content.setTextColor(contentColor);
        mTv_title.setText(title);

        if (TextUtils.isEmpty(title)){
            mTv_title.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
        }else {
            mTv_title.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.GONE);
           // mLine.setVisibility(View.VISIBLE);
        }

        mBt_cancel.setText(leftBtnText);
        mBt_cancel.setTextColor(leftBtnColor);
        mBt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mBt_right.setText(rightBtnText);
        mBt_right.setTextColor(rightBtnColor);
        mBt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnBtnRightClickListener != null) {
                    mOnBtnRightClickListener.onClick(CustomDialog.this, view);
                }
            }
        });

    }

    public CustomDialog setOnBtnRightClickListener(OnBtnClickListen onBtnRightClickListener) {
        mOnBtnRightClickListener = onBtnRightClickListener;
        return this;
    }

    public CustomDialog setContent(String s) {
        content = s;
        if (mTv_content != null) {
            mTv_content.setText(s);
        }
        return this;
    }
    public CustomDialog setTitle(String s) {
        title = s;
        if (mTv_title != null) {
            mTv_title.setText(s);
        }
        return this;
    }
    public CustomDialog setLeftBtnColor(int leftBtnColor) {
        this.leftBtnColor = leftBtnColor;
        return this;
    }

    public CustomDialog setRightBtnColor(int rightBtnColor) {
        this.rightBtnColor = rightBtnColor;
        return this;
    }

    public CustomDialog setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
        return this;
    }

    public CustomDialog setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
        return this;
    }

    public CustomDialog setContentColor(int contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    /* public static class Builder {
        private String content;
        private View.OnClickListener btnRightOnClickListen;

        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setBtnRightOnClickListen(View.OnClickListener btnRightOnClickListen) {
            this.btnRightOnClickListen = btnRightOnClickListen;
            return this;

        }

        public CustomDialog build() {
            return new CustomDialog(mContext, this);
        }
    }*/
}
