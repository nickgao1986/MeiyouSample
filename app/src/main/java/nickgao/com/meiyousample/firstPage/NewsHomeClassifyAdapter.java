package nickgao.com.meiyousample.firstPage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BadgeRelativeLaoutView;
import com.lingan.seeyou.ui.view.MultiImageView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.meetyou.crsdk.listener.OnListViewStatusListener;
import com.meetyou.crsdk.video.view.JCTopicVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nickgao.com.framework.utils.StringUtil;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.controller.NewsHomeController;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * 新的首页适配器
 *
 * @author wuminjian
 */
@SuppressLint("ResourceAsColor")
public class NewsHomeClassifyAdapter extends BaseAdapter {
    private Activity mContext;
    public List<TalkModel> list;
    private LayoutInflater inflater;
    public ListView listview;
    //图片显示的宽度
    private int imageWidth;
    private int imageFlatFigureHeight;//扁图的高度
    //大图显示的宽度
    private int bigImageWidth;
    public int homeBottomContent = 2;//底部显示得内容：1- 圈子信息   2-用户信息
    public int homeFeedbackButton = 1;//反馈按钮是否显示：1- 显示   0-不显示
    public int homeFeedsImageType = 2;//图片显示方图还是扁图 1-方图 2 扁图
    public int time_view = 1;//时间开关是不是显示 0不显示 1显示
    public int isShowBottomIcon = 1;//是否显示底部icon

    private int leftSpace12, topSpace, bottomSpace5, bottomSpace10;
    private int feedBackHeight;//反馈兰的高度
    private int txtPaddingSpace;
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

