package nickgao.com.meiyousample.settings;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MineItemModel {

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

}
