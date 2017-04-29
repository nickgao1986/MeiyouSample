package biz.task;

import android.os.Handler;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class PriorityManager implements ModifyPriorityCallback {
    private static final String sTAG ="PriorityManager";
    private ConcurrentHashMap<String,GroupHolder> groupMap;

    private BlockingQueue<Runnable> poolWorkQueue;
    private PriorityStrategy priorityStrategy;
    private Handler handler;

    public PriorityManager(BlockingQueue<Runnable> poolWorkQueue, Handler handler) {
        //TODO EventBus.getDefault().register(this);
        groupMap = new ConcurrentHashMap<String, GroupHolder>();
        this.poolWorkQueue = poolWorkQueue;
        this.handler = handler;

    }

    public PriorityManager(BlockingQueue<Runnable> poolWorkQueue,
                           ConcurrentHashMap<String,GroupHolder> groupMap,
                           Handler handler) {
        // TODO EventBus.getDefault().register(this);
        this.groupMap = groupMap;
        this.poolWorkQueue = poolWorkQueue;
        this.handler = handler;
    }


    /**
     * 设置 调整优先级的策略
     *
     * @param priorityStrategy
     */
    public void setPriorityStrategy(PriorityStrategy priorityStrategy) {
        this.priorityStrategy = priorityStrategy;
        this.priorityStrategy.setCallback(this);
    }

    /**
     * 用于接收UI 的变化事件，从而触发调整任务优先级
     * poolWorkQueue 是线程安全的
     *
     * @param event
     */
    @SuppressWarnings("unused")
    public void onEventBackgroundThread(UIChangeEvent event) {
        handler.sendMessage(handler.obtainMessage(TaskServer.MSG_TYPE_MODIFY_PRIORITY, 0, 0, event));
    }


    public boolean modifyPriority(String name, String groupName, int priority) {
        LogUtils.d(sTAG, "modifyPriority");
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if (!poolWorkQueue.isEmpty()) {
            TaskHolder holder = getTaskHolderFromMap(name, groupName);
            if (holder == null) {
                return true;
            }
            CmpTask task = holder.task;
            if (task == null) {
                return true;
            }
            int status = holder.task.getStatus();
            switch (status) {
                case CmpTask.STATUS_WAIT:
                    task.setPriority(priority);
                    break;
                case CmpTask.STATUS_READY:
                    if (!task.isDone()) {
                        task.setPriority(priority);
                        if (poolWorkQueue.contains(task)) {
                            //触发重新排序
                            poolWorkQueue.remove(task);
                            List<TaskHolder> list = getGroupList(groupName);

                            if(list!=null  && list.contains(holder)){
                                list.remove(holder);
                            }
                            task.setStatus(CmpTask.STATUS_INIT);
                            //poolWorkQueue.add(task);
                            //重新提交任务，由优先级决定是提交还是等待
                            LogUtils.d(sTAG,"remove and resubmit task "+ task.getName());
                            TaskManager.getInstance().submit(task);

                        }
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean executeModify(String name, String groupName, int priority) {
        return modifyPriority(name, groupName, priority);
    }

    /**
     * 取消任务，如果当前支持取消
     *
     * @param name
     * @return
     */
    @Override
    public boolean cancel(String name, String groupName) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        TaskHolder holder = getTaskHolderFromMap(name, groupName);

        if (holder == null) {
            return true;
        }
        CmpTask task = holder.task;
        if (task == null) {
            return false;
        }
        if (!task.isCanStop()) {
            return false;
        }

        task.setStatus(CmpTask.STATUS_CANCEL);
        Future<?> future = holder.future;

        if (future != null && !future.isDone() && future.cancel(false)) {
            handler.sendMessage(handler.obtainMessage(TaskServer.MSG_TYPE_REMOVE_TASK, 0, 0, task));
            return true;
        }

        return false;
    }

    public ConcurrentHashMap<String, GroupHolder> getGroupMap() {
        return groupMap;
    }

    public List<TaskHolder> getGroupList(String groupName){
        return groupMap.get(groupName)==null?null: groupMap.get(groupName).taskHolderList;
    }



    public TaskHolder getTaskHolderFromMap( String name, String groupName) {
        List<TaskHolder> list =getGroupList(groupName);
        if (list == null || list.isEmpty()) {
            return null;
        }
        Iterator<TaskHolder> iterator = list.iterator();
        for(;iterator.hasNext();) {
            TaskHolder holder = iterator.next();
            if (StringUtils.equals(holder.task.getName(), name)) {
                return holder;
            }
        }
        return null;
    }
    public int calculateGroupPriority(GroupHolder groupHolder) {
        List<TaskHolder> taskHolderList = groupHolder.taskHolderList;
        if (taskHolderList == null || taskHolderList.size() == 0) {
            return groupHolder.groupPriority;
        }
        int priority = taskHolderList.get(0).task.getPriority();
        Iterator<TaskHolder> iterator = taskHolderList.iterator();
        for (; iterator.hasNext(); ) {
            CmpTask tmp = iterator.next().task;
            if (tmp.getPriority() < priority) {
                priority = tmp.getPriority();
            }
        }
        return priority;
    }


    public PriorityStrategy getPriorityStrategy() {
        return priorityStrategy;
    }

}
