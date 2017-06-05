package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomUrlTextView;
import com.lingan.seeyou.ui.view.MeasureGridView;
import com.lingan.seeyou.ui.view.PraiseButton;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.Iterator;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.DynamicImageApdater;
import nickgao.com.meiyousample.adapter.DynamicModelType;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.IHomeDynamicType;
import nickgao.com.meiyousample.utils.CalendarUtil;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class DynamicHomeRecycleViewAdapter extends RecyclerView.Adapter<DynamicHomeRecycleViewAdapter.ViewHolder> {


    private static final int LAYOUT_TYPE_DEFAULT = 0;  //默认  说说布局
    private static final int LAYOUT_TYPE_SHARE = 1;    //话题分享布局
    private static final int LAYOUT_TYPE_CIRCLE_RECOMMEND = 2;  //话题推荐布局

    private static final int TYPE_COUNT = 2;//2种布局
    private Activity context;
    private List<HomeDynamicModel> models;
    private int bigImageWidth;
    private int fullImageWidht;//推荐话题，宽度铺满
    private int tvContentWidth = 200;

    private int fromID; //来源id

    //是否是个人主页
    private boolean isPersonal = false;


    public DynamicHomeRecycleViewAdapter(Activity context, List<HomeDynamicModel> dataList) {
        this.context = context;
        models = dataList;
        bigImageWidth = DeviceUtils.getScreenWidth(context.getApplicationContext()) / 2; //大图限宽高  屏幕1/2
        fullImageWidht = DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80);
        tvContentWidth = ((DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80)));
    }


    public void setList(List<HomeDynamicModel> dataList) {
        models.clear();
        models = dataList;
        notifyDataSetChanged();
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == LAYOUT_TYPE_DEFAULT) {
            view = this.context.getLayoutInflater().inflate(R.layout.layout_home_dynamic_item, parent, false);
        } else if (viewType == LAYOUT_TYPE_SHARE) {
            view = this.context.getLayoutInflater().inflate(R.layout.layout_home_dynamic_item_share, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        try {
            final HomeDynamicModel homeDynamicModel = models.get(position);

            if (!StringUtils.isNull(homeDynamicModel.createTime)) {
                viewHolder.tvTime.setText(CalendarUtil.convertTime(homeDynamicModel.createTime));
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
            }

            switch (getItemViewType(position)) {
                case LAYOUT_TYPE_DEFAULT:
                    viewHolder.tvNickname.setText(homeDynamicModel.screenName);
                    if (!StringUtils.isNull(homeDynamicModel.content)) {
                        viewHolder.tvContent.setVisibility(View.VISIBLE);
                        viewHolder.tvContent.setText(homeDynamicModel.content);
                    } else {
                        viewHolder.tvContent.setVisibility(View.GONE);
                    }


                    fillImages(viewHolder, homeDynamicModel, position);

                    break;
                case LAYOUT_TYPE_SHARE:
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //这种类型不显示点赞
    private boolean isPublicNewsType(final HomeDynamicModel homeDynamicModel) {
        return homeDynamicModel.type == DynamicModelType.PUBLIC_NEWS;
    }


    private void fillAvatarIcon(final ViewHolder viewHolder, final HomeDynamicModel homeDynamicModel) {
        try {
            String iconUrl = homeDynamicModel.iconUrl;

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillImages(ViewHolder viewHolder, final HomeDynamicModel homeDynamicModel, int position) {
        try {
            if (null != homeDynamicModel.imagesList && homeDynamicModel.imagesList.size() > 0) {
                if (homeDynamicModel.imagesList.size() > 1) {
                    viewHolder.vsImageGrid.setVisibility(View.VISIBLE);
                    viewHolder.vsImages.setVisibility(View.GONE);
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
                    viewHolder.gvImages.setAdapter(dynamicImageApdater);
                } else {
                    viewHolder.vsImages.setVisibility(View.VISIBLE);
                    viewHolder.vsImageGrid.setVisibility(View.GONE);
                    handleImage(viewHolder.llImageContainer, homeDynamicModel, bigImageWidth, false);

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


    @Override
    public int getItemCount() {
        return models.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rlItemContainer;
        LoaderImageView ivAvatar;
        TextView tvNickname;
        CustomUrlTextView tvContent;
        MeasureGridView gvImages;
        LinearLayout llZanReply;
        PraiseButton btn_praise;
        ImageView zanDivider;
        TextView tvReply;
        LinearLayout llImageContainer;
        ViewStub vsImages;
        ViewStub vsImageGrid;

        LinearLayout llShareContent;
        LoaderImageView ivShareIcon;
        TextView tvShareTitle;
        TextView tvSharePublisher;

        ImageView ivMoreItem;
        TextView tvTime;
        View divider;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == LAYOUT_TYPE_DEFAULT) {
                vsImages = (ViewStub) view.findViewById(R.id.vsImages);
                vsImageGrid = (ViewStub) view.findViewById(R.id.vsImagesGrid);
                tvContent = (CustomUrlTextView) view.findViewById(R.id.tvContent);
                ivMoreItem = (ImageView) view.findViewById(R.id.ivItemMore);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                gvImages = (MeasureGridView) view.findViewById(R.id.gvImage);
                llImageContainer = (LinearLayout) view.findViewById(R.id.llImageContainer);
            } else if (viewType == LAYOUT_TYPE_SHARE) {
                tvContent = (CustomUrlTextView) view.findViewById(R.id.tvContent);
                ivShareIcon = (LoaderImageView) view.findViewById(R.id.ivShareIcon);
                tvShareTitle = (TextView) view.findViewById(R.id.tvShareContent);
                llShareContent = (LinearLayout) view.findViewById(R.id.llShareContent);
                tvSharePublisher = (TextView) view.findViewById(R.id.tvSharePublisher);
                ivMoreItem = (ImageView) view.findViewById(R.id.ivItemMore);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
            }

            rlItemContainer = (RelativeLayout) view.findViewById(R.id.llItemContainer);
            ivAvatar = (LoaderImageView) view.findViewById(R.id.ivAvatar);
            tvNickname = (TextView) view.findViewById(R.id.tvNickname);

            btn_praise = (PraiseButton) view.findViewById(R.id.btn_praise);
            tvReply = (TextView) view.findViewById(R.id.tvReply);
            llZanReply = (LinearLayout) view.findViewById(R.id.llZanReply);
            zanDivider = (ImageView) view.findViewById(R.id.zanDivider);

            divider = view.findViewById(R.id.divider);
//            tvReply.setCompoundDrawablesWithIntrinsicBounds(context.getResources().
//                            getDrawable(R.drawable.tata_icon_commentthick),
//                    null, null, null);

        }
    }

}