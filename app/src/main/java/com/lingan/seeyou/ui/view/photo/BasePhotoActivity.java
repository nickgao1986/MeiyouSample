package com.lingan.seeyou.ui.view.photo;

import android.os.Bundle;

import activity.LinganActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/8/26.
 */
public class BasePhotoActivity extends LinganActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(PhotoController.FinishPicking event){
        this.finish();
    }
}
