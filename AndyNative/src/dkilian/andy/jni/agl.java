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
	public static native void Initialize2D(int w, int h);
	
	// TODO: everything else :)
	// TODO: and documentation
}
