/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meetyou.pullrefresh.star;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import nickgao.com.meiyousample.R;

/**
 * This class is the custom view where all of the Droidstars are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidstars.
 */
public class StarView extends View {

    private static final String TAG = "StarView";
    int ArrayResId[] = new int[]{R.drawable.apk_all_startone, R.drawable.apk_all_starttow};

    Bitmap droid;       // The bitmap that all stars use
    int numstars = 0;  // Current number of stars
    ArrayList<Star> stars = new ArrayList<Star>(); // List of current stars

    // Animator used to drive all separate star animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all stars for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and fps
//    int frames = 0;     // Used to track frames per second
//    Paint textPaint;    // Used for rendering fps text
    Matrix m = new Matrix(); // Matrix used to translate/rotate each star during rendering

    Paint p = new Paint();
    /**
     * Constructor. Create objects used throughout the life of the View: the Paint and
     * the animator
     */
    public StarView(Context context) {
        super(context);
        try{

          droid = BitmapFactory.decodeResource(getResources(), ArrayResId[0]);

//        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        textPaint.setColor(Color.RED);
//        textPaint.setTextSize(24);

            // This listener is where the action is for the flak animations. Every frame of the
            // animation, we calculate the elapsed time and update every star's position and rotation
            // according to its speed.
            /*animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator arg0) {
                    try{

                        long nowTime = System.currentTimeMillis();
                        float secs = (float)(nowTime - prevTime) / 1000f;
                        prevTime = nowTime;
                        for (int i = 0; i < numstars; ++i) {
                            Star star = stars.get(i);
                            if (star.isFadin&&star.alpha<=255){
                                star.alpha +=(Math.random()*10/3);
                                if (star.alpha>=255){
                                    star.isFadin = !star.isFadin;
                                }
                            }else if(!star.isFadin){
                                star.alpha -=(Math.random()*10/3);
                                if (star.alpha<=0){
                                    star.isFadin = !star.isFadin;
                                    star.resetXY(getWidth(),getHeight());
                                }
                            }
                            star.rotation = star.rotation + (star.rotationSpeed * secs);
                        }
                        // Force a redraw to see the stars in their new positions and orientations
                        invalidate();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            //animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setDuration(3000);     */
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 回收星星
     */
    public void recycleStar(){
        try{

            int count = stars.size();
            for(int i=0;i<count;i++){
                Star star = stars.get(i);
                if(star!=null && star.bitmap!=null && !star.bitmap.isRecycled()){
                    star.bitmap.recycle();
                }

            }

            animator.removeAllUpdateListeners();


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    int getNumstars() {
        return numstars;
    }

    public void setNumstars(int quantity) {
        numstars = quantity;
    }

    /**
     * Add the specified number of droidstars.
     */
    void addstars(int quantity) {
        try{
            Random random = new Random();
            for (int i = 0; i < quantity; ++i) {

                droid = BitmapFactory.decodeResource(getResources(), ArrayResId[random.nextBoolean()?0:1]);
                stars.add(Star.createstar(getWidth(),getHeight(), droid));

            }
            setNumstars(numstars + quantity);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Subtract the specified number of droidstars. We just take them off the end of the
     * list, leaving the others unchanged.
     */
    void subtractstars(int quantity) {
        try{
            for (int i = 0; i < quantity; ++i) {
                int index = numstars - i - 1;
                stars.remove(index);
            }
            setNumstars(numstars - quantity);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidstars, then restart it with 8 stars
        stars.clear();
        numstars = 0;
        addstars(5);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        //frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each star: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        try{

            for (int i = 0; i < numstars; ++i) {
                Star star = stars.get(i);
                if(star.bitmap!=null && !star.bitmap.isRecycled()){
                    m.setTranslate(-star.width/2, -star.height/2);
                    m.postRotate(star.rotation);
                    m.postTranslate(star.width / 2 + star.x, star.height / 2 + star.y);
                    //Paint p = new Paint();
                    p.setAlpha(star.alpha);
                    canvas.drawBitmap(star.bitmap, m, p);
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        // fps counter: count how many frames we draw and once a second calculate the
        // frames per second
//        ++frames;
//        long nowTime = System.currentTimeMillis();
//        long deltaTime = nowTime - startTime;
//        if (deltaTime > 1000) {
//            float secs = (float) deltaTime / 1000f;
//            fps = (float) frames / secs;
//            fpsString = "fps: " + fps;
//            startTime = nowTime;
//            frames = 0;
//        }
//        canvas.drawText(numstarsString, getWidth() - 200, getHeight() - 50, textPaint);
//        canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
    }

    public void pause() {
        // Make sure the animator's not spinning in the background when the activity is paused.
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}
