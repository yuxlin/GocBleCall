package cn.kaer.gocbluetooth.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;


@SuppressLint("AppCompatCustomView")
public class KeyBoardButton extends ImageButton {
    private int size = 50;

    public KeyBoardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorFilter(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        copyParentClickRect();
    }

    //小按键设置和父布局一样的点击区域
    public void copyParentClickRect() {
        View parentView = (View) getParent();
        Rect rect = new Rect();
        this.getHitRect(rect);
        rect.left -= 500;
        rect.right += 500;
        rect.top -= 500;
        rect.bottom += 500;
        parentView.setTouchDelegate(new TouchDelegate(rect, this));
    }
}
