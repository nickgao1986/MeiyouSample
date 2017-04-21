package nickgao.com.meiyousample.settings;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MineSection{

    /**
     * 9宫格
     */
    public static final int STYLE_NINE = 1;
    /**
     * 一行一张banner
     */
    public static final int STYLE_BANNER = 2;

    /**
     * 一行2张banner
     */
    public static final int STYLE_TWO_BANNER = 3;

    /**
     * 列表
     */
    public static final int STYLE_LIST = 4;

    /**
     * 小工具
     */
    public static final int TRACE_TYPE_TOOLS = 1;

    /**
     * 我的
     */
    public static final int TRACE_TYPE_MINE = 2;

    public String title;
    public int asso_id;
    public int mode;
    public boolean is_hide;//隐藏
    public boolean has_more;//显示更多
    public String attr_text;//跳转链接
    public int attr_id;//跳转id
    public int style;//1,九宫格,2,一行一张图片,3,一行两张图片
    public boolean has_line;//是否和上一个section保持间距
    public int uri_type;//more_url的跳转方式
    public int trace_type;//埋点类型
    public String icon;//小图标

}
