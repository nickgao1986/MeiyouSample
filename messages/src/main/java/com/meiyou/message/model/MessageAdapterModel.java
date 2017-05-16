package com.meiyou.message.model;

import com.meiyou.message.db.MessageDO;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

/**
 * 适配器uimodel;
 * model的逻辑是:
 * MessageDO:存储服务端推送过来的原始数据
 * MessageModel:存储解析完原始数据的具体信息
 * MessageAdapterModel:在MessageModel的基础上,增加一些UI元素,比如是否动画,是否全选之类的
 * Author: lwh
 * Date: 1/5/17 08:57.
 */

public class MessageAdapterModel extends MessageModel implements Selectable {

    private static final String TAG = "MessageAdapterModel";
    private List<String> mMultiAvatar;//多头像
    private boolean isSelect;

    //被聊天model征用的字段
    private String mChatTitle;
    private String mChatContent;
    private String mChatAvatar;
    private String mSessionId;
    private String mChatMsgFrom;
    private String mChatMsgTo;
    private String mFriendId;
    private int mChatMediaType;
    private int mChatStatus;

    //是否是新消息,用于区分,话题是跳转叠楼还是跳转话题详情
    private boolean isMesssageNew;
    /*
    private String forum_name;
    private String related_content;
    private String msg1,msg2;
        private int total_updates;//旧版本消息数量,聊天里代表状态
    */

    public MessageAdapterModel() {
        super();
    }

    public MessageAdapterModel(MessageDO messageDO) {
        super(messageDO);

    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }




    /**
     * 获取多头像
     *
     * @return
     */
    public List<String> getMultiAvatar() {
        if (mMultiAvatar == null) {
            mMultiAvatar = new ArrayList<>();
            if (getTop_review_avatar() != null) {
                String[] array = getTop_review_avatar().split(",");
                if (array != null && array.length > 0) {
                    for (String avatar : array) {
                        if (!StringUtils.isNull(avatar)) {
                            mMultiAvatar.add(avatar);
                        }
                    }
                }
            }
        }
        return mMultiAvatar;
    }



    private String getSessionId(String msg_from, String msg_to) {
        int from = 0, to = 0;
        if (msg_from != null && msg_from.contains("my_")) {//my_123123
            from = StringUtils.getInt(msg_from.subSequence(3, msg_from.length()).toString());
        } else {
            from = StringUtils.getInt(msg_from);
        }
        if (msg_to != null && msg_to.contains("my_")) {//my_123123
            to = StringUtils.getInt(msg_to.subSequence(3, msg_to.length()).toString());
        } else {
            to = StringUtils.getInt(msg_to);
        }
        if (from > to) {
            return msg_to + "_" + msg_from;
        } else {
            return msg_from + "_" + msg_to;
        }
    }

    public String getChatTitle() {
        return mChatTitle;
    }

    public void setChatTitle(String chatTitle) {
        mChatTitle = chatTitle;
    }

    public String getChatContent() {
        return mChatContent;
    }

    public void setChatContent(String chatContent) {
        mChatContent = chatContent;
    }

    public String getChatAvatar() {
        return mChatAvatar;
    }

    public void setChatAvatar(String chatAvatar) {
        mChatAvatar = chatAvatar;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String sessionId) {
        mSessionId = sessionId;
    }

    public int getChatMediaType() {
        return mChatMediaType;
    }

    public void setChatMediaType(int chatMediaType) {
        mChatMediaType = chatMediaType;
    }


    public int getChatStatus() {
        return mChatStatus;
    }

    public void setChatStatus(int chatStatus) {
        mChatStatus = chatStatus;
    }

    public String getChatMsgFrom() {
        return mChatMsgFrom;
    }

    public void setChatMsgFrom(String chatMsgFrom) {
        mChatMsgFrom = chatMsgFrom;
    }

    public String getChatMsgTo() {
        return mChatMsgTo;
    }

    public void setChatMsgTo(String chatMsgTo) {
        mChatMsgTo = chatMsgTo;
    }

    public String getFriendId() {
        return mFriendId;
    }

    public void setFriendId(String friendId) {
        mFriendId = friendId;
    }

    public boolean isMesssageNew() {
        return isMesssageNew;
    }

    public void setMesssageNew(boolean messsageNew) {
        isMesssageNew = messsageNew;
    }

    @Override
    public boolean getSelected() {
        return isSelect;
    }

    @Override
    public void setSelected(boolean isSelected) {
        isSelect = isSelected;
    }


}
