package nickgao.com.meiyousample.firstPage.module;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.firstPage.HomeClassifyModel;
import nickgao.com.meiyousample.firstPage.TalkModel;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class RecommendTopicResponeModel implements Serializable {
    private static final long serialVersionUID = 4654631534L;
    public int resultCode = 0;
    public int nums;

    public int page;

    public int time;

    public int news_time;

    public String banner;

    public String baby_img;

    public List<TalkModel> list = new ArrayList<TalkModel>();

    public List<Integer> top_topic_cancles = new ArrayList<>();//要删除的ids

    public List<HomeClassifyModel> classifyModelList = new ArrayList<>();//资讯分类数据源

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return resultCode >= 200 && resultCode < 400;
    }

    public RecommendTopicResponeModel() {

    }

    public RecommendTopicResponeModel(Context context, JSONObject jsonObject) {
        try {
            baby_img = StringUtils.getJsonString(jsonObject, "baby_img");
            nums = StringUtils.getJsonInt(jsonObject, "nums");
            page = StringUtils.getJsonInt(jsonObject, "page");
            time = StringUtils.getJsonInt(jsonObject, "time");
            news_time = StringUtils.getJsonInt(jsonObject, "news_time");
            //话题取消要删除的过滤的数据
            JSONArray deleteArray = StringUtils.getJsonArray(jsonObject, "top_topic_cancle");
            if (deleteArray != null) {
                int deletLen = deleteArray.length();
                for (int i = 0; i < deletLen; i++) {
                    int id = deleteArray.getInt(i);
                    top_topic_cancles.add(id);
                }
            }
            //帖子话题和资讯贴数据
            JSONArray itemArray = StringUtils.getJsonArray(jsonObject, "items");
            if (null != itemArray) {
                int len = itemArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject obj = itemArray.getJSONObject(i);
                    TalkModel talkModel = new TalkModel(obj);
                    list.add(talkModel);
                }
            }
            //资讯分类数据
            JSONArray categoryArray = StringUtils.getJsonArray(jsonObject, "category");
            if (null != categoryArray) {
                int len = categoryArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject obj = categoryArray.getJSONObject(i);
                    HomeClassifyModel homeClassifyModel = new HomeClassifyModel(obj);
                    classifyModelList.add(homeClassifyModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}