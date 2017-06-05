package nickgao.com.meiyousample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.lingan.seeyou.ui.view.CustomEditText;
import com.lingan.seeyou.ui.view.MeasureGridView;
import com.lingan.seeyou.ui.view.photo.OnSelectPhotoListener;
import com.lingan.seeyou.ui.view.photo.PhotoActivity;
import com.lingan.seeyou.ui.view.photo.PhotoController;
import com.lingan.seeyou.ui.view.photo.PreviewImageActivity;
import com.lingan.seeyou.ui.view.photo.model.PhotoConfig;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;
import com.lingan.seeyou.ui.view.photo.model.PreviewImageModel;
import com.lingan.seeyou.ui.view.publish.PublishEmojiPanelLayout;
import com.lingan.seeyou.ui.view.publish.PublishFourSwitchBar;
import com.lingan.seeyou.ui.view.publish.PublishThreeSwitchBar;
import com.lingan.seeyou.ui.view.publish.PublishTopicWatchLayout;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.widget.EmojiLayout;

import java.util.ArrayList;
import java.util.List;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.adapter.GridViewImageApdater;
import nickgao.com.meiyousample.controller.UserController;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.GridViewImageModel;
import nickgao.com.meiyousample.model.ShuoshuoModel;

import static nickgao.com.meiyousample.R.id.scrollview;

/**
 * Created by gaoyoujian on 2017/5/17.
 */

public class PublishShoushouActivity  extends PeriodBaseActivity{


    private static final String TAG = "PublishShoushouActivity";

