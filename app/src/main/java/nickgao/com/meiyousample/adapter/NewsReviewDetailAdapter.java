package nickgao.com.meiyousample.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.com.meetyou.news.model.NewsReviewModel;
import com.lingan.seeyou.ui.view.PraiseButton;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.meetyou.crsdk.util.NetWorkStatusUtil;
import com.meetyou.crsdk.util.ToastUtils;
import com.meetyou.news.view.ClickableMovementMethod;
import com.meetyou.news.view.NewsCommentHelper;
import com.meetyou.news.view.NoUnderlineClickableSpan;

import java.util.List;

import nickgao.com.framework.utils.BeanManager;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class NewsReviewDetailAdapter extends BaseQuickAdapter<NewsReviewModel, BaseViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int mNewsId;
    private NewsReviewModel mMainModel;
    private NewsCommentHelper mReplyHelper;
    private long mPageCode;

    public NewsReviewDetailAdapter(Context context, List<NewsReviewModel> list) {
        super(R.layout.layout_news_review_detail_item, list);
        mContext = context;
    }

    /**
     * 设置输入框帮助类
     *
     * @param replyHelper
     */
    public void setReplyHelper(NewsCommentHelper replyHelper) {
        mReplyHelper = replyHelper;
    }

    public void setPageCode(long pageCode) {
        mPageCode = pageCode;
    }

    /**
     * 设置adapter需要的相关值
     */
    public void setValue(int newsId, NewsReviewModel mainModel) {
        this.mNewsId = newsId;
        this.mMainModel = mainModel;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        TextView textView = holder.getView(R.id.tv_review_content);
        if (textView != null) {
            textView.setMovementMethod(ClickableMovementMethod.getInstance());
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NewsReviewModel item) {
        helper.itemView.setTag(item);
        helper.itemView.setOnClickListener(this);
        helper.itemView.setOnLongClickListener(this);
        PraiseButton btnPraise = helper.getView(R.id.btn_praise);
        btnPraise.setPraiseState(item.is_praise);
        btnPraise.setPraiseCount(item.praise_count);
        btnPraise.setOnPraiseButtonListener(new PraiseButton.OnPraiseButtonClickListener() {
            @Override
            public boolean onClick(boolean isPraised) {

                if (NetWorkStatusUtil.queryNetWork(mContext)) {
                   // NewsDetailController.getInstance().praiseNewsReview(mNewsId, mMainModel.id, item.id, item.publisher.id, isPraised);
                    item.is_praise = isPraised;
                    item.praise_count += isPraised ? 1 : -1;
                    return true;
                } else {
                    ToastUtils.showToast(mContext, R.string.network_broken);
                    return false;
                }
            }
        });
        if (item.publisher != null) {
            helper.setText(R.id.tv_user_name, item.publisher.screen_name)
                    .setOnClickListener(R.id.tv_user_name, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  NewsUtils.handleJumpToPersonActivity(mContext, item.publisher.id, item.publisher.error);
                        }
                    });
            TextView textView = helper.getView(R.id.tv_user_name);
            int drawableRight = 0;
//            if (item.is_author) {
//                drawableRight = R.drawable.tag_author;
//            }
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);
        }

        helper.setText(R.id.tv_user_name,"dsfsdfsdfds");
        CharSequence content = getContent(item);
        TextView textView = helper.getView(R.id.tv_review_content);
        textView.setText(content);
    }

    /**
     * 生成评论内容
     *
     * @param item
     * @return
     */
    private CharSequence getContent(final NewsReviewModel item) {
        CharSequence content;
        int emojiSize = (int) mContext.getResources().getDimension(R.dimen.list_icon_height_22);
        if (item.reference != null && item.reference.publisher != null) {
            String name = item.reference.publisher.screen_name;
            String string = mContext.getString(R.string.reply_to_with_content, name, item.content);
            SpannableString spannableString = new SpannableString(string);
            NoUnderlineClickableSpan clickableSpan = new NoUnderlineClickableSpan() {
                @Override
                public void onClick(View widget) {
                  //  NewsUtils.handleJumpToPersonActivity(mContext, item.reference.publisher.id, item.reference.publisher.error);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    int color = SkinManager.getInstance().getAdapterColor(R.color.colour_a);
                    ds.setColor(color);
                }
            };
            int start = string.indexOf(name);
            int end = start + name.length();
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            content = EmojiConversionUtil.getInstace().getExpressionString(mContext, spannableString, emojiSize, emojiSize);
        } else {
            content = EmojiConversionUtil.getInstace().getExpressionString(mContext, item.content, emojiSize, emojiSize);
        }
        return content;
    }

    @Override
    public void onClick(View v) {
        NewsReviewModel model = (NewsReviewModel) v.getTag();
        mReplyHelper.showEditPanel(mNewsId, mMainModel, model, mPageCode);
    }

    @Override
    public boolean onLongClick(View v) {
        NewsReviewModel model = (NewsReviewModel) v.getTag();
        boolean isCurrentUser = BeanManager.getUtilSaver().getUserId(mContext) == model.publisher.id;
//        NewsReviewController.getInstance().showNewsBottomDialog((Activity) mContext,
//                mNewsId,
//                mMainModel.id,
//                model.id,
//                model.publisher.id,
//                model.content,
//                isCurrentUser,
//                mPageCode);
        return false;
    }
}
