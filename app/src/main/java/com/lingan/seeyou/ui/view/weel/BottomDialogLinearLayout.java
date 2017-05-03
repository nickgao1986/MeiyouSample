package com.lingan.seeyou.ui.view.weel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Author: lwh
 * Date: 3/22/17 14:00.
 */

public class BottomDialogLinearLayout extends LinearLayout {

    private Context mContext;
    private Scroller mScroller;
    public BottomDialogLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public BottomDialogLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public BottomDialogLinearLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mScroller = new Scroller(mContext);
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    public Scroller getScroller() {
        return mScroller;
    }
}
