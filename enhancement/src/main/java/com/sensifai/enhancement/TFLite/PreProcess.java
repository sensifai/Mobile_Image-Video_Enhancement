package com.sensifai.enhancement.TFLite;

import android.graphics.Bitmap;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public interface PreProcess {
    /**
     * get tensor buffer value base on given value(height,width,mean,std)
     * @param bitmap image that want to be process
     * @param orientation orientation of given image
     * @return tensor buffer
     */
    TensorBuffer apply(Bitmap bitmap, int orientation);
}
