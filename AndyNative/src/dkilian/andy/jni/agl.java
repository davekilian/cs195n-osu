package dkilian.andy.jni;

/**
 * Java interface for AGL, Andy's native OpenGL library.
 * 
 * These native calls batch several OpenGL using a single JNI call,
 * which is much less expensive than calling each individual OpenGL
 * function in a separate JNI call. 
 * 
 * @author dkilian
 */
public class agl 
{	
	static
	{
		System.loadLibrary("AndyNative");
	}
	
	/**
	 * Initializes Andy's native OpenGL renderer in 2D rendering mode
	 * @param w The width of the screen in virtual coordinates
	 * @param h The height of the screen in virtual coordinates
	 */
	public static native void Initialize2D(int w, int h);
	
	/**
	 * Releases internal state resources allocated in aglInitialize2D
	 * and destroys all active shaders, and framebuffers (including their
	 * attached depth buffers and color buffer textures) that were created 
	 * using agl's APIs. Textures not currently bound to an FBO are not
	 * affected by this call.
	 */
	public static native void Cleanup2D();
	
	/**
	 * Sets the dimensions of the screen in virtual coordinates
	 * @param w The width of the screen in virtual coordinates
	 * @param h The height of the screen in virtual coordinates
	 */
	public static native void SetVirtualDimensions(int w, int h);
	
	/**
	 * Gets the transformation from virtual coordinates to screen coordinates
	 * @param m A 16-element array to receive the 4x4 transformation matrix
	 */
	public static native void GetVirtualTransform(float[] m);
	
	/**
	 * Sets the transformation from virtual coordinates to screen coordinates
	 * @param m A 16-element array containing the 4x4 transformation matrix
	 */
	public static native void SetVirtualTransform(float[] m);
	
	/**
	 * Compute the transformation from virtual space to screen space using the virtual
	 * coordinates specified in a previous aglInitialize2D() or aglSetVirtualDimensions() call
	 */
	public static native void ComputeVirtualTransform();
	
	/**
	 * Disables blending. Any pixels drawn by one sprite will overwrite all other pixels in
	 * the framebuffer, regardless of alpha values.
	 * 
	 * out = src
	 */
	public static native void BlendNone();
	
	/**
	 * Enables blending in additive mode. Any pixels drawn by one sprite will be added to
	 * all other pixels in the framebuffer, regardless of alpha values.
	 * 
	 * out = src + dst
	 */
	public static native void BlendAdditive();
	
	/**
	 * Enables alpha blending. The alpha values in pixels drawn will be used to determine 
	 * the opacity of each pixel, used when mixing the pixels with those in the framebuffer
	 * 
	 * out = (src.alpha) * src + (1 - src.alpha) * dst
	 */
	public static native void BlendAlpha();
	
	/**
	 * Enables premultiplied alpha blending. The alpha values in pixels are used only to determine
	 * how much of the framebuffer pixel is blended into the final result; the alpha value is
	 * premultiplied into the original pixel color (thus the original value is used).
	 * 
	 * out = src + (1 - src.alpha) * dst
	 */
	public static native void BlendPremultiplied();
	
	/**
	 * Sets the current clip rectangle. Any pixels outside of the clip rectangle will be left
	 * unmodified by drawing operations that would normally modify them.
	 * @param x The X coordinate of the clip rectangle's top-left corner, in virtual space
	 * @param y The Y coordinate of the clip rectangle's top-left corner, in virtual space
	 * @param w The width of the clip rectangle in virtual coordinates
	 * @param h The height of the clip rectangle in virtual coordinates
	 */
	public static native void Clip(int x, int y, int w, int h);
	
	/**
	 * Compiles a shader program
	 * @param vertex The source code of the vertex shader
	 * @param fragment The source code of the fragment shader
	 * @return A handle to the shader, or zero if an error occurred
	 */
	public static native int LoadShader(String vertex, String fragment);

	/**
	 * Sets a 1D uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param v The value to assign to the shader uniform
	 */
	public static native void Uniform(int shader, String param, float v);

	/**
	 * Sets a 2D uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param v1 The value to assign to the shader uniform's X component
	 * @param v2 The value to assign to the shader uniform's Y component
	 */
	public static native void Uniform2(int shader, String param, float v1, float v2);

