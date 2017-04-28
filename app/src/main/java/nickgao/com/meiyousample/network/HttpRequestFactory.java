package nickgao.com.meiyousample.network;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;

import nickgao.com.meiyousample.utils.LogUtils;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class HttpRequestFactory {

    private static volatile ExecutorService sExecutorService = Executors.newCachedThreadPool();
    private static final String LOG_TAG = "[RC]HttpUtils";
    private static final AtomicLong sFactoryId = new AtomicLong(1);

    private static final AtomicLong sThreads = new AtomicLong(0);
    private static final AtomicLong sMakeRequests = new AtomicLong(0);
    private static final AtomicLong sRequests = new AtomicLong(0);
    private static final AtomicLong sLockNumber = new AtomicLong(0);

    private static final int MAX_BODY_LENGTH_FOR_LOGGING = 10 * 1024;
    private static final String AUTHENTICATION = "XDS 3.y3XoQIQaP+UgRMDq6/Q/8v9qEguoU7gNbujmOVNMeg8=";
    /**
     * Keeps current authorization context.
     */
    private volatile AuthContext mAuthContext;
    /**
     * Keeps factoryId of the session.
     */
    private volatile long mFactoryId;

    private volatile RestHttpClient mHttpClient;


    public HttpRequestFactory() {
        mFactoryId = sFactoryId.get();
        AuthContext authContext = new AuthContext();
        authContext.accessToken = AUTHENTICATION;
        authContext.authId = getSequenceNumber();

        if (mHttpClient == null) {
            mHttpClient = RestHttpClient.getClient();
            if (mHttpClient == null) {
                UNLOCK("authorize:http_client");
                LogUtils.w(LOG_TAG, ".authorize: invalid HTTP client.");
                return;
            }
        }


        mAuthContext = authContext;

    }

    /**
     * Authorization context.
     */
    static class AuthContext {
        String host;
        String accessToken;
        long authId = 0;
    }

    /**
     * Returns host.
     *
     * @param ctx the execution context.
     *
     * @return host URI
     */
    private static String getHost(Context ctx) {
        return "http://test.friends.seeyouyima.com";
    }

    /**
     * Common sync-primitive.
     */
    private static final ReentrantLock Lock = new ReentrantLock();
    /**
     * Keeps unique identifiers generator.
     */
    private static final AtomicLong sSequenceNumber = new AtomicLong(1);


    public boolean sendRequest(final RestRequest request) {
        request.turnedToState(RestRequest.InternalState.PENDING_EXECUTION);
        sRequests.getAndIncrement();
        postRequestCommand(getRequestContext(request));
        return true;
    }

    private static void postRequestCommand(final RequestContext requestCtx) {
        try {
            LogUtils.d(LOG_TAG, ".postRequestCommand : " + requestCtx.request.mLogTag);
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    sThreads.incrementAndGet();
                    try {
                        requestCommand(requestCtx);
                    } finally {
                        sThreads.decrementAndGet();
                    }
                }
            });
        } catch (Throwable th) {
            LogUtils.e(LOG_TAG, "postRequestCommand : exception : " + th.toString());
        }
    }

    /**
     * Acquires the common lock.
     */
    private static void LOCK() {
        sLockNumber.getAndIncrement();
        Lock.lock();
    }

    /**
     * Attempts to release common lock.
     */
    private static void UNLOCK(String msg) {
        sLockNumber.getAndDecrement();
        try {
            Lock.unlock();
        } catch (IllegalMonitorStateException imsex) {
            LogUtils.e(LOG_TAG, msg + ": UNLOCK : unexpected exception : " + imsex.toString());
        }
    }


    private RequestContext getRequestContext(RestRequest request) {
        RequestContext requestCtx = new RequestContext();
        requestCtx.authId      = mAuthContext.authId;
        requestCtx.accessToken = mAuthContext.accessToken;
        requestCtx.host        = mAuthContext.host;
        requestCtx.httpClient  = mHttpClient;
        requestCtx.request     = request;
        requestCtx.mFactoryId  = mFactoryId;
        return requestCtx;
    }


    private static void requestCommand(final RequestContext requestCtx) {
        if (requestCtx == null) {
            LogUtils.e(LOG_TAG, "requestCommand : request is null.");
            return;
        }

        if (requestCtx.mFactoryId != sFactoryId.get()) {
            LogUtils.w(LOG_TAG, "requestCommand : omitted, invalid factory.");
            sRequests.getAndDecrement();
            requestToCompletion(requestCtx.request, RestApiErrorCodes.INVALID_SESSION_STATE, true);
            return;
        }

        try {
            int maxTries = 0;
            boolean loop = true;
            while(loop){
                //Check network
//                int netStatus = getNetworkState();
//                if(netStatus != RestApiErrorCodes.OK){
//                    LogUtils.d(LOG_TAG, "requestCommand: invalid network state,break loop.");
//                    break;
//                }
                //SYNC,execute command.
                makeRequest(requestCtx);

                final RestRequest request = requestCtx.request;
                int httpCode = request.mHttpCode;

                LogUtils.d(LOG_TAG, "requestCommand :loop current http code ="+httpCode
                        + " current tries="+maxTries);
                //handle error.
                switch(httpCode){
                    case RestApiErrorCodes.OK: {
                        //still,we need to check reponse is ok
                        if(requestCtx.request.getResult() == RestApiErrorCodes.OK){
                            loop = false;
                            LogUtils.d(LOG_TAG, "requestCommand : http status code=ok,loop=false");
                        }
                    }
                    break;
                    case RestApiErrorCodes.INVALID_REQUEST:
                    case RestApiErrorCodes.CLIENT_INVALID_ERROR:{
                        //invalidate parameter for request,just break loop.
                        LogUtils.d(LOG_TAG, "requestCommand : invalidate parameter for request,just break loop");
                        loop = false;
                    }
                    break;

                    default:{
                        LogUtils.d(LOG_TAG, "requestCommand : DO NOT repeat the request. Display error message");
                        loop = false;
                    }
                    break;
                }
            }

            //makeRequest(requestCtx);
//            sProcessor.execute(new Command("OnRequestCompletion") {
//                @Override
//                public void run() {
//                    onRequest(requestCtx);
//                }
//            });
            requestToCompletion(requestCtx.request, requestCtx.request.getResult(), true);
        } catch (Throwable th) {
            LogUtils.e(LOG_TAG, "requestCommand : exception : " + th.toString());

            try {
                requestToCompletion(requestCtx.request, RestApiErrorCodes.INVALID_REQUEST, true);
            } catch (Throwable thIn) {
            }
        }
    }


    private static void requestToCompletion(final RestRequest rest, final int result,
                                            final boolean callOnCompletion) {
        try {
//            if (result == RestApiErrorCodes.NO_ERROR) {
//                sASyncRequests.getAndIncrement();
//            } else {
//                sASyncFailedRequests.getAndIncrement();
//            }

            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    sThreads.incrementAndGet();
                    try {
                        rest.setCompletedState(result, callOnCompletion);
                    } finally {
                        sThreads.decrementAndGet();
                    }
                }
            });
        } catch (Throwable th) {
            LogUtils.e(LOG_TAG, "requestToCompletion : exception : " + th.toString());
        }
    }

    /**
     * A context for request executions.
     */
    static class RequestContext {
        long mFactoryId, mailboxId, completionTime, authId;
        String accessToken, host;
        RestRequest request;
        RestHttpClient httpClient;
    }


    private static void makeRequest(final RequestContext requestCtx) {

        sMakeRequests.getAndIncrement();

        if (requestCtx == null) {
            LogUtils.e(LOG_TAG, "makeRequest : invalid request parameter.");
            return;
        }

        RestRequest rest = requestCtx.request;
        String logTag    = rest.mLogTag;

        if (!rest.isActive()) {
            LogUtils.e(LOG_TAG, logTag + " makeRequest : request is not active");
            rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
            rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
            return;
        }

        rest.turnedToState(RestRequest.InternalState.EXECUTION);

        int executions = rest.totalExecutions.incrementAndGet();


        String path = null;
        try {
            path = rest.getURI();
        } catch (Throwable th) {
            LogUtils.e(LOG_TAG, logTag + " makeRequest : getURI : exception " + th.toString());
            rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
            rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
            return;
        }

        if (path == null) {
            try {
                path = rest.getPath();
            } catch (Throwable th) {
                LogUtils.e(LOG_TAG, logTag + " makeRequest : getPath : exception " + th.toString());
                rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
                rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
                return;
            }
        }

        if (TextUtils.isEmpty(path)) {
            LogUtils.e(LOG_TAG, logTag + " makeRequest : invalid URI");
            rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
            rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
            return;
        }

        // TODO: Validate final URI

        DefaultHttpClient client = null;
        HttpRequestBase http = null;
        HttpResponse response = null;
        ByteArrayOutputStream baos = null;
        ByteArrayEntity att_byte = null;
        try {
            boolean requestBody = false;
            switch (rest.mMethod) {
                case GET:    http = new HttpGet(path);    break;
                case POST:   http = new HttpPost(path);   requestBody = true; break;
                case DELETE: http = new HttpDelete(path); break;
                default:     http = new HttpPut(path);    requestBody = true; break;
            }

            String body = null;
            if (requestBody) {
                try {
                    body = rest.getBody();
                } catch (Throwable th) {
                    LogUtils.e(LOG_TAG, logTag + " makeRequest : getBody : exception " + th.toString());
                    rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
                    rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
                    return;
                }
            }

            //authorization
            http.addHeader("Authorization", AUTHENTICATION);

            http.addHeader("Accept",        HttpUtils.JSON_CONTENT_TYPE);

            try {
                rest.onHeaderForming(http);
            } catch (Throwable th) {
                LogUtils.e(LOG_TAG, logTag + " makeRequest : onHeaderForming : exception " + th.toString());
                rest.setResult(RestApiErrorCodes.INVALID_REQUEST);
                rest.mHttpCode = RestApiErrorCodes.CLIENT_INVALID_ERROR;
                return;
            }

            if (!TextUtils.isEmpty(body)) {
                ((HttpEntityEnclosingRequestBase)http).setEntity(new StringEntity(body));
            }

            if (rest.hasAttachment()) {
                baos = rest.getAttachment();
                if (baos != null) {
                    att_byte = new ByteArrayEntity(baos.toByteArray());

                    try {
                        if (baos != null) {
                            baos.close();
                            baos = null;
                        }
                    } catch (Throwable th) {
                    }

                    ((HttpEntityEnclosingRequestBase)http).setEntity(att_byte);
                }
            }


            client = requestCtx.httpClient.getHttpClient();

            response = client.execute(http);

            try {
                if (att_byte != null) {
                    att_byte.consumeContent();
                    att_byte = null;
                }
            } catch (Throwable th) {
            }

            rest.mHttpCode = response.getStatusLine().getStatusCode();
            requestCtx.completionTime = SystemClock.elapsedRealtime();

            if (rest.mHttpCode == HttpStatus.SC_OK) {
                //succeed
                LogUtils.d(LOG_TAG, logTag + " successful http status code : " + rest.mHttpCode);
                HttpEntity respEntity = response.getEntity();

                if (respEntity == null) {
                    LogUtils.d(LOG_TAG, logTag + "No response entity");
                    rest.setResult(RestApiErrorCodes.NO_ERROR);
                    LogUtils.d(LOG_TAG, logTag + ".Completed");
                    return;
                }

                Header h = respEntity.getContentType();
                String contentType = null;     if (h != null) {contentType = h.getValue();}
                h = respEntity.getContentEncoding();
                String contentEncoding = null; if (h != null) {contentEncoding = h.getValue();}
                long length = respEntity.getContentLength();
                ByteArrayBuffer bt= new ByteArrayBuffer(4096);

                InputStream respInpStream = null;
                try {
                    respInpStream = respEntity.getContent();
                    Reader iReader = null;

                    boolean streamedLogging = false;
                    GZIPInputStream gis = new GZIPInputStream(respInpStream);


//                    iReader = new InputStreamReader(respInpStream, "gbk");
//                    int l;
//                    byte[] tmp = new byte[4096];
//                    while ((l=gis.read(tmp))!=-1){
//                        bt.append(tmp, 0, l);
//                    }

//                    String resultString = new String(bt.toByteArray(),"utf-8");
//                    LogUtils.d(resultString);

                    iReader = new InputStreamReader(gis,"utf-8");

                    boolean onResponseError = false;
                    try {
                        rest.onResponse(iReader, respInpStream, contentType, contentEncoding, length, response.getAllHeaders());
                    } catch (Throwable iox){
                        LogUtils.e(LOG_TAG, logTag + ".onResponse exception : " + iox.toString());
                        onResponseError = true;
                        rest.setResult(RestApiErrorCodes.RESPONSE_PROCESSING_ERROR);
                    }



                    if (onResponseError) {
                        return;
                    }
                    rest.setResult(RestApiErrorCodes.NO_ERROR);
                    LogUtils.d(LOG_TAG, logTag + ".Completed");
                } catch (Throwable th) {
                    LogUtils.e(LOG_TAG, logTag + " response exception : " + th.toString(), th);
                    rest.setResult(RestApiErrorCodes.RESPONSE_PROCESSING_ERROR);
                } finally {
                    if (respInpStream != null) {
                        try {
                            respInpStream.close();
                            respInpStream = null;
                        } catch (Throwable th) {
                        }
                    }

                    if (respEntity != null) {
                        try {
                            respEntity.consumeContent();
                        } catch (Throwable th) {
                        }
                    }
                }
            } else {
                //error code.
                LogUtils.d(LOG_TAG, logTag + " response : error status code : " + rest.mHttpCode);

                rest.setResult(RestApiErrorCodes.RESPONSE_HTTP_STATUS_ERROR);
                InputStream respInpStream = null;
                HttpEntity respEntity = null;
                try {
                    respEntity = response.getEntity();
                    respInpStream = respEntity.getContent();
                    Reader iReader = new InputStreamReader(respInpStream, "UTF-8");

                    if (true) {
                        // TODO: Turn to new logging stream
                        StringBuilder sBuilder = new StringBuilder();
                        BufferedReader bReader = new BufferedReader(iReader);
                        try {
                            for (String line; (line = bReader.readLine()) != null;) {
                                sBuilder.append(line).append('\n');
                            }
                        } finally {
                            try {
                                bReader.close();
                            } catch (IOException e) {
                            }
                        }

                        LogUtils.w(LOG_TAG, logTag + "RestErrorResponse raw: " + sBuilder.toString());
                        iReader = new StringReader(sBuilder.toString());
                    }

                    rest.mRestErrorResponse = RestErrorResponse.onErrorResponse(iReader);
                    if (rest.mRestErrorResponse != null) {
                        LogUtils.e(LOG_TAG, logTag + " " + rest.mRestErrorResponse.toString());
                    } else {
                        LogUtils.e(LOG_TAG, logTag + "REST Error Response :is null");
                    }
                } catch (Throwable th) {
                    LogUtils.e(LOG_TAG, logTag + "an error in processing of REST Error Response : " + th.toString(), th);
                } finally {
                    if (respInpStream != null) {
                        try {
                            respInpStream.close();
                            respInpStream = null;
                        } catch (Throwable th) {
                        }
                    }

                    if (respEntity != null) {
                        try {
                            respEntity.consumeContent();
                        } catch (Throwable th) {
                        }
                    }
                }
            }
        } catch (OutOfMemoryError ooe) {
            LogUtils.e(LOG_TAG, logTag + " exception on request : " + ooe.toString());
            rest.setResult(RestApiErrorCodes.OUT_OF_MEMORY_ERROR);
        } catch (Exception th) {
            LogUtils.e(LOG_TAG, logTag + " exception on request : " + th.toString());
            rest.setResult(RestApiErrorCodes.CONNECTION_ERROR);
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (Throwable th) {
            }

            try {
                if (att_byte != null) {
                    att_byte.consumeContent();
                    att_byte = null;

                }
            } catch (Throwable th) {
            }

            System.gc();

            try {
                if (http != null) {
                    if (http instanceof HttpEntityEnclosingRequestBase) {
                        HttpEntity h = ((HttpEntityEnclosingRequestBase)http).getEntity();
                        if (h != null) {
                            h.consumeContent();
                        }
                    }
                }
            } catch (Throwable th) {
            }

            try {
                if (response != null) {
                    HttpEntity h = response.getEntity();
                    if (h != null) {
                        h.consumeContent();
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    /**
     * Obtain a sequence number that is unique across REST API implementation.
     */
    static long getSequenceNumber() {
        return sSequenceNumber.getAndIncrement();
    }

}
