package com.meetyou.crsdk.video.core;

import android.content.Context;
import android.media.AudioManager;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Nathen
 * On 2016/02/21 12:25
 */
public class JCUtils {

  public static String stringForTime(long timeMs) {
    if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
      return "00:00";
    }
    long totalSeconds = timeMs / 1000;
    long seconds = totalSeconds % 60;
    long minutes = (totalSeconds / 60) % 60;
    long hours = totalSeconds / 3600;
    StringBuilder mFormatBuilder = new StringBuilder();
    Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    if (hours > 0) {
      return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
    } else {
      return mFormatter.format("%02d:%02d", minutes, seconds).toString();
    }
  }
  private static AudioManager mAudioManager;
  private static int mVolume;
  public static AudioManager getAudioManager(Context context){
    if(mAudioManager==null){
        mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    return mAudioManager;
  }

  public static int getVolume() {
    return mVolume;
  }

  /**
   * //获取视频的缩略图显示在开始
   MediaMetadataRetriever media = new MediaMetadataRetriever();
   media.setDataSource(videoPath, new HashMap());
   Bitmap bitmap = media.getFrameAtTime();
   //显示在上面
   jieCao_video.ivThumb.setImageBitmap(bitmap);
   */
}
