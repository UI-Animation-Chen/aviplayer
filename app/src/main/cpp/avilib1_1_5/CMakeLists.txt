# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

add_library(avi-lib SHARED avilib.c platform_posix.c)
