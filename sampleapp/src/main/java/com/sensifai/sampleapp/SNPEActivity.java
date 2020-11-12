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

package com.sensifai.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.sensifai.enhancement.Device;
import com.sensifai.enhancement.snpe.Enhancement;
import com.sensifai.enhancement.results.EnhancementResult;
import com.sensifai.enhancement.results.ProcessResult;

/**
 * Note that this class is only for AndroidTests on SNPE functionality.
 * For TFLite Tests refer to {@link MainActivity}.
 * To run the AndroidTests refer to {@link SNPEActivityTest} in androidTest package
 */
public class SNPEActivity extends AppCompatActivity {
    public static final String ENHANCEMENT_SNPE_MODEL_NAME = "Enhancement";

    Enhancement enhancement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_n_p_e);
        enhancement = new Enhancement();
    }

    public boolean loadJNIso() {
        return enhancement.loadJNIso();
    }

    public boolean initModel() {
        return enhancement.init(this, ENHANCEMENT_SNPE_MODEL_NAME, Device.GPU, 1);
    }

    public ProcessResult<EnhancementResult> process(Bitmap[] bmps, int sizeOperation){
        return enhancement.process(bmps, sizeOperation);
    }
}