package com.meetyou.media.player.client.util;

import android.os.Environment;
import android.util.Base64;

import com.meetyou.media.player.client.MeetyouPlayerEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gaoyoujian on 2017/5/10.
 */

public class Utils {


    public static final String CACHE_NAME = ".meetyouplayer_data";

    public interface PlayerCacheFilter{
        public boolean onCache(File file);
    }

    public static String getCacheFile() {
        File cacheDir;
        String fileName = Environment.getExternalStorageDirectory().getPath() + "/" + CACHE_NAME;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(fileName);
        } else {
            cacheDir = MeetyouPlayerEngine.Instance().getContext().getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 清理整个缓冲区
     */
    public static void clearCacheRoot(){
        clearCache(new PlayerCacheFilter() {
            @Override
            public boolean onCache(File file) {
                return true;
            }
        });
    }

    public static void clearCache(PlayerCacheFilter filter){
        File dir = new File(getCacheFile());
        if(dir.exists()){
            clearDir(dir.listFiles(),filter);
        }
    }

    public static void clearCacheBeforeTime(final long time){
        clearCache(new PlayerCacheFilter() {
            @Override
            public boolean onCache(File file) {
                if(file.lastModified() <= time){
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 清理某个特定链接的缓冲
     * @param name
     */
    public static void clearCahce(String name) throws Exception{
        String path = getCacheRootFile().getAbsolutePath();
        String dir_path = path + "/" + base64(name);
        File dir = new File(dir_path);
        if(dir.exists()){
            clearDir(dir.listFiles(), new PlayerCacheFilter() {
                @Override
                public boolean onCache(File file) {
                    return true;
                }
            });
            dir.delete();
        }
    }

    public static void clearDir(File[] files, PlayerCacheFilter filter){
        if(files == null){
            return;
        }
        for(File file : files){
            if(file.exists()) {
                if (file.isDirectory()) {
                    clearDir(file.listFiles(), filter);
                }
                boolean filter_result = false;
                if(filter != null){
                    filter_result = filter.onCache(file);
                }
                if(filter_result) {
                    file.delete();
                }
            }
        }
    }

    public static File getCacheRootFile(){
        File dir = new File(getCacheFile());
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public static String tempFilePath(String name) throws Exception{
        String path = getCacheRootFile().getAbsolutePath();
        String dir_path = path + "/" + base64(name);
        File dir = new File(dir_path);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir_path + "/temp.mypt";//mypt
    }

    public static String readFile(File file) {
        try {
            int len = 1024;
            byte[] buffer = new byte[len];
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int nrb = fis.read(buffer, 0, len); // read up to len bytes
                while (nrb != -1) {
                    baos.write(buffer, 0, nrb);
                    nrb = fis.read(buffer, 0, len);
                }
                buffer = baos.toByteArray();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = new String(buffer);
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    public static void writeToFile(String source, File file){
        writeStringToFile(file,source, null);
    }

    public static boolean writeStringToFile(File file, String content,
                                            String enc) {
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStreamWriter os = null;
            if (enc == null || enc.length() == 0) {
                os = new OutputStreamWriter(new FileOutputStream(file));
            } else {
                os = new OutputStreamWriter(new FileOutputStream(file), enc);
            }
            os.write(content);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String getDuration(int time) {
        int s = time;
        int N = s / 3600;
        s = s % 3600;
        int K = s / 60;
        s = s % 60;
        int M = s;
        StringBuilder sb = new StringBuilder();
        if (N > 0) {
            sb.append(N).append("小时");
        }
        if (K > 0) {
            sb.append(K).append("分");
        }
        if (M > 0) {
            sb.append(M).append("秒");
        }
        return sb.toString();
    }

    public static String makeSHA1Hash(String text) {
        try {
            return makeSHA1Hash(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeSHA1Hash(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bytes, 0, bytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeSHA1HashBase64(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bytes, 0, bytes.length);
            byte[] sha1hash = md.digest();
            return Base64.encodeToString(sha1hash, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeMD5Hash(String text) {
        try {
            return makeMD5Hash(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeMD5Hash(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes, 0, bytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    public static String convertToHex(byte[] raw) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(raw.length);
        for (byte b : raw) {
            int v = b & 0xFF;
            sb.append((char) HEX_CHAR_TABLE[v >>> 4]);
            sb.append((char) HEX_CHAR_TABLE[v & 0xF]);
        }
        return sb.toString();
    }

    /**
     * create secureHashKey
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64(final String key) throws UnsupportedEncodingException {
        return makeSHA1HashBase64(key.getBytes("UTF-8"));
    }

}
