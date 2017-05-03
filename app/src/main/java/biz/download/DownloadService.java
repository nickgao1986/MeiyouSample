package biz.download;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import nickgao.com.meiyousample.utils.LogUtils;

/**
 * 下载服务
 * Created by lwh on 2015/9/15.
 */
public class DownloadService extends Service {
    private static final String TAG = "DownloadService";

    //service状态
    public static final String DATA = "data";
    public static final String PAUSE = "pause";
    public static final String STOP = "stop";

    //通知栏
    private NotifycationManager notifycationManager;

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(TAG, "-->onBind ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "-->onCreate ");
        notifycationManager = new NotifycationManager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        try {
//            Log.w(TAG,"-->onStartCommand ");
//            if(intent==null){
//                return super.onStartCommand(intent, flags, startId);
//            }
//            Log.w(TAG,"-->onStartCommand start");
//            //get data
//            final DownloadConfig downloadConfig = (DownloadConfig)intent.getSerializableExtra(DATA);
//            boolean is_stop = intent.getBooleanExtra(STOP, false);
//            boolean is_pause = intent.getBooleanExtra(PAUSE, false);
//            //start download
//            if(downloadConfig!=null){
//                //start
//                if(!is_stop && !is_pause){
//                    if(TextUtils.isEmpty(downloadConfig.dirPath)){
//                        downloadConfig.dirPath= CacheDisc.getCacheFile(getApplicationContext());
//                    }
//                    if(!TextUtils.isEmpty(downloadConfig.url) && !TextUtils.isEmpty(downloadConfig.dirPath)){
//                        Log.w(TAG,"-->onStartCommand dlStart");
//                        DLManager.getInstance(getApplicationContext()).dlStart(downloadConfig.url,downloadConfig.ip,downloadConfig.dirPath,downloadConfig.isForceReDownload,new DLTaskListener(){
//                            @Override
//                            public void onStart(String fileName, String url) {
//                                super.onStart(fileName, url);
//                                LogUtils.d("-->onStart url:" + url + "--filename:" + fileName);
//                                if(downloadConfig.isBrocastProgress){
//                                    downloadConfig.progress=5;
//                                    sendBrocast(downloadConfig,String.valueOf(DownloadStatus.DOWNLOAD_ING.value()));
//                                }
//                                if(!downloadConfig.isShowNotificationProgress)
//                                    return;
//                                Message message = new Message();
//                                message.what = DownloadStatus.DOWNLOAD_ING.value();
//                                message.obj = downloadConfig;
//                                message.arg1  = 5;
//                                updateHandler.sendMessage(message);
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                super.onError(error);
//                                LogUtils.d("-->onError:" + error);
//                                if(downloadConfig.isBrocastProgress){
//                                    sendBrocast(downloadConfig,String.valueOf(DownloadStatus.DOWNLOAD_FAIL.value()));
//                                }
//
//                                if(!downloadConfig.isShowNotificationProgress)
//                                    return;
//                                Message message = new Message();
//                                message.what = DownloadStatus.DOWNLOAD_FAIL.value();
//                                message.obj = downloadConfig;
//                                updateHandler.sendMessage(message);
//                            }
//
//                            @Override
//                            public boolean onConnect(int type, String msg) {
//                                LogUtils.d("-->onConnect type:" + type + "--msg:" + type);
//                                return super.onConnect(type, msg);
//                            }
//
//
//                            @Override
//                            public void onProgress(int progress) {
//                                //builder.setProgress(100, progress, false);
//                                //nm.notify(id, builder.build());
//                                LogUtils.d("-->onProgress progress:" + progress);
//                                if(downloadConfig.isBrocastProgress){
//                                    downloadConfig.progress = progress;
//                                    sendBrocast(downloadConfig,String.valueOf(DownloadStatus.DOWNLOAD_ING.value()));
//                                }
//                                if(!downloadConfig.isShowNotificationProgress || progress>=100)
//                                    return;
//                                Message message = new Message();
//                                message.what = DownloadStatus.DOWNLOAD_ING.value();
//                                message.obj = downloadConfig;
//                                message.arg1  = progress;
//                                updateHandler.sendMessage(message);
//                            }
//
//                            @Override
//                            public void onFinish(File file) {
//                                LogUtils.d("-->onFinish file:" + file.getAbsolutePath());
//                                downloadConfig.file = file;
//                                if(downloadConfig.isBrocastProgress){
//                                    sendBrocast(downloadConfig,String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.value()));
//                                }
//                                if(!downloadConfig.isShowNotificationProgress)
//                                    return;
//                                Message message = new Message();
//                                message.what = DownloadStatus.DOWNLOAD_COMPLETE.value();
//                                message.obj = downloadConfig;
//                                updateHandler.sendMessage(message);
//
//
//                                //LogUtils.i("onFinish");
//                                //nm.cancel(id);
//                            }
//                        });
//                    }else{
//                        Log.w(TAG,"params for start download is not suitable");
//                    }
//                //stop
//                }else if(is_stop){
//                    if(!TextUtils.isEmpty(downloadConfig.url)){
//                        DLManager.getInstance(getApplicationContext()).dlCancel(downloadConfig.url);
//                    }else{
//                        Log.w(TAG,"params for stop download is not suitable");
//                    }
//                //pause
//                }else if(is_pause){
//                    if(!TextUtils.isEmpty(downloadConfig.url)){
//                        DLManager.getInstance(getApplicationContext()).dlStop(downloadConfig.url);
//                    }else{
//                        Log.w(TAG,"params for pause download is not suitable");
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 发送进度广播
     * @param downloadConfig
     * @param action
     */
    private void sendBrocast(DownloadConfig downloadConfig,String action){
        Intent intent = new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DownloadService.DATA, downloadConfig);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @SuppressLint("HandlerLeak")
    private Handler updateHandler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            try {
                if(msg.what==DownloadStatus.DOWNLOAD_ING.value()){
                    DownloadConfig downloadConfig= (DownloadConfig)msg.obj;
                    int progress = msg.arg1;
                    notifycationManager.showNotification(downloadConfig,progress,true);
                }else if(msg.what==DownloadStatus.DOWNLOAD_COMPLETE.value()){
                    DownloadConfig downloadConfig= (DownloadConfig)msg.obj;
                    notifycationManager.showNotification(downloadConfig,100);
                    //点击安装PendingIntent
                    if(downloadConfig != null && downloadConfig.installApkAfterDownload && downloadConfig.file != null && downloadConfig.file.getAbsolutePath().contains("apk")){
                        boolean isExist = downloadConfig.file.exists();
                        LogUtils.d(TAG,"DOWNLOAD FINISH isExist:"+isExist+"--->path:"+downloadConfig.file.getAbsolutePath());
                        Uri uri = Uri.fromFile(downloadConfig.file);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(installIntent);
                        notifycationManager.cacleNotification(downloadConfig);
                    }
                    //TODO 删除文件，测试专用，必须去除
                    //downloadConfig.file.delete();
                }else if(msg.what==DownloadStatus.DOWNLOAD_FAIL.value()){
                    DownloadConfig downloadConfig= (DownloadConfig)msg.obj;
                    notifycationManager.updateNotification(downloadConfig, -1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
