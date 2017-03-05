package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nickgao.com.meiyousample.R;


/**
 * parallaxscrollListview 的加载更多item
 * Created by zyf on 2016/3/23.
 */
public class ParallaxScrollListViewFooter extends LinearLayout {
    private String TEXT_NORMAL = "点击或上拉加载更多";
    private String TEXT_READY = "松开载入更多";
    private String TEXT_LOADING = "正在帮您翻页哦···";
    private String TEXT_NONET = "网络不见了，请检查网络哦~";

    public static final int sNORMAL = 0; // 正常状态，显示 点击或上拉加载更多
    public static final int sREADY = 1; // 上拉到满足加载更多的条件了，但是手 未松开(未来离开屏幕)
    public static final int sLOADING = 2;// 正在加载
    public static final int sNONET = 3;//无网络

    private int state = sNORMAL;

    private int mHeight = -1;//正常显示时候的高度
    private RelativeLayout container; // 最外层 容器
    private View progress_pb; // 进度条
    private TextView stateText_tv; // 显示状态文本
    private boolean mShowProgress = true;
    public ParallaxScrollListView.OnFooterViewResetListener footerViewResetListener;

    public ParallaxScrollListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public ParallaxScrollListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 底部拉伸回到原来位置监听
     *
     * @param listener
     */
    public void setOnFooterViewResetListener(ParallaxScrollListView.OnFooterViewResetListener listener) {
        footerViewResetListener = listener;
    }

    private void initView(Context context) {
        container = (RelativeLayout) View.inflate(context, R.layout.parallaxscrolllistview_footer, null);
        addView(container, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        progress_pb = container.findViewById(R.id.pb_footer_progress);
        progress_pb.setVisibility(View.INVISIBLE);
        stateText_tv = (TextView) container.findViewById(R.id.tv_footer_tip);
        TEXT_NORMAL = TEXT_READY = TEXT_LOADING;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeight < 0) {
            // 计算出childView的高
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            View childView = getChildAt(0);
            mHeight = childView.getMeasuredHeight();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void unShowProgress() {
        mShowProgress = false;
    }


    public void setState(int newState) {
        if (this.state == newState) {
            return;
        }

        switch (newState) {
            case sNORMAL:
                progress_pb.setVisibility(View.INVISIBLE);
                stateText_tv.setText(TEXT_NORMAL);
                break;

            case sREADY:
                progress_pb.setVisibility(View.INVISIBLE);
                stateText_tv.setText(TEXT_READY);
                break;

            case sLOADING:
                progress_pb.setVisibility(View.VISIBLE);
                stateText_tv.setText(TEXT_LOADING);
                break;
            case sNONET:
                progress_pb.setVisibility(GONE);
                stateText_tv.setText(TEXT_NONET);
                break;

            default:
                break;
        }

        this.state = newState;
        if (!mShowProgress)
            progress_pb.setVisibility(GONE);
    }

    /**
     * 设置 footer 高度
     *
     * @param height
     */
    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) container.getLayoutParams();
        lp.bottomMargin = height;
        container.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) container.getLayoutParams();
        return lp.bottomMargin;
    }

    public void resetFooterMargin(int duration) {
        container.clearAnimation();
        ResetAnimation animation = new ResetAnimation();
        animation.setDuration(duration);
        container.startAnimation(animation);
    }

    /**
     * 获取正常显示时候的高度
     *
     * @return
     */
    public int getmHeight() {
        return mHeight;
    }

    /**
     * 不需要加载更多，隐藏底部
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) container.getLayoutParams();
        lp.height = 0;
        container.setLayoutParams(lp);
    }

    /**
     * 显示 footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) container.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        container.setLayoutParams(lp);
    }

    public class ResetAnimation extends Animation {

        int extraBottomMargin;
        boolean canSendListener;//是否能发出监听

        public ResetAnimation() {
            extraBottomMargin = getBottomMargin();
            canSendListener = true;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ((LayoutParams) container.getLayoutParams()).bottomMargin = (int) (extraBottomMargin * (1 - interpolatedTime));
            container.requestLayout();
            if (interpolatedTime == 1 && canSendListener) {
                canSendListener = false;
                if (footerViewResetListener != null)
                    footerViewResetListener.onFooterViewRest();
            }
        }
    }
}
