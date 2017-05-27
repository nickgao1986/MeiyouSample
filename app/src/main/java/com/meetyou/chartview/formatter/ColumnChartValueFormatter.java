package com.meetyou.chartview.formatter;


import com.meetyou.chartview.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

    /**
     * 新增附加的方法；
     * @param appendedText
     * @return
     */
     @Deprecated
    public ColumnChartValueFormatter setAppendedText(char[] appendedText);
}
