package com.meetyou.chartview.listener;


import com.meetyou.chartview.model.SubcolumnValue;

public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener {

    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

}
