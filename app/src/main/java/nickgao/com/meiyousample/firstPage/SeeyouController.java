package nickgao.com.meiyousample.firstPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.statusbar.StatusBarController;
import nickgao.com.meiyousample.statusbar.StatusbarConfig;

/**
 * Created by gaoyoujian on 2017/5/2.
 */

public class SeeyouController {

    private static final String TAG = "SeeyouController";
    private static SeeyouController mInstance;

    public static SeeyouController getInstance() {
        if (mInstance == null) {
            synchronized (SeeyouController.class) {
                if (mInstance == null) {
                    mInstance = new SeeyouController();
                }
            }
        }
        return mInstance;
    }

    private Context mContext;
    private Activity mActivity;


    public void initBeforeOnCreate(Activity activity, Bundle savedInstanceState) {
        try {
            //处理fragment被回收的情况
            if (savedInstanceState != null) {
                savedInstanceState.putParcelable("android:support:fragments", null);
            }
            mActivity = activity;
            mContext = activity.getApplicationContext();
            initSkin();
            initStatusBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //初始化皮肤
    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(mContext, mContext.getResources(), mContext.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //初始化沉浸式状态栏
    private void initStatusBar() {
        try {
            initStatusBar(mContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void initStatusBar(Context context) {
        try {
            List<String> ignoreList = new ArrayList<>();
           // ignoreList.add(DynamicDetailActivity.class.getName());

            int mDefaultColor = context.getResources().getColor(R.color.cust_status_color);
            if (SkinManager.getInstance().getOriginResources() != null) {
                mDefaultColor = SkinManager.getInstance().getAdapterColor(R.color.cust_status_color);
            }
            Map<String, Integer> specialMap = new TreeMap<>();
//            String name = DynamicDetailActivity.class.getName();
//            LogUtils.d("===name="+DynamicDetailActivity.class.getName());
            specialMap.put(context.getResources().getString(R.string.text_DynamicDetailActivity), SkinManager.getInstance().getAdapterColor(R.color.all_black));


            Map<String, Boolean> specialStatusMap = new HashMap<>();

            StatusBarController.getInstance()
                    .init(StatusbarConfig.newBuilder()
                            .withIsEnableStatusBar(true)
                            .withSpecailStatusMap(specialStatusMap)
                            .withDefaultColor(mDefaultColor)
                            .withIgnorePageList(ignoreList)
                            .withSpecailPageMap(specialMap)
                            .build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
