#include "com_example_beyongha_softwareexperimentteam15_NativeCall.h"

JNIEXPORT jstring JNICALL Java_com_example_beyongha_softwareexperimentteam15_NativeCall_string(JNIEnv *env, jobject obj) {

    return (*env)->NewStringUTF(env, "jni test");

}