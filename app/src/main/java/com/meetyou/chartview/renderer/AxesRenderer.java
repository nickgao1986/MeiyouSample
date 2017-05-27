package com.meetyou.chartview.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.meetyou.chartview.computator.ChartComputator;
import com.meetyou.chartview.model.Axis;
import com.meetyou.chartview.model.AxisValue;
import com.meetyou.chartview.model.Viewport;
import com.meetyou.chartview.util.AxisAutoValues;
import com.meetyou.chartview.util.ChartUtils;
import com.meetyou.chartview.util.FloatUtils;
import com.meetyou.chartview.view.Chart;

/**
 * Default axes renderer. Can draw maximum four axes - two horizontal(top/bottom) and two vertical(left/right).
 */
public class AxesRenderer {
    private static final int DEFAULT_AXIS_NAME_MARGIN_DP = 20; // 除了坐标轴名字高度之外的margin,若为0则顶部margin只留出字体高度
    private static final int DEFAULT_AXIS_MARGIN_DP = 2; // 临近坐标轴的间隔
    private static final int DEFAULT_AXIS_MARGIN_LABEL = 10; // yLabel到坐标轴顶部的距离
    private static final int DEFAULT_AXIS_INSET_MARGIN_TOP = DEFAULT_AXIS_MARGIN_LABEL + 10; // 图表顶部与坐标轴的间距
    private static final int DEFAULT_AXIS_DEGREE_MARGIN = 0; // 刻度向右偏移的距离
    private static final int DEFAULT_AXIS_LEFT_AND_RIGHT_MAX_LABEL_CHARS = 1; // 默认左、有坐标轴的最大label字符数
    private static final int DEFAULT_AXIS_HORIZONTAL_ABOVE_MARGIN = 0; // x轴向左凸出超过left的长度
    public static final int DEFAULT_AXIS_VERTICAL_ABOVE_MARGIN = 10; // y轴向上凸出超过top的长度

