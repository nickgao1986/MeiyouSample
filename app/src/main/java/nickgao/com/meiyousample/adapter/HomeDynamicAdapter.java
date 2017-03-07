package nickgao.com.meiyousample.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomUrlTextView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.listener.OnItemTextExpandListener;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.IHomeDynamicType;
import nickgao.com.meiyousample.utils.CalendarUtil;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;


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


        if (layoutType == IHomeDynamicType.SHUOSHUO || layoutType == IHomeDynamicType.SHUOSHUO_RECOMMEND || layoutType == IHomeDynamicType
                .FRIEND_RECOMMEND || layoutType == IHomeDynamicType.LOCAL_DATA) {
            return LAYOUT_TYPE_DEFAULT;
        } else if (layoutType == IHomeDynamicType.NEWS_DETAIL || layoutType == IHomeDynamicType.SHARE_TO_SHUOSHUO || layoutType == IHomeDynamicType
                .TOPIC_SHARE || layoutType == IHomeDynamicType.TOPIC_SHARE_RECOMMEND || layoutType ==
                IHomeDynamicType.TIPS_SHARE || layoutType == IHomeDynamicType.TOOL || layoutType == IHomeDynamicType.PREGNAN_TPHOTO) {
            return LAYOUT_TYPE_SHARE;
        } else if (layoutType == IHomeDynamicType.CIRCLE_RECOMMEND || layoutType == IHomeDynamicType.INNER_LINK || layoutType == IHomeDynamicType
                .OUTTER_LINK || layoutType == IHomeDynamicType.CIRCLE) {
            return LAYOUT_TYPE_CIRCLE_RECOMMEND;
        }

        return LAYOUT_TYPE_DEFAULT;

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
                        view = ViewFactory.from(this.context).getLayoutInflater().inflate(R.layout.layout_home_dynamic_item, viewGroup, false);
                        viewHolder.vsImages = (ViewStub) view.findViewById(R.id.vsImages);
                        viewHolder.vsImageGrid = (ViewStub) view.findViewById(R.id.vsImagesGrid);
                        viewHolder.tvContent = (CustomUrlTextView) view.findViewById(R.id.tvContent);
                        viewHolder.tvDelete = (TextView) view.findViewById(R.id.tvDelete);
                        viewHolder.tvWatchMore = (TextView) view.findViewById(R.id.tvWatchMore);
                        viewHolder.ivFollow = (ImageView) view.findViewById(R.id.ivFollow);
                        viewHolder.vsFriendInfo = (ViewStub) view.findViewById(R.id.vsFriendInfo);
                        viewHolder.llDynamicContent = (LinearLayout) view.findViewById(R.id.llDynamicContent);
                        viewHolder.ivMoreItem = (ImageView)view.findViewById(R.id.ivItemMore);
                        break;

                }

                viewHolder.rlItemContainer = (RelativeLayout) view.findViewById(R.id.llItemContainer);
                viewHolder.ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
                viewHolder.tvNickname = (TextView) view.findViewById(R.id.tvNickname);
                viewHolder.tvTypeFrom = (TextView) view.findViewById(R.id.tvTypeFrom);
                viewHolder.tvPublishTime = (TextView) view.findViewById(R.id.tvPublishTime);

                viewHolder.llZan = (LinearLayout) view.findViewById(R.id.llZan);
                viewHolder.ivZan = (ImageView) view.findViewById(R.id.ivZanImage);
                viewHolder.tvZan = (TextView) view.findViewById(R.id.tvZan);
                viewHolder.tvReply = (TextView) view.findViewById(R.id.tvReply);
                viewHolder.llZanReply = (LinearLayout) view.findViewById(R.id.llZanReply);
                viewHolder.ivLeftTopBadge = (ImageView) view.findViewById(R.id.ivLeftTopBadge);
                viewHolder.divider = view.findViewById(R.id.divider);
                viewHolder.tvReply.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.apk_tata_comment_gray),
                        null, null, null);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final HomeDynamicModel homeDynamicModel = models.get(position);

            if (!StringUtils.isNull(homeDynamicModel.createTime)) {
                viewHolder.tvPublishTime.setText(CalendarUtil.convertUtcTime(homeDynamicModel.createTime));
                viewHolder.tvPublishTime.setVisibility(View.VISIBLE);
            } else if (!StringUtils.isNull(homeDynamicModel.updateTime)) {
                viewHolder.tvPublishTime.setText(CalendarUtil.convertUtcTime(homeDynamicModel.updateTime));
                viewHolder.tvPublishTime.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvPublishTime.setVisibility(View.GONE);
            }

            //赞和回复
            if (homeDynamicModel.isAllowOperate) {
                viewHolder.llZanReply.setVisibility(View.VISIBLE);
                viewHolder.tvZan.setText(StringUtils.getString(homeDynamicModel.praiseNum));

                if (homeDynamicModel.isPraise == 1) {
                    SkinManager.getInstance().setDrawable(viewHolder.ivZan, R.drawable.apk_tata_good_up);
                    SkinManager.getInstance().setTextColor(viewHolder.tvZan, R.color.red_b);
                } else {
                    viewHolder.ivZan.setImageResource(R.drawable.apk_tata_good);
                    SkinManager.getInstance().setTextColor(viewHolder.tvZan, R.color.black_c);
                }
              //  fillComments(view, viewHolder, position, homeDynamicModel);
            } else {
                viewHolder.llZanReply.setVisibility(View.GONE);
                viewHolder.vsComment.setVisibility(View.GONE);
            }

            if (homeDynamicModel.isExpand == 1) {
                viewHolder.ivLeftTopBadge.setVisibility(View.VISIBLE);
                viewHolder.ivLeftTopBadge.setImageResource(R.drawable.apk_first_add);
            } else if (homeDynamicModel.isRecommend == 1 || homeDynamicModel.type == IHomeDynamicType.FRIEND_RECOMMEND) {
                viewHolder.ivLeftTopBadge.setVisibility(View.VISIBLE);
                viewHolder.ivLeftTopBadge.setImageResource(R.drawable.apk_first_recomment);
            } else {
                viewHolder.ivLeftTopBadge.setVisibility(View.GONE);
            }

            switch (layoutType) {
                case LAYOUT_TYPE_DEFAULT:
                    viewHolder.tvNickname.setText(homeDynamicModel.screenName);
                    if (!StringUtils.isNull(homeDynamicModel.content)) {
                        viewHolder.tvContent.setHtmlUrlTextDynamic(homeDynamicModel.content, false);
                        viewHolder.tvContent.setVisibility(View.VISIBLE);

                    } else {
                        viewHolder.tvContent.setVisibility(View.GONE);
                        viewHolder.tvWatchMore.setVisibility(View.GONE);
                    }

                    //改成角标显示
                    if (homeDynamicModel.type == IHomeDynamicType.SHUOSHUO_RECOMMEND) {
                        viewHolder.tvTypeFrom.setText(homeDynamicModel.recommType);
                    }
                    viewHolder.tvTypeFrom.setText("");
                    viewHolder.tvDelete.setVisibility(View.GONE);


                    //推荐的说说，好友的关注按钮
                    viewHolder.ivFollow.setVisibility(View.GONE);
                    viewHolder.vsFriendInfo.setVisibility(View.GONE);

                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }




    private void scaleAnimation(final ImageView imageView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        imageView.startAnimation(scaleAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                        .RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);
                scaleAnimation.setFillAfter(true);
                imageView.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    class ViewHolder {
        RelativeLayout rlItemContainer;
        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvTypeFrom;
        TextView tvPublishTime;
        TextView tvDelete;
        CustomUrlTextView tvContent;
        TextView tvWatchMore;
        LinearLayout llDynamicContent;

        LinearLayout llZanReply;
        LinearLayout llZan;
        ImageView ivZan;
        TextView tvZan;
        TextView tvReply;

        ViewStub vsComment;
        ViewStub vsImages;
        ViewStub vsImageGrid;
        ViewStub vsFriendInfo;

        TextView tvRecommendResoan;

        LinearLayout llShareContent;
        ImageView ivShareIcon;
        TextView tvShareTitle;
        TextView tvSharePublisher;
        TextView tvSharePublisher2;
        TextView tv_video_time;

        ImageView bvVerify;
        ImageView ivLeftTopBadge;
        ImageView ivFollow;
        ImageView ivMoreItem;

        View divider;
    }
}
