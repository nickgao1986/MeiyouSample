package nickgao.com.meiyousample.myfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/6/5.
 */

public class TestFragmentUsingRecycleView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_fragement_using_recycleview);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.personal);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }


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

                }else if(state == State.COLLAPSED){
                    //折叠状态

                }else {
                    //中间状态

                }
            }
        });


    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
}
