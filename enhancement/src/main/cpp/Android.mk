LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := enhancement_utils

#LOCAL_CFLAGS += -D__ndk1=__1 
LOCAL_CPPFLAGS += -fexceptions
LOCAL_SRC_FILES :=  \
	 utils.cpp \
	 aes.cpp \

LOCAL_C_INCLUDES := \
    $(LOCAL_PATH)/include \
	$(LOCAL_PATH) \

LOCAL_LDLIBS += \
	-llog \
    -landroid \
	-ljnigraphics \
		
LOCAL_LDFLAGS := \
-Wl

ifeq ($(VERBOSE_BUILD),true)
LOCAL_LDFLAGS += -v
endif
CPPFLAGS=-stdlib=libstdc++ LDLIBS=-lstdc++
LOCAL_CFLAGS += -std=c++11
	
include $(BUILD_SHARED_LIBRARY)
#include $(BUILD_EXECUTABLE) 

