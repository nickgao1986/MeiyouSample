package com.meetyou.crsdk.video.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ListView;

import com.meetyou.crsdk.listener.OnListViewStatusListener;
import com.meetyou.crsdk.util.NetUtil;
import com.meetyou.crsdk.util.NetWorkStatusUtil;
import com.meetyou.crsdk.util.PublicCons;
import com.meetyou.crsdk.util.ViewUtil;
import com.meetyou.crsdk.video.core.JCMediaManager;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.event.BackFullScreenEvent;
import com.meetyou.crsdk.video.event.FragmentVisibleEvent;
import com.meetyou.crsdk.video.event.JumpToFullEvent;
import com.meetyou.crsdk.video.event.NetChangeEvent;
import com.meetyou.crsdk.video.event.NewsHomeSelectedEvent;
import com.meetyou.crsdk.video.event.VideoEventController;
import com.meetyou.crsdk.video.event.VideoPlayCompleteEvent;

import nickgao.com.playerui.R;


/**
 * Created by wuzhongyou on 2016/11/15.
 */

public abstract class JCVideoView extends JCVideoPlayer implements VideoEventController.VideoEventListener {

    private final int DELAY_CHECK_VIEW_MSG = 2500;

    private Handler mHandler;

    /**
     * 是否textureview被销毁
     */
    protected boolean isTextureViewDestoryed = false;

    /**
     * 上次记录的网络状况
     */
    protected int netType = NetWorkStatusUtil.NETWORK_CLASS_WIFI;

    //*****************************监听信息**********************************
    /**
     * 事件监听
     */
    private VideoEventController mEventController;

    public JCVideoView(Context context) {
        super(context);
    }

