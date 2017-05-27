package com.meetyou.chartview.model;


import com.meetyou.chartview.util.ChartUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ckq on 5/12/16.
 */
public class MultiShape extends AbstractElement {
    private List<ShapeValue> values = new ArrayList<>();
    private List<SingleShape> shapes = new ArrayList<>();

    public static final int UNINITIALIZED = 0;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int trianglePointColor = UNINITIALIZED;
    private int circlePointColor = UNINITIALIZED;
    private int squarePointColor = UNINITIALIZED;

    public void setValues(List<ShapeValue> values) {
        this.values = values;

        shapes.clear();
        for (ShapeValue value: values){
            SingleShape shape = new SingleShape();
            shape.setShape(value.getShape());
            shape.setValue(value);
            shapes.add(shape);
        }
    }

    public List<ShapeValue> getValues() {
        return values;
    }

    public void setShapes(List<SingleShape> shapes) {
        this.shapes = shapes;
    }

    public List<SingleShape> getShapes() {
        return shapes;
    }

    public void setCirclePointColor(int circlePointColor) {
        this.circlePointColor = circlePointColor;
        for (SingleShape shape: shapes) {
            if (shape.getShape() == ValueShape.CIRCLE)
                shape.setPointColor(circlePointColor);
        }
    }

    public int getCirclePointColor() {
        if (circlePointColor == UNINITIALIZED) {
            return color;
        } else {
            return circlePointColor;
        }
    }

    public void setSquarePointColor(int squarePointColor) {
        this.squarePointColor = squarePointColor;
        for (SingleShape shape: shapes) {
            if (shape.getShape() == ValueShape.SQUARE)
                shape.setPointColor(squarePointColor);
        }
    }

    public int getSquarePointColor() {
        if (squarePointColor == UNINITIALIZED) {
            return color;
        } else {
            return squarePointColor;
        }
    }

    public void setTrianglePointColor(int trianglePointColor) {
        this.trianglePointColor = trianglePointColor;
        for (SingleShape shape: shapes) {
            if (shape.getShape() == ValueShape.TRIANGLE)
                shape.setPointColor(trianglePointColor);
        }
    }

    public int getTrianglePointColor() {
        if (trianglePointColor == UNINITIALIZED) {
            return color;
        } else {
            return trianglePointColor;
        }
    }
}
