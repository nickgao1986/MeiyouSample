package nickgao.com.meiyousample.firstPage;

import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.meiyousample.utils.StringUtils;

/**
 * 首页资讯分类model
 * Created by wuminjian on 16/11/14.
 */

public class HomeClassifyModel implements Serializable {
    public int catid;
    public String name;

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect;

    public HomeClassifyModel() {

    }

    public HomeClassifyModel(JSONObject obj) {
        try {
            if (obj == null)
                return;
            this.catid = StringUtils.getJsonInt(obj, "catid");
            this.name = StringUtils.getJsonString(obj, "name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