    public JCVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mHandler = new JCVideoView.DelayCheckHandler(Looper.getMainLooper());
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_layout_standard;
    }

    /**
     * 设置播放的信息
     *
     * @param position
     * @param ivFrontType
     * @param isNeedFinishContent
     * @param isEnableControlBar
     * @param videoPlayStatus
     * @param videoViewInfo
     * @param videoViewSetInfo
     * @param viewListener
     * @param surfaceCallback
     */
    @Override
    protected final void setUp(int position, int ivFrontType, boolean isNeedFinishContent, boolean isEnableControlBar, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo, VideoViewSetInfo videoViewSetInfo, ViewListener viewListener, TextureView.SurfaceTextureListener surfaceCallback) {
        super.setUp(position, ivFrontType, isNeedFinishContent, isEnableControlBar, videoPlayStatus, videoViewInfo, videoViewSetInfo, viewListener, surfaceCallback);
        if (checkDataNormal()) {
//            mVideoPlayStatus.setEventController(new VideoEventController(this, mPosition));
            if (mEventController != null) {
                mEventController.release();
            }
            this.mEventController = new VideoEventController(this, mPosition);
            if (mVideoPlayStatus.isPlaying && initPlaying(mVideoPlayStatus.progress)) {
                mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
            }
        }
    }

    //*********************************监听注册控制字*******************************

    /**
     * 注册监听事件
     */
    public final void doRegisterEvents() {
        if (checkDataNormal()) {
            if (mEventController != null) {
                mEventController.doRegisterEvents();
            }
        }
    }

    /**
     * 注销监听事件
     */
    public final void doUnRegisterEvents() {
        if (checkDataNormal()) {
            if (mEventController != null) {
                mEventController.doUnRegisterEvents();
            }
        }
    }

    @Override
    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mVideoPlayStatus != null && mVideoViewInfo != null && mVideoViewSetInfo != null) {
            doRegisterEvents();
            if (mViewController == null) {
                mViewController = new ViewController(getContext(), this);
                resetSetupInfo();
            }
        }
    }

    @Override
    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
            mHandler = null;
        }
        if (checkDataNormal()) {
            doUnRegisterEvents();
            if (!mVideoPlayStatus.isScrolling
                    && !mVideoPlayStatus.isCompleted
                    && mVideoPlayStatus.isPlaying) {
                mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                stopAndRelease(false, false, false);
            }
        }
        if (mVideoPlayStatus != null) {
            JCMediaManager.getInstance().setJCMediaPlayerListener(null, mVideoPlayStatus.uniqueVideoListId);
        }
        if (mViewController != null) {
            mViewController.releaseAll();
            mViewController = null;
        }
    }

    protected abstract void resetSetupInfo();

    //*********************************视图状态回调*******************************

    @Override
    protected final void handleSurfaceCreated(SurfaceTexture surface, int width, int height) {
        setSurface(new Surface(surface));
        if (checkDataNormal() && !mVideoPlayStatus.isScrolling && !isLockScreen()) {
            if (mVideoPlayStatus.isLastPlaying) {
                mVideoPlayStatus.isLastPlaying = false;
                if (!mVideoPlayStatus.isPlaying) {
                    setDisplaySurfaceHolder();
                    if (mVideoViewSetInfo.isFullScreen) {
                        play();
                        mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                    } else {
                        if (initPlaying(mVideoPlayStatus.progress)) {
                            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                        }
                    }
                }
            } else {
                if (mVideoPlayStatus.isFromFullContinuePlay) {
                    mVideoPlayStatus.isFromFullContinuePlay = false;
                    setDisplaySurfaceHolder();
                    if (!mVideoPlayStatus.isPlaying && startPlay(mVideoPlayStatus.progress)) {
                        mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                    }
                }
            }
        }
        if (mPosition <= 3
                && mSurface != null
                && checkDataNormal()
                && !mVideoPlayStatus.isScrolled
                && !mVideoPlayStatus.isInited) {
            mVideoPlayStatus.isInited = true;
            if (mHandler != null) {
                mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
                mHandler.sendEmptyMessageDelayed(DELAY_CHECK_VIEW_MSG, 150);
            }
        }
        isTextureViewDestoryed = false;
    }

    @Override
    protected final void handleSurfaceDestroyed(SurfaceTexture surface) {
        isTextureViewDestoryed = true;
        if (mHandler != null) {
            mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
        }
        if (checkDataNormal()) {
            if (!mVideoPlayStatus.isScrolling) {
                if (mVideoPlayStatus.isJumpToFull) {
                    mVideoPlayStatus.isJumpToFull = false;
                    return;
                }
                if (mVideoPlayStatus.isPlaying) {
                    pause(false);
                    if (mVideoViewSetInfo.isFullScreen || (getVideoType() != VIDEO_TYPE_TOPIC && !mVideoViewSetInfo.isNeedControllerBar)) {
                        mVideoPlayStatus.isLastPlaying = true;
                        mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                    }
                } else if (mViewController.getCurrentStatus() == ViewStatus.PAUSE.value()) {
                    stopAndRelease(false, false, false);
                    mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                    mViewController.setViewStatus(ViewStatus.NORAML);
                }
            }
        }
        setSurface(null);
    }

    //*********************************事件监听*******************************

    /**
     * 跳转进入全屏通知
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(JumpToFullEvent event) {
        if (!checkDataNormal()
                || mVideoPlayStatus.isScrolling
                || mVideoViewSetInfo.isFullScreen
                || !mVideoPlayStatus.uniqueVideoListId.equals(event.getUniqueVideoListId())) {
            return;
        }
        if (event.getPosition() != mPosition
                && mVideoPlayStatus.isPlaying) {
            // 前面的播放完，自动播放下一个
            mVideoPlayStatus.isJumpFull = true;
        } else {
            mVideoPlayStatus.isJumpFull = false;
        }
    }

    /**
     * 播放完成回调
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(VideoPlayCompleteEvent event) {
        if (!checkDataNormal()
                || mVideoPlayStatus.isScrolling
                || mVideoViewSetInfo.isFullScreen
                || !mVideoPlayStatus.uniqueVideoListId.equals(event.getUniqueVideoListId())) {
            return;
        }
        if (event.getPosition() < mPosition && mVideoViewSetInfo.isNeedAutoPlay) {
            // 前面的播放完，自动播放下一个
            doAutoPlay(true);
        }
    }

    /**
     * 全屏播放返回回调
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(BackFullScreenEvent event) {
        if (!checkDataNormal()
                || mVideoPlayStatus.isScrolling
                || mVideoViewSetInfo.isFullScreen
                || !mVideoPlayStatus.uniqueVideoListId.equals(event.getUniqueVideoListId())) {
            return;
        }
        mVideoPlayStatus.isFromFullScreen = true;
        if (mPosition == event.getPosition()) {
            mVideoPlayStatus.progress = event.getProgress();
            mVideoPlayStatus.reportStatus = event.getReportStatus();
            if (event.getStatus() == 0) {
                // 播完状态返回
                mVideoPlayStatus.reportPlay = 0;
                mVideoPlayStatus.changeVideoPlayStatus(false, true, false, true);
                stopAndRelease(true, false, false);
                handleCompleteFormFullScreenEvent();
            } else if (event.getStatus() == 1) {
                // 播放状态返回
                mVideoPlayStatus.isFromFullContinuePlay = true;
                if (!isTextureViewDestoryed && mSurface != null) {
                    mVideoPlayStatus.isFromFullContinuePlay = false;
                    if (!mVideoPlayStatus.isPlaying) {
                        if (startPlay(mVideoPlayStatus.progress)) {
                            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                        }
                    }
                }
            } else {
                mVideoPlayStatus.changeVideoPlayStatus(false, true, true, false);
            }
            handleStartFormFullScreenEvent();
        } else {
            if (event.getStatus() == 0 && mVideoPlayStatus.isJumpFull) {
                // 其他视频全屏播放完成，继续播放上次未播完视频
                mVideoPlayStatus.isJumpFull = false;
                mVideoPlayStatus.isFromFullContinuePlay = true;
                if (!isTextureViewDestoryed && mSurface != null) {
                    mVideoPlayStatus.isFromFullContinuePlay = false;
                    if (!mVideoPlayStatus.isPlaying && startPlay(mVideoPlayStatus.progress)) {
                        mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                    }
                }
            }
        }
    }

    /**
     * 播放完成从全屏返回
     */
    protected void handleCompleteFormFullScreenEvent() {
    }

    /**
     * 播放完成从全屏返回
     */
    protected void handleStartFormFullScreenEvent() {
    }

    /**
     * 用于处理首页tab切换处理
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(FragmentVisibleEvent event) {
        if (!checkDataNormal()) {
            return;
        }
        if (mVideoViewSetInfo.isFullScreen)
            return;
        if (mVideoPlayStatus.isFromFullScreen) {
            mVideoPlayStatus.isFromFullScreen = false;
            return;
        }
        handleFragmentVisibleChanged(event);
    }

    /**
     * 用于网络变化切换处理
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(NetChangeEvent event) {
//        if (!checkDataNormal()
//                || getVideoType() == VIDEO_TYPE_OPENSCREEN
//                || !mVideoViewSetInfo.isNeedControllerBar
//                || !mVideoPlayStatus.isPlaying) {//非播放中，不带进度条和全屏情况下，都不用处理
//            if (NetWorkStatusUtil.queryNetWork(getContext())) {
//                netType = event.getNetType();
//            } else {
//                netType = NetWorkStatusUtil.NETWORK_CLASS_UNKNOWN;
//            }
//            return;
//        }
//        if (NetWorkStatusUtil.queryNetWork(getContext())) {
//            if (!isIgnoreNetwork
//                    || (JCMediaManager.isIgnoreNetwork && (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar))
//                    && event.getNetType() != NetWorkStatusUtil.NETWORK_CLASS_WIFI
//                    && (netType == NetWorkStatusUtil.NETWORK_CLASS_WIFI || netType == NetWorkStatusUtil.NETWORK_CLASS_UNKNOWN)) { //wifi
//                pause(true);
////                isLastPlaying = true;
//                mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
//                isCauseByNetChange = true;
//                mViewController.setViewStatus(ViewStatus.NET_CHANGE);
//            }
//            netType = event.getNetType();
//        } else {
//            if (netType != NetWorkStatusUtil.NETWORK_CLASS_UNKNOWN) {
//                ToastUtils.showToast(getContext(), "暂无网络");
//                netType = NetWorkStatusUtil.NETWORK_CLASS_UNKNOWN;
//            }
//        }
    }

    /**
     * 用于监听锁屏
     *
     * @param event
     */
