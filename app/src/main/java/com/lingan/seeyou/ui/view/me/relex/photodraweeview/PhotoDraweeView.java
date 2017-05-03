package com.lingan.seeyou.ui.view.me.relex.photodraweeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * 1. 优化PreviewImage 缩放 比例，会根据图片大小自动算法，三级缩放比例
 * 2. image Preview Activity suppot auto play Gif.
 * 3. PhotoDraweeView 新增方法回调。
 * 4. 大图查看优化； 支持微博长图查看，会根据根据 手机配置自动去掉硬件加速...默认开启硬件加速
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/12/2 上午11:05
 */
public class PhotoDraweeView extends SimpleDraweeView implements IAttacher {

    private Attacher mAttacher;

    private boolean mEnableDraweeMatrix = true;

    public PhotoDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public PhotoDraweeView(Context context) {
        super(context);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        if (mAttacher == null || mAttacher.getDraweeView() == null) {
            mAttacher = new Attacher(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int saveCount = canvas.save();
        if (mEnableDraweeMatrix) {
            canvas.concat(mAttacher.getDrawMatrix());
        }
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    @Override
    public float getMinimumScale() {
        return mAttacher.getMinimumScale();
    }

    @Override
    public float getMediumScale() {
        return mAttacher.getMediumScale();
    }

    @Override
    public float getMaximumScale() {
        return mAttacher.getMaximumScale();
    }

    @Override
    public void setMinimumScale(float minimumScale) {
        mAttacher.setMinimumScale(minimumScale);
    }

    @Override
    public void setMediumScale(float mediumScale) {
        mAttacher.setMediumScale(mediumScale);
    }

    @Override
    public void setMaximumScale(float maximumScale) {
        mAttacher.setMaximumScale(maximumScale);
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public void setScale(float scale) {
        mAttacher.setScale(scale);
    }

    @Override
    public void setScale(float scale, boolean animate) {
        mAttacher.setScale(scale, animate);
    }

    @Override
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        mAttacher.setScale(scale, focalX, focalY, animate);
    }

    @Override
    public void setZoomTransitionDuration(long duration) {
        mAttacher.setZoomTransitionDuration(duration);
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener listener) {
        mAttacher.setOnDoubleTapListener(listener);
    }

    @Override
    public void setOnScaleChangeListener(OnScaleChangeListener listener) {
        mAttacher.setOnScaleChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener) {
        mAttacher.setOnLongClickListener(listener);
    }

    @Override
    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnViewTapListener(OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override
    public OnPhotoTapListener getOnPhotoTapListener() {
        return mAttacher.getOnPhotoTapListener();
    }

    @Override
    public OnViewTapListener getOnViewTapListener() {
        return mAttacher.getOnViewTapListener();
    }

    @Override
    public void update(int imageInfoWidth, int imageInfoHeight) {
        mAttacher.update(imageInfoWidth, imageInfoHeight);
    }

    public boolean isEnableDraweeMatrix() {
        return mEnableDraweeMatrix;
    }

    public void setEnableDraweeMatrix(boolean enableDraweeMatrix) {
        mEnableDraweeMatrix = enableDraweeMatrix;
    }

    public void setPhotoUri(Uri uri) {
        setPhotoUri(uri, null, null);
    }

    public void setPhotoUri(final Uri uri, @Nullable final Context context, final CallBack callBack) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                                                       .setPostprocessor(new BasePostprocessor() {
                                                           @Override
                                                           public String getName() {
                                                               return super.getName();
                                                           }

                                                           @Override
                                                           public void process(Bitmap bitmap) {
                                                               super.process(bitmap);
                                                               int height = bitmap.getHeight();
//                        int[] maxTexture = getMaxTexture();
//                        int maxTextureSize = getMaxTextureSize();
//                        int maximumBitmapHeight = new Canvas().getMaximumBitmapHeight();
//                        if (height > maximumBitmapHeight) {
//                            PhotoDraweeView.this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//                        }
                                                           }
                                                       })
                                                       .build();

        mEnableDraweeMatrix = false;
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                            .setCallerContext(context)
                                            .setUri(uri)
                                            .setAutoPlayAnimations(true)
                                            .setOldController(getController())
                                            .setImageRequest(imageRequest)
                                            .setControllerListener(new BaseControllerListener<ImageInfo>() {

                                                @Override
                                                public void onFailure(String id, Throwable throwable) {
                                                    super.onFailure(id, throwable);
                                                    mEnableDraweeMatrix = false;
                                                    callBack.onFail(id, throwable);
                                                }

                                                @Override
                                                public void onFinalImageSet(String id, ImageInfo imageInfo,
                                                                            Animatable animatable) {
                                                    super.onFinalImageSet(id, imageInfo, animatable);
                                                    mEnableDraweeMatrix = true;
                                                    if (imageInfo != null) {
                                                        int longer;
                                                        int shorter;
                                                        int width = imageInfo.getWidth();
                                                        int height = imageInfo.getHeight();
                                                        if (height > width) {
                                                            longer = height;
                                                            shorter = width;
                                                        } else {
                                                            longer = width;
                                                            shorter = height;
                                                        }

//                            int maximumBitmapHeight = new Canvas().getMaximumBitmapHeight();
                                                        int maxTextureSize = getMaxTextureSize();
                                                        if (longer > maxTextureSize) {
                                                            PhotoDraweeView.this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                                        }
//                            Math.ceil
                                                        float maxScale = (float) ((double) longer / (double) shorter);
                                                        if (maxScale < DEFAULT_MAX_SCALE) {
                                                            maxScale = DEFAULT_MAX_SCALE;
                                                        }
                                                        float mediumScale = (maxScale + DEFAULT_MIN_SCALE) / 2;
                                                        mAttacher.setScale(DEFAULT_MIN_SCALE, mediumScale, maxScale);

                                                        update(width, height);
                                                    }
                                                    callBack.onSuccess(id, imageInfo);
                                                }

                                                @Override
                                                public void onIntermediateImageFailed(String id, Throwable throwable) {
                                                    super.onIntermediateImageFailed(id, throwable);
                                                    mEnableDraweeMatrix = false;
                                                }

                                                @Override
                                                public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                                                    super.onIntermediateImageSet(id, imageInfo);
                                                    mEnableDraweeMatrix = true;
                                                    if (imageInfo != null) {
                                                        update(imageInfo.getWidth(), imageInfo.getHeight());
                                                    }
                                                }
                                            })

                                            .build();
        setController(controller);
    }


//    public static int[] getMaxTexture(){
//    new Canvas().getMaximumBitmapHeight();

//        int[] maxTextureSize = new int[1];
//        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
//        return maxTextureSize;
//    }

    //        //http://stackoverflow.com/questions/7428996/hw-accelerated-activity-how-to-get-opengl-texture-size-limit?noredirect=1&lq=1
    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
    }


    public interface CallBack {

        public void onSuccess(String url,
                              Object... obj);

        public void onFail(String url, Object... obj);
    }

}