	/**
	 * Sets a 3D uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param v1 The value to assign to the shader uniform's X component
	 * @param v2 The value to assign to the shader uniform's Y component
	 * @param v3 The value to assign to the shader uniform's Z component
	 */
	public static native void Uniform3(int shader, String param, float v1, float v2, float v3);

	/**
	 * Sets a 4D uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param v1 The value to assign to the shader uniform's X component
	 * @param v2 The value to assign to the shader uniform's Y component
	 * @param v3 The value to assign to the shader uniform's Z component
	 * @param v4 The value to assign to the shader uniform's W component
	 */
	public static native void Uniform4(int shader, String param, float v1, float v2, float v3, float v4);
	
	/**
	 * Sets a 2x2 uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param m The contents of the 2x2 matrix to assign to the shader uniform
	 */
	public static native void UniformMat2(int shader, String param, float[] m);

	/**
	 * Sets a 3x3 uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param m The contents of the 3x3 matrix to assign to the shader uniform
	 */
	public static native void UniformMat3(int shader, String param, float[] m);

	/**
	 * Sets a 4x4 uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param m The contents of the 4x4 matrix to assign to the shader uniform
	 */
	public static native void UniformMat4(int shader, String param, float[] m);
	
	/**
	 * Sets a texture uniform value
	 * @param shader A handle to the shader to modify
	 * @param param The name of the shader uniform to modify
	 * @param texture The texture index to bind to the uniform. Usually this is 0, for GL_TEXTURE0.
	 */
	public static native void UniformTexture(int shader, String param, int texture);
	
	/**
	 * Sets the given shader as the active shader, which will be used in subsequent rendering operations
	 * @param shader A handle to the shader to make active
	 */
	public static native void UseShader(int shader);
	
	/**
	 * Sets the built-in textured-quad rendering shader as the active shader, which will be used in
	 * subsequent rendering operations. Useful if you plan on calling aglTexturedQuad(). This is 
	 * handled automatically in all variants of aglDrawBitmapWithoutShader(). 
	 */
	public static native void UseQuadShader();
	
	/**
	 * Clears the current shader, marking no shader as active
	 */
	public static native void ClearShader();
	
	/**
	 * Deletes a shader, invalidating it and freeing resources it uses
	 * @param shader A handle to the shader to delete
	 */
	public static native void DeleteShader(int shader);
	
	/**
	 * Creates a texture. This texture will have color data, but the data inside the texture is undefined.
	 * The return value from this function may safely be passed to aglAttachToFBO(). The texture will remain
	 * bound when this function returns.
	 * @param w The width of the texture, in pixels
	 * @param h The height of the texture, in pixels
	 * @return A handle to the texture
	 */
	public static native int CreateTexture(int w, int h);
	
	/**
	 * Creates a texture with no attached data. Use GLUtils.texImage2D() to fill the texture with color data.
	 * @return A handle to the texture created
	 */
	public static native int  CreateEmptyTexture();
	
	/**
	 * Binds the given texture, setting it as the currently active texture. As a side effect, this method
	 * unbinds any other texture that is already bound.
	 * @param tex A handle to the texture to bind
	 */
	public static native void BindTexture(int tex);
	
	/**
	 * Unbinds any texture that is currently bound.
	 */
	public static native void UnbindTexture();
	
	/**
	 * Deletes a texture, invalidating its handle and freeing all resources it is using
	 * @param tex A handle to the texture to free
	 */
	public static native void DeleteTexture(int tex);
	
