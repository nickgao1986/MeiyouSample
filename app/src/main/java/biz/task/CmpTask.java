package biz.task;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public abstract class CmpTask extends FutureTask
        implements Comparable<CmpTask>{

    private static final String sTAG = "CmpTask";

    public static final int PRIOR_HIGH = 0;
    public static final int PRIOR_UI = 32;
    public static final int PRIOR_NORMAL = 256;
    volatile int priority = PRIOR_NORMAL;


    public static final int CANCEL_TYPE_NONE = 0;
    public static final int CANCEL_TYPE_DUPLICATE = 1;//同一个group任务,提交的最新任务A如果在队列中有重名的任务B,A任务将被直接去重
    public static final int CANCEL_TYPE_FORWARD = 2;//同一group任务，提交最新的任务A 将取消之前的所有提交的同名任务 ：前向取消
    protected boolean dynamic;
    protected boolean canStop = true;
    protected String name;
    protected String groupName;
    protected boolean needSerial;//同一类任务需要按照提交顺序依次执行
    protected int cancelType = CANCEL_TYPE_NONE;//

    protected static AtomicInteger prior = new AtomicInteger(PRIOR_UI);
    public static Comparator<Runnable> comparator = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs == null || rhs == null) {
                LogUtils.w(sTAG, " Comparator  values has null");
                return 0;
            }
            CmpTask lt = (CmpTask) lhs;
            CmpTask rt = (CmpTask) rhs;
            if (lt == null || rt == null) {
                LogUtils.e(sTAG, "Comparator cast failed!");
                return 0;
            }
            return Integer.valueOf(lt.priority).compareTo(rt.priority);
        }
    };


    /**
     * task 的执行状态
     * ready 是被提交到 线程池的
     * wait 是被加入到列表但是等待被加人线程池
     */
    public static final int STATUS_INIT = 0;
    public static final int STATUS_WAIT = 1;
    public static final int STATUS_READY = 2;
    public static final int STATUS_RUNNING = 3;
    public static final int STATUS_OVER = 4;
    public static final int STATUS_CANCEL = 5;
    private int status = STATUS_INIT;
    protected TaskMiniExt taskMiniExt;

    /**
     * 下标 与 status 对应
     */
    public Long[] costTime = new Long[]{0l, 0l, 0l, 0l, 0l, 0l};


    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void setCostTime(int i) {
        costTime[i] = getCurrentTime();
    }


    /**
     * @param dynamic    动态化优先级
     * @param name       任务名称
     * @param groupName  任务组名称
     * @param canStop    是否可停止
     * @param needSerial 是否需要顺序化
     * @param runnable   runnable
     */
    public CmpTask(boolean dynamic, String name, String groupName,
                   boolean canStop, boolean needSerial, Runnable runnable, TaskMiniExt taskMiniEx) {
        super(runnable, null);
        this.canStop = canStop;
        this.dynamic = dynamic;
        this.name = name;
        this.groupName = groupName;
        this.needSerial = needSerial;
        this.taskMiniExt = taskMiniEx;
        priority = generatePriority();
        setCostTime(STATUS_INIT);
    }


    public CmpTask(String name, String groupName, Runnable runnable) {
        this(true, name, groupName, true, false, runnable, null);
    }

    public CmpTask(String name, String groupName, Runnable runnable, boolean canStop) {
        this(true, name, groupName, canStop, false, runnable, null);
    }


    @Override
    public int compareTo(CmpTask cmpTask) {
        if (cmpTask == null) {
            LogUtils.e(sTAG, "CmpTask compareTo failed!");
            return 0;
        }
        return Integer.valueOf(priority).compareTo(cmpTask.priority);
    }

    public TaskMiniExt getTaskMiniExt() {
        return taskMiniExt;
    }

    public CmpTask setTaskMiniExt(TaskMiniExt taskMiniExt) {
        this.taskMiniExt = taskMiniExt;
        return this;
    }

    /**
     * 生成优先级数字 数字越小，优先级越大
     */
    protected int generatePriority() {
        return prior.getAndIncrement() + getPriorOffset();
    }

    abstract public int getPriorOffset();

    public boolean isNeedSerial() {
        return needSerial;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public int getPriority() {
        return priority;
    }

    public void setStatus(int status) {
        this.status = status;
        this.setCostTime(status);
    }
    public int getCancelType() {
        return cancelType;
    }

    public CmpTask setCancelType(int cancelType) {
        this.cancelType = cancelType;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getStatus() {
        return status;
    }

    public static int getLowestPrior(Map<String, GroupHolder> holderMap) {

        int prior = PRIOR_UI;
        for (String name : holderMap.keySet()) {
            List<TaskHolder> holderList = holderMap.get(name).taskHolderList;
            for (TaskHolder holder : holderList) {
                if (holder == null) {
                    continue;
                }
                CmpTask task = holder.task;
                if (task != null) {
                    if (prior < task.getPriority()) {
                        prior = task.getPriority();
                    }
                }
            }
        }
        prior = prior + 1;
        if (prior >= PRIOR_NORMAL) {
            prior = PRIOR_NORMAL - 1;
        }
        return prior;
    }

    /**
     * 获取最高优先级 最高优先级 = highest-1
     */
    public static int getHighPrior(Map<String, GroupHolder> holderMap) {
        int prior = PRIOR_NORMAL;
        for (String name : holderMap.keySet()) {
            List<TaskHolder> holderList = holderMap.get(name).taskHolderList;
            for (TaskHolder holder : holderList) {
                if (holder == null) {
                    continue;
                }
                CmpTask task = holder.task;
                if (task != null) {
                    if (prior > task.getPriority()) {
                        prior = task.getPriority();
                    }
                }
            }
        }
        prior = prior - 1;
        if (prior < PRIOR_UI) {
            prior = PRIOR_UI;
        }
        return prior;
    }

    public void setPriority(int priority) {
        if (dynamic) {
            this.priority = priority;
        }
    }

    public boolean isCanStop() {
        return canStop;
    }

}
