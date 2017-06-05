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
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.model.reply.TopicModelItem;
import nickgao.com.meiyousample.model.topic.MyTopicModel;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.service.ServiceFactory;
import nickgao.com.meiyousample.service.TopicService;

/**
 * Created by gaoyoujian on 2017/6/3.
 */

public class TopicModel extends RecycleViewListModel<TopicData,MyTopicModel> {


    private Disposable mDisposable;
    private DynamicData mDynamicData;
    private boolean hasNext;
    private String published_date = "";

    public TopicModel(List<MyTopicModel> list) {
        super(list);
    }

    @Override
    public void setData(boolean isRefreshing, TopicData response) {
        if (isRefreshing) {
            clear();
        }
        List<MyTopicModel> mapList = map(response);
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
        published_date = "";
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
            published_date = "";
        } else {
            published_date = mList.get(mList.size() - 1).published_date;
        }
    }

    @Override
    public void load(final OnLoadListener<TopicData> listener) {
        listener.onStart();
        mDisposable = Flowable.create(new FlowableOnSubscribe<TopicData>() {
            @Override
            public void subscribe(FlowableEmitter<TopicData> emitter) throws Exception {
//                DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
//                DynamicData mDynamicData = service.sendRequest(sort);

                TopicService service = (TopicService) ServiceFactory.getInstance().getService(TopicService.class.getName());
                TopicData topicData = service.sendRequest(published_date);
                emitter.onNext(topicData);

            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TopicData>() {
                    @Override
                    public void accept(TopicData dynamicModel) throws Exception {
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
    public List<MyTopicModel> map(TopicData response) {
        TopicModelItem[] contents = response.data;
        final ArrayList<MyTopicModel> myList = new ArrayList<MyTopicModel>();
        for (int i=0; i < contents.length; i++) {
            TopicModelItem topicModel = contents[i];
            MyTopicModel model = new MyTopicModel();
            model.forum_name = topicModel.forum_name;
            model.image_count = topicModel.image_count;
            model.images = new ArrayList<String>();
            for (int j=0;j<topicModel.images.length;j++) {
                model.images.add(topicModel.images[j]);
            }
            model.published_date = topicModel.published_date;
            model.topic_id = topicModel.topic_id;
            model.type = topicModel.type;
            model.title = topicModel.title;
            myList.add(model);
        }

        return myList;
    }
}
