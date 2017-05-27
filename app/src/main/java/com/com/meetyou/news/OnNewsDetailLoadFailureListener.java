package com.com.meetyou.news;

import android.app.Activity;

import com.com.meetyou.news.model.HttpStatusErrorException;
import com.com.meetyou.news.model.OnLoadFailureListener;


/**
 * 资讯详情加载失败监听
 * Created by LinXin on 2017/5/2.
 */
public class OnNewsDetailLoadFailureListener implements OnLoadFailureListener {

    private Activity activity;

    public OnNewsDetailLoadFailureListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onFailure(boolean isRefreshing, Throwable e) {
        if (e instanceof HttpStatusErrorException) {
            int code = ((HttpStatusErrorException) e).getCode();
//            if (code == NewsErrorCodeConstant.NEWS_DELETED_BY_REVIEW) {
//                activity.finish();
//            }
        }
    }
}
