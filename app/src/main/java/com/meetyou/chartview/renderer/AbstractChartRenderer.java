package com.meetyou.chartview.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.meetyou.chartview.computator.ChartComputator;
import com.meetyou.chartview.model.ChartData;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.model.Viewport;
import com.meetyou.chartview.util.ChartUtils;
import com.meetyou.chartview.view.Chart;


/**
 * Abstract renderer implementation, every chart renderer extends this class(although it is not required it helps).
 */
public abstract class AbstractChartRenderer implements ChartRenderer {
    public final Context mContext;
    //label 间距提高
    public int DEFAULT_LABEL_MARGIN_DP = 8;
    protected Chart chart;
    protected ChartComputator computator;
    /**
     * Paint for value labels.
     */
    protected Paint labelPaint = new Paint();
    /**
     * Paint for labels background.
     */
    protected Paint labelBackgroundPaint = new Paint();
    /**
     * Holds coordinates for label background rect.
     */
    protected RectF labelBackgroundRect = new RectF();
    /**
     * Font metrics for label paint, used to determine text height.
     */
    protected FontMetricsInt fontMetrics = new FontMetricsInt();
    /**
     * If true maximum and current viewport will be calculated when chart data change or during data animations.
     */
    protected boolean isViewportCalculationEnabled = true;
    protected boolean isValueTouchEnabled = true;
    protected float density;
    protected float scaledDensity;
    protected SelectedValue selectedValue = new SelectedValue();
    protected char[] labelBuffer = new char[64];
    protected int labelOffset;
    protected int labelMargin;
    protected boolean isValueLabelBackgroundEnabled;
    protected boolean isValueLabelBackgroundAuto;
    /**
     * Paint used to draw press effect of every column.
     */
    protected Paint pressColumnPaint = new Paint();

    public AbstractChartRenderer(Context context, Chart chart) {
        this.mContext= context;
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.chart = chart;
        this.computator = chart.getChartComputator();

        labelMargin = ChartUtils.dp2px(density, DEFAULT_LABEL_MARGIN_DP);
        labelOffset = labelMargin;

        labelPaint.setAntiAlias(true);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setTextAlign(Align.LEFT);
        labelPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        labelPaint.setColor(Color.WHITE);

        labelBackgroundPaint.setAntiAlias(true);
        labelBackgroundPaint.setStyle(Paint.Style.FILL);

        pressColumnPaint.setAntiAlias(true);
        pressColumnPaint.setStyle(Paint.Style.FILL);
        pressColumnPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override
    public void resetRenderer() {
        this.computator = chart.getChartComputator();
    }

    @Override
    public void onChartDataChanged() {
        final ChartData data = chart.getChartData();

        Typeface typeface = chart.getChartData().getValueLabelTypeface();
        if (null != typeface) {
            labelPaint.setTypeface(typeface);
        }

        labelPaint.setColor(data.getValueLabelTextColor());
        labelPaint.setTextSize(ChartUtils.sp2px(scaledDensity, data.getValueLabelTextSize()));
        labelPaint.getFontMetricsInt(fontMetrics);

        this.isValueLabelBackgroundEnabled = data.isValueLabelBackgroundEnabled();
        this.isValueLabelBackgroundAuto = data.isValueLabelBackgroundAuto();
        this.labelBackgroundPaint.setColor(data.getValueLabelBackgroundColor());

        // Important - clear selection when data changed.
        selectedValue.clear();

    }

    /**
     * Draws label text and label background if isValueLabelBackgroundEnabled is true.
     */
    protected void drawLabelTextAndBackground(Canvas canvas, char[] labelBuffer, int startIndex, int numChars,
                                              int autoBackgroundColor) {
        final float textX;
        final float textY;

        if (isValueLabelBackgroundEnabled) {
        //Label不显示背景
//            if (isValueLabelBackgroundAuto) {
//                labelBackgroundPaint.setColor(autoBackgroundColor);
//            }
//
//            canvas.drawRect(labelBackgroundRect, labelBackgroundPaint);

            textX = labelBackgroundRect.left + labelMargin;
            textY = labelBackgroundRect.bottom - labelMargin;
        } else {
            textX = labelBackgroundRect.left;
            textY = labelBackgroundRect.bottom;
        }

        canvas.drawText(labelBuffer, startIndex, numChars, textX, textY, labelPaint);
    }
    
    

    @Override
    public boolean isTouched() {
        return selectedValue.isSet();
    }

    @Override
    public void clearTouch() {
        selectedValue.clear();
    }

    @Override
    public Viewport getMaximumViewport() {
        return computator.getMaximumViewport();
    }

    @Override
    public void setMaximumViewport(Viewport maxViewport) {
        if (null != maxViewport) {
            computator.setMaxViewport(maxViewport);
        }
    }

    @Override
    public Viewport getCurrentViewport() {
        return computator.getCurrentViewport();
    }

    @Override
    public void setCurrentViewport(Viewport viewport) {
        if (null != viewport) {
            computator.setCurrentViewport(viewport);
        }
    }

    @Override
    public boolean isViewportCalculationEnabled() {
        return isViewportCalculationEnabled;
    }

    @Override
    public void setViewportCalculationEnabled(boolean isEnabled) {
        this.isViewportCalculationEnabled = isEnabled;
    }

    @Override
    public void selectValue(SelectedValue selectedValue) {
        this.selectedValue.set(selectedValue);
    }

    @Override
    public SelectedValue getSelectedValue() {
        return selectedValue;
    }

    @Override
    public boolean isValueTouchEnabled() {
        return isValueTouchEnabled;
    }

    @Override
    public void setValueTouchEnabled(boolean isEnabled) {
        this.isValueTouchEnabled = isEnabled;
    }
}
