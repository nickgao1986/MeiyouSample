package nickgao.com.meiyousample.utils.model;


import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.framework.utils.StringUtils;

/**
 * 单个表情
 * Created by lwh on 2015/7/29.
 */
public class ExpressionSubModel implements Serializable {
    public String name;
    public String gif_name="";
    public ExpressionSubModel(JSONObject jsonObject) {
        name = StringUtils.getJsonString(jsonObject, "name");
        gif_name = StringUtils.getJsonString(jsonObject, "file_name");
    }

    public ExpressionSubModel() {

    }
}
