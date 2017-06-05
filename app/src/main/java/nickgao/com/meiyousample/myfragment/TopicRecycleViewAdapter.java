package nickgao.com.meiyousample.myfragment;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BageImageView;
import com.lingan.seeyou.ui.view.EmptySpaceSpan;
import com.lingan.seeyou.ui.view.IconTextSpan;
import com.lingan.seeyou.ui.view.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.adapter.BaseQuickAdapter;
import nickgao.com.meiyousample.adapter.BaseViewHolder;
import nickgao.com.meiyousample.model.topic.MyTopicModel;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class TopicRecycleViewAdapter extends BaseQuickAdapter<MyTopicModel, BaseViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Activity mActivity;
    private int mImageSize;
    private boolean isEditMode;
    private int leftViewWidth;
    private int mScreenWidth;
    public List<MyTopicModel> listModel;

    private int lastVisiblePosition = 0;
    private int firstVisiblePosition = 0;
    private TopicType mType;
    private ListView mListView;
    private boolean delTopicDoor;  //删帖开关
    public int screenWidth;
    //图片显示的宽度
    private int imageWidth;
    private int bottomSpace10;
    private int bottomHeight;
    private int leftSpace12;

    public enum TopicType {
        TYPE_PUBLISH, TYPE_REPLY
    }


    public TopicRecycleViewAdapter(Activity activity, List<MyTopicModel> list, TopicType type) {
        super(R.layout.layout_my_topic_item_content_for_personal, list);
        this.mActivity = activity;
        this.mType = type;
        if (this.mType == TopicType.TYPE_PUBLISH) {
            delTopicDoor = true;
        }
        mScreenWidth = DeviceUtils.getScreenWidth(mActivity.getApplicationContext());
        mImageSize = (mScreenWidth - DeviceUtils.dip2px(mActivity, 20) - DeviceUtils.dip2px(mActivity, 6 * 2)) / 3;
        leftViewWidth = (int) mActivity.getApplicationContext().getResources().getDimension(R.dimen.list_icon_height_50);
        bottomSpace10 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 10);
        leftSpace12 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 12);

        this.imageWidth = (mScreenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 24) - DeviceUtils.dip2px(SeeyouApplication.getContext(), 3 * 2)) / 3;
        bottomHeight = mScreenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 39) - imageWidth - leftSpace12;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyTopicModel model) {
        if (model.images != null && model.images.size() > 0) {
            ImageLoadParams imageLoadParams = new ImageLoadParams();
            imageLoadParams.height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2
            imageLoadParams.width = imageWidth;
            imageLoadParams.defaultholder = R.color.black_f;
            imageLoadParams.tag = mActivity.hashCode();

            if (model.images.size() == 1 || model.images.size() == 2) {
                helper.setVisible(R.id.iv_multi_image, false);
                helper.setVisible(R.id.iv_image, true);

                BageImageView ivImageView = helper.getView(R.id.iv_image);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivImageView.getLayoutParams();
                layoutParams.rightMargin = leftSpace12;
                layoutParams.height = imageLoadParams.height;//扁图的比列是3:2
                layoutParams.width = imageWidth;
                ivImageView.setLayoutParams(layoutParams);


                if (!TextUtils.isEmpty(model.video_time)) {
                    ivImageView.displayImage(model.images.get(0), imageLoadParams, model.video_time);
                } else {
                    ivImageView.displayImage(model.images.get(0), imageLoadParams, null);
                }
            } else {

                helper.setVisible(R.id.iv_multi_image, true);
                helper.setVisible(R.id.iv_image, false);
                if (model.images.size() > 3) {
                    model.images = model.images.subList(0, 3);
                }
                MultiImageView ivMultiImage = helper.getView(R.id.iv_multi_image);
                ivMultiImage.displayImage(getDisplayImageModel(model), imageWidth, imageLoadParams.height, 6, imageLoadParams);
            }

        } else {

            helper.setVisible(R.id.iv_multi_image, false);
            helper.setVisible(R.id.iv_image, false);
        }

        TextView tvCommentCount = helper.getView(R.id.tvCommentCount);

        //来源显示与否
        if (StringUtils.isNull(model.forum_name)) {
            helper.setVisible(R.id.tvCircleName, false);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvCommentCount.getLayoutParams();
            params.setMargins(0, 0, 0, 0);

        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvCommentCount.getLayoutParams();
            params.setMargins(DeviceUtils.dip2px(mActivity, 10), 0, 0, 0);
            helper.setVisible(R.id.tvCircleName, true);
            helper.setText(R.id.tvCircleName, model.forum_name);
            // 圈子名称
        }

        handleShowTopOrJinghua(helper, model);
        // 评论icon不换肤
        helper.setText(R.id.tvCommentCount, model.total_review + "回复");

        handleSetWidthAndHeight(model, helper);
        setListener(helper, model);


    }


    /**
     * 代码计算布局 太坑了这个布局设计的
     */
    private void handleSetWidthAndHeight(MyTopicModel model, final BaseViewHolder helper) {
        int feedBackHeight = DeviceUtils.dip2px(SeeyouApplication.getContext(), 28);
        LinearLayout ll_content = helper.getView(R.id.ll_content);
        BageImageView iv_image = helper.getView(R.id.iv_image);
        RelativeLayout rlBottomContent = helper.getView(R.id.rlBottomContent);
        RelativeLayout.LayoutParams ll_contentParams = (RelativeLayout.LayoutParams) ll_content.getLayoutParams();
        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        ll_contentParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        ll_content.requestLayout();

        RelativeLayout.LayoutParams rlBottomContentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
        rlBottomContentParams.addRule(RelativeLayout.LEFT_OF, iv_image.getId());
        rlBottomContentParams.addRule(RelativeLayout.BELOW, ll_content.getId());

        //话题和资讯的类型 图片逻辑 大图类型
        if (true) {
            //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
            int height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2


            int imageSize = model.images.size();//图片大小
            if (imageSize > 0) {
                if (imageSize < 3) {
                    if (false) {//超过三行字体的时候扁图的时候要换行显示底部布局
                        RelativeLayout.LayoutParams rlBottomContentParamsTwo = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
                        rlBottomContentParamsTwo.addRule(RelativeLayout.BELOW, ll_content.getId());
                        rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                        rlBottomContent.setLayoutParams(rlBottomContentParamsTwo);

                        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        ll_contentParams.height = height + bottomSpace10;
                        ll_content.requestLayout();
                    } else {
                        rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                        rlBottomContent.setLayoutParams(rlBottomContentParams);

                        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                        ll_contentParams.height = height - feedBackHeight + DeviceUtils.dip2px(SeeyouApplication.getContext(), 6);
                        ll_content.requestLayout();
                    }
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_image.getLayoutParams();
                    layoutParams.height = height;//扁图的比列是3:2
                    layoutParams.width = imageWidth;
                    iv_image.setLayoutParams(layoutParams);
                } else {//有3个图片和没有开启省流量模式的时候
                    rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 3);
                    rlBottomContent.setLayoutParams(rlBottomContentParams);
                }
            } else {
                rlBottomContentParams.topMargin = 0;
                rlBottomContent.setLayoutParams(rlBottomContentParams);
            }
        }


    }


    private void setListener(final BaseViewHolder helper, final MyTopicModel model) {

        helper.setOnClickListener(R.id.ivItemMore, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void handleShowTopOrJinghua(final BaseViewHolder helper, MyTopicModel model) {
        try {
            StringBuilder builder = new StringBuilder();
            CharacterStyle[] spans = new CharacterStyle[20];
            int spansCount = 0;
            if (model.is_elite) { //精
                builder.append(" ");
                spans[spansCount++] = new IconTextSpan(mActivity.getApplicationContext(), R.color.tag_elite, "精");
            }
            if (model.is_recommended) { //荐
                builder.append(" ");
                spans[spansCount++] = new IconTextSpan(mActivity.getApplicationContext(), R.color.tag_recommend, "荐");
            }
            if (model.is_feeds) {//首
                builder.append(" ");
                spans[spansCount++] = new IconTextSpan(mActivity.getApplicationContext(), R.color.tag_shou, "首");
            }
            //空白字符占位，避免文字贴得太紧
            if (spansCount > 0) {
                builder.append(" ");
                spans[spansCount++] = new EmptySpaceSpan(mActivity.getApplicationContext(), 3); //补上3dp与文字的间隙
            }
            SpannableString spannableString = new SpannableString(builder.append(model.title));
            for (int i = 0; i <= spansCount - 1; i++) {
                spannableString.setSpan(spans[i], i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            helper.setText(R.id.tvTopicTitle, spannableString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private List<MultiImageView.DisplayImageModel> getDisplayImageModel(MyTopicModel model) {
        List<MultiImageView.DisplayImageModel> models = new ArrayList<>();
        for (String images : model.images) {
            MultiImageView.DisplayImageModel displayImageModel = new MultiImageView.DisplayImageModel();
            displayImageModel.imageUrl = images;
            displayImageModel.isVideo = model.is_video;
            displayImageModel.isJudgeGif = true;
            displayImageModel.isJudgeLongPicture = true;
            models.add(displayImageModel);
        }
        return models;
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
