package com.meiyou.media.player.tv;

import android.os.Handler;
import android.os.Looper;

import com.meetyou.media.player.client.player.IMeetyouPlayerController;
import com.meetyou.media.player.client.ui.MeetyouPlayerView;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linhh on 17/3/1.
 */

public abstract class AbstractMeetyouPlayer implements IMeetyouPlayerController {
    private final String TAG = "AbstractMeetyouPlayer";

    protected String mPlaySource;//当前播放地址

    private boolean mPrepare = false;//是否准备中
    private boolean mStop = false;//是否停止
    private boolean mPause = false;//是否暂停
    private boolean mPlay = false;//是否播放中

    protected boolean mIsPrepared = false;//是否准备中
    protected boolean mIsStopped = true;//是否停止
    protected boolean mIsPaused = false;//是否暂停
    protected boolean mIsPlaying = false;//是否播放中
    protected boolean mIsComplete = false;

    protected boolean mUsefetcher = false;//是否使用缓冲
    protected boolean mUseHardware = true;
    protected boolean mUseSDL = false;

    private float mLeftVolume = 1.0f;//左声道音量
    private float mRightVolume = 1.0f;//右声道音量

    protected long mCurrentPosition = 0;//当前播放位置
    protected long mTotalDuration = 0;//总长度
    private long mSeekToPosition = 0;//需要seek到的位置
    private int mRotation = 0;//

    protected int mVideoHeight = 0;
    protected int mVideoWidth = 0;

    private MeetyouPlayerView mMeetyouPlayerView;

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected IPlayerCallback.OnVideoSizeChangeListener mOnVideoSizeChangeListener;
    protected List<IPlayerCallback.OnBufferingListener> mOnBufferingListeners = new ArrayList<>();
    protected IPlayerCallback.OnCompleteListener mOnCompleteListener;
    protected IPlayerCallback.OnPreparedListener mOnPreparedListener;
    protected IPlayerCallback.OnErrorListener mOnErrorListener;
    protected IPlayerCallback.OnStartListener mOnStartListener;
    protected IPlayerCallback.OnPauseListener mOnPauseListener;
    protected IPlayerCallback.OnStopListener mOnStopListener;
    protected List<IPlayerCallback.OnProgressListener> mOnProgressListeners = new ArrayList<>();
    protected IPlayerCallback.OnLoadListener mOnLoadListener;
    protected IPlayerCallback.OnSeekListener mOnSeekListener;

    @Override
    public void prepare() {
        mPlay = false;
        mStop = false;
        mPause = false;
        mIsComplete = false;
    }

    @Override
    public void play() {
        mPlay = true;
        mStop = false;
        mPause = false;
        mIsComplete = false;
    }

    @Override
    public void pause() {
        mPlay = false;
        mStop = false;
        mPause = true;
        mIsComplete = false;
    }

    public void useHardware(boolean hard){
        mUseHardware = hard;
    }

    public void useSDL(boolean sdl){
        mUseSDL = sdl;
    }

    @Override
    public void stop() {
        mStop = true;
        mPlay = false;
        mPause = false;
        mIsComplete = false;
    }

    @Override
    public void seek2(long m) {
        mSeekToPosition = m;
    }

    @Override
    public void setVolume(float left, float right) {
        mLeftVolume = left;
        mRightVolume = right;
    }

    public int getRotation(){
        return mRotation;
    }

    @Override
    public int getVideoHeight() {
        return mVideoHeight;
    }

    @Override
    public int getVideoWidth() {
        return mVideoWidth;
    }

    public void setRotation(int rotation){
        mRotation = rotation;
    }

    @Override
    public void setPlaySource(String playurl) {
        mPlaySource = playurl;
    }

    @Override
    public void setFetcher(boolean use) {
        mUsefetcher = use;
    }

    @Override
    public void release() {
        mPlaySource = null;//当前播放地址

        mPrepare = false;//是否准备中
        mStop = false;//是否停止
        mPause = false;//是否暂停
        mPlay = false;//是否播放中

        mIsPrepared = false;//是否准备中
        mIsStopped = true;//是否停止
        mIsPaused = false;//是否暂停
        mIsPlaying = false;//是否播放中

        mUsefetcher = false;//是否使用缓冲

        mLeftVolume = 1.0f;//左声道音量
        mRightVolume = 1.0f;//右声道音量

        mCurrentPosition = 0;//当前播放位置
        mTotalDuration = 0;//总长度
        mSeekToPosition = 0;//需要seek到的位置
        mRotation = 0;//

        mMeetyouPlayerView = null;
    }

    @Override
    public boolean isPlaying() {
        return mIsPlaying;
    }

    @Override
    public boolean isPerpared() {
        return mIsPrepared;
    }

    @Override
    public boolean isPaused() {
        return mIsPaused;
    }

