package fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.AKeyTopView;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import de.greenrobot.event.EventBus;
import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class PeriodBaseFragment extends LinganFragment {


    private AKeyTopView aKeyTopView;

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addAkeyTop();
        updateSkin();
    }

    //添加一键置顶功能
    private void addAkeyTop() {
        try {
            RelativeLayout llManBan=new RelativeLayout(getActivity().getApplicationContext());
            aKeyTopView = new AKeyTopView(getActivity().getApplicationContext(),llManBan,null);
            View view = ViewFactory.from(getActivity().getApplicationContext()).getLayoutInflater().inflate(R.layout.layout_a_key_top, null);
            aKeyTopView.init(view);
            RelativeLayout.LayoutParams paramsManban = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            getRootView().addView(llManBan, paramsManban);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getRootView().addView(view, params);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public AKeyTopView getAKeyTopView() {
        return aKeyTopView;
    }


    @SuppressLint("ResourceAsColor")
    public void updateSkin() {
        try {
            SkinManager.getInstance().setDrawableBackground(getRootView(),R.color.black_f)
                    .setDrawableBackground(titleBarCommon, R.drawable.apk_default_titlebar_bg)
                    .setDrawableBackground(baseLayout,R.drawable.bottom_bg)
                    .setDrawable(titleBarCommon.getIvLeft(), R.drawable.nav_btn_back)
                    .setTextColor(titleBarCommon.getTvTitle(), R.color.white_a)
                    .setTextColor(titleBarCommon.getTvRight(), R.color.white_a)
                    .setTextColor(titleBarCommon.getTvLeft(), R.color.white_a);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //获取标题栏
    protected TitleBarCommon getTitleBar() {
        return titleBarCommon;
    }
}
