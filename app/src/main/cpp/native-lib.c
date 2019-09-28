#include <jni.h>
#include <string.h>
#include <android/bitmap.h>

#include "config.h"
#include "avilib1_1_5/avilib.h"

JNIEXPORT jlong JNICALL
Java_com_czf_aviplayer_NativeLibInterface_openFile(JNIEnv *env, jclass clazz, jstring jfilePath) {
  const char *filePath = (*env)->GetStringUTFChars(env, jfilePath, NULL);
  if (!filePath) {
    return -1;
  }
  log("--==--: %s\n", filePath);
  avi_t *avi = 0;
  avi = AVI_open_input_file(filePath, 1);
  (*env)->ReleaseStringUTFChars(env, jfilePath, filePath);
  if (!avi) {
    log("--==--: %s\n", AVI_strerror());
    return -1;
  }

  return (jlong)avi;
}

JNIEXPORT jint JNICALL
Java_com_czf_aviplayer_NativeLibInterface_frameWidth(JNIEnv *env, jclass clazz, jlong fileFd) {
  return AVI_video_width((avi_t *)fileFd);
}

JNIEXPORT jint JNICALL
Java_com_czf_aviplayer_NativeLibInterface_frameHeight(JNIEnv *env, jclass clazz, jlong fileFd) {
  return AVI_video_height((avi_t *)fileFd);
}

JNIEXPORT jdouble JNICALL
Java_com_czf_aviplayer_NativeLibInterface_frameRate(JNIEnv *env, jclass clazz, jlong fileFd) {
  return AVI_frame_rate((avi_t *)fileFd);
}

JNIEXPORT jlong JNICALL
Java_com_czf_aviplayer_NativeLibInterface_closeFile(JNIEnv *env, jclass clazz, jlong fileFd) {
  AVI_close((avi_t *)fileFd);
}

JNIEXPORT jint JNICALL
Java_com_czf_aviplayer_NativeLibInterface_setFrame(JNIEnv *env, jclass clazz, jlong avi, jobject jbitmap) {
  char *buf = NULL;
  if (AndroidBitmap_lockPixels(env, jbitmap, (void **)&buf) < 0) {
    return -1;
  }

  int keyFrame = 0;
  long frameSize = 0;
  frameSize = AVI_read_frame((avi_t *)avi, buf, &keyFrame);
  if (frameSize < 0) {
    AndroidBitmap_unlockPixels(env, jbitmap);
    log("--==--: %s\n", AVI_strerror());
    return -1;
  }
  log("--==--: frameSize: %ld\n", frameSize);
  log("--==--: keyFrame: %d\n", keyFrame);

  if (AndroidBitmap_unlockPixels(env, jbitmap) < 0) {
    return -1;
  }

  return 0;
}
