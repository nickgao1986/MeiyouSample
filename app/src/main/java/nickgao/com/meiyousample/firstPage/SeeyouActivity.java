package nickgao.com.meiyousample.firstPage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import biz.util.ViewUtilController;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class SeeyouActivity extends FragmentActivity{

    private LinearLayout llSeeyouBottom;

    private NewsHomeFragment newsHomeFragment;
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
    // 提示点
    private TextView tvMyPromotion, tvFindPromotion, tvHomePromotion;//,tvCommunityPromotion;//tvCalendarPromotion,  ;
    // 蒙版
    private LinearLayout linearMengban;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seeyou_fragment);
        switchPage();
        //初始化UI
        initBottomIconHeight();
        //switchIcon(0);
        initBottom();
        updateUI();

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
                  //  switchToPage(true, index);
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




}
