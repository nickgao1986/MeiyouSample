package com.meetyou.media.player.client.fetcher;

import com.meiyou.media.player.tv.playengine.MeetyouPlayerDef;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 获取文件数据信息
 * Created by Linhh on 17/1/16.
 */

public class NetworkContentFetcher extends AbstractFetcher implements IFetcher<String> {

    public NetworkContentFetcher(OkHttpClient okHttpClient){
        this.mOkHttpClient = okHttpClient;
    }

    @Override
    public void fetch(String source) throws Exception{
        Request request = new Request.Builder()
                .url(source)
                .build();
        //请求数据头
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mIMeetyouNetworkListener.onError(MeetyouPlayerDef.READ_HEAD_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response != null && response.code() == 200){
                    //请求数据头成功
                    produceMedia(response.request().url().toString(),
                            response.body().contentLength());
                    response.body().close();
                }else{
                    //失败
                    mIMeetyouNetworkListener.onError(MeetyouPlayerDef.READ_HEAD_ERROR);
                }
//                response.request().url().toString();
            }
        });
    }

    @Override
    public String getSource() {
        return mSource;
    }
}
