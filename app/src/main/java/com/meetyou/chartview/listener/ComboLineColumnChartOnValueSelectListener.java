package com.meetyou.chartview.listener;


import com.meetyou.chartview.model.PointValue;
import com.meetyou.chartview.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value);

}
