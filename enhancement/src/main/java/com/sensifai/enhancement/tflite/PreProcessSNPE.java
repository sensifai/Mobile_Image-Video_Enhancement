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
import android.graphics.Matrix;
import android.graphics.RectF;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

class PreProcessSNPE implements PreProcess {
    private static final int CHANNELS = 3;
    private final int width;
    private final int height;
    private final boolean normalizeInput;
    private final float[] mean;
    private final float[] std;
    private final boolean bgr;
    private final TensorBuffer tensorBuffer;

    /**
     * {@inheritDoc}
     *
     * @param width          width of given image
     * @param height         height of given image
     * @param normalizeInput
     * @param bgr
     * @param mean           each item must between 0,255 or 0,1
     * @param std            each item must between 0,255 or 0,1
     */
    // mean & std values are in rgb order
    PreProcessSNPE(int width, int height, boolean normalizeInput,
                   boolean bgr, float[] mean, float[] std) {
        this.width = width;
        this.height = height;
        this.normalizeInput = normalizeInput;
        this.mean = mean;
        this.std = std;
        this.bgr = bgr;
        this.tensorBuffer = TensorBuffer.createFixedSize(
                new int[]{CHANNELS * width * height}, DataType.FLOAT32);
    }

    public TensorBuffer apply(Bitmap bitmap, int orientation) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        Matrix transform = new Matrix();
        transform.setRectToRect(
                new RectF(0, 0, srcWidth, srcHeight),
                new RectF(0, 0, width, height), Matrix.ScaleToFit.FILL);
        if (orientation != 0) {
            float deltaX = width / 2f;
            float deltaY = height / 2f;
            // Translate so center of image is at origin.
            transform.postTranslate(-deltaX, -deltaY);
            // Rotate around origin.
            transform.postRotate(orientation);
            // Translate back from origin centered reference to destination frame.
            transform.postTranslate(deltaX, deltaY);
        }

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, transform, true);

        float[] buff = new float[CHANNELS * width * height];
        int idx, batchIdx;
        int[] colors = new int[width * height];
        bitmap.getPixels(colors, 0, width, 0, 0, width, height);

        PreprocFunc preproc;
        // If divide bgr/rgb values by a norm variable (1 or 255) it results in infinity in run mode!!
        // So there was no better way except separating states like this.
        if (normalizeInput) {
            preproc = new PreprocFunc() {
                @Override
                public float apply(float val, float mean, float std) {
                    return (val / 255.0f - mean) / std;
                }
            };
        } else {
            preproc = new PreprocFunc() {
                @Override
                public float apply(float val, float mean, float std) {
                    return (val - mean) / std;
                }
            };
        }

        // Condition got out of loops to improve performance
        if (bgr) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    idx = y * width + x;
                    batchIdx = idx * 3;
                    buff[batchIdx] = preproc.apply(blue(colors[idx]), mean[2], std[2]);
                    buff[batchIdx + 1] = preproc.apply(green(colors[idx]), mean[1], std[1]);
                    buff[batchIdx + 2] = preproc.apply(red(colors[idx]), mean[0], std[0]);
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    idx = y * width + x;
                    batchIdx = idx * 3;
                    buff[batchIdx] = preproc.apply(red(colors[idx]), mean[0], std[0]);
                    buff[batchIdx + 1] = preproc.apply(green(colors[idx]), mean[1], std[1]);
                    buff[batchIdx + 2] = preproc.apply(blue(colors[idx]), mean[2], std[2]);
                }
            }
        }

        tensorBuffer.loadArray(buff);
        return tensorBuffer;
    }

    private interface PreprocFunc {
        float apply(float v, float m, float s);
    }
}
