package nickgao.com.meiyousample.skin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class SkinModel extends DecorationModel {

    public String img_url;//图片地址
    public List<String> imgs = new ArrayList<String>();//预览图片地址
    public String skinName;//皮肤名字
    public String skinContent;//皮肤简介
    public String skinPackageName;//皮肤包名
    public String content;//皮肤说明内容
    public int skinCoin;//柚币数
    public float fileSize;//文件长度
    public boolean isDownloadPic;//是否下载过图片
    public int skin_type = -1;//0 表示默认皮肤
    public int mUcoinCount = 0;
    public boolean is_recomm;//是否是推荐
    public boolean is_host_tag;//是否热门
    public int page;//分页页数
    public int original_price;//原价
    public int label;//等于5是new的标志
    public String tag_icon; //左上角图标



    /**
     * 解析json数据
     * 平常的解析
     *
     * @param obj
     */
    public SkinModel(JSONObject obj, int page) {
        this.page = page;
        skinId = StringUtils.getJsonInt(obj, "id");
        skinName = StringUtils.getJsonString(obj, "name");
        img_url = StringUtils.getJsonString(obj, "images");

        if (obj.has("preview")) {//预览图
            try {
                JSONArray array = obj.getJSONArray("preview");
                for (int i = 0; i < array.length(); i++) {
                    String pic = array.get(i).toString();
                    imgs.add(pic);
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }
        version = StringUtils.getJsonInt(obj, "version");
        skinCoin = StringUtils.getJsonInt(obj, "task_count");
        skinContent = StringUtils.getJsonString(obj, "description");
        downLoadPath = StringUtils.getJsonString(obj, "download_url");
        String size = StringUtils.getJsonString(obj, "file_size");
        fileSize = StringUtils.getFloat(size);
        is_exchanged = StringUtils.getJsonBoolean(obj, "is_exchanged");
        tag_icon = StringUtils.getJsonString(obj, "tag_icon");
        skinPackageName = StringUtils.getJsonString(obj, "package");
        original_price = StringUtils.getJsonInt(obj, "original_price");
        label = StringUtils.getJsonInt(obj, "label");
        is_prize = StringUtils.getJsonBoolean(obj, "is_prize");

        if (is_prize || is_exchanged) {
            updateStastus = SkinDownloadType.SKIN_INIT;
        }
        if (skinCoin == 0) {
            updateStastus = SkinDownloadType.SKIN_INIT;
        }
        skin_type = StringUtils.getJsonInt(obj, "skin_type");//0表示默认皮肤
        if (skin_type == 0) {
            updateStastus = SkinDownloadType.SKIN_APPLY;
            skinPackageName = "";
        }
        mUcoinCount = StringUtils.getJsonInt(obj, "my_coin");

        is_recomm = StringUtils.getJsonBoolean(obj, "is_recomm");//是否推荐
        is_host_tag = StringUtils.getJsonBoolean(obj, "is_host_tag");//是否热门

    }

    /**
     * 解析json数据
     * 我的皮肤，已兑换过的皮肤；列表解析
     * @param obj
     */
    public SkinModel(JSONObject obj, boolean isExchanged) {
        skinId = StringUtils.getJsonInt(obj, "id");
        skinName = StringUtils.getJsonString(obj, "name");
        img_url = StringUtils.getJsonString(obj, "images");

        if (obj.has("preview")) {//预览图
            try {
                JSONArray array = obj.getJSONArray("preview");
                for (int i = 0; i < array.length(); i++) {
                    String pic = array.get(i).toString();
                    imgs.add(pic);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        version = StringUtils.getJsonInt(obj, "version");
        skinCoin = StringUtils.getJsonInt(obj, "task_count");
        skinContent = StringUtils.getJsonString(obj, "description");
        downLoadPath = StringUtils.getJsonString(obj, "download_url");
        String size = StringUtils.getJsonString(obj, "file_size");
        fileSize = StringUtils.getFloat(size);
        is_prize = StringUtils.getJsonBoolean(obj, "is_prize");
        is_exchanged = true;//因为这边都是兑换过的皮肤 所以都是兑换的
        skinPackageName = StringUtils.getJsonString(obj, "package");
        updateStastus = SkinDownloadType.SKIN_INIT;
        skin_type = StringUtils.getJsonInt(obj, "skin_type");//0表示默认皮肤
        if (skin_type == 0) {
            updateStastus = SkinDownloadType.SKIN_APPLY;
            skinPackageName = "";
        }
    }

    public SkinModel() {

    }

    public String getFileName() {
        return skinId + "_" + version;//以皮肤的id编号加上版本号  命名
    }

    public String getTempFileName() {
        return skinId + "_" + version + ".tmp";//临时文件名，以皮肤的id编号加上版本号  命名
    }

    public void setFileName(String fileName) {
        try {
            String[] strings = fileName.split("_");
            skinId = Integer.parseInt(strings[0]);
            version = Integer.parseInt(strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
