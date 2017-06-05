package nickgao.com.meiyousample.myfragment;

import com.com.meetyou.news.model.OnLoadListener;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/6/2.
 */

public interface IRecycleViewModel<Response,ListItem> {

    /**
     * 界面数据是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 设置数据
     *
     * @param response
     */
    void setData(boolean isRefreshing, Response response);

    /**
     * 刷新前要做的操作,一般用于改变缓存类型,列表页面须用次方法将页码归零
     */
    void preRefresh();

    /**
     * 重新加载前要做的操作,一般用于改变缓存类型
     */
    void preReLoad();

    void loadMore();

    /**
     * 加载数据
     *
     * @param listener 加载监听
     */
    void load(OnLoadListener<Response> listener);

    /**
     * 取消加载
     */
    void cancel();


    List<ListItem> map(Response response);

    boolean hasNext();

}
