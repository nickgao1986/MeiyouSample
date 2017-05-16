package nickgao.com.meiyousample.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;


// 搜索首页数据
public class SearchCircleHomeModel implements Serializable {

    //经期
    public SearchCircleHomeItemModel mode_jq;

    //孕早
    public SearchCircleHomeItemModel mode_ye;

    //孕中
    public SearchCircleHomeItemModel mode_ym;

    //孕晚
    public SearchCircleHomeItemModel mode_yl;

    //备孕
    public SearchCircleHomeItemModel mode_by;

    //辣妈 暂时没用
    public SearchCircleHomeItemModel mode_lm;

    // 新生儿妈咪
    public SearchCircleHomeItemModel mode_newborn;

    // 婴儿期妈咪
    public SearchCircleHomeItemModel mode_baby;

    // 幼儿期妈咪
    public SearchCircleHomeItemModel mode_child;


    public SearchCircleHomeModel(JSONObject object) {
        try {
            if (object.has("mode_jq")) {
                mode_jq = new SearchCircleHomeItemModel(object.getJSONObject("mode_jq"));
            }
            if (object.has("mode_ye")) {
                mode_ye = new SearchCircleHomeItemModel(object.getJSONObject("mode_ye"));
            }
            if (object.has("mode_ym")) {
                mode_ym = new SearchCircleHomeItemModel(object.getJSONObject("mode_ym"));
            }
            if (object.has("mode_yl")) {
                mode_yl = new SearchCircleHomeItemModel(object.getJSONObject("mode_yl"));
            }
            if (object.has("mode_by")) {
                mode_by = new SearchCircleHomeItemModel(object.getJSONObject("mode_by"));
            }
            if (object.has("mode_lm")) {
                mode_lm = new SearchCircleHomeItemModel(object.getJSONObject("mode_lm"));
            }
            if (object.has("mode_newborn")) {
                mode_newborn = new SearchCircleHomeItemModel(object.getJSONObject("mode_newborn"));
            }
            if (object.has("mode_baby")) {
                mode_baby = new SearchCircleHomeItemModel(object.getJSONObject("mode_baby"));
            }
            if (object.has("mode_child")) {
                mode_child = new SearchCircleHomeItemModel(object.getJSONObject("mode_child"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * mode_jq : {"body":[{"title":"经期","data":["痛经","月经推迟","月经量少","黄体酮","安全期","月经不调","经期保养"],"type":0},{}],"word":"痛经怎么办"}
     */
    public static class SearchCircleHomeItemModel implements Serializable {
        /**
         * body : [{"title":"经期","data":["痛经","月经推迟","月经量少","黄体酮","安全期","月经不调","经期保养"],"type":0},{}]
         * word : 痛经怎么办
         */
        public List<SearchCircleHomeItemBodyModel> body;
        public String word;

        public SearchCircleHomeItemModel(JSONObject jsonObject){
            word = StringUtils.getJsonString(jsonObject, "word");

            try {
                JSONArray array = jsonObject.getJSONArray("body");

                if(array != null && array.length() > 0){
                    body = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        SearchCircleHomeItemBodyModel model = new SearchCircleHomeItemBodyModel(array.getJSONObject(j));
                        body.add(model);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public static class SearchCircleHomeItemBodyModel implements Serializable {
            /**
             * title : 经期
             * data : ["痛经","月经推迟","月经量少","黄体酮","安全期","月经不调","经期保养"]
             * type : 0
             */
            public String title;
            // public List<SearchCircleHomeItemBodyDataModel> data;
            public List<String> keyList;
            public List<Integer> idList;
            public int type;

            public SearchCircleHomeItemBodyModel(List<String> keyList,List<Integer> idList,String title,int type) {
                this.keyList = keyList;
                this.idList = idList;
                this.type = type;
                this.title = title;
                this.type = type;
            }

            public SearchCircleHomeItemBodyModel(JSONObject jsonObject) {
                title = StringUtils.getJsonString(jsonObject, "title");
                type = StringUtils.getJsonInt(jsonObject, "type");

                try {
                    JSONArray array = jsonObject.getJSONArray("data");
                    if(array != null && array.length() > 0){
                        keyList = new ArrayList<>();
                        idList = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            SearchCircleHomeItemBodyDataModel model = new SearchCircleHomeItemBodyDataModel(array.getJSONObject(j));
                            keyList.add(model.word);
                            idList.add(model.id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public static class SearchCircleHomeItemBodyDataModel implements Serializable {
                public String word;
                public int id;

                public SearchCircleHomeItemBodyDataModel(JSONObject jsonObject) {
                    word = StringUtils.getJsonString(jsonObject, "word");
                    id = StringUtils.getJsonInt(jsonObject, "id");
                }

            }
        }
    }

}
