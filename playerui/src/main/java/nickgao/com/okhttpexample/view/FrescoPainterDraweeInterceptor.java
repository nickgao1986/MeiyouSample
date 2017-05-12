package nickgao.com.okhttpexample.view;

import android.graphics.drawable.Drawable;

/**
 * Created by gaoyoujian on 2017/4/26.
 */
public interface FrescoPainterDraweeInterceptor {
    Drawable onSetPlaceholderImage(int resourceId);
    Drawable onSetRetryImage(int resourceId);
    Drawable onSetFailureImage(int resourceId);
    Drawable onSetProgressBarImage(int resourceId);
    Drawable onSetBackground(int resourceId);
}
