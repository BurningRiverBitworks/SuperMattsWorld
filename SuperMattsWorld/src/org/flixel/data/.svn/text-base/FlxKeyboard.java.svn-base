package org.flixel.data;

import java.util.Hashtable;

public class FlxKeyboard
{
	protected static final int JUST_PRESSED = 2;
	protected static final int PRESSED = 1;
	protected static final int NOT_PRESSED = 0;
	protected static final int JUST_RELEASED = -1;
	
	protected Hashtable<Integer, FlxKeyboardData> _map;
	
	public FlxKeyboard()
	{
		_map = new Hashtable<Integer, FlxKeyboardData>();
	}
	
	public void update()
	{
		for(FlxKeyboardData data : _map.values())
		{
			if((data.last == JUST_RELEASED) && (data.current == JUST_RELEASED)) data.current = NOT_PRESSED;
			else if((data.last == JUST_PRESSED) && (data.current == JUST_PRESSED)) data.current = PRESSED;
			data.last = data.current;
		}
		
	}
	
	public void reset()
	{
		for(FlxKeyboardData data : _map.values())
		{
			data.current = 0;
			data.last = 0;
		}
	}
	
	//@desc		Check to see if this key is pressed
	//@param	Key		One of the key constants listed above (e.g. LEFT or A)
	//@return	Whether the key is pressed
	public boolean pressed(int Key) 
	{ 
		if (_map.containsKey(Key))
			return _map.get(Key).current == PRESSED || _map.get(Key).current == JUST_PRESSED;
		
		return false;
	}
	
	//@desc		Check to see if this key was JUST pressed
	//@param	Key		One of the key constants listed above (e.g. LEFT or A)
	//@return	Whether the key was just pressed
	public boolean justPressed(int Key) 
	{ 
		if (_map.containsKey(Key))
			return _map.get(Key).current == JUST_PRESSED;
		
		return false;
	}
	
	//@desc		Check to see if this key is NOT pressed
	//@param	Key		One of the key constants listed above (e.g. LEFT or A)
	//@return	Whether the key is not pressed
	public boolean justReleased(int Key) 
	{ 
		if (_map.containsKey(Key))
			return _map.get(Key).current == JUST_RELEASED;
		
		return false; 
	}
	
	public void handleKeyDown(int Key)
	{
		if (!_map.containsKey(Key))
			_map.put(Key, new FlxKeyboardData());
		
		FlxKeyboardData o = _map.get(Key);
		if(o.current > 0) o.current = PRESSED;
		else o.current = JUST_PRESSED; 		
	}
	
	public void handleKeyUp(int Key)
	{
		if (!_map.containsKey(Key))
			_map.put(Key, new FlxKeyboardData());
		
		FlxKeyboardData o = _map.get(Key);
		if(o.current > 0) o.current = JUST_RELEASED;
		else o.current = NOT_PRESSED; 
	}
}
