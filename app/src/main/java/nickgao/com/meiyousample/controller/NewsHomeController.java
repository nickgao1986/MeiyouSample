package nickgao.com.meiyousample.controller;

/**
 * Created by gaoyoujian on 2017/3/7.
 */

public class NewsHomeController {

    private boolean mOnRefresh = false;//是否在刷新 外层view和里层共用一个变量值 为了不会产生差异

    private NewsHomeController() {
    }

    public static NewsHomeController getInstance() {
        return new NewsHomeController();
    }
    /**
     * 首页动画效果
     *
     * @return
     */
    public boolean isOnRefresh() {
        return mOnRefresh;
    }

    public void setOnRefresh(boolean mOnRefresh) {
        this.mOnRefresh = mOnRefresh;
    }

}
