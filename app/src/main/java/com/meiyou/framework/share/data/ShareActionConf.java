package com.meiyou.framework.share.data;

import java.io.Serializable;

/**
 * Created by hxd on 16/7/18.
 */
public class ShareActionConf implements Serializable{

    /**
     * 结果是否要显示toast
     */
    private boolean isShowResultToast = true;

    private boolean useDefaultCallback = true;

    public static enum EDIT_VIEW_MODE {
        DEFAULT,
        USE,
        NO
    }

    public boolean isShowResultToast() {
        return isShowResultToast;
    }

    public void setIsShowResultToast(boolean isShowResultToast) {
        this.isShowResultToast = isShowResultToast;
    }

    private EDIT_VIEW_MODE editViewMode = EDIT_VIEW_MODE.DEFAULT;

    public EDIT_VIEW_MODE getEditViewMode() {
        return editViewMode;
    }

    public void setEditViewMode(EDIT_VIEW_MODE editViewMode) {
        this.editViewMode = editViewMode;
    }

    public void setShowResultToast(boolean showResultToast) {
        isShowResultToast = showResultToast;
    }


    public boolean isUseDefaultCallback() {
        return useDefaultCallback;
    }

    public void setUseDefaultCallback(boolean useDefaultCallback) {
        this.useDefaultCallback = useDefaultCallback;
    }

}
