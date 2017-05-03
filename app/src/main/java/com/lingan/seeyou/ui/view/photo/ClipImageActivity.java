package com.lingan.seeyou.ui.view.photo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.OnSaveBitmapListener;

import activity.LinganActivity;
import fresco.view.AbstractImageLoader;
import fresco.view.ImageLoadParams;
import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;


/**
 * Created by Administrator on 13-12-21.
 */
public class ClipImageActivity extends LinganActivity {

    private BizcardImageView photoView;
    private ClipView clipView;
    private TextView btnOK, btnNO;
    private static String mUri;
    private static boolean bShowRetry;
    private static double zoomValue;
    private static long userId;
    private int[] loc = new int[2];

    /**
     * 进入页面
     *
     * @param context
     */
    static void enterActivity(Context context, String uri,
                              boolean bShowRetry, double zoomValue, OnClipListener listener, long userId) {
        Intent intent = new Intent(context, ClipImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ClipImageActivity.mUri = uri;
        ClipImageActivity.bShowRetry = bShowRetry;
        ClipImageActivity.clipListener = listener;
        ClipImageActivity.zoomValue = zoomValue;
        ClipImageActivity.userId = userId;
    }

    private static OnClipListener clipListener;

    public interface OnClipListener {
        // 裁剪结果
        public void onClipResult(String uri);

        // 重拍
        public void onReCamera();

        // 取消操作
        public void onCancle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clip_image_new);
        titleBarCommon.setTitle("移动和裁剪");
        initUI();
        fillResource();
        initLogic();
    }


    @SuppressLint("ResourceAsColor")
    private void fillResource() {
        try {
            ((TextView) findViewById(R.id.btnNO)).setTextColor(getResources().getColor(R.color.red_b));
            ((TextView) findViewById(R.id.btnOK)).setTextColor(getResources().getColor(R.color.red_b));
            findViewById(R.id.rl_layout).setBackgroundResource(R.drawable.apk_all_white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        photoView = (BizcardImageView) findViewById(R.id.photoView);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            photoView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        clipView = (ClipView) findViewById(R.id.clipView);
        clipView.setZoomValue(zoomValue);
        btnOK = (TextView) findViewById(R.id.btnOK);
        btnNO = (TextView) findViewById(R.id.btnNO);
        if (bShowRetry) {
            btnNO.setVisibility(View.VISIBLE);
        } else {
            btnNO.setVisibility(View.GONE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clipView.getLocationInWindow(loc);
            }
        }, 200);
        setListener();

    }

    private boolean isSucess = false;

    // 设置监听
    private void setListener() {
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSucess) {
                    getBitmap(new onBitmapListenr() {
                        @Override
                        public void onResult(String uri) {
                            isSucess = true;
                            if (uri != null) {
                                if (clipListener != null) {
                                    clipListener.onClipResult(uri);
                                }
                            } else {
                                ToastUtils.showToast(ClipImageActivity.this, "头像截取不成功");
                            }
                            finish();
                        }
                    });
                }
            }
        });
        btnNO.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clipListener != null) {
                    clipListener.onReCamera();
                }
                finish();
            }
        });

        titleBarCommon.setLeftButtonListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // To change body of implemented methods use File | Settings |
                // File Templates.
                handleFinish();
            }
        });
    }

    private void initLogic() {
        try {
            ImageLoader.getInstance().loadImage(this, mUri, new ImageLoadParams(), new AbstractImageLoader.onCallBack() {
                @Override
                public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
                    if (bitmap != null) {
                        photoView.setImageBitmapss(bitmap, photoView.getWidth(), photoView.getHeight());
                    }
                }

                @Override
                public void onFail(String url, Object... obj) {

                }

                @Override
                public void onProgress(int total, int progess) {

                }

                @Override
                public void onExtend(Object... object) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSucess = false;
        if (finalBitmap != null) {
            finalBitmap.recycle();
        }
    }

    private void handleFinish() {
        finish();
        if (clipListener != null) {
            clipListener.onCancle();
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); //To change body of overridden methods use
        // File | Settings | File Templates.
        handleFinish();
    }

    private Bitmap finalBitmap;

    /* 获取矩形区域内的截图 */
    private void getBitmap(final onBitmapListenr listener) {

        try {
            getBarHeight();

            Bitmap screenShoot = takeScreenShot();

            int width = clipView.getWidth();
            int height = clipView.getHeight();

            int viewWidth = (int) (width * zoomValue);
            int topY = (height - viewWidth) / 2;
            int leftX = (width - viewWidth) / 2;
            try {
                finalBitmap = Bitmap.createBitmap(screenShoot, leftX, topY
                        + loc[1], viewWidth, viewWidth);
            } catch (OutOfMemoryError e) {
                while (finalBitmap == null) {
                    System.gc();
                    System.runFinalization();
                    finalBitmap = Bitmap.createBitmap(screenShoot, leftX, topY
                            + loc[1], viewWidth, viewWidth);
                }
            }
            // 缩放宽高，文件大小不改变，等到保存的时候再去改变文件大小；
            final String filename = ImageUploaderUtil.createSaveFileName(finalBitmap,userId);
            // 保存，最大宽度为20；
            PhotoController.getInstance(getApplicationContext()).saveBitmap2FileInThread(this, finalBitmap, filename, new OnSaveBitmapListener() {
                @Override
                public void OnSaveResult(boolean success, String strSaveFileName) {
                    try {
                        finalBitmap.recycle();
                        System.gc();
                        finalBitmap = null;
                        if (!success) {
                            if (listener != null)
                                listener.onResult(null);
                            ToastUtils.showToast(ClipImageActivity.this,
                                    "图片获取失败 1005");
                        } else {
                            //LogUtils.d("ClipImageActivity", "裁剪图片成功");
                            final String pathname = ImageUploaderUtil.getCompressPhotoPath(getApplicationContext(),filename);
                            if (listener != null)
                                listener.onResult(pathname);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface onBitmapListenr {
        public void onResult(String uri);
    }

    private void getBarHeight() {
        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    }

    // 获取Activity的截屏
    private Bitmap takeScreenShot() {
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        return view.getDrawingCache();
    }

}
