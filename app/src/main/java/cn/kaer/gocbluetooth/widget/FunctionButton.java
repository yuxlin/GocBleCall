package cn.kaer.gocbluetooth.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cn.kaer.gocbluetooth.R;

/**
 * User: yxl
 * Date: 2020/11/3
 */
public class FunctionButton extends LinearLayout {

    private View mInflate;

    public interface OnFunctionStateChange {
        void onChecked(int viewId, boolean isChecked);
    }

    private String TAG = getClass().getSimpleName();

    private ImageView mImageView;
    private TextView mTextView;
    private OnFunctionStateChange mOnFunctionStateChange;

    public FunctionButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

        initData(attrs);
    }

    private void initData(AttributeSet attrs) {

        @SuppressLint("CustomViewStyleable") TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.funButton);
        String text = ta.getString(R.styleable.funButton_text);
        Drawable drawable = ta.getDrawable(R.styleable.funButton_image);
        float dimension = ta.getDimension(R.styleable.funButton_textSize, 14);
        ta.recycle();
        mImageView.setImageDrawable(drawable);
        mTextView.setText(text);

    }

    private void initView() {
        setClickable(true);
        setFocusable(true);
        mInflate = LayoutInflater.from(getContext()).inflate(R.layout.view_fun_bt, null);
        addView(mInflate);
        mImageView = mInflate.findViewById(R.id.iv_fun);
        mTextView = mInflate.findViewById(R.id.tv_fun);
        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerState(view);
            }
        });
    }

    private void handlerState(View view) {
        boolean isChecked = getViewState(mImageView);
        isChecked = !isChecked;


        setButtonState(isChecked, mImageView);

        if (mOnFunctionStateChange != null) mOnFunctionStateChange
                .onChecked(view.getId(), isChecked);
    }


    private void setButtonState(boolean isChecked, ImageView view) {
        Log.e(TAG, "setButtonState:" + isChecked);
        if (isChecked) { //  选中操作
            view.setBackgroundResource(R.drawable.bg_bt_circle);
            view.setColorFilter(ContextCompat.getColor(getContext(), R.color.backgroundDark));
        } else {  //取消操作
            view.setBackground(null);
            view.setColorFilter(Color.WHITE);

        }
        mImageView.setTag(isChecked);
    }

    public void setOnCheckListen(OnFunctionStateChange onCheckListen) {
        mOnFunctionStateChange = onCheckListen;
    }

    public void setCheck(boolean isCheck) {
        setButtonState(isCheck, mImageView);
    }


    private boolean getViewState(View view) {
        boolean isChecked = false;
        if (view.getTag() == null) {
        } else {
            isChecked = (boolean) view.getTag();
        }
        return isChecked;
    }
}
