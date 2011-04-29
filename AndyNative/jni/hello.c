
#include <jni.h>

jstring Java_dkilian_andy_jni_Hello_hello(JNIEnv *env, jobject *thiz)
{
    return (*env)->NewStringUTF(env, "Move off every zig for great justice");
}
