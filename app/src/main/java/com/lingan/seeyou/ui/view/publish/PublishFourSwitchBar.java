package com.lingan.seeyou.ui.view.publish;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 发布话题页面键盘弹出时的底部Bar
 * Created by huangyuxiang on 2017/2/16.
 */

public class PublishFourSwitchBar extends LinearLayout {
    private PublishTopicWatchLayout mPublishTopicWatchLayout;

    public PublishFourSwitchBar(Context context) {
        super(context);
    }

    public PublishFourSwitchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishFourSwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPublishTopicWatchLayout(PublishTopicWatchLayout publishTopicWatchLayout) {
        mPublishTopicWatchLayout = publishTopicWatchLayout;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mPublishTopicWatchLayout != null && (!mPublishTopicWatchLayout.isShowKeyboard() && !mPublishTopicWatchLayout.isShowEmojiPanel())){
            //不显示键盘且不显示表情Panel，这条bar隐藏
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
