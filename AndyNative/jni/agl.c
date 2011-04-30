
#include "agl.h"

#include "stdio.h"
#include "stdlib.h"
#include "math.h"

void  aglInitialize2D(GLint w, GLint h)
{

}

void  aglSetVirtualDimensions(GLint w, GLint h)
{

}

void  aglGetVirtualTransform(GLfloat *m)
{

}

void  aglSetVirtualTransform(GLfloat *m)
{

}

void  aglComputeVirtualTransform()
{

}

GLint aglLoadShader(const char* vertex, const char* fragment)
{
	return -1;
}

void  aglUseShader(GLint shader)
{

}

void  aglClearShader()
{

}

void  aglDeleteShader(GLint shader)
{

}

GLint aglCreateTexture(GLint w, GLint h)
{
	return -1;
}

GLint aglCreateEmptyTexture()
{
	return -1;
}

void  aglBindTexture(GLint tex)
{

}

void  aglUnbindTexture()
{

}

void  aglDeleteTexture(GLint tex)
{

}

void  aglTexturedQuad()
{

}

void  aglDrawBitmap(GLint tex)
{

}

void  aglDrawBitmapTranslated(GLint tex, GLfloat x, GLfloat y)
{

}

void  aglDrawBitmapTransformed(GLint tex, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale)
{

}

void  aglDrawBitmapWithShader(GLint tex, GLint shader)
{

}

void  aglDrawBitmapWithShaderTranslated(GLint tex, GLint shader, GLfloat x, GLfloat y)
{

}

void  aglDrawBitmapWithShaderTransformed(GLint tex, GLint shader, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale)
{

}

void  aglDrawBitmapWithoutShader(GLint tex)
{

}

void  aglDrawBitmapWithoutShaderTranslated(GLint tex, GLfloat x, GLfloat y)
{

}

void  aglDrawBitmapWithoutShaderTransformed(GLint tex, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale)
{

}

void  aglClearColor(GLfloat r, GLfloat g, GLfloat b)
{

}

void  aglBeginFrame()
{

}

void  aglEndFrame()
{

}

GLint aglCreateFBO(GLint w, GLint h)
{
	return -1;
}

void  aglAttachToFBO(GLint fbo, GLint tex)
{

}

void  aglDeleteFBO(GLint fbo)
{

}

void  aglBeginOffscreenRender(GLint fbo)
{

}

void  aglEndOffscreenRender()
{

}


void Java_dkilian_andy_jni_agl_Initialize2D(JNIEnv *env, jobject *thiz, jint w, jint h)
{
	aglInitialize2D(w, h);
}

void Java_dkilian_andy_jni_agl_SetVirtualDimensions(JNIEnv *env, jobject *thiz, jint w, jint h)
{
	aglSetVirtualDimensions(w, h);
}

void Java_dkilian_andy_jni_agl_GetVirtualTransform(JNIEnv *env, jobject *thiz, jfloatArray mat)
{
	float m[16];
	aglGetVirtualTransform(m);
	(*env)->SetFloatArrayRegion(env, mat, 0, 16, &(m[0]));
}

void Java_dkilian_andy_jni_agl_SetVirtualTransfrom(JNIEnv *env, jobject *thiz, jfloatArray mat)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	aglSetVirtualTransform(m);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_ComputeVirtualTransform(JNIEnv *env, jobject *thiz)
{
	aglComputeVirtualTransform();
}

jint Java_dkilian_andy_jni_agl_LoadShader(JNIEnv *env, jobject *thiz, jstring vertex, jstring fragment)
{
	const char* v, *f;

	v = (*env)->GetStringUTFChars(env, vertex, NULL);
	f = (*env)->GetStringUTFChars(env, fragment, NULL);

	jint ret = aglLoadShader(v, f);

	(*env)->ReleaseStringUTFChars(env, vertex, v);
	(*env)->ReleaseStringUTFChars(env, fragment, f);

	return ret;
}

