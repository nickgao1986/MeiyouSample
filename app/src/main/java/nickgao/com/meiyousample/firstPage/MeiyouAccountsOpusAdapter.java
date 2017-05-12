package nickgao.com.meiyousample.firstPage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BageImageView;
import com.lingan.seeyou.ui.view.MultiImageView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.controller.NewsHomeController;
import nickgao.com.meiyousample.firstPage.module.OnListViewStatusListener;
import nickgao.com.meiyousample.utils.CalendarUtil;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtil;
import nickgao.com.meiyousample.utils.StringUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * @author kahn chaisen@xiaoyouzi.com
 * @version 1.0
 * @ClassName
 * @date 2017/1/16
 * @Description
 */
public class MeiyouAccountsOpusAdapter extends BaseAdapter {

    private Activity mContext;
    public List<TalkModel> list;
    private LayoutInflater inflater;
    public ListView listview;
    //图片显示的宽度
    private int imageWidth;
    //大图显示的宽度
    private int bigImageWidth;
    public int homeBottomContent = 2;//底部显示得内容：1- 圈子信息   2-用户信息
    public int homeFeedbackButton = 1;//反馈按钮是否显示：1- 显示   0-不显示
    public int homeFeedsImageType = 2;//图片显示方图还是扁图 1-方图 2 扁图
    public int time_view = 1;//时间开关是不是显示 0不显示 1显示
    public int isShowBottomIcon = 1;//是否显示底部icon

    private int leftSpace12, topSpace, bottomSpace5, bottomSpace10;
    private int feedBackHeight;//反馈兰的高度
    private int bottomHeight;
    private int classifyId;//分类id
    public String classifyName;//分类名字
    public int screenWidth, videoHeight;
    public String onlyVideoId;//视频要用的唯一id

    private OnRealPositionListener mOnRealPositionListener;
    //TreeMap是按key升序,列表使用Position,其他使用特殊key
    private TreeMap<Integer, OnListViewStatusListener> mOnBesideListViewScrollListener = new TreeMap<>();
    private boolean mUserVisibleHint;//当前界面是否可见
    /**
     * 视频状态信息处理
     */

