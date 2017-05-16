package nickgao.com.messages;

import android.content.Context;

/**
 * Author: lwh
 * Date: 1/3/17 10:57.
 */

public class MessageManager {

    private static final String TAG = "MessageManager";
    private Context mContext;
    private int mRegisterId;
    public MessageManager(Context context) {
        mContext = context;
    }

    public int getRegisterId() {
        return mRegisterId;
    }


    /**
     * 设置别名
     *
     * @param userId
     */
    public void registerUser(int userId) {
        try {
            mRegisterId = userId;
            PushBizManager.getInstance().registerUser(userId, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unRegisterUser() {
        try {
            PushBizManager.getInstance().unRegister();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
