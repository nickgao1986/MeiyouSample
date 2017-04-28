package nickgao.com.meiyousample.model.reply;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class TopicModel {

//        "topic_id": 3851870,
//                "type": 4,
//                "title": "白百何出轨",
//                "images": [],
//                "published_date": "2017-04-19 04:33:09",
//                "total_review": 8,
//                "video_time": "",
//                "image_count": 0,
//                "forum_name": "美丽圣经",
//                "uri": "meiyou:///news/comment/detail?params=eyJyZXZpZXdJZCI6MjIwNDc0NSwiZ290b0lkIjo2NTU3OTV9"
    public int topic_id;
    public int type;
    public String title;
    public String[] images;
    public int total_review;
    public int image_count;
    public String forum_name;
    public String uri;
    public String published_date;
}
