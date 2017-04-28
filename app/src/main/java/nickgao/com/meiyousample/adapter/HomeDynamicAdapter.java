package nickgao.com.meiyousample.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomUrlTextView;
import com.lingan.seeyou.ui.view.LoaderImageView;
import com.lingan.seeyou.ui.view.MeasureGridView;
import com.lingan.seeyou.ui.view.PraiseButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.listener.OnItemTextExpandListener;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.IHomeDynamicType;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

import static nickgao.com.meiyousample.utils.CalendarUtil.convertTime;


/**
 * 说说首页adapter
 * Created by Administrator on 2014/7/11.
 */
public class HomeDynamicAdapter extends BaseAdapter {
    private static final int LAYOUT_TYPE_DEFAULT = 0;  //默认  说说布局
    private static final int LAYOUT_TYPE_SHARE = 1;    //话题分享布局
    private static final int LAYOUT_TYPE_CIRCLE_RECOMMEND = 2;  //话题推荐布局

    private static final int TYPE_COUNT = LAYOUT_TYPE_CIRCLE_RECOMMEND + 1;//3种布局
    private Activity context;
    private List<HomeDynamicModel> models;
    private int bigImageWidth;
    private int fullImageWidht;//推荐话题，宽度铺满
    private int tvContentWidth = 200;

    private int fromID; //来源id

    //是否是个人主页
    private boolean isPersonal = false;


