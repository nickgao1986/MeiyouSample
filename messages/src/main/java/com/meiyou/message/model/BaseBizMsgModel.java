package com.meiyou.message.model;


import org.json.JSONObject;

import nickgao.com.framework.utils.StringUtils;

/**
 * Created by hxd on 15/7/24.
 */
public class BaseBizMsgModel extends BasePushModel {
    /**
     *
     //柚麻通知
     * {
     "name": "柚妈",
     "push_type": 1,
     "leap_type": 2,
     "sn": "03150233T0m7UQJ",
     "message": {
     "updated_date": "2017-01-03 07:02:32",
     "uri_type": 1,
     "title": "",
     "msg_title": "",
     "content": "",
     "topic_id": 33497475,
     "url_title": "查看详情",
     "push_title": "武汉开游艇上天了"
     },
     "type": 0,
     "icon": "http://sc.seeyouyima.com/xxy_400.png"
     }

     跳转到帖子详情；
     {
     "name": "柚妈",
     "push_type": 1,
     "leap_type": 2,
     "sn": "03150233T0m7UQJ",
     "message": {
     "updated_date": "2017-01-03 07:02:32",
     "uri_type": 1,
     "title": "",
     "msg_title": "",
     "content": "",
     "topic_id": 33497475,
     "url_title": "查看详情",
     "push_title": "武汉开游艇上天了"
     },
     "type": 0,
     "icon": "http://sc.seeyouyima.com/xxy_400.png"
     }
     */


    /**
     * 她他圈话题回复
     * {
     "type": 1,
     "push_type": 48,
     "leap_type": 1,
     "multiPush": 1,
     "message": {
     "type": 1,
     "topic_id": 33497540,
     "review_id": 960297018,
     "forum_id": 94,
     "title": "先审后发自动化测试——转载自美柚linwuhan",
     "push_title": "不要动我的账号啊回复了你的话题",
     "content": "不会斤斤计较",
     "updated_date": "2017-01-05 02:05:55",
     "publisher": {
     "id": 82412998,
     "screen_name": "不要动我的账号啊",
     "avatar": "http://sc.seeyouyima.com/avatar_82412998?imageView/1/w/120/h/120/q/100/"
     },
     "top_review_avatar": "http://sc.seeyouyima.com/avatar_82412998?imageView/1/w/120/h/120/q/100/",
     "referenced_content": "先审后发自动化测试——转载自美柚linwuhan",
     "uri": "meiyou:///circles/group/topic?params=eyJ0b3BpY0lEIjozMzQ5NzU0MCwicmVmZXJlbmNlZElkIjotMSwiZ290b0lEIjo5NjAyOTcwMTh9"
     },
     "sn": "05100555058baez"
     }

     柚子街推送消息
     {
     "leap_type": 3,
     "message": {
     "content": "柚子街消息推送",
     "updated_date": "2017-01-05 05:44:32",
     "publisher": {
     "id": 182320,
     "avatar": "http://img.seeyouyima.com/push/youzijie_logo.png",
     "screen_name": ""
     },
     "title": "",
     "items": [
     {
     "uri": "meetyou.linggan://authority/sale/home",
     "id": "088ffa84-d30a-11e6-802c-f01fafe6b8d5",
     "icon": "http://sc.seeyouyima.com/forum/data/58647ecccccbe_540_958.png",
     "title": "柚子街消息推送"
     }
     ]
     },
     "type": 30,
     "sn": "05134432vWCJ3sF"
     }



     * 密友圈关注
     * <p/>
     * "type": 27,
     * "relation": {
     * "update_time": 1405938941,
     * "title": "添加关注通知",
     * "content": "关注了您",
     * "fans": {
     * "update_time": 1405938941,
     * "user_id": 10867595,
     * "screen_name": "阿斯兰萨拉",
     * "avatar": "http://sc.seeyouyima.com/avatar_10867595?imageView/1/w/60/h/60/q/100/1405933357",
     * "source": "source",
     * "fansnum": 1,
     * "dynamicnum": 0
     * }
     * }
     *
     *
     /**
     * socket推送消息
     * {
     * "to": "822148",
     * "id": "92",
     * "data": "{\"type\":303,\"push_type\":3000,\"leap_type\":1,\"message\":{\"title\":\"\\u7981\\u8a00\\u901a\\u77e5\",\"push_title\":\"\\u7981\\u8a00\\u901a\\u77e5\",\"content\":\"\\u4eb2\\u7231\\u7684\\u201c\\u4f60\\u4f60\\u4f60\\u4f60\\u4f601\\u201d\\uff1a\\n\\u56e0\\u6d89\\u53ca\\u201c\\u4ea7\\u54c1\\u5ba3\\u4f20\\u3001\\u7f51\\u8d2d\\u517c\\u804c\\u3001\\u4ee3\\u7406\\u62db\\u8058\\u7b49\\u7591\\u4f3c\\u5e7f\\u544a\\u5185\\u5bb9\\u201d\\uff0c\\u60a8\\u7684\\u8d26\\u53f7\\u5df2\\u88ab\\u7ba1\\u7406\\u5458\\u7981\\u8a001\\u5929,\\u5c06\\u4e8e\\u201c2015-08-14 11:54:12\\u201d\\u6062\\u590d\\u6b63\\u5e38\\u3002\",\"updated_date\":\"2015-08-13 08:01:49\",\"publisher\":{\"id\":822148,\"screen_name\":\"\\u4f60\\u4f60\\u4f60\\u4f60\\u4f601\",\"avatar\":\"http:\\/\\/sc.seeyouyima.com\\/tata\\/icon\\/xxy_400.png\"},\"uri_type\":3004,\"url_title\":\"\\u6211\\u8981\\u7533\\u8bc9\",\"url\":\"http:\\/\\/circle.seeyouyima.com\\/user\\/complaint-index\\/?id=497791649&type=3&topic_id=0&review_id=0&user_id=822148\"},\"data_type\":\"\"}",
     * "sn": "1108:846:0:1428903721792:NDM0RjlCMjItRjExRi03MTI0LTdGNUItRTJCQTM0QjJEQzdF",
     * "type": 1,
     * "status": 0
     * }
     */
    /**
     * socket推送离线消息
     * MSG_TYPE_OFFLINE_PUSH_RESP
     * {
     * JKEY_MSG_SN
     * JKEY_MSG_ID
     * JKEY_MSG_DATA
     * }
     * <p/>
     * {"msg_array":[{"msg_data":"{\"type\":\"3\",\"forum_id\":\"26\",\"id\":\"7790450\",\"url\":\"\",\"title\":\"话题推荐上首页，柚币轻松拿！柚妈教你赚柚币，一次10柚币，每天上限120柚币，快到碗里来~\",\"text\":\"\",\"data_type\": 2}","msg_id":"139"}],
     * "msg_offline_leftsize":1,"msg_sn":"-1566028596","msg_status":0,"msg_type":2}

     */

