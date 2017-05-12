package com.meetyou.media.player.client.fetcher;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * 本地缓存数据读取fetcher
 * Created by Linhh on 17/1/17.
 */

public class DiskContentFetcher extends AbstractFetcher implements IFetcher<String> {

    public DiskContentFetcher(OkHttpClient okHttpClient){
        this.mOkHttpClient = okHttpClient;
    }

    @Override
    public void fetch(String source) throws Exception{
        mSource = source;
        MediaInfoManager mediaInfoManager = new MediaInfoManager(source);
        if(!mediaInfoManager.isTempMediaExist() || !mediaInfoManager.isInfoMediaExist()){
            //如果媒体文件不存在,说明需要走network
            File info_file = mediaInfoManager.getInfoFile();
            if(info_file.exists()){
                info_file.delete();
            }
            IFetcher<String> network_fetcher = new NetworkContentFetcher(mOkHttpClient);
            network_fetcher.setFetcherListener(mFetcherListener);
            network_fetcher.fetch(source);
            return;
        }

        produceMedia(source, mediaInfoManager.getInfo().getTotalSize());
    }

    @Override
    public String getSource() {
        return mSource;
    }
}
