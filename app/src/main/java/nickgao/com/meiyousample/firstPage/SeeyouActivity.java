package nickgao.com.meiyousample.firstPage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import activity.PeriodBaseActivity;
import biz.util.ViewUtilController;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.MyFragment;
import nickgao.com.meiyousample.PersonalFragment;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.friend.AddFriendFragment;
import nickgao.com.meiyousample.myfragment.DynamicHomeFragment;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class SeeyouActivity extends PeriodBaseActivity {

    private LinearLayout llSeeyouBottom;

    private NewsHomeFragment newsHomeFragment;
    private DynamicHomeFragment mDynamicHomeFragment;
    private PersonalFragment mPersonalFragment;
    private AddFriendFragment mAddFriendFragment;
    private MyFragment mMyFragment;
    private int mIconWidth = 48;
    private int mIconHeight = 48;
    // 正常图标
    private int[] mNormalId = new int[]{R.drawable.apk_all_bottommeetyou,
            R.drawable.apk_all_bottomrili, R.drawable.apk_all_bottomta, R.drawable.apk_all_bottomb2c,
            R.drawable.apk_all_bottommine};

    // 选中图标
    private int[] mPresseId = new int[]{R.drawable.apk_all_bottommeetyou_up,
            R.drawable.apk_all_bottomrili_up, R.drawable.apk_all_bottomta_up, R.drawable.apk_all_bottomb2c_up,
            R.drawable.apk_all_bottommine_up};

    private String[] mText = new String[]{"美柚", "记录", "她她圈", "柚子街", "我"};

    private String[] mArrayFragmentName = new String[]{
            NewsHomeFragment.class.getSimpleName(),
            DynamicHomeFragment.class.getSimpleName(),
            PersonalFragment.class.getSimpleName(),
            AddFriendFragment.class.getSimpleName(),
            MyFragment.class.getSimpleName()};

    // 提示点
    private TextView tvMyPromotion, tvFindPromotion, tvHomePromotion;//,tvCommunityPromotion;//tvCalendarPromotion,  ;
    // 蒙版
    private LinearLayout linearMengban;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SeeyouController.getInstance().initBeforeOnCreate(this, savedInstanceState);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_seeyou_fragment);
        //initSkin();
        switchPage();
        //初始化UI
        initBottomIconHeight();
        //switchIcon(0);
        initBottom();
        updateUI();
        getTitleBar().setCustomTitleBar(-1);
        initFindPromotion();
        initHomePromotion();
        initMyPromotion();
    }

    private void updateUI() {
        updateSkin();
        SkinManager.getInstance()
                .setDrawableBackground(llSeeyouBottom, R.drawable.apk_all_bottombg);
        switchIcon(0);
    }

    @SuppressLint("ResourceAsColor")
    public void updateSkin() {
        try {
//            handleShowNightMengban();
//            if (titleBarCommon == null)
//                return;
//            SkinManager.getInstance().setDrawableBackground(mBoxImageView, R.drawable.apk_newsbg)
//                    .setDrawableBackground(titleBarCommon, R.drawable.apk_default_titlebar_bg)
//                    .setDrawableBackground(baseLayout, R.drawable.bottom_bg)
//                    .setDrawable(titleBarCommon.getIvLeft(), R.drawable.nav_btn_back);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void switchPage() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (newsHomeFragment != null) {
//            newsHomeFragment.updateFragment(isSamePage, isFromNotify);
//            newsHomeFragment.handleShowFragment();
            fragmentTransaction.show(newsHomeFragment);
        } else {
            newsHomeFragment = new NewsHomeFragment();

            fragmentTransaction.add(R.id.flContainer, newsHomeFragment, MainTabType.TAB_NEWS_HOME);
        }
        fragmentTransaction.commitAllowingStateLoss();

    }


    private void initBottomIconHeight() {
        try {
            mIconWidth = (int) (DeviceUtils.getScreenWidth(getApplicationContext()) / 9.5);

            int mOrgWidth = 120;
            int mOrgHeight = 90;
            mIconHeight = (mIconWidth * mOrgHeight) / mOrgWidth;
            //底部容器宽高
            int mContainerHeight = mIconHeight + StringUtils.getFontHeight(getResources().getDimension(R.dimen.text_size_xxs)) + DeviceUtils.dip2px(getApplicationContext(), 2) - DeviceUtils.dip2px(getApplicationContext(), 5);
            llSeeyouBottom = (LinearLayout) findViewById(R.id.llSeeyouBottom);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llSeeyouBottom.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.FILL_PARENT;
            params.height = mContainerHeight;
            llSeeyouBottom.requestLayout();
            //保存全局TAB高度
            //BeanManager.getUtilSaver().setSeeyouTabHeight(mContainerHeight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initBottom() {
        llSeeyouBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        int count = llSeeyouBottom.getChildCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            RelativeLayout relativeLayout = (RelativeLayout) llSeeyouBottom.getChildAt(i);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 切换图标
                    switchToPage(true, index);
                }
            });
        }
        RelativeLayout relativeLayout = (RelativeLayout) llSeeyouBottom.getChildAt(3);
        if (relativeLayout == null)
            return;
        relativeLayout.setVisibility(View.VISIBLE);
    }


    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(this, this.getResources(), this.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchIcon(int position) {
        try {
            for (int i = 0; i < mNormalId.length; i++) {
                // 获取对应的图标和文字
                RelativeLayout child = (RelativeLayout) llSeeyouBottom.getChildAt(i);
                LinearLayout child_one = (LinearLayout) child.getChildAt(0);
                ImageView imageView = null;
                TextView textView = null;
                TextView iconFont = null;
//                if (i == 1) {
//                    iconFont = (TextView) child_one.getChildAt(0);
//                    iconFont.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dp_18));
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iconFont.getLayoutParams();
//                    params.width = mIconWidth;
//                    params.height = mIconHeight;
//                    imageView = (ImageView) child_one.getChildAt(1);
//                    textView = (TextView) child_one.getChildAt(2);
//                } else {
//                    imageView = (ImageView) child_one.getChildAt(0);
//                    textView = (TextView) child_one.getChildAt(1);
//                }
                imageView = (ImageView) child_one.getChildAt(0);
                textView = (TextView) child_one.getChildAt(1);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.width = mIconWidth;
                params.height = mIconHeight;
              //  params.setMargins(params.leftMargin,params.topMargin,params.rightMargin,5);

//                if (i == 1) {
//                    iconFont.setText(IconFontManager.getInstance(getApplicationContext())
//                            .getDayResourcesString());
//                    iconFont.setTypeface(IconFontManager.getInstance(getApplicationContext())
//                            .getmTypeface());
//                    iconFont.setVisibility(View.VISIBLE);
//                    imageView.setVisibility(View.GONE);
//                } else {
//                    imageView.setVisibility(View.VISIBLE);
//                    if (i == 1) {
//                        iconFont.setVisibility(View.GONE);
//                    }
//                }
                // 选中，
//                if (i == position) {
//                    if (i == 1) {
//                        iconFont.setTextColor(SkinManager.getInstance()
//                                .getAdapterColor(R.color.red_b));
//                    }
//                    imageView.setImageDrawable(SkinManager.getInstance()
//                            .getAdapterDrawable(mPresseId[i]));
//                    textView.setTextColor(SkinManager.getInstance()
//                            .getAdapterColor(R.color.red_b));
//                    // 非选中
//                } else {
//                    if (i == 1) {
//                        iconFont.setTextColor(SkinManager.getInstance()
//                                .getAdapterColor(R.color.colour_c));
//                    }
//                    imageView.setImageDrawable(SkinManager.getInstance()
//                            .getAdapterDrawable(mNormalId[i]));
//                    textView.setTextColor(SkinManager.getInstance()
//                            .getAdapterColor(R.color.colour_c));
//                }
                textView.setText(mText[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void initMyPromotion() {
        try {
            tvMyPromotion = (TextView) findViewById(R.id.tvMyPromotion);
            ViewUtilController.getInstance()
                    .setPromotiomSmall(getApplicationContext(), tvMyPromotion, 0);
            tvMyPromotion.setVisibility(View.GONE);
            int marignRight = DeviceUtils.getScreenWidth(getApplicationContext()) / 5 / 2 / 2 + 5;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvMyPromotion.getLayoutParams();
            layoutParams.rightMargin = marignRight;
            tvMyPromotion.requestLayout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initHomePromotion() {
        try {
            tvHomePromotion = (TextView) findViewById(R.id.tvHomePromotion);
            ViewUtilController.getInstance()
                    .setPromotiomSmall(getApplicationContext(), tvHomePromotion, 0);
            tvHomePromotion.setVisibility(View.GONE);

            int marignRight = DeviceUtils.getScreenWidth(getApplicationContext()) / 5 / 2 / 2 + 2;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvHomePromotion
                    .getLayoutParams();
            layoutParams.rightMargin = marignRight;
            tvHomePromotion.requestLayout();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void initFindPromotion() {
        try {
            tvFindPromotion = (TextView) findViewById(R.id.tvFindPromotion);
            ViewUtilController.getInstance()
                    .setPromotiomSmall(getApplicationContext(), tvFindPromotion, 0);
            tvFindPromotion.setVisibility(View.VISIBLE);

            int marignRight = DeviceUtils.getScreenWidth(getApplicationContext()) / 5 / 2 / 2 + 5;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvFindPromotion
                    .getLayoutParams();
            layoutParams.rightMargin = marignRight;
            tvFindPromotion.requestLayout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private int currentPosition = 0;
    private boolean isRecyle = false;

    private void switchToPage(boolean isClick, int position) {
        int lastPosition = currentPosition;
        try {

            if (position == 3) {
                tvFindPromotion.setVisibility(View.GONE);
            }
            // 不能重复点击日历
            if (currentPosition == 1 && position == 1) {
                return;
            }
            if (position > mNormalId.length - 1) {
                return;
            }
            switchIcon(position);
            boolean isSamePage = false; // 是否点击同一个页面
            if (currentPosition == position) {
                isSamePage = true;
            } else {
                currentPosition = position;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            if (isRecyle) {//回收情况下，重新从FragmentManager中获取，否则会出现快速切换TAB混乱的奇葩现象
                newsHomeFragment = (NewsHomeFragment) fragmentManager.findFragmentByTag(MainTabType.TAB_NEWS_HOME);
                mAddFriendFragment = (AddFriendFragment) fragmentManager.findFragmentByTag(MainTabType.TAB_TODAYSALE);
                mDynamicHomeFragment = (DynamicHomeFragment) fragmentManager.findFragmentByTag(MainTabType.TAB_CALENDAR);
                mPersonalFragment = (PersonalFragment) fragmentManager.findFragmentByTag(MainTabType.TAB_COMMUNITY);
                mMyFragment = (MyFragment) fragmentManager.findFragmentByTag(MainTabType.TAB_MINE);
            }
            switch (position) {
                case 0: {
                    if (newsHomeFragment != null) {
                        newsHomeFragment.updateFragment(isSamePage, true);
                        newsHomeFragment.handleShowFragment();
                        fragmentTransaction.show(newsHomeFragment);
                    } else {
                        newsHomeFragment = new NewsHomeFragment();
                        fragmentTransaction.add(R.id.flContainer, newsHomeFragment, MainTabType.TAB_NEWS_HOME);
                    }


                    if (null != mPersonalFragment) {
                        fragmentTransaction.hide(mPersonalFragment);
                    }
                    if (null != mDynamicHomeFragment) {
                        fragmentTransaction.hide(mDynamicHomeFragment);
                    }
                    if (null != mAddFriendFragment) {
                        fragmentTransaction.hide(mAddFriendFragment);
                    }
                    if (null != mMyFragment) {
                        fragmentTransaction.hide(mMyFragment);
                    }
                    break;
                }
                case 1: {

                    if (mDynamicHomeFragment != null) {
                        fragmentTransaction.show(mDynamicHomeFragment);
                    } else {
                        mDynamicHomeFragment = new DynamicHomeFragment();
                        fragmentTransaction.add(R.id.flContainer, mDynamicHomeFragment, MainTabType.TAB_CALENDAR);
                    }

                    if (newsHomeFragment != null) {
                        fragmentTransaction.hide(newsHomeFragment);
                        newsHomeFragment.handleHideFragment();
                    }

                    if (null != mPersonalFragment) {
                        fragmentTransaction.hide(mPersonalFragment);
                    }

                    if (null != mAddFriendFragment) {
                        fragmentTransaction.hide(mAddFriendFragment);
                    }
                    if (null != mMyFragment) {
                        fragmentTransaction.hide(mMyFragment);
                    }
                    break;
                }

                case 2: {

                    if (mPersonalFragment != null) {
                        fragmentTransaction.show(mPersonalFragment);
                    } else {
                        mPersonalFragment = new PersonalFragment();
                        fragmentTransaction.add(R.id.flContainer, mPersonalFragment, MainTabType.TAB_COMMUNITY);
                    }

                    if (newsHomeFragment != null) {
                        fragmentTransaction.hide(newsHomeFragment);
                        newsHomeFragment.handleHideFragment();
                    }

                    if (null != mDynamicHomeFragment) {
                        fragmentTransaction.hide(mDynamicHomeFragment);
                    }

                    if (null != mAddFriendFragment) {
                        fragmentTransaction.hide(mAddFriendFragment);
                    }
                    if (null != mMyFragment) {
                        fragmentTransaction.hide(mMyFragment);
                    }
                    break;
                }

                case 3: {

                    if (mAddFriendFragment != null) {
                        fragmentTransaction.show(mAddFriendFragment);
                    } else {
                        mAddFriendFragment = new AddFriendFragment();
                        fragmentTransaction.add(R.id.flContainer, mAddFriendFragment, MainTabType.TAB_TODAYSALE);
                    }

                    if (newsHomeFragment != null) {
                        fragmentTransaction.hide(newsHomeFragment);
                        newsHomeFragment.handleHideFragment();
                    }

                    if (null != mPersonalFragment) {
                        fragmentTransaction.hide(mPersonalFragment);
                    }

                    if (null != mDynamicHomeFragment) {
                        fragmentTransaction.hide(mDynamicHomeFragment);
                    }

                    if (null != mMyFragment) {
                        fragmentTransaction.hide(mMyFragment);
                    }

                    break;
                }

                case 4: {

                    if (mMyFragment != null) {
                        mMyFragment.updateFragment();
                        fragmentTransaction.show(mMyFragment);
                    } else {
                        mMyFragment = new MyFragment();
                        fragmentTransaction.add(R.id.flContainer, mMyFragment, MainTabType.TAB_MINE);
                    }
                    if (newsHomeFragment != null) {
                        fragmentTransaction.hide(newsHomeFragment);
                        newsHomeFragment.handleHideFragment();
                    }

                    if (null != mPersonalFragment) {
                        fragmentTransaction.hide(mPersonalFragment);
                    }

                    if (null != mDynamicHomeFragment) {
                        fragmentTransaction.hide(mDynamicHomeFragment);
                    }

                    if (null != mAddFriendFragment) {
                        fragmentTransaction.hide(mAddFriendFragment);
                    }


                    break;
                }

            }
            fragmentTransaction.commitAllowingStateLoss();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_seeyou_fragment;
    }


    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }
}
