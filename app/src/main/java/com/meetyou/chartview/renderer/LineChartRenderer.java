package com.meetyou.chartview.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.meetyou.chartview.model.Line;
import com.meetyou.chartview.model.LineChartData;
import com.meetyou.chartview.model.PointValue;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.model.ValueShape;
import com.meetyou.chartview.model.Viewport;
import com.meetyou.chartview.provider.LineChartDataProvider;
import com.meetyou.chartview.util.ChartUtils;
import com.meetyou.chartview.view.Chart;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;


/**
 * Renderer for line chart. Can draw lines, cubic lines, filled area chart and scattered chart.
 */
public class LineChartRenderer extends AbstractChartRenderer {
    private static final float LINE_SMOOTHNESS = 0.05f;
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 1;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;
    public static final int DEFAULT_BIGGEST_VALUE_TEXT_DP = 13;

    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private final Drawable drawableFill;

    private LineChartDataProvider dataProvider;

    private int checkPrecision;
    /**
     * 坐标轴的值
     */
    public float baseValue;
    private float titleSize; // 高亮文字大小
    private float highlightCircleRadius; // 高亮半径
    private float scale; // 图表scale到viewport为6时需要scale的大小 6 / xAxisLabel.size()
    private float viewPortRight; // 图表最右viewPort值
    private float visibleViewPortRight; // 可视范围的right值

    private int touchToleranceMargin;
    private Path path = new Path();
    private Paint linePaint = new Paint();
    private Paint pointPaint = new Paint();
    private Path mPathStandard = new Path(); // 标准线path
    private Paint mPaintStandard = new Paint(); // 标准线paint
    private Paint mPaintBiggest = new Paint(); // 最大点

    private boolean isFirstStandardPath = false; // 是否是标准线的第一条，标准线只能设置2条
    private boolean isViewPortYCalcEnabled = false; // Y轴是否根据值的范围相应变化

    private Bitmap softwareBitmap;
    private Canvas softwareCanvas = new Canvas();
    private Viewport tempMaximumViewport = new Viewport();

    private Rect rectTitle = new Rect();

