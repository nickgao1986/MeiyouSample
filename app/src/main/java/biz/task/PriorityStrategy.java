package biz.task;

import java.util.Map;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

abstract public class PriorityStrategy {

    protected ModifyPriorityCallback callback;

    abstract public void modifyPriority(UIChangeEvent event,
                                        Map<String,GroupHolder> groupMap);

    public ModifyPriorityCallback getCallback() {
        return callback;
    }

    public void setCallback(ModifyPriorityCallback callback) {
        this.callback = callback;
    }
}
