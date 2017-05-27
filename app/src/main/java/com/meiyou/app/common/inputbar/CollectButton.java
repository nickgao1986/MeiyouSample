package com.meiyou.app.common.inputbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import nickgao.com.meiyousample.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * 收藏按钮
 * Created by huangyuxiang on 2016/11/30.
 */
public class CollectButton extends LinearLayout {
    private boolean mIsCollected = false;  //是否是已收藏状态
    private ImageView ivNotCollect, ivCollect; //未收藏状态，收藏状态

    private CollectButton.OnCollectButtonClickListener mListener;

    public CollectButton(Context context) {
        super(context);
        init(context);
    }

    public CollectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);

        View contentView = ViewFactory.from(context).getLayoutInflater().inflate(R.layout.layout_common_collect_button, this, true);
        ivNotCollect = (ImageView) contentView.findViewById(R.id.iv_no_collect);
        ivNotCollect.setVisibility(View.VISIBLE);
        ivCollect = (ImageView) contentView.findViewById(R.id.iv_collect);
        ivCollect.setVisibility(View.GONE);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    boolean result = mListener.onClick(!mIsCollected);
                    if (result) {
                        //处理点击，并开始执行成功动画
                        setCollectState(!mIsCollected);
                        startCollectAnimation(mIsCollected);
                    }
                }
            }
        });
    }

    /**
     * 开始收藏动画，取消收藏无动画用setCollectState
     *
     * @param isCollected 收藏动作
     */
    private void startCollectAnimation(boolean isCollected) {
        //停掉之前的动画
        stopAnimation();
        ImageView ivShow, ivHide;
        if (isCollected) {
            ivShow = ivCollect;
            ivHide = ivNotCollect;
        } else {
            ivShow = ivNotCollect;
            ivHide = ivCollect;
        }

        //隐藏动画
        AnimationSet hideAnimationSet = new AnimationSet(false);
        hideAnimationSet.setFillAfter(true);
        ScaleAnimation s1 = new ScaleAnimation(1f, 0f, 1f, 0f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        s1.setDuration(240);
        s1.setInterpolator(new AccelerateDecelerateInterpolator());
        hideAnimationSet.addAnimation(s1);
        AlphaAnimation a1 = new AlphaAnimation(1.0f, 0.0f);
        a1.setDuration(240);
        a1.setInterpolator(new DecelerateInterpolator());
        hideAnimationSet.addAnimation(a1);

        //显示动画
        AnimationSet showAnimatorSet = new AnimationSet(false);
        ScaleAnimation s2 = new ScaleAnimation(0f, 1f, 0f, 1f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        s2.setDuration(240);
        showAnimatorSet.addAnimation(s2);
        AlphaAnimation a2 = new AlphaAnimation(0.0f, 1.0f);
        a2.setDuration(240);
        a2.setInterpolator(new AccelerateInterpolator());
        showAnimatorSet.addAnimation(a2);

        //执行动画
        ivHide.setVisibility(View.VISIBLE);
        ivHide.startAnimation(hideAnimationSet);
        ivShow.setVisibility(View.VISIBLE);
        ivShow.startAnimation(showAnimatorSet);
    }

    /**
     * 设置收藏状态，不做动画
     *
     * @param isCollected 是否收藏
     */
    public void setCollectState(boolean isCollected) {
        this.mIsCollected = isCollected;
        stopAnimation();

        if (isCollected) {
            ivCollect.setVisibility(View.VISIBLE);
            ivNotCollect.setVisibility(View.GONE);
        } else {
            ivCollect.setVisibility(View.GONE);
            ivNotCollect.setVisibility(View.VISIBLE);
        }
    }

    public void setOnCollectButtonClickListener(OnCollectButtonClickListener listener) {
        mListener = listener;
    }

    public boolean isCollected() {
        return mIsCollected;
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        ivCollect.clearAnimation();
        ivNotCollect.clearAnimation();
    }

    public void setCollectDrawables(int unCollectResId, int collectResId) {
        ivCollect.setImageResource(collectResId);
        ivNotCollect.setImageResource(unCollectResId);
    }

    public interface OnCollectButtonClickListener {
        /**
         * @param isCollect 是否是收藏动作
         * @return 收藏按钮是否需要做UI变化
         */
        boolean onClick(boolean isCollect);
    }
}
