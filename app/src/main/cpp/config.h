#define DEBUG

#ifdef DEBUG
  #include <android/log.h>
  #define log(...) __android_log_print(ANDROID_LOG_VERBOSE, "native-lib", __VA_ARGS__)
#else
  #define log(...)
#endif
