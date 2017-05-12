package nickgao.com.meiyousample.adapter;

import android.app.Activity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BageImageView;
import com.lingan.seeyou.ui.view.EmptySpaceSpan;
import com.lingan.seeyou.ui.view.IconTextSpan;
import com.lingan.seeyou.ui.view.MultiImageView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.model.topic.MyTopicModel;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;
import nickgao.com.okhttpexample.view.ImageLoadParams;


/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class MyPersonalTopicListAdapter extends BaseAdapter {
    public List<MyTopicModel> listModel;
    private Activity mActivity;
    private int mImageSize;
    private boolean isEditMode;
    private int leftViewWidth;
    private int mScreenWidth;

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

    public MyPersonalTopicListAdapter(Activity activity, List<MyTopicModel> listModel, TopicType type, ListView listView) {
        this.mActivity = activity;
        this.listModel = listModel;
        this.mType = type;
        if (this.mType == TopicType.TYPE_PUBLISH) {
            delTopicDoor = true;
        }
        this.mListView = listView;
        mScreenWidth = DeviceUtils.getScreenWidth(mActivity.getApplicationContext());
        mImageSize = (mScreenWidth - DeviceUtils.dip2px(mActivity, 20) - DeviceUtils.dip2px(mActivity, 6 * 2)) / 3;
        leftViewWidth = (int) mActivity.getApplicationContext().getResources().getDimension(R.dimen.list_icon_height_50);
        bottomSpace10 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 10);
        leftSpace12 = DeviceUtils.dip2px(SeeyouApplication.getContext(), 12);

        this.imageWidth = (mScreenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 24) - DeviceUtils.dip2px(SeeyouApplication.getContext(), 3 * 2)) / 3;
        bottomHeight = mScreenWidth - DeviceUtils.dip2px(SeeyouApplication.getContext(), 39) - imageWidth - leftSpace12;

    }

    public void setLastVisiblePosition(int lastVisiblePosition) {
        this.lastVisiblePosition = lastVisiblePosition;
    }

    public void setFirstVisiblePosition(int firstVisiblePosition) {
        this.firstVisiblePosition = firstVisiblePosition;
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public MyTopicModel getItem(int position) {
        if (getCount() == 0)
            return null;
        //
        if (position >= getCount()) {
            position = getCount() - 1;
        } else if (position < 0) {
            position = 0;
        }
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listModel.get(position).topic_id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ViewFactory.from(mActivity).getLayoutInflater().inflate(
                    R.layout.layout_my_topic_item_content_for_personal, null, false);

            convertView.setTag(viewHolder);
            viewHolder.initView(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyTopicModel model = listModel.get(position);
        //分割线
        if (position == getCount() - 1) {
            viewHolder.bottomLine.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.bottomLine.setVisibility(View.VISIBLE);
        }
        //图片

        if (model.images != null && model.images.size() > 0) {
            ImageLoadParams imageLoadParams = new ImageLoadParams();
            imageLoadParams.height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2
            imageLoadParams.width = imageWidth;
            imageLoadParams.defaultholder = R.color.black_f;
            imageLoadParams.tag = mActivity.hashCode();

            if (model.images.size() == 1 || model.images.size() == 2) {
                viewHolder.ivMultiImage.setVisibility(View.GONE);
                viewHolder.iv_image.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.iv_image.getLayoutParams();
                layoutParams.rightMargin = leftSpace12;
                layoutParams.height = imageLoadParams.height;//扁图的比列是3:2
                layoutParams.width = imageWidth;
                viewHolder.iv_image.setLayoutParams(layoutParams);

//                ImageLoader.getInstance().displayImage(mActivity,
//                        viewHolder.iv_image, model.images.get(0),
//                        R.drawable.apk_mine_photo, R.drawable.apk_mine_photo, 0, 0,
//                        false, imageWidth, imageLoadParams.height, null);


                if (!TextUtils.isEmpty(model.video_time)) {
                    viewHolder.iv_image.displayImage(model.images.get(0), imageLoadParams, model.video_time);
                } else {
                    viewHolder.iv_image.displayImage(model.images.get(0), imageLoadParams, null);
                }
                //viewHolder.iv_image.displayImage(model.images.get(0),imageLoadParams);
            } else {
                viewHolder.ivMultiImage.setVisibility(View.VISIBLE);
                viewHolder.iv_image.setVisibility(View.GONE);
                if (model.images.size() > 3) {
                    model.images = model.images.subList(0, 3);
                }

                viewHolder.ivMultiImage.displayImage(getDisplayImageModel(model), imageWidth, imageLoadParams.height, 6, imageLoadParams);
            }

        } else {
            viewHolder.iv_image.setVisibility(View.GONE);
            viewHolder.ivMultiImage.setVisibility(View.GONE);
        }

        //来源显示与否
        if (StringUtils.isNull(model.forum_name)) {
            viewHolder.tvCircleName.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.tvCommentCount.getLayoutParams();
            params.setMargins(0, 0, 0, 0);

        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.tvCommentCount.getLayoutParams();
            params.setMargins(DeviceUtils.dip2px(mActivity, 10), 0, 0, 0);
            viewHolder.tvCircleName.setVisibility(View.VISIBLE);
            // 圈子名称
            viewHolder.tvCircleName.setText(model.forum_name);
        }

        handleShowTopOrJinghua(viewHolder,model);
        // 评论icon不换肤
        viewHolder.tvCommentCount.setText(model.total_review + "回复");

        handleSetWidthAndHeight(model, viewHolder);
        setListener(viewHolder, model);

        return convertView;
    }

    private void setListener(final ViewHolder viewHolder, final MyTopicModel model) {
        viewHolder.ivItemMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    /**
     * 判断是否全选
     *
     * @param list
     * @return
     */
    private boolean checkIsSelectAll(List<MyTopicModel> list) {
        if (null == list || list.size() == 0) {
            return false;
        }
        for (MyTopicModel myTopicModel : list) {
            if (!myTopicModel.isSelected) {
                return false;
            }
        }
        return true;
    }

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


    /**
     * 代码计算布局 太坑了这个布局设计的
     */
    private void handleSetWidthAndHeight(MyTopicModel model, ViewHolder vHolder) {
        int feedBackHeight = DeviceUtils.dip2px(SeeyouApplication.getContext(), 28);

        RelativeLayout.LayoutParams ll_contentParams = (RelativeLayout.LayoutParams) vHolder.ll_content.getLayoutParams();
        ll_contentParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        ll_contentParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        vHolder.ll_content.requestLayout();

        RelativeLayout.LayoutParams rlBottomContentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, feedBackHeight);
        rlBottomContentParams.addRule(RelativeLayout.LEFT_OF, vHolder.iv_image.getId());
        rlBottomContentParams.addRule(RelativeLayout.BELOW, vHolder.ll_content.getId());

        //话题和资讯的类型 图片逻辑 大图类型
        if (true) {
            //话题图片和资讯除'纯图片和纯视频'外的类型图片显示
            int height = (int) ((double) imageWidth / 1.5);//扁图的比列是3:2


            int imageSize = model.images.size();//图片大小
            if (imageSize > 0) {
                if (imageSize < 3) {

                    if (getTextViewLineCount(vHolder.tvTopicTitle,
                            model.title, bottomHeight) >= 3) {//超过三行字体的时候扁图的时候要换行显示底部布局
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
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vHolder.iv_image.getLayoutParams();
                    layoutParams.height = height;//扁图的比列是3:2
                    layoutParams.width = imageWidth;
                    vHolder.iv_image.setLayoutParams(layoutParams);
                } else {//有3个图片和没有开启省流量模式的时候
                    rlBottomContentParams.topMargin = DeviceUtils.dip2px(SeeyouApplication.getContext(), 3);
                    vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
                }
            } else {
                rlBottomContentParams.topMargin = 0;
                vHolder.rlBottomContent.setLayoutParams(rlBottomContentParams);
            }
        }


    }

    private void handleShowTopOrJinghua(ViewHolder holder, MyTopicModel model) {
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
            holder.tvTopicTitle.setText(spannableString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private class ViewHolder {
        private View bottomLine;

        //正常样式
        private TextView tvTopicTitle, tvCircleName;
        private TextView tvCommentCount;
        private MultiImageView ivMultiImage;
        private BageImageView iv_image;
        private LinearLayout ll_content;
        private RelativeLayout rlBottomContent;
        private ImageView ivItemMore;

        public void initView(View convertView) {
            ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
            rlBottomContent = (RelativeLayout) convertView.findViewById(R.id.rlBottomContent);
            bottomLine = convertView.findViewById(R.id.publish_line);
            iv_image = (BageImageView) convertView.findViewById(R.id.iv_image);
            ivItemMore = (ImageView) convertView.findViewById(R.id.ivItemMore);
            //正常样式
            ivMultiImage = (MultiImageView) convertView.findViewById(R.id.iv_multi_image);
            tvCircleName = (TextView) convertView.findViewById(R.id.tvCircleName);
            tvTopicTitle = (TextView) convertView.findViewById(R.id.tvTopicTitle);
            tvCommentCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
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

    void setEditMode(boolean value) {
        isEditMode = value;
        notifyDataSetChanged();
    }
}
