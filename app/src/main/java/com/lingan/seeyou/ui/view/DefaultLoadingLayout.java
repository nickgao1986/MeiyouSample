package com.lingan.seeyou.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nickgao.com.meiyousample.R;


@SuppressLint("ViewConstructor")
public class DefaultLoadingLayout extends LoadingLayout {

    private final TextView headerText, TimeText;

    private ImageView icon;

    private String pullLabel;
    private String refreshingLabel;
    private String releaseLabel;
//    private StarView starView;

    private final AnimationDrawable iconAnimation;

    public void setTimeTextVisibility(int visibility) {
        if (this.TimeText != null) {
            if (visibility == GONE) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
                headerText.setLayoutParams(params);
            }
            this.TimeText.setVisibility(visibility);
        }
    }

    @SuppressLint("ResourceAsColor")
    public DefaultLoadingLayout(Context context, final int mode, String releaseLabel,
                                String pullLabel, String refreshingLabel) {
        super(context);

        ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.pull_to_refresh_header, this);
        icon = (ImageView) header.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.refresh_icon_list_image);

        iconAnimation = (AnimationDrawable) icon.getDrawable();
        iconAnimation.setOneShot(false);

        headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
        TimeText = (TextView) header.findViewById(R.id.txt_time);
//        starView = new StarView(context);
        try{
           /* LinearLayout container = (LinearLayout) header.findViewById(R.id.ptr_stars_frame);
            container.addView(starView);*/
        }catch (Exception ex){
            ex.printStackTrace();
        }

        setTimeText();
        headerText.setTextColor(context.getResources().getColor(R.color.black_b));
        TimeText.setTextColor(context.getResources().getColor(R.color.black_b));
        final Interpolator interpolator = new LinearInterpolator();

        this.releaseLabel = releaseLabel;
        this.pullLabel = pullLabel;
        this.refreshingLabel = refreshingLabel;
    }

    //回收星星
    public void recycleBitmap(){
        try{
//            starView.recycleStar();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setIconVisible(boolean visible){
        if(visible){
            icon.setVisibility(View.VISIBLE);
        }else{
            icon.setVisibility(View.GONE);
        }
    }

    public void reset() {
        headerText.setText(pullLabel);
        iconAnimation.stop();
        iconAnimation.selectDrawable(0);
//        setTimeText();
    }

    @Override
    public void setTimeText() {
        TimeText.setText("上次刷新时间：" + getLastRefreshTime());
    }

    void saveLastRefreshTime() {
        getContext().getSharedPreferences("time_saver", 0).edit()
                .putLong("refresh_time", System.currentTimeMillis()).commit();
    }

    String getLastRefreshTime() {

        try{

            if(getContext()==null)
                return "刚刚";

            SharedPreferences pref = getContext().getSharedPreferences("time_saver", 0);
            if(pref==null)
                return "";

            long time = pref.getLong("refresh_time", 0);
            if (time == 0) {
                return "第一次";
            }
            long currentTime = System.currentTimeMillis();
            long dur = currentTime - time;
            if (dur > Time.DAY * 30) {
                return "很久之前";
            } else if (dur > Time.DAY) {
                return dur/ Time.DAY +"天前";
            } else if (dur > Time.HOUR) {
                return dur / Time.HOUR + "小时前";
            } else if (dur > Time.MINUTES) {
                return dur / Time.MINUTES + "分钟前";
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "刚刚";
    }
    public interface Time {
        long SECOND = 1000l;
        long MINUTES = 60 * SECOND;
        long HOUR = 60 * MINUTES;
        long DAY = 24 * HOUR;
    }

    public void releaseToRefresh() {
//        starView.resume();
        headerText.setText(releaseLabel);
    }

    public void setPullLabel(String pullLabel) {
        this.pullLabel = pullLabel;
    }

    public void refreshing() {
//        starView.pause();
        saveLastRefreshTime();
        headerText.setText(refreshingLabel);
        iconAnimation.stop();
        iconAnimation.selectDrawable(0);
        iconAnimation.start();
    }

    public void setRefreshingLabel(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(String releaseLabel) {
        this.releaseLabel = releaseLabel;
    }

    public void pullToRefresh() {
        headerText.setText(pullLabel);
    }

    public void setTextColor(int color) {
        headerText.setTextColor(color);
    }

    public int getScrollMiniHeight() {
        return getHeight();
    }
    public int getScrollMaxHeight() {
        return 3*getHeight();
    }
}
