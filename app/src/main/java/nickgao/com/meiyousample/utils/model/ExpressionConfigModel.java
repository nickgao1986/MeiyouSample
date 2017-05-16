package nickgao.com.meiyousample.utils.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

/**
 * 表情配置model
 * Created by lwh on 2015/7/15.
 */
public class ExpressionConfigModel implements Serializable {
    /**
     * [
     * {
     * "id": 1,                                   //表情包id
     * "name": "冰雪奇缘",                       //表情包id
     * "package_name": "com.seeyouyima.com_kkk",  //表情包的英文字符
     * "host": "http://sc.seeyouyimc.com",           //表情包id
     * "expression_dir": "exp_1",                  //表情路径
     * "configs": [
     * {
     * "name": "微笑",           //名称： 微笑
     * "file_name": "spy_06.gif",    //文件名：smile.jpg
     * }
     * ]
     * }
     * ]
     */


    public int id;
    public String name;
    public String package_name;
    public String host;
    public String expression_dir;
    public String name_image;
    public List<ExpressionSubModel> listConfig = new ArrayList<>();

    public ExpressionConfigModel() {

    }

    public String getGifUrl(String gif_name){
        return host+"/"+expression_dir+"/"+gif_name;
    }

    public ExpressionConfigModel(JSONObject jsonObject) {
        try {
            id = StringUtils.getJsonInt(jsonObject, "id");
            name = StringUtils.getJsonString(jsonObject, "name");
            package_name = StringUtils.getJsonString(jsonObject, "package_name");
            host = StringUtils.getJsonString(jsonObject, "host");
            name_image= StringUtils.getJsonString(jsonObject, "name_image");
            expression_dir = StringUtils.getJsonString(jsonObject, "expression_dir");
            listConfig.clear();
            if (jsonObject.has("configs")) {
                JSONArray jsonArray = jsonObject.getJSONArray("configs");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ExpressionSubModel model = new ExpressionSubModel(jsonObject1);
                        listConfig.add(model);
                    }
                }
            }
            id = StringUtils.getJsonInt(jsonObject, "id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取该表情包的表情数目
     *
     * @return
     */
    public int getFaceSize() {
        return listConfig.size();
    }
}
