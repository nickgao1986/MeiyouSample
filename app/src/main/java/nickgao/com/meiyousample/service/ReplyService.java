package nickgao.com.meiyousample.service;

import android.content.Context;
import android.text.TextUtils;

import nickgao.com.meiyousample.MeiyouApplication;
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


    public void sendRequest(ReplyListener listener, final String last) {
        Context context = MeiyouApplication.getContext();
        RcRestRequest<ReplyData> request = this.mRequestFactory.createReplyListRequest(last);
        mListener = listener;
        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<ReplyData>() {
            @Override
            public void onSuccess(RcRestRequest<ReplyData> request, ReplyData response) {
                mListener.onSuccess(response,!TextUtils.isEmpty(last));
            }

            @Override
            public void onFail(RcRestRequest<ReplyData> request, int errorCode) {

            }

            @Override
            public void onComplete(RcRestRequest<ReplyData> request) {

            }
        });

        request.executeRequest(context);

    }
}
