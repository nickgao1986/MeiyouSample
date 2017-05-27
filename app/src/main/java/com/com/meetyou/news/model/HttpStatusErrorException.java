package com.com.meetyou.news.model;

/**
 * 服务器端返回错误状态异常
 * Created by LinXin on 2017/3/15.
 */
public class HttpStatusErrorException extends Exception {

    private int code;

    public HttpStatusErrorException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
