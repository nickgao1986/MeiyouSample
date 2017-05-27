package nickgao.com.meiyousample;

/**
 * 帖子详情拓展分享类型
 * Author: hyx
 * Date: 9/23/16 10:17.
 */
public enum TopicDetailAction {
    COPY_TOPIC_URL(R.string.dialog_copy_news_url, R.drawable.all_share_btn_copylink, "qzxq-fzlj", 11);

    private int titleId;
    private int iconId;
    private String traceString;
    private int mShareType;

    /**
     * @param titleId 标题ResId
     * @param iconId 图标ResId
     * @param traceString 友盟统计事件
     * @param shareType 用于选择哪个
     */
    TopicDetailAction(int titleId, int iconId, String traceString, int shareType) {
        this.titleId = titleId;
        this.iconId = iconId;
        this.traceString = traceString;
        this.mShareType = shareType;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTraceString() {
        return traceString;
    }

    public int getShareType() {
        return mShareType;
    }
}
