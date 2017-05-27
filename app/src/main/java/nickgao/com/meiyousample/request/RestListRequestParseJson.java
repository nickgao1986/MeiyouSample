package nickgao.com.meiyousample.request;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.network.RcRestRequest;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class RestListRequestParseJson<T> extends RcRestRequest<T> {

    public RestListRequestParseJson(String url, HttpMethod method, String logTag) {
        super(url, method, logTag);
    }


    @Override
    public void onResponse(Reader response, InputStream responseStream, String contentType, String contentEncoding, long length, Header[] headers) throws IOException {
        onParse(response);
    }


    public void onParse(Reader responseReader) {
        BufferedReader reader = new BufferedReader(responseReader);
        String content = "";

        String str = "";
        try {
            while ((str = reader.readLine()) != null) {
                content += str;
            }
            LogUtils.d("============="+content);
        }catch (IOException ex) {
            LogUtils.e("====ex="+ex);
        }

        if(isParseByFastJson) {
            mResponseData = (T)content;
        }
    }


}
