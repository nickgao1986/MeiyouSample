package fresco.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StatFs;
import android.widget.ImageView;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public abstract class AbstractImageLoader {

    static OkHttpClient sOkHttpClient;

    /**
     * 加载本地资源
     * @param context
     * @param imageView
     * @param res
     * @param icf
     * @param callBack
     */
    public abstract void displayImage(Context context, final IFrescoImageView imageView, int res, ImageLoadParams icf, final onCallBack callBack);

    public abstract void displayImage(Context context, final IFrescoImageView imageView, String imageurl, ImageLoadParams icf, final onCallBack callBack);

    public abstract void displayImageCorners(Context context, final IFrescoImageView imageView, String imageurl, ImageLoadParams icf, final onCallBack callBack);

    /**
     * 根据api版本调用对应的getDrawable
     *
     * @param context
     * @param resId
     * @return
     */
    public Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        } else {
            return context.getResources().getDrawable(resId);
        }
    }


    public static OkHttpClient getOkHttpClient(Context context) {
        if (sOkHttpClient == null) {
            synchronized (AbstractImageLoader.class) {
                if (sOkHttpClient == null) {
                    File file = createDefaultCacheDir(context);
                    sOkHttpClient = (new OkHttpClient.Builder())
                            .cache(new Cache(file, calculateDiskCacheSize(file)))
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }

    static File createDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "okHttp-cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }

        return cache;
    }

    static long calculateDiskCacheSize(File dir) {
        long size = 5242880L;

        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
            size = available / 50L;
        } catch (IllegalArgumentException var6) {
            ;
        }

        return Math.max(Math.min(size, 52428800L), 5242880L);
    }

    abstract public void pause(Context context, Object tag);

    abstract public void resume(Context context, Object tag);

    public interface onCallBack {
        public void onSuccess(ImageView imageView, Bitmap bitmap, String url,
                              Object... obj);

        public void onFail(String url, Object... obj);

        public void onProgress(int total, int progess);

        public void onExtend(Object... object);
    }
}

