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

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * This class represents a single Droidflake, with properties representing its
 * size, rotation, location, and speed.
 */
@SuppressLint("UseSparseArrays")
public class Star {

    // These are the unique properties of any flake: its size, rotation, speed,
    // location, and its underlying Bitmap object
    float x, y;
    float rotation;
    float rotationSpeed;
    int width, height;
    int alpha;
    boolean isFadin;
    Bitmap bitmap;

    // This map stores pre-scaled bitmaps according to the width. No reason to create
    // new bitmaps for sizes we've already seen.
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    /**
     * Creates a new droidflake in the given xRange and with the given bitmap. Parameters of
     * location, size, rotation, and speed are randomly determined.
     */
    static Star createstar(float xRange,float yRange, Bitmap originalBitmap) {
        try{

            Star star = new Star();
            // Size each flake with a width between 5 and 55 and a proportional height
            star.width = (int)(10 + (float) Math.random() * 30);
            float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
            star.height = (int)(star.width * hwRatio);

            star.alpha = (int)(Math.random()*128);
            //star.isFadin = (int)Math.random()%2!=0;
            star.isFadin = false;


            // Position the flake horizontally between the left and right of the range
            star.x = (float) Math.random() * (xRange - star.width);
            // Position the flake vertically slightly off the top of the display
            star.y = (float) Math.random() * (yRange - star.height);

            // star start at -90 to 90 degrees rotation, and rotate between -45 and 45
            // degrees per second
            star.rotation = (float) Math.random() * 100;
            star.rotationSpeed = (float) Math.random() * 200;

            // Get the cached bitmap for this size if it exists, otherwise create and cache one
            star.bitmap = bitmapMap.get(star.width);
            if (star.bitmap == null || star.bitmap.isRecycled()) {
                star.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                        (int) star.width, (int) star.height, true);
                bitmapMap.put(star.width, star.bitmap);
            }


            return star;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new Star();
    }

    public void resetXY(float xRange,float yRange){
        x = (float) Math.random() * (xRange - width);
        y = (float) Math.random() * (yRange - height);
    }
}
