package com.meetyou.chartview.model;


import com.meetyou.chartview.formatter.LineChartValueFormatter;
import com.meetyou.chartview.formatter.SimpleLineChartValueFormatter;
import com.meetyou.chartview.util.ChartUtils;

/**
 * Created by ckq on 5/12/16.
 */
public class SingleShape extends AbstractElement {

    public static final int UNINITIALIZED = 0;
    private static final int DEFAULT_POINT_RADIUS_DP = 6;
    private int pointColor = UNINITIALIZED;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private int pointRadius = DEFAULT_POINT_RADIUS_DP;

    private ValueShape shape = ValueShape.CIRCLE;
    private ShapeValue value;

    private LineChartValueFormatter formatter = new SimpleLineChartValueFormatter();

    public void setValue(ShapeValue value) {
        this.value = value;
    }

    public ShapeValue getValue() {
        return this.value;
    }

    public ValueShape getShape() {
        return shape;
    }

    public SingleShape setShape(ValueShape shape) {
        this.shape = shape;
        return this;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public SingleShape setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
        return this;
    }


    public SingleShape setColor(int color) {
        if (pointColor == UNINITIALIZED) {
            this.darkenColor = ChartUtils.darkenColor(color);
        }
        return this;
    }

    public int getPointColor() {
        return pointColor;
    }

    public SingleShape setPointColor(int pointColor) {
        this.pointColor = pointColor;
        if (pointColor == UNINITIALIZED) {
            this.darkenColor = ChartUtils.darkenColor(pointColor);
        }
        return this;
    }

    public int getDarkenColor() {
        return darkenColor;
    }

    public LineChartValueFormatter getFormatter() {
        return formatter;
    }

    public SingleShape setFormatter(LineChartValueFormatter formatter) {
        if (null != formatter) {
            this.formatter = formatter;
        }
        return this;
    }
}