    //通用字段
    public String msg_sn;
    public String msg_id;
    public String msg_to;
    public int status;
    public String icon;
    public String data;
    //type	消息类型（柚妈、小柚子、点赞）	否
    public int type;
    // 1、2消息回复，3柚妈消息
    public int push_type;
    //1 通知栏 + 消息列表, 2 通知栏  3 消息列表
    public int leap_type;
    //暂时没用
    public String data_type;

    public boolean isClick;
    /**
     * type	消息类型（柚妈、小柚子、点赞）	否
     push_type	消息跳转类型	否
     leap_type	消息类型（应用内还是通知栏）
     */

    public BaseBizMsgModel(String jsonString, String jsonStringBase64) {
        super( jsonString,jsonStringBase64);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            //通用数据
            msg_sn = jsonObject.optString(SocketDataKey.SN);
            msg_id =  StringUtils.getJsonInt(jsonObject, SocketDataKey.ID)+"";//jsonObject.optString(SocketDataKey.ID);
            msg_to= jsonObject.optString(SocketDataKey.TO);
            status = jsonObject.optInt(SocketDataKey.STATUS);
            icon = jsonObject.optString(SocketDataKey.ICON);
            type = jsonObject.optInt(SocketDataKey.TYPE);
            push_type = jsonObject.optInt(SocketDataKey.PUSH_TYPE);
            leap_type = jsonObject.optInt(SocketDataKey.LEAP_TYPE);
            data_type = jsonObject.optString(SocketDataKey.DATA_TYPE);
            //业务数据
            String str = jsonObject.optString(SocketDataKey.DATA);
            this.data = str;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }

    public int getPushType() {
        return push_type;
    }

    public int getLeapType() {
        return leap_type;
    }

    public String getDataType() {
        return data_type;
    }

    public String getMsgSn() {
        return msg_sn;
    }

    public String getMsgId() {
        return msg_id;
    }

    public String getMsgTo() {
        return msg_to;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getIcon() {
        return icon;
    }

    public void setMsg_sn(String msg_sn) {
        this.msg_sn = msg_sn;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public void setMsg_to(String msg_to) {
        this.msg_to = msg_to;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPush_type(int push_type) {
        this.push_type = push_type;
    }

    public void setLeap_type(int leap_type) {
        this.leap_type = leap_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
