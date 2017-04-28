package nickgao.com.meiyousample.personal;

import nickgao.com.meiyousample.model.topic.TopicData;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public interface TopicListener {
    public void onSuccess(TopicData response, boolean isLoaderMore);
    public void onFail();
}
