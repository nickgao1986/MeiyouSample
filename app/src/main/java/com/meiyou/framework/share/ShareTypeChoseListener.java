package com.meiyou.framework.share;

import com.meiyou.framework.share.data.BaseShareInfo;

/**
 * Created by hxd on 15/7/31.
 */
public interface ShareTypeChoseListener {
    public BaseShareInfo onChoose(ShareType shareType, BaseShareInfo shareInfoDO);
}
