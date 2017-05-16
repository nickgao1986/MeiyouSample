package nickgao.com.meiyousample.controller;

import biz.task.TaskManager;
import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class LinganController {

    protected TaskManager taskManager;

    protected final String uniqueId = getClass().getSimpleName() + Math.random();

    public LinganController() {
        LogUtils.d("=====LinganController");
        taskManager = TaskManager.getInstance();
    }


    public void submitLocalTask(String name, Runnable runnable) {
        taskManager.submit(name, uniqueId, runnable);
    }

    public void submitLocalTask(String name, boolean canStop, Runnable runnable) {
        taskManager.submit(name, uniqueId, runnable, canStop);
    }
}
