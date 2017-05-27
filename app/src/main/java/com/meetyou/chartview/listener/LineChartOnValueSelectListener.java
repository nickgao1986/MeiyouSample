package com.meetyou.chartview.listener;


import com.meetyou.chartview.model.PointValue;

/**
 * 线图 点 点击事件
 * @author zhengxiaobin@xiaoyouzi.com
 * @since  17/3/2 上午11:05
 */
public interface LineChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int lineIndex, int pointIndex, PointValue value);

}
