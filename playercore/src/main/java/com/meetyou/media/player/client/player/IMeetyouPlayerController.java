package com.meetyou.media.player.client.player;

/**
 * Created by Linhh on 17/3/1.
 */

public interface IMeetyouPlayerController {

    public void prepare();

    public void play();

    public void pause();

    public void stop();

    public void seek2(long m);

    public void setVolume(float left, float right);

    public void setPlaySource(String playurl);

    public void setFetcher(boolean use);

    public void release();

    public boolean isPlaying();

    public boolean isPerpared();

    public boolean isPaused();

    public boolean isStopped();

    public boolean isCompleted();

    public long getCurrentPos();

    public long getTotalDuration();

    public int getRotation();

    public int getVideoHeight();

    public int getVideoWidth();

    public void pauseFetcher();

    public void remuseFetcher();
}
