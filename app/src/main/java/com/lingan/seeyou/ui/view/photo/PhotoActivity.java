package com.lingan.seeyou.ui.view.photo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.OnSaveBitmapListener;
import com.lingan.seeyou.ui.view.dialog.bottomdialog.BottomMenuDialog;
import com.lingan.seeyou.ui.view.dialog.bottomdialog.BottomMenuModel;
import com.lingan.seeyou.ui.view.dialog.bottomdialog.PhoneProgressDialog;
import com.lingan.seeyou.ui.view.photo.model.PhotoConfig;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;
import com.meetyou.crsdk.util.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import activity.LinganActivity;
import biz.util.BitmapUtil;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.CacheDisc;
import nickgao.com.meiyousample.skin.ToastUtils;
import nickgao.com.okhttpexample.view.AbstractImageLoader;
import nickgao.com.okhttpexample.view.ImageLoadParams;

/**
 * 通用获取图片入口
 */
public class PhotoActivity extends LinganActivity {
    private static final String TAG = "PhotoActivity";

    private final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private final int MY_PERMISSIONS_REQUEST_CAMARE = 1001;
    private static final String CAMERA = "拍照";
    private static final String PHOTO = "从手机相册选择";
    private TextView tvTitle;
    private boolean mIsCancle = true;


    private String FILE_NAME = "BitmapCache.jpg";

    private File fileCamera = null;

    /**
     *
     * 获取拍照的默认输出文件对象
     *
     * @return
     *
     *         private File getDefaultCameraOutputFile(Context context) {
     *         if(fileCamera==null) fileCamera= new
     *         File(CacheDisc.getCacheFile(context), strFileName); return
     *         fileCamera; }
     */
    /**
     * /** 设置选中的照片
     *
     * @param listLastSelectedPhoto
     * <p>
     * public void setSelectedPhoto(List<PhotoModel>
     * listLastSelectedPhoto) { listPhotoModel.clear();
     * listPhotoModel.addAll(listLastSelectedPhoto); }
     * <p>
     * /** 设置 选择图片的监听器
     * <p>
     * public void setOnSelectPhotoListener(OnSelectPhotoListener
     * listener) { mPhotoLister = listener; }
     */

    private static List<PhotoModel> listPhotoModel = new ArrayList<PhotoModel>();
    //不压缩的视图
    private List<PhotoModel> listPhotoModelUnCompreesd = new ArrayList<PhotoModel>();
    // 从相册中选中了图片
    private PhotoAlumbListener mPhotoAlumbLister = new PhotoAlumbListener();

    private static int mMaxCount = 9;
    private static boolean bCrop = false;// 是否裁剪
    public static OnSelectPhotoListener mPhotoLister;
    public static OnAnalyzeListener mAnalyzeListener;
    private static boolean bShowTitle = false;
    private static String titleContent = "";
    /**
     * 是否直接进入相册
     */
    private static boolean isOnlyEnableGallery = false;
    private static PhotoConfig mPhotoConfig;


    private static long userId;

    /**
     * 外部调用入口
     *
     * @param context
     * @param listLastSelectedPhoto 已经选择的图片
     * @param config
     * @param listener              选择成功后回调
     */
    public static void enterActivity(Context context, List<PhotoModel> listLastSelectedPhoto, PhotoConfig config, OnSelectPhotoListener listener) {

        if (listLastSelectedPhoto != null) {
            listPhotoModel.clear();
            listPhotoModel.addAll(listLastSelectedPhoto);
        }
        mPhotoConfig = config;
        mMaxCount = config.getMaxCount();
        bCrop = config.isCrop();
        userId = config.getUserId();
        bShowTitle = config.isShowTitle;
        titleContent = config.title;
        isOnlyEnableGallery = config.isOnlyEnableGallery;
        mPhotoLister = listener;
        doIntent(context, PhotoActivity.class);
    }

    /**
     * @param analyzeListener report analyze event
     */
    public static void enterActivity(Context context, List<PhotoModel> listLastSelectedPhoto, PhotoConfig config, OnSelectPhotoListener listener, OnAnalyzeListener analyzeListener) {
        mAnalyzeListener = analyzeListener;
        enterActivity(context, listLastSelectedPhoto, config, listener);
    }

