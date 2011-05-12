
#include "agl.h"
#include "matrix.h"
#include "matstack.h"

#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <android/log.h>

#include "stdio.h"
#include "stdlib.h"
#include "math.h"

typedef struct _ShaderAttachment
{
	GLint shader;
	GLint program;
	struct _ShaderAttachment *next;
} ShaderAttachment;

typedef struct _FBODepthAttachment
{
	GLint depth;
	GLint fbo;
	struct _FBODepthAttachment *next;
} FBODepthAttachment;

typedef struct _FBOColorAttachment
{
	GLint color;
	GLint fbo;
	struct _FBOColorAttachment *next;
} FBOColorAttachment;

const GLfloat _agl_quad_vdata[] =
{
		0.f, 0.f,
		1.f, 0.f,
		0.f, 1.f,
		1.f, 1.f
};

const GLushort _agl_quad_edata[] =
{
		0, 1, 2, 3
};

const char _agl_quad_vshader[] =
		"attribute lowp vec2 aglPosition;"
		""
		"uniform mat4 aglModelview;"
		"uniform mat4 aglVirtualTransform;"
		""
		"varying lowp vec2 texcoord;"
		""
		"void main()"
		"{"
		"	gl_Position = aglVirtualTransform * aglModelview * vec4(aglPosition, 0.0, 1.0);"
		"	texcoord = aglPosition;"
		"}";

const char _agl_quad_fshader[] =
		"uniform sampler2D aglTexture;"
		"uniform lowp float aglAlpha;"
		""
		"varying lowp vec2 texcoord;"
		""
		"void main()"
		"{"
		"	gl_FragColor = texture2D(aglTexture, texcoord);"
		"   gl_FragColor *= aglAlpha;"
		"}";

GLint _agl_virtualWidth = 0;			// The width of the viewport in virtual coordinates
GLint _agl_virtualHeight = 0;			// The height of the viewport in virtual coordinates
Matrix _agl_virtualTransform;			// The transform from virtual coordinates to world coordinates
ShaderAttachment *_agl_shaders = 0;		// Pairs each shader program with its attached vertex/fragment shaders, for cleanup purposes
MatrixStack *_agl_modelview = 0;		// Emulates OpenGL 1.0's ModelView matrix stack
FBOColorAttachment *_agl_fbo_color = 0;	// Pairs each FBO with its attached depth buffer, for cleanup purposes
FBODepthAttachment *_agl_fbo_depth = 0;	// Pairs each FBO with its attached color buffer(s), for cleanup purposes
GLfloat _agl_clear_r = 0;				// The red component of the current clear color
GLfloat _agl_clear_g = 0;				// The green component of the current clear color
GLfloat _agl_clear_b = 0;				// The blue component of the current clear color
GLfloat _agl_clear_a = 0;				// The alpha component of the current clear color
GLuint _agl_quad_verts = 0;				// Vertex buffer containing the textured quad's data
GLuint _agl_quad_elems = 0;				// Element buffer contain the textured quad's indices
GLint _agl_quad_program = 0;			// The program used to draw textured quads
GLint _agl_bound_shader = 0;			// The currently bound shader. Used to set aglPosition in aglTexturedQuad() if applicable.
GLfloat _agl_alpha = 1.f;				// The current alpha channel value, between 0 and 1

// Maybe there are preprocessor hacks to make this prettier
#define LOG_ENABLED 0

#if LOG_ENABLED
#define logcat(a) __android_log_print(ANDROID_LOG_VERBOSE, "aglVerbose", a);
#define logfmt(a, b) __android_log_print(ANDROID_LOG_VERBOSE, "aglVerbose", a, b);
#define logfmt2(a, b, c) __android_log_print(ANDROID_LOG_VERBOSE, "aglVerbose", a, b, c);
#define logfmt3(a, b, c, d) __android_log_print(ANDROID_LOG_VERBOSE, "aglVerbose", a, b, c, d);
#else
#define logcat(a)
#define logfmt(a, b)
#define logfmt2(a, b, c)
#define logfmt3(a, b, c, d)
#endif

void  aglInitialize2D(GLint w, GLint h)
{
	glEnable(GL_BLEND);
	glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);	// Supposedly GLUtils.glTexImage2D() produces premultiplied alpha results

	_agl_modelview = matrix_stack_create();

	aglSetVirtualDimensions(w, h);
	aglComputeVirtualTransform();

	glGenBuffers(1, &_agl_quad_verts);
	glBindBuffer(GL_ARRAY_BUFFER, _agl_quad_verts);
	glBufferData(GL_ARRAY_BUFFER, sizeof(_agl_quad_vdata), _agl_quad_vdata, GL_STATIC_DRAW);
	glBindBuffer(GL_ARRAY_BUFFER, 0);

	glGenBuffers(1, &_agl_quad_elems);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _agl_quad_elems);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(_agl_quad_edata), _agl_quad_edata, GL_STATIC_DRAW);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

	_agl_quad_program = aglLoadShader(_agl_quad_vshader, _agl_quad_fshader);

	logfmt2("Virtual dimensions: %dx%d", w, h);
	logfmt("Loaded quad shader handle: %d", _agl_quad_program);
	logfmt("Modelview matrix stack initialized with depth: %d", matrix_stack_depth(_agl_modelview));
	logcat("agl init complete");
}

