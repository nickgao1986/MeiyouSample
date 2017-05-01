package nickgao.com.meiyousample.friend;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import activity.PeriodBaseActivity;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;

/**
 * 添加关注的界面
 * Created by Administrator on 14-7-8.
 */
public class AddFriendActivity extends PeriodBaseActivity implements View.OnClickListener {

    private int[] TAB_IDS = new int[]{R.id.rl_type_notable, R.id.rl_type_interest};
    private Activity mActivity;
    public int mDefaultTab = 0;
    private RelativeLayout rl_type_notable, rl_type_interest, rl_type_friend;
    private FragmentManager fragmentManager;
    private FamousPersonFriendFragment famousPersonFriendFragment;
    private InterestFriendFragment interestFriendFragment;
    private String TAG_FAMOUSPERSON = "/famousperson";
    private String TAG_INTEREST = "/interest";
    private boolean isFollow;//是否有关注操作

    public static Intent getNotifyIntent(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_add_friend;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        fragmentManager = getSupportFragmentManager();
        intTitle();
        intUI();
        intLogic();
        setListener();
    }

    private void intTitle() {
        getTitleBar().setTitle("添加关注").setRightButtonRes(R.drawable.social_search_selector);
        getTitleBar().setButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void intUI() {
        try {
            rl_type_notable = (RelativeLayout) findViewById(R.id.rl_type_notable);
            rl_type_friend = (RelativeLayout) findViewById(R.id.rl_type_friend);
            rl_type_interest = (RelativeLayout) findViewById(R.id.rl_type_interest);

            RelativeLayout rlNagivationBar = (RelativeLayout) findViewById(R.id.baselayout_vg_general);

//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), rlNagivationBar, R.drawable.apk_default_titlebar_bg);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.rl_root), R.drawable.bottom_bg);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.rl_type), R.drawable.apk_all_spread_kuang_bottom_selector);
//            SkinEngine.getInstance().setViewImageDrawable(getApplicationContext(), (ImageView) findViewById(R.id.baselayout_iv_left), R.drawable.back_layout);
//            SkinEngine.getInstance().setViewImageDrawable(getApplicationContext(), (ImageView) findViewById(R.id.baselayout_iv_right), R.drawable.social_search_selector);
//            SkinEngine.getInstance().setViewTextColor(getApplicationContext(), ((TextView) findViewById(R.id.tv_type_notable)), R.color.textview_color_topic);
//            SkinEngine.getInstance().setViewTextColor(getApplicationContext(), ((TextView) findViewById(R.id.tv_type_interest)), R.color.textview_color_topic);
//            SkinEngine.getInstance().setViewTextColor(getApplicationContext(), ((TextView) findViewById(R.id.tv_type_friend)), R.color.textview_color_topic);
//            SkinEngine.getInstance().setViewTextColor(getApplicationContext(), ((TextView) findViewById(R.id.baselayout_tv_title)), R.color.white_a);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.view_select_one), R.drawable.btn_friend_recommend_selector);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.view_driver_one), R.drawable.apk_all_linetow);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.view_select_two), R.drawable.btn_friend_recommend_selector);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.view_driver_two), R.drawable.apk_all_linetow);
//            SkinEngine.getInstance().setViewBackground(getApplicationContext(), findViewById(R.id.view_select_three), R.drawable.btn_friend_recommend_selector);
            //SkinEngine.getInstance().setViewBackground(getApplicationContext(),findViewById(R.id.iv_line),R.drawable.ptn_wavy_line);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private void setListener() {
        rl_type_notable.setOnClickListener(this);
        rl_type_friend.setOnClickListener(this);
        rl_type_interest.setOnClickListener(this);
    }

    private void intLogic() {
        handleTabChange(mDefaultTab);

    }

    private void handleTabChange(int position) {
        mDefaultTab = position;

        for (int i = 0; i < TAB_IDS.length; i++) {
            findViewById(TAB_IDS[i]).setSelected(position == i);
        }
        try {
            Fragment fragment = null;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (mDefaultTab == 0) {
                famousPersonFriendFragment = (FamousPersonFriendFragment) fragmentManager.findFragmentByTag(TAG_FAMOUSPERSON);
                if (null == famousPersonFriendFragment) {
                    famousPersonFriendFragment = new FamousPersonFriendFragment();
                }
                fragment = famousPersonFriendFragment;
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.flContainers, fragment, TAG_FAMOUSPERSON);
                }
                if (interestFriendFragment != null && interestFriendFragment.isAdded()) {
                    fragmentTransaction.hide(interestFriendFragment);
                }

            } else {
                interestFriendFragment = (InterestFriendFragment) fragmentManager.findFragmentByTag(TAG_INTEREST);
                if (null == interestFriendFragment) {
                    interestFriendFragment = new InterestFriendFragment();
                }
                fragment = interestFriendFragment;
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.flContainers, fragment, TAG_INTEREST);
                }
                if (famousPersonFriendFragment != null && famousPersonFriendFragment.isAdded()) {
                    fragmentTransaction.hide(famousPersonFriendFragment);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        for (int i = 0; i < TAB_IDS.length; i++) {
            if (v.getId() == TAB_IDS[i]) {
                handleTabChange(i);
                return;
            }
        }
    }

    public void addFollow() {
        isFollow = true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        if (isFollow)
            setResult(100);
        super.finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
