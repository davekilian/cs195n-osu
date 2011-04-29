package dkilian.andy;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Entry point for an Andy game. The main Activity of your game must 
 * extend this class. For any Activity methods you override, you must
 * call the super implementation as well (this applied to Android
 * activities (Activity's?) in general anyways)
 * 
 * @author dkilian
 */
public abstract class KernelActivity extends Activity 
{
	/** The game's kernel */
	private Kernel _kernel;
	/** Whether or not to quit when the game is paused */
	private boolean _quitOnPause = true;
	
	/** Gets a value indicating whether this activity quits when paused by the system */
	public boolean getQuitOnPause()
	{
		return _quitOnPause;
	}

	/** Sets a value indicating whether this activity quits when paused by the system */
	public void setQuitOnPause(boolean quit)
	{
		_quitOnPause = quit;
	}
	
	/** Called before the Kernel is created. Allows the game to required initialization tasks */
	protected abstract void initialize();
	
	/** Called after the Kernel is created. Allows the game to required initialization tasks */
	protected abstract void onKernelInitialized();
	
	/** Gets the Screen to execute. Called after onKernelInitialized() */
	protected abstract Screen execFirst();
	
	/** The application's entry point */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    						 WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
		initialize();
		
		_kernel = new Kernel(this);
		setContentView(_kernel.getView());
		_kernel.getVirtualScreen().setPhysicalDimensions(_kernel.getView());
		_kernel.getView().getPaint().setFilterBitmap(true);
		
		onKernelInitialized();

		_kernel.getView().queueResize();
		_kernel.setScreen(execFirst());
		
		_kernel.start();
	}
	
	/** Called when a device configuration change occurs */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		_kernel.getView().queueResize();
	}
	
	/** Called by the OS during touch events */
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
    	if (e.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		_kernel.getTouch().onPress(e.getX(), e.getY());
    	}
    	else if (e.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		_kernel.getTouch().onMove(e.getX(), e.getY());
    	}
    	else if (e.getAction() == MotionEvent.ACTION_CANCEL || e.getAction() == MotionEvent.ACTION_UP)
    	{
    		_kernel.getTouch().onRelease();
    	}
    	
    	super.onTouchEvent(e);
    	return true;
    }
    
    /** Pause/resume currently isn't supported. To save users' battery life, the kernel exits whenever the application is stopped,
     *  as long as quit-on-pause functionality hasn't been disabled (see KernelActivity.[get|set]QuitOnPause) */
    @Override
    public void onPause()
    {
    	if (_quitOnPause)
    		System.exit(0);
    	else
    		super.onPause();
    }
	
	/** Gets a reference to the game's Kernel. This value is undefined before onKernelInitialized() has been called */
	public final Kernel getKernel()
	{
		return _kernel;
	}
}
