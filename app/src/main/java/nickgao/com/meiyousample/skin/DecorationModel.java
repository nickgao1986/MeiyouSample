package nickgao.com.meiyousample.skin;

import java.io.Serializable;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public abstract class DecorationModel implements Serializable {
    public int skinId;//皮肤id
    public String downLoadPath;//下载地址
    public int updateStastus = SkinDownloadType.SKIN_CONVERT_COIN;//几种状态,默认是未兑换状态
    public int version;//版本
    public int completeSize;//已下载的长度
    public boolean is_exchanged;//是否兑换过了皮肤
    public boolean is_prize;//是否已赠送
    public int is_use; //表情是否使用

    public abstract String getFileName();
    public abstract String getTempFileName();
}

