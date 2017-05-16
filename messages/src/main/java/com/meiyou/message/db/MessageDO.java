package com.meiyou.message.db;


import com.meiyou.message.model.SocketDataKey;
import com.meiyou.sdk.common.database.BaseDO;

import org.json.JSONObject;

import nickgao.com.framework.utils.Base64Str;
import nickgao.com.framework.utils.StringUtils;

/**
 * 存储服务端发送过来的原始数据
 * Author: lwh
 * Date: 1/4/17 15:16.
 */

public class MessageDO extends BaseDO {
    /**
     *  {
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
     "is_float":true
     },
     "type": 0,
     "icon": "http://sc.seeyouyima.com/xxy_400.png"
     }
     */
    //服务端入库数据
    private String name;
    private int pushType;
    private int leapType;
    private String sn;
    private String originalData;//原始数据,BASE64那串;
    private int type;
    private String icon;
    private int updates;
    private String messageId;//小米推送的messageid

    //本地聊天参数
    private boolean isMine;//是不是我的
    private boolean isSend;//是发送的还是接收的
    private int isPublicChat;//是否公众号聊天0:不是,1:是
    public MessageDO(){

    }
    /**
     * 兼容数据迁移
     * @param originalData
     */
    public MessageDO(String originalData){
        try {
            setOriginalData(originalData);
            //解析并填充
            String jsonString = new String(Base64Str.decode(originalData));
            JSONObject jsonObject = new JSONObject(jsonString);

            setPushType(jsonObject.optInt(SocketDataKey.PUSH_TYPE));
            setLeapType(jsonObject.optInt(SocketDataKey.LEAP_TYPE));
            setSn(jsonObject.optString(SocketDataKey.SN));
            setType(jsonObject.optInt(SocketDataKey.TYPE));
            setIcon(jsonObject.optString(SocketDataKey.ICON));
            setUpdates(jsonObject.optInt(SocketDataKey.UPDATES));
            JSONObject jsonObjectMessage = jsonObject.optJSONObject("message");
            if(jsonObjectMessage!=null){
                JSONObject jsonObjectPublisher = jsonObjectMessage.optJSONObject("publisher");
                if(jsonObjectPublisher!=null){
                    int userid =  jsonObjectPublisher.optInt("id");
                    setUserId(Long.valueOf(userid));
                }
            }
            String data = jsonObject.optString("data");
            if(!StringUtils.isNull(data)){
                JSONObject jsonData = new JSONObject(data);
                if(jsonData!=null){
                    int user_type = jsonData.optInt("user_type");
                    setPublicChat(user_type);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public MessageDO(MessageDO messageDO){
        setColumnId(messageDO.getColumnId());
        setUserId(messageDO.getUserId());
        setName(messageDO.getName());
        setPushType(messageDO.getPushType());
        setLeapType(messageDO.getLeapType());
        setSn(messageDO.getSn());
        setOriginalData(messageDO.getOriginalData());
        setType(messageDO.getType());
        setIcon(messageDO.getIcon());
        setUpdates(messageDO.getUpdates());
        setMine(messageDO.isMine());
        setSend(messageDO.isSend());
        setPublicChat(messageDO.isPublicChat());
        setMessageId(messageDO.getMessageId());
    }



    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public int getLeapType() {
        return leapType;
    }

    public void setLeapType(int leapType) {
        this.leapType = leapType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int isPublicChat() {
        return isPublicChat;
    }

    public void setPublicChat(int publicChat) {
        isPublicChat = publicChat;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append("name:").append(name).append("\n")
                .append("push_type:").append(pushType).append("\n")
                .append("leap_type:").append(leapType).append("\n")
                .append("sn:").append(sn).append("\n")
                .append("type:").append(type).append("\n")
                .append("icon:").append(icon).append("\n")
                .append("updates:").append(updates).append("\n")
                .append("originalData:").append(originalData).append("\n");
        return stringBuilder.toString();
    }
}
