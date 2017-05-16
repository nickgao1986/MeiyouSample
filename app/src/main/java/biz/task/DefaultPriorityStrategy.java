package biz.task;

import java.util.List;
import java.util.Map;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class DefaultPriorityStrategy extends PriorityStrategy {
    @Override
    public void modifyPriority(UIChangeEvent event
            , Map<String, GroupHolder> groupMap) {
        LogUtils.d("DefaultPriorityStrategy", "modifyPriority");
        if(event==null  || StringUtils.isEmpty(event.getUniqueId())){
            LogUtils.e("DefaultPriorityStrategy getUniqueId null");
            return;}
        GroupHolder groupHolder = groupMap.get(event.getUniqueId());
        if(groupHolder == null){
            return ;
        }
        List<TaskHolder> groupList = groupHolder.taskHolderList;

        if (this.callback != null && groupList != null && !groupList.isEmpty()) {
            for (TaskHolder holder :groupList) {
                if(holder==null){
                    continue;
                }
                String name = holder.task.getName();
                String groupName =holder.task.getGroupName();
                CmpTask task = holder.task;
                if (task == null) {
                    continue;
                }
                int prior = task.getPriority();
                switch (event.getStatus()) {
                    case UIChangeEvent.STATUS_ACTIVE:
                        prior = CmpTask.getHighPrior(groupMap);
                        break;
                    case UIChangeEvent.STATUS_STOP:
                        prior = CmpTask.getLowestPrior(groupMap);
                        break;
                    case UIChangeEvent.STATUS_DESTROY:
                        //cancel
                        callback.cancel(name,groupName);
                        break;
                }
                callback.executeModify(name,groupName, prior);
            }
        }
    }
}
