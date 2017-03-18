package nickgao.com.meiyousample.model;

import java.io.Serializable;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsDetailRecommendModel implements Serializable {
    public int id;
    public String title;
    public String thumb;
    public String video_time;
    public String author;
    public int view_times;
    public int algorithm;//埋点统计使用
    public String url; //图文h5链接
    public String redirect_url;//协议连接
    /**
     * 图集资讯推荐
     * "id": 663803,
     "title": "组图：力压范冰冰！周冬雨马思纯齐获金马影后",
     "src": "http://mycdn.seeyouyima.com/news/img/30087da4d1254afa58d084df392251c0_852_1280.jpg",
     "algorithm": 12,
     "cust": "source_type:2"
     */

    public String src;
    public String cust;

}
