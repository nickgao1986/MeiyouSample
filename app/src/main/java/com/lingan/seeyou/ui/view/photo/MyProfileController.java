package com.lingan.seeyou.ui.view.photo;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.RoundedImageView;
import com.lingan.seeyou.ui.view.photo.model.PhotoConfig;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import biz.util.BitmapUtil;
import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.controller.UserController;
import nickgao.com.meiyousample.mypage.module.MyHeaderController;
import nickgao.com.meiyousample.settings.DataSaveHelper;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * 我的资料--底部 用户信息控制逻辑
 *
 * @author zhengxiaobin <gybin02@Gmail.com>
 * @since 2016/8/9 18:08
 */
public class MyProfileController implements OnClickListener {
    private static final String TAG = "MyProfileController";
    //private final MyLoginController loginController;
    private DataSaveHelper mSaver;
    private Activity mActivity;

    private RoundedImageView iv_my_avatar;
    private TextView tv_nickname_hint;

    //账号安全
    private RelativeLayout rlAccountSafe;
    private TextView tvAccountSafeHint;

    //切换账号
    private Button btnSwitchAccount;

    //高清大图提示
    private ImageView iv_hd_promotion;

    public MyProfileActivity.OnEditPersonalListener onEditPersonalListener;

    // 头像是否发生变化
    private boolean isImageChange = false;
    private boolean isCancelUploadImage = false;
    /**
     * 旧头像URL
     */
    private String oldHeaderPic;
    /**
     * 新头像URL
     */
    private String fileNameNew;

    private DataSaveHelper saveHelper;
    private UserController userController;

