/*
 * MIT License
 *
 * Copyright (c)2020 Sensifai
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.sensifai.enhancement.snpe;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Size;

import androidx.core.util.Pair;

import com.qualcomm.qti.snpe.FloatTensor;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qualcomm.qti.snpe.SNPE;
import com.sensifai.enhancement.Device;
import com.sensifai.enhancement.Processor;
import com.sensifai.enhancement.results.EnhancementResult;
import com.sensifai.enhancement.results.ProcessResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.sensifai.enhancement.snpe.Constant.AllModels;


public class Enhancement implements Processor<EnhancementResult> {

    private static final String TAG = Enhancement.class.getSimpleName();
    private boolean isJNILoaded;
    private ModelInfo model;
    private NeuralNetwork network;
    private String inputNameRGB;
    private String outputName;
    private Map<String, FloatTensor> inputs;
    private Context context;

    /**
     * initialize and load cpp files
     *
     * @return true if cpp files loaded successfuly else return false
     */
    public boolean loadJNIso() {
        if (!isJNILoaded) {
            isJNILoaded = Utils.loadJNISo();
        }
        return isJNILoaded;
    }

    /**
     * initialize all requirement object and load model file based on @modelName argument
     * @param context to get application context
     * @param modelName The name of the model we intend to use
     * @param device Which hardware to use to perform the process
     * @param numThreads The number of threads to be used for the process
     * @return return true if everything ok else return false
     */
    public boolean init(Context context, String modelName, Device device, int numThreads) {
        Log.i(TAG, String.format("Initializing model %s.", modelName));
        this.context = context;
        Application application = (Application) context.getApplicationContext();

        if (network != null) {
            Log.e(TAG, "Already initialized.");
            return true;
        }
        if (!loadJNIso()) {
            Log.e(TAG, "Cannot load JNI shared object.");
            return false;
        }
        model = AllModels.get(modelName);
        if (model == null) {
            Log.e(TAG, String.format("No model exists named %s.", modelName));
            return false;
        }

        try {
            String modelFile = model.getModelName() + ".dlc";
            InputStream modelData = model.isEncrypted()
                    ? new ByteArrayInputStream(Utils.decrypt(modelFile, application.getAssets()))
                    : application.getAssets().open(modelFile);
            int modelSize = modelData.available();
            NeuralNetwork.Runtime runtime = NeuralNetwork.Runtime.valueOf(device.name());
            Log.i(TAG, String.format("Model loaded successfully. Building network on %s.", runtime));
            final SNPE.NeuralNetworkBuilder builder = new SNPE.NeuralNetworkBuilder(application)
                    .setDebugEnabled(false)
                    .setRuntimeOrder(runtime)
                    .setModel(modelData, modelSize)
                    .setCpuFallbackEnabled(true)
                    .setUseUserSuppliedBuffers(false)
                    .setExecutionPriorityHint(NeuralNetwork.ExecutionPriorityHint.HIGH);
            network = builder.build();
            Log.i(TAG, "Network built successfully.");
            modelData.close();

            Set<String> inputNames = network.getInputTensorsNames();
            Set<String> outputNames = network.getOutputTensorsNames();
            Log.i(TAG, String.format("Network Inputs: %s, Network Outputs: %s",
                    TextUtils.join(",", inputNames),
                    TextUtils.join(",", outputNames)));
            if (inputNames.size() > 2 || outputNames.size() != 1) {
                throw new IllegalStateException("Invalid network input and/or output tensors.");
            } else {
                Iterator<String> it = inputNames.iterator();
                inputNameRGB = it.next();
                // inputName_Gray = it.next();
                outputName = outputNames.iterator().next();
                final FloatTensor inputTensor_RGB = network.createFloatTensor(
                        network.getInputTensorsShapes().get(inputNameRGB));
                // final FloatTensor inputTensor_Gray = network.createFloatTensor(
                //         network.getInputTensorsShapes().get(inputName_Gray));
                inputs = new HashMap<>();
                inputs.put(inputNameRGB, inputTensor_RGB);
                // inputs.put(inputName_Gray, inputTensor_Gray);
            }
        } catch (IllegalStateException | IOException e) {
            Log.e(TAG, e.getMessage(), e);
            release();
        }

        return network != null;
    }

    public Size getModelSize() {
        if (model == null) {
            Log.e(TAG, "Not initialized.");
            return null;
        }
        return new Size(model.getInputW(), model.getInputH());
    }

    @Override
    public ProcessResult<EnhancementResult> process(Bitmap[] images, int sizeOperation) {
        Log.i(TAG, String.format(Locale.US, "Processing %d images %dx%d.", images.length, images[0].getWidth(), images[0].getHeight()));

        if (network == null) {
            Log.e(TAG, "Not initialized.");
            return null;
        }

        try {
            long startTime = System.nanoTime();

            //doing size operations on input images before pre-process
            Bitmap[] processReadyImages = new Bitmap[images.length];
            Matrix rotateMatrix = new Matrix();
            rotateMatrix.postRotate(90);

            switch (sizeOperation) {
                case 0:
                    processReadyImages = images;
                    break;
                case 1:
                    for (int i = 0; i < images.length; i++) {
                        processReadyImages[i] = Bitmap.createBitmap(images[i], 0, 0, images[i].getWidth(), images[i].getHeight(), rotateMatrix, true);
                    }
                    break;
                case 2:
                    for (int i = 0; i < images.length; i++) {
                        processReadyImages[i] = Bitmap.createScaledBitmap(images[i], model.getInputW(), model.getInputH(), true);
                    }
                    break;
                case 3:
                    for (int i = 0; i < images.length; i++) {
                        Bitmap scaled = Bitmap.createScaledBitmap(images[i], model.getInputH(), model.getInputW(), true);
                        processReadyImages[i] = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), rotateMatrix, true);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("wrong size operation given");
            }

            Pair<float[], float[]> preprocImage = Utils.preprocess(processReadyImages,
                    model.getInputW(), model.getInputH(), model.getPreprocessInfo());
            inputs.get(inputNameRGB).write(preprocImage.first, 0, preprocImage.first.length);
            // inputs.get(inputName_Gray).write(preprocImage.second, 0, preprocImage.second.length);
            long inferenceStartTime = System.nanoTime();
            final Map<String, FloatTensor> outputs = network.execute(inputs);
            long inferenceEndTime = System.nanoTime();
            Log.i(TAG, String.format(Locale.US, "Inference time %d usec.", (inferenceEndTime - inferenceStartTime) / 1000));

            final float[] buff = new float[outputs.get(outputName).getSize()];
            outputs.get(outputName).read(buff, 0, buff.length);

            Bitmap[] result = Utils.postprocess(buff,
                    model.getInputW(), model.getInputH(), model.getPreprocessInfo());

            Bitmap[] revertedProcessedImages = new Bitmap[images.length];
            Matrix rotateBackMatrix = new Matrix();
            rotateBackMatrix.postRotate(-90);

            switch (sizeOperation) {
                case 0:
                    revertedProcessedImages = images;
                    break;
                case 1:
                    for (int i = 0; i < result.length; i++) {
                        revertedProcessedImages[i] = Bitmap.createBitmap(result[i], 0, 0, result[i].getWidth(), result[i].getHeight(), rotateBackMatrix, true);
                    }
                    break;
                case 2:
                    for (int i = 0; i < result.length; i++) {
                        revertedProcessedImages[i] = Bitmap.createScaledBitmap(result[i], images[i].getWidth(), images[i].getHeight(), true);
                    }
                    break;
                case 3:
                    for (int i = 0; i < result.length; i++) {
                        Bitmap scaled = Bitmap.createScaledBitmap(result[i], images[i].getHeight(), images[i].getWidth(), true);
                        revertedProcessedImages[i] = Bitmap.createBitmap(scaled, 0, 0, scaled.getWidth(), scaled.getHeight(), rotateBackMatrix, true);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("wrong size operation given");
            }

            long endTime = System.nanoTime();
            Log.i(TAG, String.format(Locale.US, "Total time %d usec.", (endTime - startTime) / 1000));

            List<EnhancementResult> results = new ArrayList<>();
            results.add(new EnhancementResult(revertedProcessedImages));
            return new ProcessResult<>(results,
                    new String[0],
                    (inferenceEndTime - inferenceStartTime) / 1000000,
                    (endTime - startTime) / 1000000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getModelBatchSize() {
        return model.getBatchSize();
    }

    public boolean release() {
        if (network == null) {
            Log.e(TAG, "Not initialized.");
            return false;
        }
        network.release();
        network = null;
        model = null;
        if (inputs != null) {
            inputs.get(inputNameRGB).release();
            // inputs.get(inputName_Gray).release();
            inputs = null;
        }
        return true;
    }

    public static Set<String> getAllModels() {
        return AllModels.keySet();
    }

    public Context getContext() {
        return context;
    }
}
