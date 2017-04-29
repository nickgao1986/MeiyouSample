package com.lingan.seeyou.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * 加载视图 有三种状态，加载中，无数据，无网络 Created by Administrator on 13-8-7.
 */
public class LoadingView extends LinearLayout {

    private Context context;
    private TextView tvResultContent;
    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;
    //private AnimationDrawable frameAnimation;

    private Integer[] imageResIds = new Integer[16];//预先设置大小
    private StringRes[] resultContentIds = new StringRes[16];
    private Integer[] loadTextIds = new Integer[16];
    private OnClickListener listener;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        // 加入小柚图标
        mImageView = new ImageView(context);
        mImageView.setVisibility(View.GONE);
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER_HORIZONTAL;
        mImageView.setBackgroundResource(R.drawable.loading_frame);
        addView(mImageView, param);

        //frameAnimation = (AnimationDrawable) mImageView.getBackground();

        tvResultContent = new TextView(context);
        //tvResultContent.setBackgroundResource(R.drawable.apk_girlword_bg);
//        tvResultContent.setTextColor(getResources().getColor(R.color.black_a));
        SkinManager.getInstance().setTextColor(tvResultContent, R.color.black_b);
//        tvResultContent.setTextColor(getResources().getColor(R.color.black_b));
        tvResultContent.setPadding(10, 10, 10, 10);
        tvResultContent.setTextSize(14);
        tvResultContent.setGravity(Gravity.CENTER);
        LayoutParams param0 = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        param0.gravity = Gravity.CENTER;
        addView(tvResultContent, param0);

        // 加入文字
        mTextView = new TextView(context);
        mTextView.setText(getResources().getString(R.string.loading));
//        mTextView.setTextColor(getResources().getColor(R.color.black_b));
        SkinManager.getInstance().setTextColor(mTextView, R.color.black_b);
        mTextView.setTextSize(14);
//        mTextView.setTextColor(getResources().getColor(R.color.black_b));
//        mTextView.setTextSize(16);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setVisibility(View.INVISIBLE);
        param.topMargin = 3;
        addView(mTextView, param);

