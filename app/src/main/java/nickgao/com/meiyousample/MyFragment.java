package nickgao.com.meiyousample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fragment.PeriodBaseFragment;
import nickgao.com.meiyousample.mypage.module.DynamicNewRedHotEvent;
import nickgao.com.meiyousample.mypage.module.MyFamilyController;
import nickgao.com.meiyousample.mypage.module.MyHeaderController;
import nickgao.com.meiyousample.mypage.module.MyNightController;
import nickgao.com.meiyousample.mypage.module.ViewHolder;
import nickgao.com.meiyousample.settings.MineControl;
import nickgao.com.meiyousample.settings.MineModel;
import nickgao.com.meiyousample.settings.MineSection;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MyFragment extends PeriodBaseFragment {


    private Activity mActivity;
    private static final String TAG = "MyFragment";

    private LinearLayout llContent = null;
    //用于缓存界面上的gridview,刷新时直接使用
    private List<ViewHolder> mViewHolderList = new ArrayList<>();


    private boolean bIsNewProductLoading;

    MyFamilyController myFamilyController;
    MyNightController myNightController;
    MyHeaderController myHeaderController;

    public void updateFragment() {
        //setAvatar(false);
    }

    private void initController() {
        ViewGroup rootView = getRootView();
        myFamilyController = new MyFamilyController(mActivity, rootView);
        myNightController = new MyNightController(mActivity, rootView);
        myHeaderController = new MyHeaderController(mActivity, rootView);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initController();
        findView();
        init();


    }

    @Override
    protected int getLayout() {
        return R.layout.my_layout;
    }



    private void init() {
        myFamilyController.initMeiyouFamilyData();
        initMyDataFromLocal();
        myNightController.initSkinSwitch();
    }

    private void findView() {
        getTitleBar().setCustomTitleBar(R.layout.layout_my_titlebar);
//        myHeaderController.findView();
        myNightController.findView();
        //美柚家族
        myFamilyController.findView();

        //我的动态布局
        llContent = (LinearLayout) getRootView().findViewById(R.id.ll_my_datacontent);

        fillResource();

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("ResourceAsColor")
    private void fillResource() {
        try {

            super.updateSkin();

            //美柚家族
            myFamilyController.fillResource();
            //myNightController.fillResource();
            myHeaderController.fillResource();

            fillResourceMyLayout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myNightController.destroy();
    }

    private void fillResourceMyLayout() {
        if (mViewHolderList != null) {
            for (ViewHolder vh : mViewHolderList) {
                vh.fillResources(mActivity.getApplicationContext());
            }
        }
    }

    /**
     * 第一次进来初始化我的数据
     */
    private void initMyDataFromLocal() {
        //读取本地数据
        loadMyDataFromLocal();
        //加载网络数据,刷新本地缓存
       // loadMineData(mActivity, CalendarController.getInstance().getIdentifyManager().getIdentifyModelValue());
    }



    /**
     * 从本地读取我的数据
     */
    private void loadMyDataFromLocal() {
        MineControl.getInstance().updateViewToMyContent(mActivity, MineControl.getInstance().getMine(mActivity), llContent, mViewHolderList);
    }



    /**
     * 生成我的界面样式的view,并添加到content中
     *
     * @param result
     * @param content
     * @param viewHolderList
     */
    public void updateViewToMyContent(Activity activity, MineModel result, ViewGroup content, List<ViewHolder> viewHolderList) {
        updateViewToMyContent(activity, result, content, viewHolderList, MineControl.MINE_VIEWHOLDER_FROM_MINE);
    }

    /**
     * 生成我的界面样式的view,并添加到content中
     *
     * @param result
     * @param content
     * @param viewHolderList
     */
    public void updateViewToMyContent(Activity activity, MineModel result, ViewGroup content, List<ViewHolder> viewHolderList, int from) {
        if (result != null && content != null && viewHolderList != null) {
            content.removeAllViews();
            viewHolderList.clear();
            //生成视图
            List<MineSection> sectionList = result.getSections();
            if (sectionList != null) {
                for (int i = 0; i < sectionList.size(); i++) {
                    MineSection mineSection = sectionList.get(i);
                    ViewHolder viewHolder = null;
                    if (result.getItems() != null && i < result.getItems().size()) {
                        // viewHolder = MineLayoutFactory.getInstance().createView(activity, mineSection, result.getItems().get(i), from);
                    }
                    if (viewHolder == null) {
                        continue;
                    }
                    viewHolderList.add(viewHolder);
                    View convertView = viewHolder.getRootView();
                    //把视图添加到界面上去
                    if (mineSection.has_line) {
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                                .LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, DeviceUtils.dip2px(activity, 10), 0, 0);
                        convertView.setLayoutParams(llp);
                    }
                    content.addView(convertView);
                }
            }
        }

    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    class LoadFileTask extends AsyncTask<Void, Void, MineModel> {
//
//        @Override
//        protected MineModel doInBackground(Void... params) {
//            MineModel mineModel = MineControl.getInstance().getMine(MyFragment.this);
//            return mineModel;
//        }
//
//        @Override
//        protected void onPostExecute(MineModel homeDynamicModels) {
//            Log.d(TAG,"=====tag="+homeDynamicModels.getSections().get(0).title);
//        }
//    }

    //密友圈动态更新红点通知事件
    public void onEventMainThread(final DynamicNewRedHotEvent event) {
        if (event != null) {

        }
    }


    public void handleHideFragment() {

    }

    public void handleShowFragment() {

    }

}
