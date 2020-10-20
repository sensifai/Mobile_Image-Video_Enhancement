package com.sensifai.enhancement.TFLite;

class ModelInfo<E> {
    private final String modelFileName;
    private final String labelFileName;
    private final ModelType modelType;
    private final PreProcess preprocessor;
    private final PostProcess<E> postprocessor;

    /**
     * constructor of class
     * @param modelFileName name of model
     * @param labelFileName name labels file
     * @param modelType type of chipset
     * @param preprocessor preProcess object
     * @param postprocessor postProcess object
     */
    ModelInfo(String modelFileName, String labelFileName, ModelType modelType,
              PreProcess preprocessor, PostProcess<E> postprocessor) {
        this.modelFileName = modelFileName;
        this.labelFileName = labelFileName;
        this.modelType = modelType;
        this.preprocessor = preprocessor;
        this.postprocessor = postprocessor;
    }

    String getModelFileName() {
        return modelFileName;
    }

    String getLabelFileName() {
        return labelFileName;
    }

    ModelType getModelType() {
        return modelType;
    }

    PreProcess getPreProcessor() {
        return preprocessor;
    }

    PostProcess<E> getPostProcessor() {
        return postprocessor;
    }

    /**
     * type of chipset
     */
    public enum ModelType {
        TFLite,
        SNPE
    }
}