void  aglCleanup2D()
{
	GLuint uint = 0;
	ShaderAttachment *sa = 0;
	FBOColorAttachment *ca = 0;
	FBODepthAttachment *da = 0;

	matrix_stack_destroy(_agl_modelview);
	_agl_modelview = 0;

	glDeleteBuffers(1, &_agl_quad_verts);
	glDeleteBuffers(1, &_agl_quad_elems);

	aglDeleteShader(_agl_quad_program);
	_agl_quad_program = 0;

	sa = _agl_shaders;
	while (sa)
	{
		glDetachShader(sa->program, sa->shader);
		glDeleteShader(sa->shader);
		logfmt2("Deleted shader %d attached to program %d", sa->shader, sa->program);
		sa = sa->next;
	}

	sa = _agl_shaders;
	while (sa)
	{
		ShaderAttachment *tmp = sa->next;
		glDeleteProgram(sa->program);
		logfmt("Deleted program %d", sa->program);
		free(sa);
		sa = tmp;
	}

	ca = _agl_fbo_color;
	while (ca)
	{
		FBOColorAttachment *tmp = ca->next;
		uint = ca->color;
		glDeleteTextures(1, &uint);
		uint = ca->fbo;
		glDeleteFramebuffers(1, &uint);
		logfmt2("Deleted color attachment %d attached to FBO %d", ca->color, ca->fbo);
		free(ca);
		ca = tmp;
	}

	da = _agl_fbo_depth;
	while (da)
	{
		FBODepthAttachment *tmp = da->next;
		uint = da->depth;
		glDeleteRenderbuffers(1, &uint);
		uint = da->fbo;
		glDeleteFramebuffers(1, &uint);
		logfmt2("Deleted depth attachment %d attached to FBO %d", da->depth, da->fbo);
		free(da);
		da = tmp;
	}
}

void  aglSetVirtualDimensions(GLint w, GLint h)
{
	_agl_virtualWidth = w;
	_agl_virtualHeight = h;
	logfmt2("Virtual dimensions set to %dx%d, aglComputeTransform() to apply.", w, h);
}

void  aglGetVirtualTransform(GLfloat *m)
{
	memcpy(m, &_agl_virtualTransform.data, 16 * sizeof(float));
}

void  aglSetVirtualTransform(GLfloat *m)
{
	memcpy(&_agl_virtualTransform.data, m, 16 * sizeof(float));
}

void  aglComputeVirtualTransform()
{
	matrix_identity(&_agl_virtualTransform);
	float scalex, scaley;
	GLint v[4];
	GLint vw, vh, pw, ph;

	logcat("Computing virtual transforms ...");

	glGetIntegerv(GL_VIEWPORT, v);

	vw = _agl_virtualWidth;
	vh = _agl_virtualHeight;

	pw = v[2];
	ph = v[3];

	scalex = (float)pw / (float)vw;
	scaley = (float)ph / (float)vh;

	logfmt2("Physical dimensions: %dx%d", pw, ph);
	logfmt2("Virtual dimensions: %dx%d", vw, vh);

	// Read bottom-up to get a chronological view of transformations

	// Flip
	matrix_scale(&_agl_virtualTransform, 1.f, -1.f, 1.f);

	// Normalized device coordinates to OpenGL space
	matrix_translate(&_agl_virtualTransform, -1.f, -1.f, 0.f);
	matrix_scale(&_agl_virtualTransform, 2.f, 2.f, 1.f);

	// Device coordinates to normalized device coordinates
	matrix_scale(&_agl_virtualTransform, 1.f / pw, 1.f / ph, 1.f);

	if (scalex < scaley)
	{
		float delta = .5f * (ph - scalex * vh);

		logcat("Top/bottom letterboxes");
		logfmt("Scaling entire scene by a factor of %f", scalex);
		logfmt("Translating by %f", .5f * delta);

		// Virtual coordinates to device coordinates
		matrix_translate(&_agl_virtualTransform, 0.f, delta, 0.f);
		matrix_scale(&_agl_virtualTransform, scalex, scalex, 1.f);
	}
	else
	{
		float delta = .5f * (pw - scaley * vw);

		logcat("Left/right letterboxes");
		logfmt("Scaling entire scene by a factor of %f", scaley);
		logfmt("Translating by %f", delta);

		// Virtual coordinates to device coordinates
		matrix_translate(&_agl_virtualTransform, delta, 0.f, 0.f);
		matrix_scale(&_agl_virtualTransform, scaley, scaley, 1.f);
	}
}

