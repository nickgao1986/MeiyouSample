package nickgao.com.meiyousample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.skin.DecorationModel;
import nickgao.com.meiyousample.skin.SkinModel;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class SkinStastus_DataBase extends DecorationStatusDatabase {

    public SkinStastus_DataBase(Context context) {
        super(context);
    }

    @Override
    protected String getDatabaseName() {
        return "skin_status_" + BaseDatabase.getTokenTableKey(mContext, 129746620) + ".db";
    }

    @Override
    protected String getTableName() {
        return "skin_status";
    }

    @Override
    protected int getDatabaseVersion() {
        return 1;
    }

    @Override
    protected String createSentence() {
        mSentence.add(SKIN_ID, 0);
        mSentence.add(DOWNLOAD_URL, "");
        mSentence.add(UPDATESTASTUS, 0);
        mSentence.add(COMPELETESIZE, 0);
        mSentence.add(VERSION, 0);
        mSentence.add(FIFLE_NAME, 0);
        return mSentence.create();
    }

    @Override
    public void updateStatusModel(DecorationModel model, int status) {
        try {
            ContentValues values = new ContentValues();
            values.put(UPDATESTASTUS, status);
            update(values, SKIN_ID + "=" + model.skinId + " and " + VERSION + "=" + model.version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DecorationModel> selectStatusModels() {
        List<DecorationModel> models = new ArrayList<DecorationModel>();
        Cursor cursor=null;
        try {
            cursor= select();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SkinModel model = new SkinModel();
                model.skinId = getCursorInt(cursor, SKIN_ID);
                model.version = getCursorInt(cursor, VERSION);
                model.completeSize = getCursorInt(cursor, COMPELETESIZE);
                model.downLoadPath = getCursorString(cursor, DOWNLOAD_URL);
                model.updateStastus = getCursorInt(cursor, UPDATESTASTUS);
                model.setFileName(getCursorString(cursor, FIFLE_NAME));
                models.add(model);
                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
//            close();
        }
        return models;
    }
}
