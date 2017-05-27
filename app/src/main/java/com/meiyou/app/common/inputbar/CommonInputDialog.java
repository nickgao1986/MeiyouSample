package com.meiyou.app.common.inputbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lingan.seeyou.ui.view.widget.EmojiLayout;

import biz.LinganDialog;
import nickgao.com.framework.utils.BeanManager;
import nickgao.com.meiyousample.R;


/**
 * 输入对话框
 */
public class CommonInputDialog extends LinganDialog implements DialogInterface.OnDismissListener {
    private LinearLayout mPanelActionLayout;
    private FrameLayout mPanelLayout;
    private EditText mEditText;
    private Button mSubmitBtn;
    private EmojiLayout mEmojiLayout;
    private ImageView mEmojiToggleImv;
    private Activity mActivity;

    private View mNightMaskView;//夜间模式蒙层

    public CommonInputDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = (Activity) context;
        setContentView(R.layout.layout_common_edit_dialog);
        setOnDismissListener(this);
        initWindows();
        initViews();
    }

    /**
     * 初始化对话框属性
     */
    private void initWindows() {
        Window window = getWindow();
        if (window == null)
            return;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0);
        }
        window.setDimAmount(0);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        window.setBackgroundDrawable(dw);
        window.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth();
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 初始化属性
     */
    private void initViews() {
        mPanelActionLayout = (LinearLayout) findViewById(R.id.common_edit_dialog_panel_action_layout);
        mPanelLayout = (FrameLayout) findViewById(R.id.common_edit_dialog_panel_layout);
        mEditText = (EditText) findViewById(R.id.common_edit_dialog_edit_text);
        mSubmitBtn = (Button) findViewById(R.id.common_edit_dialog_submit_btn);
        mEmojiLayout = (EmojiLayout) findViewById(R.id.common_edit_dialog_emoji_layout);
        mEmojiToggleImv = (ImageView) findViewById(R.id.common_edit_dialog_emoji_toggle_imv);
        mEmojiLayout.setEtContent(mEditText);
        mEmojiLayout.setActivity(mActivity);
        mEmojiLayout.setAnimation(false);
        mEmojiLayout.setShowCustomExpression(false);
        mEmojiLayout.setIbEmojiKeyboard(mEmojiToggleImv);

        mNightMaskView = findViewById(R.id.common_edit_dialog_night_mask_view);
    }

    public ImageView getEmojiToggleImv() {
        return mEmojiToggleImv;
    }

    public EmojiLayout getEmojiLayout() {
        return mEmojiLayout;
    }

    public Button getSubmitBtn() {
        return mSubmitBtn;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void showEmoji() {
        mEmojiLayout.show();
    }

    /**
     * 显示评论输入框
     */
    public void showWithHint(String hint) {
        mEditText.setHint(hint);
        mEditText.requestFocus();
        customShow(true);
    }

    public void showEditTextWithHintAndEmoji(String hint) {
        mEditText.setHint(hint);
        mEmojiLayout.show();
        customShow(false);
    }

    /**
     * 显示对话框
     *
     * @param isShowKeyBoard 是否显示键盘
     */
    private void customShow(boolean isShowKeyBoard) {
        Window window = getWindow();
        if (window != null) {
            if (isShowKeyBoard) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
        if (BeanManager.getUtilSaver().getIsNightMode(getContext())) {
            mNightMaskView.setVisibility(View.VISIBLE);
        } else {
            mNightMaskView.setVisibility(View.GONE);
        }
        show();
    }

    public FrameLayout getPanelLayout() {
        return mPanelLayout;
    }

    public LinearLayout getPanelActionLayout() {
        return mPanelActionLayout;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mEmojiLayout.hideEmojiView();
    }
}