void aglBlendNone()
{
	glDisable(GL_BLEND);
}

void aglBlendAdditive()
{
	glEnable(GL_BLEND);
	glBlendFunc(GL_ONE, GL_ONE);
}

void aglBlendAlpha()
{
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
}

void aglBlendPremultiplied()
{
	glEnable(GL_BLEND);
	glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
}

GLint aglLoadShader(const char* vertex, const char* fragment)
{
	logcat("Compiling a shader ...");

	GLint program, v, f, len;
	ShaderAttachment *va, *fa;

	// Vertex shader
	v = glCreateShader(GL_VERTEX_SHADER);
	len = strlen(vertex);
	glShaderSource(v, 1, &vertex, &len);
	glCompileShader(v);
	logfmt("Vertex shader created with handle; %d", v);
#if LOG_ENABLED
	{
		GLint compiled = 0;
		glGetShaderiv(v, GL_COMPILE_STATUS, &compiled);
		if (compiled == GL_FALSE)
		{
			char *log;
			int loglen;

			glGetShaderiv(v, GL_INFO_LOG_LENGTH, &loglen);
			log = malloc(loglen);
			glGetShaderInfoLog(v, loglen, &loglen, log);

			logcat("Vertex shader compilation failed");
			logcat(log);

			free(log);
		}
		else
		{
			logcat("Vertex shader compilation succeeded");
		}
	}
#endif

	// Fragment shader
	f = glCreateShader(GL_FRAGMENT_SHADER);
	len = strlen(fragment);
	glShaderSource(f, 1, &fragment, &len);
	glCompileShader(f);
	logfmt("Fragment shader created with handle; %d", f);
#if LOG_ENABLED
	{
		GLint compiled = 0;
		glGetShaderiv(f, GL_COMPILE_STATUS, &compiled);
		if (compiled == GL_FALSE)
		{
			char *log;
			int loglen;

			glGetShaderiv(f, GL_INFO_LOG_LENGTH, &loglen);
			log = malloc(loglen);
			glGetShaderInfoLog(f, loglen, &loglen, log);

			logcat("Fragment shader compilation failed");
			logcat(log);

			free(log);
		}
		else
		{
			logcat("Fragment shader compilation succeeded");
		}
	}
#endif

	// Program
	program = glCreateProgram();
	glAttachShader(program, v);
	glAttachShader(program, f);
	glLinkProgram(program);
	logfmt("Shader program created with handle: %d", program);
#if LOG_ENABLED
	{
		GLint linked = 0;
		glGetProgramiv(program, GL_LINK_STATUS, &linked);
		if (linked == GL_FALSE)
		{
			char *log;
			int loglen;

			glGetProgramiv(program, GL_INFO_LOG_LENGTH, &loglen);
			log = malloc(loglen);
			glGetProgramInfoLog(program, loglen, &loglen, log);

			logcat("Program linkage failed");
			logcat(log);

			free(log);
		}
		else
		{
			logcat("Program linkage succeeded");
		}
	}
#endif

	// Remember attachment (for cleanup later)
	va = (ShaderAttachment*)malloc(sizeof(ShaderAttachment));
	fa = (ShaderAttachment*)malloc(sizeof(ShaderAttachment));

	va->shader = v;
	va->program = program;
	va->next = fa;

	fa->shader = f;
	fa->program = program;
	fa->next = _agl_shaders;

	_agl_shaders = va;

	logcat("Compilation completed.");
	return program;
}

void  aglUniform(GLint program, const char* param, GLfloat v)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniform1f(loc, v);
}

void  aglUniform2(GLint program, const char* param, GLfloat v1, GLfloat v2)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniform2f(loc, v1, v2);
}

void  aglUniform3(GLint program, const char* param, GLfloat v1, GLfloat v2, GLfloat v3)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniform3f(loc, v1, v2, v3);
}

void  aglUniform4(GLint program, const char* param, GLfloat v1, GLfloat v2, GLfloat v3, GLfloat v4)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniform4f(loc, v1, v2, v3, v4);
}

void  aglUniformMat2(GLint program, const char* param, GLfloat *m)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniformMatrix2fv(loc, 1, 0, m);
}

void  aglUniformMat3(GLint program, const char* param, GLfloat *m)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniformMatrix3fv(loc, 1, 0, m);
}

void  aglUniformMat4(GLint program, const char* param, GLfloat *m)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniformMatrix4fv(loc, 1, 0, m);
}

