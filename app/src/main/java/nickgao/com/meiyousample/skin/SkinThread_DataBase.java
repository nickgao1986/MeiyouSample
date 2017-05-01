package nickgao.com.meiyousample.skin;

import android.content.Context;

/**
 * 皮肤下载多线程信息数据库
 * Created by Administrator on 2014/9/23.
 */
public class SkinThread_DataBase extends DecorationThreadDatabase {

    public SkinThread_DataBase(Context context) {
        super(context);
    }

    @Override
    protected String getDatabaseName() {
        return "skin_thread_" + getTokenTableKey(mContext, 129746620) + ".db";
    }

    @Override
    protected String getTableName() {
        return "skin_thread";
    }

    @Override
    protected int getDatabaseVersion() {
        return 1;
    }
}
