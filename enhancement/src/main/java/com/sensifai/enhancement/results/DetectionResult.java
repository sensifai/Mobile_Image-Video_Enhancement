package com.sensifai.enhancement.results;

import android.graphics.Rect;

public class DetectionResult {

    private final Rect box;
    private final int classIndex;
    private final float score;

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
