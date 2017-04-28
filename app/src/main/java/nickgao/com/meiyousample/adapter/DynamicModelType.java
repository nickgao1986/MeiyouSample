package nickgao.com.meiyousample.adapter;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public interface DynamicModelType {
    //点击进动态详情:
    int SHUOSHUO = 1;
    int TOPIC_SHARE = 2;
    int SHARE_NEWS = 13;
    int SHARE_PERSONAL_PAGE = 14;
    int TOOL = 11;
    int BIG_PIC = 12;


    //点击  两种形式:1.美柚号点击上面 ，进入帖子，然后点击comment进入帖子弹出键盘。。13以上的有下发协议
    int PUBLICK_TOPIC = 17;
    int REPLY_TOPIC = 15;
    int REPLY_NEWS = 16;
    int PUBLIC_NEWS = 18;

    int LOCAL_DATA = -1;
    //int TIPS_SHARE = 9;


    //下面是回复的类型   int REPLY_TOPIC = 15; int REPLY_NEWS = 16;
    //下面是分享的类型  1,2,13,12,11,14 有进动态详情
    // 17,18跳转协议

    //15,16,17,18全部跳协议

    //点整个item的  需要跳原文的 发布类型的  17,18
    //点框里面的全部跳原文
}
