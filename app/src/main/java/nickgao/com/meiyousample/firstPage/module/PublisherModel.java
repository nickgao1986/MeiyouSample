package nickgao.com.meiyousample.firstPage.module;

import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.framework.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class PublisherModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public String screen_name;
    public int isvip = 0;
    public String avatar;

    public PublisherModel() {
    }

    public PublisherModel(JSONObject jsonObject) {
        this.id = StringUtils.getJsonInt(jsonObject, "id");
        this.screen_name = StringUtils.getJsonString(jsonObject, "screen_name");
        this.isvip = StringUtils.getJsonInt(jsonObject, "isvip");
        this.avatar = StringUtils.getJsonString(jsonObject, "avatar");
    }
}
