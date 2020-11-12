/*
 * MIT License
 *
 * Copyright (c)2020 Sensifai
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.sensifai.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.sensifai.enhancement.Device;
import com.sensifai.enhancement.tflite.Enhancement;
import com.sensifai.enhancement.results.EnhancementResult;
import com.sensifai.enhancement.results.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Note that this class is only for AndroidTests on TFLite functionality.
 * For SNPE Tests refer to {@link SNPEActivity}.
 * To run the AndroidTests refer to {@link MainActivityTest} in androidTest package
 */
public class MainActivity extends AppCompatActivity {
    Enhancement enhancement, superResolution;
    public static final String ENHANCEMENT_TFLITE_MODEL_NAME = "Enhancement";
    public static final int ENHANCEMENT_PROCESS_TYPE = 1;
    public static final int SUPER_RESOLUTION_PROCESS_TYPE = 0;
    public static final float SUPER_RES_COEFF = 2;
    public static final int MAX_BITMAP_SIZE = 12 * 1024 * 1024; //12 megapixels
    public static final int MAX_INPUT_SIZE = 8 * 1024 * 1024; //8 megapixels
    public static Bitmap emptyBmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    private InputStream originalFile;
    public static final File BaseDirectory = new File(Environment.getExternalStorageDirectory(), "ImageEnhancer");
    public static final File tempFile = new File(BaseDirectory, "temp.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public boolean initModel() {
        enhancement = new com.sensifai.enhancement.tflite.Enhancement(true);
        return enhancement.init(this, ENHANCEMENT_TFLITE_MODEL_NAME, Device.GPU, 1);
    }

    public ProcessResult<EnhancementResult> processImage(int type) {
        try {
            originalFile = getAssets().open("test.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessResult<EnhancementResult> result = null;
        enhancement = new com.sensifai.enhancement.tflite.Enhancement(true);
        enhancement.init(MainActivity.this, ENHANCEMENT_TFLITE_MODEL_NAME, Device.GPU, 1);
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap scaled = BitmapFactory.decodeStream(originalFile);
            ;
            int width;
            int height;
            if (type == SUPER_RESOLUTION_PROCESS_TYPE) {
                Bitmap original = BitmapFactory.decodeStream(originalFile);
                width = (int) (options.outWidth * SUPER_RES_COEFF);
                height = (int) (options.outHeight * SUPER_RES_COEFF);
            } else {
                Bitmap original = BitmapFactory.decodeStream(originalFile);
                width = options.outWidth;
                height = options.outHeight;
            }
            int scale = 1;
            while ((width / scale) * (height / scale) > MAX_BITMAP_SIZE) {
                scale *= 2;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            double newScale = Math.max(Math.sqrt(scaled.getWidth() * scaled.getHeight() / (float) MAX_INPUT_SIZE), 1);
            scaled = Bitmap.createScaledBitmap(scaled, (int) (scaled.getWidth() / newScale),
                    (int) (scaled.getHeight() / newScale), false);
            result = type == SUPER_RESOLUTION_PROCESS_TYPE ? superResolution.process(new Bitmap[]{scaled}, 0) : enhancement.process(new Bitmap[]{scaled}, 0);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            result = new ProcessResult<>(new ArrayList<>(),
                    new String[0], 0, 0);
        }
        //This seems to be the only way to free up memory after process so far
        if (type == SUPER_RESOLUTION_PROCESS_TYPE) {
            superResolution.process(new Bitmap[]{emptyBmp}, 0);
        } else {

            enhancement.process(new Bitmap[]{emptyBmp}, 0);
            release();
        }
        return result;
    }


    public boolean release() {
        return enhancement.release();
    }

}