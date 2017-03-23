package nickgao.com.meiyousample.service;

import android.content.Context;

import nickgao.com.meiyousample.MeiyouApplication;
import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class UserHomeService extends AbstractService {

    private  IRequestFactory requestFactory;

    public UserHomeService(IRequestFactory requestFactory) {
        super(requestFactory);
    }


    public void sendRequest() {
        Context context = MeiyouApplication.getContext();
        RcRestRequest<UserHomePage> request = this.mRequestFactory.createUserHomeRequest();

        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<UserHomePage>() {
            @Override
            public void onSuccess(RcRestRequest<UserHomePage> request, UserHomePage response) {
                LogUtils.d("====UserHomeServices success="+response);

            }

            @Override
            public void onFail(RcRestRequest<UserHomePage> request, int errorCode) {
                LogUtils.d("====UserHomeServices fail="+errorCode);

            }

            @Override
            public void onComplete(RcRestRequest<UserHomePage> request) {

            }
        });

        request.executeRequest(context);

    }
}
