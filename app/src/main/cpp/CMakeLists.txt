# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

find_library(log-lib log)

find_library(jnigraphics-lib jnigraphics)

ADD_SUBDIRECTORY(avilib1_1_5)

add_library(native-lib SHARED native-lib-jni.c)

# 本文件中需要${name}来引用，其他module直接用库名字。
target_link_libraries(native-lib ${log-lib} ${jnigraphics-lib} avi-lib)
