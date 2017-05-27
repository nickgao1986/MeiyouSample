package com.meiyou.framework.share;


import com.umeng.socialize.SocializeException;

/**
 * Created by hxd on 15/7/30.
 */
public class AuthException extends SocializeException {

    public AuthException(int i, String s) {
        super(i, s);
    }

    public AuthException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AuthException(String s) {
        super(s);
    }

}
