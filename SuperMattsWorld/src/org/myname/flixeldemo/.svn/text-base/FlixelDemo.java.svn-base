package org.myname.flixeldemo;

import org.flixel.FlxGameView;
import org.flixel.FlxResourceManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import audio.SFXPool;

public class FlixelDemo extends Activity implements OnTouchListener
{
	private FlxGameView view;
	private Button left;
	private Button right;
	private Button space;
	private Button centerDpad;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        // turn off the window's title bar
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                            WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.main);
        
        view = (FlxGameView) findViewById(R.id.game);
        left = (Button) findViewById(R.id.left);
        left.setOnTouchListener(this);
        right = (Button)findViewById(R.id.right);
        right.setOnTouchListener(this);
        space = (Button)findViewById(R.id.space);
        space.setOnTouchListener(this);
        centerDpad = (Button)findViewById(R.id.centerdpad);
        centerDpad.setOnTouchListener(this);
    }       

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int keyCode = 0;
		if(v.getId() == R.id.left)
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		if(v.getId() == R.id.right)
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		if(v.getId() == R.id.space)
			keyCode = KeyEvent.KEYCODE_DPAD_UP;
		if(v.getId() == R.id.centerdpad)
			keyCode = KeyEvent.KEYCODE_DPAD_CENTER;
		
		if(keyCode == 0)
			return false;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			view.onKeyDown(keyCode, null);
		if(event.getAction() == MotionEvent.ACTION_UP)
			view.onKeyUp(keyCode, null);
		
		return false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		SFXPool.release();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SFXPool.release();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		if(!SFXPool.isInit())
			SFXPool.init(FlxResourceManager.context);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		SFXPool.release();
		super.onPause();
	}
}