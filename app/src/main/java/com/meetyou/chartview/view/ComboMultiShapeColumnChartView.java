package com.meetyou.chartview.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.meetyou.chartview.BuildConfig;
import com.meetyou.chartview.listener.ComboMultiShapeColumnChartOnValueSelectListener;
import com.meetyou.chartview.listener.DummyComboMultiShapeColumnChartOnValueSelectListener;
import com.meetyou.chartview.model.ChartData;
import com.meetyou.chartview.model.ColumnChartData;
import com.meetyou.chartview.model.ComboMultiShapeColumnChartData;
import com.meetyou.chartview.model.MultiShapeChartData;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.provider.ColumnChartDataProvider;
import com.meetyou.chartview.provider.ComboMultiShapeColumnChartDataProvider;
import com.meetyou.chartview.provider.MultiShapeChartDataProvider;
import com.meetyou.chartview.renderer.ColumnChartRenderer;
import com.meetyou.chartview.renderer.ComboMultiShapeColumnChartRenderer;
import com.meetyou.chartview.renderer.MultiShapeChartRender;
import com.meetyou.chartview.util.ChartUtils;

public class ComboMultiShapeColumnChartView extends AbstractChartView implements ComboMultiShapeColumnChartDataProvider {
    private static final String TAG = "ComboLineColumnChart";
    protected ComboMultiShapeColumnChartData data;
    protected ColumnChartDataProvider columnChartDataProvider = new ComboColumnChartDataProvider();
    protected MultiShapeChartDataProvider multiShapeChartDataProvider = new ComboMultiShapeChartDataProvider();
    protected ComboMultiShapeColumnChartOnValueSelectListener onValueTouchListener = new
            DummyComboMultiShapeColumnChartOnValueSelectListener();

    public static final int DEFAULT_MARGIN_TOP_FOR_DRAWING_LABEL = 30;

    private Rect displayRect; // 绘制矩形区域,顶部需要留白以供label绘制
    private float density;

    public ComboMultiShapeColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ComboMultiShapeColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComboMultiShapeColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChartRenderer(new ComboMultiShapeColumnChartRenderer(context, this, columnChartDataProvider,
                multiShapeChartDataProvider));
        setComboMultiShapeColumnChartData(ComboMultiShapeColumnChartData.generateDummyData());

        displayRect = new Rect();
        density = context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isEnabled()) {
            axesRenderer.drawInBackground(canvas);
            int clipRestoreCount = canvas.save();
            clipRectForChart(canvas);
            chartRenderer.draw(canvas);
            canvas.restoreToCount(clipRestoreCount);
            chartRenderer.drawUnclipped(canvas);
            axesRenderer.drawInForeground(canvas);
        } else {
            canvas.drawColor(ChartUtils.DEFAULT_COLOR);
        }
    }
    
    protected void clipRectForChart(Canvas canvas) {
        displayRect.set(chartComputator.getContentRectMinusAllMargins().left,
                chartComputator.getContentRectMinusAxesMargins().top,
                chartComputator.getContentRectMinusAllMargins().right,
                chartComputator.getContentRectMinusAllMargins().bottom);
        canvas.clipRect(displayRect);
    }

    @Override
    public ComboMultiShapeColumnChartData getComboMultiShapeColumnChartData() {
        return data;
    }

    @Override
    public void setComboMultiShapeColumnChartData(ComboMultiShapeColumnChartData data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for ComboLineColumnChartView");
        }

        if (null == data) {
            this.data = null;// generateDummyData();
        } else {
            this.data = data;
        }

        super.onChartDataChange();
    }

    @Override
    public ChartData getChartData() {
        return data;
    }

    @Override
    public void callTouchListener() {
        SelectedValue selectedValue = chartRenderer.getSelectedValue();

        if (selectedValue.isSet()) {
            onValueTouchListener.onColumnValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), null);
            /*if (SelectedValue.SelectedValueType.COLUMN.equals(selectedValue.getType())) {

                SubcolumnValue value = data.getColumnChartData().getColumns().get(selectedValue.getFirstIndex())
                        .getValues().get(selectedValue.getSecondIndex());
                onValueTouchListener.onColumnValueSelected(selectedValue.getFirstIndex(),
                        selectedValue.getSecondIndex(), value);

            } else if (SelectedValue.SelectedValueType.MULTI_SHAPE.equals(selectedValue.getType())) {

                *//*PointValue value = data.getMultiShapeChartData().getMultiShapes().get(selectedValue.getFirstIndex()).getValues()
                        .get(selectedValue.getSecondIndex());
                onValueTouchListener.onPointValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(),
                        value);*//*
                Log.d(TAG, "You select multi_shape!");

            } else {
                Log.d(TAG, "Invalid selected value type " + selectedValue.getType().name());
                // throw new IllegalArgumentException("Invalid selected value type " + selectedValue.getType().name());
            }*/
        } else {
            onValueTouchListener.onValueDeselected();
        }
    }

    public ComboMultiShapeColumnChartOnValueSelectListener getOnValueTouchListener() {
        return onValueTouchListener;
    }

    public void setOnValueTouchListener(ComboMultiShapeColumnChartOnValueSelectListener touchListener) {
        if (null != touchListener) {
            this.onValueTouchListener = touchListener;
        }
    }

    public void setColumnChartRenderer(Context context, ColumnChartRenderer columnChartRenderer) {
        setChartRenderer(new ComboMultiShapeColumnChartRenderer(context, this, columnChartRenderer, multiShapeChartDataProvider));
    }

    public void setMultiShapeChartRenderer(Context context, MultiShapeChartRender multiShapeChartRender){
        setChartRenderer(new ComboMultiShapeColumnChartRenderer(context, this, columnChartDataProvider, multiShapeChartRender));
    }

    private class ComboMultiShapeChartDataProvider implements MultiShapeChartDataProvider {

        @Override
        public MultiShapeChartData getMultiShapeChartData() {
            return ComboMultiShapeColumnChartView.this.data.getMultiShapeChartData();
        }

        @Override
        public void setMultiShapeChartData(MultiShapeChartData data) {
            ComboMultiShapeColumnChartView.this.data.setMultiShapeChartData(data);

        }

    }

    private class ComboColumnChartDataProvider implements ColumnChartDataProvider {

        @Override
        public ColumnChartData getColumnChartData() {
            return ComboMultiShapeColumnChartView.this.data.getColumnChartData();
        }

        @Override
        public void setColumnChartData(ColumnChartData data) {
            ComboMultiShapeColumnChartView.this.data.setColumnChartData(data);

        }

    }

    public MultiShapeChartDataProvider getMultiShapeChartDataProvider() {
        return multiShapeChartDataProvider;
    }

    public ColumnChartDataProvider getColumnChartDataProvider() {
        return columnChartDataProvider;
    }

    public void setStartOffset(float scale, float offset) {
        chartRenderer.onChartViewportChanged();
        scale(scale, offset);
    }

    public void setFullColumnTouched(boolean fullColumnTouched) {
        ((ComboMultiShapeColumnChartRenderer) chartRenderer).columnChartRenderer.setFullColumnTouched(fullColumnTouched);
    }

}
