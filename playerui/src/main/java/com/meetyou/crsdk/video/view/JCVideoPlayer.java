package com.meetyou.crsdk.video.view;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.meetyou.crsdk.util.CPUUtil;
import com.meetyou.crsdk.util.NetWorkStatusUtil;
import com.meetyou.crsdk.util.ToastUtils;
import com.meetyou.crsdk.video.core.JCMediaManager;
import com.meetyou.crsdk.video.core.JCMediaPlayerListener;
import com.meetyou.crsdk.video.core.JCUtils;
import com.meetyou.crsdk.video.core.JCVideoInitMsg;
import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.media.player.client.player.MeetyouPlayerCode;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtil;


/**
 * Manage MediaPlayer
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public abstract class JCVideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener {

    public static final String TAG = "JCVideoPlayer";

    /** 广告视频类型 */
    public static int VIDEO_TYPE_AD = 1;
    /** 帖子视频类型 */
    public static int VIDEO_TYPE_TOPIC = 2;
    /** 开屏视频类型 */
    public static int VIDEO_TYPE_OPENSCREEN = 3;

    public static final int IV_TYPE_CENTER_FIX_XY = 0;              //拉伸
    public static final int IV_TYPE_CENTER_BLACK_CENTER_INSIDE = 1; //缩放黑边
    public static final int IV_TYPE_CENTER_CROP = 2;                //缩放裁剪
    //视图控制类
    protected ViewController mViewController;
    //视频视图显示信息
    protected VideoViewInfo mVideoViewInfo;
    //视频视图设置信息
    protected VideoViewSetInfo mVideoViewSetInfo;
    //视频播放状态信息
    protected VideoPlayStatus mVideoPlayStatus;
    //视频按钮回调
    protected ViewListener mViewListener;
    //视频控件回调
    protected TextureView.SurfaceTextureListener mSurfaceCallback;
    //位置，由于基本用于列表，所以有位置信息
    protected int mPosition = -1;
    //是否需要结束文案
    protected boolean mIsNeedFinishContent;
    //是否忽略网络状况
    protected boolean isIgnoreNetwork = false;
    //显示surface
    public Surface mSurface;

    //图片缩放类型
    protected int mIvFrontType;

    //是否由于网络变化引起提示
    protected boolean isCauseByNetChange = false;

    //抽象方法
    public abstract int getLayoutId();

    public JCVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        LogUtils.d(TAG, "初始化");
        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        addView(view);
        //data
        //视图
        mViewController = new ViewController(context, this);
    }

    /**
     * 设置数据源
     *
     * @param position
     * @param ivFrontType         首帧图片显示类型
     *                            0:JCVideoPlayer.IV_TYPE_CENTER_FIX_XY                    拉伸
     *                            1:JCVideoPlayer.IV_TYPE_CENTER_BLACK_CENTER_INSIDE       缩放黑边
     *                            2:JCVideoPlayer.IV_TYPE_CENTER_CROP                      缩放裁剪
     * @param isNeedFinishContent 是否需要播放结束文案
     * @param isEnableControlBar  是否控制条和暂停可用
     * @param videoPlayStatus     播放状态控制信息
     * @param videoViewInfo       播放视图显示信息
     * @param videoViewSetInfo    播放视图设置信息
     * @param viewListener        视图控制回调
     * @param surfaceCallback     view回调
     */
    protected void setUp(int position, int ivFrontType, boolean isNeedFinishContent, boolean isEnableControlBar,
                         VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo, VideoViewSetInfo videoViewSetInfo,
                         final ViewListener viewListener, TextureView.SurfaceTextureListener surfaceCallback) {
        this.mPosition = position;
        this.mIvFrontType = ivFrontType;
        this.mIsNeedFinishContent = isNeedFinishContent;
        this.mVideoPlayStatus = videoPlayStatus;
        this.mVideoViewInfo = videoViewInfo;
        this.mVideoViewSetInfo = videoViewSetInfo;
        this.mViewListener = viewListener;
        this.mSurfaceCallback = surfaceCallback;
        if (!checkDataNormal()) {
            return;
        }
        mViewController.initViewController(getVideoType(), mIvFrontType, mIsNeedFinishContent, isEnableControlBar,
                mVideoViewInfo, mVideoViewSetInfo, mViewListener, new ViewController.OnVideoListener() {

                    boolean isPlaying;

                    @Override
                    public void onPlayClick(final boolean isReplay) {
                        if (!checkDataNormal())
                            return;
                        if (isReplay) {
                            mVideoPlayStatus.progress = 0;
                        }
                        if (mVideoViewSetInfo.isFullScreen
                                || !mVideoViewSetInfo.isNeedControllerBar) {
                            doClickPlay(isReplay);
                        } else {
                            if (NetWorkStatusUtil.queryNetWork(getContext())
                                    || getVideoType() == VIDEO_TYPE_OPENSCREEN) {
                                int netType = NetWorkStatusUtil.getNetType(getContext());
                                if (isIgnoreNetwork
                                        || (JCMediaManager.isIgnoreNetwork && (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar))
                                        || netType == NetWorkStatusUtil.NETWORK_CLASS_WIFI
                                        || getVideoType() == VIDEO_TYPE_OPENSCREEN) {
                                    doClickPlay(isReplay);
                                } else {
                                    isCauseByNetChange = false;
                                    mViewController.setViewStatus(ViewStatus.NET_CHANGE);
                                }
                            } else {
                                ToastUtils.showToast(getContext(), "暂无网络");
                                doClickPlay(isReplay);
                            }
                        }
                    }

                    @Override
                    public void onPauseClick() {
                        if (!checkDataNormal()) {
                            return;
                        }
                        if (mVideoPlayStatus.isPlaying) {
                            pause(true);
                            mVideoPlayStatus.changeVideoPlayStatus(false, true, true, false);
                        }
                        if (mViewController.getViewListener() != null) {
                            mViewController.getViewListener().onClickPauseOver();
                        }
                    }

                    @Override
                    public void onContinueClick() {
                        JCMediaManager.isIgnoreNetwork = true;
                        isIgnoreNetwork = true;
                        JCMediaManager.isIgnoreToast = true;
                        if (!isCauseByNetChange) {
                            doClickPlay(false);
                        } else {
                            play();
                            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                        }
                    }

                    @Override
                    public void onFrontIvClick() {
                        if (!checkDataNormal())
                            return;
                        if (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar) {
                            if (mViewController.getCurrentStatus() != ViewStatus.NORAML.value()
                                    && mViewController.getCurrentStatus() != ViewStatus.NET_CHANGE.value())
                                return;
                            if (mVideoViewSetInfo.isFullScreen) {
                                doClickPlay(false);
                            } else {
                                if (NetWorkStatusUtil.queryNetWork(getContext())) {
                                    int netType = NetWorkStatusUtil.getNetType(getContext());
                                    if (isIgnoreNetwork
                                            || (JCMediaManager.isIgnoreNetwork && (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar))
                                            || netType == NetWorkStatusUtil.NETWORK_CLASS_WIFI) {
                                        doClickPlay(false);
                                    } else {
                                        isCauseByNetChange = false;
                                        mViewController.setViewStatus(ViewStatus.NET_CHANGE);
                                    }
                                } else {
                                    ToastUtils.showToast(getContext(), "暂无网络");
                                    doClickPlay(false);
                                }
                            }
                        }
                    }

                    @Override
                    public long getCurrentProgress() {
                        if (mVideoPlayStatus != null)
                            return mVideoPlayStatus.progress;
                        return 0;
                    }

                    @Override
                    public void onProgressUpdate(long position, long duration, int progress) {
                        if (!checkDataNormal()) {
                            return;
                        }
                        if (mVideoPlayStatus.isPlaying && mViewController != null && !mViewController.isSeekTouch()) {
                            mVideoPlayStatus.progress = position;
                        }
                        if (progress >= 25 && mVideoPlayStatus.reportStatus < 1) {
                            mVideoPlayStatus.reportStatus = 1;
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.ONEQUARTER);
                            }
                        } else if (progress >= 50 && mVideoPlayStatus.reportStatus < 2) {
                            mVideoPlayStatus.reportStatus = 2;
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.HALF);
                            }
                        } else if (progress >= 75 && mVideoPlayStatus.reportStatus < 3) {
                            mVideoPlayStatus.reportStatus = 3;
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.THREEQUARTER);
                            }
                        }
                    }

                    @Override
                    public void onSeekOver(int value) {
                        try {
                            long time = JCMediaManager.getInstance().getMediaPlayer().getTotalDuration();
                            time = time * value / 100;
                            if (time >= JCMediaManager.getInstance().getMediaPlayer().getTotalDuration()) {
                                time = JCMediaManager.getInstance().getMediaPlayer().getTotalDuration() - 1;
                            }
                            if (time < 0) {
                                time = 0;
                            }
                            JCMediaManager.getInstance().getMediaPlayer().seek2(time);
                            updateCurrentTimeText();
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.SEEKBARTOUCH);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSeekTouchDown() {
                        try {
                            if (!checkDataNormal()) {
                                return;
                            }
                            isPlaying = mVideoPlayStatus.isPlaying;
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onSeekTouchDown(isPlaying);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSeekTouchUp() {
                        try {
                            if (!checkDataNormal()) {
                                return;
                            }
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onSeekTouchUp(isPlaying);
                            }
                            isPlaying = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        if (mVideoPlayStatus.isCompleted) {
            mViewController.setViewStatus(ViewStatus.COMPLETE);
        } else if (mVideoPlayStatus.isPlaying) {
            mViewController.setViewStatus(ViewStatus.PLAYING);
        } else {
            mViewController.setViewStatus(ViewStatus.NORAML);
        }
        setSurfaceHolderCallback(mSurfaceCallback);
    }

    /**
     * 获取视频类型
     */
    protected abstract int getVideoType();

    /**
     * 检查数据是否正常
     *
     * @return
     */
    public final boolean checkDataNormal() {
        if (mVideoPlayStatus != null && mVideoViewInfo != null && mVideoViewSetInfo != null && mViewController != null) {
            return true;
        }
        return false;
    }

    private void setSurfaceHolderCallback(TextureView.SurfaceTextureListener callback) {
        if (callback == null) {
            mViewController.getTextureView().setSurfaceTextureListener(this);
        } else {
            mViewController.getTextureView().setSurfaceTextureListener(callback);
        }
    }

    @Override
    public final void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        handleSurfaceCreated(surface, width, height);
    }

    @Override
    public final void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public final boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        handleSurfaceDestroyed(surface);
        return true;
    }

    @Override
    public final void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    protected abstract void handleSurfaceCreated(SurfaceTexture surface, int width, int height);

    protected abstract void handleSurfaceDestroyed(SurfaceTexture surface);

    public final synchronized void setSurface(Surface surface) {
        if (surface != null) {
            mSurface = surface;
        } else {
            mSurface = null;
        }
        if (mViewController != null && mViewController.getTextureView() != null) {
            mViewController.getTextureView().setJCSurface(mSurface);
        }
    }

    /**
     * 设置SurfaceHolder
     */
    public final void setDisplaySurfaceHolder() {
        try {
            handleAudio(true);
            if (JCMediaManager.getInstance().getMediaPlayer() != null
                    && mViewController != null) {
                JCMediaManager.getInstance().getMediaPlayer().setMeetyouPlayerView(mViewController.getTextureView());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    private boolean isPlaying() {
        return mViewController.getCurrentStatus() == ViewStatus.PLAYING.value() ? true : false;
    }

    /**
     * 开始播放
     */
    public final boolean initPlaying(long progress) {
        try {
            if (!checkHardAccFail() && CPUUtil.isArmCPUSystem()) {
                return prepareVideo(progress);
            } else {
                if (checkDataNormal() && mVideoViewSetInfo.isFullScreen) {
                    stopAndRelease(false, false, false);
                }
                mViewController.setViewStatus(ViewStatus.ERROR);
                if (!CPUUtil.isArmCPUSystem()) {
                    ToastUtils.showToast(getContext(), "暂不兼容本机CPU类型");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 开始播放
     */
    public final boolean startPlay(long progress) {
        try {
            if (!checkHardAccFail() && CPUUtil.isArmCPUSystem()) {
                if (!checkDataNormal()) {
                    return false;
                }
                if (mViewController.getCurrentStatus() == ViewStatus.NORAML.value()
                        || mViewController.getCurrentStatus() == ViewStatus.NET_CHANGE.value()
                        || mViewController.getCurrentStatus() == ViewStatus.PAUSE.value()
                        || mViewController.getCurrentStatus() == ViewStatus.ERROR.value()
                        || mViewController.getCurrentStatus() == ViewStatus.NO_NET.value()
                        || mViewController.getCurrentStatus() == ViewStatus.COMPLETE.value()) {
                    if (progress == 0) {
                        if (mViewController.getViewListener() != null) {
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.CLICKSTART);
                        }
                    }
                    return prepareVideo(progress);
                }
                return false;
            } else {
                if (checkDataNormal() && mVideoViewSetInfo.isFullScreen) {
                    stopAndRelease(false, false, false);
                }
                mViewController.setViewStatus(ViewStatus.ERROR);
                if (!CPUUtil.isArmCPUSystem()) {
                    ToastUtils.showToast(getContext(), "暂不兼容本机CPU类型");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 准备开始
     */
    private boolean prepareVideo(final long progress) {
        try {
            LogUtils.d(TAG, "--->prepareVideo");
            if (JCMediaManager.getInstance() != null) {
                if (JCMediaManager.getInstance().getJCMediaPlayerListener() != null) {
                    JCMediaManager.getInstance().getJCMediaPlayerListener().onPause();
                }
                JCMediaManager.getInstance().operationMediaPlayer(null, false, null);
            }
            if (!checkDataNormal()) {
                return false;
            }
            if (progress == 0) {
                mVideoPlayStatus.resetReportStatus();
            }
            //设置视频监听
            JCMediaManager.getInstance().setJCMediaPlayerListener(new JCMediaPlayerListener() {

                /**
                 * 视图改变
                 */
                @Override
                public void onVideoSizeChanged() {
                    LogUtils.d(TAG, "--->onVideoSizeChanged");
                    if (!checkDataNormal()) {
                        return;
                    }
                    if (getVideoType() == VIDEO_TYPE_OPENSCREEN) {
                        //视频数据获取成功的回调,该回调并不属于视频生命周期,会在视频数据刷新的时候也会回调
                        mViewController.resizeFullVideoView();
                    } else {
                        int mVideoWidth = JCMediaManager.getInstance().currentVideoWidth;
                        int mVideoHeight = JCMediaManager.getInstance().currentVideoHeight;
                        if (mVideoWidth != 0 && mVideoHeight != 0) {
                            mViewController.resizeVideoView(mVideoWidth, mVideoHeight);
                        }
                    }
                }

                @Override
                public void onBufferingUpdate(int percent) {
                    if (!checkDataNormal()) {
                        return;
                    }
                    if (mViewController != null) {
                        mViewController.setBufferProgress(percent);
                    }
                }

                @Override
                public void onProgressUpdate(long cur, long total) {
                    mViewController.setTextAndProgress(cur, total);
                }

                @Override
                public void onPrepared() {
                    LogUtils.d(TAG, "--->onPrepared");
                    if (isPlaying()) {
                        return;
                    }
                    if (!checkDataNormal()) {
                        return;
                    }
                    JCMediaManager.getInstance().getMediaPlayer().play();
                    if (progress != 0) {
                        long time = progress;
                        if (time >= JCMediaManager.getInstance().getMediaPlayer().getTotalDuration()) {
                            time = JCMediaManager.getInstance().getMediaPlayer().getTotalDuration() - 1;
                        }
                        if (time < 0) {
                            time = 0;
                        }
                        JCMediaManager.getInstance().getMediaPlayer().seek2(time);
                    } else {
                        if (mViewController.getViewListener() != null) {
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.START);
                        }
                    }
                    mViewController.setViewStatus(ViewStatus.PLAYING);
                    checkPlayNetState();
                }

                @Override
                public void onCompletion() {
                    LogUtils.d(TAG, "--->onCompletion");
                    if (checkDataNormal()) {
                        mVideoPlayStatus.changeComplete();
                        if (mViewController.getViewListener() != null) {
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.COMPLETE);
                        }
                        stopAndRelease(true, false, false);
                        if (!mVideoViewSetInfo.isFullScreen) {
                            //EventBus.getDefault().post(new VideoPlayCompleteEvent(mPosition, mVideoPlayStatus.uniqueVideoListId));
                        }
                    }
                }

                @Override
                public void onError(final int what) {
                    LogUtils.e(TAG, "--->onError what:" + what);
                    if (!checkDataNormal() || mViewController.getCurrentStatus() == ViewStatus.NET_CHANGE.value()) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (mVideoViewSetInfo.isFullScreen) {
                                stopAndRelease(false, false, false);
                            } else {
                                if (mVideoPlayStatus.isPlaying) {
                                    mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                                    stopAndRelease(true, false, true);
                                }
                            }
                            // MeetyouPlayerDef
                            if (what == MeetyouPlayerCode.FETCH_FILE_ERROR
                                    || what == MeetyouPlayerCode.READ_HEAD_ERROR) {
                                mViewController.setViewStatus(ViewStatus.NO_NET);
                            } else {
                                mViewController.setViewStatus(ViewStatus.ERROR);
                            }
                            if (mViewController.getViewListener() != null) {
                                mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.ERROR);
                            }
                        }
                    });
                }

                @Override
                public void onPause() {
                    LogUtils.d(TAG, "--->onPause");
                    if (!checkDataNormal()) {
                        return;
                    }
                    if (mVideoPlayStatus.isPlaying || mViewController.getCurrentStatus() == ViewStatus.PAUSE.value()) {
                        pause(false);
                        mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                    }
                }

                @Override
                public void onStartCallback() {
                    mViewController.onFirstRenderingStar();
                    if (mVideoPlayStatus != null && mViewController.getViewListener() != null) {
                        if (mVideoPlayStatus.reportPlay == 2) {
                            mVideoPlayStatus.reportPlay = 1;
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.CONTINUE);
                        } else if (mVideoPlayStatus.reportPlay == 0) {
                            mVideoPlayStatus.reportPlay = 1;
                        }
                    }
                }

                @Override
                public void onPauseCallback() {
                    if (mVideoPlayStatus != null && mViewController.getViewListener() != null) {
                        if (mVideoPlayStatus.reportPlay == 1) {
                            mVideoPlayStatus.reportPlay = 2;
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.PAUSE);
                        }
                    }
                }

                @Override
                public void onStopCallback() {
                    if (mVideoPlayStatus != null && mViewController.getViewListener() != null) {
                        if (mVideoPlayStatus.reportPlay == 1 && !mVideoPlayStatus.isCompleted) {
                            mVideoPlayStatus.reportPlay = 2;
                            mViewController.getViewListener().onProgressStatusCallback(VideoProgressStatus.PAUSE);
                        } else if (mVideoPlayStatus.isCompleted){
                            mVideoPlayStatus.reportPlay = 0;
                        }
                    }
                }
            }, "");
            //更新UI
            mViewController.setViewStatus(ViewStatus.PREPARE);
            //准备播放
            handleAudio(true);
            if (mSurface != null && checkDataNormal()) {
                String videoUrl = mVideoViewInfo.getCurrentVideoUrl(getContext());
//                if (NetUtil.getNetWorkType(getContext()) != PublicCons.NetType.INVALID) {
                    JCMediaManager.getInstance().operationMediaPlayer(getContext().getApplicationContext(), true,
                            new JCVideoInitMsg(videoUrl, false, mVideoViewSetInfo.isNeedVoice, mViewController.getTextureView(), mSurface, mVideoPlayStatus.uniqueVideoListId));
//                } else {
//                    if (checkDataNormal()) {
//                        if (mVideoPlayStatus.isPlaying) {
//                            mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
//                            stopAndRelease(true, false, true);
//                        }
//                        mViewController.setViewStatus(ViewStatus.NO_NET);
//                    }
//                    return false;
//                }
            } else {
                if (mVideoPlayStatus != null && mVideoPlayStatus.isPlaying) {
                    pause(false);
                    mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                }
                if (mViewController.getViewListener() != null) {
                    mViewController.getViewListener().onClickPauseOver();
                }
                mViewController.setViewStatus(ViewStatus.ERROR);
                return false;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 设置时间
     */
    public final void updateCurrentTimeText() {
        mViewController.setTextAndProgress(JCMediaManager.getInstance().getMediaPlayer().getCurrentPos(),
                JCMediaManager.getInstance().getMediaPlayer().getTotalDuration());
    }

    /**
     * 显示网络提示
     */
    public final void showNetChangeHint() {
        if (mViewController != null) {
            isCauseByNetChange = false;
            mViewController.setViewStatus(ViewStatus.NET_CHANGE);
        }
    }

    /**
     * 设置声音管理
     */
    private void handleAudio(boolean open) {
        if (open && mVideoViewSetInfo != null && (mVideoViewSetInfo.isFullScreen || mVideoViewSetInfo.isNeedVoice)) {
            JCUtils.getAudioManager(getContext().getApplicationContext()).requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
//            JCUtils.getAudioManager(getContext().getApplicationContext()).setStreamMute(AudioManager.STREAM_MUSIC, false);
//            JCUtils.getAudioManager(getContext().getApplicationContext()).setStreamSolo(AudioManager.STREAM_MUSIC, true);
            JCUtils.getAudioManager(getContext().getApplicationContext()).abandonAudioFocus(mAudioFocusListener);
        } else {
            JCUtils.getAudioManager(getContext().getApplicationContext()).requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
//            JCUtils.getAudioManager(getContext().getApplicationContext()).setStreamMute(AudioManager.STREAM_MUSIC, true);
//            JCUtils.getAudioManager(getContext().getApplicationContext()).setStreamSolo(AudioManager.STREAM_MUSIC, false);
            JCUtils.getAudioManager(getContext().getApplicationContext()).abandonAudioFocus(mAudioFocusListener);
        }
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
        }
    };

    /**
     * 获取位置信息
     *
     * @return
     */
    public int getPosition() {
        return mPosition;
    }

    protected abstract void doClickPlay(boolean isReplay);

    public final void play() {
        try {
            if (JCMediaManager.getInstance().isPause()) {
                if (!JCMediaManager.getInstance().getMediaPlayer().isPlaying()) {
                    handleAudio(true);
                    JCMediaManager.getInstance().setPlaying(true);
                    JCMediaManager.getInstance().getMediaPlayer().play();
                    mViewController.setViewStatus(ViewStatus.PLAYING);
                    checkPlayNetState();
                }
            } else {
                startPlay(mVideoPlayStatus.progress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void pause(boolean isPause) {
        try {
            if (mViewController.getCurrentStatus() == ViewStatus.NORAML.value()
                    || mViewController.getCurrentStatus() == ViewStatus.NET_CHANGE.value())
                return;
            try {
                if (isPause) {
                    if (JCMediaManager.getInstance().getMediaPlayer().isPlaying()) {
                        JCMediaManager.getInstance().setPause(true);
                        JCMediaManager.getInstance().getMediaPlayer().pause();
                    }
                } else {
                    JCMediaManager.getInstance().operationMediaPlayer(null, false, null);
                }
                handleAudio(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtils.d(TAG, "取消静音:" + JCUtils.getVolume());
            //重置正常状态
            if (isPause) {
                mViewController.setViewStatus(ViewStatus.PAUSE);
            } else {
                mViewController.setViewStatus(ViewStatus.NORAML);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public final void stopAndRelease(boolean isComplete, boolean isPause, boolean isNotNeedChangeView) {
        try {
            if (!isComplete
                    && (mViewController.getCurrentStatus() == ViewStatus.NORAML.value()
                    || mViewController.getCurrentStatus() == ViewStatus.PAUSE.value()))
                return;
            LogUtils.d(TAG, "取消静音:" + JCUtils.getVolume());
            //重置正常状态
            mViewController.resetProgressAndTime();
            //重置正常状态
            if (!isNotNeedChangeView) {
                if (isComplete) {
                    mViewController.setViewStatus(ViewStatus.COMPLETE);
                } else {
                    mViewController.setViewStatus(ViewStatus.NORAML);//setStateAndUi(CURRENT_STATE_NORMAL);
                }
            }

            if (!isPause) {
                JCMediaManager.getInstance().operationMediaPlayer(null, false, null);
            }
            LogUtils.d(TAG, "设置静音");
            handleAudio(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //*******************************************************

    /**
     * 重新查看是否可播放
     *
     * @return
     */
    private boolean checkHardAccFail() {
        boolean isHardAccFail = !isHardwareAccelerated();
        if (isHardAccFail) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        return isHardAccFail;
    }

    /**
     * 检查当前播放网络是否是在移动网络
     */
    public final void checkPlayNetState() {
        if (JCMediaManager.isIgnoreToast) {
            JCMediaManager.isIgnoreToast = false;
            return;
        }
        if (NetWorkStatusUtil.queryNetWork(getContext())
                && (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar)) {
            int netType = NetWorkStatusUtil.getNetType(getContext());
            if (netType != NetWorkStatusUtil.NETWORK_CLASS_WIFI) {
                if (mVideoViewInfo != null && !StringUtil.isNull(mVideoViewInfo.totalSizeStr)) {
                    ToastUtils.showToast(getContext().getApplicationContext(), "正在使用流量播放，本视频约" + mVideoViewInfo.totalSizeStr);
                } else {
                    ToastUtils.showToast(getContext().getApplicationContext(), "正在使用流量播放");
                }
            }
        }
    }

    //********************处理的接口方法*********************

    public final void resetFullCompleteStatus() {
        if (mViewController != null) {
            mViewController.setFullCompleteResetStatus();
        }
    }

    public final void setScrolling(boolean scrolling) {
        if (mVideoPlayStatus != null)
            mVideoPlayStatus.isScrolling = scrolling;
    }

    public boolean isLockScreen() {
        if (JCMediaManager.isLockScreen) {
            PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (isScreenOn) {
                KeyguardManager mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
                boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
                JCMediaManager.isLockScreen = flag;
            }
        }
        return JCMediaManager.isLockScreen;
    }

    public final void setHadShow50Percent(boolean hadShow50Percent) {
        if (mVideoPlayStatus != null)
            mVideoPlayStatus.isHadShow50Percent = hadShow50Percent;
    }

    public final void changeJumpToFull(boolean jumpToFull) {
        if (mVideoPlayStatus != null)
            mVideoPlayStatus.isJumpToFull = jumpToFull;
    }

    public void setIgnoreNetwork(boolean ignoreNetwork) {
        isIgnoreNetwork = ignoreNetwork;
    }
}
