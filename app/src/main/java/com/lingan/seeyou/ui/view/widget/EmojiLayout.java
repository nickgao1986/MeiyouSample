package com.lingan.seeyou.ui.view.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.YiPageIndicator;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.ExpressionController;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.meiyousample.utils.model.EmojiModel;
import nickgao.com.meiyousample.utils.model.ExpressionConfigModel;
import nickgao.com.meiyousample.utils.model.ExpressionSubModel;
import nickgao.com.okhttpexample.view.LoaderImageView;


public class EmojiLayout extends RelativeLayout implements OnClickListener {

    private static final String TAG = "EmojiLayout";
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    /**
     * 主界面View
     */
    public View mainView;

    private OnEmojiViewShowListener mShowListener;
    /**
     * 显示表情页的viewpager
     */
    private ExpressionViewPager vpEmoji;
    public ExpressionViewPagerAdapter pageAdapter;
    //游标
    private YiPageIndicator indicator;

    /**
     * 表情框下方圆游标点数组
     */
    private ImageView[] pointViews;

    /**
     * 表情区域
     */
    private View emojiView;

    /**
     * 输入框
     */
    private EditText etContent;
    /**
     * 当前显示的表情类别
     */
    public ExpressionConfigModel expressionConfigModel;

    /**
     * 当前表情页
     */
    private int currentPage;

    //emoji个数
    private int mEmojiCount;
    //自定义表情个数
    private int mExpressionCount;


    /**
     * 是否能输入表情  默认true
     */
    private boolean isCanInputEmoji = true;

    /**
     * 不能输入表情提示语
     */
    private String cantInputTip;

    /**
     * 表情预览弹出窗
     */
    //private ExpressionPopupView expressionPopupView;
    /**
     * 是否长按显示表情预览弹出窗
     */
    private boolean isShowFacePopwin = false;

   /* private HorizontalScrollView faceMenuHs;
    private LinearLayout faceMenuLayout;
    private RelativeLayout rl_bottom;
    private LinearLayout addFaceLayout, delFaceLayout;*/