    //美柚修改属性
    /**
     * 标签大小
     */
    private float labelSize;
    /**
     * 最高的点Y轴 ；用于画渐变颜色
     */
    private float minY = Integer.MAX_VALUE;

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider dataProvider) {
        super(context, chart);
        this.dataProvider = dataProvider;

        touchToleranceMargin = ChartUtils.dp2px(density, DEFAULT_TOUCH_TOLERANCE_MARGIN_DP);

        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Cap.ROUND);
        linePaint.setStrokeWidth(ChartUtils.dp2px(density, DEFAULT_LINE_STROKE_WIDTH_DP));

        checkPrecision = ChartUtils.dp2px(density, 2);

        titleSize = density * 10.0f;
        highlightCircleRadius = titleSize * 2.5f;
        labelSize = density * 20f;
        labelPaint.setTextSize(labelSize);

        //
        drawableFill = ContextCompat.getDrawable(context, R.drawable.fade_red);
    }

    public void onChartSizeChanged() {
        final int internalMargin = calculateContentRectInternalMargin();
        computator.insetContentRectByInternalMargins(internalMargin, internalMargin,
                internalMargin, internalMargin);
        if (computator.getChartWidth() > 0 && computator.getChartHeight() > 0) {
            softwareBitmap = Bitmap.createBitmap(computator.getChartWidth(), computator.getChartHeight(),
                    Bitmap.Config.ARGB_8888);
            softwareCanvas.setBitmap(softwareBitmap);
        }
    }

    @Override
    public void onChartDataChanged() {
        super.onChartDataChanged();
        final int internalMargin = calculateContentRectInternalMargin();
        computator.insetContentRectByInternalMargins(internalMargin, internalMargin,
                internalMargin, internalMargin);
        baseValue = dataProvider.getLineChartData().getBaseValue();

        onChartViewportChanged();
    }

    @Override
    public void onChartViewportChanged() {
        if (isViewportCalculationEnabled) {
            calculateMaxViewport();
            computator.setMaxViewport(tempMaximumViewport);
            computator.setCurrentViewport(computator.getMaximumViewport());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final LineChartData data = dataProvider.getLineChartData();

        final Canvas drawCanvas;

        // softwareBitmap can be null if chart is rendered in layout editor. In that case use default canvas and not
        // softwareCanvas.
        if (null != softwareBitmap) {
            drawCanvas = softwareCanvas;
            drawCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        } else {
            drawCanvas = canvas;
        }

        for (Line line : data.getLines()) {
            if (line.hasLines()) {
                if (line.isCubic()) {
                    drawSmoothPath(drawCanvas, line);
                } else if (line.isSquare()) {
                    drawSquarePath(drawCanvas, line);
                } else {
                    drawPath(drawCanvas, line);
                }
            }
        }

        if (null != softwareBitmap) {
            canvas.drawBitmap(softwareBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawUnclipped(Canvas canvas) {
        final LineChartData data = dataProvider.getLineChartData();
        int lineIndex = 0;
        for (Line line : data.getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                drawPoints(canvas, line, lineIndex, MODE_DRAW);
            }
            ++lineIndex;
        }
        if (isTouched()) {
            // Redraw touched point to bring it to the front
            highlightPoints(canvas);
        }
    }

    private boolean checkIfShouldDrawPoints(Line line) {
        return line.hasPoints() || line.getValues().size() == 1;
    }

    @Override
    public boolean checkTouch(float touchX, float touchY) {
        //保存一份 上次点击点；
        SelectedValue oldSelectedValue = new SelectedValue();
        oldSelectedValue.set(selectedValue);

        selectedValue.clear();
        final LineChartData data = dataProvider.getLineChartData();
        int lineIndex = 0;
        for (Line line : data.getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int pointRadius = ChartUtils.dp2px(density, line.getPointRadius());
                int valueIndex = 0;
                for (PointValue pointValue : line.getValues()) {
                    final float rawValueX = computator.computeRawX(pointValue.getX());
                    final float rawValueY = computator.computeRawY(pointValue.getY());
                    if (isInArea(rawValueX, rawValueY, touchX, touchY, pointRadius + touchToleranceMargin)) {
                        selectedValue.set(lineIndex, valueIndex, SelectedValue.SelectedValueType.LINE);
                    } else {
                        //前一个选中值是 线上的点 点击； 同时
                        if (lineIndex == oldSelectedValue.getFirstIndex() && valueIndex == oldSelectedValue
                                .getSecondIndex() && pointValue.labelRect.contains(touchX, touchY)) {
                            selectedValue.set(lineIndex, valueIndex, SelectedValue.SelectedValueType.LABEL);
                        }
                    }
                    ++valueIndex;
                }
            }
            ++lineIndex;
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        LineChartData data = dataProvider.getLineChartData();

        if (isViewPortYCalcEnabled()) {
            tempMaximumViewport.left = 0;
            tempMaximumViewport.right = getViewPortRight();
            for (Line line : data.getLines()) {
                for (int i = 0; i < line.getValues().size(); i++) {
                    // 不包括超出屏幕外的点
                    if (i < computator.getVisibleViewport().left || i > computator.getVisibleViewport().right)
                        continue;
                    if (line.getValues().get(i).getY() < tempMaximumViewport.bottom) {
                        tempMaximumViewport.bottom = line.getValues()
                                                         .get(i)
                                                         .getY() - 5 > 0 ? line.getValues()
                                                                               .get(i)
                                                                               .getY() - 5 : 0;
                    }
                    if (line.getValues().get(i).getY() > tempMaximumViewport.top) {
                        tempMaximumViewport.top = line.getValues().get(i).getY();
                    }
                }
            }
        } else {
            for (Line line : data.getLines()) {
                for (PointValue pointValue : line.getValues()) {
                    if (pointValue.getX() < tempMaximumViewport.left) {
                        tempMaximumViewport.left = pointValue.getX();
                    }
                    if (pointValue.getX() > tempMaximumViewport.right) {
                        tempMaximumViewport.right = pointValue.getX();
                    }
                    if (pointValue.getY() < tempMaximumViewport.bottom) {
                        tempMaximumViewport.bottom = pointValue.getY();
                    }
                    if (pointValue.getY() > tempMaximumViewport.top) {
                        tempMaximumViewport.top = pointValue.getY();
                    }
                }
            }
        }
    }

    private int calculateContentRectInternalMargin() {
        int contentAreaMargin = 0;
        final LineChartData data = dataProvider.getLineChartData();
        for (Line line : data.getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int margin = line.getPointRadius() + DEFAULT_TOUCH_TOLERANCE_MARGIN_DP;
                if (margin > contentAreaMargin) {
                    contentAreaMargin = margin;
                }
            }
        }
        return ChartUtils.dp2px(density, contentAreaMargin);
    }

    /**
     * Draws lines, uses path for drawing filled area on software canvas. Line is drawn with canvas.drawLines() method.
     */
    private void drawPath(Canvas canvas, final Line line) {
        prepareLinePaint(line);

        int valueIndex = 0;
        for (PointValue pointValue : line.getValues()) {

            final float rawX = computator.computeRawX(pointValue.getX());
            final float rawY = computator.computeRawY(pointValue.getY());

            if (valueIndex == 0) {
                path.moveTo(rawX, rawY);
            } else {
                path.lineTo(rawX, rawY);
            }

            ++valueIndex;

        }

        canvas.drawPath(path, linePaint);

        if (line.isFilled()) {
            drawArea(canvas, line);
        }

        path.reset();
    }

    private void drawSquarePath(Canvas canvas, final Line line) {
        prepareLinePaint(line);

        int valueIndex = 0;
        float previousRawY = 0;
        for (PointValue pointValue : line.getValues()) {

            final float rawX = computator.computeRawX(pointValue.getX());
            final float rawY = computator.computeRawY(pointValue.getY());

            if (valueIndex == 0) {
                path.moveTo(rawX, rawY);
            } else {
                path.lineTo(rawX, previousRawY);
                path.lineTo(rawX, rawY);
            }

            previousRawY = rawY;

            ++valueIndex;

        }

        canvas.drawPath(path, linePaint);

        if (line.isFilled()) {
            drawArea(canvas, line);
        }

        path.reset();
    }

    private void drawSmoothPath(Canvas canvas, final Line line) {
        prepareLinePaint(line);

        final int lineSize = line.getValues().size();

        if (line.isStandared()) {
            drawStandardArea(canvas, line, lineSize);
        } else {
            drawNormalSmoothLine(canvas, line, lineSize);
        }
    }

    private void drawNormalSmoothLine(Canvas canvas, Line line, int lineSize) {
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;

        List<Float> currentPointXList = new ArrayList<>();
        List<Float> currentPointYList = new ArrayList<>();
        List<Integer> skippedIndexList = new ArrayList<>();

        boolean previousPointSkipped = false;
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            boolean isSkipped = line.getValues().get(valueIndex).isSkipped();
            if (isSkipped) {
                skippedIndexList.add(valueIndex);
            }
            if (Float.isNaN(currentPointX)) {
                PointValue linePoint = line.getValues().get(valueIndex);
                currentPointX = computator.computeRawX(linePoint.getX());
                currentPointY = computator.computeRawY(linePoint.getY());
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    PointValue linePoint = line.getValues().get(valueIndex - 1);
                    previousPointX = computator.computeRawX(linePoint.getX());
                    previousPointY = computator.computeRawY(linePoint.getY());
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    PointValue linePoint = line.getValues().get(valueIndex - 2);
                    prePreviousPointX = computator.computeRawX(linePoint.getX());
                    prePreviousPointY = computator.computeRawY(linePoint.getY());
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // nextPoint is always new one or it is equal currentPoint.
            if (valueIndex < lineSize - 1) {
                PointValue linePoint = line.getValues().get(valueIndex + 1);
                nextPointX = computator.computeRawX(linePoint.getX());
                nextPointY = computator.computeRawY(linePoint.getY());
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            //获取最高点位置
            if (minY > currentPointY) {
                minY = currentPointY;
            }

            if (valueIndex == 0) {
                // Move to start point.
                path.moveTo(currentPointX, currentPointY);
            } else {
                if (isSkipped || previousPointSkipped) {
                    path.moveTo(currentPointX, currentPointY);
                    previousPointSkipped = false;
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                    if (currentPointY == previousPointY) {
                        path.lineTo(currentPointX, currentPointY);
                    } else {
                        path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY, currentPointX, currentPointY);
                    }
                }
            }

            currentPointXList.add(currentPointX);
            currentPointYList.add(currentPointY);

            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;

            // 如果改点无需绘制，则到下一个点的连线也无需绘制
            if (isSkipped) {
                previousPointSkipped = true;
            }
        }

        // 连接skipped point
        drawSkippedLine(canvas, skippedIndexList, currentPointXList, currentPointYList);

        canvas.drawPath(path, linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        path.reset();
    }

    // Only can have two standard lines in a LineChartView
    private void drawStandardArea(Canvas canvas, Line line, int lineSize) {
        int restoreCount = canvas.save();
        canvas.clipRect(computator.getContentRectMinusAllMargins().left, computator.getContentRectMinusAllMargins().top, visibleViewPortRight, computator
                .getContentRectMinusAllMargins().bottom);

        // 是否是绘制的第一条标准线
        if (!isFirstStandardPath) {
            float prePreviousPointX = Float.NaN;
            float prePreviousPointY = Float.NaN;
            float previousPointX = Float.NaN;
            float previousPointY = Float.NaN;
            float currentPointX = Float.NaN;
            float currentPointY = Float.NaN;
            float nextPointX = Float.NaN;
            float nextPointY = Float.NaN;

            for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
                if (Float.isNaN(currentPointX)) {
                    PointValue linePoint = line.getValues().get(valueIndex);
                    currentPointX = computator.computeRawX(linePoint.getX());
                    currentPointY = computator.computeRawY(linePoint.getY());
                }
                if (Float.isNaN(previousPointX)) {
                    if (valueIndex > 0) {
                        PointValue linePoint = line.getValues().get(valueIndex - 1);
                        previousPointX = computator.computeRawX(linePoint.getX());
                        previousPointY = computator.computeRawY(linePoint.getY());
                    } else {
                        previousPointX = currentPointX;
                        previousPointY = currentPointY;
                    }
                }

                if (Float.isNaN(prePreviousPointX)) {
                    if (valueIndex > 1) {
                        PointValue linePoint = line.getValues().get(valueIndex - 2);
                        prePreviousPointX = computator.computeRawX(linePoint.getX());
                        prePreviousPointY = computator.computeRawY(linePoint.getY());
                    } else {
                        prePreviousPointX = previousPointX;
                        prePreviousPointY = previousPointY;
                    }
                }

                // nextPoint is always new one or it is equal currentPoint.
                if (valueIndex < lineSize - 1) {
                    PointValue linePoint = line.getValues().get(valueIndex + 1);
                    nextPointX = computator.computeRawX(linePoint.getX());
                    nextPointY = computator.computeRawY(linePoint.getY());
                } else {
                    nextPointX = currentPointX;
                    nextPointY = currentPointY;
                }


                if (valueIndex == 0) {
                    // Move to start point.
                    mPathStandard.moveTo(currentPointX, currentPointY);
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                    mPathStandard.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY, currentPointX, currentPointY);
                }


                // Shift values by one back to prevent recalculation of values that have
                // been already calculated.
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
                previousPointX = currentPointX;
                previousPointY = currentPointY;
                currentPointX = nextPointX;
                currentPointY = nextPointY;
            }
            isFirstStandardPath = true;
        } else {
            float prePreviousPointX = Float.NaN;
            float prePreviousPointY = Float.NaN;
            float previousPointX = Float.NaN;
            float previousPointY = Float.NaN;
            float currentPointX = Float.NaN;
            float currentPointY = Float.NaN;
            float nextPointX = Float.NaN;
            float nextPointY = Float.NaN;

            for (int valueIndex = lineSize - 1; valueIndex >= 0; --valueIndex) {
                if (Float.isNaN(currentPointX)) {
                    PointValue linePoint = line.getValues().get(valueIndex);
                    currentPointX = computator.computeRawX(linePoint.getX());
                    currentPointY = computator.computeRawY(linePoint.getY());
                }
                if (Float.isNaN(previousPointX)) {
                    if (valueIndex < lineSize - 1) {
                        PointValue linePoint = line.getValues().get(valueIndex + 1);
                        previousPointX = computator.computeRawX(linePoint.getX());
                        previousPointY = computator.computeRawY(linePoint.getY());
                    } else {
                        previousPointX = currentPointX;
                        previousPointY = currentPointY;
                    }
                }

                if (Float.isNaN(prePreviousPointX)) {
                    if (valueIndex < lineSize - 2) {
                        PointValue linePoint = line.getValues().get(valueIndex + 2);
                        prePreviousPointX = computator.computeRawX(linePoint.getX());
                        prePreviousPointY = computator.computeRawY(linePoint.getY());
                    } else {
                        prePreviousPointX = previousPointX;
                        prePreviousPointY = previousPointY;
                    }
                }

                // nextPoint is always new one or it is equal currentPoint.
                if (valueIndex > 0) {
                    PointValue linePoint = line.getValues().get(valueIndex - 1);
                    nextPointX = computator.computeRawX(linePoint.getX());
                    nextPointY = computator.computeRawY(linePoint.getY());
                } else {
                    nextPointX = currentPointX;
                    nextPointY = currentPointY;
                }


                if (valueIndex == lineSize - 1) {
                    // Move to start point.
                    mPathStandard.lineTo(currentPointX, currentPointY);
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                    mPathStandard.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY, currentPointX, currentPointY);
                }

                // Shift values by one back to prevent recalculation of values that have
                // been already calculated.
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
                previousPointX = currentPointX;
                previousPointY = currentPointY;
                currentPointX = nextPointX;
                currentPointY = nextPointY;
            }
            isFirstStandardPath = false;

            mPathStandard.close();
            canvas.drawPath(mPathStandard, mPaintStandard);
            mPathStandard.reset();
        }
        canvas.restoreToCount(restoreCount);
    }

    // 绘制跳过的点到其他点的连接线
    private void drawSkippedLine(Canvas canvas, List<Integer> skipIndexList, List<Float> xValueList, List<Float> yValueList) {
        Path skippedPath = new Path();
        for (int i = 0; i < skipIndexList.size(); i++) {
            int start = i;
            int size = 1;
            int end = start;
            if (i < skipIndexList.size() - 1) {
                end = start + 1;
                while (skipIndexList.get(end) == skipIndexList.get(end - 1) + 1) {
                    size++;
                    end++;
                    if (end >= skipIndexList.size())
                        break;
                }
            }
            if (size >= 2) {
                if (skipIndexList.get(end - 1) == (xValueList.size() - 1)) // 最后一个点是skipped无需绘制
                    continue;
                if (skipIndexList.get(start) != 0) {
                    skippedPath.moveTo(xValueList.get(skipIndexList.get(start) - 1), yValueList.get(skipIndexList
                            .get(start) - 1));
                    skippedPath.lineTo(xValueList.get(skipIndexList.get(end - 1) + 1), yValueList.get(skipIndexList
                            .get(end - 1) + 1));
                    canvas.drawPath(skippedPath, linePaint);
                    skippedPath.reset();
                }
                i = end - 1;
            } else {
                if (skipIndexList.get(i) == (xValueList.size() - 1)) // 最后一个点是skipped无需绘制
                    continue;
                if (skipIndexList.get(i) != 0) {
                    skippedPath.moveTo(xValueList.get(skipIndexList.get(i) - 1), yValueList.get(skipIndexList
                            .get(i) - 1));
                    skippedPath.lineTo(xValueList.get(skipIndexList.get(i) + 1), yValueList.get(skipIndexList
                            .get(i) + 1));
                    canvas.drawPath(skippedPath, linePaint);
                    skippedPath.reset();
                }
            }
        }

    }

    private void prepareLinePaint(final Line line) {
        linePaint.setStrokeWidth(ChartUtils.dp2px(density, line.getStrokeWidth()));
        linePaint.setColor(line.getColor());
        linePaint.setPathEffect(line.getPathEffect());

        mPaintStandard.setStyle(Paint.Style.FILL);
        mPaintStandard.setColor(line.getStandardAreaColor());
        mPaintStandard.setStrokeWidth(0);
    }

    // TODO Drawing points can be done in the same loop as drawing lines but it
    // may cause problems in the future with
    // implementing point styles.
    private void drawPoints(Canvas canvas, Line line, int lineIndex, int mode) {
        preparePointPaint(line);
        pointPaint.setColor(line.getPointColor());
        int valueIndex = 0;
        for (PointValue pointValue : line.getValues()) {
            int pointRadius = ChartUtils.dp2px(density, line.getPointRadius());
            final float rawX = computator.computeRawX(pointValue.getX());
            final float rawY = computator.computeRawY(pointValue.getY());
            if (computator.isWithinContentRect(rawX, rawY, checkPrecision)) {
                // Draw points only if they are within contentRectMinusAllMargins, using contentRectMinusAllMargins
                // instead of viewport to avoid some
                // float rounding problems.
                if (MODE_DRAW == mode) {
                    drawPoint(canvas, line, pointValue, rawX, rawY, pointRadius, false);
                    if (line.hasLabels()) {
                        drawLabel(canvas, line, pointValue, rawX, rawY, pointRadius + labelOffset);
                    }
                } else if (MODE_HIGHLIGHT == mode) {
                    highlightPoint(canvas, line, pointValue, rawX, rawY, lineIndex, valueIndex);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + mode);
                }
            }
            ++valueIndex;
        }
    }

    private void preparePointPaint(Line line) {
        mPaintBiggest.setColor(line.getColor());
        mPaintBiggest.setTextSize(DEFAULT_BIGGEST_VALUE_TEXT_DP * density);
    }


    /**
     * 画连接点
     *
     * @param canvas
     * @param line
     * @param pointValue
     * @param rawX
     * @param rawY
     * @param pointRadius
     * @param isHightLight  选中，是否需要高亮的点
     */
    public void drawPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY,
                          float pointRadius, boolean isHightLight) {
        if (pointValue.isSkipped())
            return;

        if (ValueShape.SQUARE.equals(line.getShape())) {
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius,
                    pointPaint);
        } else if (ValueShape.CIRCLE.equals(line.getShape())) {

            pointPaint.setStyle(Paint.Style.STROKE);
            //体温曲线，选中的圆圈更大
            float strokeWidth = density * 2;
            if (isHightLight) {
                strokeWidth = density * 4;
            }
            pointPaint.setStrokeWidth(strokeWidth);

            int color = line.getColor();
            if (pointValue.isMockData) {
                color = ChartUtils.COLOR_MY_GRAY;
            }
            pointPaint.setColor(color);
            //先刷一圈白色的
            Paint pointBackPaint = new Paint(pointPaint);
            pointBackPaint.setStyle(Paint.Style.FILL);
            pointBackPaint.setColor(ChartUtils.COLOR_WHITE);
            canvas.drawCircle(rawX, rawY, pointRadius, pointBackPaint);
            //再刷真正的圆圈；
            canvas.drawCircle(rawX, rawY, pointRadius, pointPaint);

//            pointPaint.setAntiAlias(true);
//            pointPaint.setColor(line.getColor());
//            pointPaint.setStyle(Paint.Style.STROKE);
//            pointPaint.setStrokeWidth(density * 2);
//            canvas.drawCircle(rawX, rawY, pointRadius + 2, pointPaint);
//            if (pointValue.isHighLight()) {
//                pointPaint.setColor(line.getColor());
//            } else {
//                pointPaint.setColor(ChartUtils.COLOR_WHITE);
//            }
//            pointPaint.setStyle(Paint.Style.FILL);
//            canvas.drawCircle(rawX, rawY, pointRadius, pointPaint);

            // Draw biggest point value.-- 成长曲线
            if (pointValue.isBiggest()) {
                String title = pointValue.getTitle();
                mPaintBiggest.getTextBounds(title, 0, title.length(), rectTitle);
                canvas.drawText(title, rawX - (rectTitle.width() / 2), rawY - 10 * density, mPaintBiggest);
            }
        } else if (ValueShape.DIAMOND.equals(line.getShape())) {
            canvas.save();
            canvas.rotate(45, rawX, rawY);
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius,
                    pointPaint);
            canvas.restore();
        } else if (ValueShape.TEXT.equals(line.getShape())) {
            String label = new String(pointValue.getLabelAsChars());
            float x = rawX;
            float y = rawY;
            //画笔
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ChartUtils.COLOR_BLACK);
            paint.setTextSize(12 * density);
            paint.setAntiAlias(true);
