package biz.task;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class LocalTask extends CmpTask {

    public LocalTask(boolean dynamic, String name, String groupName,
                     boolean canStop, boolean needSerial, Runnable runnable,TaskMiniExt taskMiniExt) {
        super(dynamic, name, groupName, canStop, needSerial, runnable,taskMiniExt);
    }

    public LocalTask(String name, String groupName, Runnable runnable) {
        super(name, groupName, runnable);
        dynamic = false;
    }
    public LocalTask(String name, String groupName, Runnable runnable,boolean canStop) {
        super(name, groupName, runnable,canStop);
        dynamic = false;
    }

    @Override
    public int getPriorOffset() {
        return CmpTask.PRIOR_NORMAL;
    }
}