	/**
	 * Draws a textured quad, from (0, 0, 0, 1) to (1, 1, 0, 1), using the current ModelView matrix.
	 */
	public static native void TexturedQuad();
	
	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The current ModelView matrix will be used as the current transformation.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmap(int tex, float w, float h, float alpha);
	
	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied absolute translation will be used, overwriting the current Modelview matrix.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapTranslated(int tex, float w, float h, float x, float y, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapTransformed(int tex, float w, float h, float x, float y, float rot, float xscale, float yscale, float alpha);

	/** 
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param mat The transformation matrix to apply to the quad before drawing it
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapMatrix(int tex, float w, float h, float[] mat, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The current ModelView matrix will be used as the current transformation.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param shader The shader to use to render the sprite
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithShader(int tex, float w, float h, int shader, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied absolute translation will be used, overwriting the current Modelview matrix.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param shader The shader to use to render the sprite
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithShaderTranslated(int tex, float w, float h, int shader, float x, float y, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param shader The shader to use to render the sprite
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithShaderTransformed(int tex, float w, float h, int shader, float x, float y, float rot, float xscale, float yscale, float alpha);

	/** 
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param shader The shader to use to render the sprite
	 * @param mat The transformation matrix to apply to the quad before drawing it
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithShaderMatrix(int tex, float w, float h, int shader, float[] mat, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The current ModelView matrix will be used as the current transformation.
	 * Any currently bound shader will be unbound first, and AGL's built-in no-frills quad shader will be used to render the sprite.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithoutShader(int tex, float w, float h, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied absolute translation will be used, overwriting the current Modelview matrix.
	 * Any currently bound shader will be unbound first, and AGL's built-in no-frills quad shader will be used to render the sprite.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithoutShaderTranslated(int tex, float w, float h, float x, float y, float alpha);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * Any currently bound shader will be unbound first, and AGL's built-in no-frills quad shader will be used to render the sprite.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithoutShaderTransformed(int tex, float w, float h, float x, float y, float rot, float xscale, float yscale, float alpha);

	/** 
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be used, overwriting the current Modelview matrix.
	 * Any currently bound shader will be unbound first, and AGL's built-in no-frills quad shader will be used to render the sprite.
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param mat The transformation matrix to apply to the quad before drawing it
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawBitmapWithoutShaderMatrix(int tex, float w, float h, float[] mat, float alpha);

	/**
	 * Draws several copies of a sprite by interpolating along a linear path
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param x1 The world-space X coordinate of the original sprite
	 * @param y1 The world-space Y coordinate of the original sprite
	 * @param x2 The world-space X coordinate of the final sprite
	 * @param y2 The world-space Y coordinate of the final sprite
	 * @param numSteps The number of sprites to draw along the path
	 * @param rot The rotation to apply to each sprite, in degrees
	 * @param xscale The factor by which to horizontally scale each sprite
	 * @param yscale The factor by which to vertically scale each sprite
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void InstanceBitmapLinear(int tex, int w, int h, float x1, float y1, float x2, float y2, int numSteps, float rot, float xscale, float yscale, float alpha);

	/**
	 * Draws several copies of a sprite by interpolating along a Bezier path
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param controlPoints A set of at least two Bezier control points, packed in the format [x1 y1 x2 y2 ... xn yn]
	 * @param numPoints The number of control point locations contained in the controlPoints array.
	 * @param numSteps The number of sprites to draw along the path
	 * @param tmin The first point on the bezier curve to instance along
	 * @param tmax The last point on the bezier curve to instance along
	 * @param rot The rotation to apply to each sprite, in degrees
	 * @param xscale The factor by which to horizontally scale each sprite
	 * @param yscale The factor by which to vertically scale each sprite
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void InstanceBitmapBezier(int tex, int w, int h, float[] controlPoints, int numPoints, int numSteps, float tmin, float tmax, float rot, float xscale, float yscale, float alpha);

	/**
	 * Draws several copies of a sprite by interpolating along a Catmull-Rom path
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param controlPoints A set of four Catmull-Rom control points, packed in the format [x1 y1 x2 y2 x3 y3 x4 y4]
	 * @param numSteps The number of sprites to draw along the path
	 * @param rot The rotation to apply to each sprite, in degrees
	 * @param xscale The factor by which to horizontally scale each sprite
	 * @param yscale The factor by which to vertically scale each sprite
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void InstanceBitmapCatmull(int tex, int w, int h, float[] controlPoints, int numSteps, float rot, float xscale, float yscale, float alpha);

	/**
	 * Draws a sprite centered at the evaluation of a certain point along a Bezier path
	 * @param tex The texture containing the bitmap data to draw
	 * @param w The width of the sprite in virtual coordinates
	 * @param h The height of the sprite in virtual coordinates
	 * @param controlPoints A set of at least two Bezier control points, packed in the format [x1 y1 x2 y2 ... xn yn]
	 * @param numPoints The number of control point locations contained in the controlPoints array.
	 * @param t The point along the path to evaluate, where 0 is the first point and 1 is the last point
	 * @param rot The rotation to apply to each sprite, in degrees
	 * @param xscale The factor by which to horizontally scale each sprite
	 * @param yscale The factor by which to vertically scale each sprite
	 * @param alpha The alpha value to multiply with each pixel in the entire sprite, between 0 and 1
	 */
	public static native void DrawAlongBezierPath(int tex, int w, int h, float[] controlPoints, int numPoints, float t, float rot, float xscale, float yscale, float alpha);
	
	/**
	 * Sets the color the backbuffer is cleared to at the beginning of every frame
	 * @param r The red component of the backbuffer clear color, in [0, 1]
	 * @param g The green component of the backbuffer clear color, in [0, 1]
	 * @param b The blue component of the backbuffer clear color, in [0, 1]
	 */
	public static native void ClearColor(float r, float g, float b);
	
	/**
	 * Does all initialization needed to draw a single frame. Should be called at the beginning of each draw().
	 */
	public static native void BeginFrame();
	
	/**
	 * Does all cleanup neededafter drawing a single frame. Should be called at the end of each draw()
	 */
	public static native void EndFrame();
	
	/**
	 * Creates a new frame buffer object
	 * @param w The width of the FBO, in pixels
	 * @param h The height of the FBO, in pixels
	 * @return A handle to the FBO created
	 */
	public static native int CreateFBO(int w, int h);
	
	/**
	 * Attaches a color texture to an FBO. See aglCreateTexture().
	 * @param fbo A handle to the framebuffer object to attach the color buffer to
	 * @param tex A handle to the color buffer to attach to the framebuffer object
	 */
	public static native void AttachToFBO(int fbo, int tex);
	
	/**
	 * Deletes an FBO and its attachments, invalidating them and releasing resources they are currently using.
	 * This includes all color textures attached to the FBO. Detach them first (manually) if you want to keep
	 * the texture after the FBO is destroyed.
	 * @param fbo A handle to the FBO to delete
	 */
	public static native void DeleteFBO(int fbo);
	
	/**
	 * Begins redirecting render calls to a framebuffer object
	 * @param fbo A handle to the framebuffer object to render to
	 */
	public static native void BeginOffscreenRender(int fbo);
	
	/**
	 * Stops redirecting render calls to a framebuffer object and makes it possible for the framebuffer object's
	 * color buffer(s) to be read back / rendered like any other texture.
	 */
	public static native void EndOffscreenRender();
	
	/**
	 * Sets the top value on the modelview matrix stack to the identity matrix
	 */
	public static native void LoadIdentity();
	
	/**
	 * Sets the top value on the modelview matrix stack
	 * @param m A 16-value array containing the values to place in the matrix
	 */
	public static native void LoadMatrix(float[] m);
	
	/**
	 * Gets the top value of the maodelview matrix stack
	 * @param m A 16-value array that will receive the values from the matrix
	 */
	public static native void GetMatrix(float[] m);
	
	/**
	 * Pushes a new matrix onto the modelview matrix stack. The new top matrix will be a
	 * component-wise copy of the matrix that was previously the top of the stack.
	 */
	public static native void PushMatrix();
	
	/**
	 * Pops a matrix off the modelview matrix stack. If the stack only has one object,
	 * this call is a no-op
	 */
	public static native void PopMatrix();
	
	/**
	 * Multiplies the top of the modelview matrix stack by a translation matrix
	 * @param tx The X component of the translation, in virtual coordinates
	 * @param ty The Y component of the translation, in virtual coordinates
	 * @param tz The Z component of the translation, in virtual coordinates
	 */
	public static native void Translatef(float tx, float ty, float tz);
	
	/**
	 * Multiplies the top of the modelview matrix stack by a rotation (around the Z axis) matrix
	 * @param angle The rotation angle, in degrees 
	 */
	public static native void Rotatef(float angle);
	
	/**
	 * Multiplies the top of the modelview matrix stack by a rotation (around an arbitrary axis) matrix
	 * @param x The X component of the rotation axis
	 * @param y The Y component of the rotation axis
	 * @param z The Z component of the rotation axis
	 * @param angle The rotation angle, in degrees
	 */
	public static native void AxisAngle(float x, float y, float z, float angle);
	
	/**
	 * Multiplies the top of the modelview matrix stack by a scale matrix
	 * @param sx The scaling factor for the X axis
	 * @param sy The scaling factor for the Y axis
	 * @param sz The scaling factor for the Z axis
	 */
	public static native void Scalef(float sx, float sy, float sz);
}
