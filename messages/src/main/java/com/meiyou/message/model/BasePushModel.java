package com.meiyou.message.model;

import java.io.Serializable;

/**
 * Created by hxd on 15/7/23.
 */
public class BasePushModel implements Serializable {

    public String jsonString;
    public String jsonStringBase64;

    public BasePushModel(String json, String jsonStringBase64) {
        this.jsonString = json;
        this.jsonStringBase64 = jsonStringBase64;
    }

    public String getJsonString() {
        return jsonString;
    }

    public String getJsonStringBase64() {
        return jsonStringBase64;
    }
}
