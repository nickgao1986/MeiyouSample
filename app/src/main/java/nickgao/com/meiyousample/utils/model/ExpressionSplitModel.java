package nickgao.com.meiyousample.utils.model;

import android.content.res.Resources;

/**
 * 文本切割类型
 * Created by lwh on 2015/7/15.
 */
public class ExpressionSplitModel {
    public static final int TYPE_TEXT=0; //普通文本或者包含emoji的文本
    public static final int TYPE_EXPRESSION=1;//自定义表情
    public int type;//类型
    public String content="";//内容
    //用于html展示
    public int privilege;//是否可转换url
    public int blockId;//圈子ID
    //用于自定义表情展示
    public boolean is_local_exist =false;//本地是否存在自定义表情
    public Resources resource;//resource
    public String package_name;//自定义表情包名 如:com.lingan.expression_cat;
    public String gif_name;//自定义表情本地名字 如 hello_kitty.gif
    public String gif_url;//自定义表情网络url


}
