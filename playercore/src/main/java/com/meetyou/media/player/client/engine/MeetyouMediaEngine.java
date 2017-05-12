package com.meetyou.media.player.client.engine;

import com.meetyou.media.player.client.fetcher.IMeetyouPlayerListener;
import com.meetyou.media.player.client.fetcher.MediaInfoManager;
import com.meetyou.media.player.client.fetcher.MediaPartManager;
import com.meetyou.media.player.client.fetcher.NetworkMediaDownloader;
import com.meetyou.media.player.client.util.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by gaoyoujian on 2017/5/10.
 */

public class MeetyouMediaEngine extends AbstractMeetyouMediaDataSource implements IMediaDataSource {

    private NetworkMediaDownloader m_networkMediaDownloader;
    private MediaPartManager m_mediaPartManager;
    private MediaInfoManager m_mediaInfoManager;

    private IMeetyouPlayerListener m_imeetyouNetworkListener;

    private boolean m_close = false;

    private String m_source;
    private long m_total_size;
    private OkHttpClient m_okHttpClient;

    public MeetyouMediaEngine(OkHttpClient okHttpClient, String source, long totalSize, IMeetyouPlayerListener iMeetyouNetworkListener) throws Exception{
        m_source = source;
        m_total_size = totalSize;
        m_okHttpClient = okHttpClient;
        m_imeetyouNetworkListener = iMeetyouNetworkListener;
        DebugLog.d("datasource","totalsize:"+totalSize);
        produce();
    }

    public String getSource(){
        return m_source;
    }

    private void produce() throws Exception{
        m_close = false;
        //构造媒体数据管理器
        m_mediaInfoManager = new MediaInfoManager(m_source);
        m_mediaInfoManager.setTotalSize(m_total_size);
        //利用媒体数据管理器构造媒体分段管理器
        m_mediaPartManager = new MediaPartManager(m_mediaInfoManager);
        String file_name = Utils.tempFilePath(m_source);
        m_randomAccessFile = new RandomAccessFile(file_name,"rw");
//        m_randomAccessFile.setLength(m_total_size);//设置文件最大值
        //将所有数据放置到下载引擎中
        m_networkMediaDownloader = new NetworkMediaDownloader(m_okHttpClient,
                m_mediaPartManager,
                file_name,
                m_source,
                m_total_size,m_imeetyouNetworkListener);
    }

    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (size == 0) {
            //seek的时候这里size会为0
            return 0;
        }
        long startPage = getPageForPosition(position);
        long end = position + size;
        if(end > m_total_size){
            //判断需要读取的大小是否超过分块
            end = m_total_size;
        }
        long endPage = getPageForPosition(end);
        DebugLog.d("meetyou_player", "开始加载数据:"+position + "--->"+end+",加载数:"+size+",分块数:"+startPage +"--->"+ endPage);
        for (long i = 0; i <= endPage - startPage; i++) {
            //如果当前的分块没有下载好久让他下载
            while(!m_close && m_mediaPartManager.queryPartStatus(startPage + i) != MediaPartManager.FETCHER_OK){
                // TODO: 17/1/17 将网络数据保存到内存中供下次使用,无需加载文件数据
                if(m_imeetyouNetworkListener != null){
                    m_imeetyouNetworkListener.onLoad(true);
                }
                DebugLog.d("meetyou_player", "缓冲数据不完整开始下载");
                m_networkMediaDownloader.downloadPart(startPage + i);
            }
        }

        if(m_imeetyouNetworkListener != null){
            m_imeetyouNetworkListener.onLoad(false);
        }

        if(m_close){
            return -1;
        }

        //读取缓存数据
        int flag = seek(position,buffer,offset,size);

        DebugLog.d("meetyou_player", "完成加载数据:"+position + "--->"+end + ",标志位:"+ flag+",分块数:"+startPage +"--->"+ endPage);
        return flag;
    }

    @Override
    public long getSize() throws IOException {
        return m_total_size;
    }

    @Override
    public void close() throws IOException {
        //TODO:close
        DebugLog.d("meetyou_media_engine","close");
        m_close = true;
        m_total_size = 0;
        m_networkMediaDownloader.close();
        m_randomAccessFile.close();
    }

    @Override
    public void pause() {
        m_networkMediaDownloader.pause();
    }

    @Override
    public void remuse() {
        m_networkMediaDownloader.remuse();
    }
}
