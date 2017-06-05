package nickgao.com.meiyousample.service;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.personal.PersonalListener;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class DynamicService extends AbstractService {

    private  IRequestFactory requestFactory;
    private PersonalListener mListener;
    private int sort;
    private boolean isLoadMore = false;
    public DynamicData mDynamicData = null;

    public DynamicService(IRequestFactory requestFactory) {
        super(requestFactory);
        requestFactory = requestFactory;
    }


    public DynamicData sendRequest(final int sort) throws InterruptedException{
        final CountDownLatch latch=new CountDownLatch(1);//两个工人的协作

        Context context = SeeyouApplication.getContext();
        RcRestRequest<DynamicData> request = this.mRequestFactory.createDynamicListRequest(sort);
        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<DynamicData>() {
            @Override
            public void onSuccess(RcRestRequest<DynamicData> request, DynamicData response) {
                LogUtils.d("====DynamicService success="+response);
                if(sort == 0) {
                    isLoadMore = true;
                }
                mDynamicData = response;
                latch.countDown();
              //  mListener.onSuccess(response,isLoadMore);
            }

            @Override
            public void onFail(RcRestRequest<DynamicData> request, int errorCode) {
                LogUtils.d("====onFail="+errorCode);

            }

            @Override
            public void onComplete(RcRestRequest<DynamicData> request) {

            }
        });

        request.executeRequest(context);
        latch.await();
        return mDynamicData;

    }


}
