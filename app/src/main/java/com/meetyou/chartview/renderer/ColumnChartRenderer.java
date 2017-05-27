package com.meetyou.chartview.renderer;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.meetyou.chartview.model.Column;
import com.meetyou.chartview.model.ColumnChartData;
import com.meetyou.chartview.model.SelectedValue.SelectedValueType;
import com.meetyou.chartview.model.SubcolumnValue;
import com.meetyou.chartview.model.Viewport;
import com.meetyou.chartview.provider.ColumnChartDataProvider;
import com.meetyou.chartview.util.ChartUtils;
import com.meetyou.chartview.view.Chart;

import java.util.List;

/**
 * Magic renderer for ColumnChart.
 */
public class ColumnChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_SUBCOLUMN_SPACING_DP = 0;
    private static final int DEFAULT_COLUMN_TOUCH_ADDITIONAL_WIDTH_DP = 4;
    private static final int DEFAULT_ROUNDED_RECT_RADIUS = 4;
    private static final int MIN_VIEWPORT_TOP = 4;

    private static final int MODE_DRAW = 0;
    private static final int MODE_CHECK_TOUCH = 1;
    private static final int MODE_HIGHLIGHT = 2;

    private float scale;
    private float viewPortRight; // 图表最右viewPort值
    private boolean isViewPortYCalcEnabled;
    private boolean isFullColumnTouched; // 底部整栏点击效果
    private boolean isCustomAxes; // 自定义坐标轴间距
    private int complemetHeight;

    private ColumnChartDataProvider dataProvider;
    /**
     * 是否显示 辅助线
     */
    public boolean enableGuideLine = false;
    /**
     * 辅助线 的值
     */
    public float guideLineValue = 0;


    /**
     * Additional width for hightlighted column, used to give tauch feedback.
     */
    private int touchAdditionalWidth;

    /**
     * Spacing between sub-columns.
     */
    private int subcolumnSpacing;

    /**
     * Paint used to draw every column.
     */
    private Paint columnPaint = new Paint();

    /**
     * Holds coordinates for currently processed column/sub-column.
     */
    private RectF drawRect = new RectF();

    /**
     * 点击范围
     */
    private RectF touchRect = new RectF();

    /**
     * Coordinated of user tauch.
     */
    private PointF touchedPoint = new PointF();

    private float fillRatio;

    private float baseValue;

    protected Viewport tempMaximumViewport = new Viewport();

    // 当前选中的点的index 当前为columnIndex,如果有subColumn或者stack数据再判断valueIndex
    private int touchIndex = -1;

    public ColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider dataProvider) {
        super(context, chart);
        this.dataProvider = dataProvider;
        subcolumnSpacing = ChartUtils.dp2px(density, DEFAULT_SUBCOLUMN_SPACING_DP);
        touchAdditionalWidth = ChartUtils.dp2px(density, DEFAULT_COLUMN_TOUCH_ADDITIONAL_WIDTH_DP);

        columnPaint.setAntiAlias(true);
        columnPaint.setStyle(Paint.Style.FILL);
        columnPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override
    public void onChartSizeChanged() {
    }

    @Override
    public void onChartDataChanged() {
        super.onChartDataChanged();
        ColumnChartData data = dataProvider.getColumnChartData();
        fillRatio = data.getFillRatio();
        baseValue = data.getBaseValue();

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

    public void draw(Canvas canvas) {
        final ColumnChartData data = dataProvider.getColumnChartData();
        //显示辅助线
        if (enableGuideLine) {
            float rawY = computator.computeRawY(guideLineValue);
            Rect contentRectMinusAllMargins = computator.getContentRectMinusAllMargins();
            int right = contentRectMinusAllMargins.right;
            Path path = new Path();
            path.moveTo(0, rawY);
            path.lineTo(right, rawY);
            Paint paint = new Paint();
            paint.setPathEffect(new DashPathEffect(new float[]{15, 15}, 0));
            paint.setColor(Color.parseColor("#ff74B9"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            canvas.drawPath(path, paint);
        }

        if (data.isStacked()) {
            if (isTouched()) {
                if (isFullColumnTouched) {
                    highlightFullColumn(canvas);
                } else {
                    highlightColumnForStacked(canvas);
                }
            }
            drawColumnForStacked(canvas);
        } else {
            drawColumnsForSubcolumns(canvas);
            if (isFullColumnTouched) {
                highlightFullColumn(canvas);
            } else {
                if (isTouched()) {
                    highlightColumnsForSubcolumns(canvas);
                }
            }
        }
    }

    @Override
    public void drawUnclipped(Canvas canvas) {
        // Do nothing, for this kind of chart there is nothing to draw beyond clipped area
    }

    public boolean checkTouch(float touchX, float touchY) {
        selectedValue.clear();
        touchIndex = -1;
        final ColumnChartData data = dataProvider.getColumnChartData();
        if (data.isStacked()) {
            checkTouchForStacked(touchX, touchY);
        } else {
            checkTouchForSubcolumns(touchX, touchY);
        }
        return isTouched();
    }

    public void calculateMaxViewport() {
        final ColumnChartData data = dataProvider.getColumnChartData();
        // Column chart always has X values from 0 to numColumns-1, to add some margin on the left and right I added
        // extra 0.5 to the each side, that margins will be negative scaled according to number of columns, so for more
        // columns there will be less margin.
        tempMaximumViewport.set(-0.5f, baseValue, data.getColumns().size() - 0.5f, baseValue);
        if (data.isStacked()) {
            calculateMaxViewportForStacked(data);
        } else {
            calculateMaxViewportForSubcolumns(data);
        }
    }

    protected void calculateMaxViewportForSubcolumns(ColumnChartData data) {
        for (Column column : data.getColumns()) {
            for (SubcolumnValue columnValue : column.getValues()) {
                if (columnValue.getValue() >= baseValue && columnValue.getValue() > tempMaximumViewport.top) {
                    tempMaximumViewport.top = columnValue.getValue();
                }
                if (columnValue.getValue() < baseValue && columnValue.getValue() < tempMaximumViewport.bottom) {
                    tempMaximumViewport.bottom = columnValue.getValue();
                }
            }
        }
    }

    private void calculateMaxViewportForStacked(ColumnChartData data) {
        if (isViewPortYCalcEnabled()) {
            for (int i = 0; i < data.getColumns().size(); i++) {
                float sumPositive = baseValue;
                float sumNegative = baseValue;
                // 不包括超出屏幕外的点
                if ((i + data.getFillRatio() / 2) < computator.getVisibleViewport().left || (i - data
                        .getFillRatio() / 2) > computator.getVisibleViewport().right) {
                    continue;
                }

                for (SubcolumnValue columnValue : data.getColumns().get(i).getValues()) {
                    if (columnValue.getValue() >= baseValue) {
                        sumPositive += columnValue.getValue();
                    } else {
                        sumNegative += columnValue.getValue();
                    }
                }
                if (sumPositive > tempMaximumViewport.top) {
                    if (sumPositive < MIN_VIEWPORT_TOP) {
                        tempMaximumViewport.top = MIN_VIEWPORT_TOP;
                    } else {
                        tempMaximumViewport.top = sumPositive;
                    }
                }
                if (sumNegative < tempMaximumViewport.bottom) {
                    tempMaximumViewport.bottom = sumNegative;
                }
            }
        } else {
            for (Column column : data.getColumns()) {
                float sumPositive = baseValue;
                float sumNegative = baseValue;
                for (SubcolumnValue columnValue : column.getValues()) {
                    if (columnValue.getValue() >= baseValue) {
                        sumPositive += columnValue.getValue();
                    } else {
                        sumNegative += columnValue.getValue();
                    }
                }
                if (sumPositive > tempMaximumViewport.top) {
                    tempMaximumViewport.top = sumPositive;
                }
                if (sumNegative < tempMaximumViewport.bottom) {
                    tempMaximumViewport.bottom = sumNegative;
                }
            }
        }
    }

    private void drawColumnsForSubcolumns(Canvas canvas) {
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForSubcolumns(canvas, column, columnWidth, columnIndex, MODE_DRAW);
            ++columnIndex;
        }
    }

    private void highlightColumnsForSubcolumns(Canvas canvas) {
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        Column column = data.getColumns().get(selectedValue.getFirstIndex());
        processColumnForSubcolumns(canvas, column, columnWidth, selectedValue.getFirstIndex(), MODE_HIGHLIGHT);
    }

    private void checkTouchForSubcolumns(float touchX, float touchY) {
        // Using member variable to hold touch point to avoid too much parameters in methods.
        float left = computator.getContentRectMinusAllMargins().left;
        float right = computator.getContentRectMinusAllMargins().right;

        touchedPoint.x = touchX;

        // 坐标轴外的点不触发点击效果
        if (touchX < left || touchX > right)
            return;

        touchedPoint.y = touchY;
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            // canvas is not needed for checking touch
            processColumnForSubcolumns(null, column, columnWidth, columnIndex, MODE_CHECK_TOUCH);
            ++columnIndex;
        }
    }

    private void processColumnForSubcolumns(Canvas canvas, Column column, float columnWidth, int columnIndex,
                                            int mode) {
        // For n subcolumns there will be n-1 spacing and there will be one
        // subcolumn for every columnValue
        float subcolumnWidth = (columnWidth - (subcolumnSpacing * (column.getValues().size() - 1)))
                / column.getValues().size();
        if (subcolumnWidth < 1) {
            subcolumnWidth = 1;
        }
        // Columns are indexes from 0 to n, column index is also column X value
        final float rawX = computator.computeRawX(columnIndex);
        final float halfColumnWidth = columnWidth / 2;
        final float baseRawY = computator.computeRawY(baseValue);
        // First subcolumn will starts at the left edge of current column,
        // rawValueX is horizontal center of that column
        float subcolumnRawX = rawX - halfColumnWidth;
        int valueIndex = 0;
        for (SubcolumnValue columnValue : column.getValues()) {
            if (subcolumnRawX > rawX + halfColumnWidth) {
                break;
            }
            final float rawY = computator.computeRawY(columnValue.getValue());
            calculateRectToDraw(columnValue, subcolumnRawX, subcolumnRawX + subcolumnWidth, baseRawY, rawY);
            switch (mode) {
                case MODE_DRAW:
                    drawSubcolumn(canvas, column, columnIndex, columnValue, false);
                    break;
                case MODE_HIGHLIGHT:
                    highlightSubcolumn(canvas, column, columnValue, valueIndex, false);
                    break;
                case MODE_CHECK_TOUCH:
                    checkRectToDraw(columnIndex, valueIndex);
                    break;
                default:
                    // There no else, every case should be handled or exception will
                    // be thrown
                    throw new IllegalStateException("Cannot process column in mode: " + mode);
            }
            subcolumnRawX += subcolumnWidth + subcolumnSpacing;
            ++valueIndex;
        }
    }

    private void drawColumnForStacked(Canvas canvas) {
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        // Columns are indexes from 0 to n, column index is also column X value
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForStacked(canvas, column, columnWidth, columnIndex, MODE_DRAW);
            ++columnIndex;
        }
    }

    private void highlightColumnForStacked(Canvas canvas) {
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        // Columns are indexes from 0 to n, column index is also column X value
        Column column = data.getColumns().get(selectedValue.getFirstIndex());
        processColumnForStacked(canvas, column, columnWidth, selectedValue.getFirstIndex(), MODE_HIGHLIGHT);
    }

    private void checkTouchForStacked(float touchX, float touchY) {
        touchedPoint.x = touchX;
        touchedPoint.y = touchY;
        final ColumnChartData data = dataProvider.getColumnChartData();
        final float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            // canvas is not needed for checking touch
            processColumnForStacked(null, column, columnWidth, columnIndex, MODE_CHECK_TOUCH);
            ++columnIndex;
        }
    }

    private void processColumnForStacked(Canvas canvas, Column column, float columnWidth, int columnIndex, int mode) {
        try {
            final float rawX = computator.computeRawX(columnIndex);
            final float halfColumnWidth = columnWidth / 2;
            float mostPositiveValue = baseValue;
            float mostNegativeValue = baseValue;
            float subcolumnBaseValue = baseValue;
            int valueIndex = 0;
            for (SubcolumnValue columnValue : column.getValues()) {
                columnPaint.setColor(columnValue.getColor());
                if (columnValue.getValue() >= baseValue) {
                    // Using values instead of raw pixels make code easier to
                    // understand(for me)
                    subcolumnBaseValue = mostPositiveValue;
                    mostPositiveValue += columnValue.getValue();
                } else {
                    subcolumnBaseValue = mostNegativeValue;
                    mostNegativeValue += columnValue.getValue();
                }
                final float rawBaseY, rawY;
                if (isCustomAxes) {
                    rawBaseY = computator.computeAxisRawY(subcolumnBaseValue, computator.getComplemenHeight());
                    rawY = computator.computeAxisRawY(subcolumnBaseValue + columnValue.getValue(), computator
                            .getComplemenHeight());
                } else {
                    rawBaseY = computator.computeRawY(subcolumnBaseValue);
                    rawY = computator.computeRawY(subcolumnBaseValue + columnValue.getValue());
                }

                calculateRectToDraw(columnValue, rawX - halfColumnWidth, rawX + halfColumnWidth, rawBaseY, rawY);
                switch (mode) {
                    case MODE_DRAW:
                        drawSubcolumn(canvas, column, columnIndex, columnValue, true);
                        break;
                    case MODE_HIGHLIGHT:
                        highlightSubcolumn(canvas, column, columnValue, valueIndex, true);
                        break;
                    case MODE_CHECK_TOUCH:
                        checkRectToDraw(columnIndex, valueIndex);
                        break;
                    default:
                        // There no else, every case should be handled or exception will
                        // be thrown
                        throw new IllegalStateException("Cannot process column in mode: " + mode);
                }
                ++valueIndex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void drawSubcolumn(Canvas canvas, Column column, int columnIndex, SubcolumnValue columnValue, boolean isStacked) {
        if (columnValue.getShaderStartColor() != 0) {
            columnPaint.setShader(new LinearGradient(0, drawRect.top, 0, drawRect.bottom, columnValue
                    .getShaderStartColor(), columnValue.getShaderEndColor(), Shader.TileMode.MIRROR));
        } else {
            columnPaint.setColor(columnValue.getColor());
        }
        if (columnValue.isRoundedRect()) {
            Path path = ChartUtils.buildRoundedRectPath(drawRect, density * DEFAULT_ROUNDED_RECT_RADIUS, density * DEFAULT_ROUNDED_RECT_RADIUS, true, true, false, false);
            canvas.drawPath(path, columnPaint);
        } else {
            canvas.drawRect(drawRect, columnPaint);
        }
        if (column.hasLabels()) {
            clearAppendedTextWhenNotHighLight(column);
            if (touchIndex != columnIndex && columnValue.getValue() != 0) { // 如果是点击的点不重复绘制,值为0也不绘制
                drawLabel(canvas, column, columnValue, isStacked, labelOffset);
            }
        }

        //新增显示 Value在柱状图底部；
        if (column.isShowValues()) {
            Paint valuePaint = new Paint();
            valuePaint.setColor(Color.parseColor("#323232"));
            valuePaint.setAntiAlias(true);
            valuePaint.setStyle(Paint.Style.FILL);
            valuePaint.setTextAlign(Paint.Align.LEFT);
//            valuePaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            valuePaint.setTextSize(12 * density);
            float columnMiddle = (drawRect.left + drawRect.right) / 2;
            //显示底部Value
            if (columnValue.enableLabeBottom) {
                String valueStr = columnValue.getLabelBottom();
                final float labelWidth = valuePaint.measureText(valueStr);
                float rawX = columnMiddle - labelWidth / 2;
                float rawY = drawRect.bottom - 16 * density;
                canvas.drawText(valueStr, rawX, rawY, valuePaint);    
            }
        }
    }

    private void highlightSubcolumn(Canvas canvas, Column column, SubcolumnValue columnValue, int valueIndex,
                                    boolean isStacked) {
        float columnWidth = computator.getContentRectMinusAllMargins().width() / computator
                .getVisibleViewport().width();
        float columnMiddle = (drawRect.left + drawRect.right) / 2;
        if (selectedValue.getSecondIndex() == valueIndex) {
            if (columnValue.getDarkenShaderStartColor() != 0) {
                pressColumnPaint.setShader(new LinearGradient(0, drawRect.top, 0, drawRect.bottom, columnValue
                        .getDarkenShaderStartColor(), columnValue.getDarkenShaderEndColor(), Shader.TileMode.MIRROR));
            } else {
                pressColumnPaint.setColor(columnValue.getDarkenColor());
            }
            /*canvas.drawRect(drawRect.left - touchAdditionalWidth, drawRect.top, drawRect.right + touchAdditionalWidth,
                    drawRect.bottom, pressColumnPaint);*/
            if (columnValue.isRoundedRect()) {
                Path path = ChartUtils.buildRoundedRectPath(columnMiddle - columnWidth / 4, drawRect.top, columnMiddle + columnWidth / 4, drawRect.bottom, density * DEFAULT_ROUNDED_RECT_RADIUS, density * DEFAULT_ROUNDED_RECT_RADIUS, true, true, false, false);
                canvas.drawPath(path, pressColumnPaint);
            } else {
                canvas.drawRect(columnMiddle - columnWidth / 4, drawRect.top, columnMiddle + columnWidth / 4, drawRect.bottom, pressColumnPaint);
            }
            if (column.hasLabels() || column.hasLabelsOnlyForSelected()) {
                setAppendedTextWhenHighLight(column);
                drawLabel(canvas, column, columnValue, isStacked, labelOffset);
            }
        }
    }

    /**
     * 高亮的时候加上append
     */
    protected void setAppendedTextWhenHighLight(Column column) {
    }

    /**
     * 当非高亮的时候清空append
     */
    protected void clearAppendedTextWhenNotHighLight(Column column) {
        column.getFormatter().setAppendedText("".toCharArray());
    }

    private void highlightFullColumn(Canvas canvas) {
        if (selectedValue.isSet()) {
            /** 不管是stacked还是subColumn,所有子column加起来值为0则不需要全栏高亮效果 **/
            float value = 0.0f;
            List<SubcolumnValue> list = dataProvider.getColumnChartData()
                                                    .getColumns()
                                                    .get(selectedValue.getFirstIndex())
                                                    .getValues();
            for (SubcolumnValue subcolumnValue : list) {
                value += subcolumnValue.getValue();
            }
            if (value == 0.0f)
                return;
        }
        final float rawX = computator.computeRawX(selectedValue.getFirstIndex());
        float columnWidth = computator.getContentRectMinusAllMargins()
                                      .width() / computator.getVisibleViewport().width();
        pressColumnPaint.setColor(ChartUtils.COLOR_COLUMN_DARKEN);
        canvas.drawRect(rawX - columnWidth / 2, computator.getContentRectMinusAllMargins().top, rawX + columnWidth / 2,
                computator.getContentRectMinusAllMargins().bottom, pressColumnPaint);
    }

    private void checkRectToDraw(int columnIndex, int valueIndex) {
        if (drawRect.contains(touchedPoint.x, touchedPoint.y)) {
            selectedValue.set(columnIndex, valueIndex, SelectedValueType.COLUMN);
            touchIndex = columnIndex;
        }
        if (isFullColumnTouched) {
            if (touchRect.contains(touchedPoint.x, touchedPoint.y)) {
                selectedValue.set(columnIndex, valueIndex, SelectedValueType.COLUMN);
                touchIndex = columnIndex;
            }
        }
    }

    private float calculateColumnWidth() {
        // columnWidht should be at least 2 px
        float columnWidth = fillRatio * computator.getContentRectMinusAllMargins()
                                                  .width() / computator
                .getVisibleViewport().width();
        if (columnWidth < 2) {
            columnWidth = 2;
        }
        return columnWidth;
    }

    private void calculateRectToDraw(SubcolumnValue columnValue, float left, float right, float rawBaseY, float rawY) {
        // Calculate rect that will be drawn as column, subcolumn or label background.
        drawRect.left = left;
        drawRect.right = right;
        if (columnValue.getValue() >= baseValue) {
            drawRect.top = rawY;
            drawRect.bottom = rawBaseY - subcolumnSpacing;
        } else {
            drawRect.bottom = rawY;
            drawRect.top = rawBaseY + subcolumnSpacing;
        }
        if (isFullColumnTouched) {
            touchRect.left = left;
            touchRect.right = right;
            touchRect.top = computator.getContentRectMinusAllMargins().top;
            touchRect.bottom = computator.getContentRectMinusAllMargins().bottom;
        }
    }

    private void drawLabel(Canvas canvas, Column column, SubcolumnValue columnValue, boolean isStacked, float offset) {
        final int numChars = column.getFormatter().formatChartValue(labelBuffer, columnValue);

        if (numChars == 0) {
            // No need to draw empty label
            return;
        }

        final float labelWidth = labelPaint.measureText(labelBuffer, labelBuffer.length - numChars, numChars);
        final int labelHeight = Math.abs(fontMetrics.ascent);
        float left = drawRect.centerX() - labelWidth / 2;
        float right = drawRect.centerX() + labelWidth / 2;
        float top;
        float bottom;
        if (isStacked && labelHeight < drawRect.height() - (2 * labelMargin)) {
            // For stacked columns draw label only if label height is less than subcolumn height - (2 * labelMargin).
            if (columnValue.getValue() >= baseValue) {
                top = drawRect.top;
                bottom = drawRect.top + labelHeight + labelMargin * 2;
            } else {
                top = drawRect.bottom - labelHeight - labelMargin * 2;
                bottom = drawRect.bottom;
            }
        } else if (!isStacked) {
            // For not stacked draw label at the top for positive and at the bottom for negative values
            if (columnValue.getValue() >= baseValue) {
                top = drawRect.top - offset - labelHeight - labelMargin * 2;
                /*if (top < computator.getContentRectMinusAllMargins().top) {
                    top = drawRect.top + offset;
                    bottom = drawRect.top + offset + labelHeight + labelMargin * 2;
                } else {*/
                bottom = drawRect.top - offset;
                //}
            } else {
                bottom = drawRect.bottom + offset + labelHeight + labelMargin * 2;
                if (bottom > computator.getContentRectMinusAllMargins().bottom) {
                    top = drawRect.bottom - offset - labelHeight - labelMargin * 2;
                    bottom = drawRect.bottom - offset;
                } else {
                    top = drawRect.bottom + offset;
                }
            }
        } else {
            // Draw nothing.
            return;
        }

        labelBackgroundRect.set(left, top, right, bottom);
        drawLabelTextAndBackground(canvas, labelBuffer, labelBuffer.length - numChars, numChars,
                columnValue.getDarkenColor());

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

    public boolean isFullColumnTouched() {
        return isFullColumnTouched;
    }

    public void setFullColumnTouched(boolean fullColumnTouched) {
        isFullColumnTouched = fullColumnTouched;
    }

    public void setCustomAxes(boolean customAxes) {
        isCustomAxes = customAxes;
    }
}
