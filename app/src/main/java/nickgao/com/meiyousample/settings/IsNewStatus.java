package nickgao.com.meiyousample.settings;

import java.io.Serializable;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class IsNewStatus implements Serializable {

    //数据编号
    private int assoId;
    //上新时间
    private long newTime;
    //是否已点击
    private boolean click;
    //该值用作判断是否显示小红点还是new（红点逻辑：第一次加载显示new，第二次更新时间大于本次显示红点）isNew = newTime > localNewTime
    //true为更新,false上新
    private boolean showRedDot;

    //是否显示new
    private boolean upNew;


    //数字
    private int count;

    //蜜友圈更新时的头像地址
    private String avatar;

    public IsNewStatus() {

    }


    public IsNewStatus(int assoId, long newTime, boolean click, boolean isShowRedDot, int count, boolean upNew) {
        this.assoId = assoId;
        this.newTime = newTime;
        this.click = click;
        this.showRedDot = isShowRedDot;
        this.upNew = upNew;
        this.count = count;
    }

}