void  aglUniformTexture(GLint program, const char* param, GLint t)
{
	GLint loc = glGetUniformLocation(program, param);
	if (loc >= 0)
		glUniform1i(loc, t);
}

void  aglUseShader(GLint shader)
{
	logfmt("Binding shader %d", shader);

	glUseProgram(shader);

#if LOG_ENABLED
	if (glGetError())
		logcat("Binding failed.");
#endif

	logfmt("aglUseShader: Alpha: %f", _agl_alpha);

	aglUniformMat4(shader, "aglModelview", matrix_stack_data_ptr(_agl_modelview));
	aglUniformMat4(shader, "aglVirtualTransform", _agl_virtualTransform.data);
	aglUniformTexture(shader, "aglTexture", 0);	// For texture 0
	aglUniform(shader, "aglAlpha", _agl_alpha);

	_agl_bound_shader = shader;
}

void  aglUseQuadShader()
{
	aglUseShader(_agl_quad_program);
}

void  aglClearShader()
{
	_agl_bound_shader = 0;
	glUseProgram(0);
}

void  aglDeleteShader(GLint shader)
{
	ShaderAttachment *prev = 0, *curr = _agl_shaders;

	while (curr)
	{
		if (curr->program == shader)
		{
			glDetachShader(curr->program, curr->shader);
			glDeleteShader(curr->shader);

			if (prev)
			{
				prev->next = curr->next;
				free(curr);
				curr = prev->next;
			}
			else
			{
				_agl_shaders = curr->next;
				free(curr);
				curr = _agl_shaders;
			}
		}
		else
		{
			prev = curr;
			curr = curr->next;
		}
	}

	glDeleteShader(shader);
}

GLint aglCreateTexture(GLint w, GLint h)
{
	GLuint tex = aglCreateEmptyTexture();
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
	return tex;
}

GLint aglCreateEmptyTexture()
{
	GLuint tex;

	glGenTextures(1, &tex);
	glBindTexture(GL_TEXTURE_2D, tex);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	return tex;
}

void  aglBindTexture(GLint tex)
{
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, tex);
}

void  aglUnbindTexture()
{
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, 0);
}

void  aglDeleteTexture(GLint tex)
{
	GLuint t = tex;
	glDeleteTextures(1, &t);
}

void  aglTexturedQuad()
{
	GLint pos = glGetAttribLocation(_agl_bound_shader, "aglPosition");

	glBindBuffer(GL_ARRAY_BUFFER, _agl_quad_verts);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _agl_quad_elems);

	if (pos >= 0)
	{
		glVertexAttribPointer(pos, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(GLfloat), 0);
		glEnableVertexAttribArray(pos);
	}

	glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, 0);

	if (pos >= 0)
		glDisableVertexAttribArray(pos);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
}

void  aglDrawBitmap(GLint tex, GLfloat w, GLfloat h, GLfloat alpha)
{
	aglBindTexture(tex);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	aglTexturedQuad();
}

void  aglDrawBitmapTranslated(GLint tex, GLfloat x, GLfloat y, GLfloat w, GLfloat h, GLfloat alpha)
{
	aglLoadIdentity();
	aglTranslatef(x, y, 0.f);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	aglDrawBitmap(tex, w, h, alpha);
}

void  aglDrawBitmapTransformed(GLint tex, GLfloat w, GLfloat h, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	aglLoadIdentity();
	aglTranslatef(x, y, 0.f);
	aglRotatef(rot);
	aglScalef(xscale, yscale, 1.f);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	aglDrawBitmap(tex, w, h, alpha);
}

void  aglDrawBitmapMatrix(GLint tex, GLfloat w, GLfloat h, GLfloat *m, GLfloat alpha)
{
	aglLoadMatrix(m);
	aglScalef(w, h, 1.f);
	aglDrawBitmap(tex, w, h, alpha);
}

void  aglDrawBitmapWithShader(GLint tex, GLfloat w, GLfloat h, GLint shader, GLfloat alpha)
{
	aglBindTexture(tex);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	_agl_alpha = alpha;
	aglUseShader(shader);
	aglTexturedQuad();
	_agl_alpha = 1.f;
}

void  aglDrawBitmapWithShaderTranslated(GLint tex, GLfloat w, GLfloat h, GLint shader, GLfloat x, GLfloat y, GLfloat alpha)
{
	aglLoadIdentity();
	aglTranslatef(x, y, 0.f);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	_agl_alpha = alpha;
	aglUseShader(shader);
	aglDrawBitmap(tex, w, h, alpha);
	_agl_alpha = 1.f;
}

