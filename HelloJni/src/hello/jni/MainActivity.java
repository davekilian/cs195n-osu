package hello.jni;

import dkilian.andy.KernelActivity;
import dkilian.andy.Screen;

public class MainActivity extends KernelActivity 
{
	@Override
	protected boolean enableOpenGL() 
	{
		return false;
	}

	@Override
	protected int getMinimumGLVersion() 
	{
		return 0;
	}

	@Override
	protected String getMissingGLError() 
	{
		return "lol wat";
	}

	@Override
	protected void initialize() 
	{
	}

	@Override
	protected void onKernelInitialized() 
	{		
		getKernel().getVirtualScreen().setWidth(640);
		getKernel().getVirtualScreen().setHeight(480);
		getKernel().getVirtualScreen().computeTransform(true);
		getKernel().setTargetDrawsPerSecond(60);
		getKernel().setTargetUpdatesPerSecond(60);
	}

	@Override
	protected Screen execFirst() 
	{
		return new MainScreen();
	}
}