    /**
     * Axis positions indexes, used for indexing tabs that holds axes parameters, see below.
     */
    public static final int TOP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    /**
     * Used to measure label width. If label has mas 5 characters only 5 first characters of this array are used to
     * measure text width.
     */
    private static final char[] labelWidthChars = new char[]{
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

    private Chart chart;
    private ChartComputator computator;
    private int axisMargin, axisNameMargin;
    private float density;
    private float scaledDensity;
    private Paint[] labelPaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] namePaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] linePaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] separatePaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};
    private float[] nameBaselineTab = new float[4];
    private float[] labelBaselineTab = new float[4];
    private float[] separationLineTab = new float[4];
    private int[] labelWidthTab = new int[4];
    private int[] labelTextAscentTab = new int[4];
    private int[] labelTextDescentTab = new int[4];
    private int[] labelDimensionForMarginsTab = new int[4];
    private int[] labelDimensionForStepsTab = new int[4];
    private int[] tiltedLabelXTranslation = new int[4];
    private int[] tiltedLabelYTranslation = new int[4];
    private FontMetricsInt[] fontMetricsTab = new FontMetricsInt[]{new FontMetricsInt(), new FontMetricsInt(),
            new FontMetricsInt(), new FontMetricsInt()};
    /**
     * Holds formatted axis value label.
     */
    private char[] labelBuffer = new char[64];

    /**
     * Holds number of values that should be drown for each axis.
     */
    private int[] valuesToDrawNumTab = new int[4];

    /**
     * Holds raw values to draw for each axis.
     */
    private float[][] rawValuesTab = new float[4][0];

    /**
     * Holds auto-generated values that should be drawn, i.e if axis is inside not all auto-generated values should be
     * drawn to avoid overdrawing. Used only for auto axes.
     */
    private float[][] autoValuesToDrawTab = new float[4][0];

    /**
     * Holds custom values that should be drawn, used only for custom axes.
     */
    private AxisValue[][] valuesToDrawTab = new AxisValue[4][0];

    /**
     * Buffers for axes lines coordinates(to draw grid in the background).
     */
    private float[][] linesDrawBufferTab = new float[4][0];

    // 是否可以自定义自动计算的坐标数目
    private boolean isCustomAxesStep;

    // 坐标间隔
    private int customStep;

    // 自定义坐标最大值的最小公约数
    private int multiple;

    // 坐标刻度与坐标轴的距离
    private float axisDegreeMargin;

    // 纵向坐标轴向上凸起超出top的长度
    private float axisVerticalMargin;

    // 横坐标向左凸出超过left的长度
    private float axisHorizontalLeftMargin;

    // 是否隐藏第一个点
    private boolean hideFirstAxisLabel;
    /**
     * 如果label名字，包含 "今天"，高亮显示
     */
    private boolean highLightAxisLabel;

    private Paint highLightLabelPaint;

    /**
     * Buffers for auto-generated values for each axis, used only if there are auto axes.
     */
    private AxisAutoValues[] autoValuesBufferTab = new AxisAutoValues[]{new AxisAutoValues(),
            new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues()};

    public AxesRenderer(Context context, Chart chart) {
        this.chart = chart;
        computator = chart.getChartComputator();
        density = context.getResources().getDisplayMetrics().density;
        scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        axisMargin = ChartUtils.dp2px(density, DEFAULT_AXIS_MARGIN_DP);
        axisNameMargin = ChartUtils.dp2px(density, DEFAULT_AXIS_NAME_MARGIN_DP);
        axisDegreeMargin = ChartUtils.dp2px(density, DEFAULT_AXIS_DEGREE_MARGIN);
        axisVerticalMargin = ChartUtils.dp2px(density, DEFAULT_AXIS_VERTICAL_ABOVE_MARGIN);
        axisHorizontalLeftMargin = ChartUtils.dp2px(density, DEFAULT_AXIS_HORIZONTAL_ABOVE_MARGIN);
        for (int position = 0; position < 4; ++position) {
            labelPaintTab[position].setStyle(Paint.Style.FILL);
            labelPaintTab[position].setAntiAlias(true);
            namePaintTab[position].setStyle(Paint.Style.FILL);
            namePaintTab[position].setAntiAlias(true);
            linePaintTab[position].setStyle(Paint.Style.STROKE);
            linePaintTab[position].setAntiAlias(true);
            separatePaintTab[position].setStyle(Paint.Style.STROKE);
            separatePaintTab[position].setAntiAlias(true);

        }
    }

    public void onChartSizeChanged() {
        onChartDataOrSizeChanged();
    }

    public void onChartDataChanged() {
        onChartDataOrSizeChanged();
    }

    private void onChartDataOrSizeChanged() {
        initAxis(chart.getChartData().getAxisXTop(), TOP);
        initAxis(chart.getChartData().getAxisXBottom(), BOTTOM);
        initAxis(chart.getChartData().getAxisYLeft(), LEFT);
        initAxis(chart.getChartData().getAxisYRight(), RIGHT);
    }

    public void resetRenderer() {
        this.computator = chart.getChartComputator();
    }

    /**
     * Initialize attributes and measurement for axes(left, right, top, bottom);
     */
    private void initAxis(Axis axis, int position) {
        if (null == axis) {
            return;
        }
        initAxisAttributes(axis, position);
        initAxisMargin(axis, position);
        initAxisMeasurements(axis, position);
    }

    private void initAxisAttributes(Axis axis, int position) {
        initAxisPaints(axis, position);
        initAxisTextAlignment(axis, position);
        if (axis.hasTiltedLabels()) {
            initAxisDimensionForTiltedLabels(position);
            intiTiltedLabelsTranslation(axis, position);
        } else {
            initAxisDimension(position);
        }
    }

    private void initAxisPaints(Axis axis, int position) {
        Typeface typeface = axis.getTypeface();
        if (null != typeface) {
            labelPaintTab[position].setTypeface(typeface);
            namePaintTab[position].setTypeface(typeface);
        }
        labelPaintTab[position].setColor(axis.getTextColor());
        labelPaintTab[position].setTextSize(ChartUtils.sp2px(scaledDensity, axis.getTextSize()));
        labelPaintTab[position].getFontMetricsInt(fontMetricsTab[position]);
        namePaintTab[position].setColor(axis.getTextColor());
        namePaintTab[position].setTextSize(ChartUtils.sp2px(scaledDensity, axis.getTextSize()));
        linePaintTab[position].setColor(axis.getLineColor());
        separatePaintTab[position].setColor(axis.getSeparateLineColor());

        labelTextAscentTab[position] = Math.abs(fontMetricsTab[position].ascent);
        labelTextDescentTab[position] = Math.abs(fontMetricsTab[position].descent);
        int maxLabelChars = axis.getMaxLabelChars();
        labelWidthTab[position] = (int) labelPaintTab[position].measureText(labelWidthChars, 0, maxLabelChars);

        initHighlightLabelPaint(axis);
    }

    private void initHighlightLabelPaint(Axis axis) {
        highLightLabelPaint = new Paint();
        highLightLabelPaint.setStyle(Paint.Style.FILL);
        highLightLabelPaint.setAntiAlias(true);
        highLightLabelPaint.setColor(ChartUtils.COLOR_RED_B);
        highLightLabelPaint.setTextSize(ChartUtils.sp2px(scaledDensity, axis.getTextSize()));
        highLightLabelPaint.setTextAlign(Align.CENTER);
    }

    private void initAxisTextAlignment(Axis axis, int position) {
        namePaintTab[position].setTextAlign(Align.CENTER);
        if (TOP == position || BOTTOM == position) {
            labelPaintTab[position].setTextAlign(Align.CENTER);
        } else if (LEFT == position) {
            if (axis.isInside()) {
                labelPaintTab[position].setTextAlign(Align.LEFT);
            } else {
                labelPaintTab[position].setTextAlign(Align.RIGHT);
            }
        } else if (RIGHT == position) {
            if (axis.isInside()) {
                labelPaintTab[position].setTextAlign(Align.RIGHT);
            } else {
                labelPaintTab[position].setTextAlign(Align.LEFT);
            }
        }
    }

    private void initAxisDimensionForTiltedLabels(int position) {
        int pythagoreanFromLabelWidth = (int) Math.sqrt(Math.pow(labelWidthTab[position], 2) / 2);
        int pythagoreanFromAscent = (int) Math.sqrt(Math.pow(labelTextAscentTab[position], 2) / 2);
        labelDimensionForMarginsTab[position] = pythagoreanFromAscent + pythagoreanFromLabelWidth;
        labelDimensionForStepsTab[position] = Math.round(labelDimensionForMarginsTab[position] * 0.75f);
    }

    private void initAxisDimension(int position) {
        if (LEFT == position || RIGHT == position) {
            labelDimensionForMarginsTab[position] = labelWidthTab[position];
            labelDimensionForStepsTab[position] = labelTextAscentTab[position];
        } else if (TOP == position || BOTTOM == position) {
            labelDimensionForMarginsTab[position] = labelTextAscentTab[position] +
                    labelTextDescentTab[position];
            labelDimensionForStepsTab[position] = labelWidthTab[position];
        }
    }

    private void intiTiltedLabelsTranslation(Axis axis, int position) {
        int pythagoreanFromLabelWidth = (int) Math.sqrt(Math.pow(labelWidthTab[position], 2) / 2);
        int pythagoreanFromAscent = (int) Math.sqrt(Math.pow(labelTextAscentTab[position], 2) / 2);
        int dx = 0;
        int dy = 0;
        if (axis.isInside()) {
            if (LEFT == position) {
                dx = pythagoreanFromAscent;
            } else if (RIGHT == position) {
                dy = -pythagoreanFromLabelWidth / 2;
            } else if (TOP == position) {
                dy = (pythagoreanFromAscent + pythagoreanFromLabelWidth / 2) - labelTextAscentTab[position];
            } else if (BOTTOM == position) {
                dy = -pythagoreanFromLabelWidth / 2;
            }
        } else {
            if (LEFT == position) {
                dy = -pythagoreanFromLabelWidth / 2;
            } else if (RIGHT == position) {
                dx = pythagoreanFromAscent;
            } else if (TOP == position) {
                dy = -pythagoreanFromLabelWidth / 2;
            } else if (BOTTOM == position) {
                dy = (pythagoreanFromAscent + pythagoreanFromLabelWidth / 2) - labelTextAscentTab[position];
            }
        }
        tiltedLabelXTranslation[position] = dx;
        tiltedLabelYTranslation[position] = dy;
    }

    private void initAxisMargin(Axis axis, int position) {
        int margin = 0;
        int axisNameMargin = 0;
        if (!axis.isInside() && (axis.isAutoGenerated() || !axis.getValues().isEmpty())) {
            margin += axisMargin + labelDimensionForMarginsTab[position] + axisMargin;
        }
        // margin += getAxisNameMargin(axis, position);
        mAxisMarginWithLabel[position] = margin;
        axisNameMargin = getAxisNameMargin(axis, position);
        insetContentRectWithAxesMargins(axis, margin, axisNameMargin, position);
    }

    private int[] mAxisMarginWithLabel = new int[4];

    // 获取包括label的axes边距
    public int getAxesMarginWithLabel(int position) {
        return mAxisMarginWithLabel[position];
    }

    private int getAxisNameMargin(Axis axis, int position) {
        int margin = 0;
        if (!TextUtils.isEmpty(axis.getName())) {
            margin += labelTextAscentTab[position];
            margin += labelTextDescentTab[position];
            margin += axisNameMargin;
        }
        return margin;
    }

    private void insetContentRectWithAxesMargins(Axis axis, int axisMargin, int axisNameMargin, int position) {
        if (LEFT == position) {
            chart.getChartComputator()
                 .insetContentRect(axis.hasYLabels() ? axisMargin : 0, axisNameMargin, 0, 0);
        } else if (RIGHT == position) {
            chart.getChartComputator().insetContentRect(0, axisNameMargin, axisMargin, 0);
        } else if (TOP == position) {
            chart.getChartComputator().insetContentRect(0, axisMargin, axisNameMargin, 0);
        } else if (BOTTOM == position) {
            chart.getChartComputator().insetContentRect(0, 0, axisNameMargin, axisMargin);
        }
    }

    private void initAxisMeasurements(Axis axis, int position) {
        if (LEFT == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = computator.getContentRectMinusAllMargins().left + axisMargin;
                nameBaselineTab[position] = computator.getContentRectMinusAxesMargins().left - axisMargin
                        - labelTextDescentTab[position];
            } else {
                labelBaselineTab[position] = computator.getContentRectMinusAxesMargins().left - axisMargin;
                nameBaselineTab[position] = labelBaselineTab[position] - axisMargin
                        - labelTextDescentTab[position] - labelDimensionForMarginsTab[position];
            }
            separationLineTab[position] = computator.getContentRectMinusAllMargins().left;
        } else if (RIGHT == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = computator.getContentRectMinusAllMargins().right - axisMargin;
                nameBaselineTab[position] = computator.getContentRectMinusAxesMargins().right + axisMargin
                        + labelTextAscentTab[position];
            } else {
                labelBaselineTab[position] = computator.getContentRectMinusAxesMargins().right + axisMargin;
                nameBaselineTab[position] = labelBaselineTab[position] + axisMargin
                        + labelTextAscentTab[position] + labelDimensionForMarginsTab[position];
            }
            separationLineTab[position] = computator.getContentRectMinusAllMargins().right;
        } else if (BOTTOM == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = computator.getContentRectMinusAllMargins().bottom - axisMargin
                        - labelTextDescentTab[position];
                nameBaselineTab[position] = computator.getContentRectMinusAxesMargins().bottom + axisMargin
                        + labelTextAscentTab[position];
            } else {
                labelBaselineTab[position] = computator.getContentRectMinusAxesMargins().bottom + axisMargin
                        + labelTextAscentTab[position];
                nameBaselineTab[position] = labelBaselineTab[position] + axisMargin +
                        labelDimensionForMarginsTab[position];
            }
            separationLineTab[position] = computator.getContentRectMinusAllMargins().bottom;
        } else if (TOP == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = computator.getContentRectMinusAllMargins().top + axisMargin
                        + labelTextAscentTab[position];
                nameBaselineTab[position] = computator.getContentRectMinusAxesMargins().top - axisMargin
                        - labelTextDescentTab[position];
            } else {
                labelBaselineTab[position] = computator.getContentRectMinusAxesMargins().top - axisMargin
                        - labelTextDescentTab[position];
                nameBaselineTab[position] = labelBaselineTab[position] - axisMargin -
                        labelDimensionForMarginsTab[position];
            }
            separationLineTab[position] = computator.getContentRectMinusAllMargins().top;
        } else {
            throw new IllegalArgumentException("Invalid axis position: " + position);
        }
    }

    /**
     * Prepare axes coordinates and draw axes lines(if enabled) in the background.
     *
     * @param canvas
     */
    public void drawInBackground(Canvas canvas) {
        /*Axis axis = chart.getChartData().getAxisYLeft();
        if (null != axis) {
            prepareAxisToDraw(axis, LEFT);
            drawAxisLines(canvas, axis, LEFT);
        }

        axis = chart.getChartData().getAxisYRight();
        if (null != axis) {
            prepareAxisToDraw(axis, RIGHT);
            drawAxisLines(canvas, axis, RIGHT);
        }

        axis = chart.getChartData().getAxisXBottom();
        if (null != axis) {
            prepareAxisToDraw(axis, BOTTOM);
            drawAxisLines(canvas, axis, BOTTOM);
        }

        axis = chart.getChartData().getAxisXTop();
        if (null != axis) {
            prepareAxisToDraw(axis, TOP);
            drawAxisLines(canvas, axis, TOP);
        }*/
    }

    private void prepareAxisToDraw(Axis axis, int position) {
        if (axis.isAutoGenerated()) {
            prepareAutoGeneratedAxis(axis, position);
        } else {
            prepareCustomAxis(axis, position);
        }
    }

    /**
     * Draw axes labels and names in the foreground.
     *
     * @param canvas
     */
    public void drawInForeground(Canvas canvas) {
        Axis axis = chart.getChartData().getAxisYLeft();
        if (null != axis) {
            prepareAxisToDraw(axis, LEFT);
            drawAxisLabelsAndName(canvas, axis, LEFT);
            drawAxisLines(canvas, axis, LEFT);
        }

        axis = chart.getChartData().getAxisYRight();
        if (null != axis) {
            prepareAxisToDraw(axis, RIGHT);
            drawAxisLabelsAndName(canvas, axis, RIGHT);
            drawAxisLines(canvas, axis, RIGHT);
        }

        axis = chart.getChartData().getAxisXBottom();
        if (null != axis) {
            prepareAxisToDraw(axis, BOTTOM);
            drawAxisLabelsAndName(canvas, axis, BOTTOM);
            drawAxisLines(canvas, axis, BOTTOM);
        }

        axis = chart.getChartData().getAxisXTop();
        if (null != axis) {
            prepareAxisToDraw(axis, TOP);
            drawAxisLabelsAndName(canvas, axis, TOP);
            drawAxisLines(canvas, axis, TOP);
        }
    }

    private void prepareCustomAxis(Axis axis, int position) {
        final Viewport maxViewport = computator.getMaximumViewport();
        final Viewport visibleViewport = computator.getVisibleViewport();
        final Rect contentRect = computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(position);
        float viewportMin, viewportMax;
        float scale = 1;
        if (isAxisVertical) {
            if (maxViewport.height() > 0 && visibleViewport.height() > 0) {
                scale = contentRect.height() * (maxViewport.height() / visibleViewport.height());
            }
            viewportMin = visibleViewport.bottom;
            viewportMax = visibleViewport.top;
        } else {
            if (maxViewport.width() > 0 && visibleViewport.width() > 0) {
                scale = contentRect.width() * (maxViewport.width() / visibleViewport.width());
            }
            viewportMin = visibleViewport.left;
            viewportMax = visibleViewport.right;
        }
        if (scale == 0) {
            scale = 1;
        }
        int module = (int) Math.max(1,
                Math.ceil((axis.getValues()
                               .size() * labelDimensionForStepsTab[position] * 1.5) / scale));
        //Reinitialize tab to hold lines coordinates.
        if (axis.hasLines() && (linesDrawBufferTab[position].length < axis.getValues()
                                                                          .size() * 4)) {
            linesDrawBufferTab[position] = new float[axis.getValues().size() * 4];
        }
        //Reinitialize tabs to hold all raw values to draw.
        if (rawValuesTab[position].length < axis.getValues().size()) {
            rawValuesTab[position] = new float[axis.getValues().size()];
        }
        //Reinitialize tabs to hold all raw values to draw.
        if (valuesToDrawTab[position].length < axis.getValues().size()) {
            valuesToDrawTab[position] = new AxisValue[axis.getValues().size()];
        }

        float rawValue;
        int valueIndex = 0;
        int valueToDrawIndex = 0;
        for (AxisValue axisValue : axis.getValues()) {
            // Draw axis values that are within visible viewport.
            final float value = axisValue.getValue();
            if (value >= viewportMin && value <= viewportMax) {
                // Draw axis values that have 0 module value, this will hide some labels if there is no place for them.
                if (0 == valueIndex % module) {
                    if (isAxisVertical) {
                        rawValue = computator.computeRawY(value);
                    } else {
                        rawValue = computator.computeRawX(value);
                    }
                    if (checkRawValue(contentRect, rawValue, axis.isInside(), position, isAxisVertical)) {
                        rawValuesTab[position][valueToDrawIndex] = rawValue;
                        valuesToDrawTab[position][valueToDrawIndex] = axisValue;
                        ++valueToDrawIndex;
                    }
                }
                // If within viewport - increment valueIndex;
                ++valueIndex;
            }
        }
        valuesToDrawNumTab[position] = valueToDrawIndex;
    }

    private void prepareAutoGeneratedAxis(Axis axis, int position) {
        final Viewport visibleViewport = computator.getVisibleViewport();
        final Rect contentRect = computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(position);
        float start, stop;
        int contentRectDimension;
        if (isAxisVertical) {
            start = visibleViewport.bottom;
            stop = visibleViewport.top;
            contentRectDimension = contentRect.height();
        } else {
            start = visibleViewport.left;
            stop = visibleViewport.right;
            contentRectDimension = contentRect.width();
        }
        if (isCustomAxesStep) {
            FloatUtils.computeAutoGeneratedAxisValuesWithCustomStep(start, stop, customStep, multiple, autoValuesBufferTab[position], computator);
        } else {
            FloatUtils.computeAutoGeneratedAxisValues(start, stop, Math.abs(contentRectDimension) /
                    labelDimensionForStepsTab[position] / 2, autoValuesBufferTab[position]);
        }
        //Reinitialize tab to hold lines coordinates.
        if (axis.hasLines()
                && (linesDrawBufferTab[position].length < autoValuesBufferTab[position].valuesNumber * 4)) {
            linesDrawBufferTab[position] = new float[autoValuesBufferTab[position].valuesNumber * 4];
        }
        //Reinitialize tabs to hold all raw and auto values.
        if (rawValuesTab[position].length < autoValuesBufferTab[position].valuesNumber) {
            rawValuesTab[position] = new float[autoValuesBufferTab[position].valuesNumber];
        }
        if (autoValuesToDrawTab[position].length < autoValuesBufferTab[position].valuesNumber) {
            autoValuesToDrawTab[position] = new float[autoValuesBufferTab[position].valuesNumber];
        }

        float rawValue;
        int valueToDrawIndex = 0;
        for (int i = 0; i < autoValuesBufferTab[position].valuesNumber; ++i) {
            if (isAxisVertical) {
                if (isCustomAxesStep) {
                    rawValue = computator.computeAxisRawY(autoValuesBufferTab[position].values[i], autoValuesBufferTab[position].complementHeight);
                } else {
                    rawValue = computator.computeRawY(autoValuesBufferTab[position].values[i]);
                }
            } else {
                rawValue = computator.computeRawX(autoValuesBufferTab[position].values[i]);
            }
            if (checkRawValue(contentRect, rawValue, axis.isInside(), position, isAxisVertical)) {
                rawValuesTab[position][valueToDrawIndex] = rawValue;
                autoValuesToDrawTab[position][valueToDrawIndex] = autoValuesBufferTab[position].values[i];
                ++valueToDrawIndex;
            }
        }
        valuesToDrawNumTab[position] = valueToDrawIndex;
    }

    private boolean checkRawValue(Rect rect, float rawValue, boolean axisInside, int position, boolean isVertical) {
        if (axisInside) {
            if (isVertical) {
                float marginBottom = labelTextAscentTab[BOTTOM] + axisMargin;
                float marginTop = labelTextAscentTab[TOP] + axisMargin;
                if (rawValue <= rect.bottom - marginBottom && rawValue >= rect.top + marginTop) {
                    return true;
                } else {
                    return false;
                }
            } else {
                float margin = labelWidthTab[position] / 2;
                if (rawValue >= rect.left + margin && rawValue <= rect.right - margin) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawAxisLines(Canvas canvas, Axis axis, int position) {
        // final Rect contentRectMargins = computator.getContentRectMinusAxesMargins();
        // 此处不需要有突出
        final Rect contentRectMargins = computator.getContentRectMinusAllMargins();
        float separationX1, separationY1, separationX2, separationY2;
        separationX1 = separationY1 = separationX2 = separationY2 = 0;
        float lineX1, lineY1, lineX2, lineY2;
        lineX1 = lineY1 = lineX2 = lineY2 = 0;
        boolean isAxisVertical = isAxisVertical(position);
        if (LEFT == position || RIGHT == position) {
            separationX1 = separationX2 = separationLineTab[position];
            separationY1 = contentRectMargins.bottom;
            // separationY2 = contentRectMargins.top - axisVerticalMargin * density; // y轴高度
            separationY2 = computator.getContentRectMinusAxesMargins().top;
            lineX1 = contentRectMargins.left;
            lineX2 = contentRectMargins.right;
        } else if (TOP == position || BOTTOM == position) {
            separationX1 = contentRectMargins.left - axisHorizontalLeftMargin * density;
            separationX2 = contentRectMargins.right;
            separationY1 = separationY2 = separationLineTab[position];
            lineY1 = computator.getContentRectMinusAxesMargins().top;
            lineY2 = contentRectMargins.bottom;
        }
        // Draw separation line with the same color as axis labels and name.
        if (axis.hasSeparationLine()) {
            canvas.drawLine(separationX1, separationY1, separationX2, separationY2, linePaintTab[position]);
        }

        if (axis.hasLines()) {
            int valueToDrawIndex = 0;
            for (; valueToDrawIndex < valuesToDrawNumTab[position]; ++valueToDrawIndex) {
                if (isAxisVertical) {
                    lineY1 = lineY2 = rawValuesTab[position][valueToDrawIndex];
                } else {
                    lineX1 = lineX2 = rawValuesTab[position][valueToDrawIndex];
                }

                if (position == BOTTOM && !axis.isHasEndLine() && valueToDrawIndex == valuesToDrawNumTab[position] - 1) {
                    // 最右的线不绘制
                } else {
                    linesDrawBufferTab[position][valueToDrawIndex * 4 + 0] = lineX1;
                    linesDrawBufferTab[position][valueToDrawIndex * 4 + 1] = lineY1;
                    linesDrawBufferTab[position][valueToDrawIndex * 4 + 2] = lineX2;
                    linesDrawBufferTab[position][valueToDrawIndex * 4 + 3] = lineY2;
                }
            }
            canvas.drawLines(linesDrawBufferTab[position], 0, valueToDrawIndex * 4, linePaintTab[position]);
        }
    }

    private void drawAxisLabelsAndName(Canvas canvas, Axis axis, int position) {
        boolean isAxisVertical = isAxisVertical(position);

        // Drawing axis name
        final Rect contentRectMargins = computator.getContentRectMinusAxesMargins();
        if (!TextUtils.isEmpty(axis.getName())) {
            if (isAxisVertical) {
                canvas.save();
                /*canvas.rotate(-90, contentRectMargins.centerY(), contentRectMargins.centerY());
                canvas.drawText(axis.getName(), contentRectMargins.centerY(), nameBaselineTab[position],
                        namePaintTab[position]);*/
                canvas.drawText(axis.getName(),
                        computator.getContentRectMinusAllMargins().left,
                        computator.getContentRectMinusAxesMargins().top / 2 + namePaintTab[position]
                                .getTextSize() / 2,
                        namePaintTab[position]);
                canvas.restore();
            } else {
                canvas.drawText(axis.getName(), contentRectMargins.right + ChartUtils.dp2px(density, 3), labelBaselineTab[position],
                        namePaintTab[position]);
            }
        }

        if (!isAxisVertical && !axis.hasXLabels())
            return;

        if (isAxisVertical && !axis.hasYLabels())
            return;

        float labelX, labelY;
        labelX = labelY = 0;

        if (LEFT == position || RIGHT == position) {
            labelX = labelBaselineTab[position] + axisDegreeMargin;
        } else if (TOP == position || BOTTOM == position) {
            labelY = labelBaselineTab[position];
        }

        for (int valueToDrawIndex = 0; valueToDrawIndex < valuesToDrawNumTab[position]; ++valueToDrawIndex) {
            if (hideFirstAxisLabel && valueToDrawIndex == 0 && (position == LEFT || position == RIGHT))
                continue;
            //文字总长度
            int charsNumber = 0;
            AxisValue axisValue = null;
            if (axis.isAutoGenerated()) {
                final float value = autoValuesToDrawTab[position][valueToDrawIndex];
                charsNumber = axis.getFormatter()
                                  .formatValueForAutoGeneratedAxis(labelBuffer, value,
                                          autoValuesBufferTab[position].decimals);
            } else {
                axisValue = valuesToDrawTab[position][valueToDrawIndex];
                charsNumber = axis.getFormatter().formatValueForManualAxis(labelBuffer, axisValue);
            }

            if (isAxisVertical) {
                labelY = rawValuesTab[position][valueToDrawIndex] + labelPaintTab[position].getTextSize() / 2; // 文字与线居中对齐
            } else {
                labelX = rawValuesTab[position][valueToDrawIndex];
            }
            //倾斜
            int indexStart = labelBuffer.length - charsNumber;
            if (axis.hasTiltedLabels()) {
                canvas.save();
                canvas.translate(tiltedLabelXTranslation[position], tiltedLabelYTranslation[position]);
                canvas.rotate(-45, labelX, labelY);
                canvas.drawText(labelBuffer, indexStart, charsNumber, labelX, labelY,
                        labelPaintTab[position]);
                canvas.restore();
            } else {
                int indexOfMultiLine = new String(labelBuffer).lastIndexOf("\n");

                // 绘制第二行,用"\n"分割,记得chartView的Bottom 需要pading留出足够空间
                if (indexOfMultiLine > 0 && axisValue != null) {
                    String label = new String(axisValue.getLabelAsChars());
                    //换行开始
                    int indexGap = label.indexOf("\n");
                    int countAfterNewline = label.length() - indexGap - 1;
                    //单行
                    if (indexGap < 0) {
                        //X轴的值
                        canvas.drawText(labelBuffer, indexStart, charsNumber, labelX, labelY,
                                labelPaintTab[position]);
                    } else {
                        //双行
                        String strFirst = label.substring(0, indexGap);
                        String strSecond = label.substring(indexGap + 1, label.length());
                        if (axisValue.isHasBg()) {
                            // 创建画笔  
                            Paint paintBg = new Paint();
                            //画圆角矩形  
                            paintBg.setStyle(Paint.Style.FILL);//充满  
                            paintBg.setColor(ChartUtils.COLOR_Axis_Backgroud);
                            //圆角矩形；
                            float width = 18 * density;
                            float height = 4 * density;
                            RectF oval3 = new RectF(labelX - 20 * density, labelY - 12 * density, labelX + width, labelY + height);
                            //第二个参数是x半径，第三个参数是y半径
                            canvas.drawRoundRect(oval3, width / 2, width / 2, paintBg);
                            Paint paintText = new Paint(labelPaintTab[position]);
                            paintText.setColor(ChartUtils.COLOR_WHITE);
                            canvas.drawText(strFirst.toCharArray(), 0, strFirst.length(), labelX, labelY, paintText);
                        } else {
                            canvas.drawText(strFirst.toCharArray(), 0, strFirst.length(), labelX, labelY,
                                    labelPaintTab[position]);
                        }

                        //第二行，如果是今天 需要高亮红色
                        float secondLabelY = labelY + 16 * density;
                        Paint labelPaint = getLabelPaint(strSecond, position);
                        int indexSecond = indexStart + indexGap + 1;
                        canvas.drawText(strSecond.toCharArray(), 0, strSecond.length(), labelX, secondLabelY, labelPaint);
                    }
                } else {
                    // 普通绘制
                    canvas.drawText(labelBuffer, indexStart, charsNumber, labelX, labelY,
                            labelPaintTab[position]);
                }
            }
        }

    }

    private Paint getLabelPaint(String label, int position) {
        if (!highLightAxisLabel) {
            return labelPaintTab[position];
        }
        if (label.equals("今天")) {
            return highLightLabelPaint;
        } else {
            return labelPaintTab[position];
        }
    }

    private boolean isAxisVertical(int position) {
        if (LEFT == position || RIGHT == position) {
            return true;
        } else if (TOP == position || BOTTOM == position) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid axis position " + position);
        }
    }

    public void setCustomAxesStep(boolean isCustomAxesStep) {
        this.isCustomAxesStep = isCustomAxesStep;
    }

    public void setCustomStep(int customStep, int multiple) {
        this.customStep = customStep;
        this.multiple = multiple;
    }

    public void setAxisDegreeMargin(int axisDegreeMargin) {
        this.axisDegreeMargin = ChartUtils.dp2px(density, axisDegreeMargin);
    }

    public void setAxisVerticalMargin(float axisVerticalMargin) {
        this.axisVerticalMargin = axisVerticalMargin;
    }

    public void setAxisHorizontalLeftMargin(float axisHorizontalLeftMargin) {
        this.axisHorizontalLeftMargin = axisHorizontalLeftMargin;
    }

    public void setHideFirstAxisLabel(boolean hideFirstAxisLabel) {
        this.hideFirstAxisLabel = hideFirstAxisLabel;
    }

    public void setHighLightAxisLabel(boolean highLightAxisLabel) {
        this.highLightAxisLabel = highLightAxisLabel;
    }
}