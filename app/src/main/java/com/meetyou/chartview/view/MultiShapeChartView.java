package com.meetyou.chartview.view;

import android.content.Context;
import android.util.AttributeSet;

import com.meetyou.chartview.model.ChartData;
import com.meetyou.chartview.model.MultiShapeChartData;
import com.meetyou.chartview.provider.MultiShapeChartDataProvider;
import com.meetyou.chartview.renderer.MultiShapeChartRender;


/**
 * Created by ckq on 5/11/16.
 */
public class MultiShapeChartView extends AbstractChartView implements MultiShapeChartDataProvider {
    protected MultiShapeChartData data;

    public MultiShapeChartView(Context context) {
        this(context, null, 0);
    }

    public MultiShapeChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiShapeChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChartRenderer(new MultiShapeChartRender(context, this, this));
    }

    @Override
    public MultiShapeChartData getMultiShapeChartData() {
        return data;
    }

    @Override
    public void setMultiShapeChartData(MultiShapeChartData data) {
        if (null == data) {
            this.data = MultiShapeChartData.generateDummyData();
        } else {
            this.data = data;
        }

        super.onChartDataChange();
    }

    @Override
    public ChartData getChartData() {
        return data;
    }

    @Override
    public void callTouchListener() {
        /*SelectedValue selectedValue = chartRenderer.getSelectedValue();

        if (selectedValue.isSet()) {
            PointValue point = data.getLines().get(selectedValue.getFirstIndex()).getValues()
                    .get(selectedValue.getSecondIndex());
            onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
        } else {
            onValueTouchListener.onValueDeselected();
        }*/
    }

}
