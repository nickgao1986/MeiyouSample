package nickgao.com.meiyousample.controller;

import android.widget.TextView;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/5/23.
 */

public class AnalysisController extends LinganController{



    /**
     * 因策textview的问号
     *
     * @param tv
     */
    public void hideQuestionIcon(TextView tv) {
        if (tv != null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tv.setCompoundDrawablePadding(0);
        }
    }

    /**
     * 给textview添加一个问号图标
     *
     * @param tv
     */
    public void addQuestionIcon(TextView tv) {
        if (tv != null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.analysis_icon_shuoming, 0);
            tv.setCompoundDrawablePadding(DeviceUtils.dip2px(tv.getContext(), 10));
        }
    }



}
