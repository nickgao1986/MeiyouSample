package nickgao.com.meiyousample.model;

import java.io.Serializable;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsReviewReferenceModel implements Serializable {
    public int id;
    public String content;
    public String created_at;

    public NewsReviewPublisherModel publisher;
}
