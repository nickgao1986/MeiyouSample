package com.meetyou.crsdk.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.meetyou.crsdk.video.core.JCMediaManager;
import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.event.BackFullScreenEvent;
import com.meetyou.crsdk.video.screen.BaseFullScreenController;
import com.meetyou.crsdk.video.view.JCVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.playerui.R;

import static android.content.ContentValues.TAG;


/**
 * Created by wuzhongyou on 2016/11/15.
 */
public class JCFullScreenActivity extends Activity {

    private static final int HANDLE_VIDEO_RECOVER_SEEK = 0x250;
    private static final String INTENT_VIDEO_VIEW_INFO_KEY = "intent_video_view_info_key";
    private static final String INTENT_VIDEO_PLAY_STATUS_KEY = "intent_video_play_status_key";
    protected static int position;
    protected VideoPlayStatus mVideoPlayStatus;
    private VideoViewInfo mVideoViewInfo;
    protected static BaseFullScreenController mFullScreenController;

    protected RelativeLayout mJCVideoContainer;
    protected JCVideoView mJCVideoView;

    private boolean isHadInit = false;

    private boolean isDestory = false;

    private boolean isPause = false;

    private int status = 1;
    /**
     * 使用handler来处理前后台切换引起的textureview回收的seek,为了兼容魅族等会自动处理textureview恢复的机型
     */
    private Handler mHandler;

    protected boolean isTextureViewDestoryed = false;

