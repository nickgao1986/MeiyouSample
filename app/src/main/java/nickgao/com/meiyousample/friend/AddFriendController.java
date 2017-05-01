package nickgao.com.meiyousample.friend;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import biz.threadutil.FileUtils;
import nickgao.com.meiyousample.controller.UserController;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class AddFriendController {

    private static AddFriendController mInstance;

    public static final int NOATTENTION = 0;        //未关注
    public static final int ATTENTION = 1;        //已关注 ,对方还没关注自己
    public static final int ATTENTION_ME = 2;    //对方关注了自己
    public static final int BACKLIST = 3;            //黑名单
    public static final int ATTENTION_MUTUAL = 4; //相互关注了
    public static final int FOR_BACKLIST = 5;//对方拉黑我
    public static final String INTEREST_FRIEND_FILE = "friend_interest_file";//感兴趣好友
    public static final String FRIEND_RECOMMEND_FILE = "friend_recommend_file";//推荐好友
    public static final String FAMOUS_PERSON_FILE = "friend_famous_person_file";//名人好友
    public static final String THIRD_FRIEND_FILE = "friend_third_file";//朋友第三方好友
    public static final String FRIEND_FANS_FILE = "friend_fans_file";//粉丝
    public static final String FRIEND_FOLLOW_FILE = "friend_follow_file";//关注
    private Context mContext;

    public AddFriendController(Context mContext) {
        this.mContext = mContext;
    }

    public static AddFriendController getInstance(Context context) {
        Context mContext = context.getApplicationContext();
        if (mInstance == null) {
            mInstance = new AddFriendController(mContext);
        }
        return mInstance;
    }

    public List<AddFriendModel> getListFromCache(String file) {
        try {
            List<AddFriendModel> models = (List<AddFriendModel>) FileUtils.getObjectFromLocal(
                    mContext, file
                            + UserController.getInstance().getUserId(mContext));
            if (models != null) {
                return models;
            }
            return new ArrayList<AddFriendModel>();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<AddFriendModel>();

    }


}
