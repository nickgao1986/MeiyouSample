package biz.download;

import java.io.File;
import java.io.Serializable;

/**
 * Created by lwh on 2015/9/15.
 */
public class DownloadConfig implements Serializable {
    //下载地址
    public String url;
    //主要用于HttpDns
    public String ip;
    //存储路径
    public String dirPath;
    //下载名称
    //public String name;
    //是否显示通知栏进度
    public boolean isShowNotificationProgress =false;
    //是否通过广播发送进度
    public boolean isBrocastProgress =false;
    //是否强制下载，即使本地存在一样的文件
    public boolean isForceReDownload =false;
    //通知栏标题
    public String notify_title;
    //通知栏图标资源
    public int notify_icon_res;
    //下载完成后的文件
    public File file;
    //业务对象，必须经过序列化
    public Object object;
    //通知栏ID
    public int notify_id;
    //下载进度
    public int progress;
    //下载应用后是否执行安装
    public boolean installApkAfterDownload = true;
}
