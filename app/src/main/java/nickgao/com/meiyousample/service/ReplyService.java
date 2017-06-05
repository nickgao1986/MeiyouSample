package nickgao.com.meiyousample.service;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.model.reply.ReplyData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.personal.ReplyListener;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class ReplyService extends AbstractService {

    private IRequestFactory requestFactory;
    private ReplyListener mListener;
    public ReplyService(IRequestFactory requestFactory) {
        super(requestFactory);
    }
    public ReplyData mReplyData = null;


    public ReplyData sendRequest(final String last) throws InterruptedException {
        Context context = SeeyouApplication.getContext();
        RcRestRequest<ReplyData> request = this.mRequestFactory.createReplyListRequest(last);
        final CountDownLatch latch=new CountDownLatch(1);//两个工人的协作
        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<ReplyData>() {
            @Override
            public void onSuccess(RcRestRequest<ReplyData> request, ReplyData response) {
                mReplyData = response;
                latch.countDown();
            }

            @Override
            public void onFail(RcRestRequest<ReplyData> request, int errorCode) {

            }

            @Override
            public void onComplete(RcRestRequest<ReplyData> request) {

            }
        });

        request.executeRequest(context);
        latch.await();
        return mReplyData;
    }
}
