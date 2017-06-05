package nickgao.com.meiyousample.myfragment;

import com.com.meetyou.news.model.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.dynamicModel.DynamicContent;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.service.DynamicService;
import nickgao.com.meiyousample.service.ServiceFactory;

/**
 * Created by gaoyoujian on 2017/6/2.
 */

public class DynamicModel extends RecycleViewListModel<DynamicData,HomeDynamicModel> {

    private Disposable mDisposable;
    private DynamicData mDynamicData;
    private boolean hasNext;
    private int sort;

    public DynamicModel(List<HomeDynamicModel> list) {
        super(list);
    }

    @Override
    public void setData(boolean isRefreshing, DynamicData response) {
        if (isRefreshing) {
            clear();
        }
        List<HomeDynamicModel> mapList = map(response);
        LogUtils.d("====list size="+mapList.size());
        if(mapList.size() == 0) {
            hasNext = false;
        }else{
            hasNext = true;
        }
        if (mapList == null) {
            return;
        }
        mList.addAll(mapList);
    }

    @Override
    public void preRefresh() {
        sort = 0;
    }

    @Override
    public void preReLoad() {

    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }


    @Override
    public void loadMore() {
        if (mList.isEmpty()) {
            sort = 0;
        } else {
            sort = mList.get(mList.size() - 1).sort;
        }
    }

    @Override
    public void load(final OnLoadListener<DynamicData> listener) {
        listener.onStart();
        mDisposable = Flowable.create(new FlowableOnSubscribe<DynamicData>() {
            @Override
            public void subscribe(FlowableEmitter<DynamicData> emitter) throws Exception {
                DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
                DynamicData mDynamicData = service.sendRequest(sort);
                emitter.onNext(mDynamicData);

            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DynamicData>() {
                    @Override
                    public void accept(DynamicData dynamicModel) throws Exception {
                        listener.onSuccess(dynamicModel);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.onError(throwable);
                    }
                });

    }

    @Override
    public void cancel() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public List<HomeDynamicModel> map(DynamicData dynamicModel) {
        DynamicContent[] contents = dynamicModel.content;
        ArrayList myList = new ArrayList<HomeDynamicModel>();
        for (int i = 0; i < contents.length; i++) {
            DynamicContent dynamicContent = contents[i];
            HomeDynamicModel model = new HomeDynamicModel();
            model.screenName = dynamicContent.screen_name;
            model.isAllowOperate = dynamicContent.allow_operate;
            model.iconUrl = dynamicContent.avatar;
            model.content = dynamicContent.content;
            model.type = dynamicContent.type;
            model.sort = dynamicContent.sort;
            model.imagesList = new ArrayList<String>();
            for (int j = 0; j < dynamicContent.images.length; j++) {
                model.imagesList.add(dynamicContent.images[j]);
            }
            myList.add(model);
        }
        return myList;
    }
}
