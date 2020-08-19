package com.sensifai.enhancement.TFLite;

import android.graphics.Bitmap;

import com.sensifai.enhancement.results.EnhancementResult;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;


class PostProcess_FEQE implements PostProcess<EnhancementResult> {

    private final float[] mean;
    private final float[] std;

    PostProcess_FEQE() {
        this.mean = new float[]{0.33007812f, 0.33007812f, 0.33007812f};
        this.std = new float[]{0.2323942f, 0.22483271f, 0.22995465f};
//        this.mean = new float[]{0, 0, 0};
//        this.std = new float[]{1, 1, 1};
    }

    public EnhancementResult[] apply(TensorBuffer[] outputs, int dstWidth, int dstHeight, int orientation, TensorBuffer inputBuffer) {
        int imageHeight = outputs[0].getShape()[1];
        int imageWidth = outputs[0].getShape()[2];
        int imageSize = imageWidth * imageHeight;

        float[] output = outputs[0].getFloatArray();
        int[] pixels = new int[imageSize];
        for (int i = 0; i < imageSize * 3; i += 3) {
            int a = 0xFF;
            float r = Math.min(Math.max(output[i] * std[0] + mean[0], 0), 1) * 255;
            float g = Math.min(Math.max(output[i + 1] * std[1] + mean[1], 0), 1) * 255;
            float b = Math.min(Math.max(output[i + 2] * std[2] + mean[2], 0), 1) * 255;
            pixels[i / 3] = a << 24 | ((int) r << 16) | ((int) g << 8) | (int) b;
        }

        Bitmap enhancedImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        enhancedImage.setPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);

        EnhancementResult result = new EnhancementResult(new Bitmap[]{enhancedImage});

        return new EnhancementResult[]{result};
    }
}
