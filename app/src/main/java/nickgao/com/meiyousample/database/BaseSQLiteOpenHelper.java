package nickgao.com.meiyousample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private String tableName;
    private String sentence;
    public BaseSQLiteOpenHelper(Context context, String dbName, int dbVersion, String tableName, String sentence) {
        super(context, dbName, null, dbVersion);
        // TODO Auto-generated constructor stub
        this.tableName = tableName;
        this.sentence = sentence;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(sentence); // 创建笑话表
    }
}