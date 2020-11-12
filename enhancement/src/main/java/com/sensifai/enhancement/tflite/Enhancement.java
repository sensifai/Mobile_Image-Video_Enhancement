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

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Keep;

import com.sensifai.enhancement.results.EnhancementResult;
import com.sensifai.enhancement.results.ProcessResult;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.util.LinkedHashMap;
import java.util.Map;

@Keep
public class Enhancement extends Processor<EnhancementResult> {
    private final boolean isDynamicInput;
    /**
     * load all models and put them on @EnhancementModels
     */
    private static final Map<String, ModelInfo<EnhancementResult>> ENHANCEMENT_MODELS = new LinkedHashMap<String, ModelInfo<EnhancementResult>>() {{
        put("Enhancement", new ModelInfo<>(
                "Sensifai_Enhancement_TFLite.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcessFEQE(), new PostProcessFEQE()));
        put("SuperResolution", new ModelInfo<>(
                "Sensifai_SuperResolution_TFLite.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_SuperResolution_FEQE(), new PostProcessSuperResolutionFEQE()));
        put("Enhancement16", new ModelInfo<>(
                "Sensifai_Enhancement_TFLite_float16.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcessFEQE(), new PostProcessFEQE()));

    }};
    /**
     *A tag identifying a group of log messages. Should be a constant in the
     *class calling the logger.
     */
    private static final String TAG = Enhancement.class.getSimpleName();

    /**
     * class constructor
     * @param isDynamicInput {@inheritDoc}
     */
    public Enhancement(boolean isDynamicInput) {
        super(ENHANCEMENT_MODELS);
        this.isDynamicInput = isDynamicInput;
    }

    @Override
    public ProcessResult<EnhancementResult> process(Bitmap[] images, int orientation) {
        long totalStartTime = SystemClock.uptimeMillis();

        if (isDynamicInput) {
            int[] shape = tfliteOutputs[0].getShape();
            shape[1] = (images[0].getHeight() / 4) * 4;
            shape[2] = (images[0].getWidth() / 4) * 4;
            DataType outputDataType = tfliteOutputs[0].getDataType();
            tfliteOutputs = new TensorBuffer[]{
                    TensorBuffer.createFixedSize(shape, outputDataType)
            };
            tflite.resizeInput(0, shape);
        }

        ProcessResult<EnhancementResult> result = super.process(images, orientation);

        if (result == null) {
            return null;
        }

        long totalTime = SystemClock.uptimeMillis() - totalStartTime;

        Log.i(TAG, String.format("Total time %d ms.", totalTime));
        Log.i(TAG, String.format("Resize time %d ms.", totalTime - result.getTotalTime()));

        return new ProcessResult<>(
                result.getResults(), new String[0], result.getInferenceTime(), totalTime);
    }
}