    public MeiyouAccountsOpusAdapter(Activity context, LayoutInflater inflater, List list, ListView listview, int classifyId, String classifyName, OnRealPositionListener onRealPositionListener) {
        this.classifyId = classifyId;
        this.classifyName = classifyName;
        this.listview = listview;
        this.mContext = context;
        this.list = list;
        this.inflater = inflater;
        this.mOnRealPositionListener = onRealPositionListener;

        time_view = 1/*DataSaveHelper.getInstance(SeeyouApplication.getContext()).getHomeTimeView()*/;

        screenWidth = DeviceUtils.getScreenWidth(SeeyouApplication.getContext());
        videoHeight = (int) (screenWidth * 360 / 640.0f);

        this.imageWidth = (screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 24) - DeviceUtils.dip2px(SeeyouApplication.getContext(), 3 * 2)) / 3;
        bigImageWidth = screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 24);
        leftSpace12 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 12);
        topSpace = DeviceUtils.dip2px(SeeyouApplication.getContext(), 11);
        bottomSpace5 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 5);
        bottomSpace10 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 10);
        feedBackHeight = DeviceUtils.dip2px(SeeyouApplication.getContext(), 28);
        bottomHeight = screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 39) - imageWidth - leftSpace12;
    }

    public interface OnRealPositionListener {
        int getRealPosition(int position);
    }

    public void setOnlyVideoIdAndVisible(String onlyVideoId, boolean mUserVisibleHint) {
        this.mUserVisibleHint = mUserVisibleHint;
        this.onlyVideoId = onlyVideoId;
    }

    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {
        TalkModel model = list.get(position);
        if (model == null)
            return HomeType.HOME_LAYOUT_TYPE_NORMAL;
        if (model.recomm_type == HomeType.TYPE_SEPARATOR_BAR) {//分隔栏类型
            return HomeType.HOME_LAYOUT_TYPE_SEPARATORBAR;
        } else if (model.news_type == NewsType.NEWS_VIDEO.getNewsType() && model.feeds_play == HomeType.HOME_VIDEO_PLAY) {//纯视频类型
            return HomeType.HOME_LAYOUT_TYPE_VIDEO;
        } else if (model.recomm_type == HomeType.TYPE_YIMA_GO || model.recomm_type == HomeType.TYPE_YIMA_COME) {//姨妈卡片类型
            return HomeType.HOME_LAYOUT_TYPE_YIMA;
        } else {//正常的话题和资讯数据
            return HomeType.HOME_LAYOUT_TYPE_NORMAL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return HomeType.TYPE_COUNT;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        try {
            ViewHolder vHolder;
            int layoutType = getItemViewType(position);
            if (view == null) {
                vHolder = new ViewHolder();
                view = getItemTypeView(layoutType, arg2);
                vHolder.init(view, layoutType);
                view.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) view.getTag();
                if (vHolder == null) {
                    vHolder = new ViewHolder();
                    view = getItemTypeView(layoutType, arg2);
                    vHolder.init(view, layoutType);
                    view.setTag(vHolder);
                }
            }
            final TalkModel talk = list.get(position);
            if (talk == null) {
                return view;
            }
            //孕妈圈feeds样式
            if (talk.recomm_type == HomeType.TYPE_SEPARATOR_BAR) {//分隔栏布局
                handleSeparatorBar(vHolder);
            } else if (talk.news_type == NewsType.NEWS_VIDEO.getNewsType() && talk.feeds_play == HomeType.HOME_VIDEO_PLAY) {//在列表中可播放的视频样式
                handlePlayVideo(vHolder, talk, position);
            } else if (talk.recomm_type == HomeType.TYPE_YIMA_GO || talk.recomm_type == HomeType.TYPE_YIMA_COME) {
                //经期卡片大姨妈
                // handleYimaData(vHolder, talk);
            } else {
                handleTopicType(vHolder, talk);
            }
            setListener(vHolder, talk, position, layoutType);//点击事件
            //exposureStatistics(talk, position);//曝光统计
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }


    /**
     * 可播放的视频类型
     */
    private void handlePlayVideo(final ViewHolder viewHolder, final TalkModel talkModel, int position) {
//            if (mContext == null)
//                return;
//            NewsHomeVideoController.getInstance().handleVideo(mContext, this, viewHolder, talkModel, getVideoPlayStatus(mContext, talkModel, onlyVideoId), mOnRealPositionListener != null ? mOnRealPositionListener.getRealPosition(position) : position, new OnHomeCallBackListener() {
//                @Override
//                public void OnUpdateCollect(Object object) {
//                    int is_favorite = (int) object;
//                    handleUpdateCollectData(talkModel, is_favorite);
//                }
//
//                @Override
//                public void OnUpdateNoLike(Object object) {
//                    closeFeedBack(viewHolder, talkModel);
//                }
//
//            });
    }

    /**
     * 收藏的时候刷新数据
     */
    private void handleUpdateCollectData(TalkModel talkModel, int is_favorite) {
        for (TalkModel model : list) {
            if (model.id == talkModel.id) {
                model.is_favorite = is_favorite;
                break;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 显示分隔栏
     */
    private void handleSeparatorBar(ViewHolder vHolder) {
        vHolder.rlSeparatorBar.setVisibility(View.VISIBLE);
    }

    /**
     * 资讯贴和话题正常数据类型
     *
     * @param vHolder
     * @param talk
     */

    private void handleTopicType(final ViewHolder vHolder, final TalkModel talk) {
        try {
            //标题
            vHolder.tv_topic_title_two.setText(NewsHomeController.getInstance().getSpannableTitle(SeeyouApplication.getContext(), talk));
//            if (talk.news_type == NewsType.NEWS_VIDEO.getNewsType()) {
//                vHolder.tv_topic_title_two.setMinLines(2);
//            } else {
//                vHolder.tv_topic_title_two.setMinLines(-1);
//            }

            if (talk.isRead) {//已读
                SkinManager.getInstance().setTextColor(vHolder.tv_topic_title_two, R.color.black_b);
            } else {
                SkinManager.getInstance().setTextColor(vHolder.tv_topic_title_two, R.color.black_at);
            }
            handleAvatarAndName(vHolder, talk);//头像和名字
            //评论
            if (talk.review_count == 0) {//需求是等宇0的时候隐藏这个控件
                vHolder.tv_comment_count_two.setVisibility(View.GONE);
                //  vHolder.ic_comment_count_two.setVisibility(View.GONE);
            } else {
                vHolder.tv_comment_count_two.setText(StringUtil.getFiveNum(talk.review_count)+"回复");
                vHolder.tv_comment_count_two.setVisibility(View.VISIBLE);
                // vHolder.ic_comment_count_two.setVisibility(View.VISIBLE);
            }
            //时间
            vHolder.tv_time_two.setVisibility(View.VISIBLE);
            vHolder.tv_time_two.setText(CalendarUtil.convertUtcTime(talk.updated_date));

            handlePic(talk, vHolder);//图片处理
            handleSetWidthAndHeight(talk, vHolder);
            handleHomeFeedbackButton(talk, vHolder);//开关反馈

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头像跟名字的处理
     */
    private void handleAvatarAndName(ViewHolder vHolder, TalkModel talk) {
        String avatar;//头像
        String circle_name;//名字
        if (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC) {//资讯贴子
            avatar = talk.publisher == null ? "" : talk.publisher.avatar;
            circle_name = talk.publisher == null ? "" : talk.publisher.screen_name;
        } else {
            avatar = homeBottomContent == 1 ? talk.circle_icon : talk.publisher == null ? "" : talk.publisher.avatar;
            circle_name = homeBottomContent == 1 ? talk.circle_name : talk.publisher == null ? "" : talk.publisher.screen_name;
        }


        //TODO 5.9.2 美柚号作品列表，头像不显示
        isShowBottomIcon = HomeType.HOME_BOTTOM_NO_SHOW_ICON;
        if (isShowBottomIcon == HomeType.HOME_SHOW_ICON) {
            vHolder.iv_block_two.setVisibility(View.VISIBLE);
            // 头像
            if (!StringUtils.isNull(avatar)) {
                ImageLoader.getInstance().displayImage(SeeyouApplication.getContext(), vHolder.iv_block_two, avatar, R.drawable.apk_mine_photo, 0, 0, 0, true, ImageLoader.getChatImageWH(mContext), ImageLoader.getChatImageWH(mContext), null);
            } else {
                vHolder.iv_block_two.setImageResource(R.drawable.apk_mine_photo);
            }
        } else {
            vHolder.iv_block_two.setVisibility(View.GONE);
        }
        //圈子名字
        vHolder.tv_block_name_two.setText(circle_name);

        //TODO 5.9.2 该位置显示阅读量和播放次数------------------
        String news_type_str = "";
        int view_times = talk.view_times;
        boolean isUnit = false;
        if (view_times > 10000) {
            view_times = view_times / 10000;
            isUnit = true;
        }
        if (talk.news_type == NewsType.NEWS_VIDEO.getNewsType()) {
            news_type_str = mContext.getResources().getText(R.string.meiyouaccount_play_count) + "" + view_times + "" + (isUnit ? "万" : "次");
        } else {
            news_type_str = mContext.getResources().getText(R.string.meiyouaccount_read_count) + "" + view_times + "" + (isUnit ? "万" : "次");
        }

        vHolder.tv_block_name_two.setText(news_type_str);
        //----------------------------------------------------
    }

    /**
     * 反馈开关逻辑处理
     */
    private void handleHomeFeedbackButton(TalkModel talk, ViewHolder vHolder) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();

        //TODO 5.9.2 作品列表反馈按钮隐藏
        homeFeedbackButton = 0;
        //反馈开关是否显示
        if (homeFeedbackButton == 0) {//不显示
            //时间
            vHolder.tv_time_two.setVisibility(time_view == 1 ? View.VISIBLE : View.GONE);
            params.bottomMargin = bottomSpace5;
            vHolder.iv_image.requestLayout();
            vHolder.ivClose.setVisibility(View.GONE);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, vHolder.tv_block_name_two.getId());
            vHolder.rlTime.setLayoutParams(lp);
            vHolder.rlTime.requestLayout();
        }
    }

    /**
     * 代码计算布局 太坑了这个布局设计的
     */
    private void handleSetWidthAndHeight(TalkModel talk, ViewHolder vHolder) {
        RelativeLayout.LayoutParams ll_contentParams = (RelativeLayout.LayoutParams) vHolder.ll_content.getLayoutParams();
        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        ll_contentParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        vHolder.ll_content.requestLayout();

        RelativeLayout.LayoutParams rlBottomContentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
        rlBottomContentParams.addRule(RelativeLayout.LEFT_OF, vHolder.iv_image.getId());
        rlBottomContentParams.addRule(RelativeLayout.BELOW, vHolder.ll_content.getId());

        //话题和资讯的类型 图片逻辑 大图类型
        if (true) {//纯图片 纯视频类型
            //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
            int height;
            if (homeFeedsImageType == 1) {//方图
                height = imageWidth;
            } else {
                height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2
            }
            int imageSize = talk.images.size();//图片大小
            if (imageSize > 0) {
                if (imageSize < 3) {
                    if (homeFeedsImageType == 2) {//扁图
                        if (NewsHomeController.getInstance().getTextViewLineCount(vHolder.tv_topic_title_two,
                                NewsHomeController.getInstance().getSpannableTitle(SeeyouApplication.getContext(), talk), bottomHeight) >= 3) {//超过三行字体的时候扁图的时候要换行显示底部布局
                            RelativeLayout.LayoutParams rlBottomContentParamsTwo = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
                            rlBottomContentParamsTwo.addRule(RelativeLayout.BELOW, vHolder.ll_content.getId());
                            rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                            vHolder.rlBottomContent.setLayoutParams(rlBottomContentParamsTwo);

                            ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                            ll_contentParams.height = height + bottomSpace10;
                            vHolder.ll_content.requestLayout();
                        } else {
                            rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                            vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);

                            ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                            ll_contentParams.height = height - feedBackHeight + DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                            vHolder.ll_content.requestLayout();
                        }
                    }

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();
                    layoutParams.height = height;//扁图的比列是3:2
                    layoutParams.width = imageWidth;
                    vHolder.iv_image.setLayoutParams(layoutParams);
                } else {
                    rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 3);

                    vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
                }
            } else {
                rlBottomContentParams.topMargin = 0;
                vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
            }
        }
    }

    /**
     * 图片处理
     */
    private void handlePic(TalkModel talk, ViewHolder vHolder) {
        try {
            ImageLoadParams imageParams = new ImageLoadParams();
            imageParams.anim = false;
            imageParams.defaultholder = R.color.black_f;
            imageParams.tag = mContext != null ? mContext.hashCode() : SeeyouApplication.getContext().hashCode();
            //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
            if (homeFeedsImageType == 1) {//方图
                imageParams.height = imageWidth;
                imageParams.width = imageWidth;
            } else {//扁图
                imageParams.height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2
                imageParams.width = imageWidth;
            }
            if (talk.images.size() == 0) {
                vHolder.iv_image.setVisibility(View.GONE);
                vHolder.fl_multi_image.setVisibility(View.GONE);

//                vHolder.iv_multi_image.setVisibility(View.GONE);
//                vHolder.tv_multi_image.setVisibility(View.GONE);
                return;
            }
            if (talk.images.size() >= 3) {//超过3张的情况
                vHolder.fl_multi_image.setVisibility(View.VISIBLE);
                vHolder.iv_image.setVisibility(View.GONE);
                //最多只显示3张图
                List<String> shrinkList = talk.images.subList(0, 3);
                vHolder.iv_multi_image.displayImage(HomeRecommendCacheController.getInstance().getDisplayImageModel(shrinkList, talk), imageWidth, imageParams.height, 3, imageParams);
                LogUtils.d("====multi pic imageWidth="+imageWidth);
                if(talk.imgs_count < 3) {
                    vHolder.tv_multi_image.setVisibility(View.GONE);
                }else{
                    vHolder.tv_multi_image.setVisibility(View.VISIBLE);
                    vHolder.tv_multi_image.setText(talk.imgs_count + "图");
                }
            } else {//产品说：个人主页无单张大图的展示，单张是图文类型，展示在标题右侧，且这个页面与首页和收藏页面不一致。。。
                vHolder.fl_multi_image.setVisibility(View.GONE);
                vHolder.iv_image.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();
                layoutParams.rightMargin = leftSpace12;
                if (homeFeedsImageType == 1) {//方图
                    layoutParams.width = imageWidth;
                    layoutParams.height = imageWidth;
                } else {
                    layoutParams.height = imageParams.height;//扁图的比列是3:2
                    layoutParams.width = imageWidth;
                }
                LogUtils.d("====single pic imageWidth="+imageWidth);

                vHolder.iv_image.setLayoutParams(layoutParams);
//                ImageLoader.getInstance().displayImage(mContext, vHolder.iv_image, talk.images.get(0), imageParams, null);
                if(talk.news_type == NewsType.NEWS_VIDEO.getNewsType()) {
                    vHolder.iv_image.displayImage(talk.images.get(0),imageParams,talk.video_time);
                }else{
                    vHolder.iv_image.displayImage(talk.images.get(0),imageParams,null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     *
     * @param vHolder
     * @param talk
     * @param position
     */
    public void setListener(final ViewHolder vHolder, final TalkModel talk, final int position, int layoutType) {

    }

    /**
     * 关闭反馈
     *
     * @param vHolder
     * @param talk
     */
    public void closeFeedBack(ViewHolder vHolder, final TalkModel talk) {

    }

    /**
     * 新样式和旧样式的点击事件
     *
     * @param talk
     * @param position
     */
    public void onContentClick(ViewHolder viewHolder, TalkModel talk, int position) {

    }

    /**
     * 新的样式点击逻辑
     *
     * @param talk
     * @param position
     */
    public void newsTopicClick(TalkModel talk, int position) {

    }

    private void enterDetail(TalkModel talk, int position) {

    }



    public void setListViewStatus(int status) {
        if (mOnBesideListViewScrollListener != null) {
            if (status == ListViewStatus.SCROLL_START) {
                onScrollStart();
            } else if (status == ListViewStatus.SCROLL_FINISH) {
                onScrollFinish();
            }
            if (status == ListViewStatus.SCROLLING) {
                onScrolling();
            }
        }
    }

    private void onScrollStart() {
        if (mOnBesideListViewScrollListener != null) {
            Iterator iter = mOnBesideListViewScrollListener.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                OnListViewStatusListener listViewStatusListener = (OnListViewStatusListener) entry.getValue();
                listViewStatusListener.onScrollStart();
            }
        }
    }

    private void onScrollFinish() {
        if (mOnBesideListViewScrollListener != null) {
            Iterator iter = mOnBesideListViewScrollListener.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                OnListViewStatusListener listViewStatusListener = (OnListViewStatusListener) entry.getValue();
                listViewStatusListener.onScrollFinish();
            }
        }
    }

    private void onScrolling() {
        if (mOnBesideListViewScrollListener != null) {
            Iterator iter = mOnBesideListViewScrollListener.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                OnListViewStatusListener listViewStatusListener = (OnListViewStatusListener) entry.getValue();
                listViewStatusListener.onScrolling();
            }
        }
    }

    public void setListViewStatusListener(int key, OnListViewStatusListener onBesideListViewScrollListener) {
        mOnBesideListViewScrollListener.put(key, onBesideListViewScrollListener);
    }


    /**
     * 根据类型获取不同的布局
     *
     * @param layoutType
     * @param arg2
     * @return
     */
    private View getItemTypeView(int layoutType, ViewGroup arg2) {
        switch (layoutType) {
            case HomeType.HOME_LAYOUT_TYPE_NORMAL:
                return inflater.inflate(R.layout.layout_meiyou_account_feed_normal, arg2, false);
//            case HomeType.HOME_LAYOUT_TYPE_VIDEO:
//                return inflater.inflate(R.layout.layout_news_home_video_item, arg2, false);
            case HomeType.HOME_LAYOUT_TYPE_YIMA:
                return inflater.inflate(R.layout.layout_home_yima, arg2, false);
            case HomeType.HOME_LAYOUT_TYPE_SEPARATORBAR:
                return inflater.inflate(R.layout.layout_period_home_feed_separatorbar, arg2, false);
            default:
                return inflater.inflate(R.layout.layout_meiyou_account_feed_normal, arg2, false);
        }
    }

    public class ViewHolder {
        //经期feeds样式控件
        private View mView;
        //话题和资讯正常业务类型布局
        public RelativeLayout ll_style_two;
        private BageImageView iv_image;
        private LinearLayout ll_content;
        private TextView tv_topic_title_two;
        private MultiImageView iv_multi_image;
        private FrameLayout fl_multi_image;
        private TextView tv_multi_image;
        public RelativeLayout rlBottomContent;
        private LoaderImageView iv_block_two;
        private TextView tv_block_name_two;
        private TextView tv_comment_count_two;
        // private ImageView ic_comment_count_two;
        private RelativeLayout rlTime;
        private TextView tv_time_two;
        private ImageView ivClose;
        public View view_yunqi_feeds;
        //可以播放的视频
        public LinearLayout ll_news_home_video;
       // public JCTopicVideoView jctVideoView;
        public TextView tv_video_name;
        public ImageView ic_video_comment;
        public TextView tv_video_comment_count;
        public ImageView ivShare;
        public View view_video_space;
        public TextView tv_video_play_time;
        //姨妈卡片类型布局
        public LinearLayout ll_home_yima;
        //分隔栏类型布局
        public RelativeLayout rlSeparatorBar;

        void init(View view, int layoutType) {
            this.mView = view;
            switch (layoutType) {
                case HomeType.HOME_LAYOUT_TYPE_NORMAL:
                    ll_style_two = (RelativeLayout) view.findViewById(R.id.ll_style_two);
                    iv_image = (BageImageView) view.findViewById(R.id.iv_image);
                    ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
                    tv_topic_title_two = (TextView) view.findViewById(R.id.tv_topic_title_two);
                    iv_multi_image = (MultiImageView) view.findViewById(R.id.iv_multi_image);
                    fl_multi_image = (FrameLayout) view.findViewById(R.id.fl_multi_image);
                    tv_multi_image = (TextView) view.findViewById(R.id.tv_multi_image);
                    rlBottomContent = (RelativeLayout) view.findViewById(R.id.rlBottomContent);
                    iv_block_two = (LoaderImageView) view.findViewById(R.id.iv_block_two);
                    tv_block_name_two = (TextView) view.findViewById(R.id.tv_block_name_two);
                    tv_comment_count_two = (TextView) view.findViewById(R.id.tv_comment_count_two);
                    // ic_comment_count_two = (ImageView) view.findViewById(R.id.ic_comment_count_two);
                    rlTime = (RelativeLayout) view.findViewById(R.id.rlTime);
                    tv_time_two = (TextView) view.findViewById(R.id.tv_time_two);
                    ivClose = (ImageView) view.findViewById(R.id.ivClose);
                    view_yunqi_feeds = view.findViewById(R.id.view_yunqi_feeds);
                    break;
//                case HomeType.HOME_LAYOUT_TYPE_VIDEO:
//                    ll_news_home_video = (LinearLayout) view.findViewById(R.id.ll_news_home_video);
//                    jctVideoView = (JCTopicVideoView) view.findViewById(R.id.jctVideoView);
//                    tv_video_name = (TextView) view.findViewById(R.id.tv_video_name);
//                    ic_video_comment = (ImageView) view.findViewById(R.id.ic_video_comment);
//                    tv_video_comment_count = (TextView) view.findViewById(R.id.tv_video_comment_count);
//                    ivShare = (ImageView) view.findViewById(R.id.ivShare);
//                    view_video_space = view.findViewById(R.id.view_video_space);
//                    tv_video_play_time = (TextView) view.findViewById(R.id.tv_video_play_time);
//                    break;
                case HomeType.HOME_LAYOUT_TYPE_YIMA:
                    ll_home_yima = (LinearLayout) view.findViewById(R.id.ll_home_yima);
                    break;
                case HomeType.HOME_LAYOUT_TYPE_SEPARATORBAR:
                    rlSeparatorBar = (RelativeLayout) view.findViewById(R.id.rlSeparatorBar);
                    break;
            }
        }
    }
}
