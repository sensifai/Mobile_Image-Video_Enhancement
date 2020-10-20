package com.sensifai.enhancement.results;

import android.graphics.Bitmap;

public class EnhancementResult {
    /**
     * responsible to get enhanced image(s)
     */
    private final Bitmap[] enhancedImages;

    /**
     * class constructor
     *
     * @param enhancedImages responsible to get enhanced image(s) and put it on @enhancedImages
     */
    public EnhancementResult(Bitmap[] enhancedImages) {
        this.enhancedImages = enhancedImages;
    }

    public Bitmap[] getEnhancedImages() {
        return enhancedImages;
    }
}
