package nickgao.com.meiyousample.skin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.database.BaseDatabase;

/**
 * 个性装扮线程数据库类，子类:SkinThread_DataBase和ExpressionThreadDatabase
 * Created by hyx on 2015/7/18.
 */
public abstract class DecorationThreadDatabase extends BaseDatabase {

    public static  String Lock = "dblock";
    public static final String SKIN_ID = "skin_id";//皮肤id
    public static final String THREAD_ID = "thread_id";//线程id
    public static final String START_POS = "start_pos";//开始下载位置
    public static final String AND_POS = "end_pos";//结束下载位置
    public static final String COMPELETE_SIZE = "compelete_size";//完成度
    public static final String DOWNLOAD_URL = "download_url";//下载地址

    public DecorationThreadDatabase(Context context) {
        super(context);
    }

    /**
     * 建表
     * @return
     */
    @Override
    protected String createSentence() {
        mSentence.add(SKIN_ID, 0);
        mSentence.add(THREAD_ID, 0);
        mSentence.add(START_POS, 0);
        mSentence.add(AND_POS, 0);
        mSentence.add(COMPELETE_SIZE, 0);
        mSentence.add(DOWNLOAD_URL, "");
        return mSentence.create();
    }

    /**
     * 如果不存在则返回true
     */
    public boolean isNotExist(int skinId) {
        Cursor  cursor =null;
        try{
            String sql = "select count(*)  from " + getTableName() + " where skin_id=?";
            cursor = selectSql(sql, new String[]{String.valueOf(skinId)});
            cursor.moveToFirst();
            //getInt方法返回第0列的值
            int count = cursor.getInt(0);
            return count == 0;
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
            close();
        }
        return  false;

    }

    /**
     * 獲取指定路徑的線程的具体下载信息
     *
     * @return List<DownloadInfo> 一个下载器信息集合器,里面存放了每条线程的下载信息
     */
    public List<DownloadInfo> getInfos(int skinId) {
        synchronized (Lock) {
            Cursor cursor = null;
            List<DownloadInfo> list = new ArrayList<DownloadInfo>();
            try{
                String st = "select * from " + getTableName() + " where " + SKIN_ID + "=" + skinId;
                cursor = selectSql(st);
                while (cursor.moveToNext()) {
                    DownloadInfo info = new DownloadInfo(getCursorInt(cursor, SKIN_ID), getCursorInt(cursor, THREAD_ID),
                            getCursorInt(cursor, START_POS), getCursorInt(cursor, AND_POS), getCursorInt(cursor, COMPELETE_SIZE),
                            getCursorString(cursor, DOWNLOAD_URL));
                    list.add(info);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                if(null != cursor && !cursor.isClosed()){
                    cursor.close() ;
                }
                close();
            }
            return list;
        }
    }

    /**
     * 插入一條 線程下载的具体信息
     * 保存和更新方法最好设置为同步
     */
    public void saveInfos(List<DownloadInfo> infos, Context context) {
        //这里也要采用事务的方法提高效率
        synchronized (Lock) {

            try {
                for (DownloadInfo info : infos) {
                    ContentValues values = new ContentValues();
                    values.put(SKIN_ID, info.getSkinId());
                    values.put(THREAD_ID, info.getThreadId());
                    values.put(START_POS, info.getStartPos());
                    values.put(AND_POS, info.getEndPos());
                    values.put(COMPELETE_SIZE, info.getCompeleteSize());
                    values.put(DOWNLOAD_URL, info.getUrl());
                    long id = insert(values);
                    LogUtils.d("aaaa: 增加成功否: " + (id > 0));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    public void updataInfos(int skinId, int threadId, int compeleteSize, String urlstr) {
        synchronized (Lock) {
            //这里因为是要更新数据,所以要采用写操作,和事务的方法来提高效率

            try {
                ContentValues values = new ContentValues();
                values.put(COMPELETE_SIZE, compeleteSize);
                update(values, SKIN_ID + "=" + skinId + " and " + THREAD_ID + "=" + threadId);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }

    }

    public void deleteThreadData(DecorationModel model) {
        try {
            boolean isSucess = delete(SKIN_ID + "=" + model.skinId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