//            paint.setStrokeWidth(4);
            canvas.drawText(label, x, y, paint);
        } else {
//            Log.e("LineChartRender", "Invalid point shape: " + line.getShape());
//            throw new IllegalArgumentException("Invalid point shape: " + line.getShape());
        }
    }

    private void highlightPoints(Canvas canvas) {
        int lineIndex = selectedValue.getFirstIndex();
        Line line = dataProvider.getLineChartData().getLines().get(lineIndex);
        drawPoints(canvas, line, lineIndex, MODE_HIGHLIGHT);
    }

    private void highlightPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, int lineIndex,
                                int valueIndex) {

        if (!line.isEnableHighLightPoint())
            return;

        if (pointValue.isSkipped())
            return;

        if (selectedValue.getFirstIndex() == lineIndex && selectedValue.getSecondIndex() == valueIndex) {
            //画选中的点
            int pointRadius = ChartUtils.dp2px(density, line.getPointRadius());
            pointPaint.setColor(line.getDarkenColor());
            drawPoint(canvas, line, pointValue, rawX, rawY, pointRadius + touchToleranceMargin, true);

            if (line.hasLabels() || line.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, line, pointValue, rawX, rawY, pointRadius + labelOffset);
            }
            //经期图表中选中的 圆形点击框 
//            drawCircleLabel(canvas,line,pointValue,rawX,rawY,lineIndex,valueIndex);
        }
    }

    /**
     * 经期图表中选中的 圆形点击框
     *
     * @param canvas
     * @param line
     * @param pointValue
     * @param rawX
     * @param rawY
     * @param lineIndex
     * @param valueIndex
     */
