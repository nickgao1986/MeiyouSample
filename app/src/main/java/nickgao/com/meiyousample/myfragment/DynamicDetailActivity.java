package nickgao.com.meiyousample.myfragment;

import android.os.Bundle;
import android.view.View;

import activity.PeriodBaseActivity;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.firstPage.DynamicDetailClickPraiseEvent;

/**
 * Created by gaoyoujian on 2017/5/2.
 */

public class DynamicDetailActivity extends PeriodBaseActivity implements
        View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitle("动态详情")
                .setRightButtonRes(R.drawable.btn_more_selector)
                .setRightButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_dynamic_detail;
    }

    @Override
    public void onClick(View v) {

    }

    public void onEventMainThread(DynamicDetailClickPraiseEvent event) {
        if (event == null) {
            return;
        }

    }
}
