package com.lingan.seeyou.ui.view.publish;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.framework.utils.LogUtils;


/**
 * 布局高度改变处理器
 * Created by huangyuxiang on 2017/4/18.
 */

public class LayoutHeightChangedHandler {
    private static final String TAG = "LayoutHeightChangedHand";
    private int mStatusBarHeight;
    //最外层布局旧的高度
    private int mOldHeight = -1;
    private Context mContext;

    public LayoutHeightChangedHandler(Activity activity){
        mContext = activity.getApplicationContext();
        mStatusBarHeight = DeviceUtils.getStatusBarHeight(activity);
    }

    /**
     * 处理measure的高度变化
     * @param height View高度
     * @return 0 无变化，> 0 键盘弹出，< 0 键盘收起
     */
    public int onKeyboardChanged(int height){
        if (height < 0) {
            return 0;
        }
        if (mOldHeight < 0) {
            mOldHeight = height;
            return 0;
        }

        final int offset = mOldHeight - height;

        if (offset == 0) {
            LogUtils.d(TAG, "" + offset + " == 0 break;");
            return 0;
        }

        if (Math.abs(offset) == mStatusBarHeight) {
            LogUtils.w(TAG, String.format("offset just equal statusBar height %d", offset));
            // 极有可能是 相对本页面的二级页面的主题是全屏&是透明，但是本页面不是全屏，因此会有status bar的布局变化差异，进行调过
            // 极有可能是 该布局采用了透明的背景(windowIsTranslucent=true)，而背后的布局`full screen`为false，
            // 因此有可能第一次绘制时没有attach上status bar，而第二次status bar attach上去，导致了这个变化。
            return 0;
        }

        //重新赋值mOldHeight
        mOldHeight = height;

        // 检测到布局变化非键盘引起
        if (Math.abs(offset) < TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, mContext.getResources().getDisplayMetrics())) {
            LogUtils.w(TAG, "system bottom-menu-bar(such as HuaWei Mate7) causes layout changed");
            return 0;
        }
        return offset;
    }
}
