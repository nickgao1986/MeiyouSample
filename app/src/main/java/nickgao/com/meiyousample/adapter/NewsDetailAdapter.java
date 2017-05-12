package nickgao.com.meiyousample.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.PraiseButton;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.model.NewsReviewModel;
import nickgao.com.meiyousample.utils.CalendarUtil;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * 资讯详情回复Adapter
 * Created by huangyuxiang on 2016/11/10.
 */

public class NewsDetailAdapter extends BaseAdapter {
    private Activity mContext;
    private List<NewsReviewModel> mReviews;
    private ImageLoadParams mAvatarLoadParams;
    private boolean isShowPraise;  //是否显示点赞按钮
    private int mNewsId; //资讯id
    private boolean showNoReviewTip = false;  //如果没有数据是否显示tip

    private int mScreenWidth;  //屏幕宽度
    private long mPageCode;
    private boolean isVideo;

    private View.OnClickListener mGotoReviewDetailClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            NewsReviewModel model = (NewsReviewModel) v.getTag();
//            if (isVideo) {
//                EventBus.getDefault().post(new ClickReviewItemEvent(model));
//            } else {
//                NewsReviewDetailActivity.enterActivity(mContext, model.id, false, null);
//            }
        }
    };

    public NewsDetailAdapter(Activity context, List<NewsReviewModel> reviews, int newsId, long pageCode, boolean isVideo) {
        this.mContext = context;
        this.mReviews = reviews;
        this.mNewsId = newsId;
        this.mPageCode = pageCode;

        this.mScreenWidth = DeviceUtils.getScreenWidth(mContext);
        int mAvatarSize = DeviceUtils.dip2px(mContext, 32);
        this.mAvatarLoadParams = new ImageLoadParams();
        mAvatarLoadParams.width = mAvatarSize;
        mAvatarLoadParams.height = mAvatarSize;
        mAvatarLoadParams.round = true;
        mAvatarLoadParams.defaultholder = R.drawable.apk_mine_photo;
        mAvatarLoadParams.tag = mContext.hashCode();

        this.isVideo = isVideo;
    }

    @Override
    public int getCount() {
        if (mReviews == null || mReviews.size() == 0) {
            return showNoReviewTip ? 1 : 0; //没有评论时显示"无评论"icon
        } else {
            return mReviews.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ViewFactory.from(mContext).getLayoutInflater().inflate(R.layout.layout_news_review_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //判断是否要显示顶部间隙
        if (position == 0) {
            if (parent instanceof ListView && ((ListView) parent).getHeaderViewsCount() > 0) {
                //有头部才显示顶部间隙
                viewHolder.topSpaceView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.topSpaceView.setVisibility(View.GONE);
            }
        } else {
            viewHolder.topSpaceView.setVisibility(View.GONE);
        }

        //分割线
        if (shouldShowDivider(position)) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }

        if (mReviews == null || mReviews.size() == 0) { //无评论提示
            viewHolder.rlReviewContent.setVisibility(View.GONE);
            viewHolder.llEmptyReviewTip.setVisibility(View.VISIBLE);
            viewHolder.llEmptyReviewTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mOnNoReviewClickListener != null) {
//                       // mOnNoReviewClickListener.onClick();
//                    }
                }
            });
        } else {
            viewHolder.rlReviewContent.setVisibility(View.VISIBLE);
            viewHolder.llEmptyReviewTip.setVisibility(View.GONE);

            final NewsReviewModel model = (NewsReviewModel) getItem(position);
            if (model.publisher != null) {
//                ImageLoader.getInstance().displayImage(mContext.getApplicationContext(), viewHolder.ivUserAvatar, model.publisher.avatar, mAvatarLoadParams, null);
//                viewHolder.ivUserAvatar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NewsDetailController.getInstance().jump2PersonalActivity(mContext, model.publisher.id);
//                    }
//                });
                viewHolder.tvUserName.setText(model.publisher.screen_name);
                viewHolder.tvUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //NewsDetailController.getInstance().jump2PersonalActivity(mContext, model.publisher.id);
                    }
                });
            }
            viewHolder.tvPublishTime.setText(CalendarUtil.convertUtcTime(model.created_at));
            int emojiSize = (int) mContext.getResources().getDimension(R.dimen.list_icon_height_22);
            viewHolder.tvContent.setText(EmojiConversionUtil.getInstace().getExpressionString(mContext, model.content, emojiSize, emojiSize));
            //展开全部
            int lineCount = getTextViewLineCount(model.content, getTvContentWidth(), viewHolder.tvContent.getPaint());
            if (lineCount > 6 && !isVideo) {
                //内容大于6行，显示“展开全部”
                viewHolder.tvExpandAll.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvExpandAll.setVisibility(View.GONE);
            }
            //叠楼
            int revealNum = 5;//透传最大值
            if (model.sub_review != null && !model.sub_review.isEmpty() && revealNum != 0) {
                viewHolder.mSubReviewLayout.setVisibility(View.VISIBLE);
                List<NewsReviewModel> subReviews = model.sub_review;
                if (subReviews.size() > revealNum) {
                    subReviews = subReviews.subList(0, revealNum);
                }
                viewHolder.mSubRecyclerView.setAdapter(new NewsDetailSubReviewAdapter(model, subReviews));
                //"查看更多xx条回复
                int restCount = model.review_count - revealNum;
                if (restCount > 0) {
                    viewHolder.llSeeMoreReview.setVisibility(View.VISIBLE);
                    viewHolder.tvMoreNReviews.setText(mContext.getString(R.string.more_n_reviews, restCount));
                } else {
                    viewHolder.llSeeMoreReview.setVisibility(View.GONE);
                }
            } else {
                viewHolder.mSubReviewLayout.setVisibility(View.GONE);
            }
            //点赞按钮
            if (isShowPraise) {
                viewHolder.btnPraise.setVisibility(View.VISIBLE);
                viewHolder.btnPraise.setPraiseState(model.is_praise);
                viewHolder.btnPraise.setPraiseCount(model.praise_count);
//                viewHolder.btnPraise.setOnPraiseButtonListener(new PraiseButton.OnPraiseButtonClickListener() {
//                    @Override
//                    public boolean onClick(boolean isPraised) {
//                        if (NetWorkStatusUtil.queryNetWork(mContext)) {
//                            NewsDetailController.getInstance().praiseNewsReview(mNewsId, model.id, 0, model.publisher.id, isPraised);
//                            model.is_praise = isPraised;
//                            model.praise_count += isPraised ? 1 : -1;
//                            return true;
//                        } else {
//                            ToastUtils.showToast(mContext, R.string.network_broken);
//                            return false;
//                        }
//                    }
//                });
            } else {
                viewHolder.btnPraise.setVisibility(View.GONE);
            }

            viewHolder.llSeeMoreReview.setTag(model);
            viewHolder.rlReviewContent.setTag(model);
        }
        viewHolder.rlReviewContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleItemLongClick(position);
                return true;
            }
        });
        return convertView;
    }

    /**
     * 处理长按点击
     *
     * @param position
     */
    private void handleItemLongClick(int position) {
        if (mReviews.size() == 0) {
            return;
        }
//        NewsReviewModel newsReviewModel = (NewsReviewModel) getItem(position);
//        NewsDetailController.getInstance().showNewsBottomDialog(mContext, mNewsId, newsReviewModel.id, 0,
//                newsReviewModel.publisher.id, newsReviewModel.content,
//                BeanManager.getUtilSaver().getUserId(mContext) == newsReviewModel.publisher.id, mPageCode);
    }

    /**
     * 当前Item是否应该显示底部分割线
     *
     * @param position 当前position
     */
    private boolean shouldShowDivider(int position) {
        return !(position == getCount() - 1);  //最后一个Item不显示;
    }

    /**
     * 获取TextView行数
     *
     * @param text          内容
     * @param textViewWidth 控件宽度
     * @param paint         textView的paint
     * @return
     */
    private int getTextViewLineCount(String text, int textViewWidth, TextPaint paint) {
        StaticLayout staticLayout = new StaticLayout(text, paint, textViewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        return staticLayout.getLineCount();
    }

    /**
     * 获取tvContent的宽度
     *
     * @return
     */
    private int getTvContentWidth() {
        return mScreenWidth - DeviceUtils.dip2px(mContext, 12 + 32 + 10 + 12);
    }

    private class ViewHolder {
        //没评论提示
        LinearLayout llEmptyReviewTip;
        //评论内容
        RelativeLayout rlReviewContent;
        LoaderImageView ivUserAvatar;
        TextView tvUserName, tvPublishTime, tvContent;
        TextView tvExpandAll, tvReview, tvMoreNReviews;
        PraiseButton btnPraise;
        ImageView divider;
        View topSpaceView;
        RecyclerView mSubRecyclerView;
        View mSubReviewLayout;
        View llSeeMoreReview;

        private ViewHolder(View view) {
            llSeeMoreReview = view.findViewById(R.id.ll_see_more_review);
            llEmptyReviewTip = (LinearLayout) view.findViewById(R.id.ll_empty_review_tip);
            rlReviewContent = (RelativeLayout) view.findViewById(R.id.rl_review_content);
            ivUserAvatar = (LoaderImageView) view.findViewById(R.id.iv_user_avatar);
            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvExpandAll = (TextView) view.findViewById(R.id.tv_expand_all);
            tvExpandAll.setVisibility(View.GONE);
            tvReview = (TextView) view.findViewById(R.id.tv_review);
            tvMoreNReviews = (TextView) view.findViewById(R.id.tv_more_review);
            btnPraise = (PraiseButton) view.findViewById(R.id.btn_praise);
            //特殊处理，ui说要和她她圈不一样,不定死宽度
            ViewGroup.LayoutParams praiseParams = btnPraise.getTvPraiseCount().getLayoutParams();
            if (praiseParams != null) {
                praiseParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                btnPraise.getTvPraiseCount().setLayoutParams(praiseParams);
            }
            divider = (ImageView) view.findViewById(R.id.divider);
            topSpaceView = view.findViewById(R.id.top_space_view);

            mSubRecyclerView = (RecyclerView) view.findViewById(R.id.rv_sub_review);
            mSubRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext));
            mSubRecyclerView.setFocusable(false);
            mSubRecyclerView.clearFocus();
            int dividerHeight = (int) mContext.getResources().getDimension(R.dimen.dp_value_8);
            mSubRecyclerView.addItemDecoration(new VSpaceItemDecoration(dividerHeight));
            mSubReviewLayout = view.findViewById(R.id.ll_sub_review);

            rlReviewContent.setOnClickListener(mGotoReviewDetailClick);//点击“内容区域”.........弹输入框
            llSeeMoreReview.setOnClickListener(mGotoReviewDetailClick);//点击“查看更多”.........不弹

            if (isVideo) {//视频资讯页面取消6行限制
                tvContent.setMaxLines(Integer.MAX_VALUE);
            }
        }
    }

    public void setShowPraise(boolean showPraise) {
        this.isShowPraise = showPraise;
    }

    public void setShowNoReviewTip(boolean showNoReviewTip) {
        this.showNoReviewTip = showNoReviewTip;
    }
}
