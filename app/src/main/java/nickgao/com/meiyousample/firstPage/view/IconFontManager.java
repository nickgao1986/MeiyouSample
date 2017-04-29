package nickgao.com.meiyousample.firstPage.view;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Calendar;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class IconFontManager {

    private static IconFontManager manager;
    public Typeface mTypeface;
    private Context context;

    public IconFontManager(Context context) {
        this.context = context;
        mTypeface = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

    public static IconFontManager getInstance(Context context) {
        if (manager == null) {
            manager = new IconFontManager(context);
        }
        return manager;
    }

    public Typeface getmTypeface() {
        return mTypeface;
    }

    /**
     * 根据当前的天数获取 String里面的资源字符
     *
     * @return
     */
    public String getDayResourcesString() {
        String dayStr = null;
        try {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int stringId = context.getResources()
                    .getIdentifier("day_" + day, "string", context.getPackageName());
            dayStr = context.getResources().getString(stringId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayStr;
    }
}
