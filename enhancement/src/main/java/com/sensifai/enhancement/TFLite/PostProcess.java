package com.sensifai.enhancement.TFLite;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

interface PostProcess<E> {
    /**
     * process the result off process image
     * @param outputs loaded model
     * @param dstWidth width of the given image in pixels
     * @param dstHeights height of the given image in pixels
     * @param orientation orientation of given image
     * @param input the tensor buffer value that return from preProcess method
     * @return based on given generic class
     */
    E[] apply(TensorBuffer[] outputs, int dstWidth, int dstHeights, int orientation, TensorBuffer input);
}
