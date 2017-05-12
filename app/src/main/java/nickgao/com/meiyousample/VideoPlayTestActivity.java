package nickgao.com.meiyousample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.meetyou.crsdk.util.DeviceUtils;
import com.meetyou.crsdk.util.VideoPlayUtil;
import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.view.JCTopicVideoView;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;
import com.meetyou.crsdk.video.view.VideoViewSetInfo;

/**
 * Created by gaoyoujian on 2017/5/11.
 */

public class VideoPlayTestActivity extends Activity {

    JCTopicVideoView mJcTopicVideoView;
    private String onlyVideoId;//唯一值

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_play_test_activity);
        onlyVideoId = getClass().getSimpleName() + "_" + System.currentTimeMillis() + "_" + Math.random();

        mJcTopicVideoView = (JCTopicVideoView) findViewById(R.id.jctVideoView);
        videoPlayInit(this);
    }

    /**
     * 视频播放
     */
    private void videoPlayInit(final Context context) {

        final VideoPlayStatus videoPlayStatus = new VideoPlayStatus(context, onlyVideoId);
        String imageUrl = "https://sc.seeyouyima.com/mp_131305811_1490235028.JPG";
        String videoUrl = "http://i.l.inmobicdn.net/adtools/videoads/prod/1701d05de0294e57be0bb1e009e34566/videos/54c5473f-9ce1-4720-8de5-a83feae2e0fa/video.720p.mp4";
        String hdVideoUrl = "https://sc.seeyouyima.com/news/vod/a710747b514921490234989634672207.mp4";
        String title = "美柚号纯视频测试";
        String finishContent = "";
        final String totalTimeStr = "09:28";
        final VideoViewInfo videoViewInfo = new VideoViewInfo(imageUrl, videoUrl, hdVideoUrl, title, "", totalTimeStr);
        videoViewInfo.totalSizeStr = "0M";

        final VideoViewSetInfo videoViewSetInfo = new VideoViewSetInfo(true, false, true, false, false, 720, 405);


        mJcTopicVideoView.setUpVideoInfo(1, videoPlayStatus, videoViewInfo, videoViewSetInfo, new ViewListener() {

            @Override
            public void onClickFullScreen() {
                VideoPlayUtil.fullPlay(context, mJcTopicVideoView, videoPlayStatus, videoViewInfo, 2121, "aa", totalTimeStr);
            }

            @Override
            public void onClickPlayOver() {

            }

            @Override
            public void onClickPauseOver() {
            }

            @Override
            public void onProgressStatusCallback(VideoProgressStatus progressStatus) {

            }

            @Override
            public void onClickReplayOver() {
            }

            @Override
            public void onClickVideoView() {
            }

            @Override
            public void onClickComplte() {

            }

            @Override
            public void onSeekTouchDown(boolean isPlaying) {
            }

            @Override
            public void onSeekTouchUp(boolean isPlaying) {
            }

            @Override
            public void onClickBack() {

            }
        }, null);

        int mRangStart = DeviceUtils.dip2px(context, 69);
        int mRangEnd = DeviceUtils.getScreenHeight(context) - DeviceUtils.dip2px(context, 50);
        //adapter.setListViewStatusListener(talkModel.id, viewHolder.jctVideoView.getVideoScrollListener(adapter.listview));
        mJcTopicVideoView.initPlayStatues(DeviceUtils.dip2px(context, 35), mRangStart, mRangEnd);
    }
}
