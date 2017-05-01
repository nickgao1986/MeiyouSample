package nickgao.com.meiyousample.settings;

import org.json.JSONObject;

import java.io.Serializable;

import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MineItemModel implements Serializable, Comparable<MineItemModel> {

    /**
     *  使用images字段，使用图片随机轮播
     */
    public static final int IS_AD_HAS = 1;

    /**
     * 不进行图片轮播
     */
    public static final int IS_AD_NO = 0;

    public int asso_id;
    public String title;
    public String icon;
    public boolean is_new;
    public String new_time;
    public int newsCount;
    public int uri_type;
    public String attrText;
    public int attrId;
    public int section_id;
    public int ordinal_0;
    public int ordinal_1;
    public int ordinal_2;
    public int ordinal_3;
    public int mode;
    public boolean isRedDot;
    public String attr_text;
    public int attr_id;
    public int trace_type;//埋点类型
    public String tips_title;//透出的文字
    public String tips_icon;//透出的图标
    /**
     * 是否是广告（试用中心）
     */
    public int is_ad = IS_AD_NO;

    /**
     * 如果is_ad 为1的话，试用images内的图片地址进行随机切换，如果images为空，默认还试用icon
     */
    public String[] images;

    public MineItemModel() {}
    public MineItemModel(JSONObject obj) {
        try {
            asso_id = StringUtils.getJsonInt(obj, "asso_id");
            title = StringUtils.getJsonString(obj, "title");
            icon = StringUtils.getJsonString(obj, "icon");
            attrText = StringUtils.getJsonString(obj, "attrText");
            is_new = StringUtils.getJsonBoolean(obj, "is_new");
            new_time = StringUtils.getJsonString(obj, "new_time");
            newsCount = StringUtils.getJsonInt(obj, "newsCount");
            uri_type = StringUtils.getJsonInt(obj, "uri_type");
            attrId = StringUtils.getJsonInt(obj, "attrId");
            section_id = StringUtils.getJsonInt(obj, "section_id");
            ordinal_0 = StringUtils.getJsonInt(obj, "ordinal_0");
            ordinal_1 = StringUtils.getJsonInt(obj, "ordinal_1");
            ordinal_2 = StringUtils.getJsonInt(obj, "ordinal_2");
            ordinal_3 = StringUtils.getJsonInt(obj, "ordinal_3");
            mode = StringUtils.getJsonInt(obj, "mode");
            isRedDot = StringUtils.getJsonBoolean(obj, "isRedDot");
            trace_type = StringUtils.getJsonInt(obj, "trace_type");
            attr_id = StringUtils.getJsonInt(obj, "attr_id");
        } catch (Exception e) {
        }
    }


    @Override
    public int compareTo(MineItemModel mineItemModel) {
       return 0;

    }

    @Override
    public String toString() {
        return "MineItemModel{" +
                "asso_id=" + asso_id +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", is_new=" + is_new +
                ", new_time='" + new_time + '\'' +
                ", newsCount=" + newsCount +
                ", uri_type=" + uri_type +
                ", attrText='" + attrText + '\'' +
                ", attrId=" + attrId +
                ", section_id=" + section_id +
                ", ordinal_0=" + ordinal_0 +
                ", ordinal_1=" + ordinal_1 +
                ", ordinal_2=" + ordinal_2 +
                ", ordinal_3=" + ordinal_3 +
                ", mode=" + mode +
                ", isRedDot=" + isRedDot +
                ", url='" + attr_text + '\'' +
                '}';
    }
}


