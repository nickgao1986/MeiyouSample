package com.lingan.seeyou.ui.view.photo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomViewPager;
import com.lingan.seeyou.ui.view.LoadingSmallView;
import com.lingan.seeyou.ui.view.YiPageIndicator;
import com.lingan.seeyou.ui.view.dialog.bottomdialog.BottomMenuDialog;
import com.lingan.seeyou.ui.view.dialog.bottomdialog.BottomMenuModel;
import com.lingan.seeyou.ui.view.me.relex.photodraweeview.OnPhotoTapListener;
import com.lingan.seeyou.ui.view.me.relex.photodraweeview.PhotoDraweeView;
import com.lingan.seeyou.ui.view.photo.model.PreviewImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.LinganActivity;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;

/**
 * 通用大图预览
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/9/29 下午4:14
 */
public class PreviewImageActivity extends LinganActivity {

    private static final String TAG = "PreviewImageActivity";

    public static final int MODE_DELETE = 0;
    public static final int MODE_SAVE = 1;
    public static final int MODE_SAVE_DELETE = 2;

    public static boolean bHideRightIcon = false;
    public static boolean bHideStausBar = false;
    public static boolean bHideTitleBar = false;
    public static boolean bShowTextTitle = false;
    public static int mPageIndicatorMode = PreviewUiConfig.PAGE_INDECATOR_CIRCLE;

    public static int mCurrentPosition;

    public static List<PreviewImageModel> listModel = new ArrayList<PreviewImageModel>();
    public static int mCurrentMode = MODE_SAVE_DELETE;

    private CustomViewPager mViewPager;
    private PreviewImageAdapter mAdapter;
    private YiPageIndicator indicator;
    private TextView tvContent;


    private RelativeLayout rlTitle;
   // private ImageView ivLeft, ivRight;
    private TextView tvTitle;

