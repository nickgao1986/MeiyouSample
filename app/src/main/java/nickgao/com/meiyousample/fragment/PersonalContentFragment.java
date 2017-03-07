package nickgao.com.meiyousample.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;
import com.lingan.seeyou.ui.view.ScrollableLayout;

import nickgao.com.meiyousample.R;

/**
 * 个人主页内容的adapter
 *
 * @author kahn chaisen@xiaoyouzi.com
 * @version 1.0
 * @ClassName
 * @date 2017/2/24
 * @Description
 */
public class PersonalContentFragment extends Fragment {


    protected Activity mActivity;
    protected NewsHomeParallaxListview mListview;
    protected int activityObjectId;
    protected int userId;
    protected View footer;
    protected LoadingView loadingView;
    private View mListViewHeader;
    private ImageView ivPersonalBg;
    private ScrollableLayout news_home_scroll_layout;
    private boolean isVisible;
    RelativeLayout rl_loadding;
    RelativeLayout rl_update;
    protected boolean mIsRefreshHeader;
    private View rootView;


    public static Fragment newInstance(int id, int type, String name, int position, int currentSelectedPage, String url) {
        PersonalContentFragment classifyFragment = create(type);

        Bundle bundle = new Bundle();
        bundle.putInt("classifyId", id);
        bundle.putInt("classifyType", type);
        bundle.putString("classifyName", name);
        bundle.putInt("position", position);
        bundle.putInt("currentSelectedPage", currentSelectedPage);
        bundle.putString("url", url);
        classifyFragment.setArguments(bundle);

        return classifyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.layout_personal_content_fragment,null);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private static PersonalContentFragment create(int type) {
        PersonalContentFragment fragment = new PersonalContentDynamicFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initView();
        initHeadUI();
    }

    private void init() {
        mActivity = getActivity();
    }



    private void initView() {
        mListview = (NewsHomeParallaxListview) rootView.findViewById(R.id.news_home_listview);

        loadingView = (LoadingView) rootView.findViewById(R.id.news_home_loadingView);
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //没在滚动，并且到底，触发获取更多
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && view.getLastVisiblePosition() == (view
                        .getCount() - 1)) {
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }


    private void initHeadUI() {
        mListViewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.layout_news_home_head_animation, null);
        mListview.addHeaderView(mListViewHeader);
        RelativeLayout rlSelectCity = (RelativeLayout) mListViewHeader.findViewById(R.id.rlSelectCity);
        TextView tvSelectCity = (TextView) mListViewHeader.findViewById(R.id.tvSelectCity);
        rlSelectCity.setVisibility(View.GONE);

        // 加入头部
        rl_loadding = (RelativeLayout) mListViewHeader.findViewById(R.id.rl_loadding);
        rl_update = (RelativeLayout) mListViewHeader.findViewById(R.id.rl_update);
        mListview.setScaleView(ivPersonalBg);
        mListview.setLoaddingView(rl_update, rl_loadding, news_home_scroll_layout);
        if (isVisible) {
            setViews();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            setViews();
        } else {
            isVisible = false;
        }
    }

    /**
     * listview头部的控件传到外层去做
     */
    private void setViews() {
        if (news_home_scroll_layout == null || rl_update == null || rl_loadding == null || ivPersonalBg == null)
            return;
        news_home_scroll_layout.setLoaddingView(rl_update, rl_loadding, ivPersonalBg);
        news_home_scroll_layout.setOnRefreshListener(new NewsHomeParallaxListview.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                //pullDownRefresh();//下拉刷新
            }
        });
    }

}
