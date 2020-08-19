package com.sensifai.enhancement.TFLite;

import android.graphics.Bitmap;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public interface PreProcess {
    TensorBuffer apply(Bitmap bitmap, int orientation);
}