    public static void toJCFullScreenActivity(Context context, int pos, VideoPlayStatus videoPlayStatus, VideoViewInfo videoViewInfo, BaseFullScreenController fullScreenController) {
        position = pos;
        mFullScreenController = fullScreenController;
        Intent intent = new Intent(context, JCFullScreenActivity.class);
        intent.putExtra(INTENT_VIDEO_PLAY_STATUS_KEY, videoPlayStatus);
        intent.putExtra(INTENT_VIDEO_VIEW_INFO_KEY, videoViewInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            setContentView(R.layout.activity_jc_ad_full_screen);
            mJCVideoContainer = (RelativeLayout) findViewById(R.id.v_videocontainder);
            mVideoPlayStatus = (VideoPlayStatus) getIntent().getSerializableExtra(INTENT_VIDEO_PLAY_STATUS_KEY);
            mVideoViewInfo = (VideoViewInfo) getIntent().getSerializableExtra(INTENT_VIDEO_VIEW_INFO_KEY);
            mJCVideoView = createJCVideoView();
            mJCVideoContainer.addView(mJCVideoView);
            initVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVideo() {
        mHandler = new JCFullScreenActivity.RecoveryHandler(getMainLooper());
        resetVideoInfo();
    }

    protected void resetVideoInfo() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mFullScreenController != null && mJCVideoView != null && mVideoPlayStatus != null)
                    mFullScreenController.resetVideoInfo(mJCVideoView, position, mVideoPlayStatus, mVideoViewInfo, mViewListener, mSurfaceTextureListener);
            }
        });
    }

    protected JCVideoView createJCVideoView() {
        return mFullScreenController.createJCVideoView(this);
    }

    //********************************TextureView.SurfaceTextureListener实现*****************

    protected TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mJCVideoView.setSurface(new Surface(surface));
            if (mJCVideoView == null || !mJCVideoView.checkDataNormal() || mVideoPlayStatus == null) {
                return;
            }
            if (!isHadInit && !mJCVideoView.isLockScreen()) {
                mJCVideoView.startPlay(mVideoPlayStatus.progress);
                status = 1;
                mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                isHadInit = true;
            } else {
                mJCVideoView.setDisplaySurfaceHolder();
                if (status == 2) {
                    if (mHandler != null) {
                        mHandler.removeMessages(HANDLE_VIDEO_RECOVER_SEEK);
                        mHandler.sendEmptyMessageDelayed(HANDLE_VIDEO_RECOVER_SEEK, 150);
                    }
                } else if (mVideoPlayStatus.isCompleted) {
                    status = 0;
                    mJCVideoView.resetFullCompleteStatus();
                } else if (!mVideoPlayStatus.isPlaying && !mJCVideoView.isLockScreen() && !isPause) {
                    status = 1;
                    mJCVideoView.play();
                    mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
                }
            }
            isTextureViewDestoryed = false;
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            isTextureViewDestoryed = true;
            if (mJCVideoView == null || !mJCVideoView.checkDataNormal()) {
                return false;
            }
            mJCVideoView.setSurface(null);
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    //********************************ViewListener实现***************************************

    protected ViewListener mViewListener = new ViewListener() {

        @Override
        public void onClickFullScreen() {
        }

        @Override
        public void onClickPlayOver() {
            // 根据位置判断是否全屏播放
            LogUtils.d(TAG, "视频处于屏幕内,开始自动播放");
            status = 1;
        }

        @Override
        public void onClickPauseOver() {
            status = 2;
        }

        @Override
        public void onProgressStatusCallback(VideoProgressStatus progressStatus) {
            if (mFullScreenController != null) {
                mFullScreenController.handleProgressStatusCallback(progressStatus);
            }
            if (progressStatus.value() == VideoProgressStatus.COMPLETE.value()) {
                if (mJCVideoView == null || !mJCVideoView.checkDataNormal() || mVideoPlayStatus == null) {
                    return;
                }
                mVideoPlayStatus.changeComplete();
                status = 0;
                if (mFullScreenController == null || mFullScreenController.isCompleteBack()) {
                    doBack();
                }
            }
        }

        @Override
        public void onClickReplayOver() {
            status = 1;
        }

        @Override
        public void onClickVideoView() {
            // TODO 进入全屏播放
        }

        @Override
        public void onClickComplte() {
        }

        @Override
        public void onSeekTouchDown(boolean isPlaying) {
            try {
                if (!mJCVideoView.checkDataNormal()) {
                    return;
                }
                if (isPlaying) {
                    status = 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSeekTouchUp(boolean isPlaying) {
            try {
                if (!mJCVideoView.checkDataNormal()) {
                    return;
                }
                if (isPlaying) {
                    status = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClickBack() {
            doBack();
        }
    };

    @Override
    protected final void onResume() {
        super.onResume();
        isPause = false;
        if (!isTextureViewDestoryed && isHadInit && mJCVideoView != null && mJCVideoView.checkDataNormal() && mVideoPlayStatus != null) {
            mJCVideoView.setDisplaySurfaceHolder();
            if (status == 2) {
            } else if (mVideoPlayStatus.isCompleted) {
            } else if (!mVideoPlayStatus.isPlaying) {
                status = 1;
                mJCVideoView.play();
                mVideoPlayStatus.changeVideoPlayStatus(true, true, false, false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected final void onPause() {
        super.onPause();
        isPause = true;
        if (!isDestory && mJCVideoView != null && mJCVideoView.checkDataNormal() && mVideoPlayStatus != null && mVideoPlayStatus.isPlaying) {
            mJCVideoView.pause(false);
            mVideoPlayStatus.setIsPlaying(false);
        }
    }

    @Override
    public final void onBackPressed() {
        doBack();
    }

    private void doBack() {
        isDestory = true;
        BackFullScreenEvent backFullScreenEvent = null;
        if (mJCVideoView != null) {
            if (mJCVideoView.checkDataNormal()) {
                backFullScreenEvent = new BackFullScreenEvent(status, position, mVideoPlayStatus.progress, mVideoPlayStatus.reportStatus, mVideoPlayStatus.uniqueVideoListId);
                if (mVideoPlayStatus.isPlaying) {
                    mJCVideoView.pause(false);
                    mVideoPlayStatus.setIsPlaying(false);
                }
            }
            mJCVideoView.stopAndRelease(true, false, true);
        }
        JCFullScreenActivity.this.finish();
        if (backFullScreenEvent != null) {
            //EventBus.getDefault().post(backFullScreenEvent);
        }
        releaseAll();
    }

    private void releaseAll() {
        mVideoPlayStatus = null;
        if (mHandler != null) {
            mHandler.removeMessages(HANDLE_VIDEO_RECOVER_SEEK);
            mHandler = null;
        }
        if (mFullScreenController != null) {
            mFullScreenController.releaseAll();
            mFullScreenController = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class RecoveryHandler extends Handler {
        public RecoveryHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_VIDEO_RECOVER_SEEK:
                    if (status == 2 && !isDestory) {
                        if (mJCVideoView != null && mVideoPlayStatus != null) {
                            JCMediaManager.getInstance().getMediaPlayer().seek2(mVideoPlayStatus.progress);
                        }
                    }
                    break;
            }
        }
    }
}
