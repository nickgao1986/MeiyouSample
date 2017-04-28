package fresco.volley;


import android.content.Context;
import android.os.StatFs;

import java.io.File;

import fresco.view.AbstractImageLoader;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Author: lwh
 * Date: 12/20/16 11:55.
 * 将图片加载器的http和接口的http统一在一起;方便修改和后边的统一整合
 */

public class OKHttpClientUtil {

    //是否开启OpenHttpDns
    private static boolean isOpenHttpsDns = false;

    public static boolean isOpenHttpsDns() {
        return isOpenHttpsDns;
    }

    public static void setOpenHttpsDns(Context context, boolean openHttpsDns) {
        try {
            isOpenHttpsDns = openHttpsDns;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static OkHttpClient mOkHttpClientImage;
    public static OkHttpClient mOkHttpClient;

//    public static OkHttpClient generateOkHttpClient(Request<?> request) {
//        if (mOkHttpClient == null) {
//            synchronized (OkHttp3Stack.class) {
//                if (mOkHttpClient == null) {
//                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//                    int timeoutMs = request.getTimeoutMs();
//                    clientBuilder.connectTimeout(timeoutMs, TimeUnit.MILLISECONDS);
//                    clientBuilder.readTimeout(timeoutMs, TimeUnit.MILLISECONDS);
//                    clientBuilder.writeTimeout(timeoutMs, TimeUnit.MILLISECONDS);
//                    //Gzip拦截器
//                    clientBuilder.addInterceptor(new GzipRequestInterceptor());
//                    //https dns 域名验证
//                    if (isOpenHttpsDns())
//                        clientBuilder.hostnameVerifier(new ExHostnameVerifier());
//
//                    mOkHttpClient = clientBuilder.build();
//                }
//            }
//        }
//        return mOkHttpClient;
//    }

    public static OkHttpClient generateOkHttpClientForImage(Context context) {
        if (mOkHttpClientImage == null) {
            synchronized (AbstractImageLoader.class) {
                if (mOkHttpClientImage == null) {
                    File file = createDefaultCacheDir(context);
                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                    //clientBuilder.addInterceptor((HttpDNSInterceptor.with(context)).addProcessHost("sc.seeyouyima.com"));
                    clientBuilder.cache(new Cache(file, calculateDiskCacheSize(file)));
                    //图片先不上https dns;未经过测试
                    //if(isOpenHttpsDns())
                    //  clientBuilder.hostnameVerifier(new ExHostnameVerifier());
                    mOkHttpClientImage = clientBuilder.build();
                    /*
                       mOkHttpClientImage = (new OkHttpClient.Builder())
                            .addInterceptor((HttpDNSInterceptor.with(context)).addProcessHost("sc.seeyouyima.com"))
                            .cache(new Cache(file, calculateDiskCacheSize(file)))
                            .build();*/
                }
            }
        }
        return mOkHttpClientImage;
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
}
