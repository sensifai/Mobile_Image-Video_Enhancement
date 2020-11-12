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

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sensifai.enhancement.results.DetectionResult;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.util.ArrayList;

class PostProcessSSD implements PostProcess<DetectionResult> {
    private final double confidenceThreshold;
    private final int topK;
    private final float minSize;

    /**
     * constructor of class
     *
     * @param confidenceThreshold{@inheritDoc}
     * @param topK                             max number of process result
     * @param minSize                          {@inheritDoc}
     */
    PostProcessSSD(double confidenceThreshold, int topK, float minSize) {
        this.confidenceThreshold = confidenceThreshold;
        this.topK = topK == -1 ? Integer.MAX_VALUE : topK;
        this.minSize = minSize <= 0 ? 0 : minSize;
    }

    public DetectionResult[] apply(TensorBuffer[] outputs, int dstWidth, int dstHeight, int orientation, TensorBuffer input) {
        float[] outputLocations = outputs[0].getFloatArray();
        float[] outputClasses = outputs[1].getFloatArray();
        float[] outputScores = outputs[2].getFloatArray();
        int numDetections = Math.min((int) outputs[3].getFloatArray()[0], topK);
        final ArrayList<DetectionResult> detections = new ArrayList<>(topK);

        Matrix transform = new Matrix();
        if (orientation != 0) {
            // Translate so center of image is at origin.
            transform.postTranslate(-0.5f, -0.5f);
            // Rotate around origin.
            transform.postRotate(-orientation);
            // Translate back from origin centered reference to destination frame.
            transform.postTranslate(0.5f, 0.5f);
        }
        transform.postScale(dstWidth, dstHeight);

        for (int i = 0; i < numDetections && outputScores[i] >= confidenceThreshold; ++i) {
            RectF bboxF = new RectF(
                    Math.min(Math.max(0, outputLocations[i * 4 + 1]), 1),
                    Math.min(Math.max(0, outputLocations[i * 4]), 1),
                    Math.min(Math.max(0, outputLocations[i * 4 + 3]), 1),
                    Math.min(Math.max(0, outputLocations[i * 4 + 2]), 1));
            if (bboxF.width() < minSize || bboxF.height() < minSize) {
                continue;
            }
            transform.mapRect(bboxF);
            final Rect bbox = new Rect();
            bboxF.round(bbox);

            detections.add(new DetectionResult(
                    bbox, (int) outputClasses[i] + 1, outputScores[i]));
        }
        detections.trimToSize();
        return detections.toArray(new DetectionResult[0]);
    }
}
