package nickgao.com.meiyousample.friend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoadingView;
import com.lingan.seeyou.ui.view.PullToRefreshBase;
import com.lingan.seeyou.ui.view.PullToRefreshListView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import biz.threadutil.ThreadUtil;
import fragment.PeriodBaseFragment;
import nickgao.com.framework.utils.StringUtil;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.personal.ILoadMoreViewState;

/**
 * 感兴趣好友界面
 * Created by Administrator on 14-7-11.
 */
@SuppressLint("ResourceAsColor")
public class InterestFriendFragment extends PeriodBaseFragment {
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private LoadingView loadingView;
    private Activity mActivity;
    private InterestFriendAdapter mAddFriendAdapter;
//    private LinearLayout ll_no_login;
//    private Button btNoLogin;

    // 列表底部
    private View moreView;
    private ProgressBar moreProgressBar;
    private TextView loadMoreView;
    private int visibleLastIndex = 0; // 最后的可视项索引
    // 是否正在加载
    private boolean mIsLoadingMore = false;
    public List<AddFriendModel> models_InterestFriends = new ArrayList<AddFriendModel>();//感兴趣好友models

    @Override
    public int getLayout() {
        return R.layout.layout_follow_recommend_friend;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        intUI();
        intLogic();
        setListenner();
    }

    private void intUI() {
        getTitleBar().setCustomTitleBar(-1);
        refreshListView = (PullToRefreshListView) getRootView().findViewById(R.id.listview);
        listView = refreshListView.getRefreshableView();
        loadingView = (LoadingView) getRootView().findViewById(R.id.loadingView);
//        ll_no_login = (LinearLayout) getRootView().findViewById(R.id.ll_no_login);
//        btNoLogin = (Button) getRootView().findViewById(R.id.btNoLogin);
        initListViewFooter();


//        SkinEngine.getInstance().setViewBackground(getApplicationContext(),findViewById(R.id.btNoLogin),R.drawable.btn_red_selector);
//        SkinEngine.getInstance().setViewBackground(getApplicationContext(),findViewById(R.id.btn_record),R.drawable.btn_red_selector);
//        SkinEngine.getInstance().setViewBackground(getApplicationContext(),findViewById(R.id.rl_follow_recomment_layout),R.drawable.bottom_bg);
//        SkinEngine.getInstance().setViewTextColor(getApplicationContext(),(TextView)findViewById(R.id.empty_des),R.color.black_b);
//        SkinEngine.getInstance().setViewTextColor(getApplicationContext(), (TextView) findViewById(R.id.tvNoLogin), R.color.black_b);
//        SkinEngine.getInstance().setViewTextColor(getApplicationContext(), (Button) findViewById(R.id.btNoLogin), R.color.white_a);
//        SkinEngine.getInstance().setViewTextColor(getApplicationContext(), (Button) findViewById(R.id.btn_record), R.color.white_a);
    }

