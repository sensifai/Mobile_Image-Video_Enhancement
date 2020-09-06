# Sensifai Mobile Image & Video Enhancement

This repository contains an Android SDK for Sensifai Image & Video Enhancement. This SDK brings the ability to any Android mobile application which needs to enhance the quality of images or videos. The AI engine of this SDK enhances the color range and resolution of input image or video in real-time.

## Build
Clone this repository and import into **Android Studio**
From the gradle menu choose assembleRelease/assembleDebug Option to build the AAR file (in /enhancement/build/outputs/aar)

## Usage
Create a new Android Application Project and import the AAR file as a dependency
add the following dependencies to `build.gradle` file:
***TFLite***
```gradle
    implementation 'org.tensorflow:tensorflow-lite:0.0.0-nightly'
    implementation 'org.tensorflow:tensorflow-lite-gpu:0.0.0-nightly'
    implementation 'org.tensorflow:tensorflow-lite-support:0.0.0-nightly'
    implementation 'org.tensorflow:tensorflow-lite-hexagon:0.0.0-nightly'
```
***SNPE***
(import `snpe-release.aar` file as module dependency into the project)
```gradle
    implementation project(':snpe-release')
```
