package nickgao.com.meiyousample.skin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.database.BaseDatabase;
import nickgao.com.meiyousample.database.DecorationStatusDatabase;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class ExpressionStatusDatabase extends DecorationStatusDatabase {
    //是否启用
    public static final String IS_USE = "is_use";

    public ExpressionStatusDatabase(Context context) {
        super(context);
    }

    @Override
    protected String getDatabaseName() {
        return "expression_status_" + BaseDatabase.getTokenTableKey(mContext, 129746620) + ".db";
//        return "expression_status" + ".db";
    }

    @Override
    protected String getTableName() {
        return "expression_status";
    }

    @Override
    protected int getDatabaseVersion() {
        return 1;
    }

    @Override
    protected String createSentence(){
        mSentence.add(SKIN_ID, 0);
        mSentence.add(DOWNLOAD_URL, "");
        mSentence.add(UPDATESTASTUS, 0);
        mSentence.add(COMPELETESIZE, 0);
        mSentence.add(VERSION, 0);
        mSentence.add(FIFLE_NAME, 0);
        mSentence.add(IS_USE, 0);
        return mSentence.create();
    }

    @Override
    public void updateStatusModel(DecorationModel model, int status) {
        try {
            ContentValues values = new ContentValues();
            values.put(UPDATESTASTUS, status);
            values.put(IS_USE, 1);
            update(values, SKIN_ID + "=" + model.skinId + " and " + VERSION + "=" + model.version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新表情启用、未启用
     * @return
     */
    public void updateIsUse(DecorationModel model){
        try {
            ContentValues values = new ContentValues();
            values.put(IS_USE, model.is_use);
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
                ExpressionModel model = new ExpressionModel();
                model.skinId = getCursorInt(cursor, SKIN_ID);
                model.version = getCursorInt(cursor, VERSION);
                model.completeSize = getCursorInt(cursor, COMPELETESIZE);
                model.downLoadPath = getCursorString(cursor, DOWNLOAD_URL);
                model.updateStastus = getCursorInt(cursor, UPDATESTASTUS);
                model.is_use = getCursorInt(cursor, IS_USE);
                models.add(model);
                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
            close();
        }
        return models;
    }
}
