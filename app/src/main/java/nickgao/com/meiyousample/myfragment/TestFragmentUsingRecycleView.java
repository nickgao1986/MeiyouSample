package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CircleShapeImageView;
import com.lingan.seeyou.ui.view.ScrollableLayout;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.PersonalTabModel;
import nickgao.com.meiyousample.model.UserHomePage.Tabs;
import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.personal.UserDataListener;
import nickgao.com.meiyousample.service.ServiceFactory;
import nickgao.com.meiyousample.service.UserHomeService;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/6/5.
 */

public class TestFragmentUsingRecycleView extends PeriodBaseActivity implements UserDataListener {

    private int mCurrentPosition;
    private Activity mActivity;
    private ImageView emptyView;
    private ScrollableLayout news_home_scroll_layout;
    private TextView tvName;
    private RelativeLayout rlRank;
    private TextView tvRank;
    private TextView tvHeadMsg;
    private TextView tvAttention, tvFans;
    private RelativeLayout rlAttentionOrFans;
    private TextView tv_publish_dynamic;
    //透明titleBar
    private RelativeLayout rl_custom_title_bar;
    private TextView custom_tv_title;
    private ImageView iv_custom_iv_left, iv_custom_iv_right;
    private TextView tv_PersonalDescription;
    private Button btn_personal_head_attention;
    private RelativeLayout rl_personal_header_without_info_layout;
    private RelativeLayout ll_personal_header_with_info_layout;
    private LinearLayout ll_header_layout;
    boolean isShowPersonalInfo = false;
    private RelativeLayout personal_header_layout;
    private boolean mIsSelected = false;
    private int preCurrentY = 0;
    private Handler mHandler = new Handler();
    private TextView tvFansCount;
    private TextView tvAttentionCount;
    protected LoaderImageView ivBannerBg;
    private CircleShapeImageView imgHead;
    private ImageView mMask;


