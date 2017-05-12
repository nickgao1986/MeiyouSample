package nickgao.com.okhttpexample.view;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.widget.ImageView;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.image.ImageInfo;

import nickgao.com.okhttpexample.fresco.FrescoPainter;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public class FrescoPainterLoader extends AbstractImageLoader{

    private static FrescoPainterLoader mInstance;

    public static FrescoPainterLoader getInstance() {
        if (null == mInstance) {
            mInstance = new FrescoPainterLoader();
        }
        return mInstance;
    }

    private FrescoPainterDraweeInterceptor mFrescoPainterDraweeInterceptor;

    @Override
    public void displayImage(Context context, IFrescoImageView imageView, int res, ImageLoadParams icf, final onCallBack callBack) {
        if (null == icf) return;
        FrescoPainterPen frescoPainterPen = FrescoPainterPen.newBuilderWithResource(res)
                .setDefaultHolder(icf.defaultholder)
                .setDefaultHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setFailHolder(icf.failholder)
                .setFailHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setRetryHolder(icf.retryholder)
                .setRetryHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setScaleType(icf.scaleType != null ? convertScaleType(icf.scaleType) : ScalingUtils.ScaleType.CENTER_CROP)
                .setBgHolder(icf.bgholder)
                .setAnim(icf.anim)
                .setOverColor(icf.roundBgColor)
                .setFrescoPainterDraweeInterceptor(mFrescoPainterDraweeInterceptor)
                .setFadeDuration(icf.isFade?300:0)
                .setRetryEnable(icf.enableRetry)
                .setAutoRotateEnabled(true)
                .asCircle(icf.round);
        imageView.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                if (callBack != null) {
                    callBack.onFail("", "");
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo,
                                        Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (callBack != null) {
                    callBack.onSuccess(null, null, "", imageInfo.getWidth(), imageInfo.getHeight());
                }

            }

            @Override
            public void onIntermediateImageFailed(String id,
                                                  Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
            }
        });
        FrescoPainter.workspace().paint(imageView,frescoPainterPen);
    }

    private ScalingUtils.ScaleType convertScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.CENTER) {
            return ScalingUtils.ScaleType.CENTER;
        }
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            return ScalingUtils.ScaleType.FIT_XY;
        }
        if (scaleType == ImageView.ScaleType.FIT_START) {
            return ScalingUtils.ScaleType.FIT_START;
        }
        if (scaleType == ImageView.ScaleType.FIT_CENTER) {
            return ScalingUtils.ScaleType.FIT_CENTER;
        }
        if (scaleType == ImageView.ScaleType.FIT_END) {
            return ScalingUtils.ScaleType.FIT_END;
        }
        if (scaleType == ImageView.ScaleType.CENTER_INSIDE) {
            return ScalingUtils.ScaleType.CENTER_INSIDE;
        }
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            return ScalingUtils.ScaleType.CENTER_CROP;
        }
        return ScalingUtils.ScaleType.CENTER;

    }

    @Override
    public void displayImage(Context context, IFrescoImageView imageView, String imageurl, ImageLoadParams icf, final onCallBack callBack) {
        if (null == icf) return;
        FrescoPainterPen frescoPainterPen = null;
        if (!imageurl.startsWith("http")) {
            frescoPainterPen = FrescoPainterPen.newBuilderWithDisk(imageurl);
        }else {
            frescoPainterPen = FrescoPainterPen.newBuilderWithURL(imageurl);
        }

        frescoPainterPen.setDefaultHolder(icf.defaultholder)
                .setDefaultHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setFailHolder(icf.failholder)
                .setFailHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setRetryHolder(icf.retryholder)
                .setRetryHolderScaleType(icf.round ? ScalingUtils.ScaleType.CENTER_CROP : ScalingUtils.ScaleType.CENTER)
                .setScaleType(icf.scaleType != null ? convertScaleType(icf.scaleType) : ScalingUtils.ScaleType.CENTER_CROP)
                .setBgHolder(icf.bgholder)
                .setOverColor(icf.roundBgColor)
                .setFrescoPainterDraweeInterceptor(mFrescoPainterDraweeInterceptor)
                .setAnim(icf.anim)
                .setFadeDuration(300)
                .setRetryEnable(icf.enableRetry)
                .setAutoRotateEnabled(true)
                .asCircle(icf.round);
        imageView.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                if (callBack != null) {
                    callBack.onFail("", "");
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo,
                                        Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (callBack != null) {
                    callBack.onSuccess(null, null, "", imageInfo.getWidth(), imageInfo.getHeight());
                }

            }

            @Override
            public void onIntermediateImageFailed(String id,
                                                  Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
            }
        });
        FrescoPainter.workspace().paint(imageView,frescoPainterPen);
    }

    @Override
    public void displayImageCorners(Context context, IFrescoImageView imageView, String imageurl, ImageLoadParams icf, onCallBack callBack) {

    }

    @Override
    public void pause(Context context, Object tag) {

    }

    @Override
    public void resume(Context context, Object tag) {

    }
}
