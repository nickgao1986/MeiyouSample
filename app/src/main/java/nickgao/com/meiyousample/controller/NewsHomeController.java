package nickgao.com.meiyousample.controller;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.IconTextSpan;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.firstPage.HomeType;
import nickgao.com.meiyousample.firstPage.NewsType;
import nickgao.com.meiyousample.firstPage.TalkModel;
import nickgao.com.meiyousample.firstPage.module.IOnExcuteListener;
import nickgao.com.meiyousample.firstPage.module.RecommendTopicResponeModel;
import nickgao.com.meiyousample.firstPage.view.ScrollableLayout;

/**
 * Created by gaoyoujian on 2017/3/7.
 */

public class NewsHomeController extends LinganController {

    private NewsHomeController() {
    }

    public final static String HOME_RECOMMEND_CACHE_FILE = "home_recommend_list";

    public static NewsHomeController getInstance() {
        return new NewsHomeController();
    }
    private boolean isTopicLoading;//是否正在加载

    private final String TAG = "NewsHomeController";
    private boolean mOnRefresh = false;//是否在刷新 外层view和里层共用一个变量值 为了不会产生差异
    private String[] noDataHint = {"暂无更新内容~", "暂无更新内容，去柚子街逛逛吧~", "暂无更新内容，去她她圈看看吧~"};//服务端没有给数据的时候，随机选择一个提示
    private int currentScrollY;//滚动位置


    private List<TalkModel> listRecommonTabTemp = new ArrayList<>();//第一次请求和tab是推荐的时候临时内存缓存
    public void setCurrentScrollY(int currentScrollY) {
        this.currentScrollY = currentScrollY;
    }

    public int getCurrentScrollY() {
        return currentScrollY;
    }

    //当前是第几个tab页
    private int currentPagePosition;

    private int pullDownCount, flipCount;
    private boolean enablePullToFlip = true;//是否翻转花朵和怀孕几率
    private Map<Integer, RecommendTopicResponeModel> classifyFragmentsIsLoadingDatas = new HashMap<>();//保存某个tab下面的数据

    private List<TalkModel> bgTalkModels = new ArrayList<>();//首页曝光过的数据源

    public void setFlipCount(int flipCount) {
        this.flipCount = flipCount;
    }

    public void setEnablePullToFlip(boolean enablePullToFlip) {
        this.enablePullToFlip = enablePullToFlip;
    }



    /**
     * 读出分类1下面的缓存数据
     */
    public void loadHomeCatIdCache(final Context context, final int catid) {
        submitLocalTask("loadHomeCatIdCache" + catid, new Runnable() {
            @Override
            public void run() {
                final RecommendTopicResponeModel communityResponeModel = new RecommendTopicResponeModel();
               // communityResponeModel.list = filteReadedRecommend(context, HomeRecommendCacheController.getInstance().getHomeRecommendToCache(context, catid));
                communityResponeModel.list = getHomeRecommendToCache(context,catid);
                classifyFragmentsIsLoadingDatas.put(catid, communityResponeModel);//保存起来
            }
        });
    }


    /**
     * 首页数据的缓存
     */
    public List<TalkModel> getHomeRecommendToCache(final Context context, int catid) {
//        try {
//            int homeType = 3;
//            List<TalkModel> list = FastPersistenceDAO.getObjectList(context, HOME_RECOMMEND_CACHE_FILE + homeType + catid + UserController.getInstance().getUserId(context), TalkModel.class);
//            return list;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return new ArrayList<>();
        String str = getFromAssets("talk.txt",context);
        try {
            JSONObject object = new JSONObject(str);
            RecommendTopicResponeModel model = new RecommendTopicResponeModel(context, object);
            List<TalkModel> list = model.list;
            return list;
        }catch (Exception ex) {

        }

        return new ArrayList<>();
    }

