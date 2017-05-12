package com.meetyou.media.player.client;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;

import com.meetyou.media.player.client.util.Utils;
import com.meiyou.media.player.tv.MeetyouPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Linhh on 17/3/2.
 */

public class MeetyouPlayerEngine {

    private static MeetyouPlayerEngine mMeetyouPlayerEngine;
    private final HashMap<String, MeetyouPlayer> mPlayers = new HashMap<>();
    private boolean mIsInit = false;
    private boolean mIsDebug = false;

    private Context mContext;

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public MeetyouPlayerEngine(){

    }

    public OkHttpClient getOkHttpClient(){
        return mClient;
    }

    public HashMap<String, MeetyouPlayer> getPlayers(){
        return mPlayers;
    }

    public MeetyouPlayer bindPlayer(String key){
        if(mPlayers.containsKey(key)){
            return mPlayers.get(key);
        }
        MeetyouPlayer meetyouPlayerCore = new MeetyouPlayer();
        mPlayers.put(key, meetyouPlayerCore);
        return meetyouPlayerCore;
    }

    public boolean unbindPlayer(String key){
        if(mPlayers.containsKey(key)){
            MeetyouPlayer meetyouPlayerCore = mPlayers.get(key);
            meetyouPlayerCore.release();
            return true;
        }
        return false;
    }

    public static MeetyouPlayerEngine Instance(){
        if(mMeetyouPlayerEngine == null){
            mMeetyouPlayerEngine = new MeetyouPlayerEngine();
        }
        return mMeetyouPlayerEngine;
    }

    public Context getContext(){
        return mContext;
    }

    public void init(Context context, boolean debug){
        if(mIsInit){
            return;
        }
        mIsDebug = debug;
        mIsInit = true;
        mContext = context;
        initLibs();
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    public void init(Context context){
        init(context, false);
    }

    public void release(){
        IjkMediaPlayer.native_profileEnd();
        System.gc();
    }

    public boolean isDebug(){
        return mIsDebug;
    }

    public void formatActivity(Activity activity){
        if(activity == null){
            return;
        }
        //设置window的属性和surfaceview的一致，避免首次进入时由于属性不一导致windowmanger销毁当前窗口重建而产生的闪屏
        activity.getWindow().setFormat(PixelFormat.TRANSPARENT);
        activity.getWindow().setBackgroundDrawable(null);
    }

    private void initLibs(){
        IjkMediaPlayer.loadLibrariesOnce(null);
    }

    /**
     * 清除缓冲区
     */
    public void clearCache(){
        Utils.clearCacheRoot();
    }

    public void clearCache(Utils.PlayerCacheFilter playerCacheFilter){
        Utils.clearCache(playerCacheFilter);
    }

    /**
     * 指定时间清理缓存
     * @param time
     */
    public void clearCacheBeforeTime(long time){
        Utils.clearCacheBeforeTime(time);
    }

    /**
     * 清除特定某个缓冲
     * @param name
     * @throws Exception
     */
    public void clearCache(String name) throws Exception{
        Utils.clearCahce(name);
    }

    /**
     * 获得整个缓冲区
     * @return
     */
    public File getRootCache(){
        return Utils.getCacheRootFile();
    }

    /**
     * 获取缓冲区内数据
     * @return
     */
    public File[] getCaches(){
        File file = Utils.getCacheRootFile();
        File[] files = file.listFiles();
        return files;
    }
}
