package nickgao.com.meiyousample.service;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.personal.TopicListener;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class TopicService extends AbstractService {

    private IRequestFactory requestFactory;
    private TopicListener mListener;
    public TopicData mTopicData = null;

    public TopicService(IRequestFactory requestFactory) {
        super(requestFactory);
    }


    public TopicData sendRequest(final String last) throws InterruptedException{
        Context context = SeeyouApplication.getContext();
        RcRestRequest<TopicData> request = this.mRequestFactory.createTopicListRequest(last);
        final CountDownLatch latch=new CountDownLatch(1);//两个工人的协作

        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<TopicData>() {
            @Override
            public void onSuccess(RcRestRequest<TopicData> request, TopicData response) {
//                mListener.onSuccess(response, !TextUtils.isEmpty(last));
                mTopicData = response;
                latch.countDown();
            }

            @Override
            public void onFail(RcRestRequest<TopicData> request, int errorCode) {
               // mListener.onFail();
            }

            @Override
            public void onComplete(RcRestRequest<TopicData> request) {

            }
        });

        request.executeRequest(context);
        latch.await();
        return mTopicData;
    }
}
