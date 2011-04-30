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
	/**
	 * Initializes Andy's native OpenGL renderer in 2D rendering mode
	 * @param w The width of the screen in virtual coordinates
	 * @param h The height of the screen in virtual coordinates
	 */
	public static native void Initialize2D(int w, int h);
	
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
	 * Compiles a shader program
	 * @param vertex The source code of the vertex shader
	 * @param fragment The source code of the fragment shader
	 * @return A handle to the shader, or zero if an error occurred
	 */
	public static native int  LoadShader(String vertex, String fragment);
	
	/**
	 * Sets the given shader as the active shader, which will be used in subsequent rendering operations
	 * @param shader A handle to the shader to make active
	 */
	public static native void UseShader(int shader);
	
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
	public static native int  CreateTexture(int w, int h);
	
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
	 */
	public static native void DrawBitmap(int tex);
	
	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied translation will be post-concatenated with the current ModelView matrix.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 */
	public static native void DrawBitmapTranslated(int tex, float x, float y);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be post-concatenated with the current ModelView matrix.
	 * The currently bound shader will be used, if applicable.
	 * @param tex The texture containing the bitmap data to draw
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 */
	public static native void DrawBitmapTransformed(int tex, float x, float y, float rot, float xscale, float yscale);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The current ModelView matrix will be used as the current transformation.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param shader The shader to use to render the sprite
	 */
	public static native void DrawBitmapWithShader(int tex, int shader);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied translation will be post-concatenated with the current ModelView matrix.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param shader The shader to use to render the sprite
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 */
	public static native void DrawBitmapWithShaderTranslated(int tex, int shader, float x, float y);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be post-concatenated with the current ModelView matrix.
	 * The given shader will be bound, in place of any previously bound shader (if applicable).
	 * @param tex The texture containing the bitmap data to draw
	 * @param shader The shader to use to render the sprite
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 */
	public static native void DrawBitmapWithShaderTransformed(int tex, int shader, float x, float y, float rot, float xscale, float yscale);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The current ModelView matrix will be used as the current transformation.
	 * Any currently bound shader will be unbound first.
	 * @param tex The texture containing the bitmap data to draw
	 */
	public static native void DrawBitmapWithoutShader(int tex);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied translation will be post-concatenated with the current ModelView matrix.
	 * Any currently bound shader will be unbound first.
	 * @param tex The texture containing the bitmap data to draw
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 */
	public static native void DrawBitmapWithoutShaderTranslated(int tex, float x, float y);

	/**
	 * Draws a bitmap sprite as a textured quad. 
	 * The supplied transformations will be post-concatenated with the current ModelView matrix.
	 * Any currently bound shader will be unbound first.
	 * @param tex The texture containing the bitmap data to draw
	 * @param x The amount to translate the sprite, in virtual coordinates
	 * @param y The amount to translate the sprite, in virtual coordinates
	 * @param rot The amount to rotate the sprite, in degrees
	 * @param xscale The horizontal scale factor
	 * @param yscale The vertical scale factor
	 */
	public static native void DrawBitmapWithoutShaderTransformed(int tex, float x, float y, float rot, float xscale, float yscale);
	
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
	public static native int  CreateFBO(int w, int h);
	
	/**
	 * Attaches a color texture to an FBO. See aglCreateTexture().
	 * @param fbo A handle to the framebuffer object to attach the color buffer to
	 * @param tex A handle to the color buffer to attach to the framebuffer object
	 */
	public static native void AttachToFBO(int fbo, int tex);
	
	/**
	 * Deletes an FBO and its attachments, invalidating them and releasing resources they are currently using
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
}