void  aglDrawBitmapWithShaderTransformed(GLint tex, GLfloat w, GLfloat h, GLint shader, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	aglLoadIdentity();
	aglTranslatef(x, y, 0.f);
	aglRotatef(rot);
	aglScalef(xscale, yscale, 1.f);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	glBindTexture(GL_TEXTURE_2D, tex);
	_agl_alpha = alpha;
	aglUseShader(shader);
	aglDrawBitmap(tex, w, h, alpha);
	_agl_alpha = 1.f;
}

void  aglDrawBitmapWithShaderMatrix(GLint tex, GLfloat w, GLfloat h, GLint shader, GLfloat *m, GLfloat alpha)
{
	aglLoadMatrix(m);
	aglTranslatef(-.5f * w, -.5f * h, 0.f);
	aglScalef(w, h, 1.f);
	_agl_alpha = alpha;
	aglUseShader(shader);
	aglDrawBitmap(tex, w, h, alpha);
	_agl_alpha = 1.f;
}

void  aglDrawBitmapWithoutShader(GLint tex, GLfloat w, GLfloat h, GLfloat alpha)
{
	_agl_alpha = alpha;
	aglUseShader(_agl_quad_program);
	_agl_alpha = 1.f;
	aglDrawBitmapWithShader(tex, w, h, _agl_quad_program, alpha);
}

void  aglDrawBitmapWithoutShaderTranslated(GLint tex, GLfloat w, GLfloat h, GLfloat x, GLfloat y, GLfloat alpha)
{
	aglDrawBitmapWithShaderTranslated(tex, w, h, _agl_quad_program, x, y, alpha);
}

void  aglDrawBitmapWithoutShaderTransformed(GLint tex, GLfloat w, GLfloat h, GLfloat x, GLfloat y, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	aglDrawBitmapWithShaderTransformed(tex, w, h, _agl_quad_program, x, y, rot, xscale, yscale, alpha);
}

void  aglDrawBitmapWithoutShaderMatrix(GLint tex, GLfloat w, GLfloat h, GLfloat *m, GLfloat alpha)
{
	aglDrawBitmapWithShaderMatrix(tex, w, h, _agl_quad_program, m, alpha);
}

void  aglEvalBezier(GLfloat *controlPoints, GLint numPoints, GLfloat t, GLfloat *outx, GLfloat *outy)
{
	// This is a variation of http://www.cgafaq.info/wiki/Bezier_curve_evaluation
	// I'm manipulating the original control points directly to avoid extraneous memory
	// (de)allocation activity. The caller needs to copy their original control point
	// buffer if they wish to retain it. aglInstanceBitmapBezier does this with only
	// one allocation, which is worth moving allocation out of this function.

	GLfloat oneMinusT = 1.f - t;
	GLint i = 0, j = 0, degree = numPoints - 1;

	for (i = 0; i < degree; ++i)
	{
		for (j = 0; j < degree - i; ++j)
		{
			controlPoints[2*j+0] = oneMinusT * controlPoints[2*j+0] + t * controlPoints[2*(j+1)+0];
			controlPoints[2*j+1] = oneMinusT * controlPoints[2*j+1] + t * controlPoints[2*(j+1)+1];
		}
	}

	*outx = controlPoints[0];
	*outy = controlPoints[1];
}

void  aglInstanceBitmapLinear(GLint tex, GLint w, GLint h, GLfloat x1, GLfloat y1, GLfloat x2, GLfloat y2, GLint numSteps, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	GLint i;
	GLfloat x = x1, y = y1, dx = (x2 - x1) / numSteps, dy = (y2 - y1) / numSteps;

	for (i = 0; i <= numSteps; ++i)
	{
		aglDrawBitmapWithoutShaderTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);
		x += dx;
		y += dy;
	}
}

void  aglInstanceBitmapBezier(GLint tex, GLint w, GLint h, GLfloat *controlPoints, GLint numPoints, GLint numSteps, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	GLfloat t = 0.f, dt = 1.f / numSteps, x = 0.f, y = 0.f;
	GLint i = 0, cplen = 2 * numPoints * sizeof(GLfloat);
	GLfloat *cp = (GLfloat*)malloc(cplen);

	logcat("Instancing bitmap using Bezier interpolation");
	logfmt("%d control points", numPoints);
	logfmt("%d steps", numSteps);

#if LOG_ENABLED
	for (i = 0; i < numPoints; ++i)
	{
		logfmt3("Control point %d: (%f, %f)", i, controlPoints[2*i], controlPoints[2*i+1]);
	}
#endif

	for (i = 0; i <= numSteps; ++i)
	{
		memcpy(cp, controlPoints, cplen);
		aglEvalBezier(cp, numPoints, 1.f - t, &x, &y);
		aglDrawBitmapWithoutShaderTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);
		t += dt;
		logfmt3("Step %d evaluated to (%f,%f)", i, x, y);
	}

	free(cp);
}

