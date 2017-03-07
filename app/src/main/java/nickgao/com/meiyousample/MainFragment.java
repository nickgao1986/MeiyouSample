package nickgao.com.meiyousample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.HomeSlidingTabLayout;
import com.lingan.seeyou.ui.view.HomeSlidingTabStrip;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;
import com.lingan.seeyou.ui.view.NewsHomeViewPager;
import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.PersonalFragmentPagerAdapter;
import nickgao.com.meiyousample.model.PersonalTabModel;

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
    }

    private void initView() {
        news_home_viewpager = (NewsHomeViewPager) mRootView.findViewById(R.id.news_home_viewpager);

        news_home_sliding_tab = (HomeSlidingTabLayout) mRootView.findViewById(R.id.news_home_sliding_tab);
        news_home_sliding_tab.setCustomTabView(R.layout.layout_home_classify_tab_item, R.id.homeTab);
        news_home_sliding_tab.setIsDrawDiver(true);
       // listView = (NewsHomeParallaxListview)mRootView.findViewById(R.id.news_home_listview);

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
}
