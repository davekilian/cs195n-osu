/*
 * agl.h
 *
 * Function definitions for Andy's high-level native OpenGL ES wrapper functions
 */

#ifndef AGL_H_
#define AGL_H_

#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

void  aglInitialize2D(GLint w, GLint h);
void  aglSetVirtualDimensions(GLint w, GLint h);
void  aglGetVirtualTransform(GLfloat *m);
void  aglSetVirtualTransform(GLfloat *m);
void  aglComputeVirtualTransform();
GLint aglLoadShader(const char* vertex, const char* fragment);
void  aglUseShader(GLint shader);
void  aglClearShader();
void  aglDeleteShader(GLint shader);
GLint aglCreateTexture(GLint w, GLint h);
GLint aglCreateEmptyTexture();
void  aglBindTexture(GLint tex);
void  aglUnbindTexture();
void  aglDeleteTexture(GLint tex);
void  aglTexturedQuad();
void  aglDrawBitmap(GLint tex);
void  aglDrawBitmapTranslated(GLint tex, GLfloat x, GLfloat y);
void  aglDrawBitmapTransformed(GLint tex, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale);
void  aglDrawBitmapWithShader(GLint tex, GLint shader);
void  aglDrawBitmapWithShaderTranslated(GLint tex, GLint shader, GLfloat x, GLfloat y);
void  aglDrawBitmapWithShaderTransformed(GLint tex, GLint shader, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale);
void  aglDrawBitmapWithoutShader(GLint tex);
void  aglDrawBitmapWithoutShaderTranslated(GLint tex, GLfloat x, GLfloat y);
void  aglDrawBitmapWithoutShaderTransformed(GLint tex, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale);
void  aglClearColor(GLfloat r, GLfloat g, GLfloat b);
void  aglBeginFrame();
void  aglEndFrame();
GLint aglCreateFBO(GLint w, GLint h);
void  aglAttachToFBO(GLint fbo, GLint tex);
void  aglDeleteFBO(GLint fbo);
void  aglBeginOffscreenRender(GLint fbo);
void  aglEndOffscreenRender();
// TODO: particle system acceleration. Efficient data marshaling could be tricky

void Java_dkilian_andy_jni_agl_Initialize2D(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_SetVirtualDimensions(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_GetVirtualTransform(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_SetVirtualTransfrom(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_ComputeVirtualTransform(JNIEnv *env, jobject *thiz);
jint Java_dkilian_andy_jni_agl_LoadShader(JNIEnv *env, jobject *thiz, jstring vertex, jstring fragment);
void Java_dkilian_andy_jni_agl_UseShader(JNIEnv *env, jobject *thiz ,jint shader);
void Java_dkilian_andy_jni_agl_ClearShader(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_DeleteShader(JNIEnv *env, jobject *thiz, jint shader);
jint Java_dkilian_andy_jni_agl_CreateTexture(JNIEnv *env, jobject *thiz, jint w, jint h);
jint Java_dkilian_andy_jni_agl_CreateEmptyTexture(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_BindTexture(JNIEnv *env, jobject *thiz, jint tex);
void Java_dkilian_andy_jni_agl_UnbindTexture(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_DeleteTexture(JNIEnv *env, jobject *thiz, jint tex);
void Java_dkilian_andy_jni_agl_TexturedQuad(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_DrawBitmap(JNIEnv *env, jobject *thiz, jint tex);
void Java_dkilian_andy_jni_agl_DrawBitmapTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y);
void Java_dkilian_andy_jni_agl_DrawBitmapTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShader(JNIEnv *env, jobject *thiz, jint tex, jint shader);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShader(JNIEnv *env, jobject *thiz, jint tex);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale);
void Java_dkilian_andy_jni_agl_ClearColor(JNIEnv *env, jobject *thiz, jfloat r, jfloat g, jfloat b);
void Java_dkilian_andy_jni_agl_BeginFrame(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_EndFrame(JNIEnv *env, jobject *thiz);
jint Java_dkilian_andy_jni_agl_CreateFBO(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_AttachToFBO(JNIEnv *env, jobject *thiz, jint fbo, jint tex);
void Java_dkilian_andy_jni_agl_DeleteFBO(JNIEnv *env, jobject *thiz, jint fbo);
void Java_dkilian_andy_jni_agl_BeginOffscreenRender(JNIEnv *env, jobject *thiz, jint fbo);
void Java_dkilian_andy_jni_agl_EndOffscreenRender(JNIEnv *env, jobject *thiz);

#endif /* AGL_H_ */
