package com.sensifai.enhancement.TFLite;

import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


class PreProcess_FEQE implements PreProcess {

    private final float[] mean;
    private final float[] std;
    private TensorImage tensorImage;

    PreProcess_FEQE() {
        this.tensorImage = new TensorImage(DataType.FLOAT32);
        this.mean = new float[]{0.33007812f * 255, 0.33007812f * 255, 0.33007812f * 255};
        this.std = new float[]{0.2323942f * 255, 0.22483271f * 255, 0.22995465f * 255};
//        this.mean = new float[]{0, 0, 0};
//        this.std = new float[]{255, 255, 255};
    }

    public TensorBuffer apply(Bitmap bitmap, int orientation) {
        tensorImage.load(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int evenWidth = (width / 4) * 4;
        int evenHeight = (height / 4) * 4;

        // Creates processor for the TensorImage.
        ImageProcessor imageProcessor = evenWidth != width || evenHeight != height
                ? new ImageProcessor.Builder()
                .add(new ResizeOp(evenHeight, evenWidth, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(mean, std))
                .build()
                : new ImageProcessor.Builder()
                .add(new NormalizeOp(mean, std))
                .build();
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage.getTensorBuffer();
    }
}
