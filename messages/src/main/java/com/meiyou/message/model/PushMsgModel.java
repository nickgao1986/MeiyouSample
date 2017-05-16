package com.meiyou.message.model;


import org.json.JSONObject;

import nickgao.com.framework.utils.StringUtils;

/**
 *
 */
public class PushMsgModel extends BaseBizMsgModel {

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

    public PushMsgModel(String jsonString, String jsonStringBase64) {
        super(jsonString,jsonStringBase64);
    }

    public JSONObject getDataObject() {
        try {
            if(StringUtils.isNotEmpty(data)){
                return new JSONObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