        mButton = new Button(context);
        mButton.setText(getResources().getString(R.string.favorites_shop_no_data));
//        mButton.setBackgroundResource(R.drawable.btn_red_selector);
        SkinManager.getInstance().setDrawableBackground(mButton, R.drawable.btn_red_selector);
        mButton.setPadding(DeviceUtils.dip2px(context, 20), DeviceUtils.dip2px(context, 10), DeviceUtils.dip2px(context, 20), DeviceUtils.dip2px(context, 10));
        mButton.setTextSize(16);
//        mButton.setTextColor(getResources().getColor(R.color.white_a));
        SkinManager.getInstance().setTextColor(mButton, R.color.white_a);
        mButton.setGravity(Gravity.CENTER);
        LayoutParams param_button = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        param_button.topMargin = DeviceUtils.dip2px(context, 20);
        addView(mButton, param_button);
        mButton.setVisibility(View.GONE);
        initNormalStatus();
        setListener();
    }

    private void setListener() {
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        tvResultContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }



    private void initNormalStatus() {

        for (int i = 0; i < loadTextIds.length; ++i) {
            loadTextIds[i] = 0;
        }
        for (int i = 0; i < resultContentIds.length; ++i) {
            resultContentIds[i] = new StringRes(StringRes.TYPE_RES, null, 0);
        }
        for (int i = 0; i < imageResIds.length; ++i) {
            imageResIds[i] = 0;
        }

        loadTextIds[1] = R.string.loading;

        resultContentIds[2] = new StringRes(StringRes.TYPE_RES, null, R.string.no_record);// 与 imageResIds[2]对应
        resultContentIds[3] = new StringRes(StringRes.TYPE_RES, null, R.string.no_internet_for_loading);

        imageResIds[1] = R.drawable.loading_frame;
        imageResIds[2] = R.drawable.apk_girlone;     //产品需求
        imageResIds[3] = R.drawable.apk_girlone;
        imageResIds[4] = R.drawable.apk_girltwo;
    }


    // 加载中
    public static final int STATUS_LOADING = 111101;
    // 无数据
    public static final int STATUS_NODATA = 20200001;
    // 小提示
    public static final int STATUS_TIP = 40400001;
    // 无网络
    public static final int STATUS_NONETWORK = 30300001;

    //底部有个按钮
    public static final int TYPE_BUTTON_HINT = 7070001;

    public static final int STATUS_HIDDEN = 0;
    private int status;

    public int getStatus() {
        return status;
    }

    public int setContent(Activity activity, int originStatus, String value) {
        return setContent(originStatus, value);
    }

    public int setContent(int originStatus, String value) {
        int i = 3;
        for (; i < resultContentIds.length; ++i) {
            if (resultContentIds[i].getrValue() == 0
                    &&
                    (StringUtils.isEmpty(resultContentIds[i].getValue())
                            || StringUtils.equals(resultContentIds[i].getValue(), value))) {
                break;
            }
        }
        if (i < resultContentIds.length) {
            resultContentIds[i].setValue(value);
        }
        int newStatus = i * 10000000 + originStatus % 10000000;
        setStatus(newStatus);
        return newStatus;
    }

    public int setContent(int originStatus, int stringId) {
        int i = 3;
        for (; i < resultContentIds.length; ++i) {
            if (StringUtils.isEmpty(resultContentIds[i].getValue())
                    &&
                    (resultContentIds[i].getrValue() == stringId
                            || resultContentIds[i].getrValue() == 0)) {
                break;
            }

        }
        if (i < resultContentIds.length) {
            resultContentIds[i].setrValue(stringId);
        }
        int newStatus = i * 10000000 + originStatus % 10000000;
        setStatus(newStatus);
        return newStatus;
    }

    public void setStatus(int status) {//20200000
        this.status = status;
        int result = status % 100000000 / 10000000;
        int imag = status % 1000000 / 100000;
        final int animation = status % 10000 / 1000;
        int text = status % 1000 / 100;   //
        int button = status % 100 / 10;   //
        int visible = status % 10;      //0 or 1
        if (visible == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }


        if (imag > imageResIds.length) {
            LogUtils.e("imag index  > imageResIds.length!!!!!!!!");
            imag = 1;
        }
        if (imag == 0) {
            mImageView.setVisibility(GONE);
        } else {
            mImageView.setVisibility(VISIBLE);
            mImageView.setBackgroundResource(imageResIds[imag]);
        }

        if (mImageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable frameAnimation = (AnimationDrawable) mImageView.getBackground();
            if (animation == 0) {
                frameAnimation.stop();
            } else {
                frameAnimation.start();
            }
        }

        if (result > resultContentIds.length) {
            result = 1;
        }
        if (result == 0) {
            tvResultContent.setVisibility(GONE);
        } else {
            tvResultContent.setVisibility(VISIBLE);
            tvResultContent.setText(resultContentIds[result].getStringValue(getContext()));
        }

        if (text > loadTextIds.length) {
            text = 1;
            LogUtils.e("result index  > resultContentIds.length!!!!!!!!");
        }
        if (text == 0) {
            mTextView.setVisibility(GONE);
        } else {
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(loadTextIds[text]);
        }

        if (button == 0) {
            mButton.setVisibility(GONE);
        } else {
            mButton.setVisibility(VISIBLE);
        }

    }

    private static class StringRes {
        public static final int TYPE_RES = 0;
        public static final int TYPE_STR = 1;
        private int type;
        private int rValue;
        private String value;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getrValue() {
            return rValue;
        }

        public void setrValue(int rValue) {
            this.rValue = rValue;
            this.type = TYPE_RES;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
            this.type = TYPE_STR;
        }

        public StringRes(int type, String value, int rValue) {
            this.type = type;
            this.value = value;
            this.rValue = rValue;
        }

        public String getStringValue(Context context) {
            switch (type) {
                case TYPE_RES:
                    return context.getResources().getString(rValue);
                case TYPE_STR:
                    return value;
                default:
                    return context.getResources().getString(rValue);
            }
        }
    }

    public Button getmButton() {
        return mButton;
    }

    @Deprecated
    public void setStatus(Activity activity, int status) {
        setStatus(status);
    }

    /**
     * 一定要停止动画 不然持续耗费绘制时间
     */
    public void hide() {
        if (mImageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable frameAnimation = (AnimationDrawable) mImageView.getBackground();
            frameAnimation.stop();
        }
        //调整顺序， 防止setVisible 由于动画导致隐藏慢；
        this.clearAnimation();
        setVisibility(GONE);
    }

    /**
     * 设置颜色
     */
    public void setTextViewColor(int color) {
        if (mTextView != null) {
            mTextView.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置颜色
     */
    public void setResultContentColor(int color) {
        if (tvResultContent != null) {
            tvResultContent.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置换肤的颜色
     */
    public void setResultSkinContentColor(int color) {
        if (tvResultContent != null) {
            tvResultContent.setTextColor(color);
        }
    }
}