void Java_dkilian_andy_jni_agl_UseShader(JNIEnv *env, jobject *thiz, jint shader)
{
	aglUseShader(shader);
}

void Java_dkilian_andy_jni_agl_ClearShader(JNIEnv *env, jobject *thiz)
{
	aglClearShader();
}

void Java_dkilian_andy_jni_agl_DeleteShader(JNIEnv *env, jobject *thiz, jint shader)
{
	aglDeleteShader(shader);
}

jint Java_dkilian_andy_jni_agl_CreateTexture(JNIEnv *env, jobject *thiz, jint w, jint h)
{
	return aglCreateTexture(w, h);
}

jint Java_dkilian_andy_jni_agl_CreateEmptyTexture(JNIEnv *env, jobject *thiz)
{
	return aglCreateEmptyTexture();
}

void Java_dkilian_andy_jni_agl_BindTexture(JNIEnv *env, jobject *thiz, jint tex)
{
	aglBindTexture(tex);
}

void Java_dkilian_andy_jni_agl_UnbindTexture(JNIEnv *env, jobject *thiz)
{
	aglUnbindTexture();
}

void Java_dkilian_andy_jni_agl_DeleteTexture(JNIEnv *env, jobject *thiz, jint tex)
{
	aglDeleteTexture(tex);
}

void Java_dkilian_andy_jni_agl_TexturedQuad(JNIEnv *env, jobject *thiz)
{
	aglTexturedQuad();
}

void Java_dkilian_andy_jni_agl_DrawBitmap(JNIEnv *env, jobject *thiz, jint tex)
{
	aglDrawBitmap(tex);
}

void Java_dkilian_andy_jni_agl_DrawBitmapTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y)
{
	aglDrawBitmapTranslated(tex, x, y);
}

void Java_dkilian_andy_jni_agl_DrawBitmapTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale)
{
	aglDrawBitmapTransformed(tex, x, y, rot, xscale, yscale);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShader(JNIEnv *env, jobject *thiz, jint tex, jint shader)
{
	aglDrawBitmapWithShader(tex, shader);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y)
{
	aglDrawBitmapWithShaderTranslated(tex, shader, x, y);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale)
{
	aglDrawBitmapWithShaderTransformed(tex, shader, x, y, rot, xscale, yscale);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShader(JNIEnv *env, jobject *thiz, jint tex)
{
	aglDrawBitmapWithoutShader(tex);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y)
{
	aglDrawBitmapWithoutShaderTranslated(tex, x, y);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale)
{
	aglDrawBitmapWithoutShaderTransformed(tex, x, y, rot, xscale, yscale);
}

void Java_dkilian_andy_jni_agl_ClearColor(JNIEnv *env, jobject *thiz, jfloat r, jfloat g, jfloat b)
{
	aglClearColor(r, g, b);
}

void Java_dkilian_andy_jni_agl_BeginFrame(JNIEnv *env, jobject *thiz)
{
	aglBeginFrame();
}

void Java_dkilian_andy_jni_agl_EndFrame(JNIEnv *env, jobject *thiz)
{
	aglEndFrame();
}

jint Java_dkilian_andy_jni_agl_CreateFBO(JNIEnv *env, jobject *thiz, jint w, jint h)
{
	return aglCreateFBO(w, h);
}

void Java_dkilian_andy_jni_agl_AttachToFBO(JNIEnv *env, jobject *thiz, jint fbo, jint tex)
{
	aglAttachToFBO(fbo, tex);
}

void Java_dkilian_andy_jni_agl_DeleteFBO(JNIEnv *env, jobject *thiz, jint fbo)
{
	aglDeleteFBO(fbo);
}

void Java_dkilian_andy_jni_agl_BeginOffscreenRender(JNIEnv *env, jobject *thiz, jint fbo)
{
	aglBeginOffscreenRender(fbo);
}

void Java_dkilian_andy_jni_agl_EndOffscreenRender(JNIEnv *env, jobject *thiz)
{
	aglEndOffscreenRender();
}
