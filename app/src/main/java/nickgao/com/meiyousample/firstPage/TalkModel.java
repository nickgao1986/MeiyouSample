package nickgao.com.meiyousample.firstPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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


    public String sd_size;//视频大小

    public int has_data;//卡片更新的时候 1-有数据  0-无数据

    public int review_count;//回复数
}