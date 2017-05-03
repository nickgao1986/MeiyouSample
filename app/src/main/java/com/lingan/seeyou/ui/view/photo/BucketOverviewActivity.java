//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lingan.seeyou.ui.view.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CheckableView;
import com.lingan.seeyou.ui.view.photo.model.BucketModel;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;
import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import fresco.view.ImageLoadParams;
import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;
import nickgao.com.meiyousample.utils.DeviceUtils;

public class BucketOverviewActivity extends BasePhotoActivity {
    private TextView btnOk;
    private Button btnReview;
    private GridView gridView;
    private BucketModel mData;
    private long userId;
    private GridViewAdapter mAdapter;
    private TextView mTvState;
    private int mViewSize;
    private RelativeLayout mRlBadge;
    private TextView mTvBadge;
    private ImageView mIvBadge;

    private String mRedBColorString;

    public BucketOverviewActivity() {
    }

    public void onBackPressed() {
        this.handleFinish();
    }

    private void handleFinish() {
        this.startActivity(new Intent(this.getApplicationContext(), BucketActivity.class));
        this.finish();
    }

    protected void onResume() {
        super.onResume();
        if (this.mData.Id == PhotoController.RECENT_BUCKET_ID) {
            this.mAdapter = new GridViewAdapter(PhotoController.getInstance(this).getRecentPhotos());
            this.gridView.setAdapter(this.mAdapter);
            this.resetOkButton();
        } else {
            this.mAdapter = new GridViewAdapter(PhotoController.getInstance(this).getPhotosById(mData.Id));
            this.gridView.setAdapter(this.mAdapter);
            this.resetOkButton();
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid_activity_new);

        int redColor = SkinManager.getInstance().getAdapterColor(R.color.red_b);
        mRedBColorString = "#" + (Integer.toHexString(redColor)).substring(2);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        this.mViewSize = display.getWidth() / 4;
        this.mData = (BucketModel) this.getIntent().getExtras().getSerializable("Data");
        this.userId = getIntent().getLongExtra("userid", 0);
        titleBarCommon.setTitle(this.mData.Name).setLeftButtonListener(new OnClickListener() {
            public void onClick(View v) {
                BucketOverviewActivity.this.handleFinish();
            }
        }).setRightTextViewString(R.string.cancel).setRightTextViewListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoController.getInstance(BucketOverviewActivity.this.getApplicationContext()).finishPicking(true);
            }
        });
        this.gridView = (GridView) this.findViewById(R.id.gv);
        this.btnOk = (TextView) this.findViewById(R.id.btnOk);
        this.btnReview = (Button) this.findViewById(R.id.btnReview);
        this.mTvState = (TextView) this.findViewById(R.id.tvState);
        this.mRlBadge = (RelativeLayout) this.findViewById(R.id.rlBadge);
        this.mTvBadge = (TextView) this.findViewById(R.id.tvBadge);
        this.mIvBadge = (ImageView) this.findViewById(R.id.ivBadge);
        this.btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoController.getInstance(BucketOverviewActivity.this).getSelectedItems().size() > 0) {
                    PhotoController.getInstance(BucketOverviewActivity.this).finishPicking(false);
                }

            }
        });
        this.btnReview.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoController.getInstance(BucketOverviewActivity.this.getApplicationContext()).getSelectedItems().size() > 0) {
                    Intent intent = new Intent(BucketOverviewActivity.this, ReviewActivity.class);
                    intent.putExtra("KEY_MODE", 10001);
                    BucketOverviewActivity.this.startActivity(intent);
                }

            }
        });
        if (this.mData.Id == PhotoController.RECENT_BUCKET_ID) {
            this.mAdapter = new GridViewAdapter(PhotoController.getInstance(this).getRecentPhotos());

        } else {
            this.mAdapter = new GridViewAdapter(PhotoController.getInstance(this).getPhotosById(this.mData.Id));
        }

        if (PhotoController.getInstance(this.getApplicationContext()).isSingleCropableMode()) {
            this.mTvState.setVisibility(View.GONE);
            this.btnReview.setVisibility(View.GONE);
        } else {
            this.mTvState.setVisibility(View.VISIBLE);
            this.btnReview.setVisibility(View.VISIBLE);
        }

        this.gridView.setAdapter(this.mAdapter);
        this.resetOkButton();
        this.fillResource();
    }

    @SuppressLint({"ResourceAsColor"})
    private void fillResource() {
        try {
            ((Button) findViewById(R.id.btnReview)).setTextColor(getResources().getColorStateList(R.color.btn_red_to_white_color_selector));
            ((Button) findViewById(R.id.btnOk)).setTextColor(getResources().getColor(R.color.white_a));
            ((TextView) findViewById(R.id.tvBadge)).setTextColor(getResources().getColor(R.color.white_a));
            findViewById(R.id.root_view_gallery).setBackgroundResource(R.drawable.bottom_bg);
        } catch (Exception var2) {
        }

    }

    private void resetOkButton() {
        List<PhotoModel> mSelected = PhotoController.getInstance(this.getApplicationContext()).getSelectedItems();
        int selected = mSelected == null ? 0 : mSelected.size();
        if (selected != 0) {
            this.mTvBadge.setVisibility(View.VISIBLE);
            this.mIvBadge.setVisibility(View.VISIBLE);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mIvBadge, "scaleX", new float[]{0.75F, 1.1F, 0.85F, 1.0F}), ObjectAnimator.ofFloat(this.mIvBadge, "scaleY", new float[]{0.75F, 1.1F, 0.85F, 1.0F})});
            set.setInterpolator(new DecelerateInterpolator());
            set.setStartDelay(100L);
            set.setDuration(700L).start();
            this.mTvBadge.setText(String.valueOf(selected));
            this.btnOk.setEnabled(true);
            this.btnReview.setEnabled(true);
        } else {
            this.mTvBadge.setVisibility(View.GONE);
            this.mIvBadge.setVisibility(View.GONE);
            this.btnOk.setEnabled(false);
            this.btnReview.setEnabled(false);
        }

        this.mTvState.setText(Html.fromHtml("最多" + PhotoController.getInstance(this.getApplicationContext()).getMaxSelect() + "张，已选<font color=\'" + mRedBColorString + "\'>" + selected + "</font>张"));
    }

    private class GridViewAdapter extends BaseAdapter {
        private List<PhotoModel> mDataSet;
        private int mImageWidthHeight;

        public GridViewAdapter(List<PhotoModel> dataSet) {
            this.mDataSet = dataSet;
            this.mImageWidthHeight = (DeviceUtils.getScreenWidth(BucketOverviewActivity.this.getApplicationContext()) - 3 * DeviceUtils.dip2px(BucketOverviewActivity.this.getApplicationContext(), 2.0F)) / 4;
        }

        public int getCount() {
            return null != this.mDataSet ? this.mDataSet.size() : 0;
        }

        public Object getItem(int position) {
            return null != this.mDataSet ? mDataSet.get(position) : null;
        }

        public long getItemId(int position) {
            return 0L;
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = BucketOverviewActivity.this.getLayoutInflater().inflate(R.layout.cp_photo_gv_item_new, parent, false);
                viewHolder.initHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final PhotoModel dataObj = mDataSet.get(position);
            final ImageLoadParams params = new ImageLoadParams();
            params.height = mViewSize;
            params.width = mViewSize;
            params.anim = false;
            params.bgholder = R.color.black_f;
            if (null != dataObj.UrlThumbnail) {
                ImageLoader.getInstance().displayImage(getApplicationContext(), viewHolder.checkableView, dataObj.UrlThumbnail, params, null);
            } else {
                ImageLoader.getInstance().displayImage(getApplicationContext(), viewHolder.checkableView, dataObj.Url, params, null);
            }
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.imageView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case 1:
                            if (!PhotoController.getInstance(BucketOverviewActivity.this).isItemSelected(dataObj)
                                    && PhotoController.getInstance(BucketOverviewActivity.this).reachMaximum()) {
                                ToastUtils.showToast(BucketOverviewActivity.this,
                                        "你最多只能选择" + PhotoController.getInstance(getApplicationContext()).getMaxSelect() + "张照片哦~");
                                return true;
                            }
                        default:
                            return !PhotoController.getInstance(BucketOverviewActivity.this.getApplicationContext()).isItemSelected(dataObj) && PhotoController.getInstance(BucketOverviewActivity.this.getApplicationContext()).reachMaximum();
                    }
                }
            });
            if (PhotoController.getInstance(BucketOverviewActivity.this).isSingleCropableMode()) {
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.checkableView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {

                        ClipImageActivity.enterActivity(BucketOverviewActivity.this, dataObj.Url, false, 0.9D, new ClipImageActivity.OnClipListener() {
                            public void onClipResult(String uri) {
                                if (uri != null) {
                                    PhotoModel photoModel = new PhotoModel();
                                    photoModel.Id = System.currentTimeMillis();
                                    photoModel.Url = uri;
                                    photoModel.UrlThumbnail = uri;
                                    PhotoController.getInstance(BucketOverviewActivity.this).clearSelection();
                                    PhotoController.getInstance(BucketOverviewActivity.this).addNewPhoto(photoModel);
                                    PhotoController.getInstance(BucketOverviewActivity.this).finishPicking(false);
                                }

                            }

                            public void onCancle() {
                            }

                            public void onReCamera() {
                            }
                        }, userId);
                    }
                });
            } else {
                viewHolder.imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (!finalViewHolder.checkableView.getChecked()) {
                            finalViewHolder.imageView.setImageResource(R.drawable.apk_image_chose_bg_s);
                        } else {
                            finalViewHolder.imageView.setImageResource(R.drawable.apk_image_chose_s);
                        }

                        finalViewHolder.checkableView.setChecked(!finalViewHolder.checkableView.getChecked());
                        PhotoController.getInstance(BucketOverviewActivity.this).selectItem(dataObj);
                        BucketOverviewActivity.this.resetOkButton();
                    }
                });
                viewHolder.checkableView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(BucketOverviewActivity.this, ReviewActivity.class);
                        intent.putExtra("KEY_MODE", 10002);
                        intent.putExtra("KEY_BUCKET_ID", BucketOverviewActivity.this.mData.Id);
                        intent.putExtra("KEY_POSITION", position);
                        BucketOverviewActivity.this.startActivity(intent);
                    }
                });
            }

            if (PhotoController.getInstance(BucketOverviewActivity.this).isItemSelected(dataObj)) {
                viewHolder.checkableView.setCheckedWithoutNotify(true);
                viewHolder.imageView.setImageResource(R.drawable.apk_image_chose_bg_s);
            } else {
                viewHolder.checkableView.setCheckedWithoutNotify(false);
                viewHolder.imageView.setImageResource(R.drawable.apk_image_chose_s);
            }

            return convertView;
        }

        public class ViewHolder {
            public CheckableView checkableView;
            public ImageView imageView;

            public ViewHolder() {
            }

            public void initHolder(View convertView) {
                this.imageView = (ImageView) convertView.findViewById(R.id.iv);
                this.checkableView = (CheckableView) convertView.findViewById(R.id.chk);
                LayoutParams params = (LayoutParams) this.checkableView.getLayoutParams();
                params.width = GridViewAdapter.this.mImageWidthHeight;
                params.height = GridViewAdapter.this.mImageWidthHeight;
                this.checkableView.requestLayout();
            }
        }
    }
}
