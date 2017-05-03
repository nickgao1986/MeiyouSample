package biz.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 下载进度接收器
 * Created by lwh on 2015/9/17.
 */
public abstract class DownloadReceiver extends BroadcastReceiver {

    public DownloadReceiver(){
        super();
    }
    private Context mContext;
    public DownloadReceiver(Context context){
        super();
        mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(String.valueOf(DownloadStatus.DOWNLOAD_START.value()));
        intentFilter.addAction(String.valueOf(DownloadStatus.DOWNLOAD_ING.value()));
        intentFilter.addAction(String.valueOf(DownloadStatus.DOWNLOAD_FAIL.value()));
        intentFilter.addAction(String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.value()));
        context.registerReceiver(this, intentFilter);
    }

    public void destory(){

        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            try{
                if(intent==null || context==null)
                    return;
                String action = intent.getAction();
                DownloadConfig downloadConfig =(DownloadConfig)intent.getSerializableExtra(DownloadService.DATA);
                if(action.equals(String.valueOf(DownloadStatus.DOWNLOAD_ING.value()))){
                    onReceive(DownloadStatus.DOWNLOAD_ING,downloadConfig);
                }else  if(action.equals(String.valueOf(DownloadStatus.DOWNLOAD_FAIL.value()))){
                    onReceive(DownloadStatus.DOWNLOAD_FAIL,downloadConfig);
                }else  if(action.equals(String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.value()))){
                    onReceive(DownloadStatus.DOWNLOAD_COMPLETE,downloadConfig);
                }else if(action.equals(String.valueOf(DownloadStatus.DOWNLOAD_START.value()))){
                    onReceive(DownloadStatus.DOWNLOAD_START,downloadConfig);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
    }
    public abstract void onReceive(DownloadStatus downloadStatus,DownloadConfig downloadConfig);
}
