package nickgao.com.meiyousample.myfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.AKeyTopView;
import com.lingan.seeyou.ui.view.BadgeImageView;
import com.lingan.seeyou.ui.view.ParallaxScrollListView;
import com.lingan.seeyou.ui.view.ResizeLayout;
import com.lingan.seeyou.ui.view.RoundedImageView;
import com.lingan.seeyou.ui.view.photo.UserPhotoManager;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.firstPage.DynamicDetailClickPraiseEvent;
import nickgao.com.meiyousample.firstPage.module.IOnExcuteListener;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.dynamicModel.DynamicContent;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.personal.ILoadMoreViewState;
import nickgao.com.meiyousample.personal.ImageViewWithMask;
import nickgao.com.meiyousample.personal.OnListViewScrollListener;
import nickgao.com.meiyousample.personal.PersonalListener;
import nickgao.com.meiyousample.service.DynamicService;
import nickgao.com.meiyousample.service.ServiceFactory;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;


/**
 * 密友圈
 */
public class DynamicHomeActivity extends PeriodBaseActivity implements OnClickListener,PersonalListener {

    private String TAG = "DynamicHomeActivity";
    public static final String PREV = "prev";
    public static final String NEXT = "next";
    private Activity myActivity;

    private HomeDynamicController mHomeDynamicController;
    private ResizeLayout rootContainer; // 根布局
    private ParallaxScrollListView parallaxListview;


    private LoaderImageView ivBannerBg;
    private TextView tvNoNetwork;


    private RoundedImageView ivHead;


    private BadgeImageView bivVerify;
    private LinearLayout llMsgTip;
    private TextView tvMsgTip;// 消息提示


    private LoaderImageView ivMsgIcon;
    private BadgeImageView bivMsgVerify;

    private LinearLayout llTopMenu;


    // 列表底部
    private View moreView;
    private ProgressBar moreProgressBar;
    private TextView loadMoreView;

    // 正在加载
    private boolean bLoading = false;
    private boolean isFistLoad;//是否首次加载

    private LinearLayout llEmptyContainer;
    private Button btnOperate;


    private HomeDynamicAdapter mAdapter;

    private int mLvCurrentPosition = 0;
    private boolean isKeyboardShow;

    private int scrolledX;
    private int scrolledY;
    boolean isActivityFinish = false;
    private float mLastY;
    private RelativeLayout rl_custom_title_bar;
    private float headerHeight = 0f;
    private View mListViewHeader;
    //private ImageView mMask;
    private ImageViewWithMask mImageViewWithMask;

