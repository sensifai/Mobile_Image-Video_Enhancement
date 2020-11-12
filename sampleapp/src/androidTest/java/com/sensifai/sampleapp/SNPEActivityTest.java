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

package com.sensifai.sampleapp;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SNPEActivityTest {

    @Rule
    public ActivityTestRule<SNPEActivity> activityActivityTestRule = new ActivityTestRule<>(SNPEActivity.class);

    @Test
    public void loadJNIsoTest() {
        assertTrue(activityActivityTestRule.getActivity().loadJNIso());
    }

    @Test
    public void initModelTest(){
        assertTrue(activityActivityTestRule.getActivity().initModel());
    }

    @Test
    public void processTest() {
        initModelTest();
        AssetManager assetManager = activityActivityTestRule.getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("a0481-480p.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        assertNotNull(activityActivityTestRule.getActivity().process(new Bitmap[] {bitmap}, 0));
    }

    @Test
    public void processGrowTest() {
        initModelTest();
        AssetManager assetManager = activityActivityTestRule.getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("a0481-240p.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        assertNotNull(activityActivityTestRule.getActivity().process(new Bitmap[] {bitmap}, 2));
    }

    @Test
    public void processFailureTest(){
        initModelTest();
        AssetManager assetManager = activityActivityTestRule.getActivity().getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("test.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        assertNull(activityActivityTestRule.getActivity().process(new Bitmap[] {bitmap}, 0));
    }
}