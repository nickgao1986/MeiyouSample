package com.meetyou.chartview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.meetyou.chartview.BuildConfig;
import com.meetyou.chartview.listener.ColumnChartOnValueSelectListener;
import com.meetyou.chartview.listener.DummyColumnChartOnValueSelectListener;
import com.meetyou.chartview.model.ColumnChartData;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.model.SubcolumnValue;
import com.meetyou.chartview.provider.ColumnChartDataProvider;
import com.meetyou.chartview.renderer.ColumnChartRenderer;

/**
 * ColumnChart/BarChart, supports subcolumns, stacked collumns and negative values.
 *
 * @author Leszek Wach
 */
public class ColumnChartView extends AbstractChartView implements ColumnChartDataProvider {
    private static final String TAG = "ColumnChartView";
    private ColumnChartData data;
    private ColumnChartOnValueSelectListener onValueTouchListener = new DummyColumnChartOnValueSelectListener();

    public ColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChartRenderer(new ColumnChartRenderer(context, this, this));
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    @Override
    public ColumnChartData getColumnChartData() {
        return data;
    }

    @Override
    public void setColumnChartData(ColumnChartData data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for ColumnChartView");
        }

        if (null == data) {
            this.data = ColumnChartData.generateDummyData();
        } else {
            this.data = data;
        }

        super.onChartDataChange();

    }

    @Override
    public ColumnChartData getChartData() {
        return data;
    }

    @Override
    public void callTouchListener() {
        SelectedValue selectedValue = chartRenderer.getSelectedValue();

        if (selectedValue.isSet()) {
            SubcolumnValue value = data.getColumns().get(selectedValue.getFirstIndex()).getValues()
                                       .get(selectedValue.getSecondIndex());
            onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), value);
        } else {
            onValueTouchListener.onValueDeselected();
        }
    }

    public ColumnChartOnValueSelectListener getOnValueTouchListener() {
        return onValueTouchListener;
    }

    public void setViewPortYCalcEnabled(boolean isEnabled) {
        ((ColumnChartRenderer) chartRenderer).setViewPortYCalcEnabled(isEnabled);
    }

    public void setOnValueTouchListener(ColumnChartOnValueSelectListener touchListener) {
        if (null != touchListener) {
            this.onValueTouchListener = touchListener;
        }
    }

    /************
     * 自定义方法实现
     ************/

    @Deprecated
    public void setScale(float scale) {
        ((ColumnChartRenderer) chartRenderer).setScale(scale);
    }

    public float getViewPortRight() {
        return ((ColumnChartRenderer) chartRenderer).getViewPortRight();
    }

    public void setViewPortRight(float viewportRight) {
        ((ColumnChartRenderer) chartRenderer).setViewPortRight(viewportRight);
    }

    public void setStartOffset(float scale, float offset) {
        chartRenderer.onChartViewportChanged();
        scale(scale, offset);
    }

    public boolean isFullColumnTouched() {
        return ((ColumnChartRenderer) chartRenderer).isFullColumnTouched();
    }

    public void setFullColumnTouched(boolean fullColumnTouched) {
        ((ColumnChartRenderer) chartRenderer).setFullColumnTouched(fullColumnTouched);
    }

    public void setAxesCustomStep(int customStep, int multiple) {
        getAxesRenderer().setCustomAxesStep(true);
        getAxesRenderer().setCustomStep(customStep, multiple);
        ((ColumnChartRenderer) chartRenderer).setCustomAxes(true);
    }

    /**
     * 是否显示辅助线
     *
     * @param enabelGuideLine
     * @param guideLineValue  辅助线的值
     */
    public void enableGuildeLine(boolean enabelGuideLine, float guideLineValue) {
        ((ColumnChartRenderer) chartRenderer).enableGuideLine = enabelGuideLine;
        ((ColumnChartRenderer) chartRenderer).guideLineValue = guideLineValue;
    }
}
