package com.meetyou.chartview.model;

import com.meetyou.chartview.util.ChartUtils;

import java.util.Arrays;


/**
 * Single sub-column value for ColumnChart.
 */
public class SubcolumnValue {

    private float value;
    private float originValue;
    private float diff;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    // 柱状渐变起始色
    private int shaderStartColor;
    // 柱状渐变结束色
    private int shaderEndColor;
    // 柱状点击渐变起始色
    private int darkenShaderStartColor;
    // 柱状点击渐变结束色
    private int darkenShaderEndColor;
    // 柱状是否有圆角
    private boolean isRoundedRect;
    //柱状图的Label
    private char[] label;
    //美柚柱状图底部显示列值；
    private String labelBottom;
    //默认不显示 柱状底部值
    public boolean enableLabeBottom=false;

    public SubcolumnValue() {
        setValue(0);
    }

    public SubcolumnValue(float value) {
        // point and targetPoint have to be different objects
        setValue(value);
    }

    public SubcolumnValue(float value, int color) {
        // point and targetPoint have to be different objects
        setValue(value);
        setColor(color);
    }

    public SubcolumnValue(SubcolumnValue columnValue) {
        setValue(columnValue.value);
        setColor(columnValue.color);
        this.label = columnValue.label;
    }

    public void update(float scale) {
        value = originValue + diff * scale;
    }

    public void finish() {
        setValue(originValue + diff);
    }

    public float getValue() {
        return value;
    }

    public SubcolumnValue setValue(float value) {
        this.value = value;
        this.originValue = value;
        this.diff = 0;
        this.labelBottom = value + "";
        return this;
    }

    /**
     * Set target value that should be reached when data animation finish then call {@link Chart#startDataAnimation()}
     *
     * @param target
     * @return
     */
    public SubcolumnValue setTarget(float target) {
        setValue(value);
        this.diff = target - originValue;
        return this;
    }

    public int getColor() {
        return color;
    }

    public SubcolumnValue setColor(int color) {
        this.color = color;
        this.darkenColor = ChartUtils.darkenColor(color);
        return this;
    }

    public SubcolumnValue setDarkenColor(int color) {
        this.darkenColor = color;
        return this;
    }

    public int getDarkenColor() {
        return darkenColor;
    }

    public int getShaderStartColor() {
        return shaderStartColor;
    }

    public void setShaderStartColor(int shaderStartColor) {
        this.shaderStartColor = shaderStartColor;
    }

    public int getShaderEndColor() {
        return shaderEndColor;
    }

    public void setShaderEndColor(int shaderEndColor) {
        this.shaderEndColor = shaderEndColor;
    }

    public int getDarkenShaderStartColor() {
        return darkenShaderStartColor;
    }

    public void setDarkenShaderStartColor(int darkenShaderStartColor) {
        this.darkenShaderStartColor = darkenShaderStartColor;
    }

    public int getDarkenShaderEndColor() {
        return darkenShaderEndColor;
    }

    public void setDarkenShaderEndColor(int darkenShaderEndColor) {
        this.darkenShaderEndColor = darkenShaderEndColor;
    }

    public boolean isRoundedRect() {
        return isRoundedRect;
    }

    public void setRoundedRect(boolean roundedRect) {
        isRoundedRect = roundedRect;
    }

    @Deprecated
    public char[] getLabel() {
        return label;
    }

    /**
     * 设置柱状图顶部label显示
     * @param label
     * @return
     */
    public SubcolumnValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return label;
    }

    @Deprecated
    public SubcolumnValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    @Override
    public String toString() {
        return "ColumnValue [value=" + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubcolumnValue that = (SubcolumnValue) o;

        if (color != that.color) return false;
        if (darkenColor != that.darkenColor) return false;
        if (Float.compare(that.diff, diff) != 0) return false;
        if (Float.compare(that.originValue, originValue) != 0) return false;
        if (Float.compare(that.value, value) != 0) return false;
        if (!Arrays.equals(label, that.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (value != +0.0f ? Float.floatToIntBits(value) : 0);
        result = 31 * result + (originValue != +0.0f ? Float.floatToIntBits(originValue) : 0);
        result = 31 * result + (diff != +0.0f ? Float.floatToIntBits(diff) : 0);
        result = 31 * result + color;
        result = 31 * result + darkenColor;
        result = 31 * result + (label != null ? Arrays.hashCode(label) : 0);
        return result;
    }

    public String getLabelBottom() {
        return labelBottom;
    }

    /**
     * 设置列底部显示值；
     *
     * @param labelBottom
     */
    public void setLabelBottom(String labelBottom) {
        this.enableLabeBottom=true;
        this.labelBottom = labelBottom;
    }
}