    public static void doIntent(Context mContext, Class<?> mClass) {
        Intent intent = new Intent();
        intent.setClass(mContext, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT) {
            // overridePendingTransition(R.anim.dialog_bottom_come,R.anim.dialog_bottom_go);
            overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_animation_none);
        }
        // getWindow().setBackgroundDrawableResource();
        setContentView(R.layout.layout_photo_new);
        titleBarCommon.setCustomTitleBar(-1);
        initFile();
        LogUtils.d(TAG, "----fileCamera:" + fileCamera.getAbsolutePath());
        tvTitle = (TextView) findViewById(R.id.dialog_upload_title);
        if (bShowTitle) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(titleContent);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        initLogic();
        /*
        findViewById(R.id.rootView).setOnClickListener(this);
        findViewById(R.id.dialog_upload_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_upload_camera).setOnClickListener(this);
        findViewById(R.id.dialog_upload_sdcard).setOnClickListener(this);
        */
        List<BottomMenuModel> bottomMenuModelList = new ArrayList<BottomMenuModel>();

        if (mPhotoConfig != null && !StringUtils.isNull(mPhotoConfig.getMenuItem()) && mPhotoConfig.getMenuItemListener() != null) {

            BottomMenuModel bottomMenuModel = new BottomMenuModel();
            bottomMenuModel.title = mPhotoConfig.getMenuItem();
            bottomMenuModelList.add(bottomMenuModel);
        }

        if ((mPhotoConfig != null && mPhotoConfig.isEnablePhotoOption()) || mPhotoConfig == null) {
            BottomMenuModel bottomMenuModel = new BottomMenuModel();
            bottomMenuModel.title = PHOTO;
            bottomMenuModelList.add(bottomMenuModel);
        }

        BottomMenuModel bottomMenuModel2 = new BottomMenuModel();
        bottomMenuModel2.title = CAMERA;
        bottomMenuModelList.add(bottomMenuModel2);

        final BottomMenuDialog bottomMenuDialog = new BottomMenuDialog(this,
                bottomMenuModelList);

        bottomMenuDialog.setOnAnalyzeListener(new BottomMenuDialog.OnAnalyzeListener() {
            @Override
            public void onEvent(int index) {
                if (null != mAnalyzeListener) {
                    mAnalyzeListener.onEvent(index);
                }
            }
        });

        bottomMenuDialog.setOnMenuSelectListener(new BottomMenuDialog.OnMenuSelectListener() {
            @Override
            public void OnSelect(int index, String title) {

                if (mPhotoConfig != null && !StringUtils.isNull(mPhotoConfig.getMenuItem()) && mPhotoConfig
                        .getMenuItemListener() != null && title.equals(mPhotoConfig.getMenuItem())) {

                    mPhotoConfig.getMenuItemListener().onClick(new View(getApplicationContext()));
                    mIsCancle = true;
                    bottomMenuDialog.dismiss();
                    finish();

                } else if (title.equals(PHOTO)) {
                    pickPhoto();
                } else if (title.equals(CAMERA)) {
                    getPhotoFromCamara();
                    mIsCancle = false;
                } else {
                    mIsCancle = true;
                    bottomMenuDialog.dismiss();
                    finish();
                }
            }
        });
        bottomMenuDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                mIsCancle = false;//防止onDismiss被再次出发
                if (mPhotoLister != null) {
                    mPhotoLister.onCancel();
                }
                finish();
            }
        });
        bottomMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mIsCancle) {
                    if (mPhotoLister != null) {
                        mPhotoLister.onCancel();
                    }
                }
            }
        });
        bottomMenuDialog.show();

