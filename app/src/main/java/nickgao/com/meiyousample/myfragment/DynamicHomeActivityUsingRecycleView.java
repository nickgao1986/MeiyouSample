package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.os.Bundle;
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

import fragment.PeriodBaseFragment;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.HomeDynamicMultiItemAdapter;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.HomeDynamicModel;


/**
 * 密友圈
 */
public class DynamicHomeActivityUsingRecycleView extends PeriodBaseFragment implements IRecycleViewLoaderView {

    private String TAG = "DynamicHomeActivity";
    private Activity myActivity;
    private HomeDynamicMultiItemAdapter mAdapter;

    private int mLvCurrentPosition = 0;
    private boolean isKeyboardShow;
    private RecyclerView mRecyclerView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadingView loadingView;
    private LinearLayoutManager mLayoutManager;
    ArrayList<HomeDynamicModel> myList = new ArrayList<HomeDynamicModel>();
    private RecycleViewLoader mViewLoader;
    private DynamicModel mDynamicModel = new DynamicModel(myList);
    private ImageView backgroudView;
  //  private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected int getLayout() {
        return R.layout.personal_fragment_item_using_recycleview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i("DynamicHomeActivity onActivityCreated");
        getTitleBar().setCustomTitleBar(-1);
        View view = getRootView();
        //mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipRefreshlayout);
        loadingView = (LoadingView) view.findViewById(R.id.loadingView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        backgroudView = (ImageView) view.findViewById(R.id.fruit_image_view);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();

        mAdapter = new HomeDynamicMultiItemAdapter(myActivity, myList);
        mLayoutManager = new LinearLayoutManager(myActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener(myActivity));
        mRecyclerView.setAdapter(mAdapter);

        mViewLoader = new RecycleViewLoader(this, mDynamicModel);
        mViewLoader.setListViewHelper(new RecyclerViewHelper(mRecyclerView));
        mViewLoader.setLoadStateHelper(new LoadStateHelper(mRecyclerView, loadingView));
      //  mViewLoader.setRefreshViewHelper(new RecycleViewRefreshViewHelper(mSwipeRefreshLayout));

        mViewLoader.setOnLoadFailureListener(new OnNewsDetailLoadFailureListener(myActivity));
        mViewLoader.setOnLoadSuccessListener(new OnLoadListSuccessListener<DynamicModel>() {
            @Override
            public void onSuccess(boolean isRefreshing, DynamicModel response) {
                myList = (ArrayList<HomeDynamicModel>) response.mList;
                mAdapter.notifyDataSetChanged();
            }
        });

        mViewLoader.load(true);

    }


    @Override
    public void onAttach(Activity activity) {
        LogUtils.i("DynamicHomeActivity onAttach");

        super.onAttach(activity);
        myActivity = activity;
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