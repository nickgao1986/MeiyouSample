package com.meiyou.media.player.tv;

import android.net.Uri;
import android.view.Surface;

import com.meetyou.media.player.client.ui.MeetyouPlayerView;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

import java.util.List;

/**
 * Created by Linhh on 16/12/26.
 */

public interface MeetyouPlayerLifer {
    public final static int FETCHER_PAUSE = 1;
    public final static int FETCHER_REMUSE = 2;
    public void turnOn();

    public void turnOff();

    public void reset();

    public AbstractMeetyouPlayer getPlayer();

    public void setUri(Uri uri);

    public void setUri(String uri);

    public void setUri(List<MeetyouTrackInfo> uris);

    public Uri getUri();

    public void play();

    public void pause();

    public void stop();

    public boolean isPlaying();

    public boolean isPaused();

    public boolean isStopped();

    public boolean isPrepared();

    public void seekTo(long msec);

    public long getCurrentPos();

    public long getDuration();

    public void fetcherStatus(int status);

    public void setView(MeetyouPlayerView view);

    public void setSurface(Surface surface);

    @Deprecated
    public void registerPlayerCallBack(IPlayerCallback playerCallback);

    public void setOnVideoSizeChangeListener(IPlayerCallback.OnVideoSizeChangeListener onVideoSizeChangeListener);

    public void setOnBufferingListener(IPlayerCallback.OnBufferingListener onBufferingListener);

    public void setOnCompleteListener(IPlayerCallback.OnCompleteListener onCompleteListener);

    public void setOnPreparedListener(IPlayerCallback.OnPreparedListener onPreparedListener);

    public void setOnErrorListener(IPlayerCallback.OnErrorListener onErrorListener);

    public void setOnStartListener(IPlayerCallback.OnStartListener onStartListener);

    public void setOnPauseListener(IPlayerCallback.OnPauseListener onPauseListener);

    public void setOnStopListener(IPlayerCallback.OnStopListener onStopListener);

    public void setOnProgressListener(IPlayerCallback.OnProgressListener onProgressListener);

    public void setUIManager(PlayerUIManager playerUIManager);

    public void useFetcher(boolean use);

    public void setVolume(float volume);
}
