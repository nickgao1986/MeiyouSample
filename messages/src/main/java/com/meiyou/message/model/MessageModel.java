package com.meiyou.message.model;

import com.meiyou.message.db.MessageDO;

import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.framework.utils.Base64Str;
import nickgao.com.framework.utils.StringUtils;

/**
 * 消息业务model,存储解析数据
 * Author: lwh
 * Date: 1/5/17 08:39.
 */

public class MessageModel implements Serializable {

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


     小柚子消息
     {
     "type": "31",
     "push_type": 16,
     "leap_type": "1",
     "message": {
     "uri_type": 16,
     "title": "\u6d88\u606f\u6807\u9898",
     "push_title": "\u901a\u77e5\u680f\u6807\u9898",
     "content": "\u6d88\u606f\u5185\u5bb9",
     "updated_date": "2017-01-12 09:32:37",
     "publisher": {
     "id": 105025921,
     "screen_name": "\u7528\u6237\u6635\u79f0",
     "avatar": "http:\/\/sc.seeyouyima.com\/icon\/xiaoyouzi_150_150.png"
     }
     },
     "sn": "12173237877540m"
     }


     柚子街消息
     {
     'leap_type': 3,
     'message': {
     'content': '\xe6\x9f\x9a\xe5\xad\x90\xe8\xa1\x97\xe6\xb6\x88\xe6\x81\xaf\xe6\x8e\xa8\xe9\x80\x81',
     'updated_date': '2017-01-1306: 00: 32',
     'publisher': {
     'id': 105039136,
     'avatar': 'http: //img.seeyouyima.com/push/youzijie_logo.png',
     'screen_name': ''
     },
     'items': [
     {
     'uri': 'meetyou.linggan: //authority/sale/home',
     'id': '981ec81e-d955-11e6-a9ee-f01fafe6b8d5',
     'icon': 'http: //sc.seeyouyima.com/forum/data/58647ecccccbe_540_958.png',
     'title': '\xe6\x9f\x9a\xe5\xad\x90\xe8\xa1\x97\xe6\xb6\x88\xe6\x81\xaf\xe6\x8e\xa8\xe9\x80\x81'
     },
     {
     'uri': 'meetyou.linggan: //authority/youbi',
     'id': '981ecc06-d955-11e6-a9ee-f01fafe6b8d5',
     'icon': 'http: //sc.seeyouyima.com/forum/data/5873525946ab7_419_312.jpg',
     'title': '\xe6\x9f\x9a\xe5\xad\x90\xe8\xa1\x97\xe6\xb6\x88\xe6\x81\xaf\xe6\x8e\xa8\xe9\x80\x81'
     },
     {
     'uri': '',
     'id': '981eceae-d955-11e6-a9ee-f01fafe6b8d5',
     'icon': 'http: //sc.seeyouyima.com/forum/data/5873531a4782c_419_312.jpg',
     'title': '\xe6\x9f\x9a\xe5\xad\x90\xe8\xa1\x97\xe6\xb6\x88\xe6\x81\xaf\xe6\x8e\xa8\xe9\x80\x81'
     },
     {
     'uri': '',
     'id': '981ed11a-d955-11e6-a9ee-f01fafe6b8d5',
     'icon': 'http: //sc.seeyouyima.com/forum/data/58735321c68f6_419_312.jpg',
     'title': '\xe6\x9f\x9a\xe5\xad\x90\xe8\xa1\x97\xe6\xb6\x88\xe6\x81\xaf\xe6\x8e\xa8\xe9\x80\x81'
     }
     ]
     },
     'type': 30,
     'sn': '13140032pJTLiAD'
     }

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


