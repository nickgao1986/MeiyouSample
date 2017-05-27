package com.meetyou.chartview.model;

import java.util.List;

/**
 * Data model for combo line-column chart. It uses ColumnChartData and LineChartData internally.
 */
public class ComboMultiShapeColumnChartData extends AbstractChartData {

    private ColumnChartData columnChartData;
    private MultiShapeChartData multishapeChartData;
    private List<MultiShape> multiShapes;

    public ComboMultiShapeColumnChartData() {
        this.columnChartData = new ColumnChartData();
        this.multishapeChartData = new MultiShapeChartData();
    }

    public ComboMultiShapeColumnChartData(ColumnChartData columnChartData, MultiShapeChartData multishapeChartData) {
        setColumnChartData(columnChartData);
        setMultiShapeChartData(multishapeChartData);
    }

    public ComboMultiShapeColumnChartData(ComboMultiShapeColumnChartData data) {
        super(data);

        setColumnChartData(new ColumnChartData(data.getColumnChartData()));
        setMultiShapeChartData(new MultiShapeChartData(data.getMultiShapeChartData()));
    }

    public static ComboMultiShapeColumnChartData generateDummyData() {
        ComboMultiShapeColumnChartData data = new ComboMultiShapeColumnChartData();
        data.setColumnChartData(ColumnChartData.generateDummyData());
        data.setMultiShapeChartData(MultiShapeChartData.generateDummyData());
        return data;
    }

    @Override
    public void update(float scale) {
        columnChartData.update(scale);
        multishapeChartData.update(scale);
    }

    @Override
    public void finish() {
        columnChartData.finish();
        multishapeChartData.finish();
    }

    public ColumnChartData getColumnChartData() {
        return columnChartData;
    }

    public void setColumnChartData(ColumnChartData columnChartData) {
        if (null == columnChartData) {
            this.columnChartData = new ColumnChartData();
        } else {
            this.columnChartData = columnChartData;
        }
    }

    /*public List<MultiShape> getMultiShapeChartData() {
        return multiShapes;
    }*/

    public MultiShapeChartData getMultiShapeChartData() {
        return multishapeChartData;
    }

    public void setMultiShapeChartData(MultiShapeChartData multishapeChartData) {
        if (null == multishapeChartData) {
            this.multishapeChartData = new MultiShapeChartData();
        } else {
            this.multishapeChartData = multishapeChartData;
        }
    }

}