    private void initListViewFooter() {
        moreView = ViewFactory.from(getActivity()).getLayoutInflater().inflate(R.layout.listfooter_more, null);
        moreProgressBar = (ProgressBar) moreView
                .findViewById(R.id.pull_to_refresh_progress);
        loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
        moreProgressBar.setVisibility(View.GONE);
        moreView.setVisibility(View.INVISIBLE);
        // 空底部
        LinearLayout linearFoot = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearFoot.addView(moreView, layoutParams);
        // 加入底部
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(linearFoot);
        }
    }

    private void intLogic() {
        //ll_no_login.setVisibility(View.GONE);
        refreshListView.setVisibility(View.VISIBLE);
        loadInterestFriendData(true);

    }

    private void setListenner() {
        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInterestFriendData(true);
            }
        });
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInterestFriendData(false);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (models_InterestFriends.size() == 0) {
                    return;
                }
                int lastIndex = mAddFriendAdapter.getCount() - 1;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && !mIsLoadingMore && visibleLastIndex == lastIndex) {
                    mIsLoadingMore = true;
                    updateFooter(ILoadMoreViewState.LOADING);
                    loadInterestMoreFriendData(models_InterestFriends.get(lastIndex).page + 1);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem - 2 + visibleItemCount;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
//        btNoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    private void updateFooter(int state) {
        try {
            switch (state) {
                case ILoadMoreViewState.ERROR:
                    moreView.setVisibility(View.VISIBLE);
                    moreProgressBar.setVisibility(View.GONE);
                    loadMoreView.setText("加载失败！");
                    break;
                case ILoadMoreViewState.LOADING:
                    moreView.setVisibility(View.VISIBLE);
                    moreProgressBar.setVisibility(View.VISIBLE);
                    loadMoreView.setText("正在加载更多...");
                    break;
                case ILoadMoreViewState.NORMAL:
                    moreView.setVisibility(View.GONE);
                    break;
                case ILoadMoreViewState.COMPLETE:
                    moreView.setVisibility(View.VISIBLE);
                    moreProgressBar.setVisibility(View.INVISIBLE);
                    loadMoreView.setText("没有更多好友啦~");
                    break;
                default:
                    break;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 加载感兴趣好友信息
     */
    private void loadInterestFriendData(boolean bShowLoading) {
        if (bShowLoading) {
            models_InterestFriends.clear();
            models_InterestFriends.addAll(AddFriendController.getInstance(mActivity).getListFromCache(AddFriendController.INTEREST_FRIEND_FILE));
            mAddFriendAdapter = new InterestFriendAdapter(mActivity, models_InterestFriends);
            listView.setAdapter(mAddFriendAdapter);
            if (models_InterestFriends.size() == 0) {
                loadingView.setStatus(getActivity(), LoadingView.STATUS_LOADING);
            } else {
                loadingView.hide();
                refreshListView.setRefreshing(true);
            }
        } else {
            loadingView.hide();
        }
        ThreadUtil.addTask(mActivity, new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                return loadFamousPersonFriend();
            }

            @Override
            public void onFinish(Object result) {
                List<AddFriendModel> models = (List<AddFriendModel>) result;
                if (models.size() > 0) {
                    models_InterestFriends.clear();
                    models_InterestFriends.addAll(models);
                    if (mAddFriendAdapter == null) {
                        mAddFriendAdapter = new InterestFriendAdapter(mActivity, models_InterestFriends);
                        listView.setAdapter(mAddFriendAdapter);
                    } else {
                        mAddFriendAdapter.notifyDataSetChanged();
                    }
                }
                refreshListView.onRefreshComplete();
                loadingView.hide();
                handleNoResult(models);
            }
        });
    }

    /**
     * 上拉加载更多
     */
    private void loadInterestMoreFriendData(final int page) {
        ThreadUtil.addTask(mActivity, new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                return loadFamousPersonFriend();
            }

            @Override
            public void onFinish(Object result) {
                mIsLoadingMore = false;
                List<AddFriendModel> models = (List<AddFriendModel>) result;
                if (models.size() > 0) {
                    models_InterestFriends.addAll(models);
                    if (mAddFriendAdapter == null) {
                        mAddFriendAdapter = new InterestFriendAdapter(mActivity, models_InterestFriends);
                        listView.setAdapter(mAddFriendAdapter);
                    } else {
                        mAddFriendAdapter.notifyDataSetChanged();
                    }
                    updateFooter(ILoadMoreViewState.NORMAL);
                } else {
                    updateFooter(ILoadMoreViewState.COMPLETE);
                }
                refreshListView.onRefreshComplete();
            }
        });
    }

    // 处理没数据 和 没网络
    private void handleNoResult(List<AddFriendModel> models) {
        if (models.size() == 0) {
            loadingView.setStatus(getActivity(), LoadingView.STATUS_NODATA);
        } else {
            loadingView.hide();
        }
    }


    private List<AddFriendModel> loadFamousPersonFriend() {
        final String response = getFromAssets("interestrecom.txt");
        List<AddFriendModel> listResult = new ArrayList<AddFriendModel>();

        if (!StringUtil.isNull(response)) {
            try {
                JSONObject object = new JSONObject(response);
                int pages = StringUtils.getJsonInt(object, "page");
                if (object.has("data")) {
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        AddFriendModel model = new AddFriendModel(obj, "", pages);
                        listResult.add(model);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listResult;
    }


    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }

}