void  aglInstanceBitmapCatmull(GLint tex, GLint w, GLint h, GLfloat *controlPoints, GLint numSteps, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	GLint i;
	GLfloat t = 0.f, dt = 1.f / numSteps, x = 0.f, y = 0.f,
			P0x = controlPoints[0], P0y = controlPoints[1],
			P1x = controlPoints[2], P1y = controlPoints[3],
			P2x = controlPoints[4], P2y = controlPoints[5],
			P3x = controlPoints[6], P3y = controlPoints[7];

	for (i = 0; i <= numSteps; ++i)
	{
		x = .5f * (2 * P1x + (-P0x + P2x) * t + (2 * P0x - 5 * P1x + 4 * P2x - P3x) * t * t + (-P0x + 3 * P1x - 3 * P2x + P3x) * t * t * t);
		y = .5f * (2 * P1y + (-P0y + P2y) * t + (2 * P0y - 5 * P1y + 4 * P2y - P3y) * t * t + (-P0y + 3 * P1y - 3 * P2y + P3y) * t * t * t);

		aglDrawBitmapWithoutShaderTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);
		t += dt;
	}
}

void  aglDrawAlongBezierPath(GLint tex, GLint w, GLint h, GLfloat *controlPoints, GLint numPoints ,GLfloat t, GLfloat rot, GLfloat xscale, GLfloat yscale, GLfloat alpha)
{
	GLfloat x, y;
	GLint cplen = 2 * numPoints * sizeof(GLfloat);
	GLfloat *cp = (GLfloat*)malloc(cplen);
	memcpy(cp, controlPoints, cplen);

	aglEvalBezier(cp, numPoints, t, &x, &y);
	aglDrawBitmapWithoutShaderTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);

	free(cp);
}

void  aglClearColor(GLfloat r, GLfloat g, GLfloat b)
{
	glClearColor(r, g, b, 1.f);
}

void  aglBeginFrame()
{
	glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
}

void  aglEndFrame()
{
}

GLint aglCreateFBO(GLint w, GLint h)
{
	GLuint fbo, depth;
	FBODepthAttachment *da;

	// Depth buffer
	glGenRenderbuffers(1, &depth);
	glBindRenderbuffer(GL_RENDERBUFFER, depth);
	glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h);

	// Frame buffer
	glGenFramebuffers(1, &fbo);
	glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depth);

	// Remember association for cleanup
	da = (FBODepthAttachment*)malloc(sizeof(FBODepthAttachment));
	da->fbo = fbo;
	da->depth = depth;
	da->next = _agl_fbo_depth;
	_agl_fbo_depth = da;

	return fbo;
}

void  aglAttachToFBO(GLint fbo, GLint tex)
{
	FBOColorAttachment *ca;

	// Attach color buffer
	glBindTexture(GL_TEXTURE_2D, 0);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tex, 0);

	// Remember association for cleanup
	ca = (FBOColorAttachment*)malloc(sizeof(FBOColorAttachment));
	ca->fbo = fbo;
	ca->color = tex;
	ca->next = _agl_fbo_color;
	_agl_fbo_color = ca;
}

void  aglDeleteFBO(GLint fbo)
{
	GLuint uint;
	FBODepthAttachment *prevd = 0, *currd = _agl_fbo_depth;
	FBOColorAttachment *prevc = 0, *currc = _agl_fbo_color;

	while (currd)
	{
		if (currd->fbo == fbo)
		{
			uint = currd->depth;
			glDeleteRenderbuffers(1, &uint);

			if (prevd)
			{
				prevd->next = currd->next;
				free(currd);
				currd = prevd->next;
			}
			else
			{
				_agl_fbo_depth = currd->next;
				free(currd);
				currd = _agl_fbo_depth;
			}
		}
		else
		{
			prevd = currd;
			currd = currd->next;
		}
	}

	while (currc)
	{
		if (currc->fbo == fbo)
		{
			uint = currc->color;
			glDeleteTextures(1, &uint);

			if (prevc)
			{
				prevc->next = currc->next;
				free(currc);
				currc = prevc->next;
			}
			else
			{
				_agl_fbo_color = currc->next;
				free(currc);
				currc = _agl_fbo_color;
			}
		}
		else
		{
			prevc = currc;
			currc = currc->next;
		}
	}

	uint = fbo;
	glDeleteFramebuffers(1, &uint);
}

void  aglBeginOffscreenRender(GLint fbo)
{
	glBindFramebuffer(GL_FRAMEBUFFER, fbo);

	glClearColor(_agl_clear_r, _agl_clear_g, _agl_clear_b, _agl_clear_a);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}

void  aglEndOffscreenRender()
{
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
}

void  aglLoadIdentity()
{
	matrix_identity(matrix_stack_matrix_ptr(_agl_modelview));
}

void  aglLoadMatrix(GLfloat *m)
{
	memcpy(matrix_stack_data_ptr(_agl_modelview), m, 16 * sizeof(float));
}

