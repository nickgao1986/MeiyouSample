package nickgao.com.meiyousample.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsReviewModel implements Serializable {
    public int id;
    public String content;
    public String created_at;
    public int review_count;
    public int praise_count;
    public boolean is_praise;

    public NewsReviewPublisherModel publisher;
    public NewsReviewReferenceModel reference;
    public NewsDetailBean news_detail;
    public List<NewsReviewModel> sub_review;

    public static class NewsDetailBean {
        public int id;
        public String title;
        public String images;
        public String redirect_url;
    }
}
