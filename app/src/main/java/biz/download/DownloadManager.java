package biz.download;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;


import java.io.File;

/**
 * 下载控制类
 * Created by lwh on 2015/9/15.
 */
public class DownloadManager {
    static class InstanceHolder {
        static DownloadManager instance = new DownloadManager();
    }
    public static DownloadManager getInstance() {
        return InstanceHolder.instance;
    }
    /**
     * 开始下载
     * @param context
     * @param downloadModel
     */
    public  void start(Context context,DownloadConfig downloadModel) {
        try {
            if (!hasWriteExternalStoragePermission(context)) {
                return;
            }

            //发个下载广播给外界
            Intent intentBrocast = new Intent();
            intentBrocast.setAction(String.valueOf(DownloadStatus.DOWNLOAD_START.value()));
            Bundle bundleBrocast = new Bundle();
            bundleBrocast.putSerializable(DownloadService.DATA, downloadModel);
            intentBrocast.putExtras(bundleBrocast);
            context.sendBroadcast(intentBrocast);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //启动下载
        Intent intent = new Intent(context, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DownloadService.DATA, downloadModel);
        intent.putExtras(bundle);
        intent.putExtra(DownloadService.PAUSE, false);
        intent.putExtra(DownloadService.STOP, false);
        context.startService(intent);

    }

    /**
     * 暂停下载
     * @param context
     * @param downloadModel
     */
    public  void pause(Context context,DownloadConfig downloadModel) {
        Intent intent = new Intent(context, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DownloadService.DATA, downloadModel);
        intent.putExtras(bundle);
        intent.putExtra(DownloadService.PAUSE, true);
        intent.putExtra(DownloadService.STOP,false);
        context.startService(intent);
    }

    /**
     * 停止下载
     * @param context
     * @param downloadModel
     */
    public  void stop(Context context,DownloadConfig downloadModel) {
        Intent intent = new Intent(context, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DownloadService.DATA, downloadModel);
        intent.putExtra(DownloadService.PAUSE, false);
        intent.putExtra(DownloadService.STOP, true);
        intent.putExtras(bundle);
        context.startService(intent);
    }


    public File start(Context context,String url,String dirPath,boolean isReloadIfLocalExist) {
        if (!hasWriteExternalStoragePermission(context)) {
            return null;
        }
        //return DLManager.getInstance(context).dlStart(url,dirPath,isReloadIfLocalExist);
        return null;
    }

    private boolean hasWriteExternalStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
        }
    }

}