//    @Override
//    public final void handleEventMainThread(ScreenStatusChangeEvent event) {
//        if (!checkDataNormal()) {//非播放中，不用处理
//            return;
//        }
//        if (event.getScreenAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            JCMediaManager.isLockScreen = true;
//            if (!mVideoPlayStatus.isCompleted && mVideoPlayStatus.isPlaying) {
//                if (mVideoViewSetInfo.isNeedAutoPlay) {
//                    mVideoPlayStatus.isLastPlaying = true;
//                }
//                scrollToStop(false, false);
//            }
//        } else if (event.getScreenAction().equals(Intent.ACTION_USER_PRESENT)) {
//            JCMediaManager.isLockScreen = false;
//            if (mVideoPlayStatus.isLastPlaying && !isTextureViewDestoryed && !mVideoPlayStatus.isJumpToFull && !mVideoPlayStatus.isPlaying) {
//                mVideoPlayStatus.isLastPlaying = false;
//                doClickPlay(false);
//            }
//        }
//    }

    /**
     * 首页翻页监听
     *
     * @param event
     */
    @Override
    public final void handleEventMainThread(NewsHomeSelectedEvent event) {
        if (!checkDataNormal()
                || mVideoViewSetInfo.isFullScreen
                || !mVideoPlayStatus.isPlaying) {//非播放中，不用处理
            return;
        }
        try {
            scrollToStop(false, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected abstract void handleFragmentVisibleChanged(FragmentVisibleEvent event);

    //    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (!checkDataNormal()
                || getVideoType() == VIDEO_TYPE_OPENSCREEN
                || mSurface == null
                || mVideoPlayStatus.isScrolling
                || !mVideoPlayStatus.isInited
                || isTextureViewDestoryed) {
            return;
        }
        if (visibility == View.VISIBLE) {
            if (!mVideoPlayStatus.isPlaying) {
                if (mVideoPlayStatus.isLastPlaying) {
                    mVideoPlayStatus.isLastPlaying = false;
                    if (mVideoViewSetInfo.isFullScreen) {
                        play();
                        mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                    } else {
                        if (initPlaying(mVideoPlayStatus.progress)) {
                            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                        }
                    }
                } else {
                    if (mVideoPlayStatus.isFromFullContinuePlay) {
                        mVideoPlayStatus.isFromFullContinuePlay = false;
                        if (!mVideoPlayStatus.isPlaying && startPlay(mVideoPlayStatus.progress)) {
                            mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                        }
                    }
                }
            } else {
                if (mVideoPlayStatus.isFromFullContinuePlay) {
                    mVideoPlayStatus.isFromFullContinuePlay = false;
                    setDisplaySurfaceHolder();
                    if (!mVideoPlayStatus.isPlaying && startPlay(mVideoPlayStatus.progress)) {
                        mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                    }
                }
            }
        } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
            if (mHandler != null) {
                mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
            }
            if (mVideoPlayStatus.isJumpToFull) {
                mVideoPlayStatus.isJumpToFull = false;
                return;
            }
            if (mVideoPlayStatus.isPlaying) {
                pause(false);
                if (mVideoViewSetInfo.isFullScreen || (getVideoType() != VIDEO_TYPE_TOPIC && !mVideoViewSetInfo.isNeedControllerBar)) {
                    mVideoPlayStatus.isLastPlaying = true;
                }
                mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
            } else if (mViewController.getCurrentStatus() == ViewStatus.PAUSE.value()) {
                stopAndRelease(false, false, false);
                mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                mViewController.setViewStatus(ViewStatus.NORAML);
            }
        }
    }

    //*********************************播放控制*******************************

    /**
     * 自动播放
     */
    public final void doAutoPlay(boolean isByScroll) {
        if (!checkDataNormal()
                || mViewController.getCurrentStatus() == ViewStatus.ERROR.value()
                || mViewController.getCurrentStatus() == ViewStatus.NO_NET.value()
                || !mVideoViewSetInfo.isNeedAutoPlay) {
            return;
        }
        if (!mVideoPlayStatus.isCompleted && !mVideoPlayStatus.isPaused) {
            // 曝光区域需要大于50%
            if (!mVideoPlayStatus.isScrolling && mVideoPlayStatus.isHadShow50Percent) {
                if (!mVideoPlayStatus.isPlaying && !isLockScreen()) {
                    boolean isVideoPlaying = false;
                    if (isByScroll) {
                        try {
                            isVideoPlaying = (JCMediaManager.getInstance().getMediaPlayer() != null
                                    && JCMediaManager.getInstance().isPlaying());
                        } catch (Exception e) {
                            isVideoPlaying = false;
                        }
                    }
                    if (!isVideoPlaying) {
                        if (isIgnoreNetwork
                                || (JCMediaManager.isIgnoreNetwork && (getVideoType() == VIDEO_TYPE_TOPIC || mVideoViewSetInfo.isNeedControllerBar))
                                || (NetUtil.getNetWorkType(getContext()) != PublicCons.NetType.INVALID && NetWorkStatusUtil.isWifi(getContext().getApplicationContext()))
                                || getVideoType() == VIDEO_TYPE_OPENSCREEN) {
                            if (startPlay(mVideoPlayStatus.progress)) {
                                mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                            }
                        } else {
                            if (mVideoPlayStatus.isPlayed) {
                                if (startPlay(mVideoPlayStatus.progress)) {
                                    mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                                }

                            }
                        }
                    }
                }
            } else {
                scrollToStop(false, false);
            }
        }
    }

    /**
     * 自动播放
     */
    public final void scrollToStop(boolean isNeedCheckScrolling, boolean isNeedRecordPlaying) {
        if (mHandler != null) {
            mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
        }
        if (!checkDataNormal()) {
            return;
        }
        if (isNeedCheckScrolling && !mVideoPlayStatus.isScrolling) {
            return;
        }
        if (!mVideoPlayStatus.isCompleted && mVideoPlayStatus.isPlaying) {
            if (isNeedRecordPlaying) {
                mVideoPlayStatus.isLastPlaying = true;
            }
            mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
            stopAndRelease(false, false, false);
        }
    }


    //***********************前三位需要初始化检测位置****************************

    protected int bottomMargin, rangStart, rangEnd;

    /**
     * 初始化播放状态,create完可能需要直接播放，如果需要添加滚动监听，则不可使用这个
     */
    public final void initPlayStatues() {
        initPlayStatues(0, 0, 0);
    }

    /**
     * 初始化播放状态,create完可能需要直接播放，如果需要添加滚动监听，则必现使用这个
     *
     * @param bottomMargin videoview的底部和item的底部的距离
     * @param rangStart    listview可见起点
     * @param rangEnd      listview可见终点
     */
    public final void initPlayStatues(int bottomMargin, int rangStart, int rangEnd) {
        this.bottomMargin = bottomMargin;
        this.rangStart = rangStart;
        this.rangEnd = rangEnd;
        doRegisterEvents();

        if (!checkDataNormal() || mVideoViewSetInfo.isFullScreen) {
            return;
        }
        if (mPosition <= 3
                && mSurface != null
                && !mVideoPlayStatus.isScrolled
                && !mVideoPlayStatus.isInited) {
            mVideoPlayStatus.isInited = true;
            if (mHandler != null) {
                mHandler.removeMessages(DELAY_CHECK_VIEW_MSG);
                mHandler.sendEmptyMessageDelayed(DELAY_CHECK_VIEW_MSG, 150);
            }
        }
    }

    /**
     * 获取滚动兼容处理，必现需要(@link initPlayStatues(int height, int bottomMargin, int rangStart, int rangEnd))才可以，
     *
     * @param listView
     * @return
     */
    public final OnListViewStatusListener getVideoScrollListener(final ListView listView) {

        return new OnListViewStatusListener() {
            @Override
            public void onScrollStart() {
                setScrolling(true);
                //开始滑动时，全部处于非50%状态，避免自动播放
                setHadShow50Percent(false);
            }

            @Override
            public void onScrollFinish() {
                if (!checkDataNormal()) {
                    return;
                }
                setScrolling(false);
                int[] location = ViewUtil.getListViewVisiableRect(listView, JCVideoView.this, mPosition, mVideoViewSetInfo.viewHeight, rangStart, bottomMargin);
                int x = location[0];
                int y = location[1];
                // 曝光区域需要大于50%, x=0表示不在屏幕中了
                if (x != 0 && y >= (rangStart - getHeight() / 2) && (y + getHeight() / 2) <= rangEnd) {
                    setHadShow50Percent(true);
                } else {
                    setHadShow50Percent(false);
                }
                if (mVideoViewSetInfo.isNeedAutoPlay) {
                    doAutoPlay(true);
                }
                mVideoPlayStatus.isScrolled = true;
            }

            @Override
            public void onScrolling() {
                if (!checkDataNormal()) {
                    return;
                }
                int[] location = ViewUtil.getListViewVisiableRect(listView, JCVideoView.this, mPosition, mVideoViewSetInfo.viewHeight, rangStart, bottomMargin);
                int x = location[0];
                int y = location[1];
                // 进入视线则注册监听，移出视线则取消监听，, x=0表示不在屏幕中了
                if (!ViewUtil.isListViewVideoVisiable(listView, mPosition)) {
                    if (mVideoPlayStatus.isPlaying) {
                        scrollToStop(false, false);
                    }
                    doUnRegisterEvents();
                } else {
                    doRegisterEvents();
                    if (mVideoPlayStatus.isPlaying) {
                        // 曝光区域需要小于50%，暂停掉
                        if (x == 0 || y < (rangStart - getHeight() / 2) || (y + getHeight() / 2) > rangEnd) {
                            scrollToStop(true, false);
                        }
                    } else if (mVideoPlayStatus.isCompleted) {
                        // 曝光区域需要小于50%，修改播放状态
                        if (x == 0 || y < (rangStart - getHeight() / 2) || (y + getHeight() / 2) > rangEnd) {
                            mVideoPlayStatus.changeVideoPlayStatus(false, true, false, false);
                        }
                    }
                }
            }
        };
    }

    //******************************************************************************

    /**
     * 处理初始化时的自动播放
     */
    protected void checkInitAutoPlay() {
    }

    private class DelayCheckHandler extends Handler {

        public DelayCheckHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAY_CHECK_VIEW_MSG:
                    if (!isTextureViewDestoryed) {
                        checkInitAutoPlay();
                    }
                    break;
            }
        }
    }
}

