package com.meiyou.app.common.inputbar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.lingan.seeyou.ui.view.widget.EmojiLayout;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.R;


/**
 * 底部输入框工具条
 * Created by LinXin on 2017/3/2.
 */
public class CommonInputBar extends LinearLayout implements View.OnClickListener {
    private LinearLayout mActionLayout;
    private TextView mCommentCountTv;
    private TextView mWriteCommentTv;
    private ImageView mCommentImv;
    private ImageView mShareImv;
    private CollectButton mCollectBtn;
    private View topDivider;

    private boolean isHideActionButton = false;  //是否是隐藏操作按钮
    private String defaultHint = "";//默认提示语
    private int maxCommentCount = 1000;

    private CommonInputDialog mCommentDialog;
    private EditText mCommentEdt;
    private ImageView mEmojiToggleImv;
    private Button mSubmitBtn;

    private OnClickListener mCommentTvClickListener;
    private EmojiLayout.OnEmojiViewShowListener mEmojiViewShowListener;

    public CommonInputBar(Context context) {
        super(context);
        init(context);
    }

    public CommonInputBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonInputBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        setOrientation(VERTICAL);
        SkinManager.getInstance().setDrawableBackground(this, R.drawable.apk_all_white);
        ViewFactory.from(context).getLayoutInflater().inflate(R.layout.layout_common_input_bar, this, true);

        mActionLayout = (LinearLayout) findViewById(R.id.common_input_bar_action_layout);
        mWriteCommentTv = (TextView) findViewById(R.id.common_input_bar_write_comment_tv);
        mWriteCommentTv.setOnClickListener(this);
        mCommentCountTv = (TextView) findViewById(R.id.common_input_bar_review_count_tv);
        mCommentCountTv.setVisibility(View.GONE);
        mCommentImv = (ImageView) findViewById(R.id.common_input_bar_comment_imv);
        mCollectBtn = (CollectButton) findViewById(R.id.common_input_bar_collect_btn);
        mShareImv = (ImageView) findViewById(R.id.common_input_bar_share_imv);
        topDivider = findViewById(R.id.common_input_bar_top_divider);

