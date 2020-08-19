package com.sensifai.enhancement.TFLite;

class ModelInfo<E> {
    private final String modelFileName;
    private final String labelFileName;
    private final ModelType modelType;
    private final PreProcess preprocessor;
    private final PostProcess<E> postprocessor;

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

    public enum ModelType {
        TFLite,
        SNPE
    }
}
