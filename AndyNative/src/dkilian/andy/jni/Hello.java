package dkilian.andy.jni;

public class Hello 
{
	static
	{
		System.loadLibrary("AndyNative");
	}

	public static native String hello();
	public static native void helloGL();
}
