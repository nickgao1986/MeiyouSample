package nickgao.com.meiyousample.service;

import android.content.Context;
import android.text.TextUtils;

import nickgao.com.meiyousample.MeiyouApplication;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.personal.TopicListener;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class TopicService extends AbstractService {

    private IRequestFactory requestFactory;
    private TopicListener mListener;
    public TopicService(IRequestFactory requestFactory) {
        super(requestFactory);
    }


    public void sendRequest(TopicListener listener, final String last) {
        Context context = MeiyouApplication.getContext();
        RcRestRequest<TopicData> request = this.mRequestFactory.createTopicListRequest(last);
        mListener = listener;
        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<TopicData>() {
            @Override
            public void onSuccess(RcRestRequest<TopicData> request, TopicData response) {
                mListener.onSuccess(response, !TextUtils.isEmpty(last));
            }

            @Override
            public void onFail(RcRestRequest<TopicData> request, int errorCode) {
                mListener.onFail();
            }

            @Override
            public void onComplete(RcRestRequest<TopicData> request) {

            }
        });

        request.executeRequest(context);

    }
}
