package nickgao.com.meiyousample.friend;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.adapter.DynamicImageApdater;

/**
 * 所有跟好友有关的model
 * Created by Administrator on 14-7-8.
 */
public class AddFriendModel implements Serializable {
    //公用的属性
    public static final long serialVersionUID = 1210006121251515212L;
    public int fuid = 0;//好友的id
    public String name;//好友名字
    public String platform;//平台  1 QQ 2 微博 3 邮箱注册用户 4 手机注册
    public int pla;//平台数字
    public String img_url_medium = "";//中等头像
    public String img_url_large = "";//大头像
    public String img_url_small = "";//小头像
    public boolean isFollow = false;//是否关注
    public int followStatus = 0;//关注情况

    //关注和粉丝的属性
    public int dynamicnum = 0;//动态数量
    public int fans = 0;//粉丝数量
    public transient DynamicImageApdater dynamicImageApdater;
    //附近好友的属性
    public String content = "";//内容
    //图片集合
    public List<String> paths = new ArrayList<String>();
    public int score = 0;
    public boolean bTextExpand = false;
    public String reason = "推荐";//理由
    public int isVip = 0;//是否认证过 用户 0 表示未认证 1表示认证

    //粉丝和关注 分页的page
    public int page = 0;

    //朋友页面 需要的类型 -2：没有数据 -1: 绑定QQ或微博好友信息  0：为正常推荐的好友  1：为微博邀请好友 2: 微博邀邀请信息
    public int type;

    /**
     * 用户类型
     */
    public int user_type;

    /**
     * 推荐好友和附近好友解析
     *
     * @param obj
     */
    public AddFriendModel(JSONObject obj, double dis) {
        this.fuid = StringUtils.getJsonInt(obj, "id");
        this.name = StringUtils.getJsonString(obj, "screen_name");
        this.score = StringUtils.getJsonInt(obj, "score");
        if (obj.has("content")) {
            try {
                JSONObject object_content = obj.getJSONObject("content");
                this.content = StringUtils.getJsonString(object_content, "content");
                if (object_content.has("images")) {
                    JSONArray array = object_content.getJSONArray("images");
                    for (int i = 0; i < array.length(); i++) {
                        String pic = array.get(i).toString();
                        paths.add(pic);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.img_url_small = StringUtils.getJsonString(obj, "avatar");
        this.reason = StringUtils.getJsonString(obj, "reason");
        this.dynamicnum = StringUtils.getJsonInt(obj, "dynamicnum");
        this.fans = StringUtils.getJsonInt(obj, "fans");
        this.user_type = StringUtils.getJsonInt(obj, "user_type");
    }

    /**
     * 黑名单解析
     *
     * @param obj
     */
    public AddFriendModel(JSONObject obj, int type) {
        this.fuid = StringUtils.getJsonInt(obj, "id");
        this.name = StringUtils.getJsonString(obj, "screen_name");
        this.pla = StringUtils.getJsonInt(obj, "platform");
        if (pla == 1) {
            this.platform = "QQ好友";
        } else if (pla == 2) {
            this.platform = "微博好友";
        } else if (pla == 3) {
            this.platform = "美柚好友";
        } else if (pla == 4) {
            this.platform = "通讯录好友";
        }
        if (obj.has("avatar")) {//头像
            try {
                JSONObject obj_ava = obj.getJSONObject("avatar");
                this.img_url_large = StringUtils.getJsonString(obj_ava, "large");
                this.img_url_medium = StringUtils.getJsonString(obj_ava, "medium");
                this.img_url_small = StringUtils.getJsonString(obj_ava, "small");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.dynamicnum = StringUtils.getJsonInt(obj, "dynamicnum");//动态            hai
        this.fans = StringUtils.getJsonInt(obj, "fans");//粉丝
        followStatus = StringUtils.getJsonInt(obj, "isfollow");//是否我已经关注了 0 表示没有关注，1表示已经关注 2 表示关注等待验证 3 表示黑名单,4 表示相互关注

        if (followStatus == AddFriendController.NOATTENTION || followStatus == AddFriendController.ATTENTION_ME) {
            this.isFollow = false;
        } else if (followStatus == AddFriendController.ATTENTION || followStatus == AddFriendController.BACKLIST || followStatus ==
                AddFriendController.ATTENTION_MUTUAL) {
            this.isFollow = true;
        }
        this.isVip = StringUtils.getJsonInt(obj, "isvip");
        this.user_type = StringUtils.getJsonInt(obj, "user_type");
    }

    /**
     * 关注和粉丝解析 ，
     * 添加关注界面分析
     *
     * @param obj
     */
    public AddFriendModel(JSONObject obj, String type, int page) {
        this.page = page;
        this.fuid = StringUtils.getJsonInt(obj, "id");
        this.name = StringUtils.getJsonString(obj, "screen_name");
        this.pla = StringUtils.getJsonInt(obj, "platform");
        if (pla == 1) {
            this.platform = "QQ好友";
        } else if (pla == 2) {
            this.platform = "微博好友";
        } else if (pla == 3) {
            this.platform = "美柚好友";
        } else if (pla == 4) {
            this.platform = "通讯录好友";
        }
        this.img_url_small = StringUtils.getJsonString(obj, "avatars");
        this.dynamicnum = StringUtils.getJsonInt(obj, "dynamicnum");//动态            hai
        this.fans = StringUtils.getJsonInt(obj, "fans");//粉丝
        followStatus = StringUtils.getJsonInt(obj, "isfollow");//是否我已经关注了 0 表示没有关注，1表示已经关注 2 表示关注等待验证 3 表示黑名单,4 表示相互关注
        if (followStatus == AddFriendController.NOATTENTION || followStatus == AddFriendController.ATTENTION_ME) {
            this.isFollow = false;
        } else if (followStatus == AddFriendController.ATTENTION || followStatus == AddFriendController.BACKLIST || followStatus ==
                AddFriendController.ATTENTION_MUTUAL) {
            this.isFollow = true;
        }
        this.reason = StringUtils.getJsonString(obj, "reason");//推荐理由
        this.isVip = StringUtils.getJsonInt(obj, "isvip");
        this.user_type = StringUtils.getJsonInt(obj, "user_type");
    }

    public AddFriendModel() {

    }
}