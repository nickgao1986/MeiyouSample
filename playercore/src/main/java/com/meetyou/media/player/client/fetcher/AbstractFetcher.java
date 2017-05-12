package com.meetyou.media.player.client.fetcher;

import com.meetyou.media.player.client.engine.MeetyouMediaEngine;

import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by gaoyoujian on 2017/5/10.
 */

public class AbstractFetcher {

    protected OkHttpClient mOkHttpClient;
    protected FetcherListener mFetcherListener;
    protected String mSource;

    public interface FetcherListener{
        public void onProduceMedia(String source, long total_size);
        public void onIMediaDataSource(MeetyouMediaEngine iMediaDataSource);
        public void onBuffering(int percent);
        public void onError(int error);
        public void onLoad(boolean loading);
    }

    public void setFetcherListener(FetcherListener fetcherListener){
        mFetcherListener = fetcherListener;
    }

    protected IMeetyouPlayerListener mIMeetyouNetworkListener = new IMeetyouPlayerListener() {
        @Override
        public void onBuffering(int percent) {
            if(mFetcherListener != null){
                DebugLog.d("player_buffer",percent + "%");
                mFetcherListener.onBuffering(percent);
            }
        }

        @Override
        public void onError(int error) {
            if(mFetcherListener != null){
//                DebugLog.d("player_buffer",percent + "%");
                mFetcherListener.onError(error);
            }
        }

        @Override
        public void onLoad(boolean loading) {
            if(mFetcherListener != null){
//                DebugLog.d("player_buffer",percent + "%");
                mFetcherListener.onLoad(loading);
            }
        }
    };

    protected void produceMedia(String source, long total_size){
        MeetyouMediaEngine mediaDataSource = null;
        try {
            mediaDataSource = new MeetyouMediaEngine(mOkHttpClient,
                    source,
                    total_size,
                    mIMeetyouNetworkListener);
            if(mFetcherListener != null){
                mFetcherListener.onIMediaDataSource(mediaDataSource);
                mFetcherListener.onProduceMedia(source,total_size);
            }
//            mPlayer.setIMediaDataSource(new MeetyouMediaEngine(mOkHttpClient,
//                    source,
//                    total_size,
//                    mIMeetyouNetworkListener));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
