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

package com.sensifai.enhancement.tflite;

import android.graphics.Bitmap;

import com.sensifai.enhancement.results.EnhancementResult;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


class PostProcessFEQE implements PostProcess<EnhancementResult> {
    /**
     * each item must between 0,255 or 0,1
     */
    private final float[] mean;
    /**
     * each item must between 0,255 or 0,1
     */
    private final float[] std;

    /**
     * constructor of class
     */
    PostProcessFEQE() {
        this.mean = new float[]{0.33007812f, 0.33007812f, 0.33007812f};
        this.std = new float[]{0.2323942f, 0.22483271f, 0.22995465f};
    }

    public EnhancementResult[] apply(TensorBuffer[] outputs, int dstWidth, int dstHeight, int orientation, TensorBuffer inputBuffer) {
        int imageHeight = outputs[0].getShape()[1];
        int imageWidth = outputs[0].getShape()[2];
        int imageSize = imageWidth * imageHeight;

        float[] output = outputs[0].getFloatArray();
        int[] pixels = new int[imageSize];
        for (int i = 0; i < imageSize * 3; i += 3) {
            int a = 0xFF;
            float r = Math.min(Math.max(output[i] * std[0] + mean[0], 0), 1) * 255;
            float g = Math.min(Math.max(output[i + 1] * std[1] + mean[1], 0), 1) * 255;
            float b = Math.min(Math.max(output[i + 2] * std[2] + mean[2], 0), 1) * 255;
            pixels[i / 3] = a << 24 | ((int) r << 16) | ((int) g << 8) | (int) b;
        }

        Bitmap enhancedImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        enhancedImage.setPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        EnhancementResult result = new EnhancementResult(new Bitmap[]{enhancedImage});

        return new EnhancementResult[]{result};
    }
}
