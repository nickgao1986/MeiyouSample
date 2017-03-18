package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * 一键置顶的view
 * Created by wuminjian on 16/3/19.
 */
public class AKeyTopView implements View.OnClickListener {
    private final String TAG = "AKeyTopView";
    private Context context;

    public RelativeLayout rlDeformationAKeyTop, rl_base_akeytop;
    public LinearLayout llCircularAKeyTop;
    public TextView tvAkeyTop;

    private int moveAllWidth;//移动的总长
    private int startWidth;//开始位置
    private boolean isLeftAnimation;
    private boolean isAnimationFinsh;
    private int time = 3;
    private int TIME_FINISH = 1000;//时间到了
    private OnAKeyTopClickListener listener;
    private Timer animationTimer;
    private boolean topScoll, stopScoll;//是否上滑,停止了滑动
    private RelativeLayout llManBan;
    private ImageView ivHint;
    private boolean isShowAkeyTop;//是否显示控件
    private RelativeLayout mMsgBoxRelativeLayout;

    public AKeyTopView(Context context, RelativeLayout llManBan, RelativeLayout mMsgBoxRelativeLayout) {
        this.mMsgBoxRelativeLayout = mMsgBoxRelativeLayout;
        this.context = context;
        this.llManBan = llManBan;
        llManBan.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    public void init(View view) {
        moveAllWidth = DeviceUtils.dip2px(context, 135);
        startWidth = DeviceUtils.dip2px(context, 50);
        rl_base_akeytop = (RelativeLayout) view.findViewById(R.id.rl_base_akeytop);
        rl_base_akeytop.setVisibility(View.GONE);
        llCircularAKeyTop = (LinearLayout) view.findViewById(R.id.llCircularAKeyTop);
        rlDeformationAKeyTop = (RelativeLayout) view.findViewById(R.id.rlDeformationAKeyTop);
        tvAkeyTop = (TextView) view.findViewById(R.id.tvAkeyTop);
        ivHint = (ImageView) view.findViewById(R.id.ivHint);
        setLisener();
    }

    private void setLisener() {
        llCircularAKeyTop.setOnClickListener(this);
        rlDeformationAKeyTop.setOnClickListener(this);
        llManBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickMenBan();
            }
        });
    }

    /**
     * 处理点击蒙版事件
     */
    public void handleClickMenBan() {
        if (isShowHint()) {//如果第一次有弹泡的话先隐藏弹泡在进行到即时
            handleRetractAnimation();//点击萌版之后缩回动画
        } else {
            ivHint.setVisibility(View.GONE);
            handleTimer();
        }
    }

    /**
     * 点击一键置顶的动画
     */
    private void handleClickAnimation() {
        //先取消原有的定时器
        if (animationTimer != null) {
            animationTimer.cancel();
        }
        time = 3;
        tvAkeyTop.setText("回到顶部");
        rlDeformationAKeyTop.setVisibility(View.VISIBLE);
        //先缩小
        final float mScale = 0.75f;
        final ValueAnimator valueAnimatorSmall = ValueAnimator.ofFloat(1.0f, mScale);
        valueAnimatorSmall.setDuration(160);
        valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    llCircularAKeyTop.setScaleX(scale);
                    llCircularAKeyTop.setScaleY(scale);
                }
                if (scale == mScale) {
                    valueAnimatorSmall.removeUpdateListener(this);
                    llCircularAKeyTop.setVisibility(View.INVISIBLE);
                    handleLeftRightAnimation();
                }
            }
        });
        valueAnimatorSmall.start();
    }

    /**
     * 向左向右移动动画
     */
    private void handleLeftRightAnimation() {
        if (isAnimationFinsh)
            return;
        isAnimationFinsh = true;
        tvAkeyTop.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            rlDeformationAKeyTop.setAlpha(0.6f);
        }
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlDeformationAKeyTop.getLayoutParams();
        final ValueAnimator animator;
        if (isLeftAnimation) {
            ivHint.setVisibility(View.GONE);
            animator = ValueAnimator.ofInt(moveAllWidth, startWidth);
        } else {
            animator = ValueAnimator.ofInt(startWidth, moveAllWidth);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                try {
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    layoutParams.width = value;
                    rlDeformationAKeyTop.requestLayout();
                    if (isLeftAnimation) {
                        if (value == startWidth) {
                            saveShowHint();
                            llManBan.setVisibility(View.GONE);
                            rlDeformationAKeyTop.setVisibility(View.INVISIBLE);
                            llCircularAKeyTop.setVisibility(View.VISIBLE);
                            tvAkeyTop.setVisibility(View.VISIBLE);

                            layoutParams.width = 0;
                            rlDeformationAKeyTop.requestLayout();

                            handleBigSmallAnimation();
                            isAnimationFinsh = false;
                            animator.removeUpdateListener(this);
                            isLeftAnimation = !isLeftAnimation;
                        }
                    } else {
                        if (value == moveAllWidth) {
                            llManBan.setVisibility(View.VISIBLE);
                            tvAkeyTop.setVisibility(View.VISIBLE);

                            if (!isShowHint()) {
                                ivHint.setVisibility(View.VISIBLE);
                            } else {
                                handleTimer();
                            }

                            isAnimationFinsh = false;
                            animator.removeUpdateListener(this);
                            isLeftAnimation = !isLeftAnimation;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        animator.setDuration(240);
        animator.start();
    }

    /**
     * 定时器 1秒执行一次
     */
    private void handleTimer() {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time == 1) {
                    Message message = new Message();
                    message.what = TIME_FINISH;
                    handler.sendMessage(message);
                    animationTimer.cancel();
                    time = 3;
                } else {
                    --time;
                }
            }
        }, 1000, 1000);
    }

    private Handler handler = new Handler() {
        // 定义处理信息的方法
        public void handleMessage(Message msg) {
            if (msg.what == TIME_FINISH) {
                handleRetractAnimation();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 先放大后缩小的动画
     */
    private void handleBigSmallAnimation() {
        AnimatorSet scaleSet = new AnimatorSet();
        final ValueAnimator valueAnimatorSmall = ValueAnimator.ofFloat(1.1f, 1.0f);
        valueAnimatorSmall.setDuration(80);

        final ValueAnimator valueAnimatorLarge = ValueAnimator.ofFloat(0.75f, 1.1f);
        valueAnimatorLarge.setDuration(120);

        valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    llCircularAKeyTop.setScaleX(scale);
                    llCircularAKeyTop.setScaleY(scale);
                }
                if (scale == 1.0f) {
                    if (animationTimer != null) {
                        animationTimer.cancel();
                    }
                    if (isShowAkeyTop) {
                        isShowAkeyTop = false;
                        hideAkeyTopView();
                        if (mMsgBoxRelativeLayout != null) {
                            mMsgBoxRelativeLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    valueAnimatorSmall.removeUpdateListener(this);
                }
            }
        });
        valueAnimatorLarge.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    llCircularAKeyTop.setScaleX(scale);
                    llCircularAKeyTop.setScaleY(scale);
                }
                if (scale == 1.1f) {
                    valueAnimatorLarge.removeUpdateListener(this);
                }
            }
        });

        scaleSet.play(valueAnimatorLarge).before(valueAnimatorSmall);
        scaleSet.start();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llCircularAKeyTop) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (llCircularAKeyTop.getAlpha() == 0.4f)
                    return;
            }
            youMentPost(1);
            handleClickAnimation();
        } else if (i == R.id.rlDeformationAKeyTop) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                rlDeformationAKeyTop.setAlpha(1.0f);//点击设置全显示 不透明
            }
            if (listener != null) {
                listener.OnAKeyTopClick();
            }
            ivHint.setVisibility(View.GONE);
            hideAkeyTop();
            //友盟统计事件
            youMentPost(2);
        }
    }

    /**
     * 友盟统计事件
     */
    private void youMentPost(int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("次数", page + "");
    }

    /**
     * 隐藏控件
     */
    public void hideAkeyTop() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLeftAnimation) {
                    rlDeformationAKeyTop.setVisibility(View.INVISIBLE);
                    handleLeftRightAnimation();
                }
            }
        }, 300);
    }

    /**
     * 做完动画在隐藏控件
     */
    public void animationHideAkeyTop(boolean isShowAkeyTop) {
        if (isLeftAnimation) {
            hideMessageBox();
            this.isShowAkeyTop = isShowAkeyTop;
            if (animationTimer != null) {
                animationTimer.cancel();
            }
            handleLeftRightAnimation();
        } else {
            hideAkeyTopView();
        }
    }

    public void setOnAKeyTopLisener(OnAKeyTopClickListener onAKeyTopClickListener) {
        this.listener = onAKeyTopClickListener;
    }

    public void setTopScoll(boolean topScoll) {
        this.topScoll = topScoll;

        if (topScoll) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                llCircularAKeyTop.setAlpha(0.4f);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                llCircularAKeyTop.setAlpha(0.6f);
            }
        }
    }

    /**
     * 执行缩回动画
     */
    private void handleRetractAnimation() {
        if (isLeftAnimation) {
            if (animationTimer != null) {
                animationTimer.cancel();
            }
            handleLeftRightAnimation();
        }
    }

    public void setStopScoll(boolean stopScoll) {
        this.stopScoll = stopScoll;
        if (stopScoll) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                float alpha = llCircularAKeyTop.getAlpha();
                final ValueAnimator valueAnimator = ValueAnimator.ofFloat(alpha, 0.6f);
                valueAnimator.setDuration(200);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float scale = (Float) animation.getAnimatedValue();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            llCircularAKeyTop.setAlpha(scale);
                            if (scale == 0.6f) {
                                valueAnimator.removeUpdateListener(this);
                            }
                        }
                    }
                });
                valueAnimator.start();
            }
        }
    }

    public interface OnAKeyTopClickListener {
        void OnAKeyTopClick();//一键置顶
    }

    public void showAkeyTopView() {
        if (rl_base_akeytop != null) {
            rl_base_akeytop.setVisibility(View.VISIBLE);
        }
    }

    public void hideAkeyTopView() {
        if (rl_base_akeytop != null) {
            rl_base_akeytop.setVisibility(View.GONE);
        }
    }

    public void hideMessageBox() {
        if (mMsgBoxRelativeLayout != null) {
            mMsgBoxRelativeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示图片引导语
     *
     * @return
     */
    private boolean isShowHint() {
        return true;
    }

    /**
     * 保存是否显示图片引导语
     *
     * @return
     */
    private void saveShowHint() {

    }
}
