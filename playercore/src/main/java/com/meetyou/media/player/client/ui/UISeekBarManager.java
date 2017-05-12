package com.meetyou.media.player.client.ui;

import android.widget.SeekBar;

import com.meiyou.media.player.tv.MeetyouPlayer;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

/**
 * Created by Linhh on 17/3/8.
 */

public class UISeekBarManager implements IPlayerCallback.OnProgressListener,
        IPlayerCallback.OnBufferingListener,
        SeekBar.OnSeekBarChangeListener{
    private SeekBar mSeekBar;
    public static final int mSeekBarTotal = 1000;
    private MeetyouPlayer mMeetyouPlayer;
    protected long mlSeekToTime = 0;
    protected long mlSeekStartTime = 0;

    /**
     * 避免误触
     */
    private int mOnProgressChange_PreProgress = 0;
    private boolean mOnProgressChange_PreFromTouch = false;

    private boolean m_bProgressTouched = false;

    public void setView(SeekBar seekBar){
        mSeekBar = seekBar;
        if(mSeekBar != null){
            mSeekBar.setMax(mSeekBarTotal);
            mSeekBar.setOnSeekBarChangeListener(this);
        }
    }

    public void setPlayer(MeetyouPlayer meetyouPlayer){
        mMeetyouPlayer = meetyouPlayer;
        mMeetyouPlayer.addOnBufferingListener(this);
        mMeetyouPlayer.addOnProgressListener(this);
    }

    @Override
    public void onPorgress(long cur, long total) {
        if(mSeekBar == null){
            return;
        }
        if(total == 0){
            mSeekBar.setProgress(0);
        }else {
            mSeekBar.setProgress((int) (cur * mSeekBarTotal) / (int) total);
        }
    }

    @Override
    public void onBuffering(int currentBufferPercentage) {
        if(mSeekBar != null){
            mSeekBar.setSecondaryProgress(currentBufferPercentage * 10);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromtouch) {

        //需要判断surface是否存在
        if (mOnProgressChange_PreProgress == progress && mOnProgressChange_PreFromTouch == fromtouch) {
            return;
        }

        mOnProgressChange_PreProgress = progress;
        mOnProgressChange_PreFromTouch = fromtouch;

        if (fromtouch) {
            if (m_bProgressTouched){
                mlSeekToTime = mMeetyouPlayer.getTotalDuration() / mSeekBarTotal * progress;
            }

//            mtvSeekPos.setText(KasUtil.stringForTime((int) mlSeekToTime, false));
//            mtvSeekTime.setText(KasUtil.stringForTime((int) mlSeekToTime, false));
//            mtvSeekRelativeTime.setText(KasUtil.stringForTime((int) mlSeekToTime - mlSeekStartTime, true));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        m_bProgressTouched = true;
        mlSeekStartTime = mMeetyouPlayer.getCurrentPos();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (m_bProgressTouched) {
            m_bProgressTouched = false;
        }
//        mHandler.removeMessages(VIDEO_SEEK_UNSYNC);
//        mHandler.sendEmptyMessage(VIDEO_SEEK_UNSYNC);
        mMeetyouPlayer.seek2((int) mlSeekToTime);
//        if(null != mtvSeekRelativeTime && null != mtvSeekTime){
//            mtvSeekTime.setVisibility(View.GONE);
//            mtvSeekRelativeTime.setVisibility(View.GONE);
//        }
        if (!mMeetyouPlayer.isPaused() /*&& mPlayerViewHelper.getVideoReady()*/) {
            mMeetyouPlayer.play();
        }
    }
}
