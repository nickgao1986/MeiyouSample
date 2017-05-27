package nickgao.com.meiyousample.love;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import nickgao.com.meiyousample.R;

/**
 * 分析顶部更多模块
 * @author yuzongxu <yuzongxu@xiaoyouzi.com>
 * @since 2017/3/31
 */
public class AnalysisHeadMoreHelper {

    /**
     * 寄存的View
     */
    private View mView;
    /**
     * 头部名称
     */
    private String title;
    /**
     * 右边是否有说明
     */
    private String rightHint;
    /**
     * 是否箭头需要消失（再分析主页的时候需要消失）
     */
    private boolean isArrowGone;
    /**
     * 查看更多点击事件
     */
    private View.OnClickListener onClickListener;

    private TextView mTitleTextView;
    private TextView mHintTextView;
    private View mArrowView;

    public AnalysisHeadMoreHelper(Builder builder) {
        this.mView = builder.mView;
        this.title = builder.title;
        this.rightHint = builder.rightHint;
        this.isArrowGone = builder.isArrowGone;
        this.onClickListener = builder.listener;

        mTitleTextView = (TextView) mView.findViewById(R.id.tv_habit_head_title);
        mHintTextView = (TextView) mView.findViewById(R.id.tv_habit_head_hint);
        mArrowView = mView.findViewById(R.id.iv_habit_head_arrow);
    }

    /**
     * 因为默认的Head有margin_top = 10dp 需要把这个去掉
     */
    public void resetLayoutMargin() {
        if (mView != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mView.getLayoutParams();
            lp.topMargin = 0;
            mView.setLayoutParams(lp);
        }
    }

    /**
     * 开始生成头部
     */
    public void generateHeadMoreLayout() {
        mArrowView.setVisibility(isArrowGone ? View.GONE : View.VISIBLE);
        mTitleTextView.setText(!TextUtils.isEmpty(title) ? title : "");
        mHintTextView.setText(!TextUtils.isEmpty(rightHint) ? rightHint : "");

        if (onClickListener != null) {
            mHintTextView.setOnClickListener(onClickListener);
            mArrowView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 写入右边数据
     */
    public void setRightHint(String rightHint) {
        this.rightHint = rightHint;
        mHintTextView.setText(!TextUtils.isEmpty(rightHint) ? rightHint : "");
    }

    public void setArrowGone(boolean gone) {
        this.isArrowGone = gone;
        mArrowView.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    /**
     * 写入左边数据
     */
    public void setTitle(String title) {
        this.title = title;
        mTitleTextView.setText(!TextUtils.isEmpty(title) ? title : "");
    }

    public static class Builder {
        private View mView;
        private String title;
        private String rightHint;
        private boolean isArrowGone;
        private View.OnClickListener listener;

        public Builder() {

        }

        public Builder setRootView(View mView) {
            this.mView = mView;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setRightHint(String rightHint) {
            this.rightHint = rightHint;
            return this;
        }

        public Builder setArrowGone(boolean isArrowGone) {
            this.isArrowGone = isArrowGone;
            return this;
        }

        public Builder setOnclickListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public AnalysisHeadMoreHelper build() {
            return new AnalysisHeadMoreHelper(this);
        }
    }
}
