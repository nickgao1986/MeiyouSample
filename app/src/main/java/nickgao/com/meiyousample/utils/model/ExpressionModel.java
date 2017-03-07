package nickgao.com.meiyousample.utils.model;

import java.io.Serializable;

/**
 * 表情model 统一model ,有类型区分
 * Created by lwh on 2015/7/29.
 */
public class ExpressionModel implements Serializable{
    //ExpressionType类型
    public int type;
    //自定义表情参数
    public String packagename;
    public ExpressionSubModel expressionSubModel;
    public String iconurl="";
    public String gif_ui_name="";//显示回复表格里的名字
    //emoji参数
    public EmojiModel emojiModel;
}
