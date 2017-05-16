package nickgao.com.meiyousample.contentprovider;

import com.meiyou.sdk.common.database.BaseContentProvider;
import com.meiyou.sdk.common.database.DaoConfig;
import com.meiyou.sdk.common.database.DbManager;
import com.meiyou.sdk.common.database.DbUpgradeHelper;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class SeeyouContentProvider extends BaseContentProvider {
    public static final String SEEYOU_AUTHORITY = "com.lingan.seeyou";

    @Override
    protected DaoConfig genDaoConfig() {
        DaoConfig daoConfig = new DaoConfig(getContext()) {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                DbUpgradeHelper.autoUpdateAllTable(db);
            }

            @Override
            public String getAuthority() {
                return SEEYOU_AUTHORITY;
            }

            @Override
            public Class<?>[] getAllTableClassList() {
                return new Class[]{};
            }
        };
        daoConfig.setDbName("seeyou.db");
        daoConfig.setDbVersion(4);
        return daoConfig;
    }
}
