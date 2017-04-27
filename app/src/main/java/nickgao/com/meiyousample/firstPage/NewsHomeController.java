package nickgao.com.meiyousample.firstPage;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class NewsHomeController {

    private boolean mOnRefresh = false;//是否在刷新 外层view和里层共用一个变量值 为了不会产生差异
    private int currentPagePosition;
    private int currentScrollY;//滚动位置


    private static class CommunityMainControllerHodler {
        public static final NewsHomeController INSTANCE = new NewsHomeController();
    }

    public static NewsHomeController getInstance() {
        return CommunityMainControllerHodler.INSTANCE;
    }

    public void setClassifySelect(int position) {
        currentPagePosition = position;
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

    /**
     * 保存外层滚动的位置
     *
     * @param currentScrollY
     */
    public void setCurrentScrollY(int currentScrollY) {
        this.currentScrollY = currentScrollY;
    }

    public int getCurrentScrollY() {
        return currentScrollY;
    }

}
