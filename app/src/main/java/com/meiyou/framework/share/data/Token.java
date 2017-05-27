package com.meiyou.framework.share.data;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

public class Token implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6941941815596512584L;
    private Context mContext;

    public Token() {

    }

    public Token(Context context, int type) {
        try {
            this.mContext = context;
            this.mType = type;
            setOauth_verifier(mContext.getSharedPreferences("saver", 0).getString("s" + mType, null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int mType;
    public String token;
    public long expiresIn;
    public String oauth_verifier;
    public String name = "";
    public String uid;
    public int gender = 0;//性别 0 为默认女  1为男
    public int province;//省
    public int city;//城市
    public String unionid;//微信专用

    public void setExpiresIn(String expiresIn) {
        if (expiresIn != null && !expiresIn.equals(""))
            this.expiresIn = Long.parseLong(expiresIn);
    }

    public String getToken() {
        return token;
    }

    public int getType() {
        return mType;
    }

    public static List<BasicNameValuePair> getQueryParamsList(String queryString) {

        List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
        try {
            JSONObject obj = new JSONObject(queryString);
            if (obj.has("uid")) {
                result.add(new BasicNameValuePair("uid", obj.getString("uid")));
            }
            if (obj.has("name")) {
                result.add(new BasicNameValuePair("name", obj
                        .getString("name")));
            }
            if (obj.has("expires_in")) {
                result.add(new BasicNameValuePair("expires_in", obj
                        .getString("expires_in")));
            }
            if (obj.has("access_token")) {
                result.add(new BasicNameValuePair("access_token", obj
                        .getString("access_token")));
            }
            if (obj.has("gender")) {
                result.add(new BasicNameValuePair("gender", obj
                        .getString("gender")));
            }
            if (obj.has("unionid")) {
                result.add(new BasicNameValuePair("unionid", obj.getString("unionid")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setOauth_verifier(String oauth_verifier) {
        if (!StringUtils.isNull(oauth_verifier)) {
            this.oauth_verifier = oauth_verifier;
            List<BasicNameValuePair> oauth = getQueryParamsList(oauth_verifier);
            for (NameValuePair nameValuePair : oauth) {
                if (nameValuePair.getName().equals("access_token")) {
                    this.token = nameValuePair.getValue();
                } else if (nameValuePair.getName().equals("expires_in")) {
                    setExpiresIn(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("uid")) {
                    this.uid = nameValuePair.getValue();
                } else if (nameValuePair.getName().equals("name")) {
                    this.name = nameValuePair.getValue();
                } else if (nameValuePair.getName().equals("gender")) {
                    this.gender = Integer.valueOf(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("province")) {
                    this.province = Integer.valueOf(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("city")) {
                    this.city = Integer.valueOf(nameValuePair.getValue());
                } else if (nameValuePair.getName().equals("unionid")) {
                    this.unionid = nameValuePair.getValue();
                }
            }
        }
    }

    /**
     * 保存登录信息
     */
    public void save() {
        mContext.getSharedPreferences("saver", 0).edit().putString("s" + mType, oauth_verifier).commit();
        mContext.getSharedPreferences("saver", 0).edit().putLong("time" + mType, System.currentTimeMillis()).commit();
    }

    public void clear() {
        mContext.getSharedPreferences("saver", 0).edit().putString("s" + mType, null).commit();
        mContext.getSharedPreferences("saver", 0).edit().putLong("time" + mType, 0l).commit();
    }
}












