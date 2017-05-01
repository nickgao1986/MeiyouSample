package nickgao.com.meiyousample.mypage.module;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import java.util.List;

import biz.threadutil.FileUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class MyFamilyController {

    private Activity context;
    private List<FamilyModel> familyModelList = null;
    //美柚家族
    private LinearLayout llMeiYouFamily;
    private TextView tvMeiYouFamily;
    private View view_meiyou_family;
    private GridViewEx gvMeiYouFamily;
    private FamilyAdapter familyAdapter;
    private View rootView;


    public MyFamilyController(Activity mActivity, View mRootView) {
        context = mActivity;
        rootView = mRootView;
    }

    public void findView() {
        //美柚家族
        llMeiYouFamily = (LinearLayout) rootView.findViewById(R.id.llMeiYouFamily);
        tvMeiYouFamily = (TextView) rootView.findViewById(R.id.tvMeiYouFamily);
        view_meiyou_family = rootView.findViewById(R.id.view_meiyou_family);
        gvMeiYouFamily = (GridViewEx) rootView.findViewById(R.id.gvMeiYouFamily);
    }

    public void fillResource() {
        SkinManager.getInstance().setTextColor(tvMeiYouFamily, R.color.black_a);
        SkinManager.getInstance().setDrawableBackground(view_meiyou_family, R.drawable.apk_all_lineone);
        SkinManager.getInstance().setDrawableBackground(llMeiYouFamily, R.drawable.apk_all_white);
        if (familyAdapter != null) {
            familyAdapter.notifyDataSetChanged();
        }
    }
    public static final String FAMILY_FILE_NAME = "familyName";
    /**
     * 美柚家族
     */
    public void initMeiyouFamilyData() {

        try {
            String familyTitle = "family";
            familyModelList = (List<FamilyModel>) FileUtils.getObjectFromLocal(context, FAMILY_FILE_NAME);
            if (!StringUtils.isEmpty(familyTitle) && null != familyModelList && familyModelList.size() > 0) {
                tvMeiYouFamily.setText(familyTitle);
                llMeiYouFamily.setVisibility(View.VISIBLE);
                familyAdapter = new FamilyAdapter(context, familyModelList);
//                int gridViewColumnWidth = (DeviceUtils.getScreenWidth(mActivity) - DeviceUtils.dip2px(mActivity, 270)) / 3;
//                gvMeiYouFamily.setHorizontalSpacing(gridViewColumnWidth);
                gvMeiYouFamily.setAdapter(familyAdapter);

            } else {
                llMeiYouFamily.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            llMeiYouFamily.setVisibility(View.GONE);
            e.printStackTrace();
        }


    }

}