    private boolean bTitleShow = false;

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.destory();
        }
        Context context = this.getApplicationContext();
        PreviewImageController.getInstance(context).onDestroy();
        super.onDestroy();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        bUseCustomAnimation = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT) {
            overridePendingTransition(R.anim.activity_preview_image_in, R.anim.activity_preview_image_out);
        }
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //是否隐藏状态栏
        if (bHideStausBar) {
            this.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.layout_image_preview_new);
        try {
            initUI();
            initLogic();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void initUI() {
        titleBarCommon.setCustomTitleBar(-1);

        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
//        ivLeft = (ImageView) rlTitle.findViewById(R.id.ivLeft);
//        ivRight = (ImageView) rlTitle.findViewById(R.id.ivRight);
        tvTitle = (TextView) rlTitle.findViewById(R.id.tvTitle);

        if (bHideTitleBar) {
            rlTitle.setVisibility(View.GONE);
            bTitleShow = false;
        } else {
            rlTitle.setVisibility(View.VISIBLE);
            bTitleShow = true;
        }

        //ivRight.setVisibility(bHideRightIcon ? View.GONE : View.VISIBLE);

        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        mAdapter = new PreviewImageAdapter(this, listModel);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        tvContent = (TextView) findViewById(R.id.tvContent);
        indicator = (YiPageIndicator) findViewById(R.id.indicator);

        if (mPageIndicatorMode == PreviewUiConfig.PAGE_INDICATOR_TEXT) {
            tvContent.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.GONE);
            indicator.setVisibility(View.VISIBLE);
            if (listModel.size() <= 1) {
                indicator.setVisibility(View.GONE);
            } else {
                indicator.setVisibility(View.VISIBLE);
                indicator.setTotalPage(listModel.size());
                indicator.setCurrentPage(mCurrentPosition);
            }
        }

        fillResource();
        setListener();
        updateText(mCurrentPosition);
    }

    private void fillResource() {
        try {
            findViewById(R.id.rlTitle).setBackgroundResource(R.drawable.apk_default_titlebar_bg);
            // findViewById(R.id.ivLeft).setBackgroundResource(R.drawable.back_layout);
            // findViewById(R.id.ivRight).setBackgroundResource(R.drawable.delete_selector);
            ((TextView) findViewById(R.id.tvTitle)).setTextColor(getResources().getColor(R.color.white_a));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setListener() {
        try {

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int i) {
                    hideTitle();
                    mCurrentPosition = i;
                    indicator.setCurrentPage(mCurrentPosition);
                    updateText(mCurrentPosition);
                    if (mCurrentPosition == 0) {
                       // setSwipeBackEnable(true);
                    } else {
                      //  setSwipeBackEnable(false);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });


            mAdapter.setOnPreviewActionListener(new PreviewImageAdapter.OnPreviewActionListener() {
                @Override
                public void onItemClick(int position, String uri) {
                    if (bHideTitleBar) {
                        handleFinish();
                    } else {
                        handleShowOrHidTitle();
                    }

                }

                @Override
                public void onItemLongClick(int position, String uri, Bitmap bitmap) {
                    if (bHideTitleBar) {
                        if (uri == null) {
                            return;
                        }
                        handleItemLongClick(position, uri, bitmap);
                    }

                }
            });

//            ivLeft.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    handleFinish();
//                }
//            });
//
//            ivRight.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    handleDeleteItem(true);
//                }
//            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void hideTitle() {
        if (bTitleShow) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -100);
            translateAnimation.setDuration(300);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    rlTitle.setVisibility(View.GONE);
                    bTitleShow = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            rlTitle.startAnimation(translateAnimation);

        }
    }

    private void showTitle() {
        if (!bTitleShow) {

            rlTitle.setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -100, 0);
            translateAnimation.setDuration(300);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bTitleShow = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            rlTitle.startAnimation(translateAnimation);
        }
    }

    private void handleShowOrHidTitle() {
        //隐藏
        if (bTitleShow) {
            //rlTitle;
            hideTitle();
            //显示
        } else {
            showTitle();
            updateText(mCurrentPosition);
        }
    }


    /**
     * 处理右上角删除功能
     */
    private void handleDeleteItem(boolean showDialog) {

        try {
            if (showDialog) {
                BottomMenuModel bottomMenuModel = new BottomMenuModel();
                bottomMenuModel.title = "删除";
                List<BottomMenuModel> list = new ArrayList<>();
                list.add(bottomMenuModel);
                BottomMenuDialog dialog = new BottomMenuDialog(this, list);
                dialog.setTitle("要删除这张照片吗？");
                dialog.setOnMenuSelectListener(new BottomMenuDialog.OnMenuSelectListener() {

                    @Override
                    public void OnSelect(int index, String title) {
                        // TODO Auto-generated method stub
                        if (index == 0) {
                            deleteItem();
                        }
                    }
                });
                dialog.show();
            } else {
                deleteItem();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteItem() {
        try {
            int size = listModel.size();
            if (mListener != null) {
                mListener.onDelete(mCurrentPosition);
            }
            //移除
            listModel.remove(mCurrentPosition);
            //索引变动
            if (mCurrentPosition == size - 1) {
                mCurrentPosition = mCurrentPosition - 1;
            }
            //更新视图
            if (listModel.size() == 0) {
                finish();
            } else {
                updateView();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void updateView() {
        mAdapter.notifyDataSetChanged();
        /*mAdapter = new PreviewImageAdapter(this, listModel);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);*/
        if (listModel.size() <= 1) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
            indicator.setTotalPage(listModel.size());
            indicator.setCurrentPage(mCurrentPosition);
        }
        updateText(mCurrentPosition);
        setListener();
    }


    /**
     * 长按菜单
     *
     * @param position
     * @param uri
     */
    private void handleItemLongClick(final int position, final String uri, final Bitmap bitmap) {
        List<BottomMenuModel> bottomMenuModelList = new ArrayList<BottomMenuModel>();
        if (mCurrentMode == MODE_DELETE) {
            handleDeleteItem(true);
        } else if (mCurrentMode == MODE_SAVE) {
            BottomMenuModel bottomMenuModel = new BottomMenuModel();
            bottomMenuModel.title = "保存图片";
            bottomMenuModelList.add(bottomMenuModel);
            BottomMenuDialog bottomMenuDialog = new BottomMenuDialog(this, bottomMenuModelList);
            bottomMenuDialog.setOnMenuSelectListener(new BottomMenuDialog.OnMenuSelectListener() {
                @Override
                public void OnSelect(int index, String title) {
                    if (index == 0) {
                        saveBitmap(uri);
                    }
                }
            });
            bottomMenuDialog.show();
        } else if (mCurrentMode == MODE_SAVE_DELETE) {
            BottomMenuModel bottomMenuModel = new BottomMenuModel();
            bottomMenuModel.title = "保存图片";
            bottomMenuModelList.add(bottomMenuModel);

            BottomMenuModel bottomMenuModel1 = new BottomMenuModel();
            bottomMenuModel1.title = "删除";
            bottomMenuModelList.add(bottomMenuModel1);
            BottomMenuDialog bottomMenuDialog = new BottomMenuDialog(this, bottomMenuModelList);
            bottomMenuDialog.setOnMenuSelectListener(new BottomMenuDialog.OnMenuSelectListener() {
                @Override
                public void OnSelect(int index, String title) {
                    if (index == 0) {
                        saveBitmap(uri);
                    } else if (index == 1) {
                        handleDeleteItem(false);


                    }
                }
            });
            bottomMenuDialog.show();
        }
    }

    /**
     * 保存图片
     *
     * @param uri
     */
    private void saveBitmap(final String uri) {
        LogUtils.d(TAG, "保存图片：" + uri);
        final Context context = getApplicationContext();


        if (GifUtil.isGif(uri)) {
            PreviewImageController.getInstance(context).saveBitmapGif(uri);
            return;
        } else {
            PreviewImageController.getInstance(context).saveBitmap(uri);
        }
    }

    private void updateText(int position) {
        try {
            if (listModel.size() == 0)
                return;
            String description = listModel.get(position).strDescription;
            //设置1/3
            int index = mCurrentPosition + 1;
            String text = index + "/" + listModel.size();

            if (bShowTextTitle) {
                tvTitle.setText(description);
            } else {
                tvTitle.setText(text);
            }

            tvContent.setText(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initLogic() {

    }


    @Override
    public void onBackPressed() {
        //
        handleFinish();
    }

    private void handleFinish() {

        finish();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT) {
            overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_preview_image_out);
        }
    }

    public static OnOperationListener mListener;

    public interface OnOperationListener {
        void onDelete(int position);
    }


    /**
     * 进入预览页面
     *
     * @param context
     * @param bHideStausBar
     * @param bHideTitleBar
     * @param list
     * @param position
     */
    public static void enterActivity(Context context, boolean bHideStausBar, boolean bHideTitleBar, int mode, List<PreviewImageModel> list, int position, OnOperationListener listener) {
        try {
            PreviewUiConfig config = new PreviewUiConfig(mode, list, position, listener);
            config.bHideStausBar = bHideStausBar;
            config.bHideTitleBar = bHideTitleBar;

            enterActivity(context, config);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 进入预览页面
     *
     * @param context
     * @param bHideStausBar
     * @param bHideTitleBar
     * @param list
     * @param position
     */
    public static void enterActivity(Context context, boolean bShowTextTitle, boolean bHideStausBar, boolean bHideTitleBar, int mode, List<PreviewImageModel> list, int position, OnOperationListener listener) {
        try {
            PreviewUiConfig config = new PreviewUiConfig(mode, list, position, listener);
            config.bHideStausBar = bHideStausBar;
            config.bHideTitleBar = bHideTitleBar;
            config.bShowTextTitle = bShowTextTitle;
            enterActivity(context, config);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void enterActivity(Context context, boolean bHideRightIcon, boolean bHideStausBar, boolean bHideTitleBar, int mode, List<PreviewImageModel> list, int position) {
        try {

            PreviewUiConfig config = new PreviewUiConfig(mode, list, position, null);
            config.bHideRightIcon = bHideRightIcon;
            config.bHideStausBar = bHideStausBar;
            config.bHideTitleBar = bHideTitleBar;

            enterActivity(context, config);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void enterActivity(Context context, PreviewUiConfig config) {
        try {
            PreviewImageActivity.bHideRightIcon = config.bHideRightIcon;
            PreviewImageActivity.bHideStausBar = config.bHideStausBar;
            PreviewImageActivity.bHideTitleBar = config.bHideTitleBar;
            PreviewImageActivity.bShowTextTitle = config.bShowTextTitle;
            PreviewImageActivity.mPageIndicatorMode = config.pageIndicatorMode;

            PreviewImageActivity.listModel.clear();
            PreviewImageActivity.listModel.addAll(config.list);
            PreviewImageActivity.mCurrentPosition = config.position;
            PreviewImageActivity.mCurrentMode = config.mode;
            PreviewImageActivity.mListener = config.listener;

            doIntent(context, PreviewImageActivity.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void doIntent(Context mContext, Class<?> mClass) {
        Intent intent = new Intent();
        intent.setClass(mContext, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    /**
     * 图片预览适配器
     * Created with IntelliJ IDEA.
     * User: Administrator
     * Date: 14-7-31
     * Time: 下午3:40
     * To change this template use File | Settings | File Templates.
     */
    @SuppressLint("InflateParams")
    public static class PreviewImageAdapter extends PagerAdapter implements View.OnClickListener {

        private String TAG = "PreviewImageAdapter";
        private Activity mContext;
        public List<PreviewImageModel> listModel;
        //public ImageUtil imageUtil;
        public Bitmap[] bitmaps;
        private int bigImageWidth;

        public PreviewImageAdapter(Activity context, List<PreviewImageModel> listModel) {
            this.mContext = context;
            this.listModel = listModel;
            bitmaps = new Bitmap[listModel.size()];
            bigImageWidth = DeviceUtils.getScreenWidth(context);
        }


        @Override
        public int getCount() {
            return listModel.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(final View paramView, final int position) {
            View view = LayoutInflater.from(mContext)
                                      .inflate(R.layout.layout_image_preview_item_new, null);
            findView(position, view);
            ((ViewGroup) paramView).addView(view);
            //mViewMaps.put(position, view);
            return view;
        }


        private List<AnimationDrawable> listAnimationDrawable = new ArrayList<AnimationDrawable>();


        public void destory() {
            for (AnimationDrawable drawable : listAnimationDrawable) {
                if (drawable != null) {
                    drawable.stop();
                }
            }
            listAnimationDrawable.clear();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void findView(final int position, View view) {
            try {
                final PhotoDraweeView zoomImageView;
                final LoadingSmallView loadingView;
                zoomImageView = (PhotoDraweeView) view.findViewById(R.id.zoomImage);
//                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
//                    zoomImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//                }
                zoomImageView.setAllowParentInterceptOnEdge(true);
                loadingView = (LoadingSmallView) view.findViewById(R.id.loadingView);
                loadingView.hide();

                final PreviewImageModel imageModel = listModel.get(position);
                zoomImageView.setOnPhotoTapListener(new OnPhotoTapListener() {

                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        try {
                            if (mListener != null) {
                                mListener.onItemClick(position, imageModel.strUrl);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                //长按操纵
                zoomImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        try {
                            if (mListener != null) {
                                if (position >= bitmaps.length) {
                                    mListener.onItemLongClick(position, imageModel.strUrl, null);
                                } else {
                                    mListener.onItemLongClick(position, imageModel.strUrl, bitmaps[position]);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        return false;
                    }
                });
                //加载本地图片
                final String strPathName = imageModel.strPathName;
                final String strUrl = imageModel.strUrl;

                loadingView.setStatus(LoadingSmallView.TYPE_LOADING);
                String url = strPathName;
                if (StringUtils.isNull(strPathName)) {
                    url = strUrl;
                }
                loadPic(url, zoomImageView, loadingView);

                //设置标记
                view.setTag(position);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


        public static Uri toUri(String path) {
            Uri uri;
            if (path.startsWith("http")) {
                uri = Uri.parse(path);
            } else {
                uri = Uri.fromFile(new File(path));
            }
            return uri;
        }

        private void loadPic(final String strPathName, final PhotoDraweeView zoomImageView, final LoadingSmallView loadingView) {
            if (!StringUtils.isNull(strPathName)) {
                ImageLoadParams params = new ImageLoadParams();
                params.height = DeviceUtils.getScreenHeight(mContext);
                params.width = DeviceUtils.getScreenWidth(mContext);

                params.forbidenModifyUrl = true;
                params.anim = true;
                zoomImageView.setPhotoUri(toUri(strPathName), mContext, new PhotoDraweeView.CallBack() {

                    @Override
                    public void onSuccess(String url, Object... obj) {
                        setPathNameLoaded(strPathName);
                        loadingView.hide();
                    }

                    @Override
                    public void onFail(String url, Object... obj) {
                        loadingView.hide();
                    }
                });
            } else {
                loadingView.hide();
                zoomImageView.setImageResource(R.drawable.apk_remind_noimage);
            }
        }

        //设置已经加载过
        private void setPathNameLoaded(String pathname) {
            try {
                if (StringUtils.isNull(pathname))
                    return;
                for (PreviewImageModel model : listModel) {
                    if (!StringUtils.isNull(model.strPathName) && model.strPathName.equals(pathname)) {
                        model.bLoaded = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


        private OnPreviewActionListener mListener;

        public void setOnPreviewActionListener(OnPreviewActionListener listener) {
            mListener = listener;
        }

        public interface OnPreviewActionListener {
            void onItemClick(int position, String uri);

            void onItemLongClick(int position, String uri, Bitmap bitmap);
        }


        @Override
        public void onClick(View v) {
        }

        @Override
        public void destroyItem(View collection, int position, Object paramObject) {
            ((ViewGroup) collection).removeView((View) paramObject);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
}
