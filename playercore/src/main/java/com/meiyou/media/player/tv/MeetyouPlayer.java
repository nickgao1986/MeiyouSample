package com.meiyou.media.player.tv;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;

import com.meetyou.media.player.client.MeetyouPlayerEngine;
import com.meetyou.media.player.client.engine.FileMediaDataSource;
import com.meetyou.media.player.client.engine.MeetyouMediaEngine;
import com.meetyou.media.player.client.fetcher.AbstractFetcher;
import com.meetyou.media.player.client.fetcher.DiskContentFetcher;
import com.meetyou.media.player.client.fetcher.IFetcher;
import com.meetyou.media.player.client.player.IMeetyouPlayer;
import com.meetyou.media.player.client.player.MeetyouPlayerCode;
import com.meetyou.media.player.client.ui.MeetyouPlayerView;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by Linhh on 17/3/1.
 */

public class MeetyouPlayer extends AbstractMeetyouPlayer implements
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMeetyouPlayer.OnSourceLoadListener{
    private final String TAG = "MeetyouPlayerCore";
    private IMediaPlayer m_MediaPlayer;//播放器
    private FileMediaDataSource mFileMediaDataSource;
    private IFetcher<String> mNetMediaDataSource;
    private MeetyouMediaEngine mIMediaDataSource;

    protected List<IPlayerCallback.OnBufferingListener> mOnBufferingListeners = new ArrayList<>();
    protected List<IPlayerCallback.OnProgressListener> mOnProgressListeners = new ArrayList<>();


    private void initOption(){
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", mUseHardware ? 1 : 0);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", mUseSDL ? 1 : 0);
//				((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 200 * 1024);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 20);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "first-high-water-mark-ms", 100);
        //packet-buffering参数用于关闭缓冲
        //安全监测降到最低,否则会产生部分视频无法播放
        //((IjkMediaPlayer) m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "protocol_whitelist", "concat,ffconcat,file,subfile,http,https,tls,rtp,tcp,udp,crypto,cache");
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe" ,0);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        ((IjkMediaPlayer)m_MediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
    }

    private void initIJKPlayer(){
        m_MediaPlayer = new IjkMediaPlayer();
        m_MediaPlayer.setOnPreparedListener(this);
        m_MediaPlayer.setOnVideoSizeChangedListener(this);
        m_MediaPlayer.setOnCompletionListener(this);
        m_MediaPlayer.setOnErrorListener(this);
        m_MediaPlayer.setOnInfoListener(this);
    }

    @Override
    public void prepare() {
        super.prepare();
        String play_source = getPlaySource();
        if(TextUtils.isEmpty(play_source)){
            //播放地址为空,不能准备
            onError(MeetyouPlayerCode.PLAY_SOURCE_NULL_ERROR);
            return;
        }

        if (m_MediaPlayer == null) {
            //初始化播放器
            initIJKPlayer();
        }

        if (m_MediaPlayer != null) {
            if (isPlaying()) {
                stop();
            }
            m_MediaPlayer.reset();
            initOption();
            initSurface();
            //设置音量
            setVolume(getLeftVolume(), getRightVolume());
            AudioManager am = (AudioManager) MeetyouPlayerEngine.Instance().getContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            try {
                initMediaSource(play_source);
            } catch (Exception e) {
                e.printStackTrace();
                onError(MeetyouPlayerCode.SET_SOURCE_ERROR);
            }
        }
    }

    @Override
    public void release() {
        super.release();
        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
        }
        AudioManager am = (AudioManager) MeetyouPlayerEngine.Instance().getContext().getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
        mOnBufferingListeners.clear();
        mOnProgressListeners.clear();
        if(mIMediaDataSource != null){
            try {
                mIMediaDataSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mIMediaDataSource = null;
            mNetMediaDataSource = null;
        }
        if (m_MediaPlayer != null){
            if (isPlaying()) {
                stop();
            }
            m_MediaPlayer.release();
            m_MediaPlayer = null;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(m_MediaPlayer != null && mIsPlaying){
                onProgress(m_MediaPlayer.getCurrentPosition(), m_MediaPlayer.getDuration());
                updateProgress();
            }
        }
    };

    private void updateProgress(){
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    public void play() {
        super.play();
        if(m_MediaPlayer != null && mIsPrepared){
            try {
                m_MediaPlayer.start();
            } catch (IllegalStateException e) {
                DebugLog.e(TAG, "MediaPlayer start Failed!");
                return;
            }
            mIsPlaying = true;
            mIsStopped = false;
            mIsPaused = false;
            mTotalDuration = m_MediaPlayer.getDuration();
            onPlayStart();
            updateProgress();
        }
    }

    public void initSurface(){
        MeetyouPlayerView meetyouPlayerView = getMeetyouPlayerView();
        if(meetyouPlayerView == null || m_MediaPlayer == null){
            return;
        }

        m_MediaPlayer.setSurface(meetyouPlayerView.getSurface());
    }

    @Override
    public void setMeetyouPlayerView(MeetyouPlayerView meetyouPlayerView) {
        super.setMeetyouPlayerView(meetyouPlayerView);
        if(m_MediaPlayer != null && meetyouPlayerView != null) {
            m_MediaPlayer.setSurface(meetyouPlayerView.getSurface());
        }
    }

    public void initMediaSource(String play_source) throws Exception{
        if(play_source.startsWith("/")){
            //文件播放
            if(mFileMediaDataSource == null){
                mFileMediaDataSource = new FileMediaDataSource(new File(play_source));
            }
            m_MediaPlayer.setDataSource(mFileMediaDataSource);
            afterSetSource();
        }else if(play_source.startsWith("http") && isUsefetcher()){
            //网络缓冲播放
            if(mNetMediaDataSource == null){
                mNetMediaDataSource = new DiskContentFetcher(MeetyouPlayerEngine.Instance().getOkHttpClient());
                mNetMediaDataSource.setFetcherListener(new AbstractFetcher.FetcherListener() {
                    @Override
                    public void onProduceMedia(String source, long total_size) {

                    }

                    @Override
                    public void onIMediaDataSource(MeetyouMediaEngine iMediaDataSource) {
                        if(m_MediaPlayer != null && iMediaDataSource.getSource().equals(getPlaySource())) {
                            if(mIMediaDataSource != null){
                                try {
                                    mIMediaDataSource.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            mIMediaDataSource = iMediaDataSource;
                            m_MediaPlayer.setDataSource(iMediaDataSource);
                            afterSetSource();
                        }
                    }

                    @Override
                    public void onBuffering(int percent) {
                        MeetyouPlayer.super.onBuffering(percent);
                    }

                    @Override
                    public void onError(int error) {
                        MeetyouPlayer.super.onError(error);
                    }

                    @Override
                    public void onLoad(boolean loading) {
                        MeetyouPlayer.super.onLoad(loading);
                    }
                });
            }
            mNetMediaDataSource.fetch(play_source);
        }else{
            m_MediaPlayer.setDataSource(play_source);
        }
    }

    @Override
    public void setVolume(float left, float right) {
        super.setVolume(left, right);
        if(m_MediaPlayer != null){
            m_MediaPlayer.setVolume(left, right);
        }
    }

    private void afterSetSource(){
        m_MediaPlayer.setScreenOnWhilePlaying(true);
        m_MediaPlayer.prepareAsync();
    }

    @Override
    public void seek2(long m) {
        super.seek2(m);
        if(m_MediaPlayer != null && mIsPlaying){
            m_MediaPlayer.seekTo(m);
            //如果seek不为0就直接seek,并且设置0
            setSeekToPosition(0);
            onSeek(m);
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (m_MediaPlayer != null && mIsPrepared) {
            m_MediaPlayer.pause();
            mIsPaused = true;
            mIsStopped = false;
            mIsPlaying = false;
            onPause();
        }
    }

    @Override
    public long getTotalDuration() {
        if(m_MediaPlayer != null){
            return m_MediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void pauseFetcher() {
        if(mIMediaDataSource != null){
            mIMediaDataSource.pause();
        }
    }

    @Override
    public void remuseFetcher() {
        if(mIMediaDataSource != null){
            mIMediaDataSource.remuse();
        }
    }

    @Override
    public long getCurrentPos() {
        if(m_MediaPlayer != null){
            return m_MediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void stop() {
        super.stop();
        if(m_MediaPlayer != null){
            m_MediaPlayer.stop();
            onStop();
            if(mIMediaDataSource != null){
                try {
                    mIMediaDataSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mIsStopped = true;
            mIsPaused = false;
            mIsPlaying = false;
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mIsPrepared = true;
        mIsStopped = false;
        mIsPaused = false;
        onPrepare();
        if(isPlay()){
            //如果需要play,就直接play
            play();
        }
        if(getSeekToPosition() != 0){
            seek2(getSeekToPosition());
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        if(getMeetyouPlayerView() != null) {
            getMeetyouPlayerView().setVideoSampleAspectRatio(sar_num, sar_den);
//            getMeetyouPlayerView().setVideoSize(width, height);//如果需要更新显示区域请设置这个
        }
        onVideoSize(width, height);
    }

    @Override
    public void setRotation(int rotation) {
        super.setRotation(rotation);
        if(getMeetyouPlayerView() != null){
            getMeetyouPlayerView().setVideoRotation(rotation);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                DebugLog.e(TAG, "Bad interleaving of media file, audio/video are not well-formed, extra is " + extra);
                break;

            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                DebugLog.i(TAG, "Buffering start");
                break;

            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                DebugLog.i(TAG, "Buffering end, start play");
                break;

            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                DebugLog.w(TAG, "A new set of metadata is available, extra is " + extra);
                break;

            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                DebugLog.e(TAG, "The stream cannot be seeked, extra is " + extra);
                break;

            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                DebugLog.w(TAG, "It's too complex for the decoder, extra is " + extra);
                break;

            case IMediaPlayer.MEDIA_INFO_UNKNOWN:
                DebugLog.w(TAG, "Unknown info, extra is " + extra);
                break;

            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                //extra = 90 横屏拍摄视频,0 正常视频
                //改变texture方向
                setRotation(extra);
                DebugLog.w(TAG, "ROTATION changed, extra is " + extra);
                break;

            default:
                DebugLog.i(TAG, "Unknown info code: " + what + ", extra is " + extra);
                break;
        }

        return true;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        switch(what) {
            case IMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                DebugLog.e(TAG, "Media Server died, extra is " + extra);
                onError(MeetyouPlayerCode.EXCEPTION_ERROR);
                break;
            case IMediaPlayer.MEDIA_ERROR_UNKNOWN:
                DebugLog.e(TAG, "Unknown error, extra is "+extra);
                onError(MeetyouPlayerCode.EXCEPTION_ERROR);
                break;
            case IMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                DebugLog.e(TAG, "File not valid for progressive playback, extra is " + extra);
                onError(MeetyouPlayerCode.UNSUPPORT_FILE_ERROR);
                break;
            case IMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                DebugLog.e(TAG, "Codec unsupport, extra is " + extra);
                onError(MeetyouPlayerCode.UNSUPPORT_FILE_ERROR);
                break;
            default:
                DebugLog.e(TAG, "error what is " + what + "  code is " + extra);
                onError(MeetyouPlayerCode.EXCEPTION_ERROR);
                break;
        }
        stop();

        return true;
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        mIsComplete = true;
        mIsPlaying = false;
        onComplete();
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {

    }

    @Override
    public void onSourceLoad(boolean loading) {

    }
}