    public MyProfileController(Activity activit) {
        mActivity = activit;
       // loginController = new MyLoginController(mActivity);
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    public void initView() {
        mSaver = DataSaveHelper.getInstance(mActivity.getApplicationContext());
        findView();
        fillView();
    }

    public void findView() {
        tv_nickname_hint = (TextView) findViewById(R.id.tv_nickname_hint);
        iv_my_avatar = (RoundedImageView) findViewById(R.id.iv_my_avatar);
        iv_hd_promotion = (ImageView) findViewById(R.id.iv_hd_promotion);

        iv_hd_promotion.setVisibility(View.GONE);

        rlAccountSafe = (RelativeLayout) findViewById(R.id.rl_account_safe);
        tvAccountSafeHint = (TextView) findViewById(R.id.tv_account_safe_hint);

        btnSwitchAccount = (Button) findViewById(R.id.btn_my_switch_login);

        setListener();
    }

    private void setListener() {
        onEditPersonalListener = MyProfileActivity.listener;

        findViewById(R.id.rl_nickname).setOnClickListener(this);
        findViewById(R.id.rl_my_avatar).setOnClickListener(this);
        findViewById(R.id.rl_my_more).setOnClickListener(this);

        iv_my_avatar.setOnClickListener(this);
        btnSwitchAccount.setOnClickListener(this);

        rlAccountSafe.setOnClickListener(this);
    }

    public void fillView() {
        try {
            showPhotoPicker();
            saveHelper = DataSaveHelper.getInstance(mActivity);
            userController = UserController.getInstance();
           // oldHeaderPic = saveHelper.getUserHeadPic();
            if (true) {
                //has Login
                btnSwitchAccount.setVisibility(View.VISIBLE);
                String username = "nickname";
                if (!StringUtils.isNull(username)) {
                    tv_nickname_hint.setText(username);
                } else {
                    tv_nickname_hint.setText("请先设置你的昵称哦~");
                }
                //set avatar
                UserPhotoManager.getInstance().showMyPhoto(mActivity, iv_my_avatar,
                        R.drawable.apk_mine_photo, false,
                        new OnPhotoHDListener() {
                            @Override
                            public void onResult(boolean bHD) {
                                UserPhotoManager.getInstance().setHDUserPhoto(mActivity, bHD);
                                showHDImagePromotion();
                            }
                        });
//                handleAccountHintText();
//                BindingController.getInstance(mActivity.getApplicationContext()).getUserBindingInfo(mActivity, new Callback() {
//                    @Override
//                    public void call() {
//                        handleAccountHintText();// 处理美柚账号登录还是第三方登录
//                    }
//                });
                MyHeaderController.bindVipIcon(mActivity, iv_my_avatar);
                //request BindingInfo
             //   BindingController.getInstance(mActivity).getUserBindingInfo(mActivity, null);
            } else {
                tvAccountSafeHint.setText("");
                tv_nickname_hint.setHint("未登录");
                btnSwitchAccount.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.rl_my_avatar || i == R.id.iv_my_avatar) {
            getPhoto();
        }
    }



    private void showPhotoPicker() {
        //进入用户没有头像跳转后，弹出相机选择框
        Intent intent = mActivity.getIntent();
        boolean isShowPhotoPicker = intent.getBooleanExtra("isShow", false);
        if (isShowPhotoPicker) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    getPhoto();
                }

            }, 500);
        }
    }

    /**
     * 是否显示高清图提示
     */
    private void showHDImagePromotion() {
        try {
            // 如果是高清
            if (!UserPhotoManager.getInstance().isHDUserPhoto(mActivity)) {
                iv_hd_promotion.setVisibility(View.VISIBLE);
                // 非高清
            } else {
                iv_hd_promotion.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取图片
     */
    private void getPhoto() {
        // 选图
        PhotoActivity.enterActivity(mActivity,
                new ArrayList<PhotoModel>(),
                new PhotoConfig(1, true, UserController.getInstance().getUserId(mActivity), "profile"), new OnSelectPhotoListener() {
                    @Override
                    public void onCancel() {
                        LogUtils.i(TAG, "操作取消");
                    }

                    @Override
                    public void onResultSelect(List<PhotoModel> listSelected) {
                        if (listSelected == null || listSelected.size() == 0) {
                            LogUtils.i(TAG, "返回大小为：0");
                            return;
                        }
                        iv_my_avatar.setVisibility(View.VISIBLE);
                        final PhotoModel photoModel = listSelected.get(0);
                        LogUtils.i(TAG, "加载图片：" + photoModel.UrlThumbnail);
                        ImageLoader.getInstance().loadImage(mActivity, photoModel.UrlThumbnail, 0, 0, new ImageLoader.onCallBack() {
                            @Override
                            public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                                if (bitmap == null) {
                                    LogUtils.i(TAG, "加载图片失败：" + photoModel.UrlThumbnail);
                                    return;
                                }
                                BitmapUtil.setRoundImageView(iv_my_avatar, bitmap);
                                // 大图还是小图
                                UserPhotoManager userPhotoManager = UserPhotoManager.getInstance();
                                if (userPhotoManager.isBigUserPhoto(bitmap)) {
                                    userPhotoManager.setHDUserPhoto(mActivity, true);
                                } else {
                                    userPhotoManager.setHDUserPhoto(mActivity, false);
                                }
                                showHDImagePromotion();
                            }

                            @Override
                            public void onFail(String url, Object... obj) {

                            }

                            @Override
                            public void onProgress(int total, int progess) {

                            }

                            @Override
                            public void onExtend(Object... object) {

                            }
                        });
                    }

                    @Override
                    public void onResultSelectCompressPath(List<String> compressPath) {
                        try {
                            if (compressPath == null || compressPath.size() == 0)
                                return;
                            String uri = compressPath.get(0);
                            File file = new File(uri);
                            fileNameNew = file.getName();

                            if (!TextUtils.isEmpty(fileNameNew)) {
                                UserPhotoManager.getInstance().setUserPhotoUpdateSuccess(mActivity, false);
                               // final String oldHeaderPic = saveHelper.getUserHeadPic();
                                isImageChange = !StringUtils.isEqual(oldHeaderPic, fileNameNew) && fileNameNew != null;
                               // saveHelper.setUserHeadPic(fileNameNew);
                                if (onEditPersonalListener != null) {
                                    onEditPersonalListener.onEditImage(uri);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
    }




    /**
     * 统一提交
     */
    public void handleComplete() {
        String name = tv_nickname_hint.getText().toString();
        if (!name.equals("")) {
          //  saveHelper.setUserName(name);
        }

        // 头像变化
//        if (isImageChange) {
//            PhoneProgressDialog.showRoundDialog(mActivity, "上传中", new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    isCancelUploadImage = true;
//                    saveHelper.setUserHeadPic(oldHeaderPic);
//                }
//            });
//            saveHelper.setUserHeadPic(fileNameNew);
//
//            // 上传成功后
//            // 本地头像名称被重置为服务器给的头像名称，本地图片已被更换
//            UserPhotoManager.getInstance().uploadUserPhoto(mActivity, fileNameNew, oldHeaderPic, new UserPhotoManager.OnUserPhotoListener() {
//                @Override
//                public void onResult(final boolean bsuccess) {
//                    mActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                PhoneProgressDialog.dismissDialog(mActivity);
//                                if (bsuccess) {
//                                    if (!isCancelUploadImage) {
//                                        ToastUtils.showToast(mActivity, "上传成功");
//                                        AnalysisClickAgent.onEvent(mActivity, "txsccg");
//                                        mActivity.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                ExtendOperationController.getInstance().notify(OperationKey.PROFILE_CHANGE, "");
//                                                ExtendOperationController.getInstance().notify(OperationKey.SWITCH_PHOTO_CHANGE, "");
//                                                finish();
//                                            }
//                                        });
//                                    } else {
//                                        saveHelper.setUserHeadPic(oldHeaderPic);
//                                    }
//                                    UserSyncManager.getInstance().syncUserConfig2Server();
//                                    //new SynchroController(mActivity).SyncUserConfig2Server(null, SynchroController.TYPE_ALL);
//                                    if (isCancelUploadImage) {
//                                        finish();
//                                    }
//                                } else {
//                                    ToastUtils.showToast(mActivity, "上传失败");
//                                    saveHelper.setUserHeadPic(oldHeaderPic);
//                                    UserSyncManager.getInstance().syncUserConfig2Server();
//                                }
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
//            });
//        } else {
//            UserSyncManager.getInstance().syncUserConfig2Server();
//            ExtendOperationController.getInstance().notify(OperationKey.PROFILE_CHANGE, "");
//            finish();
//        }
    }

    public void finish() {
        mActivity.finish();
    }


}