    @Override
    public boolean isStopped() {
        return mIsStopped;
    }

    @Override
    public boolean isCompleted() {
        return mIsComplete;
    }

    @Override
    public long getCurrentPos() {
        return mCurrentPosition;
    }

    public long getSeekToPosition() {
        return mSeekToPosition;
    }

    public void setSeekToPosition(long seek2){
        mSeekToPosition = seek2;
    }

    public String getPlaySource() {
        return mPlaySource;
    }

    protected boolean isPrepare() {
        return mPrepare;
    }

    protected boolean isStop() {
        return mStop;
    }

    protected boolean isPause() {
        return mPause;
    }

    protected boolean isPlay() {
        return mPlay;
    }

    public boolean isUsefetcher() {
        return mUsefetcher;
    }

    public float getLeftVolume() {
        return mLeftVolume;
    }

    public float getRightVolume() {
        return mRightVolume;
    }

    public long getTotalDuration() {
        return mTotalDuration;
    }

    public void setMeetyouPlayerView(MeetyouPlayerView meetyouPlayerView){
        mMeetyouPlayerView = meetyouPlayerView;
    }


    public void setOnVideoSizeChangeListener(IPlayerCallback.OnVideoSizeChangeListener mOnVideoSizeChangeListener) {
        this.mOnVideoSizeChangeListener = mOnVideoSizeChangeListener;
    }

    public void addOnBufferingListener(IPlayerCallback.OnBufferingListener mOnBufferingListener) {
        this.mOnBufferingListeners.add(mOnBufferingListener);
    }

    public void setOnCompleteListener(IPlayerCallback.OnCompleteListener mOnCompleteListener) {
        this.mOnCompleteListener = mOnCompleteListener;
    }

    public void setOnPreparedListener(IPlayerCallback.OnPreparedListener mOnPreparedListener) {
        this.mOnPreparedListener = mOnPreparedListener;
    }

    public void setOnErrorListener(IPlayerCallback.OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    public void setOnStartListener(IPlayerCallback.OnStartListener mOnStartListener) {
        this.mOnStartListener = mOnStartListener;
    }

    public void setOnPauseListener(IPlayerCallback.OnPauseListener mOnPauseListener) {
        this.mOnPauseListener = mOnPauseListener;
    }

    public void setOnStopListener(IPlayerCallback.OnStopListener mOnStopListener) {
        this.mOnStopListener = mOnStopListener;
    }

    public void addOnProgressListener(IPlayerCallback.OnProgressListener mOnProgressListener) {
        this.mOnProgressListeners.add(mOnProgressListener);
    }

    public void setOnLoadListener(IPlayerCallback.OnLoadListener mOnLoadListener) {
        this.mOnLoadListener = mOnLoadListener;
    }

    public void setOnSeekListener(IPlayerCallback.OnSeekListener mOnSeekListener){
        this.mOnSeekListener = mOnSeekListener;
    }

    protected void onProgress(final long cur, final long total){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnProgressListeners != null){
                    for(IPlayerCallback.OnProgressListener listener : mOnProgressListeners) {
                        listener.onPorgress(cur, total);
                    }
                }
            }
        });
    }

    protected void onSeek(final long m){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnSeekListener != null){
                    mOnSeekListener.onSeek(m);
                }
            }
        });

    }

    protected void onComplete(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnCompleteListener != null) {
                    mOnCompleteListener.onComplete();
                }
            }
        });

    }

    protected void onPrepare(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnPreparedListener != null){
                    mOnPreparedListener.onPrepared();
                }
            }
        });
    }

    protected void onStop(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnStopListener != null){
                    mOnStopListener.onStop();
                }
            }
        });
    }

    protected void onPlayStart(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnStartListener != null){
                    mOnStartListener.onStart();
                }
            }
        });
    }

    protected void onError(final int error){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnErrorListener != null){
                    mOnErrorListener.onError(error);
                }
            }
        });
    }

    protected void onPause(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnPauseListener != null){
                    mOnPauseListener.onPause();
                }
            }
        });
    }

    protected void onBuffering(final int percent){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnBufferingListeners != null){
                    for(IPlayerCallback.OnBufferingListener listener : mOnBufferingListeners) {
                        listener.onBuffering(percent);
                    }
                }
            }
        });
    }

    protected void onLoad(final boolean loading){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnLoadListener != null){
                    mOnLoadListener.onLoad(loading);
                }
            }
        });

    }

    protected void onVideoSize(final int w, final int h){
        mVideoHeight = h;
        mVideoWidth = w;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mOnVideoSizeChangeListener != null){
                    mOnVideoSizeChangeListener.onVideoSizeChange(getMeetyouPlayerView(), w, h);
                }
            }
        });
    }

    public MeetyouPlayerView getMeetyouPlayerView(){
        return mMeetyouPlayerView;
    }
}
