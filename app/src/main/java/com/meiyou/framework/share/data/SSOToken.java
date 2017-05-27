/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meiyou.framework.share.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.meiyou.framework.share.ShareType;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.LogUtils;

//TODO DELETE
public class SSOToken implements Serializable {

    private SharedPreferences sp;
    //for seeyouClient
    private int mType;

    public static final int WX_LOGIN = 3;

    public static final int SINA_LOGIN = 2;

    public static final int QZONE_LOGIN = 1;

    public static final int LOGIN_ERROR = -1;


    public SSOToken() {
    }

    public SSOToken(Context context, ShareType shareType) {
        try {
            sp = context.getSharedPreferences("saver", 0);
            this.mType = covert2Type(shareType);

            setAuthVerifier(sp.getString("s" + mType, null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static ShareType convert2ShareType(int type) {
        ShareType shareType = null;
        switch (type) {
            case WX_LOGIN:
                shareType = ShareType.WX_CIRCLES;
                break;
            case SINA_LOGIN:
                shareType = ShareType.SINA;
                break;
            case QZONE_LOGIN:
                shareType = ShareType.QQ_ZONE;
                break;

        }
        return shareType;
    }

    public static int covert2Type(ShareType shareType) {
        int type = LOGIN_ERROR;
        switch (shareType) {
            case SINA:
                type = SINA_LOGIN;
                break;
            case QQ_ZONE:
            case QQ_FRIENDS:
                type = QZONE_LOGIN;
                break;
            case WX_CIRCLES:
            case WX_FRIENDS:
                type = WX_LOGIN;
            default:
                type = LOGIN_ERROR;
        }
        return type;
    }

    private String token;
    private long expiresIn;
    private String openId;
    private String authVerifier;
    private String sessionKey;
    private String sessionSecret;

    private String name = "";
    private String uid;

    public int getType() {
        return this.mType;
    }

    public String getUid() {
        return uid == null ? "" : uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        if (expiresIn != null && !expiresIn.equals(""))
            this.expiresIn = Long.parseLong(expiresIn);
    }

    public String getAuthVerifier() {
        return authVerifier;
    }

    public void setAuthVerifier(String authVerifier) {
        if (authVerifier != null) {
            this.authVerifier = authVerifier;
            List<BasicNameValuePair> oauth = getQueryParamsList(authVerifier);
            for (NameValuePair nameValuePair : oauth) {
                if (nameValuePair.getName().equals("access_token")) {
                    LogUtils.d("aaaa: access_token： " + nameValuePair.getValue());
                    setToken(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("expires_in")) {
                    LogUtils.d("aaaa: expires_in： " + nameValuePair.getValue());
                    setExpiresIn(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("openid")) {
                    setOpenId(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("session_key")) {
                    setSessionKey(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("session_secret")) {
                    setSessionSecret(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("name")) {
                    setName(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("uid")) {
                    setUid(nameValuePair.getValue());
                    LogUtils.d("aaaa: uid： " + nameValuePair.getValue());
                }
            }
        }
    }

    public boolean isValiable() {
        try {
            if (this.token != null || this.sessionKey != null) {
                long lastTime = sp
                        .getLong("time" + mType, System.currentTimeMillis());
                long ex = this.expiresIn * 1000;
                if (System.currentTimeMillis() - lastTime < ex
                    // || getType() == ThirdPartyLoginWatcher.QZONE_LOGIN
                        ) {
                    return true;
                }
            }
            // clear();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 保存登录信息
     */
    public void save() {
        sp.edit()
                .putString("s" + mType, authVerifier).commit();
        sp.edit()
                .putLong("time" + mType, System.currentTimeMillis()).commit();
    }

    public void clear() {
        sp.edit()
                .putString("s" + mType, null).commit();
        sp.edit()
                .putLong("time" + mType, 0l).commit();
    }

    /**
     * 添加名字
     *
     * @param name
     */
    void addName(String name) {
        String o = sp.getString(
                "s" + mType, null);
        List<BasicNameValuePair> array = getQueryParamsList(o);
        array.add(new BasicNameValuePair("name", name));
        String oauthString = getQueryString(array);
        setAuthVerifier(oauthString);
        save();
    }

    public static String getQueryString(List<BasicNameValuePair> QueryParamsList) {
        StringBuilder queryString = new StringBuilder();
        for (NameValuePair param : QueryParamsList) {
            queryString.append('&');
            queryString.append(param.getName());
            queryString.append('=');
            queryString.append(Uri.encode(param.getValue()));
        }
        // 去掉第一个&号
        return queryString.toString().substring(1);
    }

    /**
     * 分割queryString，取得List&#60NameValuePair&#62格式存储的参数队列.
     *
     * @param queryString 查询字符串
     * @return 以List&#60NameValuePair&#62格式存储的参数队列.
     */
    public static List<BasicNameValuePair> getQueryParamsList(String queryString) {

        List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
        try {
            if (queryString.contains("allback(")) {// QQ空间授权码转化
                queryString = queryString.replaceFirst(
                        "[\\s\\S]*allback\\(([\\s\\S]*)\\);[^\\)]*\\z", "$1");
                queryString = queryString.trim();
            }
            JSONObject obj = new JSONObject(queryString);

            if (obj.has("renren_token")) {
                // 服务器返回的过期时间单位为秒，故乘以1000
                long expires = obj.getJSONObject("renren_token").getLong(
                        "expires_in") * 1000;
                result.add(new BasicNameValuePair("session_key", obj
                        .getJSONObject("renren_token").getString("session_key")));
                result.add(new BasicNameValuePair("session_secret", obj
                        .getJSONObject("renren_token").getString(
                                "session_secret")));
                result.add(new BasicNameValuePair("expires_in", expires + ""));
                result.add(new BasicNameValuePair("uid", obj.getJSONObject(
                        "user").getString("id")));
            }

            if (obj.has("uid")) {
                result.add(new BasicNameValuePair("uid", obj.getString("uid")));
            }
            if (obj.has("userName")) {
                result.add(new BasicNameValuePair("name", obj
                        .getString("userName")));
            }
            if (obj.has("expires_in")) {
                result.add(new BasicNameValuePair("expires_in", obj
                        .getString("expires_in")));
            }
            if (obj.has("remind_in")) {
                result.add(new BasicNameValuePair("remind_in", obj
                        .getString("remind_in")));
            }
            if (obj.has("access_token")) {
                result.add(new BasicNameValuePair("access_token", obj
                        .getString("access_token")));
            }
            if (obj.has("openid")) {
                result.add(new BasicNameValuePair("openid", obj
                        .getString("openid")));
            }
        } catch (JSONException e) {
            if (queryString.startsWith("?")) {
                queryString = queryString.substring(1);
            }

            if (queryString != null && !queryString.equals("")) {
                String[] p = queryString.split("&");
                for (String s : p) {
                    if (s != null && !s.equals("")) {
                        if (s.indexOf('=') > -1) {
                            String[] temp = s.split("=");
                            if (temp.length > 1) {
                                result.add(new BasicNameValuePair(temp[0],
                                        Uri.decode((temp[1]))));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
