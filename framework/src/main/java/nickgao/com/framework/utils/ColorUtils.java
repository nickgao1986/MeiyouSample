package nickgao.com.framework.utils;

import android.graphics.Color;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class ColorUtils {

    public ColorUtils() {
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int alpha = Math.round((float) Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
