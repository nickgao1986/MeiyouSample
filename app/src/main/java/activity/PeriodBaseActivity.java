package activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.AKeyTopView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import de.greenrobot.event.EventBus;
import fragment.TitleBarCommon;
import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public abstract class PeriodBaseActivity extends LinganActivity {

    private ImageView mBoxImageView;
    private TextView mBoxCountTextView;
    protected RelativeLayout mMsgBoxRelativeLayout;
    private AKeyTopView aKeyTopView;
    private View tvMengban;
    private WindowManager windowManager;
    private WindowManager.LayoutParams lp;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        addMessageBox();
        addAkeyTop();
        if (getParentView() != null) {
            getParentView().setBackgroundColor(getResources().getColor(R.color.black_f));
        }
        EventBus.getDefault().register(this);
        updateSkin();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //添加消息盒子
    private void addMessageBox() {
//        try {
//            if (getParentView() == null)
//                return;
//            RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_msg_box, null);
//            mBoxImageView = (ImageView) relativeLayout.findViewById(R.id.iv_box);
//            mBoxCountTextView = (TextView) relativeLayout.findViewById(R.id.tv_box_count);
//            mMsgBoxRelativeLayout = (RelativeLayout) relativeLayout.findViewById(R.id.rl_msg_box);
//            mMsgBoxRelativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    UtilEventDispatcher.getInstance().jumptoMessage(getApplicationContext());
//                    Map<String, String> map = new HashMap<>();
//                    map.put("来源", "浮层按钮");
//                    AnalysisClickAgent.onEvent(PeriodBaseActivity.this, "xx", map);
//                }
//            });
//            mMsgBoxRelativeLayout.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
//            getParentView().addView(relativeLayout, params);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }

    public void showMessageBox(int messagecount) {
//        if (messagecount <= 0) {
//            hideMessageBox();
//            return;
//        }
//        mMsgBoxRelativeLayout.setVisibility(View.VISIBLE);
//        ViewUtilController.getInstance()
//                .setPromotionBigWithCount(getApplicationContext(), mBoxCountTextView, messagecount, 0, 0);
    }

    public void hideMessageBox() {
        if (mMsgBoxRelativeLayout != null) {
            mMsgBoxRelativeLayout.setVisibility(View.GONE);
        }
    }

    public AKeyTopView getAKeyTopView() {
        return aKeyTopView;
    }

    //添加一键置顶功能
    private void addAkeyTop() {
        try {
            if (getParentView() == null)
                return;
            RelativeLayout llManBan = new RelativeLayout(getApplicationContext());
            aKeyTopView = new AKeyTopView(getApplicationContext(), llManBan, mMsgBoxRelativeLayout);
            View view = ViewFactory.from(getApplicationContext())
                    .getLayoutInflater()
                    .inflate(R.layout.layout_a_key_top, null);
            aKeyTopView.init(view);
            RelativeLayout.LayoutParams paramsManban = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            getParentView().addView(llManBan, paramsManban);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getParentView().addView(view, params);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 初始化夜间模式蒙板参数
     */
    private void initNightMengbanLp() {
        lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.BOTTOM;// 可以自定义显示的位置
        lp.y = 10;
    }

    /**
     * 显示夜间模式的蒙版
     */

    private void handleShowNightMengban() {
//        try {
//            if (BeanManager.getUtilSaver().getIsNightMode(getApplicationContext())) {
//                if (lp == null) {
//                    initNightMengbanLp();
//                }
//                if (tvMengban == null) {
//                    tvMengban = ViewFactory.from(getApplicationContext()).getLayoutInflater().inflate(R.layout.layout_webview_mengban, null);
//                    tvMengban.setBackgroundColor(getResources().getColor(R.color.light_web_mengban));
//                }
//                windowManager.addView(tvMengban, lp);
//            } else {
//                if (null != tvMengban) {
//                    windowManager.removeView(tvMengban);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @SuppressLint("ResourceAsColor")
    public void updateSkin() {
        try {
            handleShowNightMengban();
            if (titleBarCommon == null)
                return;
            SkinManager.getInstance().setDrawableBackground(mBoxImageView, R.drawable.apk_newsbg)
                    .setDrawableBackground(titleBarCommon, R.drawable.apk_default_titlebar_bg)
                    .setDrawableBackground(baseLayout, R.drawable.bottom_bg)
                    .setDrawable(titleBarCommon.getIvLeft(), R.drawable.nav_btn_back);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    //获取标题栏
    protected TitleBarCommon getTitleBar() {
        return titleBarCommon;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {// @by yuzongxu  在LinganActivity中已经unregister了。
            EventBus.getDefault().unregister(this);
        }
       /* if(BeanManager.getUtilSaver().isBackToMain()){
            UtilEventDispatcher.getInstance().backToMainActivity(getApplicationContext());
        }*/
    }


    /**************** 可重载方法： ****************/
    /**
     * 开放出类名给子类重载，用于Umeng需要定制类名
     *
     * @return
     */
    public String getClassName() {
        return getClass().getName();
    }



}
