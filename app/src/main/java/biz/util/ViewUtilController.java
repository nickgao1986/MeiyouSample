package biz.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class ViewUtilController {

    private static final String ALL_TEXT = "全文";
    private static final String NO_ALL_TEXT = "收起";

    private static final int MAX_TARGET_LINE = 10;// 超过行数 就收起
    private static final int MAX_LINE = 6;// 超过MAX_TARGET_LINE 行数 就显示的行数
    private static final String TAG = "ViewUtilController";

    private static final int MAX_IMAGEVIEW_MORE_LINE = 1;// 超过行数 就收起
    private static final int MAX_IMAGEVIEW_LINE = 1;// 超过MAX_TARGET_LINE 行数 就显示的行数

    public ViewUtilController() {

    }

    private static ViewUtilController mInstance;

    public static ViewUtilController getInstance() {
        if (mInstance == null) {
            mInstance = new ViewUtilController();
        }
        return mInstance;
    }
    /**
     * 设置为小圆
     */
    public void setPromotiomSmall(Context context, TextView tvPromotion,
                                  int bgRes) {
        try {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvPromotion
                    .getLayoutParams();
            layoutParams.width = DeviceUtils.dip2px(context, 9);
            layoutParams.height = DeviceUtils.dip2px(context, 9);
            tvPromotion.setText("");
            if (bgRes > 0) {
//            tvPromotion.setBackgroundResource(bgRes);
                SkinManager.getInstance().setDrawableBackground(tvPromotion, bgRes);
            } else {
//            tvPromotion.setBackgroundResource(R.drawable.apk_all_newsbg);
                SkinManager.getInstance().setDrawableBackground(tvPromotion, R.drawable.apk_all_newsbg);
            }
            tvPromotion.requestLayout();


            //setDotAnimation(tvPromotion);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void setTextDrawalbe(TextView textView, Drawable DrawableLeft, Drawable DrawableTop, Drawable DrawableRight, Drawable DrawableBottom) {
        try {
            //Drawable drawable= getResources().getDrawable(R.drawable.drawable);
            //clear first ,or Sony lt26i doesn't work
            textView.setCompoundDrawablePadding(0);
            textView.setCompoundDrawables(null, null, null, null);
            //DrawableLeft.setBounds(0, 0, DrawableLeft.getMinimumWidth(), DrawableLeft.getMinimumHeight());
            /// 这一步必须要做,否则不会显示.
            if (DrawableLeft != null)
                DrawableLeft.setBounds(0, 0, DrawableLeft.getIntrinsicWidth(), DrawableLeft.getIntrinsicHeight());
            if (DrawableTop != null)
                DrawableTop.setBounds(0, 0, DrawableTop.getIntrinsicWidth(), DrawableTop.getIntrinsicHeight());
            if (DrawableRight != null)
                DrawableRight.setBounds(0, 0, DrawableRight.getIntrinsicWidth(), DrawableRight.getIntrinsicHeight());
            if (DrawableBottom != null)
                DrawableBottom.setBounds(0, 0, DrawableBottom.getIntrinsicWidth(), DrawableBottom.getIntrinsicHeight());

            textView.setCompoundDrawables(DrawableLeft, DrawableTop, DrawableRight, DrawableBottom);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
