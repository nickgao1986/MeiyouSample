package biz.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class TaskPoolExecutor extends ThreadPoolExecutor {

    private static final String sTAG = "TaskPoolExecutor";

    private TaskServer.ExecuteCallback callback;

    public TaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                            TimeUnit unit, BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory, RejectedExecutionHandler handler,
                            TaskServer.ExecuteCallback callback) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.callback = callback;
    }

    public Future<?> submit(CmpTask task) {
        return super.submit(task);
    }
}
