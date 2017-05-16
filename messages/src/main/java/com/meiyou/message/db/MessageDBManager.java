package com.meiyou.message.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class MessageDBManager {

    private Context mContext;
    private MessageDAO mMessageDAO;

    public MessageDBManager(Context context) {
        mContext = context;
        mMessageDAO = new MessageDAO();
    }

    public List<MessageDO> getMessageList(long userid) {
        return mMessageDAO.getMessageList(userid);
    }

    public List<MessageDO> getMessageListByType(long userid, int type) {
        List<MessageDO> list = mMessageDAO.getMessageListByType(userid, type);
        return list;
    }

    //获取某种类型的未读列表
    public List<MessageDO> getMessageListByTypeUnread(long userid, int type){
        List<MessageDO> listResult = new ArrayList<>();
        List<MessageDO> list = getMessageListByType(userid,type);
        if(list!=null){
            for(MessageDO messageDO:list){
                if(messageDO.getUpdates()>0){
                    listResult.add(messageDO);
                }
            }
        }
        return listResult;
    }


    public boolean addMessage(MessageDO messageDO) {
        return mMessageDAO.addMessage(messageDO);
    }

    public boolean addMessageAll(List<MessageDO> messageDO){
        return mMessageDAO.addMessageAll(messageDO);
    }


    //使用主键删除
    public boolean deleteMessage(MessageDO messageDO) {
        try {
            return mMessageDAO.deleteMessage(messageDO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //使用主键删除
    public boolean deleteMessageList(List<MessageDO> messageDO) {
        try {
            return mMessageDAO.deleteMessageList(messageDO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessageByType(int type) {
        return mMessageDAO.deleteMessageByType(type);
    }

    public boolean deleteMessageByTypeAndSn(int type,String sn){
        return mMessageDAO.deleteMessageByTypeAndSn(type,sn);
    }
    public boolean deleteMessageByTypeAndPublicChat(int type,int publicChat) {
        return mMessageDAO.deleteMessageByTypeAndPublicChat(type,publicChat);
    }

    public void deleteAll(){
        mMessageDAO.deleteAll();
    }


    public boolean updateMessage(MessageDO messageDO) {
        return mMessageDAO.updateMessage(messageDO);
    }

    public boolean updateMessageList(List<MessageDO> messageDO) {
        return mMessageDAO.updateMessageList(messageDO);
    }

    /**
     * 更新所有消息的userid
     * @param list
     * @return
     */
    public boolean updateAllMessageUserId(List<MessageDO> list) {
        return mMessageDAO.updateAllMessage(list);
    }

}
