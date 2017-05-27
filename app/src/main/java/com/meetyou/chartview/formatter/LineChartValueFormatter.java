package com.meetyou.chartview.formatter;


import com.meetyou.chartview.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
