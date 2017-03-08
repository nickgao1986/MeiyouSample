package nickgao.com.meiyousample;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.HomeSlidingTabLayout;
import com.lingan.seeyou.ui.view.HomeSlidingTabStrip;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;
import com.lingan.seeyou.ui.view.NewsHomeViewPager;
import com.lingan.seeyou.ui.view.ScrollableLayout;
import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.PersonalFragmentPagerAdapter;
import nickgao.com.meiyousample.model.PersonalTabModel;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;


/**
 * Created by gaoyoujian on 2017/3/7.
 */

public class MainFragment extends Fragment {

    private View mRootView;
    HomeSlidingTabLayout news_home_sliding_tab;
    NewsHomeParallaxListview listView;
    PersonalFragmentPagerAdapter homePagerAdapter;
    private NewsHomeViewPager news_home_viewpager;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main,null);
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initTabModel();
        LogUtils.d("===onActivityCreated");
        iv_custom_iv_right.post(new Runnable() {
            @Override
            public void run() {
                showPromoteDialog();
            }
        });

    }

    private boolean mIsSelected = false;
    private void initView() {
        news_home_viewpager = (NewsHomeViewPager) mRootView.findViewById(R.id.news_home_viewpager);
        news_home_sliding_tab = (HomeSlidingTabLayout) mRootView.findViewById(R.id.news_home_sliding_tab);
        news_home_sliding_tab.setCustomTabView(R.layout.layout_home_classify_tab_item, R.id.homeTab);
        news_home_sliding_tab.setIsDrawDiver(true);
        emptyView = (ImageView)mRootView.findViewById(R.id.empty_view);
        btn_personal_head_attention = (Button)mRootView.findViewById(R.id.btn_personal_head_attention);
        btn_personal_head_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_personal_head_attention.isSelected()) {
                    btn_personal_head_attention.setSelected(false);
                }else{
                    btn_personal_head_attention.setSelected(true);
                }
            }
        });
        //透明titleBar
        rl_custom_title_bar = (RelativeLayout) mRootView.findViewById(R.id.rl_custom_title_bar);
        rl_custom_title_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        tv_publish_dynamic = (TextView)mRootView.findViewById(R.id.tv_publish_dynamic);
        tv_PersonalDescription = (TextView)mRootView.findViewById(R.id.personal_description);
        tvName = (TextView) mRootView.findViewById(R.id.tvPersonalName);
        rlRank = (RelativeLayout) mRootView.findViewById(R.id.rlRank);
        tvRank = (TextView) mRootView.findViewById(R.id.tvRank);
        custom_tv_title = (TextView) mRootView.findViewById(R.id.custom_tv_title);
        iv_custom_iv_left = (ImageView) mRootView.findViewById(R.id.custom_iv_left);
        iv_custom_iv_right = (ImageView) mRootView.findViewById(R.id.custom_iv_right);

        news_home_scroll_layout = (ScrollableLayout) mRootView.findViewById(R.id.news_home_scroll_layout);
        news_home_scroll_layout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {

                int headHeight = DeviceUtils.dip2px(mActivity, 200.0f);
                int titleBarHeight = DeviceUtils.dip2px(mActivity, 50.0f);
                float distance = headHeight - currentY;//curentY是已经向下滑动的距离，headHeight-currentY是还露出的head的距离，然后这个距离小于两倍titlebar的距离
                //的时候开始渐变titlebar
                float f = titleBarHeight*2;
                int height = tv_PersonalDescription.getMeasuredHeight();
                if (distance < f) {
                    //f-distance是大概是200-distance ，所以barAlpha从0增长到1，因为最开始200-200=0
                    float barAlpha = (f-distance)/100.0f;
                    if (barAlpha > 1) {
                        barAlpha = 1;
                    }
                   // rl_custom_title_bar.setAlpha(barAlpha);
//                    if(barAlpha > 0.9) {
//                        tv_publish_dynamic.setBackgroundResource(R.drawable.personal_attention_btn_normal);
//                    }
                }else{

//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                        rl_custom_title_bar.setAlpha(0f);
//                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        LogUtils.d("===onAttach");

        super.onAttach(activity);
        mActivity = activity;
    }

    private void initTabModel() {
        List<PersonalTabModel> tabModels = new ArrayList<>();
        PersonalTabModel model1 = new PersonalTabModel();
        model1.index = 1;
        model1.name = "动态";
        model1.id = 1;

        PersonalTabModel model2 = new PersonalTabModel();
        model2.name = "帖子";
        model2.index = 2;
        model2.id = 2;

        PersonalTabModel model3 = new PersonalTabModel();
        model3.index = 3;
        model3.name = "回复";
        model3.id = 3;

        PersonalTabModel model4 = new PersonalTabModel();
        model4.index = 4;
        model4.name = "视频";
        model4.id = 4;

        tabModels.add(model1);
        tabModels.add(model2);
        tabModels.add(model3);
        tabModels.add(model4);

        updateViewPager(tabModels);

    }

    private void updateViewPager(List<PersonalTabModel> tabModels) {
        try {
            if (homePagerAdapter == null) {
                homePagerAdapter = new PersonalFragmentPagerAdapter(getChildFragmentManager(), tabModels);
                news_home_viewpager.setAdapter(homePagerAdapter);
            } else {
                homePagerAdapter.notifyDataSetChanged();
            }

            news_home_sliding_tab.setViewPager(news_home_viewpager);
            news_home_viewpager.setCurrentItem(mCurrentPosition);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    news_home_scroll_layout.getHelper().setCurrentScrollableContainer(homePagerAdapter.getPositionFragment());
//                }
//            }, 500);
            setTabSelect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选中tab的颜色变化
     */
    private void setTabSelect() {
        try {
            HomeSlidingTabStrip tabStrip = (HomeSlidingTabStrip) news_home_sliding_tab.getChildAt(0);
            if (tabStrip != null) {
                TextView textView = (TextView) tabStrip.getChildAt(mCurrentPosition);
                if (textView != null) {
                    if (textView != null) {
                        SkinManager.getInstance().setTextColor(textView, R.color.red_b);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_19));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPromoteDialog() {
        View windowContentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_personal_guide_popup_win, null);
        final PopupWindow popupWindow = new PopupWindow(windowContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        };
        windowContentView.setOnTouchListener(onTouchListener);
        popupWindow.setAnimationStyle(android.R.style.Animation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        View topView = windowContentView.findViewById(R.id.top_view);
        RelativeLayout.LayoutParams topViewParams = (RelativeLayout.LayoutParams) topView.getLayoutParams();
        topViewParams.topMargin = DeviceUtils.dip2px(mActivity, 9); //调整上边距
        topView.setLayoutParams(topViewParams);

        popupWindow.showAsDropDown(emptyView); //不要尝试使用showAtLocation去定位置，会对不准，有些popupWindow会占状态栏，而有些不会！
    }


}
