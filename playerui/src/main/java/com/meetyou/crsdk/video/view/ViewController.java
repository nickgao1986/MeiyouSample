package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meetyou.crsdk.util.DeviceUtils;
import com.meetyou.crsdk.util.ImageLoader;
import com.meetyou.crsdk.util.ImageSize;
import com.meetyou.crsdk.util.UrlUtil;
import com.meetyou.crsdk.video.core.JCMediaManager;
import com.meetyou.crsdk.video.core.JCUtils;
import com.meetyou.crsdk.video.core.ViewListener;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtil;
import nickgao.com.okhttpexample.view.LoaderImageView;
import nickgao.com.playerui.R;



/**
 * Author: lwh
 * Date: 5/27/16 15:58.
 */
public class ViewController implements View.OnClickListener {
    private static final String TAG = "ViewController";

    private final int HANDLE_HIDE_CONTROLER_BAR = 1002;
    private Context mContext;
    private View mView;
    private int mCurrentStatus;             // 当前播放状态

    private RelativeLayout mRlTextureView;  //视频控件容器
    private JCMediaPlayerTextureView mTextureView;       //视频播放
    private LoaderImageView ivFrontView;    //视频首帧图片
    private ProgressBar pbLoading;          //下载中进度圈

    private RelativeLayout rlMengban;       //蒙版层
    private LinearLayout llFinishContent;   //结束文案
    private TextView tvFinishContent;       //结束文案
    private TextView tvFinishCenterContent; //中间的结束文案
    private RelativeLayout llReplay;        //重播布局
    private TextView tvReplayHint;          //重播提示语
    private ImageView ivReplayBig;          //中间重播按钮
    private ImageView ivBottomReplay;       //底部的重播按钮
    private ImageView ivBottomFullScreen;   //底部的全屏按钮

    private RelativeLayout rlTitle;         //标题栏
    private ImageView ivBack;               //返回按钮
    private TextView tvTitle;               //标题内容

    private ImageView ivPause;              //暂停按钮
    private ImageView ivPlay;               //播放按钮
    private TextView tvTotalTime;           //总时长

    private ProgressBar pbBottom;           //底部进度条
    private LinearLayout linearBottom;      //控制条视图
    private TextView tvCurrentTime;         //当前时间
    private TextView tvLeftTime;            //剩余时间
    private SeekBar mSeekBar;               //进度条
    private LinearLayout rlFullScreen;         //全屏按钮
    private LinearLayout rlReduceScreen;       //退出全屏按钮

    //网络提示
    private LinearLayout llNetMengban;
    private LinearLayout llNetHintMsg;
    private TextView tvNetHintTime;
    private TextView tvNetHintSize;
    private TextView tvNetPlay;

    protected VideoViewInfo mVideoViewInfo; //视频视图显示信息
    protected VideoViewSetInfo mVideoViewSetInfo; //视频视图控制信息

    private ViewListener mViewListener;     //视图回调

    private int mVideoType;                 //视频类型
    private boolean mIsNeedFinishContent;   //是否需要播放结束文案
    private boolean mIsEnableControlBar;    //是否进度条和暂停可用

    private Handler mTimeHandler;

    private boolean mIsSeekTouch;           //是否滚动条被按下

    /**
     * 视频界面回调
     */
    private OnVideoListener mOnVideoListener;

    public ViewController(Context context, View view) {
        mContext = context;
        mView = view;
        initUI();
    }

