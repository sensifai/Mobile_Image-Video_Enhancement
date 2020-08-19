package com.sensifai.enhancement.SNPE;

public class ModelInfo {

    private final String modelName;
    private final boolean encrypted;
    private final int input_H;
    private final int input_W;
    private final PreprocessInfo preprocessInfo;
    private int output_H;
    private int output_W;
    private int batchSize;

    ModelInfo(String modelName, boolean encrypted,
              int input_H, int input_W, int output_H, int output_W, int batchSize,
              PreprocessInfo preprocessInfo) {
        this.modelName = modelName;
        this.encrypted = encrypted;
        this.input_H = input_H;
        this.input_W = input_W;
        this.output_H = output_H;
        this.output_W = output_W;
        this.batchSize = batchSize;
        this.preprocessInfo = preprocessInfo;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public int getInput_H() {
        return input_H;
    }

    public int getInput_W() {
        return input_W;
    }

    public int getOutput_H() {
        return output_H;
    }

    public int getOutput_W() {
        return output_W;
    }

    public PreprocessInfo getPreprocessInfo() {
        return preprocessInfo;
    }

    public int getBatchSize() {
        return batchSize;
    }
}
