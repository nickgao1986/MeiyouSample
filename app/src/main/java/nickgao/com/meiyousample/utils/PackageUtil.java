package nickgao.com.meiyousample.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Created by gaoyoujian on 2017/3/5.
 */
public class PackageUtil {

    private static final String TAG = "PackageUtil";
    public static final String PERIOD_PACKAGE_NAME = "com.lingan.seeyou";
    public static final String PREGNANCY_PACKAGE_NAME = "com.lingan.yunqi";

    public PackageUtil() {
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException var3) {
            LogUtils.e(var3.getLocalizedMessage());
            return new PackageInfo();
        }
    }
}
