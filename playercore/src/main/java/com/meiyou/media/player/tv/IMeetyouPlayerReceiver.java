package com.meiyou.media.player.tv;

/**
 * Created by Linhh on 16/12/26.
 */

public interface IMeetyouPlayerReceiver {

    /**
     * 是否使用硬解?
     * @return
     */
    public boolean useHardware();

    /**
     * 音频加速
     * @return
     */
    public boolean useOpenSLES();

}
