package com.lingan.seeyou.ui.view.publish;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 话题详情表情Panel外部布局
 * Created by huangyuxiang on 2017/2/17.
 */

public class PublishEmojiPanelLayout extends LinearLayout {
    private PublishTopicWatchLayout mPublishTopicWatchLayout;

    public PublishEmojiPanelLayout(Context context) {
        super(context);
    }

    public PublishEmojiPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishEmojiPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPublishTopicWatchLayout(PublishTopicWatchLayout publishTopicWatchLayout) {
        mPublishTopicWatchLayout = publishTopicWatchLayout;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mPublishTopicWatchLayout != null && !mPublishTopicWatchLayout.isShowEmojiPanel()){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
