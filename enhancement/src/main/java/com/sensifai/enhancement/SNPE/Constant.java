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
        put("Enhancement", new ModelInfo("Sensifai_Enhancement_SNPE", false,
                852, 480, 852, 480, 1, FEQE_PreprocessInfo));
        put("SuperResolution", new ModelInfo("Sensifai_SuperResolution_SNPE", false,
                852, 480, 852, 480, 1, FEQE_PreprocessInfo));
    }};
}