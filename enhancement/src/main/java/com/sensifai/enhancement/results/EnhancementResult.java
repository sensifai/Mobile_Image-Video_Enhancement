package com.sensifai.enhancement.results;

import android.graphics.Bitmap;

public class EnhancementResult {

    private final Bitmap[] enhancedImages;

    public EnhancementResult(Bitmap[] enhancedImages) {
        this.enhancedImages = enhancedImages;
    }

    public Bitmap[] getEnhancedImages() {
        return enhancedImages;
    }
}
