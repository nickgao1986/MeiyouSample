package com.meetyou.media.player.client.fetcher;

import com.meetyou.media.player.client.engine.AbstractMeetyouMediaDataSource;
import com.meiyou.media.player.tv.playengine.MeetyouPlayerDef;

import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * media 下载引擎
 * Created by Linhh on 17/1/17.
 */

public class NetworkMediaDownloader {

    protected OkHttpClient mOkHttpClient;
    protected String mSource;
    protected long mTotalSize;

    protected boolean mClose = false;

    private MediaPartManager mMediaPartManager;
    private String mFileName;

    private IMeetyouPlayerListener mIMeetyouNetworkListener;

    public NetworkMediaDownloader(OkHttpClient okHttpClient,
                                  MediaPartManager mediaPartManager,
                                  String file_name,
                                  String source,
                                  long totalSize,
                                  IMeetyouPlayerListener iMeetyouNetworkListener){
        this.mOkHttpClient = okHttpClient;
        this.mSource = source;
        this.mTotalSize = totalSize;
        this.mMediaPartManager = mediaPartManager;
        this.mFileName = file_name;
        this.mIMeetyouNetworkListener = iMeetyouNetworkListener;
        this.mClose = false;

    }

    public long getPageSize(){
        long page_size = mTotalSize % AbstractMeetyouMediaDataSource.PAGE_MAX_SIZE == 0 ? mTotalSize / AbstractMeetyouMediaDataSource.PAGE_MAX_SIZE : mTotalSize / AbstractMeetyouMediaDataSource.PAGE_MAX_SIZE + 1;
        return page_size;
    }


    public boolean downloadPart(final long part_id){

        if(mClose){
            DebugLog.d("meetyou_player", "缓冲入口已关闭");
            return false;
        }
        if(part_id + 1> getPageSize()){
            //超过最大size数量
            return false;
        }
        int status = mMediaPartManager.queryPartStatus(part_id);
        if(status == MediaPartManager.FETCHER_FETCHING || status == MediaPartManager.FETCHER_OK){
            //如果是正在下载和已经完成就返回状态
            return false;
        }

        DebugLog.d("meetyou_player", "缓冲数据启动");

        mMediaPartManager.setCurrentPart(part_id);

        mMediaPartManager.updateFetching(part_id);

        long start = part_id * AbstractMeetyouMediaDataSource.PAGE_MAX_SIZE;
        long end = (part_id + 1) *  AbstractMeetyouMediaDataSource.PAGE_MAX_SIZE + 1L;
        if(end > mTotalSize){
            end = mTotalSize + 1L;
        }


//        long page_size = mTotalSize % PAGE_MAX_SIZE == 0 ? mTotalSize / PAGE_MAX_SIZE : mTotalSize / PAGE_MAX_SIZE + 1;

        Request request = new Request.Builder()
                .url(mSource)
                .header("RANGE", String.format("bytes=%d-%d", start, end))
                .build();

        DebugLog.d("meetyou_player", "下载分块:第" + part_id+"块,数据位置:"+start + "---->" + end);

        try {
            mOkHttpClient.newCall(request).enqueue(new MediaCallBack(part_id, mFileName, start, mMediaPartManager));
        } catch (Exception e) {
            mIMeetyouNetworkListener.onError(MeetyouPlayerDef.FETCH_FILE_ERROR);
            e.printStackTrace();
        }
        return true;
    }

    public void close(){
        mClose = true;
    }

    public void pause(){
        mClose = true;
    }

    public void remuse(){
        mClose = false;
    }

    class MediaCallBack implements Callback{
        private long mPartID;
        private MediaPartManager mMediaPartManager;
        private long mStart;
        private RandomAccessFile mRandomAccessFile;

        public MediaCallBack(long partID, String file_name, long start, MediaPartManager mediaPartManager) throws Exception{
            this.mPartID = partID;
            this.mMediaPartManager = mediaPartManager;
            this.mStart = start;
            this.mRandomAccessFile = new RandomAccessFile(file_name,"rw");
        }

        @Override
        public void onFailure(Call call, IOException e) {
            this.mMediaPartManager.updateFail(this.mPartID);
            mIMeetyouNetworkListener.onError(MeetyouPlayerDef.FETCH_FILE_ERROR);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){
                ResponseBody body = response.body();
                BufferedSource source = body.source();
                long writeOffset = this.mStart;
                int bufferSize = 1024 * 10; // 10KB
                byte[] buffer = new byte[bufferSize];
                try {
                    for (int bytesRead; ((bytesRead = source.read(buffer)) != -1); ) {
                        if(mClose){
                            this.mMediaPartManager.updatePending(this.mPartID);
                            DebugLog.d("meetyou_player", "缓冲入口已关闭");
                            return;
                        }
                        DebugLog.d("meetyou_player", "写入中:第" + this.mPartID+"块,seek to:" + writeOffset + ",写入到:"+ (writeOffset + bytesRead));
                        this.mRandomAccessFile.seek(writeOffset);
                        this.mRandomAccessFile.write(buffer, 0, bytesRead);
                        writeOffset += bytesRead;

                    }
                    source.close();
                    body.close();
                    if(this.mMediaPartManager.isCurrentPart(this.mPartID) && mIMeetyouNetworkListener != null){
                        //当前下载页计算buffering
                        float buffer_f = (writeOffset * 1.00F) / mTotalSize * 100;
                        mIMeetyouNetworkListener.onBuffering((int)buffer_f);
                    }
                    DebugLog.d("meetyou_player", "下载完成:第" + this.mPartID+"块,"+this.mStart + "---->"+writeOffset);
                    this.mMediaPartManager.updateSuccess(this.mPartID);
                    //下载完成更新下一个part
                    if(this.mMediaPartManager.updateCurrentPart(this.mPartID)){
                        //下载下一分块
                        downloadPart(this.mMediaPartManager.getCurrentPart());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.mMediaPartManager.updateFail(this.mPartID);
                    mIMeetyouNetworkListener.onError(MeetyouPlayerDef.FETCH_FILE_ERROR);
                }

            }
        }
    }
}
