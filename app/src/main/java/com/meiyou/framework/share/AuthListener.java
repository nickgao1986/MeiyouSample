package com.meiyou.framework.share;

import android.os.Bundle;

/**
 * Created by hxd on 15/7/30.
 */
public interface AuthListener {
    public void onStart(ShareType shareType) ;

    public void onComplete(Bundle bundle, ShareType shareType) ;

    public void onError(AuthException e, ShareType shareType) ;

    public void onCancel(ShareType shareType);
}
