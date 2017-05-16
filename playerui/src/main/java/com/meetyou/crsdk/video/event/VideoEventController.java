package com.meetyou.crsdk.video.event;

/**
 * 用于video模块的eventbus分发
 * Created by wuzhongyou on 16/7/11.
 */
public class VideoEventController {

    private VideoEventListener mListener;
    private int position;

    private boolean isHadRegister = false;

    public VideoEventController(VideoEventListener listener, int position) {
        this.mListener = listener;
        this.position = position;
    }

    /**
     *
     */
    public void release() {
        doUnRegisterEvents();
        mListener = null;

    }

    /**
     * 注册监听事件
     */
    public void doRegisterEvents() {
        if (!isHadRegister) {
            doRegisterActions(true);
        }
    }

    /**
     * 注销监听事件
     */
    public void doUnRegisterEvents() {
        if (isHadRegister) {
            doRegisterActions(false);
        }
    }

    private synchronized void doRegisterActions(boolean isRegisterAction) {
        if (position >= 0) {
            if (isRegisterAction) {
                if (!isHadRegister) {
                    isHadRegister = true;
                    //EventBus.getDefault().register(this);
                }
            } else {
                if (isHadRegister) {
                    isHadRegister = false;
                    //EventBus.getDefault().unregister(this);
                }
            }
        }
    }

    /**
     * 跳转进入全屏通知
     *
     * @param event
     */
    public void onEventMainThread(JumpToFullEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 播放完成回调
     *
     * @param event
     */
    public void onEventMainThread(VideoPlayCompleteEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 全屏播放返回回调
     *
     * @param event
     */
    public void onEventMainThread(BackFullScreenEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 用于处理首页tab切换处理
     *
     * @param event
     */
    public void onEventMainThread(FragmentVisibleEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 用于网络变化切换处理
     *
     * @param event
     */
    public void onEventMainThread(NetChangeEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 用于监听锁屏情况
     *
     * @param event
     */
    public void onEventMainThread(ScreenStatusChangeEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    /**
     * 接收首页翻页事件重新请求广告
     *
     * @param event
     */
    public void onEventMainThread(NewsHomeSelectedEvent event) {
        if (mListener != null) {
            mListener.handleEventMainThread(event);
        }
    }

    public interface VideoEventListener {

        /**
         * 跳转进入全屏通知
         *
         * @param event
         */
        public void handleEventMainThread(JumpToFullEvent event);

        /**
         * 播放完成回调
         *
         * @param event
         */
        public void handleEventMainThread(VideoPlayCompleteEvent event);

        /**
         * 全屏播放返回回调
         *
         * @param event
         */
        public void handleEventMainThread(BackFullScreenEvent event);

        /**
         * 用于处理首页tab切换处理
         *
         * @param event
         */
        public void handleEventMainThread(FragmentVisibleEvent event);

        /**
         * 用于网络变化切换处理
         *
         * @param event
         */
        public void handleEventMainThread(NetChangeEvent event);

        /**
         * 用于监听锁屏情况
         *
         * @param event
         */
        public void handleEventMainThread(ScreenStatusChangeEvent event);

        /**
         * 用于首页翻页监听
         *
         * @param event
         */
        public void handleEventMainThread(NewsHomeSelectedEvent event);
    }
}