     /**
     *  发送包
     * {"msg_to":"895719","msg_data":"{\"content\":\"嗯嗯德国\",\"from_avatar\":\"avatar_895719\",\"image\":\"{\\\"url\\\":\\\"\\\",\\\"height\\\":0,\\\"width\\\":0,\\\"url_thumb\\\":\\\"\\\"}\",\"media_type\":0,\"from_name\":\"嗷嗷君\",\"msg_from\":\"895719\"}","msg_sn":"1415599475237","msg_cmd":3}


     * 接收包
     * * {
     "msg_data": "{\"content\":\"嗯嗯德国\",\"from_avatar\":\"avatar_895719\",\"image\":\"{\\\"url\\\":\\\"\\\",\\\"height\\\":0,\\\"width\\\":0,\\\"url_thumb\\\":\\\"\\\"}\",\"media_type\":0,\"from_name\":\"嗷嗷君\",\"msg_from\":\"895719\"}",
     "msg_from": "my_895719",
     "msg_id": "0",
     "msg_sn": "1415599475237",
     "msg_status": 0,
     "msg_time": "0",
     "msg_to": "895719",
     "msg_type": 11
     }
     * @param
     *  {
    "msg_data": "XXXXXXXXXXXXX",
    "msg_from": "my_9785956",
    "msg_id": "67",
    "msg_time": "1415613548657",
    "msg_to": "iamdps"
    }


    {"msg_sn":"-1566028593","msg_status":-401,"msg_type":11}

     */
    /**
     * 公众号 聊天返回格式
     * 返回格式示例：{
     * "public_service":"",
     * "public_service_data":"
     * {\"id\":5018,\"content\":\"\\u52f\\u8be5\\u7537u5201d\",
     * \"cid\":0,\"service_id\":0}","status":0,"type":63}
     *

     */





    private MessageDO mMessageDO;
    //推送通知
    private String name;
    private String updated_date;
    private int uri_type;
    private String title;
    private String msg_title;
    private String content;
    private int topic_id;
    private String url_title;
    private String push_title;

    //回复
    private int review_id;
    private int forum_id;
    private int publisherId;
    private String pubulisherScreenName;
    private String pushlisherAvatar;
    private String top_review_avatar;
    private String referenced_content;
    private String uri;//落地页用的uri
    private String uri_push;//消息用的uri

    //5.8资讯回复新增字段
    private int sub_review_id;

    //5.5版本新增字段 消息图片
    private String image;

    //柚子街字段;
   // private List<MessageYouzijieItem> listYouzijieItem = new ArrayList<>();

    //密友圈关注字段
    //private MessageItemDynamicFollow mMessageItemDynamicFollow = new MessageItemDynamicFollow();

    //聊天字段
   // private PeerModel mPeerModel;
    //一些特殊的字段
    /**
     * 除了这些字段,其余都取attr_id和attr_text
     * push_type_map_field = {
     #经期
     1: {"topic_id":0}, #跳转话题
     2: {"forum_id":0}, #跳转圈子
     3: {"url":""}, #外部网页
     4: {"url":""}, #内嵌网页
     30: {"user_id":0}, #个人主页、用户主页
     33: {"skin_id":0}, #皮肤列表、皮肤主题
     37: {"keyword":""}, #搜索页面，搜索结果
     46: {"tips_cid":0, "tips_title":""}, #贴士列表
     55: {"catalog_id":0}, #作品列表
     56: {"product_id":0}, #作品详情
     57: {"staff_id":0}, #店员个人页面
     61: {"order_sn":0}, #订单跟踪


     #育儿
     5009:{"topic_id":0}, #跳转话题
     5010:{"forum_id":0}, #跳转圈子
     5001:{"url":""}, #外部网页
     5002:{"url":""}, #内嵌网页
     5004:{"tiemstamp":0}, #照片列表
     5012:{"keyword":""}, #搜索页面，搜索结果
     5021:{"tid":0}, #疫苗详情

     #孕期
     3001:{"topic_id":0},
     3002:{"forum_id":0}, #跳转圈子
     3003:{"url":""}, #外部网页
     3004:{"url":""}, #内嵌网页
     3015:{"keyword":""}, #搜索页面，搜索结果
     3020:{"tips_cid":0, "tips_title":""}, #贴士列表
     }
     */
    private String url;
    private int skin_id;
    private int user_id;
    private String keyword;
    private long tiemstamp;//服务端单词拼错,将错就错
    private int tid;
    private int tips_cid;

