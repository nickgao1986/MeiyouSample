package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import nickgao.com.meiyousample.R;


/**
 * Created by Administrator on 13-11-27.
 */
public class LoadingSmallView extends LinearLayout {


    public RelativeLayout relativeLayout;
    public  ImageView imageOut;//转圈圈





    public LoadingSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);


        relativeLayout = new RelativeLayout(context);
        //加入滚动条
        //ProgressBar progressBar = new ProgressBar(context);
        imageOut = new ImageView(context);
        imageOut.setImageResource(R.drawable.loading);
        RelativeLayout.LayoutParams paramrl = new  RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        paramrl.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(imageOut, paramrl);

        LayoutParams paramRelative = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        paramRelative.gravity = Gravity.CENTER_HORIZONTAL;
        addView(relativeLayout,paramRelative);

    }
    private RotateAnimation getRotateAnimation(){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        LinearInterpolator li = new LinearInterpolator();
        rotateAnimation.setInterpolator(li);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        return rotateAnimation;
    }
    //加载中
    public static final int TYPE_LOADING=1;
    //无数据
    public static final int TYPE_NODATA=2;
    //无网络
    public static final int TYPE_NONETWORK=3;

    private String strLoading = null;
    private String strNodata= null;
    private String strNoNetwork=null;

    /**
     * 设置内容  若无调用此方法，则使用默认文字
     * @param type
     * @param strText
     */
    public void setContent(int type,String strText){
        switch (type){
            case TYPE_LOADING:{
                strLoading = strText;
                break;
            }
            case TYPE_NODATA:{
                strNodata = strText;
                break;
            }
            case TYPE_NONETWORK:{
                strNoNetwork = strText;
                break;
            }
            default:
                break;
        }
        setStatus(type);
        strNodata= null;
        strLoading = null;
        strNoNetwork=null;
    }
    /**
     *
     * 设置视图类型
     * @param type 设置视图类型
    //加载中
    public static final int TYPE_LOADING=1;
    //无数据
    public static final int TYPE_NODATA=2;
    //无网络
    public static final int TYPE_NONETWORK=3;
     */
    public void setStatus(int type){
        show();
        switch (type){
            case TYPE_LOADING:{

                relativeLayout.setVisibility(View.VISIBLE);
                imageOut.setVisibility(VISIBLE);
                //tvLoading.setVisibility(View.VISIBLE);
                if(imageOut.getAnimation() == null)
                    imageOut.startAnimation(getRotateAnimation());


                break;
            }
            case TYPE_NODATA:{
                relativeLayout.setVisibility(View.GONE);
                imageOut.clearAnimation();


                break;
            }
            case TYPE_NONETWORK:{

                relativeLayout.setVisibility(View.GONE);
                imageOut.clearAnimation();

                break;
            }
            default:
                break;
        }
    }

    /**
     * 设置没有评论
     */
    public void setNoComment(){
        setStatus(TYPE_NODATA);
        /*
        //隐藏灰色图标
        mImageView.setVisibility(View.GONE);

        //隐藏圈圈，显示ICON
        relativeLayout.setVisibility(View.VISIBLE);
        imageOut.setVisibility(View.GONE);
        imageOut.clearAnimation();
        icon.setVisibility(View.VISIBLE);
        //设置文本
        mTextView.setText("还没有评论哦～\n赶快加入做第一个评论的人吧～");
        */
    }

    /**
     * 显示
     */
    public void show(){
        setVisibility(View.VISIBLE);
    }
    /**
     * 隐藏
     */
    public void hide(){
        setVisibility(View.GONE);
        imageOut.clearAnimation();
        relativeLayout.setVisibility(View.GONE);
    }

}