    @Override
    protected int getLayoutId() {
        return R.layout.test_fragement_using_recycleview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        getTitleBar().setCustomTitleBar(-1);
       // setContentView(R.layout.test_fragement_using_recycleview);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setTitle(R.string.personal);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }


        List<String> tabsTitle = new ArrayList<String>();
        tabsTitle.add("动态");
        tabsTitle.add("帖子");
        tabsTitle.add("回复");

        TopicFragmentUsingRecycleView topicFragment = new TopicFragmentUsingRecycleView();
        topicFragment.setTopicOrReply(TopicFragmentUsingRecycleView.TopicOrReply.Topic);

        TopicFragmentUsingRecycleView replyFragment = new TopicFragmentUsingRecycleView();
        replyFragment.setTopicOrReply(TopicFragmentUsingRecycleView.TopicOrReply.Reply);

        List fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new DynamicHomeActivityUsingRecycleView());
        fragmentList.add(topicFragment);
        fragmentList.add(replyFragment);

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);

        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.addTab(tabs.newTab().setText(tabsTitle.get(0)));
        tabs.addTab(tabs.newTab().setText(tabsTitle.get(1)));
        tabs.addTab(tabs.newTab().setText(tabsTitle.get(2)));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(),fragmentList,tabsTitle);
        viewPager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        AppBarLayout mAppBarLayout = (AppBarLayout)findViewById(R.id.appBar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    LogUtils.d("====state == State.EXPANDED");

                }else if(state == State.COLLAPSED){
                    //折叠状态
                    LogUtils.d("====state == State.COLLAPSED");
                    custom_tv_title.setText(R.string.personal);

                }else {
                    //中间状态

                }
            }
        });


        imgHead = (CircleShapeImageView) findViewById(R.id.ivPersonalHead);
        ivBannerBg = (LoaderImageView) findViewById(R.id.ivPersonalBg);
        mMask = (ImageView)findViewById(R.id.mask);

        btn_personal_head_attention = (Button) findViewById(R.id.btn_personal_head_attention);
        btn_personal_head_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_personal_head_attention.isSelected()) {
                    btn_personal_head_attention.setSelected(false);
                } else {
                    btn_personal_head_attention.setSelected(true);
                }
            }
        });
        btn_personal_head_attention.setAlpha(0);
        ll_personal_header_with_info_layout = (RelativeLayout) findViewById(R.id.rlHeader);
        rl_personal_header_without_info_layout = (RelativeLayout) findViewById(R.id.personal_header_without_info_layout);
        ll_header_layout = (LinearLayout) findViewById(R.id.ll_header_layout);
        //透明titleBar
        rl_custom_title_bar = (RelativeLayout) findViewById(R.id.rl_custom_title_bar);
        rl_custom_title_bar.setAlpha(0);
        rl_custom_title_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        tv_publish_dynamic = (TextView) findViewById(R.id.tv_publish_dynamic);
        tv_PersonalDescription = (TextView) findViewById(R.id.personal_description);
        tvName = (TextView) findViewById(R.id.tvPersonalName);
        rlRank = (RelativeLayout) findViewById(R.id.rlRank);
        tvRank = (TextView) findViewById(R.id.tvRank);
        custom_tv_title = (TextView) findViewById(R.id.custom_tv_title);
        custom_tv_title.setText(R.string.personal);
        custom_tv_title.setAlpha(1);
        iv_custom_iv_left = (ImageView) findViewById(R.id.custom_iv_left);
        iv_custom_iv_right = (ImageView) findViewById(R.id.custom_iv_right);
        tvFansCount = (TextView) findViewById(R.id.tv_fans_count);
        tvAttentionCount = (TextView) findViewById(R.id.tv_att_count);


        sendRequest();
    }


    private void sendRequest() {
        UserHomeService service = (UserHomeService) ServiceFactory.getInstance().getService(UserHomeService.class.getName());
        service.sendRequest(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public void onSuccess(final UserHomePage response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initTabs(response.tabs);
                personalData(response);
                loadBg(response);
                loadAvatar(response);
            }
        });
    }

    private void loadBg(final UserHomePage response) {
        ImageLoader.getInstance().displayImage(mActivity.getApplicationContext(),
                ivBannerBg, response.banner,
                0, 0, 0, 0,
                false, DeviceUtils.getScreenWidth(mActivity.getApplicationContext()), DeviceUtils.dip2px(mActivity.getApplicationContext(), 200), new ImageLoader.onCallBack() {
                    @Override
                    public void onSuccess(ImageView imageView, Bitmap bitmap, String s, Object... objects) {
                        mMask.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFail(String s, Object... objects) {

                    }

                    @Override
                    public void onProgress(int i, int i1) {

                    }

                    @Override
                    public void onExtend(Object... objects) {

                    }
                });
    }

    private void loadAvatar(final UserHomePage response) {
        ImageLoader.getInstance().loadImage(mActivity.getApplicationContext(), response.avatars, 0, 0, new ImageLoader.onCallBack() {
            @Override
            public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                if (bitmap != null)
                    imgHead.setImageBitmap(bitmap);
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
        });

    }

    private void personalData(final UserHomePage response) {
        if (!StringUtils.isNull(response.screen_name)) {
            tvName.setText(response.screen_name);
        } else {
            tvName.setText("还没设置昵称哦~");
        }
        setRank(response);
        tvFansCount.setText("" + response.fans);
        tvAttentionCount.setText("" + response.follows);
    }

    private void setRank(final UserHomePage response) {
        if (response.userrank != -1 && response.userrank >= 0) {
            if (rlRank.getVisibility() == View.GONE) {
                rlRank.setVisibility(View.VISIBLE);
            }
            tvRank.setText("LV" + response.userrank);
        } else {
            if (rlRank.getVisibility() == View.VISIBLE) {
                rlRank.setVisibility(View.GONE);
            }
        }
    }


    private void initTabs(Tabs[] tabs) {
        List<PersonalTabModel> tabModels = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            PersonalTabModel model = new PersonalTabModel();
            model.id = i;
            model.index = i;
            model.name = tabs[i].name;
            model.url = tabs[i].url;
            model.type = tabs[i].type;
            tabModels.add(model);
        }
       // updateViewPager(tabModels);

    }


    @Override
    public void onFail() {

    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
