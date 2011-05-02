/*
 * agl.h
 *
 * Function definitions for Andy's high-level native OpenGL ES wrapper functions.
 * Let's pronounce it 'agile' :D
 */

#ifndef AGL_H_
#define AGL_H_

#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

void  aglInitialize2D(GLint w, GLint h);
void  aglCleanup2D();
void  aglSetVirtualDimensions(GLint w, GLint h);
void  aglGetVirtualTransform(GLfloat *m);
void  aglSetVirtualTransform(GLfloat *m);
void  aglComputeVirtualTransform();
GLint aglLoadShader(const char* vertex, const char* fragment);
void  aglUniform(GLint shader, const char* param, GLfloat v);
void  aglUniform2(GLint shader, const char* param, GLfloat v1, GLfloat v2);
void  aglUniform3(GLint shader, const char* param, GLfloat v1, GLfloat v2, GLfloat v3);
void  aglUniform4(GLint shader, const char* param, GLfloat v1, GLfloat v2, GLfloat v3, GLfloat v4);
void  aglUniformMat2(GLint shader, const char* param, GLfloat *m);
void  aglUniformMat3(GLint shader, const char* param, GLfloat *m);
void  aglUniformMat4(GLint shader, const char* param, GLfloat *m);
void  aglUniformTexture(GLint shader, const char* param, GLint t);
void  aglUseShader(GLint shader);
void  aglUseQuadShader();
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
void  aglDrawBitmapMatrix(GLint tex, GLfloat *m);
void  aglDrawBitmapWithShader(GLint tex, GLint shader);
void  aglDrawBitmapWithShaderTranslated(GLint tex, GLint shader, GLfloat x, GLfloat y);
void  aglDrawBitmapWithShaderTransformed(GLint tex, GLint shader, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale);
void  aglDrawBitmapWithShaderMatrix(GLint tex, GLint shader, GLfloat *m);
void  aglDrawBitmapWithoutShader(GLint tex);
void  aglDrawBitmapWithoutShaderTranslated(GLint tex, GLfloat x, GLfloat y);
void  aglDrawBitmapWithoutShaderTransformed(GLint tex, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale);
void  aglDrawBitmapWithoutShaderMatrix(GLint tex, GLfloat *m);
void  aglClearColor(GLfloat r, GLfloat g, GLfloat b);
void  aglBeginFrame();
void  aglEndFrame();
GLint aglCreateFBO(GLint w, GLint h);
void  aglAttachToFBO(GLint fbo, GLint tex);
void  aglDeleteFBO(GLint fbo);
void  aglBeginOffscreenRender(GLint fbo);
void  aglEndOffscreenRender();
void  aglLoadIdentity();
void  aglLoadMatrix(GLfloat *m);
void  aglGetMatrix(GLfloat *m);
void  aglPushMatrix();
void  aglPopMatrix();
void  aglTranslatef(GLfloat tx, GLfloat ty, GLfloat tz);
void  aglRotatef(GLfloat angle);
void  aglAxisAngle(GLfloat x, GLfloat y, GLfloat z, GLfloat angle);
void  aglScalef(GLfloat sx, GLfloat sy, GLfloat sz);
// TODO: particle system acceleration. Efficient data marshaling could be tricky.
/*
 * One possible (but friggin impossible to program)
 * - Create a gigantic float array
 * - Put each particle's parameters in the array
 * 	 - So the particle stores its position, velocity etc
 * 	 - A stride tells once how many floats to jump over to get to the next item
 * - Send the entire friggin array to OpenGL and tell it to render using the same
 *   usage, with some stride to skip over irrelevant data
 * Unpacking/freeing the array every call sucks. But it might suck less than making lots of
 * JNI calls. In the end, the only way to know is to implement both and choose the one that
 * performs better under stress.
 */

void Java_dkilian_andy_jni_agl_Initialize2D(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_Cleanup2D(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_SetVirtualDimensions(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_GetVirtualTransform(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_SetVirtualTransfrom(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_ComputeVirtualTransform(JNIEnv *env, jobject *thiz);
jint Java_dkilian_andy_jni_agl_LoadShader(JNIEnv *env, jobject *thiz, jstring vertex, jstring fragment);
void Java_dkilian_andy_jni_agl_Uniform(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v);
void Java_dkilian_andy_jni_agl_Uniform2(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2);
void Java_dkilian_andy_jni_agl_Uniform3(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2, jfloat v3);
void Java_dkilian_andy_jni_agl_Uniform4(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2, jfloat v3, jfloat v4);
void Java_dkilian_andy_jni_agl_UniformMat2(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray m);
void Java_dkilian_andy_jni_agl_UniformMat3(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray m);
void Java_dkilian_andy_jni_agl_UniformMat4(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray m);
void Java_dkilian_andy_jni_agl_UniformTexture(JNIEnv *env, jobject *thiz, jint shader, jstring param, jint t);
void Java_dkilian_andy_jni_agl_UseShader(JNIEnv *env, jobject *thiz, jint shader);
void Java_dkilian_andy_jni_agl_UseQuadShader(JNIEnv *env, jobject *thiz);
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
void Java_dkilian_andy_jni_agl_DrawBitmapMatrix(JNIEnv *env, jobject *thiz, jint tex, jfloatArray mat);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShader(JNIEnv *env, jobject *thiz, jint tex, jint shader);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale);
void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderMatrix(JNIEnv *env, jobject *thiz, jint tex, jint shader, jfloatArray mat);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShader(JNIEnv *env, jobject *thiz, jint tex);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale);
void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderMatrix(JNIEnv *env, jobject *thiz, jint tex, jfloatArray mat);
void Java_dkilian_andy_jni_agl_ClearColor(JNIEnv *env, jobject *thiz, jfloat r, jfloat g, jfloat b);
void Java_dkilian_andy_jni_agl_BeginFrame(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_EndFrame(JNIEnv *env, jobject *thiz);
jint Java_dkilian_andy_jni_agl_CreateFBO(JNIEnv *env, jobject *thiz, jint w, jint h);
void Java_dkilian_andy_jni_agl_AttachToFBO(JNIEnv *env, jobject *thiz, jint fbo, jint tex);
void Java_dkilian_andy_jni_agl_DeleteFBO(JNIEnv *env, jobject *thiz, jint fbo);
void Java_dkilian_andy_jni_agl_BeginOffscreenRender(JNIEnv *env, jobject *thiz, jint fbo);
void Java_dkilian_andy_jni_agl_EndOffscreenRender(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_LoadIdentity(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_LoadMatrix(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_GetMatrix(JNIEnv *env, jobject *thiz, jfloatArray m);
void Java_dkilian_andy_jni_agl_PushMatrix(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_PopMatrix(JNIEnv *env, jobject *thiz);
void Java_dkilian_andy_jni_agl_Translatef(JNIEnv *env, jobject *thiz, jfloat tx, jfloat ty, jfloat tz);
void Java_dkilian_andy_jni_agl_Rotatef(JNIEnv *env, jobject *thiz, jfloat angle);
void Java_dkilian_andy_jni_agl_AxisAngle(JNIEnv *env, jobject *thiz, jfloat x, jfloat y, jfloat z, jfloat angle);
void Java_dkilian_andy_jni_agl_Scalef(JNIEnv *env, jobject *thiz, jfloat sx, jfloat sy, jfloat sz);

#endif /* AGL_H_ */
