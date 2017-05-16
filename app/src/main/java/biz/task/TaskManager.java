package biz.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import nickgao.com.framework.utils.LogUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class TaskManager {

    private ConcurrentHashMap<String, TaskServer> serialTaskServerMap;

    private TaskServer networkTaskServer1,networkTaskServer2,networkTaskServer3,networkTaskServer4;
    private TaskServer normalTaskServer;
    private TaskServer realtimeServer;
    private static final int KEEP_ALIVE = 1;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = CPU_COUNT * 2;
    private static final int CORE_SIZE = CPU_COUNT * 1;
    private static final int MAX_SIZE = CPU_COUNT * 4;
    public static TaskManager getInstance() {
        return InstanceHolder.instance;
    }
    static class InstanceHolder {
        static TaskManager instance = new TaskManager();
    }


    private TaskManager() {
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        BlockingQueue<Runnable> normalBlockQueue = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        normalTaskServer = new TaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, normalBlockQueue, new TaskFactory("NormalTask"), handler);

        //四个网络线程池；
        //new BoundedPriorityBlockingQueue<Runnable>(QUEUE_SIZE, CmpTask.comparator);
        BlockingQueue<Runnable> poolWorkQueue4 = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        networkTaskServer4 = new UITaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, poolWorkQueue4, new TaskFactory("networkTaskServer4"), handler);
        BlockingQueue<Runnable> poolWorkQueue3 = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        networkTaskServer3 = new UITaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, poolWorkQueue3, new TaskFactory("poolWorkQueue3"), handler);
        BlockingQueue<Runnable> poolWorkQueue2 = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        networkTaskServer2 = new UITaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, poolWorkQueue2, new TaskFactory("poolWorkQueue2"), handler);
        BlockingQueue<Runnable> poolWorkQueue = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        networkTaskServer1 = new UITaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, poolWorkQueue, new TaskFactory("networkTaskServer1"), handler);

        BlockingQueue<Runnable> realTimeBlockQueue = new ArrayBlockingQueue<Runnable>(QUEUE_SIZE);
        realtimeServer = new TaskServer(CORE_SIZE, MAX_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, realTimeBlockQueue, new TaskFactory("RealtimeTask"), handler);

        serialTaskServerMap = new ConcurrentHashMap<String, TaskServer>();

        //groupTaskServerMap = new ConcurrentHashMap<String, TaskServer>();
    }

    public String submit(String name, String groupName, Runnable runnable) {
        this.submit(new LocalTask(name, groupName, runnable));
        return name;
    }

    public String submit(String name, String groupName, Runnable runnable, boolean canStop) {
        this.submit(new LocalTask(name, groupName, runnable, canStop));
        return name;
    }


    public void submit(CmpTask task) {
        TaskServer server = routeTaskServer(task);
        server.submit(task);
    }


    private TaskServer generateSerialTaskServer(TaskFactory taskFactory) {
        return new SerialTaskServer(taskFactory);
    }


    private TaskServer routeTaskServer(CmpTask task) {

        if (task.isNeedSerial()) {
            TaskServer taskServer = serialTaskServerMap.get(task.getGroupName());
            if (taskServer == null) {
                taskServer = generateSerialTaskServer(new TaskFactory("serialTask"));
                TaskServer tmp = serialTaskServerMap.putIfAbsent(task.getGroupName(), taskServer);
                if (tmp != null) {
                    taskServer = tmp;
                }
            }
            return taskServer;
        } else {
            if (task.isDynamic()) {
                return getFreeTask();

            } else {
                if (task.getPriority() <= CmpTask.PRIOR_UI) {
                    return realtimeServer;
                }
                return normalTaskServer;
            }
        }
    }


    private TaskServer getFreeTask(){
        try {
            //获取最空闲的线程池
            int count1 = networkTaskServer1.getActiveCount();
            int count2 = networkTaskServer2.getActiveCount();
            int count3 = networkTaskServer3.getActiveCount();
            int count4 = networkTaskServer4.getActiveCount();
            int []countArray =  new int[]{count1,count2,count3,count4};
            int min=0;
            for(int i = 1;i<countArray.length;i++){
                if(countArray[i]<countArray[min])
                    min = i;
            }
            int minValue = countArray[min];
            LogUtils.d(TAG,"使用线程池："+minValue+"==>count1:"+count1+"==>count2:"+count2+"===>count3:"+count3+"==>count4:"+count4);
            if(count1==minValue){
                return networkTaskServer1;
            }
            if(count2==minValue){
                return networkTaskServer2;
            }
            if(count3==minValue){
                return networkTaskServer3;
            }
            if(count4==minValue){
                return networkTaskServer4;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return networkTaskServer1;
    }

    private static class TaskFactory implements ThreadFactory {
        private String type;
        private final AtomicInteger mCount = new AtomicInteger(1);

        public TaskFactory(String type) {
            this.type = type;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "=====newThread"+type + " generate Thread " + mCount.getAndIncrement());
        }
    }
}
