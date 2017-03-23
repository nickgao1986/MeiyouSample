package nickgao.com.meiyousample.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.network.RestRequest;
import nickgao.com.meiyousample.network.UrlList;
import nickgao.com.meiyousample.request.RestListRequest;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class RestRequestFactory implements IRequestFactory {


    @Override
    public RcRestRequest<DynamicData> createDynamicListRequest() {
        Type type = new TypeToken<DynamicData>() {
        }.getType();
        return new RestListRequest<DynamicData>(UrlList.DYNAMIC_LIST, type, RestRequest.HttpMethod.GET, TAG_DYNAMIC_LIST);
    }


    @Override
    public RcRestRequest<UserHomePage> createUserHomeRequest() {
        Type type = new TypeToken<UserHomePage>() {
        }.getType();
        return new RestListRequest<UserHomePage>(UrlList.USERHOMEPAGE, type, RestRequest.HttpMethod.GET, TAG_USER_HOME);
    }
}