void  aglGetMatrix(GLfloat *m)
{
	memcpy(m, matrix_stack_data_ptr(_agl_modelview), 16 * sizeof(float));
}

void  aglPushMatrix()
{
	matrix_stack_push(_agl_modelview);
}

void  aglPopMatrix()
{
	matrix_stack_pop(_agl_modelview);
}

void  aglTranslatef(GLfloat tx, GLfloat ty, GLfloat tz)
{
	matrix_translate(matrix_stack_matrix_ptr(_agl_modelview), tx, ty, tz);
}

void  aglRotatef(GLfloat angle)
{
	matrix_rotate2d(matrix_stack_matrix_ptr(_agl_modelview), angle);
}

void  aglAxisAngle(GLfloat x, GLfloat y, GLfloat z, GLfloat angle)
{
	matrix_rotate3d(matrix_stack_matrix_ptr(_agl_modelview), angle, x, y, z);
}

void  aglScalef(GLfloat sx, GLfloat sy, GLfloat sz)
{
	matrix_scale(matrix_stack_matrix_ptr(_agl_modelview), sx, sy, sz);
}





void Java_dkilian_andy_jni_agl_Initialize2D(JNIEnv *env, jobject *thiz, jint w, jint h)
{
	aglInitialize2D(w, h);
}

