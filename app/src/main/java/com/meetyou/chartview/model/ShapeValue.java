package com.meetyou.chartview.model;

/**
 * Created by ckq on 5/12/16.
 */
public class ShapeValue extends PointValue {
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_TRIANGLE = 2;
    public static final int SHAPE_RECTANGLE = 3;

    private ValueShape shapeType;

    public ShapeValue(PointValue pointValue, ValueShape shapeType) {
        set(pointValue.getX(), pointValue.getY());
        this.shapeType = shapeType;
    }

    public ValueShape getShape() {
        return shapeType;
    }
}
