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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lingan.seeyou.ui.view.ListFooterUtil;
import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.ScrollableLayout;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

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
public abstract class PersonalContentFragment extends Fragment {


    protected Activity mActivity;
    protected ListView mListview;
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

    public boolean bLoading = false;//是不是在加载
    enum LoadingState {
        LOADING_NEW_DATA,LOADING_MORE,NO_DATA,LOADING_COMPLETE,NO_NETWORK,FOOTER_COMPLETE,NETWORK_ERROR,PULL_BLACK
    }

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
        PersonalContentFragment fragment = null;
        if(type == -1) {
            fragment = new PersonalContentDynamicFragment();
        }else if(type == 7) {
            fragment = new TopicFragment();
        }else if(type == 8) {
            fragment = new ReplyFragment();
        }else if(type == 9) {
            fragment = new PersonalContentDynamicFragment();
        }
        return fragment;
    }


    abstract void sendRequest();
    abstract void subClassLoadMore();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initView();

    }

    private void init() {
        mActivity = getActivity();
    }



    private void initView() {
        mListview = (ListView) rootView.findViewById(R.id.news_home_listview);
        footer = ListFooterUtil.getInstance().getListViewFooter(ViewFactory.from(mActivity.getApplicationContext()).getLayoutInflater());
        mListview.addFooterView(footer);

        loadingView = (LoadingView) rootView.findViewById(R.id.news_home_loadingView);
        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //没在滚动，并且到底，触发获取更多
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && !bLoading && view.getLastVisiblePosition() != 0 && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    loadMore();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    protected void loadMore() {
        if (bLoading)
            return;
        bLoading = true;
        subClassLoadMore();
    }

    protected void setLoadingState(LoadingState state) {
        switch (state) {
            case LOADING_NEW_DATA:
                loadingView.setStatus(LoadingView.STATUS_LOADING);
                ListFooterUtil.getInstance().hideListViewFooter(footer);
                break;
            case LOADING_MORE:
                loadingView.setStatus(LoadingView.STATUS_HIDDEN);
                ListFooterUtil.getInstance().updateListViewFooter(footer, ListFooterUtil.ListViewFooterState.LOADING, "正在加载更多...");
                break;
            case NO_DATA:
                loadingView.setStatus(LoadingView.STATUS_NODATA);
                loadingView.setContent(mActivity, LoadingView.STATUS_NODATA,"她还未发布任何动态哦~");
                ListFooterUtil.getInstance().hideListViewFooter(footer);
                break;
            case LOADING_COMPLETE: //一次请求完
                loadingView.setStatus(LoadingView.STATUS_HIDDEN);
                break;
            case FOOTER_COMPLETE: //当拉more的时候没获取数据
                loadingView.setStatus(LoadingView.STATUS_HIDDEN);
                ListFooterUtil.getInstance().updateListViewFooter(footer, ListFooterUtil.ListViewFooterState.COMPLETE, "没有更多数据啦~~~");
                break;
            case NETWORK_ERROR:
                Toast.makeText(mActivity,"亲，这次请求数据失败了",Toast.LENGTH_SHORT).show();
                break;
            case NO_NETWORK:
                loadingView.setStatus(LoadingView.STATUS_NONETWORK);
                ListFooterUtil.getInstance().hideListViewFooter(footer);
                break;
            case PULL_BLACK:
                loadingView.setStatus(LoadingView.STATUS_NODATA);
                loadingView.setContent(mActivity, LoadingView.STATUS_NODATA, "你没有权限查看~");
                ListFooterUtil.getInstance().hideListViewFooter(footer);

                break;

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

    }


}
