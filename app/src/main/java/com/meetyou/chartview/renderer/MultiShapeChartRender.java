package com.meetyou.chartview.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.meetyou.chartview.model.MultiShape;
import com.meetyou.chartview.model.MultiShapeChartData;
import com.meetyou.chartview.model.SelectedValue;
import com.meetyou.chartview.model.ShapeValue;
import com.meetyou.chartview.model.SingleShape;
import com.meetyou.chartview.model.ValueShape;
import com.meetyou.chartview.model.Viewport;
import com.meetyou.chartview.provider.MultiShapeChartDataProvider;
import com.meetyou.chartview.util.ChartUtils;
import com.meetyou.chartview.view.Chart;

import java.util.List;


/**
 * Created by ckq on 5/12/16.
 */
public class MultiShapeChartRender extends AbstractChartRenderer {
    public static final String TAG = "MultiShapeChartRender";

    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;

    private Paint circlePointPaint = new Paint();
    private Paint squarePointPaint = new Paint();
    private Paint trianglePointPaint = new Paint();
    private Paint separateLinePaint = new Paint();

    private MultiShapeChartDataProvider dataProvider;
    private int checkPrecision;
    private int touchToleranceMargin;
    private float baseValue;

    private Bitmap softwareBitmap;
    private Canvas softwareCanvas = new Canvas();
    private Viewport tempMaximumViewport = new Viewport();


    public MultiShapeChartRender(Context context, Chart chart, MultiShapeChartDataProvider dataProvider) {
        super(context, chart);
        this.dataProvider = dataProvider;

        touchToleranceMargin = ChartUtils.dp2px(density, DEFAULT_TOUCH_TOLERANCE_MARGIN_DP);
        checkPrecision = ChartUtils.dp2px(density, 2);
    }

    @Override
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
        // baseValue = dataProvider.getMultiShapeChartData().getBaseValue();
        baseValue = 0;
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
        final MultiShapeChartData data = dataProvider.getMultiShapeChartData();

        final Canvas drawCanvas;

        // softwareBitmap can be null if chart is rendered in layout editor. In that case use default canvas and not softwareCanvas.
        if (null != softwareBitmap) {
            drawCanvas = softwareCanvas;
            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        } else {
            drawCanvas = canvas;
        }

        if (null != softwareBitmap) {
            canvas.drawBitmap(softwareBitmap, 0, 0, null);
        }

