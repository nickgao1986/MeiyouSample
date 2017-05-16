package nickgao.com.meiyousample.mypage.module;

import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.framework.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class FamilyModel implements Serializable {
    public String app_name;
    public String app_logo;
    public String download_url;
    public String package_name;

    public FamilyModel(JSONObject obj) {
        try {
            app_name = StringUtils.getJsonString(obj, "app_name");
            app_logo = StringUtils.getJsonString(obj, "app_logo");
            download_url = StringUtils.getJsonString(obj, "download_url");
            package_name = StringUtils.getJsonString(obj, "package_name");
        } catch (Exception e) {
        }
    }
}
