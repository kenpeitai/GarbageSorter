// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("garbagesorter");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("garbagesorter")
//      }
//    }

// NativeLib.cpp
#include <jni.h>
#include <dlfcn.h>

#include "com_cqupt_garbagesorter_jni_Jnilib.h"

extern "C"
JNIEXPORT void JNICALL


Java_com_cqupt_garbagesorter_jni_Jnilib_say_1hello(JNIEnv *env, jclass clazz, jstring message) {
    // 调用外部方法say_hello(),lib等文件在jni目录下


}