    public static Intent getNotifyIntent(Context context) {
        Intent intent = new Intent(context, DynamicHomeActivity.class);
        intent.putExtra("is_from_notify", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.dynamic_home_layout;
//    }



    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_home_layout;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ImageLoader.initialize(this, false);
        initSkin();

        super.onCreate(savedInstanceState);
        myActivity = this;
        LogUtils.i("DynamicHomeActivity onCreate");
        try {
            init();
            initUI();
            setListener();
            loadDataFirst();
            setAvatar(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        try {
//            init();
//            initUI();
//            setListener();
//            loadDataFirst();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            LogUtils.i("DynamicHomeActivity onResume" + mLvCurrentPosition);
            initLogic();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            if (parallaxListview != null) {
                mLvCurrentPosition = parallaxListview.getFirstVisiblePosition();
                LogUtils.i("DynamicHomeActivity onPause" + mLvCurrentPosition);
                parallaxListview.stopRefresh();
            }

            LogUtils.i("DynamicHomeActivity onPause");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        LogUtils.i("DynamicHomeActivity onDestory");
        try {
            isActivityFinish = true;
           // ExtendOperationController.getInstance().unRegister(extendListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private void init() {
        mAdapter = new HomeDynamicAdapter(myActivity);

        mHomeDynamicController = HomeDynamicController.getInstance();
        // 接口监听
//        ExtendOperationController.getInstance().register(extendListener);
    }


    /**
     * 没数据时显示本地拼接数据
     */
    private void handleNoResult() {
        if (mAdapter.getCount() > 0) {
            llEmptyContainer.setVisibility(View.GONE);

        } else {
            llEmptyContainer.setVisibility(View.VISIBLE);
            moreView.setVisibility(View.GONE);

        }
    }

    private void setListener() {
        //
        //
        ivHead.setOnClickListener(this);
        //  ivBannerBg.setOnClickListener(this);
        mImageViewWithMask.setOnClickListener(this);
        llMsgTip.setOnClickListener(this);
        parallaxListview.setOnRefreshListener(new ParallaxScrollListView.OnRefreshLinstener() {
            @Override
            public void onRefresh() {
                if (isFistLoad) {//首次加载
                    isFistLoad = false;
                    mHomeDynamicController.getHomeDynamicListFromCache(
                            myActivity, new IOnExcuteListener() {
                                @Override
                                public void onResult(Object object) {
                                    try {
                                        if (null != object) {
                                            List<HomeDynamicModel> homeDynamicModels = (List<HomeDynamicModel>) object;
                                           // mAdapter.setDatas(homeDynamicModels, AddFriendController.SYDynamicFocusFromTypeMiyouQuan);
                                        }
                                        loadDynamic(0, PREV);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                } else
                    loadDynamic(0, PREV);
            }
        });

        // 上拉加载更多
        parallaxListview.setOnScrollListener(new OnListViewScrollListener(myActivity, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView,
                                             int scrollState) {
                try {
                    if (!bLoading && (absListView.getCount() - absListView.getLastVisiblePosition()) < 3 && mAdapter.getCount() > 0) {//滚动到倒数第二条的时候出发加载跟多
                        updateFooter(ILoadMoreViewState.LOADING);
                        loadDynamic(mAdapter.getLastData().sort, NEXT);
                    }


                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        scrolledX = parallaxListview.getScrollX();
                        scrolledY = parallaxListview.getScrollY();
                        LogUtils.i("listView:x" + scrolledX + ";Y:" + scrolledY);
                        // 停下来的时候计算当前页面可见item的top和height
                        handleComputeCurrentPageItemTopAndHeight(false);
                        getAKeyTopView().setStopScoll(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                mFirstVisibleIndex = firstVisibleItem;
                mVisibleItemCount = visibleItemCount;
                mTotalCount = totalItemCount;

                if(headerHeight == 0) {
                    headerHeight = mListViewHeader.getMeasuredHeight();
                }
                setHeaderAlpha();

                handleShowIbToTop();

            }
        }));

        parallaxListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //回到顶部的逻辑
                float y = motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        mLastY = y;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        int detaY = (int) (mLastY - y);
                        if (detaY != 0) {
                            if (detaY < 0) {
//                                LogUtils.d(TAG, "向下");
                                getAKeyTopView().setTopScoll(false);
                            } else {
//                                LogUtils.d(TAG, "向上");
                                getAKeyTopView().setTopScoll(true);
                            }
                        }
                        mLastY = y;
                        break;
                }
                return false;

            }
        });


        getAKeyTopView().setOnAKeyTopLisener(new AKeyTopView.OnAKeyTopClickListener() {
            @Override
            public void OnAKeyTopClick() {
                handleScrollToTop();
            }
        });


        parallaxListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View
                    view, final int position, long l) {
                try {
                    if (position > 0 && position <= mAdapter.getCount()) { //含头部 HomeDynamicModel
                        final HomeDynamicModel homeDynamicModel = mAdapter.getData(position - 1);
                        int dynamicId = homeDynamicModel.id;

                        if (mAdapter != null && null != homeDynamicModel) {

//                            mAdapter.onItemClick(homeDynamicModel,false);
                            Intent intent = new Intent();
                            intent.setClass(myActivity,DynamicDetailActivity.class);
                            startActivity(intent);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void handleScrollToTop() {
        if (parallaxListview != null && parallaxListview.getCount() > 0) {
            parallaxListview.setSelection(0);
        }
    }

    private void updateFooter(int state) {
        try {
            switch (state) {
                case ILoadMoreViewState.ERROR:
                    moreView.setVisibility(View.GONE);
                    moreProgressBar.setVisibility(View.GONE);
                    loadMoreView.setText("加载失败！");
                    break;
                case ILoadMoreViewState.LOADING:
                    moreView.setVisibility(View.VISIBLE);
                    moreProgressBar.setVisibility(View.VISIBLE);
                    loadMoreView.setText("正在加载更多...");
                    break;
                case ILoadMoreViewState.NORMAL:
                case ILoadMoreViewState.COMPLETE:
                    moreProgressBar.setVisibility(View.GONE);
                    loadMoreView.setText("没有更多动态啦~");

                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public int getScrollY() {
        View c = parallaxListview.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = parallaxListview.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }

    private void setHeaderAlpha() {
        if(headerHeight != 0  && headerHeight > getScrollY()) {
            float alpha = getScrollY() / headerHeight;
            rl_custom_title_bar.setAlpha(alpha);
        }else{
            if(rl_custom_title_bar.getAlpha() < 1) {
                rl_custom_title_bar.setAlpha(1);
            }
        }
    }
    /**
     * 置顶按钮的显示
     */
    private void handleShowIbToTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isActivityFinish) {
                        return;
                    }
                    if (parallaxListview.getLastVisiblePosition() > 4  && !isKeyboardShow) {
                        if (true) {
                            getAKeyTopView().showAkeyTopView();
                        }

                    } else {
                        getAKeyTopView().hideAkeyTopView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 200);

    }


    private void loadDynamic(int time, String loadMode) {
        if (bLoading) {
            return;
        }
//        TaskLoadDynamic taskLoadDynamic = new TaskLoadDynamic(time, loadMode);
//        taskLoadDynamic.execute();
        DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
        service.sendRequest(this,time);

    }

    private void loadDataFirst() {
        isFistLoad = true;
        parallaxListview.startRefresh();
    }

    private void initTitle() {
        ImageView custom_iv_left = (ImageView)findViewById(R.id.custom_iv_left);
        rl_custom_title_bar = (RelativeLayout)findViewById(R.id.rl_custom_title_bar);
        rl_custom_title_bar.setAlpha(0);
        TextView tv_publish_dynamic = (TextView)findViewById(R.id.tv_publish_dynamic);
        tv_publish_dynamic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnalysisClickAgent.onEvent(myActivity, "home-xss");
//                YouMentEventUtils.getInstance().countEvent(mActivity,"grzy-fdt",YouMentEventUtils.NOTHING,null);
//                handlePublishDynamic();
            }
        });
        custom_iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.finish();
            }
        });
    }
    @SuppressLint("ResourceAsColor")
    private void initUI() {
        getTitleBar().setCustomTitleBar(-1);
        initTitle();

        rootContainer = (ResizeLayout) findViewById(R.id.rootContainer);
        parallaxListview = (ParallaxScrollListView) findViewById(R.id.home_list);



        // 列表头部
        mListViewHeader = ViewFactory.from(myActivity).getLayoutInflater().inflate(R.layout.layout_dynamic_home_list_header, null);
        //  mMask = (ImageView)mListViewHeader.findViewById(R.id.mask);

        mImageViewWithMask = (ImageViewWithMask)mListViewHeader.findViewById(R.id.ivBannerBg);
        ivBannerBg = mImageViewWithMask.getLoaderImageView();

        // ivBannerBg = (LoaderImageView) mListViewHeader.findViewById(R.id.ivBannerBg);

        int bannerHeight = DeviceUtils.getScreenHeight(myActivity) / 4;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageViewWithMask.getLayoutParams();
        if (bannerHeight > 0) {
            params.height = bannerHeight;
        }
        mImageViewWithMask.requestLayout();

        ivHead = (RoundedImageView) mListViewHeader.findViewById(R.id.ivHead);

        //认证用户
//        bivVerify = new BadgeImageView(getApplicationContext(), ivHead);
//        bivVerify.setBadgePosition(BadgeImageView.POSITION_BOTTOM_RIGHT);
//        bivVerify.setImageResource(AccountAction.getShowVIcon(DataSaveHelper.getInstance(getApplicationContext()).getUserIsMeiyouAccount(), DataSaveHelper.getInstance(getApplicationContext()).isVip() ? 1 : 0));
//        bivVerify.setBadgeMargin(getResources().getDimensionPixelSize(R.dimen.space_xxs), getResources().getDimensionPixelSize(R.dimen.space_xxxs));

        tvMsgTip = (TextView) mListViewHeader.findViewById(R.id.tvMsgTip);
        llMsgTip = (LinearLayout) mListViewHeader.findViewById(R.id.llMsgTip);
        ivMsgIcon = (LoaderImageView) mListViewHeader.findViewById(R.id.ivMsgIcon);

        //消息提醒认证用户
        bivMsgVerify = new BadgeImageView(myActivity, ivMsgIcon);
        bivMsgVerify.setBadgePosition(BadgeImageView.POSITION_BOTTOM_RIGHT);
        bivMsgVerify.setImageResource(R.drawable.personal_v);

        llEmptyContainer = (LinearLayout) mListViewHeader
                .findViewById(R.id.emptyContainer);
        btnOperate = (Button) mListViewHeader.findViewById(R.id.btnOperate);

        btnOperate.setOnClickListener(this);
        // 加入头部
        parallaxListview.setScaleView((RelativeLayout) mListViewHeader);
        parallaxListview.setParallaxImageView(mImageViewWithMask, 200);

        parallaxListview.setRefreshView(R.drawable.apk_rotate);
        parallaxListview.enableCanPullAlways();

        parallaxListview.setAdapter(mAdapter);

        initListViewFooter();

        updateUI();


//        try {
//            setAvatar(false);
//            // 重新初始化头像,检查头像是否被更换
//            UserPhotoManager.getInstance().handleCheckUserImageUpdate(this, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 换肤时刷新ui
     */
    @SuppressLint("ResourceAsColor")
    private void updateUI() {

    }

    private void initListViewFooter() {
        // 列表底部
        moreView = ViewFactory.from(myActivity).getLayoutInflater().inflate(R.layout.listfooter_more,
                null);
        moreProgressBar = (ProgressBar) moreView
                .findViewById(R.id.pull_to_refresh_progress);
        loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
        moreProgressBar.setVisibility(View.GONE);
        moreView.setVisibility(View.GONE);

        // 空底部
        LinearLayout linearFoot = new LinearLayout(myActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearFoot.addView(moreView, layoutParams);
        // 加入底部
        LogUtils.e("加入底部");
        parallaxListview.addFooterView(linearFoot, null, false);

    }

    /**
     * 首先先获取用户是否有设置经期数据没有的话就进入日历界面设置
     *
     * @return
     */
    private boolean initLogic() {
        try {
            handleUpdateBanner();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }

    private boolean bTopMenuShowed = false;

    private void hidePopMenu() {
        if (!bTopMenuShowed)
            return;

        bTopMenuShowed = false;

        LinearLayout linearMenu = (LinearLayout) llTopMenu
                .findViewById(R.id.linearMenu);
        Animation animation = AnimationUtils.loadAnimation(myActivity,
                R.anim.menu_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llTopMenu.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearMenu.startAnimation(animation);
    }

    private boolean hasMeasured = false;
    private int mSanjiaoWidth = 0;

    private void showPopupMenu() {
        try {
            if (bTopMenuShowed) {
                hidePopMenu();
                return;
            }
            bTopMenuShowed = true;

            // 显示
            llTopMenu.setVisibility(View.VISIBLE);
            // 三角形的位置
            final ImageView ivTopSanjiao = (ImageView) llTopMenu
                    .findViewById(R.id.ivTopSanjiao);

            if (mSanjiaoWidth == 0) {
                ViewTreeObserver vto = ivTopSanjiao.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        if (!hasMeasured) {
                            mSanjiaoWidth = ivTopSanjiao.getMeasuredWidth();
                            // 获取到宽度和高度后，可用于计算
                            hasMeasured = true;
                            showPopupMenuNew(ivTopSanjiao);
                        }
                        return true;
                    }
                });
            } else {
                showPopupMenuNew(ivTopSanjiao);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showPopupMenuNew(ImageView ivTopSanjiao) {
        try {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivTopSanjiao
                    .getLayoutParams();
            int totalWidth = DeviceUtils.dip2px(myActivity, 48);
            int subWidth = totalWidth - mSanjiaoWidth;
            int rightMargin = subWidth / 2;
            LogUtils.i("-->totalWidth:" + totalWidth + "-->subWidth:"
                    + subWidth + "-->rightMargin:" + rightMargin);
            layoutParams.rightMargin = rightMargin;
            ivTopSanjiao.requestLayout();

            // 动画
            final LinearLayout linearMenu = (LinearLayout) llTopMenu
                    .findViewById(R.id.linearMenu);
            Animation animation = AnimationUtils.loadAnimation(myActivity,
                    R.anim.menu_in);
            linearMenu.startAnimation(animation);

            // 响应
            llTopMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 点击收缩
                    hidePopMenu();
                }
            });
            int childCount = ((LinearLayout) linearMenu.getChildAt(1))
                    .getChildCount();
            // 设置选中项
            setMenuSelected(linearMenu, -1);

            // 数目
            for (int i = 0; i < childCount; i++) {
                final int index = i;
                if (index % 2 != 0)
                    continue;
                LinearLayout linearChild = (LinearLayout) ((LinearLayout) linearMenu
                        .getChildAt(1)).getChildAt(i);
                linearChild.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (index == 0) {
                            setMenuSelected(linearMenu, 0);
                           // handlePublishDynamic();
                        } else if (index == 2) {
                            setMenuSelected(linearMenu, 2);


                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hidePopMenu();
                            }
                        }, 150);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void setMenuSelected(LinearLayout linearMenu, int selectedIndex) {
        try {
            int childCount = ((LinearLayout) linearMenu.getChildAt(1))
                    .getChildCount();
            // 显示选中项
            for (int i = 0; i < childCount; i++) {
                final int index = i;
                if (index % 2 != 0)
                    continue;
                LinearLayout linearChild = (LinearLayout) ((LinearLayout) linearMenu
                        .getChildAt(1)).getChildAt(i);

                if (i > 2) {
                    linearChild.setVisibility(View.GONE);
                }

                ImageView ivIcon = (ImageView) linearChild.getChildAt(0);
                TextView tvContent = (TextView) linearChild.getChildAt(1);
                if (i == 0) {
                    tvContent.setText(getResources().getString(R.string.publish_shuoshuo) + "    ");
                    if (i == selectedIndex) {
                        // tvContent.setTextColor(getResources().getColor(R.color.red_a));
                        // ivIcon.setImageResource(R.drawable.apk_dropdown_task_up);
                    } else {
                        tvContent.setTextColor(getResources().getColor(
                                R.color.white_a));
                        ivIcon.setImageResource(R.drawable.apk_dropdown_news);
                    }

                } else if (i == 2) {
                    tvContent.setText(getResources().getString(R.string.add_attention));
                    if (i == selectedIndex) {
                        // ivIcon.setImageResource(R.drawable.apk_dropdown_remind_up);
                        // tvContent.setTextColor(getResources().getColor(R.color.red_a));
                    } else {
                        ivIcon.setImageResource(R.drawable.apk_dropdown_add);
                        tvContent.setTextColor(getResources().getColor(
                                R.color.white_a));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * banner
     */
    private void handleUpdateBanner() {
        try {
            final String bannerUrl = "http://sc.seeyouyima.com/android-dynamicBanner-129746620-1493947221310_972_972.jpg";
            LogUtils.i("updatebanner:" + bannerUrl);
            if (!StringUtils.isNull(bannerUrl)) {
                ImageLoader.getInstance().displayImage(myActivity,
                        ivBannerBg, bannerUrl,
                        0, 0, 0, 0,
                        false, DeviceUtils.getScreenWidth(myActivity), DeviceUtils.dip2px(myActivity, 200), new ImageLoader.onCallBack(){
                            @Override
                            public void onSuccess(ImageView imageView, Bitmap bitmap, String s, Object... objects) {
                                //    mMask.setVisibility(View.VISIBLE);
                                mImageViewWithMask.addMask();
                            }

                            @Override
                            public void onFail(String s, Object... objects) {
                                SkinManager.getInstance().setDrawable(ivBannerBg, R.drawable.personal_fragment_header_background);
                            }

                            @Override
                            public void onProgress(int i, int i1) {

                            }

                            @Override
                            public void onExtend(Object... objects) {

                            }
                        });
            } else {
                LogUtils.i("updatebanner default");
                SkinManager.getInstance().setDrawable(ivBannerBg, R.drawable.personal_fragment_header_background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private int mFirstVisibleIndex, mVisibleItemCount, mTotalCount;
   // private HashMap<Integer, ListItemLocation> hashMapListVisibleItem = new HashMap<Integer, ListItemLocation>();


    private int getIndex(int position) {
        final int firstVisibleItem = parallaxListview.getFirstVisiblePosition();
        final int index = position + 1 - firstVisibleItem;
        return index;
    }


    private void handleComputeCurrentPageItemTopAndHeight(boolean bDelayCount) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 重新计算UI的值
//                hashMapListVisibleItem.clear();
//                hashMapListVisibleItem.putAll(ViewUtilController.getInstance().handleComputeCurrentPageItemTopAndHeight(mFirstVisibleIndex, mVisibleItemCount, parallaxListview));
            }
        }, bDelayCount ? 100 : 0);
    }


    //说说发布
    private void publish(HomeDynamicModel homeDynamicModel) {
//        try {
//            mAdapter.addDataInHead(homeDynamicModel);
//            scrollToTop();
//            mHomeDynamicController.saveHomeDynamicListToCache(DynamicHomeActivity.this, mAdapter.getDatas());
//            handleNoResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }



    /**
     * 加载动态task
     */
//    private class TaskLoadDynamic extends AsyncTask<Void, Void, List<HomeDynamicModel>> {
//        int time = 0;
//        String loadMode;
//
//        public TaskLoadDynamic(int time, String loadMode) {
//            this.time = time;
//            this.loadMode = loadMode;
//        }
//
//        @Override
//        protected List<HomeDynamicModel> doInBackground(Void... voids) {
////            return mHomeDynamicController.loadHomeDynamic(
////                    getApplicationContext(), time, loadMode);
//
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            bLoading = true;
//        }
//
//        @Override
//        protected void onPostExecute(List<HomeDynamicModel> homeDynamicModels) {
//            super.onPostExecute(homeDynamicModels);
//            try {
//                LogUtils.e("Dynamic onPostExecute");
//                bLoading = false;
//                parallaxListview.stopRefresh();
//                if (null != homeDynamicModels && homeDynamicModels.size() > 0) {
//                    if (loadMode.equals(PREV)) {//
//                        mAdapter.setDatas(mHomeDynamicController.refreshHomeDynamicList(DynamicHomeActivity.this, mAdapter.getDatas(), homeDynamicModels), AddFriendController.SYDynamicFocusFromTypeMiyouQuan);
//                        mHomeDynamicController.saveHomeDynamicListToCache(DynamicHomeActivity.this, mAdapter.getDatas()); // 保存到缓存
//                    } else {
//                        mAdapter.addDatas(homeDynamicModels);
//                    }
//                }
//                handleNoResult();
//                updateFooter(ILoadMoreViewState.COMPLETE);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            bLoading = false;
//        }
//    }

    /**
     * 处理头像动画
     *
     * @param
     */
    private void setAvatar(boolean bclearCache) {
        try {
            UserPhotoManager userPhotoManager = UserPhotoManager.getInstance();
             userPhotoManager.showMyPhoto(this, ivHead,
                    R.drawable.apk_all_usericon, bclearCache, null);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(DynamicData response, final boolean isLoaderMore) {
        try {
            LogUtils.e("Dynamic onPostExecute");
            DynamicContent[] contents = response.content;
            final ArrayList<HomeDynamicModel> myList = new ArrayList<HomeDynamicModel>();
            for (int i=0; i < contents.length; i++) {
                DynamicContent dynamicContent = contents[i];
                HomeDynamicModel model = new HomeDynamicModel();
                model.screenName = dynamicContent.screen_name;
                model.isAllowOperate = dynamicContent.allow_operate;
                model.iconUrl = dynamicContent.avatar;
                model.content = dynamicContent.content;
                model.type = dynamicContent.type;
                model.imagesList  = new ArrayList<String>();
                for (int j=0; j < dynamicContent.images.length; j++) {
                    model.imagesList.add(dynamicContent.images[j]);
                }
                myList.add(model);
            }
            final List<HomeDynamicModel> homeDynamicModels = myList;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bLoading = false;
                    parallaxListview.stopRefresh();
                    if (null != homeDynamicModels && homeDynamicModels.size() > 0) {
                        if (!isLoaderMore) {//
                            // mAdapter.setDatas(mHomeDynamicController.refreshHomeDynamicList(DynamicHomeActivity.this, mAdapter.getDatas(), homeDynamicModels), AddFriendController.SYDynamicFocusFromTypeMiyouQuan);
                            mHomeDynamicController.saveHomeDynamicListToCache(myActivity, mAdapter.getDatas()); // 保存到缓存
                        } else {
                            mAdapter.addDatas(homeDynamicModels);
                        }
                    }
                    handleNoResult();
                    updateFooter(ILoadMoreViewState.COMPLETE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Handler mHandler = new Handler();
    @Override
    public void onFail() {

    }

    public void onEventMainThread(DynamicDetailClickPraiseEvent event) {
        if (event == null) {
            return;
        }

    }
}