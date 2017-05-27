package nickgao.com.meiyousample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.lingan.seeyou.ui.view.CustomEditText;
import com.lingan.seeyou.ui.view.publish.PublishEmojiPanelLayout;
import com.lingan.seeyou.ui.view.publish.PublishFourSwitchBar;
import com.lingan.seeyou.ui.view.publish.PublishThreeSwitchBar;
import com.lingan.seeyou.ui.view.publish.PublishTopicWatchLayout;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.widget.EmojiLayout;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.ShuoshuoModel;

/**
 * Created by gaoyoujian on 2017/5/17.
 */

public class TestShoushouActivity extends PeriodBaseActivity{


    private static final String TAG = "PublishShoushouActivity";

    private Activity mActivity;
    private ShuoshuoModel shuoshuoModel;
    //表情
    private EmojiLayout emojiLayout;
    private CustomEditText etContent;
    private PublishTopicWatchLayout mWatchLayout; //根布局
    private ScrollView svContent;
    private RelativeLayout rlAsk, rlAnonymous;
    private View dividerAskAnonymous;
    //带文字底部操作栏（Panel&键盘未弹出时底部的bar）


    private PublishThreeSwitchBar mThreeSwitchBar;
    //不带文字底部操作栏，有收起键（Panel&键盘弹出时底部的bar）
    private PublishFourSwitchBar mFourSwitchBar;
    private LinearLayout llKbEmojiSwitch;
    private ImageView ivKbEmoji;
    //表情
    private PublishEmojiPanelLayout llEmojiPanel;

    private int mStatusBarHeight;  //状态栏高度
    private int mScreenHeight;  //屏高
    private int PHOTO_COLUMN_COUNT = 4;
    private int mPhotoWidth;  //单张图片的宽度

    private boolean hasDraft = false;
    private boolean isDraftChange = false;
    private boolean bShowKeyboard = false;


    @Override
    protected int getLayoutId() {
        return R.layout.test_publish_shoshou;
    }



    private void initUI() {

        //getTitleBar().setLeftTextViewString(R.string.cancel);
        getTitleBar().setRightTextViewString(R.string.publish);
        getTitleBar().setTitle(R.string.publish_shuoshuo);
        //getTitleBar().setLeftButtonRes(-1);

        //字数限制
        etContent = (CustomEditText) findViewById(R.id.publish_et_content);
        Context context = getApplicationContext();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)etContent.getLayoutParams();
        int titlebarHeight = this.getResources().getDimensionPixelSize(R.dimen.title_height);

        int height = mScreenHeight -titlebarHeight - mStatusBarHeight - DeviceUtils.dip2px(this,140);
//        layoutParams.height = height;
        etContent.setMinHeight(height);
        //头像限制
        //MAX_PIC = 20;

       // ivEmoji = (ImageView) findViewById(R.id.ivEmoji);

