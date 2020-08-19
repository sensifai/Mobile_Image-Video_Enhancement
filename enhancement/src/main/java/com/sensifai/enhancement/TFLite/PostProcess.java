package com.sensifai.enhancement.TFLite;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

interface PostProcess<E> {
    E[] apply(TensorBuffer[] outputs, int dstWidth, int dstHeights, int orientation, TensorBuffer input);
}
