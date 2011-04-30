LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := AndyNative
LOCAL_SRC_FILES := hello.c, hellogl.c
LOCAL_CFLAGS    := -Wall
LOCAL_LDLIBS    := -llog -lGLESv2

include $(BUILD_SHARED_LIBRARY)
