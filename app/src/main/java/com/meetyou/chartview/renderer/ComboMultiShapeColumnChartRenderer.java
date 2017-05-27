package com.meetyou.chartview.renderer;

import android.content.Context;

import com.meetyou.chartview.provider.ColumnChartDataProvider;
import com.meetyou.chartview.provider.MultiShapeChartDataProvider;
import com.meetyou.chartview.view.Chart;


public class ComboMultiShapeColumnChartRenderer extends ComboChartRenderer {

    public ColumnChartRenderer columnChartRenderer;
    public MultiShapeChartRender multiShapeChartRenderer;

    public ComboMultiShapeColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider,
                                              MultiShapeChartDataProvider multiShapeChartDataProvider) {
        this(context, chart, new ColumnChartRenderer(context, chart, columnChartDataProvider),
                new MultiShapeChartRender(context, chart, multiShapeChartDataProvider));
    }

    public ComboMultiShapeColumnChartRenderer(Context context, Chart chart, ColumnChartRenderer columnChartRenderer,
                                              MultiShapeChartDataProvider multiShapeChartDataProvider) {
        this(context, chart, columnChartRenderer, new MultiShapeChartRender(context, chart, multiShapeChartDataProvider));
    }

    public ComboMultiShapeColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider,
                                              MultiShapeChartRender multiShapeChartRenderer) {
        this(context, chart, new ColumnChartRenderer(context, chart, columnChartDataProvider), multiShapeChartRenderer);
    }

    public ComboMultiShapeColumnChartRenderer(Context context, Chart chart, ColumnChartRenderer columnChartRenderer,
                                              MultiShapeChartRender multiShapeChartRenderer) {
        super(context, chart);

        this.columnChartRenderer = columnChartRenderer;
        this.multiShapeChartRenderer = multiShapeChartRenderer;

        if (null != this.columnChartRenderer) {renderers.add(this.columnChartRenderer);}
        if (null != this.multiShapeChartRenderer) {renderers.add(this.multiShapeChartRenderer);}
    }
}