    private MeasureGridView gridViewPhoto;
    private GridViewImageApdater gridViewImageApdater;
    private List<GridViewImageModel> listPhoto = new ArrayList<GridViewImageModel>();
    private List<PhotoModel> listSelectedPhoto = new ArrayList<PhotoModel>();
    private Activity mActivity;
    private LinearLayout linearGridViewPhoto;
    private ShuoshuoModel shuoshuoModel;
    private int MAX_PIC = 9;
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
    private LinearLayout llKbEmojiSwitch, llKbImageSwitch;
    private ImageView ivKbEmoji, ivKbImage;
    //表情
    private PublishEmojiPanelLayout llEmojiPanel;
    private int PHOTO_COLUMN_COUNT = 4;
    private int mPhotoWidth;  //单张图片的宽度
    private int mStatusBarHeight;  //状态栏高度
    private int mScreenHeight;  //屏高
    private boolean hasDraft = false;
    private boolean isDraftChange = false;
    private boolean bShowKeyboard = false;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_publish_shoshou;
    }



    private void initUI() {

        //getTitleBar().setLeftTextViewString(R.string.cancel);
        getTitleBar().setRightTextViewString(R.string.publish);
        getTitleBar().setTitle(R.string.publish_shuoshuo);
        //getTitleBar().setLeftButtonRes(-1);

        //字数限制
        etContent = (CustomEditText) findViewById(R.id.publish_et_content);
        Context context = getApplicationContext();

        //头像限制
        MAX_PIC = 20;

       // ivEmoji = (ImageView) findViewById(R.id.ivEmoji);

        linearGridViewPhoto = (LinearLayout) findViewById(R.id.linearGridViewPhoto);

        gridViewPhoto = (MeasureGridView) findViewById(R.id.gridViewPhoto);
        gridViewPhoto.setTouch(true);
        gridViewPhoto.setHorizontalFadingEdgeEnabled(false);
        gridViewPhoto.setVerticalFadingEdgeEnabled(false);
        gridViewPhoto.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridViewPhoto.setNumColumns(4);
        // gridViewPhoto.setHorizontalSpacing(4);
        gridViewPhoto.setVerticalSpacing(DeviceUtils.dip2px(context, 8));
        gridViewPhoto.setGravity(Gravity.CENTER);
        gridViewPhoto.setHorizontalScrollBarEnabled(false);
        gridViewPhoto.setVerticalScrollBarEnabled(false);

        gridViewImageApdater = new GridViewImageApdater(mActivity, listPhoto);
        gridViewPhoto.setAdapter(gridViewImageApdater);

        setListener();



//        if(emojiLayout!=null){
//            SkinManager.getInstance().setDrawableBackground(emojiLayout.getEmojiView(),R.drawable.apk_all_white);
//        }
    }

    /**
     * 初始化底部Bar
     */
    private void initBottomBar() {
        //Panel&键盘未弹起时底部的bar
        mThreeSwitchBar = (PublishThreeSwitchBar) findViewById(R.id.no_keyboard_bar);
        mThreeSwitchBar.setPublishTopicWatchLayout(mWatchLayout);
        LinearLayout llEmojiSwitch = (LinearLayout) findViewById(R.id.ll_emoji_switch);
        llEmojiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmojiSwitchClick();
            }
        });
        LinearLayout llImageSwitch = (LinearLayout) findViewById(R.id.ll_image_switch);
        llImageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSwitchClick();
            }
        });
        llImageSwitch.setVisibility(View.VISIBLE);

        LinearLayout llVoteSwitch = (LinearLayout) findViewById(R.id.ll_vote_switch);
        llVoteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onVoteSwitchClick();
            }
        });
        llVoteSwitch.setVisibility(View.GONE);

        //Panel&键盘弹起时底部的bar
        mFourSwitchBar = (PublishFourSwitchBar) findViewById(R.id.keyboard_bar);
        mFourSwitchBar.setPublishTopicWatchLayout(mWatchLayout);
        ivKbEmoji = (ImageView) findViewById(R.id.iv_kb_emoji);
        ivKbImage = (ImageView) findViewById(R.id.iv_kb_image);
        llKbEmojiSwitch = (LinearLayout) findViewById(R.id.ll_kb_emoji_switch);
        llKbEmojiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKbEmojiSwitchClick();
            }
        });
        llKbImageSwitch = (LinearLayout) findViewById(R.id.ll_kb_image_switch);
        llKbImageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSwitchClick();
            }
        });
        llKbImageSwitch.setVisibility(View.VISIBLE);


        LinearLayout llHideKeyboard = (LinearLayout) findViewById(R.id.ll_hide_keyboard);
        llHideKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllBoard();
            }
        });
    }

    private void resizeEditText() {
        int mItemWH = (DeviceUtils.getScreenWidth(context)-DeviceUtils.dip2px(context,8*5))/4;
        int photoPadding = DeviceUtils.dip2px(context, 8)*2;
        int titlebarHeight = DeviceUtils.dip2px(context, 44);
        int screenHeight = DeviceUtils.getScreenHeight(this);
        int photoLineCount = gridViewImageApdater.getCount() % PHOTO_COLUMN_COUNT  == 0 ? gridViewImageApdater.getCount() / PHOTO_COLUMN_COUNT : (gridViewImageApdater.getCount() / PHOTO_COLUMN_COUNT) + 1;
        int photoWidth = gridViewPhoto.getVisibility() == View.GONE ? 0 : mItemWH * photoLineCount + photoLineCount * DeviceUtils.dip2px(this, 8);
        int bottomHeight = DeviceUtils.dip2px(context,60);
        int waningLayoutHeight = DeviceUtils.dip2px(context,25);
        LogUtils.d("====mItemWH="+mItemWH+"photoWidth="+photoWidth);
        int minEditHeight = screenHeight - bottomHeight - titlebarHeight - waningLayoutHeight - mStatusBarHeight-photoWidth;
        LogUtils.d("====minEditHeight="+minEditHeight);
        this.etContent.setMinHeight(minEditHeight);
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

    private void onImageSwitchClick() {
        LogUtils.i(TAG, "----------->bShowKeyboard:" + bShowKeyboard);
        if (bShowKeyboard) {
            DeviceUtils.hideKeyboard(PublishShoushouActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    getPhoto();
                }
            }, 25);
        } else {
            getPhoto();
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

    //处理警告文字数目
    private void handleShowWarnCount() {
//        try {
//            String content = editContent.getText().toString();
//
//            int len = StringUtils.getCharLength(content);//字符
//            int yu = len / 2;
////			int mo = len%2;
//            int cha = mMaxTextCount - yu;
//            if (cha <= 10) {
//                tvWarnCount.setText(cha + "");
//                if (cha >= 0) {
//                    tvWarnCount.setTextColor(getResources().getColor(R.color.black_a));
//                } else {
//                    tvWarnCount.setTextColor(getResources().getColor(R.color.red_a));
//                }
//            } else {
//                tvWarnCount.setText("");
//            }
//            bOverCount = cha < 0;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

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
        initSkin();
        super.onCreate(savedInstanceState);
        shuoshuoModel = new ShuoshuoModel(getApplicationContext());
        initView();
        initUI();
//        initLogic();
//        startSaveDraftTimer();

        initBottomBar();
        initEmoji();
        mPhotoWidth = (DeviceUtils.getScreenWidth(mActivity) - (int) getResources().getDimension(R.dimen.space_xs) * (PHOTO_COLUMN_COUNT + 1)) / PHOTO_COLUMN_COUNT; //一行4张图
        mScreenHeight = DeviceUtils.getScreenHeight(this);
        mStatusBarHeight = DeviceUtils.getStatusBarHeight(this);
        resizeEditText();
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
        svContent = (ScrollView) findViewById(scrollview);
        svContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DeviceUtils.showkeyboard(PublishShoushouActivity.this,svContent);

                return false;
            }
        });
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




    private void getPhoto() {

        // 浮层
		/*
		 * ImageView iv = new ImageView(getApplicationContext());
		 * iv.setBackgroundColor(getResources()
		 * .getColor(R.color.color_translucent)); setCoverView(iv, true, true,
		 * false, Gravity.CENTER, new OnCoverDismissListener() {
		 *
		 * @Override public void onDismimss() { // To change body of implemented
		 * methods use File | // Settings | File Templates.
		 * ExtendOperationController .getInstance()
		 * .notify(
		 * ExtendOperationController.OperationKey.PHOTO_FINISH, ""); } });
		 */

        LogUtils.i(TAG, "--->listSelectedPhoto:" + listSelectedPhoto.size());
        // 选图
        Context context = getApplicationContext();
        int maxCount = 8;
        int userId = UserController.getInstance().getUserId(context);
        PhotoActivity.enterActivity(
                context,
                listSelectedPhoto,
                new PhotoConfig(maxCount, false, userId,"dynamicPublish"), new OnSelectPhotoListener() {

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onResultSelect(List<PhotoModel> listSelected) {
                        if (listSelected != null)
                            LogUtils.i(TAG, "------>onResultSelect:" + listSelected.size());
                        if (listSelectedPhoto != null) {
                            listSelectedPhoto.clear();
                            listSelectedPhoto.addAll(listSelected);
                            fillGridViewPhoto(listSelectedPhoto);
                            isDraftChange = true;
                        }
                    }

                    @Override
                    public void onResultSelectCompressPath(List<String> compressPath) {
                        if (compressPath == null) {
                            return;
                        }
                        LogUtils.i(TAG, "------>onResultSelectCompressPath:"
                                + compressPath.size());
                        try {
                            for (int i = 0; i < compressPath.size(); i++) {
                                String localPath = compressPath.get(i);
                                listSelectedPhoto.get(i).compressPath = localPath;
                                listSelectedPhoto.get(i).UrlThumbnail = localPath;
                                LogUtils.i(TAG, "地址为：" + localPath);
                            }
                            shuoshuoModel.listThumbUrl.clear();
                            shuoshuoModel.listThumbUrl.addAll(compressPath);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        shuoshuoModel.listPictures.clear();
                        shuoshuoModel.listPictures.addAll(compressPath);

                        LogUtils.i(TAG,
                                "------> shuoshuoModel.listPictures:"
                                        + shuoshuoModel.listPictures.size());
                        isDraftChange = true;
                        checkPublishText();
                        resizeEditText();

                    }
                });
    }


    private void setListener() {

        gridViewPhoto
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {
                        // To change body of implemented methods use File |
                        // Settings | File Templates.
                        hideAllBoard();
                        try {
                            GridViewImageModel gridViewImageModel = listPhoto
                                    .get(i);

                            // 进入预览
                            if (!StringUtils.isNull(gridViewImageModel.strPath)) {

                                List<PreviewImageModel> listPrev = new ArrayList<PreviewImageModel>();

                                for (int j = 0; j < listPhoto.size(); j++) {
                                    PreviewImageModel model = new PreviewImageModel();
                                    model.bLoaded = false;
                                    model.strPathName = listPhoto.get(j).strPath;
                                    if (!StringUtils.isNull(model.strPathName)) {
                                        listPrev.add(model);
                                    }
                                }
                                LogUtils.i(TAG, "进入预览页面：" + listPrev.size());
                                PreviewImageActivity
                                        .enterActivity(
                                                PublishShoushouActivity.this,
                                                false,
                                                false,
                                                PreviewImageActivity.MODE_DELETE,
                                                listPrev,
                                                i,
                                                new PreviewImageActivity.OnOperationListener() {
                                                    @Override
                                                    public void onDelete(
                                                            int position) {
                                                        // To change body of
                                                        // implemented methods
                                                        // use File | Settings |
                                                        // File Templates.
                                                        try {
                                                            LogUtils.i(
                                                                    TAG,
                                                                    "--->onDelete:"
                                                                            + position);
                                                            isDraftChange = true;
                                                            // 移除
                                                            shuoshuoModel.listPictures
                                                                    .remove(position);
                                                            shuoshuoModel.listUrl
                                                                    .remove(position);
                                                            shuoshuoModel.listThumbUrl
                                                                    .remove(position);
                                                            shuoshuoModel.listImageID
                                                                    .remove(position);

                                                            // 移除选中
                                                            PhotoController
                                                                    .getInstance(
                                                                            getApplicationContext())
                                                                    .removeFromSelection(
                                                                            listPhoto
                                                                                    .get(position).id);

                                                            // 移除
                                                            listPhoto
                                                                    .remove(position);

                                                            gridViewImageApdater.resetPosition();

                                                            handleShowEmptyPhotoView();
                                                            handleAddButton();

                                                            listSelectedPhoto
                                                                    .remove(position);
                                                            LogUtils.i(
                                                                    TAG,
                                                                    "listPhoto大小："
                                                                            + listPhoto
                                                                            .size()
                                                                            + "--> shuoshuoModel.listPictures:"
                                                                            + shuoshuoModel.listPictures
                                                                            .size());


                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                });

                                DeviceUtils
                                        .hideKeyboard(PublishShoushouActivity.this);

                            } else {
                                // FIXME: 2017/3/11
//                                if(emojiLayout != null){
//                                    emojiLayout.hide(false);
//                                }
                                if (bShowKeyboard) {
                                    DeviceUtils
                                            .hideKeyboard(PublishShoushouActivity.this);
                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            getPhoto();
                                        }
                                    }, 25);
                                } else {
                                    getPhoto();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        // getTitleBar().setLeftButtonRes(R.drawable.back_layout);


        getTitleBar().setLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
               // handleFinish();
            }
        });

        getTitleBar().setRightTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
               // handlePublish();
            }
        });

