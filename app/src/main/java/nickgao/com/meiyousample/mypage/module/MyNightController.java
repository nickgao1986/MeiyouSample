package nickgao.com.meiyousample.mypage.module;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import biz.threadutil.ThreadUtil;
import biz.util.ViewUtilController;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.database.SkinStastus_DataBase;
import nickgao.com.meiyousample.personal.ExtendOperationController;
import nickgao.com.meiyousample.settings.DataSaveHelper;
import nickgao.com.meiyousample.skin.ActivityUtil;
import nickgao.com.meiyousample.skin.OnFollowListener;
import nickgao.com.meiyousample.skin.OnNotifationListener;
import nickgao.com.meiyousample.skin.OperationKey;
import nickgao.com.meiyousample.skin.SkinController;
import nickgao.com.meiyousample.skin.SkinDownloadService;
import nickgao.com.meiyousample.skin.SkinDownloadType;
import nickgao.com.meiyousample.skin.SkinEngine;
import nickgao.com.meiyousample.skin.SkinModel;
import nickgao.com.meiyousample.skin.SkinUtil;
import nickgao.com.meiyousample.skin.ToastUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class MyNightController implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private UpdateMyNightReceiver myReceiver;
    boolean isLoading = false;

    private TextView tvRight;
    private TextView tvNeightProgress;
    private ProgressBar pbGrogress;
    private LinearLayout llnight;

    private SkinStastus_DataBase stastusDataBase;
    private SkinModel model;
    //上一次身份模式,以便切换身份时,换肤所用
    private int lastMode;

    public MyNightController(Activity mContext, View mRootView) {
        activity = mContext;
        rootView = mRootView;
        myReceiver = new UpdateMyNightReceiver(activity);
        //注册下载夜间模式的广播
        myReceiver.registerAction(SkinDownloadType.UPDATE_SKIN_ACTION);
        stastusDataBase = new SkinStastus_DataBase(activity);
    }


    public void findView() {
        tvRight = (TextView) rootView.findViewById(R.id.tvRight);
        tvNeightProgress = (TextView) rootView.findViewById(R.id.tvNeightProgress);
        pbGrogress = (ProgressBar) rootView.findViewById(R.id.pbGrogress);
        llnight = (LinearLayout) rootView.findViewById(R.id.ll_my_night);

        llnight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    public void fillResource() {
        SkinManager.getInstance().setTextColor(tvRight, R.color.white_a);
        SkinManager.getInstance().setTextColor(tvNeightProgress, R.color.white_a);
    }


    public void destroy() {
        myReceiver.unregister();
        myReceiver = null;
    }

    /**
     * 通过身份状态,初始化皮肤
     */
    public void initSkinSwitch() {
        switchSkin(lastMode, true);
    }

    public void switchSkin(int identifyModel, boolean isInit) {
        if (llnight != null) {
            llnight.setVisibility(View.VISIBLE);
        }
        lastMode = identifyModel;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_my_night || id == R.id.tvRight) {
            if (true) {
                if (false) {
                    if (!DataSaveHelper.getInstance(activity).getIsNightMode()) {
                        //夜间模式
                        SkinUtil.getInstance().copyAssetFile2Data(activity, "NightSkin.apk", "NightSkin.apk");
                        DataSaveHelper.getInstance(activity).setIsNightMode(true);
                        DataSaveHelper.getInstance(activity).saveNightSkinApkName("NightSkin.apk");
                        ToastUtils.showToast(activity, "change night true");
                    } else {
                        DataSaveHelper.getInstance(activity).setIsNightMode(false);
                        ToastUtils.showToast(activity, "change night false");
                    }
                    SkinEngine.getInstance().initResources();
                    ExtendOperationController.getInstance().notify(OperationKey.UPDATE_UI, "");
                } else {
                    //不是测试都走这里
                    if (!DataSaveHelper.getInstance(activity).getIsNightMode()) {
                        //去切换成夜间模式
                        handleNightSkinModel();
                    } else {
                        //切换普通模式
                        DataSaveHelper.getInstance(activity).setIsNightMode(false);
                        ExtendOperationController.getInstance().notify(OperationKey.UPDATE_UI, "");
                        setSkinTitle();
                    }
                }
            }

        }
    }

    public void setSkinTitle() {
        if (DataSaveHelper.getInstance(activity).getIsNightMode()) {
            String skinTitle = DataSaveHelper.getInstance(activity).getSkinNightName();
//            tvSkinTitle.setText(skinTitle);
            ViewUtilController.setTextDrawalbe(tvRight, SkinManager.getInstance().getAdapterDrawable(R.drawable.nav_btn_night_hover), null, null, null);
        } else {
            String skinTitle = DataSaveHelper.getInstance(activity).getSkinName();
//            tvSkinTitle.setText(skinTitle);
            ViewUtilController.setTextDrawalbe(tvRight, SkinManager.getInstance().getAdapterDrawable(R.drawable.apk_all_top_night), null, null, null);
        }
    }


    public void handleNightSkinModel() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        ThreadUtil.addTaskForIO(activity, "", new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                return SkinController.getInstance(activity).getNightSkinModel(new OnFollowListener() {
                    @Override
                    public void OnFollow(int isFollow) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pbGrogress.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }

            @Override
            public void onFinish(Object result) {
                model = (SkinModel) result;
                if (model != null) {
                    if (model.updateStastus == SkinDownloadType.SKIN_DOWNLOADING || model.updateStastus == SkinDownloadType.SKIN_PAUSE || model.updateStastus == SkinDownloadType.SKIN_NO_NETWORK) {
                        if (model.updateStastus == SkinDownloadType.SKIN_NO_NETWORK || (SkinDownloadService.downloaders.get(model.getFileName()) == null && model.completeSize > 0)) {
                            model.updateStastus = SkinDownloadType.SKIN_PAUSE;
                        }
                    }
                    handleSkinModelStatus(model);
                } else {
                    pbGrogress.setVisibility(View.GONE);
                }
                isLoading = false;
            }
        });
    }



    class UpdateMyNightReceiver extends BroadcastReceiver

    {
        private Context context;

        public UpdateMyNightReceiver(Context context) {
            this.context = context;
        }

    public void registerAction(String action) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        context.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        //接收来自DownloadService传送过来的数据,并且更新进度条
        if (intent.getAction().equals(SkinDownloadType.UPDATE_SKIN_ACTION)) {
            int skinId = intent.getIntExtra("skinId", 0);
            boolean isSucess = intent.getBooleanExtra("flag", false);
            int completeSize = intent.getIntExtra("completeSize", 0);
            boolean isPause = intent.getBooleanExtra("pause", false);
            boolean is_int_Pause = intent.getBooleanExtra("int_pause", false);//初始化文件大小时
            if (model != null) {
                if (model.skinId == skinId) {
                    if (isSucess) {
                        model.updateStastus = SkinDownloadType.SKIN_COMPLETE;
                        model.completeSize = completeSize;
                        handleUpdataUI();

                        pbGrogress.setVisibility(View.GONE);
                        tvNeightProgress.setVisibility(View.GONE);
                        //自动使用夜间模式
                        SkinController.getInstance(context).copyFileAndUseThemeNight(activity, model, true, new OnNotifationListener() {
                            @Override
                            public void onNitifation(Object object) {
                                ActivityUtil.backToMainActivity(context);
                                model.updateStastus = SkinDownloadType.SKIN_APPLY;
                                stastusDataBase.updateStatusModel(model, model.updateStastus);
                                tvNeightProgress.setVisibility(View.GONE);
                                pbGrogress.setVisibility(View.GONE);
                                setSkinTitle();

//                                XiuAlertDialog xiuAlertDialog=new XiuAlertDialog(activity,"提示", "您已成功切换夜间模式，可以查看更多精美主题哦！");
//                                xiuAlertDialog.setButtonOkText("查看主题");
//                                xiuAlertDialog.setOnClickListener(new XiuAlertDialog.onDialogClickListener() {
//                                    @Override
//                                    public void onOk() {
//                                        context.startActivity(SkinHomeActivity.getNotifyIntent(context, SkinController.FROM_BLACK_SKIN));
//                                        AnalysisClickAgent.onEvent(context, "yjms-ckzt");
//                                    }
//
//                                    @Override
//                                    public void onCancle() {
//                                        tvNeightProgress.setVisibility(View.GONE);
//                                        pbGrogress.setVisibility(View.GONE);
//                                    }
//                                });
//                                xiuAlertDialog.show();

                            }
                        });
                    } else if (isPause) {
                        model.updateStastus = SkinDownloadType.SKIN_NO_NETWORK;
                        ToastUtils.showToast(context, "咦？网络不见了，请检查网络连接");
                        pbGrogress.setVisibility(View.GONE);
                    } else if (is_int_Pause) {
                        model.updateStastus = SkinDownloadType.SKIN_INT_NO_NETWORK;
                        ToastUtils.showToast(context, "初始化网络文件大小失败，请检查网络连接~");
                        pbGrogress.setVisibility(View.GONE);
                    } else {
                        model.completeSize = completeSize;
                        handleUpdataUI();
                    }
                }
            }
        }
    }
}

    /**
     * 更新夜间模式的UI
     */
    private void handleUpdataUI() {
        if (model != null) {
            int completeSize = model.completeSize;
            float fileSize = model.fileSize;
            float num = (float) completeSize / fileSize;
            int result = (int) (num * 100);
            tvNeightProgress.setVisibility(View.VISIBLE);
            tvNeightProgress.setText((result > 100 ? 100 : result) + "%");
        }
    }

    /**
     * 根据不同状态对夜间模式的操作 下载，切换，更新等操作
     */
    public void handleSkinModelStatus(final SkinModel skinModel) {
        switch (skinModel.updateStastus) {
            case SkinDownloadType.SKIN_INIT:
                pbGrogress.setVisibility(View.VISIBLE);
                judgeNetWork(skinModel);
                break;
            case SkinDownloadType.SKIN_UPDATE_VERSON:
                pbGrogress.setVisibility(View.VISIBLE);
                judgeNetWork(skinModel);
                break;
            case SkinDownloadType.SKIN_PAUSE:
            case SkinDownloadType.SKIN_NO_NETWORK:
                pbGrogress.setVisibility(View.VISIBLE);
                SkinDownloadService.doIntent(activity, skinModel, "setState", null);
                break;
            case SkinDownloadType.SKIN_INT_NO_NETWORK:
                pbGrogress.setVisibility(View.VISIBLE);
                SkinDownloadService.doIntent(activity, skinModel, "startDownload", null);
                break;
            case SkinDownloadType.SKIN_COMPLETE:
            case SkinDownloadType.SKIN_APPLY:
                pbGrogress.setVisibility(View.GONE);
                tvNeightProgress.setVisibility(View.GONE);
                SkinController.getInstance(activity).copyFileAndUseThemeNight(activity, skinModel, true, new OnNotifationListener() {
                    @Override
                    public void onNitifation(Object object) {
                        skinModel.updateStastus = SkinDownloadType.SKIN_APPLY;
                        stastusDataBase.updateStatusModel(skinModel, skinModel.updateStastus);
                        setSkinTitle();
                    }
                });
                break;
        }
    }


    /**
     * 判断网络情况下
     */
    public void judgeNetWork(final SkinModel model) {
        model.updateStastus = SkinDownloadType.SKIN_DOWNLOADING;
        stastusDataBase.addStatusModel(model, SkinDownloadType.SKIN_INIT, 0);
        SkinDownloadService.doIntent(activity, model, "startDownload", null);
    }



}
