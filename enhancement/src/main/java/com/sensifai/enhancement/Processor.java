package com.sensifai.enhancement;

import android.content.Context;
import android.graphics.Bitmap;

import com.sensifai.enhancement.results.ProcessResult;

public interface Processor<E> {
    boolean init(Context context, String modelName, Device device, int numThreads);

    boolean release();

    Context getContext();

    ProcessResult<E> process(Bitmap[] images, int orientation);
}
