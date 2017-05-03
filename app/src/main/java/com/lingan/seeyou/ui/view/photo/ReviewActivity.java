package com.lingan.seeyou.ui.view.photo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomViewPager;
import com.lingan.seeyou.ui.view.me.relex.photodraweeview.PhotoDraweeView;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;

import static com.lingan.seeyou.ui.view.photo.PreviewImageActivity.PreviewImageAdapter.toUri;


/**
 * 从手机相册选择-- 点击图片查看预览图
 */
public class ReviewActivity extends BasePhotoActivity {

    private List<PhotoModel> mData;
    private CheckBox mChkBox;
    private TextView mBtnOk;
    private ImageView mIvReturn;
    private TextView mTvTitle;
    private int mCurrentPagerIndex = 0;
    static final int MODE_ALL = 10002;
    static final int MODE_NORMAL = 10001;
    static final String KEY_MODE = "KEY_MODE";
    static final String KEY_BUCKET_ID = "KEY_BUCKET_ID";
    static final String KEY_POSITION = "KEY_POSITION";
    static final String KEY_PICS = "KEY_PICS";
    private RelativeLayout mRlBadge;
    private TextView mTvBadge;
    private ImageView mIvBadge;
    private PhotoController photoController;
    //private ImageUtil mImageLoader;
    public static List<PhotoModel> picsList = null;

    @Override
    protected void onResume() {
        super.onResume();
        executeAnimation(PhotoController.getInstance(getApplicationContext())
                                        .getSelectedItems()
                                        .size());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_review_activity_new);
        titleBarCommon.setCustomTitleBar(R.layout.cp_titlebar_pickphoto_new);

        int mode = getIntent().getIntExtra(KEY_MODE, -1);
        long bucketId = getIntent().getLongExtra(KEY_BUCKET_ID, -1);
        int position = getIntent().getIntExtra(KEY_POSITION, 0);
        photoController = PhotoController.getInstance(getApplicationContext());

        List<PhotoModel> picList = (List<PhotoModel>) getIntent().getSerializableExtra(KEY_PICS);
        if(picList != null && !picsList.isEmpty()){
            picsList = picList;
        }

        mData = new ArrayList<>();
      
        if (mode == -1 || mode == MODE_NORMAL) {
            mData.addAll(photoController.getSelectedItems());
        } else {
            if (picsList != null && picsList.size() > 0) {
                mData.addAll(picsList);
            } else if (bucketId == PhotoController.RECENT_BUCKET_ID) {

                mData.addAll(photoController.getRecentPhotos());
            } else {
                mData.addAll(photoController.getPhotosById(bucketId));
            }
        }


        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        mChkBox = (CheckBox) findViewById(R.id.chk);
        mBtnOk = (TextView) findViewById(R.id.btnOk);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvReturn = (ImageView) findViewById(R.id.btn_return);

        mTvTitle.setText((mCurrentPagerIndex + 1) + "/" + mData.size());
        mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRlBadge = (RelativeLayout) findViewById(R.id.rlBadge);
        mTvBadge = (TextView) findViewById(R.id.tvBadge);
        mIvBadge = (ImageView) findViewById(R.id.ivBadge);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhotoController.getInstance(getApplicationContext()).getSelectedItems().size() == 0) {
                    PhotoController.getInstance(getApplicationContext()).selectItemForce(mData.get(mCurrentPagerIndex));
                }
                photoController.finishPicking(false);
            }
        });

        if (null != mData && mData.size() > 0) {
            boolean checked = photoController
                    .isItemSelected(mData.get(0));
            mChkBox.setChecked(checked);
        }
        mChkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Context applicationContext = getApplicationContext();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (!photoController.isItemSelected(mData.get(mCurrentPagerIndex)) && photoController
                                .reachMaximum()) {
                            ToastUtils.showToast(applicationContext, "最多只能选择" + photoController
                                    .getMaxSelect() + "张照片哦~");
                            return true;
                        }
                        break;
                }
                if (mData == null || mData.size() == 0)
                    return false;
                if (!photoController.isItemSelected(mData.get(mCurrentPagerIndex)) && photoController
                        .reachMaximum()) {
                    return true;
                }
                return false;
            }
        });

        mChkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChkBox.isChecked()) {
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(mChkBox, "scaleX", 1, 1.25f, 1, 1.1f, 1),
                            ObjectAnimator.ofFloat(mChkBox, "scaleY", 1, 1.25f, 1, 1.1f, 1));
                    set.setInterpolator(new DecelerateInterpolator());
                    set.setDuration(550).start();
                }
                
                photoController.selectItem(mData.get(mCurrentPagerIndex));
                executeAnimation(photoController.getSelectedItems().size());
            }
        });
        viewPager.setAdapter(new ScreenSlidePagerAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                mChkBox.setChecked(photoController.isItemSelected(mData.get(i)));
                mCurrentPagerIndex = i;
                mTvTitle.setText((mCurrentPagerIndex + 1) + "/" + mData.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }

        });

        viewPager.setCurrentItem(position);
        
        findViewById(R.id.rl_bottom).setBackgroundResource(R.drawable.apk_all_spreadkuang);
        findViewById(R.id.ll_background).setBackgroundResource(R.color.black_a);
        ((TextView) findViewById(R.id.btnOk)).setTextColor(getResources().getColor(R.color.white_a));
        if (getIntent().hasExtra("isHideBottomLayout") && getIntent().getBooleanExtra("isHideBottomLayout", false)) {
            findViewById(R.id.rl_bottom).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        picsList = null;
    }

    private void executeAnimation(int selected) {
        if (selected != 0) {
            mTvBadge.setVisibility(View.VISIBLE);
            mIvBadge.setVisibility(View.VISIBLE);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mIvBadge, "scaleX", 0.75f, 1.1f, 0.85f, 1),
                    ObjectAnimator.ofFloat(mIvBadge, "scaleY", 0.75f, 1.1f, 0.85f, 1));
            set.setInterpolator(new DecelerateInterpolator());
            set.setStartDelay(100);
            set.setDuration(700).start();
            mTvBadge.setText(String.valueOf(selected));
            //mBtnOk.setEnabled(true);
        } else {
            mTvBadge.setVisibility(View.GONE);
            mIvBadge.setVisibility(View.GONE);
            //mBtnOk.setEnabled(false);
        }
    }

    private class ScreenSlidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            final PhotoDraweeView photoView = new PhotoDraweeView(getApplicationContext());
            photoView.setAllowParentInterceptOnEdge(true);
            final String strLargeUrl = mData.get(position).Url;
            final String strSmallUrl = mData.get(position).UrlThumbnail;

            photoView.setPhotoUri(toUri(strLargeUrl), getApplicationContext(), new PhotoDraweeView.CallBack() {
                @Override
                public void onSuccess(String url, Object... obj) {

                }

                @Override
                public void onFail(String url, Object... obj) {
                    photoView.setPhotoUri(toUri(strSmallUrl));
                }

            });

            container.addView(photoView);
            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            (collection).removeView((View) view);
        }
    }

}
