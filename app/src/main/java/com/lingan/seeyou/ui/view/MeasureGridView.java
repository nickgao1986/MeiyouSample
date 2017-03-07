package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-4
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class MeasureGridView extends GridView {

    public MeasureGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public MeasureGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureGridView(Context context) {
        this(context, null);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    	super.onLayout(changed, l, t, r, b);
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return super.onTouchEvent(ev);    //To change body of overridden methods use File | Settings | File Templates.
        if(bFlag){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }

    }

    private boolean bFlag = false;
    /**
     * 设置是否吃掉消息
     * @param flag
     */
    public void setTouch(boolean flag){
        bFlag = flag;
    }
    
   
}
