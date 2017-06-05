package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.com.meetyou.news.LoadStateHelper;
import com.com.meetyou.news.OnNewsDetailLoadFailureListener;
import com.com.meetyou.news.RecyclerViewHelper;
import com.lingan.seeyou.ui.view.LoadingView;
import com.meetyou.news.view.OnRecycleViewScrollListener;
import com.meetyou.pullrefresh.lib.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import fragment.PeriodBaseFragment;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.topic.MyTopicModel;


/**
 * 密友圈
 */
public class TopicFragmentUsingRecycleView extends PeriodBaseFragment implements IRecycleViewLoaderView {

    private String TAG = "DynamicHomeActivity";
    private Activity myActivity;
    private TopicRecycleViewAdapter mAdapter;

    private int mLvCurrentPosition = 0;
    private boolean isKeyboardShow;
    private RecyclerView mRecyclerView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadingView loadingView;
    private LinearLayoutManager mLayoutManager;
    List myList = new ArrayList<MyTopicModel>();
    private RecycleViewLoader mViewLoader;
    private TopicModel mTopicModel = new TopicModel(myList);

    private ReplyModel mReplyModel = new ReplyModel(myList);
    private ImageView backgroudView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;

    enum TopicOrReply{
        Topic,Reply
    }

    TopicOrReply mTopicOrReply = TopicOrReply.Reply;

    public void setTopicOrReply(TopicOrReply topicOrReply) {
        mTopicOrReply = topicOrReply;
    }

    @Override
    protected int getLayout() {
        return R.layout.personal_fragment_item_using_recycleview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTitleBar().setCustomTitleBar(-1);
        View view = getRootView();
      //  mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipRefreshlayout);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        backgroudView = (ImageView) view.findViewById(R.id.fruit_image_view);
//        myActivity.setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();

        mAdapter = new TopicRecycleViewAdapter(myActivity, myList, TopicRecycleViewAdapter.TopicType.TYPE_PUBLISH);
        mLayoutManager = new LinearLayoutManager(myActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener(myActivity));
        mRecyclerView.setAdapter(mAdapter);

        if(mTopicOrReply == TopicOrReply.Reply) {
            mViewLoader = new RecycleViewLoader(this, mReplyModel);
            mViewLoader.setListViewHelper(new RecyclerViewHelper(mRecyclerView));
            mViewLoader.setLoadStateHelper(new LoadStateHelper(mRecyclerView, loadingView));
           // mViewLoader.setRefreshViewHelper(new RecycleViewRefreshViewHelper(mSwipeRefreshLayout));

            mViewLoader.setOnLoadFailureListener(new OnNewsDetailLoadFailureListener(myActivity));
            mViewLoader.setOnLoadSuccessListener(new OnLoadListSuccessListener<ReplyModel>() {
                @Override
                public void onSuccess(boolean isRefreshing, ReplyModel response) {
                    myList = (ArrayList<MyTopicModel>) response.mList;
                    mAdapter.notifyDataSetChanged();
                }
            });

            mViewLoader.load(true);
        }else{
            mViewLoader = new RecycleViewLoader(this, mTopicModel);
            mViewLoader.setListViewHelper(new RecyclerViewHelper(mRecyclerView));
            mViewLoader.setLoadStateHelper(new LoadStateHelper(mRecyclerView, loadingView));
            //mViewLoader.setRefreshViewHelper(new RecycleViewRefreshViewHelper(mSwipeRefreshLayout));

            mViewLoader.setOnLoadFailureListener(new OnNewsDetailLoadFailureListener(myActivity));
            mViewLoader.setOnLoadSuccessListener(new OnLoadListSuccessListener<TopicModel>() {
                @Override
                public void onSuccess(boolean isRefreshing, TopicModel response) {
                    myList = (ArrayList<MyTopicModel>) response.mList;
                    mAdapter.notifyDataSetChanged();
                }
            });

            mViewLoader.load(true);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myActivity = (FragmentActivity) activity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("DynamicHomeActivity onCreate");


    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            LogUtils.i("DynamicHomeActivity onResume" + mLvCurrentPosition);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDestroy() {
        LogUtils.i("DynamicHomeActivity onDestory");

        super.onDestroy();
    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }

    @Override
    public void destroyLoader() {
        mViewLoader.onDestroy();
    }

    @Override
    public void setDataLoader(RecycleViewLoader loader) {

    }
}