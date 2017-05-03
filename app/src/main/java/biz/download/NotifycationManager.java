package biz.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by lwh on 2015/9/15.
 */
public class NotifycationManager {
    private Context mContext;
    private NotificationManager notificationManager;
    private HashMap<String, Integer> errorMap = new HashMap<>();
    public NotifycationManager(Context context){
        mContext = context;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     *
     * @param downloadConfig
     * @param progress
     * @param singleNotification        如果存在下载失败的通知栏项，是否移除
     */
    public void showNotification(DownloadConfig downloadConfig,int progress, boolean singleNotification){
//        try {
//            if(isInNotificationList(downloadConfig.url)){
//                //Log.w("DownloadService","url notification is in list url："+downloadConfig.url);
//                updateNotification(downloadConfig,progress);
//                return;
//            }
//            if (singleNotification){
//                Integer id = errorMap.get(downloadConfig.url);
//                if (id != null){
//                    cacleNotification(id.intValue());
//                }
//            }
//            //Notification的滚动提示tickerText1
//            String notify_title = downloadConfig.notify_title;
//            //Notification的图标，一般不要用彩色的
//            int notify_icon_res = downloadConfig.notify_icon_res;//android.R.drawable.stat_sys_download;
//            notify_icon_res =  R.drawable.apk_ico_ad_taskbar;
//            //Notification的Intent，即点击后转向的Activity
//            //Intent notificationIntent = new Intent(mContext, downloadConfig.jumpClass);
//            Intent notificationIntent = new Intent();
//            PendingIntent contentIntent1 = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            if(notify_icon_res<=0){
//                notify_icon_res = R.drawable.apk_ico_ad_taskbar;
//            }
//
//            //创建Notifcation
//            Notification notification = new Notification(notify_icon_res, notify_title, System.currentTimeMillis());
//            //设定Notification出现时的声音，一般不建议自定义
//            //notification.defaults |= Notification.DEFAULT_SOUND;
//            //设定是否振动
//            //notification.defaults |= Notification.DEFAULT_VIBRATE;
//            //notification.number=numbers++;
//            //指定Flag，Notification.FLAG_AUTO_CANCEL意指点击这个Notification后，立刻取消自身
//            //这符合一般的Notification的运作规范
//            //notification.flags|=Notification.FLAG_ONGOING_EVENT;
//
//            //创建RemoteViews用在Notification中
//            RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.layout_notifycation_service);
//            contentView.setTextViewText(R.id.tv_notify_title, notify_title);
//            contentView.setTextViewText(R.id.tv_notify_content, "");
//            contentView.setProgressBar(R.id.pb_notify, 100, 1, false);
//
//            notification.contentView = contentView;
//            notification.contentIntent=contentIntent1;
//
//        /*
//        Notification notification = new Notification(notify_icon_res, notify_title, System.currentTimeMillis());
//        Intent intent = new Intent();
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
//        //设置通知栏显示内容
//        notification.tickerText = "开始下载";
//        notification.setLatestEventInfo(mContext, notify_title, "0%", pendingIntent);*/
//
//
//            //显示这个notification
//            int notify_id = (int)System.currentTimeMillis();
//            downloadConfig.notify_id = notify_id;
//            LogUtils.v("DonwloadService notify_id:" + notify_id + "-->url:" + downloadConfig.url);
//            notificationManager.notify(notify_id, notification);
//
//            //添加到列表
//            addToNotificationList(downloadConfig.url,notification);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    public void showNotification(DownloadConfig downloadConfig,int progress){
        showNotification(downloadConfig, progress, false);
    }


    /**
     * 更新通知栏进度
     * @param downloadConfig
     * @param progress
     */
    public void updateNotification(DownloadConfig downloadConfig,int progress){
//        try{
//            Notification notification  = getNotification(downloadConfig.url);
//            if(notification==null){
//                return;
//            }
//            RemoteViews contentView = notification.contentView;
//            //do whaterver you want
//            if(progress==-1){
//                //下载完毕后变换通知形式
//                //notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                //notification.flags = Notification.FLAG_AUTO_CANCEL;
//           /* notification.contentView = null;
//            Intent intent = new Intent();// downloadConfig.jumpClass);
//            //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
//            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//            notification.setLatestEventInfo(mContext, downloadConfig.notify_title, "下载失败", contentIntent);*/
//                //下载完毕后变换通知形式
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                RemoteViews contentViewComplete = new RemoteViews(mContext.getPackageName(), R.layout.layout_notifycation_service_complete);
//                contentViewComplete.setTextViewText(R.id.tv_notify_title, downloadConfig.notify_title);
//                contentViewComplete.setTextViewText(R.id.tv_notify_content, "下载失败");
//                notification.contentView = contentViewComplete;
//                Intent intent = new Intent();
//                //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
//                PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                notification.contentIntent = contentIntent;
//                notificationManager.notify(downloadConfig.notify_id, notification);
//                removeFromNotificationList(downloadConfig.url);
//                errorMap.put(downloadConfig.url, downloadConfig.notify_id); //save error id in order to keeping notification single
//            }
//            else if(progress>0 && progress<100){
//                if(contentView!=null){
//                    contentView.setProgressBar(R.id.pb_notify, 100, progress, false);
//                    //contentView.setTextViewText(R.id.tv_notify_content, progress + "%");
//                    notificationManager.notify(downloadConfig.notify_id, notification);
//                }
//            }else{
//                //下载完毕后变换通知形式
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                RemoteViews contentViewComplete = new RemoteViews(mContext.getPackageName(), R.layout.layout_notifycation_service_complete);
//                contentViewComplete.setTextViewText(R.id.tv_notify_title, downloadConfig.notify_title);
//                contentViewComplete.setTextViewText(R.id.tv_notify_content, "下载完成");
//                notification.contentView = contentViewComplete;
//                Intent intent = new Intent();
//                //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
//                PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                notification.contentIntent = contentIntent;
//                //notification.setLatestEventInfo(mContext, downloadConfig.notify_title, "下载完成", contentIntent);
//                notificationManager.notify(downloadConfig.notify_id, notification);
//                removeFromNotificationList(downloadConfig.url);
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }

    }

    /**
     * 取消通知栏
     * @param downloadConfig
     */
    public void cacleNotification(DownloadConfig downloadConfig){
        try{
            removeFromNotificationList(downloadConfig.url);
            notificationManager.cancel(downloadConfig.notify_id);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void cacleNotification(int notifyId){
        try{
            notificationManager.cancel(notifyId);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private HashMap<String,Notification> hashMap = new HashMap<>();
    private Notification getNotification(String url){
        return hashMap.get(url);
    }
    private boolean isInNotificationList(String url){
        return hashMap.get(url)==null?false:true;
    }
    private void addToNotificationList(String url,Notification notification){
        if(null == hashMap.get(url)){
            hashMap.put(url,notification);
        }
    }
    private void removeFromNotificationList(String url){
        try{
            hashMap.remove(url);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
