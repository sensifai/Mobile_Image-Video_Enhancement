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

public class ModelInfo {

    private final String modelName;
    private final boolean encrypted;
    private final int inputH;
    private final int inputW;
    private final PreprocessInfo preprocessInfo;
    private final int outputH;
    private final int outputW;
    private final int batchSize;

    /**
     * @param modelName      name of model that want to use
     * @param encrypted      if model that we want to use is encrypted pass true else false
     * @param inputH         height of the given image in pixels
     * @param inputW         width of the given image in pixels
     * @param outputH        height of the returned image in pixels
     * @param outputW        width of the returned image in pixels
     * @param batchSize      number of batch process
     * @param preprocessInfo preProcess values ex:mean,std and etc.
     */
    ModelInfo(String modelName, boolean encrypted,
              int inputH, int inputW, int outputH, int outputW, int batchSize,
              PreprocessInfo preprocessInfo) {
        this.modelName = modelName;
        this.encrypted = encrypted;
        this.inputH = inputH;
        this.inputW = inputW;
        this.outputH = outputH;
        this.outputW = outputW;
        this.batchSize = batchSize;
        this.preprocessInfo = preprocessInfo;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public int getInputH() {
        return inputH;
    }

    public int getInputW() {
        return inputW;
    }

    public int getOutputH() {
        return outputH;
    }

    public int getOutputW() {
        return outputW;
    }

    public PreprocessInfo getPreprocessInfo() {
        return preprocessInfo;
    }

    public int getBatchSize() {
        return batchSize;
    }
}
