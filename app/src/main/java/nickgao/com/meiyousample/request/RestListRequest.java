package nickgao.com.meiyousample.request;

import java.lang.reflect.Type;

import nickgao.com.meiyousample.network.RcRestRequest;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class RestListRequest<T> extends RcRestRequest<T> {

    public RestListRequest(String url, Type responseType, HttpMethod method, String logTag) {
        super(url, method,responseType, logTag);
    }
}
