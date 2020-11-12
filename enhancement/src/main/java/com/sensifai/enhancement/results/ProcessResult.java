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
