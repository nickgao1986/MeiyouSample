package com.meiyou.media.player.tv;

import android.widget.SeekBar;

import tv.danmaku.ijk.media.player.pragma.DebugLog;

/**
 * Created by Linhh on 17/1/5.
 */

public class PlayerUIManager {
    public static final int mSeekBarTotal = 1000;
    private SeekBar mSeekBar;
    public PlayerUIManager(){
    }

    public void setSeekBar(SeekBar seekBar){
        mSeekBar = seekBar;
        if(mSeekBar != null){
            mSeekBar.setMax(mSeekBarTotal);
        }
    }

    void setSeekbarListener(SeekBar.OnSeekBarChangeListener listener){
        if(mSeekBar != null){
            mSeekBar.setOnSeekBarChangeListener(listener);
        }
    }

    void setSeekbarProgress(long cur, long total){
        if(mSeekBar == null){
            return;
        }
        mSeekBar.setProgress((int)(cur * mSeekBarTotal) / (int) total);
    }

    void setSecondProgress(int progress){
        DebugLog.d("secondprogress","s:"+progress);
        if(mSeekBar != null){
            mSeekBar.setSecondaryProgress(progress * 10);
        }
    }
}
