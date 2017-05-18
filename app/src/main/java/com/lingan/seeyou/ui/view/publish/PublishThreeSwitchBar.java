package com.lingan.seeyou.ui.view.publish;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 发布话题页面键盘未弹出时的底部Bar
 * Created by huangyuxiang on 2017/2/16.
 */

public class PublishThreeSwitchBar extends LinearLayout {
    private PublishTopicWatchLayout mPublishTopicWatchLayout;

    public PublishThreeSwitchBar(Context context) {
        super(context);
    }

    public PublishThreeSwitchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishThreeSwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPublishTopicWatchLayout(PublishTopicWatchLayout publishTopicWatchLayout) {
        mPublishTopicWatchLayout = publishTopicWatchLayout;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mPublishTopicWatchLayout != null && (mPublishTopicWatchLayout.isShowKeyboard() || mPublishTopicWatchLayout.isShowEmojiPanel())){
            //键盘弹出或Emoji panel显示，隐藏这条bar
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