    private void initUI() {
        mRlTextureView = (RelativeLayout) mView.findViewById(R.id.rl_video_textureview);
        mTextureView = (JCMediaPlayerTextureView) mView.findViewById(R.id.video_textureview);
        ivFrontView = (nickgao.com.okhttpexample.view.LoaderImageView) mView.findViewById(R.id.iv_front_cover);
        pbLoading = (ProgressBar) mView.findViewById(R.id.pb_loading);

        rlMengban = (RelativeLayout) mView.findViewById(R.id.rl_mengban);
        llFinishContent = (LinearLayout) mView.findViewById(R.id.ll_finish_content);
        tvFinishContent = (TextView) mView.findViewById(R.id.tv_finish_content);
        tvFinishCenterContent = (TextView) mView.findViewById(R.id.tv_finish_center_content);
        llReplay = (RelativeLayout) mView.findViewById(R.id.linear_replay);
        tvReplayHint = (TextView) mView.findViewById(R.id.tv_replay_hint);
        ivReplayBig = (ImageView) mView.findViewById(R.id.iv_replay_big);
        ivBottomReplay = (ImageView) mView.findViewById(R.id.iv_bottom_replay);
        ivBottomFullScreen = (ImageView) mView.findViewById(R.id.iv_bottom_fullscreen);

        rlTitle = (RelativeLayout) mView.findViewById(R.id.rl_title);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        ivBack = (ImageView) mView.findViewById(R.id.iv_back);

        pbBottom = (ProgressBar) mView.findViewById(R.id.pb_video_play_progress);
        linearBottom = (LinearLayout) mView.findViewById(R.id.linear_bottom);
        tvCurrentTime = (TextView) mView.findViewById(R.id.tv_current_time);
        tvLeftTime = (TextView) mView.findViewById(R.id.tv_left_time);
        mSeekBar = (SeekBar) mView.findViewById(R.id.seekbar);
        rlFullScreen = (LinearLayout) mView.findViewById(R.id.ll_control_bar_fullscreen);
        rlReduceScreen = (LinearLayout) mView.findViewById(R.id.ll_control_bar_reducescreen);

        ivPause = (ImageView) mView.findViewById(R.id.iv_pause);
        ivPlay = (ImageView) mView.findViewById(R.id.iv_play);
        tvTotalTime = (TextView) mView.findViewById(R.id.tv_total_time_hint);

        //网络提示
        llNetMengban = (LinearLayout) mView.findViewById(R.id.ll_net_mengban);
        llNetHintMsg = (LinearLayout) mView.findViewById(R.id.ll_net_hint_msg);
        tvNetHintTime = (TextView) mView.findViewById(R.id.tv_net_time_msg);
        tvNetHintSize = (TextView) mView.findViewById(R.id.tv_net_size_msg);
        tvNetPlay = (TextView) mView.findViewById(R.id.tv_net_continue_play);

        setListener();
    }

    public void resizeVideoView(int videoWidth, int videoHeight) {
        int viewWidth = getViewWidth();
        int viewHeight = getViewHeight();
        if (isFullScreen()) {
            viewWidth = DeviceUtils.getScreenWidth(mContext);
            viewHeight = DeviceUtils.getScreenHeight(mContext);
            if (viewHeight > viewWidth) {
                int temp = viewHeight;
                viewHeight = viewWidth;
                viewWidth = temp;
            }
        }
        if (viewWidth > 0 && viewHeight > 0) {
            float scaleH = ((float) viewWidth) / videoWidth;
            float scaleW = ((float) viewHeight) / videoHeight;
            if (scaleH > 0 && scaleW > 0) {
                ViewGroup.LayoutParams params = mTextureView.getLayoutParams();
                if (scaleH < scaleW) {
                    params.height = viewWidth * videoHeight / videoWidth;
                } else {
                    params.width = viewHeight * videoWidth / videoHeight;
                }
                mTextureView.requestLayout();
            }
        }
    }

    /**
     * 全屏拉伸播放
     */
    public void resizeFullVideoView() {
        //视频数据获取成功的回调,该回调并不属于视频生命周期,会在视频数据刷新的时候也会回调
        ViewGroup.LayoutParams params = mTextureView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mTextureView.requestLayout();
    }

