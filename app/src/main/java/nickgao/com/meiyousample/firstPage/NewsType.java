package nickgao.com.meiyousample.firstPage;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public enum NewsType {
    NEWS_TXT(1),
    NEWS_TXT_IMAGE(2),
    NEWS_IMAGE(3),
    NEWS_VIDEO(4),
    NEWS_IMAGE_VIDEO(5),
    NEWS_TYPE_VOTE(6),
    NEWS_TYPE_FORUM(7);

    private int newsType;

    NewsType(int type) {
        this.newsType = type;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }
}
