package nickgao.com.meiyousample.skin;

import org.json.JSONObject;

import nickgao.com.framework.utils.StringUtils;

/**
 * 表情Model
 * Created by hyx on 2015/7/20.
 */
public class ExpressionModel extends DecorationModel {
    public int original_price;
    public String package_url;
    public double package_size;
    public int discount_price;
    public String large_img;
    public int is_default;
    public String tag_icon;
    public String tradition_introduction;
    public String updated_at;
    public String name;
    public int id;
    public String introduction;
    public String tradition_name;
    public String long_img;
    public String small_img;

    public ExpressionModel(){

    }

    public ExpressionModel(JSONObject jsonObject){
        id = StringUtils.getJsonInt(jsonObject, "id");
        skinId = StringUtils.getJsonInt(jsonObject, "id");
        name = StringUtils.getJsonString(jsonObject, "name");
        tradition_name = StringUtils.getJsonString(jsonObject, "tradition_name");
        long_img = StringUtils.getJsonString(jsonObject, "long_img");
        large_img = StringUtils.getJsonString(jsonObject, "large_img");
        is_default = StringUtils.getJsonInt(jsonObject, "is_default");
        introduction = StringUtils.getJsonString(jsonObject, "introduction");
        tradition_introduction = StringUtils.getJsonString(jsonObject, "tradition_introduction");
        original_price = StringUtils.getJsonInt(jsonObject, "original_price");
        discount_price = StringUtils.getJsonInt(jsonObject, "discount_price");
        package_url = StringUtils.getJsonString(jsonObject, "package_url");
        updated_at = StringUtils.getJsonString(jsonObject, "updated_at");
        package_size = StringUtils.getJsonDouble(jsonObject, "package_size");
        is_exchanged = StringUtils.getJsonBoolean(jsonObject, "is_exchanged");
//        is_prize = StringUtils.getJsonBoolean(jsonObject, "is_prize");
        tag_icon = StringUtils.getJsonString(jsonObject, "tag_icon");
        small_img = StringUtils.getJsonString(jsonObject, "small_img");
    }

    @Override
    public String getFileName() {
        return "expression_" + skinId + "_" + version;
    }

    @Override
    public String getTempFileName() {
        return "expression_" + skinId + "_" + version + ".tmp";
    }
}
