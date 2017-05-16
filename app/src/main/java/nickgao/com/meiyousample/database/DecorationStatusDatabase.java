package nickgao.com.meiyousample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.skin.DecorationModel;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public abstract class DecorationStatusDatabase  extends BaseDatabase{

    public static final String SKIN_ID = "skin_id";//皮肤id
    public static final String DOWNLOAD_URL = "download_url";//下载地址
    public static final String UPDATESTASTUS = "updateStastus";//状态
    public static final String VERSION = "verson";//皮肤版本
    public static final String FIFLE_NAME = "file_name";//文件名
    public static final String COMPELETESIZE = "compeleteSize";//大小

    public DecorationStatusDatabase(Context context) {
        super(context);
    }

    /**
     * 数据库列
     * @return
     */
    protected abstract String createSentence();

    public void addStatusModel(DecorationModel model, int stastus, int comlepeteSize) {
        try {
            if (isExist(model)) {
                updateStatusModel(model, stastus);
            } else {
                ContentValues values = new ContentValues();
                values.put(SKIN_ID, model.skinId);
                values.put(DOWNLOAD_URL, model.downLoadPath);
                values.put(UPDATESTASTUS, stastus);
                values.put(VERSION, model.version);
                values.put(COMPELETESIZE, comlepeteSize);
                values.put(FIFLE_NAME, model.getFileName());
                long insertID = insert(values);
                LogUtils.d("aaaa: 增加成功： " + (insertID > 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(DecorationModel model) {
        boolean isHas = false;
        Cursor cursor=null;
        try {
            cursor = select(SKIN_ID + "=" + model.skinId + " and " + VERSION + "=" + model.version, null);
            if (cursor.getCount() > 0) {
                isHas = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
            close();
        }
        return isHas;
    }

    public abstract void updateStatusModel(DecorationModel model, int status);

    public void updataComlepeteSizeModel(DecorationModel model, int size) {
        try {
            ContentValues values = new ContentValues();
            values.put(COMPELETESIZE, size);
            update(values, SKIN_ID + "=" + model.skinId + " and " + VERSION + "=" + model.version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStatusmModel(DecorationModel model) {
        try {
            boolean isScucess = delete(SKIN_ID + "=" + model.skinId + " and " + VERSION + "=" + model.version);
            LogUtils.d("aaaa: 删除成功吗：" + isScucess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract List<DecorationModel> selectStatusModels();
}