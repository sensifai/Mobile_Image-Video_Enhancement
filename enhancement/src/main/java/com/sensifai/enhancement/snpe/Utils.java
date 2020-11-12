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

package com.sensifai.enhancement.snpe;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import androidx.core.util.Pair;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;


class Utils {
    private static final String TAG = Enhancement.class.getSimpleName();
    private static final float[] greyWeights = new float[]{0.299f, 0.587f, 0.114f};

    /**
     * initialize and load cpp files
     * @return true if cpp files loaded successfuly else return false
     */
    static boolean loadJNISo() {
        try {
            System.loadLibrary("enhancement_utils");
            return true;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "failed to load native library: " + e.getMessage());
            return false;
        }
    }

    /**
     * decrypt model file from assets
     * @param fileName the name of the model file
     * @param assetManager responsible for assets manager
     * @return byte of file
     */
    static native byte[] decrypt(String fileName, AssetManager assetManager);

    /**
     * get given image(s) array
     * @param bitmaps given image to process
     * @param dstWidth width of the given image in pixels
     * @param dstHeight height of the given image in pixels
     * @param preprocessInfo preProcess values ex:mean,std and etc.
     * @return converted image to array
     */
    static Pair<float[], float[]> preprocess(Bitmap[] bitmaps, int dstWidth, int dstHeight, PreprocessInfo preprocessInfo) {
        double meanRed = preprocessInfo.getMeanRed();
        double meanGreen = preprocessInfo.getMeanGreen();
        double meanBlue = preprocessInfo.getMeanBlue();
        double stdRed = preprocessInfo.getStdRed();
        double stdGreen = preprocessInfo.getStdGreen();
        double stdBlue = preprocessInfo.getStdBlue();
        int channel = 3;
        float[] rgb = new float[channel * dstWidth * dstHeight * bitmaps.length];
        float[] gray = new float[dstWidth * dstHeight * bitmaps.length];
        for (int i = 0; i < bitmaps.length; i++) {
            int rgbOffset = channel * dstWidth * dstHeight * i;
            int grayOffset = dstWidth * dstHeight * i;
            int idx, batchIdx;
            int[] colors = new int[dstWidth * dstHeight];
            bitmaps[i].getPixels(colors, 0, dstWidth, 0, 0, dstWidth, dstHeight);

            PreprocFunc preproc;
            // If divide bgr/rgb values by a norm variable (1 or 255) it results in infinity in run mode!!
            // So there was no better way except separating states like this.
            if (preprocessInfo.shouldNormalizeInput()) {
                preproc = new PreprocFunc() {
                    @Override
                    public float apply(double val, double mean, double std) {
                        return (float) ((val / 255.0 - mean) / std);
                    }
                };
            } else {
                preproc = new PreprocFunc() {
                    @Override
                    public float apply(double val, double mean, double std) {
                        return (float) ((val - mean) / std);
                    }
                };
            }

            // Condition got out of loops to improve performance
            if (preprocessInfo.isBgr()) {
                for (int y = 0; y < dstHeight; y++) {
                    for (int x = 0; x < dstWidth; x++) {
                        idx = y * dstWidth + x;
                        batchIdx = idx * 3;
                        rgb[rgbOffset + batchIdx] = preproc.apply(blue(colors[idx]), meanBlue, stdBlue);
                        rgb[rgbOffset + batchIdx + 1] = preproc.apply(green(colors[idx]), meanGreen, stdGreen);
                        rgb[rgbOffset + batchIdx + 2] = preproc.apply(red(colors[idx]), meanRed, stdRed);
                        gray[grayOffset + idx] = 1f - (greyWeights[0] * (rgb[batchIdx + 2] + 1) + greyWeights[1] * (rgb[batchIdx + 1] + 1) + greyWeights[2] * (rgb[batchIdx] + 1)) / 2f;
                    }
                }
            } else {
                for (int y = 0; y < dstHeight; y++) {
                    for (int x = 0; x < dstWidth; x++) {
                        idx = y * dstWidth + x;
                        batchIdx = idx * 3;
                        rgb[rgbOffset + batchIdx] = preproc.apply(red(colors[idx]), meanRed, stdRed);
                        rgb[rgbOffset + batchIdx + 1] = preproc.apply(green(colors[idx]), meanGreen, stdGreen);
                        rgb[rgbOffset + batchIdx + 2] = preproc.apply(blue(colors[idx]), meanBlue, stdBlue);
                        gray[grayOffset + idx] = 1f - (greyWeights[0] * (rgb[batchIdx] + 1) + greyWeights[1] * (rgb[batchIdx + 1] + 1) + greyWeights[2] * (rgb[batchIdx + 2] + 1)) / 2f;
                    }
                }
            }
        }

        return new Pair<>(rgb, gray);
    }

    /**
     * recreate image after process
     * @param buff array of images buffer
     * @param dstWidth width of the given image in pixels
     * @param dstHeight height of the given image in pixels
     * @param preprocessInfo preProcess values ex:mean,std and etc.
     * @return enhanced image(s)
     */
    static Bitmap[] postprocess(float[] buff, int dstWidth, int dstHeight, PreprocessInfo preprocessInfo) {
        double meanRed = preprocessInfo.getMeanRed();
        double meanGreen = preprocessInfo.getMeanGreen();
        double meanBlue = preprocessInfo.getMeanBlue();
        double stdRed = preprocessInfo.getStdRed();
        double stdGreen = preprocessInfo.getStdGreen();
        double stdBlue = preprocessInfo.getStdBlue();
        int idx, batchIdx;
        int[] colors = new int[dstWidth * dstHeight];
        int imageCount = buff.length / (3 * colors.length);
        Bitmap[] outputs = new Bitmap[imageCount];

        PreprocFunc preproc;
        // If divide bgr/rgb values by a norm variable (1 or 255) it results in infinity in run mode!!
        // So there was no better way except separating states like this.
        if (preprocessInfo.shouldNormalizeInput()) {
            preproc = new PreprocFunc() {
                @Override
                public float apply(double val, double mean, double std) {
                    return Math.max(0, Math.min(255, (float) ((val * std + mean) * 255.0)));
                }
            };
        } else {
            preproc = new PreprocFunc() {
                @Override
                public float apply(double val, double mean, double std) {
                    return Math.max(0, Math.min(255, (float) (val * std + mean)));
                }
            };
        }

        for (int imgIdx = 0; imgIdx < imageCount; imgIdx++) {
            // Condition got out of loops to improve performance
            if (preprocessInfo.isBgr()) {
                for (int y = 0; y < dstHeight; y++) {
                    for (int x = 0; x < dstWidth; x++) {
                        idx = y * dstWidth + x;
                        batchIdx = 3 * (idx + imgIdx * colors.length);
                        int blue = (int) preproc.apply(buff[batchIdx], meanBlue, stdBlue);
                        int green = (int) preproc.apply(buff[batchIdx + 1], meanGreen, stdGreen);
                        int red = (int) preproc.apply(buff[batchIdx + 2], meanRed, stdRed);
                        colors[idx] = rgb(red, green, blue);
                    }
                }
            } else {
                for (int y = 0; y < dstHeight; y++) {
                    for (int x = 0; x < dstWidth; x++) {
                        idx = y * dstWidth + x;
                        batchIdx = 3 * (idx + imgIdx * colors.length);
                        int red = (int) preproc.apply(buff[batchIdx], meanRed, stdRed);
                        int green = (int) preproc.apply(buff[batchIdx + 1], meanGreen, stdGreen);
                        int blue = (int) preproc.apply(buff[batchIdx + 2], meanBlue, stdBlue);
                        colors[idx] = rgb(red, green, blue);
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(colors, 0, dstWidth, 0, 0, dstWidth, dstHeight);
            outputs[imgIdx] = bitmap;
        }
        return outputs;
    }

    private interface PreprocFunc {
        float apply(double v, double m, double s);
    }
}
