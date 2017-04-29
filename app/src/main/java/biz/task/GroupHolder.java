package biz.task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class GroupHolder implements Comparable<GroupHolder>{
    public String groupName;
    public int groupPriority = CmpTask.PRIOR_NORMAL; //保存了本组内任务的最高的优先级
    public List<TaskHolder> taskHolderList
            = new CopyOnWriteArrayList<TaskHolder>();


    public GroupHolder(String groupName, int groupPriority) {
        this.groupName = groupName;
        this.groupPriority = groupPriority;
    }

    @Override
    public int compareTo(GroupHolder another) {
        return Integer.valueOf(this.groupPriority).
                compareTo(another.groupPriority);
    }
}
