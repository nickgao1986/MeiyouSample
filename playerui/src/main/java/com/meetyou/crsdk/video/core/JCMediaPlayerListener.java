package com.meetyou.crsdk.video.core;

/**
 * Created by wuzhongyou on 2016/11/14.
 */
public interface JCMediaPlayerListener {

    void onVideoSizeChanged();

    void onBufferingUpdate(int percent);

    void onProgressUpdate(long cur, long total);

    void onPrepared();

    void onCompletion();

    void onError(int what);

    void onPause();

    void onStartCallback();

    void onPauseCallback();

    void onStopCallback();
}