    private void setListener() {
        mTimeHandler = new TimeHandler(mContext.getMainLooper());

        mRlTextureView.setOnClickListener(this);
        ivFrontView.setOnClickListener(this);
        rlMengban.setOnClickListener(this);
        llFinishContent.setOnClickListener(this);
        tvFinishCenterContent.setOnClickListener(this);
        ivReplayBig.setOnClickListener(this);
        ivBottomReplay.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rlFullScreen.setOnClickListener(this);
        rlReduceScreen.setOnClickListener(this);
        ivBottomFullScreen.setOnClickListener(this);
        llNetMengban.setOnClickListener(this);
        tvNetPlay.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                try {
//                    if (fromUser) {
//                        if (mOnVideoListener != null) {
//                            mOnVideoListener.onSeekOver(progress);
//                        }
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnVideoListener != null) {
                    mOnVideoListener.onSeekOver(seekBar.getProgress());
                }
            }
        });
        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsSeekTouch = true;
                        if (mOnVideoListener != null) {
                            mOnVideoListener.onSeekTouchDown();
                        }
                        if (mTimeHandler != null) {
                            mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
                        }
                        ViewParent vpdown = mView.getParent();
                        while (vpdown != null) {
                            vpdown.requestDisallowInterceptTouchEvent(true);
                            vpdown = vpdown.getParent();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mOnVideoListener != null) {
                            mOnVideoListener.onSeekTouchUp();
                        }
                        if (mTimeHandler != null) {
                            mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
                            mTimeHandler.sendEmptyMessageDelayed(HANDLE_HIDE_CONTROLER_BAR, 2500);
                        }
                        ViewParent vpup = mView.getParent();
                        while (vpup != null) {
                            vpup.requestDisallowInterceptTouchEvent(false);
                            vpup = vpup.getParent();
                        }
                        mIsSeekTouch = false;
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
    //    LogUtils.v(TAG, "viewController onClick");
        int id = v.getId();
        if (id == R.id.rl_video_textureview) {
            if (isNeedControlBar() || isFullScreen()) {
                // 显示控制条信息，再点击则隐藏
                if (linearBottom.getVisibility() == View.VISIBLE) {
                    rlTitle.setVisibility(View.GONE);
                    ivPause.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.GONE);
                    linearBottom.setVisibility(View.GONE);
                    pbBottom.setVisibility(View.VISIBLE);
                    if (mTimeHandler != null)
                        mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
                } else {
                    if (isFullScreen()) {
                        rlTitle.setVisibility(View.VISIBLE);
                    } else {
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                    }
                    if (mCurrentStatus == ViewStatus.PLAYING.value()) {
                        if (mIsEnableControlBar) {
                            ivPause.setVisibility(View.VISIBLE);
                        }
                        ivPlay.setVisibility(View.GONE);
                    } else {
                        ivPause.setVisibility(View.GONE);
                        ivPlay.setVisibility(View.VISIBLE);
                    }
                    linearBottom.setVisibility(View.VISIBLE);
                    pbBottom.setVisibility(View.GONE);
                    if (mTimeHandler != null) {
                        mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
                        mTimeHandler.sendEmptyMessageDelayed(HANDLE_HIDE_CONTROLER_BAR, 2500);
                    }
                }
            } else {
                if (getViewListener() != null) {
                    getViewListener().onClickVideoView();
                }
            }
        } else if (id == R.id.iv_play) {
            // 播放
            if (mOnVideoListener != null) {
                mOnVideoListener.onPlayClick(false);
            }
        } else if (id == R.id.iv_pause) {
            // 暂停
            if (mOnVideoListener != null) {
                mOnVideoListener.onPauseClick();
            }
        } else if (id == R.id.iv_replay_big || id == R.id.iv_bottom_replay) {
            if (mOnVideoListener != null) {
                mOnVideoListener.onPlayClick(true);
            }
        } else if (id == R.id.ll_finish_content || id == R.id.tv_finish_center_content) {
            // 结束文案
            if (getViewListener() != null) {
                getViewListener().onClickComplte();
            }
        } else if (id == R.id.iv_back || id == R.id.ll_control_bar_reducescreen) {
            // 返回/退出全屏
            if (getViewListener() != null) {
                getViewListener().onClickBack();
            }
        } else if (id == R.id.ll_control_bar_fullscreen || id == R.id.iv_bottom_fullscreen) {
            // 全屏
            if (getViewListener() != null) {
                getViewListener().onClickFullScreen();
            }
        } else if (id == R.id.iv_front_cover) {
            // 传递出去，资讯帖需要播放，OOXX
            // 播放
            if (mOnVideoListener != null) {
                mOnVideoListener.onFrontIvClick();
            }
        } else if (id == R.id.rl_mengban || id == R.id.ll_net_mengban) {
            // 不响应点击，拦截掉
        } else if (id == R.id.tv_net_continue_play) {
            // 网络状态变化
            if (mOnVideoListener != null) {
                mOnVideoListener.onContinueClick();
            }
        }
    }

    /**
     * 开始播放绘制第一帧
     */
    public void onFirstRenderingStar() {
        ivFrontView.setVisibility(View.GONE);
    }

    /**
     * 设置界面状态
     *
     * @param viewStatus
     */
    public void setViewStatus(ViewStatus viewStatus) {
        try {
            LogUtils.d(TAG, "-->setViewStatus:" + viewStatus.value());
            int oldStatus = mCurrentStatus;
            mCurrentStatus = viewStatus.value();
            int value = viewStatus.value();
            if (value != ViewStatus.PLAYING.value() && mTimeHandler != null) {
                mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
            }
            if (value == ViewStatus.NORAML.value()) {
                ivPause.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                rlMengban.setVisibility(View.GONE);
                llReplay.setVisibility(View.GONE);
                pbBottom.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.GONE);
                tvFinishCenterContent.setVisibility(View.GONE);
                ivBottomFullScreen.setVisibility(View.GONE);
                ivBottomReplay.setVisibility(View.GONE);
                if (mVideoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
                    ivPlay.setVisibility(View.VISIBLE);
                    if (isFullScreen()) {
                        llFinishContent.setVisibility(View.GONE);
                        linearBottom.setVisibility(View.VISIBLE);
                        ivFrontView.setVisibility(View.GONE);
                        tvTotalTime.setVisibility(View.GONE);
                        rlTitle.setVisibility(View.VISIBLE);
                    } else {
                        linearBottom.setVisibility(View.GONE);
                        if (isNeedControlBar()) {
                            if (mIsNeedFinishContent) {
                                llFinishContent.setVisibility(View.VISIBLE);
                            } else {
                                llFinishContent.setVisibility(View.GONE);
                            }
                        } else {
                            llFinishContent.setVisibility(View.GONE);
                        }
                        ivFrontView.setVisibility(View.VISIBLE);
                        if (!StringUtil.isNull(getTotalTimeStr())) {
                            tvTotalTime.setVisibility(View.VISIBLE);
                        } else {
                            tvTotalTime.setVisibility(View.GONE);
                        }
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ivPlay.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.GONE);
                    linearBottom.setVisibility(View.GONE);
                    ivFrontView.setVisibility(View.GONE);
                    tvTotalTime.setVisibility(View.GONE);
                    rlTitle.setVisibility(View.GONE);
                }
            } else if (value == ViewStatus.PREPARE.value()) {
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                tvTotalTime.setVisibility(View.GONE);
                rlMengban.setVisibility(View.GONE);
                llReplay.setVisibility(View.GONE);
                pbBottom.setVisibility(View.GONE);
                linearBottom.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.GONE);
                tvFinishCenterContent.setVisibility(View.GONE);
                ivBottomFullScreen.setVisibility(View.GONE);
                ivBottomReplay.setVisibility(View.GONE);
                if (mVideoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
                    ivFrontView.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.VISIBLE);
                    if (isFullScreen()) {
                        llFinishContent.setVisibility(View.GONE);
                        rlTitle.setVisibility(View.VISIBLE);
                    } else {
                        if (mIsNeedFinishContent && isNeedControlBar()) {
                            llFinishContent.setVisibility(View.VISIBLE);
                        } else {
                            llFinishContent.setVisibility(View.GONE);
                        }
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ivFrontView.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.GONE);
                    rlTitle.setVisibility(View.GONE);
                }
            } else if (value == ViewStatus.PLAYING.value()) {
                //启动刷新进度条
                initProgressTimer();
                if (oldStatus != ViewStatus.PREPARE.value()) {
                    ivFrontView.setVisibility(View.GONE);
                }
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                tvTotalTime.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                rlMengban.setVisibility(View.GONE);
                llReplay.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.GONE);
                tvFinishCenterContent.setVisibility(View.GONE);
                ivBottomReplay.setVisibility(View.GONE);
                if (mVideoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
                    if (isFullScreen()) {
                        llFinishContent.setVisibility(View.GONE);
                        rlTitle.setVisibility(View.VISIBLE);
                        linearBottom.setVisibility(View.VISIBLE);
                        pbBottom.setVisibility(View.GONE);
                    } else {
                        if (mIsNeedFinishContent && isNeedControlBar()) {
                            llFinishContent.setVisibility(View.VISIBLE);
                        } else {
                            llFinishContent.setVisibility(View.GONE);
                        }
                        linearBottom.setVisibility(View.GONE);
                        if (isNeedControlBar()) {
                            pbBottom.setVisibility(View.VISIBLE);
                            ivBottomFullScreen.setVisibility(View.GONE);
                        } else {
                            pbBottom.setVisibility(View.GONE);
                            ivBottomFullScreen.setVisibility(View.VISIBLE);
                        }
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ivBottomReplay.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.GONE);
                    rlTitle.setVisibility(View.GONE);
                    linearBottom.setVisibility(View.GONE);
                    pbBottom.setVisibility(View.GONE);
                    ivBottomFullScreen.setVisibility(View.GONE);
                }
                if (mTimeHandler != null) {
                    mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
                    mTimeHandler.sendEmptyMessageDelayed(HANDLE_HIDE_CONTROLER_BAR, 1500);
                }
            } else if (value == ViewStatus.PAUSE.value()) {
                ivFrontView.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                tvTotalTime.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                rlMengban.setVisibility(View.GONE);
                llReplay.setVisibility(View.GONE);
                tvFinishCenterContent.setVisibility(View.GONE);
                ivBottomReplay.setVisibility(View.GONE);
                ivBottomFullScreen.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.GONE);
                if (mVideoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
                    ivPlay.setVisibility(View.VISIBLE);
                    if (isFullScreen()) {
                        llFinishContent.setVisibility(View.GONE);
                        rlTitle.setVisibility(View.VISIBLE);
                        linearBottom.setVisibility(View.VISIBLE);
                        pbBottom.setVisibility(View.GONE);
                    } else {
                        linearBottom.setVisibility(View.GONE);
                        if (mIsNeedFinishContent && isNeedControlBar()) {
                            llFinishContent.setVisibility(View.VISIBLE);
                        } else {
                            llFinishContent.setVisibility(View.GONE);
                        }
                        if (isNeedControlBar()) {
                            pbBottom.setVisibility(View.VISIBLE);
                        } else {
                            pbBottom.setVisibility(View.GONE);
                        }
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ivPlay.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.GONE);
                    rlTitle.setVisibility(View.GONE);
                    linearBottom.setVisibility(View.GONE);
                    pbBottom.setVisibility(View.GONE);
                }
            } else if (value == ViewStatus.NET_CHANGE.value()) {
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                tvTotalTime.setVisibility(View.GONE);
                pbLoading.setVisibility(View.GONE);
                rlMengban.setVisibility(View.GONE);
                llReplay.setVisibility(View.GONE);
                tvFinishCenterContent.setVisibility(View.GONE);
                ivBottomReplay.setVisibility(View.GONE);
                llFinishContent.setVisibility(View.GONE);
                rlTitle.setVisibility(View.GONE);
                linearBottom.setVisibility(View.GONE);
                pbBottom.setVisibility(View.GONE);
                ivBottomFullScreen.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.VISIBLE);
            } else if (value == ViewStatus.COMPLETE.value()
                    || value == ViewStatus.ERROR.value()
                    || value == ViewStatus.NO_NET.value()) {
                // 播放完成/播放失败/无网络
                ivBottomFullScreen.setVisibility(View.GONE);
                llNetMengban.setVisibility(View.GONE);
                if (mVideoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
                    ivPlay.setVisibility(View.GONE);
                    ivPause.setVisibility(View.GONE);
                    tvTotalTime.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                    rlMengban.setVisibility(View.VISIBLE);
                    linearBottom.setVisibility(View.GONE);
                    pbBottom.setVisibility(View.GONE);
                    llReplay.setVisibility(View.VISIBLE);
                    if (value == ViewStatus.ERROR.value()) {
                        tvReplayHint.setText("视频加载失败了...");
                    } else {
                        tvReplayHint.setText("网络去哪儿了");
                    }
                    if (isFullScreen()) {
                        tvFinishCenterContent.setVisibility(View.GONE);
                        ivBottomReplay.setVisibility(View.GONE);
                        if (value == ViewStatus.COMPLETE.value()) {
                            tvReplayHint.setText("重播");
                        }
                        llFinishContent.setVisibility(View.GONE);
                        rlTitle.setVisibility(View.VISIBLE);
                        ivFrontView.setVisibility(View.GONE);
                    } else {
                        if (value == ViewStatus.COMPLETE.value()) {
                            if (mIsNeedFinishContent) {
                                tvReplayHint.setText("");
                            } else {
                                tvReplayHint.setText("重播");
                            }
                        }
                        if (isNeedControlBar()) {
                            if (mIsNeedFinishContent) {
                                llFinishContent.setVisibility(View.VISIBLE);
                            } else {
                                llFinishContent.setVisibility(View.GONE);
                            }
                        } else {
                            if (value == ViewStatus.COMPLETE.value()) {
                                if (mIsNeedFinishContent) {
                                    tvFinishCenterContent.setVisibility(View.VISIBLE);
                                } else {
                                    tvFinishCenterContent.setVisibility(View.GONE);
                                }
                                llReplay.setVisibility(View.GONE);
                                llFinishContent.setVisibility(View.GONE);
                                ivBottomReplay.setVisibility(View.VISIBLE);
                            } else {
                                tvFinishCenterContent.setVisibility(View.GONE);
                                ivBottomReplay.setVisibility(View.GONE);
                            }
                        }
                        if (!StringUtil.isNull(getTitleStr())) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.GONE);
                        }
                        ivFrontView.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvFinishCenterContent.setVisibility(View.GONE);
                    ivBottomReplay.setVisibility(View.GONE);
                }
                //释放视频资源
                JCMediaManager.getInstance().operationMediaPlayer(null, false, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.e(TAG, "-->setViewStatus 异常" + ex.getMessage());
        }
    }

    /**
     * 设置全屏结束重置状态
     */
    public void setFullCompleteResetStatus() {
        try {
            mCurrentStatus = ViewStatus.COMPLETE.value();
            ivPlay.setVisibility(View.GONE);
            ivPause.setVisibility(View.GONE);
            tvTotalTime.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rlMengban.setVisibility(View.VISIBLE);
            linearBottom.setVisibility(View.GONE);
            pbBottom.setVisibility(View.GONE);
            tvReplayHint.setText("重播");
            llReplay.setVisibility(View.VISIBLE);
            llFinishContent.setVisibility(View.GONE);
            tvFinishCenterContent.setVisibility(View.GONE);
            ivBottomFullScreen.setVisibility(View.GONE);
            ivBottomReplay.setVisibility(View.GONE);
            rlTitle.setVisibility(View.VISIBLE);
            ivFrontView.setVisibility(View.VISIBLE);
            llNetMengban.setVisibility(View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.e(TAG, "-->setViewStatus 异常" + ex.getMessage());
        }
    }

    /**
     * 300毫米更新一次进度条
     */
    private void initProgressTimer() {
        if (mOnVideoListener != null) {
            setTextAndProgress(mOnVideoListener.getCurrentProgress(),
                    JCMediaManager.getInstance().getMediaPlayer().getTotalDuration());
        } else {
            setTextAndProgress(JCMediaManager.getInstance().getMediaPlayer().getCurrentPos(),
                    JCMediaManager.getInstance().getMediaPlayer().getTotalDuration());
        }
    }

    private class TimeHandler extends Handler {
        public TimeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_HIDE_CONTROLER_BAR:
                    //刷新隐藏控制条
                    if (isNeedControlBar() || isFullScreen()) {
                        rlTitle.setVisibility(View.GONE);
                        ivPause.setVisibility(View.GONE);
                        ivPlay.setVisibility(View.GONE);
                        linearBottom.setVisibility(View.GONE);
                        pbBottom.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    /**
     * 设置进度和时间
     */
    public int setTextAndProgress(long cur, long total) {
        int progress;
        try {
            progress = (int) (cur * 100 / (total == 0 ? 1 : total));
            if (mOnVideoListener != null) {
                mOnVideoListener.onProgressUpdate(cur, total, progress);
            }
            if (!mIsSeekTouch) {
                setProgressAndTime(progress, cur, total);
            }
        } catch (Exception ex) {
            progress = -1;
        }
        return progress;
    }

    /**
     * 设置进度和时间
     *
     * @param progress
     * @param currentTime
     * @param totalTime
     */
    private void setProgressAndTime(int progress, long currentTime, long totalTime) {
        try {
            mSeekBar.setProgress(progress < 5 ? 5 : progress);
            pbBottom.setProgress(progress);
            tvCurrentTime.setText(JCUtils.stringForTime(currentTime));
            tvLeftTime.setText(JCUtils.stringForTime(totalTime - currentTime));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置缓冲进度
     *
     * @param secProgress
     */
    public void setBufferProgress(int secProgress) {
        try {
            if (secProgress != 0)
                mSeekBar.setSecondaryProgress(secProgress);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 重置进度和时间
     */
    public void resetProgressAndTime() {
        try {
            //skProgress.setProgress(0);
            mSeekBar.setProgress(5);
            pbBottom.setProgress(0);
            mSeekBar.setSecondaryProgress(0);
            tvCurrentTime.setText(JCUtils.stringForTime(0));
            tvLeftTime.setText(JCUtils.stringForTime(0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 视频控件信息初始化
     *
     * @param videoType           视频类型
     * @param ivFrontType         首帧图片显示类型
     *                            0:JCVideoPlayer.IV_TYPE_CENTER_FIX_XY                    拉伸
     *                            1:JCVideoPlayer.IV_TYPE_CENTER_BLACK_CENTER_INSIDE       缩放黑边
     *                            2:JCVideoPlayer.IV_TYPE_CENTER_CROP                      缩放裁剪
     * @param isNeedFinishContent 是否需要结束文案
     * @param isEnableControlBar  是否控制条和暂停可用
     * @param videoViewInfo       视频视图显示信息
     * @param videoViewSetInfo    视频视图控制信息
     * @param viewListener        视频控件点击回调
     * @param onVideoListener     视频界面回调
     */
    public void initViewController(int videoType, int ivFrontType, boolean isNeedFinishContent, boolean isEnableControlBar, VideoViewInfo videoViewInfo, VideoViewSetInfo videoViewSetInfo, ViewListener viewListener, OnVideoListener onVideoListener) {
        this.mVideoType = videoType;
        this.mVideoViewInfo = videoViewInfo;
        this.mVideoViewSetInfo = videoViewSetInfo;
        this.mIsNeedFinishContent = isNeedFinishContent;
        this.mIsEnableControlBar = isEnableControlBar;
        mViewListener = viewListener;
        mOnVideoListener = onVideoListener;

        if (mIsEnableControlBar) {
            mSeekBar.setThumb(mContext.getResources().getDrawable(R.drawable.jc_seek_thumb));
            mSeekBar.setEnabled(true);
            mSeekBar.setPadding(DeviceUtils.dip2px(mContext, 10), DeviceUtils.dip2px(mContext, 13),
                    DeviceUtils.dip2px(mContext, 10), DeviceUtils.dip2px(mContext, 13));
        } else {
            mSeekBar.setThumb(null);
            mSeekBar.setEnabled(false);
            mSeekBar.setPadding(DeviceUtils.dip2px(mContext, 10), DeviceUtils.dip2px(mContext, 19),
                    DeviceUtils.dip2px(mContext, 10), DeviceUtils.dip2px(mContext, 19));
        }

        if (videoType != JCVideoPlayer.VIDEO_TYPE_OPENSCREEN) {
            if (isFullScreen()) {
                ivPlay.setImageResource(R.drawable.video_btn_play_fullscreen);
                ivPause.setImageResource(R.drawable.video_btn_pause_fullscreen);
                ivReplayBig.setImageResource(R.drawable.apk_ic_video_replay_full);
                ivBack.setVisibility(View.VISIBLE);
                int dip50px = DeviceUtils.dip2px(mContext, 50);
                ViewGroup.LayoutParams params = rlTitle.getLayoutParams();
                params.height = dip50px;
                rlTitle.setLayoutParams(params);
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContext.getResources().getDimension(R.dimen.text_size_l));
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setMaxLines(1);
                tvTitle.setPadding(dip50px, 0, dip50px, 0);
                tvTitle.setText(mVideoViewInfo.title);

                RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
                titleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                titleParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                titleParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                tvTitle.setLayoutParams(titleParam);

                rlFullScreen.setVisibility(View.GONE);
                rlReduceScreen.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setImageResource(R.drawable.video_btn_play);
                ivPause.setImageResource(R.drawable.video_btn_pause);
                ivReplayBig.setImageResource(R.drawable.video_btn_replay);
                tvTotalTime.setVisibility(View.GONE);
                ivBack.setVisibility(View.GONE);
                tvTotalTime.setText(mVideoViewInfo.totalTimeStr);
                ViewGroup.LayoutParams params = rlTitle.getLayoutParams();
                params.height = DeviceUtils.dip2px(mContext, 70);
                rlTitle.setLayoutParams(params);
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContext.getResources().getDimension(R.dimen.text_size_m));
                tvTitle.setGravity(Gravity.LEFT);
                tvTitle.setMaxLines(2);
                int dip6px = DeviceUtils.dip2px(mContext, 6);
                int dip12px = DeviceUtils.dip2px(mContext, 12);
                tvTitle.setPadding(dip12px, dip6px, dip12px, 0);
                tvTitle.setText(mVideoViewInfo.title);

                RelativeLayout.LayoutParams titleParam = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
                titleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                titleParam.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                titleParam.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                tvTitle.setLayoutParams(titleParam);

                rlFullScreen.setVisibility(View.VISIBLE);
                rlReduceScreen.setVisibility(View.GONE);
            }

            //主图片
            nickgao.com.okhttpexample.view.ImageLoadParams imageLoadParams = new nickgao.com.okhttpexample.view.ImageLoadParams();
            imageLoadParams.bgholder = R.color.black_f;
            if (!StringUtil.isNull(mVideoViewInfo.imageUrl)) {
                if (isFullScreen()) {
                    ivFrontView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageLoadParams.scaleType = ImageView.ScaleType.FIT_CENTER;
                } else {
                    if (ivFrontType == JCVideoPlayer.IV_TYPE_CENTER_FIX_XY) {
                        imageLoadParams.scaleType = ImageView.ScaleType.FIT_XY;
                    } else if (ivFrontType == JCVideoPlayer.IV_TYPE_CENTER_CROP) {
                        imageLoadParams.scaleType = ImageView.ScaleType.CENTER_CROP;
                    } else {
                        imageLoadParams.scaleType = ImageView.ScaleType.FIT_CENTER;
                    }
                }
                ImageSize imageSize = UrlUtil.getImageWHByUrl(mVideoViewInfo.imageUrl);
                if (imageSize != null) {
                    imageLoadParams.width = getViewWidth();
                    imageLoadParams.height = getViewWidth() * imageSize.getHeight() / imageSize.getWidth();
                }
                ImageLoader.getInstance().displayImage(mContext, ivFrontView, mVideoViewInfo.imageUrl, imageLoadParams, null);
            } else {
                imageLoadParams.scaleType = ImageView.ScaleType.FIT_XY;
                ImageLoader.getInstance().displayImage(mContext, ivFrontView, "", imageLoadParams, null);
            }
            if (videoType == JCVideoPlayer.VIDEO_TYPE_TOPIC
                    && (!StringUtil.isNull(mVideoViewInfo.totalTimeStr) | !StringUtil.isNull(mVideoViewInfo.totalSizeStr))) {
                if (!StringUtil.isNull(mVideoViewInfo.totalTimeStr)) {
                    tvNetHintTime.setText("视频时长 " + mVideoViewInfo.totalTimeStr);
                } else {
                    tvNetHintTime.setText("");
                }
                if (!StringUtil.isNull(mVideoViewInfo.totalSizeStr)) {
                    tvNetHintSize.setText("流量 约" + mVideoViewInfo.totalSizeStr);
                } else {
                    tvNetHintSize.setText("");
                }
                llNetHintMsg.setVisibility(View.VISIBLE);
            } else {
                llNetHintMsg.setVisibility(View.GONE);
            }
            ivFrontView.setVisibility(View.VISIBLE);
        } else {
            rlTitle.setVisibility(View.GONE);
            ivPlay.setVisibility(View.VISIBLE);
            ivPause.setVisibility(View.GONE);
            tvTotalTime.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
            rlMengban.setVisibility(View.GONE);
            llFinishContent.setVisibility(View.GONE);
            llReplay.setVisibility(View.GONE);
            linearBottom.setVisibility(View.GONE);
            pbBottom.setVisibility(View.GONE);
            ivFrontView.setVisibility(View.GONE);
        }
        tvFinishCenterContent.setVisibility(View.GONE);
        ivBottomFullScreen.setVisibility(View.GONE);
        ivBottomReplay.setVisibility(View.GONE);
        llNetMengban.setVisibility(View.GONE);
        //结束文案
        String finishContent = mVideoViewInfo.finishContent;
        if (StringUtil.isNull(finishContent)) {
            finishContent = "了解更多";
        } else if (finishContent.length() > 4) {
            // 标题
            finishContent = finishContent.substring(0, 4) + "...";
        }
        tvFinishContent.setText(finishContent);
        tvFinishCenterContent.setText(finishContent);
    }

    /**
     * 回收资料
     */
    public void releaseAll() {
        if (mTimeHandler != null) {
            mTimeHandler.removeMessages(HANDLE_HIDE_CONTROLER_BAR);
            mTimeHandler = null;
        }
        mViewListener = null;
        mOnVideoListener = null;
        mVideoViewInfo = null;
        mVideoViewSetInfo = null;
        mContext = null;
    }

    //*****************************************get()**********************************************

    /*****
     * 全屏
     *
     * @return
     */
    private boolean isFullScreen() {
        if (mVideoViewSetInfo != null) {
            return mVideoViewSetInfo.isFullScreen;
        }
        return false;
    }

    /**
     * 是否显示控制栏
     */
    private boolean isNeedControlBar() {
        if (mVideoViewSetInfo != null) {
            return mVideoViewSetInfo.isNeedControllerBar;
        }
        return false;
    }

    /**
     * 获得视频宽
     */
    private int getViewWidth() {
        if (mVideoViewSetInfo != null) {
            return mVideoViewSetInfo.viewWidth;
        }
        return 0;
    }

    /**
     * 获得视频高
     */
    private int getViewHeight() {
        if (mVideoViewSetInfo != null) {
            return mVideoViewSetInfo.viewHeight;
        }
        return 0;
    }

    /**
     * 获得总时长
     */
    private String getTotalTimeStr() {
        if (mVideoViewInfo != null) {
            return mVideoViewInfo.totalTimeStr;
        }
        return "";
    }

    /**
     * 获得标题
     */
    private String getTitleStr() {
        if (mVideoViewInfo != null) {
            return mVideoViewInfo.title;
        }
        return "";
    }

    /**
     * 获得结束文案
     */
    private String getFinishContent() {
        if (mVideoViewInfo != null) {
            return mVideoViewInfo.finishContent;
        }
        return "";
    }

    public boolean isSeekTouch() {
        return mIsSeekTouch;
    }

    /**
     * 监听器
     *
     * @return
     */
    public ViewListener getViewListener() {
        return mViewListener;
    }

    public int getCurrentStatus() {
        return mCurrentStatus;
    }

    public JCMediaPlayerTextureView getTextureView() {
        return mTextureView;
    }

    /**
     * 界面更新回调
     */
    public interface OnVideoListener {

        public long getCurrentProgress();

        public void onPlayClick(boolean isReplay);

        public void onPauseClick();

        public void onFrontIvClick();

        public void onContinueClick();

        /**
         * 进度更新
         */
        public void onProgressUpdate(long position, long duration, int progress);

        public void onSeekOver(int value);

        public void onSeekTouchDown();

        public void onSeekTouchUp();
    }
}
