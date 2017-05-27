package com.meetyou.chartview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ckq on 5/12/16.
 */
public class MultiShapeChartData extends AbstractChartData {

    private List<MultiShape> shapes = new ArrayList<MultiShape>();

    public MultiShapeChartData() {

    }

    public MultiShapeChartData(List<MultiShape> multiShapes) {
        setMultiShapes(multiShapes);
    }

    public MultiShapeChartData(MultiShapeChartData data) {
        for (MultiShape shape : data.shapes) {
            MultiShape multiShape = new MultiShape();
            List<ShapeValue> values = new ArrayList<>();
            for (ShapeValue value : shape.getValues()) {
                values.add(value);
            }
            multiShape.setValues(values);
            this.shapes.add(multiShape);
        }
    }

    @Override
    public void update(float scale) {
        /*for (MultiShape shape : shapes) {
            shape.update(scale);
        }*/
    }

    @Override
    public void finish() {

    }

    public MultiShapeChartData setMultiShapes(List<MultiShape> shapes) {
        if (null == shapes) {
            this.shapes = new ArrayList<MultiShape>();
        } else {
            this.shapes = shapes;
        }
        return this;
    }

    public List<MultiShape> getMultiShapes() {
        return shapes;
    }

    public static MultiShapeChartData generateDummyData() {
        final int numValues = 4;

        List<MultiShape> multiShapes = new ArrayList<>();
        MultiShapeChartData data = new MultiShapeChartData();

        for (int i = 0; i < 5; i++) {
            List<ShapeValue> values = new ArrayList<>(numValues);

            values.add(new ShapeValue(new PointValue(0, 20), ValueShape.CIRCLE));
            values.add(new ShapeValue(new PointValue(1, 40), ValueShape.CIRCLE));
            values.add(new ShapeValue(new PointValue(2, 30), ValueShape.CIRCLE));
            values.add(new ShapeValue(new PointValue(3, 40), ValueShape.CIRCLE));

            MultiShape multiShape = new MultiShape();
            multiShape.setValues(values);
            multiShapes.add(multiShape);
        }

        data.setMultiShapes(multiShapes);
        return data;
    }
}
