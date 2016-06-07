LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ndk
LOCAL_SRC_FILES := ndk.c
NDK_APP_DST_DIR := ../jniLibs/$(TARGET_ARCH_ABI)

include $(BUILD_SHARED_LIBRARY)