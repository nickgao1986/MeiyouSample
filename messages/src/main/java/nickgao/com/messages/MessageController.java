package nickgao.com.messages;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.meiyou.message.db.MessageDBManager;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.CommomCallBack;
import nickgao.com.framework.utils.BeanManager;
import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/5/15.
 */

public class MessageController {

    private static final java.lang.String TAG = "MessageController";
    private MessageManager mMessageManager;
    private MessageDBManager mMessageDBManager;
    private boolean isAtMessageActivity;

    private static class Holder {
        static MessageController instance = new MessageController();
    }

    public static MessageController getInstance() {
        return Holder.instance;
    }

    private Context mContext;

    public void init(Context context, int type, Bundle bundle) {
        try {

            mContext = context;
            long time = System.currentTimeMillis();
            //通道初始化
            PushBizManager.getInstance().init(mContext, type, bundle);
            long timeEnd = System.currentTimeMillis();
            LogUtils.d(TAG, "==> PushBizManager.getInstance().init 耗时：" + (timeEnd - time));

            time = System.currentTimeMillis();
            mMessageDBManager = new MessageDBManager(mContext);
            timeEnd = System.currentTimeMillis();
            LogUtils.d(TAG, "==> MessageDBManager 耗时：" + (timeEnd - time));
            time = System.currentTimeMillis();
            mMessageManager = new MessageManager(mContext);
            timeEnd = System.currentTimeMillis();
            LogUtils.d(TAG, "==> MessageManager 耗时：" + (timeEnd - time));

            //mMessageSettingConfig = new MessageSettingConfig();
            time = System.currentTimeMillis();
            //注册事件监听,用于设置别名
            addXiaomiRegCallback(new CommomCallBack() {
                @Override
                public void onResult(Object object) {
                    int userIdV = BeanManager.getUtilSaver().getUserVirtualId(mContext);
                    int userId = BeanManager.getUtilSaver().getUserId(mContext);
                    int id = userId > 0 ? userId : userIdV;
                    if(id<=0){
                        LogUtils.d(TAG, "xiaomi 此时还没有获取的用户ID,不进行设置别名");
                        return;
                    }
                    login(id,true);

                }
            });

            //注册通知栏消息到达监听
//            addXiaomiNotificationMessagePushArrivedCallback(new CommomCallBack() {
//                @Override
//                public void onResult(Object object) {
//                    PushMsgModel pushModel = (PushMsgModel) object;
//                    getMessageManager().handleCountMessagePushArrived(pushModel);
//                }
//            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private List<CommomCallBack> mXiaomiRegCallback = new ArrayList<>();

    public void addXiaomiRegCallback(CommomCallBack xiaomiRegCallback) {
        if (xiaomiRegCallback != null && !mXiaomiRegCallback.contains(xiaomiRegCallback)) {
            mXiaomiRegCallback.add(xiaomiRegCallback);
        }
    }

    public void notifyXiaomiRegCallback(Object object) {
        for (CommomCallBack callback : mXiaomiRegCallback) {
            try {
                callback.onResult(object);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public MessageManager getMessageManager() {
        if(mMessageManager==null)
            mMessageManager = new MessageManager(mContext);
        return mMessageManager;
    }


    public void login(final int userId, boolean isRelogin){
        try {

            LogUtils.d(TAG, "xiaomi MessageController userId:"+userId+"==>isRelogin:"+isRelogin);
            if(getMessageManager()==null){
                LogUtils.d(TAG, "xiaomi getMessageManager() is null");
                return;
            }
            LogUtils.d(TAG, "xiaomi MessageController registed userId:"+getMessageManager().getRegisterId());

            if(!isRelogin && userId==getMessageManager().getRegisterId()){
                LogUtils.d(TAG, "xiaomi 用户ID一样,无需重复注册");
                return;
            }
            getMessageManager().unRegisterUser();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMessageManager().registerUser(userId);;
                }
            },500);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void logout(){
        try {
            if(getMessageManager()==null){
                return;
            }
            getMessageManager().unRegisterUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
