package com.meetyou.media.player.client.fetcher;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Linhh on 17/1/17.
 */

public class MediaPartManager {

    public static final int FETCHER_PENDING = 0;
    public static final int FETCHER_FETCHING = 1;
    public static final int FETCHER_OK = 2;
    public static final int FETCHER_FAIL = 3;

    public long mCurrentPart = 0;//当前处理的分段

    protected Map<Long, Integer> mStatusMap;
    private MediaInfoManager mMediaInfoManager;

    public MediaPartManager(MediaInfoManager mediaInfoManager){
        mMediaInfoManager = mediaInfoManager;
        produce();
    }

    private void produce(){
        MediaInfo mediaInfo = mMediaInfoManager.getInfo();
        if(mediaInfo != null){
            mStatusMap = mediaInfo.getStatusMap();
        }
        if(mStatusMap == null){
            mStatusMap = new HashMap<>();
        }

        for(Map.Entry<Long, Integer> entry : mStatusMap.entrySet()){
            if(entry.getValue() != FETCHER_OK){
                //重置所有fetch流为等待
                updatePending(entry.getKey());
            }
        }
        mediaInfo.setStatusMap(mStatusMap);
        mMediaInfoManager.saveInfo(mediaInfo);
    }

    public void save(){
        MediaInfo mediaInfo = mMediaInfoManager.getInfo();
        if(mediaInfo != null){
            mediaInfo.setStatusMap(mStatusMap);
        }
        mMediaInfoManager.saveInfo(mediaInfo);
    }

    public int queryPartStatus(long part_id){
        if(!mStatusMap.containsKey(part_id)){
            updatePartStatus(part_id, FETCHER_PENDING);
        }
        //判断当前分块的下载状态
        int status = mStatusMap.get(part_id);
        return status;
    }

    private void updatePartStatus(long part_id, int status){
        mStatusMap.put(part_id, status);
        save();
    }

    public void updateFail(long part_id){
        updatePartStatus(part_id, FETCHER_FAIL);
        save();
    }

    public void updateSuccess(long part_id){
        updatePartStatus(part_id, FETCHER_OK);
        save();
    }

    public void updatePending(long part_id){
        updatePartStatus(part_id, FETCHER_PENDING);
        save();
    }

    public void updateFetching(long part_id){
        updatePartStatus(part_id, FETCHER_FETCHING);
        save();
    }

    public boolean isCurrentPart(long part_id){
        return part_id == mCurrentPart;
    }

    public boolean updateCurrentPart(long part_id){
        if(part_id != mCurrentPart){
            //不是当前下载的分块
            return false;
        }
        mCurrentPart ++ ;
        return true;
    }

    public long getCurrentPart(){
        return mCurrentPart;
    }

    public void setCurrentPart(long part){
        mCurrentPart = part;
    }
}
