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
        put("Enhancement - FEQE", new ModelInfo<>(
                "FEQE_SCE2_7_77_feature_16_block_10_float16_quantization_20.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE2", new ModelInfo<>(
                "FEQE_SCE2_7_77_feature_16_block_10-20.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE3", new ModelInfo<>(
                "FEQE_SCE2_without_normalize_layer_9_50_float16_quantization_Node.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE4", new ModelInfo<>(
                "bahri_7_77_feature_16_block_10_20_Without_Normalize_and_with_Gl.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE5", new ModelInfo<>(
                "FEQE_SCE2_7_77_feature_16_block_10-20_new.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE6", new ModelInfo<>(
                "bahri_7_77_feature_16_block_10_20_Without_Normalize_and_with_Gl (2).tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE7", new ModelInfo<>(
                "bahri_7_77_feature_16_block_10_20_without_Normalize_Clip_with_Global.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE8", new ModelInfo<>(
                "FEQE_SE_Scale2_Feature16_Score7_81_Without_Normalize_Clip_With_Global.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE9", new ModelInfo<>(
                "FEQE_SE_Scale2_Feature16_Score7_81_without_Normalize_Clip_ADD_with.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE10", new ModelInfo<>(
                "model_s2_full_20_without_Normalize_Clip_ADD_with_Global_aki.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE11", new ModelInfo<>(
                "model_s2_full-20_without_Normalize_Clip_Add.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE12", new ModelInfo<>(
                "FEQE_SE_Scale2_Feature16_Score7_81_Without_Normalize_Clip_With_Global.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE13", new ModelInfo<>(
                "model_without_scale_add.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE14", new ModelInfo<>(
                "model_s2_full-20without_scale_with_everything.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE15", new ModelInfo<>(
                "Final.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE16", new ModelInfo<>(
                "FEQE_SE_NoScale_R10_S9.76_Without_N_C_with_G_tf15_Float16.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE17", new ModelInfo<>(
                "FEQE_SE_NoScale_R10_S9_76_Without_N_C_with_G_tf2_3n_Default.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE18", new ModelInfo<>(
                "FEQE_SE_S4_R5_S7.57_Without_N_C_with_G_tf2.3n_Default.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE19", new ModelInfo<>(
                "FEQE_SE_S4_R5_S7.57_Without_N_C_with_G_tf2.3n_Float16.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE20", new ModelInfo<>(
                "FEQE_SE_NoScale_R10_S9_76_Without_N_C_with_G_tf2_3n_Float16.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE21", new ModelInfo<>(
                "FEQE_SE_S4_R5_S7.57_Without_N_C_with_G_tf2.3n_Float16_V2.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("Enhancement - FEQE22", new ModelInfo<>(
                "FEQE_SCE_S2_F16_R20_S9.47_without_N_C_with_G_tf2.3n_New.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_FEQE(), new PostProcess_FEQE()));
        put("SuperResolution - FEQE1", new ModelInfo<>(
                "Original_FEQE_SuperResolution.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_SuperResolution_FEQE(), new PostProcess_SuperResolution_FEQE()));
        put("SuperResolution - FEQE2", new ModelInfo<>(
                "Original_FEQE_SuperResolution_without_Normalize_and_clip.tflite", null,
                ModelInfo.ModelType.TFLite, new PreProcess_SuperResolution_FEQE(), new PostProcess_SuperResolution_FEQE()));
        put("SuperResolution - FEQE3", new ModelInfo<>(
                "model.ckpt_Original.tflite", null,
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
