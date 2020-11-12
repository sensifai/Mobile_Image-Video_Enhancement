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

class ModelInfo<E> {
    private final String modelFileName;
    private final String labelFileName;
    private final ModelType modelType;
    private final PreProcess preprocessor;
    private final PostProcess<E> postprocessor;

    /**
     * constructor of class
     * @param modelFileName name of model
     * @param labelFileName name labels file
     * @param modelType type of chipset
     * @param preprocessor preProcess object
     * @param postprocessor postProcess object
     */
    ModelInfo(String modelFileName, String labelFileName, ModelType modelType,
              PreProcess preprocessor, PostProcess<E> postprocessor) {
        this.modelFileName = modelFileName;
        this.labelFileName = labelFileName;
        this.modelType = modelType;
        this.preprocessor = preprocessor;
        this.postprocessor = postprocessor;
    }

    String getModelFileName() {
        return modelFileName;
    }

    String getLabelFileName() {
        return labelFileName;
    }

    ModelType getModelType() {
        return modelType;
    }

    PreProcess getPreProcessor() {
        return preprocessor;
    }

    PostProcess<E> getPostProcessor() {
        return postprocessor;
    }

    /**
     * type of chipset
     */
    public enum ModelType {
        TFLite,
        SNPE
    }
}
