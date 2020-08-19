package com.sensifai.enhancement.SNPE;

import java.util.LinkedHashMap;
import java.util.Map;

class Constant {
    //    private static final PreprocessInfo EnlightenGAN_PreprocessInfo =
//            new PreprocessInfo(true, false,
//                    0.5, 0.5, 0.5,
//                    0.5, 0.5, 0.5);
    private static final PreprocessInfo FEQE_No_PreprocessInfo =
            new PreprocessInfo(true, false,
                    0, 0, 0,
                    1, 1, 1);
    private static final PreprocessInfo FEQE_PreprocessInfo =
            new PreprocessInfo(true, false,
                    0.33007812, 0.33007812, 0.33007812,
                    0.2323942, 0.22483271, 0.22995465);
    private static final PreprocessInfo FEQE_SuperRes_PreprocessInfo =
            new PreprocessInfo(true, false,
                    0.5, 0.5, 0.5,
                    1 / 255.0, 1 / 255.0, 1 / 255.0);

    static final Map<String, ModelInfo> AllModels = new LinkedHashMap<String, ModelInfo>() {{
        put("FEQE - 1", new ModelInfo("model_scale_s_2_SCE_sigmoid-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 2", new ModelInfo("FEQE-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 3", new ModelInfo("FEQE_SCE2_Without_GAP_without_normalize_layer_9_50-20-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 4", new ModelInfo("FEQE_SCE2_Without_GAP_without_normalize_layer_8_84-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 5", new ModelInfo("FEQE_SCE2_With_GAP_without_normalize_layer_8_84-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 6", new ModelInfo("FEQE_SCE2_7_77_feature_16_block_10-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 7", new ModelInfo("Original_FEQE", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 8", new ModelInfo("Original_FEQE_with_Normalize_Restore_layer", false,
                512, 512, 512, 512, 1, FEQE_No_PreprocessInfo));
        put("FEQE - 9", new ModelInfo("model_scale_s_4-20_2", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 10", new ModelInfo("model_scale_s_4-20", false,
                512, 512, 512, 512, 1, FEQE_No_PreprocessInfo));
        put("FEQE - 11", new ModelInfo("FEQE_SCE_scale_4_desubpixel_without_conv-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 12", new ModelInfo("FEQE_SCE_scale_8_desubpixel_without_conv-20", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 13", new ModelInfo("FEQE_SCE_scale_4__desubpixel_without_conv_5_block", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 14", new ModelInfo("FEQE_SCE_scale_4_5_Block_7-41_score_1_desubpixel", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 15", new ModelInfo("FEQE_SE_scale_4_5_Block_1_desubpixel", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 16", new ModelInfo("FEQE_SCE_scale_8_3_block_1_desubpixel", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 17", new ModelInfo("FEQE_SCE_Scale8_Score6.31_ResBlock3_Final", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 18", new ModelInfo("FEQE_SE_Scale4_Score7.57_ResBlock5_Final", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("FEQE - 19", new ModelInfo("FEQE_SE_5B_7.57_852x480", false,
                852, 480, 852, 480, 1, FEQE_PreprocessInfo));
        put("FEQE - 20", new ModelInfo("FEQE_SE_5B_7.57_shape_5_852_480_3", false,
                852, 480, 852, 480, 5, FEQE_PreprocessInfo));
        put("FEQE - 21", new ModelInfo("FEQE_SE_5B_7.57_shape_10_852_480_3", false,
                852, 480, 852, 480, 10, FEQE_PreprocessInfo));
        put("FEQE - 22", new ModelInfo("FEQE_SE_5B_7.57_shape_15_852_480_3", false,
                852, 480, 852, 480, 15, FEQE_PreprocessInfo));
        put("FEQE - 23", new ModelInfo("Model_S4_R5_F48_S7.57_WNR_852x480", false,
                852, 480, 852, 480, 1, FEQE_PreprocessInfo));
        put("SuperResolution - FEQE1", new ModelInfo("Original_SP_FEQE", false,
                512, 512, 512, 512, 1, FEQE_PreprocessInfo));
        put("SuperResolution - FEQE2", new ModelInfo("Original_SP_FEQE_852x480", false,
                852, 480, 852, 480, 1, FEQE_PreprocessInfo));
//        put("EnlightenGAN", new ModelInfo("EnlightenGAN", false,
//                400,608,400, 608, EnlightenGAN_PreprocessInfo));
    }};
}