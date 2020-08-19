#include <jni.h>
#include <stdlib.h>
#include <cstring>
#include <math.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>
#include "include/aes.h"

#define LOG_TAG "JNI_MSG"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


#define KEY_SIZE 128
static const unsigned char K[16] = {0x74, 0x68, 0x3c, 0x73, 0x27, 0x69, 0x74, 0x20,
                                    0x74, 0x68, 0x65, 0x42, 0x6b, 0x65, 0x79, 0x2e};
static unsigned char I[16] = {0x74, 0x68, 0x69, 0x55, 0xa0, 0x69, 0x74, 0x20,
                              0x74, 0xc8, 0x65, 0x10, 0x6b, 0x65, 0x79, 0x2e};
const size_t START = 2018;
const size_t END = 2018 + AES_BLOCK_SIZE * 2019;

bool decrypt(void *in) {
    BYTE *input = static_cast<BYTE *>(in);
    size_t len = END - START;
    BYTE *buff = (BYTE *) malloc(len);
    if (!buff) {
        return false;
    }
    memcpy(buff, input + START, len);

    //set key & iv
    unsigned int key_schedule[AES_BLOCK_SIZE * 4] = {0};
    aes_key_setup(K, key_schedule, KEY_SIZE);
    aes_decrypt_cbc(buff, len, buff, key_schedule, KEY_SIZE, I);

    memcpy(input + START, buff, len);
    free(buff);

    return true;
}

// ---------------------------------------------------------------------------------------------

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_sensifai_snpe_enhancement_Utils_decrypt(JNIEnv *env, jclass type,
                                                 jstring fileName_, jobject assetManager) {

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
    const char *fileName = env->GetStringUTFChars(fileName_, 0);
    AAsset *asset = AAssetManager_open(mgr, fileName, AASSET_MODE_BUFFER);
    if (nullptr == asset) {
        LOGE("AAsset is null...");
        return NULL;
    }
    const void *data = AAsset_getBuffer(asset);
    if (nullptr == data) {
        LOGE("Read buffer is null...");
        return NULL;
    }
    off_t len = AAsset_getLength(asset);

    decrypt((void *) data);

    env->ReleaseStringUTFChars(fileName_, fileName);

    jbyteArray jData = env->NewByteArray(len);
    env->SetByteArrayRegion(jData, 0, len, (jbyte *) data);

    return jData;
}