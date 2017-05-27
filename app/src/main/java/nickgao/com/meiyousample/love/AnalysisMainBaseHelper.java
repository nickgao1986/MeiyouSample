//package nickgao.com.meiyousample.love;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.os.Handler;
//import android.support.annotation.IntDef;
//import android.text.Html;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.lingan.seeyou.ui.view.CircleProgressbar;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//import nickgao.com.meiyousample.R;
//
///**
// * Created by gaoyoujian on 2017/5/23.
// */
//
//public class AnalysisMainBaseHelper {
//
//    public static final int STATE_MAIN =0;
//    public static final int STATE_DETAIL =1;
//
//    static final int CHART_MIN_HEIGHT = 100;
//
//    public static final int DURATIOIN = 1250;
//    public static final String TAG = "AnalysisBaseActivity";
//
//
//    /**
//     * 区分是首页还是详情页
//     */
//    public  int state = STATE_MAIN;
//
//    AnalysisBaseActivity activity;
//    Context application;
//
//    /**
//     * helper用于多个Activity的时候 ->
//     */
//    private String mRxJavaKey;
//
//    public void initRxJavaKey(String rxKey) {
//        this.mRxJavaKey = rxKey;
//    }
//
//    public String getRxJavaKey() {
//        return TextUtils.isEmpty(mRxJavaKey) ? AnalysisMainActivity.OBSERVALE_KEY : mRxJavaKey;
//    }
//
//    /**
//     * 将头部Helper复用
//     */
//    public String helperTitle;
//    public AnalysisHeadMoreHelper helper;
//    public void initHelper(View mView, View.OnClickListener listener) {
//        if (helper != null) {
//            return;
//        }
//        helper = new AnalysisHeadMoreHelper.Builder()
//                .setArrowGone(state == STATE_MAIN)
//                .setRightHint(state == STATE_MAIN ? "" : activity.getResources().getString(R.string.learn_more))
//                .setTitle(helperTitle)
//                .setRootView(mView)
//                .setOnclickListener(listener)
//                .build();
//        helper.generateHeadMoreLayout();
//    }
//
//    public AnalysisHeadMoreHelper getHelper() {
//        return helper;
//    }
//
//    public AnalysisMainBaseHelper(AnalysisBaseActivity _AnalysisMainActivity){
//        activity=_AnalysisMainActivity;
//        application = activity.getApplicationContext();
//    }
//
//    public AnalysisMainBaseHelper(AnalysisBaseActivity _AnalysisMainActivity, @STATE_ANALYSIS int state){
//        this(_AnalysisMainActivity);
//        this.state =state;
//    }
//
//
//    public static int  getTextColor(int state){
//        int color= R.color.green_bar_color;
//        switch (state) {
//            case CircleProgressbar.STATE_GREEN:
//                color= R.color.green_bar_color;
//                break;
//            case CircleProgressbar.STATE_RED:
//                color= R.color.red_bar_color;
//                break;
//            case CircleProgressbar.STATE_YELLOW:
//                color= R.color.orange_bar_color;
//                break;
//            default:
//                break;
//        }
//        return color;
//    }
//
//    String getString(int resId){
//        return activity.getString(resId);
//    }
//
//    Resources getResources(){
//        return activity.getResources();
//    }
//
//    protected View findViewById(int resId){
//        return activity.findViewById(resId);
//    }
//
//
//
//    /**
//     * 设置分析圆环的百分比
//     *
//     * @param percent
//     * @param state
//     */
//    public void fillCircleProgress(CircleProgressbar mCirclePb, int percent, int state) {
//        mCirclePb.setState(state);
//        mCirclePb.setProgress(percent);
//    }
//
//    /**
//     * 圆环中 文字渐增效果
//     *
//     * @param point
//     */
//    public void refreshTextViewInProgress(int point, TextView view) {
//        int diff = point / (AnalysisBaseActivity.DURATIOIN / 100);
//        diff = diff <= 0 ? 1 : diff;
//        isResetText = true;
//        resetText(0, point, diff, view);
//    }
//
//    //添加该值的目的：当修改数据为空时，该刷新text的handler还在跑，覆盖了空值
//    private boolean isResetText = true;
//
//    /**
//     * 暂停正在刷新字体的handler，以免覆盖真正的值
//     */
//    protected void pauseResetTextHandler() {
//        isResetText = false;
//    }
//
//    /**
//     * 设置得分或者几率
//     *
//     * @param i
//     * @param finalPoint
//     * @param dif
//     */
//    private void resetText(final int i, final int finalPoint, final int dif,final TextView view) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(isResetText) {
//                    if (dif * i >= finalPoint) {
//                        view.setText(finalPoint + "");
//                    } else {
//                        view.setText(dif * i + "");
//                        resetText(1 + i, finalPoint, dif, view);
//                    }
//                }
//            }
//        }, (AnalysisBaseActivity.DURATIOIN / 100));
//    }
//
//
//    /**
//     * 圆环动画
//     * @param percent
//     */
//    public void rotateProgress(int percent, ImageView imageView) {
//        int fromDegree = 0;
//        int toDegree = 360 * percent / 100;
//        rotateProgress(fromDegree, toDegree, AnalysisBaseActivity.DURATIOIN, imageView);
//    }
//
//    /**
//     * 旋转圆形分析图箭头位置
//     *
//     * @param toDegree
//     * @param toDegree
//     * @param time
//     */
//    public void rotateProgress(float fromDegree, float toDegree, long time, ImageView imageView) {
//        RotateAnimation animation = new RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(time);
//        animation.setFillAfter(true);
//        imageView.startAnimation(animation);
//    }
//
//
//    /**
//     * 初始化本地数据等 逻辑后 给页面回调
//     */
//    public interface Callback {
//        public void handleResult(Object obj);
//    }
//
//    /**
//     * 通过Html给TextView设置数据  可以节省N多的TextView布局
//     */
//    public static void setAnalyTextViewDataByHtml(TextView textView, String firstTitle, String firstData) {
//        StringBuilder sb = new StringBuilder();// &nbsp 显示为空格
//        sb.append(firstTitle).append("&nbsp&nbsp").append("  <font color='#888888'>").append(firstData).append("<font>");
//        textView.setText(Html.fromHtml(sb.toString()));
//    }
//
//}
