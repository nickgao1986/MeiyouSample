package com.lingan.seeyou.ui.view.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import biz.download.DownloadConfig;
import biz.download.DownloadManager;
import biz.download.DownloadReceiver;
import biz.download.DownloadStatus;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.skin.ToastUtils;

/**
 * 预览图 逻辑控制
 * 保存GIF等
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 17/3/13
 */

public class PreviewImageController {
    private Context context;
    private static final String TAG = "PreviewImageController";

    private static PreviewImageController instance;
    private String downloadUrl = "";
    DownloadReceiver receiver = null;

    public PreviewImageController(Context mContext) {
        this.context = mContext;
        receiver = new DownloadReceiver(context) {
            @Override
            public void onReceive(DownloadStatus downloadStatus, DownloadConfig downloadConfig) {
                // FIXME: 17/3/13 会有问题，连续保存多张无法处理
                if (downloadStatus == DownloadStatus.DOWNLOAD_COMPLETE && downloadConfig.url.equals(downloadUrl)) {
                    File destFile = downloadConfig.file;
                    //新增方法，插入数据库
                    PhotoController
                            .insertPic2Gallery(context, destFile);
                    ToastUtils.showToast(context, "已保存到手机相册");
//                        LogUtils.d(TAG, "保存GIF图片成功：" + destFile.getPath());
                }
            }
        };
    }

    public static PreviewImageController getInstance(Context context) {
        if (instance == null) {
            instance = new PreviewImageController(context);
        }
        return instance;
    }

    /**
     * 保存GIF图片
     *
     * @param uri
     */
    public void saveBitmapGif(final String uri) {
        try {
            downloadUrl = uri;
            //获取文件名
            String fileName = URLUtil.guessFileName(uri, null, null);

            DownloadConfig downloadConfig = new DownloadConfig();
            downloadConfig.isShowNotificationProgress = false;//是否显示通知栏
            downloadConfig.isBrocastProgress = true;//是否需要广播进度

            File destDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            downloadConfig.dirPath = destDir.getAbsolutePath();//下载地址，默认可填写空
            downloadConfig.url = uri; //下载地址
            DownloadManager.getInstance()
                           .start(context, downloadConfig);

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(context, "保存图片失败");
        }

    }

    /**
     * 保存图片
     *
     * @param uri
     */
    public void saveBitmap(String uri) {
        try {
            //兼容本地
            if(!StringUtils.isNull(uri) && uri.startsWith("/")){
                uri = ("file://"+uri);
            }
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(uri))
                    .setProgressiveRenderingEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();

            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
                    .fetchDecodedImage(imageRequest, this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                     @Override
                                     protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                         if(dataSource!=null && dataSource.getFailureCause()!=null)
                                            LogUtils.e(TAG,dataSource.getFailureCause().toString());
                                     }

                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         // You can use the bitmap in only limited ways
                                         // No need to do any cleanup.

                                         if (bitmap == null) {
                                             ToastUtils.showToast(context, "保存图片失败");
                                             return;
                                         }
//                                         if (!FileUtils.isExistSdCard()) {
//                                             ToastUtils.showToast(context, "请插入SD卡后再进行操作");
//                                             return;
//                                         }
                                         ToastUtils.showToast(context, "已保存到手机相册");
                                         PhotoController photoController = PhotoController.getInstance(context);
                                         photoController
                                                 .insertPic2SystemDb(context, bitmap);

                                     }
                                 },
                    CallerThreadExecutor.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onDestroy() {
        //频繁进入退出会报异常；
        try {
            //移除下载监听
            if (receiver != null) {
                receiver.destory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
