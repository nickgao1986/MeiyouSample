package com.meiyou.message.db;

import com.meiyou.sdk.common.database.BaseDAO;
import com.meiyou.sdk.common.database.sqlite.Selector;
import com.meiyou.sdk.common.database.sqlite.WhereBuilder;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class MessageDAO {

    private BaseDAO baseDAO;

    private String[] updateColumnNames=new String[]{"name", "pushType", "leapType", "sn",  "type", "icon","updates","originalData","isMine","isSend","messageId","isPublicChat"};
    public MessageDAO(){
        this.baseDAO  = MessageDaoFactory.getInstance().getBaseDao();
    }

    /*****************查询**********************/
    public List<MessageDO> getMessageList(long userid){
        //List<BehaviorDO> list = baseDAO.queryAll(BehaviorDO.class);
        List<MessageDO> list = baseDAO.query(MessageDO.class, Selector.from(MessageDO.class).where("userId", "=", userid));
        return list;
    }

    public List<MessageDO> getMessageListByType(long userid,int type){
        //List<BehaviorDO> list = baseDAO.queryAll(BehaviorDO.class);
        List<MessageDO> list = baseDAO.query(MessageDO.class, Selector.from(MessageDO.class).where("userId", "=", userid).and("type", "=", type));
        return list;
    }


    public List<MessageDO> getUnreadMessageListByType(long userid,int type){
        //List<BehaviorDO> list = baseDAO.queryAll(BehaviorDO.class);
        List<MessageDO> list = baseDAO.query(MessageDO.class, Selector.from(MessageDO.class).where("userId", "=", userid).and("type", "=", type).and("updates", ">", 0));
        return list;
    }

    /*****************新增**********************/
    public boolean addMessage(MessageDO messageDO){
        int result =  baseDAO.insert(messageDO);
        return result>0?true:false;
    }

    public boolean addMessageAll(List<MessageDO> messageDO){
        int result =  baseDAO.insertAll(messageDO);
        return result>0?true:false;
    }

    /*****************删除**********************/
    public boolean  deleteMessage(MessageDO messageDO){
        int result =  baseDAO.delete(messageDO);
        return result>0?true:false;
    }

    public boolean deleteMessageList(List<MessageDO> messageDO){
        int result =  baseDAO.deleteAll(messageDO);
        return result>0?true:false;
    }
    public boolean deleteMessageByType(int type){
        List<MessageDO> list =  baseDAO.query(MessageDO.class,Selector.from(MessageDO.class).where(WhereBuilder.b("type", "=",type)));
        if(list!=null &&list.size()>0){
            baseDAO.deleteAll(list);
        }
        return true;
    }

    public boolean deleteMessageByTypeAndSn(int type,String sn){
        List<MessageDO> list =  baseDAO.query(MessageDO.class,Selector.from(MessageDO.class).where(WhereBuilder.b("type", "=",type).and("sn","=",sn)));
        if(list!=null &&list.size()>0){
            baseDAO.deleteAll(list);
        }
        return true;
    }

    public boolean deleteMessageByTypeAndPublicChat(int type,int publicChat){
        List<MessageDO> list =  baseDAO.query(MessageDO.class,Selector.from(MessageDO.class).where(WhereBuilder.b("type", "=",type).and("isPublicChat", "=", publicChat)));
        if(list!=null &&list.size()>0){
            baseDAO.deleteAll(list);
        }
        return true;
    }

    public void deleteAll(){
        baseDAO.deleteAll(MessageDO.class);
    }
    /*****************更新**********************/



    public boolean updateMessage(MessageDO messageDO){
        int result =  baseDAO.update(messageDO, WhereBuilder.b("columnId", "=", messageDO.getColumnId()), updateColumnNames);
        return result>0?true:false;
    }

    public boolean updateMessageList(List<MessageDO> list){
        int result =  baseDAO.updateAll(list,updateColumnNames);
        return result>0?true:false;
    }
    public boolean updateAllMessage(List<MessageDO> list){
        int result = baseDAO.updateAll(list,updateColumnNames);
        return result>0?true:false;
    }


}
