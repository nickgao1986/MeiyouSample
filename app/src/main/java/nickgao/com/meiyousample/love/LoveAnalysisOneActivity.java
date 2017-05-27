package nickgao.com.meiyousample.love;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoadingView;
import com.meetyou.chartview.view.LineChartView;

import activity.PeriodBaseActivity;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.controller.AnalysisController;
import nickgao.com.meiyousample.event.NewsWebViewEvent;

/**
 * Created by gaoyoujian on 2017/5/23.
 */

public class LoveAnalysisOneActivity  extends PeriodBaseActivity implements
        View.OnClickListener {


    private SensorManager mLoveManager = null;
    private Sensor mLoveSensor = null;

//    private boolean isFinishLandLove = true;// 代表爱爱分析横屏结束
//    private AnalysisMainLoveHelper helper;
//    private CalendarCustScrollView scLoveLoveView;
//    private LoveManagerCalendar loveAnalysisManager;
//    private ChartViewTypeModel mChartModel;

    private TextView tv_analysis_temp_periodname;
    private TextView tv_symp_desc;
    private TextView tv_now_period_date;
    private LineChartView lineChartView;
    private Button btn_analy_love_land;
    private TextView tv_symp_count;
    private TextView tv_normal;
    private int headHeight;
    private LinearLayout llAdviceContainer;
    private LoadingView loadingView;
    private LinearLayout ll_analysis_state;
    private TextView mEmptyTextView;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_analy_love_1;
    }


    private SharedPreferences preferences;
    public static final String SETTING_FILE = "data_saver";

    static class Holder {
        static AnalysisController instance = new AnalysisController();
    }

    public static AnalysisController getInstance() {
        return Holder.instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
//        mController = AnalysisController.getInstance();
//        loveAnalysisManager = mController.getLoveAnalysisManager();
//        helper = new AnalysisMainLoveHelper(this, AnalysisMainBaseHelper.STATE_DETAIL);
//        helper.initRxJavaKey(TAG);
        findView();
//        initLogic();
//        setListener();

        View view = findViewById(R.id.rl_love_dashboard);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.topMargin = 0;
        view.setLayoutParams(lp);

       // getLoveObservable();
    }
    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }


    private void findView() {
//        headHeight = DeviceUtils.dip2px(getApplicationContext(), 170);
      //  scLoveLoveView = (CalendarCustScrollView) findViewById(R.id.scLoveLoveView);
        llAdviceContainer = (LinearLayout) findViewById(R.id.llAdviceContainer);
//        mCover = (LinearLayout) findViewById(R.id.baselayout_vg_cover);
//        btn_record = (Button) findViewById(R.id.btn_record);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        lineChartView = (LineChartView) findViewById(R.id.line_chartview);
        ll_analysis_state = (LinearLayout) findViewById(R.id.ll_analysis_state);
        tv_normal = (TextView) findViewById(R.id.tv_layout_love_analysis_chart_bottom);
        fillEmptyText();

        findViewById(R.id.rl_love_layout).setOnClickListener(this);
//        btn_record.setOnClickListener(this);
        btn_analy_love_land = (Button) findViewById(R.id.btn_analy_love_land);
    //    helper.findView();
//        initHead();
    }

    private void fillEmptyText() {
        fillEmptyMessage("你还没有任何爱爱记录\n" + getResources().getString(R.string.app_name) + "不知道该怎么分析哦");
    }


    public void fillEmptyMessage(String content) {
        if (mEmptyTextView == null) {
            mEmptyTextView = (TextView) findViewById(R.id.empty_des);
        }
        if (mEmptyTextView != null) {
            mEmptyTextView.setText(content);
        }
    }


    private void initTitle() {
        getTitleBar().setTitle(R.string.pregnancy_analysis)
                .setRightTextViewString(R.string.calendar_all_record_title)
                .setLeftButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setRightTextViewListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //toAllRecord();
                    }
                });
    }




    @Override
    public void onClick(View v) {


    }
}
