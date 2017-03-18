package nickgao.com.meiyousample.model;

import java.io.Serializable;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsDetailShareBodyModel implements Serializable {
    /**
     *  "is_share": true,   //是否分享
     "src": "",
     "share_url": "https://test-news-node.seeyouyima.com/article?_is_share=1&news_id=1",
     "title": "测试",
     "content": "测试"
     */

    public boolean is_share;
    public String src;
    public String share_url;
    public String title;
    public String content;

}
