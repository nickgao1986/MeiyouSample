package nickgao.com.meiyousample.friend;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import fragment.PeriodBaseFragment;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;

/**
 * 添加关注的界面
 * Created by Administrator on 14-7-8.
 */
public class AddFriendFragment extends PeriodBaseFragment implements View.OnClickListener {

    private int[] TAB_IDS = new int[]{R.id.rl_type_notable, R.id.rl_type_interest};
    private FragmentActivity mActivity;
    public int mDefaultTab = 0;
    private RelativeLayout rl_type_notable, rl_type_interest, rl_type_friend;
    private FragmentManager fragmentManager;
    private FamousPersonFriendFragment famousPersonFriendFragment;
    private InterestFriendFragment interestFriendFragment;
    private String TAG_FAMOUSPERSON = "/famousperson";
    private String TAG_INTEREST = "/interest";
    private boolean isFollow;//是否有关注操作

    public static Intent getNotifyIntent(Context context) {
        Intent intent = new Intent(context, AddFriendFragment.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

//    @Override
//    protected int getLayoutId() {
//        return R.layout.layout_add_friend;
//    }


    @Override
    protected int getLayout() {
        return R.layout.layout_add_friend;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = mActivity.getSupportFragmentManager();

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
            View rootView = getRootView();
            rl_type_notable = (RelativeLayout) rootView.findViewById(R.id.rl_type_notable);
            rl_type_friend = (RelativeLayout) rootView.findViewById(R.id.rl_type_friend);
            rl_type_interest = (RelativeLayout) rootView.findViewById(R.id.rl_type_interest);

            RelativeLayout rlNagivationBar = (RelativeLayout) rootView.findViewById(R.id.baselayout_vg_general);


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
        View rootView = getRootView();

        for (int i = 0; i < TAB_IDS.length; i++) {
            rootView.findViewById(TAB_IDS[i]).setSelected(position == i);
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





    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
