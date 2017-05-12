package com.meiyou.media.player.tv.playengine;

import com.meetyou.media.player.client.ui.MeetyouPlayerView;

/**
 * Created by Linhh on 16/12/26.
 */
public interface IPlayerCallback {

    public interface OnVideoSizeChangeListener{
        void onVideoSizeChange(MeetyouPlayerView view, int width, int height);
    }

    public interface OnBufferingListener{
        void onBuffering(int currentBufferPercentage);
    }

    public interface OnCompleteListener{
        void onComplete();
    }

    public interface OnPreparedListener{
        void onPrepared();
    }

    public interface OnErrorListener{
        void onError(int error);
    }

    public interface OnStartListener{
        void onStart();
    }

    public interface OnPauseListener{
        void onPause();
    }

    public interface OnSeekListener{
        void onSeek(long seek);
    }

    public interface OnStopListener{
        void onStop();
    }

    public interface OnProgressListener{
        void onPorgress(long cur, long total);
    }

    public interface OnLoadListener{
        void onLoad(boolean loading);
    }

	void onSetVideoViewLayout(MeetyouPlayerView view, int width, int height);
	void onBufferingback(int currentBufferPercentage);
    void onCompletePlayback();
    void onPreparedPlayback();
    void onErrorAppeared(int error);
//    void onBufferingStart();
//    void onBufferingEnd();
    void onPlayerStart();
    void onPlayerPause();
    void onPlayerStop();
}
