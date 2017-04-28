package nickgao.com.meiyousample.network;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nickgao.com.meiyousample.utils.LogUtils;


/**
 * Created by steve.chen on 6/30/14.
 */
public class RcRestRequest<T> extends RestRequest {

    private static final String TAG = "RcRestRequest";

    //TODO Steve: FIX ME
    private static final boolean DEBUG = true;

    protected T mResponseData;

    protected OnRequestListener<T> mOnRequestListener;

    protected int mRequestId;

    protected String mRequestPath;

    protected String mRequestUri;

    protected Type mResponseType;

    private String strBody;

    private Object requestBody;

    private HashMap<String, String> mRequestHeader;
    private HashMap<String, String> mResponseHeader;


    public RcRestRequest(int requestId, Type responseType, HttpMethod method, String logTag) {
        super(method, logTag, LogHttp.ALL, LogHttp.ALL);
        mRequestId = requestId;
        mResponseType = responseType;
        mRequestHeader = new HashMap<String, String>();
        mResponseHeader = new HashMap<>();

    }

    public RcRestRequest(String url, HttpMethod method, Type responseType, String logTag) {
        super(method, logTag, LogHttp.ALL, LogHttp.ALL);
        mRequestPath = url;
        mResponseType = responseType;
        mRequestHeader = new HashMap<String, String>();
        mResponseHeader = new HashMap<>();

    }

    public void addHeader(String key, String value) {
        mRequestHeader.put(key, value);
    }

    public void setStrBody(String body) {
        this.strBody = body;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public void setResult(int result) {
        super.setResult(result);
    }

    public String getRequestUri() {
        return mRequestUri;
    }

    public void setRequestUri(String requestUri) {
        mRequestUri = requestUri;
    }

    public void registerOnRequestListener(OnRequestListener<T> onRequestListener) {
        this.mOnRequestListener = onRequestListener;
    }

    public void unregisterOnRequestListener(OnRequestListener<T> onRequestListener) {
        if (this.mOnRequestListener == onRequestListener) {
            this.mOnRequestListener = null;
        }
    }

    public T getResponseData() {
        return mResponseData;
    }

    public boolean executeRequest(Context context, Object... args) {
        HttpRequestFactory factory = new HttpRequestFactory();
        return execute(factory);
    }


    private boolean execute(HttpRequestFactory session) {
        boolean result = false;

        if (session != null) {
            result = session.sendRequest(this);
            if (!result) {
                int errorCode = this.getResult();
                LogUtils.e(this.mLogTag, this.getPath() + " failed: error " + errorCode + ": " + errorCode);
            }
        } else {
            LogUtils.e(this.mLogTag, this.getPath() + " failed: error " + RestApiErrorCodes.INVALID_SESSION_STATE + " Invalid Session State");
        }

        if (!result) {
            if (this.mOnRequestListener != null) {
                this.mOnRequestListener.onFail(this, this.getResult());
            }
        }
        return result;
    }


    public void initRequestPath(Context context, String url) {
        mRequestPath = url;
    }

    public HashMap<String, String> getResponseHeader() {
        return mResponseHeader;
    }


    @Override
    public String getPath() {
        return mRequestPath;
    }

    @Override
    public String getURI() {
        return this.mRequestUri;
    }


    @Override
    public String getBody() {
//        if (TextUtils.isEmpty(strBody) && this.requestBody != null) {
//            strBody = parse2Json(this.requestBody);
//        }
        return strBody;
    }

    @Override
    public void onResponse(Reader response, InputStream responseStream, String contentType, String contentEncoding, long length, Header[] headers) throws IOException {
        if(headers != null) {
            for(Header header : headers) {
                if(header != null) {
                    String name = header.getName();
                    String value = header.getValue();
                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                        mResponseHeader.put(name, value);
                    }
                }
            }
        }
       // printString(response);
        onParse(response);

    }

    private void printString(Reader responseReader) {
        BufferedReader reader = new BufferedReader(responseReader);
        String content = "";

        String str = "";
        try {
            while ((str = reader.readLine()) != null) {
                content += str;
            }

            LogUtils.d(content);
        }catch (IOException ex) {
            LogUtils.e("====ex="+ex);
        }
    }

//    protected AbstractResponse parserResponse(Reader responseReader, Type type) {
//        AbstractResponse response = null;
//        try {
//            Gson gson = new Gson();
//            gson.fromJson(responseReader, type);
//        } catch (Exception e) {
//            MktLog.e(TAG, "parserResponse", e);
//        }
//
//        return response;
//    }

//    private String parse2Json(Object object) {
//        String value = null;
//        try {
//            Gson gson = new Gson();
//            value = gson.toJson(object);
//        } catch (Exception e) {
//            MktLog.e(TAG, "parse2Json", e);
//        }
//        return value;
//    }
//
    protected void onParse(Reader response) {
        try {
            Gson gson = new Gson();
            mResponseData = gson.fromJson(response, mResponseType);
        } catch (Exception e) {
            LogUtils.e(TAG, "onParse", e);
        }

        LogUtils.d(""+mResponseData);
    }

    @Override
    public void onHeaderForming(HttpMessage request) {
        super.onHeaderForming(request);
        Iterator iterator = mRequestHeader.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            request.addHeader(key, val);
        }
        //request.addHeader(HttpUtils.CONTENT_TYPE_NAME, HttpUtils.MIXED_CONTENT_TYPE + "; boundary=" + BOUDNARY);
    }

    @Override
    public final void onCompletion() {

        if (this.mOnRequestListener != null) {
            if (getResult() == RestApiErrorCodes.NO_ERROR) {
                this.mOnRequestListener.onSuccess(this, this.mResponseData);
            } else {
                this.mOnRequestListener.onFail(this, this.getResult());
            }

            this.mOnRequestListener.onComplete(this);
        }
    }

    public interface OnRequestListener<T> {

        void onSuccess(RcRestRequest<T> request, T response);

        void onFail(RcRestRequest<T> request, int errorCode);

        void onComplete(RcRestRequest<T> request);
    }
}
