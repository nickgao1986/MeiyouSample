package biz.task;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public interface ModifyPriorityCallback {
    boolean executeModify(String name, String groupName, int priority);
    boolean cancel(String name, String groupName);
}
