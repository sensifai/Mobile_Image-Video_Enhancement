package com.sensifai.enhancement.results;

import java.util.List;

public class ProcessResult<E> {
    /**
     * the result of image process
     */
    private final List<E> results;
    /**
     * list of tags label
     */
    private final String[] classes;
    /**
     * inference time of processing
     */
    private final long inferenceTime;
    /**
     * total time of processing
     */
    private final long totalTime;

    /**
     * class constructor
     * @param results result of process
     * @param classes list of tags label
     * @param inferenceTime inference time of processing
     * @param totalTime total time of processing
     */
    public ProcessResult(List<E> results, String[] classes, long inferenceTime, long totalTime) {

        this.results = results;
        this.classes = classes;
        this.inferenceTime = inferenceTime;
        this.totalTime = totalTime;
    }

    public List<E> getResults() {
        return results;
    }

    public String[] getClasses() {
        return classes;
    }

    public long getInferenceTime() {
        return inferenceTime;
    }

    public long getTotalTime() {
        return totalTime;
    }
}
