package nickgao.com.okhttpexample.fresco;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public class RequestParams {

    public static final int TYPE_STRING = 0;
    public static final int TYPE_JSON = 1;
    public static final int TYPE_JSON_ARRAY = 2;
    public static final int TYPE_FILE = 3;
    public static final String KEY_NOT_ENCODE_PARAMS = "not-encode-params"; //不要encode params 默认不传该参数,是encode的

    public static final String VALUE_NOT_ENCODE_PARAMS = "true";// 特殊场景可以传入该参数

    protected Map<String, String> params = new HashMap<>();
    protected boolean canCache = true;
    //丑陋的url拼接,是否将post的参数拼接到url上,这个是服务端要求的
    protected boolean uglyUrlPatch = true;
    //// FIXME: 17/2/3  怎么是返回null  by zxb
    public String getContent() {
        return null;
    }

    /**
     * 用户Get 拼接URL
     * @param params 普通请求方式 仅仅放入 业务请求参数
     */
    public RequestParams(Map<String, String> params) {
        if (params != null) {
            this.params.putAll(params);
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    public int getType() {
        return TYPE_STRING;
    }


    public boolean isCanCache() {
        return canCache;
    }

    public RequestParams setCanCache(boolean canCache) {
        this.canCache = canCache;
        return this;
    }

    /**
     * 默认是ture
     * @return this
     */
    public boolean isUglyUrlPatch() {
        return uglyUrlPatch;
    }

    public RequestParams setUglyUrlPatch(boolean uglyUrlPatch) {
        this.uglyUrlPatch = uglyUrlPatch;
        return this;
    }

}
