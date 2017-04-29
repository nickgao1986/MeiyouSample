package nickgao.com.meiyousample.firstPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.firstPage.module.PublisherModel;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public class TalkModel implements Serializable {
    private static final long serialVersionUID = 489545631L;

    //推广数据表和话题表里面的自增id
    public int id;

    // 圈子id、发布话题ID
    public int forum_id;

    //话题id
    public int topic_id;

    //用户id
    public int user_id;

    //内容
    public String content;

    //显示的图片列表
    public List<String> images = new ArrayList<>();

    //显示的图片列表
    public List<String> images_night = new ArrayList<>();

    //标题
    public String title;

    //发布者
    public PublisherModel publisher;

    //时间
    public String updated_date;

    //圈子头像
    public String circle_icon;

    //圈子名称
    public String circle_name;

    //推广类型 	1普通推荐话题，2 推广主题，3 推广圈子，4 推广外链，5 内嵌网页  话题是2，咨询是4
    public int type;
    //1:普通话题  2:推广  3:推荐  4:专享，5:天气  11:淘宝AD  100:淘宝数据
    public int recomm_type = -1;

    public int UM_recomm_type = -1;//专题卡片统计用的 字段

    public int switch_type; // 开关类型，0未不用开关，1为大姨妈来了，2为大姨妈走了

    public int switch_value; // 开关默认值，0为否，1为是

    public int is_fold; // 0 不折叠 1 折叠

    //是否需要固定显示在某一项 0表示不需要固定显示 其它表示固定显示在哪一项
    public Integer ordinal = 0;

    //内链，外连用的地址
    public String url;

    public String preloadingUrl;//预加载需要的url拼接了很多字段参数的

    //缓存用，刷新用的时间
    public int time;

    public int total_review;

    //跳转时制定皮肤的ID
    public int skin_id;
    //搜索结果
    public String keyword;
    //首页跳转他人主页的用户ID和皮肤ID
    public int attr_id;
    //首页跳转搜索需要的搜索字段
    public String attr_text;

    public boolean isRead;//是否已读

    //资讯贴子新增的字断
    //参考NewsType
    public int news_type;//资讯类型：1纯文本, 2图文混排, 3纯图片, 4纯视频, 5图片视频混排,6投票，7图文详情

    public int imgs_count;//图片数量

    public String video_time;//视频时间

//    public List<CloseFeedBackModel> label = new ArrayList<>();//反馈的数据

    public String redirect_url;//5.7新增加的字断 用来协议跳转的 只要这个字段有给值的话，那就走协议跳转否则还是走以前老得跳转

    //可以播放的视频字段
    public int feeds_play;//视频可以不可以在列表播放 0不能 1 可以
    public String nd_url;//普清视频
    public String sd_url;//标清视频
    public String hd_url;//高清视频
    public int view_times;//视频播放次数

    //经期数据卡片字断
    public int timaCat;//大姨妈所处时期(通用卡片没有)
    public int is_favorite;//视频收藏还是没收藏 0没收藏 1收藏

    //美柚号文章
    public int attr_type;//1是美柚号文章 2.专题小图模式  3.专题大图模式 4.专题混合模式 5.品牌号

    //卡片字段用来标识title所用
    public String r_text;
    //专题卡片数据集合
   // public List<SpecialHomeModel> specialHomeModels = new ArrayList<>();

    public String sd_size;//视频大小

    public int has_data;//卡片更新的时候 1-有数据  0-无数据

    public int review_count;//回复数

    public TalkModel() {
    }


    public TalkModel(JSONObject jsonObject) {

        content = StringUtils.getJsonString(jsonObject, "content");
        forum_id = StringUtils.getJsonInt(jsonObject, "forum_id");
        skin_id = StringUtils.getJsonInt(jsonObject, "skin_id");
        keyword = StringUtils.getJsonString(jsonObject, "keyword");
        id = StringUtils.getJsonInt(jsonObject, "id");
        title = StringUtils.getJsonString(jsonObject, "title");
        updated_date = StringUtils.getJsonString(jsonObject, "updated_date");
        circle_icon = StringUtils.getJsonString(jsonObject, "circle_icon");
        circle_name = StringUtils.getJsonString(jsonObject, "circle_name");
        type = StringUtils.getJsonInt(jsonObject, "type");
        recomm_type = StringUtils.getJsonInt(jsonObject, "recomm_type");
        switch_type = StringUtils.getJsonInt(jsonObject, "switch_type");
        switch_value = StringUtils.getJsonInt(jsonObject, "switch_value");
        is_fold = StringUtils.getJsonInt(jsonObject, "is_fold");
        user_id = StringUtils.getJsonInt(jsonObject, "user_id");
        topic_id = StringUtils.getJsonInt(jsonObject, "topic_id");
        total_review = StringUtils.getJsonInt(jsonObject, "total_review");

        attr_id = StringUtils.getJsonInt(jsonObject, "attr_id");
        attr_text = StringUtils.getJsonString(jsonObject, "attr_text");
        attr_type = StringUtils.getJsonInt(jsonObject, "attr_type");

        ordinal = StringUtils.getJsonInt(jsonObject, "ordinal");
        url = StringUtils.getJsonString(jsonObject, "url");
//        if (!StringUtils.isNull(url)) {//拼接预加载的url
//            preloadingUrl = url + WebViewController.getInstance().getWebUrlParams(url, BeanManager.getUtilSaver().getUserIdentify(BeanManager.getUtilSaver().getContext()));
//        }
        r_text = StringUtils.getJsonString(jsonObject, "r_text");
        sd_size = StringUtils.getJsonString(jsonObject, "sd_size");
        has_data = StringUtils.getJsonInt(jsonObject, "has_data");

        JSONObject publisherObj = StringUtils.getJsonObejct(jsonObject, "publisher");
        if (null != publisherObj) {
            publisher = new PublisherModel(publisherObj);
        }

        //5.8新增的字断
        feeds_play = StringUtils.getJsonInt(jsonObject, "feeds_play");
        is_favorite = StringUtils.getJsonInt(jsonObject, "is_favorite");
        view_times = StringUtils.getJsonInt(jsonObject, "view_times");
        if (jsonObject.has("video_url")) {
            JSONObject videoObj = StringUtils.getJsonObejct(jsonObject, "video_url");
            nd_url = StringUtils.getJsonString(videoObj, "nd_url");
            hd_url = StringUtils.getJsonString(videoObj, "hd_url");
            sd_url = StringUtils.getJsonString(videoObj, "sd_url");
        }

        //新增的资讯贴子的字断
        news_type = StringUtils.getJsonInt(jsonObject, "news_type");
        imgs_count = StringUtils.getJsonInt(jsonObject, "imgs_count");
        video_time = StringUtils.getJsonString(jsonObject, "video_time");

        review_count = StringUtils.getJsonInt(jsonObject, "review_count");
        LogUtils.d("=====review_count=" + review_count);
        //协议字段
        redirect_url = StringUtils.getJsonString(jsonObject, "redirect_url");

        try {
//            if (jsonObject.has("items")) {//专题卡片混合的类型数据解析
//                //卡片数据
//                JSONArray itemArray = StringUtils.getJsonArray(jsonObject, "items");
//                if (null != itemArray) {
//                    int len = itemArray.length();
//                    for (int i = 0; i < len; i++) {
//                        JSONObject obj = itemArray.getJSONObject(i);
//                        SpecialHomeModel talkModel = new SpecialHomeModel(obj);
//                        specialHomeModels.add(talkModel);
//                    }
//                }
//            }
//            if (jsonObject.has("label")) {//反馈的数据
//                JSONArray array = StringUtils.getJsonArray(jsonObject, "label");
//                int labelLen = array.length();
//                if (array != null && labelLen > 0) {
//                    for (int i = 0; i < labelLen; i++) {
//                        JSONObject obj = array.getJSONObject(i);
//                        CloseFeedBackModel model = new CloseFeedBackModel(obj);
//                        label.add(model);
//                    }
//                }
//            }

            if (jsonObject.has("images")) {
                JSONArray array = StringUtils.getJsonArray(jsonObject, "images");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        images.add(array.getString(i));
                    }
                }
            }

            if (jsonObject.has("images_night")) {
                JSONArray array = StringUtils.getJsonArray(jsonObject, "images_night");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        images_night.add(array.getString(i));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}