package nickgao.com.meiyousample.personal;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public interface ILoadMoreViewState {
    int NORMAL = 0; // 普通状态
    int LOADING = 1; // 加载状态
    int COMPLETE = 2; // 结束状态
    int ERROR = -1; // 结束状态
    int HIDE=3;
}
