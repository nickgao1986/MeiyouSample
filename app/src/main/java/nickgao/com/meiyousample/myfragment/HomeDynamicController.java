package nickgao.com.meiyousample.myfragment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import biz.threadutil.FileUtils;
import biz.threadutil.ThreadUtil;
import nickgao.com.meiyousample.controller.UserController;
import nickgao.com.meiyousample.firstPage.module.IOnExcuteListener;
import nickgao.com.meiyousample.model.HomeDynamicModel;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class HomeDynamicController {

    private final String HOME_LIST_CACHE_FILE = "home_dynamic_list_cache";
    private final String MY_PUBLISH_CACHE_FILE = "my_publish_dynamic_list_cache";
    private final int MAX_SEND_TIME = 5;//最大重连次数
    private static final String TAG = "HomeDynamicController";
    private static HomeDynamicController instance;


    public static HomeDynamicController getInstance() {
        if (null == instance) {
            instance = new HomeDynamicController();
        }
        return instance;
    }

    public void getHomeDynamicListFromCache(final Context context, final IOnExcuteListener onExcuteListener) {
        ThreadUtil.addTaskForDynamic(context, true, "", new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                List<HomeDynamicModel> homeList = getDynamicListCache(context, HOME_LIST_CACHE_FILE);
                return homeList;
//                return refreshHomeDynamicList(mContext,homeList);
            }

            @Override
            public void onFinish(Object result) {
                if (null != onExcuteListener) {
                    onExcuteListener.onResult(result);
                }
            }
        });
    }

    private List<HomeDynamicModel> getDynamicListCache(Context context, String fileName) {
        List<HomeDynamicModel> resultList = new ArrayList<HomeDynamicModel>();
        try {
            List<HomeDynamicModel> list = (List<HomeDynamicModel>) FileUtils.getObjectFromLocal(context, fileName
                    + UserController.getInstance().getUserId(context));
            if (null != list) {
                resultList.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }


    public void saveHomeDynamicListToCache(final Context context, final List<HomeDynamicModel> list) {
        ThreadUtil.addTaskForDynamic(context, true, "", new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                List<HomeDynamicModel> subList;
                if (null != list && list.size() > 0) {
                    if (list.size() > 20) {
                        subList = list.subList(0, 20);
                    } else {
                        subList = list;
                    }
                    saveDynamicListToCache(context, subList, HOME_LIST_CACHE_FILE);
                }
                return null;
            }

            @Override
            public void onFinish(Object result) {

            }
        });
    }

    private void saveDynamicListToCache(Context context, List<HomeDynamicModel> list, String fileName) {
        try {
            List<HomeDynamicModel> saveList = new ArrayList<HomeDynamicModel>();
            saveList.addAll(list);
            FileUtils.saveObjectToLocal(context, saveList, fileName
                    + UserController.getInstance().getUserId(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
