package dkilian.andy;

import java.util.Stack;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

/**
 * Manages the main event loop and the Screen state machine
 * @author dkilian
 */
public class Kernel
{
	/** The activity used to detect input and changes in configuration */
	private KernelActivity _activity;
	/** The view used to output graphics */
	private KernelView _view;
	/** The virtual screen used to abstract the device's resolution */
	private VirtualScreen _virtualScreen;
	/** The stack of active screens. The top item is the one currently executing */
	private Stack<Screen> _screenStack;
	/** The target number of times the executing Screen should have its update() called per second */
	float _targetUpdatesPerSec;
	/** The target number of times the executing Screen should have its draw() called per second */
	float _targetDrawsPerSec;
	/** Handles calls to the current Screen's update() method */
	Handler _updateHandler;
	/** Handles calls to the current Screen's draw() method */
	Handler _drawHandler;
	/** The last update time, in milliseconds */
	long _lastUpdate;
	/** The last draw time, in milliseconds */
	long _lastDraw;
	/** The time delta (in partial seconds) last passed to a Screen's update() method */
	float _updateDelta;
	/** The time delta (in partial seconds) last passed to a Screen's draw() method */
	float _drawDelta;
	/** True if the kernel is currently executing a screen; false otherwise */
	boolean _running;
	/** The current touch input information */
	private TouchInput _touch;
	/** The current accelerometer input information */
	private AccelerometerInput _accel;
	/** The device's vibrator */
	private Vibrator _vibrate;
	
	/**
	 * Creates a new Kernel object. Games inheriting KernelActivity will never need to call this.
	 * @param activity The activity creating this object.
	 */
	public Kernel(KernelActivity activity)
	{
		_activity = activity;
		_view = new KernelView(this, activity);
		_virtualScreen = new VirtualScreen();
		_screenStack = new Stack<Screen>();
		_targetUpdatesPerSec = _targetDrawsPerSec = 30;
		_lastUpdate = 0;
		_lastDraw = 0;
		_touch = new TouchInput(this);
		_accel = new AccelerometerInput();
		_vibrate = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);
		_updateHandler = new Handler(new KernelUpdateCallback(this));
		_drawHandler = new Handler(new KernelDrawCallback(this));
	}
	
	/** Gets the activity that started this kernel and collects input / configuration changes */
	public final KernelActivity getActivity()
	{
		return _activity;
	}
	
	/** Gets the main view, containing the Paint and Canvas objects to do custom rendering */
	public final KernelView getView()
	{
		return _view;
	}
	
	/** Gets the object that manages the transformation from virtual to physical screen space */
	public final VirtualScreen getVirtualScreen()
	{
		return _virtualScreen;
	}
	
	/** Prepares a screen for execution */
	private final void prepScreen(Screen s)
	{
		if (!s.isLoaded())
			s.load(this);
	}
	
	/** Cleans up a screen that will no longer be used */
	private final void finishScreen(Screen s)
	{
		if (s.isLoaded())
			s.unload(this);
	}
	
	/** 
	 * Pushes a Screen onto the stack. The currently executing Screen will not be unloaded,
	 * and will be executed again on the next popScreen() call. If the specified screen has
	 * not been loaded yet, it will be loaded during the call.
	 * @param s The screen to push onto the stack
	 */
	public final void pushScreen(Screen s)
	{
		prepScreen(s);
		_screenStack.push(s);
	}
	
	/**
	 * Pops a screen off the stack, unloading it first if necessary.
	 */
	public final void popScreen()
	{
		if (!_screenStack.empty())
			finishScreen(_screenStack.pop());
	}
	
	/**
	 * Swaps the current top of the stack with another Screen. The currently executing Screen
	 * will be unloaded if necessary. If the specified screen has not been loaded yet, it will
	 * be loaded during the call. 
	 * @param s The screen to swap with the top item of the stack
	 */
	public final void swapScreen(Screen s)
	{
		prepScreen(s);
		popScreen();
		pushScreen(s);
	}
	
	/**
	 * Begins executing a Screen. Every screen currently in the stack will be unloaded (if necessary)
	 * and then the stack will be cleared. If the specified screen has not been loaded yet, it will
	 * be loaded during the call
	 * @param s The screen to begin executing
	 */
	public final void setScreen(Screen s)
	{
		while (!_screenStack.isEmpty())
			finishScreen(_screenStack.pop());
		
		prepScreen(s);
		_screenStack.push(s);
	}
	
	/** Gets the currently executing screen (null if there is none) */
	public final Screen getScreen()
	{
		return _screenStack.isEmpty() ? null : _screenStack.peek();
	}
	
	/** Gets the maximum number of times the executing Screen's update() is called per second */
	public final float getTargetUpdatesPerSecond()
	{
		return _targetUpdatesPerSec;
	}

	/** Sets the maximum number of times the executing Screen's update() is called per second */
	public final void setTargetUpdatesPerSecond(float ups)
	{
		_targetUpdatesPerSec = ups;
	}

	/** Gets the maximum number of times the executing Screen's draw() is called per second */
	public final float getTargetDrawsPerSecond()
	{
		return _targetDrawsPerSec;
	}

	/** Sets the maximum number of times the executing Screen's draw() is called per second */
	public final void setTargetDrawsPerSecond(float dps)
	{
		_targetDrawsPerSec = dps;
	}
	
	/** Gets the amount of time in partial seconds since the executing Screen's update() was last called */
	public final float getUpdateTimeDelta()
	{
		return _updateDelta;
	}

	/** Gets the amount of time in partial seconds since the executing Screen's draw() was last called */
	public final float getDrawTimeDelta()
	{
		return _drawDelta;
	}
	
	/** Begins or resumes execution of the current Screen unless already doing so */
	public final void start()
	{
		if (!_running)
		{
			_running = true;
			_updateHandler.sendEmptyMessage(0);
			_drawHandler.sendEmptyMessage(0);
		}
	}
	
	/** Pauses Screen execution */
	public final void stop()
	{
		_running = false;
	}
	
	/** Gets information about the current user-generated touch input */
	public TouchInput getTouch()
	{
		return _touch;
	}
	
	/** Gets information about the current user-generated accelerometer input */
	public AccelerometerInput getAccelerometer()
	{
		return _accel;
	}
	
	/** Vibrates the device for the specified number of milliseconds */
	public void vibrate(long millis)
	{
		_vibrate.vibrate(millis);
	}
}
