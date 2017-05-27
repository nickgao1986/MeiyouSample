package com.meetyou.chartview.listener;


import com.meetyou.chartview.model.PointValue;

/**
 * 线图 标签点击事件
 * @author zhengxiaobin@xiaoyouzi.com
 * @since  17/3/2 上午11:04
 */
public interface LineChartOnLabelSelectListener {

    public void onLabelSelected(int lineIndex, int pointIndex, PointValue value);
//    @Deprecated
//    public void onLabelDeselected() ;
}
