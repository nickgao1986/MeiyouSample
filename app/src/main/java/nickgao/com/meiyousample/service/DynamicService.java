package nickgao.com.meiyousample.service;

import android.content.Context;

import nickgao.com.meiyousample.MeiyouApplication;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.utils.LogUtils;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class DynamicService extends AbstractService {

    private  IRequestFactory requestFactory;

    public DynamicService(IRequestFactory requestFactory) {
        super(requestFactory);
        requestFactory = requestFactory;
    }


    public void sendRequest() {
        Context context = MeiyouApplication.getContext();
        RcRestRequest<DynamicData> request = this.mRequestFactory.createDynamicListRequest();

        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<DynamicData>() {
            @Override
            public void onSuccess(RcRestRequest<DynamicData> request, DynamicData response) {
                LogUtils.d("====DynamicService success="+response);
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

    }
}
