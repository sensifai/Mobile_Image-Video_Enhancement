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