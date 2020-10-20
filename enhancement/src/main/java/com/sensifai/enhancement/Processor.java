package com.sensifai.enhancement;

import android.content.Context;
import android.graphics.Bitmap;

import com.sensifai.enhancement.results.ProcessResult;

public interface Processor<E> {
    /**
     * initialize all requirement object and load model file based on @modelName argument
     * @param context
     * @param modelName The name of the model we intend to use
     * @param device Which hardware to use to perform the process
     * @param numThreads The number of threads to be used for the process
     * @return
     */
    boolean init(Context context, String modelName, Device device, int numThreads);

    /**
     * overrride this method to release all allocation memory and variables
     * @return true if success else return false
     */
    boolean release();

    /**
     * get current context
     * @return current context
     */
    Context getContext();

    /**
     * overrride this method to process given image(s)
     * @param images array of images that want to be process
     * @param orientation orientation of given image
     * @return  a generic list of result
     */
    ProcessResult<E> process(Bitmap[] images, int orientation);
}
