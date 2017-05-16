package com.lingan.seeyou.ui.view.photo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lingan.seeyou.ui.view.RoundedImageView;
import com.meetyou.crsdk.util.ImageLoader;

import biz.util.BitmapUtil;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.statusbar.Pref;

/**
 * Created by gaoyoujian on 2017/5/3.
 */

public class UserPhotoManager {

    private static final String TAG = "UserPhotoManager";
    private boolean bHandlingPhoto = false;
    public static final String URL_AVATOR_MP = "www.mp.meiyou.com/";
    public static final String URL_AVATOR_FLAG = "?";
    private static UserPhotoManager userPhotoManager;

    public UserPhotoManager() {
    }

    public static UserPhotoManager getInstance() {
        if(userPhotoManager == null) {
            userPhotoManager = new UserPhotoManager();
        }

        return userPhotoManager;
    }


    public boolean isUserPhotoUpdateSuccess(Context context) {
        return Pref.getBoolean(context, "is_upload_success_129746620", false);
    }

    public void setUserPhotoUpdateSuccess(Context context, boolean flag) {
        Pref.saveBoolean(context, "is_upload_success_129746620", flag);
    }

    public boolean isHDUserPhoto(Context context) {
        return Pref.getBoolean(context, "is_big_user_photo_129746620", false);
    }

    public void setHDUserPhoto(Context context, boolean flag) {
        Pref.saveBoolean(context, "is_big_user_photo_129746620", flag);
    }

    public boolean isBigUserPhoto(int width, int height) {
        try {
            if(height >= 200 || width >= 200) {
                return true;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return false;
    }

    public boolean isBigUserPhoto(Bitmap bitmap) {
        if(bitmap == null) {
            return false;
        } else {
            LogUtils.i("UserPhotoManager", "isBigUserPhoto width:" + bitmap.getWidth() + "-->height:" + bitmap.getHeight());
            return this.isBigUserPhoto(bitmap.getHeight(), bitmap.getWidth());
        }
    }

    public void showMyPhoto(final Activity activity, final RoundedImageView imageView, final int defaultPhoto, boolean bReloadeFromCache, final OnPhotoHDListener listener) {
        try {
            if(this.bHandlingPhoto) {
                return;
            }

            this.bHandlingPhoto = true;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    imageView.setCornerRadius(10.0F);
                    imageView.setOval(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    String photoname = "aaa";
                    if(StringUtils.isNull(photoname)) {
                        imageView.setImageResource(defaultPhoto);
                       // UserPhotoManager.this.showMyPhotoFinish(listener);
                    } else {
                        String photoNameLocale = photoname;
                        if(photoname.contains("?")) {
                            photoNameLocale = UserPhotoManager.url2FileName(photoname);
                        }

                        Context context = activity.getApplicationContext();
                        String localPathName = ImageUploaderUtil.getHeadPicLocalFilePath(context, photoNameLocale);
                        LogUtils.i("UserPhotoManager", "------------>showMyPhoto  localPathName:" + localPathName);
                        Bitmap bitmapTwo = BitmapUtil.getBitmapByLocalPathName(context, localPathName, ImageLoader.getRoundImageWH(context), ImageLoader.getRoundImageWH(context));
                        if(bitmapTwo != null) {
                            LogUtils.i("UserPhotoManager", "------------>showMyPhoto  获取到本地缓存图片");
                            imageView.setImageBitmap(bitmapTwo);
                            //UserPhotoManager.this.showMyPhotoFinish(listener);
                        } else {
                            imageView.setImageResource(defaultPhoto);
                            LogUtils.i("UserPhotoManager", "------------>showMyPhoto  获取不到本地缓存图片，需要重新下载");
//                            if(!NetWorkStatusUtil.hasNetWork(context)) {
//                                UserPhotoManager.this.showMyPhotoFinish(listener);
//                                return;
//                            }

//                            UserPhotoManager.this.handleReloadUserImage(context, new UserPhotoManager.OnReLoadListener() {
//                                public void onComplete() {
//                                    UserPhotoManager.this.showMyPhotoFinish(listener);
//                                }
//                            });
                        }

                    }
                }
            });
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static String url2FileName(String url) {
        return url.replaceAll("[^\\w]", "");
    }

}
