package com.meetyou.media.player.client.fetcher;

/**
 * Created by Linhh on 17/1/17.
 */

public interface IMeetyouPlayerListener {
    void onBuffering(int percent);
    void onError(int error);
    void onLoad(boolean loading);
}
