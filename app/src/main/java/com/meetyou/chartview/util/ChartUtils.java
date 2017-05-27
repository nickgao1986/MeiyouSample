package com.meetyou.chartview.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.TypedValue;

public abstract class ChartUtils {

    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int COLOR_BLUE = Color.parseColor("#33B5E5");
    public static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
    public static final int COLOR_GREEN = Color.parseColor("#99CC00");
    public static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
    public static final int COLOR_RED = Color.parseColor("#FF4444");
    public static final int COLOR_WHITE = Color.parseColor("#FFFFFF");
    public static final int COLOR_BLACK = Color.parseColor("#000000");
    public static final int COLOR_RED_B = Color.parseColor("#FF5073");
    public static final int COLOR_AREA_RED_COLUMN = Color.parseColor("#FFDBE3");
    public static final int COLOR_AREA_YELLOW_COLUMN = Color.parseColor("#FFC170");
    public static final int COLOR_COLUMN_DARKEN = Color.parseColor("#F5F5F5");
    public static final int COLOR_BLACK_B = Color.parseColor("#888888");
    public static final int COLOR_BLACK_D = Color.parseColor("#B5B5B5");
    public static final int COLOR_BLACK_E = Color.parseColor("#DDDDDD");

    //美柚颜色：
    public static final int COLOR_MY_RED = COLOR_RED;
    public static final int COLOR_MY_BLACK = COLOR_BLACK_B;
    public static final int COLOR_MY_GRAY = COLOR_BLACK_D;
    public static final int COLOR_MY_BLUE = COLOR_BLUE;
    public static final int COLOR_MY_GREEN = COLOR_GREEN;
    public static final int COLOR_MY_GREEN_LINE = Color.parseColor("#ffcc9afc");
    public static final int COLOR_MY_GREEN_AREA = Color.parseColor("#cccc9afc");
    //     Color.parseColor("#e6ccfe");
    public static final int COLOR_MY_YELLOW = COLOR_ORANGE;

    public static final int COLOR_Axis_Backgroud = Color.parseColor("#FFC170");

    public static final int[] COLORS = new int[]{COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED};
    private static final float DARKEN_SATURATION = 1.1f;
    private static final float DARKEN_INTENSITY = 0.9f;
    private static int COLOR_INDEX = 0;

    public static final int pickColor() {
        return COLORS[(int) Math.round(Math.random() * (COLORS.length - 1))];
    }

    public static final int nextColor() {
        if (COLOR_INDEX >= COLORS.length) {
            COLOR_INDEX = 0;
        }
        return COLORS[COLOR_INDEX++];
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);

    }

    public static int px2dp(float density, int px) {
        return (int) Math.ceil(px / density);
    }

    public static int sp2px(float scaledDensity, int sp) {
        if (sp == 0) {
            return 0;
        }
        return (int) (sp * scaledDensity + 0.5f);
    }

    public static int px2sp(float scaledDensity, int px) {
        return (int) Math.ceil(px / scaledDensity);
    }

    public static int mm2px(Context context, int mm) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, context.getResources()
                                                                                       .getDisplayMetrics()) + 0.5f);
    }

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        int alpha = Color.alpha(color);
        Color.colorToHSV(color, hsv);
        hsv[1] = Math.min(hsv[1] * DARKEN_SATURATION, 1.0f);
        hsv[2] = hsv[2] * DARKEN_INTENSITY;
        int tempColor = Color.HSVToColor(hsv);
        return Color.argb(alpha, Color.red(tempColor), Color.green(tempColor), Color.blue(tempColor));
    }

    public static boolean isNull(String str) {
        try {
            if (str == null) {
                return true;
            } else if (str != null) {
                if (str.equals("") || str.equals("null") || str.equals("[]")) {
                    return true;
                } else if (str.trim().equals("") || str.trim().equals("null")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;

    }

    /**
     * Generate rounded path.
     */
    public static Path buildRoundedRectPath(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
    ) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    /**
     * Generate rounded path.
     */
    public static Path buildRoundedRectPath(RectF rect,
                                            float rx, float ry,
                                            boolean tl, boolean tr, boolean br, boolean bl) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = rect.right - rect.left;
        float height = rect.bottom - rect.top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(rect.right, rect.top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
}
