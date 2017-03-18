package nickgao.com.meiyousample.event;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class BaseEvent {

    private long mPageCode; //用来唯一标识是哪个页面需要响应，通常为创建Activity的时间戳

    public BaseEvent(){}

    public BaseEvent(long pageCode) {
        mPageCode = pageCode;
    }

    public long getPageCode() {
        return mPageCode;
    }

}
