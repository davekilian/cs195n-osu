
#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void Java_dkilian_andy_jni_Hello_helloGL(JNIEnv *env, jobject *thiz)
{
	float r, g, b, a;

	r = (float)rand() / (float)RAND_MAX;
	g = (float)rand() / (float)RAND_MAX;
	b = (float)rand() / (float)RAND_MAX;
	a = (float)rand() / (float)RAND_MAX;

	glClearColor(r, g, b, a);
	glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
}
