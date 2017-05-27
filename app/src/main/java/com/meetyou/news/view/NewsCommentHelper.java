package com.meetyou.news.view;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.com.meetyou.news.model.NewsReviewModel;
import com.lingan.seeyou.ui.view.widget.EmojiLayout;
import com.meetyou.crsdk.util.NetWorkStatusUtil;
import com.meetyou.crsdk.util.ToastUtils;
import com.meiyou.app.common.inputbar.CommonInputBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;


/**
 * 资讯回复帮助类
 * Created by LinXin on 2017/5/12.
 */
public class NewsCommentHelper {

    private static final int MAX_CONTENT_COUNT = 300;

    private Activity mActivity;
    private CommonInputBar mInputBar;

    private boolean isInReviewDetail;//是否在评论详情里面，评论详情页里回复主评论的文案不是@主评论的作者
    private int mNewsId;//资讯id
    private int mReviewId;//主评论id
    private int mReferenceId;//子评论id
    private long mPageCode;//Event标示符

    public NewsCommentHelper(CommonInputBar inputBar) {
        this.mActivity = (Activity) inputBar.getContext();
        this.mInputBar = inputBar;
        initInputBar();
    }

    public static int count(String str) {
        int singelC = 0, doubleC = 0;
        String s = "[^\\x00-\\xff]";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(str);

        while (ma.find()) {
            doubleC++;
        }
        singelC = str.length() - doubleC;
        if (singelC % 2 != 0) {
            doubleC += (singelC + 1) / 2;
        } else {
            doubleC += singelC / 2;
        }
        return doubleC;
    }

    /**
     * 初始化输入栏
     */
    private void initInputBar() {
        mInputBar.setDefaultHint(mActivity.getString(R.string.news_review_hint));
        mInputBar.setOnSubmitClickListener(new CommonInputBar.OnSubmitClickListener() {

            @Override
            public void onSubmitClick(final Editable editable) {
                String content = editable.toString();
                if (StringUtils.isNull(content.trim())) {
                    ToastUtils.showToast(mActivity, "您的回复为空，多写一点吧");
                    return;
                }
                if (!NetWorkStatusUtil.queryNetWork(mActivity.getApplicationContext())) {
                    ToastUtils.showToast(mActivity, R.string.network_broken);
                    return;
                }
                //提交评论
//                NewsDetailController.getInstance().postNewsReview(mActivity, mNewsId, mReviewId, mReferenceId, content, mPageCode, new OnCallBackListener() {
//                    @Override
//                    public void OnCallBack(Object o) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                editable.clear();
//                                mInputBar.dismissDialog();
//                            }
//                        });
//                    }
//                });
            }
        });
        mInputBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //内容字数判断
                String strContent = s.toString();
                int length = count(strContent);
                if (StringUtils.isNull(strContent) && length <= MAX_CONTENT_COUNT) {
                    mInputBar.setSubmitEnable(false);
                } else {
                    mInputBar.setSubmitEnable(true);
                }
                if (length > MAX_CONTENT_COUNT) {
                    String limit = mActivity.getString(R.string.news_review_limit, MAX_CONTENT_COUNT);
                    ToastUtils.showToast(mActivity.getApplicationContext(), limit);
                    String strTitle = StringUtils.subStr(strContent, MAX_CONTENT_COUNT);
                    String startString;
                    int lastLen = strTitle.lastIndexOf("[");
                    if (lastLen != -1) {
                        String lastString = strTitle.substring(lastLen, strTitle.length());
                        if (!lastString.contains("]")) {
                            startString = strTitle.substring(0, lastLen);
                        } else {
                            startString = strTitle;
                        }
                    } else {
                        startString = strTitle;
                    }
                    mInputBar.setText(startString);
                    mInputBar.setSelection(startString.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputBar.setOnEmojiViewShowListener(new EmojiLayout.OnEmojiViewShowListener() {
            @Override
            public void onShow() {
            }

            @Override
            public void onHide() {

            }
        });
    }

    /**
     * 是否在评论详情中
     *
     * @param inReviewDetail
     */
    public void setInReviewDetail(boolean inReviewDetail) {
        this.isInReviewDetail = inReviewDetail;
    }

    /**
     * 设置NewsId和PageCode
     *
     * @param newsId
     * @param pageCode
     */
    public void updateNewsIdAndPageCode(int newsId, long pageCode) {
        this.mNewsId = newsId;
        this.mPageCode = pageCode;
    }

    /**
     * 显示输入框
     *
     * @param newsId
     * @param mainModel
     * @param subModel
     * @param pageCode
     */
    public void showEditPanel(int newsId, NewsReviewModel mainModel, NewsReviewModel subModel, long pageCode) {
//        if (!NewsDetailController.getInstance().checkIsLoginedAndNickName(mActivity))
//            return;
//        if (NewsDetailController.getInstance().isNeedGotoBindPhone(mActivity)) {
//            return;
//        }
        mPageCode = pageCode;
        mNewsId = newsId;
        mReviewId = 0;
        mReferenceId = 0;
        String name = null;
        if (mainModel != null) {//主评论
            mReviewId = mainModel.id;
            if (!isInReviewDetail) {//评论详情页里，回复主评论是不用显示主评论的作者
                name = mainModel.publisher.screen_name;
            }
        }
        if (subModel != null) {//子评论
            mReferenceId = subModel.id;
            name = subModel.publisher.screen_name;
        }
        String hint = mActivity.getString(R.string.news_review_hint);
        if (!TextUtils.isEmpty(name)) {
            hint = mActivity.getString(R.string.reply_to, name);
        }
        mInputBar.showEditTextWithHint(hint);
    }
}
