package nickgao.com.meiyousample.event;

/**
 * Created by gaoyoujian on 2017/3/18.
 */

public class NewsWebViewEvent extends BaseEvent {
    private EventType mEventType;

    public enum EventType{
        LOAD_DATA_SUCCESS //数据加载完成
    }

    public NewsWebViewEvent(long pageCode, EventType eventType) {
        super(pageCode);
        mEventType = eventType;
    }

    public EventType getEventType() {
        return mEventType;
    }
}