void Java_dkilian_andy_jni_agl_Cleanup2D(JNIEnv *env, jobject *thiz)
{
	aglCleanup2D();
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

void Java_dkilian_andy_jni_agl_BlendNone(JNIEnv *env, jobject *thiz)
{
	aglBlendNone();
}

void Java_dkilian_andy_jni_agl_BlendAdditive(JNIEnv *env, jobject *thiz)
{
	aglBlendAdditive();
}

void Java_dkilian_andy_jni_agl_BlendAlpha(JNIEnv *env, jobject *thiz)
{
	aglBlendAlpha();
}

void Java_dkilian_andy_jni_agl_BlendPremultiplied(JNIEnv *env, jobject *thiz)
{
	aglBlendPremultiplied();
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

void Java_dkilian_andy_jni_agl_Uniform(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v)
{
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniform(shader, p, v);
	(*env)->ReleaseStringUTFChars(env, param, p);
}

void Java_dkilian_andy_jni_agl_Uniform2(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2)
{
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniform2(shader, p, v1, v2);
	(*env)->ReleaseStringUTFChars(env, param, p);
}

void Java_dkilian_andy_jni_agl_Uniform3(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2, jfloat v3)
{
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniform3(shader, p, v1, v2, v3);
	(*env)->ReleaseStringUTFChars(env, param, p);
}

void Java_dkilian_andy_jni_agl_Uniform4(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloat v1, jfloat v2, jfloat v3, jfloat v4)
{
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniform4(shader, p, v1, v2, v3, v4);
	(*env)->ReleaseStringUTFChars(env, param, p);
}

void Java_dkilian_andy_jni_agl_UniformMat2(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray mat)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniformMat2(shader, p, m);
	(*env)->ReleaseStringUTFChars(env, param, p);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_UniformMat3(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray mat)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniformMat3(shader, p, m);
	(*env)->ReleaseStringUTFChars(env, param, p);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_UniformMat4(JNIEnv *env, jobject *thiz, jint shader, jstring param, jfloatArray mat)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniformMat4(shader, p, m);
	(*env)->ReleaseStringUTFChars(env, param, p);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_UniformTexture(JNIEnv *env, jobject *thiz, jint shader, jstring param, jint t)
{
	const char *p = (*env)->GetStringUTFChars(env, param, NULL);
	aglUniformTexture(shader, p, t);
	(*env)->ReleaseStringUTFChars(env, param, p);
}

void Java_dkilian_andy_jni_agl_UseShader(JNIEnv *env, jobject *thiz, jint shader)
{
	aglUseShader(shader);
}

void Java_dkilian_andy_jni_agl_UseQuadShader(JNIEnv *env, jobject *thiz)
{
	aglUseQuadShader();
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

void Java_dkilian_andy_jni_agl_DrawBitmap(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloat alpha)
{
	aglDrawBitmap(tex, w, h, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat x, jfloat y, jfloat w, jfloat h, jfloat alpha)
{
	aglDrawBitmapTranslated(tex, w, h, x, y, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	aglDrawBitmapTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapMatrix(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloatArray mat, jfloat alpha)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	aglDrawBitmapMatrix(tex, w, h, m, alpha);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShader(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jint shader, jfloat alpha)
{
	aglDrawBitmapWithShader(tex, w, h, shader, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jint shader, jfloat x, jfloat y, jfloat alpha)
{
	aglDrawBitmapWithShaderTranslated(tex, w, h, shader, x, y, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jint shader, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	aglDrawBitmapWithShaderTransformed(tex, w, h, shader, x, y, rot, xscale, yscale, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithShaderMatrix(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jint shader, jfloatArray mat, jfloat alpha)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	aglDrawBitmapWithShaderMatrix(tex, w, h, shader, m, alpha);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShader(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloat alpha)
{
	aglDrawBitmapWithoutShader(tex, w, h, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTranslated(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloat x, jfloat y, jfloat alpha)
{
	aglDrawBitmapWithoutShaderTranslated(tex, w, h, x, y, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderTransformed(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloat x, jfloat y, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	aglDrawBitmapWithoutShaderTransformed(tex, w, h, x, y, rot, xscale, yscale, alpha);
}

void Java_dkilian_andy_jni_agl_DrawBitmapWithoutShaderMatrix(JNIEnv *env, jobject *thiz, jint tex, jfloat w, jfloat h, jfloatArray mat, jfloat alpha)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	aglDrawBitmapWithoutShaderMatrix(tex, w, h, m, alpha);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_InstanceBitmapLinear(JNIEnv *env, jobject *thiz, jint tex, jint w, jint h, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jint numSteps, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	aglInstanceBitmapLinear(tex, w, h, x1, y1, x2, y2, numSteps, rot, xscale, yscale, alpha);
}

void Java_dkilian_andy_jni_agl_InstanceBitmapBezier(JNIEnv *env, jobject *thiz, jint tex, jint w, jint h, jfloatArray controlPoints, jint numPoints, jint numSteps, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	float *c = (*env)->GetFloatArrayElements(env, controlPoints, NULL);
	aglInstanceBitmapBezier(tex, w, h, c, numPoints, numSteps, rot, xscale, yscale, alpha);
	(*env)->ReleaseFloatArrayElements(env, controlPoints, c, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_InstanceBitmapCatmull(JNIEnv *env, jobject *thiz, jint tex, jint w, jint h, jfloatArray controlPoints, jint numSteps, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	float *c = (*env)->GetFloatArrayElements(env, controlPoints, NULL);
	aglInstanceBitmapCatmull(tex, w, h, c, numSteps, rot, xscale, yscale, alpha);
	(*env)->ReleaseFloatArrayElements(env, controlPoints, c, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_DrawAlongBezierPath(JNIEnv *env, jobject *thiz, jint tex, jint w, jint h, jfloatArray controlPoints, jint numPoints, jfloat t, jfloat rot, jfloat xscale, jfloat yscale, jfloat alpha)
{
	float *c = (*env)->GetFloatArrayElements(env, controlPoints, NULL);
	aglDrawAlongBezierPath(tex, w, h, c, numPoints, t, rot, xscale, yscale, alpha);
	(*env)->ReleaseFloatArrayElements(env, controlPoints, c, JNI_ABORT);
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

void Java_dkilian_andy_jni_agl_LoadIdentity(JNIEnv *env, jobject *thiz)
{
	aglLoadIdentity();
}

void Java_dkilian_andy_jni_agl_LoadMatrix(JNIEnv *env, jobject *thiz, jfloatArray mat)
{
	float *m = (*env)->GetFloatArrayElements(env, mat, NULL);
	aglLoadMatrix(m);
	(*env)->ReleaseFloatArrayElements(env, mat, m, JNI_ABORT);
}

void Java_dkilian_andy_jni_agl_GetMatrix(JNIEnv *env, jobject *thiz, jfloatArray mat)
{
	float m[16];
	aglGetMatrix(m);
	(*env)->SetFloatArrayRegion(env, mat, 0, 16, &(m[0]));
}

void Java_dkilian_andy_jni_agl_PushMatrix(JNIEnv *env, jobject *thiz)
{
	aglPushMatrix();
}

void Java_dkilian_andy_jni_agl_PopMatrix(JNIEnv *env, jobject *thiz)
{
	aglPopMatrix();
}

void Java_dkilian_andy_jni_agl_Translatef(JNIEnv *env, jobject *thiz, jfloat tx, jfloat ty, jfloat tz)
{
	aglTranslatef(tx, ty, tz);
}

void Java_dkilian_andy_jni_agl_Rotatef(JNIEnv *env, jobject *thiz, jfloat angle)
{
	aglRotatef(angle);
}

void Java_dkilian_andy_jni_agl_AxisAngle(JNIEnv *env, jobject *thiz, jfloat x, jfloat y, jfloat z, jfloat angle)
{
	aglAxisAngle(x, y, z, angle);
}

void Java_dkilian_andy_jni_agl_Scalef(JNIEnv *env, jobject *thiz, jfloat sx, jfloat sy, jfloat sz)
{
	aglScalef(sx, sy, sz);
}
