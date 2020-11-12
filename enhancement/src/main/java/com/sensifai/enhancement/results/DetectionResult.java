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