    public EmojiLayout(Context context) {
        super(context);
        this.context = context;
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

   /* public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
        mListener = listener;
    }*/

    public void setOnEmojiViewShowListener(OnEmojiViewShowListener listener) {
        mShowListener = listener;
    }

    private boolean bShowCustomExpression = false;

    /**
     * 是否显示自定义表情
     *
     * @param flag
     */
    public void setShowCustomExpression(boolean flag) {
        bShowCustomExpression = flag;
        bShowCustomExpression = false;
        if (!bShowCustomExpression) {
            initBottomMenu();
        }
    }


    private boolean bShowCustomExpressionInEdit = true;

    /**
     * 自定义表情是否显示在编辑框
     *
     * @param flag
     */
    public void setShowCustomExpressionInEdit(boolean flag) {
        bShowCustomExpressionInEdit = flag;
    }

    /**
     * 设置点击自定义表情的监听
     *
     * @param listener
     */
    public void setOnItemCustomExpressionClickListener(OnItemCustomExpressionClickListener listener) {
        onItemCustomExpressionClickListener = listener;
    }

    private OnItemCustomExpressionClickListener onItemCustomExpressionClickListener;

    public interface OnItemCustomExpressionClickListener {
        public void OnItemClic(ExpressionSubModel expressionModel);
    }

    private OnItemEmojiClickListener onItemEmojiClickListener;

    public interface OnItemEmojiClickListener {
        void onEmojiClick(EmojiModel emojiModel);
    }

    public void setOnItemEmojiClickListener(OnItemEmojiClickListener onItemEmojiClickListener) {
        this.onItemEmojiClickListener = onItemEmojiClickListener;
    }

    private boolean bAnimation = false;

    public void setAnimation(boolean animation) {
        bAnimation = animation;
    }

    /**
     * 显示监听
     */
    public interface OnEmojiViewShowListener {
        void onShow();

        void onHide();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
/*
    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        if(operationKey==ExtendOperationController.OperationKey.EXPRESSION_REFRESH){
            pageAdapter = null;
            initBottomMenu();
            vpEmoji.setCurrentItem(mCurrentIndex);
        }
    }*/

    private ImageView ibEmoji;

    /**
     * 帖子回复，表情和输入法切换
     *
     * @param imageButton
     */
    public void setIbEmojiKeyboard(ImageView imageButton) {
        this.ibEmoji = imageButton;
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handleIbEmojiClick();
                show();
            }
        });

    }


    public void show() {
        if (emojiView.getVisibility() == View.VISIBLE) {
            return;
        }
        emojiView.setVisibility(View.VISIBLE);
        if (bAnimation) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, emojiView.getHeight(), 0);
            translateAnimation.setDuration(250);
            emojiView.startAnimation(translateAnimation);
        }
        emojiView.bringToFront();
        if (null != mShowListener) {
            mShowListener.onShow();
        }
    }


    public void hide(final boolean isShowKeyboard) {
        if (emojiView.getVisibility() != View.VISIBLE) {
            return;
        }

        if (bAnimation) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, emojiView.getHeight());
            translateAnimation.setDuration(250);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    emojiView.setVisibility(View.GONE);
                    if (null != mShowListener) {
                        mShowListener.onHide();
                    }
                    if (isShowKeyboard) {
                        DeviceUtils.showkeyboard(activity, etContent);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            emojiView.startAnimation(translateAnimation);
        } else {
            emojiView.setVisibility(View.GONE);
            if (null != mShowListener) {
                mShowListener.onHide();
            }
            if (isShowKeyboard)
                DeviceUtils.showkeyboard(activity, etContent);
        }
    }


    public void handleIbEmojiClick() {
        try {
            if (!isCanInputEmoji) {
                return;
            }
            if (emojiView.getVisibility() == View.VISIBLE) {
                etContent.requestFocus();
                hide(true);
            } else {
                if (null != activity && DeviceUtils.isShowkeyboard(activity)) {
                    DeviceUtils.hideKeyboard(activity, etContent);
                }
                showExpression(ExpressionController.getInstance().getPageCount(), 0);
                //showSystemFace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            if (view == null){
                view = activity.getCurrentFocus();
            }
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setEtContent(final EditText etContent) {
        try {
            this.etContent = etContent;
            String content = etContent.getEditableText().toString();
            mEmojiCount = EmojiConversionUtil.getInstace().getEmojiCount(content);
            mExpressionCount = ExpressionController.getInstance().getExpressionCount(content);
            etContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideEmojiView();
                }
            });
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        String content = editable.toString();
                        mEmojiCount = EmojiConversionUtil.getInstace().getEmojiCount(content);
                        mExpressionCount = ExpressionController.getInstance().getExpressionCount(content);
                        //LogUtils.d(TAG,"afterTextChanged mEmojiCount:"+mEmojiCount+"->mExpressionCount:"+mExpressionCount+"--length:" + editable.length());
                        if (mEmojiCount + mExpressionCount > EmojiConversionUtil.LIMIT_NUM) {
                            handlePressDelete();
                           /* String text = EmojiConversionUtil.getInstace().filterEmoji(editable.toString());
                            etContent.setText(EmojiConversionUtil.getInstace().getExpressionString(context, text));*/
                            ToastUtils.showToast(context, "最多只能输入" + EmojiConversionUtil.LIMIT_NUM + "个表情哦");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /**
     * 隐藏表情选择框
     */
    public boolean hideEmojiView() {
        // 隐藏表情选择框
        try {
            if (null == emojiView) {
                return false;
            }
            if (emojiView.getVisibility() == View.VISIBLE) {
                emojiView.setVisibility(View.GONE);
                if (null != mShowListener) {
                    mShowListener.onHide();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmojiViewShowing() {
        if (emojiView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ViewFactory viewFactory = ViewFactory.from(context);
        inflater = viewFactory.getLayoutInflater();//.//;LayoutInflater.from(context);
        mainView = this;
        vpEmoji = (ExpressionViewPager) findViewById(R.id.vp_contains);

        emojiView = findViewById(R.id.ll_emojichoose);
     /*   faceMenuHs = (HorizontalScrollView) findViewById(R.id.faceMenuHs);
        faceMenuLayout = (LinearLayout) findViewById(R.id.faceMenuLayout);*/
        indicator = (YiPageIndicator) findViewById(R.id.indicator);
     /*   addFaceLayout = (LinearLayout) findViewById(R.id.addFaceLayout);
        delFaceLayout = (LinearLayout) findViewById(R.id.delFaceLayout);
        rl_bottom = (RelativeLayout)findViewById(R.id.rl_bottom);*/
        // emojiView.setBackgroundResource(R.drawable.apk_all_white); //布局已经设置 不需要再设置

        //SkinEngine.getInstance().setViewBackground(getContext(), emojiView, R.drawable.apk_all_white);
        initBottomMenu();
        setListener();
    }

    public View getEmojiView() {
        return emojiView;
    }

    private List<ExpressionConfigModel> listDatas;

    /**
     * 初始化表情菜单
     */
    private void initBottomMenu() {
        try {
            long time = System.currentTimeMillis();
            //初始化表情菜单：自定义表情等
            listDatas = new ArrayList<>();
            List<ExpressionConfigModel> listTemp = ExpressionController.getInstance().getMineExpression();
            if (listTemp == null || listTemp.size() == 0) {
                listTemp = new ArrayList<>();
            }
            //LogUtils.d(TAG,"获取到我的表情："+listTemp.size());
            listDatas.addAll(listTemp);

            //加入默认表情
            ExpressionConfigModel modelDefault = new ExpressionConfigModel();
            modelDefault.name = "默认";
            modelDefault.id = 0;
            listDatas.add(0, modelDefault);

            //计算viewpager页数
            ExpressionController.getInstance().handleCaculateData(context, listDatas, !bShowCustomExpression);
            final int size = ExpressionController.getInstance().getPageCount();

            //不显示自定义表情
            //LogUtils.d(TAG, "paget count:" + size);
      /*  if(bShowCustomExpression)
            rl_bottom.setVisibility(View.VISIBLE);
        else
            rl_bottom.setVisibility(View.GONE);
        //移除所有
        faceMenuLayout.removeAllViews();*/
            //加入
            for (int index = 0; index < listDatas.size(); index++) {
                final ExpressionConfigModel model = listDatas.get(index);
                final int i = index;
                View view = inflater.inflate(R.layout.custom_face_menu, null);
                final LoaderImageView iv_expression = (LoaderImageView) view.findViewById(R.id.iv_expression);
                //显示图标
                if (model.id == 0) {
                    iv_expression.setImageResource(R.drawable.apk_ic_tata_default_brow);
                } else {
                    //ImageLoader.getInstance().displayImage(context, iv_expression, model.name_image, 0, 0, 0, 0, false, DeviceUtils.dip2px(context, 36), DeviceUtils.dip2px(context, 36), null);
                /*ImageLoader.getInstance().loadImage(context, model.name_image, 0, 0, new ImageLoader.onCallBack() {
                    @Override
                    public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                        if (bitmap != null) {
                            if (!ExpressionController.getInstance().isLocalExsited(context, model.id)) {
                                Drawable drawable = BitmapUtil.toGrayscale(bitmap);
                                iv_expression.setImageDrawable(drawable);
                            } else {
                                iv_expression.setImageBitmap(bitmap);
                            }
                            //iv_expression.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFail(String url, Object... obj) {

                    }

                    @Override
                    public void onProgress(int total, int progess) {

                    }

                    @Override
                    public void onExtend(Object... object) {

                    }
                });*/
                }
                //faceBtn.setText(model.name);
                //处理点击
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //防止重复点击
                        if (expressionConfigModel.id == model.id)
                            return;
                        //默认皮肤,且是从其他地方切换过来的，
                        if (model.id == 0) {
                            showExpression(size, 0);
                        } else {
                        /*//已下载，显示
                        if (ExpressionController.getInstance().isLocalExsited(context, model.id)) {
                            int index = ExpressionController.getInstance().getViewPagerSelectIndex(i);
                            //LogUtils.d(TAG, "--index:" + index + "--i:" + i + "--size:" + size);
                            showExpression(size, index);
                            //未下载，跳转详情页
                        } else {
                            YouMentEventUtils.getInstance().countEvent(context, "tjbq-bqxq", YouMentEventUtils.NOTHING, "");
                            //UtilEventDispatcher.getInstance().jumpToExpressionDetailActivity(context, model.id);
                            return;
                        }*/
                        }
                        //记录当前皮肤
                        expressionConfigModel = model;
                        //resetMenuStatsu(i);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtils.dip2px(context, 60), LinearLayout.LayoutParams.WRAP_CONTENT);
                //faceMenuLayout.addView(view, params);
            }

            //先显示默认
            showExpression(size, 0);
            expressionConfigModel = modelDefault;
            //resetMenuStatsu(0);
            handlePageSelected(0);
            long time2 = System.currentTimeMillis();
            long interval = time2 - time;
            //LogUtils.d(TAG,"initBottomMenu time:"+interval);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showExpression(int pageCount, int select_index) {
        try {
            if (pageAdapter == null) {
                pageAdapter = new ExpressionViewPagerAdapter(this, pageCount);
                vpEmoji.setAdapter(pageAdapter);
                vpEmoji.setCurrentItem(select_index);
            } else {
                vpEmoji.setCurrentItem(select_index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* private void resetMenuStatsu(int currentIndex){
        try{
            int size = faceMenuLayout.getChildCount();
            for(int i=0;i<size;i++){
                View child = faceMenuLayout.getChildAt(i);
                RelativeLayout rl_menu = (RelativeLayout)child.findViewById(R.id.rl_menu);
                if(i==currentIndex){
                    rl_menu.setBackgroundResource(R.drawable.apk_all_whitebg_up);
                }else{
                    rl_menu.setBackgroundResource(R.drawable.apk_all_white);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/


    private int mCurrentIndex = 0;

    private void setListener() {
     /*   addFaceLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //YouMentEventUtils.getInstance().countEvent(context,"bqk-bqsc",YouMentEventUtils.NOTHING,"");
                //添加表情
                //UtilEventDispatcher.getInstance().jumpToExpressionActivity(context);
            }
        });
        delFaceLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //回退
                handlePressDelete();
            }
        });
*/
        vpEmoji.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handlePageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void handlePageSelected(int position) {
        try {

            mCurrentIndex = position;
            //tab selectini
            int index = ExpressionController.getInstance().getTabIndex(position);
            //resetMenuStatsu(index);
            //indicator select
            int cursorIndex = ExpressionController.getInstance().getIndicatorIndex(position);
            int cursorTotal = ExpressionController.getInstance().getIndicatorTotalCount(position);
            indicator.setTotalPage(cursorTotal);
            indicator.setCurrentPage(cursorIndex);

            //tab index:0--cursorTotal:4--cursorIndex:0
            //LogUtils.d(TAG,"position:"+position+"tab index:"+index+"--cursorTotal:"+cursorTotal+"--cursorIndex:"+cursorIndex);
            expressionConfigModel = listDatas.get(index);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除输入框中的文本或图片
     */
    public void handlePressDelete() {
        try {
            String content = etContent.getText().toString().trim();
            //LogUtils.d(TAG,"handlePressDelete content:"+content);

            // if(ExpressionController.getInstance().ha)
            int selection = etContent.getSelectionStart();
            String text = etContent.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(0, selection);
                if (text2.endsWith("]")) {
                    int start = text2.lastIndexOf("[");
                    int end = selection;
                    etContent.getText().delete(start, end);
                    return;
                }
                etContent.getText().delete(selection - 1, selection);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 显示表情预览
     */
    /*public void showExpressionPreview(ExpressionModel expressionModel, Rect viewRect) {
        if (expressionPopupView != null) {
            if (expressionPopupView.isSameExpressionModel(expressionModel)) {
                return;
            }
            expressionPopupView.dismiss();
            expressionPopupView = null;
        }
        expressionPopupView = new ExpressionPopupView(context, expressionModel);
        expressionPopupView.showPopupView(mainView, viewRect, DeviceUtils.getScreenVisiableHeight(activity));
    }
*/

    /**
     * 关闭表情预览弹出窗
     */
    public void dismissExpressionPreview() {
        /*if (expressionPopupView != null) {
            expressionPopupView.dismiss();
            expressionPopupView = null;
        }*/
    }

    /**
     * 设置是否可以预览表情
     */
    public void setIsShowExpressionPreview(boolean isShowFacePopwin) {
        this.isShowFacePopwin = isShowFacePopwin;
        vpEmoji.setTouchIntercept(!isShowFacePopwin);
    }

    /**
     * 是否可以显示预览表情
     *
     * @return
     */
    public boolean canShowExpressionPreview() {
        return isShowFacePopwin;
    }

    public ExpressionConfigModel getExpressionConfigModel() {
        return expressionConfigModel;
    }


    private int pageCount;

    /**
     * 表情圆点指示显示设置
     */
    private void setcurrentPoint(int position) {
        if (position < 0 || position > pageCount - 1 || currentPage == position) {
            return;
        }
        currentPage = position;
        pointViews[currentPage].setEnabled(true);
        pointViews[position].setEnabled(false);
        currentPage = position;
    }

    /**
     * 点击emoji
     *
     * @param emoji
     */
    public void handleOnItemClick(EmojiModel emoji) {
        if (emoji.getId() == R.drawable.btn_emoji_del_selector) {
            handlePressDelete();
        } else {
            if (!TextUtils.isEmpty(emoji.getCharacter())) {
                SpannableString spannableString = EmojiConversionUtil.getInstace().convertEmojiToSpannableString(getContext(), emoji.getId(), emoji.getCharacter());
                int selection = etContent.getSelectionStart();
                Editable editable = etContent.getEditableText();
                if (selection < 0 || selection >= editable.length()) {
                    editable.append(spannableString);
                } else {
                    editable.insert(selection, spannableString);
                }
                if (onItemEmojiClickListener != null) {
                    onItemEmojiClickListener.onEmojiClick(emoji);
                }
            }
        }
        etContent.requestFocus();
    }

    /**
     * 点击自定义表情
     *
     * @param model
     */
    public void handleOnItemClick(ExpressionSubModel model) {
        try {
            if (bShowCustomExpression) {
                int selection = etContent.getSelectionStart();
                Editable editable = etContent.getEditableText();
                if (selection < 0 || selection >= editable.length()) {
                    editable.append(model.name);
                } else {
                    editable.insert(selection, model.name);
                }
            }
            if (onItemCustomExpressionClickListener != null) {
                onItemCustomExpressionClickListener.OnItemClic(model);
            }

            etContent.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCantInputTip(String cantInputTip) {
        this.cantInputTip = cantInputTip;
    }

    public void setCanInputEmoji(boolean isCanInputEmoji) {
        this.isCanInputEmoji = isCanInputEmoji;
    }
}
