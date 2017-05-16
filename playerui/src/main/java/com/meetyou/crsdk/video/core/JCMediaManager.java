package com.meetyou.crsdk.video.core;

import android.content.Context;
import android.text.TextUtils;

import com.meetyou.media.player.client.MeetyouPlayerEngine;
import com.meetyou.media.player.client.ui.MeetyouPlayerView;
import com.meiyou.media.player.tv.MeetyouPlayer;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

import nickgao.com.framework.utils.StringUtil;


/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaManager implements IPlayerCallback.OnVideoSizeChangeListener, IPlayerCallback.OnBufferingListener,
        IPlayerCallback.OnProgressListener, IPlayerCallback.OnErrorListener, IPlayerCallback.OnCompleteListener,
        IPlayerCallback.OnStartListener, IPlayerCallback.OnPauseListener, IPlayerCallback.OnStopListener {

    private static JCMediaManager jcMediaManager;

    // 是否忽略网络状况
    public static boolean isIgnoreNetwork = false;
    //是否忽略网络提示
    public static boolean isIgnoreToast = false;
    //是否锁屏
    public static boolean isLockScreen = false;

    public static String TAG_PLAYER_INSTANCE = "tag_video_ad";
    private MeetyouPlayer mMeetyouPlayerCore;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    private JCMediaPlayerListener mJCMediaPlayerListener;
    public String mPlayingUniqueId;
    /**
     * 是否正在播放,准备中也包括在内
     */
    private boolean isPlaying = false;

    /**
     * 是否只是暂停
     *
     * @return
     */
    private boolean isPause = false;

    public static JCMediaManager getInstance() {
        if (jcMediaManager == null) {
            init();
        }
        return jcMediaManager;
    }

    public static synchronized void init() {
        if (jcMediaManager == null) {
            jcMediaManager = new JCMediaManager();
        }
    }

    public JCMediaManager() {
    }

    private synchronized MeetyouPlayer createMeetyouPlayerLifer() {
        if (mMeetyouPlayerCore == null) {
            mMeetyouPlayerCore = MeetyouPlayerEngine.Instance().bindPlayer(TAG_PLAYER_INSTANCE);
        }
        return mMeetyouPlayerCore;
    }


    /**
     * 释放视频
     */
    public static void releaseAllVideos() {
        try {
            JCMediaManager.getInstance().operationMediaPlayer(null, false, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 检查是否切换了请求
     */
    public static void checkMediaManagerChanged(String playingUniqueId) {
        if (!StringUtil.isNull(JCMediaManager.getInstance().mPlayingUniqueId)
                && !StringUtil.isNull(playingUniqueId)
                && !playingUniqueId.equals(JCMediaManager.getInstance().mPlayingUniqueId)) {
            releaseAllVideos();
        }
    }

    /**
     * 检查是否切换了请求
     */
    public static void resetCurrentMediaManagerStatus(String playingUniqueId) {
        if (!StringUtil.isNull(JCMediaManager.getInstance().mPlayingUniqueId)
                && !StringUtil.isNull(playingUniqueId)
                && playingUniqueId.equals(JCMediaManager.getInstance().mPlayingUniqueId)) {
            releaseAllVideos();
        }
    }

    /**
     * 操作MediaPlayer
     *
     * @param isPrepare   是否准备播放 true:准备播放 false:释放资源
     * @param jcVideoInfo 视频播放信息
     */
    public synchronized void operationMediaPlayer(Context context, boolean isPrepare, JCVideoInitMsg jcVideoInfo) {
        if (isPrepare && context != null) {
            prepare(context, jcVideoInfo);
        } else {
            release();
        }
    }

    private void prepare(Context context, final JCVideoInitMsg jcVideoInfo) {
        if (TextUtils.isEmpty(jcVideoInfo.getUrl())) return;
        try {
            currentVideoWidth = 0;
            currentVideoHeight = 0;
            if (mMeetyouPlayerCore == null) {
                mMeetyouPlayerCore = createMeetyouPlayerLifer();
            }
            mPlayingUniqueId = jcVideoInfo.getUniqueRequestVideoId();
            mMeetyouPlayerCore.setFetcher(true);
            setPlaying(true);
            mMeetyouPlayerCore.setOnVideoSizeChangeListener(this);
            mMeetyouPlayerCore.addOnBufferingListener(this);
            mMeetyouPlayerCore.setOnPreparedListener(new IPlayerCallback.OnPreparedListener() {
                @Override
                public void onPrepared() {
                    if (jcVideoInfo != null && mMeetyouPlayerCore != null) {
                        if (jcVideoInfo.isNeedVoice()) {
                            mMeetyouPlayerCore.setVolume(1f, 1f);
                        } else {
                            mMeetyouPlayerCore.setVolume(0f, 0f);
                        }
                    }
                    if (mJCMediaPlayerListener != null) {
                        mJCMediaPlayerListener.onPrepared();
                    }
                }
            });
            mMeetyouPlayerCore.setOnCompleteListener(this);
            mMeetyouPlayerCore.setOnErrorListener(this);
            mMeetyouPlayerCore.addOnProgressListener(this);
            mMeetyouPlayerCore.setOnStartListener(this);
            mMeetyouPlayerCore.setOnPauseListener(this);
            mMeetyouPlayerCore.setOnStopListener(this);
            mMeetyouPlayerCore.setMeetyouPlayerView(jcVideoInfo.getTextureView());
            mMeetyouPlayerCore.setPlaySource(jcVideoInfo.getUrl());
//            mMeetyouPlayerCore.setUri(Uri.parse(jcVideoInfo.getUrl()));
//            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(context,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
//                    new PermissionsResultAction() {
//                        @Override
//                        public void onGranted() {
            mMeetyouPlayerCore.prepare();
            mMeetyouPlayerCore.play();
//                        }
//
//                        @Override
//                        public void onDenied(String permission) {
//
//                        }
//                    });
        } catch (Exception e) {
            setPlaying(false);
            e.printStackTrace();
        }
    }


    @Override
    public void onVideoSizeChange(MeetyouPlayerView meetyouPlayerView, int width, int height) {
        currentVideoWidth = width;
        currentVideoHeight = height;
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onVideoSizeChanged();
        }
    }

    @Override
    public void onBuffering(int procent) {
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onBufferingUpdate(procent);
        }
    }

    @Override
    public void onError(int what) {
        setPlaying(false);
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onError(what);
        }
    }

    @Override
    public void onComplete() {
        setPlaying(false);
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onCompletion();
        }
    }

    @Override
    public void onPorgress(long cur, long total) {
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onProgressUpdate(cur, total);
        }
    }

    @Override
    public void onStart() {
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onStartCallback();
        }
    }

    @Override
    public void onPause() {
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onPauseCallback();
        }
    }

    @Override
    public void onStop() {
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener.onStopCallback();
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        setPlaying(false);
        if (mMeetyouPlayerCore != null) {
            try {
                mMeetyouPlayerCore.stop();
                if (mJCMediaPlayerListener != null) {
                    mJCMediaPlayerListener.onStopCallback();
                }
            } catch (Exception ex) {
            }
            try {
                mMeetyouPlayerCore.release();
            } catch (Exception ex) {
            }
            mMeetyouPlayerCore.setOnPreparedListener(null);
            mMeetyouPlayerCore = null;
        }
        if (mJCMediaPlayerListener != null) {
            mJCMediaPlayerListener = null;
        }
        mPlayingUniqueId = null;
    }

    public JCMediaPlayerListener getJCMediaPlayerListener() {
        return mJCMediaPlayerListener;
    }

    public void setJCMediaPlayerListener(JCMediaPlayerListener JCMediaPlayerListener, String playingUniqueId) {
        if (JCMediaPlayerListener != null || playingUniqueId == mPlayingUniqueId) {
            mJCMediaPlayerListener = JCMediaPlayerListener;
        }
    }

    public MeetyouPlayer getMediaPlayer() {
        return mMeetyouPlayerCore;
    }

    public boolean isPlaying() {
        return isPlaying && !isPause;
    }

    public synchronized void setPlaying(boolean isPlaying) {
        //EventBus.getDefault().post(new VideoManagerStatus(isPlaying));
        this.isPlaying = isPlaying;
        isPause = false;
    }

    public void setPause(boolean pause) {
        //EventBus.getDefault().post(new VideoManagerStatus(false));
        isPause = pause;
    }

    public boolean isPause() {
        return isPause;
    }
}
