package nickgao.com.meiyousample.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class DataSaveHelper {

    public static final String SETTING_FILE = "data_saver";
    //private static final String TAG = "DataSaveHelper";

    private SharedPreferences preferences;
    private Context mContext;
    private static DataSaveHelper mInstance;

    public static DataSaveHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataSaveHelper(context);
        }
        return mInstance;
    }

    public DataSaveHelper(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.preferences = mContext.getSharedPreferences(SETTING_FILE, 0);

    }

}
