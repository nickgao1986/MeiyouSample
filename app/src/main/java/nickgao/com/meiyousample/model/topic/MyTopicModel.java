package nickgao.com.meiyousample.model.topic;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class MyTopicModel {

    public static final int TIPS = 1;
    public static final int TOPIC = 2;
    public static final int DYNAMIC = 3;
    public static final int NEWS = 4;
    public int type;
    public String published_date;
    public int topic_id;
    public String title;
    public String forum_name;
    public int total_review;
    public String uri;
    public List<String> images;
    public boolean isSelected;
    public boolean is_feeds;
    public boolean is_video;
    public boolean is_elite;
    public boolean is_recommended;
    public int image_count;
    public String video_time;
    public int news_type;
    public int author_type;
}
