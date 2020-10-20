package com.sensifai.enhancement.TFLite;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.sensifai.enhancement.results.DetectionResult;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.util.ArrayList;

class PostProcess_SSD implements PostProcess<DetectionResult> {
    private final double confidenceThreshold;
    private final int topK;
    private final float minSize;

    /**
     * constructor of class
     * @param confidenceThreshold{@inheritDoc}
     * @param topK max number of process result
     * @param minSize {@inheritDoc}
     */
    PostProcess_SSD(double confidenceThreshold, int topK, float minSize) {
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
                    Math.min(Math.max(0, outputLocations[i * 4 + 0]), 1),
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
