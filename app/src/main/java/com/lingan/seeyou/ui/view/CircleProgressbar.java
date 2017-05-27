package com.lingan.seeyou.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


@SuppressLint("HandlerLeak")
public class CircleProgressbar extends View {

    public CircleProgressbar(Context context) {
        super(context);
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public static final String TAG = "CircleProgressbar";

    public int bgStrokeWidth = 16;
    public int bgColor = 0x20000000;
    public int barEndColor = 0xffffc375;
    public int barStartColor = 0xffffc375;
    public int progress = 0;
    public int startAngle = 275;
    public int endAngle = 360;
    /**
     * 环形画笔
     */
    public Paint mPaintBar = null;
    /**
     * 环形背景画笔
     */
    public Paint mPaintBg = null;
    /**
     * 环形 开始或者结束圆角
     */
    public Paint mPaintCircleStart = null;
    public Paint mPaintCircleEnd = null;
    public RectF rectBg = null;
    /**
     * 直徑。
     */
    public int diameter = -1;
    public int cx1, cy1, arcRadius;
    /**
     * 用户设置的进度
     */
    int finalProgress = 0;

    public static final int STATE_GREEN = 2;
    public static final int STATE_YELLOW = 1;
    public static final int STATE_RED = 0;
    public static final int STATE_DEFUALT = 3;
    /**
     * startColor
     * endColor
     * bgColor
     */
    private int[] colorGreen ={0xffb4f4f7,0xff93df75,0xffe9f9fa};
    private int[] colorYellow ={0xffffd480,0xfffdac53,0x20000000};
    private int[] colorRed ={0xfffac4ef,0xffff668c,0xfff8eef6};
    private int[] colorDefualt ={0xffffc375,0xffffc375,0x20000000};

    /**
     * 设置State，支持 STATE_GREEN，STATE_YELLOW，STATE_RED
     * @param state
     */
    public void setState(int state) {
        int[] colorArray=new int[3];
        switch (state) {
            case STATE_RED:
                colorArray= colorRed;
                break;
            case STATE_YELLOW:
                colorArray= colorYellow;
                break;
            case STATE_GREEN:
                colorArray= colorGreen;
                break;
            case STATE_DEFUALT:
                colorArray = colorDefualt;
                break;
            default:
                break;
        }
        setCustomColor(colorArray[0],colorArray[1],colorArray[2]);
    }

    /**
     * 设置环形宽度
     * @param _StrokeWidth
     */
    public void setStrokeWidth(int _StrokeWidth){
        bgStrokeWidth=_StrokeWidth;
    }
    
    /**
     * 设置圆环自定义颜色，默认可以使用SetState，提供三种默认颜色值
     * @param startColor 圆环开始色
     * @param barEndColor 圆环结束色
     * @param bgColor 圆环背景色
     */
    public void setCustomColor(int startColor,int barEndColor,int bgColor){
        this.barStartColor =startColor;
        this.barEndColor =barEndColor;
        this.bgColor =bgColor;
        if (mPaintCircleStart != null)
            mPaintCircleStart.setColor(barStartColor);
        if (mPaintBar != null){
            mPaintBar.setColor(barEndColor);
            Shader gradient = new SweepGradient(cx1, cy1, barStartColor, barEndColor);
            float rotate = 270f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, cx1, cy1);
            gradient.setLocalMatrix(gradientMatrix);
            mPaintBar.setShader(gradient);

            if (mPaintCircleEnd != null){
                mPaintCircleEnd.setColor(barEndColor);
                mPaintCircleEnd.setShader(gradient);
            }
        }
        if (mPaintBg != null){
            mPaintBg.setColor(bgColor);
        }

        invalidate();
    }

