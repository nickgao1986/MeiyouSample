package com.meiyou.message.db;

import com.meiyou.sdk.common.database.BaseDAO;
import com.meiyou.sdk.common.database.DaoConfig;
import com.meiyou.sdk.common.database.DbManager;
import com.meiyou.sdk.common.database.DbUpgradeHelper;
import com.meiyou.sdk.common.database.DefaultBaseDAO;

import nickgao.com.framework.utils.BeanManager;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class MessageDaoFactory {

    private static MessageDaoFactory messageDaoFactory;
    private static final String DB_NAME="message_module.db";
    public static MessageDaoFactory getInstance(){
        if(messageDaoFactory ==null){
            messageDaoFactory = new MessageDaoFactory();
        }
        return messageDaoFactory;
    }

    private DaoConfig daoConfig;

    private MessageDaoFactory(){
        daoConfig = new DaoConfig(BeanManager.getUtilSaver().getContext()) {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                //自动升级
                DbUpgradeHelper.autoUpdateAllTable(db);
            }

            @Override
            public String getAuthority() {
                return null;
            }

            @Override
            public Class<?>[] getAllTableClassList() {
                return new Class<?>[0];
            }
        };
        daoConfig.setDbName(DB_NAME);
        daoConfig.setDbVersion(1);
        DbManager.create(daoConfig).init();

    }
    public BaseDAO getBaseDao(){
        return new DefaultBaseDAO(DbManager.get(DB_NAME).getDatabase());
    }

}
