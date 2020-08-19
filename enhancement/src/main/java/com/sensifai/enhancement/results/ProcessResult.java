package com.sensifai.enhancement.results;

import java.util.List;

public class ProcessResult<E> {

    private final List<E> results;
    private final String[] classes;
    private final long inferenceTime;
    private final long totalTime;

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
