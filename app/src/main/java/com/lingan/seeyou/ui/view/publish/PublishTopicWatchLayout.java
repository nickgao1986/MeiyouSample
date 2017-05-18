package com.lingan.seeyou.ui.view.publish;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 键盘弹出收起监听Layout
 * Created by huangyuxiang on 2017/2/16.
 */

public class PublishTopicWatchLayout extends LinearLayout {
    private boolean mShowKeyboard = false;  //是否显示键盘
    private boolean mShowEmojiPanel = false;  //是否显示表情Panel

    private LayoutHeightChangedHandler mHeightChangedHandler;

    private OnKeyboardStatusChangeListener mListener;

    public PublishTopicWatchLayout(Context context){
        super(context);
        init();
    }

    public PublishTopicWatchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PublishTopicWatchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mHeightChangedHandler = new LayoutHeightChangedHandler((Activity) getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        handleBeforeMeasure(MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void handleBeforeMeasure(int height) {
        int offset = mHeightChangedHandler.onKeyboardChanged(height);
        if(offset == 0){
            return;
        }
        if (offset > 0) {
            //键盘弹起 (offset > 0，高度变小)，Panel不展示
            this.mShowKeyboard = true;
            this.mShowEmojiPanel = false;
        }else{
            // 1. 总得来说，在监听到键盘已经显示的前提下，键盘收回才是有效有意义的。
            // 2. 修复在Android L下使用V7.Theme.AppCompat主题，进入Activity，默认弹起面板bug，
            // 第2点的bug出现原因:在Android L下使用V7.Theme.AppCompat主题，并且不使用系统的ActionBar/ToolBar，V7.Theme.AppCompat主题,还是会先默认绘制一帧默认ActionBar，然后再将他去掉（略无语）
            //键盘收回 (offset < 0，高度变大)
            this.mShowKeyboard = false;
        }
        if(mListener != null){
            mListener.onChanged(offset > 0);
        }
    }

    public boolean isShowEmojiPanel() {
        return mShowEmojiPanel;
    }

    /**
     * 设置当前是否显示Emoji Panel
     * @param showEmojiPanel
     */
    public void setShowEmojiPanel(boolean showEmojiPanel) {
        mShowEmojiPanel = showEmojiPanel;
    }

    public boolean isShowKeyboard() {
        return mShowKeyboard;
    }

    public void setOnKeyboardStatusChangeListener(OnKeyboardStatusChangeListener listener) {
        mListener = listener;
    }

    public interface OnKeyboardStatusChangeListener{
        void onChanged(boolean isShowing);
    }
}
