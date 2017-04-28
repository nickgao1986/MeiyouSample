package nickgao.com.meiyousample.fragment;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.MyPersonalTopicListAdapter;
import nickgao.com.meiyousample.model.topic.MyTopicModel;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.model.topic.TopicModel;
import nickgao.com.meiyousample.personal.TopicListener;
import nickgao.com.meiyousample.service.ReplyService;
import nickgao.com.meiyousample.service.ServiceFactory;
import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class ReplyFragment extends PersonalContentFragment implements TopicListener {


    private Handler mHandler = new Handler();
    MyPersonalTopicListAdapter mAdapter = null;
    private List<MyTopicModel> models = new ArrayList<MyTopicModel>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendRequest();
        initView();
    }


    private void initView() {
        mAdapter = new MyPersonalTopicListAdapter(mActivity, models, MyPersonalTopicListAdapter.TopicType.TYPE_REPLY,mListview);
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void subClassLoadMore() {
        if(models != null && models.size() > 0) {
            setLoadingState(LoadingState.LOADING_MORE);
            LogUtils.d("======dynamic subclassLoadmore");
            ReplyService service = (ReplyService) ServiceFactory.getInstance().getService(ReplyService.class.getName());
            service.sendRequest(this,models.get(models.size() - 1).published_date);
        }
    }

    @Override
    public void onSuccess(TopicData response, final boolean isLoadMore) {
        TopicModel[] contents = response.data.topics;
        final ArrayList<MyTopicModel> myList = new ArrayList<MyTopicModel>();
        for (int i=0; i < contents.length; i++) {
            TopicModel topicModel = contents[i];
            MyTopicModel model = new MyTopicModel();
            model.forum_name = topicModel.forum_name;
            model.image_count = topicModel.image_count;
            model.images = new ArrayList<String>();
            model.title = topicModel.title;
            for (int j=0;j<topicModel.images.length;j++) {
                model.images.add(topicModel.images[j]);
            }
            model.published_date = topicModel.published_date;
            model.topic_id = topicModel.topic_id;
            model.type = topicModel.type;

            myList.add(model);
        }

        if(!isLoadMore) {
            models.clear();
            models.addAll(myList);

        }else{
            models.addAll(myList);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(!isLoadMore) {
                    if(myList.size() == 0) {
                        setLoadingState(LoadingState.NO_DATA);
                    }else{
                        setLoadingState(LoadingState.LOADING_COMPLETE);
                    }
                }else{
                    setLoadingState(LoadingState.FOOTER_COMPLETE);
                }
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    void sendRequest() {
        setLoadingState(LoadingState.LOADING_NEW_DATA);

        ReplyService service = (ReplyService) ServiceFactory.getInstance().getService(ReplyService.class.getName());
        service.sendRequest(this,null);
    }

    @Override
    public void onFail() {

    }
}
