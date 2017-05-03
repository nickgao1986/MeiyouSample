package com.lingan.seeyou.ui.view.photo.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA. R
 * Date: 14-7-14
 */
public class PhotoModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3704947381379661036L;
	public long Id;
    public long BucketId;
    public boolean IsRecent = false;
    public String DisplayName;

    public String Url;  //原图路径
    public String UrlThumbnail; //压缩图路径
    public String compressPath;
    public boolean isTakePhoto = false;//是否是拍照

    private long time;
    private int section;

    private String chinaTime="";

    private boolean hasUp = false;

    private int indexPosition;

    public int getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    public boolean getHasUp() {
        return hasUp;
    }
    public void setHasUp(boolean hasUp) {
        this.hasUp = hasUp;

    }
    private int status = -1;

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;

    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
        chinaTime = paserTimeToChinaTime(time);
    }


    public String getChinaTime() {
        return chinaTime;
    }
    /**
     * 时间转换
     * @param time
     * @return
     */
    public static String paserTimeToChinaTime(long time) {
        //System.setProperty("user.timezone", "Asia/Shanghai");
        //TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        //TimeZone.setDefault(tz);
        //TimeZone.setDefault(TimeZone.getDefault());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
        return format.format(new Date(time * 1000L));
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public PhotoModel(){

    }
    public PhotoModel(long id, long bucketId, String displayName, String url,boolean recent,String urlThumbnail) {
        Id = id;
        BucketId = bucketId;
        DisplayName = displayName;
        Url = url;
        IsRecent = recent;
        UrlThumbnail = urlThumbnail;
    }

    @Override
    public String toString() {
        return "PhotoModel{" +
                "Id=" + Id +
                ", BucketId=" + BucketId +
                ", IsRecent=" + IsRecent +
                ", DisplayName='" + DisplayName + '\'' +
                ", Url='" + Url + '\'' +
                ", UrlThumbnail='" + UrlThumbnail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof PhotoModel){
            return Id == ((PhotoModel) o).Id;
        }else{
            return false;
        }
    }
}
