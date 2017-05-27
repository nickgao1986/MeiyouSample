package com.meetyou.chartview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.meetyou.chartview.BuildConfig;
import com.meetyou.chartview.listener.DummyLineChartOnValueSelectListener;
import com.meetyou.chartview.listener.LineChartOnLabelSelectListener;
import com.meetyou.chartview.listener.LineChartOnValueSelectListener;
import com.meetyou.chartview.model.ChartData;
import com.meetyou.chartview.model.Line;
import com.meetyou.chartview.model.LineChartData;
import com.meetyou.chartview.model.PointValue;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.provider.LineChartDataProvider;
import com.meetyou.chartview.renderer.AxesRenderer;
import com.meetyou.chartview.renderer.LineChartRenderer;
import com.meetyou.chartview.util.ChartUtils;

/**
 * LineChart, supports cubic lines, filled lines, circle and square points. Point radius and stroke width can be
 * adjusted using LineChartData attributes.
 *
 * @author Leszek Wach
 */
public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected LineChartOnValueSelectListener onValueTouchListener = new DummyLineChartOnValueSelectListener();
    //标签点击事件
    protected LineChartOnLabelSelectListener onLabelSelectListener = new LineChartOnLabelSelectListener() {
        @Override
        public void onLabelSelected(int lineIndex, int pointIndex, PointValue value) {
        }
    };

    public LineChartView(Context context) {
        this(context, null, 0);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChartRenderer(new LineChartRenderer(context, this, this));
        setLineChartData(LineChartData.generateDummyData());
    }

    @Override
    public LineChartData getLineChartData() {
        return data;
    }

    @Override
    public void setLineChartData(LineChartData data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for LineChartView");
        }

        if (null == data) {
            this.data = LineChartData.generateDummyData();
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
            PointValue point = data.getLines().get(selectedValue.getFirstIndex()).getValues()
                                   .get(selectedValue.getSecondIndex());
            if (selectedValue.getType() == SelectedValue.SelectedValueType.LABEL) {
                onLabelSelectListener.onLabelSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
            } else {
                onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
            }
        } else {
            onValueTouchListener.onValueDeselected();
        }
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return onValueTouchListener;
    }

    /**
     * 设置 Point点 点击事件
     *
     * @param touchListener
     */
    public void setOnValueSelectListener(LineChartOnValueSelectListener touchListener) {
        if (null != touchListener) {
            this.onValueTouchListener = touchListener;
        }
    }

    /**
     * 设置 Label点击事件
     *
     * @param onLabelSelectListener
     */
    public void setOnLabelSelectListener(LineChartOnLabelSelectListener onLabelSelectListener) {
        if (onLabelSelectListener != null) {
            this.onLabelSelectListener = onLabelSelectListener;
        }
    }

    /**
     * 设置图表起始偏移；不要使用，使用SetViewPort设置显示视野
     *
     * @param firstPointIndex
     * @param scale
     */
    @Deprecated
    public void setStartOffset(int firstPointIndex, float scale) {
        final float newWidth = chartComputator.getCurrentViewport().width();
        // chartComputator.computeScrollSurfaceSize(surfaceSizeBuffer);

        Line line = getLineChartData().getLines().get(0);
        float end = chartComputator.computeRawX(line.getValues().get(firstPointIndex).getX());
        float start = chartComputator.computeRawX(line.getValues().get(0).getX());
        float density = getResources().getDisplayMetrics().density;
        int pointRadius = ChartUtils.dp2px(density, line.getPointRadius());
        int axesMargin = getAxesRenderer().getAxesMarginWithLabel(AxesRenderer.LEFT);
        //TODO 这里有点算不准
        float offset = end - start - axesMargin - pointRadius * 1.7f;

        float left = Math.round((chartComputator.getContentRectMinusAllMargins().left + offset)
                * (newWidth / chartComputator.getContentRectMinusAllMargins().width()));
        float right = left + newWidth;

        chartComputator.setCurrentViewport(left, chartComputator.getCurrentViewport().top, right, chartComputator
                .getCurrentViewport().bottom);

        invalidate();

        chartRenderer.onChartViewportChanged();
        scale(scale, left);
    }

    @Deprecated
    public boolean isViewPortYCalcEnabled() {
        return ((LineChartRenderer) chartRenderer).isViewPortYCalcEnabled();
    }

    @Deprecated
    public void setViewPortYCalcEnabled(boolean isEnabled) {
        ((LineChartRenderer) chartRenderer).setViewPortYCalcEnabled(isEnabled);
    }

    @Deprecated
    public float getScale() {
        return ((LineChartRenderer) chartRenderer).getScale();
    }

    @Deprecated
    public void setScale(float scale) {
        ((LineChartRenderer) chartRenderer).setScale(scale);
    }

    @Deprecated
    public float getViewPortRight() {
        return ((LineChartRenderer) chartRenderer).getViewPortRight();
    }

    /**
     * 不准，最好不要用
     *
     * @param viewportRight
     */
    @Deprecated
    public void setViewPortRight(float viewportRight) {
        ((LineChartRenderer) chartRenderer).setViewPortRight(viewportRight);
    }
}