//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
//                permissions, new PermissionsResultAction() {
//
//                    @Override
//                    public void onGranted() {
////                        writeToStorage();
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
////                        Toast.makeText(MainActivity.this,
////                                "Sorry, we need the Storage Permission to do that",
////                                Toast.LENGTH_SHORT).show();
//                    }
//                });
        //初始化相册，不需要外部调用；
        PhotoController.getInstance(this).init(this);
    }

    private void pickPhoto() {
        mIsCancle = false;
        listPhotoModelUnCompreesd.clear();
        listPhotoModelUnCompreesd.addAll(listPhotoModel);
        PhotoController.getInstance(getApplicationContext())
                .startPicking(listPhotoModel, mMaxCount, bCrop, mPhotoAlumbLister, userId);

    }

    private void initLogic() {
        if (mPhotoConfig == null) {
            mPhotoConfig = new PhotoConfig();
        }
        if (mPhotoConfig.isOnlyEnableGallery) {
            pickPhoto();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initFile() {
        if (fileCamera != null && fileCamera.exists()) {
            fileCamera.delete();
        }
        String strFileName = System.currentTimeMillis() + FILE_NAME;
        fileCamera = new File(CacheDisc.getCacheFile(getApplicationContext()), strFileName);
        if (fileCamera != null && fileCamera.exists()) {
            fileCamera.delete();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPhotoLister != null) {
            mPhotoLister.onCancel();
        }

    }


    private class PhotoAlumbListener implements
            PhotoController.OnFinishPickingListener {
        @Override
        public void OnFinish(boolean isCancel, List<PhotoModel> listPhoto) {
            try {
                if (!isCancel) {
                    if (listPhoto != null && listPhoto.size() > 0) {
                        // 获取选中项
                        listPhotoModel.clear();
                        listPhotoModel.addAll(listPhoto);
                        LogUtils.d(TAG, "选完图片，总共有：" + listPhotoModel.size());
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onResultSelect(listPhotoModel);
                        }
                        // 构造需要压缩的图片
                        final int size = listPhotoModel.size();
                        final List<String> listUrlWanttoCompress = new ArrayList<String>();
                        for (int i = 0; i < size; i++) {
                            PhotoModel photoModel = listPhoto.get(i);
                            String url = photoModel.Url;
                            String thumbUrl = photoModel.UrlThumbnail;
                            listUrlWanttoCompress.add(url);

                            LogUtils.d(TAG, "缩略图：" + thumbUrl + "\n------>原图：" + url);
                            //add gif 图片同时 没有已经在 不压缩里面
                            if (GifUtil.isGif(url) && !listPhotoModelUnCompreesd.contains(photoModel)) {
                                listPhotoModelUnCompreesd.add(photoModel);
                            }
                        }
                        // 对话框
                        final PhoneProgressDialog phoneProgressDialog = new PhoneProgressDialog();
                        final PhotoActivity context = PhotoActivity.this;
                        if (listUrlWanttoCompress.size() > 1) {
                            phoneProgressDialog.showRoundDialog(context,
                                    "请稍候...", new OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            finish();
                                        }
                                    });
                        }
                        final List<String> listCompressResult = new ArrayList<>(size);
                        final String[] arrayCompress = new String[size];
                        // 压缩
                        OnSaveBitmapListListener onSaveBitmapListListener = new OnSaveBitmapListListener() {
                            @Override
                            public void OnSaveResult(boolean success, int position, final String strSaveFileName) {
                                try {
                                    //文件名
//                                String strSaveFileName = new File(url).getName();
//                                A context = getApplicationContext();
                                    if (!success) {
                                        LogUtils.d(TAG, "压缩失败，退出页面：" + strSaveFileName);
                                        phoneProgressDialog.dismissDialog(context);
                                        // 页面消失
                                        finish();
                                        return;
                                    }
                                    if (GifUtil.isGif(strSaveFileName)) {
                                        //Gif不压缩,地址在图片的压缩路径下;
                                        arrayCompress[position] = ImageUploaderUtil.getCompressPhotoPath(context, strSaveFileName);
                                    } else {
                                        final String pathname = ImageUploaderUtil.getCompressPhotoPath(context, strSaveFileName);
                                        LogUtils.d(TAG, "压缩后图片：" + pathname);
                                        arrayCompress[position] = pathname;
                                    }

                                    //图片全部压缩完
                                    boolean isFull = true;
                                    for (String arrayCompres : arrayCompress) {
                                        if (StringUtils.isNull(arrayCompres)) {
                                            isFull = false;
                                            break;
                                        }
                                    }
                                    LogUtils.d(TAG, "isFull：" + isFull);
                                    // 回调
                                    if (isFull) {
                                        Collections.addAll(listCompressResult, arrayCompress);
                                        PhoneProgressDialog.dismissDialog(context);
                                        if (mPhotoLister != null) {
                                            mPhotoLister.onResultSelectCompressPath(listCompressResult);
                                        } else {
                                            LogUtils.d(TAG, "mPhotoLister null");
                                        }
                                        mPhotoLister = null;
                                        // 页面消失
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        };

                        PhotoController.getInstance(context)
                                .compressFileList(context, mPhotoConfig != null ? mPhotoConfig.mPrefix : "", listUrlWanttoCompress, listPhotoModelUnCompreesd, onSaveBitmapListListener, userId);
                    } else {
                        onCancel();
                    }
                } else {
                    onCancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onCancel() {
        // 通知外部
        if (mPhotoLister != null) {
            mPhotoLister.onCancel();
        }
        // 页面消失
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onSaveInstanceState");
        try {
            savedInstanceState.putSerializable("fileCamera", fileCamera);
            savedInstanceState.putSerializable("bCrop", bCrop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.d(TAG, "onRestoreInstanceState");
        try {
            fileCamera = (File) savedInstanceState.getSerializable("fileCamera");
            bCrop = savedInstanceState.getBoolean("bCrop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bCrop = false;
        bShowTitle = false;
        fileCamera = null;
        mPhotoConfig = null;
        mPhotoLister = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 拍照
            case PHOTO_REQUEST_TAKEPHOTO:
                LogUtils.d(TAG, "---onActivityResult PHOTO_REQUEST_TAKEPHOTO resultCode:"
                        + resultCode + "--->requestCode:" + requestCode + "   bCrop: " + bCrop);
                if (resultCode == RESULT_OK) {
                    if (!bCrop) {
                        fillBitmapByUri(fileCamera);
                    } else {
                        LogUtils.d(TAG, "fileCamera path:" + fileCamera.getAbsolutePath());
                        doClipFile(fileCamera);
                    }
                } else {
                    finish();
                    try {
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onCancel();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                break;
            // 从相册选择
            case PHOTO_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Uri uri = data.getData();
                        // File file =
                        // getDefaultCameraOutputFile(getApplicationContext());
                        // fillBitmapByUri(file);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleBitmap(Bitmap bitmap) {
        try {
            if (bitmap == null)
                return;
            PhoneProgressDialog.showRoundDialog(PhotoActivity.this, "请稍候...", null);
            final String filename = ImageUploaderUtil.createSaveFileName(mPhotoConfig.mPrefix, bitmap, userId);
            final Context context = getApplicationContext();
            OnSaveBitmapListener onSaveBitmapListener = new OnSaveBitmapListener() {

                @Override
                public void OnSaveResult(
                        boolean success,
                        String strSaveFileName) {
                    PhoneProgressDialog.dismissDialog(PhotoActivity.this);
                    if (success) {

                        final String pathname = ImageUploaderUtil.getCompressPhotoPath(context, filename);
                        LogUtils.d(TAG, "-->fillBitmapByUri pathname：" + pathname);
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.UrlThumbnail = pathname;
                        photoModel.Url = pathname;
                        photoModel.Id = System.currentTimeMillis();
                        photoModel.isTakePhoto = true;
                        photoModel.BucketId = PhotoController.RECENT_BUCKET_ID;
                        photoModel.IsRecent = true;

                        // 需要加入到photos中，保持选中
                        PhotoController.getInstance(context).addNewPhoto(photoModel);

                        // 获取选中项
                        // listPhotoModel.clear();
                        listPhotoModel.add(photoModel);
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onResultSelect(listPhotoModel);
                        }
                        LogUtils.d(TAG, "拍完照，总共有：" + listPhotoModel.size());
                        List<String> listLocalUrl = new ArrayList<String>();
                        for (int i = 0; i < listPhotoModel.size(); i++) {
                            PhotoModel model = listPhotoModel.get(i);
                            if (!StringUtils.isNull(model.compressPath))
                                listLocalUrl.add(model.compressPath);
                        }
                        // 通知外部
                        listLocalUrl.add(pathname);
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onResultSelectCompressPath(listLocalUrl);
                        }
                        PhotoController.getInstance(context).clearSelection();
                        // 页面消失
                        finish();
                    } else {
                        finish();
                        ToastUtils.showToast(PhotoActivity.this, "图片获取失败 1006");
                    }
                }

            };
            PhotoController.getInstance(context)
                    .saveBitmap2FileInThread(this, bitmap, filename, onSaveBitmapListener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 拍照结束,返回的图片处理
     *
     * @param bitmapFile
     */
    private void fillBitmapByUri(final File bitmapFile) {
        final int angle = BitmapUtil.getBitmapRotatoAngle(bitmapFile);
        LogUtils.d(TAG, "拍完照Uri，：" + bitmapFile.getAbsolutePath()
                + "--->现有照片数量：" + listPhotoModel.size() + "angle:" + angle);
        final ImageLoadParams params = new ImageLoadParams();
        ImageLoader.getInstance()
                .loadImage(getApplicationContext(), bitmapFile.getAbsolutePath(), params, new AbstractImageLoader.onCallBack() {
                    @Override
                    public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                        if (bitmap != null) {
                            handleBitmap(bitmap);
                        } else {
                            ToastUtils.showToast(getApplicationContext(), "图片获取失败 10070");
                            // 页面消失
                            finish();
                        }
                    }

                    @Override
                    public void onFail(String url, Object... obj) {
                        Bitmap bitmap = BitmapUtil.getBitmapByLocalPathName(getApplicationContext(), bitmapFile
                                .getAbsolutePath());
                        if (bitmap != null) {
                            handleBitmap(bitmap);
                        } else {
                            ToastUtils.showToast(getApplicationContext(), "图片获取失败 1007");
                        }
                        // 页面消失
                        finish();
                    }

                    @Override
                    public void onProgress(int total, int progess) {

                    }

                    @Override
                    public void onExtend(Object... object) {

                    }
                });


    }

    private void getPhotoFromCamara() {
        /*
         * File file = getDefaultCameraOutputFile(getApplicationContext()); if
		 * (file != null && file.exists()) { file.delete(); }
		 */
        //申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                LogUtils.d(TAG, "没有摄像头权限");
                mIsCancle = true;
                finish();
            } else {
                LogUtils.d(TAG, "有摄像头权限");
                //执行获取权限后的操作
                getPhotoFromCamaraReally();
            }
        } else {
            getPhotoFromCamaraReally();
        }

    }

    private void getPhotoFromCamaraReally() {
        try {
//            if (!FileUtils.isExistSdCard()) {
//                ToastUtils.showToast(getApplicationContext(), "无SD卡，请插入SD卡后再试");
//                this.finish();      //关闭页面，继续操作
//                return;
//            }
            String strFileName = System.currentTimeMillis() + FILE_NAME;
            fileCamera = new File(CacheDisc.getCacheFile(getApplicationContext()), strFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileCamera));
            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtils.showToast(this, "没有相关的应用哦~");
        }
    }

    /**
     * 进入裁剪并保存文件
     *
     * @param file
     */
    private void doClipFile(File file) {
        if (file == null)
            return;
        // 先去裁剪
        ClipImageActivity.enterActivity(this, file.getAbsolutePath(), true,
                0.8,

                new ClipImageActivity.OnClipListener() {

                    @Override
                    public void onCancle() {
                        try {
                            // 通知外部
                            if (mPhotoLister != null) {
                                mPhotoLister.onCancel();
                            }
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onReCamera() {
                        initFile();
                        getPhotoFromCamara();
                    }

                    @Override
                    public void onClipResult(String uri) {
                        LogUtils.d(TAG, "裁剪后图片地址为：" + uri);
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.UrlThumbnail = uri;
                        photoModel.Url = uri;
                        photoModel.Id = System.currentTimeMillis();
                        photoModel.isTakePhoto = true;
                        photoModel.BucketId = PhotoController.RECENT_BUCKET_ID;
                        photoModel.IsRecent = true;
                        // 需要加入到photos中，保持选中
                        PhotoController.getInstance(getApplicationContext())
                                .addNewPhoto(photoModel);
                        // 获取选中项
                        // listPhotoModel.clear();
                        listPhotoModel.add(photoModel);
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onResultSelect(listPhotoModel);
                        }
                        LogUtils.d(TAG, "拍完照，总共有：" + listPhotoModel.size());
                        List<String> listLocalUrl = new ArrayList<String>();
                        for (int i = 0; i < listPhotoModel.size(); i++) {
                            PhotoModel model = listPhotoModel.get(i);
                            if (!StringUtils.isNull(model.compressPath))
                                listLocalUrl.add(model.compressPath);
                        }
                        // 通知外部
                        listLocalUrl.add(uri);
                        // 通知外部
                        if (mPhotoLister != null) {
                            mPhotoLister.onResultSelectCompressPath(listLocalUrl);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                }, userId);
    }


    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(0, 0);
    }
}
