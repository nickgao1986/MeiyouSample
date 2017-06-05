package nickgao.com.meiyousample.myfragment;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/6/2.
 */

public abstract class RecycleViewListModel<Response,ListItem> implements IRecycleViewModel<Response,ListItem>{

    protected static int FIRST_PAGE = 0;
    protected static int PAGE_SIZE = 10;
    protected List<ListItem> mList;
    protected int page;
    private boolean hasNext;
    private int newAddCount;
    private int oldCount;

    public RecycleViewListModel(List<ListItem> itemList) {
        this.mList = itemList;
    }

    @Override
    public boolean isEmpty() {
        return mList == null || mList.isEmpty();
    }


    @Override
    public boolean hasNext() {
        return hasNext;
    }

    public void clear() {
        mList.clear();
    }

    public void setData(boolean isRefreshing, Response response) {
        if (isRefreshing) {
            clear();
        }
        List<ListItem> mapList = map(response);
        hasNext = ensureHasNext(response, mapList);
        if (mapList == null) {
            return;
        }
        newAddCount = mapList.size();
        oldCount = mList.size();
        mList.addAll(mapList);
    }


    private boolean ensureHasNext(Response response,List<ListItem> mapList) {
        return true;
    }


    public int getOldCount() {
        return oldCount;
    }

    public int getNewAddCount() {
        return newAddCount;
    }

}
