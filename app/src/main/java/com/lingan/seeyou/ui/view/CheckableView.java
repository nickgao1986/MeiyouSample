package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA. R
 * Date: 14-7-4
 */
public class CheckableView extends LoaderImageView {
    protected boolean mChecked = false;
    protected int mViewSize;
    protected OnCheckedChangeListener mCheckedChangeListener;

    public CheckableView(Context context) {
        super(context);
        initialView();
    }

    public CheckableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView();
    }

    public CheckableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialView();
    }

    protected void initialView() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        mViewSize = display.getWidth() /3;

    }
    public void  setViewSize(int size){
        mViewSize =size;
    }
    public void setChecked(boolean checked) {
        mChecked = checked;
        if (null != mCheckedChangeListener) {
            mCheckedChangeListener.onChange(mChecked);
        }
        invalidate();
    }

    public void setCheckedWithoutNotify(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }

    public boolean getChecked() {
        return mChecked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(mViewSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mViewSize, MeasureSpec.EXACTLY));
    }

    public interface OnCheckedChangeListener {
        public void onChange(boolean checked);
    }
}