    public String getFromAssets(String fileName,final Context context){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setClassifySelect(int position) {
        currentPagePosition = position;
    }


    /**
     * 首页动画效果
     *
     * @return
     */
    public boolean isOnRefresh() {
        return mOnRefresh;
    }

    public void setOnRefresh(boolean mOnRefresh) {
        this.mOnRefresh = mOnRefresh;
    }

    public CharSequence getSpannableTitle(Context mContext, TalkModel talk) {
        if (talk.recomm_type == HomeType.TYPE_SPECIAL_TOPIC || (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC_CARD && talk.attr_type == HomeType.HOME_SPECIAL_BLEND)) {//专题类型标题前面要加一个专题的icon
            //专题
            IconTextSpan iconTextSpan = new IconTextSpan(mContext, R.color.red_bt, "专题");
            iconTextSpan.setRightMarginDpValue(5);
            iconTextSpan.setTextSizeSpValue(11, false);
            iconTextSpan.setTextColorResId(R.color.white_a);
            iconTextSpan.setBgHeightDpValue(17);
            iconTextSpan.setBgWidthDpValue(28);
            SpannableString spannableString = new SpannableString(" " + talk.title);
            spannableString.setSpan(iconTextSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            return talk.title;
        }
    }

    public boolean isSetWidthAndHeight(TalkModel talk) {
        boolean isSetWidthAndHeight = false;
        if (talk.news_type != NewsType.NEWS_IMAGE.getNewsType() && talk.news_type != NewsType.NEWS_VIDEO.getNewsType()
                && talk.attr_type != HomeType.HOME_SPECIAL_BLEND && talk.attr_type != HomeType.HOME_SPECIAL_BIG_IMG) {//纯图片 纯视频类型 专题大图和混合
            isSetWidthAndHeight = true;
        }
        return isSetWidthAndHeight;
    }

    /**
     * 获取textview行数
     *
     * @param textView
     * @param title
     * @param maxTextViewWidth
     * @return
     */
    public int getTextViewLineCount(TextView textView, CharSequence title, int maxTextViewWidth) {
        try {
            StaticLayout e = new StaticLayout(title, textView.getPaint(), maxTextViewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            int lines = e.getLineCount();
            return lines;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }


    public void loadCommunityDataFromCache(final Activity context, final int catid, final IOnExcuteListener onExcuteListener) {
        if (catid == HomeType.RECOMMEND_ID && listRecommonTabTemp.size() > 0) {
            final RecommendTopicResponeModel communityResponeModel = new RecommendTopicResponeModel();
            communityResponeModel.list = listRecommonTabTemp;
            if (null != onExcuteListener) {
                onExcuteListener.onResult(communityResponeModel);
            }
        } else {
          //  RecommendTopicResponeModel recommendTopicResponeModel = classifyFragmentsIsLoadingDatas.get(HomeType.RECOMMEND_ID);
            String str = getFromAssets("talk.txt",context);
            RecommendTopicResponeModel recommendTopicResponeModel = null;
            try {
                JSONObject object = new JSONObject(str);
                recommendTopicResponeModel = new RecommendTopicResponeModel(context, object);
            }catch (Exception ex) {

            }
            if (recommendTopicResponeModel != null) {//说明内存里面有缓存了直接拿
                if (null != onExcuteListener) {
                    onExcuteListener.onResult(recommendTopicResponeModel);
                }
            }
        }
    }


    public void handleAlpha(Activity mActivity, ScrollableLayout scrollableLayout, RelativeLayout rl_news_home_sliding_tab, RelativeLayout rlHomeTitleBar, RelativeLayout rlLeft, RelativeLayout rlQian, TextView home_title, int halfHeight) {
        try {
            float alpha = 1f;
            if (scrollableLayout.getCurY() > halfHeight) {
                alpha = 1 - getAlpha(scrollableLayout, halfHeight);
                rl_news_home_sliding_tab.setVisibility(View.VISIBLE);
                rlLeft.setVisibility(View.GONE);
                rlQian.setVisibility(View.GONE);
                home_title.setVisibility(View.GONE);
            } else {
                rlLeft.setVisibility(View.VISIBLE);
                rlQian.setVisibility(View.VISIBLE);
                home_title.setVisibility(View.VISIBLE);
            }
            rlHomeTitleBar.setAlpha(alpha);

            if (alpha >= 1) {
                rl_news_home_sliding_tab.setVisibility(View.GONE);
//                StatusBarController.getInstance().setStatusTextColor(mActivity, false);
//                StatusBarController.getInstance().setStatusBarDefaultColor(mActivity);
            } else if (alpha <= 0) {
               // NewsHomeController.getInstance().setStatusColor(mActivity);
                rl_news_home_sliding_tab.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取首页移动的时候透明度
     *
     * @return
     */
    public float getAlpha(ScrollableLayout scrollableLayout, int halfHeight) {
        float alpha = 0;
        int scrollY = scrollableLayout.getCurY() - halfHeight;
        if (scrollY >= 0) {
            alpha = (float) scrollY / (float) halfHeight;
            if (alpha > 1.0f) {
                alpha = 1.0f;
            }
        }
        return alpha;
    }
}