    public NewsHomeClassifyAdapter(Activity context, LayoutInflater inflater, List<TalkModel> list, ListView listview, int classifyId, String classifyName, OnRealPositionListener onRealPositionListener) {
        this.classifyId = classifyId;
        this.classifyName = classifyName;
        this.listview = listview;
        this.mContext = context;
        this.list = list;
        this.inflater = inflater;
        this.mOnRealPositionListener = onRealPositionListener;

        screenWidth = DeviceUtils.getScreenWidth(SeeyouApplication.getContext());
        videoHeight = (int) (screenWidth * 360 / 640.0f);

        this.imageWidth = (screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 30) - DeviceUtils.dip2px(SeeyouApplication.getContext(), 3 * 2)) / 3;
        bigImageWidth = screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 30);
        leftSpace12 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 15);
        topSpace = DeviceUtils.dip2px(SeeyouApplication.getContext(), 11);
        bottomSpace5 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 5);
        bottomSpace10 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 10);
        feedBackHeight = DeviceUtils.dip2px(SeeyouApplication.getContext(), 28);
        txtPaddingSpace = 0;
        bottomHeight = screenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 30) - imageWidth - leftSpace12;
        imageFlatFigureHeight = (int) ((double) imageWidth / 1.5);//扁图高度
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
        int homeLayoutType = HomeType.HOME_LAYOUT_TYPE_NORMAL;
        switch (model.recomm_type) {
            case HomeType.TYPE_SEPARATOR_BAR://分隔栏类型
                homeLayoutType = HomeType.HOME_LAYOUT_TYPE_SEPARATORBAR;
                break;
            case HomeType.TYPE_YIMA_GO://大姨妈卡片类型
            case HomeType.TYPE_YIMA_COME:
                homeLayoutType = HomeType.HOME_LAYOUT_TYPE_YIMA;
                break;
            case HomeType.TYPE_AGE_CARD://年龄卡片
                homeLayoutType = HomeType.HOME_LAYOUT_TYPE_AGE_CARD;
                break;
            case HomeType.TYPE_NEWS_TOPIC_CARD://资讯 \ 话题 专题卡片
                if (model.attr_type == HomeType.HOME_SPECIAL_BLEND) {//专题卡片混合的模式
                    homeLayoutType = HomeType.HOME_LAYOUT_TYPE_NEWS_TOPIC_CARD;
                }
                break;
            case HomeType.TYPE_TIPS_PREGNANCY://今日密保 \ 怀孕几率卡片
                homeLayoutType = HomeType.HOME_LAYOUT_TYPE_TIPS_PREGNANCY;
                break;
            case HomeType.TYPE_NEWS_TOPIC://资讯类型
                if (model.news_type == NewsType.NEWS_VIDEO.getNewsType() && model.feeds_play == HomeType.HOME_VIDEO_PLAY) {//纯视频类型
                    homeLayoutType = HomeType.HOME_LAYOUT_TYPE_VIDEO;
                }
                break;
        }
        return homeLayoutType;
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
            switch (layoutType) {
                case HomeType.HOME_LAYOUT_TYPE_NORMAL://资讯和话题,专题大图小图，公用的类型逻辑
                    handleTopicType(vHolder, talk, position);
                    break;
                case HomeType.HOME_LAYOUT_TYPE_VIDEO://在列表中可播放的视频样式
                    handlePlayVideo(vHolder, talk, position);
                    break;

            }
            setListener(vHolder, talk, position, layoutType);//点击事件
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }

    /**
     * 今日密报，怀孕几率卡片
     *
     * @param vHolder
     * @param talk
     */
    private void handleTipsPregancy(ViewHolder vHolder, TalkModel talk) {
//        vHolder.tvPregnancyTitle.setText(talk.title);
//        RelativeLayout.LayoutParams imageParams = NewsHomeSpecialCardHelper.getInstance().handleSetWidthHeight(vHolder.ivPregnancyImage, homeFeedsImageType, imageWidth, imageFlatFigureHeight);
//        String image = talk.images.size() > 0 ? talk.images.get(0) : "";
//
//        ImageLoader.getInstance().displayImage(SeeyouApplication.getContext(), vHolder.ivPregnancyImage, image, NewsHomeController.getInstance().getImageLoadParams(mContext, imageParams.width, imageParams.height), null);

    }


    /**
     * 可播放的视频类型
     */
    private void handlePlayVideo(final ViewHolder viewHolder, final TalkModel talkModel, final int position) {
        if (mContext == null)
            return;

        final VideoPlayStatus videoPlayStatus = new VideoPlayStatus(mContext, onlyVideoId);

        NewsHomeVideoController.getInstance().handleVideo(mContext, this, viewHolder, talkModel, videoPlayStatus);
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
//        notifyDataSetChanged();

    }

    /**
     * 姨妈数据类型
     *
     * @param vHolder
     * @param talk
     */
    public void handleYimaData(ViewHolder vHolder, TalkModel talk, int position) {
        if (mContext == null)
            return;
        //HomeYimaStatusHelper.getInstance().initView(mContext, vHolder.mView, talk, imageWidth, position);
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

    private void handleTopicType(final ViewHolder vHolder, final TalkModel talk, int position) {
        try {
            //标题
            vHolder.tv_topic_title_two.setText(NewsHomeController.getInstance().getSpannableTitle(SeeyouApplication.getContext(), talk));

            if (talk.isRead) {//已读
                SkinManager.getInstance().setTextColor(vHolder.tv_topic_title_two, R.color.black_b);
            } else {
                SkinManager.getInstance().setTextColor(vHolder.tv_topic_title_two, R.color.black_at);
            }
            handleAvatarAndName(vHolder, talk);//头像和名字和回复数
            handlePic(talk, vHolder);//图片处理
            handleSetWidthAndHeight(talk, vHolder);
            handleHomeFeedbackButton(vHolder);//开关反馈
            //间隔线是否显示
            if (position + 1 < getCount()) {
                TalkModel nextModel = list.get(position + 1);
                if (nextModel.recomm_type == HomeType.TYPE_SEPARATOR_BAR || (nextModel.recomm_type == HomeType.TYPE_NEWS_TOPIC_CARD && nextModel.attr_type == HomeType.HOME_SPECIAL_BLEND)) {//专题混合型的卡片 和分隔栏有间距要隐藏线
                    vHolder.view_yunqi_feeds.setVisibility(View.GONE);
                }else{
                    vHolder.view_yunqi_feeds.setVisibility(View.VISIBLE);
                }
            } else {
                vHolder.view_yunqi_feeds.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头像跟名字的处理
     */
    private void handleAvatarAndName(ViewHolder vHolder, TalkModel talk) {
        String circle_name;//名字
        int nameColor = R.color.black_c;
        String dec = "回复";
        if (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC) {//资讯贴子
            circle_name = talk.publisher == null ? "" : talk.publisher.screen_name;
        } else if (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC_CARD && (talk.attr_type == HomeType.HOME_SPECIAL_SMALL_IMG || talk.attr_type == HomeType.HOME_SPECIAL_BIG_IMG)) {
            circle_name = "专题";
            dec = "热度";
            nameColor = R.color.red_b;
        } else {
            circle_name = homeBottomContent == 1 ? talk.circle_name : talk.publisher == null ? "" : talk.publisher.screen_name;
        }
        //圈子名字
        vHolder.tv_block_name_two.setTextColor(mContext.getResources().getColor(nameColor));
        vHolder.tv_block_name_two.setText(circle_name);
        /*if (talk.attr_type == 1) {//美柚号
            vHolder.tv_block_name_two.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.feed_icon_meiyou), null);
        } else */
        vHolder.tv_block_name_two.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        //评论
        if (talk.total_review == 0) {//需求是等宇0的时候隐藏这个控件
            vHolder.tv_comment_count_two.setVisibility(View.GONE);
        } else {
            vHolder.tv_comment_count_two.setText(StringUtil.getFiveNum(talk.total_review) + dec);
            vHolder.tv_comment_count_two.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 反馈开关逻辑处理
     */
    private void handleHomeFeedbackButton(ViewHolder vHolder) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();
        //反馈开关是否显示
        if (homeFeedbackButton == 0) {//不显示
            //时间
            params.bottomMargin = bottomSpace5;
            vHolder.iv_image.requestLayout();
            vHolder.ivClose.setVisibility(View.GONE);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, vHolder.tv_block_name_two.getId());
            vHolder.tv_comment_count_two.setLayoutParams(lp);
        } else {//显示
            params.bottomMargin = bottomSpace10;
            vHolder.iv_image.requestLayout();
            vHolder.ivClose.setVisibility(View.VISIBLE);
            lp.addRule(RelativeLayout.RIGHT_OF, vHolder.tv_block_name_two.getId());
            lp.addRule(RelativeLayout.ALIGN_BOTTOM, vHolder.tv_block_name_two.getId());
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            vHolder.tv_comment_count_two.setLayoutParams(lp);
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
        rlBottomContentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //话题和资讯的类型 图片逻辑 大图类型
        if (NewsHomeController.getInstance().isSetWidthAndHeight(talk)) {//如果不是纯图片 纯视频类型 和专题卡片大图类型和专题卡片混合类型的数据都要进行代码计算位置宽高等
            //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
            int height;
            if (homeFeedsImageType == 1) {//方图
                height = imageWidth;
            } else {
                height = imageFlatFigureHeight;//扁图的比列是3:2
            }
            int imageSize = talk.images.size();//图片大小
            if (imageSize > 0) {
                if (imageSize < 3) {
                    vHolder.tv_topic_title_two.setPadding(0, 0, 0, 0);
                    if (homeFeedsImageType == 2) {//扁图
                        if (NewsHomeController.getInstance().getTextViewLineCount(vHolder.tv_topic_title_two,
                                NewsHomeController.getInstance().getSpannableTitle(SeeyouApplication.getContext(), talk), bottomHeight) >= 3) {//超过三行字体的时候扁图的时候要换行显示底部布局
                            RelativeLayout.LayoutParams rlBottomContentParamsTwo = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
                            rlBottomContentParamsTwo.addRule(RelativeLayout.BELOW, vHolder.ll_content.getId());
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
                    } else {
                        rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                        vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);

                        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        ll_contentParams.height = height - feedBackHeight + DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                        vHolder.ll_content.requestLayout();
                    }

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();
                    if (homeFeedsImageType == 1) {//方图
                        layoutParams.width = imageWidth;
                        layoutParams.height = imageWidth;
                    } else {
                        layoutParams.height = height;//扁图的比列是3:2
                        layoutParams.width = imageWidth;
                    }
                    vHolder.iv_image.setLayoutParams(layoutParams);
                } else {//有3个图片和没有开启省流量模式的时候
                    vHolder.tv_topic_title_two.setPadding(0, 0, 0, txtPaddingSpace);
                    if (homeFeedsImageType == 1) {
                        rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 8);
                    } else {
                        rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 5);
                    }
                    vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
                }
            } else {
                vHolder.tv_topic_title_two.setPadding(0, 0, 0, 0);
                rlBottomContentParams.topMargin = 0;
                vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
            }
        } else {
            vHolder.tv_topic_title_two.setPadding(0, 0, 0, txtPaddingSpace);
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
            //话题和资讯的类型 图片逻辑 大图类型
            if (talk.news_type == NewsType.NEWS_IMAGE.getNewsType() || talk.news_type == NewsType.NEWS_VIDEO.getNewsType() ||
                    (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC_CARD && talk.attr_type == HomeType.HOME_SPECIAL_BIG_IMG)) {//纯图片和点击进详情界面的视频类型和专题卡片的大图类型
                vHolder.iv_image_video_list.setVisibility(View.VISIBLE);
                vHolder.iv_image.setVisibility(View.GONE);
                vHolder.iv_multi_image.setVisibility(View.GONE);

                if (talk.images.size() == 0) {
                    vHolder.iv_image_video_list.setVisibility(View.GONE);
                    return;
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vHolder.iv_image_video_list.getLayoutParams();
                layoutParams.width = bigImageWidth;
                if (talk.news_type == NewsType.NEWS_VIDEO.getNewsType()) {
                    layoutParams.height = (int) ((double) bigImageWidth / 1.78D);//比列是640/360
                } else {
                    layoutParams.height = (int) ((double) bigImageWidth / 1.93D);//比列是640/330
                }
                imageParams.height = layoutParams.height;
                imageParams.width = bigImageWidth;
                vHolder.iv_image_video_list.setLayoutParams(layoutParams);
                vHolder.iv_image_video_list.displayImage(talk.images.get(0), imageParams, HomeRecommendCacheController.getInstance().getDisplayImageModel(talk.images.subList(0, 1), talk).get(0));
            } else {
                //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
                if (homeFeedsImageType == 1) {//方图
                    imageParams.height = imageWidth;
                    imageParams.width = imageWidth;
                } else {//扁图
                    imageParams.height = imageFlatFigureHeight;//扁图的比列是3:2
                    imageParams.width = imageWidth;
                }

                vHolder.iv_image_video_list.setVisibility(View.GONE);
                if (talk.images.size() > 0) {
                    if (talk.images.size() >= 3) {
                        vHolder.iv_multi_image.setVisibility(View.VISIBLE);
                        vHolder.iv_image.setVisibility(View.GONE);

                        //最多只显示3张图
                        List<String> shrinkList = talk.images.subList(0, 3);
                        vHolder.iv_multi_image.displayImage(HomeRecommendCacheController.getInstance().getDisplayImageModel(shrinkList, talk), imageWidth, imageParams.height, 3, imageParams);
                    } else {
                        vHolder.iv_multi_image.setVisibility(View.GONE);
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
                        vHolder.iv_image.setLayoutParams(layoutParams);
                        vHolder.iv_image.displayImage(talk.images.get(0), imageParams, HomeRecommendCacheController.getInstance().getDisplayImageModel(talk.images.subList(0, 1), talk).get(0));
                    }
                } else {
                    vHolder.iv_multi_image.setVisibility(View.GONE);
                    vHolder.iv_image.setVisibility(View.GONE);
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
    public void setListener(final ViewHolder vHolder, final TalkModel talk, final int position, final int layoutType) {

    }

    /**
     * 关闭反馈
     *
     * @param vHolder
     * @param talk
     */
    public void closeFeedBack(ViewHolder vHolder, final TalkModel talk, final int position) {

    }

    /**
     * 新样式和旧样式的点击事件
     *
     * @param talk
     * @param position
     */
    public void onContentClick(ViewHolder viewHolder, TalkModel talk, int position) {
        if (mContext == null)
            return;

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
                return inflater.inflate(R.layout.layout_period_home_feed_normal, arg2, false);
            case HomeType.HOME_LAYOUT_TYPE_VIDEO:
                return inflater.inflate(R.layout.layout_news_home_video_item, arg2, false);
            default:
                return inflater.inflate(R.layout.layout_period_home_feed_normal, arg2, false);
        }
    }

    public class ViewHolder {
        //经期feeds样式控件
        private View mView;
        //话题和资讯正常业务类型布局
        public RelativeLayout ll_style_two;
        private BadgeRelativeLaoutView iv_image, iv_image_video_list;
        private LinearLayout ll_content;
        private TextView tv_topic_title_two;
        private MultiImageView iv_multi_image;
        public RelativeLayout rlBottomContent;
        private TextView tv_block_name_two;
        private TextView tv_comment_count_two;
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

        //年龄卡片类型布局
        public TextView tvAgeCard, tvAgeContent, tvFillIn;
        public LinearLayout llSubTitle;

        // 今日密保 \ 怀孕几率卡片类型布局
        public TextView tvPregnancyTitle;
        public LoaderImageView ivPregnancyImage;
        //专题卡片混合类型
        public RelativeLayout rlSpecialLayout;
        public RelativeLayout rlSpecialBottomContentOne, rlSpecialBottomContentTwo;
        public TextView tvSpecialTitle;
        public ImageView ivSpecialClose;
        public RelativeLayout llSpecialItemOne, llSpecialItemTwo;
        public LoaderImageView ivSpecialImgOne, ivSpecialImgTwo;
        public TextView tvSpecialTitleOne, tvSpecialTitleTwo;
        public TextView tvSpecialNameOne, tvSpecialNameTwo;
        public TextView tvSpecialCommentCountOne, tvSpecialCommentCountTwo;
        public RelativeLayout rlSpecialMore;
        public JCTopicVideoView jctVideoView;


        void init(View view, int layoutType) {
            this.mView = view;
            switch (layoutType) {
                case HomeType.HOME_LAYOUT_TYPE_NORMAL:
                    ll_style_two = (RelativeLayout) view.findViewById(R.id.ll_style_two);
                    iv_image = (BadgeRelativeLaoutView) view.findViewById(R.id.iv_image);
                    iv_image_video_list = (BadgeRelativeLaoutView) view.findViewById(R.id.iv_image_video_list);
                    ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
                    tv_topic_title_two = (TextView) view.findViewById(R.id.tv_topic_title_two);
                    iv_multi_image = (MultiImageView) view.findViewById(R.id.iv_multi_image);
                    rlBottomContent = (RelativeLayout) view.findViewById(R.id.rlBottomContent);
                    tv_block_name_two = (TextView) view.findViewById(R.id.tv_block_name_two);
                    tv_comment_count_two = (TextView) view.findViewById(R.id.tv_comment_count_two);
                    ivClose = (ImageView) view.findViewById(R.id.ivClose);
                    view_yunqi_feeds = view.findViewById(R.id.view_yunqi_feeds);
                    break;

                case HomeType.HOME_LAYOUT_TYPE_VIDEO:
                    ll_news_home_video = (LinearLayout) view.findViewById(R.id.ll_news_home_video);
                    jctVideoView = (JCTopicVideoView) view.findViewById(R.id.jctVideoView);
                    tv_video_name = (TextView) view.findViewById(R.id.tv_video_name);
                    ic_video_comment = (ImageView) view.findViewById(R.id.ic_video_comment);
                    tv_video_comment_count = (TextView) view.findViewById(R.id.tv_video_comment_count);
                    ivShare = (ImageView) view.findViewById(R.id.ivShare);
                    view_video_space = view.findViewById(R.id.view_video_space);
                    tv_video_play_time = (TextView) view.findViewById(R.id.tv_video_play_time);
                    break;
            }
        }
    }
}
