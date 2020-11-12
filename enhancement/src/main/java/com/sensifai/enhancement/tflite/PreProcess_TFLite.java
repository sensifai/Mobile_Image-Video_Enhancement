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
import org.tensorflow.lite.support.common.SupportPreconditions;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat;

class PreProcess_TFLite implements PreProcess {
    /**
     * {@inheritDoc}
     * @param width width of given image
     * @param height height of given image
     * @param isQuantized
     * @param bgr
     * @param mean each item must between 0,255
     * @param std each item must between 0,255
     */
    private final int width;
    private final int height;
    private final float[] mean;
    private final float[] std;
    private final boolean isBgr;
    private TensorImage tensorImage;

    PreProcess_TFLite(int width, int height, boolean isQuantized) {
        this.width = width;
        this.height = height;
        this.mean = isQuantized ? new float[]{0, 0, 0} : new float[]{127.5f, 127.5f, 127.5f};
        this.std = isQuantized ? new float[]{1, 1, 1} : new float[]{127.5f, 127.5f, 127.5f};
        this.isBgr = false;
        this.tensorImage = new TensorImage(isQuantized ? DataType.UINT8 : DataType.FLOAT32);
    }

    PreProcess_TFLite(int width, int height, float[] mean, float[] std, boolean isBgr) {
        this.width = width;
        this.height = height;
        this.mean = mean;
        this.std = std;
        this.isBgr = isBgr;
        this.tensorImage = new TensorImage(DataType.FLOAT32);
    }

    public TensorBuffer apply(Bitmap bitmap, int orientation) {
        tensorImage.load(bitmap);

        // Creates processor for the TensorImage.
        int numRoration = orientation / 90;
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
                        .add(new Rot90Op(-numRoration))
                        .add(new NormalizeOp(mean, std))
                        .add(new RGB2BGROp(isBgr))
                        .build();
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage.getTensorBuffer();
    }


    private class RGB2BGROp implements TensorOperator {
        private final boolean isIdentityOp;

        RGB2BGROp(boolean convert) {
            this.isIdentityOp = !convert;
        }

        public TensorBuffer apply(TensorBuffer input) {
            if (this.isIdentityOp) {
                return input;
            } else {
                int[] shape = input.getShape();
                SupportPreconditions.checkArgument(shape.length != 0 && shape[shape.length - 1] == 3, "Number of channels (size of last axis) should be 3.");
                float[] values = input.getFloatArray();

                for (int i = 0; i < values.length / 3; i += 3) {
                    float red = values[i];
                    values[i] = values[i + 2]; // red <-- blue
                    values[i + 2] = red; // blue <-- red
                }

                TensorBuffer output;
                if (input.isDynamic()) {
                    output = TensorBufferFloat.createDynamic(DataType.FLOAT32);
                } else {
                    output = TensorBufferFloat.createFixedSize(shape, DataType.FLOAT32);
                }

                output.loadArray(values, shape);
                return output;
            }
        }
    }
}
