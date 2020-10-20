package com.sensifai.enhancement.SNPE;

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
