package nickgao.com.meiyousample.model;

import com.com.meetyou.news.model.NewsReviewModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsDetailReviewListModel implements Serializable {
    public boolean is_show_praise; //是否显示点赞按钮
    public NewsDetailShareBodyModel share_body;

    public List<NewsReviewModel> hot_reviews;
    public List<NewsReviewModel> news_review;
    public List<NewsDetailRecommendModel> news_recommend;//视频资讯推荐

    public boolean is_favorite;
    public int review_count;
    public int author_type;//1是美柚号文章 0不是
    public NewsReviewPublisherModel publisher;//美柚号发布人
}
