package com.sensifai.enhancement.results;

import android.graphics.Rect;

public class DetectionResult {
    /**
     * the box that contain face
     */
    private final Rect box;
    /**
     * {@inheritDoc}
     */
    private final int classIndex;
    /**
     * score of detection between 0,1
     */
    private final float score;

    /**
     * class constructor
     * @param box the box that contain face
     * @param classIndex {@inheritDoc}
     * @param score sore of detection
     */
    public DetectionResult(Rect box, int classIndex, float score) {
        this.box = box;
        this.classIndex = classIndex;
        this.score = score;
    }

    public Rect getBox() {
        return box;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public float getScore() {
        return score;
    }
}
