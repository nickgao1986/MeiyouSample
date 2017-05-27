package nickgao.com.meiyousample.service;

import android.content.Context;

import java.util.concurrent.CountDownLatch;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.network.RcRestRequest;
import nickgao.com.meiyousample.network.RestRequest;
import nickgao.com.meiyousample.network.UrlList;
import nickgao.com.meiyousample.request.RestListRequestParseJson;

/**
 * Created by gaoyoujian on 2017/5/26.
 */

public class NewsReviewService {

    public static String content = "";
    public static String sendRequest(Context context) throws InterruptedException{
        final CountDownLatch latch=new CountDownLatch(1);//两个工人的协作
        RestListRequestParseJson request = new RestListRequestParseJson<Object>(UrlList.NEWS_REVIEW_LIST, RestRequest.HttpMethod.GET, "aa");
        request.executeRequest(context);

        request.registerOnRequestListener(new RcRestRequest.OnRequestListener(){
            @Override
            public void onSuccess(RcRestRequest request, Object response) {
                String ss = (String)response;
                LogUtils.d("===ss="+ss);
                content = ss;
                latch.countDown();
            }

            @Override
            public void onFail(RcRestRequest request, int errorCode) {

            }

            @Override
            public void onComplete(RcRestRequest request) {

            }
        });
        latch.await();
        return content;
    }
}
