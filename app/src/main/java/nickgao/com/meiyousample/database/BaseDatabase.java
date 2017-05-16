package nickgao.com.meiyousample.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public abstract class BaseDatabase {
    private static final String TAG = "BaseDatabase";
    public String mPrimaryKey;
    private SQLiteDatabase mSqlDatabase = null;
    private SQLiteOpenHelper mSqlHelper = null;
    protected Context mContext;
    protected BaseSentence mSentence;
    protected String mTableName;
    protected String mDatabaseName;

    /**
     * 获取唯一码
     *
     * @return
     */
    protected String getPrimaryKey() {
        return getClass().getName();
    }

    protected abstract String getDatabaseName();

    protected abstract String getTableName();

    protected abstract int getDatabaseVersion();

    protected abstract String createSentence();

    public static void clearFirstLoginUserKey(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences("tokenbase", 0);
            sp.edit().putString("frist_launch_user", null).commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public BaseDatabase(Context context) {
        this.mContext = context;
        this.mPrimaryKey = getPrimaryKey();
        this.mTableName = getTableName();
        this.mDatabaseName = getDatabaseName();
        this.mSentence = new BaseSentence(mTableName);
        this.mSqlHelper = getSQLiteOpenUpdateHelper();
        open();
    }


    public static void setFirstLoginUserKey(Context context, int userId) {
        SharedPreferences sp = context.getSharedPreferences("tokenbase", 0);
        String frist_launch_user = sp.getString("frist_launch_user", null);
        if (frist_launch_user == null) {
            if (userId > 0) {
                sp.edit().putString("frist_launch_user", getTokenKey(userId)).commit();
            }
        }
    }

    private static String getTokenKey(int userId) {
        int math = Math.abs(userId);
        return math + "";
    }

    public static String getTokenTableKey(Context mContext, int userId) {
        SharedPreferences sp = mContext.getSharedPreferences("tokenbase", 0);
        //UserModel user = new UserModel(mContext);
        if (userId > 0) {
            String tableKey = getTokenKey(userId);
            String frist_launch_user = sp.getString("frist_launch_user", null);
            //LogUtils.d
            LogUtils.d("BaseDatabase", "getTokenTableKey 已经登录  tableKey：" + tableKey + "-->frist_launch_user:" + frist_launch_user);
            if (!tableKey.equals(frist_launch_user)) {
                return tableKey;
            }
        } else {
            LogUtils.d("BaseDatabase", "getTokenTableKey 未登录");
        }
        return "";
    }


    /**
     * public static void setFirstLoginUserKey(Context context) {
     SharedPreferences sp = context.getSharedPreferences("tokenbase", 0);
     String frist_launch_user = sp.getString("frist_launch_user", null);
     if (frist_launch_user == null) {
     UserModel user = new UserModel(context);
     if (user.isLogin() ) {
     sp.edit().putString("frist_launch_user", getTokenKey(user,false)).commit();
     }else if(user.isLoginVirtual()){
     sp.edit().putString("frist_launch_user", getTokenKey(user,true)).commit();
     }
     }
     }

     private static String getTokenKey(UserModel user,boolean bLoginVirtual) {
     if(bLoginVirtual){
     int math = user.user_id_virtual;
     math = Math.abs(math);
     return math + "";
     }else{
     int math = user.user_id;
     math = Math.abs(math);
     return math + "";
     }
     }
     public static String getTokenTableKey(Context mContext) {
     SharedPreferences sp = mContext.getSharedPreferences("tokenbase", 0);
     UserModel user = new UserModel(mContext);
     if (user.isLogin()) {
     String tableKey = getTokenKey(user,false);
     String frist_launch_user = sp.getString("frist_launch_user", null);
     LogUtils.d("BaseDatabase", "getTokenTableKey 已经登录  tableKey：" + tableKey + "-->frist_launch_user:" + frist_launch_user);
     if (!tableKey.equals(frist_launch_user)) {
     return tableKey;
     }
     }else  if (user.isLoginVirtual()) {
     String tableKey = getTokenKey(user,true);
     String frist_launch_user = sp.getString("frist_launch_user", null);
     LogUtils.d("BaseDatabase", "getTokenTableKey 已经登录  tableKey：" + tableKey + "-->frist_launch_user:" + frist_launch_user);
     if (!tableKey.equals(frist_launch_user)) {
     return tableKey;
     }
     } else{
     LogUtils.d("BaseDatabase", "getTokenTableKey 未登录");
     }
     return "";
     }
     */

    /**
     * public static String getTokenTableKey(Context mContext){
     * SharedPreferences sp = mContext.getSharedPreferences("tokenbase", 0);
     * UserModel user = new UserModel(mContext);
     * if(user.isLogin()) {
     * String token =  getTokenKey(user);
     * LogUtils.d("BaseDatabase","getTokenTableKey :"+token);
     * return token;
     * <p/>
     * }else{
     * LogUtils.d("BaseDatabase","getTokenTableKey 未登录");
     * return "";
     * <p/>
     * }
     * <p/>
     * }
     */

    public SQLiteOpenHelper getSQLiteOpenUpdateHelper() {
        return new BaseSQLiteOpenHelper(mContext, mDatabaseName,
                getDatabaseVersion(), mTableName, createSentence());
    }

    /**
     * 打开数据库
     */
    protected void open() {
        try {
            synchronized (this) {
                if (mSqlDatabase != null) {
                    if (mSqlDatabase.isOpen()) {
                        return;
                    }
                }
                try {
                    mSqlDatabase = mSqlHelper.getWritableDatabase();
                    LogUtils.i(TAG, "database hashcode = " + mSqlDatabase.hashCode());
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 关闭数据库
     */
    public void close() {
    }

    /**
     * 原先的关闭数据库并无实现,调用众多,按需修改
     */
    protected void realClose() {
        mSqlDatabase.close();
    }

    public long insert(String nullColumnHack, ContentValues values) {
        long id;
        synchronized (this) {
            open();
            id = mSqlDatabase.insert(mTableName, nullColumnHack, values);
        }
        close();
        return id;

    }

    public long insert(String tableName, String nullColumnHack, ContentValues values) {
        long id;
        synchronized (this) {
            open();
            id = mSqlDatabase.insert(tableName, nullColumnHack, values);

        }
        close();
        return id;
    }

    public long insert(ContentValues values) {
        long id;
        synchronized (this) {
            id = insert(BaseSentence.PRIMARY_KEY, values);
        }
        close();
        return id;
    }


    // ================================================删
    // delete========================================

    public boolean delete() {
        return delete(null);
    }

    public boolean delete(String selection) {
        boolean flag;
        synchronized (this) {
            open();
            flag = mSqlDatabase.delete(mTableName, selection, null) > 0;
        }
        close();
        return flag;
    }

    public boolean delete(String tableName, String selection) {
        boolean flag;
        synchronized (this) {
            open();
            flag = mSqlDatabase.delete(tableName, selection, null) > 0;
        }
        close();
        return flag;
    }

    public boolean delete(String selection, String[] args) {
        boolean flag;
        synchronized (this) {
            open();
            flag = mSqlDatabase.delete(mTableName, selection, args) > 0;
        }
        close();
        return flag;
    }

    // ================================================改
    // update========================================

    public void update(ContentValues values, String whereClause) {
        synchronized (this) {
            open();
            mSqlDatabase.update(mTableName, values, whereClause, null);
        }
        close();
    }

    public void update(String tableName, ContentValues values, String whereClause) {
        synchronized (this) {
            open();
            mSqlDatabase.update(tableName, values, whereClause, null);
        }
        close();
    }

    // ================================================查
    // select========================================
    public Cursor select() {
        return select(null, null);
    }

    public Cursor select(String selection, String orderBy) {

        synchronized (this) {
            open();
            return mSqlDatabase.query(mTableName, null, selection, null, null,
                    null, orderBy, null);
        }

    }

    public Cursor selectSql(String rawSql) {
        synchronized (this) {
            open();
            return mSqlDatabase.rawQuery(rawSql, null);
        }
    }

    public Cursor selectSql(String rawSql, String[] args) {
        synchronized (this) {
            open();
            return mSqlDatabase.rawQuery(rawSql, args);
        }
    }

    public Boolean is(String selection) {
        Boolean is = false;
        Cursor cursor = select(selection, null);
        is = cursor.moveToFirst();

        cursor.close();
        return is;
    }

    public boolean isEmpty() {
        Cursor cursor = select();
        if (cursor != null && cursor.moveToNext()) {
            cursor.close();
            return false;
        }
        return true;
    }
//=======================================================


    public void execSql(String sql) {
        synchronized (this) {
            open();
            mSqlDatabase.execSQL(sql);
        }
    }

    public String getCursorString(Cursor cursor, String tipTitle) {
        // TODO Auto-generated method stub
        try {
            int index = cursor.getColumnIndex(tipTitle);
            if (index < 0) {
                return null;
            }
            String result = cursor.getString(index);
            if (result != null) {
                result = result.trim();
                if (result.equals("null"))
                    result = "";
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

        //return cursor.getString(index);
    }

    public String getCursorStringNoTrim(Cursor cursor, String tipTitle) {
        // TODO Auto-generated method stub
        try {
            int index = cursor.getColumnIndex(tipTitle);
            if (index < 0) {
                return null;
            }
            String result = cursor.getString(index);
            if (result != null) {
                if (result.equals("null"))
                    result = "";
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

        //return cursor.getString(index);
    }

    public int getCursorInt(Cursor cursor, String tipTitle) {
        try {
            int index = cursor.getColumnIndex(tipTitle);
            if (index < 0) {
                return -1;
            }
            int count = cursor.getInt(cursor.getColumnIndex(tipTitle));

            return count;
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.d(TAG, tipTitle + " getCursorInt字段异常");

        }
        return 0;

    }

    public long getCursorLong(Cursor cursor, String tipTitle) {
        try {
            int index = cursor.getColumnIndex(tipTitle);
            if (index < 0) {
                return -1;
            }
            return cursor.getLong(cursor.getColumnIndex(tipTitle));
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.d(TAG, tipTitle + " getCursorLong字段异常");
        }
        return 0;

    }
}
