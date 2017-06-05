package nickgao.com.meiyousample.myfragment;

/**
 * 加载成功监听
 * Created by LinXin on 2016/6/20 9:55.
 */
public interface OnLoadListSuccessListener<ListItem> {

    /**
     * 加载成功
     *
     * @param response 响应结果
     */
    void onSuccess(boolean isRefreshing, ListItem response);
}
