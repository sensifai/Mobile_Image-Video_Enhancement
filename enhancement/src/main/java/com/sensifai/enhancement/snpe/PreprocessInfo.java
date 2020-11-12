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

public class PreprocessInfo {

    private final boolean normalizeInput;
    private final double meanBlue;
    private final double meanGreen;
    private final double meanRed;
    private final double stdRed;
    private final double stdGreen;
    private final double stdBlue;
    private final boolean bgr;

    /**
     * {@inheritDoc}
     * @param normalizeInput
     * @param bgr
     * @param meanRed
     * @param meanGreen
     * @param meanBlue
     * @param stdRed
     * @param stdGreen
     * @param stdBlue
     */

    PreprocessInfo(boolean normalizeInput, boolean bgr,
                   double meanRed, double meanGreen, double meanBlue,
                   double stdRed, double stdGreen, double stdBlue) {
        this.normalizeInput = normalizeInput;
        this.meanBlue = meanBlue;
        this.meanGreen = meanGreen;
        this.meanRed = meanRed;
        this.stdBlue = stdBlue;
        this.stdGreen = stdGreen;
        this.stdRed = stdRed;
        this.bgr = bgr;
    }

    public boolean shouldNormalizeInput() {
        return normalizeInput;
    }

    public double getMeanBlue() {
        return meanBlue;
    }

    public double getMeanGreen() {
        return meanGreen;
    }

    public double getMeanRed() {
        return meanRed;
    }

    public double getStdBlue() {
        return stdBlue;
    }

    public double getStdGreen() {
        return stdGreen;
    }

    public double getStdRed() {
        return stdRed;
    }

    public boolean isBgr() {
        return bgr;
    }
}
