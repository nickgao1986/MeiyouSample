package nickgao.com.meiyousample.mypage.module;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.RoundedImageView;
import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.settings.DataSaveHelper;


/**
 * 主框架- 我页面 -头部逻辑
 *
 * @author zhengxiaobin <gybin02@Gmail.com>
 * @since 2016/8/10
 */
public class MyHeaderController implements View.OnClickListener {
    private static final String TAG = "MyHeaderController";
    private View mRootView;
    private Activity mActivity;

    private RelativeLayout rl_my_header;
    private RoundedImageView iv_my_head;
    private ImageView iv_my_head_camera;
    private TextView tv_my_username;
    DataSaveHelper saveHelper;
    private TextView tv_my_head_mode;
    //private MyLoginController loginController;


    public MyHeaderController(Activity mContext, View rootView) {
        this.mActivity = mContext;
        this.mRootView = rootView;
        saveHelper = DataSaveHelper.getInstance(mActivity);
       // loginController = new MyLoginController(mActivity);
    }

    public void findView() {
//        mLoginButton.setOnClickListener(this);
        rl_my_header = (RelativeLayout) findViewById(R.id.rl_my_header);
        tv_my_username = (TextView) findViewById(R.id.tv_my_username);
        tv_my_head_mode = (TextView) findViewById(R.id.tv_my_head_mode);
        iv_my_head = (RoundedImageView) findViewById(R.id.iv_my_avatar);
        iv_my_head_camera = (ImageView) findViewById(R.id.iv_my_head_camera);
        iv_my_head_camera.setVisibility(View.GONE);


        iv_my_head.setOnClickListener(this);
        rl_my_header.setOnClickListener(this);
    }

    public View findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public void fillResource() {
        SkinManager.getInstance().setDrawableBackground(rl_my_header, R.drawable.apk_all_white);
        SkinManager.getInstance().setTextColor(tv_my_username, R.color.black_b);
        SkinManager.getInstance().setTextColor(tv_my_head_mode, R.color.black_a);

        SkinManager.getInstance().setDrawableBackground(findViewById(R.id.divider1), R.drawable.apk_all_lineone);
        SkinManager.getInstance().setDrawable((ImageView) findViewById(R.id.ivArrow1), R.drawable.all_icon_arrow);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_my_username) {
            gotoProfile();

        } else if (i == R.id.rl_my_header || i == R.id.iv_my_avatar) {
            gotoProfile();

//            case R.id.rl_my_header:
//                YouMentEventUtils.getInstance().buildJsonAddCacheOne(mActivity, 23);//事件统计
//                if (mUserToken != null) {
//                    MobclickAgent.onEvent(mActivity, "mine-wdzl");
//                    MyProfileActivity.enterActivity(mActivity,
//                            MyProfileActivity.class, false);
//                } else {
//                    doLoginIntent();
//                }
//                break;
        }

    }


    // 处理登录
    private void gotoProfile() {

    }


    /**
     * 登录后调用
     */
    public void initView() {

    }


    /**
     * 处理头像动画
     *
     * @param
     */
    public void setAvatar(boolean bclearCache) {
//        UserPhotoManager.getInstance().showMyPhoto(mActivity, iv_my_head,
//                R.drawable.apk_mine_photo, bclearCache, null);
//
//        bindVipIcon(mActivity, iv_my_head);
    }

    public static void bindVipIcon(Activity activity, RoundedImageView iv_my_head) {
//        try {
//            // 头像加v
//            int isVip = DataSaveHelper.getInstance(activity).isVip() ? 1 : 0;
//            if (AccountAction.isShowV(DataSaveHelper.getInstance(activity.getApplicationContext()).getUserIsMeiyouAccount(), isVip)) {
//                BindVIconController.getInstance().bindVipIcon(activity, iv_my_head, isVip);
//            } else {
//                BindVIconController.getInstance().unbindVipIcon();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }


}
