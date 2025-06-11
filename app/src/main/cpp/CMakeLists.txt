cmake_minimum_required(VERSION 3.22.1)
project("myapplication")

# Path to OpenCV config (adjust if your path is different)
set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/../../../../sdk/OpenCV-android-sdk/sdk/native/jni)

# Find OpenCV
find_package(OpenCV REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS})

# Create native library from myapplication.cpp
add_library(
        myapplication
        SHARED
        myapplication.cpp
)

# Link OpenCV and Android log lib
target_link_libraries(
        myapplication
        ${OpenCV_LIBS}
        log
)
