package com.lingan.seeyou.ui.view.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import activity.PeriodBaseActivity;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.firstPage.DynamicDetailClickPraiseEvent;
import nickgao.com.meiyousample.personal.ExtendOperationController;
import nickgao.com.meiyousample.personal.ExtendOperationListener;
import nickgao.com.meiyousample.skin.OperationKey;


/**
 * 我的资料
 *
 * @author zhengxiaobin <gybin02@Gmail.com>
 * @since 2016/8/9 18:08
 */
public class MyProfileActivity extends PeriodBaseActivity implements ExtendOperationListener {
    private static final String TAG = "MyProfileActivity";

    public interface OnEditPersonalListener {
        void onEditName(String name);

        void onEditImage(String url);
    }

    public static OnEditPersonalListener listener;

    private MyProfileController myProfileController;
    //ModeChangeController modeChangeController;

//    public static void enterActivity(Context context) {
//        Intent intent = getNotifyIntent(context);
//        context.startActivity(intent);
//    }

//    public static void enterActivity(Context context,
//                                     OnEditPersonalListener listener) {
//        MyProfileActivity.listener = listener;
//        enterActivity(context);
//    }

    public static void enterActivity(Context context, int toMode) {
        Intent intent = new Intent(context, MyProfileActivity.class);
        intent.putExtra("mode", toMode);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//    public static Intent getNotifyIntent(Context context) {
//        Intent intent = new Intent(context, MyProfileActivity.class);
//        int toMode = CalendarController.getInstance().getIdentifyManager().getIdentifyModelValue();
//        intent.putExtra("mode", toMode);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        return intent;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getIntentData(getIntent());
        initSkin();
        initController();
        initTitle();
        myProfileController.initView();
       // modeChangeController.initView();
        ExtendOperationController.getInstance().register(this);
    }

    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(this, this.getResources(), this.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initController() {
        myProfileController = new MyProfileController(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_profile_my;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //modeChangeController.onResume();
    }


    @Override
    public void onBackPressed() {
        onFinish();
    }

    protected void initTitle() {
        getTitleBar().setTitle(R.string.profile);
        getTitleBar().setButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        }, null);
    }


    private void onFinish() {
        myProfileController.handleComplete();
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        ExtendOperationController.getInstance().unRegister(this);
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        if (operationKey == OperationKey.UPDATE_UI) {
            updateSkin();
            //modeChangeController.fillResource();
        }
    }


    public void onEventMainThread(DynamicDetailClickPraiseEvent event) {
    }
}
