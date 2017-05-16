package nickgao.com.okhttpexample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import nickgao.com.okhttpexample.fresco.FrescoPainter;
import nickgao.com.okhttpexample.fresco.ImageLoaderConfigure;
import nickgao.com.okhttpexample.volley.OKHttpClientUtil;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public class MyImageLoader {

    public static final String WIDTH ="width";
    public static final String HEIGHT ="height";
    public static final String DEFAULTHOLDER ="defalutholder";
    public static final String DEFAULTHOLDER_DRAWABLE ="defalutholder_drawable";
    public static final String FORBIDENMODIFYURL ="forbidenModifyUrl";


    private MyImageLoader() {
    }

    public static synchronized void initialize(Context context, boolean debug) {
        sImageLoaderConfigure.initConfig(context);
        getInstance().init(context, debug);
    }

    private void init(Context context, boolean debug) {
        try {
//            if(this.mHttpInterceptorList == null) {
//                this.mHttpInterceptorList = new ArrayList();
//            } else {
//                this.mHttpInterceptorList.clear();
//            }
//
//            if(this.mProcessorList == null) {
//                this.mProcessorList = new ArrayList();
//            } else {
//                this.mProcessorList.clear();
//            }

            this.initFresco(context);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void initFresco(Context context) {
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context, OKHttpClientUtil.generateOkHttpClientForImage(context)).setDownsampleEnabled(true).build();
        long time = System.currentTimeMillis();
        FrescoPainter.initialize(context, imagePipelineConfig);
        long timeEnd = System.currentTimeMillis();
        Log.d("ImageLoader", "==>FrescoPainter.initialize 耗时：" + (timeEnd - time));
    }

    public synchronized static MyImageLoader getInstance() {
        if (instance == null) {
            instance = new MyImageLoader();
        }
        if (mAbstractImageLoader == null) {
            mAbstractImageLoader = FrescoPainterLoader.getInstance();
            //暂时关闭其他loader
//            if (sImageLoaderConfigure.configValid()) {
//                chooseLoaderWithConfig();
//            } else {
//                chooseLoaderWithVersion();
//            }
        }
        return instance;
    }

    private static MyImageLoader instance;
    private static FrescoPainterLoader mAbstractImageLoader;
   // private List<HttpInterceptor> mHttpInterceptorList;
    private static ImageLoaderConfigure sImageLoaderConfigure = new ImageLoaderConfigure();

    /**
     * @param context
     * @param value
     */
    public void saveImageLoaderConfig(Context context, int value) {
        sImageLoaderConfigure.saveImageLoaderConfig(context, value);
    }

    public interface onCallBack {
        public void onSuccess(ImageView imageView, Bitmap bitmap, String url,
                              Object... obj);

        public void onFail(String url, Object... obj);

        public void onProgress(int total, int progess);

        public void onExtend(Object... object);
    }

    public void displayImage(Context context, final IFrescoImageView imageView,
                             String imageurl, int defaultholder, int failholder, int retryholder, int bgholder, boolean bRound, int width, int height,
                             final onCallBack callBack) {
        ImageLoadParams loadParams = new ImageLoadParams();
        loadParams.defaultholder = defaultholder;
        loadParams.failholder = failholder;
        loadParams.retryholder = retryholder;
        loadParams.bgholder = bgholder;
        loadParams.round = bRound;
        loadParams.width = width;
        loadParams.height = height;
        displayImage(context, imageView, imageurl, loadParams, new AbstractImageLoader.onCallBack() {
            @Override
            public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                if (callBack != null) {
                    callBack.onSuccess(imageView, bitmap, url, obj);
                }
            }

            @Override
            public void onFail(String url, Object... obj) {
                if (callBack != null) {
                    callBack.onFail(url, obj);
                }
            }

            @Override
            public void onProgress(int total, int progess) {
                if (callBack != null) {
                    callBack.onProgress(total, progess);
                }
            }

            @Override
            public void onExtend(Object... object) {
                if (callBack != null) {
                    callBack.onExtend(object);
                }
            }
        });
    }

    //拦截
    public String interceptUrl(String url,ImageLoadParams imageLoadParams) {
//        try {
//            if (mHttpInterceptorList == null || mHttpInterceptorList.size() == 0)
//                return url;
//            for (HttpInterceptor interceptor : mHttpInterceptorList) {
//                HashMap<String,String> hashMap = new HashMap();
//                hashMap.put(MyImageLoader.WIDTH,String.valueOf(imageLoadParams.width));
//                hashMap.put(MyImageLoader.HEIGHT,String.valueOf(imageLoadParams.height));
//                hashMap.put(MyImageLoader.DEFAULTHOLDER,String.valueOf(imageLoadParams.defaultholder));
//                hashMap.put(MyImageLoader.FORBIDENMODIFYURL,String.valueOf(imageLoadParams.forbidenModifyUrl));
//                RequestParams requestParams = new RequestParams(hashMap);
//                HttpInterceptor.InterceptorData data = new HttpInterceptor.InterceptorData(url, 0, null, requestParams);
//                HttpInterceptor.InterceptorData beforeData = interceptor.beforeExecute(data);
//
//                return beforeData.mUrl;
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
        return url;
    }


    private AbstractImageLoader chooseLoader(ImageLoadParams params) {
//        if (params.loadMode == ImageLoadParams.LOAD_MODE_LOCAL) {
//            return PicassoLoader.getInstance();
//        }
        return mAbstractImageLoader;
    }

    public void displayImage(Context context, final IFrescoImageView imageView, String imageurl, ImageLoadParams icf, final AbstractImageLoader.onCallBack callBack) {
        if (context == null || imageView == null || imageurl == null || null == icf || null == mAbstractImageLoader) {
            return;
        }
        imageurl = interceptUrl(imageurl,icf);

        chooseLoader(icf).displayImage(context, imageView, imageurl, icf, callBack);
    }

    public void displayImage(Context context, final FrescoImageView imageView, int res, ImageLoadParams icf, final AbstractImageLoader.onCallBack callBack) {
        if (context == null || imageView == null || res <= 0 || null == icf || null == mAbstractImageLoader) {
            return;
        }

        chooseLoader(icf).displayImage(context, imageView, res, icf, callBack);
    }

}
