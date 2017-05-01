package nickgao.com.meiyousample.friend;

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
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.personal.ILoadMoreViewState;
import nickgao.com.meiyousample.utils.StringUtil;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * 名人好友界面
 * Created by Administrator on 14-7-11.
 */
public class FamousPersonFriendFragment extends PeriodBaseFragment implements View.OnClickListener {
    private PullToRefreshListView listView;
    private ListView mListView;
    private LoadingView loadingView;
    private Activity mActivity;
    private FamousPersonFriendAdapter adapter;
    public List<AddFriendModel> models_FamousPersons = new ArrayList<AddFriendModel>();//强制关注推荐好友models

    // 列表底部
    private View moreView;
    private ProgressBar moreProgressBar;
    private TextView loadMoreView;
    private int visibleLastIndex = 0; // 最后的可视项索引
    // 是否正在加载
    private boolean mIsLoadingMore = false;

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
        setLisener();
    }

    private void intUI() {
        getTitleBar().setCustomTitleBar(-1);
        listView = (PullToRefreshListView) getRootView().findViewById(R.id.listview);
        mListView = listView.getRefreshableView();
        loadingView = (LoadingView) getRootView().findViewById(R.id.loadingView);
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
        // 列表底部
        moreView = ViewFactory.from(getActivity()).getLayoutInflater().inflate(R.layout.listfooter_more, null);
        moreProgressBar = (ProgressBar) moreView.findViewById(R.id.pull_to_refresh_progress);
        loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
        moreProgressBar.setVisibility(View.GONE);
        moreView.setVisibility(View.INVISIBLE);
        // 空底部
        LinearLayout linearFoot = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        linearFoot.addView(moreView, layoutParams);
        // 加入底部
        if (mListView.getFooterViewsCount() == 0) {
            mListView.addFooterView(linearFoot);
        }
    }

    private void intLogic() {
        loadFamousPersonFriend(true);
    }

    /**
     * 获取名人好友
     */
    private void loadFamousPersonFriend(boolean bShowLoading) {
        if (bShowLoading) {
            models_FamousPersons.clear();
            models_FamousPersons.addAll(AddFriendController.getInstance(mActivity).getListFromCache(AddFriendController.FAMOUS_PERSON_FILE));
            adapter = new FamousPersonFriendAdapter(mActivity, models_FamousPersons);
            mListView.setAdapter(adapter);
            if (models_FamousPersons.size() == 0) {
                loadingView.setStatus(getActivity(), LoadingView.STATUS_LOADING);
            } else {
                loadingView.hide();
                listView.setRefreshing(true);
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
                    models_FamousPersons.clear();
                    models_FamousPersons.addAll(models);
                    if (adapter == null) {
                        adapter = new FamousPersonFriendAdapter(mActivity, models_FamousPersons);
                        mListView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
                listView.onRefreshComplete();
                loadingView.hide();
                handleNoResult(models);
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

    private void setLisener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (models_FamousPersons.size() == 0) {
                    return;
                }
                int lastIndex = adapter.getCount() - 1;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && !mIsLoadingMore && visibleLastIndex == lastIndex) {
                    mIsLoadingMore = true;
                    updateFooter(ILoadMoreViewState.LOADING);
                    loadFamousPersonsMoreFriendData(models_FamousPersons.get(lastIndex).page + 1);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem - 2 + visibleItemCount;
            }
        });
        loadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFamousPersonFriend(true);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFamousPersonFriend(false);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    if (models_FamousPersons.size() == 0)
//                        return;
//                    final AddFriendModel model = models_FamousPersons.get(position);
//                    if (model != null) {
//                        PersonalActivity.toPersonalIntent(mActivity, model.fuid, AddFriendController.SYDynamicFocusFromTypeRecom, new OnFollowListener() {
//                            @Override
//                            public void OnFollow(final int isFollow) {
//                                model.followStatus = isFollow;
//                                AddFriendController.getInstance(mActivity).updateModels(model, AddFriendController.FAMOUS_PERSON_FILE);
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private List<AddFriendModel> loadFamousPersonFriend() {
        final String response = getFromAssets("viprecom.txt");
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




    /**
     * 上拉加载更多
     */
    private void loadFamousPersonsMoreFriendData(final int page) {
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
                    models_FamousPersons.addAll(models);
                    if (adapter == null) {
                        adapter = new FamousPersonFriendAdapter(mActivity, models_FamousPersons);
                        mListView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    updateFooter(ILoadMoreViewState.NORMAL);
                } else {
                    updateFooter(ILoadMoreViewState.COMPLETE);
                }
                listView.onRefreshComplete();
            }
        });
    }
    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
