package com.meetyou.media.player.client.fetcher;

import com.alibaba.fastjson.JSON;
import com.meetyou.media.player.client.util.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * 管理media数据信息
 * Created by Linhh on 17/1/17.
 */

public class MediaInfoManager {

    String m_base64_file_dir;
    String m_source;
    long m_total_size;

    File m_dir_path;
    File m_info_path;

    MediaInfo m_temp_info;

    public MediaInfoManager(String source){
        try {
            m_base64_file_dir = Utils.base64(source);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        m_source = source;
        produceFile();
    }

    public void setTotalSize(long totalSize){
        m_total_size = totalSize;
        if(m_temp_info != null){
            m_temp_info.setTotalSize(m_total_size);
        }
    }

    public void produceFile(){
        String dir_path = Utils.getCacheRootFile() + "/" + m_base64_file_dir;
        if(m_dir_path == null) {
            m_dir_path = new File(dir_path);
        }
        if(!m_dir_path.exists()){
            m_dir_path.mkdir();
        }
        String info_path = dir_path + "/" + "media_info.mypi";
        if(m_info_path == null){
            m_info_path = new File(info_path);
        }
    }

    public void saveInfo(MediaInfo mediaInfo){
        m_temp_info = mediaInfo;
        String json = JSON.toJSONString(m_temp_info);
        Utils.writeToFile(json, m_info_path);
    }

    /**
     * 当前temp媒体是否存在
     * @return
     */
    public boolean isTempMediaExist() throws Exception{
        String temp = Utils.tempFilePath(m_source);
        File file = new File(temp);
        return file.exists();
    }

    /**
     * 判断当前媒体数据文件是否存在
     * @return
     */
    public boolean isInfoMediaExist(){
        return m_info_path.exists();
    }

    public File getInfoFile(){
        return m_info_path;
    }

    public MediaInfo getInfo(){
        if(m_temp_info != null){
            return m_temp_info;
        }
        if(!m_info_path.exists()){
            m_temp_info = new MediaInfo();
            m_temp_info.statusMap = new HashMap<>();
            m_temp_info.setSource(m_source);
            m_temp_info.setTotalSize(m_total_size);
            return m_temp_info;
        }
        String json = Utils.readFile(m_info_path);
        m_temp_info = JSON.parseObject(json,MediaInfo.class);
        return m_temp_info;
    }
}