        //初始化对话框
        mCommentDialog = new CommonInputDialog(context);
        mCommentEdt = mCommentDialog.getEditText();
        EmojiLayout emojiLayout = mCommentDialog.getEmojiLayout();
        mEmojiToggleImv = mCommentDialog.getEmojiToggleImv();
        mSubmitBtn = mCommentDialog.getSubmitBtn();
        emojiLayout.setOnEmojiViewShowListener(new EmojiLayout.OnEmojiViewShowListener() {
            @Override
            public void onShow() {
                SkinManager.getInstance().setDrawable(mEmojiToggleImv, R.drawable.sel_common_keyboard_btn);
                if (mEmojiViewShowListener != null) {
                    mEmojiViewShowListener.onShow();
                }
            }

            @Override
            public void onHide() {
                SkinManager.getInstance().setDrawable(mEmojiToggleImv, R.drawable.sel_common_emoji_btn);
                if (mEmojiViewShowListener != null) {
                    mEmojiViewShowListener.onHide();
                }
            }
        });
    }

    /**
     * 点击回复监听
     *
     * @param listener
     */
    public void setOnCommentClickListener(OnClickListener listener) {
        mCommentImv.setOnClickListener(listener);
    }

    /**
     * 点击分享监听
     *
     * @param listener
     */
    public void setOnShareClickListener(OnClickListener listener) {
        mShareImv.setOnClickListener(listener);
    }

    /**
     * 点击收藏监听
     *
     * @param listener
     */
    public void setOnCollectButtonClickListener(CollectButton.OnCollectButtonClickListener listener) {
        mCollectBtn.setOnCollectButtonClickListener(listener);
    }

    /**
     * 设置点击回复监听
     *
     * @param listener
     */
    public void setOnSubmitClickListener(final OnSubmitClickListener listener) {
        mSubmitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = mCommentEdt.getText();
                listener.onSubmitClick(editable);
            }
        });
    }

    /**
     * 设置评论框点击事件，如没设置，则默认处理，弹出评论当前资讯的输入框
     *
     * @param mCommentTvClickListener
     */
    public void setOnWriteCommentTextViewClickListener(OnClickListener mCommentTvClickListener) {
        this.mCommentTvClickListener = mCommentTvClickListener;
    }

    public void setDefaultHint(String defaultHint) {
        this.defaultHint = defaultHint;
    }

    /**
     * 设置回复数，count > 0，才显示
     */
    public void setReviewCount(int count) {
        if (isHideActionButton)
            return;
        if (mCommentImv.getVisibility() == GONE)
            return;
        if (count > 0) {
            mCommentCountTv.setVisibility(View.VISIBLE);
            if (count < maxCommentCount) {
                mCommentCountTv.setText(String.valueOf(count));
            } else {
                String maxCount = (maxCommentCount - 1) + "+";
                mCommentCountTv.setText(maxCount);
            }
        } else {
            mCommentCountTv.setVisibility(View.GONE);
        }
    }

    /**
     * 评论数最大显示数
     *
     * @param maxCommentCount
     */
    public void setMaxCommentCount(int maxCommentCount) {
        this.maxCommentCount = maxCommentCount;
    }

    /**
     * 隐藏回复、收藏、分享按钮，不支持单独隐藏
     */
    public void hideActionButton() {
        isHideActionButton = true;
        mCommentImv.setVisibility(GONE);
        mCommentCountTv.setVisibility(GONE);
        mShareImv.setVisibility(GONE);
        mCollectBtn.setVisibility(GONE);
        LayoutParams lp = (LayoutParams) mWriteCommentTv.getLayoutParams();
        lp.rightMargin = lp.leftMargin;
        mWriteCommentTv.setLayoutParams(lp);
    }

    /**
     * 隐藏顶部分割线
     */
    public void hideTopDivider() {
        topDivider.setVisibility(GONE);
    }

    /**
     * 设置点赞图片
     *
     * @param unCollectResId
     * @param collectResId
     */
    public void setCollectDrawables(@DrawableRes int unCollectResId, @DrawableRes int collectResId) {
        mCollectBtn.setCollectDrawables(unCollectResId, collectResId);
    }

    /**
     * 设置回复按钮
     *
     * @param resId
     */
    public void setCommentIcon(@DrawableRes int resId) {
        mCommentImv.setImageResource(resId);
    }

    /**
     * 设置分享按钮
     *
     * @param resId
     */
    public void setShareIcon(@DrawableRes int resId) {
        mShareImv.setImageResource(resId);
    }

    /**
     * 设置是否显示分享按钮
     *
     * @param isVisible
     */
    public void setShowShareIcon(boolean isVisible) {
        if (isVisible) {
            mShareImv.setVisibility(VISIBLE);
        } else {
            mShareImv.setVisibility(GONE);
        }
    }

    /**
     * 设置输入栏提示文字的背景
     *
     * @param resId
     */
    public void setWriteCommentTextViewBackground(int resId) {
        mWriteCommentTv.setBackgroundResource(resId);
    }

    /**
     * 设置输入栏提示文字的背景
     *
     * @param color
     */
    public void setWriteCommentTextColor(int color) {
        mWriteCommentTv.setTextColor(color);
    }

    /**
     * 设置收藏状态
     *
     * @param is_favorite
     */
    public void setCollectState(boolean is_favorite) {
        mCollectBtn.setCollectState(is_favorite);
    }

    /**
     * 是否已收藏
     *
     * @return
     */
    public boolean isCollected() {
        return mCollectBtn.isCollected();
    }

    /**
     * 点击评论
     */
    public void performCommentClick() {
        mCommentImv.performClick();
    }

    @Override
    public void onClick(View v) {
        if (v == mWriteCommentTv) {
            if (mCommentTvClickListener != null) {
                mCommentTvClickListener.onClick(v);
            } else {
                mCommentDialog.showWithHint(defaultHint);
            }
        }
    }

    public void setWriteHint(String hint) {
        mWriteCommentTv.setText(hint);
    }

    /**
     * 显示输入框对话框
     *
     * @param hint 提示
     */
    public void showEditTextWithHint(String hint) {
        mCommentDialog.showWithHint(hint);
    }

    /**
     * 显示输入框和表情
     *
     * @param hint
     */
    public void showEditTextWithHintAndEmoji(String hint) {
        mCommentDialog.showEditTextWithHintAndEmoji(hint);
    }

    /**
     * 关闭输入对话框
     */
    public void dismissDialog() {
        mCommentDialog.dismiss();
    }

    /**
     * 清除已经输入的文字
     */
    public void clearInputedText() {
        mCommentEdt.getText().clear();
    }

    /**
     * 添加文字变化监听
     *
     * @param watcher
     */
    public void addTextChangedListener(TextWatcher watcher) {
        mCommentEdt.addTextChangedListener(watcher);
    }

    /**
     * 设置输入框文字
     *
     * @param text
     */
    public void setText(String text) {
        mCommentEdt.setText(text);
    }

    /**
     * 获取输入框文字
     *
     * @return
     */
    public Editable getText() {
        return mCommentEdt.getText();
    }

    /**
     * 设置输入框光标位置
     *
     * @param selection
     */
    public void setSelection(int selection) {
        mCommentEdt.setText(selection);
    }

    /**
     * 设置提交按钮是否可按
     *
     * @param enable
     */
    public void setSubmitEnable(boolean enable) {
        mSubmitBtn.setEnabled(enable);
    }

    /**
     * 表情栏展开关闭监听
     *
     * @param listener
     */
    public void setOnEmojiViewShowListener(EmojiLayout.OnEmojiViewShowListener listener) {
        this.mEmojiViewShowListener = listener;
    }

    public ImageView getShareImageView() {
        return mShareImv;
    }

    public ImageView getEmojiToggleImageView() {
        return mEmojiToggleImv;
    }

    public CollectButton getCollectButton() {
        return mCollectBtn;
    }

    public Button getSubmitButton() {
        return mSubmitBtn;
    }

    public CommonInputDialog getCommentDialog() {
        return mCommentDialog;
    }

    public EditText getCommentEditText() {
        return mCommentEdt;
    }

    public ImageView getCommentImageView() {
        return mCommentImv;
    }

    public TextView getCommentCountTextView() {
        return mCommentCountTv;
    }

    public TextView getWriteCommentTextView() {
        return mWriteCommentTv;
    }

    public View getTopDivider() {
        return topDivider;
    }

    public LinearLayout getActionLayout() {
        return mActionLayout;
    }

    /**
     * 获取面板操作布局，可以自定义面板上面的子View
     *
     * @return 面板操作布局
     */
    public LinearLayout getPanelActionLayout() {
        return mCommentDialog.getPanelActionLayout();
    }

    /**
     * 获取面板布局,可以自定义面板
     *
     * @return 面板布局
     */
    public FrameLayout getPanelLayout() {
        return mCommentDialog.getPanelLayout();
    }

    /**
     * 生成操作视图的LayoutParams
     *
     * @param context
     * @return
     */
    public static LayoutParams generateActionLayoutParams(Context context) {
        int width = DeviceUtils.dip2px(context, 48);
        int height = DeviceUtils.dip2px(context, 48);
        return new LayoutParams(width, height);
    }

    /**
     * 点击提交监听
     */
    public interface OnSubmitClickListener {
        void onSubmitClick(Editable editable);
    }
}