//

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
                handleShowWarnCount();

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

    private void fillGridViewPhoto(final List<PhotoModel> listSelectPhoto) {
        try {

            if (listSelectPhoto == null)
                return;
            LogUtils.i(TAG, "选中图片大小为：" + listSelectPhoto.size());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // To change body of implemented methods use File | Settings
                    // | File Templates.

                    for (GridViewImageModel gridViewImageModel : listPhoto) {
                        if (gridViewImageModel.bitmap != null)
                            gridViewImageModel.bitmap.recycle();
                    }

                    listPhoto.clear();

                    shuoshuoModel.listImageID.clear();
                    shuoshuoModel.listThumbUrl.clear();
                    shuoshuoModel.listUrl.clear();
                    shuoshuoModel.listImageFrom.clear();

                    // 生成bitmap
                    for (PhotoModel photoModel : listSelectPhoto) {

                        // 加入gridview,并显示
                        GridViewImageModel gridViewImageModel = new GridViewImageModel();
                        gridViewImageModel.strPath = photoModel.Url;
                        gridViewImageModel.Url = photoModel.Url;
                        gridViewImageModel.UrlThumbnail = photoModel.UrlThumbnail;
                        LogUtils.i(TAG, "得到缩略图地址：" + photoModel.UrlThumbnail
                                + "\n原图地址：" + photoModel.Url);
                        gridViewImageModel.id = photoModel.Id;

                        if (listPhoto.size() == 0) {
                            listPhoto.add(gridViewImageModel);
                        } else {
                            int len = listPhoto.size();
                            listPhoto.add(len, gridViewImageModel);
                        }

                        // listBitmapUrl.add(photoModel.Url);
                        shuoshuoModel.listImageID.add(photoModel.Id + "");
                        shuoshuoModel.listUrl.add(photoModel.Url);
                        shuoshuoModel.listThumbUrl.add(photoModel.UrlThumbnail);
                        shuoshuoModel.listImageFrom
                                .add(photoModel.isTakePhoto ? "1" : "0");
                        LogUtils.i(TAG, "是否来自拍照:" + photoModel.isTakePhoto);

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // To change body of implemented methods use File |
                            // Settings | File Templates.

                            LogUtils.i(TAG,
                                    "刷新gridview：" + listSelectPhoto.size());

                            // 显示gridview
                            handleShowEmptyPhotoView();
                            handleAddButton();

                        }
                    });

                }
            }).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    // 是否显示空照片布局
    private void handleShowEmptyPhotoView() {
        if (listPhoto.size() == 0) {
            // linearEmptyPhoto.setVisibility(View.VISIBLE);
            linearGridViewPhoto.setVisibility(View.GONE);
        } else if (listPhoto.size() == 1) {
            GridViewImageModel gridViewImageModel = listPhoto.get(0);
            if (StringUtils.isNull(gridViewImageModel.strPath)) {
                listPhoto.clear();
                //  linearEmptyPhoto.setVisibility(View.VISIBLE);
                linearGridViewPhoto.setVisibility(View.GONE);
            } else {
                //    linearEmptyPhoto.setVisibility(View.GONE);
                linearGridViewPhoto.setVisibility(View.VISIBLE);
            }
        } else {
            //    linearEmptyPhoto.setVisibility(View.GONE);
            linearGridViewPhoto.setVisibility(View.VISIBLE);
        }

    }

    // 处理是否显示添加按钮
    private void handleAddButton() {
        if (listPhoto == null || listPhoto.size() == 0)
            return;

        if (listPhoto.size() > 0) {
            // 添加加号
            if (listPhoto.size() < MAX_PIC) {
                boolean isExistAddButton = false;
                for (GridViewImageModel gridViewImageModel : listPhoto) {
                    if (StringUtils.isNull(gridViewImageModel.strPath)) {
                        isExistAddButton = true;
                        break;
                    }
                }
                if (!isExistAddButton) {
                    GridViewImageModel gridViewImageModel = new GridViewImageModel();
                    listPhoto.add(gridViewImageModel);
                    // gridViewImageApdater.notifyDataSetChanged();
                }
                // 移除加号
            } else if (listPhoto.size() > MAX_PIC) {
                for (GridViewImageModel gridViewImageModel : listPhoto) {
                    if (StringUtils.isNull(gridViewImageModel.strPath)) {
                        listPhoto.remove(gridViewImageModel);
                        break;
                    }
                }
            }
        }
        LogUtils.i(TAG, "------>handleAddButton listPhoto size:" + listPhoto.size());
        for (GridViewImageModel model : listPhoto) {
            LogUtils.i(TAG, "-->缩略图图片地址：" + model.UrlThumbnail);
        }
        gridViewImageApdater.notifyDataSetChanged();
    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
