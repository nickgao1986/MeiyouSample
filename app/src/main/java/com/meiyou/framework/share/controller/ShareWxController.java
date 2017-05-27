package com.meiyou.framework.share.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.meiyou.framework.share.data.BaseShareInfo;
import com.umeng.socialize.media.UMImage;

/**
 * Created by gaoyoujian on 2017/5/27.
 */

public abstract class ShareWxController extends ShareWithoutEidtViewController {

    public ShareWxController(Activity activity, BaseShareInfo shareInfoDO) {
        super(activity, shareInfoDO);
    }

    @Override
    protected UMImage processLocalImage(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        return new UMImage(context, changeColor(bitmap));
    }



    /**
     * 微信分享SDK 的bug
     * 如果分享图片含有透明部分 微信sdk就处理成了黑色
     * 这里自己处理bitmap
     * bitmap中的透明色用白色替换
     *
     * @param bitmap
     * @return
     */
    public static Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取和白色混合颜色
     *
     * @return
     */
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite

                        (green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    /**
     * 获取单色的混合值
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }
}
