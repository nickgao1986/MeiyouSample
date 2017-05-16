package nickgao.com.meiyousample.firstPage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.meetyou.crsdk.util.VideoPlayUtil;
import com.meetyou.crsdk.video.core.VideoProgressStatus;
import com.meetyou.crsdk.video.core.ViewListener;
import com.meetyou.crsdk.video.view.VideoPlayStatus;
import com.meetyou.crsdk.video.view.VideoViewInfo;
import com.meetyou.crsdk.video.view.VideoViewSetInfo;

import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/5/12.
 */

public class NewsHomeVideoController {

    private static class NewsHomeVideoControllerHodler {
        public static final NewsHomeVideoController INSTANCE = new NewsHomeVideoController();
    }


    public static NewsHomeVideoController getInstance() {
        return NewsHomeVideoController.NewsHomeVideoControllerHodler.INSTANCE;
    }

    public void handleVideo(final Activity context, final NewsHomeClassifyAdapter adapter, NewsHomeClassifyAdapter.ViewHolder vHolder, final TalkModel talk, VideoPlayStatus videoPlayStatus) {
        try {
            handleName(context, vHolder, talk, adapter.homeBottomContent);//头像和名字
            //评论
            if (talk.total_review <= 0) {//需求是等宇－1的时候隐藏这个控件
                vHolder.ic_video_comment.setVisibility(View.GONE);
                vHolder.tv_video_comment_count.setVisibility(View.GONE);
            } else {
                vHolder.tv_video_comment_count.setText(String.valueOf(5));
                vHolder.tv_video_comment_count.setVisibility(View.VISIBLE);
                vHolder.ic_video_comment.setVisibility(View.VISIBLE);
            }
            //播放次数
            if (talk.view_times == 0) {
                vHolder.tv_video_play_time.setVisibility(View.GONE);
            } else {
                vHolder.tv_video_play_time.setVisibility(View.VISIBLE);
                vHolder.tv_video_play_time.setText("5次播放");
            }


            setVideoViewLp(vHolder, adapter.screenWidth, adapter.videoHeight);
            if (talk.images.size() > 0) {
                videoPlayInit(context, adapter, vHolder, talk, videoPlayStatus, 0);
            }
            int normalPos = 0;
            //视频间距是否显示
            if (normalPos + 1 < adapter.getCount()) {
                TalkModel nextModel = adapter.list.get(normalPos + 1);
                if (nextModel.recomm_type == HomeType.TYPE_SEPARATOR_BAR || (nextModel.recomm_type == HomeType.TYPE_NEWS_TOPIC_CARD && nextModel.attr_type == HomeType.HOME_SPECIAL_BLEND)) {//专题混合型的卡片 和分隔栏有间距要隐藏线
                    vHolder.view_video_space.setVisibility(View.GONE);
                } else {
                    vHolder.view_video_space.setVisibility(View.VISIBLE);
                }
            } else {
                if (normalPos == adapter.getCount() - 1) {
                    vHolder.view_video_space.setVisibility(View.GONE);
                }else
                    vHolder.view_video_space.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置视频播放的宽高
    private void setVideoViewLp(NewsHomeClassifyAdapter.ViewHolder viewHolder, int screenWidth, int videoHeight) {
        LinearLayout.LayoutParams videoLp = (LinearLayout.LayoutParams) viewHolder.jctVideoView.getLayoutParams();
        videoLp.width = screenWidth;
        videoLp.height = videoHeight;
        viewHolder.jctVideoView.requestLayout();
    }



    /**
     * 视频播放
     */
    private void videoPlayInit(final Context context, final NewsHomeClassifyAdapter adapter, final NewsHomeClassifyAdapter.ViewHolder viewHolder, final TalkModel talkModel, final VideoPlayStatus videoPlayStatus, final int position) {
        final VideoViewInfo videoViewInfo = new VideoViewInfo(talkModel.images.get(0), talkModel.sd_url, talkModel.hd_url, talkModel.title, "", talkModel.video_time);
        videoViewInfo.totalSizeStr = talkModel.sd_size;
        final VideoViewSetInfo videoViewSetInfo = new VideoViewSetInfo(true, false, true, false, false, adapter.screenWidth, adapter.videoHeight);
        viewHolder.jctVideoView.setUpVideoInfo(position, videoPlayStatus, videoViewInfo, videoViewSetInfo, new ViewListener() {

            @Override
            public void onClickFullScreen() {
                VideoPlayUtil.fullPlay(context, viewHolder.jctVideoView, videoPlayStatus, videoViewInfo, talkModel.id, adapter.classifyName, talkModel.video_time);
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
        adapter.setListViewStatusListener(talkModel.id, viewHolder.jctVideoView.getVideoScrollListener(adapter.listview));
        viewHolder.jctVideoView.initPlayStatues(DeviceUtils.dip2px(context, 35), mRangStart, mRangEnd);
    }

    /**
     * 名字的处理
     */
    private void handleName(Activity mContext, NewsHomeClassifyAdapter.ViewHolder vHolder, TalkModel talk, int homeBottomContent) {
        String circle_name;//名字
        if (talk.recomm_type == HomeType.TYPE_NEWS_TOPIC) {//资讯贴子
            circle_name = talk.publisher == null ? "" : talk.publisher.screen_name;
        } else {
            circle_name = homeBottomContent == 1 ? talk.circle_name : talk.publisher == null ? "" : talk.publisher.screen_name;
        }
        //圈子名字
        vHolder.tv_video_name.setText(circle_name);
        /*if (talk.attr_type == 1) {//美柚号
            vHolder.tv_video_name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.feed_icon_meiyou), null);
        } else */
        vHolder.tv_video_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

    }

}
