package osu.main;

import osu.screen.SplashScreen;
import dkilian.andy.KernelActivity;
import dkilian.andy.Screen;

public class MainActivity extends KernelActivity
{
	@Override
	protected boolean enableOpenGL() 
	{
		return true;
	}

	@Override
	protected int getMinimumGLVersion() 
	{
		return 0x20000;
	}

	@Override
	protected String getMissingGLError() 
	{
		return "lol your device sucks";
	}

	@Override
	protected void initialize() 
	{
	}

	@Override
	protected void onKernelInitialized() 
	{		
		getKernel().getVirtualScreen().setWidth(512);
		getKernel().getVirtualScreen().setHeight(384);
		getKernel().getVirtualScreen().computeTransform(true);
		getKernel().setTargetDrawsPerSecond(60);
		getKernel().setTargetUpdatesPerSecond(60);
	}

	@Override
	protected Screen execFirst() 
	{
		return new SplashScreen();
	}
}