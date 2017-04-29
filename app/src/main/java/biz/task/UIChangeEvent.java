package biz.task;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class UIChangeEvent {
    public static final int STATUS_ACTIVE=0;
    public static final int STATUS_STOP=1;
    public static final int STATUS_DESTROY=2;

    private int status;

    private String uniqueId;

    public UIChangeEvent(int status,String uniqueId){
        this.status=status;
        this.uniqueId = uniqueId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
