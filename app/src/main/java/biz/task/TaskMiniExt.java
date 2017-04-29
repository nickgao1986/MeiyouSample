package biz.task;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public interface TaskMiniExt {
    /**
     * 这里只允许执行UI相关的操作
     * @param task task
     */
    public void beforeRun(CmpTask task);
    /**
     * 这里只允许执行UI相关的操作
     * @param task task
     */
    public void postRun(CmpTask task);
}