//    private void drawCircleLabel(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, int lineIndex,
//                                 int valueIndex) {
//
//        Paint mPaintBalloon = new Paint();
//        mPaintBalloon.setColor(linePaint.getColor());
//        mPaintBalloon.setStyle(Paint.Style.FILL);
//        mPaintBalloon.setStrokeWidth(density * 2.0f);
//
//        Paint mPaintBalloonShadow = new Paint(mPaintBalloon);
//        mPaintBalloonShadow.setColor(line.getShadowColor());
//
//        Paint mPaintHighLightText = new Paint();
//        mPaintHighLightText.setColor(line.getHighlightTextColor());
//        mPaintHighLightText.setTextSize(titleSize);
//
//        // int pointRadius = ChartUtils.dp2px(density, line.getPointRadius());
//        float circleY;
//
//        // 距下发4/5内的位置往上绘制,剩下的往下绘制
//        if (rawY > computator.getContentRectMinusAllMargins().top + computator.getContentRectMinusAllMargins()
//                                                                              .height() * 0.2f) {
//            circleY = computator.getContentRectMinusAllMargins().bottom * 0.2f; // 往上绘制
//        } else {
//            circleY = (computator.getContentRectMinusAllMargins().top + computator.getContentRectMinusAllMargins()
//                                                                                  .height()) * 0.5f; // 往下绘制
//        }
//
//        // 绘制圆形阴影
//        canvas.drawCircle(rawX, circleY, highlightCircleRadius * 1.25f, mPaintBalloonShadow);
//
//        // 绘制圆形
//        canvas.drawCircle(rawX, circleY, highlightCircleRadius, mPaintBalloon);
//
//        // 绘制连接线
//        canvas.drawLine(rawX, rawY, rawX, circleY, mPaintBalloon);
//
//        // 绘制高亮文字
//        String title = pointValue.getTitle();
//        if (!ChartUtils.isNull(title)) {
//            mPaintHighLightText.getTextBounds(title, 0, title.length(), rectTitle);
//            canvas.drawText(title, rawX - (rectTitle.width() / 2), circleY - 0.2f * highlightCircleRadius, mPaintHighLightText);
//
//            title = pointValue.getSubTitle();
//            mPaintHighLightText.getTextBounds(title, 0, title.length(), rectTitle);
//            canvas.drawText(title, rawX - (rectTitle.width() / 2), circleY + 0.2f * highlightCircleRadius + rectTitle
//                    .height(), mPaintHighLightText);
//        }
//    }

    /**
     * 画点上的标签
     *
     * @param canvas
     * @param line
     * @param pointValue
     * @param rawX
     * @param rawY
     * @param offset
     */
    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float offset) {
        final Rect contentRect = computator.getContentRectMinusAllMargins();
        //创建 label 文字
        int numChars = line.getFormatter().formatChartValue(labelBuffer, pointValue);
        String s = pointValue.getY() + "";
        String result = s + this.dataProvider.getLineChartData().unit;
        labelPaint.setTextSize(20 * density);
        if (line.isEnableLabelColorCustom()) {
            labelPaint.setColor(pointValue.getColor());
        }else{
            labelPaint.setColor(line.getColor());
        }
        
        if (pointValue.isMockData) {
            result = "未记录";
            labelPaint.setColor(ChartUtils.COLOR_MY_GRAY);
        }
        labelBuffer = result.toCharArray();
        numChars = labelBuffer.length;

        if (numChars == 0) {
            // No need to draw empty label
            return;
        }

        final float labelWidth = labelPaint.measureText(labelBuffer, labelBuffer.length - numChars, numChars);
        final int labelHeight = Math.abs(fontMetrics.ascent);
        float left = rawX - labelWidth / 2 - labelMargin;
        float right = rawX + labelWidth / 2 + labelMargin;

        float top;
        float bottom;

        if (pointValue.getY() >= baseValue) {
            top = rawY - offset - labelHeight - labelMargin * 2;
            bottom = rawY - offset;
        } else {
            top = rawY + offset;
            bottom = rawY + offset + labelHeight + labelMargin * 2;
        }


        if (top < contentRect.top) {
            top = rawY + offset;
            bottom = rawY + offset + labelHeight + labelMargin * 2;
        }
        if (bottom > contentRect.bottom) {
            top = rawY - offset - labelHeight - labelMargin * 2;
            bottom = rawY - offset;
        }
        if (left < contentRect.left) {
            left = rawX;
            right = rawX + labelWidth + labelMargin * 2;
        }
        if (right > contentRect.right) {
            left = rawX - labelWidth - labelMargin * 2;
            right = rawX;
        }

        labelBackgroundRect.set(left, top, right, bottom);
        //保存Label位置
        pointValue.labelRect = new RectF(labelBackgroundRect);

        //美柚Label
        drawLabelTextAndBackground(canvas, labelBuffer, labelBuffer.length - numChars, numChars,
                line.getDarkenColor());
        if (pointValue.isMockData && line.enableLabelSelect) {
            //未记录图片
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_edit);
            canvas.drawBitmap(bitmap, labelBackgroundRect.right, labelBackgroundRect.top, new Paint());
            //更新label背景，处理点击；
            pointValue.labelRect.set(labelBackgroundRect.left, labelBackgroundRect.top, labelBackgroundRect.right + bitmap
                    .getWidth(), labelBackgroundRect.bottom);
        }
    }

    /**
     * 画区域
     *
     * @param canvas
     * @param line
     */
    private void drawArea(Canvas canvas, Line line) {
        final int lineSize = line.getValues().size();
        if (lineSize < 2) {
            //No point to draw area for one point or empty line.
            return;
        }

        final Rect contentRect = computator.getContentRectMinusAllMargins();
        final float baseRawValue = Math.min(contentRect.bottom, Math.max(computator.computeRawY(baseValue),
                contentRect.top));
        //That checks works only if the last point is the right most one.
        PointValue pointValueStart = line.getValues()
                                         .get(0);
        final float left = Math.max(computator.computeRawX(pointValueStart
                .getX()), contentRect.left);
        PointValue pointValueEnd = line.getValues()
                                       .get(lineSize - 1);
        final float right = Math.min(computator.computeRawX(pointValueEnd
                        .getX()),
                contentRect.right);
        final float rawXStart = computator.computeRawX(pointValueStart.getX());
        final float rawYStart = computator.computeRawY(pointValueStart.getY());
        final float rawXEnd = computator.computeRawX(pointValueEnd.getX());
        final float rawYEnd = computator.computeRawY(pointValueEnd.getY());

        path.lineTo(right, baseRawValue);
        path.lineTo(left, baseRawValue);
        path.close();

//        linePaint.setStyle(Paint.Style.FILL);
//        linePaint.setAlpha(line.getAreaTransparency());
//        canvas.drawPath(path, linePaint);
//        linePaint.setStyle(Paint.Style.STROKE);
        //add by zxb 画渐变颜色； 不准；
        Paint paint = new Paint();
//        line.getAreaTransparency()
        paint.setAlpha(200);
        int color = line.getColor();
        LinearGradient linearGradient = new LinearGradient(left, minY, left, baseRawValue, new int[]{
                color, Color.WHITE}, null,
                Shader.TileMode.REPEAT);
        //设置渲染器  
        paint.setShader(linearGradient);
        canvas.drawPath(path, paint);
        //使用画图方法

        //图表背景绘制
//        drawFilledPath(canvas, path, drawableFill, (int) right, (int) baseRawValue);

    }

    /**
     * Draws the provided path in filled mode with the provided drawable.
     *
     * @param c
     * @param filledPath
     * @param drawable
     */
    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable, int right, int bottom) {

        if (clipPathSupported()) {

            int save = c.save();
            c.clipPath(filledPath);
//            c.drawColor(Color.rgb(255,42,34));

            drawable.setBounds(0, 0, right, bottom);

//            drawable.setBounds((int) mViewPortHandler.contentLeft(),
//                    (int) mViewPortHandler.contentTop(),
//                    (int) mViewPortHandler.contentRight(),
//                    (int) mViewPortHandler.contentBottom());
            drawable.draw(c);

            c.restoreToCount(save);
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, ");
//                    "this code was run on API level " + Utils.getSDKInt() + ".");
        }
    }

    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     *
     * @return
     */
    private boolean clipPathSupported() {
        return true;
//        return Utils.getSDKInt() >= 18;
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }

    public boolean isViewPortYCalcEnabled() {
        return isViewPortYCalcEnabled;
    }

    public void setViewPortYCalcEnabled(boolean isEnabled) {
        isViewPortYCalcEnabled = isEnabled;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getViewPortRight() {
        return viewPortRight;
    }

    public void setViewPortRight(float viewPortRight) {
        this.viewPortRight = viewPortRight;
    }

    public void setVisibleViewPortRight(float visibleViewPortRight) {
        this.visibleViewPortRight = visibleViewPortRight;
    }

}