    /**
     * 设置进度（0-100）；
     * @param _progress
     */
    public void setProgress(float _progress) {
        progress = 0;
        finalProgress = (int) ((_progress / 100.0f) * endAngle);
        if (finalProgress > endAngle) {
            finalProgress = 0;
        }
        handler.sendEmptyMessage(0);
    }
    public void initOnceTime() {
        if (diameter <= 0) {
            float density = getResources().getDisplayMetrics().density;
            bgStrokeWidth *= density;

            diameter = getWidth();
            // 画弧形的矩阵区域。
            rectBg = new RectF(bgStrokeWidth / 2, bgStrokeWidth / 2, diameter
                    - bgStrokeWidth / 2, diameter - bgStrokeWidth / 2);
            // 计算弧形的圆心和半径。
            cx1 = (diameter) / 2;
            cy1 = (diameter) / 2;
			arcRadius = (diameter - bgStrokeWidth) / 2;
            // ProgressBar结尾和开始画2个圆，实现ProgressBar的圆角。
            mPaintCircleStart = new Paint();
            mPaintCircleStart.setAntiAlias(true);
            mPaintCircleStart.setColor(barStartColor);

            // 弧形背景。
            mPaintBg = new Paint();
            mPaintBg.setAntiAlias(true);
            mPaintBg.setStyle(Style.STROKE);
            mPaintBg.setStrokeWidth(bgStrokeWidth);
            mPaintBg.setColor(bgColor);
            // 弧形ProgressBar。
            mPaintBar = new Paint();
            mPaintBar.setAntiAlias(true);
            mPaintBar.setStyle(Style.STROKE);
            mPaintBar.setStrokeWidth(bgStrokeWidth);
            mPaintBar.setColor(barEndColor);

//            int black =Color.BLACK;
//            float[] positions = {1,0};
//            int[] colors = {0xFFFFCC99, 0xFFFF9933};
            //add by zxb
            Shader gradient = new SweepGradient(cx1, cy1, barStartColor, barEndColor);
            float rotate = 270f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, cx1, cy1);
            gradient.setLocalMatrix(gradientMatrix);
            mPaintBar.setShader(gradient);

            mPaintCircleEnd = new Paint();
            mPaintCircleEnd.setAntiAlias(true);
            mPaintCircleEnd.setColor(barEndColor);
            mPaintCircleEnd.setShader(gradient);
        }
    }


//    public void updateBarColor(int barColor) {
//        this.barEndColor = barColor;
//        if (mPaintCircleStart != null)
//            mPaintCircleStart.setColor(this.barEndColor);
//        if (mPaintBar != null)
//            mPaintBar.setColor(this.barEndColor);
//        invalidate();
//    }

    // TODO: 2016/3/30 会存在泄漏，需要改 
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int percent = msg.what;
            if (percent < finalProgress) {
                progress = percent;
                handler.sendEmptyMessage(percent + 5);
                invalidate();
            } else {
                progress = finalProgress;
                invalidate();
            }
        }

    };


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);    //To change body of overridden methods use File | Settings | File Templates.
        drawCanvas(canvas);
    }

    private void drawCanvas(Canvas canvas) {
        initOnceTime();
//        if (progress == 0) {
//
//        } else if (progress == endAngle) {
//
//        }
        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);
        if(progress>0){
            canvas.drawCircle(
                    (float) (cx1 + arcRadius * Math.cos((startAngle) * 3.14 / 180)),
                    (float) (cy1 + arcRadius * Math.sin((startAngle) * 3.14 / 180)),
                    bgStrokeWidth / 2, mPaintCircleStart);// 小圆

            canvas.drawCircle(
                    (float) (cx1 + arcRadius * Math.cos((startAngle+progress) * 3.14 / 180)),
                    (float) (cy1 + arcRadius * Math.sin((startAngle+progress)* 3.14 / 180)),
                    bgStrokeWidth / 2, mPaintCircleEnd);// 小圆
        }

        canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);


        /*if (progress != 0) {
            canvas.drawCircle(
                    (float) (cx1 + arcRadius * Math.cos((progress - 360 + 140) * 3.14 / 180)),
                    (float) (cy1 + arcRadius * Math.sin((progress - 360 + 140) * 3.14 / 180)),
                    bgStrokeWidth / 2, mPaintCircleStart);// 小圆
        }*/


       
//        //画背景
//        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);
//        /**
//         * 画弧形
//         */
////        resetPaintBar();
//        canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);
    }

    /**
     * 更新PaintBar
     */
    public void resetPaintBar(){
        if (mPaintBar != null){
//            mPaintBar.setColor(barEndColor);
            // 这样计算有问题。
            int tempEndColor= ((barEndColor-barStartColor)/360) * progress;
            Shader gradient = new SweepGradient(cx1, cy1, barStartColor, tempEndColor);
            float rotate = 270f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, cx1, cy1);
            gradient.setLocalMatrix(gradientMatrix);
            mPaintBar.setShader(gradient);
        }  
    }
}