    private int attr_id;
    private String attr_text;
    private boolean is_float;



    //自定义本地参数;
    //是否申诉过
    private boolean is_shensu;

    //本地小柚子提醒字段
    private int count;



    public MessageModel( ){

    }

    public MessageModel(MessageDO messageDO){
        mMessageDO = messageDO;
        parseDo();
    }

    public void setMessageDO(MessageDO messageDO) {
        mMessageDO = messageDO;
    }

    public MessageDO getMessageDO() {
        //防止Crash
        if(mMessageDO==null)
            mMessageDO = new MessageDO();
        return mMessageDO;
    }

    private void parseDo(){
        try {
            String json = new String(Base64Str.decode(mMessageDO.getOriginalData()));
            //推送通知
            JSONObject jsonObject = new JSONObject(json);

            name = jsonObject.optString("name");
            //密友圈关注
//            JSONObject jsonDynamicFollow = jsonObject.optJSONObject("relation");
//            if(jsonDynamicFollow!=null){
//                mMessageItemDynamicFollow = new MessageItemDynamicFollow(jsonDynamicFollow);
//            }
            //socket chat
//            String jsonData = jsonObject.optString("data");
//            if(!StringUtils.isNull(jsonData)){
//                mPeerModel = new PeerModel(json,mMessageDO.getOriginalData());
//            }
            //mi push,socket push
            JSONObject jsonObjectMessage = jsonObject.optJSONObject("message");
            if(jsonObjectMessage!=null){
                uri_type = jsonObjectMessage.optInt("uri_type");
                updated_date = jsonObjectMessage.optString("updated_date");
                title = jsonObjectMessage.optString("title");
                msg_title = jsonObjectMessage.optString("msg_title");
                content = jsonObjectMessage.optString("content");
                topic_id = jsonObjectMessage.optInt("topic_id");
                url_title = jsonObjectMessage.optString("url_title");
                push_title = jsonObjectMessage.optString("push_title");


                //回复
                review_id =  jsonObjectMessage.optInt("review_id");
                forum_id =  jsonObjectMessage.optInt("forum_id");
                JSONObject jsonObjectPublisher = jsonObjectMessage.optJSONObject("publisher");
                if(jsonObjectPublisher!=null){
                    publisherId =  jsonObjectPublisher.optInt("id");
                    pubulisherScreenName =  jsonObjectPublisher.optString("screen_name");
                    pushlisherAvatar =  jsonObjectPublisher.optString("avatar");
                }
                //柚子街
//                JSONArray jsonArrayItems = jsonObjectMessage.optJSONArray("items");
//                if(jsonArrayItems!=null){
//                    for(int i=0;i<jsonArrayItems.length();i++){
//                        JSONObject jsonObject1 = jsonArrayItems.getJSONObject(i);
//                        MessageYouzijieItem item = new MessageYouzijieItem(jsonObject1);
//                        listYouzijieItem.add(item);
//                    }
//                }
                top_review_avatar =  jsonObjectMessage.optString("top_review_avatar");
                referenced_content =  jsonObjectMessage.optString("referenced_content");
                uri =  jsonObjectMessage.optString("uri");
                uri_push=  jsonObjectMessage.optString("uri_push");
                //
                sub_review_id= jsonObjectMessage.optInt("sub_review_id");

                image = jsonObjectMessage.optString("image");//StringUtils.getJsonString(jsonObject, "image");

                //特殊类型
                url = StringUtils.getJsonString(jsonObjectMessage, "url");
                skin_id = StringUtils.getJsonInt(jsonObjectMessage, "skin_id");
                user_id = StringUtils.getJsonInt(jsonObjectMessage, "user_id");
                keyword = StringUtils.getJsonString(jsonObjectMessage, "keyword");
                tiemstamp =  jsonObjectMessage.optLong("tiemstamp");
                tid = StringUtils.getJsonInt(jsonObjectMessage, "tid");
                tips_cid = StringUtils.getJsonInt(jsonObjectMessage, "tips_cid");

                //除了以上特殊类型参数,其余的都是attr_id和attr_text
                attr_id = StringUtils.getJsonInt(jsonObjectMessage, "attr_id");
                attr_text = StringUtils.getJsonString(jsonObjectMessage, "attr_text");
                is_float = StringUtils.getJsonBoolean(jsonObjectMessage, "is_float");
                is_shensu = jsonObjectMessage.optBoolean("is_shensu");
                count = jsonObjectMessage.optInt("count");

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public int getSkin_id() {
        return skin_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public long getTiemstamp() {
        return tiemstamp;
    }

    public int getTid() {
        return tid;
    }

    public int getTips_cid() {
        return tips_cid;
    }

    public String getName() {
        return name;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public int getUri_type() {
        /*if(uri_type<=0 && mMessageDO!=null){
            return mMessageDO.getPushType();
        }*/
        return uri_type;
    }

    public String getUri_push() {
        return uri_push;
    }

    public void setUri_push(String uri_push) {
        this.uri_push = uri_push;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public String getContent() {
        return content;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public String getUrl_title() {
        return url_title;
    }

    public String getPush_title() {
        return push_title;
    }

    public int getReview_id() {
        return review_id;
    }

    public int getForum_id() {
        return forum_id;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPubulisherScreenName() {
        return pubulisherScreenName;
    }

    public String getPushlisherAvatar() {
        return pushlisherAvatar;
    }

    public String getTop_review_avatar() {
        return top_review_avatar;
    }

    public String getReferenced_content() {
        return referenced_content;
    }

    public String getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public void setUri_type(int uri_type) {
        this.uri_type = uri_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public void setUrl_title(String url_title) {
        this.url_title = url_title;
    }

    public void setPush_title(String push_title) {
        this.push_title = push_title;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public void setForum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setPubulisherScreenName(String pubulisherScreenName) {
        this.pubulisherScreenName = pubulisherScreenName;
    }

    public void setPushlisherAvatar(String pushlisherAvatar) {
        this.pushlisherAvatar = pushlisherAvatar;
    }

    public void setTop_review_avatar(String top_review_avatar) {
        this.top_review_avatar = top_review_avatar;
    }

    public void setReferenced_content(String referenced_content) {
        this.referenced_content = referenced_content;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setSub_review_id(int sub_review_id) {
        this.sub_review_id = sub_review_id;
    }

    public int getSub_review_id() {
        return sub_review_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }

    public String getAttr_text() {
        return attr_text;
    }

    public void setAttr_text(String attr_text) {
        this.attr_text = attr_text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean is_shensu() {
        return is_shensu;
    }

    public void setIs_shensu(boolean is_shensu) {
        this.is_shensu = is_shensu;
    }

    public boolean is_float() {
        return is_float;
    }

    public void setIs_float(boolean is_float) {
        this.is_float = is_float;
    }

//    public List<MessageYouzijieItem> getListYouzijieItem() {
//        return listYouzijieItem;
//    }
//
//    public void setListYouzijieItem(List<MessageYouzijieItem> listYouzijieItem) {
//        this.listYouzijieItem = listYouzijieItem;
//    }
//
//    public MessageItemDynamicFollow getMessageItemDynamicFollow() {
//        return mMessageItemDynamicFollow;
//    }
//
//    public void setMessageItemDynamicFollow(MessageItemDynamicFollow messageItemDynamicFollow) {
//        mMessageItemDynamicFollow = messageItemDynamicFollow;
//    }
//
//    public PeerModel getPeerModel() {
//        return mPeerModel;
//    }
//
//    public void setPeerModel(PeerModel peerModel) {
//        mPeerModel = peerModel;
//    }
}
