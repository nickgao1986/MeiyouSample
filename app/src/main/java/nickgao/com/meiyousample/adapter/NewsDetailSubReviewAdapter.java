package nickgao.com.meiyousample.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.com.meetyou.news.model.NewsReviewModel;
import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;

/**
 * 资讯详情页面子评论适配器
 * Created by LinXin on 2017/3/4.
 */
public class NewsDetailSubReviewAdapter extends BaseQuickAdapter<NewsReviewModel, BaseViewHolder> implements View.OnClickListener {

    private NewsReviewModel mMainReviewModel;

    public NewsDetailSubReviewAdapter(NewsReviewModel mainReviewModel, List<NewsReviewModel> data) {
        super(R.layout.layout_news_detail_sub_review_item, data);
        this.mMainReviewModel = mainReviewModel;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsReviewModel item) {
        CharSequence content = getContent(item);
        helper.setText(R.id.tv_sub_review, content)
                .setTag(R.id.tv_sub_review, item)
                .setOnClickListener(R.id.tv_sub_review, this);
    }

    /**
     * 生成评论内容
     *
     * @param item
     * @return
     */
    private CharSequence getContent(final NewsReviewModel item) {
        int emojiSize = (int) mContext.getResources().getDimension(R.dimen.list_icon_height_22);
        String name = item.publisher.screen_name;
        String string;
        if (item.reference != null) {
            String referenceName = item.reference.publisher.screen_name;
            string = mContext.getString(R.string.sub_review_content_with_reference, name, referenceName, item.content);
        } else {
            string = mContext.getString(R.string.sub_review_content, name, item.content);
        }
        SpannableString spannableString = new SpannableString(string);
//        NoUnderlineClickableSpan clickableSpan = new NoUnderlineClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                NewsReviewController.getInstance().jump2PersonalActivity(mContext, item.publisher.id);
//            }
//        };
        int start = string.indexOf(name);
        int end = start + name.length();
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(SkinManager.getInstance().getAdapterColor(R.color.colour_a));
       // spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return EmojiConversionUtil.getInstace().getExpressionString(mContext, spannableString, emojiSize, emojiSize);
    }

    @Override
    public void onClick(View v) {
//        NewsReviewModel model = (NewsReviewModel) v.getTag();
//        if (mContext instanceof NewsDetailVideoActivity) {
//            EventBus.getDefault().post(new ClickReviewItemEvent(model));
//        } else {
//            NewsReviewDetailActivity.enterActivity(mContext, mMainReviewModel.id, false, model);
//        }
    }
}