        if (isTouched()) {
            highlightFullColumn(canvas);
        }
    }

    @Override
    public void drawUnclipped(Canvas canvas) {
        final MultiShapeChartData data = dataProvider.getMultiShapeChartData();
        int shapeIndex = 0;
        for (MultiShape multiShape : data.getMultiShapes()) {
            // if (checkIfShouldDrawMultiShape(multiShape)) {
            drawMultiShape(canvas, multiShape, shapeIndex, MODE_DRAW);
            drawSeparateLine(canvas);
            // }
            ++shapeIndex;
        }
        /*if (isTouched()) {
            // Redraw touched point to bring it to the front
            highlightPoints(canvas);
        }*/
    }

    @Override
    public boolean checkTouch(float touchX, float touchY) {
        selectedValue.clear();
        int shapeIndex = 0;

        final MultiShapeChartData data = dataProvider.getMultiShapeChartData();

        for (MultiShape multiShape : data.getMultiShapes()) {
            if (checkIfShouldDrawMultiShape(multiShape)) {
                int valueIndex = 0;
                /* 本来此处是高亮每个点的,需求只需要高亮整栏
                int pointRadius = ChartUtils.dp2px(density, multiShape.getShapes().get(0).getPointRadius());
                for (ShapeValue shapeValue : multiShape.getValues()) {
                    final float rawValueX = computator.computeRawX(shapeValue.getX());
                    final float rawValueY = computator.computeRawY(shapeValue.getY());
                    if (isInArea(rawValueX, rawValueY, touchX, touchY, pointRadius + touchToleranceMargin)) {
                        selectedValue.set(shapeIndex, valueIndex, SelectedValue.SelectedValueType.MULTI_SHAPE);
                    }
                    ++valueIndex;
                }*/
                for (ShapeValue shapeValue : multiShape.getValues()) {
                    if (isInColumnTouchArea((int) shapeValue.getX(), touchX, touchY)) {
                        selectedValue.set(shapeIndex, (int) shapeValue.getX(), SelectedValue.SelectedValueType.MULTI_SHAPE);
                    }
                    ++valueIndex;
                }
            }
            ++shapeIndex;
        }

        return isTouched();
    }

    private void drawMultiShape(Canvas canvas, MultiShape multiShape, int shapeIndex, int mode) {
        circlePointPaint.setColor(multiShape.getCirclePointColor());
        squarePointPaint.setColor(multiShape.getSquarePointColor());
        trianglePointPaint.setColor(multiShape.getTrianglePointColor());

        int valueIndex = 0;

        for (int i = 0; i < multiShape.getShapes().size(); i++) {
            ShapeValue shapeValue = multiShape.getValues().get(i);
            SingleShape singleShape = multiShape.getShapes().get(i);
            int pointRadius = ChartUtils.dp2px(density, singleShape.getPointRadius());
            final float rawX = computator.computeRawX(shapeValue.getX());
            final float rawY = computator.computeRawY(shapeValue.getY());
            if (computator.isWithinContentRect(rawX, rawY, checkPrecision)) {
                if (MODE_DRAW == mode) {
                    drawSingleShape(canvas, singleShape, shapeValue, rawX, rawY, pointRadius);
                    /*if (singleShape.hasLabels()) {
                        drawLabel(canvas, singleShape, shapeValue, rawX, rawY, pointRadius + labelOffset);
                    }*/
                    // drawLabel(canvas, singleShape, shapeValue, rawX, rawY, pointRadius + labelOffset);
                } else if (MODE_HIGHLIGHT == mode) {
                    highlightPoint(canvas, singleShape, shapeValue, rawX, rawY, shapeIndex, valueIndex);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + mode);
                }
            }
            ++valueIndex;
        }
    }

    private void highlightPoints(Canvas canvas) {
        int shapeIndex = selectedValue.getFirstIndex();
        MultiShape multiShape = dataProvider.getMultiShapeChartData().getMultiShapes().get(shapeIndex);
        drawMultiShape(canvas, multiShape, shapeIndex, MODE_HIGHLIGHT);
    }

    private void highlightPoint(Canvas canvas, SingleShape singleShape, ShapeValue shapeValue, float rawX, float rawY, int shapeIndex, int valueIndex) {
        if (!isEnableHighLightPoint)
            return;

        if (selectedValue.getFirstIndex() == shapeIndex && selectedValue.getSecondIndex() == valueIndex) {
            int pointRadius = ChartUtils.dp2px(density, singleShape.getPointRadius());
            // pointPaint.setColor(singleShape.getDarkenColor());
            drawSingleShape(canvas, singleShape, shapeValue, rawX, rawY, pointRadius + touchToleranceMargin);
            /*if (singleShape.hasLabels() || singleShape.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, singleShape, shapeValue, rawX, rawY, pointRadius + labelOffset);
            }*/
            drawLabel(canvas, singleShape, shapeValue, rawX, rawY, pointRadius + labelOffset);
        }
    }

    private void drawSingleShape(Canvas canvas, SingleShape singleShape, ShapeValue shapeValue, float rawX, float rawY, float pointRadius) {
        Drawable d;
        ValueShape shape = singleShape.getShape();
        if (ValueShape.QINWEI.equals(shape)) {
            d = shapeMumDrawable;
        } else if (ValueShape.PINGWEI.equals(shape)) {
            d = shapeBottleDrawable;
        } else if (ValueShape.MIX.equals(shape)){
            d = shapeAllDrawable;
        } else {
            d = shapeMumDrawable;
            Log.e(TAG, "Wrong shape type!");
        }
        d.setBounds((int ) (rawX - pointRadius * 2.0f), (int ) (rawY - pointRadius * 2.0f), (int) (rawX + pointRadius * 2.0f), (int) (rawY + pointRadius * 2.0f));
        d.draw(canvas);
    }

    private int calculateContentRectInternalMargin() {
        int contentAreaMargin = 0;
        final MultiShapeChartData data = dataProvider.getMultiShapeChartData();

        int margin;
        if (data.getMultiShapes().size() > 0) {
            MultiShape firstMultiShape = data.getMultiShapes().get(0);
            if (firstMultiShape.getShapes().size() > 0) {
                SingleShape firstSingleShape = firstMultiShape.getShapes().get(0);
                margin = firstSingleShape.getPointRadius() + DEFAULT_TOUCH_TOLERANCE_MARGIN_DP;

                if (margin > contentAreaMargin) {
                    contentAreaMargin = margin;
                }

                MultiShape lastMultiShape = data.getMultiShapes().get(data.getMultiShapes().size() - 1);
                SingleShape lastSingleShape = lastMultiShape.getShapes().get(0);
                margin = lastSingleShape.getPointRadius() + DEFAULT_TOUCH_TOLERANCE_MARGIN_DP;

                if (margin > contentAreaMargin) {
                    contentAreaMargin = margin;
                }
            }
        }

        return ChartUtils.dp2px(density, contentAreaMargin);
    }

    public void calculateMaxViewport() {
        tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        MultiShapeChartData data = dataProvider.getMultiShapeChartData();

        for (MultiShape multiShape: data.getMultiShapes()) {
            for (ShapeValue shapeValue: multiShape.getValues()) {
                if (shapeValue.getX() < tempMaximumViewport.left) {
                    tempMaximumViewport.left = shapeValue.getX() - 0.5f;
                }
                if (shapeValue.getX() > tempMaximumViewport.right) {
                    tempMaximumViewport.right = shapeValue.getX() + 0.5f;
                }
                if (shapeValue.getY() < tempMaximumViewport.bottom) {
                    tempMaximumViewport.bottom = shapeValue.getY();
                }
                if (shapeValue.getY() > tempMaximumViewport.top) {
                    tempMaximumViewport.top = shapeValue.getY();
                    tempMaximumViewport.top = shapeValue.getY();
                }
            }
        }

        /*if (data.getMultiShapes().size() > 0) {
            MultiShape firstMultiShape = data.getMultiShapes().get(0);
            ShapeValue firstShapeValue = firstMultiShape.getValues().get(0);
            if (firstShapeValue.getX() < tempMaximumViewport.left) {
                tempMaximumViewport.left = firstShapeValue.getX() - 0.5f;
            }
            if (firstShapeValue.getY() < tempMaximumViewport.bottom) {
                tempMaximumViewport.bottom = firstShapeValue.getY();
            }
            if (firstShapeValue.getY() > tempMaximumViewport.top) {
                tempMaximumViewport.top = firstShapeValue.getY();
            }

            MultiShape lastMultiShape = data.getMultiShapes().get(data.getMultiShapes().size() - 1);
            ShapeValue lastShapeValue = lastMultiShape.getValues().get(lastMultiShape.getShapes().size() - 1);
            if (lastShapeValue.getX() > tempMaximumViewport.right) {
                tempMaximumViewport.right = lastShapeValue.getX() + 0.5f;
            }
            if (lastShapeValue.getY() < tempMaximumViewport.bottom) {
                tempMaximumViewport.bottom = lastShapeValue.getY();
            }
            if (lastShapeValue.getY() > tempMaximumViewport.top) {
                tempMaximumViewport.top = lastShapeValue.getY();
            }
        }*/
    }

    private boolean checkIfShouldDrawMultiShape(MultiShape multiShape) {
        return multiShape.getShapes().size() > 0;
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }

    // 是否在全栏高亮的栏内
    private boolean isInColumnTouchArea(int shapeIndex, float touchX, float touchY) {
        float columnWidth = computator.getContentRectMinusAllMargins().width() / computator.getVisibleViewport().width();
        float top = computator.getContentRectMinusAllMargins().top;
        float bottom = computator.getContentRectMinusAllMargins().bottom;
        float left = computator.getCurrentViewport().left;
        return touchY > top
                && touchY < bottom
                && touchX > (shapeIndex - left)* columnWidth
                && touchX < (shapeIndex + 1 - left) * columnWidth;
    }

    private void drawLabel(Canvas canvas, SingleShape singleShape, ShapeValue shapeValue, float rawX, float rawY, float offset) {
        final Rect contentRect = computator.getContentRectMinusAllMargins();
        final int numChars = singleShape.getFormatter().formatChartValue(labelBuffer, shapeValue);
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

        if (shapeValue.getY() >= baseValue) {
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
        drawLabelTextAndBackground(canvas, labelBuffer, labelBuffer.length - numChars, numChars, singleShape.getDarkenColor());
    }

    private Drawable shapeAllDrawable, shapeBottleDrawable, shapeMumDrawable;

    public void setShapeAllDrawable(Drawable d) {
        this.shapeAllDrawable = d;
    }

    public void setShapeBottleDrawable(Drawable d) {
        this.shapeBottleDrawable = d;
    }

    public void setShapeMumDrawable(Drawable d) {
        this.shapeMumDrawable = d;
    }

    private boolean isEnableHighLightPoint;

    public void setEnableHighLightPoint(boolean isEnableHighLightPoint) {
        this.isEnableHighLightPoint = isEnableHighLightPoint;
    }

    private void highlightFullColumn(Canvas canvas) {
        if (selectedValue.isSet()) {
            /** 不管是stacked还是subColumn,所有子column加起来值为0则不需要全栏高亮效果
             *  firstIndex: 该栏Index secondIndex: 该点x坐标值
             **/
            float value = 0.0f;
            List<SingleShape> list = dataProvider.getMultiShapeChartData().getMultiShapes().get(0).getShapes();
            for (SingleShape singleShape : list) {
                if (singleShape.getValue().getX() == selectedValue.getSecondIndex()) {
                    value += singleShape.getValue().getY();
                }
            }
            if (value == 0.0f)
                return;
        }
        final float rawX = computator.computeRawX(selectedValue.getSecondIndex());
        float columnWidth = computator.getContentRectMinusAllMargins().width() / computator.getVisibleViewport().width();
        pressColumnPaint.setColor(highlightColor);
        canvas.drawRect(rawX - columnWidth / 2, computator.getContentRectMinusAxesMargins().top, rawX + columnWidth / 2,
                computator.getContentRectMinusAllMargins().bottom, pressColumnPaint);
    }

    private int separateLineColor;

    public void setSeparateLineColor(int color) {
        separateLineColor = color;
    }

    private int highlightColor;

    public void setHighlightColor(int color) {
        highlightColor = color;
    }

    /**
     * 绘制分隔线
     */
    private void drawSeparateLine(Canvas canvas) {
        try {
            List<SingleShape> list = dataProvider.getMultiShapeChartData().getMultiShapes().get(0).getShapes();
            if (list.size() == 0)
                return;
            // 绘制起点
            final float originRawX = computator.computeRawX(dataProvider.getMultiShapeChartData().getMultiShapes().get(0).getShapes().get(0).getValue().getX());
            float columnWidth = computator.getContentRectMinusAllMargins().width() / computator.getVisibleViewport().width();
            separateLinePaint.setStyle(Paint.Style.STROKE);
            separateLinePaint.setAntiAlias(true);
            separateLinePaint.setColor(separateLineColor != 0 ? separateLineColor : ChartUtils.COLOR_BLACK_E);
            for (int i = 0; i <= list.size(); i++) {
                float rawX = originRawX + i * columnWidth;
                canvas.drawLine(rawX - columnWidth / 2,
                        computator.getContentRectMinusAllMargins().bottom,
                        rawX - columnWidth / 2,
                        computator.getContentRectMinusAxesMargins().top, separateLinePaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}