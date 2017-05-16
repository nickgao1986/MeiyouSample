package nickgao.com.meiyousample.service;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.model.reply.ReplyData;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.network.RestRequest;
import nickgao.com.meiyousample.network.UrlList;
import nickgao.com.meiyousample.request.RestListRequest;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class RestRequestFactory implements IRequestFactory {


    @Override
    public RcRestRequest<DynamicData> createDynamicListRequest(int sort) {
        Type type = new TypeToken<DynamicData>() {
        }.getType();
        String url = UrlList.DYNAMIC_LIST;
        if(sort != 0) {
            url += "&"+"time="+sort;
            url += "&"+"fuid=129746620";
        }
        LogUtils.d("=====url="+url);
        return new RestListRequest<DynamicData>(UrlList.DYNAMIC_LIST, type, RestRequest.HttpMethod.GET, TAG_DYNAMIC_LIST);
    }


    @Override
    public RcRestRequest<UserHomePage> createUserHomeRequest() {
        Type type = new TypeToken<UserHomePage>() {
        }.getType();
        return new RestListRequest<UserHomePage>(UrlList.USERHOMEPAGE, type, RestRequest.HttpMethod.GET, TAG_USER_HOME);
    }

    @Override
    public RcRestRequest<TopicData> createTopicListRequest(String last) {
        Type type = new TypeToken<TopicData>() {
        }.getType();

        String url = UrlList.TOPIC_LIST;
        if(!TextUtils.isEmpty(last)) {
            url += "&"+"last="+last;
        }
        return new RestListRequest<TopicData>(url, type, RestRequest.HttpMethod.GET, TAG_TOPIC_LIST);
    }

    @Override
    public RcRestRequest<ReplyData> createReplyListRequest(String last) {
        Type type = new TypeToken<ReplyData>() {
        }.getType();

        String url = UrlList.REPLY_LIST;
        if(!TextUtils.isEmpty(last)) {
            url += "&"+"last="+last;
        }
        return new RestListRequest<ReplyData>(url, type, RestRequest.HttpMethod.GET, TAG_REPLY_LIST);
    }
}
