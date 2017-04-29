package biz.task;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class UITaskServer extends TaskServer {
    PriorityManager priorityManager;

    public UITaskServer(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        //super init call initHandler
        priorityManager = new PriorityManager(workQueue, groupHolderMap, threadHandler);
        priorityManager.setPriorityStrategy(new DefaultPriorityStrategy());
    }

    @Override
    protected void initHandler() {
        TaskHandlerThread taskHandlerThread = new UITaskHandlerThread("uiTask-handler-thread");
        taskHandlerThread.start();
        threadHandler = new Handler(taskHandlerThread.getLooper(), taskHandlerThread);
    }

    public class UITaskHandlerThread extends TaskHandlerThread {

        public UITaskHandlerThread(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TYPE_MODIFY_PRIORITY:
                    UIChangeEvent event = (UIChangeEvent) msg.obj;
                    priorityManager.getPriorityStrategy().
                            modifyPriority(event, priorityManager.getGroupMap());
                    break;
            }
            return false;
        }
    }

}
