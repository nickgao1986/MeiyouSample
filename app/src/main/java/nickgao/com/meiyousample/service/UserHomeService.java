package nickgao.com.meiyousample.service;

import android.content.Context;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.personal.UserDataListener;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class UserHomeService extends AbstractService {

    private IRequestFactory requestFactory;
    private UserDataListener mListener;
    public UserHomeService(IRequestFactory requestFactory) {
        super(requestFactory);
    }


    public void sendRequest(UserDataListener listener) {
        Context context = SeeyouApplication.getContext();
        RcRestRequest<UserHomePage> request = this.mRequestFactory.createUserHomeRequest();
        mListener = listener;
        request.registerOnRequestListener(new RcRestRequest.OnRequestListener<UserHomePage>() {
            @Override
            public void onSuccess(RcRestRequest<UserHomePage> request, UserHomePage response) {
                LogUtils.d("====UserHomeServices success="+response);
                mListener.onSuccess(response);
            }

            @Override
            public void onFail(RcRestRequest<UserHomePage> request, int errorCode) {
                LogUtils.d("====UserHomeServices fail="+errorCode);
                mListener.onFail();

            }

            @Override
            public void onComplete(RcRestRequest<UserHomePage> request) {

            }
        });

        request.executeRequest(context);

    }
}
