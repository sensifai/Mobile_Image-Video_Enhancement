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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


class PreProcess_SuperResolution_FEQE implements PreProcess {

    /**
     * each item must between 0,255 or 0,1
     */
    private final float[] mean;
    /**
     * each item must between 0,255 or 0,1
     */
    private final float[] std;
    private TensorImage tensorImage;
    /**
     * {@inheritDoc}
     *
     */
    PreProcess_SuperResolution_FEQE() {
        this.tensorImage = new TensorImage(DataType.FLOAT32);
        this.mean = new float[]{0.5f * 255, 0.5f * 255, 0.5f * 255};
        this.std = new float[]{1 / 255f * 255, 1 / 255f * 255, 1 / 255f * 255};
//        this.mean = new float[]{0, 0, 0};
//        this.std = new float[]{255, 255, 255};
    }

    public TensorBuffer apply(Bitmap bitmap, int orientation) {
        tensorImage.load(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int evenWidth = (width / 4) * 4;
        int evenHeight = (height / 4) * 4;

        // Creates processor for the TensorImage.
        ImageProcessor imageProcessor = evenWidth != width || evenHeight != height
                ? new ImageProcessor.Builder()
                .add(new ResizeOp(evenHeight, evenWidth, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(mean, std))
                .build()
                : new ImageProcessor.Builder()
                .add(new NormalizeOp(mean, std))
                .build();
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage.getTensorBuffer();
    }
}
