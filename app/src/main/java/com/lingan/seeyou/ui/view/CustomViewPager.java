package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2014/7/7.
 */
public class CustomViewPager extends ViewPager {

    private boolean isTouchEnable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	try{
    		 if (isTouchEnable)
    	            return super.onTouchEvent(ev);
    	        return false;	
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return false;
       
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	try{
    		if(bIntercept)
    			return bIntercept;
    		 if (isTouchEnable) {
    	            try {
    	                return super.onInterceptTouchEvent(ev);
    	            } catch (IllegalArgumentException e) {
    	                e.printStackTrace();
    	                return false;
    	            }
    	        }
    	        return false;	
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return false;
       
    }

    public void setTouchEnable(boolean touchEnable) {
        this.isTouchEnable = touchEnable;
    }

    private boolean bIntercept=false;
    public void setIntercept(boolean flag){
    	bIntercept = false;
    }
}
