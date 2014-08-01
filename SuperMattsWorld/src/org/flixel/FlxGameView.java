package org.flixel;

import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;

public class FlxGameView 
	extends SurfaceView 
	implements SurfaceHolder.Callback 
{
    class GameThread extends Thread 
    {
    	protected boolean running = true;
    	protected SurfaceHolder surfaceHolder = null; 
    	protected FlxGame game = null;
    	protected FlxGameView view = null;
    	
    	public GameThread(FlxGameView view, FlxGame game, SurfaceHolder surfaceHolder, Handler handler) 
    	{   		
    		this.surfaceHolder = surfaceHolder;  
    		this.game = game;
    		this.view = view;
    	}
    	
        public void setRunning(boolean b) 
        {
        	running = b;
        }
        
    	@Override
        public void run() 
    	{
	        while (running)
	        {
                Canvas c = null;
                try 
                {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) 
                    {
                        if (!game.onEnterFrame(c))
                        {
                        	running = false;
                        	game.shutdown(true);
                        	view.shutdown();
                        }
                    }
                } 
                finally 
                {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) 
                    {
                    	surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }         
        }
        
        public boolean doKeyDown(int keyCode, KeyEvent msg) 
        {
            synchronized (surfaceHolder) 
            {
            	game.onKeyDown(keyCode);
            }
            
            return true;
    	}
        
        public boolean doKeyUp(int keyCode, KeyEvent msg) 
        {
            synchronized (surfaceHolder) 
            {
            	game.onKeyUp(keyCode);
            }
            
            return true;
    	}
        
        public void onWindowFocusChanged(boolean hasWindowFocus) 
        {
        	synchronized (surfaceHolder) 
            {
        		if (hasWindowFocus)
        			game.onFocus();
        		else
        			game.onFocusLost();
            }
        }
        
        public void setSurfaceSize(int width, int height) 
        {
            synchronized (surfaceHolder) 
            {

            }
        }
    }
    
    /** The thread that actually draws the animation */
    protected GameThread thread = null;
    protected FlxGame game = null;
    protected Activity activity = null;
    protected boolean shutdown = false;
    
    public FlxGameView(FlxGame game, Context context, AttributeSet attrs) 
    {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        this.activity = (Activity)context;
        
        this.game = game; 
        
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }
    
    public GameThread getThread() 
    {
        return thread;
    }
    
    public void shutdown()
    {
       	
    	thread = null;
       	shutdown = true;
    	if (this.activity != null)
    		this.activity.finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) 
    {
    	if (thread != null)
    		return thread.doKeyDown(keyCode, msg);
    	
    	return false;
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) 
    {
    	if (thread != null)
    		return thread.doKeyUp(keyCode, msg);
    	
    	return false;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) 
    {
    	if (thread != null)
    	{
    		thread.onWindowFocusChanged(hasWindowFocus);
    	}
    }

    public void surfaceCreated(SurfaceHolder holder) 
    {     
    	if (thread == null && !shutdown)
    	{
	    	thread = new GameThread(this, game, holder, new Handler() 
	        {
	            @Override
	            public void handleMessage(Message m) 
	            {
	                
	            }
	        });
	        
	        thread.start();
    	}
    }

    public void surfaceDestroyed(SurfaceHolder holder) 
    {
    	if (thread != null)
    	{
	    	boolean retry = true;
	        thread.setRunning(false);
	        while (retry) 
	        {
	            try 
	            {
	                thread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) 
	            {
	            }
	        }
    	}
    	thread = null;
    }

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		if (thread != null)
			thread.setSurfaceSize(width, height);
	}
}
