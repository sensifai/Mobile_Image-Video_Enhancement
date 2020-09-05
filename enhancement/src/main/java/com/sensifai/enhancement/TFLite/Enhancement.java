package com.sensifai.enhancement.TFLite;

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

    private static final Map<String, ModelInfo<EnhancementResult>> EnhancementModels = new LinkedHashMap<String, ModelInfo<EnhancementResult>>() {{
        put("Enhancement", new ModelInfo<>(
                "Sensifai_Enhancement_TFLite.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("SuperResolution", new ModelInfo<>(
                "Sensifai_SuperResolution_TFLite.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_SuperResolution_FEQE(), new PostProcess_SuperResolution_FEQE()));

    }};

    private static final String TAG = Enhancement.class.getSimpleName();

    public Enhancement(boolean isDynamicInput) {
        super(EnhancementModels);
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

        if (result == null)
            return null;

        long totalTime = SystemClock.uptimeMillis() - totalStartTime;

        Log.i(TAG, String.format("Total time %d ms.", totalTime));
        Log.i(TAG, String.format("Resize time %d ms.", totalTime - result.getTotalTime()));

        return new ProcessResult<>(
                result.getResults(), new String[0], result.getInferenceTime(), totalTime);
    }
}
