package nickgao.com.meiyousample.service;


import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.network.RcRestRequest;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public interface IRequestFactory {

    String TAG_DYNAMIC_LIST = "[RC]Dynamic";
    String TAG_USER_HOME = "[RC]UserHome";

    RcRestRequest<DynamicData> createDynamicListRequest();

    RcRestRequest<UserHomePage> createUserHomeRequest();

}
