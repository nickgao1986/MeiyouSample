package com.meetyou.media.player.client.engine;

import java.io.IOException;
import java.io.RandomAccessFile;

import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by gaoyoujian on 2017/5/10.
 */

public abstract class AbstractMeetyouMediaDataSource {

    protected RandomAccessFile m_randomAccessFile;//tempfile

    public static final long PAGE_MAX_SIZE = 1024L * 512L;

    public abstract void pause();

    public abstract void remuse();

    protected int seek(long position, byte[] buffer, int offset, int size) throws IOException {
        DebugLog.d("download_Part","start seek:" + position + ","+size);
        m_randomAccessFile.seek(position);
        int length = m_randomAccessFile.read(buffer,offset,size);
        DebugLog.d("download_Part","start length:" + length);
        return length;
    }


    public long getPageForPosition(long position) {
        return position / PAGE_MAX_SIZE;
    }

    public long getPage(long size){
        return size % PAGE_MAX_SIZE == 0 ? size/PAGE_MAX_SIZE : size/PAGE_MAX_SIZE + 1;
    }

}
