package nickgao.com.messages;

import android.content.Context;
import android.os.Bundle;

import com.meiyou.message.mipush.MiPushAdapter;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class PushBizManager {

    static class Holder {
        static PushBizManager instance = new PushBizManager();
    }

    public static PushBizManager getInstance() {
        return Holder.instance;
    }

    private Context context;

    private List<PushAdapter> mPushAdapterArrayList = new ArrayList<>();


    /**
     * 初始化push客户端
     *
     * @param context context
     * @param type    type  see @PUSH_TYPE_DEFAULT ,PUSH_TYPE_XIAOMI
     * @param bundle  params see@com.meiyou.message.mipush.MiPushAdapter.getInitParams
     *                see@com.meiyou.message.socket.DefaultSocketAdapter.getInitParams
     *
     */
    public void init(Context context, int type, Bundle bundle) {
        this.context = context;
        mPushAdapterArrayList.clear();

//        switch (type) {
//            case PushClientType.PUSH_TYPE_DEFAULT:
//                PushAdapter pushAdapter = new DefaultSocketAdapter(bundle);
//                pushAdapter.init(context, bundle);
//                mPushAdapterArrayList.add(pushAdapter);
//                break;
//            case PushClientType.PUSH_TYPE_XIAOMI:
//                pushAdapter = new MiPushAdapter(bundle);
//                pushAdapter.init(context, bundle);
//                mPushAdapterArrayList.add(pushAdapter);
//                break;
//            case PushClientType.PUSH_TYPE_DEFAULT_AD_XM:
//                pushAdapter = new DefaultSocketAdapter(bundle);
//                pushAdapter.init(context, bundle);
//                PushAdapter miAdapter = new MiPushAdapter(bundle);
//                miAdapter.init(context, bundle);
//                mPushAdapterArrayList.add(pushAdapter);
//                mPushAdapterArrayList.add(miAdapter);
//                break;
//        }
        MiPushAdapter pushAdapter = new MiPushAdapter(bundle);
        pushAdapter.init(context, bundle);
        mPushAdapterArrayList.add(pushAdapter);
    }

    /**
     * 向push中心 注册用户
     * 注意: 支持多次调用,
     * 每一次,该方法都将覆盖掉之前的userId
     *
     * @param userId user
     */
    @Deprecated
    public void registerUser(long userId) {
        for (PushAdapter pushAdapter :
                mPushAdapterArrayList) {
            MiPushClient.getAllAlias(context).clear();
            pushAdapter.registerUser(userId);
        }
    }

    public void registerUser(long userId,boolean isTest) {
        for (PushAdapter pushAdapter :
                mPushAdapterArrayList) {
            //MiPushClient.getAllAlias(context).clear();
            pushAdapter.registerUser(userId,isTest);
        }
    }

    /**
     * 移除注册
     */
    public void unRegister() {
        for (PushAdapter pushAdapter :
                mPushAdapterArrayList) {
            pushAdapter.unRegister();
        }
    }


}
