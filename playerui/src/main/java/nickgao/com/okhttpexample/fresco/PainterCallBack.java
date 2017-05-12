package nickgao.com.okhttpexample.fresco;

import android.graphics.Bitmap;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public interface PainterCallBack {

    /**
     * 加载成功，注意这里的bitmap在结束该回调后是会被直接回收的
     */
    void onSuccess(String url, Bitmap bitmap);

    /**
     * 加载失败
     */
    void onFailure(String url,Throwable throwable);
}