        setListener();

    }

    /**
     * 初始化底部Bar
     */
    private void initBottomBar() {
        //Panel&键盘未弹起时底部的bar
//        mThreeSwitchBar = (PublishThreeSwitchBar) findViewById(R.id.no_keyboard_bar);
//        mThreeSwitchBar.setPublishTopicWatchLayout(mWatchLayout);
//        LinearLayout llEmojiSwitch = (LinearLayout) findViewById(R.id.ll_emoji_switch);
//        llEmojiSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onEmojiSwitchClick();
//            }
//        });

        //Panel&键盘弹起时底部的bar
        mFourSwitchBar = (PublishFourSwitchBar) findViewById(R.id.keyboard_bar);
        mFourSwitchBar.setPublishTopicWatchLayout(mWatchLayout);
        ivKbEmoji = (ImageView) findViewById(R.id.iv_kb_emoji);
        llKbEmojiSwitch = (LinearLayout) findViewById(R.id.ll_kb_emoji_switch);
        llKbEmojiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKbEmojiSwitchClick();
            }
        });

        LinearLayout llHideKeyboard = (LinearLayout) findViewById(R.id.ll_hide_keyboard);
        llHideKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllBoard();
            }
        });
    }

    /**
     * 隐藏键盘和表情键盘
     */
    private void hideAllBoard(){
        if (mWatchLayout.isShowEmojiPanel()) {
            setEmojiPanelVisibility(false, true);
        } else {
            DeviceUtils.hideKeyboard(mActivity);
        }
    }

    /**
     * 表情开关被点击
     */
    private void onEmojiSwitchClick() {
        if (getCurrentFocus() != etContent) {
            etContent.setFocusableInTouchMode(true);
            etContent.requestFocus();
        }
        setEmojiPanelVisibility(true, true);
    }

    /**
     * 设置表情Panel是否可见
     *
     * @param visible 表情panel是否可见
     * @param requestLayout 未触发键盘弹出或收起，需要传true
     */
    private void setEmojiPanelVisibility(boolean visible, boolean requestLayout) {
        if(visible){
            SkinManager.getInstance().setDrawable(ivKbEmoji, R.drawable.selector_btn_keyboard);
        }else{
            SkinManager.getInstance().setDrawable(ivKbEmoji, R.drawable.selector_btn_emoji);
        }
        mWatchLayout.setShowEmojiPanel(visible);

        if(requestLayout){
            mFourSwitchBar.requestLayout();
            mThreeSwitchBar.requestLayout();
            llEmojiPanel.requestLayout();
        }
    }

    /**
     * 键盘或表情Panel弹出时表情开关被点击
     */
    private void onKbEmojiSwitchClick() {
        boolean isShowEmojiPanel = mWatchLayout.isShowEmojiPanel();
        if (isShowEmojiPanel) {
            //当前可见，要隐藏，并弹键盘
            setEmojiPanelVisibility(false, false);
            if (getCurrentFocus() != null) {
                DeviceUtils.showkeyboard(mActivity, getCurrentFocus());
            }
        } else {
            //当前不可见，要展示，并隐藏键盘
            setEmojiPanelVisibility(true, false);
            DeviceUtils.hideKeyboard(mActivity);
        }
    }

    private boolean bOverCount = false;


    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(this, this.getResources(), this.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        bUseCustomAnimation = true;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_animation_none);
        }
        mScreenHeight = DeviceUtils.getScreenHeight(this);
        mStatusBarHeight = DeviceUtils.getStatusBarHeight(this);
        initSkin();
        super.onCreate(savedInstanceState);
        shuoshuoModel = new ShuoshuoModel(getApplicationContext());
        initView();
        initUI();

        initBottomBar();
        initEmoji();
        hideAllBoard();
    }

    /**
     * 初始化表情相关
     */
    private void initEmoji() {
        llEmojiPanel = (PublishEmojiPanelLayout) findViewById(R.id.ll_emoji_panel);
        llEmojiPanel.setPublishTopicWatchLayout(mWatchLayout);

        emojiLayout = (EmojiLayout) findViewById(R.id.emojiLayout);
        emojiLayout.setEtContent(etContent);
        etContent.setOnClickListener(null);  //清掉emojiLayout.setEtContent里设置的错误clickListener
        emojiLayout.setActivity(this);
        emojiLayout.show();
    }



    private void initView() {
        svContent = (ScrollView) findViewById(R.id.scrollview);
        mWatchLayout = (PublishTopicWatchLayout) findViewById(R.id.ll_keyboard_watch);
        mWatchLayout.setOnKeyboardStatusChangeListener(new PublishTopicWatchLayout.OnKeyboardStatusChangeListener() {
            @Override
            public void onChanged(boolean isShowing) {
                if(getCurrentFocus() == etContent){
                    if(isShowing){
                        SkinManager.getInstance().setDrawable(ivKbEmoji, R.drawable.selector_btn_emoji);
                    }else{
                        SkinManager.getInstance().setDrawable(ivKbEmoji, R.drawable.selector_btn_keyboard);
                    }
                }
            }
        });
    }

    private void setListener() {

        // 字数限制
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i2, int i3) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,
                                      int i3) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
                // 得到说说内容
                LogUtils.i(TAG, "内容为：" + editable.toString());
                // shuoshuoModel.content = editable.toString();
                shuoshuoModel.setContent(getApplicationContext(), editable.toString());
                isDraftChange = true;
                LogUtils.i(TAG,
                        "---------->afterTextChanged postDelayed isDraftChange:"
                                + isDraftChange);
                checkPublishText();

            }
        });
    }

    private void checkPublishText() {
        if (shuoshuoModel.isEmpty()) {
            // textview1.setTextColor(Color.argb(255, 0, 255, 0)); //文字透明度
            getTitleBar().getRightTextView().setTextColor(
                    Color.argb(128, 255, 255, 255));
            // getTitleBar().getRightTextView().setTextColor(getResources().getColor(R.color.black_b));
        } else {
//			getTitleBar().getRightTextView().setTextColor(
//					getResources().getColor(R.color.white_a));
            SkinManager.getInstance().setTextColor(getTitleBar().getRightTextView(), R.color.white_a);
        }
    }





    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
