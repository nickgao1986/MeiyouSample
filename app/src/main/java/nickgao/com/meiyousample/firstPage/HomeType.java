package nickgao.com.meiyousample.firstPage;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class HomeType {

    //首页数据类型
    public static final int TYPE_TALK = 1;                             // 普通推荐话题
    public static final int TYPE_YOU_ASK = 2;                          // 柚打听的数据
    public static final int TYPE_WEATHER = 5;                          // 天气
    public static final int TYPE_NEWS_TOPIC = 6;                       // 资讯贴
    public static final int TYPE_SPECIAL_TOPIC = 7;                    // 专题帖子
    public static final int TYPE_YIMA_COME = 8;                        // 经期卡片 大姨妈来了
    public static final int TYPE_YIMA_GO = 9;                          // 经期卡片 大姨妈走了
    public static final int TYPE_AGE_CARD = 11;                        // 年龄卡片
    public static final int TYPE_NEWS_TOPIC_CARD = 12;                 // 资讯 \ 话题 专题卡片
    public static final int TYPE_TIPS_PREGNANCY = 13;                  // 今日密保 \ 怀孕几率卡片
    public static final int TYPE_SEPARATOR_BAR = 100;                  // 看到哪的分隔栏类型

    //风格样式
    public static final int HOME_STYLE_ONE = 1;                        // 经期样式feeds流样式
    public static final int HOME_STYLE_TWO = 2;                        // 孕妈圈的feeds流样式
    public static final int HOME_STYLE_TWO_NAVIGATION_BAR = 3;         // 孕妈圈的feeds流样式＋导航栏

    public static final int HOME_VIDEO_NO_PLAY = 0;                    // 视频不可以播放
    public static final int HOME_VIDEO_PLAY = 1;                       // 视频可以播放

    public static final int HOME_VIDEO_NO_COLLECT = 0;                 // 视频没有收藏
    public static final int HOME_VIDEO_COLLECT = 1;                    // 视频收藏

    public static final int HOME_BOTTOM_NO_SHOW_ICON = 0;              // 首页底部icon不显示
    public static final int HOME_SHOW_ICON = 1;                        // 首页底部icon显示

    public static final int CITY_ID = 2;                               // 本地tab的id固定死的不能变
    public static final int RECOMMEND_ID = 1;                          // 分类推荐tab id
    public static final int VIDEO_ID = 4;                              // 分类视频tab id

    public static final int NO_LIKE = 0;                               // 不感兴趣的回调类型

    //专题卡片细分类型
    public static final int HOME_SPECIAL_SMALL_IMG = 2;                // 专题卡片小图类型
    public static final int HOME_SPECIAL_BIG_IMG = 3;                  // 专题卡片大图类型
    public static final int HOME_SPECIAL_BLEND = 4;                    // 专题卡片混合类型

    //首页有几种布局
    public static final int HOME_LAYOUT_TYPE_NORMAL = 0;               // 默认通用布局
    public static final int HOME_LAYOUT_TYPE_VIDEO = 1;                // 纯视频布局
    public static final int HOME_LAYOUT_TYPE_YIMA = 2;                 // 姨妈卡片布局
    public static final int HOME_LAYOUT_TYPE_SEPARATORBAR = 3;         // 分隔栏布局
    public static final int HOME_LAYOUT_TYPE_AGE_CARD = 4;             // 年龄卡片
    public static final int HOME_LAYOUT_TYPE_NEWS_TOPIC_CARD = 5;      // 资讯 \ 话题 专题卡片
    public static final int HOME_LAYOUT_TYPE_TIPS_PREGNANCY = 6;       // 今日密保 \ 怀孕几率卡片
    public static final int TYPE_COUNT = HOME_LAYOUT_TYPE_TIPS_PREGNANCY + 1;//7种布局

    //首页双击底下tab是卡住刷新还是回原位刷新
    public static final int MEIYOU_TAB_STUCK_UPDATE = 1;               // 卡住刷新
    public static final int MEIYOU_TAB_IN_SITU_UPDATE = 2;             // 回原位刷新

    //首页分类按钮显示还是不显示
    public static final int MEIYOU_MORE_TAB_NO_SHOW = 0;               // 不显示
    public static final int MEIYOU_MORE_TAB_SHOW = 1;                  // 显示

    //改变记录数据要刷新的卡片id值
    public static final int TYPE_CARD_PREGNANCY = 2;                   //怀孕几率卡片
    public static final int TYPE_CARD_YIMA = 3;                        //经期卡片
    public static final int TYPE_CARD_TODAY_TIPS = 4;                  //今日密报卡片


}
