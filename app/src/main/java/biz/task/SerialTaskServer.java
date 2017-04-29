package biz.task;

import android.os.Build;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class SerialTaskServer extends TaskServer{

  //  protected TaskTimeOutChecker timeOutChecker;

    public SerialTaskServer(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                            TimeUnit unit, BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
       // timeOutChecker = new TaskTimeOutChecker(groupHolderMap,threadHandler);
        //TODO 这里要考虑回收无用的server
        if(android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
            executor.allowCoreThreadTimeOut(true);//允许回收 core thread  ，Serial 有可能存在很多个，所以要考虑回收
        }
    }

    public SerialTaskServer(ThreadFactory threadFactory) {
        this(1, 1, 1, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),
                threadFactory,new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    protected void beforeDoSubmit(CmpTask task) {
        super.beforeDoSubmit(task);
       // timeOutChecker.start();
    }

    @Override
    protected <T> boolean checkCanReady(CmpTask task) {
        return true;
    }

}
