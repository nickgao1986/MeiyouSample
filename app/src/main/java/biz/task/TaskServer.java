package biz.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class TaskServer {

    public TaskServer(
            int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {

        groupHolderMap = new ConcurrentHashMap<String, GroupHolder>();
        initHandler();

        executor = new TaskPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory, handler, callback);
        MAXIMUM_POOL_SIZE = maximumPoolSize;
    }


    protected void initHandler() {
        TaskHandlerThread taskHandlerThread = new TaskHandlerThread("task-handler-thread");
        taskHandlerThread.start();
        threadHandler = new Handler(taskHandlerThread.getLooper(), taskHandlerThread);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public class TaskHandlerThread extends HandlerThread implements Handler.Callback {

        public TaskHandlerThread(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TYPE_SUBMIT_TASK:
                    CmpTask task = (CmpTask) msg.obj;
                    processBeforeExt(task);
                    beforeDoSubmit(task);
                    doSubmitTask(task);
                    afterDoSubmit(task);
                    break;
                case MSG_TYPE_REMOVE_TASK:
                    task = (CmpTask) msg.obj;
                    if (msg.arg1 == 0) {
                        LogUtils.e(sTAG, "task exec failed");
                    }
                    processAfterExt(task);

//                    removeOverTask();
//                    submitReadyTask();
                    break;
            }
            return false;
        }
    }

    public int getActiveCount(){
        return executor.getActiveCount();
    }



    protected void processBeforeExt(final CmpTask task) {
        if (task.getTaskMiniExt() == null) {
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                task.getTaskMiniExt().beforeRun(task);
            }
        });
    }

    protected void processAfterExt(final CmpTask task) {
        if (task.getTaskMiniExt() == null) {
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                task.getTaskMiniExt().postRun(task);
            }
        });
    }

    protected void beforeDoSubmit(CmpTask task) {
    }

    protected void afterDoSubmit(CmpTask task) {
    }

    protected static final String sTAG = "TaskServer";
    protected TaskPoolExecutor executor;
    protected ConcurrentHashMap<String, GroupHolder> groupHolderMap;
    protected static AtomicInteger anonymousId = new AtomicInteger(1);
    protected final int MAXIMUM_POOL_SIZE;

    protected Handler threadHandler;
    protected Handler mainHandler;

    public static final int MSG_TYPE_SUBMIT_TASK = 0;
    public static final int MSG_TYPE_REMOVE_TASK = 1;
    public static final int MSG_TYPE_MODIFY_PRIORITY = 2;

    protected ExecuteCallback callback = new ExecuteCallback() {

        @Override
        public void before(CmpTask task) {
            task.setStatus(CmpTask.STATUS_RUNNING);
        }

        @Override
        public void execute(CmpTask task) {

        }

        @Override
        public void post(CmpTask task) {
            task.setStatus(CmpTask.STATUS_OVER);
            postExecuteTask(task, true);
        }

        @Override
        public void exception(CmpTask task) {
            task.setStatus(CmpTask.STATUS_OVER);
            postExecuteTask(task, false);
        }
    };

    public void postExecuteTask(CmpTask task, boolean executeStatus) {
//        LogUtils.d(sTAG, task.toString());
        threadHandler.sendMessage(threadHandler.obtainMessage(MSG_TYPE_REMOVE_TASK, executeStatus ? 1 : 0, 0, task));
    }


    public interface ExecuteCallback {

        public void before(CmpTask task);

        public void execute(CmpTask task);

        public void post(CmpTask task);

        public void exception(CmpTask task);
    }

    private void doSubmitTask(CmpTask task) {
        if (verify(task)) return;

        if (duplicateCheck(task)) return;

        TaskHolder holder = new TaskHolder();
        holder.task = task;

        GroupHolder groupHolder = getGroupHolder(task);

        insertHolder(holder, groupHolder);

        //如果需要前向取消
        /*if (task.getCancelType() == CmpTask.CANCEL_TYPE_FORWARD) {
            cancelForwardTask(holder, groupHolder.taskHolderList);
        }*/

        boolean canSubmit = checkCanReady(task);
        if (canSubmit) {
            submitToBlockQueue(task, holder);
        } else {
            waitInTaskList(task, holder);
        }
    }

    protected <T> boolean checkCanReady(CmpTask task) {
        if (task == null) {
            return false;
        }
        boolean canSubmit = true;
        //查看是否存在同一个groupName 的任务已经 在/准备 执行
        if (task.isNeedSerial()) {
            Map<String, GroupHolder> groupMap = groupHolderMap;
            if (groupMap.isEmpty()) {
                LogUtils.d(sTAG, "checkCanReady groupMap is empty,true ");
                return poolMaxSize();
            }
            List<TaskHolder> list = getGroupList(task.getGroupName());
            if (list == null || list.isEmpty()) {
                LogUtils.d(sTAG, "checkCanReady list is empty,true ");
                return poolMaxSize();
            }
            Iterator<TaskHolder> iterator = list.iterator();
            for (; iterator.hasNext(); ) {
                TaskHolder holder = iterator.next();
                if (holder.task != null
                        && (
                        holder.task.getStatus() == CmpTask.STATUS_READY
                                || holder.task.getStatus() == CmpTask.STATUS_RUNNING)
                        && holder.task.isNeedSerial()) {
                    canSubmit = false;
                    break;
                }
            }
        }
        canSubmit = canSubmit && poolMaxSize();
        return canSubmit;
    }

    protected boolean poolMaxSize() {
        if (executor.getActiveCount() >= MAXIMUM_POOL_SIZE
                || executor.getQueue().remainingCapacity() == 0) {
            LogUtils.w(sTAG, "executor pool size  max or block queue is full !!!!--------:"+executor.getActiveCount());
            return false;
        }
        return true;
    }

    private void submitToBlockQueue(CmpTask task, TaskHolder holder) {
//        LogUtils.d(sTAG, "need serial? " + task.isNeedSerial() + " task submit! " + task.getName());
        holder.task.setStatus(CmpTask.STATUS_READY);
        holder.future = executor.submit(task);
    }

    private void waitInTaskList(CmpTask task, TaskHolder holder) {
//        LogUtils.d(sTAG, "task need Serial! waiting..." + task.getName());
        holder.task.setStatus(CmpTask.STATUS_WAIT);
    }


    private void insertHolder(TaskHolder holder, GroupHolder groupHolder) {
        List<TaskHolder> groupList = groupHolder.taskHolderList;
        Iterator<TaskHolder> tmpIterator = groupList.iterator();
        int i = 0;
        for (; tmpIterator.hasNext(); ++i) {
            CmpTask tmpTask = tmpIterator.next().task;
            if ((tmpTask.getStatus() == CmpTask.STATUS_INIT
                    || tmpTask.getStatus() == CmpTask.STATUS_WAIT
                    || tmpTask.getStatus() == CmpTask.STATUS_CANCEL
            ) && holder.task.compareTo(tmpTask) < 0) {
                break;
            }
        }
        if (i >= groupList.size()) {
            groupList.add(holder);
        } else {
            groupList.add(i, holder);
        }

        if (groupHolder.groupPriority > holder.task.getPriority()) {
            groupHolder.groupPriority = holder.task.getPriority();
        }
    }


    private GroupHolder getGroupHolder(CmpTask task) {
        GroupHolder groupHolder = groupHolderMap.get(task.getGroupName());
        if (groupHolder == null) {
            groupHolder = new GroupHolder(task.getGroupName(), task.getPriority());
            GroupHolder tmp = groupHolderMap.putIfAbsent(task.getGroupName(), groupHolder);
            if (tmp != null) {
                groupHolder = tmp;
            }
        }
        return groupHolder;
    }


    private boolean verify(CmpTask task) {
        if (task == null) {
            return true;
        }
        if (StringUtils.isBlank(task.getName())) {
            task.setName(genTaskDefaultName(null));
        }
        if (StringUtils.isBlank(task.getGroupName())) {
            throw new RuntimeException("task no group name !");
        }
        return false;
    }

    private boolean duplicateCheck(CmpTask task) {
        //默认是后向去重
        TaskHolder holder;
        if (task.getCancelType() == CmpTask.CANCEL_TYPE_DUPLICATE) {
            holder = getTaskHolderFromMap(task.getName(), task.getGroupName());
            if (holder != null) {
                return true;
            }
        }
        return false;
    }

    final public void submit(CmpTask task) {
        threadHandler.sendMessage(
                threadHandler.obtainMessage(MSG_TYPE_SUBMIT_TASK, task));
    }

    public TaskHolder getTaskHolderFromMap(String name, String groupName) {
        List<TaskHolder> list = getGroupList(groupName);
        if (list == null || list.isEmpty()) {
            return null;
        }
        Iterator<TaskHolder> iterator = list.iterator();
        for (; iterator.hasNext(); ) {
            TaskHolder holder = iterator.next();
            if (StringUtils.equals(holder.task.getName(), name)) {
                return holder;
            }
        }
        return null;
    }

    public List<TaskHolder> getGroupList(String groupName) {
        return groupHolderMap.get(groupName) == null ? null : groupHolderMap.get(groupName).taskHolderList;
    }

    public static String genTaskDefaultName(String name) {
        if (StringUtils.isBlank(name)) {
            name = "anonymous-task-";
        }
        return name + "-" + anonymousId.getAndIncrement();
    }
}