    public HomeDynamicAdapter(Activity context) {
        this.context = context;
        models = new ArrayList<>();
        //
        bigImageWidth = DeviceUtils.getScreenWidth(context.getApplicationContext()) / 2; //大图限宽高  屏幕1/2
        fullImageWidht = DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80);
        tvContentWidth = ((DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80)));
    }

    public HomeDynamicAdapter(Activity context, List<HomeDynamicModel> dataList) {
        this.context = context;
        models = dataList;
        bigImageWidth = DeviceUtils.getScreenWidth(context.getApplicationContext()) / 2; //大图限宽高  屏幕1/2
        fullImageWidht = DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80);
        tvContentWidth = ((DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80)));
    }

    /**
     * 设置数据
     *
     * @param newModels
     * @param fromID
     */
    public void setDatas(List<HomeDynamicModel> newModels, int fromID) {
        this.fromID = fromID;
        models.clear();
        addDatas(newModels);
    }

    /**
     * 增加数据
     *
     * @param newModels
     */
    public void addDatas(List<HomeDynamicModel> newModels) {
        if (newModels != null)
            models.addAll(newModels);
        notifyDataSetChanged();
    }

    /**
     * 在第一位添加数据
     *
     * @param newModel
     */
    public void addDataInHead(HomeDynamicModel newModel) {
        if (newModel == null)
            return;
        models.add(0, newModel);
        notifyDataSetChanged();
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<HomeDynamicModel> getDatas() {
        return models;
    }

    /**
     * 获取指定位置数据
     *
     * @param position
     * @return
     */
    public HomeDynamicModel getData(int position) {
        try {
            return models.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取最后一条数据
     *
     * @return
     */
    public HomeDynamicModel getLastData() {
        if (models.size() > 0) {
            return models.get(models.size() - 1);
        }
        return null;
    }

    /**
     * 删除数据
     *
     * @param homeDynamicModel
     */
    public void deleteData(HomeDynamicModel homeDynamicModel) {
        try {
            Iterator<HomeDynamicModel> iterator = models.iterator();
            while (iterator.hasNext()) {
                HomeDynamicModel model = iterator.next();
                if (model.id > 0) {
                    if (model.id == homeDynamicModel.id) {
                        iterator.remove();
                        break;
                    }
                } else {
                    if (model.uuid.equals(homeDynamicModel.uuid)) {
                        iterator.remove();
                        break;
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHome(boolean isPersonal) {
        this.isPersonal = isPersonal;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }


    @Override
    public int getItemViewType(int position) {
        HomeDynamicModel homeDynamicModel = models.get(position);

        if (null == homeDynamicModel) {
            return LAYOUT_TYPE_DEFAULT;
        }
        int layoutType = homeDynamicModel.type;

        if (layoutType == DynamicModelType.SHUOSHUO) {
            return LAYOUT_TYPE_DEFAULT;
        } else {
            return LAYOUT_TYPE_SHARE;
        }

    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }


    private OnItemTextExpandListener mListener;

    /**
     * 设置展开项监听
     *
     * @param listener
     */
    public void setOnItemTextExpandListener(OnItemTextExpandListener listener) {
        mListener = listener;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        try {
            ViewHolder viewHolder;
            int layoutType = getItemViewType(position);
            if (view == null) {
                viewHolder = new ViewHolder();
                switch (layoutType) {
                    case LAYOUT_TYPE_DEFAULT:
                        view = this.context.getLayoutInflater().inflate(R.layout.layout_home_dynamic_item, viewGroup, false);
                        viewHolder.vsImages = (ViewStub) view.findViewById(R.id.vsImages);
                        viewHolder.vsImageGrid = (ViewStub) view.findViewById(R.id.vsImagesGrid);
                        viewHolder.tvContent = (CustomUrlTextView) view.findViewById(R.id.tvContent);
                        // viewHolder.tvWatchMore = (TextView) view.findViewById(R.id.tvWatchMore);
                        viewHolder.ivMoreItem = (ImageView) view.findViewById(R.id.ivItemMore);
                        viewHolder.tvTime = (TextView) view.findViewById(R.id.tvTime);
                        break;
                    case LAYOUT_TYPE_SHARE:
                        view = this.context.getLayoutInflater().inflate(R.layout
                                .layout_home_dynamic_item_share, viewGroup, false);
                        viewHolder.tvContent = (CustomUrlTextView) view.findViewById(R.id.tvContent);
                        viewHolder.ivShareIcon = (LoaderImageView) view.findViewById(R.id.ivShareIcon);
                        viewHolder.tvShareTitle = (TextView) view.findViewById(R.id.tvShareContent);
                        viewHolder.llShareContent = (LinearLayout) view.findViewById(R.id.llShareContent);
                        // viewHolder.tvWatchMore = (TextView) view.findViewById(R.id.tvWatchMore);
                        viewHolder.tvSharePublisher = (TextView) view.findViewById(R.id.tvSharePublisher);
                        // viewHolder.tv_video_time = (TextView) view.findViewById(R.id.tv_video_time);
                        viewHolder.ivMoreItem = (ImageView) view.findViewById(R.id.ivItemMore);
                        viewHolder.tvTime = (TextView) view.findViewById(R.id.tvTime);
                        // viewHolder.tvTypeFrom = (TextView) view.findViewById(tvTypeFrom);

                        break;
                }

                viewHolder.rlItemContainer = (RelativeLayout) view.findViewById(R.id.llItemContainer);
                viewHolder.ivAvatar = (LoaderImageView) view.findViewById(R.id.ivAvatar);
                viewHolder.tvNickname = (TextView) view.findViewById(R.id.tvNickname);

                viewHolder.btn_praise = (PraiseButton) view.findViewById(R.id.btn_praise);

//                viewHolder.llZan = (LinearLayout) view.findViewById(R.id.llZan);
//                viewHolder.ivZan = (ImageView) view.findViewById(R.id.ivZanImage);
//                viewHolder.tvZan = (TextView) view.findViewById(R.id.tvZan);

                viewHolder.tvReply = (TextView) view.findViewById(R.id.tvReply);
                viewHolder.llZanReply = (LinearLayout) view.findViewById(R.id.llZanReply);
                viewHolder.zanDivider = (ImageView) view.findViewById(R.id.zanDivider);

                viewHolder.divider = view.findViewById(R.id.divider);
                viewHolder.tvReply.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.tata_icon_commentthick),
                        null, null, null);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final HomeDynamicModel homeDynamicModel = models.get(position);

            if (!StringUtils.isNull(homeDynamicModel.createTime)) {
                viewHolder.tvTime.setText(convertTime(homeDynamicModel.createTime));
                viewHolder.tvTime.setVisibility(View.VISIBLE);
            }
            if (homeDynamicModel.isAllowOperate) {
                viewHolder.llZanReply.setVisibility(View.VISIBLE);
                viewHolder.tvReply.setVisibility(View.VISIBLE);

                viewHolder.tvReply.setText(StringUtils.getString(homeDynamicModel.commentNum));

                if (isPublicNewsType(homeDynamicModel)) {
                    viewHolder.btn_praise.setVisibility(View.GONE);
                    viewHolder.zanDivider.setVisibility(View.GONE);
                } else {
                    viewHolder.btn_praise.setVisibility(View.VISIBLE);
                    viewHolder.zanDivider.setVisibility(View.VISIBLE);

                    if (homeDynamicModel.isPraise == 1) {
                        viewHolder.btn_praise.setPraiseState(true);
                        viewHolder.btn_praise.setPraiseCount(homeDynamicModel.praiseNum);
                    } else {
                        viewHolder.btn_praise.setPraiseState(false);
                        viewHolder.btn_praise.setPraiseCount(homeDynamicModel.praiseNum);
                    }
                }
            } else {
                viewHolder.llZanReply.setVisibility(View.GONE);
                // viewHolder.tvReply.setVisibility(View.GONE);
            }


            switch (layoutType) {
                case LAYOUT_TYPE_DEFAULT:
                    LogUtils.d("====layoutType=LAYOUT_TYPE_DEFAULT homeDynamicModel.content=" + homeDynamicModel.content);
                    viewHolder.tvNickname.setText(homeDynamicModel.screenName);
                    if (!StringUtils.isNull(homeDynamicModel.content)) {
                        viewHolder.tvContent.setVisibility(View.VISIBLE);
                        viewHolder.tvContent.setText(homeDynamicModel.content);
                    } else {
                        viewHolder.tvContent.setVisibility(View.GONE);
                        // viewHolder.tvWatchMore.setVisibility(View.GONE);
                    }


                    fillImages(view, viewHolder, homeDynamicModel, position);

                    break;
                case LAYOUT_TYPE_SHARE:
                    LogUtils.d("====layoutType=LAYOUT_TYPE_SHARE sharewords=" + homeDynamicModel.shareWords);

                    viewHolder.tvNickname.setText(homeDynamicModel.screenName);
                    if (!StringUtils.isNull(homeDynamicModel.shareWords)) {
                        String words = homeDynamicModel.shareWords;
                        viewHolder.tvContent.setText(words);
                        viewHolder.tvContent.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvContent.setVisibility(View.GONE);
                        //viewHolder.tvWatchMore.setVisibility(View.GONE);
                    }

                    if (null != homeDynamicModel.imagesList && homeDynamicModel.imagesList.size() > 0 && !StringUtils.isNull(homeDynamicModel
                            .imagesList.get(0))) {
                        String url = homeDynamicModel.imagesList.get(0);
                        ImageLoader.getInstance().displayImage(context.getApplicationContext(), viewHolder.ivShareIcon, url, R.drawable
                                        .tata_img_goodtopic, 0, 0, 0, false,
                                this.context.getResources().getDimensionPixelOffset(R.dimen.list_icon_height_56), this.context.getResources().getDimensionPixelOffset(R.dimen.list_icon_height_56), null);

                    } else {
                        viewHolder.ivShareIcon.setImageResource(R.drawable.tata_img_goodtopic);
                    }
                    viewHolder.tvShareTitle.setText(homeDynamicModel.content);


                    if (homeDynamicModel.type == IHomeDynamicType.TIPS_SHARE) {  //贴士显示分类
                        if (!StringUtils.isNull(homeDynamicModel.tipCategory)) {
                            viewHolder.tvSharePublisher.setVisibility(View.VISIBLE);
                            viewHolder.tvSharePublisher.setText(homeDynamicModel.tipCategory);
                        } else {
                            viewHolder.tvSharePublisher.setVisibility(View.GONE);
                        }
                    } else {
                        if (!StringUtils.isNull(homeDynamicModel.publisher)) {
                            viewHolder.tvSharePublisher.setVisibility(View.VISIBLE);
                            viewHolder.tvSharePublisher.setText(homeDynamicModel.publisher);
                        } else {
                            viewHolder.tvSharePublisher.setVisibility(View.GONE);
                        }
                    }

                    break;
            }
            fillAvatarIcon(viewHolder, homeDynamicModel);
//            setListeners(viewHolder, homeDynamicModel, position, layoutType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    //这种类型不显示点赞
    private boolean isPublicNewsType(final HomeDynamicModel homeDynamicModel) {
        return homeDynamicModel.type == DynamicModelType.PUBLIC_NEWS;
    }


    private void fillAvatarIcon(final ViewHolder viewHolder, final HomeDynamicModel homeDynamicModel) {
        try {
            String iconUrl =  homeDynamicModel.iconUrl;

            //都没数据，重新赋值，服务端有的类型有修改过
            if (StringUtils.isNull(iconUrl)) {
                if (null != homeDynamicModel.avatarModel) {
                    iconUrl = homeDynamicModel.avatarModel.medium;
                }
            }

            if (!StringUtils.isNull(iconUrl)) {
                int width = this.context.getResources().getDimensionPixelOffset(R.dimen.list_icon_height_40);
                ImageLoader.getInstance().displayImage(context.getApplicationContext(), viewHolder.ivAvatar, iconUrl, R.drawable.apk_mine_photo, R
                                .drawable.apk_mine_photo, 0, 0,
                        true, width, width, null);
            }

            if (homeDynamicModel.type == IHomeDynamicType.LOCAL_DATA) {
                viewHolder.ivAvatar.setImageResource(R.drawable.apk_news_remindmeetyou);
            }
            //设置V认证图标
//            if (AccountAction.isShowV(homeDynamicModel.user_type, homeDynamicModel.isvip)) {
//                if (null == viewHolder.bvVerify) {
//                    viewHolder.bvVerify = new BadgeImageView(context, viewHolder.ivAvatar);
//                    viewHolder.bvVerify.setBadgePosition(BadgeImageView.POSITION_BOTTOM_RIGHT);
//                    viewHolder.bvVerify.setImageResource(AccountAction.getShowVIcon(homeDynamicModel.user_type, homeDynamicModel.isvip));
//                }
//                viewHolder.bvVerify.show();
//            } else {
//                if (viewHolder.bvVerify != null && viewHolder.bvVerify.isShown()) {
//                    viewHolder.bvVerify.hide();
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillImages(View view, ViewHolder viewHolder, final HomeDynamicModel homeDynamicModel, int position) {
        try {
            if (null != homeDynamicModel.imagesList && homeDynamicModel.imagesList.size() > 0) {
                if (homeDynamicModel.imagesList.size() > 1) {
                    viewHolder.vsImageGrid.setVisibility(View.VISIBLE);
                    viewHolder.vsImages.setVisibility(View.GONE);
                    MeasureGridView gvImages = (MeasureGridView) view.findViewById(R.id.gvImage);
                    DynamicImageApdater dynamicImageApdater = homeDynamicModel.imagesListAdapter;
                    if (null == dynamicImageApdater) {
                        dynamicImageApdater = new DynamicImageApdater(context, homeDynamicModel.imagesList, false, 0);
                        dynamicImageApdater.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                             //   handleContentLongClick(homeDynamicModel, view, true);
                                return false;
                            }
                        });
                        homeDynamicModel.imagesListAdapter = dynamicImageApdater;
                    }
                    gvImages.setAdapter(dynamicImageApdater);
                } else {
                    viewHolder.vsImages.setVisibility(View.VISIBLE);
                    viewHolder.vsImageGrid.setVisibility(View.GONE);
                    LinearLayout llImageContainer = (LinearLayout) view.findViewById(R.id.llImageContainer);
                    handleImage(llImageContainer, homeDynamicModel, bigImageWidth, false);

                }
            } else {
                viewHolder.vsImages.setVisibility(View.GONE);
                viewHolder.vsImageGrid.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleImage(LinearLayout linearImage, final HomeDynamicModel homeDynamicModel, int defaultImageWidht, boolean isFullWidth) {
    }




    class ViewHolder {
        RelativeLayout rlItemContainer;
        LoaderImageView ivAvatar;
        TextView tvNickname;
        // TextView tvTypeFrom;
        CustomUrlTextView tvContent;
        // TextView tvWatchMore;
        LinearLayout llDynamicContent;

        LinearLayout llZanReply;
        PraiseButton btn_praise;
        ImageView zanDivider;
        //        LinearLayout llZan;
//        ImageView ivZan;
//        TextView tvZan;
        TextView tvReply;

        ViewStub vsImages;
        ViewStub vsImageGrid;
        ViewStub vsFriendInfo;

        TextView tvRecommendResoan;

        LinearLayout llShareContent;
        LoaderImageView ivShareIcon;
        TextView tvShareTitle;
        TextView tvSharePublisher;
        TextView tvSharePublisher2;
        //TextView tv_video_time;

      //  BadgeImageView bvVerify;
        ImageView ivMoreItem;
        TextView tvTime;

        View divider;
    }
}
