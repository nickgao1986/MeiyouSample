package nickgao.com.meiyousample.service;


import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.model.reply.ReplyData;
import nickgao.com.meiyousample.model.topic.TopicData;
import nickgao.com.meiyousample.network.RcRestRequest;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public interface IRequestFactory {

    String TAG_DYNAMIC_LIST = "[RC]Dynamic";
    String TAG_USER_HOME = "[RC]UserHome";
    String TAG_TOPIC_LIST = "[RC]Topic";
    String TAG_REPLY_LIST = "[RC]Reply";
    RcRestRequest<DynamicData> createDynamicListRequest(int sort);

    RcRestRequest<UserHomePage> createUserHomeRequest();


    RcRestRequest<TopicData> createTopicListRequest(String last);

    RcRestRequest<ReplyData> createReplyListRequest(String last);
}
